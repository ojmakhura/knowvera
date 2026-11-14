package bw.co.knowvera.form.submission;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.form.Form;
import bw.co.knowvera.kyc.KycRecord;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

/**
 * A client's answers to a form, from the request to fill it to the organisation's decision.
 * They are part of the client's KYC record.
 */
@Entity
@Table(
    name = "FORM_SUBMISSION",
    indexes = {
        @Index(name = "IDX_FORM_SUBMISSION_KYC_RECORD", columnList = "KYC_RECORD_FK")
    })
public class FormSubmission
    extends Auditable
{
    private static final long serialVersionUID = 3306918854771209450L;

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORM_FK")
    private Form form;

    /** The KYC record the answers are part of; they go when the record goes. */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "KYC_RECORD_FK")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private KycRecord kycRecord;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private FormSubmissionStatus status = FormSubmissionStatus.NEW;

    @Column(name = "DUE_DATE", nullable = true)
    private LocalDate dueDate;

    @Column(name = "SUBMITTED_BY", nullable = true)
    private String submittedBy;

    @Column(name = "SUBMITTED_AT", nullable = true)
    private LocalDateTime submittedAt;

    @Column(name = "REVIEWED_BY", nullable = true)
    private String reviewedBy;

    @Column(name = "REVIEWED_AT", nullable = true)
    private LocalDateTime reviewedAt;

    @Column(name = "REVIEW_NOTE", nullable = true, length = 2000)
    private String reviewNote;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "submission", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<FormFieldValue> values = new ArrayList<FormFieldValue>();

    public UUID getId() { return this.id; }
    public void setId(UUID idIn) { this.id = idIn; }

    public Form getForm() { return this.form; }
    public void setForm(Form formIn) { this.form = formIn; }

    public KycRecord getKycRecord() { return this.kycRecord; }
    public void setKycRecord(KycRecord kycRecordIn) { this.kycRecord = kycRecordIn; }

    public FormSubmissionStatus getStatus() { return this.status; }
    public void setStatus(FormSubmissionStatus statusIn) { this.status = statusIn; }

    public LocalDate getDueDate() { return this.dueDate; }
    public void setDueDate(LocalDate dueDateIn) { this.dueDate = dueDateIn; }

    public String getSubmittedBy() { return this.submittedBy; }
    public void setSubmittedBy(String submittedByIn) { this.submittedBy = submittedByIn; }

    public LocalDateTime getSubmittedAt() { return this.submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAtIn) { this.submittedAt = submittedAtIn; }

    public String getReviewedBy() { return this.reviewedBy; }
    public void setReviewedBy(String reviewedByIn) { this.reviewedBy = reviewedByIn; }

    public LocalDateTime getReviewedAt() { return this.reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAtIn) { this.reviewedAt = reviewedAtIn; }

    public String getReviewNote() { return this.reviewNote; }
    public void setReviewNote(String reviewNoteIn) { this.reviewNote = reviewNoteIn; }

    public List<FormFieldValue> getValues() { return this.values; }
    public void setValues(List<FormFieldValue> valuesIn) { this.values = valuesIn; }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof FormSubmission))
        {
            return false;
        }
        final FormSubmission that = (FormSubmission)object;
        return this.id != null && this.id.equals(that.getId());
    }

    @Override
    public int hashCode()
    {
        return 29 * (this.id == null ? 0 : this.id.hashCode());
    }

    @Override
    public String toString()
    {
        return "FormSubmission( status=" + this.status + " id=" + this.id + ")";
    }

    /**
     * Constructs new instances of {@link FormSubmission}.
     */
    public static final class Factory
    {
        public static FormSubmission newInstance()
        {
            return new FormSubmission();
        }
    }
}
