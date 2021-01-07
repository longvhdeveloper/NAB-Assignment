package vn.co.abc.banking.sms.message;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SMSVoucherMessage extends AbstractMessage {

    private String phoneNumber;
    private String voucherCode;

    public SMSVoucherMessage(String phoneNumber, String voucherCode) {
        this.phoneNumber = phoneNumber;
        this.voucherCode = voucherCode;
    }
}
