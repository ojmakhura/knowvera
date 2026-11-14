package bw.co.knowvera.settings;

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
import org.springframework.data.domain.Page;

/**
 * <p>
 * Spring Service base class for <code>SettingsService</code>,
 * provides access to all services and entities referenced by this service.
 * </p>
 *
 * @see SettingsService
 */
@CacheConfig(cacheNames = "settings")
public abstract class SettingsServiceBase
    implements SettingsService
{
    protected Logger logger = LoggerFactory.getLogger(SettingsService.class);
    protected final SettingsRepository settingsRepository;
    protected final SettingsMapper settingsMapper;
    protected final SalaryRangeRepository salaryRangeRepository;
    protected final SalaryRangeMapper salaryRangeMapper;

    public SettingsServiceBase(
        SettingsRepository settingsRepository,
        SettingsMapper settingsMapper,         SalaryRangeRepository salaryRangeRepository,
        SalaryRangeMapper salaryRangeMapper    ) {
        this.settingsRepository = settingsRepository;
        this.settingsMapper = settingsMapper;
        this.salaryRangeRepository = salaryRangeRepository;
        this.salaryRangeMapper = salaryRangeMapper;
    }

    /**
     * Gets the reference to <code>settings</code>'s repository.
     * @return settingsRepository
     */
    public SettingsRepository getSettingsRepository() {
        return settingsRepository;
    }

    /**
     * Gets the reference to <code>salaryRange</code>'s repository.
     * @return salaryRangeRepository
     */
    public SalaryRangeRepository getSalaryRangeRepository() {
        return salaryRangeRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id")
    public SettingsDTO findById(final String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.findById(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleFindById(id);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.findById(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #findById(String)}
     * @param id java.lang.String TODO: Model Documentation for SettingsService.findById(id)
     * @return SettingsDTO
     * @throws Exception
     */
    protected abstract SettingsDTO handleFindById(String id)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true, beforeInvocation = true) }, put = { @CachePut(key = "#result.id", unless = "#result == null") })
    public SettingsDTO save(final @Valid SettingsDTO settings)
    {
        if (settings == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.save(@Valid SettingsDTO settings) - 'settings' can not be null");
        }
        try
        {
            return this.handleSave(settings);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.save(@Valid SettingsDTO settings)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #save(@Valid SettingsDTO)}
     * @param settings SettingsDTO TODO: Model Documentation for SettingsService.save(settings)
     * @return SettingsDTO
     * @throws Exception
     */
    protected abstract SettingsDTO handleSave(@Valid SettingsDTO settings)
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
                "bw.co.knowvera.settings.SettingsService.remove(String id) - 'id' can not be null or empty");
        }
        try
        {
            return this.handleRemove(id);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.remove(String id)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #remove(String)}
     * @param id java.lang.String TODO: Model Documentation for SettingsService.remove(id)
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
    public List<SettingsDTO> getAll()
    {
        try
        {
            return this.handleGetAll();
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.getAll()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll()}
     * @return List<SettingsDTO>
     * @throws Exception
     */
    protected abstract List<SettingsDTO> handleGetAll()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SettingsDTO> search(final String criteria)
    {
        if (criteria == null || criteria.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.search(String criteria) - 'criteria' can not be null or empty");
        }
        try
        {
            return this.handleSearch(criteria);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.search(String criteria)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(String)}
     * @param criteria java.lang.String TODO: Model Documentation for SettingsService.search(criteria)
     * @return List<SettingsDTO>
     * @throws Exception
     */
    protected abstract List<SettingsDTO> handleSearch(String criteria)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SettingsDTO> getAll(final Integer pageNumber, final Integer pageSize)
    {
        try
        {
            return this.handleGetAll(pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.getAll(Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAll(Integer, Integer)}
     * @param pageNumber java.lang.Integer TODO: Model Documentation for SettingsService.getAll(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for SettingsService.getAll(pageSize)
     * @return Page<SettingsDTO>
     * @throws Exception
     */
    protected abstract Page<SettingsDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SettingsDTO> search(final String criteria, final Integer pageNumber, final Integer pageSize)
    {
        if (criteria == null || criteria.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.search(String criteria, Integer pageNumber, Integer pageSize) - 'criteria' can not be null or empty");
        }
        try
        {
            return this.handleSearch(criteria, pageNumber, pageSize);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.search(String criteria, Integer pageNumber, Integer pageSize)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #search(String, Integer, Integer)}
     * @param criteria java.lang.String TODO: Model Documentation for SettingsService.search(criteria)
     * @param pageNumber java.lang.Integer TODO: Model Documentation for SettingsService.search(pageNumber)
     * @param pageSize java.lang.Integer TODO: Model Documentation for SettingsService.search(pageSize)
     * @return Page<SettingsDTO>
     * @throws Exception
     */
    protected abstract Page<SettingsDTO> handleSearch(String criteria, Integer pageNumber, Integer pageSize)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public SettingsDTO uploadTemplate(final String invoiceTemplate, final TargetEntity target, final String user)
    {
        if (invoiceTemplate == null || invoiceTemplate.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.uploadTemplate(String invoiceTemplate, TargetEntity target, String user) - 'invoiceTemplate' can not be null or empty");
        }
        if (target == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.uploadTemplate(String invoiceTemplate, TargetEntity target, String user) - 'target' can not be null");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.uploadTemplate(String invoiceTemplate, TargetEntity target, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleUploadTemplate(invoiceTemplate, target, user);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.uploadTemplate(String invoiceTemplate, TargetEntity target, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #uploadTemplate(String, TargetEntity, String)}
     * @param invoiceTemplate java.lang.String TODO: Model Documentation for
SettingsService.uploadTemplate(invoiceTemplate)
     * @param target TargetEntity TODO: Model Documentation for SettingsService.uploadTemplate(target)
     * @param user java.lang.String TODO: Model Documentation for SettingsService.uploadTemplate(user)
     * @return SettingsDTO
     * @throws Exception
     */
    protected abstract SettingsDTO handleUploadTemplate(String invoiceTemplate, TargetEntity target, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public SettingsDTO attachDocumentType(final String documentTypeId, final DocumentTypePurpose purpose)
    {
        if (documentTypeId == null || documentTypeId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.attachDocumentType(String documentTypeId, DocumentTypePurpose purpose) - 'documentTypeId' can not be null or empty");
        }
        if (purpose == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.attachDocumentType(String documentTypeId, DocumentTypePurpose purpose) - 'purpose' can not be null");
        }
        try
        {
            return this.handleAttachDocumentType(documentTypeId, purpose);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.attachDocumentType(String documentTypeId, DocumentTypePurpose purpose)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #attachDocumentType(String, DocumentTypePurpose)}
     * @param documentTypeId java.lang.String TODO: Model Documentation for
SettingsService.attachDocumentType(documentTypeId)
     * @param purpose DocumentTypePurpose TODO: Model Documentation for SettingsService.attachDocumentType(purpose)
     * @return SettingsDTO
     * @throws Exception
     */
    protected abstract SettingsDTO handleAttachDocumentType(String documentTypeId, DocumentTypePurpose purpose)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public SettingsDTO detachDocumentType(final String documentTypeId, final DocumentTypePurpose purpose)
    {
        if (documentTypeId == null || documentTypeId.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.detachDocumentType(String documentTypeId, DocumentTypePurpose purpose) - 'documentTypeId' can not be null or empty");
        }
        if (purpose == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.detachDocumentType(String documentTypeId, DocumentTypePurpose purpose) - 'purpose' can not be null");
        }
        try
        {
            return this.handleDetachDocumentType(documentTypeId, purpose);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.detachDocumentType(String documentTypeId, DocumentTypePurpose purpose)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #detachDocumentType(String, DocumentTypePurpose)}
     * @param documentTypeId java.lang.String TODO: Model Documentation for
SettingsService.detachDocumentType(documentTypeId)
     * @param purpose DocumentTypePurpose TODO: Model Documentation for SettingsService.detachDocumentType(purpose)
     * @return SettingsDTO
     * @throws Exception
     */
    protected abstract SettingsDTO handleDetachDocumentType(String documentTypeId, DocumentTypePurpose purpose)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "'one'")
    public SettingsDTO loadSettings()
    {
        try
        {
            return this.handleLoadSettings();
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.loadSettings()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #loadSettings()}
     * @return SettingsDTO
     * @throws Exception
     */
    protected abstract SettingsDTO handleLoadSettings()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public PlatformIdentity getPlatformIdentity()
    {
        try
        {
            return this.handleGetPlatformIdentity();
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.getPlatformIdentity()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getPlatformIdentity()}
     * @return PlatformIdentity
     * @throws Exception
     */
    protected abstract PlatformIdentity handleGetPlatformIdentity()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationalMetrics getOperationalMetrics()
    {
        try
        {
            return this.handleGetOperationalMetrics();
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.getOperationalMetrics()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getOperationalMetrics()}
     * @return OperationalMetrics
     * @throws Exception
     */
    protected abstract OperationalMetrics handleGetOperationalMetrics()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public SettingsFieldGroups getSettingsFieldGroups()
    {
        try
        {
            return this.handleGetSettingsFieldGroups();
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.getSettingsFieldGroups()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getSettingsFieldGroups()}
     * @return SettingsFieldGroups
     * @throws Exception
     */
    protected abstract SettingsFieldGroups handleGetSettingsFieldGroups()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public TemplateMappings getTemplateMappings()
    {
        try
        {
            return this.handleGetTemplateMappings();
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.getTemplateMappings()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getTemplateMappings()}
     * @return TemplateMappings
     * @throws Exception
     */
    protected abstract TemplateMappings handleGetTemplateMappings()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public FinancialSettings getFinancialSettings()
    {
        try
        {
            return this.handleGetFinancialSettings();
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.getFinancialSettings()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getFinancialSettings()}
     * @return FinancialSettings
     * @throws Exception
     */
    protected abstract FinancialSettings handleGetFinancialSettings()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentRequirements getDocumentRequirements()
    {
        try
        {
            return this.handleGetDocumentRequirements();
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.getDocumentRequirements()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getDocumentRequirements()}
     * @return DocumentRequirements
     * @throws Exception
     */
    protected abstract DocumentRequirements handleGetDocumentRequirements()
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public PlatformIdentity savePlatformIdentity(final @Valid PlatformIdentity platformIdentity)
    {
        if (platformIdentity == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.savePlatformIdentity(@Valid PlatformIdentity platformIdentity) - 'platformIdentity' can not be null");
        }
        try
        {
            return this.handleSavePlatformIdentity(platformIdentity);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.savePlatformIdentity(@Valid PlatformIdentity platformIdentity)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #savePlatformIdentity(@Valid PlatformIdentity)}
     * @param platformIdentity PlatformIdentity TODO: Model Documentation for
SettingsService.savePlatformIdentity(platformIdentity)
     * @return PlatformIdentity
     * @throws Exception
     */
    protected abstract PlatformIdentity handleSavePlatformIdentity(@Valid PlatformIdentity platformIdentity)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public OperationalMetrics saveOperationalMetrics(final @Valid OperationalMetrics operationalMetrics)
    {
        if (operationalMetrics == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.saveOperationalMetrics(@Valid OperationalMetrics operationalMetrics) - 'operationalMetrics' can not be null");
        }
        try
        {
            return this.handleSaveOperationalMetrics(operationalMetrics);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.saveOperationalMetrics(@Valid OperationalMetrics operationalMetrics)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #saveOperationalMetrics(@Valid OperationalMetrics)}
     * @param operationalMetrics OperationalMetrics TODO: Model Documentation for
SettingsService.saveOperationalMetrics(operationalMetrics)
     * @return OperationalMetrics
     * @throws Exception
     */
    protected abstract OperationalMetrics handleSaveOperationalMetrics(@Valid OperationalMetrics operationalMetrics)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public SettingsFieldGroups saveSettingsFieldGroups(final @Valid SettingsFieldGroups settingsFieldGroups)
    {
        if (settingsFieldGroups == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.saveSettingsFieldGroups(@Valid SettingsFieldGroups settingsFieldGroups) - 'settingsFieldGroups' can not be null");
        }
        try
        {
            return this.handleSaveSettingsFieldGroups(settingsFieldGroups);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.saveSettingsFieldGroups(@Valid SettingsFieldGroups settingsFieldGroups)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #saveSettingsFieldGroups(@Valid SettingsFieldGroups)}
     * @param settingsFieldGroups SettingsFieldGroups TODO: Model Documentation for
SettingsService.saveSettingsFieldGroups(settingsFieldGroups)
     * @return SettingsFieldGroups
     * @throws Exception
     */
    protected abstract SettingsFieldGroups handleSaveSettingsFieldGroups(@Valid SettingsFieldGroups settingsFieldGroups)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public TemplateMappings saveTemplateMappings(final @Valid TemplateMappings templateMappings)
    {
        if (templateMappings == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.saveTemplateMappings(@Valid TemplateMappings templateMappings) - 'templateMappings' can not be null");
        }
        try
        {
            return this.handleSaveTemplateMappings(templateMappings);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.saveTemplateMappings(@Valid TemplateMappings templateMappings)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #saveTemplateMappings(@Valid TemplateMappings)}
     * @param templateMappings TemplateMappings TODO: Model Documentation for
SettingsService.saveTemplateMappings(templateMappings)
     * @return TemplateMappings
     * @throws Exception
     */
    protected abstract TemplateMappings handleSaveTemplateMappings(@Valid TemplateMappings templateMappings)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public FinancialSettings saveFinancialSettings(final @Valid FinancialSettings financialSettings)
    {
        if (financialSettings == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.saveFinancialSettings(@Valid FinancialSettings financialSettings) - 'financialSettings' can not be null");
        }
        try
        {
            return this.handleSaveFinancialSettings(financialSettings);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.saveFinancialSettings(@Valid FinancialSettings financialSettings)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #saveFinancialSettings(@Valid FinancialSettings)}
     * @param financialSettings FinancialSettings TODO: Model Documentation for
SettingsService.saveFinancialSettings(financialSettings)
     * @return FinancialSettings
     * @throws Exception
     */
    protected abstract FinancialSettings handleSaveFinancialSettings(@Valid FinancialSettings financialSettings)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public DocumentRequirements saveDocumentRequirements(final @Valid DocumentRequirements documentRequirements)
    {
        if (documentRequirements == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.saveDocumentRequirements(@Valid DocumentRequirements documentRequirements) - 'documentRequirements' can not be null");
        }
        try
        {
            return this.handleSaveDocumentRequirements(documentRequirements);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.saveDocumentRequirements(@Valid DocumentRequirements documentRequirements)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #saveDocumentRequirements(@Valid DocumentRequirements)}
     * @param documentRequirements DocumentRequirements TODO: Model Documentation for
SettingsService.saveDocumentRequirements(documentRequirements)
     * @return DocumentRequirements
     * @throws Exception
     */
    protected abstract DocumentRequirements handleSaveDocumentRequirements(@Valid DocumentRequirements documentRequirements)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public FinancialSettings saveSalaryRange(final @Valid SalaryRangeDTO salaryRange, final String user)
    {
        if (salaryRange == null)
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.saveSalaryRange(@Valid SalaryRangeDTO salaryRange, String user) - 'salaryRange' can not be null");
        }
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.saveSalaryRange(@Valid SalaryRangeDTO salaryRange, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleSaveSalaryRange(salaryRange, user);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.saveSalaryRange(@Valid SalaryRangeDTO salaryRange, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #saveSalaryRange(@Valid SalaryRangeDTO, String)}
     * @param salaryRange SalaryRangeDTO TODO: Model Documentation for SettingsService.saveSalaryRange(salaryRange)
     * @param user java.lang.String TODO: Model Documentation for SettingsService.saveSalaryRange(user)
     * @return FinancialSettings
     * @throws Exception
     */
    protected abstract FinancialSettings handleSaveSalaryRange(@Valid SalaryRangeDTO salaryRange, String user)
        throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(cacheNames = {"branches", "clientRequests", "documents", "employmentRecords", "individuals", "invoices", "kycRecords", "organisations", "settings", "subscriptions"}, allEntries = true)
    public FinancialSettings removeSalaryRange(final Long salaryRangeId, final String user)
    {
        if (user == null || user.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "bw.co.knowvera.settings.SettingsService.removeSalaryRange(Long salaryRangeId, String user) - 'user' can not be null or empty");
        }
        try
        {
            return this.handleRemoveSalaryRange(salaryRangeId, user);
        }
        catch (Throwable th)
        {
            throw new SettingsServiceException(
                "Error performing 'SettingsService.removeSalaryRange(Long salaryRangeId, String user)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #removeSalaryRange(Long, String)}
     * @param salaryRangeId java.lang.Long TODO: Model Documentation for
SettingsService.removeSalaryRange(salaryRangeId)
     * @param user java.lang.String TODO: Model Documentation for SettingsService.removeSalaryRange(user)
     * @return FinancialSettings
     * @throws Exception
     */
    protected abstract FinancialSettings handleRemoveSalaryRange(Long salaryRangeId, String user)
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