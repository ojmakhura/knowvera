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
import bw.co.centralkyc.KeyFieldMatchResult;
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

import static org.junit.jupiter.api.DynamicTest.stream;

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

        boolean isNew = entity.getId() == null;

        extractExpectedInformation(entity);
        entity = documentRepository.save(entity);

        if(isNew) {

            switch (entity.getTarget()) {
                case ORGANISATION:
                    Organisation org = organisationRepository.findById(UUID.fromString(entity.getTargetId()))
                            .orElseThrow(() -> new DocumentServiceException("No organisation found for document target"));
                    if(org.getDocuments() == null) {
                        org.setDocuments(new ArrayList<>());
                    }

                    org.getDocuments().add(entity);
                    organisationRepository.save(org);
                    break;

                case INDIVIDUAL:
                    Individual ind = individualRepository.findById(UUID.fromString(entity.getTargetId()))
                            .orElseThrow(() -> new DocumentServiceException("No individual found for document target"));
                    if(ind.getDocuments() == null) {
                        ind.setDocuments(new ArrayList<>());
                    }

                    ind.getDocuments().add(entity);
                    individualRepository.save(ind);
                    break;
            
                default:
                    break;
            }

        }

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

            dto = new DocumentListDTO(
                    dto.id(),
                    dto.target(),
                    dto.targetId(),
                    label,
                    dto.fileName(),
                    dto.documentTypeId(),
                    dto.documentType(),
                    dto.analyticsStatus(),
                    dto.verificationStatus());
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

            // switch (expectedField.getKeyField()) {
            //     case ORGANISATION_NAME:
            //         expectedInformation.put(expectedField.getField(), organisation.getName());
            //         break;

            //     case ORGANISATION_REGISTRATION_NO:
            //         expectedInformation.put(expectedField.getField(), organisation.getRegistrationNo());
            //         break;
            //     case ORGANISATION_PHONE_NUMBER:
            //         expectedInformation.put(expectedField.getField(), organisation.getPhoneNumbers());
            //         break;
            //     case ORGANISATION_PHYSICAL_ADDRESS:
            //         expectedInformation.put(expectedField.getField(), organisation.getPhysicalAddress());
            //         break;
            //     case ORGANISATION_POSTAL_ADDRESS:
            //         expectedInformation.put(expectedField.getField(), organisation.getPostalAddress());
            //         break;

            //     case ORGANISATION_EMAIL_ADDRESS:
            //         expectedInformation.put(expectedField.getField(), organisation.getContactEmailAddress());
            //         break;

            //     default:
            //         break;
            // }

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

                // switch (expectedField.getKeyField()) {
                //     case INDIVIDUAL_FIRST_NAME:
                //         expectedInformation.put(expectedField.getField(), individual.getFirstName());
                //         break;

                //     case INDIVIDUAL_MIDDLE_NAME:
                //         expectedInformation.put(expectedField.getField(), individual.getMiddleName());

                //         break;
                //     case INDIVIDUAL_SURNAME:
                //         expectedInformation.put(expectedField.getField(), individual.getSurname());
                //         break;
                //     case INDIVIDUAL_IDENTITY_NO:
                //         expectedInformation.put(expectedField.getField(), individual.getIdentityNo());
                //         break;
                //     case INDIVIDUAL_IDENTITY_TYPE:
                //         expectedInformation.put(expectedField.getField(), individual.getIdentityType());
                //         break;
                //     case INDIVIDUAL_POSTAL_ADDRESS:
                //         expectedInformation.put(expectedField.getField(), individual.getPostalAddress());
                //         break;
                //     case INDIVIDUAL_PHYSICAL_ADDRESS:
                //         expectedInformation.put(expectedField.getField(), individual.getPhysicalAddress());
                //         break;

                //     case INDIVIDUAL_EMAIL_ADDRESS:
                //         expectedInformation.put(expectedField.getField(), individual.getEmailAddress());
                //         break;

                //     case INDIVIDUAL_SEX:
                //         expectedInformation.put(expectedField.getField(), individual.getSex());
                //         break;
                //     case INDIVIDUAL_NATIONALITY:
                //         expectedInformation.put(expectedField.getField(), individual.getNationality());
                //         break;

                //     default:
                //         break;
                // }

            }
        }

        return expectedInformation;
    }

    private List<DataVerification> processVerificationFields(Collection<VerificationDataConfig> dataConfigs,
            Collection<ExpectedField> expectedFields, Map expectedInformation,
            Map extractedInformation) {

        // Map<KeyField, Boolean> mandatoryMap = expectedFields.stream()
        //         .collect(Collectors.toMap(ExpectedField::getKeyField, ExpectedField::getMandatory));

        // Map<KeyField, String> fielNameMap = expectedFields.stream()
        //         .collect(Collectors.toMap(ExpectedField::getKeyField, ExpectedField::getField));
        List<DataVerification> verifications = new ArrayList<>();

        // for (VerificationDataConfig config : dataConfigs) {

        //     DataVerification verification = new DataVerification();
        //     verification.setVerificationDataConfigId(config.getId().toString());
        //     verification.setVerificationDataName(config.getName());

        //     StringBuilder expectedBuilder = new StringBuilder();
        //     StringBuilder extractedBuilder = new StringBuilder();

        //     List<KeyFieldMatchResult> matchResults = new ArrayList<>();

        //     for (KeyField keyField : config.getKeyFields()) {

        //         KeyFieldMatchResult match = new KeyFieldMatchResult();
        //         match.setKeyField(keyField);

        //         String fieldName = fielNameMap.get(keyField);

        //         String extracted = null;

        //         if (extractedInformation.containsKey(fieldName)) {

        //             if (extractedBuilder.length() > 0) {
        //                 extractedBuilder.append(" ");
        //             }

        //             Object info = extractedInformation.get(fieldName);

        //             if (info != null) {
        //                 extracted = info.toString();
        //                 extractedBuilder.append(extracted);
        //             } else {

        //                 extracted = "";
        //             }
        //         }

        //         String expected = null;

        //         if (expectedInformation.containsKey(fieldName)) {

        //             if (expectedBuilder.length() > 0) {
        //                 expectedBuilder.append(" ");
        //             }

        //             Object info = expectedInformation.get(fieldName);

        //             if (info != null) {

        //                 expected = info.toString();
        //                 expectedBuilder.append(expected);
        //             } else {

        //                 expected = "";
        //             }
        //         }

        //         boolean continueProcessing = continueProcessing(keyField,
        //                 expected,
        //                 extracted);

        //         match.setExpectedValue(expected);
        //         match.setExtractedValue(extracted);
        //         if (continueProcessing) {
        //             match.setSimilarity(stringMatcher.calculateFilteredSimilarity(extracted, expected));
        //         } else {
        //             match.setSimilarity(0.0);
        //         }
        //         match.setMandatory(mandatoryMap.get(keyField));
        //         match.setSuccess(continueProcessing);

        //         matchResults.add(match);
        //     }

        //     List<String> values = new ArrayList<>();

        //     if (expectedBuilder.length() > 0) {
        //         values.add(expectedBuilder.toString());
        //     }

        //     if (extractedBuilder.length() > 0) {
        //         values.add(extractedBuilder.toString());
        //     }

        //     verification.setKeyFieldMatches(matchResults);

        //     List<KeyFieldMatchResult> mandatory = matchResults.stream()
        //             .filter(KeyFieldMatchResult::getMandatory)
        //             .toList();

        //     if (mandatory == null || mandatory.isEmpty()) {
        //         mandatory = matchResults;
        //         verification.setHasMandatoryFields(false);
        //     } else {
        //         verification.setHasMandatoryFields(true);
        //     }

        //     double similarity = mandatory
        //             .stream()
        //             .reduce(0.0, (sum, match) -> sum + match.getSimilarity(), Double::sum)
        //             / mandatory.size();

        //     verification.setScore(similarity);

        //     boolean hasFailedMandatory = mandatory.stream()
        //             .anyMatch(match -> !match.getSuccess());

        //     if (hasFailedMandatory || similarity < 0.4) {

        //         verification.setVerificationStatus(DataVerificationStatus.VERIFICATION_FAILED);

        //         StringBuilder reasonBuilder = new StringBuilder();
        //         if (hasFailedMandatory) {

        //             if (verification.getHasMandatoryFields()) {
        //                 reasonBuilder.append("Failed verification of mandatory field(s). ");
        //             } else {
        //                 reasonBuilder.append("Failed verification of field(s). ");
        //             }
        //             reasonBuilder.append('\n').append(mandatory.stream()
        //                     .filter(match -> !match.getSuccess())
        //                     .map(match -> match.getKeyField().name())
        //                     .collect(Collectors.joining(", ")));
        //         } else {

        //             reasonBuilder.append("Overall similarity score below threshold. ")
        //                     .append('\n')
        //                     .append(String.format("Similarity: %.2f", similarity));
        //         }

        //         verification.setVerificationReport(reasonBuilder.toString());

        //     } else if (similarity >= 0.8) {

        //         verification.setVerificationStatus(DataVerificationStatus.VERIFIED);

        //         if (verification.getHasMandatoryFields()) {
        //             verification.setVerificationReport(
        //                     "All mandatory fields passed verification with satisfactory similarity.");
        //         } else {
        //             verification.setVerificationReport("All fields passed verification with satisfactory similarity.");
        //         }

        //     } else {
        //         verification.setVerificationStatus(DataVerificationStatus.UNVERIFIED);
        //     }

        //     verifications.add(verification);
        // }

        return verifications;
    }

    @Override
    protected DocumentDTO handleVerifyData(String id, String user) throws Exception {

        Document document = documentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new Exception("Document not found"));
        if (MapUtils.isEmpty(document.getExtractedInformation())) {

            return documentMapper.toDocumentDTO(document);
        }

        DocumentType docType = document.getDocumentType();

        document.setVerificationStatus(DocumentVerificationStatus.UNVERIFIED);

        List<DataVerification> verifications = processVerificationFields(docType.getVerificationDataConfigs(),
                docType.getExpectedFields(),
                document.getExpectedInformation(), document.getExtractedInformation());

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

                boolean hasFailedMandatory = verifications.stream()
                        .anyMatch(verification -> verification.getHasMandatoryFields()
                                && verification.getVerificationStatus() == DataVerificationStatus.VERIFICATION_FAILED);

                if (hasFailedMandatory || score < 0.4) {
                    document.setVerificationStatus(DocumentVerificationStatus.REJECTED);
                } else if (score >= 0.8) {
                    document.setVerificationStatus(DocumentVerificationStatus.VERIFIED);
                } else {
                    document.setVerificationStatus(DocumentVerificationStatus.MANUAL_REVIEW);
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

            if (document.getTarget() == TargetEntity.KYC_RECORD) {
                KycRecord record = kycRecordRepository.findById(UUID.fromString(document.getTargetId()))
                        .orElseThrow(() -> new DocumentServiceException("Document not found"));
                this.rabbitTemplate.convertAndSend(
                        rabbitProperties.getKycVerificationQueueExchange(),
                        rabbitProperties.getKycVerificationQueueRoutingKey(),
                        new QueueObject(record.getId().toString(), record.getTarget(), record.getTargetId()));
            }
        }

        DocumentDTO dto = documentMapper.toDocumentDTO(document);
        setTargetLabel(dto);
        return dto;

    }

    private boolean continueProcessing(KeyField keyField, String expected, Object extracted) {

        if (StringUtils.isBlank(expected) && keyField != KeyField.DOCUMENT_DATE) {

            return true;
        }

        boolean isExpectedString = expected instanceof String;
        boolean isExtractedString = extracted instanceof String;

        String expectedStr = expected != null ? expected.toString().toLowerCase() : "";
        String extractedStr = extracted != null ? extracted.toString().toLowerCase() : "";

        switch (keyField) {
            case INDIVIDUAL_IDENTITY_NO, ORGANISATION_REGISTRATION_NO:
                if (isExpectedString && isExtractedString) {

                    return expectedStr.equals(extractedStr);
                }
                break;

            case INDIVIDUAL_FIRST_NAME, INDIVIDUAL_SURNAME, ORGANISATION_NAME:
                if (isExpectedString && isExtractedString) {
                    double score = stringMatcher.calculateFilteredSimilarity(expectedStr, extractedStr);
                    boolean satisfactoryScore = score >= 0.8;
                    return satisfactoryScore;
                }
                break;

            case DOCUMENT_DATE:

                LocalDate documentDate = LocalDate.parse(extractedStr);

                LocalDate expiryDate = documentDate.plusMonths(3);

                return LocalDate.now().isBefore(expiryDate);

            default:
                break;
        }

        return true;
    }
}
