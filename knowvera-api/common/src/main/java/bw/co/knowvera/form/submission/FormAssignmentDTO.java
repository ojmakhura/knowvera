package bw.co.knowvera.form.submission;

import java.io.Serializable;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

/**
 * Asks a client to fill a form as part of its KYC record.
 */
public class FormAssignmentDTO
    implements Serializable
{
    private static final long serialVersionUID = -6642087114402915533L;

    protected String formId;
    /** The KYC record of the client that has to fill the form: the answers become part of it. */
    protected String kycRecordId;
    /** When the answers are expected. */
    @Nullable
    protected LocalDate dueDate;

    public FormAssignmentDTO()
    {
    }

    public String getFormId() { return this.formId; }
    public void setFormId(final String value) { this.formId = value; }

    public String getKycRecordId() { return this.kycRecordId; }
    public void setKycRecordId(final String value) { this.kycRecordId = value; }

    public LocalDate getDueDate() { return this.dueDate; }
    public void setDueDate(final LocalDate value) { this.dueDate = value; }
}
