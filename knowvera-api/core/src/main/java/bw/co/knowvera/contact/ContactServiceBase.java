package bw.co.knowvera.contact;

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
 * Spring Service base class for <code>ContactService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see ContactService
 */
@CacheConfig(cacheNames = "clientContacts")
public abstract class ContactServiceBase
    implements ContactService
{
    protected Logger logger = LoggerFactory.getLogger(ContactService.class);
    protected final ContactRepository contactRepository;
    protected final ContactMapper contactMapper;

    public ContactServiceBase(
        ContactRepository contactRepository,
        ContactMapper contactMapper    ) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    /**
     * Gets the reference to <code>contact</code>'s repository.
     * @return contactRepository
     */
    public ContactRepository getContactRepository() {
        return contactRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public ContactDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.contact.ContactService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new ContactServiceException(
                "Error performing 'ContactService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for ContactService.findById(id)
     * @return ContactDTO
     * @throws Exception
     */
    protected abstract ContactDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"clientContacts"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public ContactDTO save(final @Valid ContactDTO document)
    {
        if (document == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.contact.ContactService.save(@Valid ContactDTO document) - 'document' can not be null");
        }
        if (document.getMessage() == null || document.getMessage().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.contact.ContactService.save(@Valid ContactDTO document) - 'document.message' can not be null or empty");
        }
        if (document.getEmail() == null || document.getEmail().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.contact.ContactService.save(@Valid ContactDTO document) - 'document.email' can not be null or empty");
        }
        try
        {
            return this.handleSave(document);
        }
        catch (Throwable th)
        {
            throw new ContactServiceException(
                "Error performing 'ContactService.save(@Valid ContactDTO document)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid ContactDTO)}
     * @param document ContactDTO TODO: Model Documentation for ContactService.save(document)
     * @return ContactDTO
     * @throws Exception
     */
    protected abstract ContactDTO handleSave(@Valid ContactDTO document)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"clientContacts"}, allEntries = true)
    public boolean remove(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.contact.ContactService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new ContactServiceException(
                "Error performing 'ContactService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for ContactService.remove(id)
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
    public List<ContactDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new ContactServiceException(
                "Error performing 'ContactService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<ContactDTO>
     * @throws Exception
     */
    protected abstract List<ContactDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ContactDTO> search(final String criteria)
    {
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new ContactServiceException(
                "Error performing 'ContactService.search(String criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(String)}
     * @param criteria java.lang.String TODO: Model Documentation for ContactService.search(criteria)
     * @return List<ContactDTO>
     * @throws Exception
     */
    protected abstract List<ContactDTO> handleSearch(String criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ContactDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new ContactServiceException(
                "Error performing 'ContactService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for ContactService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for ContactService.getAll(pageSize)
     * @return Page<ContactDTO>
     * @throws Exception
     */
    protected abstract Page<ContactDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ContactDTO> findByType(final ContactType type)
    {
        if (type == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.contact.ContactService.findByType(ContactType type) - 'type' can not be null");
        }
        try
        {
            return this.handleFindByType(type);
        }
        catch (Throwable th)
        {
            throw new ContactServiceException(
                "Error performing 'ContactService.findByType(ContactType type)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByType(ContactType)}
     * @param type ContactType TODO: Model Documentation for ContactService.findByType(type)
     * @return List<ContactDTO>
     * @throws Exception
     */
    protected abstract List<ContactDTO> handleFindByType(ContactType type)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ContactDTO> findByType(final ContactType type, final Integer pageNumber, final Integer pageSize)
    {
        if (type == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.contact.ContactService.findByType(ContactType type, Integer pageNumber, Integer pageSize) - 'type' can not be null");
        }
        try
        {
            return this.handleFindByType(type, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new ContactServiceException(
                "Error performing 'ContactService.findByType(ContactType type, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByType(ContactType, Integer, Integer)}
     * @param type ContactType TODO: Model Documentation for ContactService.findByType(type)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for ContactService.findByType(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for ContactService.findByType(pageSize)
     * @return Page<ContactDTO>
     * @throws Exception
     */
    protected abstract Page<ContactDTO> handleFindByType(ContactType type, Integer pageNumber, Integer pageSize)
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