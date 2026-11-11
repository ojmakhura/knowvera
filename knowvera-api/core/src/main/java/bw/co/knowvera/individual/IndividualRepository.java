package bw.co.knowvera.individual;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface IndividualRepository extends JpaRepository<Individual, UUID>, JpaSpecificationExecutor<Individual> {

    @Query(
        value = """
            SELECT i FROM Individual i WHERE i.identityNo = :identityNo AND i.identityType = :identityType
        """
    )
    Optional<bw.co.knowvera.individual.Individual> findByIdentityNoAndIdentityType(String identityNo, bw.co.knowvera.individual.IndividualIdentityType identityType);

    Optional<java.lang.Long> countByPepStatus(bw.co.knowvera.individual.PepStatus pepStatus);

    Optional<java.lang.Long> countByKycStatus(bw.co.knowvera.kyc.KycComplianceStatus kycStatus);

    Optional<java.lang.Long> countByEmploymentStatus(bw.co.knowvera.individual.EmploymentStatus employmentStatus);

    Optional<java.lang.Long> countBySex(bw.co.knowvera.individual.Sex sex);

    @Query(
        value = """
            SELECT i FROM Individual i WHERE i.identityNo = :identityNo
        """
    )
    Optional<bw.co.knowvera.individual.Individual> findByIdentityNo(String identityNo);

    @Query(
        value = """
            SELECT i FROM Individual i WHERE i.userId = :userId
        """
    )
    Optional<bw.co.knowvera.individual.Individual> findByUserId(String userId);

}
