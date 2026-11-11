// false
// true
package bw.co.knowvera.suspicion;

import bw.co.knowvera.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * TODO: Model Documentation for SuspicionCase
 */
@Entity
@Table(
    name = "SUSPICION_CASE", uniqueConstraints = {
        @UniqueConstraint(
            name="SUSPICION_CASE_REF",
            columnNames={
                "REF"
            }
        )
    }
)
// Uncomment to enable caching for SuspicionCase
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQuery(name = "SuspicionCase.findAll", query = "SELECT s FROM SuspicionCase AS s")
public class SuspicionCase
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 2240831800281539549L;

    // Generate 13 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = false, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for SuspicionCase.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for SuspicionCase.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "REF", unique = false, nullable = false, insertable = true, updatable = true)
    private String ref;

    /**
     * TODO: Model Documentation for SuspicionCase.ref
     * @return this.ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for SuspicionCase.ref
     * @param refIn String
     */
    public void setRef(String refIn)
    {
        this.ref = refIn;
    }


    @Column(name = "KYC_RECORD_ID", unique = false, nullable = true, insertable = true, updatable = true)
    private String kycRecordId;

    /**
     * TODO: Model Documentation for SuspicionCase.kycRecordId
     * @return this.kycRecordId String
     */
    public String getKycRecordId()
    {
        return this.kycRecordId;
    }

    /**
     * TODO: Model Documentation for SuspicionCase.kycRecordId
     * @param kycRecordIdIn String
     */
    public void setKycRecordId(String kycRecordIdIn)
    {
        this.kycRecordId = kycRecordIdIn;
    }


    @Column(name = "SUBJECT_NAME", unique = false, nullable = false, insertable = true, updatable = true)
    private String subjectName;

    /**
     * TODO: Model Documentation for SuspicionCase.subjectName
     * @return this.subjectName String
     */
    public String getSubjectName()
    {
        return this.subjectName;
    }

    /**
     * TODO: Model Documentation for SuspicionCase.subjectName
     * @param subjectNameIn String
     */
    public void setSubjectName(String subjectNameIn)
    {
        this.subjectName = subjectNameIn;
    }


    @Column(name = "CAUSE", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private SuspicionTrigger cause;

    /**
     * TODO: Model Documentation for SuspicionCase.cause
     * @return this.cause SuspicionTrigger
     */
    public SuspicionTrigger getCause()
    {
        return this.cause;
    }

    /**
     * TODO: Model Documentation for SuspicionCase.cause
     * @param causeIn SuspicionTrigger
     */
    public void setCause(SuspicionTrigger causeIn)
    {
        this.cause = causeIn;
    }


    @Column(name = "STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private SuspicionCaseStatus status;

    /**
     * TODO: Model Documentation for SuspicionCase.status
     * @return this.status SuspicionCaseStatus
     */
    public SuspicionCaseStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for SuspicionCase.status
     * @param statusIn SuspicionCaseStatus
     */
    public void setStatus(SuspicionCaseStatus statusIn)
    {
        this.status = statusIn;
    }


    @Column(name = "GROUNDS", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "TEXT")
    private String grounds;

    /**
     * TODO: Model Documentation for SuspicionCase.grounds
     * @return this.grounds String
     */
    public String getGrounds()
    {
        return this.grounds;
    }

    /**
     * TODO: Model Documentation for SuspicionCase.grounds
     * @param groundsIn String
     */
    public void setGrounds(String groundsIn)
    {
        this.grounds = groundsIn;
    }


    @Column(name = "SUSPICION_DATE", unique = false, nullable = false, insertable = true, updatable = true)
    private LocalDate suspicionDate;

    /**
     * TODO: Model Documentation for SuspicionCase.suspicionDate
     * @return this.suspicionDate LocalDate
     */
    public LocalDate getSuspicionDate()
    {
        return this.suspicionDate;
    }

    /**
     * TODO: Model Documentation for SuspicionCase.suspicionDate
     * @param suspicionDateIn LocalDate
     */
    public void setSuspicionDate(LocalDate suspicionDateIn)
    {
        this.suspicionDate = suspicionDateIn;
    }


    @Column(name = "REPORT_DEADLINE", unique = false, nullable = false, insertable = true, updatable = true)
    private LocalDate reportDeadline;

    /**
     * TODO: Model Documentation for SuspicionCase.reportDeadline
     * @return this.reportDeadline LocalDate
     */
    public LocalDate getReportDeadline()
    {
        return this.reportDeadline;
    }

    /**
     * TODO: Model Documentation for SuspicionCase.reportDeadline
     * @param reportDeadlineIn LocalDate
     */
    public void setReportDeadline(LocalDate reportDeadlineIn)
    {
        this.reportDeadline = reportDeadlineIn;
    }


    @Column(name = "NOTES", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> notes;

    /**
     * TODO: Model Documentation for SuspicionCase.notes
     * @return this.notes List<String>
     */
    public List<String> getNotes()
    {
        return this.notes;
    }

    /**
     * TODO: Model Documentation for SuspicionCase.notes
     * @param notesIn List<String>
     */
    public void setNotes(List<String> notesIn)
    {
        this.notes = notesIn;
    }


    @Column(name = "REPORTED_AT", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDateTime reportedAt;

    /**
     * TODO: Model Documentation for SuspicionCase.reportedAt
     * @return this.reportedAt LocalDateTime
     */
    public LocalDateTime getReportedAt()
    {
        return this.reportedAt;
    }

    /**
     * TODO: Model Documentation for SuspicionCase.reportedAt
     * @param reportedAtIn LocalDateTime
     */
    public void setReportedAt(LocalDateTime reportedAtIn)
    {
        this.reportedAt = reportedAtIn;
    }


    @Column(name = "FIA_REFERENCE", unique = false, nullable = true, insertable = true, updatable = true)
    private String fiaReference;

    /**
     * TODO: Model Documentation for SuspicionCase.fiaReference
     * @return this.fiaReference String
     */
    public String getFiaReference()
    {
        return this.fiaReference;
    }

    /**
     * TODO: Model Documentation for SuspicionCase.fiaReference
     * @param fiaReferenceIn String
     */
    public void setFiaReference(String fiaReferenceIn)
    {
        this.fiaReference = fiaReferenceIn;
    }


    @Column(name = "OUTCOME", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "TEXT")
    private String outcome;

    /**
     * TODO: Model Documentation for SuspicionCase.outcome
     * @return this.outcome String
     */
    public String getOutcome()
    {
        return this.outcome;
    }

    /**
     * TODO: Model Documentation for SuspicionCase.outcome
     * @param outcomeIn String
     */
    public void setOutcome(String outcomeIn)
    {
        this.outcome = outcomeIn;
    }

    /**
     * Returns <code>true</code> if the argument is an SuspicionCase instance and all identifiers for this entity
     * equal the identifiers of the argument entity. The <code>equals</code> method of the parent entity
     * will also need to return <code>true</code>. Returns <code>false</code> otherwise.
     *
     * @see Auditable#equals(Object)
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof SuspicionCase))
        {
            return false;
        }
        final SuspicionCase that = (SuspicionCase)object;
        if (this.id == null || that.getId() == null || !this.id.equals(that.getId()))
        {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers and the hash code of the parent entity.
     *
     * @see Auditable#hashCode()
     */
    @Override
    public int hashCode()
    {
        int hashCode = super.hashCode();
        hashCode = 29 * hashCode + (this.id == null ? 0 : this.id.hashCode());

        return hashCode;
    }

    /**
     * Constructs new instances of {@link SuspicionCase}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link SuspicionCase}.
         * @return new SuspicionCase()
         */
        public static SuspicionCase newInstance()
        {
            return new SuspicionCase();
        }

        /**
         * Constructs a new instance of {@link SuspicionCase}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param ref String
         * @param subjectName String
         * @param cause SuspicionTrigger
         * @param status SuspicionCaseStatus
         * @param suspicionDate LocalDate
         * @param reportDeadline LocalDate
         * @return newInstance
         */
        public static SuspicionCase newInstance(LocalDateTime createdAt, String createdBy, String ref, String subjectName, SuspicionTrigger cause, SuspicionCaseStatus status, LocalDate suspicionDate, LocalDate reportDeadline)
        {
            final SuspicionCase entity = new SuspicionCase();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setRef(ref);
            entity.setSubjectName(subjectName);
            entity.setCause(cause);
            entity.setStatus(status);
            entity.setSuspicionDate(suspicionDate);
            entity.setReportDeadline(reportDeadline);
            return entity;
        }

        /**
         * Constructs a new instance of {@link SuspicionCase}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param ref String
         * @param kycRecordId String
         * @param subjectName String
         * @param cause SuspicionTrigger
         * @param status SuspicionCaseStatus
         * @param grounds String
         * @param suspicionDate LocalDate
         * @param reportDeadline LocalDate
         * @param notes List<String>
         * @param reportedAt LocalDateTime
         * @param fiaReference String
         * @param outcome String
         * @return newInstance SuspicionCase
         */
        public static SuspicionCase newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, String ref, String kycRecordId, String subjectName, SuspicionTrigger cause, SuspicionCaseStatus status, String grounds, LocalDate suspicionDate, LocalDate reportDeadline, List<String> notes, LocalDateTime reportedAt, String fiaReference, String outcome)
        {
            final SuspicionCase entity = new SuspicionCase();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setRef(ref);
            entity.setKycRecordId(kycRecordId);
            entity.setSubjectName(subjectName);
            entity.setCause(cause);
            entity.setStatus(status);
            entity.setGrounds(grounds);
            entity.setSuspicionDate(suspicionDate);
            entity.setReportDeadline(reportDeadline);
            entity.setNotes(notes);
            entity.setReportedAt(reportedAt);
            entity.setFiaReference(fiaReference);
            entity.setOutcome(outcome);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(SuspicionCase other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getRef() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRef().compareTo(other.getRef()));
            }
            if (this.getKycRecordId() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getKycRecordId().compareTo(other.getKycRecordId()));
            }
            if (this.getSubjectName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSubjectName().compareTo(other.getSubjectName()));
            }
            if (this.getCause() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getCause().compareTo(other.getCause()));
            }
            if (this.getStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getStatus().compareTo(other.getStatus()));
            }
            if (this.getGrounds() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getGrounds().compareTo(other.getGrounds()));
            }
            if (this.getSuspicionDate() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSuspicionDate().compareTo(other.getSuspicionDate()));
            }
            if (this.getReportDeadline() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getReportDeadline().compareTo(other.getReportDeadline()));
            }
            if (this.getNotes() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getNotes().size()-other.getNotes().size()));
            }
            if (this.getReportedAt() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getReportedAt().compareTo(other.getReportedAt()));
            }
            if (this.getFiaReference() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFiaReference().compareTo(other.getFiaReference()));
            }
            if (this.getOutcome() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getOutcome().compareTo(other.getOutcome()));
            }
        }
        return cmp;
    }

    /**
     * Returns a String representation of the object
     * @return String Textual representation of the object displaying name/value pairs for all attributes
     * @see Object#toString
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SuspicionCase(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" ref=").append(getRef());
        sb.append(" kycRecordId=").append(getKycRecordId());
        sb.append(" subjectName=").append(getSubjectName());
        sb.append(" cause=").append(getCause());
        sb.append(" status=").append(getStatus());
        sb.append(" grounds=").append(getGrounds());
        sb.append(" suspicionDate=").append(getSuspicionDate());
        sb.append(" reportDeadline=").append(getReportDeadline());
        sb.append(" notes=").append(getNotes());
        sb.append(" reportedAt=").append(getReportedAt());
        sb.append(" fiaReference=").append(getFiaReference());
        sb.append(" outcome=").append(getOutcome());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}