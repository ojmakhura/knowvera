package bw.co.knowvera.kyc;

import bw.co.knowvera.PhoneNumber;
import bw.co.knowvera.individual.IndividualIdentityType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for OwnerDetails
 */
public class OwnerDetails
    implements Serializable, Comparable<OwnerDetails>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 1872525254090130685L;

    // Class attributes
    /**
     * TODO: Model Documentation for OwnerDetails.phoneNumbers
     */
    @Nullable
    protected Collection<PhoneNumber> phoneNumbers;
    /**
     * TODO: Model Documentation for OwnerDetails.physicalAddress
     */
    @Nullable
    protected String physicalAddress;
    /**
     * TODO: Model Documentation for OwnerDetails.postalAddress
     */
    @Nullable
    protected String postalAddress;
    /**
     * TODO: Model Documentation for OwnerDetails.emailAddress
     */
    @Nullable
    protected String emailAddress;
    /**
     * TODO: Model Documentation for OwnerDetails.identityNo
     */
    @Nullable
    protected String identityNo;
    /**
     * TODO: Model Documentation for OwnerDetails.name
     */
    @Nullable
    protected String name;
    /**
     * TODO: Model Documentation for OwnerDetails.identityType
     */
    protected IndividualIdentityType identityType;
    /**
     * TODO: Model Documentation for OwnerDetails.id
     */
    @Nullable
    protected String id;

    /** Default Constructor with no properties */
    public OwnerDetails()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param identityTypeIn IndividualIdentityType TODO: Model Documentation for OwnerDetails.identityType
     */
    public OwnerDetails(final IndividualIdentityType identityTypeIn)
    {
        this.identityType = identityTypeIn;
    }

    /**
     * Constructor with all properties
     * @param phoneNumbersIn Collection<PhoneNumber>
     * @param physicalAddressIn String
     * @param postalAddressIn String
     * @param emailAddressIn String
     * @param identityNoIn String
     * @param nameIn String
     * @param identityTypeIn IndividualIdentityType
     * @param idIn String
     */
    public OwnerDetails(final Collection<PhoneNumber> phoneNumbersIn, final String physicalAddressIn, final String postalAddressIn, final String emailAddressIn, final String identityNoIn, final String nameIn, final IndividualIdentityType identityTypeIn, final String idIn)
    {
        this.phoneNumbers = phoneNumbersIn;
        this.physicalAddress = physicalAddressIn;
        this.postalAddress = postalAddressIn;
        this.emailAddress = emailAddressIn;
        this.identityNo = identityNoIn;
        this.name = nameIn;
        this.identityType = identityTypeIn;
        this.id = idIn;
    }

    /**
     * Copies constructor from other OwnerDetails
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public OwnerDetails(final OwnerDetails otherBean)
    {
        this.phoneNumbers = otherBean.getPhoneNumbers();
        this.physicalAddress = otherBean.getPhysicalAddress();
        this.postalAddress = otherBean.getPostalAddress();
        this.emailAddress = otherBean.getEmailAddress();
        this.identityNo = otherBean.getIdentityNo();
        this.name = otherBean.getName();
        this.identityType = otherBean.getIdentityType();
        this.id = otherBean.getId();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final OwnerDetails otherBean)
    {
        if (null != otherBean)
        {
            this.setPhoneNumbers(otherBean.getPhoneNumbers());
            this.setPhysicalAddress(otherBean.getPhysicalAddress());
            this.setPostalAddress(otherBean.getPostalAddress());
            this.setEmailAddress(otherBean.getEmailAddress());
            this.setIdentityNo(otherBean.getIdentityNo());
            this.setName(otherBean.getName());
            this.setIdentityType(otherBean.getIdentityType());
            this.setId(otherBean.getId());
        }
    }

    /**
     * TODO: Model Documentation for OwnerDetails.phoneNumbers
     * Get the phoneNumbers Attribute
     * @return phoneNumbers Collection<PhoneNumber>
     */
    public Collection<PhoneNumber> getPhoneNumbers()
    {
        if (this.phoneNumbers == null)
        {
            this.phoneNumbers = new ArrayList<PhoneNumber>();
        }
        return this.phoneNumbers;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.phoneNumbers
     * @param value Collection<PhoneNumber>
     */
    public void setPhoneNumbers(final Collection<PhoneNumber> value)
    {
        this.phoneNumbers = value;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.physicalAddress
     * Get the physicalAddress Attribute
     * @return physicalAddress String
     */
    public String getPhysicalAddress()
    {
        return this.physicalAddress;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.physicalAddress
     * @param value String
     */
    public void setPhysicalAddress(final String value)
    {
        this.physicalAddress = value;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.postalAddress
     * Get the postalAddress Attribute
     * @return postalAddress String
     */
    public String getPostalAddress()
    {
        return this.postalAddress;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.postalAddress
     * @param value String
     */
    public void setPostalAddress(final String value)
    {
        this.postalAddress = value;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.emailAddress
     * Get the emailAddress Attribute
     * @return emailAddress String
     */
    public String getEmailAddress()
    {
        return this.emailAddress;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.emailAddress
     * @param value String
     */
    public void setEmailAddress(final String value)
    {
        this.emailAddress = value;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.identityNo
     * Get the identityNo Attribute
     * @return identityNo String
     */
    public String getIdentityNo()
    {
        return this.identityNo;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.identityNo
     * @param value String
     */
    public void setIdentityNo(final String value)
    {
        this.identityNo = value;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.identityType
     * Get the identityType Attribute
     * @return identityType IndividualIdentityType
     */
    public IndividualIdentityType getIdentityType()
    {
        return this.identityType;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.identityType
     * @param value IndividualIdentityType
     */
    public void setIdentityType(final IndividualIdentityType value)
    {
        this.identityType = value;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.id
     * Get the id Attribute
     * @return id String
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for OwnerDetails.id
     * @param value String
     */
    public void setId(final String value)
    {
        this.id = value;
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
        OwnerDetails rhs = (OwnerDetails) object;
        return new EqualsBuilder()
            .append(this.getPhoneNumbers(), rhs.getPhoneNumbers())
            .append(this.getPhysicalAddress(), rhs.getPhysicalAddress())
            .append(this.getPostalAddress(), rhs.getPostalAddress())
            .append(this.getEmailAddress(), rhs.getEmailAddress())
            .append(this.getIdentityNo(), rhs.getIdentityNo())
            .append(this.getName(), rhs.getName())
            .append(this.getIdentityType(), rhs.getIdentityType())
            .append(this.getId(), rhs.getId())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final OwnerDetails object)
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
            .append(this.getPhoneNumbers(), object.getPhoneNumbers())
            .append(this.getPhysicalAddress(), object.getPhysicalAddress())
            .append(this.getPostalAddress(), object.getPostalAddress())
            .append(this.getEmailAddress(), object.getEmailAddress())
            .append(this.getIdentityNo(), object.getIdentityNo())
            .append(this.getName(), object.getName())
            .append(this.getIdentityType(), object.getIdentityType())
            .append(this.getId(), object.getId())
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
            .append(this.getPhoneNumbers())
            .append(this.getPhysicalAddress())
            .append(this.getPostalAddress())
            .append(this.getEmailAddress())
            .append(this.getIdentityNo())
            .append(this.getName())
            .append(this.getIdentityType())
            .append(this.getId())
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
        builder.append("\t\"phoneNumbers\" : \"" + this.getPhoneNumbers() + "\"");
        builder.append("\t,\"physicalAddress\" : \"" + this.getPhysicalAddress() + "\"");
        builder.append("\t,\"postalAddress\" : \"" + this.getPostalAddress() + "\"");
        builder.append("\t,\"emailAddress\" : \"" + this.getEmailAddress() + "\"");
        builder.append("\t,\"identityNo\" : \"" + this.getIdentityNo() + "\"");
        builder.append("\t,\"name\" : \"" + this.getName() + "\"");
        builder.append("\t,\"identityType\" : \"" + this.getIdentityType() + "\"");
        builder.append("\t,\"id\" : \"" + this.getId() + "\"");
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

        final OwnerDetails that = (OwnerDetails)thatObject;
        return
            equal(this.getPhoneNumbers(), that.getPhoneNumbers())
            && equal(this.getPhysicalAddress(), that.getPhysicalAddress())
            && equal(this.getPostalAddress(), that.getPostalAddress())
            && equal(this.getEmailAddress(), that.getEmailAddress())
            && equal(this.getIdentityNo(), that.getIdentityNo())
            && equal(this.getName(), that.getName())
            && equal(this.getIdentityType(), that.getIdentityType())
            && equal(this.getId(), that.getId())
        ;
    }

    /**
     * This is a convenient helper method which is able to detect whether or not two values are equal. Two values
     * are equal when they are both {@code null}, are arrays of the same length with equal elements or are
     * equal objects (this includes {@link Collection} and {@link java.util.Map} instances).
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
        else // note that the following also covers Collection and java.util.Map
        {
            equal = first.equals(second);
        }

        return equal;
    }

}