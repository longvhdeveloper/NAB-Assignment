package vn.co.abc.banking.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.co.abc.banking.api.controller.request.PrepaidRequest;
import vn.co.abc.banking.api.controller.response.PrepaidResponse;
import vn.co.abc.banking.api.exception.ExecutePrepaidException;
import vn.co.abc.banking.api.service.PrepaidService;
import vn.co.abc.banking.proto.PaymentRequest;
import vn.co.abc.banking.proto.VoucherInfo;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/v1")
@Slf4j
public class PrepaidApiController {

    private final PrepaidService prepaidService;

    @Autowired
    public PrepaidApiController(PrepaidService prepaidService) {
        this.prepaidService = prepaidService;
    }

    @PostMapping("/prepaid")
    public ResponseEntity<PrepaidResponse> prepaid(@Valid @RequestBody PrepaidRequest request) throws Throwable {
        log.info("request receive: {}", request);

        VoucherInfo voucherInfo = prepaidService.processPrepaid(PaymentRequest.newBuilder()
                .setPhoneNumber(request.getPhoneNumber())
                .setAmount(request.getAmount())
                .build());

        if (Objects.isNull(voucherInfo)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(PrepaidResponse.builder()
                .code(voucherInfo.getCode())
                .build());
    }
}
