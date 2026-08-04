package bw.co.kyvera.document.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import bw.co.kyvera.document.type.field.ExpectedField;
import bw.co.kyvera.document.type.field.ExpectedFieldDTO;
import bw.co.kyvera.document.type.field.ExpectedFieldMapper;
import bw.co.kyvera.document.type.field.ExpectedFieldRepository;
import bw.co.kyvera.document.type.verification.VerificationDataConfig;
import bw.co.kyvera.document.type.verification.VerificationDataConfigDTO;
import bw.co.kyvera.document.type.verification.VerificationDataConfigMapper;
import bw.co.kyvera.document.type.verification.VerificationDataConfigRepository;

@ExtendWith(MockitoExtension.class)
class DocumentTypeServiceImplTest {

    @Mock
    private DocumentTypeRepository documentTypeRepository;
    @Mock
    private DocumentTypeMapper documentTypeMapper;
    @Mock
    private ExpectedFieldRepository expectedFieldRepository;
    @Mock
    private VerificationDataConfigRepository verificationDataConfigRepository;
    @Mock
    private ExpectedFieldMapper expectedFieldMapper;
    @Mock
    private VerificationDataConfigMapper verificationDataConfigMapper;
    @Mock
    private MessageSource messageSource;

    private DocumentTypeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DocumentTypeServiceImpl(
                documentTypeRepository,
                documentTypeMapper,
                expectedFieldRepository,
                verificationDataConfigRepository,
                expectedFieldMapper,
                verificationDataConfigMapper,
                messageSource);
    }

    @Test
    void findByIdAddsVerificationDataConfigsToDto() throws Exception {
        UUID id = UUID.randomUUID();
        DocumentType entity = DocumentType.Factory.newInstance();
        entity.setId(id);
        DocumentTypeDTO dto = new DocumentTypeDTO();
        List<VerificationDataConfig> configs = List.of(VerificationDataConfig.Factory.newInstance());
        List<VerificationDataConfigDTO> configDtos = List.of(new VerificationDataConfigDTO());

        when(documentTypeRepository.findById(id)).thenReturn(Optional.of(entity));
        when(documentTypeMapper.toDocumentTypeDTO(entity)).thenReturn(dto);
        when(verificationDataConfigRepository.findByDocumentTypeId(id)).thenReturn(configs);
        when(verificationDataConfigMapper.toVerificationDataConfigDTOCollection(configs)).thenReturn(configDtos);

        DocumentTypeDTO actual = service.findById(id.toString());

        assertSame(dto, actual);
        assertSame(configDtos, actual.getVerificationDataConfigs());
    }

    @Test
    void searchWithBlankCriteriaUsesSortedFindAll() throws Exception {
        List<DocumentType> entities = List.of(DocumentType.Factory.newInstance());
        List<DocumentTypeDTO> expected = List.of(new DocumentTypeDTO());
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        when(documentTypeRepository.findAll(sort)).thenReturn(entities);
        when(documentTypeMapper.toDocumentTypeDTOCollection(entities)).thenReturn(expected);

        List<DocumentTypeDTO> actual = service.search(" ");

        assertSame(expected, actual);
    }

    @Test
    void addExpectedFieldAddsNewFieldAndBindsDocumentType() throws Exception {
        UUID id = UUID.randomUUID();
        DocumentType documentType = DocumentType.Factory.newInstance();
        documentType.setId(id);
        DocumentTypeDTO expected = new DocumentTypeDTO();

        ExpectedFieldDTO fieldDTO = new ExpectedFieldDTO();
        fieldDTO.setField("companyName");
        ExpectedField field = ExpectedField.Factory.newInstance();

        when(documentTypeRepository.findById(id)).thenReturn(Optional.of(documentType));
        when(expectedFieldMapper.expectedFieldDTOToEntity(fieldDTO)).thenReturn(field);
        when(documentTypeRepository.save(documentType)).thenReturn(documentType);
        when(documentTypeMapper.toDocumentTypeDTO(documentType)).thenReturn(expected);

        DocumentTypeDTO actual = service.addExpectedField(id.toString(), Set.of(fieldDTO));

        assertSame(expected, actual);
        assertEquals(1, documentType.getExpectedFields().size());
        assertSame(documentType, field.getDocumentType());
    }

    @Test
    void findByIdThrowsExceptionWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(documentTypeRepository.findById(id)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class,
                () -> service.findById(id.toString()),
                "DocumentType not found for id: " + id);
    }

    @Test
    void saveWithVerificationDataConfigs() throws Exception {
        DocumentTypeDTO input = new DocumentTypeDTO();
        input.setCode("DOC");
        input.setName("Document");
        DocumentType entity = DocumentType.Factory.newInstance();
        UUID typeId = UUID.randomUUID();
        entity.setId(typeId);
        DocumentTypeDTO expected = new DocumentTypeDTO();
        List<VerificationDataConfig> configs = List.of(VerificationDataConfig.Factory.newInstance());
        List<VerificationDataConfigDTO> configDtos = List.of(new VerificationDataConfigDTO());

        when(documentTypeMapper.documentTypeDTOToEntity(input)).thenReturn(entity);
        when(documentTypeRepository.save(entity)).thenReturn(entity);
        when(documentTypeMapper.toDocumentTypeDTO(entity)).thenReturn(expected);
        when(verificationDataConfigRepository.findByDocumentTypeId(typeId)).thenReturn(configs);
        when(verificationDataConfigMapper.toVerificationDataConfigDTOCollection(configs)).thenReturn(configDtos);

        DocumentTypeDTO actual = service.save(input);

        assertSame(expected, actual);
        assertSame(configDtos, actual.getVerificationDataConfigs());
        verify(documentTypeRepository).save(entity);
    }

    @Test
    void removeConvertsStringToUuidAndReturnsTrue() throws Exception {
        UUID id = UUID.randomUUID();

        boolean removed = service.remove(id.toString());

        assertTrue(removed);
        verify(documentTypeRepository).deleteById(id);
    }

    @Test
    void getAllReturnsAllDocumentTypes() throws Exception {
        List<DocumentType> entities = List.of(
                DocumentType.Factory.newInstance(),
                DocumentType.Factory.newInstance());
        List<DocumentTypeDTO> expected = List.of(
                new DocumentTypeDTO(),
                new DocumentTypeDTO());

        when(documentTypeRepository.findAll()).thenReturn(entities);
        when(documentTypeMapper.toDocumentTypeDTOCollection(entities)).thenReturn(expected);

        List<DocumentTypeDTO> actual = service.getAll();

        assertSame(expected, actual);
        assertEquals(2, actual.size());
    }

    @Test
    void searchWithCriteriaBuildSpecificationAndSearches() throws Exception {
        String criteria = "testCriteria";
        List<DocumentType> entities = List.of(DocumentType.Factory.newInstance());
        List<DocumentTypeDTO> expected = List.of(new DocumentTypeDTO());

        when(documentTypeRepository.findAll(org.mockito.ArgumentMatchers.<Specification<DocumentType>>any(), any(Sort.class))).thenReturn(entities);
        when(documentTypeMapper.toDocumentTypeDTOCollection(entities)).thenReturn(expected);

        List<DocumentTypeDTO> actual = service.search(criteria);

        assertSame(expected, actual);
    }

    @Test
    void getAllWithPaginationReturnsPaginatedResults() throws Exception {
        List<DocumentType> entities = List.of(DocumentType.Factory.newInstance());
        Page<DocumentType> page = new PageImpl<>(entities);

        when(documentTypeRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<DocumentTypeDTO> actual = service.getAll(0, 10);

        assertEquals(0, actual.getNumber());
        assertEquals(1, actual.getSize());
    }

    @Test
    void searchWithPaginationAndBlankCriteriaUsesFindAll() throws Exception {
        String criteria = "   ";
        List<DocumentType> entities = List.of(DocumentType.Factory.newInstance());
        Page<DocumentType> page = new PageImpl<>(entities);

        when(documentTypeRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<DocumentTypeDTO> actual = service.search(criteria, 0, 10);

        verify(documentTypeRepository).findAll(any(PageRequest.class));
        assertEquals(1, actual.getSize());
    }

    @Test
    void searchWithPaginationAndCriteriaUsesSpecification() throws Exception {
        String criteria = "documentCriteria";
        List<DocumentType> entities = List.of(DocumentType.Factory.newInstance());
        Page<DocumentType> page = new PageImpl<>(entities);

        when(documentTypeRepository.findAll(org.mockito.ArgumentMatchers.<Specification<DocumentType>>any(), any(PageRequest.class))).thenReturn(page);

        Page<DocumentTypeDTO> actual = service.search(criteria, 0, 10);
        
        verify(documentTypeRepository).findAll(org.mockito.ArgumentMatchers.<Specification<DocumentType>>any(), any(PageRequest.class));
        assertEquals(1, actual.getSize());
    }

    @Test
    void addExpectedFieldUpdatesExistingField() throws Exception {
        UUID id = UUID.randomUUID();
        UUID fieldId = UUID.randomUUID();
        DocumentType documentType = DocumentType.Factory.newInstance();
        documentType.setId(id);
        ExpectedField existingField = ExpectedField.Factory.newInstance();
        existingField.setId(fieldId);
        documentType.getExpectedFields().add(existingField);
        DocumentTypeDTO expected = new DocumentTypeDTO();

        ExpectedFieldDTO fieldDTO = new ExpectedFieldDTO();
        fieldDTO.setId(fieldId.toString());
        fieldDTO.setField("newField");
        fieldDTO.setMandatory(true);
        ExpectedField updatedField = ExpectedField.Factory.newInstance();
        updatedField.setField("newField");
        updatedField.setMandatory(true);

        when(documentTypeRepository.findById(id)).thenReturn(Optional.of(documentType));
        when(expectedFieldMapper.expectedFieldDTOToEntity(fieldDTO)).thenReturn(updatedField);
        when(documentTypeRepository.save(documentType)).thenReturn(documentType);
        when(documentTypeMapper.toDocumentTypeDTO(documentType)).thenReturn(expected);

        DocumentTypeDTO actual = service.addExpectedField(id.toString(), Set.of(fieldDTO));

        assertSame(expected, actual);
        assertEquals(1, documentType.getExpectedFields().size());
    }

    @Test
    void addExpectedFieldAddsMultipleNewFields() throws Exception {
        UUID id = UUID.randomUUID();
        DocumentType documentType = DocumentType.Factory.newInstance();
        documentType.setId(id);
        DocumentTypeDTO expected = new DocumentTypeDTO();

        ExpectedFieldDTO fieldDTO1 = new ExpectedFieldDTO();
        fieldDTO1.setField("field1");
        ExpectedFieldDTO fieldDTO2 = new ExpectedFieldDTO();
        fieldDTO2.setField("field2");

        ExpectedField field1 = ExpectedField.Factory.newInstance();
        ExpectedField field2 = ExpectedField.Factory.newInstance();

        when(documentTypeRepository.findById(id)).thenReturn(Optional.of(documentType));
        when(expectedFieldMapper.expectedFieldDTOToEntity(fieldDTO1)).thenReturn(field1);
        when(expectedFieldMapper.expectedFieldDTOToEntity(fieldDTO2)).thenReturn(field2);
        when(documentTypeRepository.save(documentType)).thenReturn(documentType);
        when(documentTypeMapper.toDocumentTypeDTO(documentType)).thenReturn(expected);

        DocumentTypeDTO actual = service.addExpectedField(id.toString(), Set.of(fieldDTO1, fieldDTO2));

        assertSame(expected, actual);
        assertEquals(2, documentType.getExpectedFields().size());
    }

    @Test
    void addExpectedFieldThrowsExceptionWhenDocumentTypeNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        ExpectedFieldDTO fieldDTO = new ExpectedFieldDTO();
        
        when(documentTypeRepository.findById(id)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class,
                () -> service.addExpectedField(id.toString(), Set.of(fieldDTO)),
                "DocumentType not found for id: " + id);
    }

    @Test
    void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById("\t"));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(" "));
        assertThrows(IllegalArgumentException.class, () -> service.addExpectedField(null, Set.of()));
        assertThrows(IllegalArgumentException.class, () -> service.addExpectedField("\n", Set.of()));
        assertThrows(IllegalArgumentException.class,
                () -> service.addExpectedField(UUID.randomUUID().toString(), null));
    }

    @Test
    void serviceBaseSaveGuardsRejectMissingRequiredFields() {
        DocumentTypeDTO missingCode = validDocumentType();
        missingCode.setCode(" ");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingCode));

        DocumentTypeDTO missingName = validDocumentType();
        missingName.setName(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingName));
    }

    private static DocumentTypeDTO validDocumentType() {
        DocumentTypeDTO dto = new DocumentTypeDTO();
        dto.setCode("DOC");
        dto.setName("Document");
        return dto;
    }
}