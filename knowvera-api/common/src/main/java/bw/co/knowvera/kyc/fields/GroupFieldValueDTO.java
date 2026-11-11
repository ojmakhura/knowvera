package bw.co.knowvera.kyc.fields;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for GroupFieldValueDTO
 */
public class GroupFieldValueDTO
    implements Serializable, Comparable<GroupFieldValueDTO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 7618226410930854928L;

    // Class attributes
    /**
     * TODO: Model Documentation for GroupFieldValueDTO.position
     */
    @Nullable
    protected Integer position;
    /**
     * TODO: Model Documentation for GroupFieldValueDTO.fieldGroupId
     */
    @Nullable
    protected String fieldGroupId;
    /**
     * TODO: Model Documentation for GroupFieldValueDTO.fieldId
     */
    protected String fieldId;
    /**
     * TODO: Model Documentation for GroupFieldValueDTO.field
     */
    protected String field;
    /**
     * TODO: Model Documentation for GroupFieldValueDTO.id
     */
    protected String id;
    /**
     * TODO: Model Documentation for GroupFieldValueDTO.data
     */
    protected ValueData data;
    /**
     * TODO: Model Documentation for GroupFieldValueDTO.reportSectionId
     */
    protected String reportSectionId;
    /**
     * TODO: Model Documentation for GroupFieldValueDTO.fieldLabel
     */
    protected String fieldLabel;
    /**
     * TODO: Model Documentation for GroupFieldValueDTO.mandatory
     */
    @Nullable
    protected Boolean mandatory = false;

    /** Default Constructor with no properties */
    public GroupFieldValueDTO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param fieldIdIn String TODO: Model Documentation for GroupFieldValueDTO.fieldId
     * @param fieldIn String TODO: Model Documentation for GroupFieldValueDTO.field
     * @param idIn String TODO: Model Documentation for GroupFieldValueDTO.id
     * @param dataIn ValueData TODO: Model Documentation for GroupFieldValueDTO.data
     * @param reportSectionIdIn String TODO: Model Documentation for GroupFieldValueDTO.reportSectionId
     * @param fieldLabelIn String TODO: Model Documentation for GroupFieldValueDTO.fieldLabel
     */
    public GroupFieldValueDTO(final String fieldIdIn, final String fieldIn, final String idIn, final ValueData dataIn, final String reportSectionIdIn, final String fieldLabelIn)
    {
        this.fieldId = fieldIdIn;
        this.field = fieldIn;
        this.id = idIn;
        this.data = dataIn;
        this.reportSectionId = reportSectionIdIn;
        this.fieldLabel = fieldLabelIn;
    }

    /**
     * Constructor with all properties
     * @param positionIn Integer
     * @param fieldGroupIdIn String
     * @param fieldIdIn String
     * @param fieldIn String
     * @param idIn String
     * @param dataIn ValueData
     * @param reportSectionIdIn String
     * @param fieldLabelIn String
     * @param mandatoryIn Boolean
     */
    public GroupFieldValueDTO(final Integer positionIn, final String fieldGroupIdIn, final String fieldIdIn, final String fieldIn, final String idIn, final ValueData dataIn, final String reportSectionIdIn, final String fieldLabelIn, final Boolean mandatoryIn)
    {
        this.position = positionIn;
        this.fieldGroupId = fieldGroupIdIn;
        this.fieldId = fieldIdIn;
        this.field = fieldIn;
        this.id = idIn;
        this.data = dataIn;
        this.reportSectionId = reportSectionIdIn;
        this.fieldLabel = fieldLabelIn;
        this.mandatory = mandatoryIn;
    }

    /**
     * Copies constructor from other GroupFieldValueDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public GroupFieldValueDTO(final GroupFieldValueDTO otherBean)
    {
        this.position = otherBean.getPosition();
        this.fieldGroupId = otherBean.getFieldGroupId();
        this.fieldId = otherBean.getFieldId();
        this.field = otherBean.getField();
        this.id = otherBean.getId();
        this.data = otherBean.getData();
        this.reportSectionId = otherBean.getReportSectionId();
        this.fieldLabel = otherBean.getFieldLabel();
        this.mandatory = otherBean.getMandatory();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final GroupFieldValueDTO otherBean)
    {
        if (null != otherBean)
        {
            this.setPosition(otherBean.getPosition());
            this.setFieldGroupId(otherBean.getFieldGroupId());
            this.setFieldId(otherBean.getFieldId());
            this.setField(otherBean.getField());
            this.setId(otherBean.getId());
            this.setData(otherBean.getData());
            this.setReportSectionId(otherBean.getReportSectionId());
            this.setFieldLabel(otherBean.getFieldLabel());
            this.setMandatory(otherBean.getMandatory());
        }
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.position
     * Get the position Attribute
     * @return position Integer
     */
    public Integer getPosition()
    {
        return this.position;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.position
     * @param value Integer
     */
    public void setPosition(final Integer value)
    {
        this.position = value;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.fieldGroupId
     * Get the fieldGroupId Attribute
     * @return fieldGroupId String
     */
    public String getFieldGroupId()
    {
        return this.fieldGroupId;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.fieldGroupId
     * @param value String
     */
    public void setFieldGroupId(final String value)
    {
        this.fieldGroupId = value;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.fieldId
     * Get the fieldId Attribute
     * @return fieldId String
     */
    public String getFieldId()
    {
        return this.fieldId;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.fieldId
     * @param value String
     */
    public void setFieldId(final String value)
    {
        this.fieldId = value;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.field
     * Get the field Attribute
     * @return field String
     */
    public String getField()
    {
        return this.field;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.field
     * @param value String
     */
    public void setField(final String value)
    {
        this.field = value;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.id
     * Get the id Attribute
     * @return id String
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.id
     * @param value String
     */
    public void setId(final String value)
    {
        this.id = value;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.data
     * Get the data Attribute
     * @return data ValueData
     */
    public ValueData getData()
    {
        return this.data;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.data
     * @param value ValueData
     */
    public void setData(final ValueData value)
    {
        this.data = value;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.reportSectionId
     * Get the reportSectionId Attribute
     * @return reportSectionId String
     */
    public String getReportSectionId()
    {
        return this.reportSectionId;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.reportSectionId
     * @param value String
     */
    public void setReportSectionId(final String value)
    {
        this.reportSectionId = value;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.fieldLabel
     * Get the fieldLabel Attribute
     * @return fieldLabel String
     */
    public String getFieldLabel()
    {
        return this.fieldLabel;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.fieldLabel
     * @param value String
     */
    public void setFieldLabel(final String value)
    {
        this.fieldLabel = value;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.mandatory
     * Get the mandatory Attribute
     * @return mandatory Boolean
     */
    public Boolean getMandatory()
    {
        return this.mandatory;
    }

    /**
     * TODO: Model Documentation for GroupFieldValueDTO.mandatory
     * @param value Boolean
     */
    public void setMandatory(final Boolean value)
    {
        this.mandatory = value;
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
        GroupFieldValueDTO rhs = (GroupFieldValueDTO) object;
        return new EqualsBuilder()
            .append(this.getPosition(), rhs.getPosition())
            .append(this.getFieldGroupId(), rhs.getFieldGroupId())
            .append(this.getFieldId(), rhs.getFieldId())
            .append(this.getField(), rhs.getField())
            .append(this.getId(), rhs.getId())
            .append(this.getData(), rhs.getData())
            .append(this.getReportSectionId(), rhs.getReportSectionId())
            .append(this.getFieldLabel(), rhs.getFieldLabel())
            .append(this.getMandatory(), rhs.getMandatory())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final GroupFieldValueDTO object)
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
        return new CompareToBuilder()
            .append(this.getPosition(), object.getPosition())
            .append(this.getFieldGroupId(), object.getFieldGroupId())
            .append(this.getFieldId(), object.getFieldId())
            .append(this.getField(), object.getField())
            .append(this.getId(), object.getId())
            .append(this.getData(), object.getData())
            .append(this.getReportSectionId(), object.getReportSectionId())
            .append(this.getFieldLabel(), object.getFieldLabel())
            .append(this.getMandatory(), object.getMandatory())
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
            .append(this.getPosition())
            .append(this.getFieldGroupId())
            .append(this.getFieldId())
            .append(this.getField())
            .append(this.getId())
            .append(this.getData())
            .append(this.getReportSectionId())
            .append(this.getFieldLabel())
            .append(this.getMandatory())
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
        builder.append("\t,\"data\" : \"" + this.getData() + "\"");
        builder.append("\t,\"reportSectionId\" : \"" + this.getReportSectionId() + "\"");
        builder.append("\t,\"fieldLabel\" : \"" + this.getFieldLabel() + "\"");
        builder.append("\t,\"mandatory\" : \"" + this.getMandatory() + "\"");
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
    public boolean equalProperties(final Object thatObject)
    {
        if (thatObject == null || !this.getClass().isAssignableFrom(thatObject.getClass()))
        {
            return false;
        }

        final GroupFieldValueDTO that = (GroupFieldValueDTO)thatObject;
        return
            equal(this.getPosition(), that.getPosition())
            && equal(this.getFieldGroupId(), that.getFieldGroupId())
            && equal(this.getFieldId(), that.getFieldId())
            && equal(this.getField(), that.getField())
            && equal(this.getId(), that.getId())
            && equal(this.getData(), that.getData())
            && equal(this.getReportSectionId(), that.getReportSectionId())
            && equal(this.getFieldLabel(), that.getFieldLabel())
            && equal(this.getMandatory(), that.getMandatory())
        ;
    }

    /**
     * This is a convenient helper method which is able to detect whether or not two values are equal. Two values
     * are equal when they are both {@code null}, are arrays of the same length with equal elements or are
     * equal objects (this includes {@link java.util.Collection} and {@link java.util.Map} instances).
     *
     * <p/>Note that for array, collection or map instances the comparison runs one level deep.
     *
     * @param first the first object to compare, may be {@code null}
     * @param second the second object to compare, may be {@code null}
     * @return this method will return {@code true} in case both objects are equal as explained above;
     *      in all other cases this method will return {@code false}
     */
    protected static boolean equal(final Object first, final Object second)
    {
        final boolean equal;

        if (first == null)
        {
            equal = (second == null);
        }
        else if (first.getClass().isArray() && (second != null) && second.getClass().isArray())
        {
            equal = Arrays.equals((Object[])first, (Object[])second);
        }
        else // note that the following also covers java.util.Collection and java.util.Map
        {
            equal = first.equals(second);
        }

        return equal;
    }

}