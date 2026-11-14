package bw.co.knowvera.organisation.branch;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.document.DocumentListDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for BranchDTO
 */
public class BranchDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -5323607701104341485L;

    // Class attributes
    /**
     * TODO: Model Documentation for BranchDTO.code
     */
    protected String code;
    /**
     * TODO: Model Documentation for BranchDTO.name
     */
    protected String name;
    /**
     * TODO: Model Documentation for BranchDTO.description
     */
    @Nullable
    protected String description;
    /**
     * TODO: Model Documentation for BranchDTO.physicalAddress
     */
    @Nullable
    protected String physicalAddress;
    /**
     * TODO: Model Documentation for BranchDTO.organisationId
     */
    protected String organisationId;
    /**
     * TODO: Model Documentation for BranchDTO.organisation
     */
    protected String organisation;
    /**
     * TODO: Model Documentation for BranchDTO.documents
     */
    @Nullable
    protected List<DocumentListDTO> documents;

    /** Default Constructor with no properties */
    public BranchDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param codeIn String TODO: Model Documentation for BranchDTO.code
     * @param nameIn String TODO: Model Documentation for BranchDTO.name
     * @param organisationIdIn String TODO: Model Documentation for BranchDTO.organisationId
     * @param organisationIn String TODO: Model Documentation for BranchDTO.organisation
     */
    public BranchDTO(final String codeIn, final String nameIn, final String organisationIdIn, final String organisationIn)
    {
        super();
        this.code = codeIn;
        this.name = nameIn;
        this.organisationId = organisationIdIn;
        this.organisation = organisationIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param codeIn String
     * @param nameIn String
     * @param descriptionIn String
     * @param physicalAddressIn String
     * @param organisationIdIn String
     * @param organisationIn String
     * @param documentsIn List<DocumentListDTO>
     */
    public BranchDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final String codeIn, final String nameIn, final String descriptionIn, final String physicalAddressIn, final String organisationIdIn, final String organisationIn, final List<DocumentListDTO> documentsIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.code = codeIn;
        this.name = nameIn;
        this.description = descriptionIn;
        this.physicalAddress = physicalAddressIn;
        this.organisationId = organisationIdIn;
        this.organisation = organisationIn;
        this.documents = documentsIn;
    }

    /**
     * Copies constructor from other BranchDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public BranchDTO(final BranchDTO otherBean)
    {
        super(otherBean);
        this.code = otherBean.getCode();
        this.name = otherBean.getName();
        this.description = otherBean.getDescription();
        this.physicalAddress = otherBean.getPhysicalAddress();
        this.organisationId = otherBean.getOrganisationId();
        this.organisation = otherBean.getOrganisation();
        this.documents = otherBean.getDocuments();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final BranchDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setCode(otherBean.getCode());
            this.setName(otherBean.getName());
            this.setDescription(otherBean.getDescription());
            this.setPhysicalAddress(otherBean.getPhysicalAddress());
            this.setOrganisationId(otherBean.getOrganisationId());
            this.setOrganisation(otherBean.getOrganisation());
            this.setDocuments(otherBean.getDocuments());
        }
    }

    /**
     * TODO: Model Documentation for BranchDTO.code
     * Get the code Attribute
     * @return code String
     */
    public String getCode()
    {
        return this.code;
    }

    /**
     * TODO: Model Documentation for BranchDTO.code
     * @param value String
     */
    public void setCode(final String value)
    {
        this.code = value;
    }

    /**
     * TODO: Model Documentation for BranchDTO.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for BranchDTO.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for BranchDTO.description
     * Get the description Attribute
     * @return description String
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * TODO: Model Documentation for BranchDTO.description
     * @param value String
     */
    public void setDescription(final String value)
    {
        this.description = value;
    }

    /**
     * TODO: Model Documentation for BranchDTO.physicalAddress
     * Get the physicalAddress Attribute
     * @return physicalAddress String
     */
    public String getPhysicalAddress()
    {
        return this.physicalAddress;
    }

    /**
     * TODO: Model Documentation for BranchDTO.physicalAddress
     * @param value String
     */
    public void setPhysicalAddress(final String value)
    {
        this.physicalAddress = value;
    }

    /**
     * TODO: Model Documentation for BranchDTO.organisationId
     * Get the organisationId Attribute
     * @return organisationId String
     */
    public String getOrganisationId()
    {
        return this.organisationId;
    }

    /**
     * TODO: Model Documentation for BranchDTO.organisationId
     * @param value String
     */
    public void setOrganisationId(final String value)
    {
        this.organisationId = value;
    }

    /**
     * TODO: Model Documentation for BranchDTO.organisation
     * Get the organisation Attribute
     * @return organisation String
     */
    public String getOrganisation()
    {
        return this.organisation;
    }

    /**
     * TODO: Model Documentation for BranchDTO.organisation
     * @param value String
     */
    public void setOrganisation(final String value)
    {
        this.organisation = value;
    }

    /**
     * TODO: Model Documentation for BranchDTO.documents
     * Get the documents Attribute
     * @return documents List<DocumentListDTO>
     */
    public List<DocumentListDTO> getDocuments()
    {
        if (this.documents == null)
        {
            this.documents = new ArrayList<DocumentListDTO>();
        }
        return this.documents;
    }

    /**
     * TODO: Model Documentation for BranchDTO.documents
     * @param value List<DocumentListDTO>
     */
    public void setDocuments(final List<DocumentListDTO> value)
    {
        this.documents = value;
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
        BranchDTO rhs = (BranchDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getCode(), rhs.getCode())
            .append(this.getName(), rhs.getName())
            .append(this.getDescription(), rhs.getDescription())
            .append(this.getPhysicalAddress(), rhs.getPhysicalAddress())
            .append(this.getOrganisationId(), rhs.getOrganisationId())
            .append(this.getOrganisation(), rhs.getOrganisation())
            .append(this.getDocuments(), rhs.getDocuments())
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
        if (!(object instanceof BranchDTO))
        {
            return -1;
        }
        BranchDTO myClass = (BranchDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getCode(), myClass.getCode())
            .append(this.getName(), myClass.getName())
            .append(this.getDescription(), myClass.getDescription())
            .append(this.getPhysicalAddress(), myClass.getPhysicalAddress())
            .append(this.getOrganisationId(), myClass.getOrganisationId())
            .append(this.getOrganisation(), myClass.getOrganisation())
            .append(this.getDocuments(), myClass.getDocuments())
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
            .append(this.getCode())
            .append(this.getName())
            .append(this.getDescription())
            .append(this.getPhysicalAddress())
            .append(this.getOrganisationId())
            .append(this.getOrganisation())
            .append(this.getDocuments())
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
        builder.append("\t\"code\" : \"" + this.getCode() + "\"");
        builder.append("\t,\"name\" : \"" + this.getName() + "\"");
        builder.append("\t,\"description\" : \"" + this.getDescription() + "\"");
        builder.append("\t,\"physicalAddress\" : \"" + this.getPhysicalAddress() + "\"");
        builder.append("\t,\"organisationId\" : \"" + this.getOrganisationId() + "\"");
        builder.append("\t,\"organisation\" : \"" + this.getOrganisation() + "\"");
        builder.append("\t,\"documents\" : \"" + this.getDocuments() + "\"");
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
        final BranchDTO that = (BranchDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getCode(), that.getCode())
            && equal(this.getName(), that.getName())
            && equal(this.getDescription(), that.getDescription())
            && equal(this.getPhysicalAddress(), that.getPhysicalAddress())
            && equal(this.getOrganisationId(), that.getOrganisationId())
            && equal(this.getOrganisation(), that.getOrganisation())
            && equal(this.getDocuments(), that.getDocuments())
        ;
    }

}