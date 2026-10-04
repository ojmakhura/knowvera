package bw.co.knowvera.config;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.keycloak.authorization.client.AuthorizationDeniedException;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Turns the caller's Keycloak authorization permissions on the API client into authorities
 * of the form {@code SCOPE_<resource>:<scope>}, e.g. {@code SCOPE_organisations:view}, so
 * that endpoints can use {@code @PreAuthorize("hasAuthority('SCOPE_organisations:view')")}.
 *
 * Keycloak evaluates the permissions (resources, scopes, policies and permissions generated
 * by keycloak/generate_authz.py). The result is cached per access token until it expires,
 * so Keycloak is called once per token rather than once per request.
 */
@Component
public class KeycloakPermissionConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    public static final String AUTHORITY_PREFIX = "SCOPE_";

    private static final Logger logger = LoggerFactory.getLogger(KeycloakPermissionConverter.class);
    private static final int CACHE_SWEEP_THRESHOLD = 10_000;

    private final boolean enabled;
    private final String issuerUri;
    private final String apiClient;
    private final String apiClientSecret;

    private final Map<String, CachedPermissions> cache = new ConcurrentHashMap<>();
    private volatile AuthzClient authzClient;

    private record CachedPermissions(Set<GrantedAuthority> authorities, Instant expiresAt) {
    }

    public KeycloakPermissionConverter(
            @Value("${app.authz.permission-authorities}") boolean enabled,
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri,
            @Value("${app.authz.client}") String apiClient,
            @Value("${app.authz.client-secret}") String apiClientSecret) {

        this.enabled = enabled;
        this.issuerUri = issuerUri;
        this.apiClient = apiClient;
        this.apiClientSecret = apiClientSecret;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        if (!enabled) {
            return Set.of();
        }

        Instant now = Instant.now();
        CachedPermissions cached = cache.get(jwt.getTokenValue());
        if (cached != null && cached.expiresAt().isAfter(now)) {
            return cached.authorities();
        }

        Set<GrantedAuthority> authorities = loadAuthorities(jwt.getTokenValue());
        Instant expiresAt = jwt.getExpiresAt() != null ? jwt.getExpiresAt() : now.plusSeconds(60);

        if (cache.size() > CACHE_SWEEP_THRESHOLD) {
            cache.values().removeIf(entry -> entry.expiresAt().isBefore(now));
        }
        cache.put(jwt.getTokenValue(), new CachedPermissions(authorities, expiresAt));

        return authorities;
    }

    private Set<GrantedAuthority> loadAuthorities(String accessToken) {

        List<Permission> permissions;
        try {
            // No specific permissions requested: Keycloak returns everything granted on the API client
            permissions = authzClient().authorization(accessToken).getPermissions(new AuthorizationRequest());
        } catch (AuthorizationDeniedException e) {
            // Nothing granted
            return Set.of();
        } catch (RuntimeException e) {
            // Fail closed: permission checks deny, role checks keep working
            logger.error("Could not load Keycloak permissions for {}", apiClient, e);
            return Set.of();
        }

        return permissions.stream()
                .flatMap(permission -> permission.getScopes() == null || permission.getScopes().isEmpty()
                        ? Stream.of(AUTHORITY_PREFIX + permission.getResourceName())
                        : permission.getScopes().stream()
                                .map(scope -> AUTHORITY_PREFIX + permission.getResourceName() + ":" + scope))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Created on first use: AuthzClient.create() contacts Keycloak, which must not block startup.
     */
    private AuthzClient authzClient() {

        AuthzClient client = authzClient;
        if (client == null) {
            synchronized (this) {
                client = authzClient;
                if (client == null) {
                    String serverUrl = issuerUri.substring(0, issuerUri.indexOf("/realms"));
                    String realm = issuerUri.substring(issuerUri.lastIndexOf('/') + 1);
                    client = AuthzClient.create(new Configuration(serverUrl, realm, apiClient,
                            Map.of("secret", apiClientSecret), null));
                    authzClient = client;
                }
            }
        }
        return client;
    }
}
