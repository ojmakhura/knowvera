package bw.co.knowvera.sequence;

import bw.co.knowvera.TargetEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for SequenceGeneratorDTO
 */
public class SequenceGeneratorDTO
    implements Serializable, Comparable<SequenceGeneratorDTO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -9098147367564503243L;

    // Class attributes
    /**
     * TODO: Model Documentation for SequenceGeneratorDTO.id
     */
    protected String id;
    /**
     * TODO: Model Documentation for SequenceGeneratorDTO.name
     */
    protected String name;
    /**
     * TODO: Model Documentation for SequenceGeneratorDTO.targetEntity
     */
    protected TargetEntity targetEntity;
    /**
     * TODO: Model Documentation for SequenceGeneratorDTO.sequenceParts
     */
    @Nullable
    protected List<SequencePartDTO> sequenceParts;

    /** Default Constructor with no properties */
    public SequenceGeneratorDTO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param idIn String TODO: Model Documentation for SequenceGeneratorDTO.id
     * @param nameIn String TODO: Model Documentation for SequenceGeneratorDTO.name
     * @param targetEntityIn TargetEntity TODO: Model Documentation for SequenceGeneratorDTO.targetEntity
     */
    public SequenceGeneratorDTO(final String idIn, final String nameIn, final TargetEntity targetEntityIn)
    {
        this.id = idIn;
        this.name = nameIn;
        this.targetEntity = targetEntityIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param nameIn String
     * @param targetEntityIn TargetEntity
     * @param sequencePartsIn List<SequencePartDTO>
     */
    public SequenceGeneratorDTO(final String idIn, final String nameIn, final TargetEntity targetEntityIn, final List<SequencePartDTO> sequencePartsIn)
    {
        this.id = idIn;
        this.name = nameIn;
        this.targetEntity = targetEntityIn;
        this.sequenceParts = sequencePartsIn;
    }

    /**
     * Copies constructor from other SequenceGeneratorDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public SequenceGeneratorDTO(final SequenceGeneratorDTO otherBean)
    {
        this.id = otherBean.getId();
        this.name = otherBean.getName();
        this.targetEntity = otherBean.getTargetEntity();
        this.sequenceParts = otherBean.getSequenceParts();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final SequenceGeneratorDTO otherBean)
    {
        if (null != otherBean)
        {
            this.setId(otherBean.getId());
            this.setName(otherBean.getName());
            this.setTargetEntity(otherBean.getTargetEntity());
            this.setSequenceParts(otherBean.getSequenceParts());
        }
    }

    /**
     * TODO: Model Documentation for SequenceGeneratorDTO.id
     * Get the id Attribute
     * @return id String
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for SequenceGeneratorDTO.id
     * @param value String
     */
    public void setId(final String value)
    {
        this.id = value;
    }

    /**
     * TODO: Model Documentation for SequenceGeneratorDTO.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for SequenceGeneratorDTO.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for SequenceGeneratorDTO.targetEntity
     * Get the targetEntity Attribute
     * @return targetEntity TargetEntity
     */
    public TargetEntity getTargetEntity()
    {
        return this.targetEntity;
    }

    /**
     * TODO: Model Documentation for SequenceGeneratorDTO.targetEntity
     * @param value TargetEntity
     */
    public void setTargetEntity(final TargetEntity value)
    {
        this.targetEntity = value;
    }

    /**
     * TODO: Model Documentation for SequenceGeneratorDTO.sequenceParts
     * Get the sequenceParts Attribute
     * @return sequenceParts List<SequencePartDTO>
     */
    public List<SequencePartDTO> getSequenceParts()
    {
        if (this.sequenceParts == null)
        {
            this.sequenceParts = new ArrayList<SequencePartDTO>();
        }
        return this.sequenceParts;
    }

    /**
     * TODO: Model Documentation for SequenceGeneratorDTO.sequenceParts
     * @param value List<SequencePartDTO>
     */
    public void setSequenceParts(final List<SequencePartDTO> value)
    {
        this.sequenceParts = value;
    }

    /**
     * @param object to compare this object against
     * @return boolean if equal
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object==null || object.getClass() != this.getClass())
        {
             return false;
        }
        // Check if the same object instance
        if (object==this)
        {
            return true;
        }
        SequenceGeneratorDTO rhs = (SequenceGeneratorDTO) object;
        return new EqualsBuilder()
            .append(this.getId(), rhs.getId())
            .append(this.getName(), rhs.getName())
            .append(this.getTargetEntity(), rhs.getTargetEntity())
            .append(this.getSequenceParts(), rhs.getSequenceParts())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final SequenceGeneratorDTO object)
    {
        if (object==null)
        {
            return -1;
        }
        // Check if the same object instance
        if (object==this)
        {
            return 0;
        }
        return new CompareToBuilder()
            .append(this.getId(), object.getId())
            .append(this.getName(), object.getName())
            .append(this.getTargetEntity(), object.getTargetEntity())
            .append(this.getSequenceParts(), object.getSequenceParts())
            .toComparison();
    }

    /**
     * @return int hashCode value
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(1249046965, -82296885)
            .append(this.getId())
            .append(this.getName())
            .append(this.getTargetEntity())
            .append(this.getSequenceParts())
            .toHashCode();
    }

    /**
     * @return String representation of object
     * @see Object#toString()
     */
    @Override
    public String toString()
    {

        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("\t\"id\" : \"" + this.getId() + "\"");
        builder.append("\t,\"name\" : \"" + this.getName() + "\"");
        builder.append("\t,\"targetEntity\" : \"" + this.getTargetEntity() + "\"");
        builder.append("\t,\"sequenceParts\" : \"" + this.getSequenceParts() + "\"");
        builder.append("}");
        return builder.toString();

    }

    /**
     * Compares the properties of this instance to the properties of the argument. This method will return
     * {@code false} as soon as it detects that the argument is {@code null} or not of the same type as
     * (or a sub-type of) this instance's type.
     *
     * <p/>For array, collection or map properties the comparison will be done one level deep, in other words:
     * the elements will be compared using the {@code equals()} operation.
     *
     * <p/>Note that two properties will be considered equal when both values are {@code null}.
     *
     * @param thatObject the object containing the properties to compare against this instance
     * @return this method will return {@code true} in case the argument has the same type as this class, or is a
     *      sub-type of this class and all properties as found on this class have equal values when queried on that
     *      argument instance; in all other cases this method will return {@code false}
     */
    public boolean equalProperties(final Object thatObject)
    {
        if (thatObject == null || !this.getClass().isAssignableFrom(thatObject.getClass()))
        {
            return false;
        }

        final SequenceGeneratorDTO that = (SequenceGeneratorDTO)thatObject;
        return
            equal(this.getId(), that.getId())
            && equal(this.getName(), that.getName())
            && equal(this.getTargetEntity(), that.getTargetEntity())
            && equal(this.getSequenceParts(), that.getSequenceParts())
        ;
    }

    /**
     * This is a convenient helper method which is able to detect whether or not two values are equal. Two values
     * are equal when they are both {@code null}, are arrays of the same length with equal elements or are
     * equal objects (this includes {@link java.util.Collection} and {@link java.util.Map} instances).
     *
     * <p/>Note that for array, collection or map instances the comparison runs one level deep.
     *
     * @param first the first object to compare, may be {@code null}
     * @param second the second object to compare, may be {@code null}
     * @return this method will return {@code true} in case both objects are equal as explained above;
     *      in all other cases this method will return {@code false}
     */
    protected static boolean equal(final Object first, final Object second)
    {
        final boolean equal;

        if (first == null)
        {
            equal = (second == null);
        }
        else if (first.getClass().isArray() && (second != null) && second.getClass().isArray())
        {
            equal = Arrays.equals((Object[])first, (Object[])second);
        }
        else // note that the following also covers java.util.Collection and java.util.Map
        {
            equal = first.equals(second);
        }

        return equal;
    }

}