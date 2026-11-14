package bw.co.knowvera.auth;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import bw.co.knowvera.context.OrganisationContext;

public class SecurityOrganisationContext implements OrganisationContext {

    @Override
    public UUID getOrganisationId() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new AccessDeniedException(
                    "No authenticated organization");
        }

        Jwt jwt = jwtAuth.getToken();

        // Extract organization from JWT
        return extractOrganizationId(jwt);
    }

    private UUID extractOrganizationId(Jwt jwt) {

        Object claim = jwt.getClaim("organization");
        
        if (!(claim instanceof Map<?, ?> orgs) || orgs.isEmpty()) {
            throw new AccessDeniedException("No organization in token");
        }

        if (orgs.size() > 1) {
            throw new AccessDeniedException(
                    "Token spans multiple orgs; request scope=organization:<alias>");
        }
        return UUID.fromString(orgs.keySet().iterator().next().toString());
    }
}
