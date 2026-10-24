package bw.co.knowvera.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.OrganizationResource;
import org.keycloak.admin.client.resource.OrganizationsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.PreDestroy;
import jakarta.ws.rs.ForbiddenException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * Access to the Keycloak admin API as the API client's service account. One admin
 * client is shared by all requests: it is thread-safe and refreshes its own token, so calls do
 * not each pay for a new HTTP client and a token request.
 */
@Component
public class KeycloakService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakService.class);

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${app.authz.client}")
    private String apiClient;

    @Value("${app.authz.client-secret}")
    private String apiClientSecret;

    private volatile Keycloak keycloak;

    /**
     * The JWT of the signed-in caller, or null when the request is not authenticated with a token.
     */
    public Jwt getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getPrincipal() instanceof Jwt jwt ? jwt : null;
    }

    private Keycloak keycloak() {
        Keycloak kc = keycloak;
        if (kc == null) {
            synchronized (this) {
                if (keycloak == null) {
                    keycloak = KeycloakBuilder.builder()
                            .serverUrl(issuerUri.substring(0, issuerUri.indexOf("/realms")))
                            .realm(realmName())
                            .clientId(apiClient)
                            .clientSecret(apiClientSecret)
                            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                            .build();
                }
                kc = keycloak;
            }
        }
        return kc;
    }

    private String realmName() {
        return issuerUri.substring(issuerUri.lastIndexOf('/') + 1);
    }

    @PreDestroy
    void close() {
        Keycloak kc = keycloak;
        if (kc != null) {
            kc.close();
        }
    }

    /* =====================================================
       ============= EXECUTION HELPERS ====================
       ===================================================== */

    /**
     * Runs {@code fn} against the application realm as the API service account.
     */
    public <T> T withApiRealm(Function<RealmResource, T> fn) {
        try {
            return fn.apply(keycloak().realm(realmName()));
        } catch (ForbiddenException e) {
            LOGGER.error("Keycloak refused {} for client '{}' in realm '{}' (403). The service account's token has the "
                    + "realm-management roles {}; the API needs {}. Roles granted in Keycloak apply once the API "
                    + "gets a new token (restart it, or wait for the token to expire).",
                    callerOperation(), apiClient, realmName(), tokenRealmManagementRoles(), REQUIRED_ROLES);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "The user directory is not available. Please contact the administrator.", e);
        }
    }

    /** realm-management roles the API's service account needs (keycloak/generate_authz.py, API_SERVICE_ACCOUNT_ROLES). */
    static final List<String> REQUIRED_ROLES = List.of("manage-users", "manage-realm", "view-clients");

    /** The application method that made the refused call, e.g. KeycloakUserService.findClientRoleNames. */
    private static String callerOperation() {
        return StackWalker.getInstance().walk(frames -> frames
                .filter(f -> f.getClassName().startsWith("bw.co.knowvera.")
                        && !f.getClassName().startsWith(KeycloakService.class.getName()))
                .map(f -> f.getClassName().substring(f.getClassName().lastIndexOf('.') + 1).replaceAll("\\$.*", "")
                        + "." + f.getMethodName().replaceAll("^lambda\\$|\\$\\d+$", ""))
                .findFirst())
                .orElse("an admin call");
    }

    /** The realm-management roles in the admin client's current access token (names only, for the log). */
    private String tokenRealmManagementRoles() {
        try {
            String token = keycloak().tokenManager().getAccessTokenString();
            return realmManagementRoles(token).toString();
        } catch (RuntimeException e) {
            return "unknown (" + e.getMessage() + ")";
        }
    }

    static List<String> realmManagementRoles(String accessToken) {
        String[] parts = accessToken.split("\\.");
        JsonNode claims = JSON.readTree(Base64.getUrlDecoder().decode(parts[1]));
        List<String> roles = new ArrayList<>();
        claims.path("resource_access").path("realm-management").path("roles").forEach(r -> roles.add(r.asString()));
        roles.sort(null);
        return roles;
    }

    private static final JsonMapper JSON = JsonMapper.builder().build();

    /* =====================================================
       ============= ORGANIZATION HELPERS =================
       ===================================================== */

    // Single organization by ID (returns a result)
    public <T> T withOrganization(String orgId, Function<OrganizationResource, T> fn) {
        return withApiRealm(realm -> {
            OrganizationResource org = realm.organizations().get(orgId);
            return fn.apply(org);
        });
    }

    // Single organization by ID (void)
    public void runWithOrganization(String orgId, Consumer<OrganizationResource> consumer) {
        withOrganization(orgId, org -> {
            consumer.accept(org);
            return null;
        });
    }

    // All organizations (returns a result)
    public <T> T withOrganizations(Function<OrganizationsResource, T> fn) {
        return withApiRealm(realm -> fn.apply(realm.organizations()));
    }

    // All organizations (void)
    public void runWithOrganizations(Consumer<OrganizationsResource> consumer) {
        withOrganizations(orgs -> { consumer.accept(orgs); return null; });
    }
}
