package vn.co.abc.banking.customer.repository;

import org.springframework.data.repository.CrudRepository;
import vn.co.abc.banking.customer.entity.Customer;

import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {
}
