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
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.SortOrder;
import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.document.type.DocumentType;
import bw.co.centralkyc.document.type.DocumentTypeRepository;
import bw.co.centralkyc.individual.Individual;
import bw.co.centralkyc.individual.IndividualRepository;
import bw.co.centralkyc.kyc.KycRecord;
import bw.co.centralkyc.kyc.KycRecordRepository;
import bw.co.centralkyc.organisation.Organisation;
import bw.co.centralkyc.organisation.OrganisationRepository;
import bw.co.centralkyc.organisation.client.ClientRequestRepository;
import bw.co.centralkyc.subscription.KycSubscriptionRepository;
import jakarta.validation.Valid;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
public class DocumentServiceImpl
        extends DocumentServiceBase {

    private final OrganisationRepository organisationRepository;
    private final IndividualRepository individualRepository;
    private final KycRecordRepository kycRecordRepository;
    private final ClientRequestRepository clientRequestRepository;
    private final KycSubscriptionRepository kycSubscriptionRepository;
    private final DocumentTypeRepository documentTypeRepository;

    public DocumentServiceImpl(DocumentDao documentDao, DocumentRepository documentRepository,
                               OrganisationRepository organisationRepository, IndividualRepository individualRepository,
                               KycRecordRepository kycRecordRepository, ClientRequestRepository clientRequestRepository,
                               KycSubscriptionRepository kycSubscriptionRepository,
                               DocumentMapper documentMapper, MessageSource messageSource, DocumentTypeRepository documentTypeRepository) {
        super(documentDao, documentRepository, documentMapper, messageSource);
        // TODO Auto-generated constructor stub

        this.organisationRepository = organisationRepository;
        this.individualRepository = individualRepository;
        this.kycRecordRepository = kycRecordRepository;
        this.clientRequestRepository = clientRequestRepository;
        this.kycSubscriptionRepository = kycSubscriptionRepository;
        this.documentTypeRepository = documentTypeRepository;
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

            dto = new DocumentListDTO(dto.id(), dto.target(), dto.targetId(), label, dto.fileName(), dto.documentTypeId(),
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

        doc.setFileContent(content);
        extractExpectedInformation(doc);
        doc = documentRepository.save(doc);
        DocumentDTO dto = documentMapper.toDocumentDTO(doc);
        setTargetLabel(dto);
        return dto;
    }

    @Override
    protected DocumentDTO handleAnalyseDocument(String id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleAnalyseDocument'");
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
                        this.getIndividualExpectedInformation(individual, doc.getDocumentType(), null));

                break;

            case ORGANISATION:
                Organisation org = organisationRepository.findById(targetId)
                        .orElseThrow(() -> new DocumentServiceException("No organisation found for document target"));

                doc.setExpectedInformation(this.getOrganisationExpectedInformation(org, doc.getDocumentType(), null));
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

        if (record.getTarget() == TargetEntity.INDIVIDUAL) {

            Individual individual = individualRepository.findById(UUID.fromString(record.getTargetId()))
                    .orElseThrow(() -> new DocumentServiceException("No individual found for document target"));

            extractedInfo = getIndividualExpectedInformation(individual, document.getDocumentType(), extractedInfo);

        } else if (record.getTarget() == TargetEntity.ORGANISATION) {
            extractedInfo = getOrganisationExpectedInformation(record.getTargetId(), document.getDocumentType(),
                    extractedInfo);
        }

        return extractedInfo;
    }

    private Map<String, Object> getOrganisationExpectedInformation(Organisation organisation, DocumentType docType,
            Map<String, Object> expectedInformation) {

        if (expectedInformation == null) {
            expectedInformation = new HashMap<>();
        }

        // for (ExpectedField expectedField : docType.getExpectedFields()) {

        //     switch (expectedField.getKeyField()) {
        //         case ORGANISATION_NAME:
        //             expectedInformation.put(expectedField.getField(), organisation.getName());
        //             break;

        //         case ORGANISATION_REGISTRATION_NO:
        //             expectedInformation.put(expectedField.getField(), organisation.getRegistrationNo());
        //             break;
        //         case ORGANISATION_PHONE_NUMBER:
        //             expectedInformation.put(expectedField.getField(), organisation.getPhoneNumbers());
        //             break;
        //         case ORGANISATION_PHYSICAL_ADDRESS:
        //             expectedInformation.put(expectedField.getField(), organisation.getPhysicalAddress());
        //             break;
        //         case ORGANISATION_POSTAL_ADDRESS:
        //             expectedInformation.put(expectedField.getField(), organisation.getPostalAddress());
        //             break;

        //         case ORGANISATION_EMAIL_ADDRESS:
        //             expectedInformation.put(expectedField.getField(), organisation.getContactEmailAddress());
        //             break;

        //         default:
        //             break;
        //     }

        // }

        return expectedInformation;

    }

    private Map<String, Object> getIndividualExpectedInformation(Individual individual, DocumentType docType,
            Map<String, Object> expectedInformation) {

        if (expectedInformation == null) {
            expectedInformation = new HashMap<>();
        }

        if(docType.getExpectedFields() != null) {
            // for (ExpectedField expectedField : docType.getExpectedFields()) {

            //     switch (expectedField.getKeyField()) {
            //         case INDIVIDUAL_FIRST_NAME:
            //             expectedInformation.put(expectedField.getField(), individual.getFirstName());
            //             break;

            //         case INDIVIDUAL_MIDDLE_NAME:
            //             expectedInformation.put(expectedField.getField(), individual.getMiddleName());

            //             break;
            //         case INDIVIDUAL_SURNAME:
            //             expectedInformation.put(expectedField.getField(), individual.getSurname());
            //             break;
            //         case INDIVIDUAL_IDENTITY_NO:
            //             expectedInformation.put(expectedField.getField(), individual.getIdentityNo());
            //             break;
            //         case INDIVIDUAL_IDENTITY_TYPE:
            //             expectedInformation.put(expectedField.getField(), individual.getIdentityType());
            //             break;
            //         case INDIVIDUAL_POSTAL_ADDRESS:
            //             expectedInformation.put(expectedField.getField(), individual.getPostalAddress());
            //             break;
            //         case INDIVIDUAL_PHYSICAL_ADDRESS:
            //             expectedInformation.put(expectedField.getField(), individual.getPhysicalAddress());
            //             break;

            //         case INDIVIDUAL_EMAIL_ADDRESS:
            //             expectedInformation.put(expectedField.getField(), individual.getEmailAddress());
            //             break;

            //         case INDIVIDUAL_SEX:
            //             expectedInformation.put(expectedField.getField(), individual.getSex());
            //             break;
            //         case INDIVIDUAL_NATIONALITY:
            //             expectedInformation.put(expectedField.getField(), individual.getNationality());
            //             break;

            //         default:
            //             break;
            //     }

            // }
        }

        return expectedInformation;
    }

    private Map<String, Object> getOrganisationExpectedInformation(String individualId, DocumentType docType,
            Map<String, Object> expectedInformation) {

        if (expectedInformation == null) {
            expectedInformation = new HashMap<>();
        }

        Organisation organisation = organisationRepository.findById(UUID.fromString(individualId))
                .orElseThrow(() -> new DocumentServiceException("No organisation found for document target"));

        // for (ExpectedField expectedField : docType.getExpectedFields()) {

        //     switch (expectedField.getKeyField()) {
        //         case ORGANISATION_NAME:
        //             expectedInformation.put(expectedField.getField(), organisation.getName());
        //             break;

        //         case ORGANISATION_REGISTRATION_NO:
        //             expectedInformation.put(expectedField.getField(), organisation.getRegistrationNo());
        //             break;
        //         case ORGANISATION_PHONE_NUMBER:
        //             expectedInformation.put(expectedField.getField(), organisation.getPhoneNumbers());
        //             break;
        //         case ORGANISATION_PHYSICAL_ADDRESS:
        //             expectedInformation.put(expectedField.getField(), organisation.getPhysicalAddress());
        //             break;
        //         case ORGANISATION_POSTAL_ADDRESS:
        //             expectedInformation.put(expectedField.getField(), organisation.getPostalAddress());
        //             break;

        //         case ORGANISATION_EMAIL_ADDRESS:
        //             expectedInformation.put(expectedField.getField(), organisation.getContactEmailAddress());
        //             break;

        //         default:
        //             break;
        //     }

        // }

        return expectedInformation;

    }

}
