package bw.co.knowvera.kyc;

import bw.co.knowvera.individual.IndividualIdentityType;
import bw.co.knowvera.individual.PepStatus;
import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for RelatedParty
 */
public class RelatedParty
    implements Serializable, Comparable<RelatedParty>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 8206191399910436350L;

    // Class attributes
    /**
     * TODO: Model Documentation for RelatedParty.individualId
     */
    @Nullable
    protected String individualId;
    /**
     * TODO: Model Documentation for RelatedParty.name
     */
    @Nullable
    protected String name;
    /**
     * TODO: Model Documentation for RelatedParty.identityNo
     */
    @Nullable
    protected String identityNo;
    /**
     * TODO: Model Documentation for RelatedParty.identityType
     */
    @Nullable
    protected IndividualIdentityType identityType;
    /**
     * TODO: Model Documentation for RelatedParty.nationality
     */
    @Nullable
    protected String nationality;
    /**
     * TODO: Model Documentation for RelatedParty.role
     */
    @Nullable
    protected RelatedPartyRole role;
    /**
     * TODO: Model Documentation for RelatedParty.ownershipPercentage
     */
    @Nullable
    protected Double ownershipPercentage;
    /**
     * TODO: Model Documentation for RelatedParty.pepStatus
     */
    @Nullable
    protected PepStatus pepStatus;
    /**
     * TODO: Model Documentation for RelatedParty.beneficialOwner
     */
    @Nullable
    protected Boolean beneficialOwner = false;
    /**
     * TODO: Model Documentation for RelatedParty.authorityReference
     */
    @Nullable
    protected String authorityReference;

    /**
     * Constructor taking only required properties
     */
    public RelatedParty()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param individualIdIn String
     * @param nameIn String
     * @param identityNoIn String
     * @param identityTypeIn IndividualIdentityType
     * @param nationalityIn String
     * @param roleIn RelatedPartyRole
     * @param ownershipPercentageIn Double
     * @param pepStatusIn PepStatus
     * @param beneficialOwnerIn Boolean
     * @param authorityReferenceIn String
     */
    public RelatedParty(final String individualIdIn, final String nameIn, final String identityNoIn, final IndividualIdentityType identityTypeIn, final String nationalityIn, final RelatedPartyRole roleIn, final Double ownershipPercentageIn, final PepStatus pepStatusIn, final Boolean beneficialOwnerIn, final String authorityReferenceIn)
    {
        this.individualId = individualIdIn;
        this.name = nameIn;
        this.identityNo = identityNoIn;
        this.identityType = identityTypeIn;
        this.nationality = nationalityIn;
        this.role = roleIn;
        this.ownershipPercentage = ownershipPercentageIn;
        this.pepStatus = pepStatusIn;
        this.beneficialOwner = beneficialOwnerIn;
        this.authorityReference = authorityReferenceIn;
    }

    /**
     * Copies constructor from other RelatedParty
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public RelatedParty(final RelatedParty otherBean)
    {
        this.individualId = otherBean.getIndividualId();
        this.name = otherBean.getName();
        this.identityNo = otherBean.getIdentityNo();
        this.identityType = otherBean.getIdentityType();
        this.nationality = otherBean.getNationality();
        this.role = otherBean.getRole();
        this.ownershipPercentage = otherBean.getOwnershipPercentage();
        this.pepStatus = otherBean.getPepStatus();
        this.beneficialOwner = otherBean.getBeneficialOwner();
        this.authorityReference = otherBean.getAuthorityReference();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final RelatedParty otherBean)
    {
        if (null != otherBean)
        {
            this.setIndividualId(otherBean.getIndividualId());
            this.setName(otherBean.getName());
            this.setIdentityNo(otherBean.getIdentityNo());
            this.setIdentityType(otherBean.getIdentityType());
            this.setNationality(otherBean.getNationality());
            this.setRole(otherBean.getRole());
            this.setOwnershipPercentage(otherBean.getOwnershipPercentage());
            this.setPepStatus(otherBean.getPepStatus());
            this.setBeneficialOwner(otherBean.getBeneficialOwner());
            this.setAuthorityReference(otherBean.getAuthorityReference());
        }
    }

    /**
     * TODO: Model Documentation for RelatedParty.individualId
     * Get the individualId Attribute
     * @return individualId String
     */
    public String getIndividualId()
    {
        return this.individualId;
    }

    /**
     * TODO: Model Documentation for RelatedParty.individualId
     * @param value String
     */
    public void setIndividualId(final String value)
    {
        this.individualId = value;
    }

    /**
     * TODO: Model Documentation for RelatedParty.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for RelatedParty.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for RelatedParty.identityNo
     * Get the identityNo Attribute
     * @return identityNo String
     */
    public String getIdentityNo()
    {
        return this.identityNo;
    }

    /**
     * TODO: Model Documentation for RelatedParty.identityNo
     * @param value String
     */
    public void setIdentityNo(final String value)
    {
        this.identityNo = value;
    }

    /**
     * TODO: Model Documentation for RelatedParty.identityType
     * Get the identityType Attribute
     * @return identityType IndividualIdentityType
     */
    public IndividualIdentityType getIdentityType()
    {
        return this.identityType;
    }

    /**
     * TODO: Model Documentation for RelatedParty.identityType
     * @param value IndividualIdentityType
     */
    public void setIdentityType(final IndividualIdentityType value)
    {
        this.identityType = value;
    }

    /**
     * TODO: Model Documentation for RelatedParty.nationality
     * Get the nationality Attribute
     * @return nationality String
     */
    public String getNationality()
    {
        return this.nationality;
    }

    /**
     * TODO: Model Documentation for RelatedParty.nationality
     * @param value String
     */
    public void setNationality(final String value)
    {
        this.nationality = value;
    }

    /**
     * TODO: Model Documentation for RelatedParty.role
     * Get the role Attribute
     * @return role RelatedPartyRole
     */
    public RelatedPartyRole getRole()
    {
        return this.role;
    }

    /**
     * TODO: Model Documentation for RelatedParty.role
     * @param value RelatedPartyRole
     */
    public void setRole(final RelatedPartyRole value)
    {
        this.role = value;
    }

    /**
     * TODO: Model Documentation for RelatedParty.ownershipPercentage
     * Get the ownershipPercentage Attribute
     * @return ownershipPercentage Double
     */
    public Double getOwnershipPercentage()
    {
        return this.ownershipPercentage;
    }

    /**
     * TODO: Model Documentation for RelatedParty.ownershipPercentage
     * @param value Double
     */
    public void setOwnershipPercentage(final Double value)
    {
        this.ownershipPercentage = value;
    }

    /**
     * TODO: Model Documentation for RelatedParty.pepStatus
     * Get the pepStatus Attribute
     * @return pepStatus PepStatus
     */
    public PepStatus getPepStatus()
    {
        return this.pepStatus;
    }

    /**
     * TODO: Model Documentation for RelatedParty.pepStatus
     * @param value PepStatus
     */
    public void setPepStatus(final PepStatus value)
    {
        this.pepStatus = value;
    }

    /**
     * TODO: Model Documentation for RelatedParty.beneficialOwner
     * Get the beneficialOwner Attribute
     * @return beneficialOwner Boolean
     */
    public Boolean getBeneficialOwner()
    {
        return this.beneficialOwner;
    }

    /**
     * TODO: Model Documentation for RelatedParty.beneficialOwner
     * @param value Boolean
     */
    public void setBeneficialOwner(final Boolean value)
    {
        this.beneficialOwner = value;
    }

    /**
     * TODO: Model Documentation for RelatedParty.authorityReference
     * Get the authorityReference Attribute
     * @return authorityReference String
     */
    public String getAuthorityReference()
    {
        return this.authorityReference;
    }

    /**
     * TODO: Model Documentation for RelatedParty.authorityReference
     * @param value String
     */
    public void setAuthorityReference(final String value)
    {
        this.authorityReference = value;
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
        RelatedParty rhs = (RelatedParty) object;
        return new EqualsBuilder()
            .append(this.getIndividualId(), rhs.getIndividualId())
            .append(this.getName(), rhs.getName())
            .append(this.getIdentityNo(), rhs.getIdentityNo())
            .append(this.getIdentityType(), rhs.getIdentityType())
            .append(this.getNationality(), rhs.getNationality())
            .append(this.getRole(), rhs.getRole())
            .append(this.getOwnershipPercentage(), rhs.getOwnershipPercentage())
            .append(this.getPepStatus(), rhs.getPepStatus())
            .append(this.getBeneficialOwner(), rhs.getBeneficialOwner())
            .append(this.getAuthorityReference(), rhs.getAuthorityReference())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final RelatedParty object)
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
            .append(this.getIndividualId(), object.getIndividualId())
            .append(this.getName(), object.getName())
            .append(this.getIdentityNo(), object.getIdentityNo())
            .append(this.getIdentityType(), object.getIdentityType())
            .append(this.getNationality(), object.getNationality())
            .append(this.getRole(), object.getRole())
            .append(this.getOwnershipPercentage(), object.getOwnershipPercentage())
            .append(this.getPepStatus(), object.getPepStatus())
            .append(this.getBeneficialOwner(), object.getBeneficialOwner())
            .append(this.getAuthorityReference(), object.getAuthorityReference())
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
            .append(this.getIndividualId())
            .append(this.getName())
            .append(this.getIdentityNo())
            .append(this.getIdentityType())
            .append(this.getNationality())
            .append(this.getRole())
            .append(this.getOwnershipPercentage())
            .append(this.getPepStatus())
            .append(this.getBeneficialOwner())
            .append(this.getAuthorityReference())
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
        builder.append("\t\"individualId\" : \"" + this.getIndividualId() + "\"");
        builder.append("\t,\"name\" : \"" + this.getName() + "\"");
        builder.append("\t,\"identityNo\" : \"" + this.getIdentityNo() + "\"");
        builder.append("\t,\"identityType\" : \"" + this.getIdentityType() + "\"");
        builder.append("\t,\"nationality\" : \"" + this.getNationality() + "\"");
        builder.append("\t,\"role\" : \"" + this.getRole() + "\"");
        builder.append("\t,\"ownershipPercentage\" : \"" + this.getOwnershipPercentage() + "\"");
        builder.append("\t,\"pepStatus\" : \"" + this.getPepStatus() + "\"");
        builder.append("\t,\"beneficialOwner\" : \"" + this.getBeneficialOwner() + "\"");
        builder.append("\t,\"authorityReference\" : \"" + this.getAuthorityReference() + "\"");
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

        final RelatedParty that = (RelatedParty)thatObject;
        return
            equal(this.getIndividualId(), that.getIndividualId())
            && equal(this.getName(), that.getName())
            && equal(this.getIdentityNo(), that.getIdentityNo())
            && equal(this.getIdentityType(), that.getIdentityType())
            && equal(this.getNationality(), that.getNationality())
            && equal(this.getRole(), that.getRole())
            && equal(this.getOwnershipPercentage(), that.getOwnershipPercentage())
            && equal(this.getPepStatus(), that.getPepStatus())
            && equal(this.getBeneficialOwner(), that.getBeneficialOwner())
            && equal(this.getAuthorityReference(), that.getAuthorityReference())
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