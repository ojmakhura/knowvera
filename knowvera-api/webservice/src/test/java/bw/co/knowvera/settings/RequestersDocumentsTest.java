package bw.co.knowvera.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.auth.CurrentCaller;
import bw.co.knowvera.document.DocumentApi;
import bw.co.knowvera.document.type.DocumentTypeDTO;
import bw.co.knowvera.individual.IndividualDTO;
import bw.co.knowvera.organisation.OrganisationDTO;
import bw.co.knowvera.organisation.OrganisationService;
import bw.co.knowvera.organisation.client.ClientRequestDTO;
import bw.co.knowvera.organisation.client.ClientRequestService;
import bw.co.knowvera.organisation.client.ClientRequestStatus;

/** A client is asked for the documents of the organisations onboarding them, not only the platform's. */
class RequestersDocumentsTest {

    private final SettingsService settings = mock(SettingsService.class);
    private final CurrentCaller caller = mock(CurrentCaller.class);
    private final ClientRequestService requests = mock(ClientRequestService.class);
    private final OrganisationService organisations = mock(OrganisationService.class);
    private SettingsApiImpl api;

    private static DocumentTypeDTO type(String id) {
        DocumentTypeDTO type = new DocumentTypeDTO();
        type.setId(id);
        return type;
    }

    private static ClientRequestDTO request(String organisationId, ClientRequestStatus status) {
        ClientRequestDTO request = new ClientRequestDTO();
        request.setOrganisationId(organisationId);
        request.setStatus(status);
        return request;
    }

    private static List<String> ids(List<DocumentTypeDTO> types) {
        return types.stream().map(DocumentTypeDTO::getId).toList();
    }

    @BeforeEach
    void setUp() throws Exception {
        api = new SettingsApiImpl(settings, mock(DocumentApi.class), caller, requests, organisations);

        DocumentRequirements platform = new DocumentRequirements();
        platform.setIndKycDocuments(new ArrayList<>(List.of(type("bank-statement"))));
        platform.setOrgKycDocuments(new ArrayList<>());
        when(settings.getDocumentRequirements()).thenReturn(platform);

        IndividualDTO individual = new IndividualDTO();
        individual.setId("client");
        when(caller.individual()).thenReturn(Optional.of(individual));
        when(caller.organisationId()).thenReturn(Optional.empty());

        OrganisationDTO requester = new OrganisationDTO();
        requester.setIndividualKycDocuments(List.of(type("passport"), type("bank-statement")));
        when(organisations.findById("requester")).thenReturn(requester);
        OrganisationDTO refused = new OrganisationDTO();
        refused.setIndividualKycDocuments(List.of(type("affidavit")));
        when(organisations.findById("refused")).thenReturn(refused);

        when(requests.findByTarget(TargetEntity.INDIVIDUAL, "client")).thenReturn(List.of(
                request("requester", ClientRequestStatus.ACCEPTED), request("refused", ClientRequestStatus.REJECTED)));
    }

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    private static void signIn(String... authorities) {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("someone", null, authorities));
    }

    @Test
    void aClientIsAskedForTheRequestersDocumentsToo() throws Exception {
        signIn("SCOPE_settings:view");

        DocumentRequirements asked = api.getDocumentRequirements().getBody();

        // The platform's first, each type once, and nothing from a request the client rejected
        assertEquals(List.of("bank-statement", "passport"), ids(asked.getIndKycDocuments()));
        assertEquals(List.of("bank-statement"), ids(settings.getDocumentRequirements().getIndKycDocuments()));
    }

    @Test
    void whoMaintainsThePlatformListsGetsThoseAlone() throws Exception {
        signIn("SCOPE_settings:view", "SCOPE_settings:edit");

        assertEquals(List.of("bank-statement"), ids(api.getDocumentRequirements().getBody().getIndKycDocuments()));
    }
}
