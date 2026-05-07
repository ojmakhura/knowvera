package bw.co.centralkyc.individual.employment;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

@ExtendWith(MockitoExtension.class)
class EmploymentRecordServiceImplTest {

    @Mock
    private EmploymentRecordDao employmentRecordDao;
    @Mock
    private EmploymentRecordRepository employmentRecordRepository;
    @Mock
    private EmploymentRecordMapper employmentRecordMapper;
    @Mock
    private MessageSource messageSource;

    private EmploymentRecordServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmploymentRecordServiceImpl(
                employmentRecordDao,
                employmentRecordRepository,
                employmentRecordMapper,
                messageSource);
    }

    @Test
    void handleFindByIdReturnsMappedEmploymentRecord() throws Exception {
        UUID id = UUID.randomUUID();
        EmploymentRecord entity = EmploymentRecord.Factory.newInstance();
        EmploymentRecordDTO expected = new EmploymentRecordDTO();

        when(employmentRecordRepository.findById(id)).thenReturn(Optional.of(entity));
        when(employmentRecordMapper.toEmploymentRecordDTO(entity)).thenReturn(expected);

        EmploymentRecordDTO actual = service.handleFindById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void handleRemoveThrowsWhenEmploymentRecordDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(employmentRecordRepository.existsById(id)).thenReturn(false);

        assertThrows(EmploymentRecordServiceException.class, () -> service.handleRemove(id.toString()));
    }

    @Test
    void handleGetAllDelegatesToMapper() throws Exception {
        List<EmploymentRecord> entities = List.of(EmploymentRecord.Factory.newInstance());
        List<EmploymentRecordDTO> expected = List.of(new EmploymentRecordDTO());

        when(employmentRecordRepository.findAll()).thenReturn(entities);
        when(employmentRecordMapper.toEmploymentRecordDTOCollection(entities)).thenReturn(expected);

        List<EmploymentRecordDTO> actual = service.handleGetAll();

        assertSame(expected, actual);
    }

    @Test
    void handleRemoveDeletesWhenEmploymentRecordExists() throws Exception {
        UUID id = UUID.randomUUID();
        when(employmentRecordRepository.existsById(id)).thenReturn(true);

        boolean removed = service.handleRemove(id.toString());

        assertTrue(removed);
        verify(employmentRecordRepository).deleteById(id);
    }
}