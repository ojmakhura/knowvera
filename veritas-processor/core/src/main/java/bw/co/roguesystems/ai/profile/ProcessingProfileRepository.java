package bw.co.roguesystems.ai.profile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProcessingProfileRepository extends JpaRepository<ProcessingProfile, UUID> {

    Optional<ProcessingProfile> findFirstByCodeOrderByVersionDesc(String code);

    Optional<ProcessingProfile> findByCodeAndVersion(String code, int version);

    List<ProcessingProfile> findByCodeOrderByVersionDesc(String code);

    /** The latest version of every profile. */
    @Query("""
            SELECT p FROM ProcessingProfile p
            WHERE p.version = (SELECT MAX(q.version) FROM ProcessingProfile q WHERE q.code = p.code)
            ORDER BY p.code""")
    List<ProcessingProfile> findLatestVersions();
}
