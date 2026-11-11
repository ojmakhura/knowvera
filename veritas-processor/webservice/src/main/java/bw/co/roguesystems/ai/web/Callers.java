package bw.co.roguesystems.ai.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Who made a request: the user name, or the client id for service accounts.
 */
public final class Callers {

    private Callers() {
    }

    public static String name(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwt) {
            String user = jwt.getToken().getClaimAsString("preferred_username");
            if (user != null && !user.startsWith("service-account-")) {
                return user;
            }
            String client = jwt.getToken().getClaimAsString("azp");
            return client == null ? jwt.getName() : client;
        }
        return authentication == null ? "anonymous" : authentication.getName();
    }
}
