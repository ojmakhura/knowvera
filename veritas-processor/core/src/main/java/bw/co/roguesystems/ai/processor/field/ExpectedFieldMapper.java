package bw.co.roguesystems.ai.processor.field;

import bw.co.roguesystems.ai.processor.DataProcessorMapper;
import bw.co.roguesystems.ai.utils.MappingUtils;
import bw.co.roguesystems.ai.processor.field.ExpectedField;
import bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO;

import java.util.Collection;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = "spring",
    uses = {
        DataProcessorMapper.class,
        MappingUtils.class
    }
)
public abstract class ExpectedFieldMapper {
    
    /**
     * Converts this entity to an object of type {@link ExpectedFieldDTO}.
     * @param entity
     * @return ExpectedFieldDTO
     */
    // WARNING! No conversion for target.dataProcessor (can't convert source.getDataProcessor():bw.co.roguesystems.ai.processor.DataProcessor to java.lang.String)
    @Mapping(target = "dataProcessor", source = "dataProcessor.name")
    @Mapping(source = "dataProcessor.id", target = "dataProcessorId")
    public abstract ExpectedFieldDTO toExpectedFieldDTO(ExpectedField entity);

     /**
     * Converts this DAO's entity to a Collection of instances of type {@link ExpectedFieldDTO}.
     * @param entities
     * @return Collection<ExpectedFieldDTO>     */
    public abstract List<ExpectedFieldDTO> toExpectedFieldDTOCollection(Collection<ExpectedField> entities);

    /**
     * Converts an instance of type {@link ExpectedFieldDTO} to this DAO's entity.
     * @param expectedFieldDTO
     * @return ExpectedField
     */
    @InheritInverseConfiguration
    public abstract ExpectedField expectedFieldDTOToEntity(ExpectedFieldDTO expectedFieldDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @InheritInverseConfiguration
    public abstract void updateExpectedFieldFromExpectedFieldDTO(ExpectedFieldDTO expectedFieldDTO, @MappingTarget ExpectedField entity);

}