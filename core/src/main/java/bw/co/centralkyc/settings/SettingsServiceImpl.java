// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::settings::SettingsService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.settings;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.document.Document;
import bw.co.centralkyc.document.DocumentRepository;
import bw.co.centralkyc.document.type.DocumentTypeRepository;

/**
 * @see bw.co.centralkyc.settings.SettingsService
 */
@Service("settingsService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class SettingsServiceImpl
        extends SettingsServiceBase {

    private final DocumentRepository documentRepository;
    private final DocumentTypeRepository documentTypeRepository;

    public SettingsServiceImpl(SettingsDao settingsDao, SettingsRepository settingsRepository,
            SettingsMapper settingsMapper,
            DocumentRepository documentRepository, DocumentTypeRepository documentTypeRepository,
            SalaryRangeMapper salaryRangeMapper,
            SalaryRangeDao salaryRangeDao, SalaryRangeRepository salaryRangeRepository, MessageSource messageSource) {
        super(settingsDao, settingsRepository, settingsMapper, salaryRangeDao, salaryRangeRepository, salaryRangeMapper,
                messageSource);
        // TODO Auto-generated constructor stub

        this.documentRepository = documentRepository;
        this.documentTypeRepository = documentTypeRepository;
    }

    /**
     * @see bw.co.centralkyc.settings.SettingsService#findById(String)
     */
    @Override
    protected SettingsDTO handleFindById(String id)
            throws Exception {
        throw new UnsupportedOperationException(
                "bw.co.centralkyc.settings.SettingsService.handleFindById(String id) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.settings.SettingsService#save(SettingsDTO)
     */
    @Override
    protected SettingsDTO handleSave(SettingsDTO settings)
            throws Exception {

        Settings entity = this.getSettingsDao().settingsDTOToEntity(settings);
        entity = this.getSettingsRepository().save(entity);
        return this.getSettingsDao().toSettingsDTO(entity);
    }

    /**
     * @see bw.co.centralkyc.settings.SettingsService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {
        // TODO implement protected boolean handleRemove(String id)
        throw new UnsupportedOperationException(
                "bw.co.centralkyc.settings.SettingsService.handleRemove(String id) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.settings.SettingsService#getAll()
     */
    @Override
    protected Collection<SettingsDTO> handleGetAll()
            throws Exception {

        Collection<Settings> entities = this.getSettingsRepository().findAll();
        return this.getSettingsDao().toSettingsDTOCollection(entities);

    }

    /**
     * @see bw.co.centralkyc.settings.SettingsService#search(String)
     */
    @Override
    protected Collection<SettingsDTO> handleSearch(String criteria)
            throws Exception {
        // TODO implement protected Collection<SettingsDTO> handleSearch(String
        // criteria)
        throw new UnsupportedOperationException(
                "bw.co.centralkyc.settings.SettingsService.handleSearch(String criteria) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.settings.SettingsService#getAll(Integer, Integer)
     */
    @Override
    protected Page<SettingsDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {
        // TODO implement protected Page<SettingsDTO> handleGetAll(Integer pageNumber,
        // Integer pageSize)
        throw new UnsupportedOperationException(
                "bw.co.centralkyc.settings.SettingsService.handleGetAll(Integer pageNumber, Integer pageSize) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.settings.SettingsService#search(String, Integer,
     *      Integer)
     */
    @Override
    protected Page<SettingsDTO> handleSearch(String criteria, Integer pageNumber, Integer pageSize)
            throws Exception {
        // TODO implement protected Page<SettingsDTO> handleSearch(String criteria,
        // Integer pageNumber, Integer pageSize)
        throw new UnsupportedOperationException(
                "bw.co.centralkyc.settings.SettingsService.handleSearch(String criteria, Integer pageNumber, Integer pageSize) Not implemented!");
    }

    @Override
    protected SettingsDTO handleUploadTemplate(String invoiceTemplate, TargetEntity target, String user)
            throws Exception {

        if (target != TargetEntity.INVOICE && target != TargetEntity.QUOTATION) {
            throw new IllegalArgumentException("Invalid target entity for template upload: " + target);
        }

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new Exception("Settings not found"));
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

        return settingsDao.toSettingsDTO(settings);
    }

    @Override
    protected SettingsDTO handleAttachDocumentType(String documentTypeId, DocumentTypePurpose purpose)
            throws Exception {
        // TODO Auto-generated method stub
        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new Exception("Settings not found"));

        UUID docTypeUUID = UUID.fromString(documentTypeId);

        switch (purpose) {
            case ORGANISATION_KYC:
                settings.getOrgKycDocuments().add(this.documentTypeRepository.findById(docTypeUUID)
                        .orElseThrow(() -> new Exception("Document Type not found")));
                break;
            case INDIVIDUAL_KYC:
                settings.getIndKycDocuments().add(this.documentTypeRepository.findById(docTypeUUID)
                        .orElseThrow(() -> new Exception("Document Type not found")));
                break;
            case ORGANISATION:
                settings.getOrganisationDocuments().add(this.documentTypeRepository.findById(docTypeUUID)
                        .orElseThrow(() -> new Exception("Document Type not found")));
                break;
            case INDIVIDUAL:
                settings.getIndividualDocuments().add(this.documentTypeRepository.findById(docTypeUUID)
                        .orElseThrow(() -> new Exception("Document Type not found")));
                break;
        }

        settings = settingsRepository.save(settings);

        return settingsDao.toSettingsDTO(settings);
    }

    @Override
    protected SettingsDTO handleDetachDocumentType(String documentTypeId, DocumentTypePurpose purpose)
            throws Exception {

        Settings settings = settingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new Exception("Settings not found"));

        switch (purpose) {
            case ORGANISATION_KYC:
                settings.getOrgKycDocuments().removeIf(dt -> dt.getId().equals(documentTypeId));
                break;
            case INDIVIDUAL_KYC:
                settings.getIndKycDocuments().removeIf(dt -> dt.getId().equals(documentTypeId));
                break;
            case ORGANISATION:
                settings.getOrganisationDocuments().removeIf(dt -> dt.getId().equals(documentTypeId));
                break;
            case INDIVIDUAL:
                settings.getIndividualDocuments().removeIf(dt -> dt.getId().equals(documentTypeId));
                break;
        }

        settings = settingsRepository.save(settings);

        return settingsDao.toSettingsDTO(settings);
    }

}