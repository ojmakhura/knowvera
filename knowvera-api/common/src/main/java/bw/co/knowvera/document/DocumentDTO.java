package bw.co.knowvera.document;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.type.verification.VerificationDataConfigDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for DocumentDTO
 */
public class DocumentDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 3377311321010872299L;

    // Class attributes
    /**
     * TODO: Model Documentation for DocumentDTO.target
     */
    @Nullable
    protected TargetEntity target;
    /**
     * TODO: Model Documentation for DocumentDTO.documentTypeId
     */
    @Nullable
    protected String documentTypeId;
    /**
     * TODO: Model Documentation for DocumentDTO.documentType
     */
    @Nullable
    protected String documentType;
    /**
     * TODO: Model Documentation for DocumentDTO.url
     */
    @Nullable
    protected String url;
    /**
     * TODO: Model Documentation for DocumentDTO.targetId
     */
    protected String targetId;
    /**
     * TODO: Model Documentation for DocumentDTO.metadata
     */
    @Nullable
    protected Map metadata;
    /**
     * TODO: Model Documentation for DocumentDTO.fileName
     */
    protected String fileName;
    /**
     * TODO: Model Documentation for DocumentDTO.fileContent
     */
    @Nullable
    protected String fileContent;
    /**
     * TODO: Model Documentation for DocumentDTO.extractedInformation
     */
    @Nullable
    protected Map extractedInformation;
    /**
     * TODO: Model Documentation for DocumentDTO.expectedFields
     */
    @Nullable
    protected Map expectedFields;
    /**
     * TODO: Model Documentation for DocumentDTO.validationResults
     */
    @Nullable
    protected DocumentValidationResults validationResults;
    /**
     * TODO: Model Documentation for DocumentDTO.verificationStatus
     */
    @Nullable
    protected DocumentVerificationStatus verificationStatus = DocumentVerificationStatus.UNVERIFIED;
    /**
     * TODO: Model Documentation for DocumentDTO.targetLabel
     */
    @Nullable
    protected String targetLabel;
    /**
     * TODO: Model Documentation for DocumentDTO.expectedInformation
     */
    @Nullable
    protected Map expectedInformation;
    /**
     * TODO: Model Documentation for DocumentDTO.analyticsStatus
     */
    @Nullable
    protected DocumentAnalyticsStatus analyticsStatus = DocumentAnalyticsStatus.INITIALISED;
    /**
     * TODO: Model Documentation for DocumentDTO.dataVerifications
     */
    @Nullable
    protected Collection<DataVerification> dataVerifications;
    /**
     * TODO: Model Documentation for DocumentDTO.dataComparisons
     */
    @Nullable
    protected Collection<DataComparisons> dataComparisons;
    /**
     * TODO: Model Documentation for DocumentDTO.verificationDataConfigs
     */
    @Nullable
    protected Collection<VerificationDataConfigDTO> verificationDataConfigs;
    /**
     * TODO: Model Documentation for DocumentDTO.verifiedBy
     */
    @Nullable
    protected String verifiedBy;
    /**
     * TODO: Model Documentation for DocumentDTO.verifiedAt
     */
    @Nullable
    protected LocalDateTime verifiedAt;

    /** Default Constructor with no properties */
    public DocumentDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param targetIdIn String TODO: Model Documentation for DocumentDTO.targetId
     * @param fileNameIn String TODO: Model Documentation for DocumentDTO.fileName
     */
    public DocumentDTO(final String targetIdIn, final String fileNameIn)
    {
        super();
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
     * @param targetIn TargetEntity
     * @param documentTypeIdIn String
     * @param documentTypeIn String
     * @param urlIn String
     * @param targetIdIn String
     * @param metadataIn Map
     * @param fileNameIn String
     * @param fileContentIn String
     * @param extractedInformationIn Map
     * @param expectedFieldsIn Map
     * @param validationResultsIn DocumentValidationResults
     * @param verificationStatusIn DocumentVerificationStatus
     * @param targetLabelIn String
     * @param expectedInformationIn Map
     * @param analyticsStatusIn DocumentAnalyticsStatus
     * @param dataVerificationsIn Collection<DataVerification>
     * @param dataComparisonsIn Collection<DataComparisons>
     * @param verificationDataConfigsIn Collection<VerificationDataConfigDTO>
     * @param verifiedByIn String
     * @param verifiedAtIn LocalDateTime
     */
    public DocumentDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final TargetEntity targetIn, final String documentTypeIdIn, final String documentTypeIn, final String urlIn, final String targetIdIn, final Map metadataIn, final String fileNameIn, final String fileContentIn, final Map extractedInformationIn, final Map expectedFieldsIn, final DocumentValidationResults validationResultsIn, final DocumentVerificationStatus verificationStatusIn, final String targetLabelIn, final Map expectedInformationIn, final DocumentAnalyticsStatus analyticsStatusIn, final Collection<DataVerification> dataVerificationsIn, final Collection<DataComparisons> dataComparisonsIn, final Collection<VerificationDataConfigDTO> verificationDataConfigsIn, final String verifiedByIn, final LocalDateTime verifiedAtIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.target = targetIn;
        this.documentTypeId = documentTypeIdIn;
        this.documentType = documentTypeIn;
        this.url = urlIn;
        this.targetId = targetIdIn;
        this.metadata = metadataIn;
        this.fileName = fileNameIn;
        this.fileContent = fileContentIn;
        this.extractedInformation = extractedInformationIn;
        this.expectedFields = expectedFieldsIn;
        this.validationResults = validationResultsIn;
        this.verificationStatus = verificationStatusIn;
        this.targetLabel = targetLabelIn;
        this.expectedInformation = expectedInformationIn;
        this.analyticsStatus = analyticsStatusIn;
        this.dataVerifications = dataVerificationsIn;
        this.dataComparisons = dataComparisonsIn;
        this.verificationDataConfigs = verificationDataConfigsIn;
        this.verifiedBy = verifiedByIn;
        this.verifiedAt = verifiedAtIn;
    }

    /**
     * Copies constructor from other DocumentDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public DocumentDTO(final DocumentDTO otherBean)
    {
        super(otherBean);
        this.target = otherBean.getTarget();
        this.documentTypeId = otherBean.getDocumentTypeId();
        this.documentType = otherBean.getDocumentType();
        this.url = otherBean.getUrl();
        this.targetId = otherBean.getTargetId();
        this.metadata = otherBean.getMetadata();
        this.fileName = otherBean.getFileName();
        this.fileContent = otherBean.getFileContent();
        this.extractedInformation = otherBean.getExtractedInformation();
        this.expectedFields = otherBean.getExpectedFields();
        this.validationResults = otherBean.getValidationResults();
        this.verificationStatus = otherBean.getVerificationStatus();
        this.targetLabel = otherBean.getTargetLabel();
        this.expectedInformation = otherBean.getExpectedInformation();
        this.analyticsStatus = otherBean.getAnalyticsStatus();
        this.dataVerifications = otherBean.getDataVerifications();
        this.dataComparisons = otherBean.getDataComparisons();
        this.verificationDataConfigs = otherBean.getVerificationDataConfigs();
        this.verifiedBy = otherBean.getVerifiedBy();
        this.verifiedAt = otherBean.getVerifiedAt();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final DocumentDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setTarget(otherBean.getTarget());
            this.setDocumentTypeId(otherBean.getDocumentTypeId());
            this.setDocumentType(otherBean.getDocumentType());
            this.setUrl(otherBean.getUrl());
            this.setTargetId(otherBean.getTargetId());
            this.setMetadata(otherBean.getMetadata());
            this.setFileName(otherBean.getFileName());
            this.setFileContent(otherBean.getFileContent());
            this.setExtractedInformation(otherBean.getExtractedInformation());
            this.setExpectedFields(otherBean.getExpectedFields());
            this.setValidationResults(otherBean.getValidationResults());
            this.setVerificationStatus(otherBean.getVerificationStatus());
            this.setTargetLabel(otherBean.getTargetLabel());
            this.setExpectedInformation(otherBean.getExpectedInformation());
            this.setAnalyticsStatus(otherBean.getAnalyticsStatus());
            this.setDataVerifications(otherBean.getDataVerifications());
            this.setDataComparisons(otherBean.getDataComparisons());
            this.setVerificationDataConfigs(otherBean.getVerificationDataConfigs());
            this.setVerifiedBy(otherBean.getVerifiedBy());
            this.setVerifiedAt(otherBean.getVerifiedAt());
        }
    }

    /**
     * TODO: Model Documentation for DocumentDTO.target
     * Get the target Attribute
     * @return target TargetEntity
     */
    public TargetEntity getTarget()
    {
        return this.target;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.target
     * @param value TargetEntity
     */
    public void setTarget(final TargetEntity value)
    {
        this.target = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.documentTypeId
     * Get the documentTypeId Attribute
     * @return documentTypeId String
     */
    public String getDocumentTypeId()
    {
        return this.documentTypeId;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.documentTypeId
     * @param value String
     */
    public void setDocumentTypeId(final String value)
    {
        this.documentTypeId = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.documentType
     * Get the documentType Attribute
     * @return documentType String
     */
    public String getDocumentType()
    {
        return this.documentType;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.documentType
     * @param value String
     */
    public void setDocumentType(final String value)
    {
        this.documentType = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.url
     * Get the url Attribute
     * @return url String
     */
    public String getUrl()
    {
        return this.url;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.url
     * @param value String
     */
    public void setUrl(final String value)
    {
        this.url = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.targetId
     * Get the targetId Attribute
     * @return targetId String
     */
    public String getTargetId()
    {
        return this.targetId;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.targetId
     * @param value String
     */
    public void setTargetId(final String value)
    {
        this.targetId = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.metadata
     * Get the metadata Attribute
     * @return metadata Map
     */
    public Map getMetadata()
    {
        return this.metadata;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.metadata
     * @param value Map
     */
    public void setMetadata(final Map value)
    {
        this.metadata = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.fileName
     * Get the fileName Attribute
     * @return fileName String
     */
    public String getFileName()
    {
        return this.fileName;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.fileName
     * @param value String
     */
    public void setFileName(final String value)
    {
        this.fileName = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.fileContent
     * Get the fileContent Attribute
     * @return fileContent String
     */
    public String getFileContent()
    {
        return this.fileContent;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.fileContent
     * @param value String
     */
    public void setFileContent(final String value)
    {
        this.fileContent = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.extractedInformation
     * Get the extractedInformation Attribute
     * @return extractedInformation Map
     */
    public Map getExtractedInformation()
    {
        return this.extractedInformation;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.extractedInformation
     * @param value Map
     */
    public void setExtractedInformation(final Map value)
    {
        this.extractedInformation = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.expectedFields
     * Get the expectedFields Attribute
     * @return expectedFields Map
     */
    public Map getExpectedFields()
    {
        return this.expectedFields;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.expectedFields
     * @param value Map
     */
    public void setExpectedFields(final Map value)
    {
        this.expectedFields = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.validationResults
     * Get the validationResults Attribute
     * @return validationResults DocumentValidationResults
     */
    public DocumentValidationResults getValidationResults()
    {
        return this.validationResults;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.validationResults
     * @param value DocumentValidationResults
     */
    public void setValidationResults(final DocumentValidationResults value)
    {
        this.validationResults = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.verificationStatus
     * Get the verificationStatus Attribute
     * @return verificationStatus DocumentVerificationStatus
     */
    public DocumentVerificationStatus getVerificationStatus()
    {
        return this.verificationStatus;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.verificationStatus
     * @param value DocumentVerificationStatus
     */
    public void setVerificationStatus(final DocumentVerificationStatus value)
    {
        this.verificationStatus = value;
    }





    /**
     * TODO: Model Documentation for DocumentDTO.targetLabel
     * Get the targetLabel Attribute
     * @return targetLabel String
     */
    public String getTargetLabel()
    {
        return this.targetLabel;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.targetLabel
     * @param value String
     */
    public void setTargetLabel(final String value)
    {
        this.targetLabel = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.expectedInformation
     * Get the expectedInformation Attribute
     * @return expectedInformation Map
     */
    public Map getExpectedInformation()
    {
        return this.expectedInformation;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.expectedInformation
     * @param value Map
     */
    public void setExpectedInformation(final Map value)
    {
        this.expectedInformation = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.analyticsStatus
     * Get the analyticsStatus Attribute
     * @return analyticsStatus DocumentAnalyticsStatus
     */
    public DocumentAnalyticsStatus getAnalyticsStatus()
    {
        return this.analyticsStatus;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.analyticsStatus
     * @param value DocumentAnalyticsStatus
     */
    public void setAnalyticsStatus(final DocumentAnalyticsStatus value)
    {
        this.analyticsStatus = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.dataVerifications
     * Get the dataVerifications Attribute
     * @return dataVerifications Collection<DataVerification>
     */
    public Collection<DataVerification> getDataVerifications()
    {
        if (this.dataVerifications == null)
        {
            this.dataVerifications = new ArrayList<DataVerification>();
        }
        return this.dataVerifications;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.dataVerifications
     * @param value Collection<DataVerification>
     */
    public void setDataVerifications(final Collection<DataVerification> value)
    {
        this.dataVerifications = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.dataComparisons
     * Get the dataComparisons Attribute
     * @return dataComparisons Collection<DataComparisons>
     */
    public Collection<DataComparisons> getDataComparisons()
    {
        if (this.dataComparisons == null)
        {
            this.dataComparisons = new ArrayList<DataComparisons>();
        }
        return this.dataComparisons;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.dataComparisons
     * @param value Collection<DataComparisons>
     */
    public void setDataComparisons(final Collection<DataComparisons> value)
    {
        this.dataComparisons = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.verificationDataConfigs
     * Get the verificationDataConfigs Attribute
     * @return verificationDataConfigs Collection<VerificationDataConfigDTO>
     */
    public Collection<VerificationDataConfigDTO> getVerificationDataConfigs()
    {
        if (this.verificationDataConfigs == null)
        {
            this.verificationDataConfigs = new ArrayList<VerificationDataConfigDTO>();
        }
        return this.verificationDataConfigs;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.verificationDataConfigs
     * @param value Collection<VerificationDataConfigDTO>
     */
    public void setVerificationDataConfigs(final Collection<VerificationDataConfigDTO> value)
    {
        this.verificationDataConfigs = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.verifiedBy
     * Get the verifiedBy Attribute
     * @return verifiedBy String
     */
    public String getVerifiedBy()
    {
        return this.verifiedBy;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.verifiedBy
     * @param value String
     */
    public void setVerifiedBy(final String value)
    {
        this.verifiedBy = value;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.verifiedAt
     * Get the verifiedAt Attribute
     * @return verifiedAt LocalDateTime
     */
    public LocalDateTime getVerifiedAt()
    {
        return this.verifiedAt;
    }

    /**
     * TODO: Model Documentation for DocumentDTO.verifiedAt
     * @param value LocalDateTime
     */
    public void setVerifiedAt(final LocalDateTime value)
    {
        this.verifiedAt = value;
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
        DocumentDTO rhs = (DocumentDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getTarget(), rhs.getTarget())
            .append(this.getDocumentTypeId(), rhs.getDocumentTypeId())
            .append(this.getDocumentType(), rhs.getDocumentType())
            .append(this.getUrl(), rhs.getUrl())
            .append(this.getTargetId(), rhs.getTargetId())
            .append(this.getMetadata(), rhs.getMetadata())
            .append(this.getFileName(), rhs.getFileName())
            .append(this.getFileContent(), rhs.getFileContent())
            .append(this.getExtractedInformation(), rhs.getExtractedInformation())
            .append(this.getExpectedFields(), rhs.getExpectedFields())
            .append(this.getValidationResults(), rhs.getValidationResults())
            .append(this.getVerificationStatus(), rhs.getVerificationStatus())
            .append(this.getTargetLabel(), rhs.getTargetLabel())
            .append(this.getExpectedInformation(), rhs.getExpectedInformation())
            .append(this.getAnalyticsStatus(), rhs.getAnalyticsStatus())
            .append(this.getDataVerifications(), rhs.getDataVerifications())
            .append(this.getDataComparisons(), rhs.getDataComparisons())
            .append(this.getVerificationDataConfigs(), rhs.getVerificationDataConfigs())
            .append(this.getVerifiedBy(), rhs.getVerifiedBy())
            .append(this.getVerifiedAt(), rhs.getVerifiedAt())
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
        if (!(object instanceof DocumentDTO))
        {
            return -1;
        }
        DocumentDTO myClass = (DocumentDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getTarget(), myClass.getTarget())
            .append(this.getDocumentTypeId(), myClass.getDocumentTypeId())
            .append(this.getDocumentType(), myClass.getDocumentType())
            .append(this.getUrl(), myClass.getUrl())
            .append(this.getTargetId(), myClass.getTargetId())
            .append(this.getMetadata(), myClass.getMetadata())
            .append(this.getFileName(), myClass.getFileName())
            .append(this.getFileContent(), myClass.getFileContent())
            .append(this.getExtractedInformation(), myClass.getExtractedInformation())
            .append(this.getExpectedFields(), myClass.getExpectedFields())
            .append(this.getValidationResults(), myClass.getValidationResults())
            .append(this.getVerificationStatus(), myClass.getVerificationStatus())
            .append(this.getTargetLabel(), myClass.getTargetLabel())
            .append(this.getExpectedInformation(), myClass.getExpectedInformation())
            .append(this.getAnalyticsStatus(), myClass.getAnalyticsStatus())
            .append(this.getDataVerifications(), myClass.getDataVerifications())
            .append(this.getDataComparisons(), myClass.getDataComparisons())
            .append(this.getVerificationDataConfigs(), myClass.getVerificationDataConfigs())
            .append(this.getVerifiedBy(), myClass.getVerifiedBy())
            .append(this.getVerifiedAt(), myClass.getVerifiedAt())
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
            .append(this.getTarget())
            .append(this.getDocumentTypeId())
            .append(this.getDocumentType())
            .append(this.getUrl())
            .append(this.getTargetId())
            .append(this.getMetadata())
            .append(this.getFileName())
            .append(this.getFileContent())
            .append(this.getExtractedInformation())
            .append(this.getExpectedFields())
            .append(this.getValidationResults())
            .append(this.getVerificationStatus())
            .append(this.getTargetLabel())
            .append(this.getExpectedInformation())
            .append(this.getAnalyticsStatus())
            .append(this.getDataVerifications())
            .append(this.getDataComparisons())
            .append(this.getVerificationDataConfigs())
            .append(this.getVerifiedBy())
            .append(this.getVerifiedAt())
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
        builder.append("\t\"target\" : \"" + this.getTarget() + "\"");
        builder.append("\t,\"documentTypeId\" : \"" + this.getDocumentTypeId() + "\"");
        builder.append("\t,\"documentType\" : \"" + this.getDocumentType() + "\"");
        builder.append("\t,\"url\" : \"" + this.getUrl() + "\"");
        builder.append("\t,\"targetId\" : \"" + this.getTargetId() + "\"");
        builder.append("\t,\"metadata\" : \"" + this.getMetadata() + "\"");
        builder.append("\t,\"fileName\" : \"" + this.getFileName() + "\"");
        builder.append("\t,\"fileContent\" : \"" + this.getFileContent() + "\"");
        builder.append("\t,\"extractedInformation\" : \"" + this.getExtractedInformation() + "\"");
        builder.append("\t,\"expectedFields\" : \"" + this.getExpectedFields() + "\"");
        builder.append("\t,\"validationResults\" : \"" + this.getValidationResults() + "\"");
        builder.append("\t,\"verificationStatus\" : \"" + this.getVerificationStatus() + "\"");
        builder.append("\t,\"targetLabel\" : \"" + this.getTargetLabel() + "\"");
        builder.append("\t,\"expectedInformation\" : \"" + this.getExpectedInformation() + "\"");
        builder.append("\t,\"analyticsStatus\" : \"" + this.getAnalyticsStatus() + "\"");
        builder.append("\t,\"dataVerifications\" : \"" + this.getDataVerifications() + "\"");
        builder.append("\t,\"dataComparisons\" : \"" + this.getDataComparisons() + "\"");
        builder.append("\t,\"verificationDataConfigs\" : \"" + this.getVerificationDataConfigs() + "\"");
        builder.append("\t,\"verifiedBy\" : \"" + this.getVerifiedBy() + "\"");
        builder.append("\t,\"verifiedAt\" : \"" + this.getVerifiedAt() + "\"");
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
        final DocumentDTO that = (DocumentDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getTarget(), that.getTarget())
            && equal(this.getDocumentTypeId(), that.getDocumentTypeId())
            && equal(this.getDocumentType(), that.getDocumentType())
            && equal(this.getUrl(), that.getUrl())
            && equal(this.getTargetId(), that.getTargetId())
            && equal(this.getMetadata(), that.getMetadata())
            && equal(this.getFileName(), that.getFileName())
            && equal(this.getFileContent(), that.getFileContent())
            && equal(this.getExtractedInformation(), that.getExtractedInformation())
            && equal(this.getExpectedFields(), that.getExpectedFields())
            && equal(this.getValidationResults(), that.getValidationResults())
            && equal(this.getVerificationStatus(), that.getVerificationStatus())
            && equal(this.getTargetLabel(), that.getTargetLabel())
            && equal(this.getExpectedInformation(), that.getExpectedInformation())
            && equal(this.getAnalyticsStatus(), that.getAnalyticsStatus())
            && equal(this.getDataVerifications(), that.getDataVerifications())
            && equal(this.getDataComparisons(), that.getDataComparisons())
            && equal(this.getVerificationDataConfigs(), that.getVerificationDataConfigs())
            && equal(this.getVerifiedBy(), that.getVerifiedBy())
            && equal(this.getVerifiedAt(), that.getVerifiedAt())
        ;
    }

}