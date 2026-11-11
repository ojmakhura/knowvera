package bw.co.knowvera.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import bw.co.knowvera.security.PermissionService;

@ExtendWith(MockitoExtension.class)
class KeycloakPermissionConverterTest {

    @Mock
    private PermissionService permissionService;

    private final Jwt jwt = Jwt.withTokenValue("t").header("alg", "none").subject("u").build();

    @Test
    void permissionsBecomeScopeAuthorities() {
        when(permissionService.grantedPermissions(jwt)).thenReturn(Set.of("documents:view", "novu"));

        Set<String> authorities = new KeycloakPermissionConverter(true, permissionService).convert(jwt).stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        assertEquals(Set.of("SCOPE_documents:view", "SCOPE_novu"), authorities);
    }

    @Test
    void switchedOffGrantsNothingAndAsksNobody() {
        assertTrue(new KeycloakPermissionConverter(false, permissionService).convert(jwt).isEmpty());
        verifyNoInteractions(permissionService);
    }
}
