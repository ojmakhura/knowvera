package bw.co.roguesystems.ai.processor;

import bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

/**
 * <p>
 * Spring Service base class for <code>DataProcessorService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see DataProcessorService
 */
@CacheConfig(cacheNames = "dataProcessors")
public abstract class DataProcessorServiceBase
    implements DataProcessorService
{
    protected Logger logger = LoggerFactory.getLogger(DataProcessorService.class);
    protected final DataProcessorRepository dataProcessorRepository;
    protected final DataProcessorMapper dataProcessorMapper;

    public DataProcessorServiceBase(
        DataProcessorRepository dataProcessorRepository,
        DataProcessorMapper dataProcessorMapper    ) {
        this.dataProcessorRepository = dataProcessorRepository;
        this.dataProcessorMapper = dataProcessorMapper;
    }

    /**
     * Gets the reference to <code>dataProcessor</code>'s repository.
     * @return dataProcessorRepository
     */
    public DataProcessorRepository getDataProcessorRepository() {
        return dataProcessorRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public DataProcessorDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.roguesystems.ai.processor.DataProcessorService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new DataProcessorServiceException(
                "Error performing 'DataProcessorService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for DataProcessorService.findById(id)
     * @return DataProcessorDTO
     * @throws Exception
     */
    protected abstract DataProcessorDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"dataProcessors", "expectedFields"}, allEntries = true)
    public DataProcessorDTO save(final @Valid DataProcessorDTO dataProcessor)
    {
        if (dataProcessor == null)
        {
            throw new IllegalArgumentException(
                "bw.co.roguesystems.ai.processor.DataProcessorService.save(@Valid DataProcessorDTO dataProcessor) - 'dataProcessor' can not be null");
        }
        if (dataProcessor.getCode() == null || dataProcessor.getCode().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.roguesystems.ai.processor.DataProcessorService.save(@Valid DataProcessorDTO dataProcessor) - 'dataProcessor.code' can not be null or empty");
        }
        if (dataProcessor.getName() == null || dataProcessor.getName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.roguesystems.ai.processor.DataProcessorService.save(@Valid DataProcessorDTO dataProcessor) - 'dataProcessor.name' can not be null or empty");
        }
        try
        {
            return this.handleSave(dataProcessor);
        }
        catch (Throwable th)
        {
            throw new DataProcessorServiceException(
                "Error performing 'DataProcessorService.save(@Valid DataProcessorDTO dataProcessor)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid DataProcessorDTO)}
     * @param dataProcessor DataProcessorDTO TODO: Model Documentation for DataProcessorService.save(dataProcessor)
     * @return DataProcessorDTO
     * @throws Exception
     */
    protected abstract DataProcessorDTO handleSave(@Valid DataProcessorDTO dataProcessor)
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
                "bw.co.roguesystems.ai.processor.DataProcessorService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new DataProcessorServiceException(
                "Error performing 'DataProcessorService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for DataProcessorService.remove(id)
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
    public List<DataProcessorDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new DataProcessorServiceException(
                "Error performing 'DataProcessorService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<DataProcessorDTO>
     * @throws Exception
     */
    protected abstract List<DataProcessorDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DataProcessorDTO> search(final String criteria)
    {
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new DataProcessorServiceException(
                "Error performing 'DataProcessorService.search(String criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(String)}
     * @param criteria java.lang.String TODO: Model Documentation for DataProcessorService.search(criteria)
     * @return List<DataProcessorDTO>
     * @throws Exception
     */
    protected abstract List<DataProcessorDTO> handleSearch(String criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<DataProcessorDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new DataProcessorServiceException(
                "Error performing 'DataProcessorService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for DataProcessorService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for DataProcessorService.getAll(pageSize)
     * @return Page<DataProcessorDTO>
     * @throws Exception
     */
    protected abstract Page<DataProcessorDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<DataProcessorDTO> search(final String criteria, final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleSearch(criteria, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new DataProcessorServiceException(
                "Error performing 'DataProcessorService.search(String criteria, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(String, Integer, Integer)}
     * @param criteria java.lang.String TODO: Model Documentation for DataProcessorService.search(criteria)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for DataProcessorService.search(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for DataProcessorService.search(pageSize)
     * @return Page<DataProcessorDTO>
     * @throws Exception
     */
    protected abstract Page<DataProcessorDTO> handleSearch(String criteria, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"dataProcessors", "expectedFields"}, allEntries = true)
    public DataProcessorDTO addExpectedField(final String id, final @Valid Set<ExpectedFieldDTO> expectedFields)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.roguesystems.ai.processor.DataProcessorService.addExpectedField(String id, @Valid Set<ExpectedFieldDTO> expectedFields) - 'id' can not be null or empty");
        }
        if (expectedFields == null)
        {
            throw new IllegalArgumentException(
                "bw.co.roguesystems.ai.processor.DataProcessorService.addExpectedField(String id, @Valid Set<ExpectedFieldDTO> expectedFields) - 'expectedFields' can not be null");
        }
        try
        {
            return this.handleAddExpectedField(id, expectedFields);
        }
        catch (Throwable th)
        {
            throw new DataProcessorServiceException(
                "Error performing 'DataProcessorService.addExpectedField(String id, @Valid Set<ExpectedFieldDTO> expectedFields)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #addExpectedField(String, @Valid Set<ExpectedFieldDTO>)}
     * @param id java.lang.String TODO: Model Documentation for DataProcessorService.addExpectedField(id)
     * @param expectedFields ExpectedFieldDTO TODO: Model Documentation for
DataProcessorService.addExpectedField(expectedFields)
     * @return DataProcessorDTO
     * @throws Exception
     */
    protected abstract DataProcessorDTO handleAddExpectedField(String id, @Valid Set<ExpectedFieldDTO> expectedFields)
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