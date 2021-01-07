package vn.co.abc.banking.api.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.co.abc.banking.api.controller.request.GetVouchersInfoRequest;
import vn.co.abc.banking.api.controller.request.PassCodeRequest;
import vn.co.abc.banking.api.entity.Passcode;
import vn.co.abc.banking.api.exception.PasscodeNotValidException;
import vn.co.abc.banking.api.message.DeletePassCodeMessage;
import vn.co.abc.banking.api.repository.PasscodeRepository;
import vn.co.abc.banking.api.specification.PasscodeSpecification;
import vn.co.abc.banking.proto.GetVouchersRequest;
import vn.co.abc.banking.proto.GetVouchersResponse;
import vn.co.abc.banking.proto.VoucherControllerGrpc;
import vn.co.abc.banking.proto.VoucherInfo;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CheckInfoService {

    private final PasscodeRepository passcodeRepository;
    private final EurekaClient client;
    private final ProducerService producerService;

    @Value("${spring.kafka.topic.deletePasscode}")
    private String deletePasscodeTopic;

    @Autowired
    public CheckInfoService(PasscodeRepository passcodeRepository, EurekaClient client, ProducerService producerService) {
        this.passcodeRepository = passcodeRepository;
        this.client = client;
        this.producerService = producerService;
    }

    public Passcode getPasscode(PassCodeRequest request) {
        Passcode passcode = new Passcode(request.getPhoneNumber());
        return passcodeRepository.save(passcode);
    }

    public List<VoucherInfo> getVouchersInfo(GetVouchersInfoRequest request) throws PasscodeNotValidException {
        Optional<Passcode> optional = getPasscode(request.getPhoneNumber(), request.getPassCode());

        if (optional.isEmpty()) {
            throw new PasscodeNotValidException("Passcode is not exist with phone number [" + request.getPhoneNumber() + "]");
        }

        Passcode passcode = optional.get();
        if (!passcode.isActivate()) {
            throw new PasscodeNotValidException("Passcode is not activated with phone number [" + request.getPhoneNumber() + "]");
        }

        final InstanceInfo instanceInfo = client.getNextServerFromEureka("voucher-service", false);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext()
                .build();

        VoucherControllerGrpc.VoucherControllerBlockingStub stub = VoucherControllerGrpc.newBlockingStub(channel);
        GetVouchersResponse vouchersResponse = stub.getVouchers(GetVouchersRequest.newBuilder().setPhoneNumber(request.getPhoneNumber()).build());

        sendObjectDeletePasscodeViaKafka(passcode);

        return vouchersResponse.getVoucherInfoList();
    }

    private Optional<Passcode> getPasscode(String phoneNumber, String passcodeString) {
        Specification<Passcode> specification = PasscodeSpecification.hasPhoneNumber(phoneNumber)
                .and(PasscodeSpecification.hasPasscode(passcodeString));
        return passcodeRepository.findOne(specification);
    }

    private void sendObjectDeletePasscodeViaKafka(Passcode passcode) {
        producerService.sendMessage(deletePasscodeTopic, new DeletePassCodeMessage(passcode.getId().toString()));
    }
}
