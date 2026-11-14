package bw.co.knowvera.form.submission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.form.Form;
import bw.co.knowvera.form.FormMapper;
import bw.co.knowvera.form.FormRepository;
import bw.co.knowvera.form.FormStatus;
import bw.co.knowvera.form.field.FormField;
import bw.co.knowvera.form.field.FormFieldType;
import bw.co.knowvera.form.section.FormSection;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordRepository;
import bw.co.knowvera.kyc.OwnerDetails;
import bw.co.knowvera.organisation.Organisation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class FormSubmissionServiceImplTest {

    private final FormSubmissionRepository repository = mock(FormSubmissionRepository.class);
    private final FormRepository formRepository = mock(FormRepository.class);
    private final KycRecordRepository kycRecordRepository = mock(KycRecordRepository.class);
    private final FormRequirements formRequirements = mock(FormRequirements.class);
    private final UUID clientId = UUID.randomUUID();
    private KycRecord record;
    private FormSubmissionServiceImpl service;
    private Form form;

    @BeforeEach
    void setUp() {
        service = new FormSubmissionServiceImpl(repository, Mappers.getMapper(FormSubmissionMapper.class),
                formRepository, Mappers.getMapper(FormMapper.class), kycRecordRepository, formRequirements);
        when(repository.save(any(FormSubmission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OwnerDetails owner = new OwnerDetails();
        owner.setName("Kabo Seretse");
        record = new KycRecord();
        record.setId(UUID.randomUUID());
        record.setRef("KYC-0001");
        record.setTarget(TargetEntity.INDIVIDUAL);
        record.setTargetId(clientId.toString());
        record.setOwnerDetails(owner);
        when(kycRecordRepository.findById(record.getId())).thenReturn(Optional.of(record));

        Organisation organisation = new Organisation();
        organisation.setId(UUID.randomUUID());
        organisation.setName("First Capital Bank");
        form = new Form();
        form.setId(UUID.randomUUID());
        form.setCode("SOF-01");
        form.setName("Source of funds declaration");
        form.setTargetType(TargetEntity.INDIVIDUAL);
        form.setStatus(FormStatus.PUBLISHED);
        form.setOrganisation(organisation);
        FormSection section = new FormSection();
        section.setId(UUID.randomUUID());
        section.setSectionId("income");
        section.setLabel("Income");
        section.setForm(form);
        form.getSections().add(section);
        FormField income = field(section, "monthly_income", "Monthly income", FormFieldType.NUMBER, true);
        income.setMinValue(java.math.BigDecimal.ZERO);
        field(section, "sources", "Sources of funds", FormFieldType.MULTI_SELECT, true)
                .setOptions(List.of("Salary", "Business", "Gift"));
        field(section, "remarks", "Remarks", FormFieldType.TEXTAREA, false);
        when(formRepository.findById(form.getId())).thenReturn(Optional.of(form));
    }

    private static FormField field(FormSection section, String key, String label, FormFieldType type, boolean required) {
        FormField field = new FormField();
        field.setId(UUID.randomUUID());
        field.setFieldId(key);
        field.setLabel(label);
        field.setFieldType(type);
        field.setRequired(required);
        field.setSection(section);
        section.getFields().add(field);
        return field;
    }

    private FormAssignmentDTO assignment() {
        FormAssignmentDTO assignment = new FormAssignmentDTO();
        assignment.setFormId(form.getId().toString());
        assignment.setKycRecordId(record.getId().toString());
        assignment.setDueDate(LocalDate.of(2026, 12, 1));
        return assignment;
    }

    /** A stored submission of the form in the given state. */
    private FormSubmission stored(FormSubmissionStatus status) {
        FormSubmission submission = new FormSubmission();
        submission.setId(UUID.randomUUID());
        submission.setForm(form);
        submission.setKycRecord(record);
        submission.setStatus(status);
        when(repository.findById(submission.getId())).thenReturn(Optional.of(submission));
        return submission;
    }

    private static FormFieldValueDTO choices(String fieldId, String... values) {
        FormFieldValueDTO answer = new FormFieldValueDTO(fieldId, null);
        answer.setValues(List.of(values));
        return answer;
    }

    @Test
    void aPublishedFormIsAddedToAKycRecord() {
        FormSubmissionDTO created = service.createSubmission(assignment(), "org.user");

        assertEquals(FormSubmissionStatus.NEW, created.getStatus());
        assertEquals("SOF-01", created.getFormCode());
        assertEquals("First Capital Bank", created.getOrganisation());
        assertEquals(record.getId().toString(), created.getKycRecordId());
        assertEquals("KYC-0001", created.getKycRecordRef());
        assertEquals(clientId.toString(), created.getTargetId());
        assertEquals("Kabo Seretse", created.getClientName());
        assertEquals(LocalDate.of(2026, 12, 1), created.getDueDate());
        assertEquals("org.user", created.getCreatedBy());
    }

    @Test
    void aFormIsAddedOnlyWhenPublishedToAnExistingRecordOfTheRightKindOfClientOnce() {
        form.setStatus(FormStatus.DRAFT);
        assertThrows(FormSubmissionServiceException.class, () -> service.createSubmission(assignment(), "org.user"));
        form.setStatus(FormStatus.PUBLISHED);

        record.setTarget(TargetEntity.ORGANISATION);
        assertThrows(FormSubmissionServiceException.class, () -> service.createSubmission(assignment(), "org.user"));
        record.setTarget(TargetEntity.INDIVIDUAL);

        FormAssignmentDTO unknown = assignment();
        unknown.setKycRecordId(UUID.randomUUID().toString());
        assertTrue(assertThrows(FormSubmissionServiceException.class,
                () -> service.createSubmission(unknown, "org.user")).isNotFound());

        when(repository.existsByFormIdAndKycRecordIdAndStatusNot(form.getId(), record.getId(),
                FormSubmissionStatus.ACCEPTED)).thenReturn(true);
        assertThrows(FormSubmissionServiceException.class, () -> service.createSubmission(assignment(), "org.user"));
        verify(repository, never()).save(any());
    }

    @Test
    void answersAreSavedAsADraftAndCanBeCleared() {
        FormSubmission submission = stored(FormSubmissionStatus.NEW);

        FormSubmissionDTO saved = service.saveValues(submission.getId().toString(), List.of(
                new FormFieldValueDTO("monthly_income", " 2500 "),
                new FormFieldValueDTO("remarks", "Paid monthly")), "client");

        assertEquals(FormSubmissionStatus.DRAFT, saved.getStatus());
        assertEquals(2, saved.getValues().size());
        assertEquals("2500", saved.getValues().get(0).getValue());
        assertEquals("Monthly income", saved.getValues().get(0).getLabel());
        assertNotNull(saved.getForm());
        assertEquals(3, saved.getForm().getSections().get(0).getFields().size());

        // Only the given fields change: the income is replaced, the remark cleared
        saved = service.saveValues(submission.getId().toString(), List.of(
                new FormFieldValueDTO("monthly_income", "3000"),
                new FormFieldValueDTO("remarks", "")), "client");
        assertEquals(1, saved.getValues().size());
        assertEquals("3000", saved.getValues().get(0).getValue());
    }

    @Test
    void anAnswerMustFitItsField() {
        FormSubmission submission = stored(FormSubmissionStatus.DRAFT);
        String id = submission.getId().toString();

        FormSubmissionServiceException e = assertThrows(FormSubmissionServiceException.class,
                () -> service.saveValues(id, List.of(
                        new FormFieldValueDTO("monthly_income", "plenty"),
                        choices("sources", "Salary", "Loan"),
                        new FormFieldValueDTO("shoe_size", "9")), "client"));

        assertTrue(e.getMessage().contains("Monthly income is not a number"));
        assertTrue(e.getMessage().contains("'Loan' is not one of the options"));
        assertTrue(e.getMessage().contains("'shoe_size' is not a field of this form"));
        verify(repository, never()).save(any());
    }

    @Test
    void submittingNeedsEveryRequiredAnswer() {
        FormSubmission submission = stored(FormSubmissionStatus.NEW);
        String id = submission.getId().toString();
        service.saveValues(id, List.of(new FormFieldValueDTO("monthly_income", "2500")), "client");

        FormSubmissionServiceException e = assertThrows(FormSubmissionServiceException.class,
                () -> service.submit(id, "client"));
        assertTrue(e.getMessage().contains("Sources of funds is required"));
        assertEquals(FormSubmissionStatus.DRAFT, submission.getStatus());

        service.saveValues(id, List.of(choices("sources", "Salary", "Gift")), "client");
        FormSubmissionDTO submitted = service.submit(id, "client");

        assertEquals(FormSubmissionStatus.SUBMITTED, submitted.getStatus());
        assertEquals("client", submitted.getSubmittedBy());
        assertNotNull(submitted.getSubmittedAt());
        assertEquals(List.of("Salary", "Gift"), submitted.getValues().get(1).getValues());

        // Submitted answers are closed to the client
        assertThrows(FormSubmissionServiceException.class,
                () -> service.saveValues(id, List.of(new FormFieldValueDTO("remarks", "One more thing")), "client"));
        assertThrows(FormSubmissionServiceException.class, () -> service.submit(id, "client"));
    }

    @Test
    void theOrganisationAcceptsOrReturnsSubmittedAnswers() {
        FormSubmission submission = stored(FormSubmissionStatus.SUBMITTED);
        String id = submission.getId().toString();

        // Returning needs a reason, and only the two decisions exist
        assertThrows(FormSubmissionServiceException.class,
                () -> service.updateStatus(id, FormSubmissionStatus.RETURNED, " ", "org.user"));
        assertThrows(FormSubmissionServiceException.class,
                () -> service.updateStatus(id, FormSubmissionStatus.DRAFT, null, "org.user"));

        FormSubmissionDTO returned = service.updateStatus(id, FormSubmissionStatus.RETURNED,
                "Attach proof of the gift", "org.user");
        assertEquals(FormSubmissionStatus.RETURNED, returned.getStatus());
        assertEquals("Attach proof of the gift", returned.getReviewNote());
        assertEquals("org.user", returned.getReviewedBy());

        // A returned form is the client's again; it is decided on once resubmitted
        assertThrows(FormSubmissionServiceException.class,
                () -> service.updateStatus(id, FormSubmissionStatus.ACCEPTED, null, "org.user"));
        service.saveValues(id, List.of(new FormFieldValueDTO("monthly_income", "2500"),
                choices("sources", "Gift")), "client");
        assertEquals(FormSubmissionStatus.RETURNED, submission.getStatus());
        service.submit(id, "client");
        assertEquals(FormSubmissionStatus.ACCEPTED,
                service.updateStatus(id, FormSubmissionStatus.ACCEPTED, null, "org.user").getStatus());
    }

    @Test
    void theFormsOfAKycRecordIncludeTheOnesItsClientHasToFill() {
        service.findRequired(record.getId().toString(), "client");

        verify(formRequirements).requireFor(record, "client");
        assertTrue(assertThrows(FormSubmissionServiceException.class,
                () -> service.findRequired(UUID.randomUUID().toString(), "client")).isNotFound());
    }

    @Test
    void anUnknownSubmissionIsNotFound() {
        assertTrue(assertThrows(FormSubmissionServiceException.class,
                () -> service.findById(UUID.randomUUID().toString())).isNotFound());
    }
}
