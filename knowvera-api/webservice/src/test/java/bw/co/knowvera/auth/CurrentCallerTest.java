package bw.co.knowvera.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.individual.IndividualDTO;
import bw.co.knowvera.individual.IndividualService;
import bw.co.knowvera.individual.IndividualServiceException;
import bw.co.knowvera.keycloak.KeycloakUserService;
import bw.co.knowvera.kyc.KycRecordService;
import bw.co.knowvera.user.UserDTO;

class CurrentCallerTest {

    private final KeycloakUserService users = mock(KeycloakUserService.class);
    private final IndividualService individuals = mock(IndividualService.class);
    private final KycRecordService kycRecords = mock(KycRecordService.class);
    private final CurrentCaller caller = new CurrentCaller(users, individuals, kycRecords);

    @BeforeEach
    void signIn() {
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"), Map.of("sub", "u1"));
        TestingAuthenticationToken auth = new TestingAuthenticationToken(jwt, null, List.of());
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void aCallerWithoutProfileOwnsNothing() throws Exception {
        when(individuals.findByUserId("u1")).thenThrow(new IndividualServiceException("Individual not found for userId: u1"));
        when(users.findUserById("u1")).thenReturn(new UserDTO());

        assertTrue(caller.individual().isEmpty());
        assertTrue(caller.organisationId().isEmpty());
        assertTrue(caller.owned(null).isEmpty());
        assertTrue(caller.kycRecords().isEmpty());
        // An empty filter would match every KYC record: the search must not run at all
        verify(kycRecords, never()).search(any(), any());
    }

    @Test
    void theCallerIsResolvedOncePerRequest() throws Exception {
        IndividualDTO individual = new IndividualDTO();
        individual.setId("ind-1");
        when(individuals.findByUserId("u1")).thenReturn(individual);
        when(users.findUserById("u1")).thenReturn(new UserDTO());
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
        try {
            caller.subjectIds();
            caller.owned(null);
            caller.individual();
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
        verify(individuals, times(1)).findByUserId("u1");
        verify(users, times(1)).findUserById("u1");
    }

    @Test
    void ownedRecordsComeFromProfileAndOrganisation() throws Exception {
        IndividualDTO individual = new IndividualDTO();
        individual.setId("ind-1");
        when(individuals.findByUserId("u1")).thenReturn(individual);
        UserDTO user = new UserDTO();
        user.setOrganisationId("org-1");
        when(users.findUserById("u1")).thenReturn(user);

        assertEquals(List.of("ind-1", "org-1"), caller.subjectIds());
        assertEquals(List.of(new CurrentCaller.Owned(TargetEntity.INDIVIDUAL, "ind-1")), caller.owned(TargetEntity.INDIVIDUAL));
        assertEquals(List.of(new CurrentCaller.Owned(TargetEntity.ORGANISATION, "org-1")), caller.owned(TargetEntity.ORGANISATION));
    }
}
