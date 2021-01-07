package vn.co.abc.banking.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.jpa.domain.Specification;
import vn.co.abc.banking.api.controller.request.GetVouchersInfoRequest;
import vn.co.abc.banking.api.entity.Passcode;
import vn.co.abc.banking.api.exception.PasscodeNotValidException;
import vn.co.abc.banking.api.repository.PasscodeRepository;
import vn.co.abc.banking.api.specification.PasscodeSpecification;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CheckInfoServiceTest {

    @Mock
    private PasscodeRepository passcodeRepository;

    @InjectMocks
    private CheckInfoService checkInfoService;

    @Test
    public void test_GetVouchersWhenPasscodeIsNotExistShouldThrowException() {

        String passcode = "123456";
        String phoneNumber = "123456789";

        Specification<Passcode> specification = PasscodeSpecification.hasPhoneNumber(phoneNumber)
                .and(PasscodeSpecification.hasPasscode(passcode));
        Mockito.when(passcodeRepository.findOne(specification)).thenReturn(Optional.empty());
        Assertions.assertThrows(PasscodeNotValidException.class, () -> {
            checkInfoService.getVouchersInfo(new GetVouchersInfoRequest(phoneNumber, passcode));
        });
    }

    @Test
    public void test_GetVouchersWhenPasscodeIsNotActivatedShouldThrowException() {

        String passcode = "123456";
        String phoneNumber = "123456789";

        Passcode passcodeObject = new Passcode(phoneNumber);
        passcodeObject.changeToDeactivate();

        Specification<Passcode> specification = PasscodeSpecification.hasPhoneNumber(phoneNumber)
                .and(PasscodeSpecification.hasPasscode(passcode));
        Mockito.when(passcodeRepository.findOne(specification)).thenReturn(Optional.of(passcodeObject));

        Assertions.assertThrows(PasscodeNotValidException.class, () -> {
            checkInfoService.getVouchersInfo(new GetVouchersInfoRequest(phoneNumber, passcode));
        });
    }
}