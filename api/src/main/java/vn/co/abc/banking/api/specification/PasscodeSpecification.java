package vn.co.abc.banking.api.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.co.abc.banking.api.entity.Passcode;

public class PasscodeSpecification {

    public static Specification<Passcode> hasPhoneNumber(String phoneNumber) {
        return (passCode, cq, cb) -> cb.equal(passCode.get("phoneNumber"), phoneNumber);
    }

    public static Specification<Passcode> hasPasscode(String passcode) {
        return (passCode, cq, cb) -> cb.equal(passCode.get("passCode"), passcode);
    }
}
