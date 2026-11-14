package bw.co.knowvera.suspicion;

import java.util.Collection;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring"
)
public abstract class SuspicionCaseMapper {
    
    /**
     * Converts this entity to an object of type {@link SuspicionCaseDTO}.
     * @param entity
     * @return SuspicionCaseDTO
     */
    public abstract SuspicionCaseDTO toSuspicionCaseDTO(SuspicionCase entity);

     /**
     * Converts this DAO's entity to a Collection of instances of type {@link SuspicionCaseDTO}.
     * @param entities
     * @return Collection<SuspicionCaseDTO>     */
    public abstract List<SuspicionCaseDTO> toSuspicionCaseDTOCollection(Collection<SuspicionCase> entities);

    /**
     * Converts an instance of type {@link SuspicionCaseDTO} to this DAO's entity.
     * @param suspicionCaseDTO
     * @return SuspicionCase
     */
    @InheritInverseConfiguration
    public abstract SuspicionCase suspicionCaseDTOToEntity(SuspicionCaseDTO suspicionCaseDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @InheritInverseConfiguration
    public abstract void updateSuspicionCaseFromSuspicionCaseDTO(SuspicionCaseDTO suspicionCaseDTO, @MappingTarget SuspicionCase entity);

}