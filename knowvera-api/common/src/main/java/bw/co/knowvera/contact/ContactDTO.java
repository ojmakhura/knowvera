package bw.co.knowvera.contact;

import bw.co.knowvera.AuditableDTO;
import java.time.LocalDateTime;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for ContactDTO
 */
public class ContactDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -9214476573884747310L;

    // Class attributes
    /**
     * TODO: Model Documentation for ContactDTO.message
     */
    protected String message;
    /**
     * TODO: Model Documentation for ContactDTO.email
     */
    protected String email;
    /**
     * TODO: Model Documentation for ContactDTO.type
     */
    @Nullable
    protected ContactType type;
    /**
     * TODO: Model Documentation for ContactDTO.ref
     */
    @Nullable
    protected String ref;

    /** Default Constructor with no properties */
    public ContactDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param messageIn String TODO: Model Documentation for ContactDTO.message
     * @param emailIn String TODO: Model Documentation for ContactDTO.email
     */
    public ContactDTO(final String messageIn, final String emailIn)
    {
        super();
        this.message = messageIn;
        this.email = emailIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param messageIn String
     * @param emailIn String
     * @param typeIn ContactType
     * @param refIn String
     */
    public ContactDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final String messageIn, final String emailIn, final ContactType typeIn, final String refIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.message = messageIn;
        this.email = emailIn;
        this.type = typeIn;
        this.ref = refIn;
    }

    /**
     * Copies constructor from other ContactDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public ContactDTO(final ContactDTO otherBean)
    {
        super(otherBean);
        this.message = otherBean.getMessage();
        this.email = otherBean.getEmail();
        this.type = otherBean.getType();
        this.ref = otherBean.getRef();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final ContactDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setMessage(otherBean.getMessage());
            this.setEmail(otherBean.getEmail());
            this.setType(otherBean.getType());
            this.setRef(otherBean.getRef());
        }
    }

    /**
     * TODO: Model Documentation for ContactDTO.message
     * Get the message Attribute
     * @return message String
     */
    public String getMessage()
    {
        return this.message;
    }

    /**
     * TODO: Model Documentation for ContactDTO.message
     * @param value String
     */
    public void setMessage(final String value)
    {
        this.message = value;
    }

    /**
     * TODO: Model Documentation for ContactDTO.email
     * Get the email Attribute
     * @return email String
     */
    public String getEmail()
    {
        return this.email;
    }

    /**
     * TODO: Model Documentation for ContactDTO.email
     * @param value String
     */
    public void setEmail(final String value)
    {
        this.email = value;
    }

    /**
     * TODO: Model Documentation for ContactDTO.type
     * Get the type Attribute
     * @return type ContactType
     */
    public ContactType getType()
    {
        return this.type;
    }

    /**
     * TODO: Model Documentation for ContactDTO.type
     * @param value ContactType
     */
    public void setType(final ContactType value)
    {
        this.type = value;
    }

    /**
     * TODO: Model Documentation for ContactDTO.ref
     * Get the ref Attribute
     * @return ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for ContactDTO.ref
     * @param value String
     */
    public void setRef(final String value)
    {
        this.ref = value;
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
        ContactDTO rhs = (ContactDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getMessage(), rhs.getMessage())
            .append(this.getEmail(), rhs.getEmail())
            .append(this.getType(), rhs.getType())
            .append(this.getRef(), rhs.getRef())
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
        if (!(object instanceof ContactDTO))
        {
            return -1;
        }
        ContactDTO myClass = (ContactDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getMessage(), myClass.getMessage())
            .append(this.getEmail(), myClass.getEmail())
            .append(this.getType(), myClass.getType())
            .append(this.getRef(), myClass.getRef())
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
            .appendSuper(super.hashCode())
            .append(this.getMessage())
            .append(this.getEmail())
            .append(this.getType())
            .append(this.getRef())
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
        builder.append("\t\"message\" : \"" + this.getMessage() + "\"");
        builder.append("\t,\"email\" : \"" + this.getEmail() + "\"");
        builder.append("\t,\"type\" : \"" + this.getType() + "\"");
        builder.append("\t,\"ref\" : \"" + this.getRef() + "\"");
        builder.append("\t,\"id\" : \"" + this.getId() + "\"");
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
    @Override
    public boolean equalProperties(final Object thatObject)
    {
        if (thatObject == null || !this.getClass().isAssignableFrom(thatObject.getClass()))
        {
            return false;
        }

        @SuppressWarnings("unchecked")
        final ContactDTO that = (ContactDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getMessage(), that.getMessage())
            && equal(this.getEmail(), that.getEmail())
            && equal(this.getType(), that.getType())
            && equal(this.getRef(), that.getRef())
        ;
    }

}