package bw.co.knowvera.individual;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.SearchObject;
import bw.co.knowvera.kyc.KycComplianceStatus;
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
 * Spring Service base class for <code>IndividualService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see IndividualService
 */
@CacheConfig(cacheNames = "individuals")
public abstract class IndividualServiceBase
    implements IndividualService
{
    protected Logger logger = LoggerFactory.getLogger(IndividualService.class);
    protected final IndividualRepository individualRepository;
    protected final IndividualMapper individualMapper;

    public IndividualServiceBase(
        IndividualRepository individualRepository,
        IndividualMapper individualMapper    ) {
        this.individualRepository = individualRepository;
        this.individualMapper = individualMapper;
    }

    /**
     * Gets the reference to <code>individual</code>'s repository.
     * @return individualRepository
     */
    public IndividualRepository getIndividualRepository() {
        return individualRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public IndividualDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for IndividualService.findById(id)
     * @return IndividualDTO
     * @throws Exception
     */
    protected abstract IndividualDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "'all'")
    public List<IndividualListDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<IndividualListDTO>
     * @throws Exception
     */
    protected abstract List<IndividualListDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<IndividualListDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for IndividualService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for IndividualService.getAll(pageSize)
     * @return Page<IndividualListDTO>
     * @throws Exception
     */
    protected abstract Page<IndividualListDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public boolean remove(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for IndividualService.remove(id)
     * @return boolean
     * @throws Exception
     */
    protected abstract boolean handleRemove(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public IndividualDTO save(final @Valid IndividualDTO individual)
    {
        if (individual == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.save(@Valid IndividualDTO individual) - 'individual' can not be null");
        }
        if (individual.getFirstName() == null || individual.getFirstName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.save(@Valid IndividualDTO individual) - 'individual.firstName' can not be null or empty");
        }
        if (individual.getSurname() == null || individual.getSurname().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.save(@Valid IndividualDTO individual) - 'individual.surname' can not be null or empty");
        }
        if (individual.getIdentityNo() == null || individual.getIdentityNo().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.save(@Valid IndividualDTO individual) - 'individual.identityNo' can not be null or empty");
        }
        if (individual.getIdentityType() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.save(@Valid IndividualDTO individual) - 'individual.identityType' can not be null");
        }
        if (individual.getKycStatus() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.save(@Valid IndividualDTO individual) - 'individual.kycStatus' can not be null");
        }
        if (individual.getSex() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.save(@Valid IndividualDTO individual) - 'individual.sex' can not be null");
        }
        if (individual.getNationality() == null || individual.getNationality().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.save(@Valid IndividualDTO individual) - 'individual.nationality' can not be null or empty");
        }
        if (individual.getMaritalStatus() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.save(@Valid IndividualDTO individual) - 'individual.maritalStatus' can not be null");
        }
        if (individual.getEmploymentStatus() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.save(@Valid IndividualDTO individual) - 'individual.employmentStatus' can not be null");
        }
        try
        {
            return this.handleSave(individual);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.save(@Valid IndividualDTO individual)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid IndividualDTO)}
     * @param individual IndividualDTO TODO: Model Documentation for IndividualService.save(individual)
     * @return IndividualDTO
     * @throws Exception
     */
    protected abstract IndividualDTO handleSave(@Valid IndividualDTO individual)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IndividualListDTO> search(final @Valid IndividualSearchCriteria criteria, final @Valid Set<PropertySearchOrder> orderings)
    {
        try
        {
            return this.handleSearch(criteria, orderings);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.search(@Valid IndividualSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(@Valid IndividualSearchCriteria, @Valid Set<PropertySearchOrder>)}
     * @param criteria IndividualSearchCriteria TODO: Model Documentation for IndividualService.search(criteria)
     * @param orderings PropertySearchOrder TODO: Model Documentation for IndividualService.search(orderings)
     * @return List<IndividualListDTO>
     * @throws Exception
     */
    protected abstract List<IndividualListDTO> handleSearch(@Valid IndividualSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<IndividualListDTO> search(final SearchObject<IndividualSearchCriteria> criteria)
    {
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.search(SearchObject<IndividualSearchCriteria> criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(SearchObject<IndividualSearchCriteria>)}
     * @param criteria SearchObject<IndividualSearchCriteria> TODO: Model Documentation for IndividualService.search(criteria)
     * @return Page<IndividualListDTO>
     * @throws Exception
     */
    protected abstract Page<IndividualListDTO> handleSearch(SearchObject<IndividualSearchCriteria> criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IndividualListDTO> getOrganisationClients(final String organisationId)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.getOrganisationClients(String organisationId) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleGetOrganisationClients(organisationId);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.getOrganisationClients(String organisationId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getOrganisationClients(String)}
     * @param organisationId java.lang.String TODO: Model Documentation for
IndividualService.getOrganisationClients(organisationId)
     * @return List<IndividualListDTO>
     * @throws Exception
     */
    protected abstract List<IndividualListDTO> handleGetOrganisationClients(String organisationId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<IndividualListDTO> getOrganisationClients(final String organisationId, final Integer pageNumber, final Integer pageSize)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.getOrganisationClients(String organisationId, Integer pageNumber, Integer pageSize) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleGetOrganisationClients(organisationId, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.getOrganisationClients(String organisationId, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getOrganisationClients(String, Integer, Integer)}
     * @param organisationId java.lang.String TODO: Model Documentation for
IndividualService.getOrganisationClients(organisationId)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
IndividualService.getOrganisationClients(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
IndividualService.getOrganisationClients(pageSize)
     * @return Page<IndividualListDTO>
     * @throws Exception
     */
    protected abstract Page<IndividualListDTO> handleGetOrganisationClients(String organisationId, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public IndividualDTO findByIdentityNoAndIdentityType(final String identityNo, final IndividualIdentityType identityType)
    {
        if (identityNo == null || identityNo.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.findByIdentityNoAndIdentityType(String identityNo, IndividualIdentityType identityType) - 'identityNo' can not be null or empty");
        }
        if (identityType == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.findByIdentityNoAndIdentityType(String identityNo, IndividualIdentityType identityType) - 'identityType' can not be null");
        }
        try
        {
            return this.handleFindByIdentityNoAndIdentityType(identityNo, identityType);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.findByIdentityNoAndIdentityType(String identityNo, IndividualIdentityType identityType)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByIdentityNoAndIdentityType(String, IndividualIdentityType)}
     * @param identityNo java.lang.String TODO: Model Documentation for
IndividualService.findByIdentityNoAndIdentityType(identityNo)
     * @param identityType IndividualIdentityType TODO: Model Documentation for
IndividualService.findByIdentityNoAndIdentityType(identityType)
     * @return IndividualDTO
     * @throws Exception
     */
    protected abstract IndividualDTO handleFindByIdentityNoAndIdentityType(String identityNo, IndividualIdentityType identityType)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countByPepStatus(final PepStatus pepStatus)
    {
        if (pepStatus == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.countByPepStatus(PepStatus pepStatus) - 'pepStatus' can not be null");
        }
        try
        {
            return this.handleCountByPepStatus(pepStatus);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.countByPepStatus(PepStatus pepStatus)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countByPepStatus(PepStatus)}
     * @param pepStatus PepStatus TODO: Model Documentation for
IndividualService.countByPepStatus(pepStatus)
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountByPepStatus(PepStatus pepStatus)
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
            throw new IndividualServiceException(
                "Error performing 'IndividualService.count()' --> " + th,
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
     * {@inheritDoc}
     */
    @Override
    public Long countByKycStatus(final KycComplianceStatus kycStatus)
    {
        if (kycStatus == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.countByKycStatus(KycComplianceStatus kycStatus) - 'kycStatus' can not be null");
        }
        try
        {
            return this.handleCountByKycStatus(kycStatus);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.countByKycStatus(KycComplianceStatus kycStatus)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countByKycStatus(KycComplianceStatus)}
     * @param kycStatus KycComplianceStatus TODO: Model Documentation for
IndividualService.countByKycStatus(kycStatus)
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountByKycStatus(KycComplianceStatus kycStatus)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countByEmploymentStatus(final EmploymentStatus employmentStatus)
    {
        if (employmentStatus == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.countByEmploymentStatus(EmploymentStatus employmentStatus) - 'employmentStatus' can not be null");
        }
        try
        {
            return this.handleCountByEmploymentStatus(employmentStatus);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.countByEmploymentStatus(EmploymentStatus employmentStatus)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countByEmploymentStatus(EmploymentStatus)}
     * @param employmentStatus EmploymentStatus TODO: Model Documentation for
IndividualService.countByEmploymentStatus(employmentStatus)
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountByEmploymentStatus(EmploymentStatus employmentStatus)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countBySex(final Sex sex)
    {
        if (sex == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.countBySex(Sex sex) - 'sex' can not be null");
        }
        try
        {
            return this.handleCountBySex(sex);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.countBySex(Sex sex)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countBySex(Sex)}
     * @param sex Sex TODO: Model Documentation for IndividualService.countBySex(sex)
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountBySex(Sex sex)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public IndividualDTO loadRequestIndividual(final String requestId, final String identityConfirmationToken, final String identityNo)
    {
        if (requestId == null || requestId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.loadRequestIndividual(String requestId, String identityConfirmationToken, String identityNo) - 'requestId' can not be null or empty");
        }
        if (identityConfirmationToken == null || identityConfirmationToken.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.loadRequestIndividual(String requestId, String identityConfirmationToken, String identityNo) - 'identityConfirmationToken' can not be null or empty");
        }
        if (identityNo == null || identityNo.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.loadRequestIndividual(String requestId, String identityConfirmationToken, String identityNo) - 'identityNo' can not be null or empty");
        }
        try
        {
            return this.handleLoadRequestIndividual(requestId, identityConfirmationToken, identityNo);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.loadRequestIndividual(String requestId, String identityConfirmationToken, String identityNo)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #loadRequestIndividual(String, String, String)}
     * @param requestId java.lang.String TODO: Model Documentation for
IndividualService.loadRequestIndividual(requestId)
     * @param identityConfirmationToken java.lang.String TODO: Model Documentation for
IndividualService.loadRequestIndividual(identityConfirmationToken)
     * @param identityNo java.lang.String TODO: Model Documentation for
IndividualService.loadRequestIndividual(identityNo)
     * @return IndividualDTO
     * @throws Exception
     */
    protected abstract IndividualDTO handleLoadRequestIndividual(String requestId, String identityConfirmationToken, String identityNo)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public IndividualDTO findByUserId(final String userId)
    {
        if (userId == null || userId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.findByUserId(String userId) - 'userId' can not be null or empty");
        }
        try
        {
            return this.handleFindByUserId(userId);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.findByUserId(String userId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByUserId(String)}
     * @param userId java.lang.String TODO: Model Documentation for IndividualService.findByUserId(userId)
     * @return IndividualDTO
     * @throws Exception
     */
    protected abstract IndividualDTO handleFindByUserId(String userId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public IndividualDTO verifyIndividual(final String id, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.verifyIndividual(String id, String user) - 'id' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.IndividualService.verifyIndividual(String id, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleVerifyIndividual(id, user);
        }
        catch (Throwable th)
        {
            throw new IndividualServiceException(
                "Error performing 'IndividualService.verifyIndividual(String id, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #verifyIndividual(String, String)}
     * @param id java.lang.String TODO: Model Documentation for IndividualService.verifyIndividual(id)
     * @param user java.lang.String TODO: Model Documentation for IndividualService.verifyIndividual(user)
     * @return IndividualDTO
     * @throws Exception
     */
    protected abstract IndividualDTO handleVerifyIndividual(String id, String user)
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