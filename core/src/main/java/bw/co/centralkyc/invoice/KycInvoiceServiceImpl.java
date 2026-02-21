// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::invoice::KycInvoiceService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Flow.Subscription;

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
import bw.co.centralkyc.kyc.KycRecordServiceException;
import bw.co.centralkyc.sequence.SequenceGenerator;
import bw.co.centralkyc.sequence.SequenceGeneratorService;
import bw.co.centralkyc.sequence.SequencePart;
import bw.co.centralkyc.sequence.SequencePartType;
import bw.co.centralkyc.subscription.KycSubsciptionStatus;
import bw.co.centralkyc.subscription.KycSubscription;
import bw.co.centralkyc.subscription.KycSubscriptionDao;
import bw.co.centralkyc.subscription.KycSubscriptionRepository;

/**
 * @see bw.co.centralkyc.invoice.KycInvoiceService
 */
@Service("kycInvoiceService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class KycInvoiceServiceImpl
        extends KycInvoiceServiceBase {

    private static final String SEQUENCE_NAME = "KYC_INVOICE_REF";
    private final SequenceGeneratorService sequenceGeneratorService;

    public KycInvoiceServiceImpl(KycInvoiceDao kycInvoiceDao, KycInvoiceRepository kycInvoiceRepository,
            KycSubscriptionDao kycSubscriptionDao, KycSubscriptionRepository kycSubscriptionRepository,
            MessageSource messageSource, SequenceGeneratorService sequenceGeneratorService) {
        super(kycInvoiceDao, kycInvoiceRepository, kycSubscriptionDao, kycSubscriptionRepository, messageSource);

        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * @see bw.co.centralkyc.invoice.KycInvoiceService#findById(String)
     */
    @Override
    protected KycInvoiceDTO handleFindById(String id)
            throws Exception {

        KycInvoice kycInvoice = this.kycInvoiceRepository.findById(UUID.fromString(id)).orElse(null);

        return this.kycInvoiceDao.toKycInvoiceDTO(kycInvoice);
    }

    /**
     * @see bw.co.centralkyc.invoice.KycInvoiceService#save(KycInvoiceDTO)
     */
    @Override
    protected KycInvoiceDTO handleSave(KycInvoiceDTO invoice)
            throws Exception {

        KycInvoice kycInvoice = this.kycInvoiceDao.kycInvoiceDTOToEntity(invoice);

        if(StringUtils.isBlank(kycInvoice.getRef())) {

            kycInvoice.setRef(createInvoiceRef());
        }

        kycInvoice = this.kycInvoiceRepository.save(kycInvoice);

        return this.kycInvoiceDao.toKycInvoiceDTO(kycInvoice);
    }

    /**
     * @see bw.co.centralkyc.invoice.KycInvoiceService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        this.kycInvoiceRepository.deleteById(UUID.fromString(id));
        return true;
    }

    /**
     * @see bw.co.centralkyc.invoice.KycInvoiceService#getAll()
     */
    @Override
    protected Collection<KycInvoiceDTO> handleGetAll()
            throws Exception {

        return this.kycInvoiceDao.toKycInvoiceDTOCollection(this.kycInvoiceRepository.findAll());
    }

    private Specification<KycInvoice> createSearchSpecification(InvoiceSearchCriteria criteria) {

        Specification<KycInvoice> specification = ((root, query, builder) -> builder.conjunction());

        if (criteria.getPaid() != null) {
            specification = (root, query, cb) -> cb.equal(root.get("paid"), criteria.getPaid());
        }

        if (StringUtils.isNotBlank(criteria.getRef())) {

            Specification<KycInvoice> refSpec = (root, query, cb) -> cb.like(cb.lower(root.get("ref")),
                    "%" + criteria.getRef().toLowerCase() + "%");
            specification = specification == null ? refSpec : specification.and(refSpec);
        }

        if (StringUtils.isNotBlank(criteria.getOrganisationName())) {

            Specification<KycInvoice> orgNameSpec = (root, query, cb) -> cb.like(
                    cb.lower(root.get("organisation").get("name")),
                    "%" + criteria.getOrganisationName().toLowerCase() + "%");
            specification = specification == null ? orgNameSpec : specification.and(orgNameSpec);
        }

        if (StringUtils.isNotBlank(criteria.getOrganisationRegistrationNo())) {

            Specification<KycInvoice> orgRegNoSpec = (root, query, cb) -> cb.like(
                    cb.lower(root.get("organisation").get("registrationNo")),
                    "%" + criteria.getOrganisationRegistrationNo().toLowerCase() + "%");
            specification = specification == null ? orgRegNoSpec : specification.and(orgRegNoSpec);
        }

        if (StringUtils.isNotBlank(criteria.getOrganisatonCode())) {

            Specification<KycInvoice> orgCodeSpec = (root, query, cb) -> cb.like(
                    cb.lower(root.get("organisation").get("code")),
                    "%" + criteria.getOrganisatonCode().toLowerCase() + "%");
            specification = specification == null ? orgCodeSpec : specification.and(orgCodeSpec);
        }

        if (StringUtils.isNotBlank(criteria.getOrganisatonId())) {

            Specification<KycInvoice> orgIdSpec = (root, query, cb) -> cb.equal(root.get("organisationId"),
                    UUID.fromString(criteria.getOrganisatonId())) ;
            specification = specification == null ? orgIdSpec : specification.and(orgIdSpec);
        }

        return specification;
    }

    /**
     * @see bw.co.centralkyc.invoice.KycInvoiceService#search(String)
     */
    @Override
    protected Collection<KycInvoiceDTO> handleSearch(InvoiceSearchCriteria criteria,
            Set<PropertySearchOrder> sortOrders)
            throws Exception {

        if (sortOrders == null || sortOrders.isEmpty()) {
            PropertySearchOrder def = new PropertySearchOrder();
            def.setOrder(SortOrder.ASC);
            def.setPropertyName("createdAt");
            sortOrders = Set.of(def);
        }

        Specification<KycInvoice> specification = createSearchSpecification(criteria);

        Collection<KycInvoice> invoices = specification == null ?
                this.kycInvoiceRepository.findAll(SortOrderFactory.createSortOrder(sortOrders)) :
                this.kycInvoiceRepository.findAll(specification, SortOrderFactory.createSortOrder(sortOrders));
        return this.kycInvoiceDao.toKycInvoiceDTOCollection(invoices);
    }

    /**
     * @see bw.co.centralkyc.invoice.KycInvoiceService#getAll(Integer, Integer)
     */
    @Override
    protected Page<KycInvoiceDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {

        PropertySearchOrder def = new PropertySearchOrder();
        def.setOrder(SortOrder.ASC);
        def.setPropertyName("createdAt");
        Set<PropertySearchOrder> sortOrders = Set.of(def);

        PageRequest page = PageRequest.of(pageNumber, pageSize,
                SortOrderFactory.createSortOrder(sortOrders));

        Page<KycInvoice> invoicePage = this.kycInvoiceRepository.findAll(page);

        return invoicePage.map(kycInvoice -> this.kycInvoiceDao.toKycInvoiceDTO(kycInvoice));
    }

    /**
     * @see bw.co.centralkyc.invoice.KycInvoiceService#search(String, Integer,
     *      Integer)
     */
    @Override
    protected Page<KycInvoiceDTO> handleSearch(SearchObject<InvoiceSearchCriteria> criteria)
            throws Exception {
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

        Specification<KycInvoice> specification = createSearchSpecification(criteria.getCriteria());

        Page<KycInvoice> invoicePage = specification == null ?
                this.kycInvoiceRepository.findAll(page) : 
                this.kycInvoiceRepository.findAll(specification, page);

        return invoicePage.map(kycInvoice -> this.kycInvoiceDao.toKycInvoiceDTO(kycInvoice));

    }

    @Override
    protected KycInvoiceDTO handleUpload(String id, String uploadUrl, UploadPurpose purpose, String user)
            throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleUpload'");
    }

    private String createInvoiceRef() {

        SequenceGenerator sequenceGenerator = sequenceGeneratorService.findByName(SEQUENCE_NAME);

        if (sequenceGenerator == null) {

            sequenceGenerator = new SequenceGenerator();
            sequenceGenerator.setName(SEQUENCE_NAME);

            Collection<SequencePart> sequenceParts = new HashSet<SequencePart>();

            SequencePart counterPart = new SequencePart();
            counterPart.setPosition(0);
            counterPart.setType(SequencePartType.STATIC);
            counterPart.setInitialValue("INV-");
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
            sequenceGenerator = sequenceGeneratorService.save(sequenceGenerator);
        }

        String nextRef = sequenceGeneratorService.generateNextSequenceValue(SEQUENCE_NAME, true);
        return nextRef;
    }

    @Override
    protected Collection<KycInvoiceDTO> handleGenerateInvoice(String subscriptionId, String user) throws Exception {

        KycSubscription subscription = this.kycSubscriptionRepository.findById(UUID.fromString(subscriptionId)).orElseThrow(
                () -> new KycRecordServiceException(
                        String.format("Subscription with id %s not found", subscriptionId)));

        if(subscription.getStatus() != KycSubsciptionStatus.ACTIVE) {
            throw new KycRecordServiceException(
                String.format("Cannot generate invoice for subscription with ref %s because it is not active", subscription.getRef()));
        }

        KycInvoice invoice = KycInvoice.Factory.newInstance();
        invoice.setKycSubscription(subscription);
        invoice.setOrganisation(subscription.getOrganisation());
        invoice.setAmount(subscription.getAmount());
        invoice.setPaid(false);

        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setCreatedBy(user);

        invoice.setRef(createInvoiceRef());
        invoice.setIssueDate(new Date());

        switch (subscription.getPeriod()) {
            case MONTH:
                // set start date to the beginning of the current month
                LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
                invoice.setStartDate(firstDayOfMonth);

                LocalDate lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1);
                invoice.setEndDate(lastDayOfMonth);
                break;
            case WEEK:
                throw new UnsupportedOperationException("Weekly subscriptions are not supported yet");
                // break;
            case YEAR:
                LocalDate firstDayOfYear = LocalDate.now().withDayOfYear(1);
                LocalDate lastDayOfYear = firstDayOfYear.plusYears(1).minusDays(1);
                invoice.setStartDate(firstDayOfYear);
                invoice.setEndDate(lastDayOfYear);
                break;
        
            default:
                break;
        }

        invoice = this.kycInvoiceRepository.save(invoice);

        return kycInvoiceRepository.findInvoicesBySubscriptionId(UUID.fromString(subscriptionId));
    }

    @Override
    protected Collection<KycInvoiceDTO> handleFindByOrganisation(String organisationId) throws Exception {

        Specification<KycInvoice> specification = (root, query, cb) -> cb.equal(
                root.get("organisation").get("id"),
                UUID.fromString(organisationId));

        Collection<KycInvoice> invoices = this.kycInvoiceRepository.findAll(specification);

        return this.kycInvoiceDao.toKycInvoiceDTOCollection(invoices);
    }

    @Override
    protected Collection<KycInvoiceDTO> handleFindBySubscription(String subscriptionId) throws Exception {

        Specification<KycInvoice> specification = (root, query, cb) -> cb.equal(
                root.get("kycSubscription").get("id"),
                UUID.fromString(subscriptionId));

        Collection<KycInvoice> invoices = this.kycInvoiceRepository.findAll(specification);

        return this.kycInvoiceDao.toKycInvoiceDTOCollection(invoices);

    }

    @Override
    protected Page<KycInvoiceDTO> handleFindByOrganisation(String organisationId, Integer pageNumber,
            Integer pageSize) throws Exception {

        return kycInvoiceRepository.findInvoicesByOrganisationId(UUID.fromString(organisationId), PageRequest.of(pageNumber, pageSize));
    }

    @Override
    protected Page<KycInvoiceDTO> handleFindBySubscription(String subscriptionId, Integer pageNumber,
            Integer pageSize) throws Exception {
        
        return kycInvoiceRepository.findInvoicesBySubscriptionId(UUID.fromString(subscriptionId), PageRequest.of(pageNumber, pageSize));
    }

    @Override
    protected Long handleCountInvoicesByOrganisationId(String organisationId) throws Exception {
        
        return kycInvoiceRepository.countInvoicesByOrganisationId(UUID.fromString(organisationId)).orElse(0L);
    }

    @Override
    protected Long handleCountInvoices(Boolean paid) throws Exception {
        
        return kycInvoiceRepository.countInvoices(paid).orElse(0L);
    }

    @Override
    protected Long handleCountOrganisationInvoices(Boolean paid, String organisationId) throws Exception {
        
        return kycInvoiceRepository.countOrganisationInvoices(paid, UUID.fromString(organisationId)).orElse(0L);
    }

    @Override
    protected Long handleCount() throws Exception {
        
        return kycInvoiceRepository.count();
    }

}