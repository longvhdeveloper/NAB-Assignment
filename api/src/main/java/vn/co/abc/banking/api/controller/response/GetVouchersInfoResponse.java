package vn.co.abc.banking.api.controller.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetVouchersInfoResponse {
    private List<VoucherResponse> vouchers;
}
