package vn.co.abc.banking.payment.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
public class Prepaid {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID customerId;

    private double amount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    public Prepaid(UUID customerId, double amount) {
        this.customerId = customerId;
        this.amount = amount;
    }
}
