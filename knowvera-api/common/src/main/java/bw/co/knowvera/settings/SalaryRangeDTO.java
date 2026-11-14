package bw.co.knowvera.settings;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for SalaryRangeDTO
 */
public class SalaryRangeDTO
    implements Serializable, Comparable<SalaryRangeDTO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 7404599077236434562L;

    // Class attributes
    /**
     * TODO: Model Documentation for SalaryRangeDTO.id
     */
    @Nullable
    protected Long id;
    /**
     * TODO: Model Documentation for SalaryRangeDTO.min
     */
    @Nullable
    protected Double min;
    /**
     * TODO: Model Documentation for SalaryRangeDTO.max
     */
    @Nullable
    protected Double max;
    /**
     * TODO: Model Documentation for SalaryRangeDTO.active
     */
    @Nullable
    protected Boolean active = true;
    /**
     * TODO: Model Documentation for SalaryRangeDTO.settings
     */
    @Nullable
    protected String settings;

    /**
     * Constructor taking only required properties
     */
    public SalaryRangeDTO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param idIn Long
     * @param minIn Double
     * @param maxIn Double
     * @param activeIn Boolean
     * @param settingsIn String
     */
    public SalaryRangeDTO(final Long idIn, final Double minIn, final Double maxIn, final Boolean activeIn, final String settingsIn)
    {
        this.id = idIn;
        this.min = minIn;
        this.max = maxIn;
        this.active = activeIn;
        this.settings = settingsIn;
    }

    /**
     * Copies constructor from other SalaryRangeDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public SalaryRangeDTO(final SalaryRangeDTO otherBean)
    {
        this.id = otherBean.getId();
        this.min = otherBean.getMin();
        this.max = otherBean.getMax();
        this.active = otherBean.getActive();
        this.settings = otherBean.getSettings();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final SalaryRangeDTO otherBean)
    {
        if (null != otherBean)
        {
            this.setId(otherBean.getId());
            this.setMin(otherBean.getMin());
            this.setMax(otherBean.getMax());
            this.setActive(otherBean.getActive());
            this.setSettings(otherBean.getSettings());
        }
    }

    /**
     * TODO: Model Documentation for SalaryRangeDTO.id
     * Get the id Attribute
     * @return id Long
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for SalaryRangeDTO.id
     * @param value Long
     */
    public void setId(final Long value)
    {
        this.id = value;
    }

    /**
     * TODO: Model Documentation for SalaryRangeDTO.min
     * Get the min Attribute
     * @return min Double
     */
    public Double getMin()
    {
        return this.min;
    }

    /**
     * TODO: Model Documentation for SalaryRangeDTO.min
     * @param value Double
     */
    public void setMin(final Double value)
    {
        this.min = value;
    }

    /**
     * TODO: Model Documentation for SalaryRangeDTO.max
     * Get the max Attribute
     * @return max Double
     */
    public Double getMax()
    {
        return this.max;
    }

    /**
     * TODO: Model Documentation for SalaryRangeDTO.max
     * @param value Double
     */
    public void setMax(final Double value)
    {
        this.max = value;
    }

    /**
     * TODO: Model Documentation for SalaryRangeDTO.active
     * Get the active Attribute
     * @return active Boolean
     */
    public Boolean getActive()
    {
        return this.active;
    }

    /**
     * TODO: Model Documentation for SalaryRangeDTO.active
     * @param value Boolean
     */
    public void setActive(final Boolean value)
    {
        this.active = value;
    }

    /**
     * TODO: Model Documentation for SalaryRangeDTO.settings
     * Get the settings Attribute
     * @return settings String
     */
    public String getSettings()
    {
        return this.settings;
    }

    /**
     * TODO: Model Documentation for SalaryRangeDTO.settings
     * @param value String
     */
    public void setSettings(final String value)
    {
        this.settings = value;
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
        SalaryRangeDTO rhs = (SalaryRangeDTO) object;
        return new EqualsBuilder()
            .append(this.getId(), rhs.getId())
            .append(this.getMin(), rhs.getMin())
            .append(this.getMax(), rhs.getMax())
            .append(this.getActive(), rhs.getActive())
            .append(this.getSettings(), rhs.getSettings())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final SalaryRangeDTO object)
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
            .append(this.getMin(), object.getMin())
            .append(this.getMax(), object.getMax())
            .append(this.getActive(), object.getActive())
            .append(this.getSettings(), object.getSettings())
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
            .append(this.getMin())
            .append(this.getMax())
            .append(this.getActive())
            .append(this.getSettings())
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
        builder.append("\t,\"min\" : \"" + this.getMin() + "\"");
        builder.append("\t,\"max\" : \"" + this.getMax() + "\"");
        builder.append("\t,\"active\" : \"" + this.getActive() + "\"");
        builder.append("\t,\"settings\" : \"" + this.getSettings() + "\"");
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

        final SalaryRangeDTO that = (SalaryRangeDTO)thatObject;
        return
            equal(this.getId(), that.getId())
            && equal(this.getMin(), that.getMin())
            && equal(this.getMax(), that.getMax())
            && equal(this.getActive(), that.getActive())
            && equal(this.getSettings(), that.getSettings())
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