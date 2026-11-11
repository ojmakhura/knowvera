// false
// $entity.generalization.embeddableSuperclass
package bw.co.knowvera.settings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * TODO: Model Documentation for SalaryRange
 */
@Entity
@Table(
    name = "SALARY_RANGE")
// Uncomment to enable caching for SalaryRange
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQuery(name = "SalaryRange.findAll", query = "SELECT s FROM SalaryRange AS s")
public class SalaryRange
    implements Serializable, Comparable<SalaryRange>{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -7426960831279452362L;

    // Generate 4 attributes

    @Column(name = "MIN", unique = false, nullable = true, insertable = true, updatable = true)
    private Double min;

    /**
     * TODO: Model Documentation for SalaryRange.min
     * @return this.min Double
     */
    public Double getMin()
    {
        return this.min;
    }

    /**
     * TODO: Model Documentation for SalaryRange.min
     * @param minIn Double
     */
    public void setMin(Double minIn)
    {
        this.min = minIn;
    }


    @Column(name = "MAX", unique = false, nullable = true, insertable = true, updatable = true)
    private Double max;

    /**
     * TODO: Model Documentation for SalaryRange.max
     * @return this.max Double
     */
    public Double getMax()
    {
        return this.max;
    }

    /**
     * TODO: Model Documentation for SalaryRange.max
     * @param maxIn Double
     */
    public void setMax(Double maxIn)
    {
        this.max = maxIn;
    }


    @Column(name = "ACTIVE", unique = false, nullable = true, insertable = true, updatable = true)
    private Boolean active = Boolean.valueOf(true);

    /**
     * TODO: Model Documentation for SalaryRange.active
     * @return this.active Boolean
     */
    public Boolean getActive()
    {
        return this.active;
    }

    /**
     * TODO: Model Documentation for SalaryRange.active
     * @param activeIn Boolean
     */
    public void setActive(Boolean activeIn)
    {
        this.active = activeIn;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SALARY_RANGE_SEQ")
    @SequenceGenerator(name="SALARY_RANGE_SEQ", sequenceName="SALARY_RANGE_SEQ", allocationSize=1)

    @Column(name = "ID", unique = false, nullable = true, insertable = true, updatable = true)
    private Long id;

    /**
     * TODO: Model Documentation for SalaryRange.id
     * @return this.id Long
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for SalaryRange.id
     * @param idIn Long
     */
    public void setId(Long idIn)
    {
        this.id = idIn;
    }

    // Generate 1 associations
    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "SETTINGS_FK")
    private Settings settings;

    /**
     * TODO: Model Documentation for Settings
     * @return this.settings Settings
     */

    public Settings getSettings()
    {
        return this.settings;
    }

    /**
     * TODO: Model Documentation for Settings
     * @param settingsIn Settings
     */
    public void setSettings(Settings settingsIn)
    {
        this.settings = settingsIn;
    }

    /**
     * Returns <code>true</code> if the argument is an SalaryRange instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof SalaryRange))
        {
            return false;
        }
        final SalaryRange that = (SalaryRange)object;
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
     * Constructs new instances of {@link SalaryRange}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link SalaryRange}.
         * @return new SalaryRange()
         */
        public static SalaryRange newInstance()
        {
            return new SalaryRange();
        }

        /**
         * Constructs a new instance of {@link SalaryRange}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param settings Settings
         * @return newInstance
         */
        public static SalaryRange newInstance(Settings settings)
        {
            final SalaryRange entity = new SalaryRange();
            entity.setSettings(settings);
            return entity;
        }

        /**
         * Constructs a new instance of {@link SalaryRange}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param min Double
         * @param max Double
         * @param active Boolean
         * @param settings Settings
         * @return newInstance SalaryRange
         */
        public static SalaryRange newInstance(Double min, Double max, Boolean active, Settings settings)
        {
            final SalaryRange entity = new SalaryRange();
            entity.setMin(min);
            entity.setMax(max);
            entity.setActive(active);
            entity.setSettings(settings);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    @Override
    public int compareTo(SalaryRange other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getMin() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMin().compareTo(other.getMin()));
            }
            if (this.getMax() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMax().compareTo(other.getMax()));
            }
            if (this.getActive() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getActive().compareTo(other.getActive()));
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
        sb.append("SalaryRange(");
        sb.append(" min=").append(getMin());
        sb.append(" max=").append(getMax());
        sb.append(" active=").append(getActive());
        sb.append(" id=").append(getId());
        sb.append(")");
        return sb.toString();
    }
}