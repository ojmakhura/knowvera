// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::document::DocumentService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.document;

import bw.co.centralkyc.KeyField;
import bw.co.centralkyc.PropertySearchOrder;
import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.SortOrder;
import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.document.type.DocumentType;
import bw.co.centralkyc.document.type.DocumentTypeDTO;
import bw.co.centralkyc.document.type.DocumentTypeMapper;
import bw.co.centralkyc.document.type.DocumentTypeRepository;
import bw.co.centralkyc.document.type.field.ExpectedField;
import bw.co.centralkyc.document.type.field.ExpectedFieldDTO;
import bw.co.centralkyc.document.type.verification.VerificationDataConfig;
import bw.co.centralkyc.individual.Individual;
import bw.co.centralkyc.individual.IndividualRepository;
import bw.co.centralkyc.kyc.KycRecord;
import bw.co.centralkyc.kyc.KycRecordRepository;
import bw.co.centralkyc.matcher.UniversalStringMatcher;
import bw.co.centralkyc.organisation.Organisation;
import bw.co.centralkyc.organisation.OrganisationRepository;
import bw.co.centralkyc.organisation.client.ClientRequestRepository;
import bw.co.centralkyc.properties.RabbitProperties;
import bw.co.centralkyc.subscription.KycSubscriptionRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * @see bw.co.centralkyc.document.DocumentService
 */
@Service("documentService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
@Validated
@Slf4j
public class DocumentServiceImpl
        extends DocumentServiceBase {

    private final OrganisationRepository organisationRepository;
    private final IndividualRepository individualRepository;
    private final KycRecordRepository kycRecordRepository;
    private final ClientRequestRepository clientRequestRepository;
    private final KycSubscriptionRepository kycSubscriptionRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentTypeMapper documentTypeMapper;
    private final UniversalStringMatcher stringMatcher;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;

    public DocumentServiceImpl(DocumentDao documentDao, DocumentRepository documentRepository,
            OrganisationRepository organisationRepository, IndividualRepository individualRepository,
            KycRecordRepository kycRecordRepository, ClientRequestRepository clientRequestRepository,
            KycSubscriptionRepository kycSubscriptionRepository, DocumentTypeMapper documentTypeMapper,
            UniversalStringMatcher stringMatcher, RabbitProperties rabbitProperties, RabbitTemplate rabbitTemplate,
            DocumentMapper documentMapper, MessageSource messageSource, DocumentTypeRepository documentTypeRepository) {
        super(documentDao, documentRepository, documentMapper, messageSource);
        // TODO Auto-generated constructor stub

        this.organisationRepository = organisationRepository;
        this.individualRepository = individualRepository;
        this.kycRecordRepository = kycRecordRepository;
        this.clientRequestRepository = clientRequestRepository;
        this.kycSubscriptionRepository = kycSubscriptionRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.documentTypeMapper = documentTypeMapper;
        this.stringMatcher = stringMatcher;
        this.rabbitProperties = rabbitProperties;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#findById(String)
     */
    @Override
    protected DocumentDTO handleFindById(String id)
            throws Exception {

        Document doc = documentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new Exception("Document not found"));

        if (doc.getExpectedInformation() == null || doc.getExpectedInformation().isEmpty()) {
            extractExpectedInformation(doc);
            doc = documentRepository.save(doc);
        }

        DocumentDTO dto = documentMapper.toDocumentDTO(doc);
        setTargetLabel(dto);
        return dto;
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#save(DocumentDTO)
     */
    @Override
    protected DocumentDTO handleSave(DocumentDTO document)
            throws Exception {

        Document entity = documentDao.documentDTOToEntity(document);
        extractExpectedInformation(entity);
        entity = documentRepository.save(entity);

        DocumentDTO dto = documentMapper.toDocumentDTO(entity);
        setTargetLabel(dto);

        return dto;
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        Document doc = documentRepository.getReferenceById(UUID.fromString(id));
        documentRepository.delete(doc);
        return true;
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#getAll()
     */
    @Override
    protected Collection<DocumentListDTO> handleGetAll()
            throws Exception {
        Collection<Document> all = documentRepository.findAll();
        return documentMapper.toDocumentListDTOCollection(all).stream()
                .map(this::setTargetLabel)
                .toList();
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#getAll(Integer, Integer)
     */
    @Override
    protected Page<DocumentListDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {

        PageRequest request = PageRequest.of(pageNumber, pageSize);
        Page<Document> page = documentRepository.findAll(request);

        return page.map(doc -> {
            DocumentListDTO dto = documentMapper.toDocumentListDTO(doc);
            setTargetLabel(dto);
            return dto;
        });
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#findByDocumentType(String)
     */
    @Override
    protected Collection<DocumentListDTO> handleFindByDocumentType(String documentTypeId)
            throws Exception {

        Specification<Document> spec = (root, cq, cb) -> {
            return cb.equal(root.get("documentType").get("id"), documentTypeId);
        };

        Collection<Document> docs = documentRepository.findAll(spec, Sort.by(Direction.ASC, "fileName"));

        return documentMapper.toDocumentListDTOCollection(docs).stream()
                .map(this::setTargetLabel)
                .toList();

    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#upload(TargetEntity, String,
     *      String, File)
     */
    @Override
    protected DocumentDTO handleUpload(TargetEntity target, String targetId, String documentTypeId, String url)
            throws Exception {
        // TODO implement protected DocumentDTO handleUpload(TargetEntity target, String
        // targetId, String documentTypeId, File url)
        throw new UnsupportedOperationException(
                "bw.co.centralkyc.document.DocumentService.handleUpload(TargetEntity target, String targetId, String documentTypeId, File url) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#findByTarget(TargetEntity,
     *      String)
     */
    @Override
    protected Collection<DocumentListDTO> handleFindByTarget(TargetEntity target, String targetId)
            throws Exception {
        Specification<Document> spec = (root, cq, cb) -> {
            return cb.and(
                    cb.equal(root.get("target"), target),
                    cb.equal(root.get("targetId"), targetId));
        };

        Collection<Document> docs = documentRepository.findAll(spec, Sort.by(Direction.ASC, "fileName"));

        return documentMapper.toDocumentListDTOCollection(docs).stream()
                .map(dto -> setTargetLabel(dto))
                .toList();

    }

    private Specification<Document> buildSearchSpecification(DocumentSearchCriteria criteria) {
        Specification<Document> spec = (root, cq, cb) -> {
            return cb.conjunction(); // Placeholder, implement actual predicates
        };

        if (criteria.getDocumentTypeId() != null) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("documentType").get("id"),
                    UUID.fromString(criteria.getDocumentTypeId())));
        } else if (StringUtils.isNotBlank(criteria.getDocumentType())) {

            spec = spec
                    .and((root, cq, cb) -> cb.equal(root.get("documentType").get("name"), criteria.getDocumentType()));
        }

        if (StringUtils.isNotBlank(criteria.getFileName())) {
            spec = spec.and((root, cq, cb) -> cb.like(root.get("fileName"), "%" + criteria.getFileName() + "%"));
        }

        if (criteria.getTarget() != null) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("target"), criteria.getTarget()));
        }

        if (StringUtils.isNotBlank(criteria.getTargetId())) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("targetId"), criteria.getTargetId()));
        }

        if (criteria.getVerificationStatus() != null) {
            spec = spec
                    .and((root, cq, cb) -> cb.equal(root.get("verificationStatus"), criteria.getVerificationStatus()));
        }

        return spec;
    }

    private String extractTargetLabel(TargetEntity target, String targetId) {
        String label = null;

        if (target != null && StringUtils.isNotBlank(targetId)) {

            if (target == TargetEntity.ORGANISATION) {
                Organisation org = organisationRepository.findById(UUID.fromString(targetId)).orElse(null);
                if (org != null) {
                    label = org.getCode() + ' ' + org.getName();
                }
            } else if (target == TargetEntity.INDIVIDUAL) {
                bw.co.centralkyc.individual.Individual ind = individualRepository
                        .findById(UUID.fromString(targetId)).orElse(null);
                if (ind != null) {
                    label = ind.getFirstName() + " " + ind.getSurname();
                }
            } else if (target == TargetEntity.KYC_RECORD) {
                bw.co.centralkyc.kyc.KycRecord record = kycRecordRepository.findById(UUID.fromString(targetId))
                        .orElse(null);
                if (record != null) {
                    label = "KYC Record - " + record.getRef();
                }
            } else if (target == TargetEntity.CLIENT_REQUEST) {
                bw.co.centralkyc.organisation.client.ClientRequest request = clientRequestRepository
                        .findById(UUID.fromString(targetId)).orElse(null);
                if (request != null) {
                    // label = "Client Request - " + request.get;
                }
            } else if (target == TargetEntity.SUBSCRIPTION) {
                bw.co.centralkyc.subscription.KycSubscription subscription = kycSubscriptionRepository
                        .findById(UUID.fromString(targetId)).orElse(null);
                if (subscription != null) {
                    label = "KYC Subscription - " + subscription.getRef();
                }
            }

        }

        return label;
    }

    private void setTargetLabel(DocumentDTO dto) {
        if (dto.getTarget() != null && StringUtils.isNotBlank(dto.getTargetId())) {

            String label = extractTargetLabel(dto.getTarget(), dto.getTargetId());

            dto.setTargetLabel(label);
        }
    }

    private DocumentListDTO setTargetLabel(DocumentListDTO dto) {
        if (dto.target() != null && StringUtils.isNotBlank(dto.targetId())) {

            String label = extractTargetLabel(dto.target(), dto.targetId());

            dto = new DocumentListDTO(dto.id(), dto.target(), dto.targetId(), label, dto.fileName(),
                    dto.documentTypeId(),
                    dto.documentType(), dto.analyticsStatus());
        }
        return dto;
    }

    @Override
    protected Collection<DocumentListDTO> handleSearch(@Valid DocumentSearchCriteria criteria,
            @Valid Set<PropertySearchOrder> orderings) throws Exception {

        Specification<Document> spec = buildSearchSpecification(criteria);

        Sort sort = Sort.by(orderings.stream()
                .map(order -> new Sort.Order(order.getOrder() == SortOrder.ASC ? Direction.ASC
                        : Direction.DESC, order.getPropertyName()))
                .toList());

        Collection<Document> docs = documentRepository.findAll(spec, sort);

        return documentMapper.toDocumentListDTOCollection(docs).stream()
                .map(dto -> setTargetLabel(dto))
                .toList();
    }

    @Override
    protected Page<DocumentListDTO> handleSearch(SearchObject<DocumentSearchCriteria> criteria) throws Exception {

        Specification<Document> spec = buildSearchSpecification(criteria.getCriteria());
        Sort sort = Sort.by(criteria.getSortings().stream()
                .map(order -> new Sort.Order(order.getOrder() == SortOrder.ASC ? Direction.ASC
                        : Direction.DESC, order.getPropertyName()))
                .toList());

        PageRequest pageRequest = PageRequest.of(criteria.getPageNumber(), criteria.getPageSize(), sort);

        Page<Document> page = documentRepository.findAll(spec, pageRequest);

        return page.map(doc -> {
            DocumentListDTO dto = documentMapper.toDocumentListDTO(doc);
            dto = setTargetLabel(dto);
            return dto;
        });
    }

    @Override
    protected DocumentDTO handleUpdateFileContent(String id, String content) throws Exception {

        Document doc = documentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new Exception("Document not found"));
        Collection<ExpectedField> fields = doc.getDocumentType().getExpectedFields();
        Collection<VerificationDataConfig> dataConfigs = doc.getDocumentType().getVerificationDataConfigs();
        doc.setFileContent(content);
        extractExpectedInformation(doc);
        doc = documentRepository.save(doc);
        DocumentDTO dto = documentMapper.toDocumentDTO(doc);
        setTargetLabel(dto);
        return dto;
    }

    private void extractExpectedInformation(Document doc) {
        // Placeholder for document analysis logic, e.g., using OCR or metadata
        // extraction
        // This method can be expanded to populate additional fields in the Document
        // entity
        UUID targetId = UUID.fromString(doc.getTargetId());

        switch (doc.getTarget()) {
            case INDIVIDUAL:

                Individual individual = individualRepository.findById(targetId)
                        .orElseThrow(() -> new DocumentServiceException("No individual found for document target"));

                doc.setExpectedInformation(
                        this.getIndividualExpectedInformation(individual,
                                doc.getDocumentType(), null));

                break;

            case ORGANISATION:
                Organisation org = organisationRepository.findById(targetId)
                        .orElseThrow(() -> new DocumentServiceException("No organisation found for document target"));

                doc.setExpectedInformation(this.getOrganisationExpectedInformation(org,
                        doc.getDocumentType(), null));
                break;
            case KYC_RECORD:
                KycRecord record = kycRecordRepository.findById(targetId)
                        .orElseThrow(() -> new DocumentServiceException("No KYC record found for document target"));

                doc.setExpectedInformation(extractKycRecordExpectedInformation(doc, record));

                break;
            case CLIENT_REQUEST:

                break;
            case SUBSCRIPTION:

                break;
            default:
                break;
        }
    }

    private Map<String, Object> extractKycRecordExpectedInformation(Document document, KycRecord record) {

        Map<String, Object> extractedInfo = new HashMap<>();
        DocumentType dt = documentTypeRepository.findById(document.getDocumentType().getId())
                .orElseThrow(() -> new DocumentServiceException("Document type not found"));

        Collection<ExpectedField> fields = dt.getExpectedFields();
        Collection<VerificationDataConfig> dataConfigs = dt.getVerificationDataConfigs();

        if (record.getTarget() == TargetEntity.INDIVIDUAL) {

            Individual individual = individualRepository.findById(UUID.fromString(record.getTargetId()))
                    .orElseThrow(() -> new DocumentServiceException("No individual found for document target"));

            extractedInfo = getIndividualExpectedInformation(individual,
                    dt, extractedInfo);

        } else if (record.getTarget() == TargetEntity.ORGANISATION) {

            Organisation organisation = organisationRepository.findById(UUID.fromString(record.getTargetId()))
                    .orElseThrow(() -> new DocumentServiceException("No organisation found for document target"));

            extractedInfo = getOrganisationExpectedInformation(organisation,
                    dt,
                    extractedInfo);
        }

        return extractedInfo;
    }

    private Map<String, Object> getOrganisationExpectedInformation(Organisation organisation, DocumentType docType,
            Map<String, Object> expectedInformation) {

        if (expectedInformation == null) {
            expectedInformation = new HashMap<>();
        }

        for (ExpectedField expectedField : docType.getExpectedFields()) {

            switch (expectedField.getKeyField()) {
                case ORGANISATION_NAME:
                    expectedInformation.put(expectedField.getField(), organisation.getName());
                    break;

                case ORGANISATION_REGISTRATION_NO:
                    expectedInformation.put(expectedField.getField(), organisation.getRegistrationNo());
                    break;
                case ORGANISATION_PHONE_NUMBER:
                    expectedInformation.put(expectedField.getField(), organisation.getPhoneNumbers());
                    break;
                case ORGANISATION_PHYSICAL_ADDRESS:
                    expectedInformation.put(expectedField.getField(), organisation.getPhysicalAddress());
                    break;
                case ORGANISATION_POSTAL_ADDRESS:
                    expectedInformation.put(expectedField.getField(), organisation.getPostalAddress());
                    break;

                case ORGANISATION_EMAIL_ADDRESS:
                    expectedInformation.put(expectedField.getField(), organisation.getContactEmailAddress());
                    break;

                default:
                    break;
            }

        }

        return expectedInformation;

    }

    private Map<String, Object> getIndividualExpectedInformation(Individual individual, DocumentType docType,
            Map<String, Object> expectedInformation) {

        if (expectedInformation == null) {
            expectedInformation = new HashMap<>();
        }

        if (docType.getExpectedFields() != null) {
            for (ExpectedField expectedField : docType.getExpectedFields()) {

                switch (expectedField.getKeyField()) {
                    case INDIVIDUAL_FIRST_NAME:
                        expectedInformation.put(expectedField.getField(), individual.getFirstName());
                        break;

                    case INDIVIDUAL_MIDDLE_NAME:
                        expectedInformation.put(expectedField.getField(), individual.getMiddleName());

                        break;
                    case INDIVIDUAL_SURNAME:
                        expectedInformation.put(expectedField.getField(), individual.getSurname());
                        break;
                    case INDIVIDUAL_IDENTITY_NO:
                        expectedInformation.put(expectedField.getField(), individual.getIdentityNo());
                        break;
                    case INDIVIDUAL_IDENTITY_TYPE:
                        expectedInformation.put(expectedField.getField(), individual.getIdentityType());
                        break;
                    case INDIVIDUAL_POSTAL_ADDRESS:
                        expectedInformation.put(expectedField.getField(), individual.getPostalAddress());
                        break;
                    case INDIVIDUAL_PHYSICAL_ADDRESS:
                        expectedInformation.put(expectedField.getField(), individual.getPhysicalAddress());
                        break;

                    case INDIVIDUAL_EMAIL_ADDRESS:
                        expectedInformation.put(expectedField.getField(), individual.getEmailAddress());
                        break;

                    case INDIVIDUAL_SEX:
                        expectedInformation.put(expectedField.getField(), individual.getSex());
                        break;
                    case INDIVIDUAL_NATIONALITY:
                        expectedInformation.put(expectedField.getField(), individual.getNationality());
                        break;

                    default:
                        break;
                }

            }
        }

        return expectedInformation;
    }

    @Override
    protected DocumentDTO handleVerifyData(String id, String user) throws Exception {

        Document document = documentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new Exception("Document not found"));

        if (MapUtils.isEmpty(document.getExtractedInformation())) {

            return documentMapper.toDocumentDTO(document);
        }

        DocumentType docType = document.getDocumentType();
        Map<KeyField, String> fielNameMap = docType.getExpectedFields().stream()
                .collect(Collectors.toMap(ExpectedField::getKeyField, ExpectedField::getField));

        document.setVerificationStatus(DocumentVerificationStatus.UNVERIFIED);

        List<DataVerification> verifications = new ArrayList<>();

        for (VerificationDataConfig config : docType.getVerificationDataConfigs()) {

            DataVerification verification = new DataVerification();
            verification.setVerificationDataConfigId(config.getId().toString());
            verification.setVerificationDataName(config.getName());

            StringBuilder expectedBuilder = new StringBuilder();
            StringBuilder extractedBuilder = new StringBuilder();

            for (KeyField keyField : config.getKeyFields()) {

                String fieldName = fielNameMap.get(keyField);

                String extracted = document.getExpectedInformation() != null
                        ? document.getExpectedInformation().toString()
                        : null;

                if (document.getExtractedInformation().containsKey(fieldName)) {

                    if (extractedBuilder.length() > 0) {
                        extractedBuilder.append(" ");
                    }

                    Object info = document.getExtractedInformation().get(fieldName);

                    if (info != null) {
                        extractedBuilder.append(info.toString());
                    }

                }

                String expected = document.getExpectedInformation() != null
                        ? document.getExpectedInformation().toString()
                        : null;

                if (document.getExpectedInformation().containsKey(fieldName)) {

                    if (expectedBuilder.length() > 0) {
                        expectedBuilder.append(" ");
                    }

                    Object info = document.getExpectedInformation().get(fieldName);

                    if (info != null) {

                        expectedBuilder.append(document.getExpectedInformation().get(fieldName).toString());
                    }
                }

                boolean continueProcessing = continueProcessing(keyField,
                        expected,
                        extracted);

                if (!continueProcessing) {
                    document.setVerificationStatus(DocumentVerificationStatus.REJECTED);
                }
            }

            List<String> values = new ArrayList<>();

            if (expectedBuilder.length() > 0) {
                values.add(expectedBuilder.toString());
            }

            if (extractedBuilder.length() > 0) {
                values.add(extractedBuilder.toString());
            }

            verification.setValues(values);

            double similarity = 0.0;

            if (extractedBuilder.length() > 0 && expectedBuilder.length() > 0) {
                similarity = stringMatcher.calculateFilteredSimilarity(extractedBuilder.toString(),
                        expectedBuilder.toString());

            } else if (expectedBuilder.length() == 0 && extractedBuilder.length() > 0) {
                similarity = 1.0;
            } else if (expectedBuilder.length() > 0 && extractedBuilder.length() == 0) {
                similarity = 0.0;
            } else {

                similarity = 1.0;
            }

            verification.setScore(similarity);

            if (similarity >= 0.8) {

                verification.setVerificationStatus(DataVerificationStatus.VERIFIED);
            } else if (similarity >= 0.4) {
                verification.setVerificationStatus(DataVerificationStatus.UNVERIFIED);
            } else {
                verification.setVerificationStatus(DataVerificationStatus.VERIFICATION_FAILED);
            }

            verifications.add(verification);
        }

        document.setDataVerifications(verifications);

        if (document.getVerificationStatus() != DocumentVerificationStatus.REJECTED) {

            boolean hasManualReview = verifications.stream()
                    .anyMatch(
                            verification -> verification.getVerificationStatus() == DataVerificationStatus.UNVERIFIED);

            if (!hasManualReview) {
                double score = verifications.stream()
                        .mapToDouble(DataVerification::getScore)
                        .average()
                        .orElse(0.0);

                if (score >= 0.8) {
                    document.setVerificationStatus(DocumentVerificationStatus.VERIFIED);
                } else if (score >= 0.4) {
                    document.setVerificationStatus(DocumentVerificationStatus.MANUAL_REVIEW);
                } else {
                    document.setVerificationStatus(DocumentVerificationStatus.REJECTED);
                }

            } else {

                document.setVerificationStatus(DocumentVerificationStatus.MANUAL_REVIEW);
            }

        }

        document.setModifiedAt(LocalDateTime.now());
        document.setModifiedBy(user);
        document = documentRepository.save(document);

        if (document.getVerificationStatus() == DocumentVerificationStatus.REJECTED
                || document.getVerificationStatus() == DocumentVerificationStatus.VERIFIED) {
            log.info("Document ID {} requires manual review or has been rejected (verification status: {})",
                    document.getId(), document.getVerificationStatus());

            KycRecord record = kycRecordRepository.getReferenceById(UUID.fromString(document.getTargetId()));
            this.rabbitTemplate.convertAndSend(
                    rabbitProperties.getKycVerificationQueueExchange(),
                    rabbitProperties.getKycVerificationQueueRoutingKey(),
                    new QueueObject(document.getTargetId(), record.getTarget(), record.getTargetId()));
        }

        DocumentDTO dto = documentMapper.toDocumentDTO(document);
        setTargetLabel(dto);
        return dto;

    }

    private boolean continueProcessing(KeyField keyField, String expected, Object extracted) {

        if (StringUtils.isBlank(expected)) {

            return true;
        }

        boolean isExpectedString = expected instanceof String;
        boolean isExtractedString = extracted instanceof String;

        String expectedStr = ((String) expected).toLowerCase();
        String extractedStr = ((String) extracted).toLowerCase();

        switch (keyField) {
            case INDIVIDUAL_IDENTITY_NO, ORGANISATION_REGISTRATION_NO:
                if (isExpectedString && isExtractedString) {

                    return expectedStr.equals(extractedStr);
                }
                break;

            case INDIVIDUAL_FIRST_NAME, INDIVIDUAL_SURNAME, ORGANISATION_NAME:
                if (isExpectedString && isExtractedString) {

                    return stringMatcher.calculateFilteredSimilarity(expectedStr, extractedStr) < 0.8;
                }
                break;

            case DOCUMENT_DATE:

                LocalDate documentDate = LocalDate.parse(expectedStr);

                LocalDate expiryDate = documentDate.plusMonths(3);

                return LocalDate.now().isBefore(expiryDate);

            default:
                break;
        }

        return true;
    }
}
