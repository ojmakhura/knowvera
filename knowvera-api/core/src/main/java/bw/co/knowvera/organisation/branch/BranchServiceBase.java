package bw.co.knowvera.organisation.branch;

import jakarta.validation.Valid;
import java.util.List;
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
 * Spring Service base class for <code>BranchService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see BranchService
 */
@CacheConfig(cacheNames = "branches")
public abstract class BranchServiceBase
    implements BranchService
{
    protected Logger logger = LoggerFactory.getLogger(BranchService.class);
    protected final BranchRepository branchRepository;
    protected final BranchMapper branchMapper;

    public BranchServiceBase(
        BranchRepository branchRepository,
        BranchMapper branchMapper    ) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    }

    /**
     * Gets the reference to <code>branch</code>'s repository.
     * @return branchRepository
     */
    public BranchRepository getBranchRepository() {
        return branchRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public BranchDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.branch.BranchService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new BranchServiceException(
                "Error performing 'BranchService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for BranchService.findById(id)
     * @return BranchDTO
     * @throws Exception
     */
    protected abstract BranchDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public BranchDTO save(final @Valid BranchDTO branch)
    {
        if (branch == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.branch.BranchService.save(@Valid BranchDTO branch) - 'branch' can not be null");
        }
        if (branch.getCode() == null || branch.getCode().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.branch.BranchService.save(@Valid BranchDTO branch) - 'branch.code' can not be null or empty");
        }
        if (branch.getName() == null || branch.getName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.branch.BranchService.save(@Valid BranchDTO branch) - 'branch.name' can not be null or empty");
        }
        if (branch.getOrganisationId() == null || branch.getOrganisationId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.branch.BranchService.save(@Valid BranchDTO branch) - 'branch.organisationId' can not be null or empty");
        }
        if (branch.getOrganisation() == null || branch.getOrganisation().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.branch.BranchService.save(@Valid BranchDTO branch) - 'branch.organisation' can not be null or empty");
        }
        try
        {
            return this.handleSave(branch);
        }
        catch (Throwable th)
        {
            throw new BranchServiceException(
                "Error performing 'BranchService.save(@Valid BranchDTO branch)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid BranchDTO)}
     * @param branch BranchDTO TODO: Model Documentation for BranchService.save(branch)
     * @return BranchDTO
     * @throws Exception
     */
    protected abstract BranchDTO handleSave(@Valid BranchDTO branch)
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
                "bw.co.knowvera.organisation.branch.BranchService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new BranchServiceException(
                "Error performing 'BranchService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for BranchService.remove(id)
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
    public List<BranchDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new BranchServiceException(
                "Error performing 'BranchService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<BranchDTO>
     * @throws Exception
     */
    protected abstract List<BranchDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BranchDTO> search(final String criteria)
    {
        if (criteria == null || criteria.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.branch.BranchService.search(String criteria) - 'criteria' can not be null or empty");
        }
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new BranchServiceException(
                "Error performing 'BranchService.search(String criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(String)}
     * @param criteria java.lang.String TODO: Model Documentation for BranchService.search(criteria)
     * @return List<BranchDTO>
     * @throws Exception
     */
    protected abstract List<BranchDTO> handleSearch(String criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<BranchDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new BranchServiceException(
                "Error performing 'BranchService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for BranchService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for BranchService.getAll(pageSize)
     * @return Page<BranchDTO>
     * @throws Exception
     */
    protected abstract Page<BranchDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<BranchDTO> search(final String criteria, final Integer pageNumber, final Integer pageSize)
    {
        if (criteria == null || criteria.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.branch.BranchService.search(String criteria, Integer pageNumber, Integer pageSize) - 'criteria' can not be null or empty");
        }
        try
        {
            return this.handleSearch(criteria, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new BranchServiceException(
                "Error performing 'BranchService.search(String criteria, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(String, Integer, Integer)}
     * @param criteria java.lang.String TODO: Model Documentation for BranchService.search(criteria)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for BranchService.search(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for BranchService.search(pageSize)
     * @return Page<BranchDTO>
     * @throws Exception
     */
    protected abstract Page<BranchDTO> handleSearch(String criteria, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BranchDTO> findByOrganisation(final String organisationId)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.branch.BranchService.findByOrganisation(String organisationId) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleFindByOrganisation(organisationId);
        }
        catch (Throwable th)
        {
            throw new BranchServiceException(
                "Error performing 'BranchService.findByOrganisation(String organisationId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByOrganisation(String)}
     * @param organisationId java.lang.String TODO: Model Documentation for
BranchService.findByOrganisation(organisationId)
     * @return List<BranchDTO>
     * @throws Exception
     */
    protected abstract List<BranchDTO> handleFindByOrganisation(String organisationId)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<BranchDTO> findByOrganisation(final String organisationId, final Integer pageNumber, final Integer pageSize)
    {
        if (organisationId == null || organisationId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.organisation.branch.BranchService.findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize) - 'organisationId' can not be null or empty");
        }
        try
        {
            return this.handleFindByOrganisation(organisationId, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new BranchServiceException(
                "Error performing 'BranchService.findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByOrganisation(String, Integer, Integer)}
     * @param organisationId java.lang.String TODO: Model Documentation for
BranchService.findByOrganisation(organisationId)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
BranchService.findByOrganisation(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
BranchService.findByOrganisation(pageSize)
     * @return Page<BranchDTO>
     * @throws Exception
     */
    protected abstract Page<BranchDTO> handleFindByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)
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