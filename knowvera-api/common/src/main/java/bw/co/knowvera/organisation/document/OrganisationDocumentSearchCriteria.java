package bw.co.knowvera.organisation.document;

import bw.co.knowvera.TargetEntity;
import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for
 * OrganisationDocumentSearchCriteria
 */
public class OrganisationDocumentSearchCriteria
    implements Serializable, Comparable<OrganisationDocumentSearchCriteria>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -6634989780489514906L;

    // Class attributes
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.documentTypeId
     */
    @Nullable
    protected String documentTypeId;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.documentType
     */
    @Nullable
    protected String documentType;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.status
     */
    @Nullable
    protected OrganisationDocumentStatus status;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.organisation
     */
    @Nullable
    protected String organisation;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.organisationId
     */
    @Nullable
    protected String organisationId;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.organisationRegistrationNo
     */
    @Nullable
    protected String organisationRegistrationNo;
    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.target
     */
    @Nullable
    protected TargetEntity target;

    /**
     * Constructor taking only required properties
     */
    public OrganisationDocumentSearchCriteria()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param documentTypeIdIn String
     * @param documentTypeIn String
     * @param statusIn OrganisationDocumentStatus
     * @param organisationIn String
     * @param organisationIdIn String
     * @param organisationRegistrationNoIn String
     * @param targetIn TargetEntity
     */
    public OrganisationDocumentSearchCriteria(final String documentTypeIdIn, final String documentTypeIn, final OrganisationDocumentStatus statusIn, final String organisationIn, final String organisationIdIn, final String organisationRegistrationNoIn, final TargetEntity targetIn)
    {
        this.documentTypeId = documentTypeIdIn;
        this.documentType = documentTypeIn;
        this.status = statusIn;
        this.organisation = organisationIn;
        this.organisationId = organisationIdIn;
        this.organisationRegistrationNo = organisationRegistrationNoIn;
        this.target = targetIn;
    }

    /**
     * Copies constructor from other OrganisationDocumentSearchCriteria
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public OrganisationDocumentSearchCriteria(final OrganisationDocumentSearchCriteria otherBean)
    {
        this.documentTypeId = otherBean.getDocumentTypeId();
        this.documentType = otherBean.getDocumentType();
        this.status = otherBean.getStatus();
        this.organisation = otherBean.getOrganisation();
        this.organisationId = otherBean.getOrganisationId();
        this.organisationRegistrationNo = otherBean.getOrganisationRegistrationNo();
        this.target = otherBean.getTarget();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final OrganisationDocumentSearchCriteria otherBean)
    {
        if (null != otherBean)
        {
            this.setDocumentTypeId(otherBean.getDocumentTypeId());
            this.setDocumentType(otherBean.getDocumentType());
            this.setStatus(otherBean.getStatus());
            this.setOrganisation(otherBean.getOrganisation());
            this.setOrganisationId(otherBean.getOrganisationId());
            this.setOrganisationRegistrationNo(otherBean.getOrganisationRegistrationNo());
            this.setTarget(otherBean.getTarget());
        }
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.documentTypeId
     * Get the documentTypeId Attribute
     * @return documentTypeId String
     */
    public String getDocumentTypeId()
    {
        return this.documentTypeId;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.documentTypeId
     * @param value String
     */
    public void setDocumentTypeId(final String value)
    {
        this.documentTypeId = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.documentType
     * Get the documentType Attribute
     * @return documentType String
     */
    public String getDocumentType()
    {
        return this.documentType;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.documentType
     * @param value String
     */
    public void setDocumentType(final String value)
    {
        this.documentType = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.status
     * Get the status Attribute
     * @return status OrganisationDocumentStatus
     */
    public OrganisationDocumentStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.status
     * @param value OrganisationDocumentStatus
     */
    public void setStatus(final OrganisationDocumentStatus value)
    {
        this.status = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.organisation
     * Get the organisation Attribute
     * @return organisation String
     */
    public String getOrganisation()
    {
        return this.organisation;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.organisation
     * @param value String
     */
    public void setOrganisation(final String value)
    {
        this.organisation = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.organisationId
     * Get the organisationId Attribute
     * @return organisationId String
     */
    public String getOrganisationId()
    {
        return this.organisationId;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.organisationId
     * @param value String
     */
    public void setOrganisationId(final String value)
    {
        this.organisationId = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.organisationRegistrationNo
     * Get the organisationRegistrationNo Attribute
     * @return organisationRegistrationNo String
     */
    public String getOrganisationRegistrationNo()
    {
        return this.organisationRegistrationNo;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.organisationRegistrationNo
     * @param value String
     */
    public void setOrganisationRegistrationNo(final String value)
    {
        this.organisationRegistrationNo = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.target
     * Get the target Attribute
     * @return target TargetEntity
     */
    public TargetEntity getTarget()
    {
        return this.target;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDocumentSearchCriteria.target
     * @param value TargetEntity
     */
    public void setTarget(final TargetEntity value)
    {
        this.target = value;
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
        OrganisationDocumentSearchCriteria rhs = (OrganisationDocumentSearchCriteria) object;
        return new EqualsBuilder()
            .append(this.getDocumentTypeId(), rhs.getDocumentTypeId())
            .append(this.getDocumentType(), rhs.getDocumentType())
            .append(this.getStatus(), rhs.getStatus())
            .append(this.getOrganisation(), rhs.getOrganisation())
            .append(this.getOrganisationId(), rhs.getOrganisationId())
            .append(this.getOrganisationRegistrationNo(), rhs.getOrganisationRegistrationNo())
            .append(this.getTarget(), rhs.getTarget())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final OrganisationDocumentSearchCriteria object)
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
            .append(this.getDocumentTypeId(), object.getDocumentTypeId())
            .append(this.getDocumentType(), object.getDocumentType())
            .append(this.getStatus(), object.getStatus())
            .append(this.getOrganisation(), object.getOrganisation())
            .append(this.getOrganisationId(), object.getOrganisationId())
            .append(this.getOrganisationRegistrationNo(), object.getOrganisationRegistrationNo())
            .append(this.getTarget(), object.getTarget())
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
            .append(this.getDocumentTypeId())
            .append(this.getDocumentType())
            .append(this.getStatus())
            .append(this.getOrganisation())
            .append(this.getOrganisationId())
            .append(this.getOrganisationRegistrationNo())
            .append(this.getTarget())
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
        builder.append("\t\"documentTypeId\" : \"" + this.getDocumentTypeId() + "\"");
        builder.append("\t,\"documentType\" : \"" + this.getDocumentType() + "\"");
        builder.append("\t,\"status\" : \"" + this.getStatus() + "\"");
        builder.append("\t,\"organisation\" : \"" + this.getOrganisation() + "\"");
        builder.append("\t,\"organisationId\" : \"" + this.getOrganisationId() + "\"");
        builder.append("\t,\"organisationRegistrationNo\" : \"" + this.getOrganisationRegistrationNo() + "\"");
        builder.append("\t,\"target\" : \"" + this.getTarget() + "\"");
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

        final OrganisationDocumentSearchCriteria that = (OrganisationDocumentSearchCriteria)thatObject;
        return
            equal(this.getDocumentTypeId(), that.getDocumentTypeId())
            && equal(this.getDocumentType(), that.getDocumentType())
            && equal(this.getStatus(), that.getStatus())
            && equal(this.getOrganisation(), that.getOrganisation())
            && equal(this.getOrganisationId(), that.getOrganisationId())
            && equal(this.getOrganisationRegistrationNo(), that.getOrganisationRegistrationNo())
            && equal(this.getTarget(), that.getTarget())
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