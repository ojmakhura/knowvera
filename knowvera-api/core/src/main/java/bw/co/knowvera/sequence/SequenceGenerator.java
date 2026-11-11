// false
// $entity.generalization.embeddableSuperclass
package bw.co.knowvera.sequence;

import bw.co.knowvera.TargetEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for SequenceGenerator
 */
@Entity
@Table(
    name = "SEQUENCE_GENERATOR")
// Uncomment to enable caching for SequenceGenerator
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "SequenceGenerator.findAll", query = "SELECT s FROM SequenceGenerator AS s"),
})
public class SequenceGenerator
    implements Serializable, Comparable<SequenceGenerator>{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 8416747553245785054L;

    // Generate 3 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for SequenceGenerator.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for SequenceGenerator.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "NAME", unique = true, nullable = false, insertable = true, updatable = true)
    private String name;

    /**
     * TODO: Model Documentation for SequenceGenerator.name
     * @return this.name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for SequenceGenerator.name
     * @param nameIn String
     */
    public void setName(String nameIn)
    {
        this.name = nameIn;
    }


    @Column(name = "TARGET_ENTITY", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private TargetEntity targetEntity;

    /**
     * TODO: Model Documentation for SequenceGenerator.targetEntity
     * @return this.targetEntity TargetEntity
     */
    public TargetEntity getTargetEntity()
    {
        return this.targetEntity;
    }

    /**
     * TODO: Model Documentation for SequenceGenerator.targetEntity
     * @param targetEntityIn TargetEntity
     */
    public void setTargetEntity(TargetEntity targetEntityIn)
    {
        this.targetEntity = targetEntityIn;
    }

    // Generate 1 associations
    @OneToMany(cascade = {CascadeType.ALL} , mappedBy = "sequenceGenerator",  fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SequencePart> sequenceParts = new ArrayList<SequencePart>();
    
    /**
     * TODO: Model Documentation for SequencePart
     * @return this.sequenceParts List<SequencePart>
     */

    public List<SequencePart> getSequenceParts()
    {
        return this.sequenceParts;
    }

    /**
     * TODO: Model Documentation for SequencePart
     * @param sequencePartsIn List<SequencePart>
     */
    public void setSequenceParts(List<SequencePart> sequencePartsIn)
    {
        this.sequenceParts = sequencePartsIn;
    }

    /**
     * TODO: Model Documentation for SequencePart
     * @param elementToAdd SequencePart
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addSequenceParts(SequencePart elementToAdd)
    {
        return this.sequenceParts.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for SequencePart
     * @param elementToRemove SequencePart
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeSequenceParts(SequencePart elementToRemove)
    {
        return this.sequenceParts.remove(elementToRemove);
    }

    /**
     * Returns <code>true</code> if the argument is an SequenceGenerator instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof SequenceGenerator))
        {
            return false;
        }
        final SequenceGenerator that = (SequenceGenerator)object;
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
     * Constructs new instances of {@link SequenceGenerator}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link SequenceGenerator}.
         * @return new SequenceGenerator()
         */
        public static SequenceGenerator newInstance()
        {
            return new SequenceGenerator();
        }

        /**
         * Constructs a new instance of {@link SequenceGenerator}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param name String
         * @param targetEntity TargetEntity
         * @return newInstance
         */
        public static SequenceGenerator newInstance(String name, TargetEntity targetEntity)
        {
            final SequenceGenerator entity = new SequenceGenerator();
            entity.setName(name);
            entity.setTargetEntity(targetEntity);
            return entity;
        }

        /**
         * Constructs a new instance of {@link SequenceGenerator}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param name String
         * @param targetEntity TargetEntity
         * @param sequenceParts List<SequencePart>
         * @return newInstance SequenceGenerator
         */
        public static SequenceGenerator newInstance(String name, TargetEntity targetEntity, List<SequencePart> sequenceParts)
        {
            final SequenceGenerator entity = new SequenceGenerator();
            entity.setName(name);
            entity.setTargetEntity(targetEntity);
            entity.setSequenceParts(sequenceParts);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    @Override
    public int compareTo(SequenceGenerator other)
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
            if (this.getTargetEntity() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTargetEntity().compareTo(other.getTargetEntity()));
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
        sb.append("SequenceGenerator(");
        sb.append(" id=").append(getId());
        sb.append(" name=").append(getName());
        sb.append(" targetEntity=").append(getTargetEntity());
        sb.append(")");
        return sb.toString();
    }
}