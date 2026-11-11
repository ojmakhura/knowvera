// false
// true
package bw.co.knowvera.document.type;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.TimePeriod;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.document.type.field.ExpectedField;
import bw.co.knowvera.document.type.verification.VerificationDataConfig;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * TODO: Model Documentation for DocumentType
 */
@Entity
@Table(
    name = "DOCUMENT_TYPE")
// Uncomment to enable caching for DocumentType
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "DocumentType.findAll", query = "SELECT d FROM DocumentType AS d"),
})
public class DocumentType
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -6756439568402042544L;

    // Generate 9 attributes

    @Column(name = "CODE", unique = true, nullable = false, insertable = true, updatable = true)
    private String code;

    /**
     * TODO: Model Documentation for DocumentType.code
     * @return this.code String
     */
    public String getCode()
    {
        return this.code;
    }

    /**
     * TODO: Model Documentation for DocumentType.code
     * @param codeIn String
     */
    public void setCode(String codeIn)
    {
        this.code = codeIn;
    }


    @Column(name = "NAME", unique = true, nullable = false, insertable = true, updatable = true)
    private String name;

    /**
     * TODO: Model Documentation for DocumentType.name
     * @return this.name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for DocumentType.name
     * @param nameIn String
     */
    public void setName(String nameIn)
    {
        this.name = nameIn;
    }


    @Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true)
    private String description;

    /**
     * TODO: Model Documentation for DocumentType.description
     * @return this.description String
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * TODO: Model Documentation for DocumentType.description
     * @param descriptionIn String
     */
    public void setDescription(String descriptionIn)
    {
        this.description = descriptionIn;
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for DocumentType.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for DocumentType.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }










    @Column(name = "EXPIRES", unique = false, nullable = true, insertable = true, updatable = true)
    private Boolean expires = Boolean.valueOf(false);

    /**
     * TODO: Model Documentation for DocumentType.expires
     * @return this.expires Boolean
     */
    public Boolean getExpires()
    {
        return this.expires;
    }

    /**
     * TODO: Model Documentation for DocumentType.expires
     * @param expiresIn Boolean
     */
    public void setExpires(Boolean expiresIn)
    {
        this.expires = expiresIn;
    }


    /**
     * The veritas-processor profile that processes documents of this type (null: the default
     * profile, app.processing.default-profile).
     */
    @Column(name = "PROCESSING_PROFILE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
    private String processingProfileCode;

    public String getProcessingProfileCode()
    {
        return this.processingProfileCode;
    }

    public void setProcessingProfileCode(String processingProfileCodeIn)
    {
        this.processingProfileCode = processingProfileCodeIn;
    }

    @Column(name = "EXPIRES_IN", unique = false, nullable = true, insertable = true, updatable = true)
    private Integer expiresIn;

    /**
     * TODO: Model Documentation for DocumentType.expiresIn
     * @return this.expiresIn Integer
     */
    public Integer getExpiresIn()
    {
        return this.expiresIn;
    }

    /**
     * TODO: Model Documentation for DocumentType.expiresIn
     * @param expiresInIn Integer
     */
    public void setExpiresIn(Integer expiresInIn)
    {
        this.expiresIn = expiresInIn;
    }


    @Column(name = "EXPIRY_PERIOD", unique = false, nullable = true, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private TimePeriod expiryPeriod;

    /**
     * TODO: Model Documentation for DocumentType.expiryPeriod
     * @return this.expiryPeriod TimePeriod
     */
    public TimePeriod getExpiryPeriod()
    {
        return this.expiryPeriod;
    }

    /**
     * TODO: Model Documentation for DocumentType.expiryPeriod
     * @param expiryPeriodIn TimePeriod
     */
    public void setExpiryPeriod(TimePeriod expiryPeriodIn)
    {
        this.expiryPeriod = expiryPeriodIn;
    }

    // Generate 4 associations
    @OneToMany(cascade = {CascadeType.ALL} , mappedBy = "documentType",  fetch = FetchType.EAGER)
    private List<ExpectedField> expectedFields = new ArrayList<ExpectedField>();
    
    /**
     * TODO: Model Documentation for ExpectedField
     * @return this.expectedFields List<ExpectedField>
     */

    public List<ExpectedField> getExpectedFields()
    {
        return this.expectedFields;
    }

    /**
     * TODO: Model Documentation for ExpectedField
     * @param expectedFieldsIn List<ExpectedField>
     */
    public void setExpectedFields(List<ExpectedField> expectedFieldsIn)
    {
        this.expectedFields = expectedFieldsIn;
    }

    /**
     * TODO: Model Documentation for ExpectedField
     * @param elementToAdd ExpectedField
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addExpectedFields(ExpectedField elementToAdd)
    {
        return this.expectedFields.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for ExpectedField
     * @param elementToRemove ExpectedField
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeExpectedFields(ExpectedField elementToRemove)
    {
        return this.expectedFields.remove(elementToRemove);
    }

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "EXPIRY_FIELD_FK")
    private ExpectedField expiryField;

    /**
     * TODO: Model Documentation for ExpectedField
     * @return this.expiryField ExpectedField
     */

    public ExpectedField getExpiryField()
    {
        return this.expiryField;
    }

    /**
     * TODO: Model Documentation for ExpectedField
     * @param expiryFieldIn ExpectedField
     */
    public void setExpiryField(ExpectedField expiryFieldIn)
    {
        this.expiryField = expiryFieldIn;
    }

    @OneToMany(cascade = {CascadeType.ALL} , mappedBy = "documentType",  fetch = FetchType.EAGER, orphanRemoval = true)
    private List<VerificationDataConfig> verificationDataConfigs = new ArrayList<VerificationDataConfig>();
    
    /**
     * TODO: Model Documentation for
     * VerificationDataConfig
     * @return this.verificationDataConfigs List<VerificationDataConfig>
     */

    public List<VerificationDataConfig> getVerificationDataConfigs()
    {
        return this.verificationDataConfigs;
    }

    /**
     * TODO: Model Documentation for
     * VerificationDataConfig
     * @param verificationDataConfigsIn List<VerificationDataConfig>
     */
    public void setVerificationDataConfigs(List<VerificationDataConfig> verificationDataConfigsIn)
    {
        this.verificationDataConfigs = verificationDataConfigsIn;
    }

    /**
     * TODO: Model Documentation for
     * VerificationDataConfig
     * @param elementToAdd VerificationDataConfig
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addVerificationDataConfigs(VerificationDataConfig elementToAdd)
    {
        return this.verificationDataConfigs.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for
     * VerificationDataConfig
     * @param elementToRemove VerificationDataConfig
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeVerificationDataConfigs(VerificationDataConfig elementToRemove)
    {
        return this.verificationDataConfigs.remove(elementToRemove);
    }

    @OneToMany( mappedBy = "documentType")
    private List<Document> documents = new ArrayList<Document>();
    
    /**
     * TODO: Model Documentation for Document
     * @return this.documents List<Document>
     */

    public List<Document> getDocuments()
    {
        return this.documents;
    }

    /**
     * TODO: Model Documentation for Document
     * @param documentsIn List<Document>
     */
    public void setDocuments(List<Document> documentsIn)
    {
        this.documents = documentsIn;
    }

    /**
     * TODO: Model Documentation for Document
     * @param elementToAdd Document
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addDocuments(Document elementToAdd)
    {
        return this.documents.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for Document
     * @param elementToRemove Document
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeDocuments(Document elementToRemove)
    {
        return this.documents.remove(elementToRemove);
    }

    /**
     * Returns <code>true</code> if the argument is an DocumentType instance and all identifiers for this entity
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
        if (!(object instanceof DocumentType))
        {
            return false;
        }
        final DocumentType that = (DocumentType)object;
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
     * Constructs new instances of {@link DocumentType}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link DocumentType}.
         * @return new DocumentType()
         */
        public static DocumentType newInstance()
        {
            return new DocumentType();
        }

        /**
         * Constructs a new instance of {@link DocumentType}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param code String
         * @param name String
         * @return newInstance
         */
        public static DocumentType newInstance(LocalDateTime createdAt, String createdBy, String code, String name)
        {
            final DocumentType entity = new DocumentType();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setCode(code);
            entity.setName(name);
            return entity;
        }

        /**
         * Constructs a new instance of {@link DocumentType}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param code String
         * @param name String
         * @param description String
         * @param expires Boolean
         * @param expiresIn Integer
         * @param expiryPeriod TimePeriod
         * @param expectedFields List<ExpectedField>
         * @param expiryField ExpectedField
         * @param verificationDataConfigs List<VerificationDataConfig>
         * @param documents List<Document>
         * @return newInstance DocumentType
         */
        public static DocumentType newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, String code, String name, String description, Boolean expires, Integer expiresIn, TimePeriod expiryPeriod, List<ExpectedField> expectedFields, ExpectedField expiryField, List<VerificationDataConfig> verificationDataConfigs, List<Document> documents)
        {
            final DocumentType entity = new DocumentType();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setCode(code);
            entity.setName(name);
            entity.setDescription(description);
            entity.setExpires(expires);
            entity.setExpiresIn(expiresIn);
            entity.setExpiryPeriod(expiryPeriod);
            entity.setExpectedFields(expectedFields);
            entity.setExpiryField(expiryField);
            entity.setVerificationDataConfigs(verificationDataConfigs);
            entity.setDocuments(documents);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(DocumentType other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getCode() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getCode().compareTo(other.getCode()));
            }
            if (this.getName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getName().compareTo(other.getName()));
            }
            if (this.getDescription() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDescription().compareTo(other.getDescription()));
            }
            if (this.getExpires() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getExpires().compareTo(other.getExpires()));
            }
            if (this.getExpiresIn() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getExpiresIn().compareTo(other.getExpiresIn()));
            }
            if (this.getExpiryPeriod() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getExpiryPeriod().compareTo(other.getExpiryPeriod()));
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
        sb.append("DocumentType(");
        sb.append(super.toString());
        sb.append(" code=").append(getCode());
        sb.append(" name=").append(getName());
        sb.append(" description=").append(getDescription());
        sb.append(" id=").append(getId());
        sb.append(" expires=").append(getExpires());
        sb.append(" expiresIn=").append(getExpiresIn());
        sb.append(" expiryPeriod=").append(getExpiryPeriod());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}