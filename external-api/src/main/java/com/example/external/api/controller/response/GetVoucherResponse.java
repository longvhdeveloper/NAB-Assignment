package com.example.external.api.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetVoucherResponse {
    private String code;
}
