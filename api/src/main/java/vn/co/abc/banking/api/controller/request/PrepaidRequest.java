package vn.co.abc.banking.api.controller.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Builder
@Data
public class PrepaidRequest {

    @NotEmpty(message = "Phone number is not valid")
    private String phoneNumber;

    @Positive(message = "Amount must be greater than zero")
    private double amount;
}
