package bw.co.knowvera.keycloak;

import org.springframework.web.server.ResponseStatusException;
import jakarta.ws.rs.NotFoundException;
import org.keycloak.representations.idm.ClientRepresentation;
import java.net.URI;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import bw.co.knowvera.individual.IndividualDTO;
import bw.co.knowvera.individual.IndividualService;
import bw.co.knowvera.individual.IndividualServiceException;
import bw.co.knowvera.organisation.OrganisationDTO;
import bw.co.knowvera.organisation.OrganisationService;
import bw.co.knowvera.organisation.branch.BranchDTO;
import bw.co.knowvera.organisation.branch.BranchService;
import bw.co.knowvera.organisation.client.ClientRequestDTO;
import bw.co.knowvera.organisation.client.ClientRequestService;
import bw.co.knowvera.settings.SettingsDTO;
import bw.co.knowvera.settings.SettingsService;
import bw.co.knowvera.user.UserDTO;
import bw.co.knowvera.utils.KycUtils;

@Component
public class KeycloakUserService {

    private static final String[] EXCLUDED_ROLES = { "offline_access", "uma_authorization",
            "default-roles-knowvera" };

    @Value("${app.organisation.manager-role}")
    private String organisationManagerRole;

    /** Realm role given to self-registered users (realm roles are hierarchical, see keycloak/generate_authz.py) */
    @Value("${app.realmUserRole}")
    private String realmUserRole;

    @Value("${app.security.password.min-length}")
    private int minPasswordLength;

    @Value("${app.admin-web}")
    private String adminWebUrl;

    @Value("${app.comm.source-email}")
    private String sourceEmail;

    @Value("${app.novu.queue.newUserQueueExchange}")
    private String newUserQueueExchange;

    @Value("${app.novu.queue.newUserQueueRoutingKey}")
    private String newUserQueueRoutingKey;

    @Value("${app.novu.queue.newOrgUserQueueExchange}")
    private String newOrgUserQueueExchange;

    @Value("${app.novu.queue.newOrgUserQueueRoutingKey}")
    private String newOrgUserQueueRoutingKey;

    private SettingsDTO settings;

    private final KeycloakService keycloakService;
    private final KeycloakOrganisationService keycloakOrganisationService;
    private final BranchService branchService;
    private final IndividualService individualService;
    private final OrganisationService organisationService;
    private final ClientRequestService clientRequestService;
    private final SettingsService settingsService;
    private final RabbitTemplate rabbitTemplate;

    private final KycUtils kycUtils;

    public KeycloakUserService(KeycloakService keycloakService, BranchService branchService,
            IndividualService individualService, OrganisationService organisationService,
            ClientRequestService clientRequestService,
            KeycloakOrganisationService keycloakOrganisationService, SettingsService settingsService,
            RabbitTemplate rabbitTemplate, KycUtils kycUtils) {
        this.keycloakService = keycloakService;
        this.keycloakOrganisationService = keycloakOrganisationService;
        this.branchService = branchService;
        this.individualService = individualService;
        this.organisationService = organisationService;
        this.clientRequestService = clientRequestService;
        this.settingsService = settingsService;
        this.rabbitTemplate = rabbitTemplate;
        this.kycUtils = kycUtils;
    }

    private static final String newOrgUserTemplate = """
            Dear %s,

            Welcome to Knowvera KYC platform. Your organisation, %s, has selected you to manage
            their account. This message alerts you that a user has been created for you on the
            platform. Please find the login details below:

            URL: %s
            Username: %s
            Password: %s

            Regards

            knowvera Team
            """;

    private static final String newUserTemplate = """
            Dear %s,

            Welcome to Knowvera KYC platform. Your new account is ready for use. Please find the login details below:

            URL: %s
            Username: %s
            Password: %s

            Kindly change your password upon first login.

            Regards

            knowvera Team
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

    private Map<String, List<String>> createAttributes(UserDTO user) {

        Map<String, List<String>> attributes = new HashMap<>();

        if (StringUtils.isNotBlank(user.getBranchId())) {
            attributes.put("branchId", Collections.singletonList(user.getBranchId()));
            if (StringUtils.isBlank(user.getOrganisationId())) {
                BranchDTO branch = branchService.findById(user.getBranchId());
                user.setOrganisationId(branch.getOrganisationId());
            }
        }

        if (StringUtils.isNotBlank(user.getBranch())) {
            attributes.put("branch", Collections.singletonList(user.getBranch()));
        }

        if (StringUtils.isNotBlank(user.getOrganisationId())) {
            attributes.put("organisationId", Collections.singletonList(user.getOrganisationId()));
        }

        if (StringUtils.isNotBlank(user.getOrganisation())) {
            attributes.put("organisation", Collections.singletonList(user.getOrganisation()));
        }

        if (StringUtils.isNotBlank(user.getIdentityNo())) {
            attributes.put("identityNo", Collections.singletonList(user.getIdentityNo()));
        }

        if (StringUtils.isNotBlank(user.getOrganisationRegistrationNo())) {
            attributes.put("organisationRegistrationNo",
                    Collections.singletonList(user.getOrganisationRegistrationNo()));
        }

        return attributes;
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

        Map<String, List<String>> attributes = createAttributes(user);

        if (!attributes.isEmpty()) {
            rep.setAttributes(attributes);
        }

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
        dto.setRoles(new LinkedHashSet<>());

        if (rep.getAttributes() != null) {
            Optional.ofNullable(rep.getAttributes().get("branchId"))
                    .ifPresent(ids -> dto.setBranchId(ids.get(0)));
            Optional.ofNullable(rep.getAttributes().get("branch"))
                    .ifPresent(names -> dto.setBranch(names.get(0)));
            Optional.ofNullable(rep.getAttributes().get("identityNo"))
                    .ifPresent(ids -> dto.setIdentityNo(ids.get(0)));

            Optional.ofNullable(rep.getAttributes().get("organisationId"))
                    .ifPresent(ids -> dto.setOrganisationId(ids.get(0)));
            Optional.ofNullable(rep.getAttributes().get("organisation"))
                    .ifPresent(names -> dto.setOrganisation(names.get(0)));
            Optional.ofNullable(rep.getAttributes().get("organisationRegistrationNo"))
                    .ifPresent(ids -> dto.setOrganisationRegistrationNo(ids.get(0)));
        }

        keycloakService.withRegistrationRealm(realm -> {
            // Fetch organizations for the user using realm-aware
            if (StringUtils.isBlank(dto.getOrganisationId())) {
                realm.organizations().members().getOrganizations(dto.getUserId())
                        .stream().findFirst()
                        .ifPresent(org -> {

                            Map<String, List<String>> attributes = org.getAttributes();
                            if (attributes != null && attributes.containsKey("registrationNo")) {
                                OrganisationDTO o = organisationService
                                        .findByRegistrationNo(attributes.get("registrationNo").get(0));
                                if (o != null) {
                                    dto.setOrganisationRegistrationNo(o.getRegistrationNo());
                                    dto.setOrganisationId(o.getId());
                                    dto.setOrganisation(o.getName());
                                }
                            }
                        });
            }

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

    private void newUserMessage(IndividualDTO individual, UserDTO user, OrganisationDTO organisation) {

        Map<String, String> payload = new HashMap<>();

        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(individual.getFirstName()).append(' ');
        if (StringUtils.isNotBlank(individual.getMiddleName())) {
            nameBuilder.append(individual.getMiddleName()).append(' ');
        }

        payload.put("firstName", nameBuilder.toString());
        payload.put("surname", individual.getSurname());
        payload.put("loginUrl", settings.getKycPortalLink());
        payload.put("username", user.getUsername());
        payload.put("password", user.getPassword());
        payload.put("currentYear", "" + LocalDate.now().getYear());

        if (organisation != null && StringUtils.isNotBlank(organisation.getId())) {
            payload.put("organisationName",
                    organisation != null ? organisation.getName() : "");
            rabbitTemplate.convertAndSend(newOrgUserQueueExchange, newOrgUserQueueRoutingKey, payload);
        } else {
            rabbitTemplate.convertAndSend(newUserQueueExchange, newUserQueueRoutingKey, payload);
        }

    }

    // -------------------- CRUD OPERATIONS --------------------

    public UserDTO findByUsername(String username) {
        return keycloakService.withRegistrationRealm(realm -> {
            List<UserRepresentation> users = realm.users().search(username, true);
            return CollectionUtils.isEmpty(users) ? null : toUserDTO(users.get(0));
        });
    }

    public UserDTO findByEmail(String email) {
        return keycloakService.withRegistrationRealm(realm -> {
            List<UserRepresentation> users = realm.users().searchByEmail(email, true);
            return CollectionUtils.isEmpty(users) ? null : toUserDTO(users.get(0));
        });
    }

    public UserDTO getLoggedInUser() {
        return keycloakService.withRegistrationRealm(realm -> {
            String userId = keycloakService.getJwt().getSubject();
            UserRepresentation rep = realm.users().get(userId).toRepresentation();
            return toUserDTO(rep);
        });
    }

    /** The user's realm roles, including those inherited through composite roles. */
    public Set<String> findEffectiveRealmRoles(String userId) {
        return keycloakService.withRegistrationRealm(realm -> realm.users().get(userId).roles().realmLevel()
                .listEffective().stream().map(RoleRepresentation::getName).collect(Collectors.toSet()));
    }

    public UserDTO findUserById(String userId) {
        return keycloakService.withRegistrationRealm(realm -> {
            UserRepresentation rep = realm.users().get(userId).toRepresentation();
            return rep == null ? null : toUserDTO(rep);
        });
    }

    public UserDTO createUser(UserDTO user) {
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

        // Only realm roles are assigned (getRoles); access to the API and portals follows from them
        if (CollectionUtils.isEmpty(user.getRoles())) {

            user.setRoles(Set.of(realmUserRole));
        }

        return keycloakService.withRegistrationRealm(realm -> {
            UserRepresentation rep = toUserRepresentation(user);
            Response response = realm.users().create(rep);
            String userId = getCreatedId(response);
            user.setUserId(userId);

            // Assign organization safely
            if (StringUtils.isNotBlank(user.getOrganisationId())) {

                OrganisationDTO organisation = organisationService.findById(user.getOrganisationId());
                OrganisationDTO keycloakOrganisation = keycloakOrganisationService
                        .findByRegistrationNo(organisation.getRegistrationNo());

                keycloakService.runWithOrganization(keycloakOrganisation.getId(),
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
        keycloakService.withRegistrationRealm(realm -> {
            UserResource userResource = realm.users().get(user.getUserId());
            UserRepresentation rep = userResource.toRepresentation();
            rep.setEmail(user.getEmail());
            rep.setFirstName(user.getFirstName());
            rep.setLastName(user.getLastName());
            rep.setEnabled(user.getEnabled());

            Map<String, List<String>> attributes = createAttributes(user);

            if (!attributes.isEmpty()) {
                rep.setAttributes(attributes);
            }

            // Assign organization safely
            if (StringUtils.isNotBlank(user.getOrganisationId())) {
                OrganisationDTO organisation = organisationService.findById(user.getOrganisationId());
                OrganisationDTO keycloakOrganisation = keycloakOrganisationService
                        .findByRegistrationNo(organisation.getRegistrationNo());

                keycloakService.runWithOrganization(keycloakOrganisation.getId(),
                        org -> org.members().addMember(user.getUserId()));
            }

            userResource.update(rep);

            return user;
        });
    }

    /**
     * Administrative password reset (requires users:manage). The password is temporary: the user must
     * choose a new one at next sign-in. Users change their own password in Keycloak, which
     * re-authenticates them and applies the password policy.
     */
    public boolean resetUserPassword(String userId, String temporaryPassword) {

        if (StringUtils.isBlank(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
        }

        if (StringUtils.isBlank(temporaryPassword) || temporaryPassword.length() < minPasswordLength) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must be at least " + minPasswordLength + " characters");
        }

        keycloakService.withRegistrationRealm(realm -> {
            realm.users().get(userId)
                    .resetPassword(createCredential(CredentialRepresentation.PASSWORD, temporaryPassword, true));
            return Boolean.TRUE;
        });
        return true;
    }

    // -------------------- SEARCH / LIST --------------------

    public List<UserDTO> search(String criteria) {
        return keycloakService.withRegistrationRealm(realm -> {
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
        return keycloakService.withRegistrationRealm(realm -> toUserDTOs(realm.users().searchByAttributes(criteria)));
    }

    public Collection<UserDTO> getUsersByRealmRoles(Set<String> roles) {
        return keycloakService.withRegistrationRealm(realm -> collectUsersByRoles(realm.roles(), roles, realm));
    }

    public Collection<UserDTO> getUsersByClientRoles(String clientId, Set<String> roles) {
        return keycloakService
                .withRegistrationRealm(
                        realm -> collectUsersByRoles(realm.clients().get(clientId).roles(), roles, realm));
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

        return keycloakService.withRegistrationRealm(realm -> {
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

        return keycloakService.withRegistrationRealm(realm -> {
            // Search users by email
            List<UserRepresentation> users = realm.users().searchByEmail(email, true);

            UserRepresentation userRep = CollectionUtils.isEmpty(users) ? null : users.get(0);

            // If user found, fetch full UserDTO
            return (userRep != null) ? this.findUserById(userRep.getId()) : null;
        });
    }

    /**
     * Grants roles of the given client (clientId as configured in Keycloak, e.g. "admin-portal").
     * Only that client's own roles are looked up, never realm roles.
     */
    public UserDTO addClientRoles(String clientId, Set<String> roles, String userId) {

        if (StringUtils.isAnyBlank(clientId, userId) || CollectionUtils.isEmpty(roles)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "clientId, userId and roles are required");
        }

        return keycloakService.withRegistrationRealm(realm -> {
            List<ClientRepresentation> clients = realm.clients().findByClientId(clientId);
            if (clients.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown client " + clientId);
            }
            String clientUuid = clients.get(0).getId();
            RolesResource clientRoles = realm.clients().get(clientUuid).roles();

            List<RoleRepresentation> roleReps = roles.stream()
                    .map(roleName -> {
                        try {
                            return clientRoles.get(roleName).toRepresentation();
                        } catch (NotFoundException e) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client " + clientId + " has no role " + roleName);
                        }
                    })
                    .toList();

            UserResource userResource = realm.users().get(userId);
            UserRepresentation userRep = userResource.toRepresentation();
            // clientLevel() takes the client's internal id, not its clientId
            userResource.roles().clientLevel(clientUuid).add(roleReps);

            return toUserDTO(userRep);
        });
    }

    public boolean updateUserRoles(String userId, String roleName, int action) {
        return keycloakService.withRegistrationRealm(realm -> {
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
        return keycloakService.withRegistrationRealm(realm -> toUserDTOs(realm.users().list()));
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

        return keycloakService.withRegistrationRealm(realm -> {
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

        return keycloakService.withRegistrationRealm(realm -> {
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
        user.setRoles(Set.of(realmUserRole));

        if (individual.getBranch() != null && !StringUtils.isBlank(individual.getBranch().getId())) {

            user.setBranchId(individual.getBranch().getId());
            user.setBranch(individual.getBranch().getName());
        }

        if (individual.getOrganisation() != null
                && StringUtils.isNotBlank(individual.getOrganisation().id())) {
            user.setOrganisation(individual.getOrganisation().name());
            user.setOrganisationId(individual.getOrganisation().id());
            user.setOrganisationRegistrationNo(organisation.getRegistrationNo());
            user.setRoles(Set.of(organisationManagerRole));
        }

        user = createRegistrationUser(user);

        if (user == null || StringUtils.isBlank(user.getUserId())) {
            throw new RuntimeException("Failed to create user for individual: " + individual.getId());
        }

        newUserMessage(individual, user, organisation);

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
