package bw.co.knowvera.organisation.client;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ClientRequestRepository extends JpaRepository<ClientRequest, UUID>, JpaSpecificationExecutor<ClientRequest> {

    Optional<java.lang.Long> countByStatus(bw.co.knowvera.organisation.client.ClientRequestStatus status);

    Optional<java.lang.Long> countByStatusAndOrganisationId(bw.co.knowvera.organisation.client.ClientRequestStatus status, String organisationId);

    @Query(
        value = """
            SELECT new bw.co.knowvera.organisation.client.ClientRequestDTO(
    CAST(c.id AS text),
    c.createdBy,
    c.createdAt,
    c.modifiedBy,
    c.modifiedAt,
    CONCAT(i.firstName, ' ', COALESCE(i.middleName, ''), ' ', i.surname),
    i.identityNo,
    i.identityType,
    i.emailAddress,
    c.status,
    CAST(o.id AS text),
    o.name,
    o.registrationNo,
    '', '', '', '', '',
    c.target,
    c.targetId,
    i.kycStatus,
    c.ref,
    o.code
)
FROM ClientRequest c
JOIN c.organisation o
JOIN Individual i ON c.targetId = CAST(i.id AS text)
WHERE c.target = bw.co.knowvera.TargetEntity.INDIVIDUAL
AND i.userCreated = false
        """
    )
    java.util.List<bw.co.knowvera.organisation.client.ClientRequestDTO> findClientRequestsForNonUserIndividuals(bw.co.knowvera.organisation.client.ClientRequestStatus status, String organisationId);

    @Query(
        value = """
            SELECT new bw.co.knowvera.organisation.client.ClientRequestDTO(
    CAST(c.id AS text),
    c.createdBy,
    c.createdAt,
    c.modifiedBy,
    c.modifiedAt,
    CONCAT(i.firstName, ' ', COALESCE(i.middleName, ''), ' ', i.surname),
    i.identityNo,
    i.identityType,
    i.emailAddress,
    c.status,
    CAST(o.id AS text),
    o.name,
    o.registrationNo,
    '', '', '', '', '',
    c.target,
    c.targetId,
    i.kycStatus,
    c.ref,
    o.code
)
FROM ClientRequest c
JOIN c.organisation o
JOIN Individual i ON c.targetId = CAST(i.id AS text)
WHERE c.target = bw.co.knowvera.TargetEntity.INDIVIDUAL
AND i.userCreated = false
        """
    )
    Page<ClientRequestDTO> findClientRequestsForNonUserIndividuals(bw.co.knowvera.organisation.client.ClientRequestStatus status, java.lang.String organisationId, Pageable pageable);

}
