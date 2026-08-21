package bw.co.knowvera.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.document.DocumentRepository;
import bw.co.knowvera.document.type.DocumentType;
import bw.co.knowvera.document.type.DocumentTypeRepository;

@ExtendWith(MockitoExtension.class)
class SettingsServiceImplTest {

    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private SettingsMapper settingsMapper;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private DocumentTypeRepository documentTypeRepository;
    @Mock
    private SalaryRangeMapper salaryRangeMapper;
    @Mock
    private SalaryRangeRepository salaryRangeRepository;

    private SettingsServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SettingsServiceImpl(
                settingsRepository,
                settingsMapper,
                null, documentRepository,
                documentTypeRepository,
                salaryRangeMapper,
                salaryRangeRepository, null, null);
    }

    @Test
    void uploadTemplateRejectsUnsupportedTargets() {
        assertThrows(
                SettingsServiceException.class,
                () -> service.uploadTemplate("template-url", TargetEntity.INDIVIDUAL, "tester"));
    }

    @Test
    void uploadTemplatePersistsInvoiceTemplateAndUpdatesSettings() throws Exception {
        Settings settings = Settings.Factory.newInstance();
        settings.setId(UUID.randomUUID());
        Document savedDocument = Document.Factory.newInstance();
        SettingsDTO expected = new SettingsDTO();

        when(settingsRepository.findAll()).thenReturn(List.of(settings));
        when(documentRepository.save(org.mockito.ArgumentMatchers.any(Document.class))).thenReturn(savedDocument);
        when(settingsRepository.save(settings)).thenReturn(settings);
        when(settingsMapper.toSettingsDTO(settings)).thenReturn(expected);

        SettingsDTO actual = service.uploadTemplate("invoice-template.pdf", TargetEntity.INVOICE, "tester");

        assertSame(expected, actual);
        assertSame(savedDocument, settings.getInvoiceTemplate());
        assertEquals("tester", settings.getModifiedBy());
        assertNotNull(settings.getModifiedAt());
        verify(settingsRepository).save(settings);
    }

    @Test
    void getAllDelegatesToMapper() throws Exception {
        List<Settings> settings = List.of(Settings.Factory.newInstance());
        List<SettingsDTO> expected = List.of(new SettingsDTO());

        when(settingsRepository.findAll()).thenReturn(settings);
        when(settingsMapper.toSettingsDTOCollection(settings)).thenReturn(expected);

        List<SettingsDTO> actual = service.getAll();

        assertSame(expected, actual);
    }

    @Test
    void findByIdThrowsUnsupportedOperationException() {
        assertThrows(SettingsServiceException.class, () -> service.findById(UUID.randomUUID().toString()));
    }

    @Test
    void saveMapsPersistsAndMapsBack() throws Exception {
        SettingsDTO input = new SettingsDTO();
        Settings entity = Settings.Factory.newInstance();
        SettingsDTO expected = new SettingsDTO();

        when(settingsMapper.settingsDTOToEntity(input)).thenReturn(entity);
        when(settingsRepository.save(entity)).thenReturn(entity);
        when(settingsMapper.toSettingsDTO(entity)).thenReturn(expected);

        SettingsDTO actual = service.save(input);

        assertSame(expected, actual);
    }

    @Test
    void removeThrowsUnsupportedOperationException() {
        assertThrows(SettingsServiceException.class, () -> service.remove(UUID.randomUUID().toString()));
    }

    @Test
    void searchThrowsUnsupportedOperationException() {
        assertThrows(SettingsServiceException.class, () -> service.search("q"));
    }

    @Test
    void getAllPagedThrowsUnsupportedOperationException() {
        assertThrows(SettingsServiceException.class, () -> service.getAll(0, 10));
    }

    @Test
    void searchPagedThrowsUnsupportedOperationException() {
        assertThrows(SettingsServiceException.class, () -> service.search("q", 0, 10));
    }

    @Test
    void uploadTemplatePersistsQuotationTemplate() throws Exception {
        Settings settings = Settings.Factory.newInstance();
        settings.setId(UUID.randomUUID());
        Document savedDocument = Document.Factory.newInstance();
        SettingsDTO expected = new SettingsDTO();

        when(settingsRepository.findAll()).thenReturn(List.of(settings));
        when(documentRepository.save(any(Document.class))).thenReturn(savedDocument);
        when(settingsRepository.save(settings)).thenReturn(settings);
        when(settingsMapper.toSettingsDTO(settings)).thenReturn(expected);

        SettingsDTO actual = service.uploadTemplate("quotation-template.pdf", TargetEntity.QUOTATION, "tester");

        assertSame(expected, actual);
        assertSame(savedDocument, settings.getQuotationTemplate());
    }

    @Test
    void uploadTemplateThrowsWhenSettingsMissing() {
        when(settingsRepository.findAll()).thenReturn(List.of());

        assertThrows(Exception.class,
                () -> service.uploadTemplate("invoice-template.pdf", TargetEntity.INVOICE, "tester"));
    }

    @Test
    void attachDocumentTypeSupportsAllPurposes() throws Exception {
        Settings settings = Settings.Factory.newInstance();
        UUID docTypeId = UUID.randomUUID();
        DocumentType documentType = DocumentType.Factory.newInstance();
        documentType.setId(docTypeId);

        when(settingsRepository.findAll()).thenReturn(List.of(settings));
        when(documentTypeRepository.findById(docTypeId)).thenReturn(Optional.of(documentType));
        when(settingsRepository.save(settings)).thenReturn(settings);
        when(settingsMapper.toSettingsDTO(settings)).thenReturn(new SettingsDTO());

        service.attachDocumentType(docTypeId.toString(), DocumentTypePurpose.ORGANISATION);
        service.attachDocumentType(docTypeId.toString(), DocumentTypePurpose.INDIVIDUAL);
        service.attachDocumentType(docTypeId.toString(), DocumentTypePurpose.ORGANISATION_KYC);
        service.attachDocumentType(docTypeId.toString(), DocumentTypePurpose.INDIVIDUAL_KYC);

        assertTrue(settings.getOrganisationDocuments().contains(documentType));
        assertTrue(settings.getIndividualDocuments().contains(documentType));
        assertTrue(settings.getOrgKycDocuments().contains(documentType));
        assertTrue(settings.getIndKycDocuments().contains(documentType));
    }

    @Test
    void attachDocumentTypeThrowsWhenDocumentTypeMissing() {
        Settings settings = Settings.Factory.newInstance();
        UUID docTypeId = UUID.randomUUID();

        when(settingsRepository.findAll()).thenReturn(List.of(settings));
        when(documentTypeRepository.findById(docTypeId)).thenReturn(Optional.empty());

        assertThrows(Exception.class,
                () -> service.attachDocumentType(docTypeId.toString(), DocumentTypePurpose.ORGANISATION));
    }

        @Test
        void attachDocumentTypeThrowsWhenDocumentTypeMissingForAllPurposes() {
        Settings settings = Settings.Factory.newInstance();
        UUID docTypeId = UUID.randomUUID();

        when(settingsRepository.findAll()).thenReturn(List.of(settings));
        when(documentTypeRepository.findById(docTypeId)).thenReturn(Optional.empty());

        assertThrows(Exception.class,
            () -> service.attachDocumentType(docTypeId.toString(), DocumentTypePurpose.ORGANISATION));
        assertThrows(Exception.class,
            () -> service.attachDocumentType(docTypeId.toString(), DocumentTypePurpose.INDIVIDUAL));
        assertThrows(Exception.class,
            () -> service.attachDocumentType(docTypeId.toString(), DocumentTypePurpose.ORGANISATION_KYC));
        assertThrows(Exception.class,
            () -> service.attachDocumentType(docTypeId.toString(), DocumentTypePurpose.INDIVIDUAL_KYC));
        }

    @Test
    void detachDocumentTypeSupportsAllPurposes() throws Exception {
        Settings settings = Settings.Factory.newInstance();
        UUID docTypeId = UUID.randomUUID();
        DocumentType documentType = DocumentType.Factory.newInstance();
        documentType.setId(docTypeId);

        settings.getOrganisationDocuments().add(documentType);
        settings.getIndividualDocuments().add(documentType);
        settings.getOrgKycDocuments().add(documentType);
        settings.getIndKycDocuments().add(documentType);

        when(settingsRepository.findAll()).thenReturn(List.of(settings));
        when(settingsRepository.save(settings)).thenReturn(settings);
        when(settingsMapper.toSettingsDTO(settings)).thenReturn(new SettingsDTO());

        service.detachDocumentType(docTypeId.toString(), DocumentTypePurpose.ORGANISATION);
        service.detachDocumentType(docTypeId.toString(), DocumentTypePurpose.INDIVIDUAL);
        service.detachDocumentType(docTypeId.toString(), DocumentTypePurpose.ORGANISATION_KYC);
        service.detachDocumentType(docTypeId.toString(), DocumentTypePurpose.INDIVIDUAL_KYC);

        assertTrue(settings.getOrganisationDocuments().isEmpty());
        assertTrue(settings.getIndividualDocuments().isEmpty());
        assertTrue(settings.getOrgKycDocuments().isEmpty());
        assertTrue(settings.getIndKycDocuments().isEmpty());
    }

    @Test
    void detachDocumentTypeThrowsWhenSettingsMissing() {
        UUID id = UUID.randomUUID();
        when(settingsRepository.findAll()).thenReturn(List.of());

        assertThrows(Exception.class,
                () -> service.detachDocumentType(id.toString(), DocumentTypePurpose.INDIVIDUAL));
    }

        @Test
        void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById("\t"));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(" "));
        assertThrows(IllegalArgumentException.class, () -> service.search(null));
        assertThrows(IllegalArgumentException.class, () -> service.search("\n"));
        assertThrows(IllegalArgumentException.class, () -> service.search(null, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.search("", 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.uploadTemplate(null, TargetEntity.INVOICE, "tester"));
        assertThrows(IllegalArgumentException.class,
            () -> service.uploadTemplate("invoice-template.pdf", null, "tester"));
        assertThrows(IllegalArgumentException.class,
            () -> service.uploadTemplate("invoice-template.pdf", TargetEntity.INVOICE, " "));
        assertThrows(IllegalArgumentException.class,
            () -> service.attachDocumentType(null, DocumentTypePurpose.INDIVIDUAL));
        assertThrows(IllegalArgumentException.class,
            () -> service.attachDocumentType("\n", DocumentTypePurpose.INDIVIDUAL));
        assertThrows(IllegalArgumentException.class,
            () -> service.attachDocumentType(UUID.randomUUID().toString(), null));
        assertThrows(IllegalArgumentException.class,
            () -> service.detachDocumentType(null, DocumentTypePurpose.INDIVIDUAL));
        assertThrows(IllegalArgumentException.class,
            () -> service.detachDocumentType("\t", DocumentTypePurpose.INDIVIDUAL));
        assertThrows(IllegalArgumentException.class,
            () -> service.detachDocumentType(UUID.randomUUID().toString(), null));
        }
}