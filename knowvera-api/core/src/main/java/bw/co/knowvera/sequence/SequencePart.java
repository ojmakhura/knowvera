// false
// $entity.generalization.embeddableSuperclass
package bw.co.knowvera.sequence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;

/**
 * TODO: Model Documentation for SequencePart
 */
@Entity
@Table(
    name = "SEQUENCE_PART", uniqueConstraints = {
        @UniqueConstraint(
            name="SEQUENCE_UNIQUE",
            columnNames={
                "POSITION",
                "NAME",
                "SEQUENCE_GENERATOR_FK"
            }
        )
    }
)
// Uncomment to enable caching for SequencePart
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQuery(name = "SequencePart.findAll", query = "SELECT s FROM SequencePart AS s")
public class SequencePart
    implements Serializable, Comparable<SequencePart>{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 6690882019162802557L;

    // Generate 9 attributes

    @Column(name = "POSITION", unique = false, nullable = true, insertable = true, updatable = true)
    private Integer position;

    /**
     * TODO: Model Documentation for SequencePart.position
     * @return this.position Integer
     */
    public Integer getPosition()
    {
        return this.position;
    }

    /**
     * TODO: Model Documentation for SequencePart.position
     * @param positionIn Integer
     */
    public void setPosition(Integer positionIn)
    {
        this.position = positionIn;
    }


    @Column(name = "INITIAL_VALUE", unique = false, nullable = true, insertable = true, updatable = true)
    private String initialValue;

    /**
     * TODO: Model Documentation for SequencePart.initialValue
     * @return this.initialValue String
     */
    public String getInitialValue()
    {
        return this.initialValue;
    }

    /**
     * TODO: Model Documentation for SequencePart.initialValue
     * @param initialValueIn String
     */
    public void setInitialValue(String initialValueIn)
    {
        this.initialValue = initialValueIn;
    }


    @Column(name = "TYPE", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private SequencePartType type;

    /**
     * TODO: Model Documentation for SequencePart.type
     * @return this.type SequencePartType
     */
    public SequencePartType getType()
    {
        return this.type;
    }

    /**
     * TODO: Model Documentation for SequencePart.type
     * @param typeIn SequencePartType
     */
    public void setType(SequencePartType typeIn)
    {
        this.type = typeIn;
    }


    @Column(name = "MIN", unique = false, nullable = true, insertable = true, updatable = true)
    private String min;

    /**
     * TODO: Model Documentation for SequencePart.min
     * @return this.min String
     */
    public String getMin()
    {
        return this.min;
    }

    /**
     * TODO: Model Documentation for SequencePart.min
     * @param minIn String
     */
    public void setMin(String minIn)
    {
        this.min = minIn;
    }


    @Column(name = "MAX", unique = false, nullable = true, insertable = true, updatable = true)
    private String max;

    /**
     * TODO: Model Documentation for SequencePart.max
     * @return this.max String
     */
    public String getMax()
    {
        return this.max;
    }

    /**
     * TODO: Model Documentation for SequencePart.max
     * @param maxIn String
     */
    public void setMax(String maxIn)
    {
        this.max = maxIn;
    }


    @Column(name = "RANDOMISED", unique = false, nullable = true, insertable = true, updatable = true)
    private Boolean randomised = Boolean.valueOf(false);

    /**
     * TODO: Model Documentation for SequencePart.randomised
     * @return this.randomised Boolean
     */
    public Boolean getRandomised()
    {
        return this.randomised;
    }

    /**
     * TODO: Model Documentation for SequencePart.randomised
     * @param randomisedIn Boolean
     */
    public void setRandomised(Boolean randomisedIn)
    {
        this.randomised = randomisedIn;
    }


    @Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true)
    private String name;

    /**
     * TODO: Model Documentation for SequencePart.name
     * @return this.name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for SequencePart.name
     * @param nameIn String
     */
    public void setName(String nameIn)
    {
        this.name = nameIn;
    }


    @Column(name = "CURRENT_VALUE", unique = false, nullable = true, insertable = true, updatable = true)
    private String currentValue;

    /**
     * TODO: Model Documentation for SequencePart.currentValue
     * @return this.currentValue String
     */
    public String getCurrentValue()
    {
        return this.currentValue;
    }

    /**
     * TODO: Model Documentation for SequencePart.currentValue
     * @param currentValueIn String
     */
    public void setCurrentValue(String currentValueIn)
    {
        this.currentValue = currentValueIn;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQUENCE_PART_SEQ")
    @jakarta.persistence.SequenceGenerator(name="SEQUENCE_PART_SEQ", sequenceName="SEQUENCE_PART_SEQ", allocationSize=1)

    @Column(name = "ID", unique = false, nullable = true, insertable = true, updatable = true)
    private Long id;

    /**
     * TODO: Model Documentation for SequencePart.id
     * @return this.id Long
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for SequencePart.id
     * @param idIn Long
     */
    public void setId(Long idIn)
    {
        this.id = idIn;
    }

    // Generate 1 associations
    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "SEQUENCE_GENERATOR_FK")
    private SequenceGenerator sequenceGenerator;

    /**
     * TODO: Model Documentation for SequenceGenerator
     * @return this.sequenceGenerator SequenceGenerator
     */

    public SequenceGenerator getSequenceGenerator()
    {
        return this.sequenceGenerator;
    }

    /**
     * TODO: Model Documentation for SequenceGenerator
     * @param sequenceGeneratorIn SequenceGenerator
     */
    public void setSequenceGenerator(SequenceGenerator sequenceGeneratorIn)
    {
        this.sequenceGenerator = sequenceGeneratorIn;
    }

    /**
     * Returns <code>true</code> if the argument is an SequencePart instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof SequencePart))
        {
            return false;
        }
        final SequencePart that = (SequencePart)object;
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
     * Constructs new instances of {@link SequencePart}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link SequencePart}.
         * @return new SequencePart()
         */
        public static SequencePart newInstance()
        {
            return new SequencePart();
        }

        /**
         * Constructs a new instance of {@link SequencePart}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param type SequencePartType
         * @param name String
         * @param sequenceGenerator SequenceGenerator
         * @return newInstance
         */
        public static SequencePart newInstance(SequencePartType type, String name, SequenceGenerator sequenceGenerator)
        {
            final SequencePart entity = new SequencePart();
            entity.setType(type);
            entity.setName(name);
            entity.setSequenceGenerator(sequenceGenerator);
            return entity;
        }

        /**
         * Constructs a new instance of {@link SequencePart}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param position Integer
         * @param initialValue String
         * @param type SequencePartType
         * @param min String
         * @param max String
         * @param randomised Boolean
         * @param name String
         * @param currentValue String
         * @param sequenceGenerator SequenceGenerator
         * @return newInstance SequencePart
         */
        public static SequencePart newInstance(Integer position, String initialValue, SequencePartType type, String min, String max, Boolean randomised, String name, String currentValue, SequenceGenerator sequenceGenerator)
        {
            final SequencePart entity = new SequencePart();
            entity.setPosition(position);
            entity.setInitialValue(initialValue);
            entity.setType(type);
            entity.setMin(min);
            entity.setMax(max);
            entity.setRandomised(randomised);
            entity.setName(name);
            entity.setCurrentValue(currentValue);
            entity.setSequenceGenerator(sequenceGenerator);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    @Override
    public int compareTo(SequencePart other)
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
            if (this.getInitialValue() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getInitialValue().compareTo(other.getInitialValue()));
            }
            if (this.getType() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getType().compareTo(other.getType()));
            }
            if (this.getMin() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMin().compareTo(other.getMin()));
            }
            if (this.getMax() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMax().compareTo(other.getMax()));
            }
            if (this.getRandomised() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRandomised().compareTo(other.getRandomised()));
            }
            if (this.getName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getName().compareTo(other.getName()));
            }
            if (this.getCurrentValue() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getCurrentValue().compareTo(other.getCurrentValue()));
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
        sb.append("SequencePart(");
        sb.append(" position=").append(getPosition());
        sb.append(" initialValue=").append(getInitialValue());
        sb.append(" type=").append(getType());
        sb.append(" min=").append(getMin());
        sb.append(" max=").append(getMax());
        sb.append(" randomised=").append(getRandomised());
        sb.append(" name=").append(getName());
        sb.append(" currentValue=").append(getCurrentValue());
        sb.append(" id=").append(getId());
        sb.append(")");
        return sb.toString();
    }
}