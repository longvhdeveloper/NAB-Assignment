package vn.co.abc.banking.api.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoucherResponse {
    private String code;
}
