package bw.co.knowvera.kyc;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface KycRecordRepository extends JpaRepository<KycRecord, UUID>, JpaSpecificationExecutor<KycRecord> {

    @Query(
        value = """
            SELECT r
              FROM KycRecord r
              LEFT JOIN FETCH r.employmentRecord
              WHERE r.targetId = :ownerId
                AND r.target = :ownerType
                AND :today BETWEEN r.uploadDate AND r.expiryDate
                AND r.expiryDate = (
                    SELECT MAX(r2.expiryDate)
                    FROM KycRecord r2
                    WHERE r2.targetId = :ownerId
                      AND r2.target = :ownerType
                      AND :today BETWEEN r2.uploadDate AND r2.expiryDate
                )
                ORDER BY r.createdAt DESC
        """
    )
    Optional<bw.co.knowvera.kyc.KycRecord> findLatestValidForOwner(String ownerId, bw.co.knowvera.TargetEntity ownerType, java.time.LocalDate today);

    @Query(
        value = """
            SELECT r
	FROM KycRecord r
	LEFT JOIN FETCH r.employmentRecord
	ORDER BY r.createdAt DESC
        """
    )
    java.util.List<bw.co.knowvera.kyc.KycRecord> findAll();

    @Query(
        value = """
            SELECT r
	FROM KycRecord r
	LEFT JOIN FETCH r.employmentRecord
	ORDER BY r.createdAt DESC
        """
    )
    Page<KycRecord> findAll(Pageable pageable);

    @Query(
        value = """
            SELECT r
	        FROM KycRecord r
	        LEFT JOIN FETCH r.employmentRecord
	        LEFT JOIN FETCH r.kycReportSections
	        WHERE r.id = :id
	        ORDER BY r.createdAt DESC
        """
    )
    Optional<bw.co.knowvera.kyc.KycRecord> findById(java.util.UUID id);

}
