package vn.co.abc.banking.voucher.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.co.abc.banking.voucher.entity.Voucher;

import java.util.UUID;

@Repository
public interface VoucherRepository extends CrudRepository<Voucher, UUID>, JpaSpecificationExecutor<Voucher> {
}
