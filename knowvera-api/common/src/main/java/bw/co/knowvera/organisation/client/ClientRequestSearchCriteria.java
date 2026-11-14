package bw.co.knowvera.organisation.client;

import bw.co.knowvera.TargetEntity;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for ClientRequestSearchCriteria
 */
public class ClientRequestSearchCriteria
    implements Serializable, Comparable<ClientRequestSearchCriteria>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -1159140760903830606L;

    // Class attributes
    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.emailAddress
     */
    @Nullable
    protected String emailAddress;
    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.name
     */
    @Nullable
    protected String name;
    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.request
     */
    @Nullable
    protected ClientRequestStatus request;
    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.organisation
     */
    @Nullable
    protected String organisation;
    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.organisationId
     */
    @Nullable
    protected String organisationId;
    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.organisationRegistrationNo
     */
    @Nullable
    protected String organisationRegistrationNo;
    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.registration
     */
    @Nullable
    protected String registration;
    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.target
     */
    @Nullable
    protected TargetEntity target;
    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.targetId
     */
    @Nullable
    protected String targetId;
    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.statuses
     */
    @Nullable
    protected Set<ClientRequestStatus> statuses;

    /**
     * Constructor taking only required properties
     */
    public ClientRequestSearchCriteria()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param emailAddressIn String
     * @param nameIn String
     * @param requestIn ClientRequestStatus
     * @param organisationIn String
     * @param organisationIdIn String
     * @param organisationRegistrationNoIn String
     * @param registrationIn String
     * @param targetIn TargetEntity
     * @param targetIdIn String
     * @param statusesIn Set<ClientRequestStatus>
     */
    public ClientRequestSearchCriteria(final String emailAddressIn, final String nameIn, final ClientRequestStatus requestIn, final String organisationIn, final String organisationIdIn, final String organisationRegistrationNoIn, final String registrationIn, final TargetEntity targetIn, final String targetIdIn, final Set<ClientRequestStatus> statusesIn)
    {
        this.emailAddress = emailAddressIn;
        this.name = nameIn;
        this.request = requestIn;
        this.organisation = organisationIn;
        this.organisationId = organisationIdIn;
        this.organisationRegistrationNo = organisationRegistrationNoIn;
        this.registration = registrationIn;
        this.target = targetIn;
        this.targetId = targetIdIn;
        this.statuses = statusesIn;
    }

    /**
     * Copies constructor from other ClientRequestSearchCriteria
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public ClientRequestSearchCriteria(final ClientRequestSearchCriteria otherBean)
    {
        this.emailAddress = otherBean.getEmailAddress();
        this.name = otherBean.getName();
        this.request = otherBean.getRequest();
        this.organisation = otherBean.getOrganisation();
        this.organisationId = otherBean.getOrganisationId();
        this.organisationRegistrationNo = otherBean.getOrganisationRegistrationNo();
        this.registration = otherBean.getRegistration();
        this.target = otherBean.getTarget();
        this.targetId = otherBean.getTargetId();
        this.statuses = otherBean.getStatuses();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final ClientRequestSearchCriteria otherBean)
    {
        if (null != otherBean)
        {
            this.setEmailAddress(otherBean.getEmailAddress());
            this.setName(otherBean.getName());
            this.setRequest(otherBean.getRequest());
            this.setOrganisation(otherBean.getOrganisation());
            this.setOrganisationId(otherBean.getOrganisationId());
            this.setOrganisationRegistrationNo(otherBean.getOrganisationRegistrationNo());
            this.setRegistration(otherBean.getRegistration());
            this.setTarget(otherBean.getTarget());
            this.setTargetId(otherBean.getTargetId());
            this.setStatuses(otherBean.getStatuses());
        }
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.emailAddress
     * Get the emailAddress Attribute
     * @return emailAddress String
     */
    public String getEmailAddress()
    {
        return this.emailAddress;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.emailAddress
     * @param value String
     */
    public void setEmailAddress(final String value)
    {
        this.emailAddress = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.request
     * Get the request Attribute
     * @return request ClientRequestStatus
     */
    public ClientRequestStatus getRequest()
    {
        return this.request;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.request
     * @param value ClientRequestStatus
     */
    public void setRequest(final ClientRequestStatus value)
    {
        this.request = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.organisation
     * Get the organisation Attribute
     * @return organisation String
     */
    public String getOrganisation()
    {
        return this.organisation;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.organisation
     * @param value String
     */
    public void setOrganisation(final String value)
    {
        this.organisation = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.organisationId
     * Get the organisationId Attribute
     * @return organisationId String
     */
    public String getOrganisationId()
    {
        return this.organisationId;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.organisationId
     * @param value String
     */
    public void setOrganisationId(final String value)
    {
        this.organisationId = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.organisationRegistrationNo
     * Get the organisationRegistrationNo Attribute
     * @return organisationRegistrationNo String
     */
    public String getOrganisationRegistrationNo()
    {
        return this.organisationRegistrationNo;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.organisationRegistrationNo
     * @param value String
     */
    public void setOrganisationRegistrationNo(final String value)
    {
        this.organisationRegistrationNo = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.registration
     * Get the registration Attribute
     * @return registration String
     */
    public String getRegistration()
    {
        return this.registration;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.registration
     * @param value String
     */
    public void setRegistration(final String value)
    {
        this.registration = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.target
     * Get the target Attribute
     * @return target TargetEntity
     */
    public TargetEntity getTarget()
    {
        return this.target;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.target
     * @param value TargetEntity
     */
    public void setTarget(final TargetEntity value)
    {
        this.target = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.targetId
     * Get the targetId Attribute
     * @return targetId String
     */
    public String getTargetId()
    {
        return this.targetId;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.targetId
     * @param value String
     */
    public void setTargetId(final String value)
    {
        this.targetId = value;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.statuses
     * Get the statuses Attribute
     * @return statuses Set<ClientRequestStatus>
     */
    public Set<ClientRequestStatus> getStatuses()
    {
        if (this.statuses == null)
        {
            this.statuses = new HashSet<ClientRequestStatus>();
        }
        return this.statuses;
    }

    /**
     * TODO: Model Documentation for
     * ClientRequestSearchCriteria.statuses
     * @param value Set<ClientRequestStatus>
     */
    public void setStatuses(final Set<ClientRequestStatus> value)
    {
        this.statuses = value;
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
        ClientRequestSearchCriteria rhs = (ClientRequestSearchCriteria) object;
        return new EqualsBuilder()
            .append(this.getEmailAddress(), rhs.getEmailAddress())
            .append(this.getName(), rhs.getName())
            .append(this.getRequest(), rhs.getRequest())
            .append(this.getOrganisation(), rhs.getOrganisation())
            .append(this.getOrganisationId(), rhs.getOrganisationId())
            .append(this.getOrganisationRegistrationNo(), rhs.getOrganisationRegistrationNo())
            .append(this.getRegistration(), rhs.getRegistration())
            .append(this.getTarget(), rhs.getTarget())
            .append(this.getTargetId(), rhs.getTargetId())
            .append(this.getStatuses(), rhs.getStatuses())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final ClientRequestSearchCriteria object)
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
            .append(this.getEmailAddress(), object.getEmailAddress())
            .append(this.getName(), object.getName())
            .append(this.getRequest(), object.getRequest())
            .append(this.getOrganisation(), object.getOrganisation())
            .append(this.getOrganisationId(), object.getOrganisationId())
            .append(this.getOrganisationRegistrationNo(), object.getOrganisationRegistrationNo())
            .append(this.getRegistration(), object.getRegistration())
            .append(this.getTarget(), object.getTarget())
            .append(this.getTargetId(), object.getTargetId())
            .append(this.getStatuses(), object.getStatuses())
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
            .append(this.getEmailAddress())
            .append(this.getName())
            .append(this.getRequest())
            .append(this.getOrganisation())
            .append(this.getOrganisationId())
            .append(this.getOrganisationRegistrationNo())
            .append(this.getRegistration())
            .append(this.getTarget())
            .append(this.getTargetId())
            .append(this.getStatuses())
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
        builder.append("\t\"emailAddress\" : \"" + this.getEmailAddress() + "\"");
        builder.append("\t,\"name\" : \"" + this.getName() + "\"");
        builder.append("\t,\"request\" : \"" + this.getRequest() + "\"");
        builder.append("\t,\"organisation\" : \"" + this.getOrganisation() + "\"");
        builder.append("\t,\"organisationId\" : \"" + this.getOrganisationId() + "\"");
        builder.append("\t,\"organisationRegistrationNo\" : \"" + this.getOrganisationRegistrationNo() + "\"");
        builder.append("\t,\"registration\" : \"" + this.getRegistration() + "\"");
        builder.append("\t,\"target\" : \"" + this.getTarget() + "\"");
        builder.append("\t,\"targetId\" : \"" + this.getTargetId() + "\"");
        builder.append("\t,\"statuses\" : \"" + this.getStatuses() + "\"");
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

        final ClientRequestSearchCriteria that = (ClientRequestSearchCriteria)thatObject;
        return
            equal(this.getEmailAddress(), that.getEmailAddress())
            && equal(this.getName(), that.getName())
            && equal(this.getRequest(), that.getRequest())
            && equal(this.getOrganisation(), that.getOrganisation())
            && equal(this.getOrganisationId(), that.getOrganisationId())
            && equal(this.getOrganisationRegistrationNo(), that.getOrganisationRegistrationNo())
            && equal(this.getRegistration(), that.getRegistration())
            && equal(this.getTarget(), that.getTarget())
            && equal(this.getTargetId(), that.getTargetId())
            && equal(this.getStatuses(), that.getStatuses())
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