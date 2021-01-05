package vn.co.abc.banking.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.co.abc.banking.api.controller.response.PrepaidResponse;

@RestController
@RequestMapping("/v1")
public class ApiController {

    @PostMapping("/prepaid")
    public ResponseEntity<PrepaidResponse> prepaid() {

        return null;
    }
}
