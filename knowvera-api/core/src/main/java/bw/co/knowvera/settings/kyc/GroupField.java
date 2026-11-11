// false
// true
package bw.co.knowvera.settings.kyc;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.document.type.field.ExpectedField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for GroupField
 */
@Entity
@Table(
    name = "GROUP_FIELD")
// Uncomment to enable caching for GroupField
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQuery(name = "GroupField.findAll", query = "SELECT g FROM GroupField AS g")
public class GroupField
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 3156053000480715759L;

    // Generate 2 attributes

    @Column(name = "POSITION", unique = false, nullable = true, insertable = true, updatable = true)
    private Integer position;

    /**
     * TODO: Model Documentation for GroupField.position
     * @return this.position Integer
     */
    public Integer getPosition()
    {
        return this.position;
    }

    /**
     * TODO: Model Documentation for GroupField.position
     * @param positionIn Integer
     */
    public void setPosition(Integer positionIn)
    {
        this.position = positionIn;
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for GroupField.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for GroupField.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }

    // Generate 2 associations
    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "KYC_FIELD_GROUP_FK")
    private KycFieldGroup kycFieldGroup;

    /**
     * TODO: Model Documentation for KycFieldGroup
     * @return this.kycFieldGroup KycFieldGroup
     */

    public KycFieldGroup getKycFieldGroup()
    {
        return this.kycFieldGroup;
    }

    /**
     * TODO: Model Documentation for KycFieldGroup
     * @param kycFieldGroupIn KycFieldGroup
     */
    public void setKycFieldGroup(KycFieldGroup kycFieldGroupIn)
    {
        this.kycFieldGroup = kycFieldGroupIn;
    }

    @OneToOne(optional = false)
    @JoinColumn(name = "EXPECTED_FIELD_FK", unique = false)
    private ExpectedField expectedField;

    /**
     * TODO: Model Documentation for ExpectedField
     * @return this.expectedField ExpectedField
     */

    public ExpectedField getExpectedField()
    {
        return this.expectedField;
    }

    /**
     * TODO: Model Documentation for ExpectedField
     * @param expectedFieldIn ExpectedField
     */
    public void setExpectedField(ExpectedField expectedFieldIn)
    {
        this.expectedField = expectedFieldIn;
    }

    /**
     * Returns <code>true</code> if the argument is an GroupField instance and all identifiers for this entity
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
        if (!(object instanceof GroupField))
        {
            return false;
        }
        final GroupField that = (GroupField)object;
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
     * Constructs new instances of {@link GroupField}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link GroupField}.
         * @return new GroupField()
         */
        public static GroupField newInstance()
        {
            return new GroupField();
        }

        /**
         * Constructs a new instance of {@link GroupField}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param kycFieldGroup KycFieldGroup
         * @param expectedField ExpectedField
         * @return newInstance
         */
        public static GroupField newInstance(LocalDateTime createdAt, String createdBy, KycFieldGroup kycFieldGroup, ExpectedField expectedField)
        {
            final GroupField entity = new GroupField();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setKycFieldGroup(kycFieldGroup);
            entity.setExpectedField(expectedField);
            return entity;
        }

        /**
         * Constructs a new instance of {@link GroupField}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param position Integer
         * @param kycFieldGroup KycFieldGroup
         * @param expectedField ExpectedField
         * @return newInstance GroupField
         */
        public static GroupField newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, Integer position, KycFieldGroup kycFieldGroup, ExpectedField expectedField)
        {
            final GroupField entity = new GroupField();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setPosition(position);
            entity.setKycFieldGroup(kycFieldGroup);
            entity.setExpectedField(expectedField);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(GroupField other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getPosition() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPosition().compareTo(other.getPosition()));
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
        sb.append("GroupField(");
        sb.append(super.toString());
        sb.append(" position=").append(getPosition());
        sb.append(" id=").append(getId());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}