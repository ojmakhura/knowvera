package bw.co.knowvera.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        Set<GrantedAuthority> authorities = new HashSet<>();

        // -----------------------
        // Realm roles
        // -----------------------
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?> roles) {
            for (Object role : roles) {
                authorities.add(new SimpleGrantedAuthority(
                        "ROLE_" + role.toString()));
            }
        }

        // -----------------------
        // Client roles (IMPORTANT)
        // -----------------------
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {

            resourceAccess.forEach((clientId, clientObj) -> {
                if (clientObj instanceof Map<?, ?> clientMap) {

                    Object rolesObj = clientMap.get("roles");
                    if (rolesObj instanceof Collection<?> roles) {

                        for (Object role : roles) {
                            authorities.add(new SimpleGrantedAuthority(
                                    "ROLE_" + role));
                        }
                    }
                }
            });
        }

        // -----------------------
        // Scopes (Spring standard)
        // -----------------------
        String scope = jwt.getClaimAsString("scope");
        if (scope != null && !scope.isBlank()) {
            Arrays.stream(scope.split(" "))
                    .forEach(s -> authorities.add(new SimpleGrantedAuthority(
                            "SCOPE_" + s)));
        }

        return authorities;
    }
}