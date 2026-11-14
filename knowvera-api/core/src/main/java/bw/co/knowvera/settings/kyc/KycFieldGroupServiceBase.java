package bw.co.knowvera.settings.kyc;

import bw.co.knowvera.TargetEntity;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

/**
 * <p>
 * Spring Service base class for <code>KycFieldGroupService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see KycFieldGroupService
 */
@CacheConfig(cacheNames = "kycFieldGroups")
public abstract class KycFieldGroupServiceBase
    implements KycFieldGroupService
{
    protected Logger logger = LoggerFactory.getLogger(KycFieldGroupService.class);
    protected final KycFieldGroupRepository kycFieldGroupRepository;
    protected final KycFieldGroupMapper kycFieldGroupMapper;

    public KycFieldGroupServiceBase(
        KycFieldGroupRepository kycFieldGroupRepository,
        KycFieldGroupMapper kycFieldGroupMapper    ) {
        this.kycFieldGroupRepository = kycFieldGroupRepository;
        this.kycFieldGroupMapper = kycFieldGroupMapper;
    }

    /**
     * Gets the reference to <code>kycFieldGroup</code>'s repository.
     * @return kycFieldGroupRepository
     */
    public KycFieldGroupRepository getKycFieldGroupRepository() {
        return kycFieldGroupRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public KycFieldGroupDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.kyc.KycFieldGroupService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new KycFieldGroupServiceException(
                "Error performing 'KycFieldGroupService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for KycFieldGroupService.findById(id)
     * @return KycFieldGroupDTO
     * @throws Exception
     */
    protected abstract KycFieldGroupDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycFieldGroups", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public KycFieldGroupDTO save(final @Valid KycFieldGroupDTO fieldGroup)
    {
        if (fieldGroup == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.kyc.KycFieldGroupService.save(@Valid KycFieldGroupDTO fieldGroup) - 'fieldGroup' can not be null");
        }
        if (fieldGroup.getLabel() == null || fieldGroup.getLabel().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.kyc.KycFieldGroupService.save(@Valid KycFieldGroupDTO fieldGroup) - 'fieldGroup.label' can not be null or empty");
        }
        if (fieldGroup.getDescription() == null || fieldGroup.getDescription().trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.kyc.KycFieldGroupService.save(@Valid KycFieldGroupDTO fieldGroup) - 'fieldGroup.description' can not be null or empty");
        }
        if (fieldGroup.getTargetType() == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.kyc.KycFieldGroupService.save(@Valid KycFieldGroupDTO fieldGroup) - 'fieldGroup.targetType' can not be null");
        }
        try
        {
            return this.handleSave(fieldGroup);
        }
        catch (Throwable th)
        {
            throw new KycFieldGroupServiceException(
                "Error performing 'KycFieldGroupService.save(@Valid KycFieldGroupDTO fieldGroup)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid KycFieldGroupDTO)}
     * @param fieldGroup KycFieldGroupDTO TODO: Model Documentation for KycFieldGroupService.save(fieldGroup)
     * @return KycFieldGroupDTO
     * @throws Exception
     */
    protected abstract KycFieldGroupDTO handleSave(@Valid KycFieldGroupDTO fieldGroup)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycFieldGroups", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public boolean remove(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.kyc.KycFieldGroupService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new KycFieldGroupServiceException(
                "Error performing 'KycFieldGroupService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for KycFieldGroupService.remove(id)
     * @return boolean
     * @throws Exception
     */
    protected abstract boolean handleRemove(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KycFieldGroupDTO> findByTarget(final TargetEntity targetType)
    {
        if (targetType == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.kyc.KycFieldGroupService.findByTarget(TargetEntity targetType) - 'targetType' can not be null");
        }
        try
        {
            return this.handleFindByTarget(targetType);
        }
        catch (Throwable th)
        {
            throw new KycFieldGroupServiceException(
                "Error performing 'KycFieldGroupService.findByTarget(TargetEntity targetType)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findByTarget(TargetEntity)}
     * @param targetType TargetEntity TODO: Model Documentation for
KycFieldGroupService.findByTarget(targetType)
     * @return List<KycFieldGroupDTO>
     * @throws Exception
     */
    protected abstract List<KycFieldGroupDTO> handleFindByTarget(TargetEntity targetType)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycFieldGroups", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public KycFieldGroupDTO removeField(final String id, final String fieldId)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.kyc.KycFieldGroupService.removeField(String id, String fieldId) - 'id' can not be null or empty");
        }
        if (fieldId == null || fieldId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.kyc.KycFieldGroupService.removeField(String id, String fieldId) - 'fieldId' can not be null or empty");
        }
        try
        {
            return this.handleRemoveField(id, fieldId);
        }
        catch (Throwable th)
        {
            throw new KycFieldGroupServiceException(
                "Error performing 'KycFieldGroupService.removeField(String id, String fieldId)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #removeField(String, String)}
     * @param id java.lang.String TODO: Model Documentation for KycFieldGroupService.removeField(id)
     * @param fieldId java.lang.String TODO: Model Documentation for KycFieldGroupService.removeField(fieldId)
     * @return KycFieldGroupDTO
     * @throws Exception
     */
    protected abstract KycFieldGroupDTO handleRemoveField(String id, String fieldId)
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