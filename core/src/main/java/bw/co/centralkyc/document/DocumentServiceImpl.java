// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::document::DocumentService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.document;

import bw.co.centralkyc.PropertySearchOrder;
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.SortOrder;
import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.individual.IndividualRepository;
import bw.co.centralkyc.kyc.KycRecordRepository;
import bw.co.centralkyc.organisation.Organisation;
import bw.co.centralkyc.organisation.OrganisationRepository;
import bw.co.centralkyc.organisation.client.ClientRequestRepository;
import bw.co.centralkyc.subscription.KycSubscriptionRepository;
import jakarta.validation.Valid;

import java.io.File;
import java.util.Collection;
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
            
    public DocumentServiceImpl(DocumentDao documentDao, DocumentRepository documentRepository,
            OrganisationRepository organisationRepository, IndividualRepository individualRepository,
            KycRecordRepository kycRecordRepository, ClientRequestRepository clientRequestRepository, KycSubscriptionRepository kycSubscriptionRepository,
            DocumentMapper documentMapper, MessageSource messageSource) {
        super(documentDao, documentRepository, documentMapper, messageSource);
        // TODO Auto-generated constructor stub

        this.organisationRepository = organisationRepository;
        this.individualRepository = individualRepository;
        this.kycRecordRepository = kycRecordRepository;
        this.clientRequestRepository = clientRequestRepository;
        this.kycSubscriptionRepository = kycSubscriptionRepository;
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#findById(String)
     */
    @Override
    protected DocumentDTO handleFindById(String id)
            throws Exception {

        Document doc = documentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new Exception("Document not found"));

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
    protected Collection<DocumentDTO> handleGetAll()
            throws Exception {
        Collection<Document> all = documentRepository.findAll();
        return documentMapper.toDocumentDTOCollection(all).stream()
                .peek(dto -> setTargetLabel(dto))
                .toList();
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#getAll(Integer, Integer)
     */
    @Override
    protected Page<DocumentDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {

        PageRequest request = PageRequest.of(pageNumber, pageSize);
        Page<Document> page = documentRepository.findAll(request);

        return page.map(doc -> {
            DocumentDTO dto = documentMapper.toDocumentDTO(doc);
            setTargetLabel(dto);
            return dto;
        });
    }

    /**
     * @see bw.co.centralkyc.document.DocumentService#findByDocumentType(String)
     */
    @Override
    protected Collection<DocumentDTO> handleFindByDocumentType(String documentTypeId)
            throws Exception {

        Specification<Document> spec = (root, cq, cb) -> {
            return cb.equal(root.get("documentType").get("id"), documentTypeId);
        };

        Collection<Document> docs = documentRepository.findAll(spec, Sort.by(Direction.ASC, "fileName"));

        return documentMapper.toDocumentDTOCollection(docs).stream()
                .peek(dto -> setTargetLabel(dto))
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
    protected Collection<DocumentDTO> handleFindByTarget(TargetEntity target, String targetId)
            throws Exception {
        Specification<Document> spec = (root, cq, cb) -> {
            return cb.and(
                    cb.equal(root.get("target"), target),
                    cb.equal(root.get("targetId"), targetId));
        };

        Collection<Document> docs = documentRepository.findAll(spec, Sort.by(Direction.ASC, "fileName"));

        return documentDao.toDocumentDTOCollection(docs).stream()
                .peek(dto -> setTargetLabel(dto))
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

        if(StringUtils.isNotBlank(criteria.getFileName())) {
            spec = spec.and((root, cq, cb) -> cb.like(root.get("fileName"), "%" + criteria.getFileName() + "%"));
        }

        if(criteria.getTarget() != null) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("target"), criteria.getTarget()));
        }   

        if(StringUtils.isNotBlank(criteria.getTargetId())) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("targetId"), criteria.getTargetId()));
        }

        if(criteria.getVerificationStatus() != null) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("verificationStatus"), criteria.getVerificationStatus()));
        }

        return spec;
    }

    private void setTargetLabel(DocumentDTO dto) {
        if (dto.getTarget() != null && StringUtils.isNotBlank(dto.getTargetId())) {
            
            String label = null;

            if(dto.getTarget() == TargetEntity.ORGANISATION) {
                Organisation org = organisationRepository.findById(UUID.fromString(dto.getTargetId())).orElse(null);
                if(org != null) {
                    label = org.getCode() + ' ' + org.getName();
                }
            } else if(dto.getTarget() == TargetEntity.INDIVIDUAL) {
                bw.co.centralkyc.individual.Individual ind = individualRepository.findById(UUID.fromString(dto.getTargetId())).orElse(null);
                if(ind != null) {
                    label = ind.getFirstName() + " " + ind.getSurname();
                }
            } else if(dto.getTarget() == TargetEntity.KYC_RECORD) {
                bw.co.centralkyc.kyc.KycRecord record = kycRecordRepository.findById(UUID.fromString(dto.getTargetId())).orElse(null);
                if(record != null) {
                    label = "KYC Record - " + record.getRef();
                }
            } else if(dto.getTarget() == TargetEntity.CLIENT_REQUEST) {
                bw.co.centralkyc.organisation.client.ClientRequest request = clientRequestRepository.findById(UUID.fromString(dto.getTargetId())).orElse(null);
                if(request != null) {
                    // label = "Client Request - " + request.get;
                }
            } else if(dto.getTarget() == TargetEntity.SUBSCRIPTION) {
                bw.co.centralkyc.subscription.KycSubscription subscription = kycSubscriptionRepository.findById(UUID.fromString(dto.getTargetId())).orElse(null);
                if(subscription != null) {
                    label = "KYC Subscription - " + subscription.getRef();
                }
            }

            dto.setTargetLabel(label);
        }
    }

    @Override
    protected Collection<DocumentDTO> handleSearch(@Valid DocumentSearchCriteria criteria,
            @Valid Set<PropertySearchOrder> orderings) throws Exception {

        Specification<Document> spec = buildSearchSpecification(criteria);

        Sort sort = Sort.by(orderings.stream()
                .map(order -> new Sort.Order(order.getOrder() == SortOrder.ASC ? Direction.ASC
                        : Direction.DESC, order.getPropertyName()))
                .toList());

        Collection<Document> docs = documentRepository.findAll(spec, sort);

        return documentMapper.toDocumentDTOCollection(docs).stream()
                .peek(dto -> setTargetLabel(dto))
                .toList();
    }

    @Override
    protected Page<DocumentDTO> handleSearch(SearchObject<DocumentSearchCriteria> criteria) throws Exception {

        Specification<Document> spec = buildSearchSpecification(criteria.getCriteria());
        Sort sort = Sort.by(criteria.getSortings().stream()
                .map(order -> new Sort.Order(order.getOrder() == SortOrder.ASC ? Direction.ASC
                        : Direction.DESC, order.getPropertyName()))
                .toList());

        PageRequest pageRequest = PageRequest.of(criteria.getPageNumber(), criteria.getPageSize(), sort);

        Page<Document> page = documentRepository.findAll(spec, pageRequest);

        return page.map(doc -> {
            DocumentDTO dto = documentMapper.toDocumentDTO(doc);
            setTargetLabel(dto);
            return dto;
        });
    }

    @Override
    protected DocumentDTO handleUpdateFileContent(String id, String content) throws Exception {

        Document doc = documentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new Exception("Document not found"));

        doc.setFileContent(content);
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

}