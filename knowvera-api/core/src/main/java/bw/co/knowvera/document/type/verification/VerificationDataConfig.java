// false
// true
package bw.co.knowvera.document.type.verification;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.document.type.DocumentType;
import bw.co.knowvera.document.type.field.ExpectedField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for VerificationDataConfig
 */
@Entity
@Table(
    name = "VERIFICATION_DATA_CONFIG", uniqueConstraints = {
        @UniqueConstraint(
            name="DOCUMENT_TYPE_DATA_UNIQUE",
            columnNames={
                "NAME",
                "DOCUMENT_TYPE_FK"
            }
        )
    }
)
// Uncomment to enable caching for VerificationDataConfig
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "VerificationDataConfig.findAll", query = "SELECT v FROM VerificationDataConfig AS v"),
})
public class VerificationDataConfig
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -46486009570086456L;

    // Generate 2 attributes

    @Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true)
    private String name;

    /**
     * TODO: Model Documentation for
     * VerificationDataConfig.name
     * @return this.name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for
     * VerificationDataConfig.name
     * @param nameIn String
     */
    public void setName(String nameIn)
    {
        this.name = nameIn;
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for
     * VerificationDataConfig.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for
     * VerificationDataConfig.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }

    // Generate 2 associations
    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable
    (
        name = "EXPECTED_FIELDS2VERIFICATION_DATA_CONFIGS",
        joinColumns = {@JoinColumn(name = "VERIFICATION_DATA_CONFIGS_FK")},
        inverseJoinColumns = {@JoinColumn(name = "EXPECTED_FIELDS_FK")}
    )
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

    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
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
     * Returns <code>true</code> if the argument is an VerificationDataConfig instance and all identifiers for this entity
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
        if (!(object instanceof VerificationDataConfig))
        {
            return false;
        }
        final VerificationDataConfig that = (VerificationDataConfig)object;
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
     * Constructs new instances of {@link VerificationDataConfig}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link VerificationDataConfig}.
         * @return new VerificationDataConfig()
         */
        public static VerificationDataConfig newInstance()
        {
            return new VerificationDataConfig();
        }

        /**
         * Constructs a new instance of {@link VerificationDataConfig}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param name String
         * @param documentType DocumentType
         * @return newInstance
         */
        public static VerificationDataConfig newInstance(LocalDateTime createdAt, String createdBy, String name, DocumentType documentType)
        {
            final VerificationDataConfig entity = new VerificationDataConfig();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setName(name);
            entity.setDocumentType(documentType);
            return entity;
        }

        /**
         * Constructs a new instance of {@link VerificationDataConfig}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param name String
         * @param expectedFields List<ExpectedField>
         * @param documentType DocumentType
         * @return newInstance VerificationDataConfig
         */
        public static VerificationDataConfig newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, String name, List<ExpectedField> expectedFields, DocumentType documentType)
        {
            final VerificationDataConfig entity = new VerificationDataConfig();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setName(name);
            entity.setExpectedFields(expectedFields);
            entity.setDocumentType(documentType);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(VerificationDataConfig other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getName().compareTo(other.getName()));
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
        sb.append("VerificationDataConfig(");
        sb.append(super.toString());
        sb.append(" name=").append(getName());
        sb.append(" id=").append(getId());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}