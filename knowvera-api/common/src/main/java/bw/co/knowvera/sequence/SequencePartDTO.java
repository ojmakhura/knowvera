package bw.co.knowvera.sequence;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for SequencePartDTO
 */
public class SequencePartDTO
    implements Serializable, Comparable<SequencePartDTO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -299536893130810628L;

    // Class attributes
    /**
     * TODO: Model Documentation for SequencePartDTO.id
     */
    @Nullable
    protected Long id;
    /**
     * TODO: Model Documentation for SequencePartDTO.position
     */
    @Nullable
    protected Integer position;
    /**
     * TODO: Model Documentation for SequencePartDTO.initialValue
     */
    protected String initialValue;
    /**
     * TODO: Model Documentation for SequencePartDTO.type
     */
    protected SequencePartType type;
    /**
     * TODO: Model Documentation for SequencePartDTO.min
     */
    @Nullable
    protected String min;
    /**
     * TODO: Model Documentation for SequencePartDTO.max
     */
    @Nullable
    protected String max;
    /**
     * TODO: Model Documentation for SequencePartDTO.randomised
     */
    @Nullable
    protected Boolean randomised = false;
    /**
     * TODO: Model Documentation for SequencePartDTO.name
     */
    protected String name;
    /**
     * TODO: Model Documentation for SequencePartDTO.currentValue
     */
    @Nullable
    protected String currentValue;

    /** Default Constructor with no properties */
    public SequencePartDTO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param initialValueIn String TODO: Model Documentation for SequencePartDTO.initialValue
     * @param typeIn SequencePartType TODO: Model Documentation for SequencePartDTO.type
     * @param nameIn String TODO: Model Documentation for SequencePartDTO.name
     */
    public SequencePartDTO(final String initialValueIn, final SequencePartType typeIn, final String nameIn)
    {
        this.initialValue = initialValueIn;
        this.type = typeIn;
        this.name = nameIn;
    }

    /**
     * Constructor with all properties
     * @param idIn Long
     * @param positionIn Integer
     * @param initialValueIn String
     * @param typeIn SequencePartType
     * @param minIn String
     * @param maxIn String
     * @param randomisedIn Boolean
     * @param nameIn String
     * @param currentValueIn String
     */
    public SequencePartDTO(final Long idIn, final Integer positionIn, final String initialValueIn, final SequencePartType typeIn, final String minIn, final String maxIn, final Boolean randomisedIn, final String nameIn, final String currentValueIn)
    {
        this.id = idIn;
        this.position = positionIn;
        this.initialValue = initialValueIn;
        this.type = typeIn;
        this.min = minIn;
        this.max = maxIn;
        this.randomised = randomisedIn;
        this.name = nameIn;
        this.currentValue = currentValueIn;
    }

    /**
     * Copies constructor from other SequencePartDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public SequencePartDTO(final SequencePartDTO otherBean)
    {
        this.id = otherBean.getId();
        this.position = otherBean.getPosition();
        this.initialValue = otherBean.getInitialValue();
        this.type = otherBean.getType();
        this.min = otherBean.getMin();
        this.max = otherBean.getMax();
        this.randomised = otherBean.getRandomised();
        this.name = otherBean.getName();
        this.currentValue = otherBean.getCurrentValue();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final SequencePartDTO otherBean)
    {
        if (null != otherBean)
        {
            this.setId(otherBean.getId());
            this.setPosition(otherBean.getPosition());
            this.setInitialValue(otherBean.getInitialValue());
            this.setType(otherBean.getType());
            this.setMin(otherBean.getMin());
            this.setMax(otherBean.getMax());
            this.setRandomised(otherBean.getRandomised());
            this.setName(otherBean.getName());
            this.setCurrentValue(otherBean.getCurrentValue());
        }
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.id
     * Get the id Attribute
     * @return id Long
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.id
     * @param value Long
     */
    public void setId(final Long value)
    {
        this.id = value;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.position
     * Get the position Attribute
     * @return position Integer
     */
    public Integer getPosition()
    {
        return this.position;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.position
     * @param value Integer
     */
    public void setPosition(final Integer value)
    {
        this.position = value;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.initialValue
     * Get the initialValue Attribute
     * @return initialValue String
     */
    public String getInitialValue()
    {
        return this.initialValue;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.initialValue
     * @param value String
     */
    public void setInitialValue(final String value)
    {
        this.initialValue = value;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.type
     * Get the type Attribute
     * @return type SequencePartType
     */
    public SequencePartType getType()
    {
        return this.type;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.type
     * @param value SequencePartType
     */
    public void setType(final SequencePartType value)
    {
        this.type = value;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.min
     * Get the min Attribute
     * @return min String
     */
    public String getMin()
    {
        return this.min;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.min
     * @param value String
     */
    public void setMin(final String value)
    {
        this.min = value;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.max
     * Get the max Attribute
     * @return max String
     */
    public String getMax()
    {
        return this.max;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.max
     * @param value String
     */
    public void setMax(final String value)
    {
        this.max = value;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.randomised
     * Get the randomised Attribute
     * @return randomised Boolean
     */
    public Boolean getRandomised()
    {
        return this.randomised;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.randomised
     * @param value Boolean
     */
    public void setRandomised(final Boolean value)
    {
        this.randomised = value;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.currentValue
     * Get the currentValue Attribute
     * @return currentValue String
     */
    public String getCurrentValue()
    {
        return this.currentValue;
    }

    /**
     * TODO: Model Documentation for SequencePartDTO.currentValue
     * @param value String
     */
    public void setCurrentValue(final String value)
    {
        this.currentValue = value;
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
        SequencePartDTO rhs = (SequencePartDTO) object;
        return new EqualsBuilder()
            .append(this.getId(), rhs.getId())
            .append(this.getPosition(), rhs.getPosition())
            .append(this.getInitialValue(), rhs.getInitialValue())
            .append(this.getType(), rhs.getType())
            .append(this.getMin(), rhs.getMin())
            .append(this.getMax(), rhs.getMax())
            .append(this.getRandomised(), rhs.getRandomised())
            .append(this.getName(), rhs.getName())
            .append(this.getCurrentValue(), rhs.getCurrentValue())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final SequencePartDTO object)
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
            .append(this.getPosition(), object.getPosition())
            .append(this.getInitialValue(), object.getInitialValue())
            .append(this.getType(), object.getType())
            .append(this.getMin(), object.getMin())
            .append(this.getMax(), object.getMax())
            .append(this.getRandomised(), object.getRandomised())
            .append(this.getName(), object.getName())
            .append(this.getCurrentValue(), object.getCurrentValue())
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
            .append(this.getPosition())
            .append(this.getInitialValue())
            .append(this.getType())
            .append(this.getMin())
            .append(this.getMax())
            .append(this.getRandomised())
            .append(this.getName())
            .append(this.getCurrentValue())
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
        builder.append("\t,\"position\" : \"" + this.getPosition() + "\"");
        builder.append("\t,\"initialValue\" : \"" + this.getInitialValue() + "\"");
        builder.append("\t,\"type\" : \"" + this.getType() + "\"");
        builder.append("\t,\"min\" : \"" + this.getMin() + "\"");
        builder.append("\t,\"max\" : \"" + this.getMax() + "\"");
        builder.append("\t,\"randomised\" : \"" + this.getRandomised() + "\"");
        builder.append("\t,\"name\" : \"" + this.getName() + "\"");
        builder.append("\t,\"currentValue\" : \"" + this.getCurrentValue() + "\"");
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

        final SequencePartDTO that = (SequencePartDTO)thatObject;
        return
            equal(this.getId(), that.getId())
            && equal(this.getPosition(), that.getPosition())
            && equal(this.getInitialValue(), that.getInitialValue())
            && equal(this.getType(), that.getType())
            && equal(this.getMin(), that.getMin())
            && equal(this.getMax(), that.getMax())
            && equal(this.getRandomised(), that.getRandomised())
            && equal(this.getName(), that.getName())
            && equal(this.getCurrentValue(), that.getCurrentValue())
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