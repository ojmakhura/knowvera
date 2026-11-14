package bw.co.knowvera.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for UserDTO
 */
public class UserDTO
    implements Serializable, Comparable<UserDTO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 8742130474828315673L;

    // Class attributes
    /**
     * TODO: Model Documentation for UserDTO.userId
     */
    @Nullable
    protected String userId;
    /**
     * TODO: Model Documentation for UserDTO.username
     */
    protected String username;
    /**
     * TODO: Model Documentation for UserDTO.email
     */
    protected String email;
    /**
     * TODO: Model Documentation for UserDTO.password
     */
    @Nullable
    protected String password;
    /**
     * TODO: Model Documentation for UserDTO.firstName
     */
    protected String firstName;
    /**
     * TODO: Model Documentation for UserDTO.lastName
     */
    protected String lastName;
    /**
     * TODO: Model Documentation for UserDTO.enabled
     */
    @Nullable
    protected Boolean enabled;
    /**
     * TODO: Model Documentation for UserDTO.roles
     */
    @Nullable
    protected Set<String> roles;
    /**
     * TODO: Model Documentation for UserDTO.organisationId
     */
    @Nullable
    protected String organisationId;
    /**
     * TODO: Model Documentation for UserDTO.organisation
     */
    @Nullable
    protected String organisation;
    /**
     * TODO: Model Documentation for UserDTO.branchId
     */
    @Nullable
    protected String branchId;
    /**
     * TODO: Model Documentation for UserDTO.branch
     */
    @Nullable
    protected String branch;
    /**
     * TODO: Model Documentation for UserDTO.identityNo
     */
    @Nullable
    protected String identityNo;
    /**
     * TODO: Model Documentation for UserDTO.realmRoles
     */
    @Nullable
    protected Collection<String> realmRoles;
    /**
     * TODO: Model Documentation for UserDTO.apiRoles
     */
    @Nullable
    protected Collection<String> apiRoles;
    /**
     * TODO: Model Documentation for UserDTO.userPortalRoles
     */
    @Nullable
    protected Collection<String> userPortalRoles;
    /**
     * TODO: Model Documentation for UserDTO.adminPortalRoles
     */
    @Nullable
    protected Collection<String> adminPortalRoles;
    /**
     * TODO: Model Documentation for UserDTO.organisationRegistrationNo
     */
    @Nullable
    protected String organisationRegistrationNo;
    /**
     * TODO: Model Documentation for UserDTO.keycloakOrganisation
     */
    @Nullable
    protected Map keycloakOrganisation;

    /** Default Constructor with no properties */
    public UserDTO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param usernameIn String TODO: Model Documentation for UserDTO.username
     * @param emailIn String TODO: Model Documentation for UserDTO.email
     * @param firstNameIn String TODO: Model Documentation for UserDTO.firstName
     * @param lastNameIn String TODO: Model Documentation for UserDTO.lastName
     */
    public UserDTO(final String usernameIn, final String emailIn, final String firstNameIn, final String lastNameIn)
    {
        this.username = usernameIn;
        this.email = emailIn;
        this.firstName = firstNameIn;
        this.lastName = lastNameIn;
    }

    /**
     * Constructor with all properties
     * @param userIdIn String
     * @param usernameIn String
     * @param emailIn String
     * @param passwordIn String
     * @param firstNameIn String
     * @param lastNameIn String
     * @param enabledIn Boolean
     * @param rolesIn Set<String>
     * @param organisationIdIn String
     * @param organisationIn String
     * @param branchIdIn String
     * @param branchIn String
     * @param identityNoIn String
     * @param realmRolesIn Collection<String>
     * @param apiRolesIn Collection<String>
     * @param userPortalRolesIn Collection<String>
     * @param adminPortalRolesIn Collection<String>
     * @param organisationRegistrationNoIn String
     * @param keycloakOrganisationIn Map
     */
    public UserDTO(final String userIdIn, final String usernameIn, final String emailIn, final String passwordIn, final String firstNameIn, final String lastNameIn, final Boolean enabledIn, final Set<String> rolesIn, final String organisationIdIn, final String organisationIn, final String branchIdIn, final String branchIn, final String identityNoIn, final Collection<String> realmRolesIn, final Collection<String> apiRolesIn, final Collection<String> userPortalRolesIn, final Collection<String> adminPortalRolesIn, final String organisationRegistrationNoIn, final Map keycloakOrganisationIn)
    {
        this.userId = userIdIn;
        this.username = usernameIn;
        this.email = emailIn;
        this.password = passwordIn;
        this.firstName = firstNameIn;
        this.lastName = lastNameIn;
        this.enabled = enabledIn;
        this.roles = rolesIn;
        this.organisationId = organisationIdIn;
        this.organisation = organisationIn;
        this.branchId = branchIdIn;
        this.branch = branchIn;
        this.identityNo = identityNoIn;
        this.realmRoles = realmRolesIn;
        this.apiRoles = apiRolesIn;
        this.userPortalRoles = userPortalRolesIn;
        this.adminPortalRoles = adminPortalRolesIn;
        this.organisationRegistrationNo = organisationRegistrationNoIn;
        this.keycloakOrganisation = keycloakOrganisationIn;
    }

    /**
     * Copies constructor from other UserDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public UserDTO(final UserDTO otherBean)
    {
        this.userId = otherBean.getUserId();
        this.username = otherBean.getUsername();
        this.email = otherBean.getEmail();
        this.password = otherBean.getPassword();
        this.firstName = otherBean.getFirstName();
        this.lastName = otherBean.getLastName();
        this.enabled = otherBean.getEnabled();
        this.roles = otherBean.getRoles();
        this.organisationId = otherBean.getOrganisationId();
        this.organisation = otherBean.getOrganisation();
        this.branchId = otherBean.getBranchId();
        this.branch = otherBean.getBranch();
        this.identityNo = otherBean.getIdentityNo();
        this.realmRoles = otherBean.getRealmRoles();
        this.apiRoles = otherBean.getApiRoles();
        this.userPortalRoles = otherBean.getUserPortalRoles();
        this.adminPortalRoles = otherBean.getAdminPortalRoles();
        this.organisationRegistrationNo = otherBean.getOrganisationRegistrationNo();
        this.keycloakOrganisation = otherBean.getKeycloakOrganisation();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final UserDTO otherBean)
    {
        if (null != otherBean)
        {
            this.setUserId(otherBean.getUserId());
            this.setUsername(otherBean.getUsername());
            this.setEmail(otherBean.getEmail());
            this.setPassword(otherBean.getPassword());
            this.setFirstName(otherBean.getFirstName());
            this.setLastName(otherBean.getLastName());
            this.setEnabled(otherBean.getEnabled());
            this.setRoles(otherBean.getRoles());
            this.setOrganisationId(otherBean.getOrganisationId());
            this.setOrganisation(otherBean.getOrganisation());
            this.setBranchId(otherBean.getBranchId());
            this.setBranch(otherBean.getBranch());
            this.setIdentityNo(otherBean.getIdentityNo());
            this.setRealmRoles(otherBean.getRealmRoles());
            this.setApiRoles(otherBean.getApiRoles());
            this.setUserPortalRoles(otherBean.getUserPortalRoles());
            this.setAdminPortalRoles(otherBean.getAdminPortalRoles());
            this.setOrganisationRegistrationNo(otherBean.getOrganisationRegistrationNo());
            this.setKeycloakOrganisation(otherBean.getKeycloakOrganisation());
        }
    }

    /**
     * TODO: Model Documentation for UserDTO.userId
     * Get the userId Attribute
     * @return userId String
     */
    public String getUserId()
    {
        return this.userId;
    }

    /**
     * TODO: Model Documentation for UserDTO.userId
     * @param value String
     */
    public void setUserId(final String value)
    {
        this.userId = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.username
     * Get the username Attribute
     * @return username String
     */
    public String getUsername()
    {
        return this.username;
    }

    /**
     * TODO: Model Documentation for UserDTO.username
     * @param value String
     */
    public void setUsername(final String value)
    {
        this.username = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.email
     * Get the email Attribute
     * @return email String
     */
    public String getEmail()
    {
        return this.email;
    }

    /**
     * TODO: Model Documentation for UserDTO.email
     * @param value String
     */
    public void setEmail(final String value)
    {
        this.email = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.password
     * Get the password Attribute
     * @return password String
     */
    public String getPassword()
    {
        return this.password;
    }

    /**
     * TODO: Model Documentation for UserDTO.password
     * @param value String
     */
    public void setPassword(final String value)
    {
        this.password = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.firstName
     * Get the firstName Attribute
     * @return firstName String
     */
    public String getFirstName()
    {
        return this.firstName;
    }

    /**
     * TODO: Model Documentation for UserDTO.firstName
     * @param value String
     */
    public void setFirstName(final String value)
    {
        this.firstName = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.lastName
     * Get the lastName Attribute
     * @return lastName String
     */
    public String getLastName()
    {
        return this.lastName;
    }

    /**
     * TODO: Model Documentation for UserDTO.lastName
     * @param value String
     */
    public void setLastName(final String value)
    {
        this.lastName = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.enabled
     * Get the enabled Attribute
     * @return enabled Boolean
     */
    public Boolean getEnabled()
    {
        return this.enabled;
    }

    /**
     * TODO: Model Documentation for UserDTO.enabled
     * @param value Boolean
     */
    public void setEnabled(final Boolean value)
    {
        this.enabled = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.roles
     * Get the roles Attribute
     * @return roles Set<String>
     */
    public Set<String> getRoles()
    {
        if (this.roles == null)
        {
            this.roles = new HashSet<String>();
        }
        return this.roles;
    }

    /**
     * TODO: Model Documentation for UserDTO.roles
     * @param value Set<String>
     */
    public void setRoles(final Set<String> value)
    {
        this.roles = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.organisationId
     * Get the organisationId Attribute
     * @return organisationId String
     */
    public String getOrganisationId()
    {
        return this.organisationId;
    }

    /**
     * TODO: Model Documentation for UserDTO.organisationId
     * @param value String
     */
    public void setOrganisationId(final String value)
    {
        this.organisationId = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.organisation
     * Get the organisation Attribute
     * @return organisation String
     */
    public String getOrganisation()
    {
        return this.organisation;
    }

    /**
     * TODO: Model Documentation for UserDTO.organisation
     * @param value String
     */
    public void setOrganisation(final String value)
    {
        this.organisation = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.branchId
     * Get the branchId Attribute
     * @return branchId String
     */
    public String getBranchId()
    {
        return this.branchId;
    }

    /**
     * TODO: Model Documentation for UserDTO.branchId
     * @param value String
     */
    public void setBranchId(final String value)
    {
        this.branchId = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.branch
     * Get the branch Attribute
     * @return branch String
     */
    public String getBranch()
    {
        return this.branch;
    }

    /**
     * TODO: Model Documentation for UserDTO.branch
     * @param value String
     */
    public void setBranch(final String value)
    {
        this.branch = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.identityNo
     * Get the identityNo Attribute
     * @return identityNo String
     */
    public String getIdentityNo()
    {
        return this.identityNo;
    }

    /**
     * TODO: Model Documentation for UserDTO.identityNo
     * @param value String
     */
    public void setIdentityNo(final String value)
    {
        this.identityNo = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.realmRoles
     * Get the realmRoles Attribute
     * @return realmRoles Collection<String>
     */
    public Collection<String> getRealmRoles()
    {
        if (this.realmRoles == null)
        {
            this.realmRoles = new ArrayList<String>();
        }
        return this.realmRoles;
    }

    /**
     * TODO: Model Documentation for UserDTO.realmRoles
     * @param value Collection<String>
     */
    public void setRealmRoles(final Collection<String> value)
    {
        this.realmRoles = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.apiRoles
     * Get the apiRoles Attribute
     * @return apiRoles Collection<String>
     */
    public Collection<String> getApiRoles()
    {
        if (this.apiRoles == null)
        {
            this.apiRoles = new ArrayList<String>();
        }
        return this.apiRoles;
    }

    /**
     * TODO: Model Documentation for UserDTO.apiRoles
     * @param value Collection<String>
     */
    public void setApiRoles(final Collection<String> value)
    {
        this.apiRoles = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.userPortalRoles
     * Get the userPortalRoles Attribute
     * @return userPortalRoles Collection<String>
     */
    public Collection<String> getUserPortalRoles()
    {
        if (this.userPortalRoles == null)
        {
            this.userPortalRoles = new ArrayList<String>();
        }
        return this.userPortalRoles;
    }

    /**
     * TODO: Model Documentation for UserDTO.userPortalRoles
     * @param value Collection<String>
     */
    public void setUserPortalRoles(final Collection<String> value)
    {
        this.userPortalRoles = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.adminPortalRoles
     * Get the adminPortalRoles Attribute
     * @return adminPortalRoles Collection<String>
     */
    public Collection<String> getAdminPortalRoles()
    {
        if (this.adminPortalRoles == null)
        {
            this.adminPortalRoles = new ArrayList<String>();
        }
        return this.adminPortalRoles;
    }

    /**
     * TODO: Model Documentation for UserDTO.adminPortalRoles
     * @param value Collection<String>
     */
    public void setAdminPortalRoles(final Collection<String> value)
    {
        this.adminPortalRoles = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.organisationRegistrationNo
     * Get the organisationRegistrationNo Attribute
     * @return organisationRegistrationNo String
     */
    public String getOrganisationRegistrationNo()
    {
        return this.organisationRegistrationNo;
    }

    /**
     * TODO: Model Documentation for UserDTO.organisationRegistrationNo
     * @param value String
     */
    public void setOrganisationRegistrationNo(final String value)
    {
        this.organisationRegistrationNo = value;
    }

    /**
     * TODO: Model Documentation for UserDTO.keycloakOrganisation
     * Get the keycloakOrganisation Attribute
     * @return keycloakOrganisation Map
     */
    public Map getKeycloakOrganisation()
    {
        return this.keycloakOrganisation;
    }

    /**
     * TODO: Model Documentation for UserDTO.keycloakOrganisation
     * @param value Map
     */
    public void setKeycloakOrganisation(final Map value)
    {
        this.keycloakOrganisation = value;
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
        UserDTO rhs = (UserDTO) object;
        return new EqualsBuilder()
            .append(this.getUserId(), rhs.getUserId())
            .append(this.getUsername(), rhs.getUsername())
            .append(this.getEmail(), rhs.getEmail())
            .append(this.getPassword(), rhs.getPassword())
            .append(this.getFirstName(), rhs.getFirstName())
            .append(this.getLastName(), rhs.getLastName())
            .append(this.getEnabled(), rhs.getEnabled())
            .append(this.getRoles(), rhs.getRoles())
            .append(this.getOrganisationId(), rhs.getOrganisationId())
            .append(this.getOrganisation(), rhs.getOrganisation())
            .append(this.getBranchId(), rhs.getBranchId())
            .append(this.getBranch(), rhs.getBranch())
            .append(this.getIdentityNo(), rhs.getIdentityNo())
            .append(this.getRealmRoles(), rhs.getRealmRoles())
            .append(this.getApiRoles(), rhs.getApiRoles())
            .append(this.getUserPortalRoles(), rhs.getUserPortalRoles())
            .append(this.getAdminPortalRoles(), rhs.getAdminPortalRoles())
            .append(this.getOrganisationRegistrationNo(), rhs.getOrganisationRegistrationNo())
            .append(this.getKeycloakOrganisation(), rhs.getKeycloakOrganisation())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final UserDTO object)
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
            .append(this.getUserId(), object.getUserId())
            .append(this.getUsername(), object.getUsername())
            .append(this.getEmail(), object.getEmail())
            .append(this.getPassword(), object.getPassword())
            .append(this.getFirstName(), object.getFirstName())
            .append(this.getLastName(), object.getLastName())
            .append(this.getEnabled(), object.getEnabled())
            .append(this.getRoles(), object.getRoles())
            .append(this.getOrganisationId(), object.getOrganisationId())
            .append(this.getOrganisation(), object.getOrganisation())
            .append(this.getBranchId(), object.getBranchId())
            .append(this.getBranch(), object.getBranch())
            .append(this.getIdentityNo(), object.getIdentityNo())
            .append(this.getRealmRoles(), object.getRealmRoles())
            .append(this.getApiRoles(), object.getApiRoles())
            .append(this.getUserPortalRoles(), object.getUserPortalRoles())
            .append(this.getAdminPortalRoles(), object.getAdminPortalRoles())
            .append(this.getOrganisationRegistrationNo(), object.getOrganisationRegistrationNo())
            .append(this.getKeycloakOrganisation(), object.getKeycloakOrganisation())
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
            .append(this.getUserId())
            .append(this.getUsername())
            .append(this.getEmail())
            .append(this.getPassword())
            .append(this.getFirstName())
            .append(this.getLastName())
            .append(this.getEnabled())
            .append(this.getRoles())
            .append(this.getOrganisationId())
            .append(this.getOrganisation())
            .append(this.getBranchId())
            .append(this.getBranch())
            .append(this.getIdentityNo())
            .append(this.getRealmRoles())
            .append(this.getApiRoles())
            .append(this.getUserPortalRoles())
            .append(this.getAdminPortalRoles())
            .append(this.getOrganisationRegistrationNo())
            .append(this.getKeycloakOrganisation())
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
        builder.append("\t\"userId\" : \"" + this.getUserId() + "\"");
        builder.append("\t,\"username\" : \"" + this.getUsername() + "\"");
        builder.append("\t,\"email\" : \"" + this.getEmail() + "\"");
        builder.append("\t,\"password\" : \"" + this.getPassword() + "\"");
        builder.append("\t,\"firstName\" : \"" + this.getFirstName() + "\"");
        builder.append("\t,\"lastName\" : \"" + this.getLastName() + "\"");
        builder.append("\t,\"enabled\" : \"" + this.getEnabled() + "\"");
        builder.append("\t,\"roles\" : \"" + this.getRoles() + "\"");
        builder.append("\t,\"organisationId\" : \"" + this.getOrganisationId() + "\"");
        builder.append("\t,\"organisation\" : \"" + this.getOrganisation() + "\"");
        builder.append("\t,\"branchId\" : \"" + this.getBranchId() + "\"");
        builder.append("\t,\"branch\" : \"" + this.getBranch() + "\"");
        builder.append("\t,\"identityNo\" : \"" + this.getIdentityNo() + "\"");
        builder.append("\t,\"realmRoles\" : \"" + this.getRealmRoles() + "\"");
        builder.append("\t,\"apiRoles\" : \"" + this.getApiRoles() + "\"");
        builder.append("\t,\"userPortalRoles\" : \"" + this.getUserPortalRoles() + "\"");
        builder.append("\t,\"adminPortalRoles\" : \"" + this.getAdminPortalRoles() + "\"");
        builder.append("\t,\"organisationRegistrationNo\" : \"" + this.getOrganisationRegistrationNo() + "\"");
        builder.append("\t,\"keycloakOrganisation\" : \"" + this.getKeycloakOrganisation() + "\"");
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

        final UserDTO that = (UserDTO)thatObject;
        return
            equal(this.getUserId(), that.getUserId())
            && equal(this.getUsername(), that.getUsername())
            && equal(this.getEmail(), that.getEmail())
            && equal(this.getPassword(), that.getPassword())
            && equal(this.getFirstName(), that.getFirstName())
            && equal(this.getLastName(), that.getLastName())
            && equal(this.getEnabled(), that.getEnabled())
            && equal(this.getRoles(), that.getRoles())
            && equal(this.getOrganisationId(), that.getOrganisationId())
            && equal(this.getOrganisation(), that.getOrganisation())
            && equal(this.getBranchId(), that.getBranchId())
            && equal(this.getBranch(), that.getBranch())
            && equal(this.getIdentityNo(), that.getIdentityNo())
            && equal(this.getRealmRoles(), that.getRealmRoles())
            && equal(this.getApiRoles(), that.getApiRoles())
            && equal(this.getUserPortalRoles(), that.getUserPortalRoles())
            && equal(this.getAdminPortalRoles(), that.getAdminPortalRoles())
            && equal(this.getOrganisationRegistrationNo(), that.getOrganisationRegistrationNo())
            && equal(this.getKeycloakOrganisation(), that.getKeycloakOrganisation())
        ;
    }

    /**
     * This is a convenient helper method which is able to detect whether or not two values are equal. Two values
     * are equal when they are both {@code null}, are arrays of the same length with equal elements or are
     * equal objects (this includes {@link Collection} and {@link Map} instances).
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
        else // note that the following also covers Collection and Map
        {
            equal = first.equals(second);
        }

        return equal;
    }

}