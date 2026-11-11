package bw.co.roguesystems.ai.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Bearer tokens from Keycloak. Access comes from the roles of the processor's client
 * (resource_access.&lt;veritas.security.client-id&gt;.roles):
 * <ul>
 * <li>profiles-view: read profiles</li>
 * <li>profiles-admin: read and save profiles</li>
 * <li>jobs-submit: submit jobs and read their results</li>
 * </ul>
 */
@Configuration
public class SecurityConfig {

    public static final String PROFILES_VIEW = "profiles-view";
    public static final String PROFILES_ADMIN = "profiles-admin";
    public static final String JOBS_SUBMIT = "jobs-submit";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            @Value("${veritas.security.client-id:veritas-processor}") String clientId) throws Exception {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> clientRoles(jwt, clientId));

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/actuator/health/**", "/actuator/info", "/v3/api-docs/**",
                                "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/profiles/**").hasAnyRole(PROFILES_VIEW, PROFILES_ADMIN)
                        .requestMatchers("/profiles/**").hasRole(PROFILES_ADMIN)
                        .requestMatchers("/jobs/**").hasRole(JOBS_SUBMIT)
                        .anyRequest().authenticated())
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)));
        return http.build();
    }

    static Collection<GrantedAuthority> clientRoles(Jwt jwt, String clientId) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (jwt.getClaim("resource_access") instanceof Map<?, ?> access
                && access.get(clientId) instanceof Map<?, ?> client
                && client.get("roles") instanceof Collection<?> roles) {
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        }
        return authorities;
    }
}
