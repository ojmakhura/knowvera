package bw.co.roguesystems.ai.processor.field;

import bw.co.roguesystems.ai.AuditableDTO;
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
     * ExpectedFieldDTO.dataProcessorId
     */
    @Nullable
    protected String dataProcessorId;
    /**
     * TODO: Model Documentation for
     * ExpectedFieldDTO.dataProcessor
     */
    @Nullable
    protected String dataProcessor;
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

    /** Default Constructor with no properties */
    public ExpectedFieldDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param fieldIn String TODO: Model Documentation for ExpectedFieldDTO.field
     * @param fieldTypeIn ExpectedFieldType TODO: Model Documentation for ExpectedFieldDTO.fieldType
     */
    public ExpectedFieldDTO(final String fieldIn, final ExpectedFieldType fieldTypeIn)
    {
        super();
        this.field = fieldIn;
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
     * @param dataProcessorIdIn String
     * @param dataProcessorIn String
     * @param manyIn Boolean
     * @param fieldTypeIn ExpectedFieldType
     * @param fieldLabelIn String
     */
    public ExpectedFieldDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final String fieldIn, final Boolean mandatoryIn, final String formatIn, final String dataProcessorIdIn, final String dataProcessorIn, final Boolean manyIn, final ExpectedFieldType fieldTypeIn, final String fieldLabelIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.field = fieldIn;
        this.mandatory = mandatoryIn;
        this.format = formatIn;
        this.dataProcessorId = dataProcessorIdIn;
        this.dataProcessor = dataProcessorIn;
        this.many = manyIn;
        this.fieldType = fieldTypeIn;
        this.fieldLabel = fieldLabelIn;
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
        this.dataProcessorId = otherBean.getDataProcessorId();
        this.dataProcessor = otherBean.getDataProcessor();
        this.many = otherBean.getMany();
        this.fieldType = otherBean.getFieldType();
        this.fieldLabel = otherBean.getFieldLabel();
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
            this.setDataProcessorId(otherBean.getDataProcessorId());
            this.setDataProcessor(otherBean.getDataProcessor());
            this.setMany(otherBean.getMany());
            this.setFieldType(otherBean.getFieldType());
            this.setFieldLabel(otherBean.getFieldLabel());
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
     * ExpectedFieldDTO.dataProcessorId
     * Get the dataProcessorId Attribute
     * @return dataProcessorId String
     */
    public String getDataProcessorId()
    {
        return this.dataProcessorId;
    }

    /**
     * TODO: Model Documentation for
     * ExpectedFieldDTO.dataProcessorId
     * @param value String
     */
    public void setDataProcessorId(final String value)
    {
        this.dataProcessorId = value;
    }

    /**
     * TODO: Model Documentation for
     * ExpectedFieldDTO.dataProcessor
     * Get the dataProcessor Attribute
     * @return dataProcessor String
     */
    public String getDataProcessor()
    {
        return this.dataProcessor;
    }

    /**
     * TODO: Model Documentation for
     * ExpectedFieldDTO.dataProcessor
     * @param value String
     */
    public void setDataProcessor(final String value)
    {
        this.dataProcessor = value;
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
            .append(this.getDataProcessorId(), rhs.getDataProcessorId())
            .append(this.getDataProcessor(), rhs.getDataProcessor())
            .append(this.getMany(), rhs.getMany())
            .append(this.getFieldType(), rhs.getFieldType())
            .append(this.getFieldLabel(), rhs.getFieldLabel())
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
            .append(this.getDataProcessorId(), myClass.getDataProcessorId())
            .append(this.getDataProcessor(), myClass.getDataProcessor())
            .append(this.getMany(), myClass.getMany())
            .append(this.getFieldType(), myClass.getFieldType())
            .append(this.getFieldLabel(), myClass.getFieldLabel())
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
            .append(this.getDataProcessorId())
            .append(this.getDataProcessor())
            .append(this.getMany())
            .append(this.getFieldType())
            .append(this.getFieldLabel())
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
        builder.append("\t,\"dataProcessorId\" : \"" + this.getDataProcessorId() + "\"");
        builder.append("\t,\"dataProcessor\" : \"" + this.getDataProcessor() + "\"");
        builder.append("\t,\"many\" : \"" + this.getMany() + "\"");
        builder.append("\t,\"fieldType\" : \"" + this.getFieldType() + "\"");
        builder.append("\t,\"fieldLabel\" : \"" + this.getFieldLabel() + "\"");
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
            && equal(this.getDataProcessorId(), that.getDataProcessorId())
            && equal(this.getDataProcessor(), that.getDataProcessor())
            && equal(this.getMany(), that.getMany())
            && equal(this.getFieldType(), that.getFieldType())
            && equal(this.getFieldLabel(), that.getFieldLabel())
        ;
    }

}