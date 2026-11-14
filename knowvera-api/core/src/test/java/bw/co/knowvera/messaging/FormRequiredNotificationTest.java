package bw.co.knowvera.messaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.form.Form;
import bw.co.knowvera.form.submission.FormSubmission;
import bw.co.knowvera.form.submission.FormSubmissionRepository;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationRepository;
import bw.co.knowvera.settings.Settings;
import bw.co.knowvera.settings.SettingsRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.transaction.support.TransactionOperations;

class FormRequiredNotificationTest {

    private static final Instant NOW = ZonedDateTime.of(2026, 11, 14, 12, 0, 0, 0, ZoneId.systemDefault()).toInstant();

    private final SettingsRepository settingsRepository = mock(SettingsRepository.class);
    private final IndividualRepository individualRepository = mock(IndividualRepository.class);
    private final OrganisationRepository organisationRepository = mock(OrganisationRepository.class);
    private final FormSubmissionRepository submissionRepository = mock(FormSubmissionRepository.class);
    private final ConfirmedPublisher publisher = mock(ConfirmedPublisher.class);
    private FormRequiredNotification notification;

    private final UUID clientId = UUID.randomUUID();
    private FormSubmission submission;
    private Settings settings;

    @BeforeEach
    void setUp() {
        // The background work runs inline, without a transaction
        notification = new FormRequiredNotification(settingsRepository, individualRepository, organisationRepository,
                submissionRepository, publisher, Runnable::run, TransactionOperations.withoutTransaction());

        Organisation bank = new Organisation();
        bank.setName("First Capital Bank");
        Form form = new Form();
        form.setName("Source of funds declaration");
        form.setOrganisation(bank);
        KycRecord record = new KycRecord();
        record.setRef("KYC-0001");
        record.setTarget(TargetEntity.INDIVIDUAL);
        record.setTargetId(clientId.toString());
        submission = new FormSubmission();
        submission.setId(UUID.randomUUID());
        submission.setForm(form);
        submission.setKycRecord(record);
        when(submissionRepository.findById(submission.getId())).thenReturn(Optional.of(submission));

        Individual client = new Individual();
        client.setFirstName("Kabo");
        client.setSurname("Seretse");
        client.setEmailAddress(" kabo@example.co.bw ");
        when(individualRepository.findById(clientId)).thenReturn(Optional.of(client));

        settings = new Settings();
        settings.setKycPortalLink("https://portal.example/");
        settings.setPlatformName("Knowvera BW");
        when(settingsRepository.findAll()).thenReturn(List.of(settings));
    }

    @Test
    void theClientIsToldWhichFormToFillAndWhere() {
        submission.setDueDate(LocalDate.of(2026, 12, 1));

        Map<String, String> payload = notification.buildPayload(submission, settings, NOW);

        assertEquals("kabo@example.co.bw", payload.get("email"));
        assertEquals("Kabo Seretse", payload.get("recipientName"));
        assertEquals("Source of funds declaration", payload.get("formName"));
        assertEquals("First Capital Bank", payload.get("organisationName"));
        assertEquals("KYC-0001", payload.get("reference"));
        assertEquals("1 December 2026", payload.get("dueDate"));
        assertEquals("https://portal.example/forms/" + submission.getId(), payload.get("formUrl"));
        assertEquals("Knowvera BW", payload.get("platformName"));
        assertEquals("our support team", payload.get("supportContact"));
        assertEquals("2026", payload.get("currentYear"));
    }

    @Test
    void anOrganisationClientIsReachedAtItsContactAddress() {
        Organisation client = new Organisation();
        client.setName("Tswana Traders");
        client.setContactEmailAddress("info@tswana.example");
        submission.getKycRecord().setTarget(TargetEntity.ORGANISATION);
        when(organisationRepository.findById(clientId)).thenReturn(Optional.of(client));

        Map<String, String> payload = notification.buildPayload(submission, null, NOW);

        assertEquals("info@tswana.example", payload.get("email"));
        assertEquals("Tswana Traders", payload.get("recipientName"));
        // No settings: no link, default platform name
        assertFalse(payload.containsKey("formUrl"));
        assertEquals("Knowvera", payload.get("platformName"));
    }

    @Test
    void aMessageIsQueuedPerSubmissionThatCanBeDelivered() {
        UUID gone = UUID.randomUUID();
        when(submissionRepository.findById(gone)).thenReturn(Optional.empty());

        notification.queue(List.of(submission.getId(), gone));

        ArgumentCaptor<Object> payload = ArgumentCaptor.forClass(Object.class);
        verify(publisher).publish(any(), any(), payload.capture());
        assertEquals("kabo@example.co.bw", ((Map<?, ?>) payload.getValue()).get("email"));
    }

    @Test
    void aClientWithoutAnEmailAddressGetsNoMessage() {
        when(individualRepository.findById(clientId)).thenReturn(Optional.of(new Individual()));

        assertNull(notification.buildPayload(submission, settings, NOW));
        notification.queue(List.of(submission.getId()));
        notification.queue(List.of());
        verify(publisher, never()).publish(any(), any(), anyMap());
    }
}
