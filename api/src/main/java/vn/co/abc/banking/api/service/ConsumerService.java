package vn.co.abc.banking.api.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import vn.co.abc.banking.api.message.DeletePassCodeMessage;
import vn.co.abc.banking.api.message.SMSVoucherMessage;
import vn.co.abc.banking.api.message.VoucherMessage;
import vn.co.abc.banking.proto.*;

import java.util.Objects;

@Service
@Slf4j
public class ConsumerService {

    private final EurekaClient client;
    private final ProducerService producerService;
    private final RetryTemplate retryTemplate;

    @Value("${spring.kafka.topic.sendSMS}")
    private String sendSMSTopic;

    @Autowired
    public ConsumerService(EurekaClient client, ProducerService producerService, RetryTemplate retryTemplate) {
        this.client = client;
        this.producerService = producerService;
        this.retryTemplate = retryTemplate;
    }

    @KafkaListener(topics = "${spring.kafka.topic.sendVoucherDLT}", containerFactory = "voucherKafkaListenerContainerFactory")
    public void listen(VoucherMessage message) throws Throwable {
        log.info("Received voucher message: {}", message);

        final int maxAttempts = 5;
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(maxAttempts));
        VoucherInfo voucher = retryTemplate.execute((RetryCallback<VoucherInfo, Throwable>) retryContext -> getVoucher(message),
                retryContext -> null);
        if (Objects.isNull(voucher)) {
            log.info("Can not get voucher with phone number {}, transaction {}", message.getPhoneNumber(), message.getTransactionId());
            return;
        }
        log.info("Voucher consumer: {}", voucher);
        sendObjectSMSViaKafka(message, voucher);
    }

    @KafkaListener(topics = "${spring.kafka.topic.deletePasscode}", containerFactory = "voucherKafkaListenerContainerFactory")
    public void listen(DeletePassCodeMessage message) throws Throwable {
        log.info("Received voucher message: {}", message);
        //
    }

    private VoucherInfo getVoucher(VoucherMessage voucherMessage) {
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("voucher-service", false);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext()
                .build();

        VoucherControllerGrpc.VoucherControllerBlockingStub stub = VoucherControllerGrpc.newBlockingStub(channel);

        GetVoucherResponse response = stub.getVoucher(GetVoucherRequest.newBuilder()
                .setPhoneNumber(voucherMessage.getPhoneNumber())
                .setTransactionId(voucherMessage.getTransactionId())
                .build());

        if (response.getStatus() != StatusCode.OK_VALUE) {
            log.error("Can not get voucher code with transaction {} and phone number {}",
                    voucherMessage.getTransactionId(), voucherMessage.getPhoneNumber());
            throw new RuntimeException("Can not get voucher code");
        }

        return response.getVoucherInfo();
    }

    private void sendObjectSMSViaKafka(VoucherMessage voucherMessage, VoucherInfo voucherInfo) {
        producerService.sendMessage(sendSMSTopic, new SMSVoucherMessage(voucherMessage.getPhoneNumber(),
                voucherInfo.getCode()));
    }
}
