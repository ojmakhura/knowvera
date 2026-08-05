package bw.co.knowvera.document;

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

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.SortOrder;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.type.DocumentTypeMapper;
import bw.co.knowvera.document.type.DocumentTypeRepository;
import bw.co.knowvera.document.DocumentMapper;
import bw.co.knowvera.document.DocumentServiceImpl;
import bw.co.knowvera.document.type.DocumentType;
import bw.co.knowvera.document.type.field.ExpectedField;
import bw.co.knowvera.document.type.field.ExpectedFieldType;
import bw.co.knowvera.document.type.verification.VerificationDataConfig;
import bw.co.knowvera.document.type.verification.VerificationDataConfigRepository;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualRepository;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordRepository;
import bw.co.knowvera.matcher.UniversalStringMatcher;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationRepository;
import bw.co.knowvera.organisation.client.ClientRequest;
import bw.co.knowvera.organisation.client.ClientRequestRepository;
import bw.co.knowvera.properties.RabbitProperties;
import bw.co.knowvera.subscription.KycSubscriptionRepository;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.DocumentListDTO;
import bw.co.knowvera.document.DocumentRepository;
import bw.co.knowvera.document.DocumentSearchCriteria;
import bw.co.knowvera.document.DocumentServiceException;
import bw.co.knowvera.document.DocumentVerificationStatus;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

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
    void findByIdReturnsMappedDtoWhenExpectedInfoAlreadyPresent() throws Exception {
        UUID id = UUID.randomUUID();
        Document document = Document.Factory.newInstance();
        document.setTarget(TargetEntity.SUBSCRIPTION);
        document.setTargetId(UUID.randomUUID().toString());
        document.setDocumentType(DocumentType.Factory.newInstance());
        document.setExpectedInformation(new HashMap<>(java.util.Map.of("a", "b")));
        DocumentDTO expected = new DocumentDTO();

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));
        when(documentMapper.toDocumentDTO(document)).thenReturn(expected);

        DocumentDTO actual = service.findById(id.toString());

        assertSame(expected, actual);
        verify(documentRepository, never()).save(document);
    }

    @Test
    void findByIdExtractsExpectedInformationWhenMissing() throws Exception {
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

        DocumentDTO actual = service.findById(id.toString());

        assertSame(mapped, actual);
        assertEquals("ORG-001 Acme Corp", actual.getTargetLabel());
        verify(documentRepository).save(document);
    }

    @Test
    void findByIdThrowsWhenDocumentNotFound() {
        UUID id = UUID.randomUUID();
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> service.findById(id.toString()));
    }

    @Test
    void saveForNewOrganisationDocumentAttachesToOrganisation() throws Exception {
        UUID orgId = UUID.randomUUID();
        DocumentType type = newDocumentTypeWithExpectedFields(newExpectedField("orgCode", "code", TargetEntity.ORGANISATION, true, true));
        Document entity = newDocument(TargetEntity.ORGANISATION, orgId, type);
        entity.setId(null);

        DocumentDTO input = new DocumentDTO();
        input.setTargetId(orgId.toString());
        input.setFileName("org-doc.pdf");
        DocumentDTO mappedBack = new DocumentDTO();
        mappedBack.setTarget(TargetEntity.ORGANISATION);
        mappedBack.setTargetId(orgId.toString());

        Organisation organisation = Organisation.Factory.newInstance();
        organisation.setCode("ORG-12");
        organisation.setName("Org Name");
        organisation.setDocuments(null);

        when(documentMapper.documentDTOToEntity(input)).thenReturn(entity);
        when(organisationRepository.findById(orgId)).thenReturn(Optional.of(organisation));
        when(documentRepository.save(entity)).thenReturn(entity);
        when(documentMapper.toDocumentDTO(entity)).thenReturn(mappedBack);

        DocumentDTO actual = service.save(input);

        assertSame(mappedBack, actual);
        assertEquals(1, organisation.getDocuments().size());
        assertEquals("ORG-12 Org Name", actual.getTargetLabel());
        verify(organisationRepository).save(organisation);
    }

    @Test
    void saveForNewIndividualDocumentAttachesToIndividual() throws Exception {
        UUID individualId = UUID.randomUUID();
        DocumentType type = newDocumentTypeWithExpectedFields(newExpectedField("firstName", "firstName", TargetEntity.INDIVIDUAL, true, true));
        Document entity = newDocument(TargetEntity.INDIVIDUAL, individualId, type);
        entity.setId(null);

        DocumentDTO input = new DocumentDTO();
        input.setTargetId(individualId.toString());
        input.setFileName("ind-doc.pdf");
        DocumentDTO mappedBack = new DocumentDTO();

        Individual individual = Individual.Factory.newInstance();
        individual.setFirstName("Jane");
        individual.setSurname("Doe");
        individual.setDocuments(null);

        when(documentMapper.documentDTOToEntity(input)).thenReturn(entity);
        when(individualRepository.findById(individualId)).thenReturn(Optional.of(individual));
        when(documentRepository.save(entity)).thenReturn(entity);
        when(documentMapper.toDocumentDTO(entity)).thenReturn(mappedBack);

        DocumentDTO actual = service.save(input);

        assertSame(mappedBack, actual);
        assertEquals(1, individual.getDocuments().size());
        verify(individualRepository).save(individual);
    }

    @Test
    void saveForExistingDocumentSkipsTargetAttachment() throws Exception {
        UUID existingId = UUID.randomUUID();
        DocumentType type = newDocumentTypeWithExpectedFields();
        Document entity = newDocument(TargetEntity.SUBSCRIPTION, UUID.randomUUID(), type);
        entity.setId(existingId);

        DocumentDTO input = new DocumentDTO();
        input.setTargetId(UUID.randomUUID().toString());
        input.setFileName("existing-doc.pdf");
        DocumentDTO mappedBack = new DocumentDTO();

        when(documentMapper.documentDTOToEntity(input)).thenReturn(entity);
        when(documentRepository.save(entity)).thenReturn(entity);
        when(documentMapper.toDocumentDTO(entity)).thenReturn(mappedBack);

        DocumentDTO actual = service.save(input);

        assertSame(mappedBack, actual);
        verify(organisationRepository, never()).save(any());
        verify(individualRepository, never()).save(any());
    }

    @Test
    void removeForOrganisationDetachesDocumentThenDeletes() throws Exception {
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

        boolean removed = service.remove(documentId.toString());

        assertTrue(removed);
        assertTrue(organisation.getDocuments().isEmpty());
        verify(organisationRepository).save(organisation);
        verify(documentRepository).delete(document);
    }

    @Test
    void removeForIndividualDetachesDocumentThenDeletes() throws Exception {
        UUID documentId = UUID.randomUUID();
        UUID individualId = UUID.randomUUID();

        Document document = newDocument(TargetEntity.INDIVIDUAL, individualId, newDocumentTypeWithExpectedFields());
        document.setId(documentId);

        Individual individual = Individual.Factory.newInstance();
        individual.setDocuments(new ArrayList<>(List.of(document)));

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(individualRepository.findById(individualId)).thenReturn(Optional.of(individual));

        boolean removed = service.remove(documentId.toString());

        assertTrue(removed);
        assertTrue(individual.getDocuments().isEmpty());
        verify(individualRepository).save(individual);
        verify(documentRepository).delete(document);
    }

    @Test
    void removeForKycRecordDetachesDocumentThenDeletes() throws Exception {
        UUID documentId = UUID.randomUUID();
        UUID recordId = UUID.randomUUID();

        Document document = newDocument(TargetEntity.KYC_RECORD, recordId, newDocumentTypeWithExpectedFields());
        document.setId(documentId);

        KycRecord record = KycRecord.Factory.newInstance();
        record.setDocuments(new ArrayList<>(List.of(document)));

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(kycRecordRepository.findById(recordId)).thenReturn(Optional.of(record));

        boolean removed = service.remove(documentId.toString());

        assertTrue(removed);
        assertTrue(record.getDocuments().isEmpty());
        verify(kycRecordRepository).save(record);
        verify(documentRepository).delete(document);
    }

    @Test
    void removeForClientRequestDeletesDocumentWithoutParentSave() throws Exception {
        UUID documentId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();

        Document document = newDocument(TargetEntity.CLIENT_REQUEST, requestId, newDocumentTypeWithExpectedFields());
        document.setId(documentId);

        ClientRequest request = ClientRequest.Factory.newInstance();

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(clientRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

        boolean removed = service.remove(documentId.toString());

        assertTrue(removed);
        verify(documentRepository).delete(document);
        verify(clientRequestRepository, never()).save(any());
    }

    @Test
    void removeForSubscriptionDeletesDocument() throws Exception {
        UUID documentId = UUID.randomUUID();
        Document document = newDocument(TargetEntity.SUBSCRIPTION, UUID.randomUUID(), newDocumentTypeWithExpectedFields());
        document.setId(documentId);

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));

        boolean removed = service.remove(documentId.toString());

        assertTrue(removed);
        verify(documentRepository).delete(document);
    }

    @Test
    void getAllReturnsMappedListWithNoTargetLookupWhenTargetIsNull() throws Exception {
        List<Document> docs = List.of(Document.Factory.newInstance());
        DocumentListDTO dto = new DocumentListDTO("id", null, null, null, "file", "typeId", "type", null, null);
        List<DocumentListDTO> expected = List.of(dto);

        when(documentRepository.findAll()).thenReturn(docs);
        when(documentMapper.toDocumentListDTOCollection(docs)).thenReturn(expected);

        List<DocumentListDTO> actual = service.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getAllWithPagingMapsEachDocument() throws Exception {
        Document doc = Document.Factory.newInstance();
        Page<Document> page = new PageImpl<>(List.of(doc));
        DocumentListDTO dto = new DocumentListDTO("id", null, null, null, "f.pdf", "t1", "Passport", null, null);

        when(documentRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(documentMapper.toDocumentListDTO(doc)).thenReturn(dto);

        Page<DocumentListDTO> result = service.getAll(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("f.pdf", result.getContent().get(0).fileName());
    }

    @Test
    void findByDocumentTypeReturnsMappedResults() throws Exception {
        List<Document> docs = List.of(Document.Factory.newInstance());
        List<DocumentListDTO> mapped = List.of(new DocumentListDTO("id", null, null, null, "a.pdf", "dt", "Doc", null, null));

        when(documentRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Document>>any(), any(Sort.class))).thenReturn(docs);
        when(documentMapper.toDocumentListDTOCollection(docs)).thenReturn(mapped);

        List<DocumentListDTO> result = service.findByDocumentType(UUID.randomUUID().toString());

        assertEquals(mapped, result);
    }

    @Test
    void uploadThrowsUnsupportedOperationException() {
        assertThrows(DocumentServiceException.class,
                () -> service.upload(TargetEntity.INDIVIDUAL, UUID.randomUUID().toString(), UUID.randomUUID().toString(), "url"));
    }

    @Test
    void findByTargetReturnsMappedResults() throws Exception {
        List<Document> docs = List.of(Document.Factory.newInstance());
        List<DocumentListDTO> mapped = List.of(new DocumentListDTO("id", null, null, null, "doc.pdf", "dt", "Doc", null, null));

        when(documentRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Document>>any(), any(Sort.class))).thenReturn(docs);
        when(documentMapper.toDocumentListDTOCollection(docs)).thenReturn(mapped);

        List<DocumentListDTO> result = service.findByTarget(TargetEntity.INDIVIDUAL, UUID.randomUUID().toString());

        assertEquals(mapped, result);
    }

    @Test
    void searchWithoutPagingUsesSpecificationAndSort() throws Exception {
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

        List<DocumentListDTO> result = service.search(criteria, orderings);

        assertEquals(mapped, result);
    }

    @Test
    void searchWithoutPagingUsesDocumentTypeNameBranchWhenIdMissing() throws Exception {
        DocumentSearchCriteria criteria = new DocumentSearchCriteria();
        criteria.setDocumentType("Passport");

        Set<PropertySearchOrder> orderings = new LinkedHashSet<>(List.of(new PropertySearchOrder("fileName", SortOrder.ASC)));

        List<Document> emptyDocs = List.of();
        when(documentRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Document>>any(), any(Sort.class))).thenReturn(emptyDocs);
        when(documentMapper.toDocumentListDTOCollection(emptyDocs)).thenReturn(List.of());

        List<DocumentListDTO> result = service.search(criteria, orderings);

        assertTrue(result.isEmpty());
    }

    @Test
    void searchWithPagingReturnsMappedPage() throws Exception {
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

        Page<DocumentListDTO> result = service.search(search);

        assertEquals(1, result.getContent().size());
        assertEquals("file.pdf", result.getContent().get(0).fileName());
    }

    @Test
    void updateFileContentUpdatesAndReturnsDto() throws Exception {
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

        DocumentDTO result = service.updateFileContent(documentId.toString(), "updated-content");

        assertSame(dto, result);
        assertEquals("updated-content", document.getFileContent());
        verify(documentRepository).save(document);
    }

    @Test
    void updateFileContentThrowsWhenMissingDocument() {
        UUID id = UUID.randomUUID();
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> service.updateFileContent(id.toString(), "data"));
    }

    @Test
    void verifyDataReturnsImmediatelyWhenNoExtractedInformation() throws Exception {
        UUID id = UUID.randomUUID();
        Document document = newDocument(TargetEntity.SUBSCRIPTION, UUID.randomUUID(), newDocumentTypeWithExpectedFields());
        document.setId(id);
        document.setExtractedInformation(Map.of());
        DocumentDTO dto = new DocumentDTO();

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));
        when(documentMapper.toDocumentDTO(document)).thenReturn(dto);

        DocumentDTO result = service.verifyData(id.toString(), "tester");

        assertSame(dto, result);
        verify(documentRepository, never()).save(any());
    }

    @Test
    void verifyDataSetsManualReviewWhenVerificationContainsUnverified() throws Exception {
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

        service.verifyData(id.toString(), "tester");

        assertEquals(DocumentVerificationStatus.MANUAL_REVIEW, document.getVerificationStatus());
    }

    @Test
    void verifyDataSetsRejectedWhenMandatoryFieldFails() throws Exception {
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

        service.verifyData(id.toString(), "tester");

        assertEquals(DocumentVerificationStatus.REJECTED, document.getVerificationStatus());
    }

    @Test
    void verifyDataSetsVerifiedWhenHighScoreAndNoFailures() throws Exception {
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

        service.verifyData(id.toString(), "tester");

        assertEquals(DocumentVerificationStatus.VERIFIED, document.getVerificationStatus());
    }

    @Test
    void verifyDataThrowsWhenDocumentMissing() {
        UUID id = UUID.randomUUID();
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> service.verifyData(id.toString(), "tester"));
    }

    @Test
    void updateVerificationStatusUpdatesAndPersistsDocument() throws Exception {
        UUID id = UUID.randomUUID();
        Document document = newDocument(TargetEntity.SUBSCRIPTION, UUID.randomUUID(), newDocumentTypeWithExpectedFields());
        document.setId(id);
        DocumentDTO dto = new DocumentDTO();

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));
        when(documentRepository.save(document)).thenReturn(document);
        when(documentMapper.toDocumentDTO(document)).thenReturn(dto);

        DocumentDTO result = service.updateVerificationStatus(id.toString(), DocumentVerificationStatus.REJECTED, "approver");

        assertSame(dto, result);
        assertEquals(DocumentVerificationStatus.REJECTED, document.getVerificationStatus());
    }

    @Test
    void updateVerificationStatusThrowsWhenDocumentMissing() {
        UUID id = UUID.randomUUID();
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DocumentServiceException.class,
                () -> service.updateVerificationStatus(id.toString(), DocumentVerificationStatus.VERIFIED, "approver"));
    }

        @Test
        void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById("\t"));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(" "));
        assertThrows(IllegalArgumentException.class,
            () -> service.search(new DocumentSearchCriteria(), null));
        assertThrows(IllegalArgumentException.class, () -> service.findByDocumentType(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByDocumentType("\n"));
        assertThrows(IllegalArgumentException.class,
            () -> service.upload(null, UUID.randomUUID().toString(), UUID.randomUUID().toString(), "url"));
        assertThrows(IllegalArgumentException.class,
            () -> service.upload(TargetEntity.INDIVIDUAL, null, UUID.randomUUID().toString(), "url"));
        assertThrows(IllegalArgumentException.class,
            () -> service.upload(TargetEntity.INDIVIDUAL, UUID.randomUUID().toString(), null, "url"));
        assertThrows(IllegalArgumentException.class,
            () -> service.upload(TargetEntity.INDIVIDUAL, UUID.randomUUID().toString(), UUID.randomUUID().toString(), "\t"));
        assertThrows(IllegalArgumentException.class,
            () -> service.findByTarget(null, UUID.randomUUID().toString()));
        assertThrows(IllegalArgumentException.class,
            () -> service.findByTarget(TargetEntity.INDIVIDUAL, null));
        assertThrows(IllegalArgumentException.class,
            () -> service.findByTarget(TargetEntity.INDIVIDUAL, " "));
        assertThrows(IllegalArgumentException.class, () -> service.updateFileContent(null, "content"));
        assertThrows(IllegalArgumentException.class,
            () -> service.updateFileContent(UUID.randomUUID().toString(), ""));
        assertThrows(IllegalArgumentException.class, () -> service.verifyData(null, "tester"));
        assertThrows(IllegalArgumentException.class,
            () -> service.verifyData(UUID.randomUUID().toString(), "\n"));
        assertThrows(IllegalArgumentException.class,
            () -> service.updateVerificationStatus(null, DocumentVerificationStatus.REJECTED, "approver"));
        assertThrows(IllegalArgumentException.class,
            () -> service.updateVerificationStatus(UUID.randomUUID().toString(), null, "approver"));
        assertThrows(IllegalArgumentException.class,
            () -> service.updateVerificationStatus(UUID.randomUUID().toString(), DocumentVerificationStatus.REJECTED, " "));
        }

        @Test
        void serviceBaseSaveGuardsRejectMissingRequiredFields() {
        DocumentDTO missingTargetId = validDocument();
        missingTargetId.setTargetId(" ");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingTargetId));

        DocumentDTO missingFileName = validDocument();
        missingFileName.setFileName(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingFileName));
        }

        private static DocumentDTO validDocument() {
        DocumentDTO dto = new DocumentDTO();
        dto.setTargetId(UUID.randomUUID().toString());
        dto.setFileName("doc.pdf");
        return dto;
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