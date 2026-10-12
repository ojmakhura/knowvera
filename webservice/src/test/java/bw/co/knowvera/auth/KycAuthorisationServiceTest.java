package bw.co.knowvera.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.DocumentService;
import bw.co.knowvera.individual.IndividualService;
import bw.co.knowvera.invoice.KycInvoiceService;
import bw.co.knowvera.keycloak.KeycloakUserService;
import bw.co.knowvera.kyc.KycRecordDTO;
import bw.co.knowvera.kyc.KycRecordService;
import bw.co.knowvera.organisation.OrganisationDTO;
import bw.co.knowvera.organisation.OrganisationService;
import bw.co.knowvera.organisation.client.ClientRequestService;
import bw.co.knowvera.subscription.KycSubscriptionService;
import bw.co.knowvera.user.UserDTO;

class KycAuthorisationServiceTest {

    static class OrganisationController {

        @RequiresOwnership(scope = "organisations:view", target = "ORGANISATION", id = "#organisationId")
        public String load(String organisationId) {
            return "loaded " + organisationId;
        }

        @RequiresOwnership(scope = "documents:list", target = "#target", id = "#targetId")
        public String loadTarget(TargetEntity target, String targetId) {
            return "loaded " + targetId;
        }

        @RequiresOwnership(scope = "kyc-records:edit", target = "#record.target", id = "#record.targetId",
                record = "KYC_RECORD", recordId = "#record.id")
        public String save(KycRecordDTO record) {
            return "saved " + record.getId();
        }
    }

    private final KeycloakUserService keycloakUserService = mock(KeycloakUserService.class);
    private final OrganisationService organisationService = mock(OrganisationService.class);
    private final KycRecordService kycRecordService = mock(KycRecordService.class);
    private OrganisationController controller;

    @BeforeEach
    void setUp() {
        KycAuthorisationService aspect = new KycAuthorisationService(kycRecordService,
                keycloakUserService, mock(IndividualService.class), organisationService,
                mock(DocumentService.class), mock(ClientRequestService.class),
                mock(KycInvoiceService.class), mock(KycSubscriptionService.class));

        AspectJProxyFactory factory = new AspectJProxyFactory(new OrganisationController());
        factory.setProxyTargetClass(true);
        factory.addAspect(aspect);
        controller = factory.getProxy();

        UserDTO user = new UserDTO();
        user.setUserId("user-1");
        user.setOrganisationId("org-1");
        when(keycloakUserService.findUserById("user-1")).thenReturn(user);

        when(organisationService.findById(any())).thenAnswer(invocation -> {
            OrganisationDTO organisation = new OrganisationDTO();
            organisation.setId(invocation.getArgument(0));
            return organisation;
        });
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    private void authenticate(String... authorities) {
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60),
                java.util.Map.of("alg", "none"), java.util.Map.of("sub", "user-1"));
        List<SimpleGrantedAuthority> granted = java.util.Arrays.stream(authorities)
                .map(SimpleGrantedAuthority::new).toList();
        TestingAuthenticationToken auth = new TestingAuthenticationToken(jwt, null, List.copyOf(granted));
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void typeLevelPermissionIsNotRestricted() {
        authenticate("ROLE_KYC_ANALYST", "SCOPE_organisations:view");

        assertEquals("loaded org-2", controller.load("org-2"));
        verify(organisationService, never()).findById(any());
    }

    @Test
    void ownPermissionMayAccessOwnRecord() {
        authenticate("ROLE_ORG_ADMIN", "SCOPE_organisations:view-own");

        assertEquals("loaded org-1", controller.load("org-1"));
    }

    @Test
    void ownPermissionIsDeniedOtherRecords() {
        authenticate("ROLE_ORG_ADMIN", "SCOPE_organisations:view-own");

        assertThrows(AuthorizationDeniedException.class, () -> controller.load("org-2"));
    }

    private static KycRecordDTO record(String id, String organisationId) {
        KycRecordDTO record = new KycRecordDTO();
        record.setId(id);
        record.setTarget(TargetEntity.ORGANISATION);
        record.setTargetId(organisationId);
        return record;
    }

    @Test
    void updateMustBeOfAStoredRecordTheCallerOwns() throws Exception {
        authenticate("ROLE_ORG_ADMIN", "SCOPE_kyc-records:edit-own");
        when(kycRecordService.findById("mine")).thenReturn(record("mine", "org-1"));
        when(kycRecordService.findById("theirs")).thenReturn(record("theirs", "org-2"));

        assertEquals("saved mine", controller.save(record("mine", "org-1")));
        // Naming itself as owner does not take over another organisation's record
        assertThrows(AuthorizationDeniedException.class, () -> controller.save(record("theirs", "org-1")));
        // Unknown records are not proven to be the caller's
        assertThrows(AuthorizationDeniedException.class, () -> controller.save(record("missing", "org-1")));
        // New records only need the owner named in the body
        assertEquals("saved null", controller.save(record(null, "org-1")));
    }

    @Test
    void rolesAloneGrantNothing() {
        // Access comes from Keycloak permissions, not role names
        authenticate("ROLE_PLATFORM_ADMIN");

        assertThrows(AuthorizationDeniedException.class, () -> controller.load("org-1"));
        verify(organisationService, never()).findById(any());
    }

    @Test
    void targetCanComeFromAnExpression() {
        authenticate("ROLE_APPLICANT", "SCOPE_documents:list-own");

        assertEquals("loaded org-1", controller.loadTarget(TargetEntity.ORGANISATION, "org-1"));
        assertThrows(AuthorizationDeniedException.class,
                () -> controller.loadTarget(TargetEntity.ORGANISATION, "org-2"));
    }
}
