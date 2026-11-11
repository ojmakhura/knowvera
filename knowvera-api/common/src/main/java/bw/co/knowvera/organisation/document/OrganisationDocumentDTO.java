package bw.co.knowvera.organisation.document;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.TargetEntity;
import java.time.LocalDateTime;
import java.util.Map;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for OrganisationDocumentDTO
 */
public class OrganisationDocumentDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 4746048552006179553L;

    // Class attributes
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.status
     */
    protected OrganisationDocumentStatus status = OrganisationDocumentStatus.ACTIVE;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.organisationId
     */
    protected String organisationId;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.organisation
     */
    protected String organisation;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.organisationRegistrationNo
     */
    protected String organisationRegistrationNo;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.documentTypeId
     */
    @Nullable
    protected String documentTypeId;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.documentType
     */
    @Nullable
    protected String documentType;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.target
     */
    protected TargetEntity target;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.targetId
     */
    protected String targetId;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.fileName
     */
    protected String fileName;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.metadata
     */
    @Nullable
    protected Map metadata;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.url
     */
    @Nullable
    protected String url;

    /** Default Constructor with no properties */
    public OrganisationDocumentDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param statusIn OrganisationDocumentStatus TODO: Model Documentation for OrganisationDocumentDTO.status
     * @param organisationIdIn String TODO: Model Documentation for
OrganisationDocumentDTO.organisationId
     * @param organisationIn String TODO: Model Documentation for
OrganisationDocumentDTO.organisation
     * @param organisationRegistrationNoIn String TODO: Model Documentation for
OrganisationDocumentDTO.organisationRegistrationNo
     * @param targetIn TargetEntity TODO: Model Documentation for OrganisationDocumentDTO.target
     * @param targetIdIn String TODO: Model Documentation for OrganisationDocumentDTO.targetId
     * @param fileNameIn String TODO: Model Documentation for OrganisationDocumentDTO.fileName
     */
    public OrganisationDocumentDTO(final OrganisationDocumentStatus statusIn, final String organisationIdIn, final String organisationIn, final String organisationRegistrationNoIn, final TargetEntity targetIn, final String targetIdIn, final String fileNameIn)
    {
        super();
        this.status = statusIn;
        this.organisationId = organisationIdIn;
        this.organisation = organisationIn;
        this.organisationRegistrationNo = organisationRegistrationNoIn;
        this.target = targetIn;
        this.targetId = targetIdIn;
        this.fileName = fileNameIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param statusIn OrganisationDocumentStatus
     * @param organisationIdIn String
     * @param organisationIn String
     * @param organisationRegistrationNoIn String
     * @param documentTypeIdIn String
     * @param documentTypeIn String
     * @param targetIn TargetEntity
     * @param targetIdIn String
     * @param fileNameIn String
     * @param metadataIn Map
     * @param urlIn String
     */
    public OrganisationDocumentDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final OrganisationDocumentStatus statusIn, final String organisationIdIn, final String organisationIn, final String organisationRegistrationNoIn, final String documentTypeIdIn, final String documentTypeIn, final TargetEntity targetIn, final String targetIdIn, final String fileNameIn, final Map metadataIn, final String urlIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.status = statusIn;
        this.organisationId = organisationIdIn;
        this.organisation = organisationIn;
        this.organisationRegistrationNo = organisationRegistrationNoIn;
        this.documentTypeId = documentTypeIdIn;
        this.documentType = documentTypeIn;
        this.target = targetIn;
        this.targetId = targetIdIn;
        this.fileName = fileNameIn;
        this.metadata = metadataIn;
        this.url = urlIn;
    }

    /**
     * Copies constructor from other OrganisationDocumentDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public OrganisationDocumentDTO(final OrganisationDocumentDTO otherBean)
    {
        super(otherBean);
        this.status = otherBean.getStatus();
        this.organisationId = otherBean.getOrganisationId();
        this.organisation = otherBean.getOrganisation();
        this.organisationRegistrationNo = otherBean.getOrganisationRegistrationNo();
        this.documentTypeId = otherBean.getDocumentTypeId();
        this.documentType = otherBean.getDocumentType();
        this.target = otherBean.getTarget();
        this.targetId = otherBean.getTargetId();
        this.fileName = otherBean.getFileName();
        this.metadata = otherBean.getMetadata();
        this.url = otherBean.getUrl();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final OrganisationDocumentDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setStatus(otherBean.getStatus());
            this.setOrganisationId(otherBean.getOrganisationId());
            this.setOrganisation(otherBean.getOrganisation());
            this.setOrganisationRegistrationNo(otherBean.getOrganisationRegistrationNo());
            this.setDocumentTypeId(otherBean.getDocumentTypeId());
            this.setDocumentType(otherBean.getDocumentType());
            this.setTarget(otherBean.getTarget());
            this.setTargetId(otherBean.getTargetId());
            this.setFileName(otherBean.getFileName());
            this.setMetadata(otherBean.getMetadata());
            this.setUrl(otherBean.getUrl());
        }
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.status
     * Get the status Attribute
     * @return status OrganisationDocumentStatus
     */
    public OrganisationDocumentStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.status
     * @param value OrganisationDocumentStatus
     */
    public void setStatus(final OrganisationDocumentStatus value)
    {
        this.status = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.organisationId
     * Get the organisationId Attribute
     * @return organisationId String
     */
    public String getOrganisationId()
    {
        return this.organisationId;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.organisationId
     * @param value String
     */
    public void setOrganisationId(final String value)
    {
        this.organisationId = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.organisation
     * Get the organisation Attribute
     * @return organisation String
     */
    public String getOrganisation()
    {
        return this.organisation;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.organisation
     * @param value String
     */
    public void setOrganisation(final String value)
    {
        this.organisation = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.organisationRegistrationNo
     * Get the organisationRegistrationNo Attribute
     * @return organisationRegistrationNo String
     */
    public String getOrganisationRegistrationNo()
    {
        return this.organisationRegistrationNo;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.organisationRegistrationNo
     * @param value String
     */
    public void setOrganisationRegistrationNo(final String value)
    {
        this.organisationRegistrationNo = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.documentTypeId
     * Get the documentTypeId Attribute
     * @return documentTypeId String
     */
    public String getDocumentTypeId()
    {
        return this.documentTypeId;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.documentTypeId
     * @param value String
     */
    public void setDocumentTypeId(final String value)
    {
        this.documentTypeId = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.documentType
     * Get the documentType Attribute
     * @return documentType String
     */
    public String getDocumentType()
    {
        return this.documentType;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.documentType
     * @param value String
     */
    public void setDocumentType(final String value)
    {
        this.documentType = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.target
     * Get the target Attribute
     * @return target TargetEntity
     */
    public TargetEntity getTarget()
    {
        return this.target;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.target
     * @param value TargetEntity
     */
    public void setTarget(final TargetEntity value)
    {
        this.target = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.targetId
     * Get the targetId Attribute
     * @return targetId String
     */
    public String getTargetId()
    {
        return this.targetId;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.targetId
     * @param value String
     */
    public void setTargetId(final String value)
    {
        this.targetId = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.fileName
     * Get the fileName Attribute
     * @return fileName String
     */
    public String getFileName()
    {
        return this.fileName;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.fileName
     * @param value String
     */
    public void setFileName(final String value)
    {
        this.fileName = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.metadata
     * Get the metadata Attribute
     * @return metadata Map
     */
    public Map getMetadata()
    {
        return this.metadata;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.metadata
     * @param value Map
     */
    public void setMetadata(final Map value)
    {
        this.metadata = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.url
     * Get the url Attribute
     * @return url String
     */
    public String getUrl()
    {
        return this.url;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentDTO.url
     * @param value String
     */
    public void setUrl(final String value)
    {
        this.url = value;
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
        OrganisationDocumentDTO rhs = (OrganisationDocumentDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getStatus(), rhs.getStatus())
            .append(this.getOrganisationId(), rhs.getOrganisationId())
            .append(this.getOrganisation(), rhs.getOrganisation())
            .append(this.getOrganisationRegistrationNo(), rhs.getOrganisationRegistrationNo())
            .append(this.getDocumentTypeId(), rhs.getDocumentTypeId())
            .append(this.getDocumentType(), rhs.getDocumentType())
            .append(this.getTarget(), rhs.getTarget())
            .append(this.getTargetId(), rhs.getTargetId())
            .append(this.getFileName(), rhs.getFileName())
            .append(this.getMetadata(), rhs.getMetadata())
            .append(this.getUrl(), rhs.getUrl())
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
        if (!(object instanceof OrganisationDocumentDTO))
        {
            return -1;
        }
        OrganisationDocumentDTO myClass = (OrganisationDocumentDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getStatus(), myClass.getStatus())
            .append(this.getOrganisationId(), myClass.getOrganisationId())
            .append(this.getOrganisation(), myClass.getOrganisation())
            .append(this.getOrganisationRegistrationNo(), myClass.getOrganisationRegistrationNo())
            .append(this.getDocumentTypeId(), myClass.getDocumentTypeId())
            .append(this.getDocumentType(), myClass.getDocumentType())
            .append(this.getTarget(), myClass.getTarget())
            .append(this.getTargetId(), myClass.getTargetId())
            .append(this.getFileName(), myClass.getFileName())
            .append(this.getMetadata(), myClass.getMetadata())
            .append(this.getUrl(), myClass.getUrl())
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
            .append(this.getStatus())
            .append(this.getOrganisationId())
            .append(this.getOrganisation())
            .append(this.getOrganisationRegistrationNo())
            .append(this.getDocumentTypeId())
            .append(this.getDocumentType())
            .append(this.getTarget())
            .append(this.getTargetId())
            .append(this.getFileName())
            .append(this.getMetadata())
            .append(this.getUrl())
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
        builder.append("\t\"status\" : \"" + this.getStatus() + "\"");
        builder.append("\t,\"organisationId\" : \"" + this.getOrganisationId() + "\"");
        builder.append("\t,\"organisation\" : \"" + this.getOrganisation() + "\"");
        builder.append("\t,\"organisationRegistrationNo\" : \"" + this.getOrganisationRegistrationNo() + "\"");
        builder.append("\t,\"documentTypeId\" : \"" + this.getDocumentTypeId() + "\"");
        builder.append("\t,\"documentType\" : \"" + this.getDocumentType() + "\"");
        builder.append("\t,\"target\" : \"" + this.getTarget() + "\"");
        builder.append("\t,\"targetId\" : \"" + this.getTargetId() + "\"");
        builder.append("\t,\"fileName\" : \"" + this.getFileName() + "\"");
        builder.append("\t,\"metadata\" : \"" + this.getMetadata() + "\"");
        builder.append("\t,\"url\" : \"" + this.getUrl() + "\"");
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
        final OrganisationDocumentDTO that = (OrganisationDocumentDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getStatus(), that.getStatus())
            && equal(this.getOrganisationId(), that.getOrganisationId())
            && equal(this.getOrganisation(), that.getOrganisation())
            && equal(this.getOrganisationRegistrationNo(), that.getOrganisationRegistrationNo())
            && equal(this.getDocumentTypeId(), that.getDocumentTypeId())
            && equal(this.getDocumentType(), that.getDocumentType())
            && equal(this.getTarget(), that.getTarget())
            && equal(this.getTargetId(), that.getTargetId())
            && equal(this.getFileName(), that.getFileName())
            && equal(this.getMetadata(), that.getMetadata())
            && equal(this.getUrl(), that.getUrl())
        ;
    }

}