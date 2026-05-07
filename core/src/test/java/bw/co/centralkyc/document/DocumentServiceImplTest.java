package bw.co.centralkyc.document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import bw.co.centralkyc.PropertySearchOrder;
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.SortOrder;
import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.document.type.DocumentTypeMapper;
import bw.co.centralkyc.document.type.DocumentTypeRepository;
import bw.co.centralkyc.document.type.DocumentType;
import bw.co.centralkyc.document.type.field.ExpectedField;
import bw.co.centralkyc.document.type.field.ExpectedFieldType;
import bw.co.centralkyc.document.type.verification.VerificationDataConfig;
import bw.co.centralkyc.document.type.verification.VerificationDataConfigRepository;
import bw.co.centralkyc.individual.Individual;
import bw.co.centralkyc.individual.IndividualRepository;
import bw.co.centralkyc.kyc.KycRecord;
import bw.co.centralkyc.kyc.KycRecordRepository;
import bw.co.centralkyc.matcher.UniversalStringMatcher;
import bw.co.centralkyc.organisation.Organisation;
import bw.co.centralkyc.organisation.OrganisationRepository;
import bw.co.centralkyc.organisation.client.ClientRequest;
import bw.co.centralkyc.organisation.client.ClientRequestRepository;
import bw.co.centralkyc.properties.RabbitProperties;
import bw.co.centralkyc.subscription.KycSubscriptionRepository;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private DocumentDao documentDao;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private VerificationDataConfigRepository verificationDataConfigRepository;
    @Mock
    private OrganisationRepository organisationRepository;
    @Mock
    private IndividualRepository individualRepository;
    @Mock
    private KycRecordRepository kycRecordRepository;
    @Mock
    private ClientRequestRepository clientRequestRepository;
    @Mock
    private KycSubscriptionRepository kycSubscriptionRepository;
    @Mock
    private DocumentTypeMapper documentTypeMapper;
    @Mock
    private UniversalStringMatcher stringMatcher;
    @Mock
    private RabbitProperties rabbitProperties;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private DocumentMapper documentMapper;
    @Mock
    private MessageSource messageSource;
    @Mock
    private DocumentTypeRepository documentTypeRepository;

    private DocumentServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DocumentServiceImpl(
                documentDao,
                documentRepository,
                verificationDataConfigRepository,
                organisationRepository,
                individualRepository,
                kycRecordRepository,
                clientRequestRepository,
                kycSubscriptionRepository,
                documentTypeMapper,
                stringMatcher,
                rabbitProperties,
                rabbitTemplate,
                documentMapper,
                messageSource,
                documentTypeRepository);
    }

    @Test
    void handleFindByIdReturnsMappedDtoWhenExpectedInfoAlreadyPresent() throws Exception {
        UUID id = UUID.randomUUID();
        Document document = Document.Factory.newInstance();
        document.setTarget(TargetEntity.SUBSCRIPTION);
        document.setTargetId(UUID.randomUUID().toString());
        document.setDocumentType(DocumentType.Factory.newInstance());
        document.setExpectedInformation(new HashMap<>(java.util.Map.of("a", "b")));
        DocumentDTO expected = new DocumentDTO();

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));
        when(documentMapper.toDocumentDTO(document)).thenReturn(expected);

        DocumentDTO actual = service.handleFindById(id.toString());

        assertSame(expected, actual);
        verify(documentRepository, never()).save(document);
    }

    @Test
    void handleFindByIdExtractsExpectedInformationWhenMissing() throws Exception {
        UUID id = UUID.randomUUID();
        UUID orgId = UUID.randomUUID();

        DocumentType type = newDocumentTypeWithExpectedFields(newExpectedField("orgCode", "code", TargetEntity.ORGANISATION, true, true));
        Document document = newDocument(TargetEntity.ORGANISATION, orgId, type);
        document.setExpectedInformation(null);

        Organisation org = Organisation.Factory.newInstance();
        org.setCode("ORG-001");
        org.setName("Acme Corp");

        DocumentDTO mapped = new DocumentDTO();
        mapped.setTarget(TargetEntity.ORGANISATION);
        mapped.setTargetId(orgId.toString());

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));
        when(organisationRepository.findById(orgId)).thenReturn(Optional.of(org));
        when(documentRepository.save(document)).thenReturn(document);
        when(documentMapper.toDocumentDTO(document)).thenReturn(mapped);

        DocumentDTO actual = service.handleFindById(id.toString());

        assertSame(mapped, actual);
        assertEquals("ORG-001 Acme Corp", actual.getTargetLabel());
        verify(documentRepository).save(document);
    }

    @Test
    void handleFindByIdThrowsWhenDocumentNotFound() {
        UUID id = UUID.randomUUID();
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> service.handleFindById(id.toString()));
    }

    @Test
    void handleSaveForNewOrganisationDocumentAttachesToOrganisation() throws Exception {
        UUID orgId = UUID.randomUUID();
        DocumentType type = newDocumentTypeWithExpectedFields(newExpectedField("orgCode", "code", TargetEntity.ORGANISATION, true, true));
        Document entity = newDocument(TargetEntity.ORGANISATION, orgId, type);
        entity.setId(null);

        DocumentDTO input = new DocumentDTO();
        DocumentDTO mappedBack = new DocumentDTO();
        mappedBack.setTarget(TargetEntity.ORGANISATION);
        mappedBack.setTargetId(orgId.toString());

        Organisation organisation = Organisation.Factory.newInstance();
        organisation.setCode("ORG-12");
        organisation.setName("Org Name");
        organisation.setDocuments(null);

        when(documentDao.documentDTOToEntity(input)).thenReturn(entity);
        when(organisationRepository.findById(orgId)).thenReturn(Optional.of(organisation));
        when(documentRepository.save(entity)).thenReturn(entity);
        when(documentMapper.toDocumentDTO(entity)).thenReturn(mappedBack);

        DocumentDTO actual = service.handleSave(input);

        assertSame(mappedBack, actual);
        assertEquals(1, organisation.getDocuments().size());
        assertEquals("ORG-12 Org Name", actual.getTargetLabel());
        verify(organisationRepository).save(organisation);
    }

    @Test
    void handleSaveForNewIndividualDocumentAttachesToIndividual() throws Exception {
        UUID individualId = UUID.randomUUID();
        DocumentType type = newDocumentTypeWithExpectedFields(newExpectedField("firstName", "firstName", TargetEntity.INDIVIDUAL, true, true));
        Document entity = newDocument(TargetEntity.INDIVIDUAL, individualId, type);
        entity.setId(null);

        DocumentDTO input = new DocumentDTO();
        DocumentDTO mappedBack = new DocumentDTO();

        Individual individual = Individual.Factory.newInstance();
        individual.setFirstName("Jane");
        individual.setSurname("Doe");
        individual.setDocuments(null);

        when(documentDao.documentDTOToEntity(input)).thenReturn(entity);
        when(individualRepository.findById(individualId)).thenReturn(Optional.of(individual));
        when(documentRepository.save(entity)).thenReturn(entity);
        when(documentMapper.toDocumentDTO(entity)).thenReturn(mappedBack);

        DocumentDTO actual = service.handleSave(input);

        assertSame(mappedBack, actual);
        assertEquals(1, individual.getDocuments().size());
        verify(individualRepository).save(individual);
    }

    @Test
    void handleSaveForExistingDocumentSkipsTargetAttachment() throws Exception {
        UUID existingId = UUID.randomUUID();
        DocumentType type = newDocumentTypeWithExpectedFields();
        Document entity = newDocument(TargetEntity.SUBSCRIPTION, UUID.randomUUID(), type);
        entity.setId(existingId);

        DocumentDTO input = new DocumentDTO();
        DocumentDTO mappedBack = new DocumentDTO();

        when(documentDao.documentDTOToEntity(input)).thenReturn(entity);
        when(documentRepository.save(entity)).thenReturn(entity);
        when(documentMapper.toDocumentDTO(entity)).thenReturn(mappedBack);

        DocumentDTO actual = service.handleSave(input);

        assertSame(mappedBack, actual);
        verify(organisationRepository, never()).save(any());
        verify(individualRepository, never()).save(any());
    }

    @Test
    void handleRemoveForOrganisationDetachesDocumentThenDeletes() throws Exception {
        UUID documentId = UUID.randomUUID();
        UUID orgId = UUID.randomUUID();

        Document document = Document.Factory.newInstance();
        document.setId(documentId);
        document.setTarget(TargetEntity.ORGANISATION);
        document.setTargetId(orgId.toString());

        Organisation organisation = Organisation.Factory.newInstance();
        organisation.setDocuments(new java.util.ArrayList<>(List.of(document)));

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(organisationRepository.findById(orgId)).thenReturn(Optional.of(organisation));

        boolean removed = service.handleRemove(documentId.toString());

        assertTrue(removed);
        assertTrue(organisation.getDocuments().isEmpty());
        verify(organisationRepository).save(organisation);
        verify(documentRepository).delete(document);
    }

    @Test
    void handleRemoveForIndividualDetachesDocumentThenDeletes() throws Exception {
        UUID documentId = UUID.randomUUID();
        UUID individualId = UUID.randomUUID();

        Document document = newDocument(TargetEntity.INDIVIDUAL, individualId, newDocumentTypeWithExpectedFields());
        document.setId(documentId);

        Individual individual = Individual.Factory.newInstance();
        individual.setDocuments(new ArrayList<>(List.of(document)));

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(individualRepository.findById(individualId)).thenReturn(Optional.of(individual));

        boolean removed = service.handleRemove(documentId.toString());

        assertTrue(removed);
        assertTrue(individual.getDocuments().isEmpty());
        verify(individualRepository).save(individual);
        verify(documentRepository).delete(document);
    }

    @Test
    void handleRemoveForKycRecordDetachesDocumentThenDeletes() throws Exception {
        UUID documentId = UUID.randomUUID();
        UUID recordId = UUID.randomUUID();

        Document document = newDocument(TargetEntity.KYC_RECORD, recordId, newDocumentTypeWithExpectedFields());
        document.setId(documentId);

        KycRecord record = KycRecord.Factory.newInstance();
        record.setDocuments(new ArrayList<>(List.of(document)));

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(kycRecordRepository.findById(recordId)).thenReturn(Optional.of(record));

        boolean removed = service.handleRemove(documentId.toString());

        assertTrue(removed);
        assertTrue(record.getDocuments().isEmpty());
        verify(kycRecordRepository).save(record);
        verify(documentRepository).delete(document);
    }

    @Test
    void handleRemoveForClientRequestDeletesDocumentWithoutParentSave() throws Exception {
        UUID documentId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();

        Document document = newDocument(TargetEntity.CLIENT_REQUEST, requestId, newDocumentTypeWithExpectedFields());
        document.setId(documentId);

        ClientRequest request = ClientRequest.Factory.newInstance();

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(clientRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        boolean removed = service.handleRemove(documentId.toString());

        assertTrue(removed);
        verify(documentRepository).delete(document);
        verify(clientRequestRepository, never()).save(any());
    }

    @Test
    void handleRemoveForSubscriptionDeletesDocument() throws Exception {
        UUID documentId = UUID.randomUUID();
        Document document = newDocument(TargetEntity.SUBSCRIPTION, UUID.randomUUID(), newDocumentTypeWithExpectedFields());
        document.setId(documentId);

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));

        boolean removed = service.handleRemove(documentId.toString());

        assertTrue(removed);
        verify(documentRepository).delete(document);
    }

    @Test
    void handleGetAllReturnsMappedListWithNoTargetLookupWhenTargetIsNull() throws Exception {
        List<Document> docs = List.of(Document.Factory.newInstance());
        DocumentListDTO dto = new DocumentListDTO("id", null, null, null, "file", "typeId", "type", null, null);
        List<DocumentListDTO> expected = List.of(dto);

        when(documentRepository.findAll()).thenReturn(docs);
        when(documentMapper.toDocumentListDTOCollection(docs)).thenReturn(expected);

        List<DocumentListDTO> actual = service.handleGetAll();

        assertEquals(expected, actual);
    }

    @Test
    void handleGetAllWithPagingMapsEachDocument() throws Exception {
        Document doc = Document.Factory.newInstance();
        Page<Document> page = new PageImpl<>(List.of(doc));
        DocumentListDTO dto = new DocumentListDTO("id", null, null, null, "f.pdf", "t1", "Passport", null, null);

        when(documentRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(documentMapper.toDocumentListDTO(doc)).thenReturn(dto);

        Page<DocumentListDTO> result = service.handleGetAll(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("f.pdf", result.getContent().get(0).fileName());
    }

    @Test
    void handleFindByDocumentTypeReturnsMappedResults() throws Exception {
        List<Document> docs = List.of(Document.Factory.newInstance());
        List<DocumentListDTO> mapped = List.of(new DocumentListDTO("id", null, null, null, "a.pdf", "dt", "Doc", null, null));

        when(documentRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Document>>any(), any(Sort.class))).thenReturn(docs);
        when(documentMapper.toDocumentListDTOCollection(docs)).thenReturn(mapped);

        List<DocumentListDTO> result = service.handleFindByDocumentType(UUID.randomUUID().toString());

        assertEquals(mapped, result);
    }

    @Test
    void handleUploadThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class,
                () -> service.handleUpload(TargetEntity.INDIVIDUAL, UUID.randomUUID().toString(), UUID.randomUUID().toString(), "url"));
    }

    @Test
    void handleFindByTargetReturnsMappedResults() throws Exception {
        List<Document> docs = List.of(Document.Factory.newInstance());
        List<DocumentListDTO> mapped = List.of(new DocumentListDTO("id", null, null, null, "doc.pdf", "dt", "Doc", null, null));

        when(documentRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Document>>any(), any(Sort.class))).thenReturn(docs);
        when(documentMapper.toDocumentListDTOCollection(docs)).thenReturn(mapped);

        List<DocumentListDTO> result = service.handleFindByTarget(TargetEntity.INDIVIDUAL, UUID.randomUUID().toString());

        assertEquals(mapped, result);
    }

    @Test
    void handleSearchWithoutPagingUsesSpecificationAndSort() throws Exception {
        DocumentSearchCriteria criteria = new DocumentSearchCriteria();
        criteria.setDocumentTypeId(UUID.randomUUID().toString());
        criteria.setFileName("passport");
        criteria.setTarget(TargetEntity.INDIVIDUAL);
        criteria.setTargetId(UUID.randomUUID().toString());
        criteria.setVerificationStatus(DocumentVerificationStatus.UNVERIFIED);

        Set<PropertySearchOrder> orderings = new LinkedHashSet<>(
                List.of(new PropertySearchOrder("fileName", SortOrder.ASC), new PropertySearchOrder("createdAt", SortOrder.DESC)));

        List<Document> docs = List.of(Document.Factory.newInstance());
        List<DocumentListDTO> mapped = List.of(new DocumentListDTO("id", null, null, null, "doc.pdf", "dt", "Doc", null, null));

        when(documentRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Document>>any(), any(Sort.class))).thenReturn(docs);
        when(documentMapper.toDocumentListDTOCollection(docs)).thenReturn(mapped);

        List<DocumentListDTO> result = service.handleSearch(criteria, orderings);

        assertEquals(mapped, result);
    }

    @Test
    void handleSearchWithoutPagingUsesDocumentTypeNameBranchWhenIdMissing() throws Exception {
        DocumentSearchCriteria criteria = new DocumentSearchCriteria();
        criteria.setDocumentType("Passport");

        Set<PropertySearchOrder> orderings = new LinkedHashSet<>(List.of(new PropertySearchOrder("fileName", SortOrder.ASC)));

        List<Document> emptyDocs = List.of();
        when(documentRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Document>>any(), any(Sort.class))).thenReturn(emptyDocs);
        when(documentMapper.toDocumentListDTOCollection(emptyDocs)).thenReturn(List.of());

        List<DocumentListDTO> result = service.handleSearch(criteria, orderings);

        assertTrue(result.isEmpty());
    }

    @Test
    void handleSearchWithPagingReturnsMappedPage() throws Exception {
        DocumentSearchCriteria criteria = new DocumentSearchCriteria();
        criteria.setDocumentType("Passport");

        SearchObject<DocumentSearchCriteria> search = new SearchObject<>();
        search.setCriteria(criteria);
        search.setPageNumber(0);
        search.setPageSize(5);
        search.setSortings(List.of(new PropertySearchOrder("fileName", SortOrder.ASC)));

        Document doc = Document.Factory.newInstance();
        Page<Document> page = new PageImpl<>(List.of(doc));
        DocumentListDTO dto = new DocumentListDTO("id", null, null, null, "file.pdf", "dt", "Passport", null, null);

        when(documentRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Document>>any(), any(PageRequest.class))).thenReturn(page);
        when(documentMapper.toDocumentListDTO(doc)).thenReturn(dto);

        Page<DocumentListDTO> result = service.handleSearch(search);

        assertEquals(1, result.getContent().size());
        assertEquals("file.pdf", result.getContent().get(0).fileName());
    }

    @Test
    void handleUpdateFileContentUpdatesAndReturnsDto() throws Exception {
        UUID documentId = UUID.randomUUID();
        UUID individualId = UUID.randomUUID();

        DocumentType type = newDocumentTypeWithExpectedFields(newExpectedField("firstName", "firstName", TargetEntity.INDIVIDUAL, true, true));
        Document document = newDocument(TargetEntity.INDIVIDUAL, individualId, type);
        document.setId(documentId);

        Individual individual = Individual.Factory.newInstance();
        individual.setFirstName("John");
        individual.setSurname("Doe");

        DocumentDTO dto = new DocumentDTO();

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(individualRepository.findById(individualId)).thenReturn(Optional.of(individual));
        when(documentRepository.save(document)).thenReturn(document);
        when(documentMapper.toDocumentDTO(document)).thenReturn(dto);

        DocumentDTO result = service.handleUpdateFileContent(documentId.toString(), "updated-content");

        assertSame(dto, result);
        assertEquals("updated-content", document.getFileContent());
        verify(documentRepository).save(document);
    }

    @Test
    void handleUpdateFileContentThrowsWhenMissingDocument() {
        UUID id = UUID.randomUUID();
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> service.handleUpdateFileContent(id.toString(), "data"));
    }

    @Test
    void handleVerifyDataReturnsImmediatelyWhenNoExtractedInformation() throws Exception {
        UUID id = UUID.randomUUID();
        Document document = newDocument(TargetEntity.SUBSCRIPTION, UUID.randomUUID(), newDocumentTypeWithExpectedFields());
        document.setId(id);
        document.setExtractedInformation(Map.of());
        DocumentDTO dto = new DocumentDTO();

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));
        when(documentMapper.toDocumentDTO(document)).thenReturn(dto);

        DocumentDTO result = service.handleVerifyData(id.toString(), "tester");

        assertSame(dto, result);
        verify(documentRepository, never()).save(any());
    }

    @Test
    void handleVerifyDataSetsManualReviewWhenVerificationContainsUnverified() throws Exception {
        UUID id = UUID.randomUUID();
        Document document = verifyReadyDocument(id);

        ExpectedField field = newExpectedField("firstName", "firstName", TargetEntity.INDIVIDUAL, true, false);
        field.setExactMatch(false);
        VerificationDataConfig config = newVerificationConfig(document.getDocumentType(), field);
        document.getDocumentType().setExpectedFields(List.of(field));

        document.setExpectedInformation(Map.of("firstName", "John"));
        document.setExtractedInformation(Map.of("firstName", "John"));

        DocumentDTO dto = new DocumentDTO();

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));
        when(verificationDataConfigRepository.findByDocumentTypeId(document.getDocumentType().getId()))
                .thenReturn(List.of(config));
        when(stringMatcher.calculateFilteredSimilarity(any(), any())).thenReturn(0.9, 0.6);
        when(documentRepository.save(document)).thenReturn(document);
        when(documentMapper.toDocumentDTO(document)).thenReturn(dto);

        service.handleVerifyData(id.toString(), "tester");

        assertEquals(DocumentVerificationStatus.MANUAL_REVIEW, document.getVerificationStatus());
    }

    @Test
    void handleVerifyDataSetsRejectedWhenMandatoryFieldFails() throws Exception {
        UUID id = UUID.randomUUID();
        Document document = verifyReadyDocument(id);

        ExpectedField field = newExpectedField("firstName", "firstName", TargetEntity.INDIVIDUAL, true, true);
        field.setExactMatch(true);
        VerificationDataConfig config = newVerificationConfig(document.getDocumentType(), field);
        document.getDocumentType().setExpectedFields(List.of(field));

        document.setExpectedInformation(Map.of("firstName", "Jane"));
        document.setExtractedInformation(Map.of("firstName", "John"));

        DocumentDTO dto = new DocumentDTO();

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));
        when(verificationDataConfigRepository.findByDocumentTypeId(document.getDocumentType().getId()))
                .thenReturn(List.of(config));
        when(documentRepository.save(document)).thenReturn(document);
        when(documentMapper.toDocumentDTO(document)).thenReturn(dto);

        service.handleVerifyData(id.toString(), "tester");

        assertEquals(DocumentVerificationStatus.REJECTED, document.getVerificationStatus());
    }

    @Test
    void handleVerifyDataSetsVerifiedWhenHighScoreAndNoFailures() throws Exception {
        UUID id = UUID.randomUUID();
        Document document = verifyReadyDocument(id);

        ExpectedField field = newExpectedField("firstName", "firstName", TargetEntity.INDIVIDUAL, true, true);
        field.setExactMatch(true);
        VerificationDataConfig config = newVerificationConfig(document.getDocumentType(), field);
        document.getDocumentType().setExpectedFields(List.of(field));

        document.setExpectedInformation(Map.of("firstName", "John"));
        document.setExtractedInformation(Map.of("firstName", "John"));

        DocumentDTO dto = new DocumentDTO();

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));
        when(verificationDataConfigRepository.findByDocumentTypeId(document.getDocumentType().getId()))
                .thenReturn(List.of(config));
        when(stringMatcher.calculateFilteredSimilarity(any(), any())).thenReturn(0.95);
        when(documentRepository.save(document)).thenReturn(document);
        when(documentMapper.toDocumentDTO(document)).thenReturn(dto);

        service.handleVerifyData(id.toString(), "tester");

        assertEquals(DocumentVerificationStatus.VERIFIED, document.getVerificationStatus());
    }

    @Test
    void handleVerifyDataThrowsWhenDocumentMissing() {
        UUID id = UUID.randomUUID();
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> service.handleVerifyData(id.toString(), "tester"));
    }

    @Test
    void handleUpdateVerificationStatusUpdatesAndPersistsDocument() throws Exception {
        UUID id = UUID.randomUUID();
        Document document = newDocument(TargetEntity.SUBSCRIPTION, UUID.randomUUID(), newDocumentTypeWithExpectedFields());
        document.setId(id);
        DocumentDTO dto = new DocumentDTO();

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));
        when(documentRepository.save(document)).thenReturn(document);
        when(documentMapper.toDocumentDTO(document)).thenReturn(dto);

        DocumentDTO result = service.handleUpdateVerificationStatus(id.toString(), DocumentVerificationStatus.REJECTED, "approver");

        assertSame(dto, result);
        assertEquals(DocumentVerificationStatus.REJECTED, document.getVerificationStatus());
    }

    @Test
    void handleUpdateVerificationStatusThrowsWhenDocumentMissing() {
        UUID id = UUID.randomUUID();
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DocumentServiceException.class,
                () -> service.handleUpdateVerificationStatus(id.toString(), DocumentVerificationStatus.VERIFIED, "approver"));
    }

    private Document verifyReadyDocument(UUID id) {
        UUID targetId = UUID.randomUUID();
        DocumentType type = newDocumentTypeWithExpectedFields();
        type.setId(UUID.randomUUID());

        Document document = newDocument(TargetEntity.SUBSCRIPTION, targetId, type);
        document.setId(id);
        return document;
    }

    private Document newDocument(TargetEntity target, UUID targetId, DocumentType type) {
        Document document = Document.Factory.newInstance();
        document.setTarget(target);
        document.setTargetId(targetId.toString());
        document.setFileName("sample.pdf");
        document.setDocumentType(type);
        return document;
    }

    private DocumentType newDocumentTypeWithExpectedFields(ExpectedField... fields) {
        DocumentType type = DocumentType.Factory.newInstance();
        type.setId(UUID.randomUUID());
        type.setCode("PPT");
        type.setName("Passport");
        type.setExpectedFields(new ArrayList<>(List.of(fields)));
        type.setVerificationDataConfigs(new ArrayList<>());
        return type;
    }

    private ExpectedField newExpectedField(String fieldName, String matchTo, TargetEntity targetType,
            boolean mandatory, boolean exactMatch) {
        ExpectedField field = ExpectedField.Factory.newInstance();
        field.setId(UUID.randomUUID());
        field.setField(fieldName);
        field.setMatchTo(matchTo);
        field.setTargetType(targetType);
        field.setMandatory(mandatory);
        field.setExactMatch(exactMatch);
        field.setFieldType(ExpectedFieldType.STRING);
        return field;
    }

    private VerificationDataConfig newVerificationConfig(DocumentType type, ExpectedField... fields) {
        VerificationDataConfig config = VerificationDataConfig.Factory.newInstance();
        config.setId(UUID.randomUUID());
        config.setName("primary");
        config.setDocumentType(type);
        config.setExpectedFields(new ArrayList<>(List.of(fields)));
        return config;
    }
}