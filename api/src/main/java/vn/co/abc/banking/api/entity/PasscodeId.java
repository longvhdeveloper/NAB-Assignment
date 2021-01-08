package vn.co.abc.banking.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasscodeId implements Serializable {
    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 6)
    private String passCode;
}
