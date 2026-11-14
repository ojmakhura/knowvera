package bw.co.knowvera.document.type.verification;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface VerificationDataConfigRepository extends JpaRepository<VerificationDataConfig, UUID>, JpaSpecificationExecutor<VerificationDataConfig> {

    @Query(
        value = """
            SELECT vdc
    FROM VerificationDataConfig vdc
    JOIN FETCH vdc.expectedFields
    WHERE vdc.documentType.id = :documentTypeId
        """
    )
    java.util.List<bw.co.knowvera.document.type.verification.VerificationDataConfig> findByDocumentTypeId(java.util.UUID documentTypeId);

    @Query(
        value = """
            SELECT vdc
    FROM VerificationDataConfig vdc
    JOIN FETCH vdc.expectedFields
    WHERE vdc.documentType.id = :documentTypeId
        """
    )
    Page<VerificationDataConfig> findByDocumentTypeId(java.util.UUID documentTypeId, Pageable pageable);

    @Query(
        value = """
            SELECT COUNT(vdc.id)
                FROM VerificationDataConfig vdc
                WHERE vdc.documentType.id = :documentTypeId
        """
    )
    Optional<java.lang.Integer> countByDocumentTypeId(java.util.UUID documentTypeId);

}
