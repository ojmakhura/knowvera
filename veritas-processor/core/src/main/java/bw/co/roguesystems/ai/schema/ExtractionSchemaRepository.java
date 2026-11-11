package bw.co.roguesystems.ai.schema;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExtractionSchemaRepository extends JpaRepository<ExtractionSchema, UUID> {

    Optional<ExtractionSchema> findFirstByCodeOrderByVersionDesc(String code);

    Optional<ExtractionSchema> findByCodeAndVersion(String code, int version);

    List<ExtractionSchema> findByCodeOrderByVersionDesc(String code);

    /** The latest version of every schema. */
    @Query("""
            SELECT s FROM ExtractionSchema s
            WHERE s.version = (SELECT MAX(q.version) FROM ExtractionSchema q WHERE q.code = s.code)
            ORDER BY s.code""")
    List<ExtractionSchema> findLatestVersions();
}
