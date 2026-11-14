package bw.co.roguesystems.ai.processor;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DataProcessorRepository extends JpaRepository<DataProcessor, UUID>, JpaSpecificationExecutor<DataProcessor> {

    @Query(
        value = """
            SELECT dt
	FROM DataProcessor dt
	LEFT JOIN FETCH dt.expectedFields
        """
    )
    java.util.List<bw.co.roguesystems.ai.processor.DataProcessor> findAll();

    @Query(
        value = """
            SELECT dt
	FROM DataProcessor dt
	LEFT JOIN FETCH dt.expectedFields
        """
    )
    Page<DataProcessor> findAll(Pageable pageable);

    @Query(
        value = """
            SELECT dt
	FROM DataProcessor dt
	LEFT JOIN FETCH dt.expectedFields
	WHERE dt.id = :id
	
        """
    )
    Optional<bw.co.roguesystems.ai.processor.DataProcessor> findById(java.util.UUID id);

}
