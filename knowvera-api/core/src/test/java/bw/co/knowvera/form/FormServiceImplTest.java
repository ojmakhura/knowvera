package bw.co.knowvera.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.form.field.FormField;
import bw.co.knowvera.form.field.FormFieldDTO;
import bw.co.knowvera.form.field.FormFieldRepository;
import bw.co.knowvera.form.field.FormFieldType;
import bw.co.knowvera.form.section.FormSection;
import bw.co.knowvera.form.section.FormSectionDTO;
import bw.co.knowvera.form.submission.FormRequirements;
import bw.co.knowvera.form.submission.FormSubmissionRepository;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class FormServiceImplTest {

    private final FormRepository formRepository = mock(FormRepository.class);
    private final FormFieldRepository formFieldRepository = mock(FormFieldRepository.class);
    private final OrganisationRepository organisationRepository = mock(OrganisationRepository.class);
    private final FormSubmissionRepository formSubmissionRepository = mock(FormSubmissionRepository.class);
    private final FormRequirements formRequirements = mock(FormRequirements.class);
    private final UUID organisationId = UUID.randomUUID();
    private FormServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new FormServiceImpl(formRepository, Mappers.getMapper(FormMapper.class), formFieldRepository,
                organisationRepository, formSubmissionRepository, formRequirements);
        Organisation organisation = new Organisation();
        organisation.setId(organisationId);
        organisation.setName("First Capital Bank");
        when(organisationRepository.findById(organisationId)).thenReturn(Optional.of(organisation));
        when(formRepository.save(any(Form.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(formRepository.findByOrganisationIdAndCodeIgnoreCase(any(), anyString())).thenReturn(Optional.empty());
    }

    private static FormFieldDTO field(String label, FormFieldType type) {
        FormFieldDTO field = new FormFieldDTO();
        field.setLabel(label);
        field.setFieldType(type);
        return field;
    }

    private static FormSectionDTO section(String label, FormFieldDTO... fields) {
        FormSectionDTO section = new FormSectionDTO();
        section.setLabel(label);
        section.setFields(new ArrayList<>(List.of(fields)));
        return section;
    }

    private FormDTO form(FormSectionDTO... sections) {
        FormDTO form = new FormDTO();
        form.setCode("SOF-01");
        form.setName("Source of funds declaration");
        form.setTargetType(TargetEntity.INDIVIDUAL);
        form.setOrganisationId(organisationId.toString());
        form.setSections(new ArrayList<>(List.of(sections)));
        return form;
    }

    /** A stored form with one section holding an "Occupation" and an "Employer" field. */
    private Form stored() {
        Form form = new Form();
        form.setId(UUID.randomUUID());
        form.setCode("SOF-01");
        form.setName("Source of funds declaration");
        form.setTargetType(TargetEntity.INDIVIDUAL);
        form.setOrganisation(organisationRepository.findById(organisationId).orElseThrow());
        FormSection section = new FormSection();
        section.setId(UUID.randomUUID());
        section.setSectionId("employment");
        section.setLabel("Employment");
        section.setForm(form);
        for (String label : List.of("Occupation", "Employer")) {
            FormField field = new FormField();
            field.setId(UUID.randomUUID());
            field.setFieldId(label.toLowerCase());
            field.setLabel(label);
            field.setFieldType(FormFieldType.TEXT);
            field.setSection(section);
            section.getFields().add(field);
        }
        form.getSections().add(section);
        when(formRepository.findById(form.getId())).thenReturn(Optional.of(form));
        return form;
    }

    @Test
    void aNewFormIsADraftWithIdsDerivedFromTheLabels() {
        FormFieldDTO income = field("Monthly income (BWP)", FormFieldType.NUMBER);
        FormFieldDTO source = field("Source of funds", FormFieldType.SELECT);
        source.setOptions(List.of("Salary", " Business ", "Salary", ""));
        FormDTO saved = service.save(form(
                section("Income", income, source),
                section("Income", field("Monthly income (BWP)", FormFieldType.NUMBER))));

        assertEquals(FormStatus.DRAFT, saved.getStatus());
        assertEquals("First Capital Bank", saved.getOrganisation());
        assertEquals(List.of("income", "income_2"), saved.getSections().stream().map(FormSectionDTO::getSectionId).toList());
        List<FormFieldDTO> fields = saved.getSections().get(0).getFields();
        assertEquals("monthly_income_bwp", fields.get(0).getFieldId());
        assertEquals("monthly_income_bwp_2", saved.getSections().get(1).getFields().get(0).getFieldId());
        assertEquals(List.of("Salary", "Business"), fields.get(1).getOptions());
        assertEquals(List.of(0, 1), fields.stream().map(FormFieldDTO::getPosition).toList());
    }

    @Test
    void theDefinitionIsChecked() {
        FormDTO noCode = form();
        noCode.setCode(" ");
        assertThrows(FormServiceException.class, () -> service.save(noCode));

        FormDTO wrongTarget = form();
        wrongTarget.setTargetType(TargetEntity.KYC_RECORD);
        assertThrows(FormServiceException.class, () -> service.save(wrongTarget));

        assertThrows(FormServiceException.class,
                () -> service.save(form(section("Choices", field("Colour", FormFieldType.RADIO)))));

        FormFieldDTO badPattern = field("Omang", FormFieldType.TEXT);
        badPattern.setPattern("[0-9");
        assertThrows(FormServiceException.class, () -> service.save(form(section("Identity", badPattern))));

        FormFieldDTO first = field("Omang", FormFieldType.TEXT);
        first.setFieldId("omang");
        FormFieldDTO second = field("Omang number", FormFieldType.TEXT);
        second.setFieldId("Omang");
        FormServiceException duplicate = assertThrows(FormServiceException.class,
                () -> service.save(form(section("Identity", first, second))));
        assertTrue(duplicate.getMessage().contains("omang"));
        verify(formRepository, never()).save(any());
    }

    @Test
    void aFormWithoutFieldsCannotBePublished() {
        FormDTO empty = form(section("Income"));
        empty.setStatus(FormStatus.PUBLISHED);
        assertThrows(FormServiceException.class, () -> service.save(empty));

        FormDTO filled = form(section("Income", field("Monthly income", FormFieldType.NUMBER)));
        filled.setStatus(FormStatus.PUBLISHED);
        assertEquals(FormStatus.PUBLISHED, service.save(filled).getStatus());
    }

    @Test
    void aPublishedFormReachesTheClientsThatAlreadyHaveAKycRecord() {
        FormDTO draft = form(section("Income", field("Monthly income", FormFieldType.NUMBER)));
        draft.setCreatedBy("org.admin");
        service.save(draft);
        verify(formRequirements, never()).requireFrom(any(), any());

        draft.setStatus(FormStatus.PUBLISHED);
        service.save(draft);
        verify(formRequirements).requireFrom(any(Form.class), org.mockito.ArgumentMatchers.eq("org.admin"));
    }

    @Test
    void theCodeIsUniqueWithinTheOrganisation() {
        Form other = new Form();
        other.setId(UUID.randomUUID());
        when(formRepository.findByOrganisationIdAndCodeIgnoreCase(organisationId, "SOF-01")).thenReturn(Optional.of(other));

        assertThrows(FormServiceException.class,
                () -> service.save(form(section("Income", field("Monthly income", FormFieldType.NUMBER)))));
    }

    @Test
    void savingReplacesTheSectionsAndFieldsOfTheStoredForm() {
        Form stored = stored();
        FormSection employment = stored.getSections().get(0);
        FormField occupation = employment.getFields().get(0);
        FormField employer = employment.getFields().get(1);

        // Occupation is renamed and moved to a new section, Employer is dropped, a field is added
        FormFieldDTO moved = field("Your occupation", FormFieldType.TEXT);
        moved.setId(occupation.getId().toString());
        moved.setFieldId("occupation");
        FormSectionDTO kept = section("Employment", field("Years employed", FormFieldType.NUMBER));
        kept.setId(employment.getId().toString());
        kept.setSectionId("employment");
        FormDTO update = form(section("About you", moved), kept);
        update.setId(stored.getId().toString());

        // The query for answers flushes the session: nothing may have changed by then, or a
        // stored field would point to a section that is not saved yet
        when(formSubmissionRepository.countValuesOfFields(anyCollection())).thenAnswer(invocation -> {
            assertEquals(employment, occupation.getSection());
            assertEquals(List.of(employer.getId()), List.copyOf(invocation.<java.util.Collection<UUID>>getArgument(0)));
            return 0L;
        });

        FormDTO saved = service.save(update);

        assertEquals(List.of("about_you", "employment"), saved.getSections().stream().map(FormSectionDTO::getSectionId).toList());
        assertEquals(occupation.getId().toString(), saved.getSections().get(0).getFields().get(0).getId());
        assertEquals("Your occupation", occupation.getLabel());
        assertEquals("about_you", occupation.getSection().getSectionId());
        assertEquals(List.of("years_employed"), saved.getSections().get(1).getFields().stream().map(FormFieldDTO::getFieldId).toList());
        verify(formFieldRepository).deleteAll(List.of(employer));
    }

    @Test
    void anAnsweredFieldCannotBeRemoved() {
        Form stored = stored();
        when(formSubmissionRepository.countValuesOfFields(anyCollection())).thenReturn(3L);
        FormDTO update = form();
        update.setId(stored.getId().toString());

        FormServiceException e = assertThrows(FormServiceException.class, () -> service.save(update));

        assertTrue(e.getMessage().contains("'Occupation'"));
        assertNull(e.getCause());
        assertEquals(2, stored.getSections().get(0).getFields().size());
        verify(formRepository, never()).save(any());
    }

    @Test
    void aFormCannotMoveToAnotherOrganisationOrBorrowAForeignSection() {
        Form stored = stored();
        FormDTO moved = form();
        moved.setId(stored.getId().toString());
        moved.setOrganisationId(UUID.randomUUID().toString());
        assertThrows(FormServiceException.class, () -> service.save(moved));

        FormSectionDTO foreign = section("Someone else's");
        foreign.setId(UUID.randomUUID().toString());
        FormDTO borrowing = form(foreign);
        borrowing.setId(stored.getId().toString());
        assertThrows(FormServiceException.class, () -> service.save(borrowing));
    }

    @Test
    void aFormClientsWereAskedToFillIsNotRemoved() {
        Form stored = stored();
        when(formSubmissionRepository.existsByFormId(stored.getId())).thenReturn(true);
        assertThrows(FormServiceException.class, () -> service.remove(stored.getId().toString()));
        verify(formRepository, never()).delete(any(Form.class));

        when(formSubmissionRepository.existsByFormId(stored.getId())).thenReturn(false);
        assertTrue(service.remove(stored.getId().toString()));
        verify(formRepository).delete(stored);
    }

    @Test
    void anUnknownFormIsNotFound() {
        FormServiceException e = assertThrows(FormServiceException.class,
                () -> service.findById(UUID.randomUUID().toString()));
        assertTrue(e.isNotFound());
        assertTrue(assertThrows(FormServiceException.class, () -> service.findById("not-an-id")).isNotFound());
    }
}
