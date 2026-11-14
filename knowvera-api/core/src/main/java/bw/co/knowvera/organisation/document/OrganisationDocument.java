// false
// true
package bw.co.knowvera.organisation.document;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.type.DocumentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for OrganisationDocument
 */
@Entity
@Table(
    name = "ORGANISATION_DOCUMENT", uniqueConstraints = {
        @UniqueConstraint(
            name="ENTITY_DOC_TYPE_UNIQUE",
            columnNames={
                "TARGET",
                "TARGET_ID"
            }
        )
    }
)
// Uncomment to enable caching for OrganisationDocument
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQuery(name = "OrganisationDocument.findAll", query = "SELECT o FROM OrganisationDocument AS o")
public class OrganisationDocument
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -7408576752180051296L;

    // Generate 4 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for OrganisationDocument.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for OrganisationDocument.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private OrganisationDocumentStatus status = OrganisationDocumentStatus.ACTIVE;

    /**
     * TODO: Model Documentation for
     * OrganisationDocument.status
     * @return this.status OrganisationDocumentStatus
     */
    public OrganisationDocumentStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocument.status
     * @param statusIn OrganisationDocumentStatus
     */
    public void setStatus(OrganisationDocumentStatus statusIn)
    {
        this.status = statusIn;
    }


    @Column(name = "TARGET", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private TargetEntity target;

    /**
     * TODO: Model Documentation for
     * OrganisationDocument.target
     * @return this.target TargetEntity
     */
    public TargetEntity getTarget()
    {
        return this.target;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocument.target
     * @param targetIn TargetEntity
     */
    public void setTarget(TargetEntity targetIn)
    {
        this.target = targetIn;
    }


    @Column(name = "TARGET_ID", unique = false, nullable = false, insertable = true, updatable = true)
    private String targetId;

    /**
     * TODO: Model Documentation for
     * OrganisationDocument.targetId
     * @return this.targetId String
     */
    public String getTargetId()
    {
        return this.targetId;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocument.targetId
     * @param targetIdIn String
     */
    public void setTargetId(String targetIdIn)
    {
        this.targetId = targetIdIn;
    }

    // Generate 1 associations
    @OneToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCUMENT_TYPE_FK", unique = true)
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
     * Returns <code>true</code> if the argument is an OrganisationDocument instance and all identifiers for this entity
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
        if (!(object instanceof OrganisationDocument))
        {
            return false;
        }
        final OrganisationDocument that = (OrganisationDocument)object;
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
     * Constructs new instances of {@link OrganisationDocument}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link OrganisationDocument}.
         * @return new OrganisationDocument()
         */
        public static OrganisationDocument newInstance()
        {
            return new OrganisationDocument();
        }

        /**
         * Constructs a new instance of {@link OrganisationDocument}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param status OrganisationDocumentStatus
         * @param target TargetEntity
         * @param targetId String
         * @param documentType DocumentType
         * @return newInstance
         */
        public static OrganisationDocument newInstance(LocalDateTime createdAt, String createdBy, OrganisationDocumentStatus status, TargetEntity target, String targetId, DocumentType documentType)
        {
            final OrganisationDocument entity = new OrganisationDocument();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setStatus(status);
            entity.setTarget(target);
            entity.setTargetId(targetId);
            entity.setDocumentType(documentType);
            return entity;
        }

        /**
         * Constructs a new instance of {@link OrganisationDocument}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param status OrganisationDocumentStatus
         * @param target TargetEntity
         * @param targetId String
         * @param documentType DocumentType
         * @return newInstance OrganisationDocument
         */
        public static OrganisationDocument newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, OrganisationDocumentStatus status, TargetEntity target, String targetId, DocumentType documentType)
        {
            final OrganisationDocument entity = new OrganisationDocument();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setStatus(status);
            entity.setTarget(target);
            entity.setTargetId(targetId);
            entity.setDocumentType(documentType);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(OrganisationDocument other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getStatus().compareTo(other.getStatus()));
            }
            if (this.getTarget() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTarget().compareTo(other.getTarget()));
            }
            if (this.getTargetId() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTargetId().compareTo(other.getTargetId()));
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
        sb.append("OrganisationDocument(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" status=").append(getStatus());
        sb.append(" target=").append(getTarget());
        sb.append(" targetId=").append(getTargetId());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}