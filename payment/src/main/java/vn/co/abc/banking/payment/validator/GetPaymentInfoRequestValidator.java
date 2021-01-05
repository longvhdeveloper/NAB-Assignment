package vn.co.abc.banking.payment.validator;

import org.springframework.stereotype.Component;
import vn.co.abc.banking.proto.GetPaymentInfoRequest;

@Component
public class GetPaymentInfoRequestValidator {
    public boolean valid(GetPaymentInfoRequest request) {
        if (request.getTransactionId().isEmpty()) {
            return false;
        }

        return true;
    }
}
