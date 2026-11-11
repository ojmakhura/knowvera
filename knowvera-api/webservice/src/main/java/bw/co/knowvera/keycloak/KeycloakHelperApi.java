package bw.co.knowvera.keycloak;

import java.util.Set;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.keycloak.KeycloakHelperApi
 */
@RequestMapping("/keycloak")
public interface KeycloakHelperApi {

    @GetMapping(
        value = "/client-roles"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_keycloak:list')")
    public @Nullable ResponseEntity<Set<String>> getClientRoles(@RequestParam(name = "clientId", required = true) String clientId) throws Exception;

    @GetMapping(
        value = "/realm-roles"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_keycloak:list')")
    public @Nullable ResponseEntity<Set<String>> getRealmRoles() throws Exception;

    @GetMapping(
        value = "/permissions"
    )
    public @Nullable ResponseEntity<Set<String>> getUserPermissions() throws Exception;
}