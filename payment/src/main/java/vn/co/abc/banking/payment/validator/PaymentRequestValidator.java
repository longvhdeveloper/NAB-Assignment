package vn.co.abc.banking.payment.validator;

import org.springframework.stereotype.Component;
import vn.co.abc.banking.proto.PaymentRequest;

@Component
public class PaymentRequestValidator {
    public boolean valid(PaymentRequest request) {
        if (request.getPhoneNumber().isEmpty()) {
            return false;
        }

        if (request.getAmount() < 0) {
            return false;
        }

        return true;
    }
}
