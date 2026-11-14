package bw.co.roguesystems.ai.processor.field;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @see bw.co.roguesystems.ai.processor.field.ExpectedFieldService
 */
@Service("expectedFieldService")
public class ExpectedFieldServiceImpl
        extends ExpectedFieldServiceBase {
    public ExpectedFieldServiceImpl(
            ExpectedFieldRepository expectedFieldRepository,
            ExpectedFieldMapper expectedFieldMapper) {

        super(
                expectedFieldRepository,
                expectedFieldMapper);
    }

    /**
     * @see bw.co.roguesystems.ai.processor.field.ExpectedFieldService#findById(String)
     */
    @Override
    protected ExpectedFieldDTO handleFindById(String id)
            throws Exception {

        ExpectedField expectedField = this.expectedFieldRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("ExpectedField not found for id: " + id));

        return this.expectedFieldMapper.toExpectedFieldDTO(expectedField);
    }

    /**
     * @see bw.co.roguesystems.ai.processor.field.ExpectedFieldService#save(@Valid
     *      ExpectedFieldDTO)
     */
    @Override
    protected ExpectedFieldDTO handleSave(@Valid ExpectedFieldDTO expectedField)
            throws Exception {

        ExpectedField expectedFieldEntity = this.expectedFieldMapper.expectedFieldDTOToEntity(expectedField);
        ExpectedField savedExpectedField = this.expectedFieldRepository.save(expectedFieldEntity);
        return this.expectedFieldMapper.toExpectedFieldDTO(savedExpectedField);
    }

    /**
     * @see bw.co.roguesystems.ai.processor.field.ExpectedFieldService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        this.expectedFieldRepository.deleteById(UUID.fromString(id));
        return true;
    }

    @Override
    protected Page<ExpectedFieldDTO> handleFindByDataProcessor(List<String> dataProcessorIds, Integer pageNumber,
            Integer pageSize) throws Exception {

        List<UUID> dataProcessorUUIDs = dataProcessorIds.stream().map(UUID::fromString).toList();
        return this.expectedFieldRepository.findDtoByDataProcessorId(dataProcessorUUIDs, PageRequest.of(pageNumber, pageSize));
    }

    @Override
    protected List<ExpectedFieldDTO> handleFindByDataProcessor(List<String> dataProcessorIds) throws Exception {

        List<UUID> dataProcessorUUIDs = dataProcessorIds.stream().map(UUID::fromString).toList();

        return this.expectedFieldRepository.findDtoByDataProcessorId(dataProcessorUUIDs);
    }

}