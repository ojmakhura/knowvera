// false
// true
package bw.co.knowvera.document.type.field;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.type.DocumentType;
import bw.co.knowvera.document.type.verification.VerificationDataConfig;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Transient;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for ExpectedField
 */
@Entity
@EntityListeners(ExpectedFieldDefinitionListener.class)
@Table(
    name = "EXPECTED_FIELD", uniqueConstraints = {
        @UniqueConstraint(
            name="EXPECTED_FIELD_UNIQUE",
            columnNames={
                "FIELD",
                "DOCUMENT_TYPE_FK"
            }
        )
    }
)
// Uncomment to enable caching for ExpectedField
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "ExpectedField.findAll", query = "SELECT e FROM ExpectedField AS e"),
})
public class ExpectedField
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 5534284184199931990L;

    // Generate 10 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for ExpectedField.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for ExpectedField.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "FIELD", unique = false, nullable = false, insertable = true, updatable = true)
    private String field;

    /**
     * TODO: Model Documentation for ExpectedField.field
     * @return this.field String
     */
    public String getField()
    {
        return this.field;
    }

    /**
     * TODO: Model Documentation for ExpectedField.field
     * @param fieldIn String
     */
    public void setField(String fieldIn)
    {
        this.field = fieldIn;
    }


    // Kept in veritas-processor (FieldDefinitionSource), not in a column here
    @Transient
    private Boolean mandatory = Boolean.valueOf(false);

    /**
     * TODO: Model Documentation for ExpectedField.mandatory
     * @return this.mandatory Boolean
     */
    public Boolean getMandatory()
    {
        return this.mandatory;
    }

    /**
     * TODO: Model Documentation for ExpectedField.mandatory
     * @param mandatoryIn Boolean
     */
    public void setMandatory(Boolean mandatoryIn)
    {
        this.mandatory = mandatoryIn;
    }


    // Kept in veritas-processor (FieldDefinitionSource), not in a column here
    @Transient
    private String format;

    /**
     * TODO: Model Documentation for ExpectedField.format
     * @return this.format String
     */
    public String getFormat()
    {
        return this.format;
    }

    /**
     * TODO: Model Documentation for ExpectedField.format
     * @param formatIn String
     */
    public void setFormat(String formatIn)
    {
        this.format = formatIn;
    }


    @Column(name = "MATCH_TO", unique = false, nullable = true, insertable = true, updatable = true)
    private String matchTo;

    /**
     * TODO: Model Documentation for ExpectedField.matchTo
     * @return this.matchTo String
     */
    public String getMatchTo()
    {
        return this.matchTo;
    }

    /**
     * TODO: Model Documentation for ExpectedField.matchTo
     * @param matchToIn String
     */
    public void setMatchTo(String matchToIn)
    {
        this.matchTo = matchToIn;
    }


    @Column(name = "TARGET_TYPE", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private TargetEntity targetType;

    /**
     * TODO: Model Documentation for ExpectedField.targetType
     * @return this.targetType TargetEntity
     */
    public TargetEntity getTargetType()
    {
        return this.targetType;
    }

    /**
     * TODO: Model Documentation for ExpectedField.targetType
     * @param targetTypeIn TargetEntity
     */
    public void setTargetType(TargetEntity targetTypeIn)
    {
        this.targetType = targetTypeIn;
    }


    // Kept in veritas-processor (FieldDefinitionSource), not in a column here
    @Transient
    private Boolean many = Boolean.valueOf(false);

    /**
     * TODO: Model Documentation for ExpectedField.many
     * @return this.many Boolean
     */
    public Boolean getMany()
    {
        return this.many;
    }

    /**
     * TODO: Model Documentation for ExpectedField.many
     * @param manyIn Boolean
     */
    public void setMany(Boolean manyIn)
    {
        this.many = manyIn;
    }


    // Kept in veritas-processor (FieldDefinitionSource), not in a column here
    @Transient
    private ExpectedFieldType fieldType;

    /**
     * TODO: Model Documentation for ExpectedField.fieldType
     * @return this.fieldType ExpectedFieldType
     */
    public ExpectedFieldType getFieldType()
    {
        return this.fieldType;
    }

    /**
     * TODO: Model Documentation for ExpectedField.fieldType
     * @param fieldTypeIn ExpectedFieldType
     */
    public void setFieldType(ExpectedFieldType fieldTypeIn)
    {
        this.fieldType = fieldTypeIn;
    }


    // Kept in veritas-processor (FieldDefinitionSource), not in a column here
    @Transient
    private String fieldLabel;

    /**
     * TODO: Model Documentation for ExpectedField.fieldLabel
     * @return this.fieldLabel String
     */
    public String getFieldLabel()
    {
        return this.fieldLabel;
    }

    /**
     * TODO: Model Documentation for ExpectedField.fieldLabel
     * @param fieldLabelIn String
     */
    public void setFieldLabel(String fieldLabelIn)
    {
        this.fieldLabel = fieldLabelIn;
    }


    @Column(name = "EXACT_MATCH", unique = false, nullable = true, insertable = true, updatable = true)
    private Boolean exactMatch = Boolean.valueOf(false);

    /**
     * TODO: Model Documentation for ExpectedField.exactMatch
     * @return this.exactMatch Boolean
     */
    public Boolean getExactMatch()
    {
        return this.exactMatch;
    }

    /**
     * TODO: Model Documentation for ExpectedField.exactMatch
     * @param exactMatchIn Boolean
     */
    public void setExactMatch(Boolean exactMatchIn)
    {
        this.exactMatch = exactMatchIn;
    }

    // Generate 3 associations
    @OneToMany( mappedBy = "expiryField")
    private Collection<DocumentType> documentTypes = new HashSet<DocumentType>();
    
    /**
     * TODO: Model Documentation for DocumentType
     * @return this.documentTypes Collection<DocumentType>
     */

    public Collection<DocumentType> getDocumentTypes()
    {
        return this.documentTypes;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param documentTypesIn Collection<DocumentType>
     */
    public void setDocumentTypes(Collection<DocumentType> documentTypesIn)
    {
        this.documentTypes = documentTypesIn;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToAdd DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addDocumentTypes(DocumentType elementToAdd)
    {
        return this.documentTypes.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToRemove DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeDocumentTypes(DocumentType elementToRemove)
    {
        return this.documentTypes.remove(elementToRemove);
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

    @ManyToMany( mappedBy = "expectedFields")
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

    /**
     * Returns <code>true</code> if the argument is an ExpectedField instance and all identifiers for this entity
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
        if (!(object instanceof ExpectedField))
        {
            return false;
        }
        final ExpectedField that = (ExpectedField)object;
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
     * Constructs new instances of {@link ExpectedField}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link ExpectedField}.
         * @return new ExpectedField()
         */
        public static ExpectedField newInstance()
        {
            return new ExpectedField();
        }

        /**
         * Constructs a new instance of {@link ExpectedField}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param field String
         * @param targetType TargetEntity
         * @param fieldType ExpectedFieldType
         * @param documentType DocumentType
         * @return newInstance
         */
        public static ExpectedField newInstance(LocalDateTime createdAt, String createdBy, String field, TargetEntity targetType, ExpectedFieldType fieldType, DocumentType documentType)
        {
            final ExpectedField entity = new ExpectedField();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setField(field);
            entity.setTargetType(targetType);
            entity.setFieldType(fieldType);
            entity.setDocumentType(documentType);
            return entity;
        }

        /**
         * Constructs a new instance of {@link ExpectedField}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param field String
         * @param mandatory Boolean
         * @param format String
         * @param matchTo String
         * @param targetType TargetEntity
         * @param many Boolean
         * @param fieldType ExpectedFieldType
         * @param fieldLabel String
         * @param exactMatch Boolean
         * @param documentTypes Collection<DocumentType>
         * @param documentType DocumentType
         * @param verificationDataConfigs List<VerificationDataConfig>
         * @return newInstance ExpectedField
         */
        public static ExpectedField newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, String field, Boolean mandatory, String format, String matchTo, TargetEntity targetType, Boolean many, ExpectedFieldType fieldType, String fieldLabel, Boolean exactMatch, Collection<DocumentType> documentTypes, DocumentType documentType, List<VerificationDataConfig> verificationDataConfigs)
        {
            final ExpectedField entity = new ExpectedField();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setField(field);
            entity.setMandatory(mandatory);
            entity.setFormat(format);
            entity.setMatchTo(matchTo);
            entity.setTargetType(targetType);
            entity.setMany(many);
            entity.setFieldType(fieldType);
            entity.setFieldLabel(fieldLabel);
            entity.setExactMatch(exactMatch);
            entity.setDocumentTypes(documentTypes);
            entity.setDocumentType(documentType);
            entity.setVerificationDataConfigs(verificationDataConfigs);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(ExpectedField other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getField() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getField().compareTo(other.getField()));
            }
            if (this.getMandatory() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMandatory().compareTo(other.getMandatory()));
            }
            if (this.getFormat() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFormat().compareTo(other.getFormat()));
            }
            if (this.getMatchTo() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMatchTo().compareTo(other.getMatchTo()));
            }
            if (this.getTargetType() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTargetType().compareTo(other.getTargetType()));
            }
            if (this.getMany() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMany().compareTo(other.getMany()));
            }
            if (this.getFieldType() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFieldType().compareTo(other.getFieldType()));
            }
            if (this.getFieldLabel() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFieldLabel().compareTo(other.getFieldLabel()));
            }
            if (this.getExactMatch() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getExactMatch().compareTo(other.getExactMatch()));
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
        sb.append("ExpectedField(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" field=").append(getField());
        sb.append(" mandatory=").append(getMandatory());
        sb.append(" format=").append(getFormat());
        sb.append(" matchTo=").append(getMatchTo());
        sb.append(" targetType=").append(getTargetType());
        sb.append(" many=").append(getMany());
        sb.append(" fieldType=").append(getFieldType());
        sb.append(" fieldLabel=").append(getFieldLabel());
        sb.append(" exactMatch=").append(getExactMatch());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}