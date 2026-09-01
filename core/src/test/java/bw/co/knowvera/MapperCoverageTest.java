package bw.co.knowvera;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import bw.co.knowvera.contact.Contact;
import bw.co.knowvera.contact.ContactDTO;
import bw.co.knowvera.contact.ContactMapper;
import bw.co.knowvera.contact.ContactType;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.DocumentMapper;
import bw.co.knowvera.document.type.DocumentTypeMapper;
import bw.co.knowvera.document.DocumentVerificationStatus;
import bw.co.knowvera.document.type.DocumentType;
import bw.co.knowvera.document.type.DocumentTypeDTO;
import bw.co.knowvera.document.type.field.ExpectedFieldMapper;
import bw.co.knowvera.document.type.field.ExpectedField;
import bw.co.knowvera.document.type.field.ExpectedFieldDTO;
import bw.co.knowvera.document.type.verification.VerificationDataConfig;
import bw.co.knowvera.document.type.verification.VerificationDataConfigDTO;
import bw.co.knowvera.document.type.verification.VerificationDataConfigMapper;
import bw.co.knowvera.document.type.verification.VerificationDataConfigRepository;
import bw.co.knowvera.individual.EmploymentStatus;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualDTO;
import bw.co.knowvera.individual.IndividualIdentityType;
import bw.co.knowvera.individual.IndividualListDTO;
import bw.co.knowvera.individual.IndividualMapper;
import bw.co.knowvera.individual.MaritalStatus;
import bw.co.knowvera.individual.PepStatus;
import bw.co.knowvera.individual.Sex;
import bw.co.knowvera.individual.employment.EmploymentRecord;
import bw.co.knowvera.individual.employment.EmploymentRecordDTO;
import bw.co.knowvera.individual.employment.EmploymentRecordMapper;
import bw.co.knowvera.invoice.KycInvoice;
import bw.co.knowvera.invoice.KycInvoiceDTO;
import bw.co.knowvera.invoice.KycInvoiceMapper;
import bw.co.knowvera.kyc.KycComplianceStatus;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordDTO;
import bw.co.knowvera.kyc.KycRecordMapper;
import bw.co.knowvera.settings.kyc.GroupField;
import bw.co.knowvera.settings.kyc.GroupFieldDTO;
import bw.co.knowvera.settings.kyc.GroupFieldMapper;
import bw.co.knowvera.kyc.fields.GroupFieldValue;
import bw.co.knowvera.kyc.fields.GroupFieldValueDTO;
import bw.co.knowvera.kyc.fields.GroupFieldValueMapper;
import bw.co.knowvera.kyc.fields.KycReportSection;
import bw.co.knowvera.kyc.fields.KycReportSectionDTO;
import bw.co.knowvera.kyc.fields.KycReportSectionMapper;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationDTO;
import bw.co.knowvera.organisation.OrganisationMapper;
import bw.co.knowvera.organisation.branch.Branch;
import bw.co.knowvera.organisation.branch.BranchDTO;
import bw.co.knowvera.organisation.branch.BranchMapper;
import bw.co.knowvera.organisation.client.ClientRequest;
import bw.co.knowvera.organisation.client.ClientRequestDTO;
import bw.co.knowvera.organisation.client.ClientRequestMapper;
import bw.co.knowvera.organisation.client.ClientRequestStatus;
import bw.co.knowvera.organisation.document.OrganisationDocument;
import bw.co.knowvera.organisation.document.OrganisationDocumentDTO;
import bw.co.knowvera.organisation.document.OrganisationDocumentMapper;
import bw.co.knowvera.organisation.document.OrganisationDocumentStatus;
import bw.co.knowvera.sequence.SequencePart;
import bw.co.knowvera.sequence.SequencePartDTO;
import bw.co.knowvera.sequence.SequencePartMapper;
import bw.co.knowvera.sequence.SequencePartType;
import bw.co.knowvera.settings.SalaryRange;
import bw.co.knowvera.settings.SalaryRangeDTO;
import bw.co.knowvera.settings.SalaryRangeMapper;
import bw.co.knowvera.settings.Settings;
import bw.co.knowvera.settings.SettingsDTO;
import bw.co.knowvera.settings.SettingsMapper;
import bw.co.knowvera.settings.ToolSelectorMapper;
import bw.co.knowvera.settings.kyc.KycFieldGroup;
import bw.co.knowvera.settings.kyc.KycFieldGroupDTO;
import bw.co.knowvera.settings.kyc.KycFieldGroupMapper;
import bw.co.knowvera.subscription.KycSubsciptionStatus;
import bw.co.knowvera.subscription.KycSubscription;
import bw.co.knowvera.subscription.KycSubscriptionDTO;
import bw.co.knowvera.subscription.KycSubscriptionMapper;

class MapperCoverageTest {

    private final ContactMapper contactMapper = Mappers.getMapper(ContactMapper.class);
    private final BranchMapper branchMapper = Mappers.getMapper(BranchMapper.class);
    private final OrganisationMapper organisationMapper = Mappers.getMapper(OrganisationMapper.class);
    private final ClientRequestMapper clientRequestMapper = Mappers.getMapper(ClientRequestMapper.class);
    private final OrganisationDocumentMapper organisationDocumentMapper = Mappers.getMapper(OrganisationDocumentMapper.class);
    private final DocumentTypeMapper documentTypeMapper = Mappers.getMapper(DocumentTypeMapper.class);
    private final ExpectedFieldMapper expectedFieldMapper = Mappers.getMapper(ExpectedFieldMapper.class);
    private final VerificationDataConfigMapper verificationDataConfigMapper = Mappers.getMapper(VerificationDataConfigMapper.class);
    private final SequencePartMapper sequencePartMapper = Mappers.getMapper(SequencePartMapper.class);
    private final SalaryRangeMapper salaryRangeMapper = Mappers.getMapper(SalaryRangeMapper.class);
    private final SettingsMapper settingsMapper = Mappers.getMapper(SettingsMapper.class);
    private final KycFieldGroupMapper kycFieldGroupMapper = Mappers.getMapper(KycFieldGroupMapper.class);
    private final GroupFieldMapper groupFieldMapper = Mappers.getMapper(GroupFieldMapper.class);
    private final IndividualMapper individualMapper = Mappers.getMapper(IndividualMapper.class);
    private final EmploymentRecordMapper employmentRecordMapper = Mappers.getMapper(EmploymentRecordMapper.class);
    private final KycInvoiceMapper kycInvoiceMapper = Mappers.getMapper(KycInvoiceMapper.class);
    private final KycSubscriptionMapper kycSubscriptionMapper = Mappers.getMapper(KycSubscriptionMapper.class);
    private final DocumentMapper documentMapper = Mappers.getMapper(DocumentMapper.class);
    private final KycRecordMapper kycRecordMapper = Mappers.getMapper(KycRecordMapper.class);
    private final KycReportSectionMapper kycReportSectionMapper = Mappers.getMapper(KycReportSectionMapper.class);
    private final GroupFieldValueMapper groupFieldValueMapper = Mappers.getMapper(GroupFieldValueMapper.class);
    private final ToolSelectorMapper toolSelectorMapper = Mappers.getMapper(ToolSelectorMapper.class);
    private final VerificationDataConfigRepository verificationDataConfigRepository = mock(VerificationDataConfigRepository.class);

    @BeforeEach
    void wireMappers() {
        when(verificationDataConfigRepository.findAllById(anyCollection())).thenReturn(List.of());

        inject(contactMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());

        inject(branchMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());
        inject(branchMapper, "documentMapper", documentMapper);

        inject(verificationDataConfigMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());
        inject(verificationDataConfigMapper, "expectedFieldMapper", expectedFieldMapper);

        inject(expectedFieldMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());

        inject(documentTypeMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());
        inject(documentTypeMapper, "verificationDataConfigRepository", verificationDataConfigRepository);
        inject(documentTypeMapper, "verificationDataConfigMapper", verificationDataConfigMapper);
        inject(documentTypeMapper, "expectedFieldMapper", expectedFieldMapper);

        inject(sequencePartMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());

        inject(salaryRangeMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());

        inject(groupFieldMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());
        inject(groupFieldMapper, "expectedFieldMapper", expectedFieldMapper);
        inject(groupFieldMapper, "kycFieldGroupMapper", kycFieldGroupMapper);

        inject(kycFieldGroupMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());
        inject(kycFieldGroupMapper, "groupFieldMapper", groupFieldMapper);
        inject(kycFieldGroupMapper, "expectedFieldMapper", expectedFieldMapper);

        inject(documentMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());
        inject(documentMapper, "documentTypeMapper", documentTypeMapper);
        inject(documentMapper, "expectedFieldMapper", expectedFieldMapper);
        inject(documentMapper, "verificationDataConfigMapper", verificationDataConfigMapper);
        inject(documentMapper, "individualRepository", mock(bw.co.knowvera.individual.IndividualRepository.class));
        inject(documentMapper, "organisationRepository", mock(bw.co.knowvera.organisation.OrganisationRepository.class));
        inject(documentMapper, "kycRecordRepository", mock(bw.co.knowvera.kyc.KycRecordRepository.class));

        inject(organisationDocumentMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());
        inject(organisationDocumentMapper, "documentTypeMapper", documentTypeMapper);

        inject(clientRequestMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());

        inject(organisationMapper, "documentMapper", documentMapper);
        inject(organisationMapper, "documentTypeMapper", documentTypeMapper);
        inject(organisationMapper, "branchMapper", branchMapper);
        inject(organisationMapper, "groupFieldMapper", groupFieldMapper);

        inject(settingsMapper, "documentTypeMapper", documentTypeMapper);
        inject(settingsMapper, "documentMapper", documentMapper);
        inject(settingsMapper, "salaryRangeMapper", salaryRangeMapper);
        inject(settingsMapper, "kycFieldGroupMapper", kycFieldGroupMapper);
        inject(settingsMapper, "toolSelectorMapper", toolSelectorMapper);
        inject(settingsMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());

        inject(individualMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());
        inject(individualMapper, "documentMapper", documentMapper);
        inject(individualMapper, "employmentRecordMapper", employmentRecordMapper);
        inject(individualMapper, "organisationMapper", organisationMapper);
        inject(individualMapper, "branchMapper", branchMapper);

        inject(employmentRecordMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());
        inject(employmentRecordMapper, "documentMapper", documentMapper);
        inject(employmentRecordMapper, "individualMapper", individualMapper);
        inject(employmentRecordMapper, "salaryRangeMapper", salaryRangeMapper);

        inject(kycSubscriptionMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());
        inject(kycSubscriptionMapper, "kycInvoiceMapper", kycInvoiceMapper);

        inject(kycInvoiceMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());
        inject(kycInvoiceMapper, "documentMapper", documentMapper);
        inject(kycInvoiceMapper, "kycSubscriptionMapper", kycSubscriptionMapper);

        inject(groupFieldValueMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());
        inject(groupFieldValueMapper, "expectedFieldMapper", expectedFieldMapper);
        inject(groupFieldValueMapper, "kycReportSectionMapper", kycReportSectionMapper);

        inject(kycReportSectionMapper, "groupFieldValueMapper", groupFieldValueMapper);
        inject(kycReportSectionMapper, "kycRecordMapper", kycRecordMapper);

        inject(kycRecordMapper, "mappingUtils", new bw.co.knowvera.utils.MappingUtils());
        inject(kycRecordMapper, "documentMapper", documentMapper);
        inject(kycRecordMapper, "organisationMapper", organisationMapper);
        inject(kycRecordMapper, "individualMapper", individualMapper);
        inject(kycRecordMapper, "employmentRecordMapper", employmentRecordMapper);
        inject(kycRecordMapper, "kycReportSectionMapper", kycReportSectionMapper);
        inject(kycRecordMapper, "groupFieldValueMapper", groupFieldValueMapper);
    }

    private static void inject(Object target, String fieldName, Object value) {
        Class<?> current = target.getClass();
        while (current != null) {
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException ex) {
                current = current.getSuperclass();
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    @Test
    void contactMapperCoversBothDirections() {
        Contact contact = Contact.Factory.newInstance();
        contact.setMessage("Hello");
        contact.setEmail("hello@example.com");
        contact.setType(ContactType.ENQUIRY);

        ContactDTO dto = contactMapper.toContactDTO(contact);
        assertNotNull(dto);

        ContactDTO input = new ContactDTO();
        input.setMessage("Hello");
        input.setEmail("hello@example.com");
        input.setType(ContactType.ENQUIRY);

        Contact entity = contactMapper.contactDTOToEntity(input);
        assertEquals("Hello", entity.getMessage());
        assertEquals("hello@example.com", entity.getEmail());
        assertEquals(ContactType.ENQUIRY, entity.getType());
    }

    @Test
    void branchMapperCoversBothDirections() {
        Organisation organisation = Organisation.Factory.newInstance();
        organisation.setId(UUID.randomUUID());
        organisation.setName("Org One");

        Branch branch = Branch.Factory.newInstance();
        branch.setCode("BR-1");
        branch.setName("Main Branch");
        branch.setOrganisation(organisation);

        BranchDTO dto = branchMapper.toBranchDTO(branch);
        assertNotNull(dto);

        BranchDTO input = new BranchDTO();
        input.setCode("BR-1");
        input.setName("Main Branch");
        input.setOrganisationId(organisation.getId().toString());
        input.setOrganisation(organisation.getName());

        Branch entity = branchMapper.branchDTOToEntity(input);
        assertEquals("BR-1", entity.getCode());
        assertEquals("Main Branch", entity.getName());
    }

    @Test
    void organisationMapperCoversBothDirections() {
        Organisation organisation = Organisation.Factory.newInstance();
        organisation.setCode("ORG-1");
        organisation.setName("Acme");
        organisation.setRegistrationNo("REG-1");
        organisation.setStatus(bw.co.knowvera.GeneralStatus.ACTIVE);
        organisation.setKycStatus(KycComplianceStatus.CURRENT);
        organisation.setCountryOfRegistration("BW");
        organisation.setDomains(new ArrayList<>());

        OrganisationDTO dto = organisationMapper.toOrganisationDTO(organisation);
        assertNotNull(dto);

        OrganisationDTO input = new OrganisationDTO();
        input.setCode("ORG-1");
        input.setName("Acme");
        input.setRegistrationNo("REG-1");
        input.setStatus(bw.co.knowvera.GeneralStatus.ACTIVE);
        input.setKycStatus(KycComplianceStatus.CURRENT);
        input.setCountryOfRegistration("BW");
        input.setDomains(new ArrayList<>());

        Organisation entity = organisationMapper.organisationDTOToEntity(input);
        assertEquals("ORG-1", entity.getCode());
        assertEquals("Acme", entity.getName());
        assertEquals("REG-1", entity.getRegistrationNo());
    }

    @Test
    void clientRequestMapperCoversBothDirections() {
        ClientRequest request = ClientRequest.Factory.newInstance();
        request.setStatus(ClientRequestStatus.PENDING);
        request.setTarget(bw.co.knowvera.TargetEntity.ORGANISATION);
        request.setTargetId(UUID.randomUUID().toString());
        request.setRef("CR-1");
        request.setAccountRequestToken("acc");
        request.setIdentityConfirmationToken("id");
        request.setRegistrationToken("reg");

        ClientRequestDTO dto = clientRequestMapper.toClientRequestDTO(request);
        assertNotNull(dto);

        ClientRequestDTO input = new ClientRequestDTO();
        input.setStatus(ClientRequestStatus.PENDING);
        input.setTarget(bw.co.knowvera.TargetEntity.ORGANISATION);
        input.setTargetId(request.getTargetId());
        input.setRef("CR-1");
        ClientRequest entity = clientRequestMapper.clientRequestDTOToEntity(input);
        assertEquals(ClientRequestStatus.PENDING, entity.getStatus());
        assertEquals(bw.co.knowvera.TargetEntity.ORGANISATION, entity.getTarget());
        assertEquals(request.getTargetId(), entity.getTargetId());
        assertEquals("CR-1", entity.getRef());
    }

    @Test
    void organisationDocumentMapperCoversBothDirections() {
        OrganisationDocument document = OrganisationDocument.Factory.newInstance();
        document.setStatus(OrganisationDocumentStatus.ACTIVE);
        document.setTarget(bw.co.knowvera.TargetEntity.ORGANISATION);
        document.setTargetId(UUID.randomUUID().toString());
        document.setDocumentType(DocumentType.Factory.newInstance());

        OrganisationDocumentDTO dto = organisationDocumentMapper.toOrganisationDocumentDTO(document);
        assertNotNull(dto);

        OrganisationDocumentDTO input = new OrganisationDocumentDTO();
        input.setStatus(OrganisationDocumentStatus.ACTIVE);
        input.setTarget(bw.co.knowvera.TargetEntity.ORGANISATION);
        input.setTargetId(document.getTargetId());
        input.setFileName("doc.pdf");

        OrganisationDocument entity = organisationDocumentMapper.organisationDocumentDTOToEntity(input);
        assertEquals(OrganisationDocumentStatus.ACTIVE, entity.getStatus());
        assertEquals(document.getTargetId(), entity.getTargetId());
    }

    @Test
    void documentTypeMapperCoversBothDirections() {
        DocumentType type = DocumentType.Factory.newInstance();
        type.setCode("PPT");
        type.setName("Passport");

        DocumentTypeDTO dto = documentTypeMapper.toDocumentTypeDTO(type);
        assertNotNull(dto);

        DocumentTypeDTO input = new DocumentTypeDTO();
        input.setCode("PPT");
        input.setName("Passport");

        DocumentType entity = documentTypeMapper.documentTypeDTOToEntity(input);
        assertEquals("PPT", entity.getCode());
        assertEquals("Passport", entity.getName());
    }

    @Test
    void expectedFieldMapperCoversBothDirections() {
        DocumentType type = DocumentType.Factory.newInstance();
        type.setName("Passport");

        ExpectedField field = ExpectedField.Factory.newInstance();
        field.setField("firstName");
        field.setFieldLabel("First Name");
        field.setTargetType(bw.co.knowvera.TargetEntity.INDIVIDUAL);
        field.setFieldType(bw.co.knowvera.document.type.field.ExpectedFieldType.STRING);
        field.setDocumentType(type);

        ExpectedFieldDTO dto = expectedFieldMapper.toExpectedFieldDTO(field);
        assertNotNull(dto);

        ExpectedFieldDTO input = new ExpectedFieldDTO();
        input.setField("firstName");
        input.setFieldLabel("First Name");
        input.setTargetType(bw.co.knowvera.TargetEntity.INDIVIDUAL);
        input.setFieldType(bw.co.knowvera.document.type.field.ExpectedFieldType.STRING);
        input.setDocumentTypeId(type.getId() == null ? null : type.getId().toString());

        ExpectedField entity = expectedFieldMapper.expectedFieldDTOToEntity(input);
        assertEquals("firstName", entity.getField());
        assertEquals("First Name", entity.getFieldLabel());
        assertEquals(bw.co.knowvera.TargetEntity.INDIVIDUAL, entity.getTargetType());
    }

    @Test
    void verificationDataConfigMapperCoversBothDirections() {
        VerificationDataConfig config = VerificationDataConfig.Factory.newInstance();
        config.setName("primary");

        VerificationDataConfigDTO dto = verificationDataConfigMapper.toVerificationDataConfigDTO(config);
        assertNotNull(dto);

        VerificationDataConfigDTO input = new VerificationDataConfigDTO();
        input.setName("primary");

        VerificationDataConfig entity = verificationDataConfigMapper.verificationDataConfigDTOToEntity(input);
        assertEquals("primary", entity.getName());
    }

    @Test
    void sequencePartMapperCoversBothDirections() {
        SequencePart part = SequencePart.Factory.newInstance();
        part.setPosition(1);
        part.setType(SequencePartType.STATIC);
        part.setInitialValue("SEQ-");
        part.setName("prefix");

        SequencePartDTO dto = sequencePartMapper.toSequencePartDTO(part);
        assertNotNull(dto);

        SequencePartDTO input = new SequencePartDTO();
        input.setPosition(1);
        input.setType(SequencePartType.STATIC);
        input.setInitialValue("SEQ-");
        input.setName("prefix");

        SequencePart entity = sequencePartMapper.sequencePartDTOToEntity(input);
        assertEquals(1, entity.getPosition());
        assertEquals(SequencePartType.STATIC, entity.getType());
        assertEquals("SEQ-", entity.getInitialValue());
    }

    @Test
    void salaryRangeMapperCoversBothDirections() {
        SalaryRange salaryRange = SalaryRange.Factory.newInstance();
        salaryRange.setMin(1000.0);
        salaryRange.setMax(5000.0);
        salaryRange.setActive(Boolean.TRUE);

        SalaryRangeDTO dto = salaryRangeMapper.toSalaryRangeDTO(salaryRange);
        assertNotNull(dto);

        SalaryRangeDTO input = new SalaryRangeDTO();
        input.setMin(1000.0);
        input.setMax(5000.0);
        input.setActive(Boolean.TRUE);

        SalaryRange entity = salaryRangeMapper.salaryRangeDTOToEntity(input);
        assertEquals(1000.0, entity.getMin());
        assertEquals(5000.0, entity.getMax());
        assertEquals(Boolean.TRUE, entity.getActive());
    }

    @Test
    void settingsMapperCoversBothDirections() {
        Settings settings = Settings.Factory.newInstance();
        settings.setOrganisationDocuments(new ArrayList<>());
        settings.setIndividualDocuments(new ArrayList<>());
        settings.setIndKycDocuments(new ArrayList<>());
        settings.setOrgKycDocuments(new ArrayList<>());
        settings.setSalaryRanges(new ArrayList<>());
        settings.setIndividualKycFieldGroups(new ArrayList<>());
        settings.setOrganisationKycFieldGroups(new ArrayList<>());

        SettingsDTO dto = settingsMapper.toSettingsDTO(settings);
        assertNotNull(dto);

        SettingsDTO input = new SettingsDTO();
        Settings entity = settingsMapper.settingsDTOToEntity(input);
        assertNotNull(entity);
    }

    @Test
    void kycFieldGroupMapperCoversBothDirections() {
        KycFieldGroup group = KycFieldGroup.Factory.newInstance();
        group.setLabel("Identity");
        group.setDescription("Identity fields");
        group.setTargetType(bw.co.knowvera.TargetEntity.INDIVIDUAL);
        group.setPosition(1);
        group.setGroupFields(new ArrayList<>());

        KycFieldGroupDTO dto = kycFieldGroupMapper.toKycFieldGroupDTO(group);
        assertNotNull(dto);

        KycFieldGroupDTO input = new KycFieldGroupDTO();
        input.setLabel("Identity");
        input.setDescription("Identity fields");
        input.setTargetType(bw.co.knowvera.TargetEntity.INDIVIDUAL);
        input.setPosition(1);

        KycFieldGroup entity = kycFieldGroupMapper.kycFieldGroupDTOToEntity(input);
        assertEquals("Identity", entity.getLabel());
        assertEquals("Identity fields", entity.getDescription());
        assertEquals(bw.co.knowvera.TargetEntity.INDIVIDUAL, entity.getTargetType());
    }

    @Test
    void groupFieldMapperCoversBothDirections() {
        GroupField field = GroupField.Factory.newInstance();
        field.setPosition(1);

        GroupFieldDTO dto = groupFieldMapper.toGroupFieldDTO(field);
        assertNotNull(dto);

        GroupFieldDTO input = new GroupFieldDTO();
        input.setPosition(1);

        GroupField entity = groupFieldMapper.groupFieldDTOToEntity(input);
        assertEquals(1, entity.getPosition());
    }

    @Test
    void individualMapperCoversBothDirections() {
        Individual individual = Individual.Factory.newInstance();
        individual.setFirstName("Jane");
        individual.setSurname("Doe");
        individual.setIdentityNo("ID-1");
        individual.setIdentityType(IndividualIdentityType.OMANG);
        individual.setKycStatus(KycComplianceStatus.CURRENT);
        individual.setSex(Sex.FEMALE);
        individual.setNationality("BW");
        individual.setMaritalStatus(MaritalStatus.SINGLE);
        individual.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        individual.setPepStatus(PepStatus.NOT_PEP);
        individual.setDocuments(new ArrayList<>());
        individual.setEmploymentRecords(new ArrayList<>());

        IndividualDTO dto = individualMapper.toIndividualDTO(individual);
        assertNotNull(dto);
        IndividualListDTO listDto = individualMapper.toIndividualListDTO(individual);
        assertNotNull(listDto);

        IndividualDTO input = new IndividualDTO();
        input.setFirstName("Jane");
        input.setSurname("Doe");
        input.setIdentityNo("ID-1");
        input.setIdentityType(IndividualIdentityType.OMANG);
        input.setKycStatus(KycComplianceStatus.CURRENT);
        input.setSex(Sex.FEMALE);
        input.setNationality("BW");
        input.setMaritalStatus(MaritalStatus.SINGLE);
        input.setEmploymentStatus(EmploymentStatus.EMPLOYED);

        Individual entity = individualMapper.individualDTOToEntity(input);
        assertEquals("Jane", entity.getFirstName());
        assertEquals("Doe", entity.getSurname());
        assertEquals("ID-1", entity.getIdentityNo());
        assertEquals(IndividualIdentityType.OMANG, entity.getIdentityType());
    }

    @Test
    void employmentRecordMapperCoversBothDirections() {
        EmploymentRecord record = EmploymentRecord.Factory.newInstance();
        record.setOccupations(new java.util.TreeSet<>(List.of("developer")));
        record.setEmploymentStart(LocalDate.now());
        record.setEmploymentEnd(LocalDate.now().plusDays(1));
        record.setEmployerId("EMP-1");
        record.setSalaryRange(SalaryRange.Factory.newInstance());
        record.setIndividual(Individual.Factory.newInstance());
        record.setKycRecords(new ArrayList<>());

        EmploymentRecordDTO dto = employmentRecordMapper.toEmploymentRecordDTO(record);
        assertNotNull(dto);

        EmploymentRecordDTO input = new EmploymentRecordDTO();

        EmploymentRecord entity = employmentRecordMapper.employmentRecordDTOToEntity(input);
        assertNotNull(entity);
    }

    @Test
    void kycInvoiceMapperCoversBothDirections() {
        KycInvoice invoice = KycInvoice.Factory.newInstance();
        invoice.setRef("INV-1");
        invoice.setIssueDate(new Date());
        invoice.setAmount(100.0);
        invoice.setVat(14.0);
        invoice.setTotalAmount(114.0);

        KycInvoiceDTO dto = kycInvoiceMapper.toKycInvoiceDTO(invoice);
        assertNotNull(dto);

        KycInvoiceDTO input = new KycInvoiceDTO();
        input.setRef("INV-1");
        input.setIssueDate(new Date());
        input.setAmount(100.0);
        input.setVat(14.0);
        input.setTotalAmount(114.0);

        KycInvoice entity = kycInvoiceMapper.kycInvoiceDTOToEntity(input);
        assertEquals("INV-1", entity.getRef());
        assertEquals(100.0, entity.getAmount());
    }

    @Test
    void kycSubscriptionMapperCoversBothDirections() {
        KycSubscription subscription = KycSubscription.Factory.newInstance();
        subscription.setStartDate(new Date());
        subscription.setEndDate(new Date());
        subscription.setAmount(100.0);
        subscription.setRef("SUB-1");
        subscription.setPeriod(bw.co.knowvera.TimePeriod.MONTH);
        subscription.setStatus(KycSubsciptionStatus.ACTIVE);
        subscription.setOrganisation(Organisation.Factory.newInstance());
        subscription.setKycInvoices(new ArrayList<>());

        KycSubscriptionDTO dto = kycSubscriptionMapper.toKycSubscriptionDTO(subscription);
        assertNotNull(dto);

        KycSubscriptionDTO input = new KycSubscriptionDTO();
        input.setStartDate(new Date());
        input.setEndDate(new Date());
        input.setAmount(100.0);
        input.setRef("SUB-1");
        input.setPeriod(bw.co.knowvera.TimePeriod.MONTH);
        input.setStatus(KycSubsciptionStatus.ACTIVE);
        input.setOrganisationCode("ORG-1");
        input.setOrganisationName("Org One");
        input.setOrganisationRegistrationNo("REG-1");

        KycSubscription entity = kycSubscriptionMapper.kycSubscriptionDTOToEntity(input);
        assertEquals("SUB-1", entity.getRef());
        assertEquals(100.0, entity.getAmount());
    }

    @Test
    void documentMapperCoversBothDirections() {
        DocumentType type = DocumentType.Factory.newInstance();
        type.setName("Passport");

        Document document = Document.Factory.newInstance();
        document.setTarget(bw.co.knowvera.TargetEntity.ORGANISATION);
        document.setTargetId(UUID.randomUUID().toString());
        document.setFileName("doc.pdf");
        document.setVerificationStatus(DocumentVerificationStatus.UNVERIFIED);
        document.setAnalyticsStatus(bw.co.knowvera.document.DocumentAnalyticsStatus.INITIALISED);
        document.setDocumentType(type);
        document.setExpectedInformation(new java.util.HashMap<>());
        document.setExtractedInformation(new java.util.HashMap<>());
        document.setDataVerifications(new ArrayList<>());

        DocumentDTO dto = documentMapper.toDocumentDTO(document);
        assertNotNull(dto);
        DocumentTypeDTO typeDto = documentTypeMapper.toDocumentTypeDTO(type);
        assertNotNull(typeDto);

        DocumentDTO input = new DocumentDTO();
        input.setTarget(bw.co.knowvera.TargetEntity.ORGANISATION);
        input.setTargetId(document.getTargetId());
        input.setFileName("doc.pdf");
        input.setDocumentTypeId(type.getId() == null ? null : type.getId().toString());
        input.setVerificationStatus(DocumentVerificationStatus.UNVERIFIED);
        input.setAnalyticsStatus(bw.co.knowvera.document.DocumentAnalyticsStatus.INITIALISED);

        Document entity = documentMapper.documentDTOToEntity(input);
        assertEquals("doc.pdf", entity.getFileName());
        assertEquals(bw.co.knowvera.TargetEntity.ORGANISATION, entity.getTarget());
    }

    @Test
    void kycRecordMapperCoversBothDirections() {
        KycRecord record = KycRecord.Factory.newInstance();
        record.setTarget(bw.co.knowvera.TargetEntity.INDIVIDUAL);
        record.setTargetId(UUID.randomUUID().toString());
        record.setRef("KR-1");
        record.setKycStatus(KycComplianceStatus.CURRENT);
        record.setDocuments(new ArrayList<>());
        record.setKycReportSections(new ArrayList<>());

        KycRecordDTO dto = kycRecordMapper.toKycRecordDTO(record);
        assertNotNull(dto);

        KycRecordDTO input = new KycRecordDTO();
        input.setTarget(bw.co.knowvera.TargetEntity.INDIVIDUAL);
        input.setTargetId(record.getTargetId());
        input.setRef("KR-1");
        input.setKycStatus(KycComplianceStatus.CURRENT);

        KycRecord entity = kycRecordMapper.kycRecordDTOToEntity(input);
        assertEquals("KR-1", entity.getRef());
        assertEquals(bw.co.knowvera.TargetEntity.INDIVIDUAL, entity.getTarget());
    }

    @Test
    void kycReportSectionMapperCoversBothDirections() {
        KycRecord record = KycRecord.Factory.newInstance();
        record.setRef("KR-2");

        KycReportSection section = KycReportSection.Factory.newInstance();
        section.setLabel("Identity");
        section.setPosition(1);
        section.setKycRecord(record);
        section.setGroupFieldValues(new ArrayList<>());

        KycReportSectionDTO dto = kycReportSectionMapper.toKycReportSectionDTO(section);
        assertNotNull(dto);

        KycReportSectionDTO input = new KycReportSectionDTO();
        input.setLabel("Identity");
        input.setPosition(1);
        input.setKycRecordId(record.getId() == null ? null : record.getId().toString());

        KycReportSection entity = kycReportSectionMapper.kycReportSectionDTOToEntity(input);
        assertEquals("Identity", entity.getLabel());
        assertEquals(1, entity.getPosition());
    }

    @Test
    void groupFieldValueMapperCoversBothDirections() {
        GroupFieldValue value = GroupFieldValue.Factory.newInstance();
        value.setPosition(1);
        value.setData(null);
        value.setKycReportSection(KycReportSection.Factory.newInstance());
        value.setExpectedField(ExpectedField.Factory.newInstance());

        GroupFieldValueDTO dto = groupFieldValueMapper.toGroupFieldValueDTO(value);
        assertNotNull(dto);

        GroupFieldValueDTO input = new GroupFieldValueDTO();
        input.setPosition(1);
        input.setReportSectionId(null);
        input.setFieldId(null);

        GroupFieldValue entity = groupFieldValueMapper.groupFieldValueDTOToEntity(input);
        assertEquals(1, entity.getPosition());
    }
}
