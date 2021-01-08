package vn.co.abc.banking.api.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import vn.co.abc.banking.api.exception.ExecutePrepaidException;
import vn.co.abc.banking.api.message.ResendVoucherMessage;
import vn.co.abc.banking.proto.*;

@Service
@Slf4j
public class PrepaidService {

    private final EurekaClient client;
    private final ProducerService producerService;

    @Value("${spring.kafka.topic.sendVoucherDLT}")
    private String voucherTopicDLT;


    private final RetryTemplate retryTemplate;

    @Autowired
    public PrepaidService(EurekaClient client, ProducerService producerService, RetryTemplate retryTemplate) {
        this.client = client;
        this.producerService = producerService;
        this.retryTemplate = retryTemplate;
    }

    public VoucherInfo processPrepaid(PaymentRequest request) throws Throwable {
        log.info("payment request: {}", request);
        PaymentInfo paymentInfo = executePrepaid(request);

        VoucherInfo voucher = retryTemplate.execute((RetryCallback<VoucherInfo, Throwable>) retryContext -> {
            VoucherInfo voucherInfo = getVoucher(paymentInfo);
            return voucherInfo;
        }, retryContext -> {
            sendObjectGetVoucherViaKafka(paymentInfo);
            return null;
        });

        log.info("Voucher: {}", voucher);
        return voucher;
    }

    private PaymentInfo executePrepaid(PaymentRequest request) throws ExecutePrepaidException {
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("payment-service", false);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext()
                .build();

        PaymentControllerGrpc.PaymentControllerBlockingStub stub = PaymentControllerGrpc.newBlockingStub(channel);

        PaymentResponse response = stub.processPrepaid(request);

        channel.shutdown();

        if (response.getStatus() != StatusCode.OK_VALUE) {
            log.error("Can not execute payment with phone number {} and amount {}",
                    request.getPhoneNumber(), request.getAmount());
            throw new ExecutePrepaidException("Can not execute payment. Please try again later");
        }

        return response.getPaymentInfo();
    }

    private VoucherInfo getVoucher(PaymentInfo paymentInfo) {
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("voucher-service", false);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext()
                .build();

        VoucherControllerGrpc.VoucherControllerBlockingStub stub = VoucherControllerGrpc.newBlockingStub(channel);

        GetVoucherResponse response = stub.getVoucher(GetVoucherRequest.newBuilder()
                .setPhoneNumber(paymentInfo.getPhoneNumber())
                .setTransactionId(paymentInfo.getTransactionId())
                .build());

        if (response.getStatus() != StatusCode.OK_VALUE) {
            log.error("Can not get voucher code with transaction {} and phone number {}",
                    paymentInfo.getTransactionId(), paymentInfo.getPhoneNumber());
            throw new RuntimeException("Can not get voucher code");
        }

        return response.getVoucherInfo();
    }

    private void sendObjectGetVoucherViaKafka(PaymentInfo paymentInfo) {
        producerService.sendMessage(voucherTopicDLT, new ResendVoucherMessage(paymentInfo.getPhoneNumber(),
                paymentInfo.getTransactionId()));
    }
}
