package bw.co.knowvera.auth;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import bw.co.knowvera.config.KeycloakPermissionConverter;

/**
 * The caller's Keycloak permissions, for checks finer than an endpoint's @PreAuthorize
 * (e.g. which fields of a record the caller may change).
 */
public final class Permissions {

    private Permissions() {
    }

    /** Whether the caller holds {@code <resource>:<scope>}, e.g. "kyc-records:review". */
    public static boolean has(String permission) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }

        String authority = KeycloakPermissionConverter.AUTHORITY_PREFIX + permission;
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(authority::equals);
    }

    /**
     * Every {@code <resource>:<scope>} permission the caller holds, sorted. OAuth scopes in the
     * token (openid, profile, ...) share the authority prefix but have no ':' and are left out.
     */
    public static SortedSet<String> current() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return Collections.emptySortedSet();
        }

        String prefix = KeycloakPermissionConverter.AUTHORITY_PREFIX;
        SortedSet<String> permissions = new TreeSet<>();
        auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith(prefix) && a.indexOf(':') > prefix.length())
                .forEach(a -> permissions.add(a.substring(prefix.length())));
        return permissions;
    }
}
