package vn.co.abc.banking.api.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetVouchersInfoRequest {

    @NotEmpty(message = "Phone number is not valid")
    private String phoneNumber;

    @NotEmpty(message = "Passcode is not valid")
    private String passCode;
}
