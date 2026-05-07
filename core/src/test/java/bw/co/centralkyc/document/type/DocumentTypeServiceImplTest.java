package bw.co.centralkyc.document.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
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

import bw.co.centralkyc.document.type.field.ExpectedField;
import bw.co.centralkyc.document.type.field.ExpectedFieldDTO;
import bw.co.centralkyc.document.type.field.ExpectedFieldMapper;
import bw.co.centralkyc.document.type.field.ExpectedFieldRepository;
import bw.co.centralkyc.document.type.verification.VerificationDataConfig;
import bw.co.centralkyc.document.type.verification.VerificationDataConfigDTO;
import bw.co.centralkyc.document.type.verification.VerificationDataConfigMapper;
import bw.co.centralkyc.document.type.verification.VerificationDataConfigRepository;

@ExtendWith(MockitoExtension.class)
class DocumentTypeServiceImplTest {

    @Mock
    private DocumentTypeDao documentTypeDao;
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
                documentTypeDao,
                documentTypeRepository,
                documentTypeMapper,
                expectedFieldRepository,
                verificationDataConfigRepository,
                expectedFieldMapper,
                verificationDataConfigMapper,
                messageSource);
    }

    @Test
    void handleFindByIdAddsVerificationDataConfigsToDto() throws Exception {
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

        DocumentTypeDTO actual = service.handleFindById(id.toString());

        assertSame(dto, actual);
        assertSame(configDtos, actual.getVerificationDataConfigs());
    }

    @Test
    void handleSearchWithBlankCriteriaUsesSortedFindAll() throws Exception {
        List<DocumentType> entities = List.of(DocumentType.Factory.newInstance());
        List<DocumentTypeDTO> expected = List.of(new DocumentTypeDTO());
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        when(documentTypeRepository.findAll(sort)).thenReturn(entities);
        when(documentTypeMapper.toDocumentTypeDTOCollection(entities)).thenReturn(expected);

        List<DocumentTypeDTO> actual = service.handleSearch(" ");

        assertSame(expected, actual);
    }

    @Test
    void handleAddExpectedFieldAddsNewFieldAndBindsDocumentType() throws Exception {
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

        DocumentTypeDTO actual = service.handleAddExpectedField(id.toString(), Set.of(fieldDTO));

        assertSame(expected, actual);
        assertEquals(1, documentType.getExpectedFields().size());
        assertSame(documentType, field.getDocumentType());
    }

    @Test
    void handleFindByIdThrowsExceptionWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(documentTypeRepository.findById(id)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class,
                () -> service.handleFindById(id.toString()),
                "DocumentType not found for id: " + id);
    }

    @Test
    void handleSaveWithVerificationDataConfigs() throws Exception {
        DocumentTypeDTO input = new DocumentTypeDTO();
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

        DocumentTypeDTO actual = service.handleSave(input);

        assertSame(expected, actual);
        assertSame(configDtos, actual.getVerificationDataConfigs());
        verify(documentTypeRepository).save(entity);
    }

    @Test
    void handleRemoveConvertsStringToUuidAndReturnsTrue() throws Exception {
        UUID id = UUID.randomUUID();

        boolean removed = service.handleRemove(id.toString());

        assertTrue(removed);
        verify(documentTypeRepository).deleteById(id);
    }

    @Test
    void handleGetAllReturnsAllDocumentTypes() throws Exception {
        List<DocumentType> entities = List.of(
                DocumentType.Factory.newInstance(),
                DocumentType.Factory.newInstance());
        List<DocumentTypeDTO> expected = List.of(
                new DocumentTypeDTO(),
                new DocumentTypeDTO());

        when(documentTypeRepository.findAll()).thenReturn(entities);
        when(documentTypeMapper.toDocumentTypeDTOCollection(entities)).thenReturn(expected);

        List<DocumentTypeDTO> actual = service.handleGetAll();

        assertSame(expected, actual);
        assertEquals(2, actual.size());
    }

    @Test
    void handleSearchWithCriteriaBuildSpecificationAndSearches() throws Exception {
        String criteria = "testCriteria";
        List<DocumentType> entities = List.of(DocumentType.Factory.newInstance());
        List<DocumentTypeDTO> expected = List.of(new DocumentTypeDTO());
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        
    when(documentTypeRepository.findAll((Specification<DocumentType>) any(), any(Sort.class))).thenReturn(entities);
        when(documentTypeMapper.toDocumentTypeDTOCollection(entities)).thenReturn(expected);

        List<DocumentTypeDTO> actual = service.handleSearch(criteria);

        assertSame(expected, actual);
    }

    @Test
    void handleGetAllWithPaginationReturnsPaginatedResults() throws Exception {
        List<DocumentType> entities = List.of(DocumentType.Factory.newInstance());
        Page<DocumentType> page = new PageImpl<>(entities);
        List<DocumentTypeDTO> dtos = List.of(new DocumentTypeDTO());
        Page<DocumentTypeDTO> expectedPage = new PageImpl<>(dtos);

        when(documentTypeRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<DocumentTypeDTO> actual = service.handleGetAll(0, 10);

        assertEquals(0, actual.getNumber());
        assertEquals(1, actual.getSize());
    }

    @Test
    void handleSearchWithPaginationAndBlankCriteriaUsesFindAll() throws Exception {
        String criteria = "   ";
        List<DocumentType> entities = List.of(DocumentType.Factory.newInstance());
        Page<DocumentType> page = new PageImpl<>(entities);

        when(documentTypeRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<DocumentTypeDTO> actual = service.handleSearch(criteria, 0, 10);

        verify(documentTypeRepository).findAll(any(PageRequest.class));
        assertEquals(1, actual.getSize());
    }

    @Test
    void handleSearchWithPaginationAndCriteriaUsesSpecification() throws Exception {
        String criteria = "documentCriteria";
        List<DocumentType> entities = List.of(DocumentType.Factory.newInstance());
        Page<DocumentType> page = new PageImpl<>(entities);
        
        when(documentTypeRepository.findAll((Specification<DocumentType>) any(), any(PageRequest.class))).thenReturn(page);

        Page<DocumentTypeDTO> actual = service.handleSearch(criteria, 0, 10);
        
    verify(documentTypeRepository).findAll((Specification<DocumentType>) any(), any(PageRequest.class));
        assertEquals(1, actual.getSize());
    }

    @Test
    void handleAddExpectedFieldUpdatesExistingField() throws Exception {
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

        DocumentTypeDTO actual = service.handleAddExpectedField(id.toString(), Set.of(fieldDTO));

        assertSame(expected, actual);
        assertEquals(1, documentType.getExpectedFields().size());
    }

    @Test
    void handleAddExpectedFieldAddsMultipleNewFields() throws Exception {
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

        DocumentTypeDTO actual = service.handleAddExpectedField(id.toString(), Set.of(fieldDTO1, fieldDTO2));

        assertSame(expected, actual);
        assertEquals(2, documentType.getExpectedFields().size());
    }

    @Test
    void handleAddExpectedFieldThrowsExceptionWhenDocumentTypeNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        ExpectedFieldDTO fieldDTO = new ExpectedFieldDTO();
        
        when(documentTypeRepository.findById(id)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class,
                () -> service.handleAddExpectedField(id.toString(), Set.of(fieldDTO)),
                "DocumentType not found for id: " + id);
    }
}