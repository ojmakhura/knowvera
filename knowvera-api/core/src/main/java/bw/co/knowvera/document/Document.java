// false
// true
package bw.co.knowvera.document;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.type.DocumentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * TODO: Model Documentation for Document
 */
@Entity
@Table(
    name = "DOCUMENT", uniqueConstraints = {
        @UniqueConstraint(
            name="TARGET_ATTACHMENT_UNIQUE",
            columnNames={
                "TARGET",
                "TARGET_ID",
                "FILE_NAME",
                "DOCUMENT_TYPE_FK"
            }
        )
    }
)
// Uncomment to enable caching for Document
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "Document.findAll", query = "SELECT d FROM Document AS d"),
})
public class Document
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -3574861983923361542L;

    // Generate 15 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for Document.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for Document.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "URL", unique = false, nullable = true, insertable = true, updatable = true)
    private String url;

    /**
     * TODO: Model Documentation for Document.url
     * @return this.url String
     */
    public String getUrl()
    {
        return this.url;
    }

    /**
     * TODO: Model Documentation for Document.url
     * @param urlIn String
     */
    public void setUrl(String urlIn)
    {
        this.url = urlIn;
    }


    @Column(name = "TARGET", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private TargetEntity target;

    /**
     * TODO: Model Documentation for Document.target
     * @return this.target TargetEntity
     */
    public TargetEntity getTarget()
    {
        return this.target;
    }

    /**
     * TODO: Model Documentation for Document.target
     * @param targetIn TargetEntity
     */
    public void setTarget(TargetEntity targetIn)
    {
        this.target = targetIn;
    }


    @Column(name = "TARGET_ID", unique = false, nullable = false, insertable = true, updatable = true)
    private String targetId;

    /**
     * TODO: Model Documentation for Document.targetId
     * @return this.targetId String
     */
    public String getTargetId()
    {
        return this.targetId;
    }

    /**
     * TODO: Model Documentation for Document.targetId
     * @param targetIdIn String
     */
    public void setTargetId(String targetIdIn)
    {
        this.targetId = targetIdIn;
    }


    @Column(name = "FILE_NAME", unique = false, nullable = false, insertable = true, updatable = true)
    private String fileName;

    /**
     * TODO: Model Documentation for Document.fileName
     * @return this.fileName String
     */
    public String getFileName()
    {
        return this.fileName;
    }

    /**
     * TODO: Model Documentation for Document.fileName
     * @param fileNameIn String
     */
    public void setFileName(String fileNameIn)
    {
        this.fileName = fileNameIn;
    }


    @Column(name = "METADATA", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map metadata;

    /**
     * TODO: Model Documentation for Document.metadata
     * @return this.metadata Map
     */
    public Map getMetadata()
    {
        return this.metadata;
    }

    /**
     * TODO: Model Documentation for Document.metadata
     * @param metadataIn Map
     */
    public void setMetadata(Map metadataIn)
    {
        this.metadata = metadataIn;
    }


    @Column(name = "FILE_CONTENT", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "TEXT")
    private String fileContent;

    /**
     * TODO: Model Documentation for Document.fileContent
     * @return this.fileContent String
     */
    public String getFileContent()
    {
        return this.fileContent;
    }

    /**
     * TODO: Model Documentation for Document.fileContent
     * @param fileContentIn String
     */
    public void setFileContent(String fileContentIn)
    {
        this.fileContent = fileContentIn;
    }


    @Column(name = "EXTRACTED_INFORMATION", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map extractedInformation;

    /**
     * TODO: Model Documentation for Document.extractedInformation
     * @return this.extractedInformation Map
     */
    public Map getExtractedInformation()
    {
        return this.extractedInformation;
    }

    /**
     * TODO: Model Documentation for Document.extractedInformation
     * @param extractedInformationIn Map
     */
    public void setExtractedInformation(Map extractedInformationIn)
    {
        this.extractedInformation = extractedInformationIn;
    }


    @Column(name = "VERIFICATION_STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private DocumentVerificationStatus verificationStatus = DocumentVerificationStatus.UNVERIFIED;

    /**
     * TODO: Model Documentation for Document.verificationStatus
     * @return this.verificationStatus DocumentVerificationStatus
     */
    public DocumentVerificationStatus getVerificationStatus()
    {
        return this.verificationStatus;
    }

    /**
     * TODO: Model Documentation for Document.verificationStatus
     * @param verificationStatusIn DocumentVerificationStatus
     */
    public void setVerificationStatus(DocumentVerificationStatus verificationStatusIn)
    {
        this.verificationStatus = verificationStatusIn;
    }


    @Column(name = "VALIDATION_RESULTS", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private DocumentValidationResults validationResults;

    /**
     * TODO: Model Documentation for Document.validationResults
     * @return this.validationResults DocumentValidationResults
     */
    public DocumentValidationResults getValidationResults()
    {
        return this.validationResults;
    }

    /**
     * TODO: Model Documentation for Document.validationResults
     * @param validationResultsIn DocumentValidationResults
     */
    public void setValidationResults(DocumentValidationResults validationResultsIn)
    {
        this.validationResults = validationResultsIn;
    }


    @Column(name = "EXPECTED_INFORMATION", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map expectedInformation;

    /**
     * TODO: Model Documentation for Document.expectedInformation
     * @return this.expectedInformation Map
     */
    public Map getExpectedInformation()
    {
        return this.expectedInformation;
    }

    /**
     * TODO: Model Documentation for Document.expectedInformation
     * @param expectedInformationIn Map
     */
    public void setExpectedInformation(Map expectedInformationIn)
    {
        this.expectedInformation = expectedInformationIn;
    }


    @Column(name = "ANALYTICS_STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private DocumentAnalyticsStatus analyticsStatus = DocumentAnalyticsStatus.INITIALISED;

    /**
     * TODO: Model Documentation for Document.analyticsStatus
     * @return this.analyticsStatus DocumentAnalyticsStatus
     */
    public DocumentAnalyticsStatus getAnalyticsStatus()
    {
        return this.analyticsStatus;
    }

    /**
     * TODO: Model Documentation for Document.analyticsStatus
     * @param analyticsStatusIn DocumentAnalyticsStatus
     */
    public void setAnalyticsStatus(DocumentAnalyticsStatus analyticsStatusIn)
    {
        this.analyticsStatus = analyticsStatusIn;
    }


    @Column(name = "DATA_VERIFICATIONS", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Collection<DataVerification> dataVerifications;

    /**
     * TODO: Model Documentation for Document.dataVerifications
     * @return this.dataVerifications Collection<DataVerification>
     */
    public Collection<DataVerification> getDataVerifications()
    {
        return this.dataVerifications;
    }

    /**
     * TODO: Model Documentation for Document.dataVerifications
     * @param dataVerificationsIn Collection<DataVerification>
     */
    public void setDataVerifications(Collection<DataVerification> dataVerificationsIn)
    {
        this.dataVerifications = dataVerificationsIn;
    }


    @Column(name = "VERIFIED_BY", unique = false, nullable = true, insertable = true, updatable = true)
    private String verifiedBy;

    /**
     * TODO: Model Documentation for Document.verifiedBy
     * @return this.verifiedBy String
     */
    public String getVerifiedBy()
    {
        return this.verifiedBy;
    }

    /**
     * TODO: Model Documentation for Document.verifiedBy
     * @param verifiedByIn String
     */
    public void setVerifiedBy(String verifiedByIn)
    {
        this.verifiedBy = verifiedByIn;
    }


    @Column(name = "VERIFIED_AT", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDateTime verifiedAt;

    /**
     * TODO: Model Documentation for Document.verifiedAt
     * @return this.verifiedAt LocalDateTime
     */
    public LocalDateTime getVerifiedAt()
    {
        return this.verifiedAt;
    }

    /**
     * TODO: Model Documentation for Document.verifiedAt
     * @param verifiedAtIn LocalDateTime
     */
    public void setVerifiedAt(LocalDateTime verifiedAtIn)
    {
        this.verifiedAt = verifiedAtIn;
    }

    // Generate 1 associations
    @ManyToOne(optional = false)
    @JoinColumn(name = "DOCUMENT_TYPE_FK")
    private DocumentType documentType;

    /**
     * TODO: Model Documentation for DocumentType
     * @return this.documentType DocumentType
     */

    public DocumentType getDocumentType()
    {
        return this.documentType;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param documentTypeIn DocumentType
     */
    public void setDocumentType(DocumentType documentTypeIn)
    {
        this.documentType = documentTypeIn;
    }

    /**
     * Returns <code>true</code> if the argument is an Document instance and all identifiers for this entity
     * equal the identifiers of the argument entity. The <code>equals</code> method of the parent entity
     * will also need to return <code>true</code>. Returns <code>false</code> otherwise.
     *
     * @see Auditable#equals(Object)
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof Document))
        {
            return false;
        }
        final Document that = (Document)object;
        if (this.id == null || that.getId() == null || !this.id.equals(that.getId()))
        {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers and the hash code of the parent entity.
     *
     * @see Auditable#hashCode()
     */
    @Override
    public int hashCode()
    {
        int hashCode = super.hashCode();
        hashCode = 29 * hashCode + (this.id == null ? 0 : this.id.hashCode());

        return hashCode;
    }

    /**
     * Constructs new instances of {@link Document}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link Document}.
         * @return new Document()
         */
        public static Document newInstance()
        {
            return new Document();
        }

        /**
         * Constructs a new instance of {@link Document}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param target TargetEntity
         * @param targetId String
         * @param fileName String
         * @param verificationStatus DocumentVerificationStatus
         * @param analyticsStatus DocumentAnalyticsStatus
         * @param documentType DocumentType
         * @return newInstance
         */
        public static Document newInstance(LocalDateTime createdAt, String createdBy, TargetEntity target, String targetId, String fileName, DocumentVerificationStatus verificationStatus, DocumentAnalyticsStatus analyticsStatus, DocumentType documentType)
        {
            final Document entity = new Document();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setTarget(target);
            entity.setTargetId(targetId);
            entity.setFileName(fileName);
            entity.setVerificationStatus(verificationStatus);
            entity.setAnalyticsStatus(analyticsStatus);
            entity.setDocumentType(documentType);
            return entity;
        }

        /**
         * Constructs a new instance of {@link Document}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param url String
         * @param target TargetEntity
         * @param targetId String
         * @param fileName String
         * @param metadata Map
         * @param fileContent String
         * @param extractedInformation Map
         * @param verificationStatus DocumentVerificationStatus
         * @param validationResults DocumentValidationResults
         * @param expectedInformation Map
         * @param analyticsStatus DocumentAnalyticsStatus
         * @param dataVerifications Collection<DataVerification>
         * @param verifiedBy String
         * @param verifiedAt LocalDateTime
         * @param documentType DocumentType
         * @return newInstance Document
         */
        public static Document newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, String url, TargetEntity target, String targetId, String fileName, Map metadata, String fileContent, Map extractedInformation, DocumentVerificationStatus verificationStatus, DocumentValidationResults validationResults, Map expectedInformation, DocumentAnalyticsStatus analyticsStatus, Collection<DataVerification> dataVerifications, String verifiedBy, LocalDateTime verifiedAt, DocumentType documentType)
        {
            final Document entity = new Document();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setUrl(url);
            entity.setTarget(target);
            entity.setTargetId(targetId);
            entity.setFileName(fileName);
            entity.setMetadata(metadata);
            entity.setFileContent(fileContent);
            entity.setExtractedInformation(extractedInformation);
            entity.setVerificationStatus(verificationStatus);
            entity.setValidationResults(validationResults);
            entity.setExpectedInformation(expectedInformation);
            entity.setAnalyticsStatus(analyticsStatus);
            entity.setDataVerifications(dataVerifications);
            entity.setVerifiedBy(verifiedBy);
            entity.setVerifiedAt(verifiedAt);
            entity.setDocumentType(documentType);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(Document other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getUrl() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getUrl().compareTo(other.getUrl()));
            }
            if (this.getTarget() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTarget().compareTo(other.getTarget()));
            }
            if (this.getTargetId() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTargetId().compareTo(other.getTargetId()));
            }
            if (this.getFileName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFileName().compareTo(other.getFileName()));
            }
            if (this.getFileContent() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFileContent().compareTo(other.getFileContent()));
            }
            if (this.getVerificationStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getVerificationStatus().compareTo(other.getVerificationStatus()));
            }
            if (this.getValidationResults() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getValidationResults().compareTo(other.getValidationResults()));
            }
            if (this.getAnalyticsStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getAnalyticsStatus().compareTo(other.getAnalyticsStatus()));
            }
            if (this.getDataVerifications() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getDataVerifications().size()-other.getDataVerifications().size()));
            }
            if (this.getVerifiedBy() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getVerifiedBy().compareTo(other.getVerifiedBy()));
            }
            if (this.getVerifiedAt() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getVerifiedAt().compareTo(other.getVerifiedAt()));
            }
        }
        return cmp;
    }

    /**
     * Returns a String representation of the object
     * @return String Textual representation of the object displaying name/value pairs for all attributes
     * @see Object#toString
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Document(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" url=").append(getUrl());
        sb.append(" target=").append(getTarget());
        sb.append(" targetId=").append(getTargetId());
        sb.append(" fileName=").append(getFileName());
        sb.append(" metadata=").append(getMetadata());
        sb.append(" fileContent=").append(getFileContent());
        sb.append(" extractedInformation=").append(getExtractedInformation());
        sb.append(" verificationStatus=").append(getVerificationStatus());
        sb.append(" validationResults=").append(getValidationResults());
        sb.append(" expectedInformation=").append(getExpectedInformation());
        sb.append(" analyticsStatus=").append(getAnalyticsStatus());
        sb.append(" dataVerifications=").append(getDataVerifications());
        sb.append(" verifiedBy=").append(getVerifiedBy());
        sb.append(" verifiedAt=").append(getVerifiedAt());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}