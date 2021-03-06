package vn.co.abc.banking.voucher.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class Voucher {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(length = 50)
    private String code;

    private UUID transactionId;

    @Column(length = 20)
    private String phoneNumber;

    private int statusCode;

    @Transient
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

    @Override
    public String toString() {
        return "Voucher{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", transactionId=" + transactionId +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", statusCode=" + statusCode +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
