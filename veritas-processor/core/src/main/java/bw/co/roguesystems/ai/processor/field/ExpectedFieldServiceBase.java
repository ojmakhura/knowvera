package bw.co.roguesystems.ai.processor.field;

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
 * Spring Service base class for <code>ExpectedFieldService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see ExpectedFieldService
 */
@CacheConfig(cacheNames = "expectedFields")
public abstract class ExpectedFieldServiceBase
    implements ExpectedFieldService
{
    protected Logger logger = LoggerFactory.getLogger(ExpectedFieldService.class);
    protected final ExpectedFieldRepository expectedFieldRepository;
    protected final ExpectedFieldMapper expectedFieldMapper;

    public ExpectedFieldServiceBase(
        ExpectedFieldRepository expectedFieldRepository,
        ExpectedFieldMapper expectedFieldMapper    ) {
        this.expectedFieldRepository = expectedFieldRepository;
        this.expectedFieldMapper = expectedFieldMapper;
    }

    /**
     * Gets the reference to <code>expectedField</code>'s repository.
     * @return expectedFieldRepository
     */
    public ExpectedFieldRepository getExpectedFieldRepository() {
        return expectedFieldRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public ExpectedFieldDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.roguesystems.ai.processor.field.ExpectedFieldService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new ExpectedFieldServiceException(
                "Error performing 'ExpectedFieldService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for ExpectedFieldService.findById(id)
     * @return ExpectedFieldDTO
     * @throws Exception
     */
    protected abstract ExpectedFieldDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"dataProcessors", "expectedFields"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public ExpectedFieldDTO save(final @Valid ExpectedFieldDTO expectedField)
    {
        if (expectedField == null)
        {
            throw new IllegalArgumentException(
                "bw.co.roguesystems.ai.processor.field.ExpectedFieldService.save(@Valid ExpectedFieldDTO expectedField) - 'expectedField' can not be null");
        }
        if (expectedField.getField() == null || expectedField.getField().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.roguesystems.ai.processor.field.ExpectedFieldService.save(@Valid ExpectedFieldDTO expectedField) - 'expectedField.field' can not be null or empty");
        }
        if (expectedField.getFieldType() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.roguesystems.ai.processor.field.ExpectedFieldService.save(@Valid ExpectedFieldDTO expectedField) - 'expectedField.fieldType' can not be null");
        }
        try
        {
            return this.handleSave(expectedField);
        }
        catch (Throwable th)
        {
            throw new ExpectedFieldServiceException(
                "Error performing 'ExpectedFieldService.save(@Valid ExpectedFieldDTO expectedField)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid ExpectedFieldDTO)}
     * @param expectedField ExpectedFieldDTO TODO: Model Documentation for
ExpectedFieldService.save(expectedField)
     * @return ExpectedFieldDTO
     * @throws Exception
     */
    protected abstract ExpectedFieldDTO handleSave(@Valid ExpectedFieldDTO expectedField)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"dataProcessors", "expectedFields"}, allEntries = true)
    public boolean remove(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.roguesystems.ai.processor.field.ExpectedFieldService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new ExpectedFieldServiceException(
                "Error performing 'ExpectedFieldService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for ExpectedFieldService.remove(id)
     * @return boolean
     * @throws Exception
     */
    protected abstract boolean handleRemove(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ExpectedFieldDTO> findByDataProcessor(final List<String> dataProcessorIds, final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleFindByDataProcessor(dataProcessorIds, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new ExpectedFieldServiceException(
                "Error performing 'ExpectedFieldService.findByDataProcessor(List<String> dataProcessorIds, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByDataProcessor(List<String>, Integer, Integer)}
     * @param dataProcessorIds java.lang.String TODO: Model Documentation for
ExpectedFieldService.findByDataProcessor(dataProcessorIds)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for
ExpectedFieldService.findByDataProcessor(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for
ExpectedFieldService.findByDataProcessor(pageSize)
     * @return Page<ExpectedFieldDTO>
     * @throws Exception
     */
    protected abstract Page<ExpectedFieldDTO> handleFindByDataProcessor(List<String> dataProcessorIds, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExpectedFieldDTO> findByDataProcessor(final List<String> dataProcessorIds)
    {
        try
        {
            return this.handleFindByDataProcessor(dataProcessorIds);
        }
        catch (Throwable th)
        {
            throw new ExpectedFieldServiceException(
                "Error performing 'ExpectedFieldService.findByDataProcessor(List<String> dataProcessorIds)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByDataProcessor(List<String>)}
     * @param dataProcessorIds java.lang.String TODO: Model Documentation for
ExpectedFieldService.findByDataProcessor(dataProcessorIds)
     * @return List<ExpectedFieldDTO>
     * @throws Exception
     */
    protected abstract List<ExpectedFieldDTO> handleFindByDataProcessor(List<String> dataProcessorIds)
        throws Exception;

    /**
     * Gets the current <code>principal</code> if one has been set,
     * otherwise returns <code>null</code>.
     *
     * @return the current principal
     */
    /**protected java.security.Principal getPrincipal()
    {
        return bw.co.roguesystems.ai.PrincipalStore.get();
    }*/


}