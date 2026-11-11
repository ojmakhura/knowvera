package bw.co.knowvera.settings;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for PlatformIdentity
 */
public class PlatformIdentity
    implements Serializable, Comparable<PlatformIdentity>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -3330804567257733378L;

    // Class attributes
    /**
     * TODO: Model Documentation for PlatformIdentity.platformName
     */
    @Nullable
    protected String platformName;
    /**
     * TODO: Model Documentation for PlatformIdentity.platformUrl
     */
    @Nullable
    protected String platformUrl;
    /**
     * TODO: Model Documentation for PlatformIdentity.supportContact
     */
    @Nullable
    protected String supportContact;
    /**
     * TODO: Model Documentation for PlatformIdentity.kycPortalLink
     */
    @Nullable
    protected String kycPortalLink;
    /**
     * TODO: Model Documentation for PlatformIdentity.user
     */
    @Nullable
    protected String user;

    /**
     * Constructor taking only required properties
     */
    public PlatformIdentity()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param platformNameIn String
     * @param platformUrlIn String
     * @param supportContactIn String
     * @param kycPortalLinkIn String
     * @param userIn String
     */
    public PlatformIdentity(final String platformNameIn, final String platformUrlIn, final String supportContactIn, final String kycPortalLinkIn, final String userIn)
    {
        this.platformName = platformNameIn;
        this.platformUrl = platformUrlIn;
        this.supportContact = supportContactIn;
        this.kycPortalLink = kycPortalLinkIn;
        this.user = userIn;
    }

    /**
     * Copies constructor from other PlatformIdentity
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public PlatformIdentity(final PlatformIdentity otherBean)
    {
        this.platformName = otherBean.getPlatformName();
        this.platformUrl = otherBean.getPlatformUrl();
        this.supportContact = otherBean.getSupportContact();
        this.kycPortalLink = otherBean.getKycPortalLink();
        this.user = otherBean.getUser();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final PlatformIdentity otherBean)
    {
        if (null != otherBean)
        {
            this.setPlatformName(otherBean.getPlatformName());
            this.setPlatformUrl(otherBean.getPlatformUrl());
            this.setSupportContact(otherBean.getSupportContact());
            this.setKycPortalLink(otherBean.getKycPortalLink());
            this.setUser(otherBean.getUser());
        }
    }

    /**
     * TODO: Model Documentation for PlatformIdentity.platformName
     * Get the platformName Attribute
     * @return platformName String
     */
    public String getPlatformName()
    {
        return this.platformName;
    }

    /**
     * TODO: Model Documentation for PlatformIdentity.platformName
     * @param value String
     */
    public void setPlatformName(final String value)
    {
        this.platformName = value;
    }

    /**
     * TODO: Model Documentation for PlatformIdentity.platformUrl
     * Get the platformUrl Attribute
     * @return platformUrl String
     */
    public String getPlatformUrl()
    {
        return this.platformUrl;
    }

    /**
     * TODO: Model Documentation for PlatformIdentity.platformUrl
     * @param value String
     */
    public void setPlatformUrl(final String value)
    {
        this.platformUrl = value;
    }

    /**
     * TODO: Model Documentation for PlatformIdentity.supportContact
     * Get the supportContact Attribute
     * @return supportContact String
     */
    public String getSupportContact()
    {
        return this.supportContact;
    }

    /**
     * TODO: Model Documentation for PlatformIdentity.supportContact
     * @param value String
     */
    public void setSupportContact(final String value)
    {
        this.supportContact = value;
    }

    /**
     * TODO: Model Documentation for PlatformIdentity.kycPortalLink
     * Get the kycPortalLink Attribute
     * @return kycPortalLink String
     */
    public String getKycPortalLink()
    {
        return this.kycPortalLink;
    }

    /**
     * TODO: Model Documentation for PlatformIdentity.kycPortalLink
     * @param value String
     */
    public void setKycPortalLink(final String value)
    {
        this.kycPortalLink = value;
    }

    /**
     * TODO: Model Documentation for PlatformIdentity.user
     * Get the user Attribute
     * @return user String
     */
    public String getUser()
    {
        return this.user;
    }

    /**
     * TODO: Model Documentation for PlatformIdentity.user
     * @param value String
     */
    public void setUser(final String value)
    {
        this.user = value;
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
        PlatformIdentity rhs = (PlatformIdentity) object;
        return new EqualsBuilder()
            .append(this.getPlatformName(), rhs.getPlatformName())
            .append(this.getPlatformUrl(), rhs.getPlatformUrl())
            .append(this.getSupportContact(), rhs.getSupportContact())
            .append(this.getKycPortalLink(), rhs.getKycPortalLink())
            .append(this.getUser(), rhs.getUser())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final PlatformIdentity object)
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
            .append(this.getPlatformName(), object.getPlatformName())
            .append(this.getPlatformUrl(), object.getPlatformUrl())
            .append(this.getSupportContact(), object.getSupportContact())
            .append(this.getKycPortalLink(), object.getKycPortalLink())
            .append(this.getUser(), object.getUser())
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
            .append(this.getPlatformName())
            .append(this.getPlatformUrl())
            .append(this.getSupportContact())
            .append(this.getKycPortalLink())
            .append(this.getUser())
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
        builder.append("\t\"platformName\" : \"" + this.getPlatformName() + "\"");
        builder.append("\t,\"platformUrl\" : \"" + this.getPlatformUrl() + "\"");
        builder.append("\t,\"supportContact\" : \"" + this.getSupportContact() + "\"");
        builder.append("\t,\"kycPortalLink\" : \"" + this.getKycPortalLink() + "\"");
        builder.append("\t,\"user\" : \"" + this.getUser() + "\"");
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

        final PlatformIdentity that = (PlatformIdentity)thatObject;
        return
            equal(this.getPlatformName(), that.getPlatformName())
            && equal(this.getPlatformUrl(), that.getPlatformUrl())
            && equal(this.getSupportContact(), that.getSupportContact())
            && equal(this.getKycPortalLink(), that.getKycPortalLink())
            && equal(this.getUser(), that.getUser())
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