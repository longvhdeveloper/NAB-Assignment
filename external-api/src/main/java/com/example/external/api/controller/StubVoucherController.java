package com.example.external.api.controller;

import com.example.external.api.controller.response.GetVoucherResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/example")
public class StubVoucherController {

    @GetMapping("/voucher")
    public ResponseEntity<GetVoucherResponse> getVoucherCode() {
        int lengthLimit = 20;
        return ResponseEntity.status(HttpStatus.OK).body(GetVoucherResponse.builder()
                .code(RandomStringUtils.random(lengthLimit, true, true))
                .build());
    }
}
