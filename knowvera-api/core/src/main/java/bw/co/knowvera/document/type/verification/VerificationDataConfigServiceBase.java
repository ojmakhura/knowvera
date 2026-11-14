package bw.co.knowvera.document.type.verification;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

/**
 * <p>
 * Spring Service base class for <code>VerificationDataConfigService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see VerificationDataConfigService
 */
@CacheConfig(cacheNames = "verificationDataConfigurations")
public abstract class VerificationDataConfigServiceBase
    implements VerificationDataConfigService
{
    protected Logger logger = LoggerFactory.getLogger(VerificationDataConfigService.class);
    protected final VerificationDataConfigRepository verificationDataConfigRepository;
    protected final VerificationDataConfigMapper verificationDataConfigMapper;

    public VerificationDataConfigServiceBase(
        VerificationDataConfigRepository verificationDataConfigRepository,
        VerificationDataConfigMapper verificationDataConfigMapper    ) {
        this.verificationDataConfigRepository = verificationDataConfigRepository;
        this.verificationDataConfigMapper = verificationDataConfigMapper;
    }

    /**
     * Gets the reference to <code>verificationDataConfig</code>'s repository.
     * @return verificationDataConfigRepository
     */
    public VerificationDataConfigRepository getVerificationDataConfigRepository() {
        return verificationDataConfigRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public VerificationDataConfigDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.verification.VerificationDataConfigService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new VerificationDataConfigServiceException(
                "Error performing 'VerificationDataConfigService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for
VerificationDataConfigService.findById(id)
     * @return VerificationDataConfigDTO
     * @throws Exception
     */
    protected abstract VerificationDataConfigDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"branches", "clientRequests", "documentTypes", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions", "verificationDataConfigurations"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public VerificationDataConfigDTO save(final @Valid VerificationDataConfigDTO verificationDataConfig)
    {
        if (verificationDataConfig == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.verification.VerificationDataConfigService.save(@Valid VerificationDataConfigDTO verificationDataConfig) - 'verificationDataConfig' can not be null");
        }
        if (verificationDataConfig.getName() == null || verificationDataConfig.getName().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.verification.VerificationDataConfigService.save(@Valid VerificationDataConfigDTO verificationDataConfig) - 'verificationDataConfig.name' can not be null or empty");
        }
        try
        {
            return this.handleSave(verificationDataConfig);
        }
        catch (Throwable th)
        {
            throw new VerificationDataConfigServiceException(
                "Error performing 'VerificationDataConfigService.save(@Valid VerificationDataConfigDTO verificationDataConfig)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid VerificationDataConfigDTO)}
     * @param verificationDataConfig VerificationDataConfigDTO TODO: Model Documentation for
VerificationDataConfigService.save(verificationDataConfig)
     * @return VerificationDataConfigDTO
     * @throws Exception
     */
    protected abstract VerificationDataConfigDTO handleSave(@Valid VerificationDataConfigDTO verificationDataConfig)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documentTypes", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions", "verificationDataConfigurations"}, allEntries = true)
    public boolean remove(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.document.type.verification.VerificationDataConfigService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new VerificationDataConfigServiceException(
                "Error performing 'VerificationDataConfigService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for
VerificationDataConfigService.remove(id)
     * @return boolean
     * @throws Exception
     */
    protected abstract boolean handleRemove(String id)
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