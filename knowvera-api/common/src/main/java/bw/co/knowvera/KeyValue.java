package bw.co.knowvera;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for KeyValue
 */
public class KeyValue
    implements Serializable, Comparable<KeyValue>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -5577811068208649541L;

    // Class attributes
    /**
     * TODO: Model Documentation for KeyValue.key
     */
    protected String key;
    /**
     * TODO: Model Documentation for KeyValue.value
     */
    protected Object value;

    /** Default Constructor with no properties */
    public KeyValue()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param keyIn String
     * @param valueIn Object
     */
    public KeyValue(final String keyIn, final Object valueIn)
    {
        this.key = keyIn;
        this.value = valueIn;
    }

    /**
     * Copies constructor from other KeyValue
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public KeyValue(final KeyValue otherBean)
    {
        this.key = otherBean.getKey();
        this.value = otherBean.getValue();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final KeyValue otherBean)
    {
        if (null != otherBean)
        {
            this.setKey(otherBean.getKey());
            this.setValue(otherBean.getValue());
        }
    }

    /**
     * TODO: Model Documentation for KeyValue.key
     * Get the key Attribute
     * @return key String
     */
    public String getKey()
    {
        return this.key;
    }

    /**
     * TODO: Model Documentation for KeyValue.key
     * @param value String
     */
    public void setKey(final String value)
    {
        this.key = value;
    }

    /**
     * TODO: Model Documentation for KeyValue.value
     * Get the value Attribute
     * @return value Object
     */
    public Object getValue()
    {
        return this.value;
    }

    /**
     * TODO: Model Documentation for KeyValue.value
     * @param value Object
     */
    public void setValue(final Object value)
    {
        this.value = value;
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
        KeyValue rhs = (KeyValue) object;
        return new EqualsBuilder()
            .append(this.getKey(), rhs.getKey())
            .append(this.getValue(), rhs.getValue())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final KeyValue object)
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
            .append(this.getKey(), object.getKey())
            .append(this.getValue(), object.getValue())
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
            .append(this.getKey())
            .append(this.getValue())
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
        builder.append("\t\"key\" : \"" + this.getKey() + "\"");
        builder.append("\t,\"value\" : \"" + this.getValue() + "\"");
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

        final KeyValue that = (KeyValue)thatObject;
        return
            equal(this.getKey(), that.getKey())
            && equal(this.getValue(), that.getValue())
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