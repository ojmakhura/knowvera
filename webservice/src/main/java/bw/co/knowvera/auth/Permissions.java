package bw.co.knowvera.auth;

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
}
