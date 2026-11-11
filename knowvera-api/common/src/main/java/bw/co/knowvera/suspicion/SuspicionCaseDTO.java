package bw.co.knowvera.suspicion;

import bw.co.knowvera.AuditableDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for SuspicionCaseDTO
 */
public class SuspicionCaseDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 6260088681072229646L;

    // Class attributes
    /**
     * TODO: Model Documentation for SuspicionCaseDTO.ref
     */
    @Nullable
    protected String ref;
    /**
     * TODO: Model Documentation for SuspicionCaseDTO.kycRecordId
     */
    @Nullable
    protected String kycRecordId;
    /**
     * TODO: Model Documentation for SuspicionCaseDTO.subjectName
     */
    @Nullable
    protected String subjectName;
    /**
     * TODO: Model Documentation for SuspicionCaseDTO.cause
     */
    @Nullable
    protected SuspicionTrigger cause;
    /**
     * TODO: Model Documentation for SuspicionCaseDTO.status
     */
    @Nullable
    protected SuspicionCaseStatus status;
    /**
     * TODO: Model Documentation for SuspicionCaseDTO.grounds
     */
    @Nullable
    protected String grounds;
    /**
     * TODO: Model Documentation for SuspicionCaseDTO.suspicionDate
     */
    @Nullable
    protected LocalDate suspicionDate;
    /**
     * TODO: Model Documentation for SuspicionCaseDTO.reportDeadline
     */
    @Nullable
    protected LocalDate reportDeadline;
    /**
     * TODO: Model Documentation for SuspicionCaseDTO.notes
     */
    @Nullable
    protected List<String> notes;
    /**
     * TODO: Model Documentation for SuspicionCaseDTO.reportedAt
     */
    @Nullable
    protected LocalDateTime reportedAt;
    /**
     * TODO: Model Documentation for SuspicionCaseDTO.fiaReference
     */
    @Nullable
    protected String fiaReference;
    /**
     * TODO: Model Documentation for SuspicionCaseDTO.outcome
     */
    @Nullable
    protected String outcome;

    /**
     * Constructor taking only required properties
     */
    public SuspicionCaseDTO()
    {
        super();
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param refIn String
     * @param kycRecordIdIn String
     * @param subjectNameIn String
     * @param causeIn SuspicionTrigger
     * @param statusIn SuspicionCaseStatus
     * @param groundsIn String
     * @param suspicionDateIn LocalDate
     * @param reportDeadlineIn LocalDate
     * @param notesIn List<String>
     * @param reportedAtIn LocalDateTime
     * @param fiaReferenceIn String
     * @param outcomeIn String
     */
    public SuspicionCaseDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final String refIn, final String kycRecordIdIn, final String subjectNameIn, final SuspicionTrigger causeIn, final SuspicionCaseStatus statusIn, final String groundsIn, final LocalDate suspicionDateIn, final LocalDate reportDeadlineIn, final List<String> notesIn, final LocalDateTime reportedAtIn, final String fiaReferenceIn, final String outcomeIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.ref = refIn;
        this.kycRecordId = kycRecordIdIn;
        this.subjectName = subjectNameIn;
        this.cause = causeIn;
        this.status = statusIn;
        this.grounds = groundsIn;
        this.suspicionDate = suspicionDateIn;
        this.reportDeadline = reportDeadlineIn;
        this.notes = notesIn;
        this.reportedAt = reportedAtIn;
        this.fiaReference = fiaReferenceIn;
        this.outcome = outcomeIn;
    }

    /**
     * Copies constructor from other SuspicionCaseDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public SuspicionCaseDTO(final SuspicionCaseDTO otherBean)
    {
        super(otherBean);
        this.ref = otherBean.getRef();
        this.kycRecordId = otherBean.getKycRecordId();
        this.subjectName = otherBean.getSubjectName();
        this.cause = otherBean.getCause();
        this.status = otherBean.getStatus();
        this.grounds = otherBean.getGrounds();
        this.suspicionDate = otherBean.getSuspicionDate();
        this.reportDeadline = otherBean.getReportDeadline();
        this.notes = otherBean.getNotes();
        this.reportedAt = otherBean.getReportedAt();
        this.fiaReference = otherBean.getFiaReference();
        this.outcome = otherBean.getOutcome();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final SuspicionCaseDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setRef(otherBean.getRef());
            this.setKycRecordId(otherBean.getKycRecordId());
            this.setSubjectName(otherBean.getSubjectName());
            this.setCause(otherBean.getCause());
            this.setStatus(otherBean.getStatus());
            this.setGrounds(otherBean.getGrounds());
            this.setSuspicionDate(otherBean.getSuspicionDate());
            this.setReportDeadline(otherBean.getReportDeadline());
            this.setNotes(otherBean.getNotes());
            this.setReportedAt(otherBean.getReportedAt());
            this.setFiaReference(otherBean.getFiaReference());
            this.setOutcome(otherBean.getOutcome());
        }
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.ref
     * Get the ref Attribute
     * @return ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.ref
     * @param value String
     */
    public void setRef(final String value)
    {
        this.ref = value;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.kycRecordId
     * Get the kycRecordId Attribute
     * @return kycRecordId String
     */
    public String getKycRecordId()
    {
        return this.kycRecordId;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.kycRecordId
     * @param value String
     */
    public void setKycRecordId(final String value)
    {
        this.kycRecordId = value;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.subjectName
     * Get the subjectName Attribute
     * @return subjectName String
     */
    public String getSubjectName()
    {
        return this.subjectName;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.subjectName
     * @param value String
     */
    public void setSubjectName(final String value)
    {
        this.subjectName = value;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.cause
     * Get the cause Attribute
     * @return cause SuspicionTrigger
     */
    public SuspicionTrigger getCause()
    {
        return this.cause;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.cause
     * @param value SuspicionTrigger
     */
    public void setCause(final SuspicionTrigger value)
    {
        this.cause = value;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.status
     * Get the status Attribute
     * @return status SuspicionCaseStatus
     */
    public SuspicionCaseStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.status
     * @param value SuspicionCaseStatus
     */
    public void setStatus(final SuspicionCaseStatus value)
    {
        this.status = value;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.grounds
     * Get the grounds Attribute
     * @return grounds String
     */
    public String getGrounds()
    {
        return this.grounds;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.grounds
     * @param value String
     */
    public void setGrounds(final String value)
    {
        this.grounds = value;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.suspicionDate
     * Get the suspicionDate Attribute
     * @return suspicionDate LocalDate
     */
    public LocalDate getSuspicionDate()
    {
        return this.suspicionDate;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.suspicionDate
     * @param value LocalDate
     */
    public void setSuspicionDate(final LocalDate value)
    {
        this.suspicionDate = value;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.reportDeadline
     * Get the reportDeadline Attribute
     * @return reportDeadline LocalDate
     */
    public LocalDate getReportDeadline()
    {
        return this.reportDeadline;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.reportDeadline
     * @param value LocalDate
     */
    public void setReportDeadline(final LocalDate value)
    {
        this.reportDeadline = value;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.notes
     * Get the notes Attribute
     * @return notes List<String>
     */
    public List<String> getNotes()
    {
        if (this.notes == null)
        {
            this.notes = new ArrayList<String>();
        }
        return this.notes;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.notes
     * @param value List<String>
     */
    public void setNotes(final List<String> value)
    {
        this.notes = value;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.reportedAt
     * Get the reportedAt Attribute
     * @return reportedAt LocalDateTime
     */
    public LocalDateTime getReportedAt()
    {
        return this.reportedAt;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.reportedAt
     * @param value LocalDateTime
     */
    public void setReportedAt(final LocalDateTime value)
    {
        this.reportedAt = value;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.fiaReference
     * Get the fiaReference Attribute
     * @return fiaReference String
     */
    public String getFiaReference()
    {
        return this.fiaReference;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.fiaReference
     * @param value String
     */
    public void setFiaReference(final String value)
    {
        this.fiaReference = value;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.outcome
     * Get the outcome Attribute
     * @return outcome String
     */
    public String getOutcome()
    {
        return this.outcome;
    }

    /**
     * TODO: Model Documentation for SuspicionCaseDTO.outcome
     * @param value String
     */
    public void setOutcome(final String value)
    {
        this.outcome = value;
    }

    /**
     * @param object to compare this object against
     * @return boolean if equal
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object==null || object.getClass() != this.getClass())
        {
             return false;
        }
        // Check if the same object instance
        if (object==this)
        {
            return true;
        }
        SuspicionCaseDTO rhs = (SuspicionCaseDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getRef(), rhs.getRef())
            .append(this.getKycRecordId(), rhs.getKycRecordId())
            .append(this.getSubjectName(), rhs.getSubjectName())
            .append(this.getCause(), rhs.getCause())
            .append(this.getStatus(), rhs.getStatus())
            .append(this.getGrounds(), rhs.getGrounds())
            .append(this.getSuspicionDate(), rhs.getSuspicionDate())
            .append(this.getReportDeadline(), rhs.getReportDeadline())
            .append(this.getNotes(), rhs.getNotes())
            .append(this.getReportedAt(), rhs.getReportedAt())
            .append(this.getFiaReference(), rhs.getFiaReference())
            .append(this.getOutcome(), rhs.getOutcome())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final AuditableDTO object)
    {
        if (object==null)
        {
            return -1;
        }
        // Check if the same object instance
        if (object==this)
        {
            return 0;
        }
        if (!(object instanceof SuspicionCaseDTO))
        {
            return -1;
        }
        SuspicionCaseDTO myClass = (SuspicionCaseDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getRef(), myClass.getRef())
            .append(this.getKycRecordId(), myClass.getKycRecordId())
            .append(this.getSubjectName(), myClass.getSubjectName())
            .append(this.getCause(), myClass.getCause())
            .append(this.getStatus(), myClass.getStatus())
            .append(this.getGrounds(), myClass.getGrounds())
            .append(this.getSuspicionDate(), myClass.getSuspicionDate())
            .append(this.getReportDeadline(), myClass.getReportDeadline())
            .append(this.getNotes(), myClass.getNotes())
            .append(this.getReportedAt(), myClass.getReportedAt())
            .append(this.getFiaReference(), myClass.getFiaReference())
            .append(this.getOutcome(), myClass.getOutcome())
            .toComparison();
    }

    /**
     * @return int hashCode value
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(1249046965, -82296885)
            .appendSuper(super.hashCode())
            .append(this.getRef())
            .append(this.getKycRecordId())
            .append(this.getSubjectName())
            .append(this.getCause())
            .append(this.getStatus())
            .append(this.getGrounds())
            .append(this.getSuspicionDate())
            .append(this.getReportDeadline())
            .append(this.getNotes())
            .append(this.getReportedAt())
            .append(this.getFiaReference())
            .append(this.getOutcome())
            .toHashCode();
    }

    /**
     * @return String representation of object
     * @see Object#toString()
     */
    @Override
    public String toString()
    {

        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("\t\"ref\" : \"" + this.getRef() + "\"");
        builder.append("\t,\"kycRecordId\" : \"" + this.getKycRecordId() + "\"");
        builder.append("\t,\"subjectName\" : \"" + this.getSubjectName() + "\"");
        builder.append("\t,\"cause\" : \"" + this.getCause() + "\"");
        builder.append("\t,\"status\" : \"" + this.getStatus() + "\"");
        builder.append("\t,\"grounds\" : \"" + this.getGrounds() + "\"");
        builder.append("\t,\"suspicionDate\" : \"" + this.getSuspicionDate() + "\"");
        builder.append("\t,\"reportDeadline\" : \"" + this.getReportDeadline() + "\"");
        builder.append("\t,\"notes\" : \"" + this.getNotes() + "\"");
        builder.append("\t,\"reportedAt\" : \"" + this.getReportedAt() + "\"");
        builder.append("\t,\"fiaReference\" : \"" + this.getFiaReference() + "\"");
        builder.append("\t,\"outcome\" : \"" + this.getOutcome() + "\"");
        builder.append("\t,\"id\" : \"" + this.getId() + "\"");
        builder.append("\t,\"createdBy\" : \"" + this.getCreatedBy() + "\"");
        builder.append("\t,\"createdAt\" : \"" + this.getCreatedAt() + "\"");
        builder.append("\t,\"modifiedBy\" : \"" + this.getModifiedBy() + "\"");
        builder.append("\t,\"modifiedAt\" : \"" + this.getModifiedAt() + "\"");
        builder.append("}");
        return builder.toString();

    }

    /**
     * Compares the properties of this instance to the properties of the argument. This method will return
     * {@code false} as soon as it detects that the argument is {@code null} or not of the same type as
     * (or a sub-type of) this instance's type.
     *
     * <p/>For array, collection or map properties the comparison will be done one level deep, in other words:
     * the elements will be compared using the {@code equals()} operation.
     *
     * <p/>Note that two properties will be considered equal when both values are {@code null}.
     *
     * @param thatObject the object containing the properties to compare against this instance
     * @return this method will return {@code true} in case the argument has the same type as this class, or is a
     *      sub-type of this class and all properties as found on this class have equal values when queried on that
     *      argument instance; in all other cases this method will return {@code false}
     */
    @Override
    public boolean equalProperties(final Object thatObject)
    {
        if (thatObject == null || !this.getClass().isAssignableFrom(thatObject.getClass()))
        {
            return false;
        }

        @SuppressWarnings("unchecked")
        final SuspicionCaseDTO that = (SuspicionCaseDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getRef(), that.getRef())
            && equal(this.getKycRecordId(), that.getKycRecordId())
            && equal(this.getSubjectName(), that.getSubjectName())
            && equal(this.getCause(), that.getCause())
            && equal(this.getStatus(), that.getStatus())
            && equal(this.getGrounds(), that.getGrounds())
            && equal(this.getSuspicionDate(), that.getSuspicionDate())
            && equal(this.getReportDeadline(), that.getReportDeadline())
            && equal(this.getNotes(), that.getNotes())
            && equal(this.getReportedAt(), that.getReportedAt())
            && equal(this.getFiaReference(), that.getFiaReference())
            && equal(this.getOutcome(), that.getOutcome())
        ;
    }

}