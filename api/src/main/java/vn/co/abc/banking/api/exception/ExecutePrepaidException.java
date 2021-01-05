package vn.co.abc.banking.api.exception;

import lombok.Getter;

@Getter
public class ExecutePrepaidException extends Exception {
    private String message;

    public ExecutePrepaidException(String message) {
        super(message);
    }
}
