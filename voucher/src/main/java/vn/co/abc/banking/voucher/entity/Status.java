package vn.co.abc.banking.voucher.entity;

import java.util.stream.Stream;

public enum Status {
    NEW(1), USED(3);

    private final int status;

    private Status(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static Status of(int status) {
        return Stream.of(Status.values())
                .filter(p -> p.getStatus() == status)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
