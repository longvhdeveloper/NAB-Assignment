package vn.co.abc.banking.payment.validator;

import org.springframework.stereotype.Component;
import vn.co.abc.banking.proto.PrepaidRequest;

@Component
public class PrepaidRequestValidator {
    public boolean valid(PrepaidRequest request) {
        if (request.getCustomerId().isEmpty()) {
            return false;
        }

        if (request.getAmount() < 0) {
            return false;
        }

        return true;
    }
}
