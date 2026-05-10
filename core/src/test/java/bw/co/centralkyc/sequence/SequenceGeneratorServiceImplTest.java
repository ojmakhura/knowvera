package bw.co.centralkyc.sequence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class SequenceGeneratorServiceImplTest {

    @Mock
    private SequenceGeneratorRepository sequenceGeneratorRepository;
    @Mock
    private SequenceGeneratorMapper sequenceGeneratorMapper;
    @Mock
    private MessageSource messageSource;

    private SequenceGeneratorServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SequenceGeneratorServiceImpl(
                sequenceGeneratorRepository,
                sequenceGeneratorMapper,
                messageSource);
    }

    @Test
    void handleGenerateNextSequenceValueBuildsAndPersistsNextValue() throws Exception {
        SequenceGenerator generator = SequenceGenerator.Factory.newInstance();
        SequencePart prefix = sequencePart(0, SequencePartType.STATIC, "SG-");
        SequencePart year = sequencePart(1, SequencePartType.YEAR, "");
        SequencePart slash = sequencePart(2, SequencePartType.STATIC, "/");
        SequencePart counter = sequencePart(3, SequencePartType.COUNTER, "0000000");
        generator.setSequenceParts(List.of(prefix, year, slash, counter));

        when(sequenceGeneratorRepository.findAll(org.mockito.ArgumentMatchers.<Specification<SequenceGenerator>>any()))
            .thenReturn(List.of(generator));

        String actual = service.generateNextSequenceValue("SG", true);

        assertEquals("SG-" + LocalDate.now().getYear() + "/0000001", actual);
        assertEquals("0000001", counter.getInitialValue());
        verify(sequenceGeneratorRepository).save(generator);
    }

    @Test
    void handleGenerateNextSequenceValueThrowsWhenGeneratorDoesNotExist() {
        when(sequenceGeneratorRepository.findAll(org.mockito.ArgumentMatchers.<Specification<SequenceGenerator>>any()))
            .thenReturn(List.of());

        assertThrows(SequenceGeneratorServiceException.class, () -> service.generateNextSequenceValue("MISSING", true));
    }

    @Test
    void handleFindByNameReturnsMappedDto() throws Exception {
        SequenceGenerator generator = SequenceGenerator.Factory.newInstance();
        SequenceGeneratorDTO expected = new SequenceGeneratorDTO();

        when(sequenceGeneratorRepository.findByName("SEQ")).thenReturn(Optional.of(generator));
        when(sequenceGeneratorMapper.toSequenceGeneratorDTO(generator)).thenReturn(expected);

        SequenceGeneratorDTO actual = service.findByName("SEQ");

        assertSame(expected, actual);
    }

    @Test
    void handleFindByIdReturnsMappedDto() throws Exception {
        UUID id = UUID.randomUUID();
        SequenceGenerator generator = SequenceGenerator.Factory.newInstance();
        SequenceGeneratorDTO expected = new SequenceGeneratorDTO();

        when(sequenceGeneratorRepository.findById(id)).thenReturn(Optional.of(generator));
        when(sequenceGeneratorMapper.toSequenceGeneratorDTO(generator)).thenReturn(expected);

        SequenceGeneratorDTO actual = service.findById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void handleSaveMapsPersistsAndReturnsDto() throws Exception {
        SequenceGeneratorDTO input = validGeneratorDto();
        SequenceGenerator mapped = SequenceGenerator.Factory.newInstance();
        SequenceGenerator saved = SequenceGenerator.Factory.newInstance();
        SequenceGeneratorDTO expected = new SequenceGeneratorDTO();

        when(sequenceGeneratorMapper.sequenceGeneratorDTOToEntity(input)).thenReturn(mapped);
        when(sequenceGeneratorRepository.save(mapped)).thenReturn(saved);
        when(sequenceGeneratorMapper.toSequenceGeneratorDTO(saved)).thenReturn(expected);

        SequenceGeneratorDTO actual = service.save(input);

        assertSame(expected, actual);
    }

    @Test
    void handleRemoveDeletesByIdAndReturnsTrue() throws Exception {
        String id = UUID.randomUUID().toString();

        boolean removed = service.remove(id);

        assertEquals(true, removed);
        verify(sequenceGeneratorRepository).deleteById(UUID.fromString(id));
    }

    @Test
    void handleGetAllMapsRepositoryResults() {
        SequenceGenerator first = SequenceGenerator.Factory.newInstance();
        SequenceGenerator second = SequenceGenerator.Factory.newInstance();
        SequenceGeneratorDTO firstDto = new SequenceGeneratorDTO();
        SequenceGeneratorDTO secondDto = new SequenceGeneratorDTO();

        when(sequenceGeneratorRepository.findAll()).thenReturn(List.of(first, second));
        when(sequenceGeneratorMapper.toSequenceGeneratorDTO(first)).thenReturn(firstDto);
        when(sequenceGeneratorMapper.toSequenceGeneratorDTO(second)).thenReturn(secondDto);

        List<SequenceGeneratorDTO> actual = service.getAll();

        assertEquals(2, actual.size());
        assertSame(firstDto, actual.get(0));
        assertSame(secondDto, actual.get(1));
    }

    @Test
    void handleSearchMapsRepositoryResults() {
        SequenceGenerator match = SequenceGenerator.Factory.newInstance();
        SequenceGeneratorDTO expected = new SequenceGeneratorDTO();

        when(sequenceGeneratorRepository.findAll(org.mockito.ArgumentMatchers.<Specification<SequenceGenerator>>any()))
            .thenReturn(List.of(match));
        when(sequenceGeneratorMapper.toSequenceGeneratorDTO(match)).thenReturn(expected);

        List<SequenceGeneratorDTO> actual = service.search("SEQ");

        assertEquals(1, actual.size());
        assertSame(expected, actual.get(0));
    }

    @Test
    void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById(" "));

        assertThrows(IllegalArgumentException.class, () -> service.save(null));

        SequenceGeneratorDTO missingId = validGeneratorDto();
        missingId.setId(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingId));

        SequenceGeneratorDTO missingName = validGeneratorDto();
        missingName.setName("\n");
        assertThrows(IllegalArgumentException.class, () -> service.save(missingName));

        SequenceGeneratorDTO missingTargetEntity = validGeneratorDto();
        missingTargetEntity.setTargetEntity(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(missingTargetEntity));

        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove("\t"));
        assertThrows(IllegalArgumentException.class, () -> service.generateNextSequenceValue(" ", true));
        assertThrows(IllegalArgumentException.class, () -> service.findByName(""));
    }

    private static SequenceGeneratorDTO validGeneratorDto() {
        SequenceGeneratorDTO input = new SequenceGeneratorDTO();
        input.setId(UUID.randomUUID().toString());
        input.setName("SEQ");
        input.setTargetEntity(bw.co.centralkyc.TargetEntity.CONTACT);
        return input;
    }

    private SequencePart sequencePart(int position, SequencePartType type, String initialValue) {
        SequencePart part = SequencePart.Factory.newInstance();
        part.setPosition(position);
        part.setType(type);
        part.setInitialValue(initialValue);
        return part;
    }
}