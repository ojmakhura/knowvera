package bw.co.knowvera.suspicion;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SuspicionCaseRepository extends JpaRepository<SuspicionCase, UUID>, JpaSpecificationExecutor<SuspicionCase> {

}