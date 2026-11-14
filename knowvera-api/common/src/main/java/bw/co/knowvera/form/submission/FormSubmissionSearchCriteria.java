package bw.co.knowvera.form.submission;

import java.io.Serializable;
import org.jspecify.annotations.Nullable;

/**
 * Filters for form submissions; an empty filter is ignored.
 */
public class FormSubmissionSearchCriteria
    implements Serializable
{
    private static final long serialVersionUID = 5120996873341207716L;

    @Nullable
    protected String formId;
    /** The organisation that issued the form. */
    @Nullable
    protected String organisationId;
    /** The KYC record the answers are part of. */
    @Nullable
    protected String kycRecordId;
    @Nullable
    protected FormSubmissionStatus status;

    public FormSubmissionSearchCriteria()
    {
    }

    public String getFormId() { return this.formId; }
    public void setFormId(final String value) { this.formId = value; }

    public String getOrganisationId() { return this.organisationId; }
    public void setOrganisationId(final String value) { this.organisationId = value; }

    public String getKycRecordId() { return this.kycRecordId; }
    public void setKycRecordId(final String value) { this.kycRecordId = value; }

    public FormSubmissionStatus getStatus() { return this.status; }
    public void setStatus(final FormSubmissionStatus value) { this.status = value; }
}
