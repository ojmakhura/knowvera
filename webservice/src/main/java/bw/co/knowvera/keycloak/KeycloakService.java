package bw.co.knowvera.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.OrganizationResource;
import org.keycloak.admin.client.resource.OrganizationsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Access to the Keycloak admin API as the registration client's service account. One admin
 * client is shared by all requests: it is thread-safe and refreshes its own token, so calls do
 * not each pay for a new HTTP client and a token request.
 */
@Component
public class KeycloakService {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${app.registration.client}")
    private String registrationClient;

    @Value("${app.registration.client-secret}")
    private String registrationClientSecret;

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
                            .clientId(registrationClient)
                            .clientSecret(registrationClientSecret)
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
     * Runs {@code fn} against the application realm as the registration service account.
     */
    public <T> T withRegistrationRealm(Function<RealmResource, T> fn) {
        return fn.apply(keycloak().realm(realmName()));
    }

    /* =====================================================
       ============= ORGANIZATION HELPERS =================
       ===================================================== */

    // Single organization by ID (returns a result)
    public <T> T withOrganization(String orgId, Function<OrganizationResource, T> fn) {
        return withRegistrationRealm(realm -> {
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
        return withRegistrationRealm(realm -> fn.apply(realm.organizations()));
    }

    // All organizations (void)
    public void runWithOrganizations(Consumer<OrganizationsResource> consumer) {
        withOrganizations(orgs -> { consumer.accept(orgs); return null; });
    }
}
