package vn.co.abc.banking.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.Valid;

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
    public ResponseEntity<PrepaidResponse> prepaid(@Valid @RequestBody PrepaidRequest request) throws ExecutePrepaidException {
        log.info("request receive: {}", request);

        prepaidService.processPrepaid(PaymentRequest.newBuilder()
                .setPhoneNumber(request.getPhoneNumber())
                .setAmount(request.getAmount())
                .build());

        return null;
    }
}
