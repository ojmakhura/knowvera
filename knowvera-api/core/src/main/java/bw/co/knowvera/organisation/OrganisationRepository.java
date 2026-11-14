package bw.co.knowvera.organisation;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface OrganisationRepository extends JpaRepository<Organisation, UUID>, JpaSpecificationExecutor<Organisation> {

    Optional<java.lang.Long> countByKycStatus(bw.co.knowvera.kyc.KycComplianceStatus kycStatus);

    Optional<java.lang.Long> countByStatus(bw.co.knowvera.GeneralStatus status);

    Optional<java.lang.Long> countByIsClientTrue();

    Optional<java.lang.Long> countByIsClientFalse();

    Optional<bw.co.knowvera.organisation.Organisation> findByCode(String code);

    Optional<bw.co.knowvera.organisation.Organisation> findByName(String name);

    Optional<bw.co.knowvera.organisation.Organisation> findByRegistrationNo(String registrationNo);

    @Query(
        value = """
            SELECT new bw.co.knowvera.organisation.OrganisationListDTO(
            CAST(o.id AS string),
            o.code, 
            o.name,
            o.registrationNo,
            o.status,
            o.contactEmailAddress, 
            o.kycStatus, 
            o.isClient,
            o.keycloakId,
            o.postalAddress,
            o.physicalAddress,
            o.closedAt
        )
        FROM Organisation o
        WHERE o.id = :id
        """
    )
    Optional<bw.co.knowvera.organisation.OrganisationListDTO> findListOrganisationById(java.util.UUID id);

    @Query(
        value = """
            SELECT new bw.co.knowvera.organisation.OrganisationListDTO(
            CAST(o.id AS string),
            o.code, 
            o.name,
            o.registrationNo,
            o.status,
            o.contactEmailAddress, 
            o.kycStatus, 
            o.isClient,
            o.keycloakId,
            o.postalAddress,
            o.physicalAddress,
            o.closedAt
        )
        FROM Organisation o
        WHERE o.keycloakId = :id
        """
    )
    Optional<bw.co.knowvera.organisation.OrganisationListDTO> findListOrganisationByKeycloakId(String keycloakId);

}
