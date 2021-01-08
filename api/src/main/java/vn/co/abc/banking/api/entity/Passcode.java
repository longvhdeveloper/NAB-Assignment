package vn.co.abc.banking.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Table(indexes = {
        @Index(name = "idx_phone_number", columnList = "phoneNumber"),
        @Index(name = "idx_pass_code", columnList = "passCode")
})
@Entity
@Getter
@NoArgsConstructor
public class Passcode {

    @EmbeddedId
    private PasscodeId id;

    @Getter
    @Basic
    private int statusCode;

    @Transient
    @Getter
    private Status status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    public Passcode(String phoneNumber) {
        this.id = new PasscodeId(phoneNumber, generatePassCode());
        this.status = Status.ACTIVATE;
    }

    public boolean isActivate() {
        return this.status.equals(Status.ACTIVATE);
    }

    public void changeToDeactivate() {
        this.status = Status.DEACTIVATE;
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
        } else {
            this.statusCode = Status.ACTIVATE.getStatus();
        }
    }

    private String generatePassCode() {
        return RandomStringUtils.randomNumeric(6);
    }

    @Override
    public String toString() {
        return "Passcode{" +
                "id=" + id +
                ", statusCode=" + statusCode +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
