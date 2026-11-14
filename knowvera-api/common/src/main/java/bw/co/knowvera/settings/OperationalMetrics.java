package bw.co.knowvera.settings;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for OperationalMetrics
 */
public class OperationalMetrics
    implements Serializable, Comparable<OperationalMetrics>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 3367145737484137161L;

    // Class attributes
    /**
     * TODO: Model Documentation for OperationalMetrics.kycDuration
     */
    @Nullable
    protected Integer kycDuration;
    /**
     * TODO: Model Documentation for
     * OperationalMetrics.timeToAccountCreation
     */
    @Nullable
    protected Integer timeToAccountCreation;
    /**
     * TODO: Model Documentation for
     * OperationalMetrics.organisationAdminRole
     */
    @Nullable
    protected String organisationAdminRole;
    /**
     * TODO: Model Documentation for OperationalMetrics.normalUserRole
     */
    @Nullable
    protected String normalUserRole;
    /**
     * TODO: Model Documentation for
     * OperationalMetrics.documentDurationLimit
     */
    @Nullable
    protected Integer documentDurationLimit;
    /**
     * TODO: Model Documentation for
     * OperationalMetrics.dataVerificationThreshold
     */
    @Nullable
    protected Double dataVerificationThreshold;
    /**
     * TODO: Model Documentation for
     * OperationalMetrics.maxDataVerificationFailureThreshold
     */
    @Nullable
    protected Double maxDataVerificationFailureThreshold;
    /**
     * TODO: Model Documentation for OperationalMetrics.user
     */
    @Nullable
    protected String user;

    /**
     * Constructor taking only required properties
     */
    public OperationalMetrics()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param kycDurationIn Integer
     * @param timeToAccountCreationIn Integer
     * @param organisationAdminRoleIn String
     * @param normalUserRoleIn String
     * @param documentDurationLimitIn Integer
     * @param dataVerificationThresholdIn Double
     * @param maxDataVerificationFailureThresholdIn Double
     * @param userIn String
     */
    public OperationalMetrics(final Integer kycDurationIn, final Integer timeToAccountCreationIn, final String organisationAdminRoleIn, final String normalUserRoleIn, final Integer documentDurationLimitIn, final Double dataVerificationThresholdIn, final Double maxDataVerificationFailureThresholdIn, final String userIn)
    {
        this.kycDuration = kycDurationIn;
        this.timeToAccountCreation = timeToAccountCreationIn;
        this.organisationAdminRole = organisationAdminRoleIn;
        this.normalUserRole = normalUserRoleIn;
        this.documentDurationLimit = documentDurationLimitIn;
        this.dataVerificationThreshold = dataVerificationThresholdIn;
        this.maxDataVerificationFailureThreshold = maxDataVerificationFailureThresholdIn;
        this.user = userIn;
    }

    /**
     * Copies constructor from other OperationalMetrics
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public OperationalMetrics(final OperationalMetrics otherBean)
    {
        this.kycDuration = otherBean.getKycDuration();
        this.timeToAccountCreation = otherBean.getTimeToAccountCreation();
        this.organisationAdminRole = otherBean.getOrganisationAdminRole();
        this.normalUserRole = otherBean.getNormalUserRole();
        this.documentDurationLimit = otherBean.getDocumentDurationLimit();
        this.dataVerificationThreshold = otherBean.getDataVerificationThreshold();
        this.maxDataVerificationFailureThreshold = otherBean.getMaxDataVerificationFailureThreshold();
        this.user = otherBean.getUser();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final OperationalMetrics otherBean)
    {
        if (null != otherBean)
        {
            this.setKycDuration(otherBean.getKycDuration());
            this.setTimeToAccountCreation(otherBean.getTimeToAccountCreation());
            this.setOrganisationAdminRole(otherBean.getOrganisationAdminRole());
            this.setNormalUserRole(otherBean.getNormalUserRole());
            this.setDocumentDurationLimit(otherBean.getDocumentDurationLimit());
            this.setDataVerificationThreshold(otherBean.getDataVerificationThreshold());
            this.setMaxDataVerificationFailureThreshold(otherBean.getMaxDataVerificationFailureThreshold());
            this.setUser(otherBean.getUser());
        }
    }

    /**
     * TODO: Model Documentation for OperationalMetrics.kycDuration
     * Get the kycDuration Attribute
     * @return kycDuration Integer
     */
    public Integer getKycDuration()
    {
        return this.kycDuration;
    }

    /**
     * TODO: Model Documentation for OperationalMetrics.kycDuration
     * @param value Integer
     */
    public void setKycDuration(final Integer value)
    {
        this.kycDuration = value;
    }

    /**
     * TODO: Model Documentation for
     * OperationalMetrics.timeToAccountCreation
     * Get the timeToAccountCreation Attribute
     * @return timeToAccountCreation Integer
     */
    public Integer getTimeToAccountCreation()
    {
        return this.timeToAccountCreation;
    }

    /**
     * TODO: Model Documentation for
     * OperationalMetrics.timeToAccountCreation
     * @param value Integer
     */
    public void setTimeToAccountCreation(final Integer value)
    {
        this.timeToAccountCreation = value;
    }

    /**
     * TODO: Model Documentation for
     * OperationalMetrics.organisationAdminRole
     * Get the organisationAdminRole Attribute
     * @return organisationAdminRole String
     */
    public String getOrganisationAdminRole()
    {
        return this.organisationAdminRole;
    }

    /**
     * TODO: Model Documentation for
     * OperationalMetrics.organisationAdminRole
     * @param value String
     */
    public void setOrganisationAdminRole(final String value)
    {
        this.organisationAdminRole = value;
    }

    /**
     * TODO: Model Documentation for OperationalMetrics.normalUserRole
     * Get the normalUserRole Attribute
     * @return normalUserRole String
     */
    public String getNormalUserRole()
    {
        return this.normalUserRole;
    }

    /**
     * TODO: Model Documentation for OperationalMetrics.normalUserRole
     * @param value String
     */
    public void setNormalUserRole(final String value)
    {
        this.normalUserRole = value;
    }

    /**
     * TODO: Model Documentation for
     * OperationalMetrics.documentDurationLimit
     * Get the documentDurationLimit Attribute
     * @return documentDurationLimit Integer
     */
    public Integer getDocumentDurationLimit()
    {
        return this.documentDurationLimit;
    }

    /**
     * TODO: Model Documentation for
     * OperationalMetrics.documentDurationLimit
     * @param value Integer
     */
    public void setDocumentDurationLimit(final Integer value)
    {
        this.documentDurationLimit = value;
    }

    /**
     * TODO: Model Documentation for
     * OperationalMetrics.dataVerificationThreshold
     * Get the dataVerificationThreshold Attribute
     * @return dataVerificationThreshold Double
     */
    public Double getDataVerificationThreshold()
    {
        return this.dataVerificationThreshold;
    }

    /**
     * TODO: Model Documentation for
     * OperationalMetrics.dataVerificationThreshold
     * @param value Double
     */
    public void setDataVerificationThreshold(final Double value)
    {
        this.dataVerificationThreshold = value;
    }

    /**
     * TODO: Model Documentation for
     * OperationalMetrics.maxDataVerificationFailureThreshold
     * Get the maxDataVerificationFailureThreshold Attribute
     * @return maxDataVerificationFailureThreshold Double
     */
    public Double getMaxDataVerificationFailureThreshold()
    {
        return this.maxDataVerificationFailureThreshold;
    }

    /**
     * TODO: Model Documentation for
     * OperationalMetrics.maxDataVerificationFailureThreshold
     * @param value Double
     */
    public void setMaxDataVerificationFailureThreshold(final Double value)
    {
        this.maxDataVerificationFailureThreshold = value;
    }

    /**
     * TODO: Model Documentation for OperationalMetrics.user
     * Get the user Attribute
     * @return user String
     */
    public String getUser()
    {
        return this.user;
    }

    /**
     * TODO: Model Documentation for OperationalMetrics.user
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
        OperationalMetrics rhs = (OperationalMetrics) object;
        return new EqualsBuilder()
            .append(this.getKycDuration(), rhs.getKycDuration())
            .append(this.getTimeToAccountCreation(), rhs.getTimeToAccountCreation())
            .append(this.getOrganisationAdminRole(), rhs.getOrganisationAdminRole())
            .append(this.getNormalUserRole(), rhs.getNormalUserRole())
            .append(this.getDocumentDurationLimit(), rhs.getDocumentDurationLimit())
            .append(this.getDataVerificationThreshold(), rhs.getDataVerificationThreshold())
            .append(this.getMaxDataVerificationFailureThreshold(), rhs.getMaxDataVerificationFailureThreshold())
            .append(this.getUser(), rhs.getUser())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final OperationalMetrics object)
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
            .append(this.getKycDuration(), object.getKycDuration())
            .append(this.getTimeToAccountCreation(), object.getTimeToAccountCreation())
            .append(this.getOrganisationAdminRole(), object.getOrganisationAdminRole())
            .append(this.getNormalUserRole(), object.getNormalUserRole())
            .append(this.getDocumentDurationLimit(), object.getDocumentDurationLimit())
            .append(this.getDataVerificationThreshold(), object.getDataVerificationThreshold())
            .append(this.getMaxDataVerificationFailureThreshold(), object.getMaxDataVerificationFailureThreshold())
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
            .append(this.getKycDuration())
            .append(this.getTimeToAccountCreation())
            .append(this.getOrganisationAdminRole())
            .append(this.getNormalUserRole())
            .append(this.getDocumentDurationLimit())
            .append(this.getDataVerificationThreshold())
            .append(this.getMaxDataVerificationFailureThreshold())
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
        builder.append("\t\"kycDuration\" : \"" + this.getKycDuration() + "\"");
        builder.append("\t,\"timeToAccountCreation\" : \"" + this.getTimeToAccountCreation() + "\"");
        builder.append("\t,\"organisationAdminRole\" : \"" + this.getOrganisationAdminRole() + "\"");
        builder.append("\t,\"normalUserRole\" : \"" + this.getNormalUserRole() + "\"");
        builder.append("\t,\"documentDurationLimit\" : \"" + this.getDocumentDurationLimit() + "\"");
        builder.append("\t,\"dataVerificationThreshold\" : \"" + this.getDataVerificationThreshold() + "\"");
        builder.append("\t,\"maxDataVerificationFailureThreshold\" : \"" + this.getMaxDataVerificationFailureThreshold() + "\"");
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

        final OperationalMetrics that = (OperationalMetrics)thatObject;
        return
            equal(this.getKycDuration(), that.getKycDuration())
            && equal(this.getTimeToAccountCreation(), that.getTimeToAccountCreation())
            && equal(this.getOrganisationAdminRole(), that.getOrganisationAdminRole())
            && equal(this.getNormalUserRole(), that.getNormalUserRole())
            && equal(this.getDocumentDurationLimit(), that.getDocumentDurationLimit())
            && equal(this.getDataVerificationThreshold(), that.getDataVerificationThreshold())
            && equal(this.getMaxDataVerificationFailureThreshold(), that.getMaxDataVerificationFailureThreshold())
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