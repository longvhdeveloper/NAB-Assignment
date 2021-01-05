package vn.co.abc.banking.payment.controller;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.co.abc.banking.payment.service.PrepaidService;
import vn.co.abc.banking.payment.validator.PrepaidRequestValidator;
import vn.co.abc.banking.proto.PaymentControllerGrpc;
import vn.co.abc.banking.proto.PrepaidRequest;
import vn.co.abc.banking.proto.Response;
import vn.co.abc.banking.proto.StatusCode;


@GRpcService
@RequestMapping("/payment")
@Slf4j
public class PaymentController extends PaymentControllerGrpc.PaymentControllerImplBase {

    private final PrepaidRequestValidator prepaidRequestValidator;
    private final PrepaidService prepaidService;

    @Autowired
    public PaymentController(PrepaidRequestValidator prepaidRequestValidator, PrepaidService prepaidService) {
        this.prepaidRequestValidator = prepaidRequestValidator;
        this.prepaidService = prepaidService;
    }

    @Override
    public void processPrepaid(PrepaidRequest request, StreamObserver<Response> responseObserver) {
        log.info("request received {}", request);

        if (!prepaidRequestValidator.valid(request)) {
            Response response = Response.newBuilder()
                    .setStatus(StatusCode.INVALID_ARGUMENT_VALUE)
                    .build();
            log.info("server responded failed validation {}", response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        if (!prepaidService.processPrepaid(request)) {
            Response response = Response.newBuilder()
                    .setStatus(StatusCode.UNKNOWN_VALUE)
                    .build();
            log.info("server responded failed process {}", response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Response response = Response.newBuilder()
                .setStatus(StatusCode.OK_VALUE)
                .build();
        log.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
