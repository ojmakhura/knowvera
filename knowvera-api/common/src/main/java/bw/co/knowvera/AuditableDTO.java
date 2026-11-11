package bw.co.knowvera;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for AuditableDTO
 */
public abstract class AuditableDTO
    implements Serializable, Comparable<AuditableDTO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -7758362737963448588L;

    // Class attributes
    /**
     * TODO: Model Documentation for AuditableDTO.id
     */
    @Nullable
    protected String id;
    /**
     * TODO: Model Documentation for AuditableDTO.createdBy
     */
    @Nullable
    protected String createdBy;
    /**
     * TODO: Model Documentation for AuditableDTO.createdAt
     */
    @Nullable
    protected LocalDateTime createdAt;
    /**
     * TODO: Model Documentation for AuditableDTO.modifiedBy
     */
    @Nullable
    protected String modifiedBy;
    /**
     * TODO: Model Documentation for AuditableDTO.modifiedAt
     */
    @Nullable
    protected LocalDateTime modifiedAt;

    /**
     * Constructor taking only required properties
     */
    public AuditableDTO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     */
    public AuditableDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn)
    {
        this.id = idIn;
        this.createdBy = createdByIn;
        this.createdAt = createdAtIn;
        this.modifiedBy = modifiedByIn;
        this.modifiedAt = modifiedAtIn;
    }

    /**
     * Copies constructor from other AuditableDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public AuditableDTO(final AuditableDTO otherBean)
    {
        this.id = otherBean.getId();
        this.createdBy = otherBean.getCreatedBy();
        this.createdAt = otherBean.getCreatedAt();
        this.modifiedBy = otherBean.getModifiedBy();
        this.modifiedAt = otherBean.getModifiedAt();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final AuditableDTO otherBean)
    {
        if (null != otherBean)
        {
            this.setId(otherBean.getId());
            this.setCreatedBy(otherBean.getCreatedBy());
            this.setCreatedAt(otherBean.getCreatedAt());
            this.setModifiedBy(otherBean.getModifiedBy());
            this.setModifiedAt(otherBean.getModifiedAt());
        }
    }

    /**
     * TODO: Model Documentation for AuditableDTO.id
     * Get the id Attribute
     * @return id String
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for AuditableDTO.id
     * @param value String
     */
    public void setId(final String value)
    {
        this.id = value;
    }

    /**
     * TODO: Model Documentation for AuditableDTO.createdBy
     * Get the createdBy Attribute
     * @return createdBy String
     */
    public String getCreatedBy()
    {
        return this.createdBy;
    }

    /**
     * TODO: Model Documentation for AuditableDTO.createdBy
     * @param value String
     */
    public void setCreatedBy(final String value)
    {
        this.createdBy = value;
    }

    /**
     * TODO: Model Documentation for AuditableDTO.createdAt
     * Get the createdAt Attribute
     * @return createdAt LocalDateTime
     */
    public LocalDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    /**
     * TODO: Model Documentation for AuditableDTO.createdAt
     * @param value LocalDateTime
     */
    public void setCreatedAt(final LocalDateTime value)
    {
        this.createdAt = value;
    }

    /**
     * TODO: Model Documentation for AuditableDTO.modifiedBy
     * Get the modifiedBy Attribute
     * @return modifiedBy String
     */
    public String getModifiedBy()
    {
        return this.modifiedBy;
    }

    /**
     * TODO: Model Documentation for AuditableDTO.modifiedBy
     * @param value String
     */
    public void setModifiedBy(final String value)
    {
        this.modifiedBy = value;
    }

    /**
     * TODO: Model Documentation for AuditableDTO.modifiedAt
     * Get the modifiedAt Attribute
     * @return modifiedAt LocalDateTime
     */
    public LocalDateTime getModifiedAt()
    {
        return this.modifiedAt;
    }

    /**
     * TODO: Model Documentation for AuditableDTO.modifiedAt
     * @param value LocalDateTime
     */
    public void setModifiedAt(final LocalDateTime value)
    {
        this.modifiedAt = value;
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
        AuditableDTO rhs = (AuditableDTO) object;
        return new EqualsBuilder()
            .append(this.getId(), rhs.getId())
            .append(this.getCreatedBy(), rhs.getCreatedBy())
            .append(this.getCreatedAt(), rhs.getCreatedAt())
            .append(this.getModifiedBy(), rhs.getModifiedBy())
            .append(this.getModifiedAt(), rhs.getModifiedAt())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final AuditableDTO object)
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
            .append(this.getCreatedBy(), object.getCreatedBy())
            .append(this.getCreatedAt(), object.getCreatedAt())
            .append(this.getModifiedBy(), object.getModifiedBy())
            .append(this.getModifiedAt(), object.getModifiedAt())
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
            .append(this.getCreatedBy())
            .append(this.getCreatedAt())
            .append(this.getModifiedBy())
            .append(this.getModifiedAt())
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
        builder.append("\t,\"createdBy\" : \"" + this.getCreatedBy() + "\"");
        builder.append("\t,\"createdAt\" : \"" + this.getCreatedAt() + "\"");
        builder.append("\t,\"modifiedBy\" : \"" + this.getModifiedBy() + "\"");
        builder.append("\t,\"modifiedAt\" : \"" + this.getModifiedAt() + "\"");
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

        final AuditableDTO that = (AuditableDTO)thatObject;
        return
            equal(this.getId(), that.getId())
            && equal(this.getCreatedBy(), that.getCreatedBy())
            && equal(this.getCreatedAt(), that.getCreatedAt())
            && equal(this.getModifiedBy(), that.getModifiedBy())
            && equal(this.getModifiedAt(), that.getModifiedAt())
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