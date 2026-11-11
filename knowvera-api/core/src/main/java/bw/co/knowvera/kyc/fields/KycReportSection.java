// false
// $entity.generalization.embeddableSuperclass
package bw.co.knowvera.kyc.fields;

import bw.co.knowvera.kyc.KycRecord;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for KycReportSection
 */
@Entity
@Table(
    name = "KYC_REPORT_SECTION")
// Uncomment to enable caching for KycReportSection
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQuery(name = "KycReportSection.findAll", query = "SELECT k FROM KycReportSection AS k")
public class KycReportSection
    implements Serializable, Comparable<KycReportSection>{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 2342898513720332821L;

    // Generate 3 attributes

    @Column(name = "LABEL", unique = false, nullable = false, insertable = true, updatable = true)
    private String label;

    /**
     * TODO: Model Documentation for KycReportSection.label
     * @return this.label String
     */
    public String getLabel()
    {
        return this.label;
    }

    /**
     * TODO: Model Documentation for KycReportSection.label
     * @param labelIn String
     */
    public void setLabel(String labelIn)
    {
        this.label = labelIn;
    }


    @Column(name = "POSITION", unique = false, nullable = true, insertable = true, updatable = true)
    private Integer position;

    /**
     * TODO: Model Documentation for KycReportSection.position
     * @return this.position Integer
     */
    public Integer getPosition()
    {
        return this.position;
    }

    /**
     * TODO: Model Documentation for KycReportSection.position
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
     * TODO: Model Documentation for KycReportSection.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for KycReportSection.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }

    // Generate 2 associations
    @OneToMany(cascade = {CascadeType.ALL} , mappedBy = "kycReportSection",  fetch = FetchType.EAGER, orphanRemoval = true)
    private List<GroupFieldValue> groupFieldValues = new ArrayList<GroupFieldValue>();
    
    /**
     * TODO: Model Documentation for GroupFieldValue
     * @return this.groupFieldValues List<GroupFieldValue>
     */

    public List<GroupFieldValue> getGroupFieldValues()
    {
        return this.groupFieldValues;
    }

    /**
     * TODO: Model Documentation for GroupFieldValue
     * @param groupFieldValuesIn List<GroupFieldValue>
     */
    public void setGroupFieldValues(List<GroupFieldValue> groupFieldValuesIn)
    {
        this.groupFieldValues = groupFieldValuesIn;
    }

    /**
     * TODO: Model Documentation for GroupFieldValue
     * @param elementToAdd GroupFieldValue
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addGroupFieldValues(GroupFieldValue elementToAdd)
    {
        return this.groupFieldValues.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for GroupFieldValue
     * @param elementToRemove GroupFieldValue
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeGroupFieldValues(GroupFieldValue elementToRemove)
    {
        return this.groupFieldValues.remove(elementToRemove);
    }

    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "KYC_RECORD_FK")
    private KycRecord kycRecord;

    /**
     * TODO: Model Documentation for KycRecord
     * @return this.kycRecord KycRecord
     */

    public KycRecord getKycRecord()
    {
        return this.kycRecord;
    }

    /**
     * TODO: Model Documentation for KycRecord
     * @param kycRecordIn KycRecord
     */
    public void setKycRecord(KycRecord kycRecordIn)
    {
        this.kycRecord = kycRecordIn;
    }

    /**
     * Returns <code>true</code> if the argument is an KycReportSection instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof KycReportSection))
        {
            return false;
        }
        final KycReportSection that = (KycReportSection)object;
        if (this.id == null || that.getId() == null || !this.id.equals(that.getId()))
        {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers.
     */
    @Override
    public int hashCode()
    {
        int hashCode = 0;
        hashCode = 29 * hashCode + (this.id == null ? 0 : this.id.hashCode());

        return hashCode;
    }

    /**
     * Constructs new instances of {@link KycReportSection}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link KycReportSection}.
         * @return new KycReportSection()
         */
        public static KycReportSection newInstance()
        {
            return new KycReportSection();
        }

        /**
         * Constructs a new instance of {@link KycReportSection}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param label String
         * @param kycRecord KycRecord
         * @return newInstance
         */
        public static KycReportSection newInstance(String label, KycRecord kycRecord)
        {
            final KycReportSection entity = new KycReportSection();
            entity.setLabel(label);
            entity.setKycRecord(kycRecord);
            return entity;
        }

        /**
         * Constructs a new instance of {@link KycReportSection}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param label String
         * @param position Integer
         * @param groupFieldValues List<GroupFieldValue>
         * @param kycRecord KycRecord
         * @return newInstance KycReportSection
         */
        public static KycReportSection newInstance(String label, Integer position, List<GroupFieldValue> groupFieldValues, KycRecord kycRecord)
        {
            final KycReportSection entity = new KycReportSection();
            entity.setLabel(label);
            entity.setPosition(position);
            entity.setGroupFieldValues(groupFieldValues);
            entity.setKycRecord(kycRecord);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    @Override
    public int compareTo(KycReportSection other)
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
        sb.append("KycReportSection(");
        sb.append(" label=").append(getLabel());
        sb.append(" position=").append(getPosition());
        sb.append(" id=").append(getId());
        sb.append(")");
        return sb.toString();
    }
}