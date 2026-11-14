package bw.co.knowvera.sequence;

import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Spring Service base class for <code>SequenceGeneratorService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see SequenceGeneratorService
 */
public abstract class SequenceGeneratorServiceBase
    implements SequenceGeneratorService
{
    protected Logger logger = LoggerFactory.getLogger(SequenceGeneratorService.class);
    protected final SequenceGeneratorRepository sequenceGeneratorRepository;
    protected final SequenceGeneratorMapper sequenceGeneratorMapper;

    public SequenceGeneratorServiceBase(
        SequenceGeneratorRepository sequenceGeneratorRepository,
        SequenceGeneratorMapper sequenceGeneratorMapper    ) {
        this.sequenceGeneratorRepository = sequenceGeneratorRepository;
        this.sequenceGeneratorMapper = sequenceGeneratorMapper;
    }

    /**
     * Gets the reference to <code>sequenceGenerator</code>'s repository.
     * @return sequenceGeneratorRepository
     */
    public SequenceGeneratorRepository getSequenceGeneratorRepository() {
        return sequenceGeneratorRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SequenceGeneratorDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.sequence.SequenceGeneratorService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new SequenceGeneratorServiceException(
                "Error performing 'SequenceGeneratorService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id String TODO: Model Documentation for SequenceGeneratorService.findById(id)
     * @return SequenceGeneratorDTO
     * @throws Exception
     */
    protected abstract SequenceGeneratorDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public SequenceGeneratorDTO save(final @Valid SequenceGeneratorDTO sequenceGenerator)
    {
        if (sequenceGenerator == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.sequence.SequenceGeneratorService.save(@Valid SequenceGeneratorDTO sequenceGenerator) - 'sequenceGenerator' can not be null");
        }
        if (sequenceGenerator.getId() == null || sequenceGenerator.getId().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.sequence.SequenceGeneratorService.save(@Valid SequenceGeneratorDTO sequenceGenerator) - 'sequenceGenerator.id' can not be null or empty");
        }
        if (sequenceGenerator.getName() == null || sequenceGenerator.getName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.sequence.SequenceGeneratorService.save(@Valid SequenceGeneratorDTO sequenceGenerator) - 'sequenceGenerator.name' can not be null or empty");
        }
        if (sequenceGenerator.getTargetEntity() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.sequence.SequenceGeneratorService.save(@Valid SequenceGeneratorDTO sequenceGenerator) - 'sequenceGenerator.targetEntity' can not be null");
        }
        try
        {
            return this.handleSave(sequenceGenerator);
        }
        catch (Throwable th)
        {
            throw new SequenceGeneratorServiceException(
                "Error performing 'SequenceGeneratorService.save(@Valid SequenceGeneratorDTO sequenceGenerator)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid SequenceGeneratorDTO)}
     * @param sequenceGenerator SequenceGeneratorDTO TODO: Model Documentation for
SequenceGeneratorService.save(sequenceGenerator)
     * @return SequenceGeneratorDTO
     * @throws Exception
     */
    protected abstract SequenceGeneratorDTO handleSave(@Valid SequenceGeneratorDTO sequenceGenerator)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.sequence.SequenceGeneratorService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new SequenceGeneratorServiceException(
                "Error performing 'SequenceGeneratorService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id String TODO: Model Documentation for SequenceGeneratorService.remove(id)
     * @return boolean
     * @throws Exception
     */
    protected abstract boolean handleRemove(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SequenceGeneratorDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new SequenceGeneratorServiceException(
                "Error performing 'SequenceGeneratorService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<SequenceGeneratorDTO>
     * @throws Exception
     */
    protected abstract List<SequenceGeneratorDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SequenceGeneratorDTO> search(final String criteria)
    {
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new SequenceGeneratorServiceException(
                "Error performing 'SequenceGeneratorService.search(String criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(String)}
     * @param criteria String TODO: Model Documentation for SequenceGeneratorService.search(criteria)
     * @return List<SequenceGeneratorDTO>
     * @throws Exception
     */
    protected abstract List<SequenceGeneratorDTO> handleSearch(String criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateNextSequenceValue(final String name, final Boolean createIfAbsent)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.sequence.SequenceGeneratorService.generateNextSequenceValue(String name, Boolean createIfAbsent) - 'name' can not be null or empty");
        }
        try
        {
            return this.handleGenerateNextSequenceValue(name, createIfAbsent);
        }
        catch (Throwable th)
        {
            throw new SequenceGeneratorServiceException(
                "Error performing 'SequenceGeneratorService.generateNextSequenceValue(String name, Boolean createIfAbsent)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #generateNextSequenceValue(String, Boolean)}
     * @param name String TODO: Model Documentation for
SequenceGeneratorService.generateNextSequenceValue(name)
     * @param createIfAbsent java.lang.Boolean TODO: Model Documentation for
SequenceGeneratorService.generateNextSequenceValue(createIfAbsent)
     * @return String
     * @throws Exception
     */
    protected abstract String handleGenerateNextSequenceValue(String name, Boolean createIfAbsent)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public SequenceGeneratorDTO findByName(final String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.sequence.SequenceGeneratorService.findByName(String name) - 'name' can not be null or empty");
        }
        try
        {
            return this.handleFindByName(name);
        }
        catch (Throwable th)
        {
            throw new SequenceGeneratorServiceException(
                "Error performing 'SequenceGeneratorService.findByName(String name)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByName(String)}
     * @param name String TODO: Model Documentation for SequenceGeneratorService.findByName(name)
     * @return SequenceGeneratorDTO
     * @throws Exception
     */
    protected abstract SequenceGeneratorDTO handleFindByName(String name)
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