package bw.co.knowvera.keycloak;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.Test;

class KeycloakServiceTest {

    private static String token(String claimsJson) {
        Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
        return enc.encodeToString("{\"alg\":\"none\"}".getBytes(StandardCharsets.UTF_8)) + "."
                + enc.encodeToString(claimsJson.getBytes(StandardCharsets.UTF_8)) + ".sig";
    }

    @Test
    void readsTheRealmManagementRolesFromTheAdminToken() {
        String token = token("{\"resource_access\":{\"realm-management\":{\"roles\":[\"manage-users\",\"manage-realm\"]},"
                + "\"account\":{\"roles\":[\"view-profile\"]}}}");

        assertEquals(List.of("manage-realm", "manage-users"), KeycloakService.realmManagementRoles(token));
    }

    @Test
    void aTokenWithoutRealmManagementRolesHasNone() {
        assertTrue(KeycloakService.realmManagementRoles(token("{\"sub\":\"service-account\"}")).isEmpty());
    }

    @Test
    void theRequiredRolesMatchTheRealmGenerator() {
        assertEquals(List.of("manage-users", "manage-realm", "view-clients"), KeycloakService.REQUIRED_ROLES);
    }
}
