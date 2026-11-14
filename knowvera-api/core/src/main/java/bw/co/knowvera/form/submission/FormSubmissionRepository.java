package bw.co.knowvera.form.submission;

import java.util.Collection;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FormSubmissionRepository extends JpaRepository<FormSubmission, UUID>, JpaSpecificationExecutor<FormSubmission> {

    boolean existsByFormId(UUID formId);

    /** Whether the form was ever asked for on this KYC record. */
    boolean existsByFormIdAndKycRecordId(UUID formId, UUID kycRecordId);

    /** Whether the KYC record already has this form in a state other than the given one. */
    boolean existsByFormIdAndKycRecordIdAndStatusNot(UUID formId, UUID kycRecordId, FormSubmissionStatus status);

    /** Number of answers captured for the given fields. */
    @Query("SELECT COUNT(v) FROM FormFieldValue v WHERE v.field.id IN :fieldIds")
    long countValuesOfFields(@Param("fieldIds") Collection<UUID> fieldIds);
}
