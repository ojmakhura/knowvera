package bw.co.knowvera.settings;

import bw.co.knowvera.TargetEntity;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService
 */
public interface SettingsService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.findById(id)
     * @return SettingsDTO
     */
    public SettingsDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.save
     * @param settings TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.save(settings)
     * @return SettingsDTO
     */
    public SettingsDTO save(@Valid SettingsDTO settings) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.getAll
     * @return List<SettingsDTO>
     */
    public List<SettingsDTO> getAll() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.search(criteria)
     * @return List<SettingsDTO>
     */
    public List<SettingsDTO> search(String criteria) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.getAll
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.getAll(pageSize)
     * @return Page<SettingsDTO>
     */
    public Page<SettingsDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.search(criteria)
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.search(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.search(pageSize)
     * @return Page<SettingsDTO>
     */
    public Page<SettingsDTO> search(String criteria, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.uploadTemplate
     * @param invoiceTemplate TODO: Model Documentation for
bw.co.knowvera.settings.SettingsService.uploadTemplate(invoiceTemplate)
     * @param target TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.uploadTemplate(target)
     * @param user TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.uploadTemplate(user)
     * @return SettingsDTO
     */
    public SettingsDTO uploadTemplate(String invoiceTemplate, TargetEntity target, String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.attachDocumentType
     * @param documentTypeId TODO: Model Documentation for
bw.co.knowvera.settings.SettingsService.attachDocumentType(documentTypeId)
     * @param purpose TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.attachDocumentType(purpose)
     * @return SettingsDTO
     */
    public SettingsDTO attachDocumentType(String documentTypeId, DocumentTypePurpose purpose) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.detachDocumentType
     * @param documentTypeId TODO: Model Documentation for
bw.co.knowvera.settings.SettingsService.detachDocumentType(documentTypeId)
     * @param purpose TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.detachDocumentType(purpose)
     * @return SettingsDTO
     */
    public SettingsDTO detachDocumentType(String documentTypeId, DocumentTypePurpose purpose) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.loadSettings
     * @return SettingsDTO
     */
    public SettingsDTO loadSettings() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.getPlatformIdentity
     * @return PlatformIdentity
     */
    public PlatformIdentity getPlatformIdentity() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.getOperationalMetrics
     * @return OperationalMetrics
     */
    public OperationalMetrics getOperationalMetrics() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.getSettingsFieldGroups
     * @return SettingsFieldGroups
     */
    public SettingsFieldGroups getSettingsFieldGroups() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.getTemplateMappings
     * @return TemplateMappings
     */
    public TemplateMappings getTemplateMappings() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.getFinancialSettings
     * @return FinancialSettings
     */
    public FinancialSettings getFinancialSettings() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.getDocumentRequirements
     * @return DocumentRequirements
     */
    public DocumentRequirements getDocumentRequirements() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.savePlatformIdentity
     * @param platformIdentity TODO: Model Documentation for
bw.co.knowvera.settings.SettingsService.savePlatformIdentity(platformIdentity)
     * @return PlatformIdentity
     */
    public PlatformIdentity savePlatformIdentity(@Valid PlatformIdentity platformIdentity) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.saveOperationalMetrics
     * @param operationalMetrics TODO: Model Documentation for
bw.co.knowvera.settings.SettingsService.saveOperationalMetrics(operationalMetrics)
     * @return OperationalMetrics
     */
    public OperationalMetrics saveOperationalMetrics(@Valid OperationalMetrics operationalMetrics) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.saveSettingsFieldGroups
     * @param settingsFieldGroups TODO: Model Documentation for
bw.co.knowvera.settings.SettingsService.saveSettingsFieldGroups(settingsFieldGroups)
     * @return SettingsFieldGroups
     */
    public SettingsFieldGroups saveSettingsFieldGroups(@Valid SettingsFieldGroups settingsFieldGroups) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.saveTemplateMappings
     * @param templateMappings TODO: Model Documentation for
bw.co.knowvera.settings.SettingsService.saveTemplateMappings(templateMappings)
     * @return TemplateMappings
     */
    public TemplateMappings saveTemplateMappings(@Valid TemplateMappings templateMappings) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.saveFinancialSettings
     * @param financialSettings TODO: Model Documentation for
bw.co.knowvera.settings.SettingsService.saveFinancialSettings(financialSettings)
     * @return FinancialSettings
     */
    public FinancialSettings saveFinancialSettings(@Valid FinancialSettings financialSettings) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.settings.SettingsService.saveDocumentRequirements
     * @param documentRequirements TODO: Model Documentation for
bw.co.knowvera.settings.SettingsService.saveDocumentRequirements(documentRequirements)
     * @return DocumentRequirements
     */
    public DocumentRequirements saveDocumentRequirements(@Valid DocumentRequirements documentRequirements) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.saveSalaryRange
     * @param salaryRange TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.saveSalaryRange(salaryRange)
     * @param user TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.saveSalaryRange(user)
     * @return FinancialSettings
     */
    public FinancialSettings saveSalaryRange(@Valid SalaryRangeDTO salaryRange, String user) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.removeSalaryRange
     * @param salaryRangeId TODO: Model Documentation for
bw.co.knowvera.settings.SettingsService.removeSalaryRange(salaryRangeId)
     * @param user TODO: Model Documentation for bw.co.knowvera.settings.SettingsService.removeSalaryRange(user)
     * @return FinancialSettings
     */
    public FinancialSettings removeSalaryRange(Long salaryRangeId, String user) ;

}