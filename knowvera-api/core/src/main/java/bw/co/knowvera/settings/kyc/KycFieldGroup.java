// false
// true
package bw.co.knowvera.settings.kyc;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.settings.Settings;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for KycFieldGroup
 */
@Entity
@Table(
    name = "KYC_FIELD_GROUP")
// Uncomment to enable caching for KycFieldGroup
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQuery(name = "KycFieldGroup.findAll", query = "SELECT k FROM KycFieldGroup AS k")
public class KycFieldGroup
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 467086217188218910L;

    // Generate 5 attributes

    @Column(name = "LABEL", unique = false, nullable = false, insertable = true, updatable = true)
    private String label;

    /**
     * TODO: Model Documentation for KycFieldGroup.label
     * @return this.label String
     */
    public String getLabel()
    {
        return this.label;
    }

    /**
     * TODO: Model Documentation for KycFieldGroup.label
     * @param labelIn String
     */
    public void setLabel(String labelIn)
    {
        this.label = labelIn;
    }


    @Column(name = "DESCRIPTION", unique = false, nullable = false, insertable = true, updatable = true)
    private String description;

    /**
     * TODO: Model Documentation for KycFieldGroup.description
     * @return this.description String
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * TODO: Model Documentation for KycFieldGroup.description
     * @param descriptionIn String
     */
    public void setDescription(String descriptionIn)
    {
        this.description = descriptionIn;
    }


    @Column(name = "TARGET_TYPE", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private TargetEntity targetType;

    /**
     * TODO: Model Documentation for KycFieldGroup.targetType
     * @return this.targetType TargetEntity
     */
    public TargetEntity getTargetType()
    {
        return this.targetType;
    }

    /**
     * TODO: Model Documentation for KycFieldGroup.targetType
     * @param targetTypeIn TargetEntity
     */
    public void setTargetType(TargetEntity targetTypeIn)
    {
        this.targetType = targetTypeIn;
    }


    @Column(name = "POSITION", unique = false, nullable = true, insertable = true, updatable = true)
    private Integer position;

    /**
     * TODO: Model Documentation for KycFieldGroup.position
     * @return this.position Integer
     */
    public Integer getPosition()
    {
        return this.position;
    }

    /**
     * TODO: Model Documentation for KycFieldGroup.position
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
     * TODO: Model Documentation for KycFieldGroup.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for KycFieldGroup.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }

    // Generate 3 associations
    @OneToMany(cascade = {CascadeType.ALL} , mappedBy = "kycFieldGroup",  fetch = FetchType.EAGER, orphanRemoval = true)
    private List<GroupField> groupFields = new ArrayList<GroupField>();
    
    /**
     * TODO: Model Documentation for GroupField
     * @return this.groupFields List<GroupField>
     */

    public List<GroupField> getGroupFields()
    {
        return this.groupFields;
    }

    /**
     * TODO: Model Documentation for GroupField
     * @param groupFieldsIn List<GroupField>
     */
    public void setGroupFields(List<GroupField> groupFieldsIn)
    {
        this.groupFields = groupFieldsIn;
    }

    /**
     * TODO: Model Documentation for GroupField
     * @param elementToAdd GroupField
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addGroupFields(GroupField elementToAdd)
    {
        return this.groupFields.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for GroupField
     * @param elementToRemove GroupField
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeGroupFields(GroupField elementToRemove)
    {
        return this.groupFields.remove(elementToRemove);
    }

    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "INDIVIDUAL_SETTINGS_FK")
    private Settings individualSettings;

    /**
     * TODO: Model Documentation for Settings
     * @return this.individualSettings Settings
     */

    public Settings getIndividualSettings()
    {
        return this.individualSettings;
    }

    /**
     * TODO: Model Documentation for Settings
     * @param individualSettingsIn Settings
     */
    public void setIndividualSettings(Settings individualSettingsIn)
    {
        this.individualSettings = individualSettingsIn;
    }

    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANISATION_SETTINGS_FK")
    private Settings organisationSettings;

    /**
     * TODO: Model Documentation for Settings
     * @return this.organisationSettings Settings
     */

    public Settings getOrganisationSettings()
    {
        return this.organisationSettings;
    }

    /**
     * TODO: Model Documentation for Settings
     * @param organisationSettingsIn Settings
     */
    public void setOrganisationSettings(Settings organisationSettingsIn)
    {
        this.organisationSettings = organisationSettingsIn;
    }

    /**
     * Returns <code>true</code> if the argument is an KycFieldGroup instance and all identifiers for this entity
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
        if (!(object instanceof KycFieldGroup))
        {
            return false;
        }
        final KycFieldGroup that = (KycFieldGroup)object;
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
     * Constructs new instances of {@link KycFieldGroup}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link KycFieldGroup}.
         * @return new KycFieldGroup()
         */
        public static KycFieldGroup newInstance()
        {
            return new KycFieldGroup();
        }

        /**
         * Constructs a new instance of {@link KycFieldGroup}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param label String
         * @param description String
         * @param targetType TargetEntity
         * @param individualSettings Settings
         * @param organisationSettings Settings
         * @return newInstance
         */
        public static KycFieldGroup newInstance(LocalDateTime createdAt, String createdBy, String label, String description, TargetEntity targetType, Settings individualSettings, Settings organisationSettings)
        {
            final KycFieldGroup entity = new KycFieldGroup();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setLabel(label);
            entity.setDescription(description);
            entity.setTargetType(targetType);
            entity.setIndividualSettings(individualSettings);
            entity.setOrganisationSettings(organisationSettings);
            return entity;
        }

        /**
         * Constructs a new instance of {@link KycFieldGroup}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param label String
         * @param description String
         * @param targetType TargetEntity
         * @param position Integer
         * @param groupFields List<GroupField>
         * @param individualSettings Settings
         * @param organisationSettings Settings
         * @return newInstance KycFieldGroup
         */
        public static KycFieldGroup newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, String label, String description, TargetEntity targetType, Integer position, List<GroupField> groupFields, Settings individualSettings, Settings organisationSettings)
        {
            final KycFieldGroup entity = new KycFieldGroup();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setLabel(label);
            entity.setDescription(description);
            entity.setTargetType(targetType);
            entity.setPosition(position);
            entity.setGroupFields(groupFields);
            entity.setIndividualSettings(individualSettings);
            entity.setOrganisationSettings(organisationSettings);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(KycFieldGroup other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getLabel() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getLabel().compareTo(other.getLabel()));
            }
            if (this.getDescription() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDescription().compareTo(other.getDescription()));
            }
            if (this.getTargetType() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTargetType().compareTo(other.getTargetType()));
            }
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
        sb.append("KycFieldGroup(");
        sb.append(super.toString());
        sb.append(" label=").append(getLabel());
        sb.append(" description=").append(getDescription());
        sb.append(" targetType=").append(getTargetType());
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