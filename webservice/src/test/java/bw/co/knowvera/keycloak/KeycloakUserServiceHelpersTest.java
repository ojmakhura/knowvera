package bw.co.knowvera.keycloak;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import bw.co.knowvera.user.UserDTO;

class KeycloakUserServiceHelpersTest {

    @Test
    void copyOfSharesNoCollectionsAndDropsThePassword() {

        UserDTO user = new UserDTO();
        user.setUserId("u1");
        user.setPassword("secret");
        user.setRoles(new LinkedHashSet<>(Set.of("APPLICANT")));
        user.setRealmRoles(new ArrayList<>(List.of("APPLICANT")));

        UserDTO copy = KeycloakUserService.copyOf(user);
        copy.getRoles().add("SUPER_ADMIN");
        copy.getRealmRoles().add("SUPER_ADMIN");

        assertEquals(Set.of("APPLICANT"), user.getRoles());
        assertEquals(List.of("APPLICANT"), user.getRealmRoles());
        assertEquals("u1", copy.getUserId());
        assertNull(copy.getPassword());
    }

    @Test
    void readAllFollowsPagesPastTheFirstHundred() {

        List<Integer> all = IntStream.range(0, 250).boxed().toList();
        List<String> requests = new ArrayList<>();

        List<Integer> read = KeycloakUserService.readAll((first, max) -> {
            requests.add(first + "+" + max);
            return all.subList(Math.min(first, all.size()), Math.min(first + max, all.size()));
        });

        assertEquals(all, read);
        assertEquals(List.of("0+100", "100+100", "200+100"), requests);
    }

    @Test
    void readAllStopsAfterAnExactlyFullLastPage() {

        List<Integer> all = IntStream.range(0, 100).boxed().toList();
        List<Integer> read = KeycloakUserService.readAll(
                (first, max) -> all.subList(Math.min(first, all.size()), Math.min(first + max, all.size())));

        assertEquals(all, read);
    }
}
