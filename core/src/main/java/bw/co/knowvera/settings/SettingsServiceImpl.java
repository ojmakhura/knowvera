// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.knowvera::settings::SettingsService
 * STEREOTYPE:  Service
 */
package bw.co.knowvera.settings;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.document.DocumentMapper;
import bw.co.knowvera.document.DocumentRepository;
import bw.co.knowvera.document.type.DocumentTypeMapper;
import bw.co.knowvera.document.type.DocumentTypeRepository;
import bw.co.knowvera.settings.kyc.KycFieldGroup;
import bw.co.knowvera.settings.kyc.KycFieldGroupMapper;
import jakarta.validation.Valid;

/**
 * @see bw.co.knowvera.settings.SettingsService
 */
@Service("settingsService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class SettingsServiceImpl
        extends SettingsServiceBase {

    private final DocumentRepository documentRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentTypeMapper documentTypeMapper;
    private final DocumentMapper documentMapper;
    private final KycFieldGroupMapper kycFieldGroupMapper;
    private final ToolSelectorMapper toolSelectorMapper;
    private final SalaryRangeMapper salaryRangeMapper;
    private final SalaryRangeRepository salaryRangeRepository;

    public SettingsServiceImpl(SettingsRepository settingsRepository, DocumentMapper documentMapper,
            SettingsMapper settingsMapper, KycFieldGroupMapper kycFieldGroupMapper,
            DocumentRepository documentRepository, DocumentTypeRepository documentTypeRepository,
            SalaryRangeMapper salaryRangeMapper, SalaryRangeRepository salaryRangeRepository,
            ToolSelectorMapper toolSelectorMapper, DocumentTypeMapper documentTypeMapper) {
        super(settingsRepository, settingsMapper, salaryRangeRepository, salaryRangeMapper);
        // TODO Auto-generated constructor stub

        this.documentRepository = documentRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.kycFieldGroupMapper = kycFieldGroupMapper;
        this.toolSelectorMapper = toolSelectorMapper;
        this.documentTypeMapper = documentTypeMapper;
        this.salaryRangeMapper = salaryRangeMapper;
        this.salaryRangeRepository = salaryRangeRepository;
        this.documentMapper = documentMapper;
    }

    /**
     * @see bw.co.knowvera.settings.SettingsService#findById(String)
     */
    @Override
    protected SettingsDTO handleFindById(String id)
            throws Exception {
        throw new UnsupportedOperationException(
                "bw.co.knowvera.settings.SettingsService.handleFindById(String id) Not implemented!");
    }

    @Override
    @CacheEvict(allEntries = true)
    public SettingsDTO save(SettingsDTO settings) {
        return super.save(settings);
    }

    /**
     * @see bw.co.knowvera.settings.SettingsService#save(SettingsDTO)
     */
    @Override
    protected SettingsDTO handleSave(SettingsDTO settings)
            throws Exception {

        Settings entity = settingsMapper.settingsDTOToEntity(settings);
        entity = this.getSettingsRepository().save(entity);
        return settingsMapper.toSettingsDTO(entity);
    }

    /**
     * @see bw.co.knowvera.settings.SettingsService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {
        // TODO implement protected boolean handleRemove(String id)
        throw new UnsupportedOperationException(
                "bw.co.knowvera.settings.SettingsService.handleRemove(String id) Not implemented!");
    }

    /**
     * @see bw.co.knowvera.settings.SettingsService#getAll()
     */
    @Override
    protected List<SettingsDTO> handleGetAll()
            throws Exception {

        Collection<Settings> entities = this.getSettingsRepository().findAll();
        return settingsMapper.toSettingsDTOCollection(entities);

    }

    /**
     * @see bw.co.knowvera.settings.SettingsService#search(String)
     */
    @Override
    protected List<SettingsDTO> handleSearch(String criteria)
            throws Exception {
        // TODO implement protected List<SettingsDTO> handleSearch(String
        // criteria)
        throw new UnsupportedOperationException(
                "bw.co.knowvera.settings.SettingsService.handleSearch(String criteria) Not implemented!");
    }

    /**
     * @see bw.co.knowvera.settings.SettingsService#getAll(Integer, Integer)
     */
    @Override
    protected Page<SettingsDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {
        // TODO implement protected Page<SettingsDTO> handleGetAll(Integer pageNumber,
        // Integer pageSize)
        throw new UnsupportedOperationException(
                "bw.co.knowvera.settings.SettingsService.handleGetAll(Integer pageNumber, Integer pageSize) Not implemented!");
    }

    /**
     * @see bw.co.knowvera.settings.SettingsService#search(String, Integer,
     *      Integer)
     */
    @Override
    protected Page<SettingsDTO> handleSearch(String criteria, Integer pageNumber, Integer pageSize)
            throws Exception {
        // TODO implement protected Page<SettingsDTO> handleSearch(String criteria,
        // Integer pageNumber, Integer pageSize)
        throw new UnsupportedOperationException(
                "bw.co.knowvera.settings.SettingsService.handleSearch(String criteria, Integer pageNumber, Integer pageSize) Not implemented!");
    }

    @Override
    protected SettingsDTO handleUploadTemplate(String invoiceTemplate, TargetEntity target, String user)
            throws Exception {

        if (target != TargetEntity.INVOICE && target != TargetEntity.QUOTATION) {
            throw new IllegalArgumentException("Invalid target entity for template upload: " + target);
        }

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new SettingsServiceException("Settings not found"));
        Document document = new Document();
        document.setCreatedAt(LocalDateTime.now());
        document.setCreatedBy(user);
        document.setUrl(invoiceTemplate);
        document.setTarget(target);
        document.setTargetId(settings.getId().toString());

        document = documentRepository.save(document);

        if (target == TargetEntity.INVOICE) {

            settings.setInvoiceTemplate(document);
        } else if (target == TargetEntity.QUOTATION) {

            settings.setQuotationTemplate(document);
        }

        settings.setModifiedBy(user);
        settings.setModifiedAt(LocalDateTime.now());

        settings = settingsRepository.save(settings);

        return settingsMapper.toSettingsDTO(settings);
    }

    @Override
    protected SettingsDTO handleAttachDocumentType(String documentTypeId, DocumentTypePurpose purpose)
            throws Exception {
        // TODO Auto-generated method stub
        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new SettingsServiceException("Settings not found"));

        UUID docTypeUUID = UUID.fromString(documentTypeId);

        switch (purpose) {
            case ORGANISATION_KYC:
                settings.getOrgKycDocuments().add(this.documentTypeRepository.findById(docTypeUUID)
                        .orElseThrow(() -> new SettingsServiceException("Document Type not found")));
                break;
            case INDIVIDUAL_KYC:
                settings.getIndKycDocuments().add(this.documentTypeRepository.findById(docTypeUUID)
                        .orElseThrow(() -> new SettingsServiceException("Document Type not found")));
                break;
            case ORGANISATION:
                settings.getOrganisationDocuments().add(this.documentTypeRepository.findById(docTypeUUID)
                        .orElseThrow(() -> new SettingsServiceException("Document Type not found")));
                break;
            case INDIVIDUAL:
                settings.getIndividualDocuments().add(this.documentTypeRepository.findById(docTypeUUID)
                        .orElseThrow(() -> new SettingsServiceException("Document Type not found")));
                break;
        }

        settings = settingsRepository.save(settings);

        return settingsMapper.toSettingsDTO(settings);
    }

    @Override
    protected SettingsDTO handleDetachDocumentType(String documentTypeId, DocumentTypePurpose purpose)
            throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new SettingsServiceException("Settings not found"));

        UUID uuid = UUID.fromString(documentTypeId);

        switch (purpose) {
            case ORGANISATION_KYC:
                settings.getOrgKycDocuments().removeIf(dt -> dt.getId().equals(uuid));
                break;
            case INDIVIDUAL_KYC:
                settings.getIndKycDocuments().removeIf(dt -> dt.getId().equals(uuid));
                break;
            case ORGANISATION:
                settings.getOrganisationDocuments().removeIf(dt -> dt.getId().equals(uuid));
                break;
            case INDIVIDUAL:
                settings.getIndividualDocuments().removeIf(dt -> dt.getId().equals(uuid));
                break;
        }

        settings = settingsRepository.save(settings);

        return settingsMapper.toSettingsDTO(settings);
    }

    @Override
    protected SettingsDTO handleLoadSettings() throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new Exception("Settings not found"));

        return settingsMapper.toSettingsDTO(settings);
    }

    @Override
    protected PlatformIdentity handleGetPlatformIdentity() throws Exception {
        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        PlatformIdentity platformIdentity = new PlatformIdentity();
        // platformIdentity.setUser(settings.getUser());
        platformIdentity.setPlatformName(settings.getPlatformName());
        platformIdentity.setKycPortalLink(settings.getKycPortalLink());
        platformIdentity.setPlatformUrl(settings.getPlatformUrl());
        platformIdentity.setSupportContact(settings.getSupportContact());

        return platformIdentity;
    }

    @Override
    protected OperationalMetrics handleGetOperationalMetrics() throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        OperationalMetrics operationalMetrics = new OperationalMetrics();

        operationalMetrics.setDataVerificationThreshold(settings.getDataVerificationThreshold());
        operationalMetrics.setDocumentDurationLimit(settings.getDocumentDurationLimit());
        operationalMetrics.setKycDuration(settings.getKycDuration());
        operationalMetrics.setMaxDataVerificationFailureThreshold(settings.getMaxDataVerificationFailureThreshold());
        operationalMetrics.setNormalUserRole(settings.getNormalUserRole());
        operationalMetrics.setOrganisationAdminRole(settings.getOrganisationAdminRole());
        operationalMetrics.setTimeToAccountCreation(settings.getTimeToAccountCreation());

        return operationalMetrics;
    }

    @Override
    protected SettingsFieldGroups handleGetSettingsFieldGroups() throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        SettingsFieldGroups settingsFieldGroups = new SettingsFieldGroups();

        if (CollectionUtils.isNotEmpty(settings.getIndividualKycFieldGroups())) {
            settingsFieldGroups.setIndividualKycFieldGroups(
                    kycFieldGroupMapper.toKycFieldGroupDTOCollection(settings.getIndividualKycFieldGroups()));
        }

        if (CollectionUtils.isNotEmpty(settings.getOrganisationKycFieldGroups())) {
            settingsFieldGroups.setOrganisationKycFieldGroups(
                    kycFieldGroupMapper.toKycFieldGroupDTOCollection(settings.getOrganisationKycFieldGroups()));
        }

        return settingsFieldGroups;
    }

    @Override
    protected SettingsToolSelectors handleGetSettingsToolSelectors() throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        SettingsToolSelectors settingsToolSelectors = new SettingsToolSelectors();

        if (CollectionUtils.isNotEmpty(settings.getDocumentConfirmationTools())) {
            settingsToolSelectors.setDocumentConfirmationTools(
                    toolSelectorMapper.toToolSelectorDTOCollection(settings.getDocumentConfirmationTools()));
        }

        if (CollectionUtils.isNotEmpty(settings.getTextProcessingTools())) {
            settingsToolSelectors.setTextProcessingTools(
                    toolSelectorMapper.toToolSelectorDTOCollection(settings.getTextProcessingTools()));
        }

        if (CollectionUtils.isNotEmpty(settings.getTextCleanupTools())) {
            settingsToolSelectors.setTextCleanupTools(
                    toolSelectorMapper.toToolSelectorDTOCollection(settings.getTextCleanupTools()));
        }

        if (CollectionUtils.isNotEmpty(settings.getTextExtractionTools())) {
            settingsToolSelectors.setTextExtractionTools(
                    toolSelectorMapper.toToolSelectorDTOCollection(settings.getTextExtractionTools()));
        }

        return settingsToolSelectors;
    }

    @Override
    protected TemplateMappings handleGetTemplateMappings() throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new Exception("Settings not found"));

        TemplateMappings templateMappings = new TemplateMappings();
        if (settings.getInvoiceTemplateType() != null) {
            templateMappings.setInvoiceTemplateType(
                    documentTypeMapper.toDocumentTypeDTO(settings.getInvoiceTemplateType()));
        }

        if (settings.getInvoiceTemplate() != null) {
            templateMappings.setInvoiceTemplate(
                    documentMapper.toDocumentDTO(settings.getInvoiceTemplate()));
        }

        if (settings.getQuotationTemplateType() != null) {
            templateMappings.setQuotationTemplateType(
                    documentTypeMapper.toDocumentTypeDTO(settings.getQuotationTemplateType()));
        }

        if (settings.getQuotationTemplate() != null) {
            templateMappings.setQuotationTemplate(
                    documentMapper.toDocumentDTO(settings.getQuotationTemplate()));
        }

        if (settings.getInvoiceDocumentType() != null) {
            templateMappings.setInvoiceDocumentType(
                    documentTypeMapper.toDocumentTypeDTO(settings.getInvoiceDocumentType()));
        }

        if (settings.getQuotationDocumentType() != null) {
            templateMappings.setQuotationDocumentType(
                    documentTypeMapper.toDocumentTypeDTO(settings.getQuotationDocumentType()));
        }

        return templateMappings;
    }

    @Override
    protected FinancialSettings handleGetFinancialSettings() throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        FinancialSettings financialSettings = new FinancialSettings();
        if (CollectionUtils.isNotEmpty(settings.getSalaryRanges())) {
            financialSettings.setSalaryRanges(salaryRangeMapper.toSalaryRangeDTOCollection(settings.getSalaryRanges()));
        }

        financialSettings.setVat(settings.getVat());

        return financialSettings;
    }

    @Override
    protected DocumentRequirements handleGetDocumentRequirements() throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        DocumentRequirements documentRequirements = new DocumentRequirements();

        if (CollectionUtils.isNotEmpty(settings.getOrganisationDocuments())) {
            documentRequirements.setOrganisationDocuments(
                    documentTypeMapper.toDocumentTypeDTOCollection(settings.getOrganisationDocuments()));
        }

        if (CollectionUtils.isNotEmpty(settings.getIndividualDocuments())) {
            documentRequirements.setIndividualDocuments(
                    documentTypeMapper.toDocumentTypeDTOCollection(settings.getIndividualDocuments()));
        }

        if (CollectionUtils.isNotEmpty(settings.getIndKycDocuments())) {
            documentRequirements.setIndKycDocuments(
                    documentTypeMapper.toDocumentTypeDTOCollection(settings.getIndKycDocuments()));
        }

        if (CollectionUtils.isNotEmpty(settings.getOrgKycDocuments())) {
            documentRequirements.setOrgKycDocuments(
                    documentTypeMapper.toDocumentTypeDTOCollection(settings.getOrgKycDocuments()));
        }

        if (settings.getClientRequestFileType() != null) {
            documentRequirements.setClientRequestFileType(
                    documentTypeMapper.toDocumentTypeDTO(settings.getClientRequestFileType()));
        }

        documentRequirements.setMaxDataVerificationFailureThreshold(settings.getMaxDataVerificationFailureThreshold());
        documentRequirements.setDataVerificationThreshold(settings.getDataVerificationThreshold());
        documentRequirements.setDocumentDurationLimit(settings.getDocumentDurationLimit());

        return documentRequirements;
    }

    @Override
    protected PlatformIdentity handleSavePlatformIdentity(@Valid PlatformIdentity platformIdentity) throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        if (settings.getId() == null) {

            settings.setCreatedAt(LocalDateTime.now());
            settings.setCreatedBy(platformIdentity.getUser());
        } else {

            settings.setModifiedAt(LocalDateTime.now());
            settings.setModifiedBy(platformIdentity.getUser());
        }

        settings.setPlatformName(platformIdentity.getPlatformName());
        settings.setKycPortalLink(platformIdentity.getKycPortalLink());
        settings.setPlatformUrl(platformIdentity.getPlatformUrl());
        settings.setSupportContact(platformIdentity.getSupportContact());

        settings = settingsRepository.saveAndFlush(settings);

        return getPlatformIdentity();
    }

    @Override
    protected OperationalMetrics handleSaveOperationalMetrics(@Valid OperationalMetrics operationalMetrics)
            throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        if (settings.getId() == null) {

            settings.setCreatedAt(LocalDateTime.now());
            settings.setCreatedBy(operationalMetrics.getUser());
        } else {

            settings.setModifiedAt(LocalDateTime.now());
            settings.setModifiedBy(operationalMetrics.getUser());
        }

        settings.setKycDuration(operationalMetrics.getKycDuration());
        settings.setTimeToAccountCreation(operationalMetrics.getTimeToAccountCreation());
        settings.setNormalUserRole(operationalMetrics.getNormalUserRole());
        settings.setOrganisationAdminRole(operationalMetrics.getOrganisationAdminRole());
        settings.setMaxDataVerificationFailureThreshold(operationalMetrics.getMaxDataVerificationFailureThreshold());
        settings.setDataVerificationThreshold(operationalMetrics.getDataVerificationThreshold());
        settings.setDocumentDurationLimit(operationalMetrics.getDocumentDurationLimit());

        settings = settingsRepository.saveAndFlush(settings);

        return getOperationalMetrics();
    }

    @Override
    protected SettingsFieldGroups handleSaveSettingsFieldGroups(@Valid SettingsFieldGroups settingsFieldGroups)
            throws Exception {
        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        if (settings.getId() == null) {

            settings.setCreatedAt(LocalDateTime.now());
            settings.setCreatedBy(settingsFieldGroups.getUser());
        } else {

            settings.setModifiedAt(LocalDateTime.now());
            settings.setModifiedBy(settingsFieldGroups.getUser());
        }

        settings = settingsRepository.saveAndFlush(settings);

        return getSettingsFieldGroups();
    }

    @Override
    protected SettingsToolSelectors handleSaveSettingsToolSelectors(@Valid SettingsToolSelectors settingsToolSelectors)
            throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        if (settings.getId() == null) {

            settings.setCreatedAt(LocalDateTime.now());
            settings.setCreatedBy(settingsToolSelectors.getUser());
        } else {

            settings.setModifiedAt(LocalDateTime.now());
            settings.setModifiedBy(settingsToolSelectors.getUser());
        }

        settings = settingsRepository.saveAndFlush(settings);

        return getSettingsToolSelectors();
    }

    @Override
    protected TemplateMappings handleSaveTemplateMappings(@Valid TemplateMappings templateMappings) throws Exception {
        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        if (settings.getId() == null) {

            settings.setCreatedAt(LocalDateTime.now());
            settings.setCreatedBy(templateMappings.getUser());
        } else {

            settings.setModifiedAt(LocalDateTime.now());
            settings.setModifiedBy(templateMappings.getUser());
        }

        if (templateMappings.getInvoiceTemplateType() != null
                && StringUtils.isNotBlank(templateMappings.getInvoiceTemplateType().getId())) {
            settings.setInvoiceTemplateType(
                    documentTypeMapper.documentTypeDTOToEntity(templateMappings.getInvoiceTemplateType()));
        }

        if(templateMappings.getInvoiceTemplate() != null && StringUtils.isNotBlank(templateMappings.getInvoiceTemplate().getId())) {
            settings.setInvoiceTemplate(
                    documentMapper.documentDTOToEntity(templateMappings.getInvoiceTemplate()));
        }

        if (templateMappings.getQuotationTemplateType() != null
                && StringUtils.isNotBlank(templateMappings.getQuotationTemplateType().getId())) {
            settings.setQuotationTemplateType(
                    documentTypeMapper.documentTypeDTOToEntity(templateMappings.getQuotationTemplateType()));
        }

        if (templateMappings.getInvoiceDocumentType() != null
                && StringUtils.isNotBlank(templateMappings.getInvoiceDocumentType().getId())) {
            settings.setInvoiceDocumentType(
                    documentTypeMapper.documentTypeDTOToEntity(templateMappings.getInvoiceDocumentType()));
        }

        if (templateMappings.getQuotationDocumentType() != null
                && StringUtils.isNotBlank(templateMappings.getQuotationDocumentType().getId())) {
            settings.setQuotationDocumentType(
                    documentTypeMapper.documentTypeDTOToEntity(templateMappings.getQuotationDocumentType()));
        }

        if( templateMappings.getQuotationTemplate() != null && StringUtils.isNotBlank(templateMappings.getQuotationTemplate().getId())) {
            settings.setQuotationTemplate(
                    documentMapper.documentDTOToEntity(templateMappings.getQuotationTemplate()));
        }

        settings = settingsRepository.saveAndFlush(settings);
        return getTemplateMappings();
    }

    @Override
    protected FinancialSettings handleSaveFinancialSettings(@Valid FinancialSettings financialSettings)
            throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        if (settings.getId() == null) {

            settings.setCreatedAt(LocalDateTime.now());
            settings.setCreatedBy(financialSettings.getUser());
        } else {

            settings.setModifiedAt(LocalDateTime.now());
            settings.setModifiedBy(financialSettings.getUser());
        }

        settings.setVat(financialSettings.getVat());

        settings = settingsRepository.saveAndFlush(settings);

        return getFinancialSettings();
    }

    @Override
    protected DocumentRequirements handleSaveDocumentRequirements(@Valid DocumentRequirements documentRequirements)
            throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        if (settings.getId() == null) {
            settings.setCreatedAt(LocalDateTime.now());
            settings.setCreatedBy(documentRequirements.getUser());
        } else {

            settings.setModifiedAt(LocalDateTime.now());
            settings.setModifiedBy(documentRequirements.getUser());
        }

        settings = settingsRepository.saveAndFlush(settings);

        return getDocumentRequirements();
    }

    @Override
    protected FinancialSettings handleSaveSalaryRange(@Valid SalaryRangeDTO salaryRange, String user) throws Exception {

        boolean isNew = (salaryRange.getId() == null);
        SalaryRange entity = salaryRangeMapper.salaryRangeDTOToEntity(salaryRange);

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElse(new Settings());

        if (settings.getId() == null) {
            settings.setCreatedAt(LocalDateTime.now());
            settings.setCreatedBy(user);
        } else {
            settings.setModifiedAt(LocalDateTime.now());
            settings.setModifiedBy(user);
        }

        if (isNew) {

            entity.setSettings(settings);
            settings.getSalaryRanges().add(entity);

        } else {

            entity = salaryRangeRepository.saveAndFlush(entity);
        }

        settingsRepository.saveAndFlush(settings);

        return getFinancialSettings();
    }

    @Override
    protected FinancialSettings handleRemoveSalaryRange(Long salaryRangeId, String user) throws Exception {

        salaryRangeRepository.deleteById(salaryRangeId);

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new SettingsServiceException("Settings not found"));

        if (settings.getId() == null) {
            settings.setCreatedAt(LocalDateTime.now());
            settings.setCreatedBy(user);
        } else {
            settings.setModifiedAt(LocalDateTime.now());
            settings.setModifiedBy(user);
        }

        settingsRepository.saveAndFlush(settings);
        return getFinancialSettings();
    }

}