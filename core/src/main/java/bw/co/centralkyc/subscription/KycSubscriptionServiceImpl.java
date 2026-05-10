// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::subscription::KycSubscriptionService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.subscription;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bw.co.centralkyc.PropertySearchOrder;
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.SortOrder;
import bw.co.centralkyc.SortOrderFactory;
import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.invoice.KycInvoiceMapper;
import bw.co.centralkyc.invoice.KycInvoiceRepository;
import bw.co.centralkyc.sequence.SequenceGenerator;
import bw.co.centralkyc.sequence.SequenceGeneratorRepository;
import bw.co.centralkyc.sequence.SequenceGeneratorService;
import bw.co.centralkyc.sequence.SequencePart;
import bw.co.centralkyc.sequence.SequencePartType;

/**
 * @see bw.co.centralkyc.subscription.KycSubscriptionService
 */
@Service("kycSubscriptionService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class KycSubscriptionServiceImpl
        extends KycSubscriptionServiceBase {

    private final SequenceGeneratorService sequenceGeneratorService;
    private final SequenceGeneratorRepository sequenceGeneratorRepository;
    private static final String SEQUENCE_NAME = "KYC_SUBSCRIPTION_REF";

    public KycSubscriptionServiceImpl(KycSubscriptionRepository kycSubscriptionRepository, 
            SequenceGeneratorService sequenceGeneratorService, SequenceGeneratorRepository sequenceGeneratorRepository, KycSubscriptionMapper kycSubscriptionMapper,
            KycInvoiceRepository kycInvoiceRepository, KycInvoiceMapper kycInvoiceMapper, MessageSource messageSource) {

        super(kycSubscriptionRepository, kycSubscriptionMapper,
                kycInvoiceRepository, kycInvoiceMapper, messageSource);

        this.sequenceGeneratorService = sequenceGeneratorService;
        this.sequenceGeneratorRepository = sequenceGeneratorRepository;
    }

    /**
     * @see bw.co.centralkyc.subscription.KycSubscriptionService#findById(String)
     */
    @Override
    protected KycSubscriptionDTO handleFindById(String id)
            throws Exception {

        KycSubscription subscription = kycSubscriptionRepository.findById(UUID.fromString(id)).orElse(null);

        return kycSubscriptionMapper.toKycSubscriptionDTO(subscription);
    }

    /**
     * @see bw.co.centralkyc.subscription.KycSubscriptionService#save(KycSubscriptionDTO)
     */
    @Override
    protected KycSubscriptionDTO handleSave(KycSubscriptionDTO subscription)
            throws Exception {

        KycSubscription entity = kycSubscriptionMapper.kycSubscriptionDTOToEntity(subscription);

        if (StringUtils.isBlank(subscription.getRef())) {

            SequenceGenerator sequenceGenerator = sequenceGeneratorRepository.findByName(SEQUENCE_NAME).orElse(null);

            if (sequenceGenerator == null) {

                sequenceGenerator = new SequenceGenerator();
                sequenceGenerator.setName(SEQUENCE_NAME);
                sequenceGenerator.setTargetEntity(TargetEntity.SUBSCRIPTION);

                List<SequencePart> sequenceParts = new ArrayList<>();

                SequencePart counterPart = new SequencePart();
                counterPart.setPosition(0);
                counterPart.setType(SequencePartType.STATIC);
                counterPart.setInitialValue("SUB-");
                counterPart.setName(SEQUENCE_NAME + "_PREFIX");
                counterPart.setSequenceGenerator(sequenceGenerator);
                sequenceParts.add(counterPart);

                counterPart = new SequencePart();
                counterPart.setPosition(1);
                counterPart.setType(SequencePartType.COUNTER);
                counterPart.setName(SEQUENCE_NAME + "_COUNTER");
                counterPart.setInitialValue("000000");
                counterPart.setSequenceGenerator(sequenceGenerator);
                sequenceParts.add(counterPart);

                sequenceGenerator.setSequenceParts(sequenceParts);
                sequenceGenerator = sequenceGeneratorRepository.save(sequenceGenerator);
            }

            String nextRef = sequenceGeneratorService.generateNextSequenceValue(SEQUENCE_NAME, true);
            entity.setRef(nextRef);
        }

        entity = kycSubscriptionRepository.save(entity);

        return kycSubscriptionMapper.toKycSubscriptionDTO(entity);
    }

    /**
     * @see bw.co.centralkyc.subscription.KycSubscriptionService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        kycSubscriptionRepository.deleteById(UUID.fromString(id));
        return true;
    }

    /**
     * @see bw.co.centralkyc.subscription.KycSubscriptionService#getAll()
     */
    @Override
    protected List<KycSubscriptionDTO> handleGetAll()
            throws Exception {

        List<KycSubscription> subscriptions = kycSubscriptionRepository.findAll();
        return kycSubscriptionMapper.toKycSubscriptionDTOCollection(subscriptions);
    }

    private Specification<KycSubscription> createSearchSpecification(SubscriptionSearchCriteria criteria) {
        Specification<KycSubscription> specification = ((root, query, builder) -> builder.conjunction());

        if (StringUtils.isNotBlank(criteria.getRef())) {

            Specification<KycSubscription> refSpec = (root, query, cb) -> cb.like(cb.lower(root.get("ref")),
                    "%" + criteria.getRef().toLowerCase() + "%");
            specification = specification == null ? refSpec : specification.and(refSpec);
        }

        if (StringUtils.isNotBlank(criteria.getOrganisationName())) {

            Specification<KycSubscription> orgNameSpec = (root, query, cb) -> cb.like(
                    cb.lower(root.get("organisation").get("name")),
                    "%" + criteria.getOrganisationName().toLowerCase() + "%");
            specification = specification == null ? orgNameSpec : specification.and(orgNameSpec);
        }

        if (StringUtils.isNotBlank(criteria.getOrganisationRegistrationNo())) {

            Specification<KycSubscription> orgRegNoSpec = (root, query, cb) -> cb.like(
                    cb.lower(root.get("organisation").get("registrationNo")),
                    "%" + criteria.getOrganisationRegistrationNo().toLowerCase() + "%");
            specification = specification == null ? orgRegNoSpec : specification.and(orgRegNoSpec);
        }

        if (StringUtils.isNotBlank(criteria.getOrganisatonCode())) {

            Specification<KycSubscription> orgCodeSpec = (root, query, cb) -> cb.like(
                    cb.lower(root.get("organisation").get("code")),
                    "%" + criteria.getOrganisatonCode().toLowerCase() + "%");
            specification = specification == null ? orgCodeSpec : specification.and(orgCodeSpec);
        }

        if (StringUtils.isNotBlank(criteria.getOrganisatonId())) {

            Specification<KycSubscription> orgIdSpec = (root, query, cb) -> cb.equal(root.get("organisationId"),
                    UUID.fromString(criteria.getOrganisatonId()));
            specification = specification == null ? orgIdSpec : specification.and(orgIdSpec);
        }

        if( criteria.getPeriod() != null) {

            Specification<KycSubscription> periodSpec = (root, query, cb) -> cb.equal(root.get("period"),
                    criteria.getPeriod());
            specification = specification == null ? periodSpec : specification.and(periodSpec);
        }

        if( criteria.getStatus() != null) {

            Specification<KycSubscription> statusSpec = (root, query, cb) -> cb.equal(root.get("status"),
                    criteria.getStatus());
            specification = specification == null ? statusSpec : specification.and(statusSpec);
        }

        return specification;
    }

    /**
     * @see bw.co.centralkyc.subscription.KycSubscriptionService#search(String)
     */
    @Override
    protected List<KycSubscriptionDTO> handleSearch(SubscriptionSearchCriteria criteria,
            Set<PropertySearchOrder> sortOrders)
            throws Exception {

        Specification<KycSubscription> specification = createSearchSpecification(criteria);
        if (sortOrders == null || sortOrders.isEmpty()) {
            PropertySearchOrder def = new PropertySearchOrder();
            def.setOrder(SortOrder.ASC);
            def.setPropertyName("createdAt");
            sortOrders = Set.of(def);
        }

        List<KycSubscription> subscriptions = kycSubscriptionRepository.findAll(specification, SortOrderFactory.createSortOrder(sortOrders));
        return kycSubscriptionMapper.toKycSubscriptionDTOCollection(subscriptions);
    }

    /**
     * @see bw.co.centralkyc.subscription.KycSubscriptionService#getAll(Integer,
     *      Integer)
     */
    @Override
    protected Page<KycSubscriptionDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {

        Page<KycSubscription> subscriptions = kycSubscriptionRepository.findAll(PageRequest.of(pageNumber, pageSize));

        return subscriptions.map(arg0 -> kycSubscriptionMapper.toKycSubscriptionDTO(arg0));
    }

    /**
     * @see bw.co.centralkyc.subscription.KycSubscriptionService#search(String,
     *      Integer, Integer)
     */
    @Override
    protected Page<KycSubscriptionDTO> handleSearch(SearchObject<SubscriptionSearchCriteria> criteria)
            throws Exception {
        Specification<KycSubscription> specification = createSearchSpecification(criteria.getCriteria());
        Set<PropertySearchOrder> sortOrders = new HashSet<>();

        if (CollectionUtils.isNotEmpty(criteria.getSortings())) {
            sortOrders.addAll(criteria.getSortings());
        } else {

            PropertySearchOrder def = new PropertySearchOrder();
            def.setOrder(SortOrder.ASC);
            def.setPropertyName("createdAt");
            sortOrders.add(def);
        }

        PageRequest page = PageRequest.of(criteria.getPageNumber(), criteria.getPageSize(),
                SortOrderFactory.createSortOrder(sortOrders));

        Page<KycSubscription> subscriptions = kycSubscriptionRepository.findAll(specification, page);

        return subscriptions.map(arg0 -> kycSubscriptionMapper.toKycSubscriptionDTO(arg0));
    }

    @Override
    protected List<KycSubscriptionDTO> handleFindByOrganisation(String organisationId, String user)
            throws Exception {

        Specification<KycSubscription> specification = (root, query, cb) -> cb.equal(
                root.get("organisation").get("id"),
                UUID.fromString(organisationId));

        List<KycSubscription> subscriptions = this.kycSubscriptionRepository.findAll(specification);
        return this.kycSubscriptionMapper.toKycSubscriptionDTOCollection(subscriptions);

    }

    @Override
    protected Long handleCountByStatus(KycSubsciptionStatus status) throws Exception {

        return this.kycSubscriptionRepository.countByStatus(status).orElse(0L);
    }

    @Override
    protected Long handleCount() throws Exception {

        return this.kycSubscriptionRepository.count();
    }

}