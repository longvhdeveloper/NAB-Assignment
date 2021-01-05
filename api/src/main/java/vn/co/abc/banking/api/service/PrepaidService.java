package vn.co.abc.banking.api.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.co.abc.banking.api.exception.ExecutePrepaidException;
import vn.co.abc.banking.proto.*;

@Service
@Slf4j
public class PrepaidService {

    private final EurekaClient client;

    @Autowired
    public PrepaidService(EurekaClient client) {
        this.client = client;
    }

    public void processPrepaid(PaymentRequest request) throws ExecutePrepaidException {
        log.info("payment request: {}", request);

        PaymentInfo paymentInfo = executePrepaid(request);
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
}
