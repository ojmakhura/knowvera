package bw.co.knowvera.settings.kyc;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface KycFieldGroupRepository extends JpaRepository<KycFieldGroup, UUID>, JpaSpecificationExecutor<KycFieldGroup> {

}