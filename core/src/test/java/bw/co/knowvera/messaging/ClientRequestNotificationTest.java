package bw.co.knowvera.messaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationRepository;
import bw.co.knowvera.organisation.client.ClientRequest;
import bw.co.knowvera.organisation.client.ClientRequestRepository;
import bw.co.knowvera.settings.Settings;
import bw.co.knowvera.settings.SettingsRepository;

@ExtendWith(MockitoExtension.class)
class ClientRequestNotificationTest {

    private static final Instant ISSUED_AT = ZonedDateTime.of(2026, 10, 12, 12, 0, 0, 0, ZoneId.systemDefault())
            .toInstant();

    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private IndividualRepository individualRepository;
    @Mock
    private OrganisationRepository organisationRepository;
    @Mock
    private ClientRequestRepository clientRequestRepository;
    @Mock
    private ConfirmedPublisher publisher;

    private ClientRequestNotification notification;

    @BeforeEach
    void setUp() {
        // Runs the background work inline, without a real transaction
        notification = new ClientRequestNotification(settingsRepository, individualRepository, organisationRepository,
                clientRequestRepository, publisher, Runnable::run, TransactionOperations.withoutTransaction());
        ReflectionTestUtils.setField(notification, "registrationUrl", "https://portal.example/register");
        ReflectionTestUtils.setField(notification, "requestLinkTtl", Duration.ofDays(7));
        ReflectionTestUtils.setField(notification, "novuNewOrgClientRequestQueueExchange", "x.requests");
        ReflectionTestUtils.setField(notification, "novuNewOrgClientRequestQueueRoutingKey", "requests");
    }

    @Test
    void individualRequestPayloadFollowsTheWorkflowContract() {
        UUID individualId = UUID.randomUUID();
        ClientRequest request = request(TargetEntity.INDIVIDUAL, individualId);

        Individual individual = Individual.Factory.newInstance();
        individual.setFirstName("Ann");
        individual.setMiddleName("Mary");
        individual.setSurname("Applicant");
        individual.setEmailAddress("ann@example.com");
        when(individualRepository.findById(individualId)).thenReturn(Optional.of(individual));

        Map<String, String> payload = notification.buildPayload(request, "tok.123", "Example Holdings", settings(),
                ISSUED_AT);

        assertEquals("ann@example.com", payload.get("email"));
        assertEquals("Ann Mary Applicant", payload.get("recipientName"));
        assertEquals("Example Holdings", payload.get("organisationName"));
        assertEquals("https://portal.example/register/" + request.getId() + "?token=tok.123",
                payload.get("kycPortalLink"));
        assertEquals("EXH_KR-00000042", payload.get("reference"));
        assertEquals("19 October 2026", payload.get("linkExpiresAt"));
        assertEquals("Knowvera", payload.get("platformName"));
        assertEquals("https://portal.example", payload.get("platformUrl"));
        assertEquals("support@example.com", payload.get("supportContact"));
        assertEquals("2026", payload.get("currentYear"));
    }

    @Test
    void organisationRequestGoesToTheContactEmail() {
        UUID organisationId = UUID.randomUUID();
        ClientRequest request = request(TargetEntity.ORGANISATION, organisationId);

        Organisation organisation = Organisation.Factory.newInstance();
        organisation.setName("Client Co");
        organisation.setContactEmailAddress("kyc@client.example");
        when(organisationRepository.findById(organisationId)).thenReturn(Optional.of(organisation));

        Map<String, String> payload = notification.buildPayload(request, "tok", "Example Holdings", null, ISSUED_AT);

        assertEquals("kyc@client.example", payload.get("email"));
        assertEquals("Client Co", payload.get("recipientName"));
        assertEquals("Knowvera", payload.get("platformName"), "default when there are no settings");
        assertEquals("our support team", payload.get("supportContact"));
    }

    @Test
    void requestWithoutRecipientEmailIsSkipped() {
        UUID individualId = UUID.randomUUID();
        ClientRequest request = request(TargetEntity.INDIVIDUAL, individualId);
        Individual individual = Individual.Factory.newInstance();
        individual.setFirstName("Ann");
        when(individualRepository.findById(individualId)).thenReturn(Optional.of(individual));
        when(settingsRepository.findAll()).thenReturn(List.of(settings()));

        assertNull(notification.buildPayload(request, "tok", "Org", null, ISSUED_AT));
        when(clientRequestRepository.findById(request.getId())).thenReturn(Optional.of(request));

        notification.queueEmailNotificationsForRequests(List.of(request),
                Map.of(individualId.toString(), "tok"), "Org");
        verify(publisher, never()).publish(anyString(), anyString(), any());
    }

    @Test
    void queuesOnlyRequestsWithANewToken() {
        UUID individualId = UUID.randomUUID();
        ClientRequest invited = request(TargetEntity.INDIVIDUAL, individualId);
        ClientRequest alreadyInvited = request(TargetEntity.INDIVIDUAL, UUID.randomUUID());

        Individual individual = Individual.Factory.newInstance();
        individual.setFirstName("Ann");
        individual.setSurname("Applicant");
        individual.setEmailAddress("ann@example.com");
        when(individualRepository.findById(individualId)).thenReturn(Optional.of(individual));
        when(settingsRepository.findAll()).thenReturn(List.of());
        when(clientRequestRepository.findById(invited.getId())).thenReturn(Optional.of(invited));

        notification.queueEmailNotificationsForRequests(List.of(invited, alreadyInvited),
                Map.of(individualId.toString(), "tok"), "Org");

        verify(publisher).publish(eq("x.requests"), eq("requests"), any());
        verify(clientRequestRepository, never()).findById(alreadyInvited.getId());
    }

    @Test
    void waitsForTheCallersCommitAndReloadsTheRequest() {
        UUID individualId = UUID.randomUUID();
        ClientRequest request = request(TargetEntity.INDIVIDUAL, individualId);
        Individual individual = Individual.Factory.newInstance();
        individual.setFirstName("Ann");
        individual.setEmailAddress("ann@example.com");
        when(individualRepository.findById(individualId)).thenReturn(Optional.of(individual));
        when(settingsRepository.findAll()).thenReturn(List.of());
        when(clientRequestRepository.findById(request.getId())).thenReturn(Optional.of(request));

        TransactionSynchronizationManager.initSynchronization();
        try {
            notification.queueEmailNotificationsForRequests(List.of(request),
                    Map.of(individualId.toString(), "tok"), "Org");

            // Inside the caller's transaction nothing is read or sent yet
            verify(clientRequestRepository, never()).findById(any());
            verify(publisher, never()).publish(anyString(), anyString(), any());

            TransactionSynchronizationManager.getSynchronizations().forEach(TransactionSynchronization::afterCommit);
        } finally {
            TransactionSynchronizationManager.clearSynchronization();
        }

        verify(clientRequestRepository).findById(request.getId());
        verify(publisher).publish(eq("x.requests"), eq("requests"), any());
    }

    @Test
    void deletedRequestGetsNoInvitation() {
        ClientRequest request = request(TargetEntity.INDIVIDUAL, UUID.randomUUID());
        when(settingsRepository.findAll()).thenReturn(List.of());
        when(clientRequestRepository.findById(request.getId())).thenReturn(Optional.empty());

        notification.queueEmailNotificationsForRequests(List.of(request), Map.of(request.getTargetId(), "tok"), "Org");

        verify(publisher, never()).publish(anyString(), anyString(), any());
    }

    private static ClientRequest request(TargetEntity target, UUID targetId) {
        ClientRequest request = ClientRequest.Factory.newInstance();
        request.setId(UUID.randomUUID());
        request.setTarget(target);
        request.setTargetId(targetId.toString());
        request.setRef("EXH_KR-00000042");
        return request;
    }

    private static Settings settings() {
        Settings settings = Settings.Factory.newInstance();
        settings.setPlatformName("Knowvera");
        settings.setKycPortalLink("https://portal.example");
        settings.setSupportContact("support@example.com");
        return settings;
    }
}
