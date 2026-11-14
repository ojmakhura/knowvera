package bw.co.knowvera.document.type.field;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.TargetEntity;
import java.time.LocalDateTime;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for ExpectedFieldDTO
 */
public class ExpectedFieldDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -4540857702170190957L;

    // Class attributes
    /**
     * TODO: Model Documentation for ExpectedFieldDTO.field
     */
    protected String field;
    /**
     * TODO: Model Documentation for ExpectedFieldDTO.mandatory
     */
    @Nullable
    protected Boolean mandatory = false;
    /**
     * TODO: Model Documentation for ExpectedFieldDTO.format
     */
    @Nullable
    protected String format;
    /**
     * TODO: Model Documentation for
     * ExpectedFieldDTO.documentTypeId
     */
    @Nullable
    protected String documentTypeId;
    /**
     * TODO: Model Documentation for
     * ExpectedFieldDTO.documentType
     */
    @Nullable
    protected String documentType;
    /**
     * TODO: Model Documentation for ExpectedFieldDTO.matchTo
     */
    @Nullable
    protected String matchTo;
    /**
     * TODO: Model Documentation for ExpectedFieldDTO.targetType
     */
    protected TargetEntity targetType;
    /**
     * TODO: Model Documentation for ExpectedFieldDTO.many
     */
    @Nullable
    protected Boolean many = false;
    /**
     * TODO: Model Documentation for ExpectedFieldDTO.fieldType
     */
    protected ExpectedFieldType fieldType;
    /**
     * TODO: Model Documentation for ExpectedFieldDTO.fieldLabel
     */
    @Nullable
    protected String fieldLabel;
    /**
     * TODO: Model Documentation for ExpectedFieldDTO.exactMatch
     */
    @Nullable
    protected Boolean exactMatch = false;

    /** Default Constructor with no properties */
    public ExpectedFieldDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param fieldIn String TODO: Model Documentation for ExpectedFieldDTO.field
     * @param targetTypeIn TargetEntity TODO: Model Documentation for ExpectedFieldDTO.targetType
     * @param fieldTypeIn ExpectedFieldType TODO: Model Documentation for ExpectedFieldDTO.fieldType
     */
    public ExpectedFieldDTO(final String fieldIn, final TargetEntity targetTypeIn, final ExpectedFieldType fieldTypeIn)
    {
        super();
        this.field = fieldIn;
        this.targetType = targetTypeIn;
        this.fieldType = fieldTypeIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param fieldIn String
     * @param mandatoryIn Boolean
     * @param formatIn String
     * @param documentTypeIdIn String
     * @param documentTypeIn String
     * @param matchToIn String
     * @param targetTypeIn TargetEntity
     * @param manyIn Boolean
     * @param fieldTypeIn ExpectedFieldType
     * @param fieldLabelIn String
     * @param exactMatchIn Boolean
     */
    public ExpectedFieldDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final String fieldIn, final Boolean mandatoryIn, final String formatIn, final String documentTypeIdIn, final String documentTypeIn, final String matchToIn, final TargetEntity targetTypeIn, final Boolean manyIn, final ExpectedFieldType fieldTypeIn, final String fieldLabelIn, final Boolean exactMatchIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.field = fieldIn;
        this.mandatory = mandatoryIn;
        this.format = formatIn;
        this.documentTypeId = documentTypeIdIn;
        this.documentType = documentTypeIn;
        this.matchTo = matchToIn;
        this.targetType = targetTypeIn;
        this.many = manyIn;
        this.fieldType = fieldTypeIn;
        this.fieldLabel = fieldLabelIn;
        this.exactMatch = exactMatchIn;
    }

    /**
     * Copies constructor from other ExpectedFieldDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public ExpectedFieldDTO(final ExpectedFieldDTO otherBean)
    {
        super(otherBean);
        this.field = otherBean.getField();
        this.mandatory = otherBean.getMandatory();
        this.format = otherBean.getFormat();
        this.documentTypeId = otherBean.getDocumentTypeId();
        this.documentType = otherBean.getDocumentType();
        this.matchTo = otherBean.getMatchTo();
        this.targetType = otherBean.getTargetType();
        this.many = otherBean.getMany();
        this.fieldType = otherBean.getFieldType();
        this.fieldLabel = otherBean.getFieldLabel();
        this.exactMatch = otherBean.getExactMatch();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final ExpectedFieldDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setField(otherBean.getField());
            this.setMandatory(otherBean.getMandatory());
            this.setFormat(otherBean.getFormat());
            this.setDocumentTypeId(otherBean.getDocumentTypeId());
            this.setDocumentType(otherBean.getDocumentType());
            this.setMatchTo(otherBean.getMatchTo());
            this.setTargetType(otherBean.getTargetType());
            this.setMany(otherBean.getMany());
            this.setFieldType(otherBean.getFieldType());
            this.setFieldLabel(otherBean.getFieldLabel());
            this.setExactMatch(otherBean.getExactMatch());
        }
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.field
     * Get the field Attribute
     * @return field String
     */
    public String getField()
    {
        return this.field;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.field
     * @param value String
     */
    public void setField(final String value)
    {
        this.field = value;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.mandatory
     * Get the mandatory Attribute
     * @return mandatory Boolean
     */
    public Boolean getMandatory()
    {
        return this.mandatory;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.mandatory
     * @param value Boolean
     */
    public void setMandatory(final Boolean value)
    {
        this.mandatory = value;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.format
     * Get the format Attribute
     * @return format String
     */
    public String getFormat()
    {
        return this.format;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.format
     * @param value String
     */
    public void setFormat(final String value)
    {
        this.format = value;
    }

    /**
     * TODO: Model Documentation for
     * ExpectedFieldDTO.documentTypeId
     * Get the documentTypeId Attribute
     * @return documentTypeId String
     */
    public String getDocumentTypeId()
    {
        return this.documentTypeId;
    }

    /**
     * TODO: Model Documentation for
     * ExpectedFieldDTO.documentTypeId
     * @param value String
     */
    public void setDocumentTypeId(final String value)
    {
        this.documentTypeId = value;
    }

    /**
     * TODO: Model Documentation for
     * ExpectedFieldDTO.documentType
     * Get the documentType Attribute
     * @return documentType String
     */
    public String getDocumentType()
    {
        return this.documentType;
    }

    /**
     * TODO: Model Documentation for
     * ExpectedFieldDTO.documentType
     * @param value String
     */
    public void setDocumentType(final String value)
    {
        this.documentType = value;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.matchTo
     * Get the matchTo Attribute
     * @return matchTo String
     */
    public String getMatchTo()
    {
        return this.matchTo;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.matchTo
     * @param value String
     */
    public void setMatchTo(final String value)
    {
        this.matchTo = value;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.targetType
     * Get the targetType Attribute
     * @return targetType TargetEntity
     */
    public TargetEntity getTargetType()
    {
        return this.targetType;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.targetType
     * @param value TargetEntity
     */
    public void setTargetType(final TargetEntity value)
    {
        this.targetType = value;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.many
     * Get the many Attribute
     * @return many Boolean
     */
    public Boolean getMany()
    {
        return this.many;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.many
     * @param value Boolean
     */
    public void setMany(final Boolean value)
    {
        this.many = value;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.fieldType
     * Get the fieldType Attribute
     * @return fieldType ExpectedFieldType
     */
    public ExpectedFieldType getFieldType()
    {
        return this.fieldType;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.fieldType
     * @param value ExpectedFieldType
     */
    public void setFieldType(final ExpectedFieldType value)
    {
        this.fieldType = value;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.fieldLabel
     * Get the fieldLabel Attribute
     * @return fieldLabel String
     */
    public String getFieldLabel()
    {
        return this.fieldLabel;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.fieldLabel
     * @param value String
     */
    public void setFieldLabel(final String value)
    {
        this.fieldLabel = value;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.exactMatch
     * Get the exactMatch Attribute
     * @return exactMatch Boolean
     */
    public Boolean getExactMatch()
    {
        return this.exactMatch;
    }

    /**
     * TODO: Model Documentation for ExpectedFieldDTO.exactMatch
     * @param value Boolean
     */
    public void setExactMatch(final Boolean value)
    {
        this.exactMatch = value;
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
        ExpectedFieldDTO rhs = (ExpectedFieldDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getField(), rhs.getField())
            .append(this.getMandatory(), rhs.getMandatory())
            .append(this.getFormat(), rhs.getFormat())
            .append(this.getDocumentTypeId(), rhs.getDocumentTypeId())
            .append(this.getDocumentType(), rhs.getDocumentType())
            .append(this.getMatchTo(), rhs.getMatchTo())
            .append(this.getTargetType(), rhs.getTargetType())
            .append(this.getMany(), rhs.getMany())
            .append(this.getFieldType(), rhs.getFieldType())
            .append(this.getFieldLabel(), rhs.getFieldLabel())
            .append(this.getExactMatch(), rhs.getExactMatch())
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
        if (!(object instanceof ExpectedFieldDTO))
        {
            return -1;
        }
        ExpectedFieldDTO myClass = (ExpectedFieldDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getField(), myClass.getField())
            .append(this.getMandatory(), myClass.getMandatory())
            .append(this.getFormat(), myClass.getFormat())
            .append(this.getDocumentTypeId(), myClass.getDocumentTypeId())
            .append(this.getDocumentType(), myClass.getDocumentType())
            .append(this.getMatchTo(), myClass.getMatchTo())
            .append(this.getTargetType(), myClass.getTargetType())
            .append(this.getMany(), myClass.getMany())
            .append(this.getFieldType(), myClass.getFieldType())
            .append(this.getFieldLabel(), myClass.getFieldLabel())
            .append(this.getExactMatch(), myClass.getExactMatch())
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
            .append(this.getField())
            .append(this.getMandatory())
            .append(this.getFormat())
            .append(this.getDocumentTypeId())
            .append(this.getDocumentType())
            .append(this.getMatchTo())
            .append(this.getTargetType())
            .append(this.getMany())
            .append(this.getFieldType())
            .append(this.getFieldLabel())
            .append(this.getExactMatch())
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
        builder.append("\t\"field\" : \"" + this.getField() + "\"");
        builder.append("\t,\"mandatory\" : \"" + this.getMandatory() + "\"");
        builder.append("\t,\"format\" : \"" + this.getFormat() + "\"");
        builder.append("\t,\"documentTypeId\" : \"" + this.getDocumentTypeId() + "\"");
        builder.append("\t,\"documentType\" : \"" + this.getDocumentType() + "\"");
        builder.append("\t,\"matchTo\" : \"" + this.getMatchTo() + "\"");
        builder.append("\t,\"targetType\" : \"" + this.getTargetType() + "\"");
        builder.append("\t,\"many\" : \"" + this.getMany() + "\"");
        builder.append("\t,\"fieldType\" : \"" + this.getFieldType() + "\"");
        builder.append("\t,\"fieldLabel\" : \"" + this.getFieldLabel() + "\"");
        builder.append("\t,\"exactMatch\" : \"" + this.getExactMatch() + "\"");
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
        final ExpectedFieldDTO that = (ExpectedFieldDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getField(), that.getField())
            && equal(this.getMandatory(), that.getMandatory())
            && equal(this.getFormat(), that.getFormat())
            && equal(this.getDocumentTypeId(), that.getDocumentTypeId())
            && equal(this.getDocumentType(), that.getDocumentType())
            && equal(this.getMatchTo(), that.getMatchTo())
            && equal(this.getTargetType(), that.getTargetType())
            && equal(this.getMany(), that.getMany())
            && equal(this.getFieldType(), that.getFieldType())
            && equal(this.getFieldLabel(), that.getFieldLabel())
            && equal(this.getExactMatch(), that.getExactMatch())
        ;
    }

}