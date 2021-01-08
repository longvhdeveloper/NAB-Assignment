package vn.co.abc.banking.api.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.co.abc.banking.api.entity.PasscodeId;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeletePassCodeMessage extends AbstractMessage {
    private PasscodeId id;
}
