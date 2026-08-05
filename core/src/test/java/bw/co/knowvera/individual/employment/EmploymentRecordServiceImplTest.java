package bw.co.knowvera.individual.employment;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import bw.co.knowvera.individual.employment.EmploymentRecordMapper;
import bw.co.knowvera.individual.employment.EmploymentRecordServiceImpl;
import bw.co.knowvera.individual.employment.EmploymentRecord;
import bw.co.knowvera.individual.employment.EmploymentRecordDTO;
import bw.co.knowvera.individual.employment.EmploymentRecordRepository;
import bw.co.knowvera.individual.employment.EmploymentRecordServiceException;

@ExtendWith(MockitoExtension.class)
class EmploymentRecordServiceImplTest {

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
                employmentRecordRepository,
                employmentRecordMapper,
                messageSource);
    }

    @Test
    void findByIdReturnsMappedEmploymentRecord() throws Exception {
        UUID id = UUID.randomUUID();
        EmploymentRecord entity = EmploymentRecord.Factory.newInstance();
        EmploymentRecordDTO expected = new EmploymentRecordDTO();

        when(employmentRecordRepository.findById(id)).thenReturn(Optional.of(entity));
        when(employmentRecordMapper.toEmploymentRecordDTO(entity)).thenReturn(expected);

        EmploymentRecordDTO actual = service.findById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void removeThrowsWhenEmploymentRecordDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(employmentRecordRepository.existsById(id)).thenReturn(false);

        assertThrows(EmploymentRecordServiceException.class, () -> service.remove(id.toString()));
    }

    @Test
    void getAllDelegatesToMapper() throws Exception {
        List<EmploymentRecord> entities = List.of(EmploymentRecord.Factory.newInstance());
        List<EmploymentRecordDTO> expected = List.of(new EmploymentRecordDTO());

        when(employmentRecordRepository.findAll()).thenReturn(entities);
        when(employmentRecordMapper.toEmploymentRecordDTOCollection(entities)).thenReturn(expected);

        List<EmploymentRecordDTO> actual = service.getAll();

        assertSame(expected, actual);
    }

    @Test
    void removeDeletesWhenEmploymentRecordExists() throws Exception {
        UUID id = UUID.randomUUID();
        when(employmentRecordRepository.existsById(id)).thenReturn(true);

        boolean removed = service.remove(id.toString());

        assertTrue(removed);
        verify(employmentRecordRepository).deleteById(id);
    }

    @Test
    void saveMapsPersistsAndReturnsDto() throws Exception {
        EmploymentRecordDTO input = validEmploymentRecord();
        EmploymentRecord entity = EmploymentRecord.Factory.newInstance();
        EmploymentRecordDTO expected = new EmploymentRecordDTO();

        when(employmentRecordMapper.employmentRecordDTOToEntity(input)).thenReturn(entity);
        when(employmentRecordRepository.save(entity)).thenReturn(entity);
        when(employmentRecordMapper.toEmploymentRecordDTO(entity)).thenReturn(expected);

        EmploymentRecordDTO actual = service.save(input);

        assertSame(expected, actual);
    }

    @Test
    void getAllWithPagingMapsPageContent() throws Exception {
        EmploymentRecord entity = EmploymentRecord.Factory.newInstance();
        EmploymentRecordDTO dto = new EmploymentRecordDTO();
        Page<EmploymentRecord> page = new PageImpl<>(List.of(entity));

        when(employmentRecordRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(employmentRecordMapper.toEmploymentRecordDTO(entity)).thenReturn(dto);

        Page<EmploymentRecordDTO> actual = service.getAll(0, 10);

        assertEquals(1, actual.getContent().size());
        assertSame(dto, actual.getContent().get(0));
    }

    @Test
    void findByIndividualMapsRepositoryResults() throws Exception {
        List<EmploymentRecord> entities = List.of(EmploymentRecord.Factory.newInstance());
        List<EmploymentRecordDTO> expected = List.of(new EmploymentRecordDTO());

        when(employmentRecordRepository.findAll(org.mockito.ArgumentMatchers.<Specification<EmploymentRecord>>any()))
                .thenReturn(entities);
        when(employmentRecordMapper.toEmploymentRecordDTOCollection(entities)).thenReturn(expected);

        List<EmploymentRecordDTO> actual = service.findByIndividual(UUID.randomUUID().toString());

        assertSame(expected, actual);
    }

    @Test
    void serviceBaseRoutesUnsupportedSearchPathsToServiceException() {
        assertThrows(EmploymentRecordServiceException.class, () -> service.search("criteria"));
        assertThrows(EmploymentRecordServiceException.class, () -> service.search("criteria", 0, 10));
    }

    @Test
    void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById("\t"));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(" "));
        assertThrows(IllegalArgumentException.class, () -> service.findByIndividual(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByIndividual("\n"));

        EmploymentRecordDTO missingName = validEmploymentRecord();
        missingName.setName(" ");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingName));

        EmploymentRecordDTO missingIdentityNo = validEmploymentRecord();
        missingIdentityNo.setIdentityNo(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingIdentityNo));
    }

    private static EmploymentRecordDTO validEmploymentRecord() {
        EmploymentRecordDTO input = new EmploymentRecordDTO();
        input.setPositions(new TreeSet<>(java.util.Set.of("Developer")));
        input.setName("Jane Doe");
        input.setIdentityNo("ID-123");
        return input;
    }
}