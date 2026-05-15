package bw.co.centralkyc.document.type.field;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
class ExpectedFieldServiceImplTest {

    @Mock
    private ExpectedFieldRepository expectedFieldRepository;
    @Mock
    private ExpectedFieldMapper expectedFieldMapper;
    @Mock
    private MessageSource messageSource;

    private ExpectedFieldServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ExpectedFieldServiceImpl(
                expectedFieldRepository,
                expectedFieldMapper,
                messageSource);
    }

    @Test
    void findByIdReturnsMappedExpectedField() throws Exception {
        UUID id = UUID.randomUUID();
        ExpectedField entity = ExpectedField.Factory.newInstance();
        ExpectedFieldDTO expected = new ExpectedFieldDTO();

        when(expectedFieldRepository.findById(id)).thenReturn(Optional.of(entity));
        when(expectedFieldMapper.toExpectedFieldDTO(entity)).thenReturn(expected);

        ExpectedFieldDTO actual = service.findById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void findByDocumentTypeWithPagingConvertsIdsAndDelegates() throws Exception {
        UUID typeId = UUID.randomUUID();
        Page<ExpectedFieldDTO> expected = new PageImpl<>(List.of(new ExpectedFieldDTO()));

        when(expectedFieldRepository.findDtoByDocumentTypeId(List.of(typeId), org.springframework.data.domain.PageRequest.of(1, 10)))
                .thenReturn(expected);

        Page<ExpectedFieldDTO> actual = service.findByDocumentType(List.of(typeId.toString()), 1, 10);

        assertSame(expected, actual);
    }

    @Test
    void removeDeletesById() throws Exception {
        UUID id = UUID.randomUUID();

        boolean removed = service.remove(id.toString());

        assertTrue(removed);
        verify(expectedFieldRepository).deleteById(id);
    }

    @Test
    void findByIdThrowsExceptionWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(expectedFieldRepository.findById(id)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> service.findById(id.toString()),
                "ExpectedField not found for id: " + id);
    }

    @Test
    void saveConvertsAndSavesExpectedField() throws Exception {
        ExpectedFieldDTO input = new ExpectedFieldDTO();
        input.setField("firstName");
        input.setTargetType(bw.co.centralkyc.TargetEntity.INDIVIDUAL);
        input.setFieldType(ExpectedFieldType.STRING);
        ExpectedField entity = ExpectedField.Factory.newInstance();
        ExpectedField savedEntity = ExpectedField.Factory.newInstance();
        ExpectedFieldDTO expected = new ExpectedFieldDTO();

        when(expectedFieldMapper.expectedFieldDTOToEntity(input)).thenReturn(entity);
        when(expectedFieldRepository.save(entity)).thenReturn(savedEntity);
        when(expectedFieldMapper.toExpectedFieldDTO(savedEntity)).thenReturn(expected);

        ExpectedFieldDTO actual = service.save(input);

        assertSame(expected, actual);
        verify(expectedFieldMapper).expectedFieldDTOToEntity(input);
        verify(expectedFieldRepository).save(entity);
        verify(expectedFieldMapper).toExpectedFieldDTO(savedEntity);
    }

    @Test
    void findByDocumentTypeWithoutPagingConvertsIdsAndDelegates() throws Exception {
        UUID typeId1 = UUID.randomUUID();
        UUID typeId2 = UUID.randomUUID();
        List<ExpectedFieldDTO> expected = List.of(new ExpectedFieldDTO(), new ExpectedFieldDTO());

        when(expectedFieldRepository.findDtoByDocumentTypeId(List.of(typeId1, typeId2)))
                .thenReturn(expected);

        List<ExpectedFieldDTO> actual = service.findByDocumentType(List.of(typeId1.toString(), typeId2.toString()));

        assertSame(expected, actual);
        verify(expectedFieldRepository).findDtoByDocumentTypeId(List.of(typeId1, typeId2));
    }

    @Test
    void findByDocumentTypeWithoutPagingReturnsEmptyListWhenNoResults() throws Exception {
        UUID typeId = UUID.randomUUID();
        List<ExpectedFieldDTO> expected = new ArrayList<>();

        when(expectedFieldRepository.findDtoByDocumentTypeId(List.of(typeId)))
                .thenReturn(expected);

        List<ExpectedFieldDTO> actual = service.findByDocumentType(List.of(typeId.toString()));

        assertTrue(actual.isEmpty());
        verify(expectedFieldRepository).findDtoByDocumentTypeId(List.of(typeId));
    }

    @Test
    void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById(" "));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove("\t"));
    }

    @Test
    void serviceBaseSaveGuardsRejectMissingRequiredFields() {
        ExpectedFieldDTO missingField = validExpectedField();
        missingField.setField(" ");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingField));

        ExpectedFieldDTO missingTargetType = validExpectedField();
        missingTargetType.setTargetType(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingTargetType));

        ExpectedFieldDTO missingFieldType = validExpectedField();
        missingFieldType.setFieldType(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingFieldType));
    }

    @Test
    void serviceBaseRoutesInvalidDocumentTypeToServiceException() {
        assertThrows(ExpectedFieldServiceException.class,
                () -> service.findByDocumentType(List.of("not-a-uuid"), 0, 10));
        assertThrows(ExpectedFieldServiceException.class,
                () -> service.findByDocumentType(List.of("not-a-uuid")));
    }

    private static ExpectedFieldDTO validExpectedField() {
        ExpectedFieldDTO dto = new ExpectedFieldDTO();
        dto.setField("firstName");
        dto.setTargetType(bw.co.centralkyc.TargetEntity.INDIVIDUAL);
        dto.setFieldType(ExpectedFieldType.STRING);
        return dto;
    }
}