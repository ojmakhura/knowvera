package bw.co.knowvera.subscription;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.invoice.KycInvoiceMapper;
import bw.co.knowvera.invoice.KycInvoiceRepository;
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
 * Spring Service base class for <code>KycSubscriptionService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see KycSubscriptionService
 */
@CacheConfig(cacheNames = "subscriptions")
public abstract class KycSubscriptionServiceBase
    implements KycSubscriptionService
{
    protected Logger logger = LoggerFactory.getLogger(KycSubscriptionService.class);
    protected final KycSubscriptionRepository kycSubscriptionRepository;
    protected final KycSubscriptionMapper kycSubscriptionMapper;
    protected final KycInvoiceRepository kycInvoiceRepository;
    protected final KycInvoiceMapper kycInvoiceMapper;

    public KycSubscriptionServiceBase(
        KycSubscriptionRepository kycSubscriptionRepository,
        KycSubscriptionMapper kycSubscriptionMapper,         KycInvoiceRepository kycInvoiceRepository,
        KycInvoiceMapper kycInvoiceMapper    ) {
        this.kycSubscriptionRepository = kycSubscriptionRepository;
        this.kycSubscriptionMapper = kycSubscriptionMapper;
        this.kycInvoiceRepository = kycInvoiceRepository;
        this.kycInvoiceMapper = kycInvoiceMapper;
    }

    /**
     * Gets the reference to <code>kycSubscription</code>'s repository.
     * @return kycSubscriptionRepository
     */
    public KycSubscriptionRepository getKycSubscriptionRepository() {
        return kycSubscriptionRepository;
    }

    /**
     * Gets the reference to <code>kycInvoice</code>'s repository.
     * @return kycInvoiceRepository
     */
    public KycInvoiceRepository getKycInvoiceRepository() {
        return kycInvoiceRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public KycSubscriptionDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.subscription.KycSubscriptionService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new KycSubscriptionServiceException(
                "Error performing 'KycSubscriptionService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for KycSubscriptionService.findById(id)
     * @return KycSubscriptionDTO
     * @throws Exception
     */
    protected abstract KycSubscriptionDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"subscriptions"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public KycSubscriptionDTO save(final @Valid KycSubscriptionDTO subscription)
    {
        if (subscription == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.subscription.KycSubscriptionService.save(@Valid KycSubscriptionDTO subscription) - 'subscription' can not be null");
        }
        if (subscription.getStartDate() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.subscription.KycSubscriptionService.save(@Valid KycSubscriptionDTO subscription) - 'subscription.startDate' can not be null");
        }
        if (subscription.getPeriod() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.subscription.KycSubscriptionService.save(@Valid KycSubscriptionDTO subscription) - 'subscription.period' can not be null");
        }
        if (subscription.getOrganisationCode() == null || subscription.getOrganisationCode().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.subscription.KycSubscriptionService.save(@Valid KycSubscriptionDTO subscription) - 'subscription.organisationCode' can not be null or empty");
        }
        if (subscription.getOrganisationName() == null || subscription.getOrganisationName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.subscription.KycSubscriptionService.save(@Valid KycSubscriptionDTO subscription) - 'subscription.organisationName' can not be null or empty");
        }
        if (subscription.getOrganisationRegistrationNo() == null || subscription.getOrganisationRegistrationNo().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.subscription.KycSubscriptionService.save(@Valid KycSubscriptionDTO subscription) - 'subscription.organisationRegistrationNo' can not be null or empty");
        }
        try
        {
            return this.handleSave(subscription);
        }
        catch (Throwable th)
        {
            throw new KycSubscriptionServiceException(
                "Error performing 'KycSubscriptionService.save(@Valid KycSubscriptionDTO subscription)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid KycSubscriptionDTO)}
     * @param subscription KycSubscriptionDTO TODO: Model Documentation for KycSubscriptionService.save(subscription)
     * @return KycSubscriptionDTO
     * @throws Exception
     */
    protected abstract KycSubscriptionDTO handleSave(@Valid KycSubscriptionDTO subscription)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"subscriptions"}, allEntries = true)
    public boolean remove(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.subscription.KycSubscriptionService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new KycSubscriptionServiceException(
                "Error performing 'KycSubscriptionService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for KycSubscriptionService.remove(id)
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
    public List<KycSubscriptionDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new KycSubscriptionServiceException(
                "Error performing 'KycSubscriptionService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<KycSubscriptionDTO>
     * @throws Exception
     */
    protected abstract List<KycSubscriptionDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KycSubscriptionDTO> search(final @Valid SubscriptionSearchCriteria criteria, final @Valid Set<PropertySearchOrder> sortOrders)
    {
        try
        {
            return this.handleSearch(criteria, sortOrders);
        }
        catch (Throwable th)
        {
            throw new KycSubscriptionServiceException(
                "Error performing 'KycSubscriptionService.search(@Valid SubscriptionSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortOrders)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(@Valid SubscriptionSearchCriteria, @Valid Set<PropertySearchOrder>)}
     * @param criteria SubscriptionSearchCriteria TODO: Model Documentation for KycSubscriptionService.search(criteria)
     * @param sortOrders PropertySearchOrder TODO: Model Documentation for KycSubscriptionService.search(sortOrders)
     * @return List<KycSubscriptionDTO>
     * @throws Exception
     */
    protected abstract List<KycSubscriptionDTO> handleSearch(@Valid SubscriptionSearchCriteria criteria, @Valid Set<PropertySearchOrder> sortOrders)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<KycSubscriptionDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new KycSubscriptionServiceException(
                "Error performing 'KycSubscriptionService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for KycSubscriptionService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for KycSubscriptionService.getAll(pageSize)
     * @return Page<KycSubscriptionDTO>
     * @throws Exception
     */
    protected abstract Page<KycSubscriptionDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<KycSubscriptionDTO> search(final SearchObject<SubscriptionSearchCriteria> criteria)
    {
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new KycSubscriptionServiceException(
                "Error performing 'KycSubscriptionService.search(SearchObject<SubscriptionSearchCriteria> criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(SearchObject<SubscriptionSearchCriteria>)}
     * @param criteria SearchObject<SubscriptionSearchCriteria> TODO: Model Documentation for KycSubscriptionService.search(criteria)
     * @return Page<KycSubscriptionDTO>
     * @throws Exception
     */
    protected abstract Page<KycSubscriptionDTO> handleSearch(SearchObject<SubscriptionSearchCriteria> criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KycSubscriptionDTO> findByOrganisation(final String organisationId, final String user)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.subscription.KycSubscriptionService.findByOrganisation(String organisationId, String user) - 'organisationId' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.subscription.KycSubscriptionService.findByOrganisation(String organisationId, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleFindByOrganisation(organisationId, user);
        }
        catch (Throwable th)
        {
            throw new KycSubscriptionServiceException(
                "Error performing 'KycSubscriptionService.findByOrganisation(String organisationId, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByOrganisation(String, String)}
     * @param organisationId java.lang.String TODO: Model Documentation for
KycSubscriptionService.findByOrganisation(organisationId)
     * @param user java.lang.String TODO: Model Documentation for
KycSubscriptionService.findByOrganisation(user)
     * @return List<KycSubscriptionDTO>
     * @throws Exception
     */
    protected abstract List<KycSubscriptionDTO> handleFindByOrganisation(String organisationId, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countByStatus(final KycSubsciptionStatus status)
    {
        if (status == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.subscription.KycSubscriptionService.countByStatus(KycSubsciptionStatus status) - 'status' can not be null");
        }
        try
        {
            return this.handleCountByStatus(status);
        }
        catch (Throwable th)
        {
            throw new KycSubscriptionServiceException(
                "Error performing 'KycSubscriptionService.countByStatus(KycSubsciptionStatus status)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countByStatus(KycSubsciptionStatus)}
     * @param status KycSubsciptionStatus TODO: Model Documentation for
KycSubscriptionService.countByStatus(status)
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountByStatus(KycSubsciptionStatus status)
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
            throw new KycSubscriptionServiceException(
                "Error performing 'KycSubscriptionService.count()' --> " + th,
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