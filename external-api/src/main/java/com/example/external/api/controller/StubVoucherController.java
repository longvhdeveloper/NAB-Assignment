package com.example.external.api.controller;

import com.example.external.api.controller.response.GetVoucherResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/v1/example")
@Slf4j
public class StubVoucherController {

    @GetMapping("/voucher")
    public ResponseEntity<GetVoucherResponse> getVoucherCode() {

        int lengthLimit = 20;

        // TODO: it stub to case get or not get voucher code
        // I using random to decide return voucher code or empty
        Random random = new Random();
        boolean isGenerateCode = random.nextBoolean();
        log.info("Is generate voucher code: {}", isGenerateCode);
        String code = isGenerateCode ? RandomStringUtils.random(lengthLimit, true, true) : "";

        return ResponseEntity.status(HttpStatus.OK).body(GetVoucherResponse.builder()
                .code(code)
                .build());
    }
}
