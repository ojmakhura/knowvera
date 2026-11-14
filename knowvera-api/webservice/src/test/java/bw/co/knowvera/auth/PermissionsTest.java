package bw.co.knowvera.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

class PermissionsTest {

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void currentListsOnlyResourceScopePermissionsSorted() {

        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("user", null, List.of(
                new SimpleGrantedAuthority("SCOPE_kyc-records:view"),
                new SimpleGrantedAuthority("SCOPE_documents:edit-own"),
                new SimpleGrantedAuthority("SCOPE_openid"),          // OAuth scope, not a permission
                new SimpleGrantedAuthority("ROLE_KYC_ANALYST"))));

        assertEquals(List.of("documents:edit-own", "kyc-records:view"), List.copyOf(Permissions.current()));
        assertTrue(Permissions.has("kyc-records:view"));
    }

    @Test
    void currentIsEmptyWithoutAnAuthentication() {
        assertTrue(Permissions.current().isEmpty());
    }
}
