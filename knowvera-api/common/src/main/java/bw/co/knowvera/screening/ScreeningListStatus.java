package bw.co.knowvera.screening;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for ScreeningListStatus
 */
public class ScreeningListStatus
    implements Serializable, Comparable<ScreeningListStatus>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 5736908512875328897L;

    // Class attributes
    /**
     * TODO: Model Documentation for ScreeningListStatus.source
     */
    @Nullable
    protected ScreeningListSource source;
    /**
     * TODO: Model Documentation for ScreeningListStatus.listVersion
     */
    @Nullable
    protected String listVersion;
    /**
     * TODO: Model Documentation for ScreeningListStatus.entryCount
     */
    @Nullable
    protected Integer entryCount;
    /**
     * TODO: Model Documentation for ScreeningListStatus.importedAt
     */
    @Nullable
    protected LocalDateTime importedAt;

    /**
     * Constructor taking only required properties
     */
    public ScreeningListStatus()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param sourceIn ScreeningListSource
     * @param listVersionIn String
     * @param entryCountIn Integer
     * @param importedAtIn LocalDateTime
     */
    public ScreeningListStatus(final ScreeningListSource sourceIn, final String listVersionIn, final Integer entryCountIn, final LocalDateTime importedAtIn)
    {
        this.source = sourceIn;
        this.listVersion = listVersionIn;
        this.entryCount = entryCountIn;
        this.importedAt = importedAtIn;
    }

    /**
     * Copies constructor from other ScreeningListStatus
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public ScreeningListStatus(final ScreeningListStatus otherBean)
    {
        this.source = otherBean.getSource();
        this.listVersion = otherBean.getListVersion();
        this.entryCount = otherBean.getEntryCount();
        this.importedAt = otherBean.getImportedAt();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final ScreeningListStatus otherBean)
    {
        if (null != otherBean)
        {
            this.setSource(otherBean.getSource());
            this.setListVersion(otherBean.getListVersion());
            this.setEntryCount(otherBean.getEntryCount());
            this.setImportedAt(otherBean.getImportedAt());
        }
    }

    /**
     * TODO: Model Documentation for ScreeningListStatus.source
     * Get the source Attribute
     * @return source ScreeningListSource
     */
    public ScreeningListSource getSource()
    {
        return this.source;
    }

    /**
     * TODO: Model Documentation for ScreeningListStatus.source
     * @param value ScreeningListSource
     */
    public void setSource(final ScreeningListSource value)
    {
        this.source = value;
    }

    /**
     * TODO: Model Documentation for ScreeningListStatus.listVersion
     * Get the listVersion Attribute
     * @return listVersion String
     */
    public String getListVersion()
    {
        return this.listVersion;
    }

    /**
     * TODO: Model Documentation for ScreeningListStatus.listVersion
     * @param value String
     */
    public void setListVersion(final String value)
    {
        this.listVersion = value;
    }

    /**
     * TODO: Model Documentation for ScreeningListStatus.entryCount
     * Get the entryCount Attribute
     * @return entryCount Integer
     */
    public Integer getEntryCount()
    {
        return this.entryCount;
    }

    /**
     * TODO: Model Documentation for ScreeningListStatus.entryCount
     * @param value Integer
     */
    public void setEntryCount(final Integer value)
    {
        this.entryCount = value;
    }

    /**
     * TODO: Model Documentation for ScreeningListStatus.importedAt
     * Get the importedAt Attribute
     * @return importedAt LocalDateTime
     */
    public LocalDateTime getImportedAt()
    {
        return this.importedAt;
    }

    /**
     * TODO: Model Documentation for ScreeningListStatus.importedAt
     * @param value LocalDateTime
     */
    public void setImportedAt(final LocalDateTime value)
    {
        this.importedAt = value;
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
        ScreeningListStatus rhs = (ScreeningListStatus) object;
        return new EqualsBuilder()
            .append(this.getSource(), rhs.getSource())
            .append(this.getListVersion(), rhs.getListVersion())
            .append(this.getEntryCount(), rhs.getEntryCount())
            .append(this.getImportedAt(), rhs.getImportedAt())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final ScreeningListStatus object)
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
            .append(this.getSource(), object.getSource())
            .append(this.getListVersion(), object.getListVersion())
            .append(this.getEntryCount(), object.getEntryCount())
            .append(this.getImportedAt(), object.getImportedAt())
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
            .append(this.getSource())
            .append(this.getListVersion())
            .append(this.getEntryCount())
            .append(this.getImportedAt())
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
        builder.append("\t\"source\" : \"" + this.getSource() + "\"");
        builder.append("\t,\"listVersion\" : \"" + this.getListVersion() + "\"");
        builder.append("\t,\"entryCount\" : \"" + this.getEntryCount() + "\"");
        builder.append("\t,\"importedAt\" : \"" + this.getImportedAt() + "\"");
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

        final ScreeningListStatus that = (ScreeningListStatus)thatObject;
        return
            equal(this.getSource(), that.getSource())
            && equal(this.getListVersion(), that.getListVersion())
            && equal(this.getEntryCount(), that.getEntryCount())
            && equal(this.getImportedAt(), that.getImportedAt())
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