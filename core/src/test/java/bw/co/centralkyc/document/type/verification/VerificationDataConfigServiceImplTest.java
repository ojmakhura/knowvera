package bw.co.centralkyc.document.type.verification;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;

import bw.co.centralkyc.document.type.DocumentTypeRepository;
import bw.co.centralkyc.document.type.field.ExpectedFieldRepository;

@ExtendWith(MockitoExtension.class)
class VerificationDataConfigServiceImplTest {

    @Mock
    private VerificationDataConfigDao verificationDataConfigDao;
    @Mock
    private VerificationDataConfigRepository verificationDataConfigRepository;
    @Mock
    private VerificationDataConfigMapper verificationDataConfigMapper;
    @Mock
    private DocumentTypeRepository documentTypeRepository;
    @Mock
    private ExpectedFieldRepository expectedFieldRepository;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private MessageSource messageSource;

    private VerificationDataConfigServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new VerificationDataConfigServiceImpl(
                verificationDataConfigDao,
                verificationDataConfigRepository,
                verificationDataConfigMapper,
                documentTypeRepository,
                expectedFieldRepository,
                jdbcTemplate,
                messageSource);
    }

    @Test
    void handleFindByIdReturnsMappedDto() throws Exception {
        UUID id = UUID.randomUUID();
        VerificationDataConfig entity = VerificationDataConfig.Factory.newInstance();
        VerificationDataConfigDTO expected = new VerificationDataConfigDTO();

        when(verificationDataConfigRepository.findById(id)).thenReturn(Optional.of(entity));
        when(verificationDataConfigMapper.toVerificationDataConfigDTO(entity)).thenReturn(expected);

        VerificationDataConfigDTO actual = service.handleFindById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void handleSaveMapsPersistsAndMapsBack() throws Exception {
        VerificationDataConfigDTO input = new VerificationDataConfigDTO();
        VerificationDataConfig entity = VerificationDataConfig.Factory.newInstance();
        VerificationDataConfigDTO expected = new VerificationDataConfigDTO();

        when(verificationDataConfigMapper.verificationDataConfigDTOToEntity(input)).thenReturn(entity);
        when(verificationDataConfigRepository.save(entity)).thenReturn(entity);
        when(verificationDataConfigMapper.toVerificationDataConfigDTO(entity)).thenReturn(expected);

        VerificationDataConfigDTO actual = service.handleSave(input);

        assertSame(expected, actual);
    }

    @Test
    void handleRemoveDeletesLoadedEntity() throws Exception {
        UUID id = UUID.randomUUID();
        VerificationDataConfig entity = VerificationDataConfig.Factory.newInstance();

        when(verificationDataConfigRepository.getReferenceById(id)).thenReturn(entity);

        boolean removed = service.handleRemove(id.toString());

        assertTrue(removed);
        verify(verificationDataConfigRepository).delete(entity);
    }

    @Test
    void handleFindByIdThrowsExceptionWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(verificationDataConfigRepository.findById(id)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> service.handleFindById(id.toString()),
                "VerificationDataConfig not found for id: " + id);
    }

    @Test
    void handleSaveVerifiesAllMappingSteps() throws Exception {
        VerificationDataConfigDTO input = new VerificationDataConfigDTO();
        VerificationDataConfig entity = VerificationDataConfig.Factory.newInstance();
        VerificationDataConfig savedEntity = VerificationDataConfig.Factory.newInstance();
        VerificationDataConfigDTO expected = new VerificationDataConfigDTO();

        when(verificationDataConfigMapper.verificationDataConfigDTOToEntity(input)).thenReturn(entity);
        when(verificationDataConfigRepository.save(entity)).thenReturn(savedEntity);
        when(verificationDataConfigMapper.toVerificationDataConfigDTO(savedEntity)).thenReturn(expected);

        VerificationDataConfigDTO actual = service.handleSave(input);

        assertSame(expected, actual);
        verify(verificationDataConfigMapper).verificationDataConfigDTOToEntity(input);
        verify(verificationDataConfigRepository).save(entity);
        verify(verificationDataConfigMapper).toVerificationDataConfigDTO(savedEntity);
    }

    @Test
    void handleRemoveConvertsStringToUuidAndReturnsTrue() throws Exception {
        UUID id = UUID.randomUUID();
        String idString = id.toString();
        VerificationDataConfig entity = VerificationDataConfig.Factory.newInstance();

        when(verificationDataConfigRepository.getReferenceById(id)).thenReturn(entity);

        boolean removed = service.handleRemove(idString);

        assertTrue(removed);
        verify(verificationDataConfigRepository).getReferenceById(id);
        verify(verificationDataConfigRepository).delete(entity);
    }
}