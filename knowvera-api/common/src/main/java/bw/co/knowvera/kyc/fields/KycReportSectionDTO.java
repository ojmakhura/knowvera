package bw.co.knowvera.kyc.fields;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for KycReportSectionDTO
 */
public class KycReportSectionDTO
    implements Serializable, Comparable<KycReportSectionDTO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 5598061031905602786L;

    // Class attributes
    /**
     * TODO: Model Documentation for KycReportSectionDTO.label
     */
    protected String label;
    /**
     * TODO: Model Documentation for KycReportSectionDTO.position
     */
    @Nullable
    protected Integer position;
    /**
     * TODO: Model Documentation for KycReportSectionDTO.id
     */
    protected String id;
    /**
     * TODO: Model Documentation for KycReportSectionDTO.fieldValues
     */
    @Nullable
    protected List<GroupFieldValueDTO> fieldValues;
    /**
     * TODO: Model Documentation for KycReportSectionDTO.kycRecordId
     */
    protected String kycRecordId;
    /**
     * TODO: Model Documentation for KycReportSectionDTO.kycRecordRef
     */
    protected String kycRecordRef;

    /** Default Constructor with no properties */
    public KycReportSectionDTO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param labelIn String TODO: Model Documentation for KycReportSectionDTO.label
     * @param idIn String TODO: Model Documentation for KycReportSectionDTO.id
     * @param kycRecordIdIn String TODO: Model Documentation for KycReportSectionDTO.kycRecordId
     * @param kycRecordRefIn String TODO: Model Documentation for KycReportSectionDTO.kycRecordRef
     */
    public KycReportSectionDTO(final String labelIn, final String idIn, final String kycRecordIdIn, final String kycRecordRefIn)
    {
        this.label = labelIn;
        this.id = idIn;
        this.kycRecordId = kycRecordIdIn;
        this.kycRecordRef = kycRecordRefIn;
    }

    /**
     * Constructor with all properties
     * @param labelIn String
     * @param positionIn Integer
     * @param idIn String
     * @param fieldValuesIn List<GroupFieldValueDTO>
     * @param kycRecordIdIn String
     * @param kycRecordRefIn String
     */
    public KycReportSectionDTO(final String labelIn, final Integer positionIn, final String idIn, final List<GroupFieldValueDTO> fieldValuesIn, final String kycRecordIdIn, final String kycRecordRefIn)
    {
        this.label = labelIn;
        this.position = positionIn;
        this.id = idIn;
        this.fieldValues = fieldValuesIn;
        this.kycRecordId = kycRecordIdIn;
        this.kycRecordRef = kycRecordRefIn;
    }

    /**
     * Copies constructor from other KycReportSectionDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public KycReportSectionDTO(final KycReportSectionDTO otherBean)
    {
        this.label = otherBean.getLabel();
        this.position = otherBean.getPosition();
        this.id = otherBean.getId();
        this.fieldValues = otherBean.getFieldValues();
        this.kycRecordId = otherBean.getKycRecordId();
        this.kycRecordRef = otherBean.getKycRecordRef();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final KycReportSectionDTO otherBean)
    {
        if (null != otherBean)
        {
            this.setLabel(otherBean.getLabel());
            this.setPosition(otherBean.getPosition());
            this.setId(otherBean.getId());
            this.setFieldValues(otherBean.getFieldValues());
            this.setKycRecordId(otherBean.getKycRecordId());
            this.setKycRecordRef(otherBean.getKycRecordRef());
        }
    }

    /**
     * TODO: Model Documentation for KycReportSectionDTO.label
     * Get the label Attribute
     * @return label String
     */
    public String getLabel()
    {
        return this.label;
    }

    /**
     * TODO: Model Documentation for KycReportSectionDTO.label
     * @param value String
     */
    public void setLabel(final String value)
    {
        this.label = value;
    }

    /**
     * TODO: Model Documentation for KycReportSectionDTO.position
     * Get the position Attribute
     * @return position Integer
     */
    public Integer getPosition()
    {
        return this.position;
    }

    /**
     * TODO: Model Documentation for KycReportSectionDTO.position
     * @param value Integer
     */
    public void setPosition(final Integer value)
    {
        this.position = value;
    }

    /**
     * TODO: Model Documentation for KycReportSectionDTO.id
     * Get the id Attribute
     * @return id String
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for KycReportSectionDTO.id
     * @param value String
     */
    public void setId(final String value)
    {
        this.id = value;
    }

    /**
     * TODO: Model Documentation for KycReportSectionDTO.fieldValues
     * Get the fieldValues Attribute
     * @return fieldValues List<GroupFieldValueDTO>
     */
    public List<GroupFieldValueDTO> getFieldValues()
    {
        if (this.fieldValues == null)
        {
            this.fieldValues = new ArrayList<GroupFieldValueDTO>();
        }
        return this.fieldValues;
    }

    /**
     * TODO: Model Documentation for KycReportSectionDTO.fieldValues
     * @param value List<GroupFieldValueDTO>
     */
    public void setFieldValues(final List<GroupFieldValueDTO> value)
    {
        this.fieldValues = value;
    }

    /**
     * TODO: Model Documentation for KycReportSectionDTO.kycRecordId
     * Get the kycRecordId Attribute
     * @return kycRecordId String
     */
    public String getKycRecordId()
    {
        return this.kycRecordId;
    }

    /**
     * TODO: Model Documentation for KycReportSectionDTO.kycRecordId
     * @param value String
     */
    public void setKycRecordId(final String value)
    {
        this.kycRecordId = value;
    }

    /**
     * TODO: Model Documentation for KycReportSectionDTO.kycRecordRef
     * Get the kycRecordRef Attribute
     * @return kycRecordRef String
     */
    public String getKycRecordRef()
    {
        return this.kycRecordRef;
    }

    /**
     * TODO: Model Documentation for KycReportSectionDTO.kycRecordRef
     * @param value String
     */
    public void setKycRecordRef(final String value)
    {
        this.kycRecordRef = value;
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
        KycReportSectionDTO rhs = (KycReportSectionDTO) object;
        return new EqualsBuilder()
            .append(this.getLabel(), rhs.getLabel())
            .append(this.getPosition(), rhs.getPosition())
            .append(this.getId(), rhs.getId())
            .append(this.getFieldValues(), rhs.getFieldValues())
            .append(this.getKycRecordId(), rhs.getKycRecordId())
            .append(this.getKycRecordRef(), rhs.getKycRecordRef())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final KycReportSectionDTO object)
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
            .append(this.getLabel(), object.getLabel())
            .append(this.getPosition(), object.getPosition())
            .append(this.getId(), object.getId())
            .append(this.getFieldValues(), object.getFieldValues())
            .append(this.getKycRecordId(), object.getKycRecordId())
            .append(this.getKycRecordRef(), object.getKycRecordRef())
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
            .append(this.getLabel())
            .append(this.getPosition())
            .append(this.getId())
            .append(this.getFieldValues())
            .append(this.getKycRecordId())
            .append(this.getKycRecordRef())
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
        builder.append("\t\"label\" : \"" + this.getLabel() + "\"");
        builder.append("\t,\"position\" : \"" + this.getPosition() + "\"");
        builder.append("\t,\"id\" : \"" + this.getId() + "\"");
        builder.append("\t,\"fieldValues\" : \"" + this.getFieldValues() + "\"");
        builder.append("\t,\"kycRecordId\" : \"" + this.getKycRecordId() + "\"");
        builder.append("\t,\"kycRecordRef\" : \"" + this.getKycRecordRef() + "\"");
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

        final KycReportSectionDTO that = (KycReportSectionDTO)thatObject;
        return
            equal(this.getLabel(), that.getLabel())
            && equal(this.getPosition(), that.getPosition())
            && equal(this.getId(), that.getId())
            && equal(this.getFieldValues(), that.getFieldValues())
            && equal(this.getKycRecordId(), that.getKycRecordId())
            && equal(this.getKycRecordRef(), that.getKycRecordRef())
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