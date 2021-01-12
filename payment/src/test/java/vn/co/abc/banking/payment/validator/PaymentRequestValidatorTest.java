package vn.co.abc.banking.payment.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.co.abc.banking.proto.PaymentRequest;

import static org.junit.jupiter.api.Assertions.assertFalse;

class PaymentRequestValidatorTest {
    private PaymentRequestValidator paymentRequestValidator;

    @BeforeEach
    public void setUp() {
        paymentRequestValidator = new PaymentRequestValidator();
    }


    @Test
    public void test_PaymentRequestWhenPhoneNumberIsEmptyShouldReturnFalse() {
        PaymentRequest paymentRequest = PaymentRequest.newBuilder().setPhoneNumber("").build();
        assertFalse(paymentRequestValidator.valid(paymentRequest));
    }

    @Test
    public void test_PaymentRequestWhenAmountIsLessThanZeroShouldReturnFalse() {
        PaymentRequest paymentRequest = PaymentRequest.newBuilder().setPhoneNumber("0123456789").setAmount(-100).build();
        assertFalse(paymentRequestValidator.valid(paymentRequest));
    }
}