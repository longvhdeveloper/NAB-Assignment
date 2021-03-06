package vn.co.abc.banking.api.message;

import lombok.Getter;

import java.util.Calendar;
import java.util.Date;

public abstract class AbstractMessage {

    @Getter
    private Date createDate;

    public AbstractMessage() {
        this.createDate = Calendar.getInstance().getTime();
    }
}
