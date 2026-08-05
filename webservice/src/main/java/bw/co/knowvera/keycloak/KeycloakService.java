package bw.co.knowvera.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.OrganizationResource;
import org.keycloak.admin.client.resource.OrganizationsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class KeycloakService {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${app.registration.client}")
    private String registrationClient;

    @Value("${app.registration.client-secret}")
    private String registrationClientSecret;

    /**
     * Returns the JWT of the currently authenticated user
     */
    public Jwt getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal() instanceof String) {
            return null;
        }

        return (Jwt) authentication.getPrincipal();
    }

    /**
     * Creates a new Keycloak admin client for the current request JWT
     */
    private Keycloak createKeycloak() {
        Jwt jwt = getJwt();

        if(jwt == null) {
            return null;
        }

        String iss = jwt.getClaimAsString("iss");  // e.g., https://auth.example.com/realms/bocraportal
        String realm = iss.substring(iss.lastIndexOf('/') + 1);
        String serverUrl = iss.substring(0, iss.indexOf("/realms"));
        String clientId = jwt.getClaimAsString("azp");

        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .authorization(jwt.getTokenValue())
                .build();
    }

    private Keycloak createRegistrationKeycloak() {
        String realm = issuerUri.substring(issuerUri.lastIndexOf('/') + 1);
        String serverUrl = issuerUri.substring(0, issuerUri.indexOf("/realms"));

        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(registrationClient)
                .clientSecret(registrationClientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    public AccessTokenResponse getAccessToken() {

        try (Keycloak kc = createRegistrationKeycloak()) {

            if(kc == null) {
                return null;
            }

            return kc.tokenManager().getAccessToken();
        }
    }

    /**
     * Returns the Keycloak realm name for the current JWT
     */
    private String getRealm() {
        Jwt jwt = getJwt();

        if(jwt == null) {
            return null;
        }
        
        String iss = jwt.getClaimAsString("iss");
        return iss.substring(iss.lastIndexOf('/') + 1);
    }

    /* =====================================================
       ============= EXECUTION HELPERS ====================
       ===================================================== */

    /**
     * Executes a Keycloak RealmResource operation that returns a value
     */
    public <T> T withRealm(Function<RealmResource, T> fn) {
        try (Keycloak kc = createKeycloak()) {

            if(kc == null) {
                return null;
            }

            RealmResource realm = kc.realm(getRealm());
            return fn.apply(realm);
        }
    }

    public <T> T withRegistrationRealm(Function<RealmResource, T> fn) {
        try (Keycloak kc = createRegistrationKeycloak()) {

            if(kc == null) {
                return null;
            }

            String r = issuerUri.substring(issuerUri.lastIndexOf('/') + 1);
            RealmResource realm = kc.realm(r);
            return fn.apply(realm);
        }
    }

    /**
     * Executes a Keycloak RealmResource operation that returns void
     */
    public void runWithRealm(Consumer<RealmResource> fn) {
        try (Keycloak kc = createKeycloak()) {

            if(kc == null) {
                return;
            }

            RealmResource realm = kc.realm(getRealm());
            fn.accept(realm);
        }
    }

    /* =====================================================
       ============= ORGANIZATION HELPERS =================
       ===================================================== */

    // Single organization by ID (returns a result)
    public <T> T withOrganization(String orgId, Function<OrganizationResource, T> fn) {
        return withRealm(realm -> {
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
        return withRealm(realm -> fn.apply(realm.organizations()));
    }

    // All organizations (void)
    public void runWithOrganizations(Consumer<OrganizationsResource> consumer) {
        withOrganizations(orgs -> { consumer.accept(orgs); return null; });
    }
}
