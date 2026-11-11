package bw.co.knowvera.invoice;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for InvoiceSearchCriteria
 */
public class InvoiceSearchCriteria
    implements Serializable, Comparable<InvoiceSearchCriteria>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -2594338098740953220L;

    // Class attributes
    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.ref
     */
    @Nullable
    protected String ref;
    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.organisatonId
     */
    @Nullable
    protected String organisatonId;
    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.organisatonCode
     */
    @Nullable
    protected String organisatonCode;
    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.organisationName
     */
    @Nullable
    protected String organisationName;
    /**
     * TODO: Model Documentation for
     * InvoiceSearchCriteria.organisationRegistrationNo
     */
    @Nullable
    protected String organisationRegistrationNo;
    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.paid
     */
    @Nullable
    protected Boolean paid;

    /**
     * Constructor taking only required properties
     */
    public InvoiceSearchCriteria()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param refIn String
     * @param organisatonIdIn String
     * @param organisatonCodeIn String
     * @param organisationNameIn String
     * @param organisationRegistrationNoIn String
     * @param paidIn Boolean
     */
    public InvoiceSearchCriteria(final String refIn, final String organisatonIdIn, final String organisatonCodeIn, final String organisationNameIn, final String organisationRegistrationNoIn, final Boolean paidIn)
    {
        this.ref = refIn;
        this.organisatonId = organisatonIdIn;
        this.organisatonCode = organisatonCodeIn;
        this.organisationName = organisationNameIn;
        this.organisationRegistrationNo = organisationRegistrationNoIn;
        this.paid = paidIn;
    }

    /**
     * Copies constructor from other InvoiceSearchCriteria
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public InvoiceSearchCriteria(final InvoiceSearchCriteria otherBean)
    {
        this.ref = otherBean.getRef();
        this.organisatonId = otherBean.getOrganisatonId();
        this.organisatonCode = otherBean.getOrganisatonCode();
        this.organisationName = otherBean.getOrganisationName();
        this.organisationRegistrationNo = otherBean.getOrganisationRegistrationNo();
        this.paid = otherBean.getPaid();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final InvoiceSearchCriteria otherBean)
    {
        if (null != otherBean)
        {
            this.setRef(otherBean.getRef());
            this.setOrganisatonId(otherBean.getOrganisatonId());
            this.setOrganisatonCode(otherBean.getOrganisatonCode());
            this.setOrganisationName(otherBean.getOrganisationName());
            this.setOrganisationRegistrationNo(otherBean.getOrganisationRegistrationNo());
            this.setPaid(otherBean.getPaid());
        }
    }

    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.ref
     * Get the ref Attribute
     * @return ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.ref
     * @param value String
     */
    public void setRef(final String value)
    {
        this.ref = value;
    }

    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.organisatonId
     * Get the organisatonId Attribute
     * @return organisatonId String
     */
    public String getOrganisatonId()
    {
        return this.organisatonId;
    }

    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.organisatonId
     * @param value String
     */
    public void setOrganisatonId(final String value)
    {
        this.organisatonId = value;
    }

    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.organisatonCode
     * Get the organisatonCode Attribute
     * @return organisatonCode String
     */
    public String getOrganisatonCode()
    {
        return this.organisatonCode;
    }

    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.organisatonCode
     * @param value String
     */
    public void setOrganisatonCode(final String value)
    {
        this.organisatonCode = value;
    }

    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.organisationName
     * Get the organisationName Attribute
     * @return organisationName String
     */
    public String getOrganisationName()
    {
        return this.organisationName;
    }

    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.organisationName
     * @param value String
     */
    public void setOrganisationName(final String value)
    {
        this.organisationName = value;
    }

    /**
     * TODO: Model Documentation for
     * InvoiceSearchCriteria.organisationRegistrationNo
     * Get the organisationRegistrationNo Attribute
     * @return organisationRegistrationNo String
     */
    public String getOrganisationRegistrationNo()
    {
        return this.organisationRegistrationNo;
    }

    /**
     * TODO: Model Documentation for
     * InvoiceSearchCriteria.organisationRegistrationNo
     * @param value String
     */
    public void setOrganisationRegistrationNo(final String value)
    {
        this.organisationRegistrationNo = value;
    }

    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.paid
     * Get the paid Attribute
     * @return paid Boolean
     */
    public Boolean getPaid()
    {
        return this.paid;
    }

    /**
     * TODO: Model Documentation for InvoiceSearchCriteria.paid
     * @param value Boolean
     */
    public void setPaid(final Boolean value)
    {
        this.paid = value;
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
        InvoiceSearchCriteria rhs = (InvoiceSearchCriteria) object;
        return new EqualsBuilder()
            .append(this.getRef(), rhs.getRef())
            .append(this.getOrganisatonId(), rhs.getOrganisatonId())
            .append(this.getOrganisatonCode(), rhs.getOrganisatonCode())
            .append(this.getOrganisationName(), rhs.getOrganisationName())
            .append(this.getOrganisationRegistrationNo(), rhs.getOrganisationRegistrationNo())
            .append(this.getPaid(), rhs.getPaid())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final InvoiceSearchCriteria object)
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
            .append(this.getRef(), object.getRef())
            .append(this.getOrganisatonId(), object.getOrganisatonId())
            .append(this.getOrganisatonCode(), object.getOrganisatonCode())
            .append(this.getOrganisationName(), object.getOrganisationName())
            .append(this.getOrganisationRegistrationNo(), object.getOrganisationRegistrationNo())
            .append(this.getPaid(), object.getPaid())
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
            .append(this.getRef())
            .append(this.getOrganisatonId())
            .append(this.getOrganisatonCode())
            .append(this.getOrganisationName())
            .append(this.getOrganisationRegistrationNo())
            .append(this.getPaid())
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
        builder.append("\t\"ref\" : \"" + this.getRef() + "\"");
        builder.append("\t,\"organisatonId\" : \"" + this.getOrganisatonId() + "\"");
        builder.append("\t,\"organisatonCode\" : \"" + this.getOrganisatonCode() + "\"");
        builder.append("\t,\"organisationName\" : \"" + this.getOrganisationName() + "\"");
        builder.append("\t,\"organisationRegistrationNo\" : \"" + this.getOrganisationRegistrationNo() + "\"");
        builder.append("\t,\"paid\" : \"" + this.getPaid() + "\"");
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

        final InvoiceSearchCriteria that = (InvoiceSearchCriteria)thatObject;
        return
            equal(this.getRef(), that.getRef())
            && equal(this.getOrganisatonId(), that.getOrganisatonId())
            && equal(this.getOrganisatonCode(), that.getOrganisatonCode())
            && equal(this.getOrganisationName(), that.getOrganisationName())
            && equal(this.getOrganisationRegistrationNo(), that.getOrganisationRegistrationNo())
            && equal(this.getPaid(), that.getPaid())
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