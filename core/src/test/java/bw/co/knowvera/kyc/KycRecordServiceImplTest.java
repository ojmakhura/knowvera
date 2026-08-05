package bw.co.knowvera.kyc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.SourceOfFunds;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.DocumentRepository;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.individual.employment.EmploymentRecord;
import bw.co.knowvera.kyc.KycRecordMapper;
import bw.co.knowvera.kyc.KycRecordServiceImpl;
import bw.co.knowvera.kyc.fields.KycReportSectionRepository;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationRepository;
import bw.co.knowvera.sequence.SequenceGenerator;
import bw.co.knowvera.sequence.SequenceGeneratorRepository;
import bw.co.knowvera.sequence.SequenceGeneratorService;
import bw.co.knowvera.settings.Settings;
import bw.co.knowvera.settings.SettingsRepository;
import bw.co.knowvera.user.UserDTO;
import bw.co.knowvera.kyc.KycComplianceStatus;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordDTO;
import bw.co.knowvera.kyc.KycRecordListDTO;
import bw.co.knowvera.kyc.KycRecordRepository;
import bw.co.knowvera.kyc.KycRecordSearchCriteria;
import bw.co.knowvera.kyc.KycRecordServiceException;
import bw.co.knowvera.kyc.KycRecordSummary;
import bw.co.knowvera.kyc.OwnerDetails;

@ExtendWith(MockitoExtension.class)
class KycRecordServiceImplTest {

    @Mock
    private KycRecordRepository kycRecordRepository;
    @Mock
    private KycRecordMapper kycRecordMapper;
    @Mock
    private MessageSource messageSource;
    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private IndividualRepository individualRepository;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private SequenceGeneratorRepository sequenceGeneratorRepository;
    @Mock
    private SequenceGeneratorService sequenceGeneratorService;
    @Mock
    private KycReportSectionRepository kycReportSectionRepository;
    @Mock
    private OrganisationRepository organisationRepository;

    private KycRecordServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new KycRecordServiceImpl(
                kycRecordRepository,
                kycRecordMapper,
                messageSource,
                settingsRepository,
                kycRecordMapper,
                individualRepository,
                documentRepository,
                sequenceGeneratorRepository,
                sequenceGeneratorService,
                kycReportSectionRepository,
                organisationRepository);
    }

    @Test
    void saveInitializesDatesClearsEmploymentAndGeneratesRef() throws Exception {
        KycRecordDTO input = mock(KycRecordDTO.class);
        KycRecord entity = KycRecord.Factory.newInstance();
        java.util.UUID targetId = java.util.UUID.randomUUID();
        bw.co.knowvera.individual.Individual individual = bw.co.knowvera.individual.Individual.Factory.newInstance();
        individual.setId(targetId);
        OwnerDetails ownerDetails = mock(OwnerDetails.class);

        entity.setKycStatus(KycComplianceStatus.INCOMPLETE);
        entity.setTarget(TargetEntity.INDIVIDUAL);
        entity.setTargetId(targetId.toString());
        entity.setSourceOfFunds(List.of(SourceOfFunds.SALARY));
        entity.setEmploymentRecord(EmploymentRecord.Factory.newInstance());

        Settings settings = Settings.Factory.newInstance();
        settings.setKycDuration(null);
        KycRecordDTO expected = new KycRecordDTO();

        when(settingsRepository.findAll()).thenReturn(List.of(settings));
        when(input.getKycStatus()).thenReturn(KycComplianceStatus.INCOMPLETE);
        when(input.getTargetId()).thenReturn(targetId.toString());
        when(input.getTarget()).thenReturn(TargetEntity.INDIVIDUAL);
        when(input.getSourceOfFunds()).thenReturn(List.of(SourceOfFunds.SALARY));
        when(input.getOwnerDetails()).thenReturn(ownerDetails);
        when(kycRecordMapper.kycRecordDTOToEntity(input)).thenReturn(entity);
        when(sequenceGeneratorRepository.findByName("KYC_RECORD_REF")).thenReturn(Optional.empty());
        when(sequenceGeneratorRepository.save(any(SequenceGenerator.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(sequenceGeneratorService.generateNextSequenceValue("KYC_RECORD_REF", true)).thenReturn("KR-2026/0000001");
        when(individualRepository.findById(targetId)).thenReturn(Optional.of(individual));
        when(individualRepository.save(individual)).thenReturn(individual);
        when(kycRecordRepository.save(entity)).thenReturn(entity);
        when(kycRecordMapper.toKycRecordDTO(entity)).thenReturn(expected);

        KycRecordDTO actual = service.save(input);

        ArgumentCaptor<SequenceGenerator> captor = ArgumentCaptor.forClass(SequenceGenerator.class);
        verify(sequenceGeneratorRepository).save(captor.capture());
        assertEquals(TargetEntity.KYC_RECORD, captor.getValue().getTargetEntity());
        assertNull(entity.getEmploymentRecord());
        assertNotNull(entity.getUploadDate());
        assertEquals(entity.getUploadDate().plusYears(2), entity.getExpiryDate());
        assertEquals("KR-2026/0000001", entity.getRef());
        assertSame(expected, actual);
    }

    @Test
    void findByIdReturnsMappedRecord() throws Exception {
        KycRecord entity = KycRecord.Factory.newInstance();
        entity.setId(java.util.UUID.randomUUID());
        KycRecordDTO expected = new KycRecordDTO();

        when(kycRecordRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(kycRecordMapper.toKycRecordDTO(entity)).thenReturn(expected);

        KycRecordDTO actual = service.findById(entity.getId().toString());

        assertSame(expected, actual);
    }

    @Test
    void removeDeletesByIdWhenRecordExists() throws Exception {
        UUID id = UUID.randomUUID();
        when(kycRecordRepository.existsById(id)).thenReturn(true);

        boolean removed = service.remove(id.toString());

        assertTrue(removed);
        verify(kycRecordRepository).deleteById(id);
    }

    @Test
    void removeThrowsWhenRecordMissing() {
        UUID id = UUID.randomUUID();
        when(kycRecordRepository.existsById(id)).thenReturn(false);

        assertThrows(KycRecordServiceException.class, () -> service.remove(id.toString()));
    }

    @Test
    void getAllMapsRepositoryResults() throws Exception {
        KycRecord record = newKycRecord(TargetEntity.INDIVIDUAL, UUID.randomUUID());
        KycRecordListDTO dto = new KycRecordListDTO("id", "ref", "identity", "name", KycComplianceStatus.CURRENT,
                LocalDate.now());

        when(kycRecordRepository.findAll()).thenReturn(List.of(record));
        when(kycRecordMapper.toKycRecordListDTOCollection(List.of(record))).thenReturn(List.of(dto));

        List<KycRecordListDTO> result = service.getAll();

        assertEquals(1, result.size());
        assertSame(dto, result.get(0));
    }

    @Test
    void searchWithCriteriaReturnsMappedList() throws Exception {
        KycRecord record = newKycRecord(TargetEntity.INDIVIDUAL, UUID.randomUUID());
        KycRecordListDTO dto = new KycRecordListDTO("id", "ref", "identity", "name", KycComplianceStatus.CURRENT,
                LocalDate.now());

        KycRecordSearchCriteria criteria = new KycRecordSearchCriteria();
        criteria.setTarget(TargetEntity.INDIVIDUAL);

        when(kycRecordRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycRecord>>any(), any(Sort.class))).thenReturn(List.of(record));
        when(kycRecordMapper.toKycRecordListDTOCollection(List.of(record))).thenReturn(List.of(dto));

        List<KycRecordListDTO> result = service.search(criteria, Set.<PropertySearchOrder>of());

        assertEquals(1, result.size());
        assertSame(dto, result.get(0));
    }

    @Test
    void getAllWithPagingMapsPageContent() throws Exception {
        KycRecord record = newKycRecord(TargetEntity.INDIVIDUAL, UUID.randomUUID());
        KycRecordListDTO dto = new KycRecordListDTO("id", "ref", "identity", "name", KycComplianceStatus.CURRENT,
                LocalDate.now());
        Page<KycRecord> page = new PageImpl<>(List.of(record));

        when(kycRecordRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(kycRecordMapper.toKycRecordListDTO(record)).thenReturn(dto);

        Page<KycRecordListDTO> result = service.getAll(0, 10);

        assertEquals(1, result.getContent().size());
        assertSame(dto, result.getContent().get(0));
    }

    @Test
    void searchWithPagingMapsOwnerInformation() throws Exception {
        UUID ownerId = UUID.randomUUID();
        KycRecord record = newKycRecord(TargetEntity.INDIVIDUAL, ownerId);
        record.setId(UUID.randomUUID());
        record.setRef("KR-1");
        Page<KycRecord> page = new PageImpl<>(List.of(record));

        Individual individual = Individual.Factory.newInstance();
        individual.setId(ownerId);
        individual.setFirstName("Test");
        individual.setSurname("User");
        individual.setIdentityNo("ID-1");

        SearchObject<KycRecordSearchCriteria> criteria = new SearchObject<>();
        criteria.setCriteria(new KycRecordSearchCriteria());
        criteria.setPageNumber(0);
        criteria.setPageSize(10);

        when(kycRecordRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycRecord>>any(), any(PageRequest.class))).thenReturn(page);
        when(individualRepository.findById(ownerId)).thenReturn(Optional.of(individual));

        Page<KycRecordListDTO> result = service.search(criteria);

        assertEquals(1, result.getContent().size());
        assertEquals("ID-1", result.getContent().get(0).identityNo());
    }

    @Test
    void findByIndividualReturnsOwnerAwareDtos() throws Exception {
        UUID ownerId = UUID.randomUUID();
        KycRecord record = newKycRecord(TargetEntity.INDIVIDUAL, ownerId);
        record.setId(UUID.randomUUID());
        record.setRef("KR-2");

        Individual individual = Individual.Factory.newInstance();
        individual.setId(ownerId);
        individual.setFirstName("First");
        individual.setSurname("Last");
        individual.setIdentityNo("ID-2");

        when(kycRecordRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycRecord>>any())).thenReturn(List.of(record));
        when(individualRepository.findById(ownerId)).thenReturn(Optional.of(individual));

        List<KycRecordListDTO> result = service.findByIndividual(ownerId.toString());

        assertEquals(1, result.size());
        assertEquals("ID-2", result.get(0).identityNo());
    }

    @Test
    void findByIdentityNoLoadsIndividualAndReturnsMappedRecords() throws Exception {
        UUID ownerId = UUID.randomUUID();
        KycRecord record = newKycRecord(TargetEntity.INDIVIDUAL, ownerId);
        record.setId(UUID.randomUUID());
        record.setRef("KR-3");

        Individual individual = Individual.Factory.newInstance();
        individual.setId(ownerId);
        individual.setFirstName("Jane");
        individual.setSurname("Doe");
        individual.setIdentityNo("ID-3");

        when(individualRepository.findByIdentityNo("ID-3")).thenReturn(Optional.of(individual));
        when(kycRecordRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycRecord>>any())).thenReturn(List.of(record));
        when(individualRepository.findById(ownerId)).thenReturn(Optional.of(individual));

        List<KycRecordListDTO> result = service.findByIdentityNo("ID-3");

        assertEquals(1, result.size());
        assertEquals("ID-3", result.get(0).identityNo());
    }

    @Test
    void findByOrganisationReturnsOwnerAwareDtos() throws Exception {
        UUID ownerId = UUID.randomUUID();
        KycRecord record = newKycRecord(TargetEntity.ORGANISATION, ownerId);
        record.setId(UUID.randomUUID());
        record.setRef("KR-4");

        Organisation organisation = Organisation.Factory.newInstance();
        organisation.setId(ownerId);
        organisation.setName("Org");
        organisation.setRegistrationNo("REG-1");

        when(kycRecordRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycRecord>>any())).thenReturn(List.of(record));
        when(organisationRepository.findById(ownerId)).thenReturn(Optional.of(organisation));

        List<KycRecordListDTO> result = service.findByOrganisation(ownerId.toString());

        assertEquals(1, result.size());
        assertEquals("REG-1", result.get(0).identityNo());
    }

    @Test
    void findByIndividualWithPagingMapsOwnerInformation() throws Exception {
        UUID ownerId = UUID.randomUUID();
        KycRecord record = newKycRecord(TargetEntity.INDIVIDUAL, ownerId);
        record.setId(UUID.randomUUID());
        record.setRef("KR-5");

        Individual individual = Individual.Factory.newInstance();
        individual.setId(ownerId);
        individual.setFirstName("Paged");
        individual.setSurname("Owner");
        individual.setIdentityNo("ID-5");

        when(kycRecordRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycRecord>>any(), eq(PageRequest.of(0, 5))))
                .thenReturn(new PageImpl<>(List.of(record)));
        when(individualRepository.findById(ownerId)).thenReturn(Optional.of(individual));

        Page<KycRecordListDTO> result = service.findByIndividual(ownerId.toString(), 0, 5);

        assertEquals(1, result.getContent().size());
        assertEquals("ID-5", result.getContent().get(0).identityNo());
    }

    @Test
    void findByIdentityNoWithPagingDelegatesThroughIndividualLookup() throws Exception {
        UUID ownerId = UUID.randomUUID();
        KycRecord record = newKycRecord(TargetEntity.INDIVIDUAL, ownerId);
        record.setId(UUID.randomUUID());

        Individual individual = Individual.Factory.newInstance();
        individual.setId(ownerId);
        individual.setFirstName("Paged");
        individual.setSurname("Identity");
        individual.setIdentityNo("ID-6");

        when(individualRepository.findByIdentityNo("ID-6")).thenReturn(Optional.of(individual));
        when(kycRecordRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycRecord>>any(), eq(PageRequest.of(0, 3))))
                .thenReturn(new PageImpl<>(List.of(record)));
        when(individualRepository.findById(ownerId)).thenReturn(Optional.of(individual));

        Page<KycRecordListDTO> result = service.findByIdentityNo("ID-6", 0, 3);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void findByOrganisationWithPagingMapsOwnerInformation() throws Exception {
        UUID ownerId = UUID.randomUUID();
        KycRecord record = newKycRecord(TargetEntity.ORGANISATION, ownerId);
        record.setId(UUID.randomUUID());

        Organisation organisation = Organisation.Factory.newInstance();
        organisation.setId(ownerId);
        organisation.setName("Org Paged");
        organisation.setRegistrationNo("REG-2");

        when(kycRecordRepository.findAll(org.mockito.ArgumentMatchers.<Specification<KycRecord>>any(), eq(PageRequest.of(1, 2))))
                .thenReturn(new PageImpl<>(List.of(record)));
        when(organisationRepository.findById(ownerId)).thenReturn(Optional.of(organisation));

        Page<KycRecordListDTO> result = service.findByOrganisation(ownerId.toString(), 1, 2);

        assertEquals(1, result.getContent().size());
        assertEquals("REG-2", result.getContent().get(0).identityNo());
    }

    @Test
    void createTargetRecordBuildsAndPersistsNewRecord() throws Exception {
        UUID ownerId = UUID.randomUUID();
        Settings settings = Settings.Factory.newInstance();
        settings.setKycDuration(30);

        KycRecord saved = newKycRecord(TargetEntity.INDIVIDUAL, ownerId);
        saved.setId(UUID.randomUUID());
        KycRecordDTO expected = new KycRecordDTO();

        Individual individual = Individual.Factory.newInstance();
        individual.setId(ownerId);

        when(settingsRepository.findAll()).thenReturn(List.of(settings));
        when(kycRecordRepository.save(any(KycRecord.class))).thenReturn(saved);
        when(individualRepository.findById(ownerId)).thenReturn(Optional.of(individual));
        when(individualRepository.save(individual)).thenReturn(individual);
        when(kycRecordMapper.toKycRecordDTO(saved)).thenReturn(expected);

        KycRecordDTO result = service.createTargetRecord(ownerId.toString(), TargetEntity.INDIVIDUAL, "maker");

        assertSame(expected, result);
    }

    @Test
    void confirmOwnershipValidatesUserAndOwner() throws Exception {
        UUID recordId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        UserDTO blankUser = mock(UserDTO.class);
        when(blankUser.getUsername()).thenReturn("tester");
        when(blankUser.getEmail()).thenReturn("t@example.com");
        when(blankUser.getFirstName()).thenReturn("T");
        when(blankUser.getLastName()).thenReturn("E");
        when(blankUser.getUserId()).thenReturn("");
        assertFalse(service.confirmOwnership(recordId.toString(), blankUser));

        UserDTO user = mock(UserDTO.class);
        when(user.getUsername()).thenReturn("tester");
        when(user.getEmail()).thenReturn("t@example.com");
        when(user.getFirstName()).thenReturn("T");
        when(user.getLastName()).thenReturn("E");
        when(user.getUserId()).thenReturn("user-1");

        KycRecord record = newKycRecord(TargetEntity.INDIVIDUAL, ownerId);
        record.setId(recordId);

        Individual individual = Individual.Factory.newInstance();
        individual.setId(ownerId);

        when(kycRecordRepository.findById(recordId)).thenReturn(Optional.of(record));
        when(individualRepository.findById(ownerId)).thenReturn(Optional.of(individual));

        assertTrue(service.confirmOwnership(recordId.toString(), user));
    }

    @Test
    void findLatestValidForOwnerReturnsMappedOrNull() throws Exception {
        UUID ownerId = UUID.randomUUID();
        KycRecord record = newKycRecord(TargetEntity.INDIVIDUAL, ownerId);
        record.setId(UUID.randomUUID());
        KycRecordDTO expected = new KycRecordDTO();

        when(kycRecordRepository.findLatestValidForOwner(ownerId.toString(), TargetEntity.INDIVIDUAL, LocalDate.now()))
                .thenReturn(Optional.of(record));
        when(kycRecordMapper.toKycRecordDTO(record)).thenReturn(expected);

        assertSame(expected,
                service.findLatestValidForOwner(ownerId.toString(), TargetEntity.INDIVIDUAL, LocalDate.now()));

        when(kycRecordRepository.findLatestValidForOwner(ownerId.toString(), TargetEntity.INDIVIDUAL, LocalDate.now()))
                .thenReturn(Optional.empty());

        assertNull(service.findLatestValidForOwner(ownerId.toString(), TargetEntity.INDIVIDUAL, LocalDate.now()));
    }

    @Test
    void createNewPersistsDocumentsAfterSavingRecord() throws Exception {
        UUID ownerId = UUID.randomUUID();
        UUID recordId = UUID.randomUUID();

        Document document = Document.Factory.newInstance();
        document.setId(UUID.randomUUID());

        KycRecord entity = newKycRecord(TargetEntity.INDIVIDUAL, ownerId);
        entity.setId(recordId);
        entity.setUploadDate(LocalDate.now());
        entity.setExpiryDate(LocalDate.now().plusYears(1));
        entity.setDocuments(new ArrayList<>(List.of(document)));

        KycRecordDTO input = mock(KycRecordDTO.class);
        KycRecordDTO output = new KycRecordDTO();
        OwnerDetails ownerDetails = mock(OwnerDetails.class);

        Individual individual = Individual.Factory.newInstance();
        individual.setId(ownerId);

        when(input.getKycStatus()).thenReturn(KycComplianceStatus.INCOMPLETE);
        when(input.getTargetId()).thenReturn(ownerId.toString());
        when(input.getTarget()).thenReturn(TargetEntity.INDIVIDUAL);
        when(input.getSourceOfFunds()).thenReturn(List.of(SourceOfFunds.SALARY));
        when(input.getOwnerDetails()).thenReturn(ownerDetails);
        when(kycRecordMapper.kycRecordDTOToEntity(input)).thenReturn(entity);
        when(sequenceGeneratorRepository.findByName("KYC_RECORD_REF")).thenReturn(Optional.empty());
        when(sequenceGeneratorRepository.save(any(SequenceGenerator.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(sequenceGeneratorService.generateNextSequenceValue("KYC_RECORD_REF", true)).thenReturn("KR-NEW");
        when(kycRecordRepository.save(any(KycRecord.class))).thenReturn(entity);
        when(documentRepository.saveAll(any())).thenReturn(List.of(document));
        when(individualRepository.findById(ownerId)).thenReturn(Optional.of(individual));
        when(individualRepository.save(individual)).thenReturn(individual);
        when(kycRecordMapper.toKycRecordDTO(entity)).thenReturn(output);

        KycRecordDTO result = service.createNew(input, "creator");

        assertSame(output, result);
        assertEquals(TargetEntity.KYC_RECORD, document.getTarget());
        assertEquals(recordId.toString(), document.getTargetId());
    }

    @Test
    void removeRecordFileRemovesAssociatedDocument() throws Exception {
        UUID ownerId = UUID.randomUUID();
        UUID recordId = UUID.randomUUID();
        UUID docId = UUID.randomUUID();

        Document document = Document.Factory.newInstance();
        document.setId(docId);
        document.setTarget(TargetEntity.KYC_RECORD);
        document.setTargetId(recordId.toString());

        KycRecord record = newKycRecord(TargetEntity.INDIVIDUAL, ownerId);
        record.setId(recordId);
        record.setDocuments(new ArrayList<>(List.of(document)));

        KycRecordDTO expected = new KycRecordDTO();

        when(documentRepository.findById(docId)).thenReturn(Optional.of(document));
        when(kycRecordRepository.findById(recordId)).thenReturn(Optional.of(record));
        when(kycRecordMapper.toKycRecordDTO(record)).thenReturn(expected);

        KycRecordDTO result = service.removeRecordFile(recordId.toString(), docId.toString());

        assertSame(expected, result);
        assertTrue(record.getDocuments().isEmpty());
    }

    @Test
    void updateRecordFilesAddsMissingDocumentAndPersists() throws Exception {
        UUID ownerId = UUID.randomUUID();
        UUID recordId = UUID.randomUUID();
        UUID docId = UUID.randomUUID();

        KycRecord record = newKycRecord(TargetEntity.INDIVIDUAL, ownerId);
        record.setId(recordId);
        record.setDocuments(new ArrayList<>());

        Document document = Document.Factory.newInstance();
        document.setId(docId);
        document.setTarget(TargetEntity.KYC_RECORD);
        document.setTargetId(recordId.toString());

        DocumentDTO documentDTO = mock(DocumentDTO.class);
        when(documentDTO.getId()).thenReturn(docId.toString());

        Individual individual = Individual.Factory.newInstance();
        individual.setId(ownerId);

        KycRecordDTO expected = new KycRecordDTO();

        when(kycRecordRepository.findById(recordId)).thenReturn(Optional.of(record));
        when(documentRepository.findById(docId)).thenReturn(Optional.of(document));
        when(kycRecordRepository.save(record)).thenReturn(record);
        when(individualRepository.findById(ownerId)).thenReturn(Optional.of(individual));
        when(individualRepository.save(individual)).thenReturn(individual);
        when(kycRecordMapper.toKycRecordDTO(record)).thenReturn(expected);

        KycRecordDTO result = service.updateRecordFiles(recordId.toString(), List.of(documentDTO));

        assertSame(expected, result);
        assertEquals(1, record.getDocuments().size());
    }

    @Test
    void findSummaryByIdPopulatesIdentityAndContactForIndividual() throws Exception {
        UUID ownerId = UUID.randomUUID();
        UUID recordId = UUID.randomUUID();

        KycRecord record = newKycRecord(TargetEntity.INDIVIDUAL, ownerId);
        record.setId(recordId);

        Individual individual = Individual.Factory.newInstance();
        individual.setId(ownerId);
        individual.setFirstName("Summary");
        individual.setSurname("User");
        individual.setIdentityNo("ID-SUM");
        individual.setEmailAddress("summary@example.com");

        KycRecordSummary summary = mock(KycRecordSummary.class);

        when(kycRecordRepository.findById(recordId)).thenReturn(Optional.of(record));
        when(kycRecordMapper.toKycRecordSummary(record)).thenReturn(summary);
        when(individualRepository.findById(ownerId)).thenReturn(Optional.of(individual));

        KycRecordSummary result = service.findSummaryById(recordId.toString());

        assertSame(summary, result);
        verify(summary).setIdentityNo("ID-SUM");
        verify(summary).setName("Summary User");
        verify(summary).setEmailAddress("summary@example.com");
    }

    @Test
    void runVerificationThrowsWhenRecordMissing() {
        UUID id = UUID.randomUUID();
        when(kycRecordRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(KycRecordServiceException.class, () -> service.runVerification(id.toString(), "checker"));
    }

    @Test
    void generateKycReportThrowsWhenSettingsMissing() {
        UUID id = UUID.randomUUID();
        when(settingsRepository.findAll()).thenReturn(List.of());

        assertThrows(KycRecordServiceException.class, () -> service.generateKycReport(id.toString(), "reporter"));
    }

    @Test
    void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById(" "));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove("\t"));
        assertThrows(IllegalArgumentException.class, () -> service.search(null, Set.of()));
        assertThrows(IllegalArgumentException.class, () -> service.search(new KycRecordSearchCriteria(), null));
        assertThrows(IllegalArgumentException.class, () -> service.findByIndividual(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByIndividual("\n"));
        assertThrows(IllegalArgumentException.class, () -> service.findByIdentityNo(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByIdentityNo(" "));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation("\t"));
        assertThrows(IllegalArgumentException.class, () -> service.findByIndividual(null, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.findByIdentityNo("", 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation("\n", 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.createTargetRecord(null, TargetEntity.INDIVIDUAL, "maker"));
        assertThrows(IllegalArgumentException.class, () -> service.createTargetRecord(UUID.randomUUID().toString(), null, "maker"));
        assertThrows(IllegalArgumentException.class,
                () -> service.createTargetRecord(UUID.randomUUID().toString(), TargetEntity.INDIVIDUAL, " "));
        assertThrows(IllegalArgumentException.class, () -> service.confirmOwnership(null, mock(UserDTO.class)));
        assertThrows(IllegalArgumentException.class, () -> service.confirmOwnership(UUID.randomUUID().toString(), null));
        assertThrows(IllegalArgumentException.class,
                () -> service.findLatestValidForOwner(null, TargetEntity.INDIVIDUAL, LocalDate.now()));
        assertThrows(IllegalArgumentException.class,
                () -> service.findLatestValidForOwner(UUID.randomUUID().toString(), null, LocalDate.now()));
        assertThrows(IllegalArgumentException.class,
                () -> service.findLatestValidForOwner(UUID.randomUUID().toString(), TargetEntity.INDIVIDUAL, null));
        assertThrows(IllegalArgumentException.class, () -> service.createNew(null, "creator"));
        assertThrows(IllegalArgumentException.class, () -> service.createNew(validKycRecord(), null));
        assertThrows(IllegalArgumentException.class, () -> service.removeRecordFile(null, UUID.randomUUID().toString()));
        assertThrows(IllegalArgumentException.class, () -> service.removeRecordFile(UUID.randomUUID().toString(), " "));
        assertThrows(IllegalArgumentException.class, () -> service.updateRecordFiles(null, List.of()));
        assertThrows(IllegalArgumentException.class, () -> service.updateRecordFiles(UUID.randomUUID().toString(), null));
        assertThrows(IllegalArgumentException.class, () -> service.findSummaryById(null));
        assertThrows(IllegalArgumentException.class, () -> service.runVerification(null, "checker"));
        assertThrows(IllegalArgumentException.class, () -> service.runVerification(UUID.randomUUID().toString(), " "));
        assertThrows(IllegalArgumentException.class, () -> service.generateKycReport(null, "reporter"));
        assertThrows(IllegalArgumentException.class, () -> service.generateKycReport(UUID.randomUUID().toString(), "\t"));
    }

    @Test
    void serviceBaseSaveAndCreateNewGuardsRejectMissingRequiredFields() {
        KycRecordDTO missingStatus = validKycRecord();
        missingStatus.setKycStatus(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingStatus));

        KycRecordDTO missingTargetId = validKycRecord();
        missingTargetId.setTargetId(" ");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingTargetId));

        KycRecordDTO missingTarget = validKycRecord();
        missingTarget.setTarget(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingTarget));

        KycRecordDTO missingStatusForCreateNew = validKycRecord();
        missingStatusForCreateNew.setKycStatus(null);
        assertThrows(IllegalArgumentException.class, () -> service.createNew(missingStatusForCreateNew, "creator"));
    }

    private KycRecordDTO validKycRecord() {
        KycRecordDTO dto = new KycRecordDTO();
        dto.setKycStatus(KycComplianceStatus.INCOMPLETE);
        dto.setTargetId(UUID.randomUUID().toString());
        dto.setTarget(TargetEntity.INDIVIDUAL);
        dto.setSourceOfFunds(List.of(SourceOfFunds.SALARY));
        dto.setOwnerDetails(mock(OwnerDetails.class));
        return dto;
    }

    private KycRecord newKycRecord(TargetEntity targetEntity, UUID ownerId) {
        KycRecord record = KycRecord.Factory.newInstance();
        record.setTarget(targetEntity);
        record.setTargetId(ownerId.toString());
        record.setDocuments(new ArrayList<>());
        record.setDataVerificationSummaries(new ArrayList<>());
        record.setKycReportSections(new ArrayList<>());
        record.setCreatedAt(LocalDateTime.now());
        record.setUploadDate(LocalDate.now());
        record.setExpiryDate(LocalDate.now().plusDays(1));
        record.setKycStatus(KycComplianceStatus.INCOMPLETE);
        return record;
    }
}