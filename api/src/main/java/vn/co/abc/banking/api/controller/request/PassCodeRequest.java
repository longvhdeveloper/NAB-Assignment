package vn.co.abc.banking.api.controller.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
public class PassCodeRequest {

    @NotEmpty(message = "Phone number is not valid")
    private String phoneNumber;
}
