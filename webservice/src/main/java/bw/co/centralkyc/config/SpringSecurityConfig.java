package bw.co.centralkyc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.PostConstruct;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				.cors(Customizer.withDefaults())  // Enable CORS with the configured CorsConfigurationSource
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests((authz) -> authz
						.requestMatchers(
								"/swagger-ui/**",  // Changed from /* to /** to match all paths
								"/v3/**",
								"/actuator/**",
								"/analytics/**",
								"/client-request/confirm-token/**",
								"/individual/request/**",
								"/organisation/request/**",
								"/client-request/*/confirm")
						.permitAll()
						.anyRequest().authenticated())
				.sessionManagement(management -> management
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

		return http.build();
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
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(
            new KeycloakRoleConverter()
        );

        // Optional: use email / preferred_username as principal
        converter.setPrincipalClaimName("preferred_username");

        return converter;
    }
}