package bw.co.knowvera.organisation;

import bw.co.knowvera.GeneralStatus;
import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for OrganisationSearchCriteria
 */
public class OrganisationSearchCriteria
    implements Serializable, Comparable<OrganisationSearchCriteria>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -6391961704981599102L;

    // Class attributes
    /**
     * TODO: Model Documentation for
     * OrganisationSearchCriteria.registrationNo
     */
    @Nullable
    protected String registrationNo;
    /**
     * TODO: Model Documentation for OrganisationSearchCriteria.status
     */
    @Nullable
    protected GeneralStatus status;
    /**
     * TODO: Model Documentation for OrganisationSearchCriteria.name
     */
    @Nullable
    protected String name;
    /**
     * TODO: Model Documentation for OrganisationSearchCriteria.id
     */
    @Nullable
    protected String id;
    /**
     * TODO: Model Documentation for
     * OrganisationSearchCriteria.contactEmailAddress
     */
    @Nullable
    protected String contactEmailAddress;
    /**
     * TODO: Model Documentation for OrganisationSearchCriteria.isClient
     */
    @Nullable
    protected Boolean isClient;

    /**
     * Constructor taking only required properties
     */
    public OrganisationSearchCriteria()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param registrationNoIn String
     * @param statusIn GeneralStatus
     * @param nameIn String
     * @param idIn String
     * @param contactEmailAddressIn String
     * @param isClientIn Boolean
     */
    public OrganisationSearchCriteria(final String registrationNoIn, final GeneralStatus statusIn, final String nameIn, final String idIn, final String contactEmailAddressIn, final Boolean isClientIn)
    {
        this.registrationNo = registrationNoIn;
        this.status = statusIn;
        this.name = nameIn;
        this.id = idIn;
        this.contactEmailAddress = contactEmailAddressIn;
        this.isClient = isClientIn;
    }

    /**
     * Copies constructor from other OrganisationSearchCriteria
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public OrganisationSearchCriteria(final OrganisationSearchCriteria otherBean)
    {
        this.registrationNo = otherBean.getRegistrationNo();
        this.status = otherBean.getStatus();
        this.name = otherBean.getName();
        this.id = otherBean.getId();
        this.contactEmailAddress = otherBean.getContactEmailAddress();
        this.isClient = otherBean.getIsClient();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final OrganisationSearchCriteria otherBean)
    {
        if (null != otherBean)
        {
            this.setRegistrationNo(otherBean.getRegistrationNo());
            this.setStatus(otherBean.getStatus());
            this.setName(otherBean.getName());
            this.setId(otherBean.getId());
            this.setContactEmailAddress(otherBean.getContactEmailAddress());
            this.setIsClient(otherBean.getIsClient());
        }
    }

    /**
     * TODO: Model Documentation for
     * OrganisationSearchCriteria.registrationNo
     * Get the registrationNo Attribute
     * @return registrationNo String
     */
    public String getRegistrationNo()
    {
        return this.registrationNo;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationSearchCriteria.registrationNo
     * @param value String
     */
    public void setRegistrationNo(final String value)
    {
        this.registrationNo = value;
    }

    /**
     * TODO: Model Documentation for OrganisationSearchCriteria.status
     * Get the status Attribute
     * @return status GeneralStatus
     */
    public GeneralStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for OrganisationSearchCriteria.status
     * @param value GeneralStatus
     */
    public void setStatus(final GeneralStatus value)
    {
        this.status = value;
    }

    /**
     * TODO: Model Documentation for OrganisationSearchCriteria.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for OrganisationSearchCriteria.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for OrganisationSearchCriteria.id
     * Get the id Attribute
     * @return id String
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for OrganisationSearchCriteria.id
     * @param value String
     */
    public void setId(final String value)
    {
        this.id = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationSearchCriteria.contactEmailAddress
     * Get the contactEmailAddress Attribute
     * @return contactEmailAddress String
     */
    public String getContactEmailAddress()
    {
        return this.contactEmailAddress;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationSearchCriteria.contactEmailAddress
     * @param value String
     */
    public void setContactEmailAddress(final String value)
    {
        this.contactEmailAddress = value;
    }

    /**
     * TODO: Model Documentation for OrganisationSearchCriteria.isClient
     * Get the isClient Attribute
     * @return isClient Boolean
     */
    public Boolean getIsClient()
    {
        return this.isClient;
    }

    /**
     * TODO: Model Documentation for OrganisationSearchCriteria.isClient
     * @param value Boolean
     */
    public void setIsClient(final Boolean value)
    {
        this.isClient = value;
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
        OrganisationSearchCriteria rhs = (OrganisationSearchCriteria) object;
        return new EqualsBuilder()
            .append(this.getRegistrationNo(), rhs.getRegistrationNo())
            .append(this.getStatus(), rhs.getStatus())
            .append(this.getName(), rhs.getName())
            .append(this.getId(), rhs.getId())
            .append(this.getContactEmailAddress(), rhs.getContactEmailAddress())
            .append(this.getIsClient(), rhs.getIsClient())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final OrganisationSearchCriteria object)
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
            .append(this.getRegistrationNo(), object.getRegistrationNo())
            .append(this.getStatus(), object.getStatus())
            .append(this.getName(), object.getName())
            .append(this.getId(), object.getId())
            .append(this.getContactEmailAddress(), object.getContactEmailAddress())
            .append(this.getIsClient(), object.getIsClient())
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
            .append(this.getRegistrationNo())
            .append(this.getStatus())
            .append(this.getName())
            .append(this.getId())
            .append(this.getContactEmailAddress())
            .append(this.getIsClient())
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
        builder.append("\t\"registrationNo\" : \"" + this.getRegistrationNo() + "\"");
        builder.append("\t,\"status\" : \"" + this.getStatus() + "\"");
        builder.append("\t,\"name\" : \"" + this.getName() + "\"");
        builder.append("\t,\"id\" : \"" + this.getId() + "\"");
        builder.append("\t,\"contactEmailAddress\" : \"" + this.getContactEmailAddress() + "\"");
        builder.append("\t,\"isClient\" : \"" + this.getIsClient() + "\"");
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

        final OrganisationSearchCriteria that = (OrganisationSearchCriteria)thatObject;
        return
            equal(this.getRegistrationNo(), that.getRegistrationNo())
            && equal(this.getStatus(), that.getStatus())
            && equal(this.getName(), that.getName())
            && equal(this.getId(), that.getId())
            && equal(this.getContactEmailAddress(), that.getContactEmailAddress())
            && equal(this.getIsClient(), that.getIsClient())
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