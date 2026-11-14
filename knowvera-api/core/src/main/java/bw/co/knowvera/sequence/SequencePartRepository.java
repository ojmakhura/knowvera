package bw.co.knowvera.sequence;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SequencePartRepository extends JpaRepository<SequencePart, Long>, JpaSpecificationExecutor<SequencePart> {

}