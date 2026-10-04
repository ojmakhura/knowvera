package bw.co.knowvera.config;

import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.PostConstruct;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuerUri;

	@Value("${app.authz.enabled}")
	private boolean policyEnforcerEnabled;

	@Value("${app.authz.client}")
	private String apiClient;

	@Value("${app.authz.client-secret}")
	private String apiClientSecret;

	@Value("${app.authz.enforcer-config}")
	private String enforcerConfig;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
			JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {

		http
				.cors(Customizer.withDefaults())  // Enable CORS with the configured CorsConfigurationSource
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests((authz) -> authz
						// Keep in sync with PUBLIC_PATHS in keycloak/generate_authz.py
						.requestMatchers(
								"/swagger-ui/**",  // Changed from /* to /** to match all paths
								"/v3/**",
								"/actuator/**",
								"/analytics/**",
								"/client-requests/confirm-token/**",
								"/individual/request/**",
								"/organisations/request/**",
								"/client-requests/*/confirm")
						.permitAll()
						.anyRequest().authenticated())
				.sessionManagement(management -> management
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

		if (policyEnforcerEnabled) {
			// Keycloak resource-based authorization: each path + HTTP method maps to a resource scope
			// whose permissions are evaluated by Keycloak. @PreAuthorize still checks record ownership.
			http.addFilterAfter(policyEnforcerFilter(), BearerTokenAuthenticationFilter.class);
		}

		return http.build();
	}

	/**
	 * Not a bean on purpose: Spring Boot would also register a Filter bean on the servlet
	 * container, outside the security chain.
	 */
	private ServletPolicyEnforcerFilter policyEnforcerFilter() throws IOException {

		PolicyEnforcerConfig config;
		try (InputStream in = new ClassPathResource(enforcerConfig).getInputStream()) {
			config = JsonSerialization.readValue(in, PolicyEnforcerConfig.class);
		}

		config.setAuthServerUrl(issuerUri.substring(0, issuerUri.indexOf("/realms")));
		config.setRealm(issuerUri.substring(issuerUri.lastIndexOf('/') + 1));
		config.setResource(apiClient);
		config.setCredentials(Map.of("secret", apiClientSecret));

		return new ServletPolicyEnforcerFilter(request -> config);
	}

	/**
	 * Enable Security for threads spawned by Resilience4j
	 */
	@PostConstruct
	public void enableAuthenticationContextOnSpawnedThreads() {
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		// WARNING: Allowing all origins (*) is insecure for production!
		// In production, specify exact origins: Arrays.asList("https://yourdomain.com")
		configuration.setAllowedOriginPatterns(Arrays.asList("*")); // Use setAllowedOriginPatterns instead of setAllowedOrigins when allowing credentials
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true); // Important for JWT tokens in cookies/headers
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(KeycloakPermissionConverter permissionConverter) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        KeycloakRoleConverter roleConverter = new KeycloakRoleConverter();

        // ROLE_<role> and SCOPE_<oauth scope> from the token, plus SCOPE_<resource>:<scope>
        // for each Keycloak permission, e.g. hasAuthority('SCOPE_organisations:view')
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new HashSet<>(roleConverter.convert(jwt));
            authorities.addAll(permissionConverter.convert(jwt));
            return authorities;
        });

        // Optional: use email / preferred_username as principal
        converter.setPrincipalClaimName("preferred_username");

        return converter;
    }
}