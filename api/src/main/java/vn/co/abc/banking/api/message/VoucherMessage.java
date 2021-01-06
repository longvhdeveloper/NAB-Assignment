package vn.co.abc.banking.api.message;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VoucherMessage extends AbstractMessage {
    private String phoneNumber;
    private String transactionId;
}
