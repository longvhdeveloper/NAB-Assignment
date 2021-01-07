package vn.co.abc.banking.api.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeletePassCodeMessage extends AbstractMessage {
    private String id;
}
