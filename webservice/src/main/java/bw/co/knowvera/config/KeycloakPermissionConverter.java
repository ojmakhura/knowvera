package bw.co.knowvera.config;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import bw.co.knowvera.security.PermissionService;

/**
 * Turns the caller's Keycloak authorization permissions on the API client into authorities
 * of the form {@code SCOPE_<resource>:<scope>}, e.g. {@code SCOPE_organisations:view}, so
 * that endpoints can use {@code @PreAuthorize("hasAuthority('SCOPE_organisations:view')")}.
 *
 * Keycloak evaluates the permissions (resources, scopes, policies and permissions generated
 * by keycloak/generate_authz.py); {@link PermissionService} asks it once per token and caches
 * the answer in Redis, shared by every API instance, until the token expires.
 */
@Component
public class KeycloakPermissionConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    public static final String AUTHORITY_PREFIX = "SCOPE_";

    private final boolean enabled;
    private final PermissionService permissionService;

    public KeycloakPermissionConverter(@Value("${app.authz.permission-authorities}") boolean enabled,
            PermissionService permissionService) {
        this.enabled = enabled;
        this.permissionService = permissionService;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        if (!enabled) {
            return Set.of();
        }
        return permissionService.grantedPermissions(jwt).stream()
                .map(permission -> new SimpleGrantedAuthority(AUTHORITY_PREFIX + permission))
                .collect(Collectors.toUnmodifiableSet());
    }
}
