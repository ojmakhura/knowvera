package bw.co.knowvera.screening;

import bw.co.knowvera.AuditableDTO;
import java.time.LocalDateTime;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for ScreeningHitDTO
 */
public class ScreeningHitDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 3941499605710828703L;

    // Class attributes
    /**
     * TODO: Model Documentation for ScreeningHitDTO.kycRecordId
     */
    @Nullable
    protected String kycRecordId;
    /**
     * TODO: Model Documentation for ScreeningHitDTO.subjectName
     */
    @Nullable
    protected String subjectName;
    /**
     * TODO: Model Documentation for ScreeningHitDTO.subjectRole
     */
    @Nullable
    protected String subjectRole;
    /**
     * TODO: Model Documentation for ScreeningHitDTO.source
     */
    @Nullable
    protected ScreeningListSource source;
    /**
     * TODO: Model Documentation for ScreeningHitDTO.entryReference
     */
    @Nullable
    protected String entryReference;
    /**
     * TODO: Model Documentation for ScreeningHitDTO.entryName
     */
    @Nullable
    protected String entryName;
    /**
     * TODO: Model Documentation for ScreeningHitDTO.score
     */
    @Nullable
    protected Double score;
    /**
     * TODO: Model Documentation for ScreeningHitDTO.status
     */
    @Nullable
    protected ScreeningHitStatus status;
    /**
     * TODO: Model Documentation for ScreeningHitDTO.listVersion
     */
    @Nullable
    protected String listVersion;
    /**
     * TODO: Model Documentation for ScreeningHitDTO.reviewedBy
     */
    @Nullable
    protected String reviewedBy;
    /**
     * TODO: Model Documentation for ScreeningHitDTO.reviewedAt
     */
    @Nullable
    protected LocalDateTime reviewedAt;
    /**
     * TODO: Model Documentation for ScreeningHitDTO.reviewNotes
     */
    @Nullable
    protected String reviewNotes;

    /**
     * Constructor taking only required properties
     */
    public ScreeningHitDTO()
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
     * @param kycRecordIdIn String
     * @param subjectNameIn String
     * @param subjectRoleIn String
     * @param sourceIn ScreeningListSource
     * @param entryReferenceIn String
     * @param entryNameIn String
     * @param scoreIn Double
     * @param statusIn ScreeningHitStatus
     * @param listVersionIn String
     * @param reviewedByIn String
     * @param reviewedAtIn LocalDateTime
     * @param reviewNotesIn String
     */
    public ScreeningHitDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final String kycRecordIdIn, final String subjectNameIn, final String subjectRoleIn, final ScreeningListSource sourceIn, final String entryReferenceIn, final String entryNameIn, final Double scoreIn, final ScreeningHitStatus statusIn, final String listVersionIn, final String reviewedByIn, final LocalDateTime reviewedAtIn, final String reviewNotesIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.kycRecordId = kycRecordIdIn;
        this.subjectName = subjectNameIn;
        this.subjectRole = subjectRoleIn;
        this.source = sourceIn;
        this.entryReference = entryReferenceIn;
        this.entryName = entryNameIn;
        this.score = scoreIn;
        this.status = statusIn;
        this.listVersion = listVersionIn;
        this.reviewedBy = reviewedByIn;
        this.reviewedAt = reviewedAtIn;
        this.reviewNotes = reviewNotesIn;
    }

    /**
     * Copies constructor from other ScreeningHitDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public ScreeningHitDTO(final ScreeningHitDTO otherBean)
    {
        super(otherBean);
        this.kycRecordId = otherBean.getKycRecordId();
        this.subjectName = otherBean.getSubjectName();
        this.subjectRole = otherBean.getSubjectRole();
        this.source = otherBean.getSource();
        this.entryReference = otherBean.getEntryReference();
        this.entryName = otherBean.getEntryName();
        this.score = otherBean.getScore();
        this.status = otherBean.getStatus();
        this.listVersion = otherBean.getListVersion();
        this.reviewedBy = otherBean.getReviewedBy();
        this.reviewedAt = otherBean.getReviewedAt();
        this.reviewNotes = otherBean.getReviewNotes();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final ScreeningHitDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setKycRecordId(otherBean.getKycRecordId());
            this.setSubjectName(otherBean.getSubjectName());
            this.setSubjectRole(otherBean.getSubjectRole());
            this.setSource(otherBean.getSource());
            this.setEntryReference(otherBean.getEntryReference());
            this.setEntryName(otherBean.getEntryName());
            this.setScore(otherBean.getScore());
            this.setStatus(otherBean.getStatus());
            this.setListVersion(otherBean.getListVersion());
            this.setReviewedBy(otherBean.getReviewedBy());
            this.setReviewedAt(otherBean.getReviewedAt());
            this.setReviewNotes(otherBean.getReviewNotes());
        }
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.kycRecordId
     * Get the kycRecordId Attribute
     * @return kycRecordId String
     */
    public String getKycRecordId()
    {
        return this.kycRecordId;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.kycRecordId
     * @param value String
     */
    public void setKycRecordId(final String value)
    {
        this.kycRecordId = value;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.subjectName
     * Get the subjectName Attribute
     * @return subjectName String
     */
    public String getSubjectName()
    {
        return this.subjectName;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.subjectName
     * @param value String
     */
    public void setSubjectName(final String value)
    {
        this.subjectName = value;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.subjectRole
     * Get the subjectRole Attribute
     * @return subjectRole String
     */
    public String getSubjectRole()
    {
        return this.subjectRole;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.subjectRole
     * @param value String
     */
    public void setSubjectRole(final String value)
    {
        this.subjectRole = value;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.source
     * Get the source Attribute
     * @return source ScreeningListSource
     */
    public ScreeningListSource getSource()
    {
        return this.source;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.source
     * @param value ScreeningListSource
     */
    public void setSource(final ScreeningListSource value)
    {
        this.source = value;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.entryReference
     * Get the entryReference Attribute
     * @return entryReference String
     */
    public String getEntryReference()
    {
        return this.entryReference;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.entryReference
     * @param value String
     */
    public void setEntryReference(final String value)
    {
        this.entryReference = value;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.entryName
     * Get the entryName Attribute
     * @return entryName String
     */
    public String getEntryName()
    {
        return this.entryName;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.entryName
     * @param value String
     */
    public void setEntryName(final String value)
    {
        this.entryName = value;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.score
     * Get the score Attribute
     * @return score Double
     */
    public Double getScore()
    {
        return this.score;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.score
     * @param value Double
     */
    public void setScore(final Double value)
    {
        this.score = value;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.status
     * Get the status Attribute
     * @return status ScreeningHitStatus
     */
    public ScreeningHitStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.status
     * @param value ScreeningHitStatus
     */
    public void setStatus(final ScreeningHitStatus value)
    {
        this.status = value;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.listVersion
     * Get the listVersion Attribute
     * @return listVersion String
     */
    public String getListVersion()
    {
        return this.listVersion;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.listVersion
     * @param value String
     */
    public void setListVersion(final String value)
    {
        this.listVersion = value;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.reviewedBy
     * Get the reviewedBy Attribute
     * @return reviewedBy String
     */
    public String getReviewedBy()
    {
        return this.reviewedBy;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.reviewedBy
     * @param value String
     */
    public void setReviewedBy(final String value)
    {
        this.reviewedBy = value;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.reviewedAt
     * Get the reviewedAt Attribute
     * @return reviewedAt LocalDateTime
     */
    public LocalDateTime getReviewedAt()
    {
        return this.reviewedAt;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.reviewedAt
     * @param value LocalDateTime
     */
    public void setReviewedAt(final LocalDateTime value)
    {
        this.reviewedAt = value;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.reviewNotes
     * Get the reviewNotes Attribute
     * @return reviewNotes String
     */
    public String getReviewNotes()
    {
        return this.reviewNotes;
    }

    /**
     * TODO: Model Documentation for ScreeningHitDTO.reviewNotes
     * @param value String
     */
    public void setReviewNotes(final String value)
    {
        this.reviewNotes = value;
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
        ScreeningHitDTO rhs = (ScreeningHitDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getKycRecordId(), rhs.getKycRecordId())
            .append(this.getSubjectName(), rhs.getSubjectName())
            .append(this.getSubjectRole(), rhs.getSubjectRole())
            .append(this.getSource(), rhs.getSource())
            .append(this.getEntryReference(), rhs.getEntryReference())
            .append(this.getEntryName(), rhs.getEntryName())
            .append(this.getScore(), rhs.getScore())
            .append(this.getStatus(), rhs.getStatus())
            .append(this.getListVersion(), rhs.getListVersion())
            .append(this.getReviewedBy(), rhs.getReviewedBy())
            .append(this.getReviewedAt(), rhs.getReviewedAt())
            .append(this.getReviewNotes(), rhs.getReviewNotes())
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
        if (!(object instanceof ScreeningHitDTO))
        {
            return -1;
        }
        ScreeningHitDTO myClass = (ScreeningHitDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getKycRecordId(), myClass.getKycRecordId())
            .append(this.getSubjectName(), myClass.getSubjectName())
            .append(this.getSubjectRole(), myClass.getSubjectRole())
            .append(this.getSource(), myClass.getSource())
            .append(this.getEntryReference(), myClass.getEntryReference())
            .append(this.getEntryName(), myClass.getEntryName())
            .append(this.getScore(), myClass.getScore())
            .append(this.getStatus(), myClass.getStatus())
            .append(this.getListVersion(), myClass.getListVersion())
            .append(this.getReviewedBy(), myClass.getReviewedBy())
            .append(this.getReviewedAt(), myClass.getReviewedAt())
            .append(this.getReviewNotes(), myClass.getReviewNotes())
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
            .append(this.getKycRecordId())
            .append(this.getSubjectName())
            .append(this.getSubjectRole())
            .append(this.getSource())
            .append(this.getEntryReference())
            .append(this.getEntryName())
            .append(this.getScore())
            .append(this.getStatus())
            .append(this.getListVersion())
            .append(this.getReviewedBy())
            .append(this.getReviewedAt())
            .append(this.getReviewNotes())
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
        builder.append("\t\"kycRecordId\" : \"" + this.getKycRecordId() + "\"");
        builder.append("\t,\"subjectName\" : \"" + this.getSubjectName() + "\"");
        builder.append("\t,\"subjectRole\" : \"" + this.getSubjectRole() + "\"");
        builder.append("\t,\"source\" : \"" + this.getSource() + "\"");
        builder.append("\t,\"entryReference\" : \"" + this.getEntryReference() + "\"");
        builder.append("\t,\"entryName\" : \"" + this.getEntryName() + "\"");
        builder.append("\t,\"score\" : \"" + this.getScore() + "\"");
        builder.append("\t,\"status\" : \"" + this.getStatus() + "\"");
        builder.append("\t,\"listVersion\" : \"" + this.getListVersion() + "\"");
        builder.append("\t,\"reviewedBy\" : \"" + this.getReviewedBy() + "\"");
        builder.append("\t,\"reviewedAt\" : \"" + this.getReviewedAt() + "\"");
        builder.append("\t,\"reviewNotes\" : \"" + this.getReviewNotes() + "\"");
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
        final ScreeningHitDTO that = (ScreeningHitDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getKycRecordId(), that.getKycRecordId())
            && equal(this.getSubjectName(), that.getSubjectName())
            && equal(this.getSubjectRole(), that.getSubjectRole())
            && equal(this.getSource(), that.getSource())
            && equal(this.getEntryReference(), that.getEntryReference())
            && equal(this.getEntryName(), that.getEntryName())
            && equal(this.getScore(), that.getScore())
            && equal(this.getStatus(), that.getStatus())
            && equal(this.getListVersion(), that.getListVersion())
            && equal(this.getReviewedBy(), that.getReviewedBy())
            && equal(this.getReviewedAt(), that.getReviewedAt())
            && equal(this.getReviewNotes(), that.getReviewNotes())
        ;
    }

}