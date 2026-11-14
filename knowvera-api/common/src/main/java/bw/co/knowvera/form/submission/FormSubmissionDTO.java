package bw.co.knowvera.form.submission;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.form.FormDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A client's answers to a form, from the request to fill it to the organisation's decision.
 * They are part of the client's KYC record.
 */
public class FormSubmissionDTO
    extends AuditableDTO
{
    private static final long serialVersionUID = -3384710925560117429L;

    protected String formId;
    @Nullable
    protected String formCode;
    @Nullable
    protected String formName;
    /** The organisation that issued the form. */
    @Nullable
    protected String organisationId;
    @Nullable
    protected String organisation;
    /** The KYC record the answers are part of. */
    protected String kycRecordId;
    @Nullable
    protected String kycRecordRef;
    /** The client the KYC record is about (read only): INDIVIDUAL or ORGANISATION, its id and name. */
    @Nullable
    protected TargetEntity target;
    @Nullable
    protected String targetId;
    @Nullable
    protected String clientName;
    protected FormSubmissionStatus status;
    @Nullable
    protected LocalDate dueDate;
    @Nullable
    protected String submittedBy;
    @Nullable
    protected LocalDateTime submittedAt;
    @Nullable
    protected String reviewedBy;
    @Nullable
    protected LocalDateTime reviewedAt;
    /** The organisation's note on its decision; says what to correct when the form is returned. */
    @Nullable
    protected String reviewNote;
    @Nullable
    protected List<FormFieldValueDTO> values;
    /** The form's definition, included when a single submission is loaded so that it can be rendered. */
    @Nullable
    protected FormDTO form;

    public FormSubmissionDTO()
    {
        super();
    }

    public String getFormId() { return this.formId; }
    public void setFormId(final String value) { this.formId = value; }

    public String getFormCode() { return this.formCode; }
    public void setFormCode(final String value) { this.formCode = value; }

    public String getFormName() { return this.formName; }
    public void setFormName(final String value) { this.formName = value; }

    public String getOrganisationId() { return this.organisationId; }
    public void setOrganisationId(final String value) { this.organisationId = value; }

    public String getOrganisation() { return this.organisation; }
    public void setOrganisation(final String value) { this.organisation = value; }

    public String getKycRecordId() { return this.kycRecordId; }
    public void setKycRecordId(final String value) { this.kycRecordId = value; }

    public String getKycRecordRef() { return this.kycRecordRef; }
    public void setKycRecordRef(final String value) { this.kycRecordRef = value; }

    public String getClientName() { return this.clientName; }
    public void setClientName(final String value) { this.clientName = value; }

    public TargetEntity getTarget() { return this.target; }
    public void setTarget(final TargetEntity value) { this.target = value; }

    public String getTargetId() { return this.targetId; }
    public void setTargetId(final String value) { this.targetId = value; }

    public FormSubmissionStatus getStatus() { return this.status; }
    public void setStatus(final FormSubmissionStatus value) { this.status = value; }

    public LocalDate getDueDate() { return this.dueDate; }
    public void setDueDate(final LocalDate value) { this.dueDate = value; }

    public String getSubmittedBy() { return this.submittedBy; }
    public void setSubmittedBy(final String value) { this.submittedBy = value; }

    public LocalDateTime getSubmittedAt() { return this.submittedAt; }
    public void setSubmittedAt(final LocalDateTime value) { this.submittedAt = value; }

    public String getReviewedBy() { return this.reviewedBy; }
    public void setReviewedBy(final String value) { this.reviewedBy = value; }

    public LocalDateTime getReviewedAt() { return this.reviewedAt; }
    public void setReviewedAt(final LocalDateTime value) { this.reviewedAt = value; }

    public String getReviewNote() { return this.reviewNote; }
    public void setReviewNote(final String value) { this.reviewNote = value; }

    public List<FormFieldValueDTO> getValues()
    {
        if (this.values == null)
        {
            this.values = new ArrayList<FormFieldValueDTO>();
        }
        return this.values;
    }
    public void setValues(final List<FormFieldValueDTO> value) { this.values = value; }

    public FormDTO getForm() { return this.form; }
    public void setForm(final FormDTO value) { this.form = value; }
}
