package bw.co.knowvera.form;

import bw.co.knowvera.TargetEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FormRepository extends JpaRepository<Form, UUID>, JpaSpecificationExecutor<Form> {

    List<Form> findByOrganisationIdOrderByNameAsc(UUID organisationId);

    Optional<Form> findByOrganisationIdAndCodeIgnoreCase(UUID organisationId, String code);

    /** The forms of the given organisations in one state, meant for one kind of client. */
    List<Form> findByOrganisationIdInAndStatusAndTargetType(Collection<UUID> organisationIds, FormStatus status,
            TargetEntity targetType);
}
