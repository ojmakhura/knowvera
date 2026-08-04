package bw.co.kyvera.document.type.verification;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import bw.co.kyvera.document.type.DocumentTypeRepository;
import bw.co.kyvera.document.type.field.ExpectedFieldRepository;

@ExtendWith(MockitoExtension.class)
class VerificationDataConfigServiceImplTest {

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
                verificationDataConfigRepository,
                verificationDataConfigMapper,
                documentTypeRepository,
                expectedFieldRepository,
                jdbcTemplate,
                messageSource);
    }

    @Test
    void findByIdReturnsMappedDto() throws Exception {
        UUID id = UUID.randomUUID();
        VerificationDataConfig entity = VerificationDataConfig.Factory.newInstance();
        VerificationDataConfigDTO expected = new VerificationDataConfigDTO();

        when(verificationDataConfigRepository.findById(id)).thenReturn(Optional.of(entity));
        when(verificationDataConfigMapper.toVerificationDataConfigDTO(entity)).thenReturn(expected);

        VerificationDataConfigDTO actual = service.findById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void saveMapsPersistsAndMapsBack() throws Exception {
        VerificationDataConfigDTO input = new VerificationDataConfigDTO();
        input.setName("primary");
        VerificationDataConfig entity = VerificationDataConfig.Factory.newInstance();
        VerificationDataConfigDTO expected = new VerificationDataConfigDTO();

        when(verificationDataConfigMapper.verificationDataConfigDTOToEntity(input)).thenReturn(entity);
        when(verificationDataConfigRepository.save(entity)).thenReturn(entity);
        when(verificationDataConfigMapper.toVerificationDataConfigDTO(entity)).thenReturn(expected);

        VerificationDataConfigDTO actual = service.save(input);

        assertSame(expected, actual);
    }

    @Test
    void removeDeletesLoadedEntity() throws Exception {
        UUID id = UUID.randomUUID();
        VerificationDataConfig entity = VerificationDataConfig.Factory.newInstance();

        when(verificationDataConfigRepository.getReferenceById(id)).thenReturn(entity);

        boolean removed = service.remove(id.toString());

        assertTrue(removed);
        verify(verificationDataConfigRepository).delete(entity);
    }

    @Test
    void findByIdThrowsExceptionWhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(verificationDataConfigRepository.findById(id)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> service.findById(id.toString()),
                "VerificationDataConfig not found for id: " + id);
    }

    @Test
    void saveVerifiesAllMappingSteps() throws Exception {
        VerificationDataConfigDTO input = new VerificationDataConfigDTO();
        input.setName("primary");
        VerificationDataConfig entity = VerificationDataConfig.Factory.newInstance();
        VerificationDataConfig savedEntity = VerificationDataConfig.Factory.newInstance();
        VerificationDataConfigDTO expected = new VerificationDataConfigDTO();

        when(verificationDataConfigMapper.verificationDataConfigDTOToEntity(input)).thenReturn(entity);
        when(verificationDataConfigRepository.save(entity)).thenReturn(savedEntity);
        when(verificationDataConfigMapper.toVerificationDataConfigDTO(savedEntity)).thenReturn(expected);

        VerificationDataConfigDTO actual = service.save(input);

        assertSame(expected, actual);
        verify(verificationDataConfigMapper).verificationDataConfigDTOToEntity(input);
        verify(verificationDataConfigRepository).save(entity);
        verify(verificationDataConfigMapper).toVerificationDataConfigDTO(savedEntity);
    }

    @Test
    void removeConvertsStringToUuidAndReturnsTrue() throws Exception {
        UUID id = UUID.randomUUID();
        String idString = id.toString();
        VerificationDataConfig entity = VerificationDataConfig.Factory.newInstance();

        when(verificationDataConfigRepository.getReferenceById(id)).thenReturn(entity);

        boolean removed = service.remove(idString);

        assertTrue(removed);
        verify(verificationDataConfigRepository).getReferenceById(id);
        verify(verificationDataConfigRepository).delete(entity);
    }

    @Test
    void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById("\t"));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(" "));
    }

    @Test
    void serviceBaseSaveGuardRejectsMissingName() {
        VerificationDataConfigDTO missingName = new VerificationDataConfigDTO();
        missingName.setName("\n");

        assertThrows(IllegalArgumentException.class, () -> service.save(missingName));
    }
}