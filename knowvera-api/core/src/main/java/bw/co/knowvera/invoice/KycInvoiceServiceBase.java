package bw.co.knowvera.invoice;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.subscription.KycSubscriptionMapper;
import bw.co.knowvera.subscription.KycSubscriptionRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;

/**
 * <p>
 * Spring Service base class for <code>KycInvoiceService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see KycInvoiceService
 */
@CacheConfig(cacheNames = "invoices")
public abstract class KycInvoiceServiceBase
    implements KycInvoiceService
{
    protected Logger logger = LoggerFactory.getLogger(KycInvoiceService.class);
    protected final KycInvoiceRepository kycInvoiceRepository;
    protected final KycInvoiceMapper kycInvoiceMapper;
    protected final KycSubscriptionRepository kycSubscriptionRepository;
    protected final KycSubscriptionMapper kycSubscriptionMapper;

    public KycInvoiceServiceBase(
        KycInvoiceRepository kycInvoiceRepository,
        KycInvoiceMapper kycInvoiceMapper,         KycSubscriptionRepository kycSubscriptionRepository,
        KycSubscriptionMapper kycSubscriptionMapper    ) {
        this.kycInvoiceRepository = kycInvoiceRepository;
        this.kycInvoiceMapper = kycInvoiceMapper;
        this.kycSubscriptionRepository = kycSubscriptionRepository;
        this.kycSubscriptionMapper = kycSubscriptionMapper;
    }

    /**
     * Gets the reference to <code>kycInvoice</code>'s repository.
     * @return kycInvoiceRepository
     */
    public KycInvoiceRepository getKycInvoiceRepository() {
        return kycInvoiceRepository;
    }

    /**
     * Gets the reference to <code>kycSubscription</code>'s repository.
     * @return kycSubscriptionRepository
     */
    public KycSubscriptionRepository getKycSubscriptionRepository() {
        return kycSubscriptionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public KycInvoiceDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for KycInvoiceService.findById(id)
     * @return KycInvoiceDTO
     * @throws Exception
     */
    protected abstract KycInvoiceDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"invoices", "subscriptions"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public KycInvoiceDTO save(final @Valid KycInvoiceDTO invoice)
    {
        if (invoice == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.save(@Valid KycInvoiceDTO invoice) - 'invoice' can not be null");
        }
        if (invoice.getRef() == null || invoice.getRef().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.save(@Valid KycInvoiceDTO invoice) - 'invoice.ref' can not be null or empty");
        }
        if (invoice.getIssueDate() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.save(@Valid KycInvoiceDTO invoice) - 'invoice.issueDate' can not be null");
        }
        if (invoice.getOrganisationId() == null || invoice.getOrganisationId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.save(@Valid KycInvoiceDTO invoice) - 'invoice.organisationId' can not be null or empty");
        }
        if (invoice.getOrganisationCode() == null || invoice.getOrganisationCode().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.save(@Valid KycInvoiceDTO invoice) - 'invoice.organisationCode' can not be null or empty");
        }
        if (invoice.getOrganisationName() == null || invoice.getOrganisationName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.save(@Valid KycInvoiceDTO invoice) - 'invoice.organisationName' can not be null or empty");
        }
        if (invoice.getOrganisationRegistrationNo() == null || invoice.getOrganisationRegistrationNo().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.save(@Valid KycInvoiceDTO invoice) - 'invoice.organisationRegistrationNo' can not be null or empty");
        }
        if (invoice.getSubscriptionRef() == null || invoice.getSubscriptionRef().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.save(@Valid KycInvoiceDTO invoice) - 'invoice.subscriptionRef' can not be null or empty");
        }
        if (invoice.getSubscriptionId() == null || invoice.getSubscriptionId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.save(@Valid KycInvoiceDTO invoice) - 'invoice.subscriptionId' can not be null or empty");
        }
        if (invoice.getSubscriptionPeriod() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.save(@Valid KycInvoiceDTO invoice) - 'invoice.subscriptionPeriod' can not be null");
        }
        try
        {
            return this.handleSave(invoice);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.save(@Valid KycInvoiceDTO invoice)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid KycInvoiceDTO)}
     * @param invoice KycInvoiceDTO TODO: Model Documentation for KycInvoiceService.save(invoice)
     * @return KycInvoiceDTO
     * @throws Exception
     */
    protected abstract KycInvoiceDTO handleSave(@Valid KycInvoiceDTO invoice)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"invoices", "subscriptions"}, allEntries = true)
    public boolean remove(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for KycInvoiceService.remove(id)
     * @return boolean
     * @throws Exception
     */
    protected abstract boolean handleRemove(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "'all'")
    public List<KycInvoiceDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<KycInvoiceDTO>
     * @throws Exception
     */
    protected abstract List<KycInvoiceDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KycInvoiceDTO> search(final @Valid InvoiceSearchCriteria criteria, final @Valid Set<PropertySearchOrder> sortOrders)
    {
        try
        {
            return this.handleSearch(criteria, sortOrders);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.search(@Valid InvoiceSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortOrders)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(@Valid InvoiceSearchCriteria, @Valid Set<PropertySearchOrder>)}
     * @param criteria InvoiceSearchCriteria TODO: Model Documentation for KycInvoiceService.search(criteria)
     * @param sortOrders PropertySearchOrder TODO: Model Documentation for KycInvoiceService.search(sortOrders)
     * @return List<KycInvoiceDTO>
     * @throws Exception
     */
    protected abstract List<KycInvoiceDTO> handleSearch(@Valid InvoiceSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortOrders)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<KycInvoiceDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for KycInvoiceService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for KycInvoiceService.getAll(pageSize)
     * @return Page<KycInvoiceDTO>
     * @throws Exception
     */
    protected abstract Page<KycInvoiceDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<KycInvoiceDTO> search(final SearchObject<InvoiceSearchCriteria> criteria)
    {
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.search(SearchObject<InvoiceSearchCriteria> criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(SearchObject<InvoiceSearchCriteria>)}
     * @param criteria SearchObject<InvoiceSearchCriteria> TODO: Model Documentation for KycInvoiceService.search(criteria)
     * @return Page<KycInvoiceDTO>
     * @throws Exception
     */
    protected abstract Page<KycInvoiceDTO> handleSearch(SearchObject<InvoiceSearchCriteria> criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"invoices", "subscriptions"}, allEntries = true)
    public KycInvoiceDTO upload(final String id, final String uploadUrl, final UploadPurpose purpose, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.upload(String id, String uploadUrl, UploadPurpose purpose, String user) - 'id' can not be null or empty");
        }
        if (uploadUrl == null || uploadUrl.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.upload(String id, String uploadUrl, UploadPurpose purpose, String user) - 'uploadUrl' can not be null or empty");
        }
        if (purpose == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.upload(String id, String uploadUrl, UploadPurpose purpose, String user) - 'purpose' can not be null");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.upload(String id, String uploadUrl, UploadPurpose purpose, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleUpload(id, uploadUrl, purpose, user);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.upload(String id, String uploadUrl, UploadPurpose purpose, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #upload(String, String, UploadPurpose, String)}
     * @param id java.lang.String TODO: Model Documentation for KycInvoiceService.upload(id)
     * @param uploadUrl java.lang.String TODO: Model Documentation for KycInvoiceService.upload(uploadUrl)
     * @param purpose UploadPurpose TODO: Model Documentation for KycInvoiceService.upload(purpose)
     * @param user java.lang.String TODO: Model Documentation for KycInvoiceService.upload(user)
     * @return KycInvoiceDTO
     * @throws Exception
     */
    protected abstract KycInvoiceDTO handleUpload(String id, String uploadUrl, UploadPurpose purpose, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"invoices", "subscriptions"}, allEntries = true)
    public List<KycInvoiceDTO> generateInvoice(final String subscriptionId, final String user)
    {
        if (subscriptionId == null || subscriptionId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.generateInvoice(String subscriptionId, String user) - 'subscriptionId' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.generateInvoice(String subscriptionId, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleGenerateInvoice(subscriptionId, user);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.generateInvoice(String subscriptionId, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #generateInvoice(String, String)}
     * @param subscriptionId java.lang.String TODO: Model Documentation for
KycInvoiceService.generateInvoice(subscriptionId)
     * @param user java.lang.String TODO: Model Documentation for KycInvoiceService.generateInvoice(user)
     * @return List<KycInvoiceDTO>
     * @throws Exception
     */
    protected abstract List<KycInvoiceDTO> handleGenerateInvoice(String subscriptionId, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KycInvoiceDTO> findByOrganisation(final String organisationId)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.findByOrganisation(String organisationId) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleFindByOrganisation(organisationId);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.findByOrganisation(String organisationId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByOrganisation(String)}
     * @param organisationId java.lang.String TODO: Model Documentation for
KycInvoiceService.findByOrganisation(organisationId)
     * @return List<KycInvoiceDTO>
     * @throws Exception
     */
    protected abstract List<KycInvoiceDTO> handleFindByOrganisation(String organisationId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KycInvoiceDTO> findBySubscription(final String subscriptionId)
    {
        if (subscriptionId == null || subscriptionId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.findBySubscription(String subscriptionId) - 'subscriptionId' can not be null or empty");
        }
        try
        {
            return this.handleFindBySubscription(subscriptionId);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.findBySubscription(String subscriptionId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findBySubscription(String)}
     * @param subscriptionId java.lang.String TODO: Model Documentation for
KycInvoiceService.findBySubscription(subscriptionId)
     * @return List<KycInvoiceDTO>
     * @throws Exception
     */
    protected abstract List<KycInvoiceDTO> handleFindBySubscription(String subscriptionId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<KycInvoiceDTO> findByOrganisation(final String organisationId, final Integer pageNumber, final Integer pageSize)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleFindByOrganisation(organisationId, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByOrganisation(String, Integer, Integer)}
     * @param organisationId java.lang.String TODO: Model Documentation for
KycInvoiceService.findByOrganisation(organisationId)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
KycInvoiceService.findByOrganisation(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for KycInvoiceService.findByOrganisation(pageSize)
     * @return Page<KycInvoiceDTO>
     * @throws Exception
     */
    protected abstract Page<KycInvoiceDTO> handleFindByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<KycInvoiceDTO> findBySubscription(final String subscriptionId, final Integer pageNumber, final Integer pageSize)
    {
        if (subscriptionId == null || subscriptionId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.findBySubscription(String subscriptionId, Integer pageNumber, Integer pageSize) - 'subscriptionId' can not be null or empty");
        }
        try
        {
            return this.handleFindBySubscription(subscriptionId, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.findBySubscription(String subscriptionId, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findBySubscription(String, Integer, Integer)}
     * @param subscriptionId java.lang.String TODO: Model Documentation for
KycInvoiceService.findBySubscription(subscriptionId)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
KycInvoiceService.findBySubscription(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for KycInvoiceService.findBySubscription(pageSize)
     * @return Page<KycInvoiceDTO>
     * @throws Exception
     */
    protected abstract Page<KycInvoiceDTO> handleFindBySubscription(String subscriptionId, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countInvoicesByOrganisationId(final String organisationId)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.countInvoicesByOrganisationId(String organisationId) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleCountInvoicesByOrganisationId(organisationId);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.countInvoicesByOrganisationId(String organisationId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countInvoicesByOrganisationId(String)}
     * @param organisationId java.lang.String TODO: Model Documentation for
KycInvoiceService.countInvoicesByOrganisationId(organisationId)
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountInvoicesByOrganisationId(String organisationId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countInvoices(final Boolean paid)
    {
        try
        {
            return this.handleCountInvoices(paid);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.countInvoices(Boolean paid)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countInvoices(Boolean)}
     * @param paid java.lang.Boolean TODO: Model Documentation for KycInvoiceService.countInvoices(paid)
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountInvoices(Boolean paid)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countOrganisationInvoices(final Boolean paid, final String organisationId)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.invoice.KycInvoiceService.countOrganisationInvoices(Boolean paid, String organisationId) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleCountOrganisationInvoices(paid, organisationId);
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.countOrganisationInvoices(Boolean paid, String organisationId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countOrganisationInvoices(Boolean, String)}
     * @param paid java.lang.Boolean TODO: Model Documentation for
KycInvoiceService.countOrganisationInvoices(paid)
     * @param organisationId java.lang.String TODO: Model Documentation for
KycInvoiceService.countOrganisationInvoices(organisationId)
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountOrganisationInvoices(Boolean paid, String organisationId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long count()
    {
        try
        {
            return this.handleCount();
        }
        catch (Throwable th)
        {
            throw new KycInvoiceServiceException(
                "Error performing 'KycInvoiceService.count()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #count()}
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCount()
        throws Exception;

    /**
     * Gets the current <code>principal</code> if one has been set,
     * otherwise returns <code>null</code>.
     *
     * @return the current principal
     */
    /**protected java.security.Principal getPrincipal()
    {
        return bw.co.knowvera.PrincipalStore.get();
    }*/


}