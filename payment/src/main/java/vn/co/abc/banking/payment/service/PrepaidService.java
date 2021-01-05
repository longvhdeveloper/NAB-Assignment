package vn.co.abc.banking.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.co.abc.banking.payment.entity.Prepaid;
import vn.co.abc.banking.payment.repository.PrepaidRepository;
import vn.co.abc.banking.proto.PrepaidRequest;

import java.util.UUID;

@Service
public class PrepaidService {

    private final PrepaidRepository prepaidRepository;

    @Autowired
    public PrepaidService(PrepaidRepository prepaidRepository) {
        this.prepaidRepository = prepaidRepository;
    }

    public boolean processPrepaid(PrepaidRequest request) {

        return true;
    }
}
