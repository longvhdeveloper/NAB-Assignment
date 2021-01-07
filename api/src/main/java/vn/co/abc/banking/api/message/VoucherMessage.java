package vn.co.abc.banking.api.message;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class VoucherMessage extends AbstractMessage {
    private String phoneNumber;
    private String transactionId;

    public VoucherMessage(String phoneNumber, String transactionId) {
        this.phoneNumber = phoneNumber;
        this.transactionId = transactionId;
    }
}
