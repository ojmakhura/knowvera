package bw.co.knowvera.analytics;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for CountDTO
 */
public class CountDTO
    implements Serializable, Comparable<CountDTO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 7936017239047993468L;

    // Class attributes
    /**
     * TODO: Model Documentation for CountDTO.organisationCount
     */
    @Nullable
    protected Long organisationCount = 0L;
    /**
     * TODO: Model Documentation for CountDTO.individualCount
     */
    @Nullable
    protected Long individualCount = 0L;
    /**
     * TODO: Model Documentation for CountDTO.requestCount
     */
    @Nullable
    protected Long requestCount = 0L;
    /**
     * TODO: Model Documentation for CountDTO.compliantCount
     */
    @Nullable
    protected Long compliantCount = 0L;
    /**
     * TODO: Model Documentation for CountDTO.pepCount
     */
    @Nullable
    protected Long pepCount = 0L;
    /**
     * TODO: Model Documentation for CountDTO.pepRelativeCount
     */
    @Nullable
    protected Long pepRelativeCount = 0L;
    /**
     * TODO: Model Documentation for CountDTO.pepAssociateCount
     */
    @Nullable
    protected Long pepAssociateCount = 0L;
    /**
     * TODO: Model Documentation for CountDTO.kycComplianceExpiredCount
     */
    @Nullable
    protected Long kycComplianceExpiredCount = 0L;
    /**
     * TODO: Model Documentation for CountDTO.kycComplianceAbsentCount
     */
    @Nullable
    protected Long kycComplianceAbsentCount = 0L;
    /**
     * TODO: Model Documentation for CountDTO.kycComplianceIncompleteCount
     */
    @Nullable
    protected Long kycComplianceIncompleteCount = 0L;
    /**
     * TODO: Model Documentation for CountDTO.subscriptionCountActive
     */
    @Nullable
    protected Long subscriptionCountActive = 0L;
    /**
     * TODO: Model Documentation for CountDTO.subscriptionCountInactive
     */
    @Nullable
    protected Long subscriptionCountInactive = 0L;
    /**
     * TODO: Model Documentation for CountDTO.subscriptionCountCancelled
     */
    @Nullable
    protected Long subscriptionCountCancelled = 0L;
    /**
     * TODO: Model Documentation for CountDTO.paidInvoicesCount
     */
    @Nullable
    protected Long paidInvoicesCount = 0L;
    /**
     * TODO: Model Documentation for CountDTO.unpaidInvoicesCount
     */
    @Nullable
    protected Long unpaidInvoicesCount = 0L;
    /**
     * TODO: Model Documentation for CountDTO.invoicesCount
     */
    @Nullable
    protected Long invoicesCount = 0L;
    /**
     * TODO: Model Documentation for CountDTO.subscriptionCount
     */
    @Nullable
    protected Long subscriptionCount = 0L;

    /**
     * Constructor taking only required properties
     */
    public CountDTO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param organisationCountIn Long
     * @param individualCountIn Long
     * @param requestCountIn Long
     * @param compliantCountIn Long
     * @param pepCountIn Long
     * @param pepRelativeCountIn Long
     * @param pepAssociateCountIn Long
     * @param kycComplianceExpiredCountIn Long
     * @param kycComplianceAbsentCountIn Long
     * @param kycComplianceIncompleteCountIn Long
     * @param subscriptionCountActiveIn Long
     * @param subscriptionCountInactiveIn Long
     * @param subscriptionCountCancelledIn Long
     * @param paidInvoicesCountIn Long
     * @param unpaidInvoicesCountIn Long
     * @param invoicesCountIn Long
     * @param subscriptionCountIn Long
     */
    public CountDTO(final Long organisationCountIn, final Long individualCountIn, final Long requestCountIn, final Long compliantCountIn, final Long pepCountIn, final Long pepRelativeCountIn, final Long pepAssociateCountIn, final Long kycComplianceExpiredCountIn, final Long kycComplianceAbsentCountIn, final Long kycComplianceIncompleteCountIn, final Long subscriptionCountActiveIn, final Long subscriptionCountInactiveIn, final Long subscriptionCountCancelledIn, final Long paidInvoicesCountIn, final Long unpaidInvoicesCountIn, final Long invoicesCountIn, final Long subscriptionCountIn)
    {
        this.organisationCount = organisationCountIn;
        this.individualCount = individualCountIn;
        this.requestCount = requestCountIn;
        this.compliantCount = compliantCountIn;
        this.pepCount = pepCountIn;
        this.pepRelativeCount = pepRelativeCountIn;
        this.pepAssociateCount = pepAssociateCountIn;
        this.kycComplianceExpiredCount = kycComplianceExpiredCountIn;
        this.kycComplianceAbsentCount = kycComplianceAbsentCountIn;
        this.kycComplianceIncompleteCount = kycComplianceIncompleteCountIn;
        this.subscriptionCountActive = subscriptionCountActiveIn;
        this.subscriptionCountInactive = subscriptionCountInactiveIn;
        this.subscriptionCountCancelled = subscriptionCountCancelledIn;
        this.paidInvoicesCount = paidInvoicesCountIn;
        this.unpaidInvoicesCount = unpaidInvoicesCountIn;
        this.invoicesCount = invoicesCountIn;
        this.subscriptionCount = subscriptionCountIn;
    }

    /**
     * Copies constructor from other CountDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public CountDTO(final CountDTO otherBean)
    {
        this.organisationCount = otherBean.getOrganisationCount();
        this.individualCount = otherBean.getIndividualCount();
        this.requestCount = otherBean.getRequestCount();
        this.compliantCount = otherBean.getCompliantCount();
        this.pepCount = otherBean.getPepCount();
        this.pepRelativeCount = otherBean.getPepRelativeCount();
        this.pepAssociateCount = otherBean.getPepAssociateCount();
        this.kycComplianceExpiredCount = otherBean.getKycComplianceExpiredCount();
        this.kycComplianceAbsentCount = otherBean.getKycComplianceAbsentCount();
        this.kycComplianceIncompleteCount = otherBean.getKycComplianceIncompleteCount();
        this.subscriptionCountActive = otherBean.getSubscriptionCountActive();
        this.subscriptionCountInactive = otherBean.getSubscriptionCountInactive();
        this.subscriptionCountCancelled = otherBean.getSubscriptionCountCancelled();
        this.paidInvoicesCount = otherBean.getPaidInvoicesCount();
        this.unpaidInvoicesCount = otherBean.getUnpaidInvoicesCount();
        this.invoicesCount = otherBean.getInvoicesCount();
        this.subscriptionCount = otherBean.getSubscriptionCount();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final CountDTO otherBean)
    {
        if (null != otherBean)
        {
            this.setOrganisationCount(otherBean.getOrganisationCount());
            this.setIndividualCount(otherBean.getIndividualCount());
            this.setRequestCount(otherBean.getRequestCount());
            this.setCompliantCount(otherBean.getCompliantCount());
            this.setPepCount(otherBean.getPepCount());
            this.setPepRelativeCount(otherBean.getPepRelativeCount());
            this.setPepAssociateCount(otherBean.getPepAssociateCount());
            this.setKycComplianceExpiredCount(otherBean.getKycComplianceExpiredCount());
            this.setKycComplianceAbsentCount(otherBean.getKycComplianceAbsentCount());
            this.setKycComplianceIncompleteCount(otherBean.getKycComplianceIncompleteCount());
            this.setSubscriptionCountActive(otherBean.getSubscriptionCountActive());
            this.setSubscriptionCountInactive(otherBean.getSubscriptionCountInactive());
            this.setSubscriptionCountCancelled(otherBean.getSubscriptionCountCancelled());
            this.setPaidInvoicesCount(otherBean.getPaidInvoicesCount());
            this.setUnpaidInvoicesCount(otherBean.getUnpaidInvoicesCount());
            this.setInvoicesCount(otherBean.getInvoicesCount());
            this.setSubscriptionCount(otherBean.getSubscriptionCount());
        }
    }

    /**
     * TODO: Model Documentation for CountDTO.organisationCount
     * Get the organisationCount Attribute
     * @return organisationCount Long
     */
    public Long getOrganisationCount()
    {
        return this.organisationCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.organisationCount
     * @param value Long
     */
    public void setOrganisationCount(final Long value)
    {
        this.organisationCount = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.individualCount
     * Get the individualCount Attribute
     * @return individualCount Long
     */
    public Long getIndividualCount()
    {
        return this.individualCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.individualCount
     * @param value Long
     */
    public void setIndividualCount(final Long value)
    {
        this.individualCount = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.requestCount
     * Get the requestCount Attribute
     * @return requestCount Long
     */
    public Long getRequestCount()
    {
        return this.requestCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.requestCount
     * @param value Long
     */
    public void setRequestCount(final Long value)
    {
        this.requestCount = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.compliantCount
     * Get the compliantCount Attribute
     * @return compliantCount Long
     */
    public Long getCompliantCount()
    {
        return this.compliantCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.compliantCount
     * @param value Long
     */
    public void setCompliantCount(final Long value)
    {
        this.compliantCount = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.pepCount
     * Get the pepCount Attribute
     * @return pepCount Long
     */
    public Long getPepCount()
    {
        return this.pepCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.pepCount
     * @param value Long
     */
    public void setPepCount(final Long value)
    {
        this.pepCount = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.pepRelativeCount
     * Get the pepRelativeCount Attribute
     * @return pepRelativeCount Long
     */
    public Long getPepRelativeCount()
    {
        return this.pepRelativeCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.pepRelativeCount
     * @param value Long
     */
    public void setPepRelativeCount(final Long value)
    {
        this.pepRelativeCount = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.pepAssociateCount
     * Get the pepAssociateCount Attribute
     * @return pepAssociateCount Long
     */
    public Long getPepAssociateCount()
    {
        return this.pepAssociateCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.pepAssociateCount
     * @param value Long
     */
    public void setPepAssociateCount(final Long value)
    {
        this.pepAssociateCount = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.kycComplianceExpiredCount
     * Get the kycComplianceExpiredCount Attribute
     * @return kycComplianceExpiredCount Long
     */
    public Long getKycComplianceExpiredCount()
    {
        return this.kycComplianceExpiredCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.kycComplianceExpiredCount
     * @param value Long
     */
    public void setKycComplianceExpiredCount(final Long value)
    {
        this.kycComplianceExpiredCount = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.kycComplianceAbsentCount
     * Get the kycComplianceAbsentCount Attribute
     * @return kycComplianceAbsentCount Long
     */
    public Long getKycComplianceAbsentCount()
    {
        return this.kycComplianceAbsentCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.kycComplianceAbsentCount
     * @param value Long
     */
    public void setKycComplianceAbsentCount(final Long value)
    {
        this.kycComplianceAbsentCount = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.kycComplianceIncompleteCount
     * Get the kycComplianceIncompleteCount Attribute
     * @return kycComplianceIncompleteCount Long
     */
    public Long getKycComplianceIncompleteCount()
    {
        return this.kycComplianceIncompleteCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.kycComplianceIncompleteCount
     * @param value Long
     */
    public void setKycComplianceIncompleteCount(final Long value)
    {
        this.kycComplianceIncompleteCount = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.subscriptionCountActive
     * Get the subscriptionCountActive Attribute
     * @return subscriptionCountActive Long
     */
    public Long getSubscriptionCountActive()
    {
        return this.subscriptionCountActive;
    }

    /**
     * TODO: Model Documentation for CountDTO.subscriptionCountActive
     * @param value Long
     */
    public void setSubscriptionCountActive(final Long value)
    {
        this.subscriptionCountActive = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.subscriptionCountInactive
     * Get the subscriptionCountInactive Attribute
     * @return subscriptionCountInactive Long
     */
    public Long getSubscriptionCountInactive()
    {
        return this.subscriptionCountInactive;
    }

    /**
     * TODO: Model Documentation for CountDTO.subscriptionCountInactive
     * @param value Long
     */
    public void setSubscriptionCountInactive(final Long value)
    {
        this.subscriptionCountInactive = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.subscriptionCountCancelled
     * Get the subscriptionCountCancelled Attribute
     * @return subscriptionCountCancelled Long
     */
    public Long getSubscriptionCountCancelled()
    {
        return this.subscriptionCountCancelled;
    }

    /**
     * TODO: Model Documentation for CountDTO.subscriptionCountCancelled
     * @param value Long
     */
    public void setSubscriptionCountCancelled(final Long value)
    {
        this.subscriptionCountCancelled = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.paidInvoicesCount
     * Get the paidInvoicesCount Attribute
     * @return paidInvoicesCount Long
     */
    public Long getPaidInvoicesCount()
    {
        return this.paidInvoicesCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.paidInvoicesCount
     * @param value Long
     */
    public void setPaidInvoicesCount(final Long value)
    {
        this.paidInvoicesCount = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.unpaidInvoicesCount
     * Get the unpaidInvoicesCount Attribute
     * @return unpaidInvoicesCount Long
     */
    public Long getUnpaidInvoicesCount()
    {
        return this.unpaidInvoicesCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.unpaidInvoicesCount
     * @param value Long
     */
    public void setUnpaidInvoicesCount(final Long value)
    {
        this.unpaidInvoicesCount = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.invoicesCount
     * Get the invoicesCount Attribute
     * @return invoicesCount Long
     */
    public Long getInvoicesCount()
    {
        return this.invoicesCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.invoicesCount
     * @param value Long
     */
    public void setInvoicesCount(final Long value)
    {
        this.invoicesCount = value;
    }

    /**
     * TODO: Model Documentation for CountDTO.subscriptionCount
     * Get the subscriptionCount Attribute
     * @return subscriptionCount Long
     */
    public Long getSubscriptionCount()
    {
        return this.subscriptionCount;
    }

    /**
     * TODO: Model Documentation for CountDTO.subscriptionCount
     * @param value Long
     */
    public void setSubscriptionCount(final Long value)
    {
        this.subscriptionCount = value;
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
        CountDTO rhs = (CountDTO) object;
        return new EqualsBuilder()
            .append(this.getOrganisationCount(), rhs.getOrganisationCount())
            .append(this.getIndividualCount(), rhs.getIndividualCount())
            .append(this.getRequestCount(), rhs.getRequestCount())
            .append(this.getCompliantCount(), rhs.getCompliantCount())
            .append(this.getPepCount(), rhs.getPepCount())
            .append(this.getPepRelativeCount(), rhs.getPepRelativeCount())
            .append(this.getPepAssociateCount(), rhs.getPepAssociateCount())
            .append(this.getKycComplianceExpiredCount(), rhs.getKycComplianceExpiredCount())
            .append(this.getKycComplianceAbsentCount(), rhs.getKycComplianceAbsentCount())
            .append(this.getKycComplianceIncompleteCount(), rhs.getKycComplianceIncompleteCount())
            .append(this.getSubscriptionCountActive(), rhs.getSubscriptionCountActive())
            .append(this.getSubscriptionCountInactive(), rhs.getSubscriptionCountInactive())
            .append(this.getSubscriptionCountCancelled(), rhs.getSubscriptionCountCancelled())
            .append(this.getPaidInvoicesCount(), rhs.getPaidInvoicesCount())
            .append(this.getUnpaidInvoicesCount(), rhs.getUnpaidInvoicesCount())
            .append(this.getInvoicesCount(), rhs.getInvoicesCount())
            .append(this.getSubscriptionCount(), rhs.getSubscriptionCount())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final CountDTO object)
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
            .append(this.getOrganisationCount(), object.getOrganisationCount())
            .append(this.getIndividualCount(), object.getIndividualCount())
            .append(this.getRequestCount(), object.getRequestCount())
            .append(this.getCompliantCount(), object.getCompliantCount())
            .append(this.getPepCount(), object.getPepCount())
            .append(this.getPepRelativeCount(), object.getPepRelativeCount())
            .append(this.getPepAssociateCount(), object.getPepAssociateCount())
            .append(this.getKycComplianceExpiredCount(), object.getKycComplianceExpiredCount())
            .append(this.getKycComplianceAbsentCount(), object.getKycComplianceAbsentCount())
            .append(this.getKycComplianceIncompleteCount(), object.getKycComplianceIncompleteCount())
            .append(this.getSubscriptionCountActive(), object.getSubscriptionCountActive())
            .append(this.getSubscriptionCountInactive(), object.getSubscriptionCountInactive())
            .append(this.getSubscriptionCountCancelled(), object.getSubscriptionCountCancelled())
            .append(this.getPaidInvoicesCount(), object.getPaidInvoicesCount())
            .append(this.getUnpaidInvoicesCount(), object.getUnpaidInvoicesCount())
            .append(this.getInvoicesCount(), object.getInvoicesCount())
            .append(this.getSubscriptionCount(), object.getSubscriptionCount())
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
            .append(this.getOrganisationCount())
            .append(this.getIndividualCount())
            .append(this.getRequestCount())
            .append(this.getCompliantCount())
            .append(this.getPepCount())
            .append(this.getPepRelativeCount())
            .append(this.getPepAssociateCount())
            .append(this.getKycComplianceExpiredCount())
            .append(this.getKycComplianceAbsentCount())
            .append(this.getKycComplianceIncompleteCount())
            .append(this.getSubscriptionCountActive())
            .append(this.getSubscriptionCountInactive())
            .append(this.getSubscriptionCountCancelled())
            .append(this.getPaidInvoicesCount())
            .append(this.getUnpaidInvoicesCount())
            .append(this.getInvoicesCount())
            .append(this.getSubscriptionCount())
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
        builder.append("\t\"organisationCount\" : \"" + this.getOrganisationCount() + "\"");
        builder.append("\t,\"individualCount\" : \"" + this.getIndividualCount() + "\"");
        builder.append("\t,\"requestCount\" : \"" + this.getRequestCount() + "\"");
        builder.append("\t,\"compliantCount\" : \"" + this.getCompliantCount() + "\"");
        builder.append("\t,\"pepCount\" : \"" + this.getPepCount() + "\"");
        builder.append("\t,\"pepRelativeCount\" : \"" + this.getPepRelativeCount() + "\"");
        builder.append("\t,\"pepAssociateCount\" : \"" + this.getPepAssociateCount() + "\"");
        builder.append("\t,\"kycComplianceExpiredCount\" : \"" + this.getKycComplianceExpiredCount() + "\"");
        builder.append("\t,\"kycComplianceAbsentCount\" : \"" + this.getKycComplianceAbsentCount() + "\"");
        builder.append("\t,\"kycComplianceIncompleteCount\" : \"" + this.getKycComplianceIncompleteCount() + "\"");
        builder.append("\t,\"subscriptionCountActive\" : \"" + this.getSubscriptionCountActive() + "\"");
        builder.append("\t,\"subscriptionCountInactive\" : \"" + this.getSubscriptionCountInactive() + "\"");
        builder.append("\t,\"subscriptionCountCancelled\" : \"" + this.getSubscriptionCountCancelled() + "\"");
        builder.append("\t,\"paidInvoicesCount\" : \"" + this.getPaidInvoicesCount() + "\"");
        builder.append("\t,\"unpaidInvoicesCount\" : \"" + this.getUnpaidInvoicesCount() + "\"");
        builder.append("\t,\"invoicesCount\" : \"" + this.getInvoicesCount() + "\"");
        builder.append("\t,\"subscriptionCount\" : \"" + this.getSubscriptionCount() + "\"");
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

        final CountDTO that = (CountDTO)thatObject;
        return
            equal(this.getOrganisationCount(), that.getOrganisationCount())
            && equal(this.getIndividualCount(), that.getIndividualCount())
            && equal(this.getRequestCount(), that.getRequestCount())
            && equal(this.getCompliantCount(), that.getCompliantCount())
            && equal(this.getPepCount(), that.getPepCount())
            && equal(this.getPepRelativeCount(), that.getPepRelativeCount())
            && equal(this.getPepAssociateCount(), that.getPepAssociateCount())
            && equal(this.getKycComplianceExpiredCount(), that.getKycComplianceExpiredCount())
            && equal(this.getKycComplianceAbsentCount(), that.getKycComplianceAbsentCount())
            && equal(this.getKycComplianceIncompleteCount(), that.getKycComplianceIncompleteCount())
            && equal(this.getSubscriptionCountActive(), that.getSubscriptionCountActive())
            && equal(this.getSubscriptionCountInactive(), that.getSubscriptionCountInactive())
            && equal(this.getSubscriptionCountCancelled(), that.getSubscriptionCountCancelled())
            && equal(this.getPaidInvoicesCount(), that.getPaidInvoicesCount())
            && equal(this.getUnpaidInvoicesCount(), that.getUnpaidInvoicesCount())
            && equal(this.getInvoicesCount(), that.getInvoicesCount())
            && equal(this.getSubscriptionCount(), that.getSubscriptionCount())
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