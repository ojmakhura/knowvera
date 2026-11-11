package bw.co.knowvera.screening;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SanctionsListEntryRepository extends JpaRepository<SanctionsListEntry, UUID>, JpaSpecificationExecutor<SanctionsListEntry> {

}