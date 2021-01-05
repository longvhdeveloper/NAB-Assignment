package vn.co.abc.banking.payment.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.co.abc.banking.payment.entity.Prepaid;

import java.util.UUID;

@Repository
public interface PrepaidRepository extends CrudRepository<Prepaid, UUID> {
}
