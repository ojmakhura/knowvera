package bw.co.roguesystems.ai.processor;

import bw.co.roguesystems.ai.processor.field.ExpectedFieldMapper;
import bw.co.roguesystems.ai.utils.MappingUtils;
import bw.co.roguesystems.ai.processor.DataProcessor;
import bw.co.roguesystems.ai.processor.DataProcessorDTO;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    uses = {
        MappingUtils.class,
        ExpectedFieldMapper.class
    }
)
public abstract class DataProcessorMapper {
    
    
    /**
     * Converts this entity to an object of type {@link DataProcessorDTO}.
     * @param entity
     * @return DataProcessorDTO
     */
    @Mapping(target = "expectedFields", source = "expectedFields")
    public abstract DataProcessorDTO toDataProcessorDTO(DataProcessor entity);


     /**
     * Converts this DAO's entity to a Collection of instances of type {@link DataProcessorDTO}.
     * @param entities
     * @return Collection<DataProcessorDTO>     */
    public abstract List<DataProcessorDTO> toDataProcessorDTOCollection(Collection<DataProcessor> entities);

    /**
     * Converts an instance of type {@link DataProcessorDTO} to this DAO's entity.
     * @param dataProcessorDTO
     * @return DataProcessor
     */
    @InheritInverseConfiguration
    public abstract DataProcessor dataProcessorDTOToEntity(DataProcessorDTO dataProcessorDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @InheritInverseConfiguration
    public abstract void updateDataProcessorFromDataProcessorDTO(DataProcessorDTO dataProcessorDTO, @MappingTarget DataProcessor entity);

}