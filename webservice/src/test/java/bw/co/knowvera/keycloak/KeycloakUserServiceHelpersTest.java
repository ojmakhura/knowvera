package bw.co.knowvera.keycloak;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import bw.co.knowvera.individual.IndividualDTO;
import bw.co.knowvera.organisation.OrganisationDTO;
import bw.co.knowvera.organisation.OrganisationListDTO;
import bw.co.knowvera.settings.SettingsDTO;
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

    @Test
    void newUserPayloadSendsNamePartsSeparatelyAndSettings() {

        IndividualDTO individual = new IndividualDTO();
        individual.setFirstName("Ann");
        individual.setMiddleName("Mary");
        individual.setSurname("Applicant");
        UserDTO user = new UserDTO();
        user.setEmail("ann@example.com");
        user.setUsername("ann@example.com");
        SettingsDTO settings = new SettingsDTO();
        settings.setPlatformName("Knowvera");
        settings.setKycPortalLink("https://portal.example");
        settings.setSupportContact("support@example.com");

        Map<String, String> payload = KeycloakUserService.newUserPayload(individual, user, null, settings,
                Duration.ofHours(72), 2026);

        assertEquals(Map.of(
                "email", "ann@example.com",
                "username", "ann@example.com",
                "firstName", "Ann",
                "middleName", "Mary",
                "surname", "Applicant",
                "loginUrl", "https://portal.example",
                "platformName", "Knowvera",
                "supportContact", "support@example.com",
                "setupLinkValidFor", "72 hours",
                "currentYear", "2026"), payload);
    }

    @Test
    void newUserPayloadOmitsBlankValuesAndNamesTheOrganisation() {

        IndividualDTO individual = new IndividualDTO();
        individual.setFirstName("Olivia");
        individual.setMiddleName(" ");
        individual.setSurname("Admin");
        UserDTO user = new UserDTO();
        user.setEmail("olivia@example.com");
        OrganisationDTO organisation = new OrganisationDTO();
        organisation.setId("org-1");
        organisation.setName("Example Holdings");

        Map<String, String> payload = KeycloakUserService.newUserPayload(individual, user, organisation, null,
                Duration.ofMinutes(90), 2026);

        assertFalse(payload.containsKey("middleName"));
        assertFalse(payload.containsKey("loginUrl"), "no empty login link");
        assertEquals("Example Holdings", payload.get("organisationName"));
        assertEquals("90 minutes", payload.get("setupLinkValidFor"));
        assertEquals("Knowvera", payload.get("platformName"), "default when there are no settings");
        assertEquals("our support team", payload.get("supportContact"));
    }

    @Test
    void readableDurations() {
        assertEquals("1 hour", KeycloakUserService.readable(Duration.ofHours(1)));
        assertEquals("24 hours", KeycloakUserService.readable(Duration.ofDays(1)));
        assertNull(KeycloakUserService.readable(Duration.ZERO));
        assertNull(KeycloakUserService.readable(null));
    }

    @Test
    void clientInvitedByAnOrganisationIsNotAnOrganisationUser() {

        IndividualDTO client = new IndividualDTO();  // no organisation of their own
        OrganisationDTO requesting = new OrganisationDTO();
        requesting.setId("org-1");
        requesting.setName("Example Holdings");

        assertNull(KeycloakUserService.memberOrganisation(client, requesting));
        assertFalse(KeycloakUserService.newUserPayload(client, new UserDTO(), null, null, Duration.ofHours(72), 2026)
                .containsKey("organisationName"), "novu-new-user, not novu-new-org-user");
    }

    @Test
    void memberOrganisationIsTheIndividualsOwn() {

        IndividualDTO member = new IndividualDTO();
        member.setOrganisation(organisation("org-2", "Own Co"));
        OrganisationDTO requesting = new OrganisationDTO();
        requesting.setId("org-1");
        requesting.setName("Example Holdings");

        OrganisationDTO result = KeycloakUserService.memberOrganisation(member, requesting);
        assertEquals("org-2", result.getId());
        assertEquals("Own Co", result.getName());
    }

    @Test
    void memberOrganisationNameFallsBackToTheSameRequestedOrganisation() {

        IndividualDTO member = new IndividualDTO();
        member.setOrganisation(organisation("org-1", null));
        OrganisationDTO requested = new OrganisationDTO();
        requested.setId("org-1");
        requested.setName("Example Holdings");

        assertEquals("Example Holdings", KeycloakUserService.memberOrganisation(member, requested).getName());
    }

    private static OrganisationListDTO organisation(String id, String name) {
        return new OrganisationListDTO(id, null, name, null, null, null, null, false, null, "", "");
    }
}
