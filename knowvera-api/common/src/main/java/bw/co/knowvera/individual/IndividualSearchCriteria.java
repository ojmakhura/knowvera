package bw.co.knowvera.individual;

import bw.co.knowvera.kyc.KycComplianceStatus;
import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for IndividualSearchCriteria
 */
public class IndividualSearchCriteria
    implements Serializable, Comparable<IndividualSearchCriteria>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -9104240596103337681L;

    // Class attributes
    /**
     * TODO: Model Documentation for IndividualSearchCriteria.emailAddress
     */
    @Nullable
    protected String emailAddress;
    /**
     * TODO: Model Documentation for IndividualSearchCriteria.identityNo
     */
    @Nullable
    protected String identityNo;
    /**
     * TODO: Model Documentation for IndividualSearchCriteria.identityType
     */
    @Nullable
    protected IndividualIdentityType identityType;
    /**
     * TODO: Model Documentation for IndividualSearchCriteria.kycStatus
     */
    @Nullable
    protected KycComplianceStatus kycStatus;
    /**
     * TODO: Model Documentation for IndividualSearchCriteria.surname
     */
    @Nullable
    protected String surname;
    /**
     * TODO: Model Documentation for IndividualSearchCriteria.middleName
     */
    @Nullable
    protected String middleName;
    /**
     * TODO: Model Documentation for IndividualSearchCriteria.firstName
     */
    @Nullable
    protected String firstName;

    /**
     * Constructor taking only required properties
     */
    public IndividualSearchCriteria()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param emailAddressIn String
     * @param identityNoIn String
     * @param identityTypeIn IndividualIdentityType
     * @param kycStatusIn KycComplianceStatus
     * @param surnameIn String
     * @param middleNameIn String
     * @param firstNameIn String
     */
    public IndividualSearchCriteria(final String emailAddressIn, final String identityNoIn, final IndividualIdentityType identityTypeIn, final KycComplianceStatus kycStatusIn, final String surnameIn, final String middleNameIn, final String firstNameIn)
    {
        this.emailAddress = emailAddressIn;
        this.identityNo = identityNoIn;
        this.identityType = identityTypeIn;
        this.kycStatus = kycStatusIn;
        this.surname = surnameIn;
        this.middleName = middleNameIn;
        this.firstName = firstNameIn;
    }

    /**
     * Copies constructor from other IndividualSearchCriteria
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public IndividualSearchCriteria(final IndividualSearchCriteria otherBean)
    {
        this.emailAddress = otherBean.getEmailAddress();
        this.identityNo = otherBean.getIdentityNo();
        this.identityType = otherBean.getIdentityType();
        this.kycStatus = otherBean.getKycStatus();
        this.surname = otherBean.getSurname();
        this.middleName = otherBean.getMiddleName();
        this.firstName = otherBean.getFirstName();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final IndividualSearchCriteria otherBean)
    {
        if (null != otherBean)
        {
            this.setEmailAddress(otherBean.getEmailAddress());
            this.setIdentityNo(otherBean.getIdentityNo());
            this.setIdentityType(otherBean.getIdentityType());
            this.setKycStatus(otherBean.getKycStatus());
            this.setSurname(otherBean.getSurname());
            this.setMiddleName(otherBean.getMiddleName());
            this.setFirstName(otherBean.getFirstName());
        }
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.emailAddress
     * Get the emailAddress Attribute
     * @return emailAddress String
     */
    public String getEmailAddress()
    {
        return this.emailAddress;
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.emailAddress
     * @param value String
     */
    public void setEmailAddress(final String value)
    {
        this.emailAddress = value;
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.identityNo
     * Get the identityNo Attribute
     * @return identityNo String
     */
    public String getIdentityNo()
    {
        return this.identityNo;
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.identityNo
     * @param value String
     */
    public void setIdentityNo(final String value)
    {
        this.identityNo = value;
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.identityType
     * Get the identityType Attribute
     * @return identityType IndividualIdentityType
     */
    public IndividualIdentityType getIdentityType()
    {
        return this.identityType;
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.identityType
     * @param value IndividualIdentityType
     */
    public void setIdentityType(final IndividualIdentityType value)
    {
        this.identityType = value;
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.kycStatus
     * Get the kycStatus Attribute
     * @return kycStatus KycComplianceStatus
     */
    public KycComplianceStatus getKycStatus()
    {
        return this.kycStatus;
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.kycStatus
     * @param value KycComplianceStatus
     */
    public void setKycStatus(final KycComplianceStatus value)
    {
        this.kycStatus = value;
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.surname
     * Get the surname Attribute
     * @return surname String
     */
    public String getSurname()
    {
        return this.surname;
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.surname
     * @param value String
     */
    public void setSurname(final String value)
    {
        this.surname = value;
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.middleName
     * Get the middleName Attribute
     * @return middleName String
     */
    public String getMiddleName()
    {
        return this.middleName;
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.middleName
     * @param value String
     */
    public void setMiddleName(final String value)
    {
        this.middleName = value;
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.firstName
     * Get the firstName Attribute
     * @return firstName String
     */
    public String getFirstName()
    {
        return this.firstName;
    }

    /**
     * TODO: Model Documentation for IndividualSearchCriteria.firstName
     * @param value String
     */
    public void setFirstName(final String value)
    {
        this.firstName = value;
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
        IndividualSearchCriteria rhs = (IndividualSearchCriteria) object;
        return new EqualsBuilder()
            .append(this.getEmailAddress(), rhs.getEmailAddress())
            .append(this.getIdentityNo(), rhs.getIdentityNo())
            .append(this.getIdentityType(), rhs.getIdentityType())
            .append(this.getKycStatus(), rhs.getKycStatus())
            .append(this.getSurname(), rhs.getSurname())
            .append(this.getMiddleName(), rhs.getMiddleName())
            .append(this.getFirstName(), rhs.getFirstName())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final IndividualSearchCriteria object)
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
            .append(this.getIdentityNo(), object.getIdentityNo())
            .append(this.getIdentityType(), object.getIdentityType())
            .append(this.getKycStatus(), object.getKycStatus())
            .append(this.getSurname(), object.getSurname())
            .append(this.getMiddleName(), object.getMiddleName())
            .append(this.getFirstName(), object.getFirstName())
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
            .append(this.getIdentityNo())
            .append(this.getIdentityType())
            .append(this.getKycStatus())
            .append(this.getSurname())
            .append(this.getMiddleName())
            .append(this.getFirstName())
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
        builder.append("\t,\"identityNo\" : \"" + this.getIdentityNo() + "\"");
        builder.append("\t,\"identityType\" : \"" + this.getIdentityType() + "\"");
        builder.append("\t,\"kycStatus\" : \"" + this.getKycStatus() + "\"");
        builder.append("\t,\"surname\" : \"" + this.getSurname() + "\"");
        builder.append("\t,\"middleName\" : \"" + this.getMiddleName() + "\"");
        builder.append("\t,\"firstName\" : \"" + this.getFirstName() + "\"");
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

        final IndividualSearchCriteria that = (IndividualSearchCriteria)thatObject;
        return
            equal(this.getEmailAddress(), that.getEmailAddress())
            && equal(this.getIdentityNo(), that.getIdentityNo())
            && equal(this.getIdentityType(), that.getIdentityType())
            && equal(this.getKycStatus(), that.getKycStatus())
            && equal(this.getSurname(), that.getSurname())
            && equal(this.getMiddleName(), that.getMiddleName())
            && equal(this.getFirstName(), that.getFirstName())
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