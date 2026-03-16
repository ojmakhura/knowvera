package bw.co.centralkyc.keycloak;

import java.net.URI;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import bw.co.centralkyc.individual.IndividualDTO;
import bw.co.centralkyc.individual.IndividualService;
import bw.co.centralkyc.individual.IndividualServiceException;
import bw.co.centralkyc.organisation.OrganisationDTO;
import bw.co.centralkyc.organisation.OrganisationService;
import bw.co.centralkyc.organisation.branch.BranchDTO;
import bw.co.centralkyc.organisation.branch.BranchService;
import bw.co.centralkyc.organisation.client.ClientRequestDTO;
import bw.co.centralkyc.organisation.client.ClientRequestService;
import bw.co.centralkyc.settings.SettingsDTO;
import bw.co.centralkyc.settings.SettingsService;
import bw.co.centralkyc.user.UserDTO;
import bw.co.centralkyc.utils.KycUtils;
import bw.co.roguesystems.comm.ContentType;
import bw.co.roguesystems.comm.MessagingPlatform;
import bw.co.roguesystems.comm.message.CommMessageDTO;

@Component
@RequiredArgsConstructor
public class KeycloakUserService {

    private static final String[] EXCLUDED_ROLES = { "offline_access", "uma_authorization",
            "default-roles-bocraportal" };

    @Value("${app.organisation.manager-role}")
    private String organisationManagerRole;

    @Value("${app.security.password.min-length}")
    private int minPasswordLength;

    @Value("${app.admin-web}")
    private String adminWebUrl;

    @Value("${app.comm.source-email}")
    private String sourceEmail;

    private SettingsDTO settings;

    private final KeycloakService keycloakService;
    private final BranchService branchService;
    private final IndividualService individualService;
    private final ClientRequestService clientRequestService;
    private final OrganisationService organisationService;
    private final SettingsService settingsService;

    private final KycUtils kycUtils;

    private static final String newOrgUserTemplate = """
            Dear %s,

            Welcome to Central KYC platform. Your organisation, %s, has selected you to manage
            their account. This message alerts you that a user has been created for you on the
            platform. Please find the login details below:

            URL: %s
            Username: %s
            Password: %s

            Regards

            CentralKYC Team
            """;

    private static final String newUserTemplate = """
            Dear %s,

            Welcome to Central KYC platform. Your new account is ready for use. Please find the login details below:

            URL: %s
            Username: %s
            Password: %s

            Kindly change your password upon first login.

            Regards

            CentralKYC Team
            """;

    // -------------------- UTILS --------------------

    private CredentialRepresentation createCredential(String type, String value, boolean temporary) {
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(type);
        cred.setValue(value);
        cred.setTemporary(temporary);
        return cred;
    }

    private String getCreatedId(Response response) {
        if (response.getStatus() != HttpStatus.CREATED.value()) {
            response.bufferEntity();
            String body = response.readEntity(String.class);
            throw new WebApplicationException(
                    "Create method returned status " + response.getStatusInfo().getReasonPhrase() +
                            " (Code: " + response.getStatusInfo().getStatusCode() + "); Response body: " + body,
                    response);
        }
        URI location = response.getLocation();
        return location == null ? null : location.getPath().substring(location.getPath().lastIndexOf('/') + 1);
    }

    private UserRepresentation toUserRepresentation(UserDTO user) {
        UserRepresentation rep = new UserRepresentation();
        rep.setUsername(user.getUsername());
        rep.setEmail(user.getEmail());
        rep.setFirstName(user.getFirstName());
        rep.setLastName(user.getLastName());
        rep.setEnabled(user.getEnabled());
        rep.setEmailVerified(false);
        rep.setRequiredActions(Collections.singletonList("VERIFY_EMAIL"));
        rep.setCredentials(Collections
                .singletonList(createCredential(CredentialRepresentation.PASSWORD, user.getPassword(), true)));

        if (StringUtils.isNotBlank(user.getUserId())) {
            rep.setId(user.getUserId());
        }

        Map<String, List<String>> attributes = new HashMap<>();
        if (StringUtils.isNotBlank(user.getBranchId())) {
            attributes.put("branchId", Collections.singletonList(user.getBranchId()));
            if (StringUtils.isBlank(user.getOrganisationId())) {
                BranchDTO branch = branchService.findById(user.getBranchId());
                user.setOrganisationId(branch.getOrganisationId());
            }
        }
        if (StringUtils.isNotBlank(user.getBranch()))
            attributes.put("branch", Collections.singletonList(user.getBranch()));
        if (StringUtils.isNotBlank(user.getIdentityNo()))
            attributes.put("identityNo", Collections.singletonList(user.getIdentityNo()));
        if (!attributes.isEmpty())
            rep.setAttributes(attributes);

        if (CollectionUtils.isNotEmpty(user.getRoles())) {
            rep.setRealmRoles(new ArrayList<>(user.getRoles()));
        }

        return rep;
    }

    private UserDTO toUserDTO(UserRepresentation rep) {
        UserDTO dto = new UserDTO();
        dto.setUserId(rep.getId());
        dto.setUsername(rep.getUsername());
        dto.setEmail(rep.getEmail());
        dto.setFirstName(rep.getFirstName());
        dto.setLastName(rep.getLastName());
        dto.setEnabled(rep.isEnabled());
        dto.setRoles(new ArrayList<>());

        if (rep.getAttributes() != null) {
            Optional.ofNullable(rep.getAttributes().get("branchId"))
                    .ifPresent(ids -> dto.setBranchId(ids.get(0)));
            Optional.ofNullable(rep.getAttributes().get("branch"))
                    .ifPresent(names -> dto.setBranch(names.get(0)));
            Optional.ofNullable(rep.getAttributes().get("identityNo"))
                    .ifPresent(ids -> dto.setIdentityNo(ids.get(0)));
        }

        keycloakService.withRealm(realm -> {
            // Fetch organizations for the user using realm-aware call
            realm.organizations().members().getOrganizations(dto.getUserId())
                    .stream().findFirst()
                    .ifPresent(org -> {
                        dto.setOrganisationId(org.getId());
                        dto.setOrganisation(org.getName());
                    });

            // Roles
            List<RoleRepresentation> roles = realm.users().get(dto.getUserId()).roles().realmLevel().listAll();
            roles.stream()
                    .filter(role -> !ArrayUtils.contains(EXCLUDED_ROLES, role.getName()))
                    .forEach(role -> dto.getRoles().add(role.getName()));

            return null;
        });

        return dto;
    }

    private List<UserDTO> toUserDTOs(Collection<UserRepresentation> reps) {
        return reps.stream().map(this::toUserDTO).collect(Collectors.toList());
    }

    private CommMessageDTO newUserMessage(IndividualDTO individual, UserDTO user, OrganisationDTO organisation) {

        CommMessageDTO message = new CommMessageDTO();

        message.setContentType(ContentType.PLAIN_TEXT);
        message.setDestinations(List.of(individual.getEmailAddress()));
        message.setSource(sourceEmail);

        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(individual.getFirstName()).append(' ');
        if (StringUtils.isNotBlank(individual.getMiddleName())) {

            nameBuilder.append(individual.getMiddleName()).append(' ');
        }

        nameBuilder.append(individual.getSurname());

        message.setPlatform(MessagingPlatform.EMAIL);

        if (organisation != null && !StringUtils.isNotBlank(organisation.getId())) {

            OrganisationDTO org = organisationService.findById(individual.getOrganisation().id());

            if (org != null) {

                if (StringUtils.isNotBlank(org.getContactEmailAddress())) {
                    message.setCcs(List.of(org.getContactEmailAddress()));
                }

                String messageStr = String.format(newOrgUserTemplate, nameBuilder.toString(),
                        individual.getOrganisation(),
                        adminWebUrl, user.getUsername(),
                        user.getPassword());

                message.setText(messageStr);

            } else {
                String messageStr = String.format(newUserTemplate, nameBuilder.toString(), settings.getKycPortalLink(),
                        user.getUsername(),
                        user.getPassword());

                message.setText(messageStr);
            }
        } else {

            String messageStr = String.format(newUserTemplate, nameBuilder.toString(), settings.getKycPortalLink(),
                    user.getUsername(),
                    user.getPassword());

            message.setText(messageStr);
        }

        return message;

    }

    // -------------------- CRUD OPERATIONS --------------------

    public UserDTO findByUsername(String username) {
        return keycloakService.withRealm(realm -> {
            List<UserRepresentation> users = realm.users().search(username, true);
            return CollectionUtils.isEmpty(users) ? null : toUserDTO(users.get(0));
        });
    }

    public UserDTO findByEmail(String email) {
        return keycloakService.withRealm(realm -> {
            List<UserRepresentation> users = realm.users().searchByEmail(email, true);
            return CollectionUtils.isEmpty(users) ? null : toUserDTO(users.get(0));
        });
    }

    public UserDTO getLoggedInUser() {
        return keycloakService.withRealm(realm -> {
            String userId = keycloakService.getJwt().getSubject();
            UserRepresentation rep = realm.users().get(userId).toRepresentation();
            return toUserDTO(rep);
        });
    }

    public UserDTO findUserById(String userId) {
        return keycloakService.withRealm(realm -> {
            UserRepresentation rep = realm.users().get(userId).toRepresentation();
            return rep == null ? null : toUserDTO(rep);
        });
    }

    public UserDTO createUser(UserDTO user) {
        return keycloakService.withRealm(realm -> {
            UserRepresentation rep = toUserRepresentation(user);
            Response response = realm.users().create(rep);
            String userId = getCreatedId(response);
            user.setUserId(userId);

            // Assign organization safely
            if (StringUtils.isNotBlank(user.getOrganisationId())) {
                keycloakService.runWithOrganization(user.getOrganisationId(),
                        org -> org.members().addMember(userId));
            }

            // Assign roles
            if (CollectionUtils.isNotEmpty(user.getRoles())) {
                List<RoleRepresentation> roleReps = user.getRoles().stream()
                        .map(roleName -> realm.roles().get(roleName).toRepresentation())
                        .filter(r -> StringUtils.isNotBlank(r.getId()))
                        .collect(Collectors.toList());
                if (!roleReps.isEmpty())
                    realm.users().get(userId).roles().realmLevel().add(roleReps);
            }

            return user;
        });
    }

    public UserDTO createRegistrationUser(UserDTO user) {

        if (StringUtils.isNotBlank(user.getUserId())) {

            throw new RuntimeException("User ID must be blank when creating a new registration user.");
        }

        return keycloakService.withRegistrationRealm(realm -> {
            UserRepresentation rep = toUserRepresentation(user);
            Response response = realm.users().create(rep);
            String userId = getCreatedId(response);
            user.setUserId(userId);

            // Assign organization safely
            if (StringUtils.isNotBlank(user.getOrganisationId())) {
                keycloakService.runWithOrganization(user.getOrganisationId(),
                        org -> org.members().addMember(userId));
            }

            // Assign roles
            if (CollectionUtils.isNotEmpty(user.getRoles())) {
                List<RoleRepresentation> roleReps = user.getRoles().stream()
                        .map(roleName -> {
                            RolesResource rs = realm.roles();

                            RoleRepresentation r = rs.get(roleName).toRepresentation();

                            return r;
                        })
                        .filter(r -> {

                            return StringUtils.isNotBlank(r.getId());
                        })
                        .collect(Collectors.toList());
                if (!roleReps.isEmpty())
                    realm.users().get(userId).roles().realmLevel().add(roleReps);
            }

            return user;
        });
    }

    public void updateUser(UserDTO user) {
        keycloakService.runWithRealm(realm -> {
            UserResource userResource = realm.users().get(user.getUserId());
            UserRepresentation rep = userResource.toRepresentation();
            rep.setEmail(user.getEmail());
            rep.setFirstName(user.getFirstName());
            rep.setLastName(user.getLastName());
            rep.setEnabled(user.getEnabled());
            userResource.update(rep);
        });
    }

    public boolean updateUserPassword(String userId, String newPassword) {
        keycloakService.runWithRealm(realm -> {
            String id = StringUtils.isNotBlank(userId) ? userId : keycloakService.getJwt().getSubject();
            UserResource userResource = realm.users().get(id);
            userResource.resetPassword(createCredential(CredentialRepresentation.PASSWORD, newPassword, false));
        });
        return true;
    }

    // -------------------- SEARCH / LIST --------------------

    public List<UserDTO> search(String criteria) {
        return keycloakService.withRealm(realm -> {
            List<UserRepresentation> reps;
            if (StringUtils.isBlank(criteria)) {
                reps = realm.users().list();
            } else {
                reps = realm.users().search(criteria);
            }
            return toUserDTOs(reps);
        });
    }

    public List<UserDTO> searchByAttributes(String criteria) {
        return keycloakService.withRealm(realm -> toUserDTOs(realm.users().searchByAttributes(criteria)));
    }

    public Collection<UserDTO> getUsersByRealmRoles(Set<String> roles) {
        return keycloakService.withRealm(realm -> collectUsersByRoles(realm.roles(), roles, realm));
    }

    public Collection<UserDTO> getUsersByClientRoles(String clientId, Set<String> roles) {
        return keycloakService
                .withRealm(realm -> collectUsersByRoles(realm.clients().get(clientId).roles(), roles, realm));
    }

    // -------------------- PRIVATE HELPERS --------------------

    private Collection<UserDTO> collectUsersByRoles(RolesResource rolesResource, Set<String> roles,
            RealmResource realm) {
        Map<String, UserDTO> users = new HashMap<>();
        for (String role : roles) {
            Set<UserDTO> uvo = rolesResource.get(role).getRoleUserMembers().stream()
                    .map(this::toUserDTO)
                    .collect(Collectors.toSet());
            uvo.forEach(user -> users.put(user.getUserId(), user));
        }
        return users.values();
    }

    public UserDTO getUserByIdentityNo(String identityNo) {
        if (StringUtils.isBlank(identityNo))
            return null;

        return keycloakService.withRealm(realm -> {
            List<UserRepresentation> users = realm.users()
                    .searchByAttributes("identityNo:" + identityNo);

            UserRepresentation userRep = users.stream()
                    .filter(rep -> {
                        Map<String, List<String>> attrs = rep.getAttributes();
                        return attrs != null
                                && attrs.containsKey("identityNo")
                                && CollectionUtils.isNotEmpty(attrs.get("identityNo"))
                                && identityNo.equalsIgnoreCase(attrs.get("identityNo").get(0));
                    })
                    .findFirst()
                    .orElse(null);

            return (userRep != null) ? this.findUserById(userRep.getId()) : null;
        });
    }

    public UserDTO getUserByEmail(String email) {
        if (StringUtils.isBlank(email))
            return null;

        return keycloakService.withRealm(realm -> {
            // Search users by email
            List<UserRepresentation> users = realm.users().searchByEmail(email, true);

            UserRepresentation userRep = CollectionUtils.isEmpty(users) ? null : users.get(0);

            // If user found, fetch full UserDTO
            return (userRep != null) ? this.findUserById(userRep.getId()) : null;
        });
    }

    public UserDTO addClientRoles(String clientId, Set<String> roles, String userId) {
        return keycloakService.withRealm(realm -> {
            // Get the user
            UserResource userResource = realm.users().get(userId);
            UserRepresentation userRep = userResource.toRepresentation();

            if (StringUtils.isBlank(userRep.getId())) {
                return null;
            }

            List<RoleRepresentation> roleReps = roles.stream()
                    .map(roleName -> {
                        RoleRepresentation roleRep = realm.roles().get(roleName).toRepresentation();
                        return StringUtils.isNotBlank(roleRep.getId()) ? roleRep : null;
                    })
                    .filter(r -> r != null)
                    .toList();

            if (CollectionUtils.isNotEmpty(roleReps)) {
                userResource.roles().clientLevel(clientId).add(roleReps);
            }

            return toUserDTO(userRep);
        });
    }

    public boolean updateUserRoles(String userId, String roleName, int action) {
        return keycloakService.withRealm(realm -> {
            // Get the role
            RoleResource roleResource = realm.roles().get(roleName);
            if (roleResource == null) {
                throw new RuntimeException("Role not found: " + roleName);
            }

            // Get the user
            UserResource userResource = realm.users().get(userId);
            if (userResource == null) {
                throw new RuntimeException("User not found: " + userId);
            }

            // Get realm-level role scope
            RoleScopeResource roleScopeResource = userResource.roles().realmLevel();
            RoleRepresentation roleRep = roleResource.toRepresentation();

            if (action > 0) {
                // Add role
                roleScopeResource.add(List.of(roleRep));
            } else {
                // Remove role
                roleScopeResource.remove(List.of(roleRep));
            }

            return true;
        });
    }

    /**
     * Find all users
     * 
     * @return
     */
    public Collection<UserDTO> findAll() {
        return keycloakService.withRealm(realm -> toUserDTOs(realm.users().list()));
    }

    /**
     * Get users belonging to a branch
     * 
     * @param branchId
     * @return
     */
    public Collection<UserDTO> getBranchUsers(String branchId) {

        if (StringUtils.isBlank(branchId))
            return Collections.emptyList();

        return keycloakService.withRealm(realm -> {
            List<UserRepresentation> users = realm.users()
                    .searchByAttributes("branchId:" + branchId);

            List<UserRepresentation> branchUsers = users.stream()
                    .filter(rep -> {
                        Map<String, List<String>> attrs = rep.getAttributes();
                        return attrs != null
                                && attrs.containsKey("branchId")
                                && CollectionUtils.isNotEmpty(attrs.get("branchId"))
                                && branchId.equalsIgnoreCase(attrs.get("branchId").get(0));
                    })
                    .toList();

            return toUserDTOs(branchUsers);
        });

    }

    /**
     * Get users belonging to an organisation
     * 
     * @param organisationId
     * @return
     */
    public Collection<UserDTO> getOrganisationUsers(String organisationId) {

        if (StringUtils.isBlank(organisationId))
            return Collections.emptyList();

        return keycloakService.withRealm(realm -> {
            List<UserRepresentation> users = realm.users()
                    .searchByAttributes("organisationId:" + organisationId);
            List<UserRepresentation> orgUsers = users.stream()
                    .filter(rep -> {
                        Map<String, List<String>> attrs = rep.getAttributes();
                        return attrs != null
                                && attrs.containsKey("organisationId")
                                && CollectionUtils.isNotEmpty(attrs.get("organisationId"))
                                && organisationId.equalsIgnoreCase(attrs.get("organisationId").get(0));
                    })
                    .toList();

            return toUserDTOs(orgUsers);
        });
    }

    /**
     * 
     * Register user for individual
     * 
     * @param individual
     * @return
     */
    public UserDTO registerUser(IndividualDTO individual, OrganisationDTO organisation) {

        if (StringUtils.isNotBlank(individual.getId())) {
            Collection<ClientRequestDTO> clientRequests = clientRequestService.findByIndividual(individual.getId());

            if (CollectionUtils.isEmpty(clientRequests)) {
                throw new RuntimeException("No client requests found for individual: " + individual.getId());
            }
        }

        settings = settingsService.getAll().stream().findFirst().orElse(null);

        if (individual.getHasUser() == null || !individual.getHasUser()) {

            throw new RuntimeException("Individual is not set to have a user account.");
        }

        Collection<UserDTO> usersByIdentityNo = searchByAttributes("identityNo:" + individual.getIdentityNo());

        if (CollectionUtils.isNotEmpty(usersByIdentityNo)) {

            boolean userExists = usersByIdentityNo.stream()
                    .anyMatch(user -> individual.getEmailAddress().equalsIgnoreCase(user.getEmail()));

            if (userExists) {
                throw new RuntimeException("User with identity number " + individual.getIdentityNo() +
                        " and email " + individual.getEmailAddress() + " already exists. Contact support.");
            }
        }

        UserDTO user = new UserDTO();
        user.setFirstName(individual.getFirstName());
        user.setLastName(individual.getSurname());
        user.setEmail(individual.getEmailAddress());
        user.setUsername(individual.getEmailAddress());
        user.setIdentityNo(individual.getIdentityNo());
        String password = kycUtils.generatePassword();
        user.setPassword(password);
        user.setEnabled(true);
        user.setRoles(List.of("KYC_USER"));

        if (individual.getBranch() != null && !StringUtils.isBlank(individual.getBranch().getId())) {

            user.setBranchId(individual.getBranch().getId());
            user.setBranch(individual.getBranch().getName());
        }

        if (individual.getOrganisation() != null
                && StringUtils.isNotBlank(individual.getOrganisation().id())) {
            user.setOrganisation(individual.getOrganisation().name());
            user.setOrganisationId(individual.getOrganisation().id());
            user.setRoles(Set.of(organisationManagerRole));
        }

        user = createRegistrationUser(user);

        if (user == null || StringUtils.isBlank(user.getUserId())) {
            throw new RuntimeException("Failed to create user for individual: " + individual.getId());
        }

        CommMessageDTO message = newUserMessage(individual, user, organisation);

        // Call messaging service to send the email
        // For now, just print to console (not recommended for production)

        return user;
    }

    /**
     * 
     * Register user for individual by id
     * 
     * @param individualId
     * @return
     */
    public UserDTO registerUser(String individualId) {

        IndividualDTO individual = individualService.findById(individualId);

        OrganisationDTO org = new OrganisationDTO();
        org.setId(individual.getOrganisation().id());
        org.setName(individual.getOrganisation().name());
        org.setCode(individual.getOrganisation().code());
        org.setRegistrationNo(individual.getOrganisation().registrationNo());

        return registerUser(individual, org);
    }
}
