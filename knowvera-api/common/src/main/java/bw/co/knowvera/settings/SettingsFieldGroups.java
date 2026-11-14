package bw.co.knowvera.settings;

import bw.co.knowvera.settings.kyc.KycFieldGroupDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for SettingsFieldGroups
 */
public class SettingsFieldGroups
    implements Serializable, Comparable<SettingsFieldGroups>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -1161933140144871379L;

    // Class attributes
    /**
     * TODO: Model Documentation for
     * SettingsFieldGroups.organisationKycFieldGroups
     */
    @Nullable
    protected List<KycFieldGroupDTO> organisationKycFieldGroups;
    /**
     * TODO: Model Documentation for
     * SettingsFieldGroups.individualKycFieldGroups
     */
    @Nullable
    protected List<KycFieldGroupDTO> individualKycFieldGroups;
    /**
     * TODO: Model Documentation for SettingsFieldGroups.user
     */
    @Nullable
    protected String user;

    /**
     * Constructor taking only required properties
     */
    public SettingsFieldGroups()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param organisationKycFieldGroupsIn List<KycFieldGroupDTO>
     * @param individualKycFieldGroupsIn List<KycFieldGroupDTO>
     * @param userIn String
     */
    public SettingsFieldGroups(final List<KycFieldGroupDTO> organisationKycFieldGroupsIn, final List<KycFieldGroupDTO> individualKycFieldGroupsIn, final String userIn)
    {
        this.organisationKycFieldGroups = organisationKycFieldGroupsIn;
        this.individualKycFieldGroups = individualKycFieldGroupsIn;
        this.user = userIn;
    }

    /**
     * Copies constructor from other SettingsFieldGroups
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public SettingsFieldGroups(final SettingsFieldGroups otherBean)
    {
        this.organisationKycFieldGroups = otherBean.getOrganisationKycFieldGroups();
        this.individualKycFieldGroups = otherBean.getIndividualKycFieldGroups();
        this.user = otherBean.getUser();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final SettingsFieldGroups otherBean)
    {
        if (null != otherBean)
        {
            this.setOrganisationKycFieldGroups(otherBean.getOrganisationKycFieldGroups());
            this.setIndividualKycFieldGroups(otherBean.getIndividualKycFieldGroups());
            this.setUser(otherBean.getUser());
        }
    }

    /**
     * TODO: Model Documentation for
     * SettingsFieldGroups.organisationKycFieldGroups
     * Get the organisationKycFieldGroups Attribute
     * @return organisationKycFieldGroups List<KycFieldGroupDTO>
     */
    public List<KycFieldGroupDTO> getOrganisationKycFieldGroups()
    {
        if (this.organisationKycFieldGroups == null)
        {
            this.organisationKycFieldGroups = new ArrayList<KycFieldGroupDTO>();
        }
        return this.organisationKycFieldGroups;
    }

    /**
     * TODO: Model Documentation for
     * SettingsFieldGroups.organisationKycFieldGroups
     * @param value List<KycFieldGroupDTO>
     */
    public void setOrganisationKycFieldGroups(final List<KycFieldGroupDTO> value)
    {
        this.organisationKycFieldGroups = value;
    }

    /**
     * TODO: Model Documentation for
     * SettingsFieldGroups.individualKycFieldGroups
     * Get the individualKycFieldGroups Attribute
     * @return individualKycFieldGroups List<KycFieldGroupDTO>
     */
    public List<KycFieldGroupDTO> getIndividualKycFieldGroups()
    {
        if (this.individualKycFieldGroups == null)
        {
            this.individualKycFieldGroups = new ArrayList<KycFieldGroupDTO>();
        }
        return this.individualKycFieldGroups;
    }

    /**
     * TODO: Model Documentation for
     * SettingsFieldGroups.individualKycFieldGroups
     * @param value List<KycFieldGroupDTO>
     */
    public void setIndividualKycFieldGroups(final List<KycFieldGroupDTO> value)
    {
        this.individualKycFieldGroups = value;
    }

    /**
     * TODO: Model Documentation for SettingsFieldGroups.user
     * Get the user Attribute
     * @return user String
     */
    public String getUser()
    {
        return this.user;
    }

    /**
     * TODO: Model Documentation for SettingsFieldGroups.user
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
        SettingsFieldGroups rhs = (SettingsFieldGroups) object;
        return new EqualsBuilder()
            .append(this.getOrganisationKycFieldGroups(), rhs.getOrganisationKycFieldGroups())
            .append(this.getIndividualKycFieldGroups(), rhs.getIndividualKycFieldGroups())
            .append(this.getUser(), rhs.getUser())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final SettingsFieldGroups object)
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
            .append(this.getOrganisationKycFieldGroups(), object.getOrganisationKycFieldGroups())
            .append(this.getIndividualKycFieldGroups(), object.getIndividualKycFieldGroups())
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
            .append(this.getOrganisationKycFieldGroups())
            .append(this.getIndividualKycFieldGroups())
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
        builder.append("\t\"organisationKycFieldGroups\" : \"" + this.getOrganisationKycFieldGroups() + "\"");
        builder.append("\t,\"individualKycFieldGroups\" : \"" + this.getIndividualKycFieldGroups() + "\"");
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

        final SettingsFieldGroups that = (SettingsFieldGroups)thatObject;
        return
            equal(this.getOrganisationKycFieldGroups(), that.getOrganisationKycFieldGroups())
            && equal(this.getIndividualKycFieldGroups(), that.getIndividualKycFieldGroups())
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