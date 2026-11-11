package bw.co.knowvera.individual.employment;

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
 * Spring Service base class for <code>EmploymentRecordService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see EmploymentRecordService
 */
@CacheConfig(cacheNames = "employmentRecords")
public abstract class EmploymentRecordServiceBase
    implements EmploymentRecordService
{
    protected Logger logger = LoggerFactory.getLogger(EmploymentRecordService.class);
    protected final EmploymentRecordRepository employmentRecordRepository;
    protected final EmploymentRecordMapper employmentRecordMapper;

    public EmploymentRecordServiceBase(
        EmploymentRecordRepository employmentRecordRepository,
        EmploymentRecordMapper employmentRecordMapper    ) {
        this.employmentRecordRepository = employmentRecordRepository;
        this.employmentRecordMapper = employmentRecordMapper;
    }

    /**
     * Gets the reference to <code>employmentRecord</code>'s repository.
     * @return employmentRecordRepository
     */
    public EmploymentRecordRepository getEmploymentRecordRepository() {
        return employmentRecordRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public EmploymentRecordDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.employment.EmploymentRecordService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new EmploymentRecordServiceException(
                "Error performing 'EmploymentRecordService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for
EmploymentRecordService.findById(id)
     * @return EmploymentRecordDTO
     * @throws Exception
     */
    protected abstract EmploymentRecordDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public EmploymentRecordDTO save(final @Valid EmploymentRecordDTO employmentRecord)
    {
        if (employmentRecord == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.employment.EmploymentRecordService.save(@Valid EmploymentRecordDTO employmentRecord) - 'employmentRecord' can not be null");
        }
        if (employmentRecord.getPositions() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.employment.EmploymentRecordService.save(@Valid EmploymentRecordDTO employmentRecord) - 'employmentRecord.positions' can not be null");
        }
        if (employmentRecord.getName() == null || employmentRecord.getName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.employment.EmploymentRecordService.save(@Valid EmploymentRecordDTO employmentRecord) - 'employmentRecord.name' can not be null or empty");
        }
        if (employmentRecord.getIdentityNo() == null || employmentRecord.getIdentityNo().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.employment.EmploymentRecordService.save(@Valid EmploymentRecordDTO employmentRecord) - 'employmentRecord.identityNo' can not be null or empty");
        }
        try
        {
            return this.handleSave(employmentRecord);
        }
        catch (Throwable th)
        {
            throw new EmploymentRecordServiceException(
                "Error performing 'EmploymentRecordService.save(@Valid EmploymentRecordDTO employmentRecord)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid EmploymentRecordDTO)}
     * @param employmentRecord EmploymentRecordDTO TODO: Model Documentation for
EmploymentRecordService.save(employmentRecord)
     * @return EmploymentRecordDTO
     * @throws Exception
     */
    protected abstract EmploymentRecordDTO handleSave(@Valid EmploymentRecordDTO employmentRecord)
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
                "bw.co.knowvera.individual.employment.EmploymentRecordService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new EmploymentRecordServiceException(
                "Error performing 'EmploymentRecordService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for
EmploymentRecordService.remove(id)
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
    public List<EmploymentRecordDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new EmploymentRecordServiceException(
                "Error performing 'EmploymentRecordService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<EmploymentRecordDTO>
     * @throws Exception
     */
    protected abstract List<EmploymentRecordDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmploymentRecordDTO> search(final String criteria)
    {
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new EmploymentRecordServiceException(
                "Error performing 'EmploymentRecordService.search(String criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(String)}
     * @param criteria java.lang.String TODO: Model Documentation for
EmploymentRecordService.search(criteria)
     * @return List<EmploymentRecordDTO>
     * @throws Exception
     */
    protected abstract List<EmploymentRecordDTO> handleSearch(String criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<EmploymentRecordDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new EmploymentRecordServiceException(
                "Error performing 'EmploymentRecordService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
EmploymentRecordService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
EmploymentRecordService.getAll(pageSize)
     * @return Page<EmploymentRecordDTO>
     * @throws Exception
     */
    protected abstract Page<EmploymentRecordDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<EmploymentRecordDTO> search(final String criteria, final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleSearch(criteria, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new EmploymentRecordServiceException(
                "Error performing 'EmploymentRecordService.search(String criteria, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(String, Integer, Integer)}
     * @param criteria java.lang.String TODO: Model Documentation for
EmploymentRecordService.search(criteria)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
EmploymentRecordService.search(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
EmploymentRecordService.search(pageSize)
     * @return Page<EmploymentRecordDTO>
     * @throws Exception
     */
    protected abstract Page<EmploymentRecordDTO> handleSearch(String criteria, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmploymentRecordDTO> findByIndividual(final String individualId)
    {
        if (individualId == null || individualId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.individual.employment.EmploymentRecordService.findByIndividual(String individualId) - 'individualId' can not be null or empty");
        }
        try
        {
            return this.handleFindByIndividual(individualId);
        }
        catch (Throwable th)
        {
            throw new EmploymentRecordServiceException(
                "Error performing 'EmploymentRecordService.findByIndividual(String individualId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByIndividual(String)}
     * @param individualId java.lang.String TODO: Model Documentation for
EmploymentRecordService.findByIndividual(individualId)
     * @return List<EmploymentRecordDTO>
     * @throws Exception
     */
    protected abstract List<EmploymentRecordDTO> handleFindByIndividual(String individualId)
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