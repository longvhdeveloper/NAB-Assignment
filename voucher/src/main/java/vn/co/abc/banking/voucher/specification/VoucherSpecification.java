package vn.co.abc.banking.voucher.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.co.abc.banking.voucher.entity.Voucher;

import java.util.UUID;

public class VoucherSpecification {
    public static Specification<Voucher> hasCode(String code) {
        return (voucher, cq, cb) -> cb.equal(voucher.get("code"), code);
    }

    public static Specification<Voucher> hasPhoneNumber(String phoneNumber) {
        return (voucher, cq, cb) -> cb.equal(voucher.get("phoneNumber"), phoneNumber);
    }

    public static Specification<Voucher> hasTransactionId(String transactionId) {
        return (voucher, cq, cb) -> cb.equal(voucher.get("transactionId"), UUID.fromString(transactionId));
    }
}
