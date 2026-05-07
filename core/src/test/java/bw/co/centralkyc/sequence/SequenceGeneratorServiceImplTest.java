package bw.co.centralkyc.sequence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    private SequenceGeneratorDao sequenceGeneratorDao;
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
                sequenceGeneratorDao,
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

        when(sequenceGeneratorRepository.findAll(any(Specification.class))).thenReturn(List.of(generator));

        String actual = service.handleGenerateNextSequenceValue("SG", true);

        assertEquals("SG-" + LocalDate.now().getYear() + "/0000001", actual);
        assertEquals("0000001", counter.getInitialValue());
        verify(sequenceGeneratorRepository).save(generator);
    }

    @Test
    void handleGenerateNextSequenceValueThrowsWhenGeneratorDoesNotExist() {
        when(sequenceGeneratorRepository.findAll(any(Specification.class))).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> service.handleGenerateNextSequenceValue("MISSING", true));
    }

    @Test
    void handleFindByNameReturnsMappedDto() throws Exception {
        SequenceGenerator generator = SequenceGenerator.Factory.newInstance();
        SequenceGeneratorDTO expected = new SequenceGeneratorDTO();

        when(sequenceGeneratorRepository.findByName("SEQ")).thenReturn(Optional.of(generator));
        when(sequenceGeneratorMapper.toSequenceGeneratorDTO(generator)).thenReturn(expected);

        SequenceGeneratorDTO actual = service.handleFindByName("SEQ");

        assertSame(expected, actual);
    }

    private SequencePart sequencePart(int position, SequencePartType type, String initialValue) {
        SequencePart part = SequencePart.Factory.newInstance();
        part.setPosition(position);
        part.setType(type);
        part.setInitialValue(initialValue);
        return part;
    }
}