// false
// $entity.generalization.embeddableSuperclass
package bw.co.knowvera.kyc.fields;

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
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * TODO: Model Documentation for GroupFieldValue
 */
@Entity
@Table(
    name = "GROUP_FIELD_VALUE")
// Uncomment to enable caching for GroupFieldValue
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQuery(name = "GroupFieldValue.findAll", query = "SELECT g FROM GroupFieldValue AS g")
public class GroupFieldValue
    implements Serializable, Comparable<GroupFieldValue>{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -5781098670996775313L;

    // Generate 3 attributes

    @Column(name = "POSITION", unique = false, nullable = true, insertable = true, updatable = true)
    private Integer position;

    /**
     * TODO: Model Documentation for GroupFieldValue.position
     * @return this.position Integer
     */
    public Integer getPosition()
    {
        return this.position;
    }

    /**
     * TODO: Model Documentation for GroupFieldValue.position
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
     * TODO: Model Documentation for GroupFieldValue.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for GroupFieldValue.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "DATA", unique = false, nullable = false, insertable = true, updatable = true, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private ValueData data;

    /**
     * TODO: Model Documentation for GroupFieldValue.data
     * @return this.data ValueData
     */
    public ValueData getData()
    {
        return this.data;
    }

    /**
     * TODO: Model Documentation for GroupFieldValue.data
     * @param dataIn ValueData
     */
    public void setData(ValueData dataIn)
    {
        this.data = dataIn;
    }

    // Generate 2 associations
    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "KYC_REPORT_SECTION_FK")
    private KycReportSection kycReportSection;

    /**
     * TODO: Model Documentation for KycReportSection
     * @return this.kycReportSection KycReportSection
     */

    public KycReportSection getKycReportSection()
    {
        return this.kycReportSection;
    }

    /**
     * TODO: Model Documentation for KycReportSection
     * @param kycReportSectionIn KycReportSection
     */
    public void setKycReportSection(KycReportSection kycReportSectionIn)
    {
        this.kycReportSection = kycReportSectionIn;
    }

    @OneToOne(optional = false,  fetch = FetchType.LAZY)
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
     * Returns <code>true</code> if the argument is an GroupFieldValue instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof GroupFieldValue))
        {
            return false;
        }
        final GroupFieldValue that = (GroupFieldValue)object;
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
     * Constructs new instances of {@link GroupFieldValue}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link GroupFieldValue}.
         * @return new GroupFieldValue()
         */
        public static GroupFieldValue newInstance()
        {
            return new GroupFieldValue();
        }

        /**
         * Constructs a new instance of {@link GroupFieldValue}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param data ValueData
         * @param kycReportSection KycReportSection
         * @param expectedField ExpectedField
         * @return newInstance
         */
        public static GroupFieldValue newInstance(ValueData data, KycReportSection kycReportSection, ExpectedField expectedField)
        {
            final GroupFieldValue entity = new GroupFieldValue();
            entity.setData(data);
            entity.setKycReportSection(kycReportSection);
            entity.setExpectedField(expectedField);
            return entity;
        }

        /**
         * Constructs a new instance of {@link GroupFieldValue}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param position Integer
         * @param data ValueData
         * @param kycReportSection KycReportSection
         * @param expectedField ExpectedField
         * @return newInstance GroupFieldValue
         */
        public static GroupFieldValue newInstance(Integer position, ValueData data, KycReportSection kycReportSection, ExpectedField expectedField)
        {
            final GroupFieldValue entity = new GroupFieldValue();
            entity.setPosition(position);
            entity.setData(data);
            entity.setKycReportSection(kycReportSection);
            entity.setExpectedField(expectedField);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    @Override
    public int compareTo(GroupFieldValue other)
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
            if (this.getData() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getData().compareTo(other.getData()));
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
        sb.append("GroupFieldValue(");
        sb.append(" position=").append(getPosition());
        sb.append(" id=").append(getId());
        sb.append(" data=").append(getData());
        sb.append(")");
        return sb.toString();
    }
}