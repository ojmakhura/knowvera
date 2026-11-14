package bw.co.knowvera.organisation;

import bw.co.knowvera.GeneralStatus;
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
 * Spring Service base class for <code>OrganisationService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see OrganisationService
 */
@CacheConfig(cacheNames = "organisations")
public abstract class OrganisationServiceBase
    implements OrganisationService
{
    protected Logger logger = LoggerFactory.getLogger(OrganisationService.class);
    protected final OrganisationRepository organisationRepository;
    protected final OrganisationMapper organisationMapper;

    public OrganisationServiceBase(
        OrganisationRepository organisationRepository,
        OrganisationMapper organisationMapper    ) {
        this.organisationRepository = organisationRepository;
        this.organisationMapper = organisationMapper;
    }

    /**
     * Gets the reference to <code>organisation</code>'s repository.
     * @return organisationRepository
     */
    public OrganisationRepository getOrganisationRepository() {
        return organisationRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public OrganisationDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for OrganisationService.findById(id)
     * @return OrganisationDTO
     * @throws Exception
     */
    protected abstract OrganisationDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "'all'")
    public List<OrganisationListDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<OrganisationListDTO>
     * @throws Exception
     */
    protected abstract List<OrganisationListDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<OrganisationListDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for OrganisationService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for OrganisationService.getAll(pageSize)
     * @return Page<OrganisationListDTO>
     * @throws Exception
     */
    protected abstract Page<OrganisationListDTO> handleGetAll(Integer pageNumber, Integer pageSize)
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
                "bw.co.knowvera.organisation.OrganisationService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for OrganisationService.remove(id)
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
    public OrganisationDTO save(final @Valid OrganisationDTO organisation)
    {
        if (organisation == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.save(@Valid OrganisationDTO organisation) - 'organisation' can not be null");
        }
        if (organisation.getCode() == null || organisation.getCode().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.save(@Valid OrganisationDTO organisation) - 'organisation.code' can not be null or empty");
        }
        if (organisation.getName() == null || organisation.getName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.save(@Valid OrganisationDTO organisation) - 'organisation.name' can not be null or empty");
        }
        if (organisation.getRegistrationNo() == null || organisation.getRegistrationNo().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.save(@Valid OrganisationDTO organisation) - 'organisation.registrationNo' can not be null or empty");
        }
        if (organisation.getStatus() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.save(@Valid OrganisationDTO organisation) - 'organisation.status' can not be null");
        }
        if (organisation.getDomains() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.save(@Valid OrganisationDTO organisation) - 'organisation.domains' can not be null");
        }
        if (organisation.getKycStatus() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.save(@Valid OrganisationDTO organisation) - 'organisation.kycStatus' can not be null");
        }
        if (organisation.getCountryOfRegistration() == null || organisation.getCountryOfRegistration().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.save(@Valid OrganisationDTO organisation) - 'organisation.countryOfRegistration' can not be null or empty");
        }
        try
        {
            return this.handleSave(organisation);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.save(@Valid OrganisationDTO organisation)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid OrganisationDTO)}
     * @param organisation OrganisationDTO TODO: Model Documentation for OrganisationService.save(organisation)
     * @return OrganisationDTO
     * @throws Exception
     */
    protected abstract OrganisationDTO handleSave(@Valid OrganisationDTO organisation)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrganisationListDTO> search(final @Valid OrganisationSearchCriteria criteria, final @Valid Set<PropertySearchOrder> orderings)
    {
        try
        {
            return this.handleSearch(criteria, orderings);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.search(@Valid OrganisationSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(@Valid OrganisationSearchCriteria, @Valid Set<PropertySearchOrder>)}
     * @param criteria OrganisationSearchCriteria TODO: Model Documentation for OrganisationService.search(criteria)
     * @param orderings PropertySearchOrder TODO: Model Documentation for OrganisationService.search(orderings)
     * @return List<OrganisationListDTO>
     * @throws Exception
     */
    protected abstract List<OrganisationListDTO> handleSearch(@Valid OrganisationSearchCriteria criteria, @Valid Set<PropertySearchOrder> orderings)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<OrganisationListDTO> search(final SearchObject<OrganisationSearchCriteria> criteria)
    {
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.search(SearchObject<OrganisationSearchCriteria> criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(SearchObject<OrganisationSearchCriteria>)}
     * @param criteria SearchObject<OrganisationSearchCriteria> TODO: Model Documentation for OrganisationService.search(criteria)
     * @return Page<OrganisationListDTO>
     * @throws Exception
     */
    protected abstract Page<OrganisationListDTO> handleSearch(SearchObject<OrganisationSearchCriteria> criteria)
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
                "bw.co.knowvera.organisation.OrganisationService.countByKycStatus(KycComplianceStatus kycStatus) - 'kycStatus' can not be null");
        }
        try
        {
            return this.handleCountByKycStatus(kycStatus);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.countByKycStatus(KycComplianceStatus kycStatus)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countByKycStatus(KycComplianceStatus)}
     * @param kycStatus KycComplianceStatus TODO: Model Documentation for
OrganisationService.countByKycStatus(kycStatus)
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountByKycStatus(KycComplianceStatus kycStatus)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countByStatus(final GeneralStatus status)
    {
        if (status == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.countByStatus(GeneralStatus status) - 'status' can not be null");
        }
        try
        {
            return this.handleCountByStatus(status);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.countByStatus(GeneralStatus status)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countByStatus(GeneralStatus)}
     * @param status GeneralStatus TODO: Model Documentation for OrganisationService.countByStatus(status)
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountByStatus(GeneralStatus status)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countByIsClientFalse()
    {
        try
        {
            return this.handleCountByIsClientFalse();
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.countByIsClientFalse()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countByIsClientFalse()}
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountByIsClientFalse()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countByIsClientTrue()
    {
        try
        {
            return this.handleCountByIsClientTrue();
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.countByIsClientTrue()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #countByIsClientTrue()}
     * @return Long
     * @throws Exception
     */
    protected abstract Long handleCountByIsClientTrue()
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
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.count()' --> " + th,
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
    public OrganisationDTO loadRequestOrganisation(final String requestId, final String identityConfirmationToken, final String registrationNo)
    {
        if (requestId == null || requestId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.loadRequestOrganisation(String requestId, String identityConfirmationToken, String registrationNo) - 'requestId' can not be null or empty");
        }
        if (identityConfirmationToken == null || identityConfirmationToken.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.loadRequestOrganisation(String requestId, String identityConfirmationToken, String registrationNo) - 'identityConfirmationToken' can not be null or empty");
        }
        if (registrationNo == null || registrationNo.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.loadRequestOrganisation(String requestId, String identityConfirmationToken, String registrationNo) - 'registrationNo' can not be null or empty");
        }
        try
        {
            return this.handleLoadRequestOrganisation(requestId, identityConfirmationToken, registrationNo);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.loadRequestOrganisation(String requestId, String identityConfirmationToken, String registrationNo)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #loadRequestOrganisation(String, String, String)}
     * @param requestId java.lang.String TODO: Model Documentation for
OrganisationService.loadRequestOrganisation(requestId)
     * @param identityConfirmationToken java.lang.String TODO: Model Documentation for
OrganisationService.loadRequestOrganisation(identityConfirmationToken)
     * @param registrationNo java.lang.String TODO: Model Documentation for
OrganisationService.loadRequestOrganisation(registrationNo)
     * @return OrganisationDTO
     * @throws Exception
     */
    protected abstract OrganisationDTO handleLoadRequestOrganisation(String requestId, String identityConfirmationToken, String registrationNo)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public OrganisationDTO findByCode(final String code)
    {
        if (code == null || code.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.findByCode(String code) - 'code' can not be null or empty");
        }
        try
        {
            return this.handleFindByCode(code);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.findByCode(String code)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByCode(String)}
     * @param code java.lang.String TODO: Model Documentation for OrganisationService.findByCode(code)
     * @return OrganisationDTO
     * @throws Exception
     */
    protected abstract OrganisationDTO handleFindByCode(String code)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public OrganisationDTO findByName(final String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.findByName(String name) - 'name' can not be null or empty");
        }
        try
        {
            return this.handleFindByName(name);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.findByName(String name)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByName(String)}
     * @param name java.lang.String TODO: Model Documentation for OrganisationService.findByName(name)
     * @return OrganisationDTO
     * @throws Exception
     */
    protected abstract OrganisationDTO handleFindByName(String name)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public OrganisationDTO findByRegistrationNo(final String registrationNo)
    {
        if (registrationNo == null || registrationNo.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.findByRegistrationNo(String registrationNo) - 'registrationNo' can not be null or empty");
        }
        try
        {
            return this.handleFindByRegistrationNo(registrationNo);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.findByRegistrationNo(String registrationNo)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByRegistrationNo(String)}
     * @param registrationNo java.lang.String TODO: Model Documentation for
OrganisationService.findByRegistrationNo(registrationNo)
     * @return OrganisationDTO
     * @throws Exception
     */
    protected abstract OrganisationDTO handleFindByRegistrationNo(String registrationNo)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public OrganisationDTO verifyOrganisation(final String id, final String user)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.verifyOrganisation(String id, String user) - 'id' can not be null or empty");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.verifyOrganisation(String id, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleVerifyOrganisation(id, user);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.verifyOrganisation(String id, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #verifyOrganisation(String, String)}
     * @param id java.lang.String TODO: Model Documentation for OrganisationService.verifyOrganisation(id)
     * @param user java.lang.String TODO: Model Documentation for
OrganisationService.verifyOrganisation(user)
     * @return OrganisationDTO
     * @throws Exception
     */
    protected abstract OrganisationDTO handleVerifyOrganisation(String id, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "'list:' + #id")
    public OrganisationListDTO findListOrganisationById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.findListOrganisationById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindListOrganisationById(id);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.findListOrganisationById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findListOrganisationById(String)}
     * @param id java.lang.String TODO: Model Documentation for
OrganisationService.findListOrganisationById(id)
     * @return OrganisationListDTO
     * @throws Exception
     */
    protected abstract OrganisationListDTO handleFindListOrganisationById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "'keycloak:' + #keycloakId")
    public OrganisationListDTO findListOrganisationByKeycloakId(final String keycloakId)
    {
        if (keycloakId == null || keycloakId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.OrganisationService.findListOrganisationByKeycloakId(String keycloakId) - 'keycloakId' can not be null or empty");
        }
        try
        {
            return this.handleFindListOrganisationByKeycloakId(keycloakId);
        }
        catch (Throwable th)
        {
            throw new OrganisationServiceException(
                "Error performing 'OrganisationService.findListOrganisationByKeycloakId(String keycloakId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findListOrganisationByKeycloakId(String)}
     * @param keycloakId java.lang.String TODO: Model Documentation for
OrganisationService.findListOrganisationByKeycloakId(keycloakId)
     * @return OrganisationListDTO
     * @throws Exception
     */
    protected abstract OrganisationListDTO handleFindListOrganisationByKeycloakId(String keycloakId)
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