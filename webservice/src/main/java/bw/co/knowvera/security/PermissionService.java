package bw.co.knowvera.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HexFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.keycloak.authorization.client.AuthorizationDeniedException;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

/**
 * Asks Keycloak Authorization Services what the caller may do on the API client: the permissions
 * keycloak/generate_authz.py defines, as {@code <resource>:<scope>} (e.g. {@code documents:view},
 * {@code kyc-records:edit-own}); a permission without scopes is just {@code <resource>}.
 * <p>
 * Taken from the token when it already is an RPT, otherwise requested with the UMA grant and cached
 * ({@link AuthzCache}: Caffeine, then Redis, shared by every instance) for at most the token's remaining
 * lifetime, keyed by a hash of the token. Failures to reach Keycloak grant nothing and are not cached.
 */
@Service
public class PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);

    static final String PERMISSIONS_KEY = "knowvera:authz:perm:";

    private final AuthzClientProvider authzClient;
    private final KeycloakAuthzProperties properties;
    private final AuthzCache cache;

    public PermissionService(AuthzClientProvider authzClient, KeycloakAuthzProperties properties, AuthzCache cache) {
        this.authzClient = authzClient;
        this.properties = properties;
        this.cache = cache;
    }

    /** The caller's permissions on the API client; empty when none are granted or Keycloak is unreachable. */
    public Set<String> grantedPermissions(Jwt jwt) {

        Map<String, Object> rpt = jwt.getClaimAsMap("authorization");
        if (rpt != null) {
            return fromRpt(rpt.get("permissions"));
        }

        String key = PERMISSIONS_KEY + sha256(jwt.getTokenValue());
        String cached = cache.get(key);
        if (cached != null) {
            return cached.isEmpty() ? Set.of() : Set.of(cached.split(" "));
        }

        Set<String> granted;
        try {
            // No specific permissions requested: Keycloak returns everything granted on the API client
            AuthorizationRequest request = new AuthorizationRequest();
            request.setAudience(properties.clientId());
            List<Permission> permissions = authzClient.get().authorization(jwt.getTokenValue()).getPermissions(request);
            granted = new LinkedHashSet<>();
            for (Permission permission : permissions) {
                add(granted, permission.getResourceName(), permission.getScopes());
            }
        } catch (AuthorizationDeniedException e) {
            granted = Set.of();
        } catch (RuntimeException e) {
            // Fail closed: permission checks deny, role checks keep working; not cached, so the next call retries
            logger.error("Could not load Keycloak permissions for {}", jwt.getSubject(), e);
            return Set.of();
        }

        cache.put(key, String.join(" ", granted), AuthzCache.until(jwt.getExpiresAt(), properties.permissionCacheTtl()));
        return Set.copyOf(granted);
    }

    /** Permissions in an RPT's {@code authorization.permissions} claim ({@code rsname} and {@code scopes}). */
    private static Set<String> fromRpt(Object permissions) {
        Set<String> granted = new LinkedHashSet<>();
        if (permissions instanceof Collection<?> list) {
            for (Object permission : list) {
                if (permission instanceof Map<?, ?> map && map.get("rsname") != null) {
                    Collection<?> scopes = map.get("scopes") instanceof Collection<?> c ? c : List.of();
                    add(granted, map.get("rsname").toString(), scopes.stream().map(Object::toString).toList());
                }
            }
        }
        return Set.copyOf(granted);
    }

    private static void add(Set<String> granted, String resource, Collection<String> scopes) {
        if (resource == null) {
            return;
        }
        if (scopes == null || scopes.isEmpty()) {
            granted.add(resource);
        } else {
            scopes.forEach(scope -> granted.add(resource + ":" + scope));
        }
    }

    static String sha256(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
