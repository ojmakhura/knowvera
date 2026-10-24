package bw.co.knowvera.security;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Connection to Keycloak Authorization Services for the confidential API client
 * ({@code knowvera-api}), and how long its answers are cached ({@link AuthzCache}).
 *
 * @param serverUrl         Keycloak base URL, e.g. {@code https://keycloak.example.com}
 * @param realm             the realm
 * @param clientId          the API (resource server) client id
 * @param clientSecret      the API client's secret
 * @param permissionCacheTtl upper bound for caching a token's permissions (never past its expiry)
 * @param localCacheTtl     upper bound for keeping an entry in the in-process cache in front of Redis
 * @param localCacheMaxSize maximum number of entries in the in-process cache
 */
@ConfigurationProperties("app.keycloak.authz")
public record KeycloakAuthzProperties(String serverUrl, String realm, String clientId, String clientSecret,
        Duration permissionCacheTtl, Duration localCacheTtl, Long localCacheMaxSize) {

    public KeycloakAuthzProperties {
        permissionCacheTtl = permissionCacheTtl == null ? Duration.ofMinutes(5) : permissionCacheTtl;
        localCacheTtl = localCacheTtl == null ? Duration.ofMinutes(1) : localCacheTtl;
        localCacheMaxSize = localCacheMaxSize == null ? 10_000L : localCacheMaxSize;
    }
}
