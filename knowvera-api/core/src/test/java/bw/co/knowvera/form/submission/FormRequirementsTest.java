package bw.co.knowvera.form.submission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.form.Form;
import bw.co.knowvera.form.FormRepository;
import bw.co.knowvera.form.FormStatus;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordRepository;
import bw.co.knowvera.messaging.FormRequiredNotification;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.client.ClientRequest;
import bw.co.knowvera.organisation.client.ClientRequestRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.jpa.domain.Specification;

@SuppressWarnings("unchecked")
class FormRequirementsTest {

    private final FormRepository formRepository = mock(FormRepository.class);
    private final FormSubmissionRepository submissionRepository = mock(FormSubmissionRepository.class);
    private final ClientRequestRepository clientRequestRepository = mock(ClientRequestRepository.class);
    private final KycRecordRepository kycRecordRepository = mock(KycRecordRepository.class);
    private final FormRequiredNotification notification = mock(FormRequiredNotification.class);
    private final FormRequirements requirements = new FormRequirements(formRepository, submissionRepository,
            clientRequestRepository, kycRecordRepository, notification);

    private Organisation bank;
    private Form form;
    private KycRecord record;
    private ClientRequest request;

    @BeforeEach
    void setUp() {
        bank = new Organisation();
        bank.setId(UUID.randomUUID());
        form = new Form();
        form.setId(UUID.randomUUID());
        form.setOrganisation(bank);
        form.setTargetType(TargetEntity.INDIVIDUAL);
        form.setStatus(FormStatus.PUBLISHED);
        record = new KycRecord();
        record.setId(UUID.randomUUID());
        record.setTarget(TargetEntity.INDIVIDUAL);
        record.setTargetId(UUID.randomUUID().toString());
        request = new ClientRequest();
        request.setOrganisation(bank);
        request.setTarget(TargetEntity.INDIVIDUAL);
        request.setTargetId(record.getTargetId());
        when(clientRequestRepository.findAll(any(Specification.class))).thenReturn(List.of(request));
        when(formRepository.findByOrganisationIdInAndStatusAndTargetType(Set.of(bank.getId()),
                FormStatus.PUBLISHED, TargetEntity.INDIVIDUAL)).thenReturn(List.of(form));
        when(kycRecordRepository.findAll(any(Specification.class))).thenReturn(List.of(record));
        // As the database does: a saved submission has an id
        when(submissionRepository.save(any(FormSubmission.class))).thenAnswer(invocation -> {
            FormSubmission saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });
    }

    @Test
    void aClientDoingItsKycGetsTheFormsOfTheOrganisationsOnboardingIt() {
        assertEquals(1, requirements.requireFor(record, "client"));

        ArgumentCaptor<FormSubmission> added = ArgumentCaptor.forClass(FormSubmission.class);
        verify(submissionRepository).save(added.capture());
        assertEquals(form, added.getValue().getForm());
        assertEquals(record, added.getValue().getKycRecord());
        assertEquals(FormSubmissionStatus.NEW, added.getValue().getStatus());
        assertEquals("client", added.getValue().getCreatedBy());
        // The client is on its KYC page, where the form shows: no message
        verify(notification, never()).queue(any());
    }

    @Test
    void aFormIsAskedForOncePerKycRecord() {
        when(submissionRepository.existsByFormIdAndKycRecordId(form.getId(), record.getId())).thenReturn(true);

        assertEquals(0, requirements.requireFor(record, "client"));
        assertEquals(0, requirements.requireFrom(form, "org.admin"));
        verify(submissionRepository, never()).save(any());
    }

    @Test
    void aClientNobodyOnboardsHasNoRequiredForms() {
        when(clientRequestRepository.findAll(any(Specification.class))).thenReturn(List.of());

        assertEquals(0, requirements.requireFor(record, "client"));
        assertEquals(0, requirements.requireFrom(form, "org.admin"));
        verify(submissionRepository, never()).save(any());
    }

    @Test
    void aPublishedFormIsAddedToTheKycRecordsItsClientsAlreadyHave() {
        KycRecord second = new KycRecord();
        second.setId(UUID.randomUUID());
        when(kycRecordRepository.findAll(any(Specification.class))).thenReturn(List.of(record, second));

        assertEquals(2, requirements.requireFrom(form, "org.admin"));
        verify(submissionRepository, times(2)).save(any(FormSubmission.class));

        // Each of these clients is told that it has a form to fill
        ArgumentCaptor<java.util.Collection<UUID>> notified = ArgumentCaptor.forClass(java.util.Collection.class);
        verify(notification).queue(notified.capture());
        assertEquals(2, notified.getValue().size());
    }

    @Test
    void onlyAPublishedFormIsRequired() {
        form.setStatus(FormStatus.DRAFT);

        assertEquals(0, requirements.requireFrom(form, "org.admin"));
        verify(submissionRepository, never()).save(any());
    }
}
