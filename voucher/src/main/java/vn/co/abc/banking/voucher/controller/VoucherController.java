package vn.co.abc.banking.voucher.controller;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.co.abc.banking.proto.*;
import vn.co.abc.banking.voucher.entity.Voucher;
import vn.co.abc.banking.voucher.service.VoucherService;
import vn.co.abc.banking.voucher.validator.GetVoucherRequestValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GRpcService
@Slf4j
@RequestMapping("/voucher")
public class VoucherController extends VoucherControllerGrpc.VoucherControllerImplBase {

    private final VoucherService voucherService;
    private final GetVoucherRequestValidator getVoucherRequestValidator;

    @Autowired
    public VoucherController(VoucherService voucherService, GetVoucherRequestValidator getVoucherRequestValidator) {
        this.voucherService = voucherService;
        this.getVoucherRequestValidator = getVoucherRequestValidator;
    }

    @Override
    public void getVoucher(GetVoucherRequest request, StreamObserver<GetVoucherResponse> responseObserver) {
        log.info("request received {}", request);

        if (!getVoucherRequestValidator.valid(request)) {
            GetVoucherResponse response = GetVoucherResponse.newBuilder()
                    .setStatus(StatusCode.INVALID_ARGUMENT_VALUE)
                    .build();
            log.info("server responded failed validation {}", response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Optional<Voucher> optional = voucherService.getVoucher(request);
        if (optional.isEmpty()) {
            GetVoucherResponse response = GetVoucherResponse.newBuilder()
                    .setStatus(StatusCode.UNKNOWN_VALUE)
                    .build();
            log.info("server responded failed {}", response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Voucher voucher = optional.get();
        log.info("Voucher: {}", voucher);
        GetVoucherResponse response = GetVoucherResponse.newBuilder()
                .setStatus(StatusCode.OK_VALUE)
                .setVoucherInfo(VoucherInfo.newBuilder()
                        .setCode(voucher.getCode())
                        .build())
                .build();
        log.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        return;
    }

    @Override
    public void getVouchers(GetVouchersRequest request, StreamObserver<GetVouchersResponse> responseObserver) {
        List<Voucher> vouchers = voucherService.getVouchers(request);
        GetVouchersResponse response = GetVouchersResponse.newBuilder()
                .setStatus(StatusCode.OK_VALUE)
                .addAllVoucherInfo(vouchers.stream().map(voucher -> VoucherInfo.newBuilder()
                        .setCode(voucher.getCode())
                        .build()).collect(Collectors.toList()))
                .build();
        log.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
