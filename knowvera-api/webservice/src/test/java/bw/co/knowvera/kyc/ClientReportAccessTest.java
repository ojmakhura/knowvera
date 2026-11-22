package bw.co.knowvera.kyc;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.auth.CurrentCaller;
import bw.co.knowvera.auth.KycAuthorisationService;
import bw.co.knowvera.document.DocumentApi;
import bw.co.knowvera.individual.IndividualService;
import bw.co.knowvera.keycloak.KeycloakOrganisationService;
import bw.co.knowvera.keycloak.KeycloakUserService;
import bw.co.knowvera.organisation.client.ClientRequestDTO;
import bw.co.knowvera.organisation.client.ClientRequestService;
import bw.co.knowvera.organisation.client.ClientRequestStatus;

/** An organisation reads its clients' KYC reports, always as it configured them, and nobody else's. */
class ClientReportAccessTest {

    private final KycRecordService service = mock(KycRecordService.class);
    private final CurrentCaller caller = mock(CurrentCaller.class);
    private final ClientRequestService requests = mock(ClientRequestService.class);
    private final KycAuthorisationService authorisation = mock(KycAuthorisationService.class);
    private final KycRecordSummary ownOrganisationsView = new KycRecordSummary();
    private KycRecordApiImpl api;

    @BeforeEach
    void setUp() throws Exception {
        api = new KycRecordApiImpl(service, mock(KeycloakOrganisationService.class), mock(KeycloakUserService.class),
                mock(IndividualService.class), mock(DocumentApi.class), caller, requests, authorisation);

        KycRecordDTO record = new KycRecordDTO();
        record.setTarget(TargetEntity.INDIVIDUAL);
        record.setTargetId("client");
        when(service.findById("r1")).thenReturn(record);
        when(service.findSummaryById("r1", "my-org")).thenReturn(ownOrganisationsView);
        when(authorisation.isKycRecordOwner("r1")).thenReturn(false);
        when(caller.organisationId()).thenReturn(Optional.of("my-org"));

        SecurityContextHolder.getContext()
                .setAuthentication(new TestingAuthenticationToken("member", null, "SCOPE_kyc-records:view-own"));
    }

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    private void requestIs(ClientRequestStatus status) throws Exception {
        ClientRequestDTO request = new ClientRequestDTO();
        request.setStatus(status);
        when(requests.findByTargetAndOrganisation(TargetEntity.INDIVIDUAL, "client", "my-org")).thenReturn(List.of(request));
    }

    @Test
    void aMemberSeesAnAcceptedClientsReportAsTheirOwnOrganisationConfiguredIt() throws Exception {
        requestIs(ClientRequestStatus.ACCEPTED);

        // Asking as another organisation changes nothing: the caller's own is used
        assertSame(ownOrganisationsView, api.findSummaryById("r1", "another-org").getBody());
        verify(service, never()).findSummaryById("r1", "another-org");
    }

    @Test
    void aClientWhoHasNotGivenPermissionYetShowsNothingOfTheirKyc() throws Exception {
        for (ClientRequestStatus status : List.of(ClientRequestStatus.PENDING, ClientRequestStatus.CONTACTED,
                ClientRequestStatus.REJECTED)) {
            requestIs(status);

            assertThrows(AuthorizationDeniedException.class, () -> api.findSummaryById("r1", "my-org"), status.name());
        }
        verify(service, never()).findSummaryById(any(), any());
    }

    @Test
    void someoneWhoIsNotAClientOfTheOrganisationIsRefused() throws Exception {
        when(requests.findByTargetAndOrganisation(any(), any(), any())).thenReturn(List.of());

        assertThrows(AuthorizationDeniedException.class, () -> api.findSummaryById("r1", null));
    }
}
