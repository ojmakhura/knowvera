package bw.co.knowvera.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import bw.co.knowvera.keycloak.KeycloakUserService;
import bw.co.knowvera.user.UserDTO;

class UserAdministrationGuardTest {

    private final KeycloakUserService users = mock(KeycloakUserService.class);
    private final UserAdministrationGuard guard = new UserAdministrationGuard(users);

    @BeforeEach
    void setUp() {
        when(users.findUserById("caller")).thenReturn(user("org-1"));
        when(users.findUserById("colleague")).thenReturn(user("org-1"));
        when(users.findUserById("outsider")).thenReturn(user("org-2"));
        when(users.findUserById("boss")).thenReturn(user(null));
        when(users.findEffectiveRealmRoles("colleague")).thenReturn(Set.of("ORG_USER", "CUSTOMER"));
        when(users.findEffectiveRealmRoles("outsider")).thenReturn(Set.of("ORG_USER", "CUSTOMER"));
        // DEVELOPER includes SUPER_ADMIN: effective roles carry both
        when(users.findEffectiveRealmRoles("boss")).thenReturn(Set.of("DEVELOPER", "SUPER_ADMIN", "STAFF"));
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    private static UserDTO user(String organisationId) {
        UserDTO user = new UserDTO();
        user.setOrganisationId(organisationId);
        return user;
    }

    private void authenticate(String... authorities) {
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"),
                Map.of("sub", "caller"));
        List<SimpleGrantedAuthority> granted = Arrays.stream(authorities).map(SimpleGrantedAuthority::new).toList();
        TestingAuthenticationToken auth = new TestingAuthenticationToken(jwt, null, List.copyOf(granted));
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void platformAdministratorAssignsBusinessRolesToAnyone() {
        authenticate("ROLE_PLATFORM_ADMIN", "SCOPE_users:manage");

        assertDoesNotThrow(() -> guard.checkCanManageUser("outsider", "users:manage"));
        assertDoesNotThrow(() -> guard.checkCanAssign(Set.of("KYC_ANALYST", "ORG_ADMIN"), "users:manage"));
    }

    @Test
    void platformAdministratorCannotEscalateOrTouchSuperAdministrators() {
        authenticate("ROLE_PLATFORM_ADMIN", "SCOPE_users:manage");

        assertThrows(AuthorizationDeniedException.class, () -> guard.checkCanAssign(Set.of("SUPER_ADMIN"), "users:manage"));
        assertThrows(AuthorizationDeniedException.class, () -> guard.checkCanAssign(Set.of("DEVELOPER"), "users:manage"));
        // e.g. resetting a super administrator's password
        assertThrows(AuthorizationDeniedException.class, () -> guard.checkCanManageUser("boss", "users:manage"));
    }

    @Test
    void superAdministratorMayAssignPrivilegedRoles() {
        authenticate("ROLE_SUPER_ADMIN", "SCOPE_users:manage");

        assertDoesNotThrow(() -> guard.checkCanAssign(Set.of("SUPER_ADMIN"), "users:manage"));
        assertDoesNotThrow(() -> guard.checkCanManageUser("boss", "users:manage"));
    }

    @Test
    void organisationAdministratorManagesOwnOrganisationWithOrganisationRoles() {
        authenticate("ROLE_ORG_ADMIN", "SCOPE_users:manage-own", "SCOPE_users:edit-own");

        assertDoesNotThrow(() -> guard.checkCanManageUser("colleague", "users:manage"));
        assertDoesNotThrow(() -> guard.checkCanAssign(Set.of("ORG_USER", "ORG_ADMIN"), "users:manage"));
        assertDoesNotThrow(() -> guard.checkCanCreateUserIn("org-1", "users:edit"));
    }

    @Test
    void organisationAdministratorCannotEscalateOrLeaveOrganisation() {
        authenticate("ROLE_ORG_ADMIN", "SCOPE_users:manage-own", "SCOPE_users:edit-own");

        assertThrows(AuthorizationDeniedException.class, () -> guard.checkCanAssign(Set.of("SUPER_ADMIN"), "users:manage"));
        assertThrows(AuthorizationDeniedException.class, () -> guard.checkCanAssign(Set.of("KYC_ANALYST"), "users:manage"));
        assertThrows(AuthorizationDeniedException.class, () -> guard.checkCanManageUser("outsider", "users:manage"));
        assertThrows(AuthorizationDeniedException.class, () -> guard.checkCanCreateUserIn("org-2", "users:edit"));
        assertThrows(AuthorizationDeniedException.class, () -> guard.checkCanManageUser("boss", "users:manage"));
    }

    @Test
    void onlyApplicationRolesCanBeAssigned() {
        authenticate("ROLE_SUPER_ADMIN", "SCOPE_users:manage");

        assertThrows(AuthorizationDeniedException.class, () -> guard.checkCanAssign(Set.of("realm-admin"), "users:manage"));
    }
}
