package vn.co.abc.banking.api.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.co.abc.banking.api.entity.Passcode;
import vn.co.abc.banking.api.entity.PasscodeId;

@Repository
public interface PasscodeRepository extends CrudRepository<Passcode, PasscodeId>, JpaSpecificationExecutor<Passcode> {
}
