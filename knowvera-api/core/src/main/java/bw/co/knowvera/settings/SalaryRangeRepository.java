package bw.co.knowvera.settings;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SalaryRangeRepository extends JpaRepository<SalaryRange, Long>, JpaSpecificationExecutor<SalaryRange> {

}