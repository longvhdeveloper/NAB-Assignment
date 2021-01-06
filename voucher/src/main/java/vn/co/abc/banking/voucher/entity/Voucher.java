package vn.co.abc.banking.voucher.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Table(indexes = {
        @Index(name = "idx_code", columnList = "code"),
        @Index(name = "idx_phone_number", columnList = "phoneNumber"),
        @Index(name = "idx_transaction_id", columnList = "transactionId")
})
public class Voucher {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(length = 50)
    private final String code;

    private final UUID transactionId;

    @Column(length = 20)
    private final String phoneNumber;

    private int statusCode;

    private Status status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    public Voucher(String code, String transactionId, String phoneNumber) {
        this.code = code;
        this.transactionId = UUID.fromString(transactionId);
        this.phoneNumber = phoneNumber;
        this.status = Status.NEW;
    }

    @PostLoad
    void fillTransient() {
        if (statusCode > 0) {
            this.status = Status.of(statusCode);
        }
    }

    @PrePersist
    void fillPersistent() {
        if (!Objects.isNull(status)) {
            this.statusCode = status.getStatus();
        }
    }

}
