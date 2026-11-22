package bw.co.knowvera.organisation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import bw.co.knowvera.GeneralStatus;
import bw.co.knowvera.auth.CurrentCaller;
import bw.co.knowvera.keycloak.KeycloakOrganisationService;
import bw.co.knowvera.kyc.KycComplianceStatus;

/** An organisation's own users edit its profile; its status, KYC status and client flag stay with staff. */
class OrganisationSelfEditTest {

    private final OrganisationService service = mock(OrganisationService.class);
    private final KeycloakOrganisationService keycloak = mock(KeycloakOrganisationService.class);
    private OrganisationApiImpl api;

    @BeforeEach
    void setUp() throws Exception {
        api = new OrganisationApiImpl(service, keycloak, mock(CurrentCaller.class));

        OrganisationDTO stored = new OrganisationDTO();
        stored.setId("o1");
        stored.setName("Old name");
        stored.setStatus(GeneralStatus.values()[0]);
        stored.setKycStatus(KycComplianceStatus.EXPIRED);
        stored.setIsClient(false);
        when(service.findById("o1")).thenReturn(stored);
        when(service.save(any())).thenAnswer(call -> call.getArgument(0));
    }

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    private static void signIn(String permission) {
        SecurityContextHolder.getContext()
                .setAuthentication(new TestingAuthenticationToken("someone", null, "SCOPE_" + permission));
    }

    /** What a caller sends to promote the organisation. */
    private static OrganisationDTO promoted() {
        OrganisationDTO sent = new OrganisationDTO();
        sent.setId("o1");
        sent.setName("New name");
        sent.setStatus(GeneralStatus.values()[GeneralStatus.values().length - 1]);
        sent.setKycStatus(KycComplianceStatus.CURRENT);
        sent.setIsClient(true);
        sent.setKeycloakId("someone-elses-keycloak-organisation");
        return sent;
    }

    private OrganisationDTO saved() throws Exception {
        ArgumentCaptor<OrganisationDTO> saved = ArgumentCaptor.forClass(OrganisationDTO.class);
        verify(service).save(saved.capture());
        return saved.getValue();
    }

    @Test
    void anOrganisationAdministratorCannotChangeWhatStaffDecide() throws Exception {
        signIn("organisations:edit-own");

        api.save(promoted());

        OrganisationDTO saved = saved();
        assertEquals("New name", saved.getName());
        assertEquals(GeneralStatus.values()[0], saved.getStatus());
        assertEquals(KycComplianceStatus.EXPIRED, saved.getKycStatus());
        assertEquals(false, saved.getIsClient());
        assertEquals(null, saved.getKeycloakId());
        verify(keycloak, never()).createOrganisation(any());
    }

    @Test
    void staffCanChangeThem() throws Exception {
        signIn("organisations:edit");
        OrganisationDTO sent = promoted();
        when(keycloak.createOrganisation(any())).thenAnswer(call -> call.getArgument(0));

        api.save(sent);

        OrganisationDTO saved = saved();
        assertEquals(sent.getStatus(), saved.getStatus());
        assertEquals(KycComplianceStatus.CURRENT, saved.getKycStatus());
        assertEquals(true, saved.getIsClient());
    }
}
