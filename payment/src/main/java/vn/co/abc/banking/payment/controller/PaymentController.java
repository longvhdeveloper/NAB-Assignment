package vn.co.abc.banking.payment.controller;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.co.abc.banking.payment.entity.Payment;
import vn.co.abc.banking.payment.service.PaymentService;
import vn.co.abc.banking.payment.validator.GetPaymentInfoRequestValidator;
import vn.co.abc.banking.payment.validator.PaymentRequestValidator;
import vn.co.abc.banking.proto.*;

import java.util.Optional;


@GRpcService
@RequestMapping("/payment")
@Slf4j
public class PaymentController extends PaymentControllerGrpc.PaymentControllerImplBase {

    private final PaymentRequestValidator paymentRequestValidator;
    private final GetPaymentInfoRequestValidator getPaymentInfoRequestValidator;
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentRequestValidator paymentRequestValidator,
                             GetPaymentInfoRequestValidator getPaymentInfoRequestValidator,
                             PaymentService paymentService) {
        this.paymentRequestValidator = paymentRequestValidator;
        this.getPaymentInfoRequestValidator = getPaymentInfoRequestValidator;
        this.paymentService = paymentService;
    }

    @Override
    public void processPrepaid(PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        log.info("request received {}", request);

        if (!paymentRequestValidator.valid(request)) {
            PaymentResponse response = PaymentResponse.newBuilder()
                    .setStatus(StatusCode.INVALID_ARGUMENT_VALUE)
                    .build();
            log.info("server responded failed validation {}", response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Payment payment = paymentService.processPrepaid(request);

        PaymentResponse response = PaymentResponse.newBuilder()
                .setStatus(StatusCode.OK_VALUE)
                .setPaymentInfo(PaymentInfo.newBuilder()
                        .setTransactionId(payment.getId().toString())
                        .build())
                .build();
        log.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPaymentInfo(GetPaymentInfoRequest request, StreamObserver<GetPaymentInfoResponse> responseObserver) {
        log.info("request received {}", request);

        if (!getPaymentInfoRequestValidator.valid(request)) {
            GetPaymentInfoResponse response = GetPaymentInfoResponse.newBuilder()
                    .build();
            log.info("server responded failed validation {}", response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Optional<Payment> optional = paymentService.getPaymentInfo(request);
        if (optional.isEmpty()) {
            GetPaymentInfoResponse response = GetPaymentInfoResponse.newBuilder()
                    .build();
            log.info("server responded not found data {}", response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Payment payment = optional.get();
        GetPaymentInfoResponse response = GetPaymentInfoResponse.newBuilder()
                .setPaymentInfo(PaymentInfo.newBuilder()
                        .setTransactionId(payment.getId().toString())
                        .build())
                .build();

        log.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
