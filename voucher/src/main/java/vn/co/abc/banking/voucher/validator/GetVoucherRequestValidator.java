package vn.co.abc.banking.voucher.validator;

import org.springframework.stereotype.Component;
import vn.co.abc.banking.proto.GetVoucherRequest;

@Component
public class GetVoucherRequestValidator {
    public boolean valid(GetVoucherRequest request) {
        if (request.getPhoneNumber().isEmpty()) {
            return false;
        }

        if (request.getTransactionId().isEmpty()) {
            return false;
        }

        return true;
    }
}
