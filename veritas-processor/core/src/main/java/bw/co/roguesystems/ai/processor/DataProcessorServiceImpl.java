package bw.co.roguesystems.ai.processor;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bw.co.roguesystems.ai.processor.field.ExpectedField;
import bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO;
import bw.co.roguesystems.ai.processor.field.ExpectedFieldMapper;
import bw.co.roguesystems.ai.processor.field.ExpectedFieldRepository;
import bw.co.roguesystems.ai.processor.DataProcessor;
import bw.co.roguesystems.ai.processor.DataProcessorDTO;
import bw.co.roguesystems.ai.processor.DataProcessorRepository;
import bw.co.roguesystems.ai.processor.DataProcessorServiceBase;
import jakarta.validation.Valid;

/**
 * @see bw.co.roguesystems.ai.processor.DataProcessorService
 */
@Service("dataProcessorService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class DataProcessorServiceImpl
        extends DataProcessorServiceBase {

    private final ExpectedFieldRepository expectedFieldRepository;
    private final ExpectedFieldMapper expectedFieldMapper;

    public DataProcessorServiceImpl(DataProcessorRepository dataProcessorRepository,
            DataProcessorMapper dataProcessorMapper, ExpectedFieldRepository expectedFieldRepository,
            ExpectedFieldMapper expectedFieldMapper) {
        super(dataProcessorRepository, dataProcessorMapper);
        // TODO Auto-generated constructor stub

        this.expectedFieldRepository = expectedFieldRepository;
        this.expectedFieldMapper = expectedFieldMapper;
    }

    /**
     * @see bw.co.roguesystems.ai.processor.DataProcessorService#findById(String)
     */
    @Override
    protected DataProcessorDTO handleFindById(String id)
            throws Exception {

        DataProcessor entity = dataProcessorRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new Exception("DataProcessor not found for id: " + id));
        DataProcessorDTO dto = dataProcessorMapper.toDataProcessorDTO(entity);

        // dto.setExpectedFields(expectedFieldRepository.findDtoByDataProcessorId(List.of(entity.getId())));

        return dto;
    }

    /**
     * @see bw.co.roguesystems.ai.processor.DataProcessorService#save(DataProcessorDTO)
     */
    @Override
    protected DataProcessorDTO handleSave(DataProcessorDTO dataProcessor)
            throws Exception {

        DataProcessor doc = dataProcessorMapper.dataProcessorDTOToEntity(dataProcessor);
        doc = dataProcessorRepository.save(doc);
        DataProcessorDTO dto = dataProcessorMapper.toDataProcessorDTO(doc);

        // dto.setExpectedFields(expectedFieldRepository.findDtoByDataProcessorId(doc.getId()));


        return dto;
    }

    /**
     * @see bw.co.roguesystems.ai.processor.DataProcessorService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        dataProcessorRepository.deleteById(UUID.fromString(id));

        return true;
    }

    /**
     * @see bw.co.roguesystems.ai.processor.DataProcessorService#getAll()
     */
    @Override
    protected List<DataProcessorDTO> handleGetAll()
            throws Exception {

        List<DataProcessor> types = dataProcessorRepository.findAll();

        return dataProcessorMapper.toDataProcessorDTOCollection(types);
    }

    private Specification<DataProcessor> createSpecification(String criteria) {

        if (StringUtils.isBlank(criteria)) {
            return null;
        }

        return (root, cq, cb) -> {
            return cb.or(
                    cb.like(cb.upper(root.get("code")), "%" + criteria.toUpperCase() + "%"),
                    cb.like(cb.upper(root.get("name")), "%" + criteria.toUpperCase() + "%"));
        };

    }

    /**
     * @see bw.co.roguesystems.ai.processor.DataProcessorService#search(String)
     */
    @Override
    protected List<DataProcessorDTO> handleSearch(String criteria)
            throws Exception {
        // TODO implement protected List<DataProcessorDTO> handleSearch(String
        // criteria)

        Specification<DataProcessor> spec = this.createSpecification(criteria);

        List<DataProcessor> types = spec == null ? dataProcessorRepository.findAll(Sort.by(Direction.ASC, "name"))
                : dataProcessorRepository.findAll(spec, Sort.by(Direction.ASC, "name"));

        return dataProcessorMapper.toDataProcessorDTOCollection(types);
    }

    /**
     * @see bw.co.roguesystems.ai.processor.DataProcessorService#getAll(Integer,
     *      Integer)
     */
    @Override
    protected Page<DataProcessorDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {
        // TODO implement protected Page<DataProcessorDTO> handleGetAll(Integer
        // pageNumber, Integer pageSize)

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, "name"));
        Page<DataProcessor> types = dataProcessorRepository.findAll(pageable);

        return types.map(type -> dataProcessorMapper.toDataProcessorDTO(type));
    }

    /**
     * @see bw.co.roguesystems.ai.processor.DataProcessorService#search(String,
     *      Integer, Integer)
     */
    @Override
    protected Page<DataProcessorDTO> handleSearch(String criteria, Integer pageNumber, Integer pageSize)
            throws Exception {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, "name"));
        Specification<DataProcessor> spec = this.createSpecification(criteria);
        Page<DataProcessor> types = spec == null ? dataProcessorRepository.findAll(pageable)
                : dataProcessorRepository.findAll(spec, pageable);

        return types.map(type -> dataProcessorMapper.toDataProcessorDTO(type));

    }

    @Override
    protected DataProcessorDTO handleAddExpectedField(String id, @Valid Set<ExpectedFieldDTO> expectedFields)
            throws Exception {

        DataProcessor dataProcessor = dataProcessorRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new Exception("DataProcessor not found for id: " + id));

        
        for (ExpectedFieldDTO expectedFieldDTO : expectedFields) {

            ExpectedField expectedField = expectedFieldMapper.expectedFieldDTOToEntity(expectedFieldDTO);
            if(StringUtils.isNotBlank(expectedFieldDTO.getId())) {
                ExpectedField f = dataProcessor.getExpectedFields().stream()
                        .filter(ef -> ef.getId().toString().equals(expectedFieldDTO.getId()))
                        .findFirst()
                        .orElse(null);

                if(f != null) {
                    f.setField(expectedField.getField());
                    f.setFieldLabel(expectedField.getFieldLabel());
                    f.setFieldType(expectedField.getFieldType());
                    f.setFormat(expectedField.getFormat());
                    f.setMandatory(expectedField.getMandatory());
                    f.setMany(expectedField.getMany());

                } else {
                    dataProcessor.getExpectedFields().add(expectedField);
                }
            } else {

                dataProcessor.getExpectedFields().add(expectedField);
            }

            expectedField.setDataProcessor(dataProcessor);
            // expectedFieldRepository.save(expectedField);
        }

        dataProcessor = dataProcessorRepository.save(dataProcessor);

        return dataProcessorMapper.toDataProcessorDTO(dataProcessor);
    }

}