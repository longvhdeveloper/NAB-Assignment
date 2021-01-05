package vn.co.abc.banking.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.co.abc.banking.payment.entity.Payment;
import vn.co.abc.banking.payment.repository.PrepaidRepository;
import vn.co.abc.banking.proto.GetPaymentInfoRequest;
import vn.co.abc.banking.proto.PaymentRequest;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PrepaidRepository prepaidRepository;

    @Autowired
    public PaymentService(PrepaidRepository prepaidRepository) {
        this.prepaidRepository = prepaidRepository;
    }

    public Payment processPrepaid(PaymentRequest request) {
        return prepaidRepository.save(new Payment(request.getPhoneNumber(), request.getAmount()));
    }

    public Optional<Payment> getPaymentInfo(GetPaymentInfoRequest request) {
        return prepaidRepository.findById(UUID.fromString(request.getTransactionId()));
    }
}
