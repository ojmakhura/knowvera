package bw.co.knowvera.settings.kyc;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.TargetEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for KycFieldGroupDTO
 */
public class KycFieldGroupDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -5118203440750539146L;

    // Class attributes
    /**
     * TODO: Model Documentation for KycFieldGroupDTO.label
     */
    protected String label;
    /**
     * TODO: Model Documentation for KycFieldGroupDTO.description
     */
    protected String description;
    /**
     * TODO: Model Documentation for KycFieldGroupDTO.groupFields
     */
    @Nullable
    protected List<GroupFieldDTO> groupFields;
    /**
     * TODO: Model Documentation for KycFieldGroupDTO.targetType
     */
    protected TargetEntity targetType;
    /**
     * TODO: Model Documentation for KycFieldGroupDTO.position
     */
    @Nullable
    protected Integer position;
    /**
     * TODO: Model Documentation for
     * KycFieldGroupDTO.individualSettingsId
     */
    @Nullable
    protected String individualSettingsId;
    /**
     * TODO: Model Documentation for
     * KycFieldGroupDTO.organisationSettingsId
     */
    @Nullable
    protected String organisationSettingsId;

    /** Default Constructor with no properties */
    public KycFieldGroupDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param labelIn String TODO: Model Documentation for KycFieldGroupDTO.label
     * @param descriptionIn String TODO: Model Documentation for KycFieldGroupDTO.description
     * @param targetTypeIn TargetEntity TODO: Model Documentation for KycFieldGroupDTO.targetType
     */
    public KycFieldGroupDTO(final String labelIn, final String descriptionIn, final TargetEntity targetTypeIn)
    {
        super();
        this.label = labelIn;
        this.description = descriptionIn;
        this.targetType = targetTypeIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param labelIn String
     * @param descriptionIn String
     * @param groupFieldsIn List<GroupFieldDTO>
     * @param targetTypeIn TargetEntity
     * @param positionIn Integer
     * @param individualSettingsIdIn String
     * @param organisationSettingsIdIn String
     */
    public KycFieldGroupDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final String labelIn, final String descriptionIn, final List<GroupFieldDTO> groupFieldsIn, final TargetEntity targetTypeIn, final Integer positionIn, final String individualSettingsIdIn, final String organisationSettingsIdIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.label = labelIn;
        this.description = descriptionIn;
        this.groupFields = groupFieldsIn;
        this.targetType = targetTypeIn;
        this.position = positionIn;
        this.individualSettingsId = individualSettingsIdIn;
        this.organisationSettingsId = organisationSettingsIdIn;
    }

    /**
     * Copies constructor from other KycFieldGroupDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public KycFieldGroupDTO(final KycFieldGroupDTO otherBean)
    {
        super(otherBean);
        this.label = otherBean.getLabel();
        this.description = otherBean.getDescription();
        this.groupFields = otherBean.getGroupFields();
        this.targetType = otherBean.getTargetType();
        this.position = otherBean.getPosition();
        this.individualSettingsId = otherBean.getIndividualSettingsId();
        this.organisationSettingsId = otherBean.getOrganisationSettingsId();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final KycFieldGroupDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setLabel(otherBean.getLabel());
            this.setDescription(otherBean.getDescription());
            this.setGroupFields(otherBean.getGroupFields());
            this.setTargetType(otherBean.getTargetType());
            this.setPosition(otherBean.getPosition());
            this.setIndividualSettingsId(otherBean.getIndividualSettingsId());
            this.setOrganisationSettingsId(otherBean.getOrganisationSettingsId());
        }
    }

    /**
     * TODO: Model Documentation for KycFieldGroupDTO.label
     * Get the label Attribute
     * @return label String
     */
    public String getLabel()
    {
        return this.label;
    }

    /**
     * TODO: Model Documentation for KycFieldGroupDTO.label
     * @param value String
     */
    public void setLabel(final String value)
    {
        this.label = value;
    }

    /**
     * TODO: Model Documentation for KycFieldGroupDTO.description
     * Get the description Attribute
     * @return description String
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * TODO: Model Documentation for KycFieldGroupDTO.description
     * @param value String
     */
    public void setDescription(final String value)
    {
        this.description = value;
    }

    /**
     * TODO: Model Documentation for KycFieldGroupDTO.groupFields
     * Get the groupFields Attribute
     * @return groupFields List<GroupFieldDTO>
     */
    public List<GroupFieldDTO> getGroupFields()
    {
        if (this.groupFields == null)
        {
            this.groupFields = new ArrayList<GroupFieldDTO>();
        }
        return this.groupFields;
    }

    /**
     * TODO: Model Documentation for KycFieldGroupDTO.groupFields
     * @param value List<GroupFieldDTO>
     */
    public void setGroupFields(final List<GroupFieldDTO> value)
    {
        this.groupFields = value;
    }

    /**
     * TODO: Model Documentation for KycFieldGroupDTO.targetType
     * Get the targetType Attribute
     * @return targetType TargetEntity
     */
    public TargetEntity getTargetType()
    {
        return this.targetType;
    }

    /**
     * TODO: Model Documentation for KycFieldGroupDTO.targetType
     * @param value TargetEntity
     */
    public void setTargetType(final TargetEntity value)
    {
        this.targetType = value;
    }

    /**
     * TODO: Model Documentation for KycFieldGroupDTO.position
     * Get the position Attribute
     * @return position Integer
     */
    public Integer getPosition()
    {
        return this.position;
    }

    /**
     * TODO: Model Documentation for KycFieldGroupDTO.position
     * @param value Integer
     */
    public void setPosition(final Integer value)
    {
        this.position = value;
    }

    /**
     * TODO: Model Documentation for
     * KycFieldGroupDTO.individualSettingsId
     * Get the individualSettingsId Attribute
     * @return individualSettingsId String
     */
    public String getIndividualSettingsId()
    {
        return this.individualSettingsId;
    }

    /**
     * TODO: Model Documentation for
     * KycFieldGroupDTO.individualSettingsId
     * @param value String
     */
    public void setIndividualSettingsId(final String value)
    {
        this.individualSettingsId = value;
    }

    /**
     * TODO: Model Documentation for
     * KycFieldGroupDTO.organisationSettingsId
     * Get the organisationSettingsId Attribute
     * @return organisationSettingsId String
     */
    public String getOrganisationSettingsId()
    {
        return this.organisationSettingsId;
    }

    /**
     * TODO: Model Documentation for
     * KycFieldGroupDTO.organisationSettingsId
     * @param value String
     */
    public void setOrganisationSettingsId(final String value)
    {
        this.organisationSettingsId = value;
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
        KycFieldGroupDTO rhs = (KycFieldGroupDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getLabel(), rhs.getLabel())
            .append(this.getDescription(), rhs.getDescription())
            .append(this.getGroupFields(), rhs.getGroupFields())
            .append(this.getTargetType(), rhs.getTargetType())
            .append(this.getPosition(), rhs.getPosition())
            .append(this.getIndividualSettingsId(), rhs.getIndividualSettingsId())
            .append(this.getOrganisationSettingsId(), rhs.getOrganisationSettingsId())
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
        if (!(object instanceof KycFieldGroupDTO))
        {
            return -1;
        }
        KycFieldGroupDTO myClass = (KycFieldGroupDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getLabel(), myClass.getLabel())
            .append(this.getDescription(), myClass.getDescription())
            .append(this.getGroupFields(), myClass.getGroupFields())
            .append(this.getTargetType(), myClass.getTargetType())
            .append(this.getPosition(), myClass.getPosition())
            .append(this.getIndividualSettingsId(), myClass.getIndividualSettingsId())
            .append(this.getOrganisationSettingsId(), myClass.getOrganisationSettingsId())
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
            .append(this.getLabel())
            .append(this.getDescription())
            .append(this.getGroupFields())
            .append(this.getTargetType())
            .append(this.getPosition())
            .append(this.getIndividualSettingsId())
            .append(this.getOrganisationSettingsId())
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
        builder.append("\t,\"description\" : \"" + this.getDescription() + "\"");
        builder.append("\t,\"groupFields\" : \"" + this.getGroupFields() + "\"");
        builder.append("\t,\"targetType\" : \"" + this.getTargetType() + "\"");
        builder.append("\t,\"position\" : \"" + this.getPosition() + "\"");
        builder.append("\t,\"individualSettingsId\" : \"" + this.getIndividualSettingsId() + "\"");
        builder.append("\t,\"organisationSettingsId\" : \"" + this.getOrganisationSettingsId() + "\"");
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
        final KycFieldGroupDTO that = (KycFieldGroupDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getLabel(), that.getLabel())
            && equal(this.getDescription(), that.getDescription())
            && equal(this.getGroupFields(), that.getGroupFields())
            && equal(this.getTargetType(), that.getTargetType())
            && equal(this.getPosition(), that.getPosition())
            && equal(this.getIndividualSettingsId(), that.getIndividualSettingsId())
            && equal(this.getOrganisationSettingsId(), that.getOrganisationSettingsId())
        ;
    }

}