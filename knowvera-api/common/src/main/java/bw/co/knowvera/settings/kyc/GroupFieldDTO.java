package bw.co.knowvera.settings.kyc;

import bw.co.knowvera.AuditableDTO;
import java.time.LocalDateTime;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for GroupFieldDTO
 */
public class GroupFieldDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -4237450014268255026L;

    // Class attributes
    /**
     * TODO: Model Documentation for GroupFieldDTO.position
     */
    @Nullable
    protected Integer position;
    /**
     * TODO: Model Documentation for GroupFieldDTO.fieldGroupId
     */
    @Nullable
    protected String fieldGroupId;
    /**
     * TODO: Model Documentation for GroupFieldDTO.fieldId
     */
    protected String fieldId;
    /**
     * TODO: Model Documentation for GroupFieldDTO.field
     */
    protected String field;

    /** Default Constructor with no properties */
    public GroupFieldDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param fieldIdIn String TODO: Model Documentation for GroupFieldDTO.fieldId
     * @param fieldIn String TODO: Model Documentation for GroupFieldDTO.field
     */
    public GroupFieldDTO(final String fieldIdIn, final String fieldIn)
    {
        super();
        this.fieldId = fieldIdIn;
        this.field = fieldIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param positionIn Integer
     * @param fieldGroupIdIn String
     * @param fieldIdIn String
     * @param fieldIn String
     */
    public GroupFieldDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final Integer positionIn, final String fieldGroupIdIn, final String fieldIdIn, final String fieldIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.position = positionIn;
        this.fieldGroupId = fieldGroupIdIn;
        this.fieldId = fieldIdIn;
        this.field = fieldIn;
    }

    /**
     * Copies constructor from other GroupFieldDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public GroupFieldDTO(final GroupFieldDTO otherBean)
    {
        super(otherBean);
        this.position = otherBean.getPosition();
        this.fieldGroupId = otherBean.getFieldGroupId();
        this.fieldId = otherBean.getFieldId();
        this.field = otherBean.getField();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final GroupFieldDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setPosition(otherBean.getPosition());
            this.setFieldGroupId(otherBean.getFieldGroupId());
            this.setFieldId(otherBean.getFieldId());
            this.setField(otherBean.getField());
        }
    }

    /**
     * TODO: Model Documentation for GroupFieldDTO.position
     * Get the position Attribute
     * @return position Integer
     */
    public Integer getPosition()
    {
        return this.position;
    }

    /**
     * TODO: Model Documentation for GroupFieldDTO.position
     * @param value Integer
     */
    public void setPosition(final Integer value)
    {
        this.position = value;
    }

    /**
     * TODO: Model Documentation for GroupFieldDTO.fieldGroupId
     * Get the fieldGroupId Attribute
     * @return fieldGroupId String
     */
    public String getFieldGroupId()
    {
        return this.fieldGroupId;
    }

    /**
     * TODO: Model Documentation for GroupFieldDTO.fieldGroupId
     * @param value String
     */
    public void setFieldGroupId(final String value)
    {
        this.fieldGroupId = value;
    }

    /**
     * TODO: Model Documentation for GroupFieldDTO.fieldId
     * Get the fieldId Attribute
     * @return fieldId String
     */
    public String getFieldId()
    {
        return this.fieldId;
    }

    /**
     * TODO: Model Documentation for GroupFieldDTO.fieldId
     * @param value String
     */
    public void setFieldId(final String value)
    {
        this.fieldId = value;
    }

    /**
     * TODO: Model Documentation for GroupFieldDTO.field
     * Get the field Attribute
     * @return field String
     */
    public String getField()
    {
        return this.field;
    }

    /**
     * TODO: Model Documentation for GroupFieldDTO.field
     * @param value String
     */
    public void setField(final String value)
    {
        this.field = value;
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
        GroupFieldDTO rhs = (GroupFieldDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getPosition(), rhs.getPosition())
            .append(this.getFieldGroupId(), rhs.getFieldGroupId())
            .append(this.getFieldId(), rhs.getFieldId())
            .append(this.getField(), rhs.getField())
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
        if (!(object instanceof GroupFieldDTO))
        {
            return -1;
        }
        GroupFieldDTO myClass = (GroupFieldDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getPosition(), myClass.getPosition())
            .append(this.getFieldGroupId(), myClass.getFieldGroupId())
            .append(this.getFieldId(), myClass.getFieldId())
            .append(this.getField(), myClass.getField())
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
            .append(this.getPosition())
            .append(this.getFieldGroupId())
            .append(this.getFieldId())
            .append(this.getField())
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
        builder.append("\t\"position\" : \"" + this.getPosition() + "\"");
        builder.append("\t,\"fieldGroupId\" : \"" + this.getFieldGroupId() + "\"");
        builder.append("\t,\"fieldId\" : \"" + this.getFieldId() + "\"");
        builder.append("\t,\"field\" : \"" + this.getField() + "\"");
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
        final GroupFieldDTO that = (GroupFieldDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getPosition(), that.getPosition())
            && equal(this.getFieldGroupId(), that.getFieldGroupId())
            && equal(this.getFieldId(), that.getFieldId())
            && equal(this.getField(), that.getField())
        ;
    }

}