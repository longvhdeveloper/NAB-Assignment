package vn.co.abc.banking.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.co.abc.banking.api.entity.Passcode;

import java.util.UUID;

@Repository
public interface PasscodeRepository extends JpaRepository<Passcode, UUID>, JpaSpecificationExecutor<Passcode> {
}
