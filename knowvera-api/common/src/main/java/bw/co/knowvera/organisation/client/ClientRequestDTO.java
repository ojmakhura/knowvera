package bw.co.knowvera.organisation.client;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.individual.IndividualIdentityType;
import bw.co.knowvera.kyc.KycComplianceStatus;
import java.time.LocalDateTime;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for ClientRequestDTO
 */
public class ClientRequestDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 2472283925272901250L;

    // Class attributes
    /**
     * TODO: Model Documentation for ClientRequestDTO.name
     */
    protected String name;
    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.registration
     */
    protected String registration;
    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.identityType
     */
    @Nullable
    protected IndividualIdentityType identityType;
    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.emailAddress
     */
    @Nullable
    protected String emailAddress;
    /**
     * TODO: Model Documentation for ClientRequestDTO.status
     */
    protected ClientRequestStatus status = ClientRequestStatus.PENDING;
    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.organisationId
     */
    protected String organisationId;
    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.organisation
     */
    protected String organisation;
    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.organisationRegistrationNo
     */
    protected String organisationRegistrationNo;
    /**
     * TODO: Model Documentation for ClientRequestDTO.documentId
     */
    @Nullable
    protected String documentId;
    /**
     * TODO: Model Documentation for ClientRequestDTO.fileName
     */
    @Nullable
    protected String fileName;
    /**
     * TODO: Model Documentation for ClientRequestDTO.fileUrl
     */
    @Nullable
    protected String fileUrl;
    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.documentTypeId
     */
    @Nullable
    protected String documentTypeId;
    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.documentType
     */
    @Nullable
    protected String documentType;
    /**
     * TODO: Model Documentation for ClientRequestDTO.target
     */
    protected TargetEntity target;
    /**
     * TODO: Model Documentation for ClientRequestDTO.targetId
     */
    protected String targetId;
    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.targetKycStatus
     */
    @Nullable
    protected KycComplianceStatus targetKycStatus;
    /**
     * TODO: Model Documentation for ClientRequestDTO.ref
     */
    @Nullable
    protected String ref;
    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.organisationCode
     */
    protected String organisationCode;

    /** Default Constructor with no properties */
    public ClientRequestDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param nameIn String TODO: Model Documentation for ClientRequestDTO.name
     * @param registrationIn String TODO: Model Documentation for ClientRequestDTO.registration
     * @param statusIn ClientRequestStatus TODO: Model Documentation for ClientRequestDTO.status
     * @param organisationIdIn String TODO: Model Documentation for ClientRequestDTO.organisationId
     * @param organisationIn String TODO: Model Documentation for ClientRequestDTO.organisation
     * @param organisationRegistrationNoIn String TODO: Model Documentation for
ClientRequestDTO.organisationRegistrationNo
     * @param targetIn TargetEntity TODO: Model Documentation for ClientRequestDTO.target
     * @param targetIdIn String TODO: Model Documentation for ClientRequestDTO.targetId
     * @param organisationCodeIn String TODO: Model Documentation for ClientRequestDTO.organisationCode
     */
    public ClientRequestDTO(final String nameIn, final String registrationIn, final ClientRequestStatus statusIn, final String organisationIdIn, final String organisationIn, final String organisationRegistrationNoIn, final TargetEntity targetIn, final String targetIdIn, final String organisationCodeIn)
    {
        super();
        this.name = nameIn;
        this.registration = registrationIn;
        this.status = statusIn;
        this.organisationId = organisationIdIn;
        this.organisation = organisationIn;
        this.organisationRegistrationNo = organisationRegistrationNoIn;
        this.target = targetIn;
        this.targetId = targetIdIn;
        this.organisationCode = organisationCodeIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param nameIn String
     * @param registrationIn String
     * @param identityTypeIn IndividualIdentityType
     * @param emailAddressIn String
     * @param statusIn ClientRequestStatus
     * @param organisationIdIn String
     * @param organisationIn String
     * @param organisationRegistrationNoIn String
     * @param documentIdIn String
     * @param fileNameIn String
     * @param fileUrlIn String
     * @param documentTypeIdIn String
     * @param documentTypeIn String
     * @param targetIn TargetEntity
     * @param targetIdIn String
     * @param targetKycStatusIn KycComplianceStatus
     * @param refIn String
     * @param organisationCodeIn String
     */
    public ClientRequestDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final String nameIn, final String registrationIn, final IndividualIdentityType identityTypeIn, final String emailAddressIn, final ClientRequestStatus statusIn, final String organisationIdIn, final String organisationIn, final String organisationRegistrationNoIn, final String documentIdIn, final String fileNameIn, final String fileUrlIn, final String documentTypeIdIn, final String documentTypeIn, final TargetEntity targetIn, final String targetIdIn, final KycComplianceStatus targetKycStatusIn, final String refIn, final String organisationCodeIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.name = nameIn;
        this.registration = registrationIn;
        this.identityType = identityTypeIn;
        this.emailAddress = emailAddressIn;
        this.status = statusIn;
        this.organisationId = organisationIdIn;
        this.organisation = organisationIn;
        this.organisationRegistrationNo = organisationRegistrationNoIn;
        this.documentId = documentIdIn;
        this.fileName = fileNameIn;
        this.fileUrl = fileUrlIn;
        this.documentTypeId = documentTypeIdIn;
        this.documentType = documentTypeIn;
        this.target = targetIn;
        this.targetId = targetIdIn;
        this.targetKycStatus = targetKycStatusIn;
        this.ref = refIn;
        this.organisationCode = organisationCodeIn;
    }

    /**
     * Copies constructor from other ClientRequestDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public ClientRequestDTO(final ClientRequestDTO otherBean)
    {
        super(otherBean);
        this.name = otherBean.getName();
        this.registration = otherBean.getRegistration();
        this.identityType = otherBean.getIdentityType();
        this.emailAddress = otherBean.getEmailAddress();
        this.status = otherBean.getStatus();
        this.organisationId = otherBean.getOrganisationId();
        this.organisation = otherBean.getOrganisation();
        this.organisationRegistrationNo = otherBean.getOrganisationRegistrationNo();
        this.documentId = otherBean.getDocumentId();
        this.fileName = otherBean.getFileName();
        this.fileUrl = otherBean.getFileUrl();
        this.documentTypeId = otherBean.getDocumentTypeId();
        this.documentType = otherBean.getDocumentType();
        this.target = otherBean.getTarget();
        this.targetId = otherBean.getTargetId();
        this.targetKycStatus = otherBean.getTargetKycStatus();
        this.ref = otherBean.getRef();
        this.organisationCode = otherBean.getOrganisationCode();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final ClientRequestDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setName(otherBean.getName());
            this.setRegistration(otherBean.getRegistration());
            this.setIdentityType(otherBean.getIdentityType());
            this.setEmailAddress(otherBean.getEmailAddress());
            this.setStatus(otherBean.getStatus());
            this.setOrganisationId(otherBean.getOrganisationId());
            this.setOrganisation(otherBean.getOrganisation());
            this.setOrganisationRegistrationNo(otherBean.getOrganisationRegistrationNo());
            this.setDocumentId(otherBean.getDocumentId());
            this.setFileName(otherBean.getFileName());
            this.setFileUrl(otherBean.getFileUrl());
            this.setDocumentTypeId(otherBean.getDocumentTypeId());
            this.setDocumentType(otherBean.getDocumentType());
            this.setTarget(otherBean.getTarget());
            this.setTargetId(otherBean.getTargetId());
            this.setTargetKycStatus(otherBean.getTargetKycStatus());
            this.setRef(otherBean.getRef());
            this.setOrganisationCode(otherBean.getOrganisationCode());
        }
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.registration
     * Get the registration Attribute
     * @return registration String
     */
    public String getRegistration()
    {
        return this.registration;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.registration
     * @param value String
     */
    public void setRegistration(final String value)
    {
        this.registration = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.identityType
     * Get the identityType Attribute
     * @return identityType IndividualIdentityType
     */
    public IndividualIdentityType getIdentityType()
    {
        return this.identityType;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.identityType
     * @param value IndividualIdentityType
     */
    public void setIdentityType(final IndividualIdentityType value)
    {
        this.identityType = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.emailAddress
     * Get the emailAddress Attribute
     * @return emailAddress String
     */
    public String getEmailAddress()
    {
        return this.emailAddress;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.emailAddress
     * @param value String
     */
    public void setEmailAddress(final String value)
    {
        this.emailAddress = value;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.status
     * Get the status Attribute
     * @return status ClientRequestStatus
     */
    public ClientRequestStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.status
     * @param value ClientRequestStatus
     */
    public void setStatus(final ClientRequestStatus value)
    {
        this.status = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.organisationId
     * Get the organisationId Attribute
     * @return organisationId String
     */
    public String getOrganisationId()
    {
        return this.organisationId;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.organisationId
     * @param value String
     */
    public void setOrganisationId(final String value)
    {
        this.organisationId = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.organisation
     * Get the organisation Attribute
     * @return organisation String
     */
    public String getOrganisation()
    {
        return this.organisation;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.organisation
     * @param value String
     */
    public void setOrganisation(final String value)
    {
        this.organisation = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.organisationRegistrationNo
     * Get the organisationRegistrationNo Attribute
     * @return organisationRegistrationNo String
     */
    public String getOrganisationRegistrationNo()
    {
        return this.organisationRegistrationNo;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.organisationRegistrationNo
     * @param value String
     */
    public void setOrganisationRegistrationNo(final String value)
    {
        this.organisationRegistrationNo = value;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.documentId
     * Get the documentId Attribute
     * @return documentId String
     */
    public String getDocumentId()
    {
        return this.documentId;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.documentId
     * @param value String
     */
    public void setDocumentId(final String value)
    {
        this.documentId = value;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.fileName
     * Get the fileName Attribute
     * @return fileName String
     */
    public String getFileName()
    {
        return this.fileName;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.fileName
     * @param value String
     */
    public void setFileName(final String value)
    {
        this.fileName = value;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.fileUrl
     * Get the fileUrl Attribute
     * @return fileUrl String
     */
    public String getFileUrl()
    {
        return this.fileUrl;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.fileUrl
     * @param value String
     */
    public void setFileUrl(final String value)
    {
        this.fileUrl = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.documentTypeId
     * Get the documentTypeId Attribute
     * @return documentTypeId String
     */
    public String getDocumentTypeId()
    {
        return this.documentTypeId;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.documentTypeId
     * @param value String
     */
    public void setDocumentTypeId(final String value)
    {
        this.documentTypeId = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.documentType
     * Get the documentType Attribute
     * @return documentType String
     */
    public String getDocumentType()
    {
        return this.documentType;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.documentType
     * @param value String
     */
    public void setDocumentType(final String value)
    {
        this.documentType = value;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.target
     * Get the target Attribute
     * @return target TargetEntity
     */
    public TargetEntity getTarget()
    {
        return this.target;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.target
     * @param value TargetEntity
     */
    public void setTarget(final TargetEntity value)
    {
        this.target = value;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.targetId
     * Get the targetId Attribute
     * @return targetId String
     */
    public String getTargetId()
    {
        return this.targetId;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.targetId
     * @param value String
     */
    public void setTargetId(final String value)
    {
        this.targetId = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.targetKycStatus
     * Get the targetKycStatus Attribute
     * @return targetKycStatus KycComplianceStatus
     */
    public KycComplianceStatus getTargetKycStatus()
    {
        return this.targetKycStatus;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.targetKycStatus
     * @param value KycComplianceStatus
     */
    public void setTargetKycStatus(final KycComplianceStatus value)
    {
        this.targetKycStatus = value;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.ref
     * Get the ref Attribute
     * @return ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for ClientRequestDTO.ref
     * @param value String
     */
    public void setRef(final String value)
    {
        this.ref = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.organisationCode
     * Get the organisationCode Attribute
     * @return organisationCode String
     */
    public String getOrganisationCode()
    {
        return this.organisationCode;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestDTO.organisationCode
     * @param value String
     */
    public void setOrganisationCode(final String value)
    {
        this.organisationCode = value;
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
        ClientRequestDTO rhs = (ClientRequestDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getName(), rhs.getName())
            .append(this.getRegistration(), rhs.getRegistration())
            .append(this.getIdentityType(), rhs.getIdentityType())
            .append(this.getEmailAddress(), rhs.getEmailAddress())
            .append(this.getStatus(), rhs.getStatus())
            .append(this.getOrganisationId(), rhs.getOrganisationId())
            .append(this.getOrganisation(), rhs.getOrganisation())
            .append(this.getOrganisationRegistrationNo(), rhs.getOrganisationRegistrationNo())
            .append(this.getDocumentId(), rhs.getDocumentId())
            .append(this.getFileName(), rhs.getFileName())
            .append(this.getFileUrl(), rhs.getFileUrl())
            .append(this.getDocumentTypeId(), rhs.getDocumentTypeId())
            .append(this.getDocumentType(), rhs.getDocumentType())
            .append(this.getTarget(), rhs.getTarget())
            .append(this.getTargetId(), rhs.getTargetId())
            .append(this.getTargetKycStatus(), rhs.getTargetKycStatus())
            .append(this.getRef(), rhs.getRef())
            .append(this.getOrganisationCode(), rhs.getOrganisationCode())
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
        if (!(object instanceof ClientRequestDTO))
        {
            return -1;
        }
        ClientRequestDTO myClass = (ClientRequestDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getName(), myClass.getName())
            .append(this.getRegistration(), myClass.getRegistration())
            .append(this.getIdentityType(), myClass.getIdentityType())
            .append(this.getEmailAddress(), myClass.getEmailAddress())
            .append(this.getStatus(), myClass.getStatus())
            .append(this.getOrganisationId(), myClass.getOrganisationId())
            .append(this.getOrganisation(), myClass.getOrganisation())
            .append(this.getOrganisationRegistrationNo(), myClass.getOrganisationRegistrationNo())
            .append(this.getDocumentId(), myClass.getDocumentId())
            .append(this.getFileName(), myClass.getFileName())
            .append(this.getFileUrl(), myClass.getFileUrl())
            .append(this.getDocumentTypeId(), myClass.getDocumentTypeId())
            .append(this.getDocumentType(), myClass.getDocumentType())
            .append(this.getTarget(), myClass.getTarget())
            .append(this.getTargetId(), myClass.getTargetId())
            .append(this.getTargetKycStatus(), myClass.getTargetKycStatus())
            .append(this.getRef(), myClass.getRef())
            .append(this.getOrganisationCode(), myClass.getOrganisationCode())
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
            .append(this.getName())
            .append(this.getRegistration())
            .append(this.getIdentityType())
            .append(this.getEmailAddress())
            .append(this.getStatus())
            .append(this.getOrganisationId())
            .append(this.getOrganisation())
            .append(this.getOrganisationRegistrationNo())
            .append(this.getDocumentId())
            .append(this.getFileName())
            .append(this.getFileUrl())
            .append(this.getDocumentTypeId())
            .append(this.getDocumentType())
            .append(this.getTarget())
            .append(this.getTargetId())
            .append(this.getTargetKycStatus())
            .append(this.getRef())
            .append(this.getOrganisationCode())
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
        builder.append("\t\"name\" : \"" + this.getName() + "\"");
        builder.append("\t,\"registration\" : \"" + this.getRegistration() + "\"");
        builder.append("\t,\"identityType\" : \"" + this.getIdentityType() + "\"");
        builder.append("\t,\"emailAddress\" : \"" + this.getEmailAddress() + "\"");
        builder.append("\t,\"status\" : \"" + this.getStatus() + "\"");
        builder.append("\t,\"organisationId\" : \"" + this.getOrganisationId() + "\"");
        builder.append("\t,\"organisation\" : \"" + this.getOrganisation() + "\"");
        builder.append("\t,\"organisationRegistrationNo\" : \"" + this.getOrganisationRegistrationNo() + "\"");
        builder.append("\t,\"documentId\" : \"" + this.getDocumentId() + "\"");
        builder.append("\t,\"fileName\" : \"" + this.getFileName() + "\"");
        builder.append("\t,\"fileUrl\" : \"" + this.getFileUrl() + "\"");
        builder.append("\t,\"documentTypeId\" : \"" + this.getDocumentTypeId() + "\"");
        builder.append("\t,\"documentType\" : \"" + this.getDocumentType() + "\"");
        builder.append("\t,\"target\" : \"" + this.getTarget() + "\"");
        builder.append("\t,\"targetId\" : \"" + this.getTargetId() + "\"");
        builder.append("\t,\"targetKycStatus\" : \"" + this.getTargetKycStatus() + "\"");
        builder.append("\t,\"ref\" : \"" + this.getRef() + "\"");
        builder.append("\t,\"organisationCode\" : \"" + this.getOrganisationCode() + "\"");
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
        final ClientRequestDTO that = (ClientRequestDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getName(), that.getName())
            && equal(this.getRegistration(), that.getRegistration())
            && equal(this.getIdentityType(), that.getIdentityType())
            && equal(this.getEmailAddress(), that.getEmailAddress())
            && equal(this.getStatus(), that.getStatus())
            && equal(this.getOrganisationId(), that.getOrganisationId())
            && equal(this.getOrganisation(), that.getOrganisation())
            && equal(this.getOrganisationRegistrationNo(), that.getOrganisationRegistrationNo())
            && equal(this.getDocumentId(), that.getDocumentId())
            && equal(this.getFileName(), that.getFileName())
            && equal(this.getFileUrl(), that.getFileUrl())
            && equal(this.getDocumentTypeId(), that.getDocumentTypeId())
            && equal(this.getDocumentType(), that.getDocumentType())
            && equal(this.getTarget(), that.getTarget())
            && equal(this.getTargetId(), that.getTargetId())
            && equal(this.getTargetKycStatus(), that.getTargetKycStatus())
            && equal(this.getRef(), that.getRef())
            && equal(this.getOrganisationCode(), that.getOrganisationCode())
        ;
    }

}