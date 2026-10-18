package bw.co.knowvera.keycloak;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.server.ResponseStatusException;
import jakarta.ws.rs.NotFoundException;
import org.keycloak.representations.idm.ClientRepresentation;
import java.net.URI;
import java.time.Duration;
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
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
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

import bw.co.knowvera.individual.IndividualDTO;
import bw.co.knowvera.messaging.ConfirmedPublisher;
import bw.co.knowvera.individual.IndividualService;
import bw.co.knowvera.organisation.OrganisationDTO;
import bw.co.knowvera.organisation.OrganisationService;
import bw.co.knowvera.organisation.branch.BranchDTO;
import bw.co.knowvera.organisation.branch.BranchService;
import bw.co.knowvera.organisation.client.ClientRequestDTO;
import bw.co.knowvera.organisation.client.ClientRequestService;
import bw.co.knowvera.settings.SettingsDTO;
import bw.co.knowvera.settings.SettingsService;
import bw.co.knowvera.user.UserDTO;

@Component
public class KeycloakUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakUserService.class);

    private static final String[] EXCLUDED_ROLES = { "offline_access", "uma_authorization" };

    /** Every realm has a default-roles-&lt;realm&gt; composite; it is never shown or managed here. */
    private static final String DEFAULT_ROLES_PREFIX = "default-roles-";

    /** Attributes this service owns on an account; updateUser sets or clears exactly these. */
    private static final List<String> MANAGED_ATTRIBUTES = List.of("branchId", "branch", "organisationId",
            "organisation", "identityNo", "organisationRegistrationNo");

    /** Keycloak's largest page; lists are read page by page so none is cut short. */
    private static final int PAGE_SIZE = 100;

    /** Required actions of an account created without a password: the user sets one from the emailed link. */
    private static final List<String> SETUP_ACTIONS = List.of("UPDATE_PASSWORD", "VERIFY_EMAIL");

    @Value("${app.organisation.manager-role}")
    private String organisationManagerRole;

    /** Realm role given to self-registered users (realm roles are hierarchical, see keycloak/generate_authz.py) */
    @Value("${app.realmUserRole}")
    private String realmUserRole;

    @Value("${app.security.password.min-length}")
    private int minPasswordLength;

    /** How long the account set-up link emailed to new users stays valid. */
    @Value("${app.users.setup-link-lifespan:72h}")
    private Duration setupLinkLifespan;

    @Value("${app.novu.queue.newUserQueueExchange}")
    private String newUserQueueExchange;

    @Value("${app.novu.queue.newUserQueueRoutingKey}")
    private String newUserQueueRoutingKey;

    @Value("${app.novu.queue.newOrgUserQueueExchange}")
    private String newOrgUserQueueExchange;

    @Value("${app.novu.queue.newOrgUserQueueRoutingKey}")
    private String newOrgUserQueueRoutingKey;

    private final KeycloakService keycloakService;
    private final KeycloakOrganisationService keycloakOrganisationService;
    private final BranchService branchService;
    private final IndividualService individualService;
    private final OrganisationService organisationService;
    private final ClientRequestService clientRequestService;
    private final SettingsService settingsService;
    private final ConfirmedPublisher publisher;

    public KeycloakUserService(KeycloakService keycloakService, BranchService branchService,
            IndividualService individualService, OrganisationService organisationService,
            ClientRequestService clientRequestService,
            KeycloakOrganisationService keycloakOrganisationService, SettingsService settingsService,
            ConfirmedPublisher publisher) {
        this.keycloakService = keycloakService;
        this.keycloakOrganisationService = keycloakOrganisationService;
        this.branchService = branchService;
        this.individualService = individualService;
        this.organisationService = organisationService;
        this.clientRequestService = clientRequestService;
        this.settingsService = settingsService;
        this.publisher = publisher;
    }

    // -------------------- UTILS --------------------

    private CredentialRepresentation createCredential(String type, String value, boolean temporary) {
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(type);
        cred.setValue(value);
        cred.setTemporary(temporary);
        return cred;
    }

    private String getCreatedId(Response response) {
        try (response) {
            if (response.getStatus() == HttpStatus.CONFLICT.value()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "A user with this username or email already exists");
            }
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
    }

    private static boolean isManagedRole(String role) {
        return !ArrayUtils.contains(EXCLUDED_ROLES, role) && !role.startsWith(DEFAULT_ROLES_PREFIX);
    }

    /** Reads a Keycloak list page by page (Keycloak returns at most 100 entries when no page is given). */
    static <T> List<T> readAll(BiFunction<Integer, Integer, List<T>> page) {
        List<T> all = new ArrayList<>();
        for (int first = 0;; first += PAGE_SIZE) {
            List<T> batch = page.apply(first, PAGE_SIZE);
            if (batch == null) {
                return all;
            }
            all.addAll(batch);
            if (batch.size() < PAGE_SIZE) {
                return all;
            }
        }
    }

    /** Users whose attributes exactly match {@code query} ("key:value"), all pages. */
    private List<UserRepresentation> searchAttributes(RealmResource realm, String query) {
        return readAll((first, max) -> realm.users().searchByAttributes(first, max, null, true, false, query));
    }

    /**
     * A copy that shares nothing with the original, so cached accounts cannot be changed by callers.
     */
    static UserDTO copyOf(UserDTO user) {
        UserDTO copy = new UserDTO(user);
        copy.setPassword(null);
        copy.setRoles(user.getRoles() == null ? null : new LinkedHashSet<>(user.getRoles()));
        copy.setRealmRoles(user.getRealmRoles() == null ? null : new ArrayList<>(user.getRealmRoles()));
        copy.setApiRoles(user.getApiRoles() == null ? null : new ArrayList<>(user.getApiRoles()));
        copy.setUserPortalRoles(user.getUserPortalRoles() == null ? null : new ArrayList<>(user.getUserPortalRoles()));
        copy.setAdminPortalRoles(user.getAdminPortalRoles() == null ? null : new ArrayList<>(user.getAdminPortalRoles()));
        copy.setKeycloakOrganisation(user.getKeycloakOrganisation() == null ? null : new HashMap<>(user.getKeycloakOrganisation()));
        return copy;
    }

    /**
     * Sets every managed attribute from {@code user}: present values are written, blank ones removed.
     * Attributes this service does not manage are kept.
     */
    private Map<String, List<String>> applyAttributes(Map<String, List<String>> existing, UserDTO user) {

        if (StringUtils.isNotBlank(user.getBranchId()) && StringUtils.isBlank(user.getOrganisationId())) {
            BranchDTO branch = branchService.findById(user.getBranchId());
            user.setOrganisationId(branch.getOrganisationId());
        }

        Map<String, List<String>> attributes = existing == null ? new HashMap<>() : new HashMap<>(existing);
        Map<String, String> values = new HashMap<>();
        values.put("branchId", user.getBranchId());
        values.put("branch", user.getBranch());
        values.put("organisationId", user.getOrganisationId());
        values.put("organisation", user.getOrganisation());
        values.put("identityNo", user.getIdentityNo());
        values.put("organisationRegistrationNo", user.getOrganisationRegistrationNo());

        for (String key : MANAGED_ATTRIBUTES) {
            String value = values.get(key);
            if (StringUtils.isNotBlank(value)) {
                attributes.put(key, Collections.singletonList(value));
            } else {
                attributes.remove(key);
            }
        }
        return attributes;
    }

    /**
     * New account representation. Without a password the account gets the set-up actions and the
     * user chooses their own password from the emailed link; a given password is temporary.
     */
    private UserRepresentation toUserRepresentation(UserDTO user) {
        UserRepresentation rep = new UserRepresentation();
        rep.setUsername(user.getUsername());
        rep.setEmail(user.getEmail());
        rep.setFirstName(user.getFirstName());
        rep.setLastName(user.getLastName());
        rep.setEnabled(user.getEnabled() == null || user.getEnabled());
        rep.setEmailVerified(false);

        if (StringUtils.isBlank(user.getPassword())) {
            rep.setRequiredActions(SETUP_ACTIONS);
        } else {
            rep.setRequiredActions(Collections.singletonList("VERIFY_EMAIL"));
            rep.setCredentials(Collections
                    .singletonList(createCredential(CredentialRepresentation.PASSWORD, user.getPassword(), true)));
        }

        Map<String, List<String>> attributes = applyAttributes(null, user);
        if (!attributes.isEmpty()) {
            rep.setAttributes(attributes);
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

        keycloakService.withApiRealm(realm -> {
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
                    .map(RoleRepresentation::getName)
                    .filter(KeycloakUserService::isManagedRole)
                    .forEach(dto.getRoles()::add);

            return null;
        });

        return dto;
    }

    private List<UserDTO> toUserDTOs(Collection<UserRepresentation> reps) {
        return reps.stream().map(this::toUserDTO).collect(Collectors.toList());
    }

    /**
     * Welcome notification. It carries no password: the user sets theirs from Keycloak's set-up email.
     */
    private void newUserMessage(IndividualDTO individual, UserDTO user, OrganisationDTO organisation,
            SettingsDTO settings) {

        Map<String, String> payload = newUserPayload(individual, user, organisation, settings,
                setupLinkLifespan, LocalDate.now().getYear());

        if (StringUtils.isBlank(payload.get("email"))) {
            LOGGER.warn("User {} has no email address: no welcome notification sent", user.getUserId());
            return;
        }

        if (payload.containsKey("organisationName")) {
            publisher.publish(newOrgUserQueueExchange, newOrgUserQueueRoutingKey, payload);
        } else {
            publisher.publish(newUserQueueExchange, newUserQueueRoutingKey, payload);
        }
    }

    /**
     * The organisation the individual belongs to, or null. {@code requested} may be another
     * organisation (the one that requested the client's KYC); it only fills in a missing name.
     */
    static OrganisationDTO memberOrganisation(IndividualDTO individual, OrganisationDTO requested) {

        if (individual.getOrganisation() == null || StringUtils.isBlank(individual.getOrganisation().id())) {
            return null;
        }
        OrganisationDTO member = new OrganisationDTO();
        member.setId(individual.getOrganisation().id());
        member.setName(individual.getOrganisation().name());
        member.setRegistrationNo(individual.getOrganisation().registrationNo());
        if (StringUtils.isBlank(member.getName()) && requested != null
                && member.getId().equals(requested.getId())) {
            member.setName(requested.getName());
        }
        return member;
    }

    /**
     * Payload of the novu-new-user and novu-new-org-user workflows (docs/novu/workflows). Blank
     * values are left out, except the texts the templates print, which fall back to defaults (the
     * Novu templates use plain variables, without Liquid filters); {@code organisationName} is
     * present only for organisation users.
     */
    static Map<String, String> newUserPayload(IndividualDTO individual, UserDTO user,
            OrganisationDTO organisation, SettingsDTO settings, Duration setupLinkLifespan, int currentYear) {

        Map<String, String> payload = new HashMap<>();

        putIfPresent(payload, "email", user.getEmail());
        putIfPresent(payload, "username", user.getUsername());
        putIfPresent(payload, "firstName", StringUtils.trim(individual.getFirstName()));
        putIfPresent(payload, "middleName", StringUtils.trim(individual.getMiddleName()));
        putIfPresent(payload, "surname", StringUtils.trim(individual.getSurname()));
        if (settings != null) {
            putIfPresent(payload, "loginUrl", settings.getKycPortalLink());
        }
        payload.put("platformName",
                StringUtils.defaultIfBlank(settings != null ? settings.getPlatformName() : null, "Knowvera"));
        payload.put("supportContact",
                StringUtils.defaultIfBlank(settings != null ? settings.getSupportContact() : null, "our support team"));
        payload.put("setupLinkValidFor", StringUtils.defaultIfBlank(readable(setupLinkLifespan), "a limited time"));
        payload.put("currentYear", String.valueOf(currentYear));

        if (organisation != null && StringUtils.isNotBlank(organisation.getId())) {
            payload.put("organisationName", StringUtils.defaultString(organisation.getName()));
        }
        return payload;
    }

    private static void putIfPresent(Map<String, String> payload, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            payload.put(key, value);
        }
    }

    /** "72 hours", "1 hour", "30 minutes"; null for no duration. */
    static String readable(Duration duration) {

        if (duration == null || duration.isZero() || duration.isNegative()) {
            return null;
        }
        long minutes = duration.toMinutes();
        if (minutes % 60 == 0) {
            long hours = minutes / 60;
            return hours + (hours == 1 ? " hour" : " hours");
        }
        return minutes + (minutes == 1 ? " minute" : " minutes");
    }

    /**
     * The Keycloak Organization for an organisation id of this application. Only client
     * organisations exist in Keycloak, so this is often empty; the organisationId attribute links
     * the account either way.
     */
    private Optional<String> keycloakOrganisationId(String organisationId) {

        if (StringUtils.isBlank(organisationId)) {
            return Optional.empty();
        }
        OrganisationDTO organisation = organisationService.findById(organisationId);
        if (organisation == null || StringUtils.isBlank(organisation.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown organisation " + organisationId);
        }
        if (StringUtils.isBlank(organisation.getRegistrationNo())) {
            return Optional.empty();
        }
        OrganisationDTO keycloakOrganisation = keycloakOrganisationService
                .findByRegistrationNo(organisation.getRegistrationNo());
        if (keycloakOrganisation == null) {
            LOGGER.debug("Organisation {} is not a Keycloak Organization; users are linked by attribute only",
                    organisation.getRegistrationNo());
            return Optional.empty();
        }
        return Optional.of(keycloakOrganisation.getId());
    }

    private RoleRepresentation realmRole(RealmResource realm, String roleName) {
        try {
            return realm.roles().get(roleName).toRepresentation();
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown role " + roleName);
        }
    }

    private UserResource existingUser(RealmResource realm, String userId) {
        UserResource user = realm.users().get(userId);
        try {
            user.toRepresentation();
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    /**
     * Creates the account, its organisation membership and roles, and sends the set-up email when
     * no password was given. Roles and organisation are checked first; if a later step fails the
     * account is deleted again, so a failure never leaves a half-made user.
     */
    private UserDTO create(UserDTO user) {

        return keycloakService.withApiRealm(realm -> {

            List<RoleRepresentation> roles = user.getRoles().stream().map(role -> realmRole(realm, role)).toList();
            Optional<String> keycloakOrganisation = keycloakOrganisationId(user.getOrganisationId());
            boolean invite = StringUtils.isBlank(user.getPassword());

            String userId = getCreatedId(realm.users().create(toUserRepresentation(user)));
            try {
                keycloakOrganisation.ifPresent(orgId -> keycloakService.runWithOrganization(orgId,
                        org -> org.members().addMember(userId).close()));

                if (!roles.isEmpty()) {
                    realm.users().get(userId).roles().realmLevel().add(roles);
                }

                if (invite) {
                    try {
                        realm.users().get(userId).executeActionsEmail(SETUP_ACTIONS,
                                (int) setupLinkLifespan.toSeconds());
                    } catch (WebApplicationException e) {
                        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                                "Could not send the account set-up email; check the realm's email settings", e);
                    }
                }
            } catch (RuntimeException e) {
                LOGGER.warn("Creating user {} failed; removing the partly created account", user.getUsername(), e);
                try (Response deleted = realm.users().delete(userId)) {
                    if (deleted.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                        LOGGER.error("Could not remove partly created user {}: {}", userId, deleted.getStatus());
                    }
                }
                throw e;
            }

            user.setUserId(userId);
            user.setPassword(null);
            return user;
        });
    }

    // -------------------- CRUD OPERATIONS --------------------

    public UserDTO findByUsername(String username) {
        return keycloakService.withApiRealm(realm -> {
            List<UserRepresentation> users = realm.users().search(username, true);
            return CollectionUtils.isEmpty(users) ? null : toUserDTO(users.get(0));
        });
    }

    public UserDTO findByEmail(String email) {
        return keycloakService.withApiRealm(realm -> {
            List<UserRepresentation> users = realm.users().searchByEmail(email, true);
            return CollectionUtils.isEmpty(users) ? null : toUserDTO(users.get(0));
        });
    }

    public UserDTO getLoggedInUser() {
        if (keycloakService.getJwt() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not signed in");
        }
        return findUserById(keycloakService.getJwt().getSubject());
    }

    /** The user's realm roles, including those inherited through composite roles. */
    public Set<String> findEffectiveRealmRoles(String userId) {
        return keycloakService.withApiRealm(realm -> realm.users().get(userId).roles().realmLevel()
                .listEffective().stream().map(RoleRepresentation::getName).collect(Collectors.toSet()));
    }

    /** Names of the realm's roles that can be assigned, sorted (built-in roles left out). */
    public SortedSet<String> findRealmRoleNames() {
        return keycloakService.withApiRealm(realm -> realm.roles().list().stream()
                .map(RoleRepresentation::getName).filter(KeycloakUserService::isManagedRole)
                .collect(Collectors.toCollection(TreeSet::new)));
    }

    /** Names of the roles of the client with this clientId (e.g. "admin-portal"), sorted; 400 for an unknown client. */
    public SortedSet<String> findClientRoleNames(String clientId) {
        if (StringUtils.isBlank(clientId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "clientId is required");
        }
        return keycloakService.withApiRealm(realm -> {
            List<ClientRepresentation> clients = realm.clients().findByClientId(clientId);
            if (clients.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown client " + clientId);
            }
            // clients().get() takes the client's internal id, not its clientId
            return realm.clients().get(clients.get(0).getId()).roles().list().stream()
                    .map(RoleRepresentation::getName)
                    .collect(Collectors.toCollection(TreeSet::new));
        });
    }

    /** The realm roles assigned to the user directly (as {@link UserDTO#getRoles()} shows them). */
    public Set<String> findDirectRealmRoles(String userId) {
        return keycloakService.withApiRealm(realm -> existingUser(realm, userId).roles().realmLevel()
                .listAll().stream().map(RoleRepresentation::getName).filter(KeycloakUserService::isManagedRole)
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    /**
     * Accounts by id, kept for app.keycloak.user-cache-ttl: endpoints resolve the caller's account
     * on each call. Changes made through this service evict the user's entry.
     */
    @Value("${app.keycloak.user-cache-ttl:60s}")
    private Duration userCacheTtl;

    private volatile ExpiringCache<String, UserDTO> userCache;

    private ExpiringCache<String, UserDTO> userCache() {
        ExpiringCache<String, UserDTO> cache = userCache;
        if (cache == null) {
            synchronized (this) {
                if (userCache == null) {
                    userCache = new ExpiringCache<>(userCacheTtl == null ? Duration.ZERO : userCacheTtl,
                            KeycloakUserService::copyOf);
                }
                cache = userCache;
            }
        }
        return cache;
    }

    /** The account with this id; 404 when there is none. */
    public UserDTO findUserById(String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
        }
        return userCache().get(userId, id -> keycloakService.withApiRealm(realm -> {
            try {
                return toUserDTO(realm.users().get(id).toRepresentation());
            } catch (NotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        }));
    }

    /**
     * Creates an account. Without a password the user is emailed a link to set their own; a
     * given password is temporary. The returned user never carries the password.
     */
    public UserDTO createUser(UserDTO user) {
        if (StringUtils.isNotBlank(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId must be blank for a new user");
        }
        return create(user);
    }

    public UserDTO createRegistrationUser(UserDTO user) {

        if (StringUtils.isNotBlank(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User ID must be blank when creating a new registration user.");
        }

        // Only realm roles are assigned (getRoles); access to the API and portals follows from them
        if (CollectionUtils.isEmpty(user.getRoles())) {
            user.setRoles(Set.of(realmUserRole));
        }

        return create(user);
    }

    /**
     * Updates the account from {@code user}. The managed attributes (branch, organisation, identity
     * number) are set or, when blank, cleared; organisation membership follows the organisation.
     * Non-empty {@code roles} become the user's direct realm roles; empty roles leave them as
     * they are. The password is never changed here.
     */
    public void updateUser(UserDTO user) {
        userCache().evict(user.getUserId());
        keycloakService.withApiRealm(realm -> {
            UserResource userResource = existingUser(realm, user.getUserId());
            UserRepresentation rep = userResource.toRepresentation();
            String previousOrganisation = rep.getAttributes() == null ? null
                    : Optional.ofNullable(rep.getAttributes().get("organisationId"))
                            .filter(ids -> !ids.isEmpty()).map(ids -> ids.get(0)).orElse(null);

            rep.setEmail(user.getEmail());
            rep.setFirstName(user.getFirstName());
            rep.setLastName(user.getLastName());
            if (user.getEnabled() != null) {
                rep.setEnabled(user.getEnabled());
            }
            rep.setAttributes(applyAttributes(rep.getAttributes(), user));

            List<RoleRepresentation> wanted = user.getRoles().stream().map(role -> realmRole(realm, role)).toList();

            // Membership follows the organisation: leave the old Keycloak Organization, join the new one
            if (!StringUtils.equals(previousOrganisation, user.getOrganisationId())) {
                try {
                    keycloakOrganisationId(previousOrganisation).ifPresent(orgId -> keycloakService
                            .runWithOrganization(orgId, org -> org.members().removeMember(user.getUserId()).close()));
                } catch (ResponseStatusException e) {
                    LOGGER.warn("Previous organisation {} of user {} no longer exists", previousOrganisation,
                            user.getUserId());
                }
            }
            keycloakOrganisationId(user.getOrganisationId()).ifPresent(orgId -> keycloakService
                    .runWithOrganization(orgId, org -> org.members().addMember(user.getUserId()).close()));

            userResource.update(rep);

            if (!wanted.isEmpty()) {
                RoleScopeResource realmRoles = userResource.roles().realmLevel();
                Set<String> wantedNames = wanted.stream().map(RoleRepresentation::getName).collect(Collectors.toSet());
                List<RoleRepresentation> current = realmRoles.listAll().stream()
                        .filter(role -> isManagedRole(role.getName())).toList();
                Set<String> currentNames = current.stream().map(RoleRepresentation::getName).collect(Collectors.toSet());

                List<RoleRepresentation> remove = current.stream().filter(r -> !wantedNames.contains(r.getName())).toList();
                List<RoleRepresentation> add = wanted.stream().filter(r -> !currentNames.contains(r.getName())).toList();
                if (!remove.isEmpty()) {
                    realmRoles.remove(remove);
                }
                if (!add.isEmpty()) {
                    realmRoles.add(add);
                }
            }

            user.setPassword(null);
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

        keycloakService.withApiRealm(realm -> {
            existingUser(realm, userId)
                    .resetPassword(createCredential(CredentialRepresentation.PASSWORD, temporaryPassword, true));
            return Boolean.TRUE;
        });
        return true;
    }

    // -------------------- SEARCH / LIST --------------------

    public List<UserDTO> search(String criteria) {
        return keycloakService.withApiRealm(realm -> {
            List<UserRepresentation> reps;
            if (StringUtils.isBlank(criteria)) {
                reps = readAll((first, max) -> realm.users().list(first, max));
            } else {
                reps = readAll((first, max) -> realm.users().search(criteria, first, max));
            }
            return toUserDTOs(reps);
        });
    }

    public List<UserDTO> searchByAttributes(String criteria) {
        return keycloakService.withApiRealm(realm -> toUserDTOs(searchAttributes(realm, criteria)));
    }

    public Collection<UserDTO> getUsersByRealmRoles(Set<String> roles) {
        return keycloakService.withApiRealm(realm -> collectUsersByRoles(realm.roles(), roles));
    }

    /** Users holding any of the roles of the client with this clientId (e.g. "admin-portal"). */
    public Collection<UserDTO> getUsersByClientRoles(String clientId, Set<String> roles) {
        return keycloakService.withApiRealm(realm -> {
            List<ClientRepresentation> clients = realm.clients().findByClientId(clientId);
            if (clients.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown client " + clientId);
            }
            // clients().get() takes the client's internal id, not its clientId
            return collectUsersByRoles(realm.clients().get(clients.get(0).getId()).roles(), roles);
        });
    }

    // -------------------- PRIVATE HELPERS --------------------

    private Collection<UserDTO> collectUsersByRoles(RolesResource rolesResource, Set<String> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "roles are required");
        }
        Map<String, UserRepresentation> users = new HashMap<>();
        for (String role : roles) {
            RoleResource roleResource = rolesResource.get(role);
            List<UserRepresentation> members;
            try {
                members = readAll(roleResource::getUserMembers);
            } catch (NotFoundException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown role " + role);
            }
            members.forEach(user -> users.putIfAbsent(user.getId(), user));
        }
        return toUserDTOs(users.values());
    }

    public UserDTO getUserByIdentityNo(String identityNo) {
        if (StringUtils.isBlank(identityNo))
            return null;

        return keycloakService.withApiRealm(realm -> {
            List<UserRepresentation> users = searchAttributes(realm, "identityNo:" + identityNo);

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

        return keycloakService.withApiRealm(realm -> {
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

        return keycloakService.withApiRealm(realm -> {
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

            UserResource userResource = existingUser(realm, userId);
            UserRepresentation userRep = userResource.toRepresentation();
            userCache().evict(userId);
            // clientLevel() takes the client's internal id, not its clientId
            userResource.roles().clientLevel(clientUuid).add(roleReps);

            return toUserDTO(userRep);
        });
    }

    /** Adds ({@code action > 0}) or removes a realm role: 400 for an unknown role, 404 for an unknown user. */
    public boolean updateUserRoles(String userId, String roleName, int action) {
        userCache().evict(userId);
        return keycloakService.withApiRealm(realm -> {
            RoleRepresentation roleRep = realmRole(realm, roleName);
            RoleScopeResource roleScopeResource = existingUser(realm, userId).roles().realmLevel();

            if (action > 0) {
                roleScopeResource.add(List.of(roleRep));
            } else {
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
        return keycloakService.withApiRealm(
                realm -> toUserDTOs(readAll((first, max) -> realm.users().list(first, max))));
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

        return keycloakService.withApiRealm(realm -> {
            List<UserRepresentation> users = searchAttributes(realm, "branchId:" + branchId);

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

        return keycloakService.withApiRealm(realm -> {
            List<UserRepresentation> users = searchAttributes(realm, "organisationId:" + organisationId);
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
     * Registers an account for the individual. The user is emailed a link to set their own
     * password (Keycloak's set-up email) and gets a welcome notification without credentials.
     */
    public UserDTO registerUser(IndividualDTO individual, OrganisationDTO organisation) {

        if (StringUtils.isNotBlank(individual.getId())) {
            Collection<ClientRequestDTO> clientRequests = clientRequestService.findByIndividual(individual.getId());

            if (CollectionUtils.isEmpty(clientRequests)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No client requests found for individual: " + individual.getId());
            }
        }

        if (individual.getHasUser() == null || !individual.getHasUser()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Individual is not set to have a user account.");
        }

        if (StringUtils.isBlank(individual.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An email address is required for a user account.");
        }

        Collection<UserDTO> usersByIdentityNo = StringUtils.isBlank(individual.getIdentityNo()) ? List.of()
                : searchByAttributes("identityNo:" + individual.getIdentityNo());

        if (usersByIdentityNo.stream().anyMatch(user -> individual.getEmailAddress().equalsIgnoreCase(user.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with identity number "
                    + individual.getIdentityNo() + " and email " + individual.getEmailAddress()
                    + " already exists. Contact support.");
        }

        if (getUserByEmail(individual.getEmailAddress()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A user with email " + individual.getEmailAddress() + " already exists. Contact support.");
        }

        SettingsDTO settings = settingsService.getAll().stream().findFirst().orElse(null);
        if (settings == null) {
            LOGGER.warn("No application settings: the welcome notification has no login link, platform name or support contact");
        }

        UserDTO user = new UserDTO();
        user.setFirstName(individual.getFirstName());
        user.setLastName(individual.getSurname());
        user.setEmail(individual.getEmailAddress());
        user.setUsername(individual.getEmailAddress());
        user.setIdentityNo(individual.getIdentityNo());
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
            user.setOrganisationRegistrationNo(organisation != null ? organisation.getRegistrationNo()
                    : individual.getOrganisation().registrationNo());
            user.setRoles(Set.of(organisationManagerRole));
        }

        // No password: create() sends Keycloak's set-up email so the user chooses their own
        user = createRegistrationUser(user);

        // The organisation-user welcome only for members of an organisation (as the role above);
        // a client invited by an organisation is an applicant and gets the plain welcome
        newUserMessage(individual, user, memberOrganisation(individual, organisation), settings);

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

        OrganisationDTO org = null;
        if (individual.getOrganisation() != null) {
            org = new OrganisationDTO();
            org.setId(individual.getOrganisation().id());
            org.setName(individual.getOrganisation().name());
            org.setCode(individual.getOrganisation().code());
            org.setRegistrationNo(individual.getOrganisation().registrationNo());
        }

        return registerUser(individual, org);
    }
}
