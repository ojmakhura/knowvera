package bw.co.roguesystems.ai.processor.field;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ExpectedFieldRepository extends JpaRepository<ExpectedField, UUID>, JpaSpecificationExecutor<ExpectedField> {

    @Query(
        value = """
            SELECT NEW bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO(
            CAST(f.id as string), f.createdBy, f.createdAt, f.modifiedBy, f.modifiedAt,
            f.field, f.mandatory, f.format, CAST(d.id as string), d.name,
            f.many, f.fieldType, f.fieldLabel)
            FROM ExpectedField f
            LEFT JOIN f.dataProcessor d
           WHERE d.id IN :dataProcessorIds
        """
    )
    java.util.List<bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO> findDtoByDataProcessorId(java.util.List<java.util.UUID> dataProcessorIds);

    @Query(
        value = """
            SELECT NEW bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO(
            CAST(f.id as string), f.createdBy, f.createdAt, f.modifiedBy, f.modifiedAt,
            f.field, f.mandatory, f.format, CAST(d.id as string), d.name,
            f.many, f.fieldType, f.fieldLabel)
            FROM ExpectedField f
            LEFT JOIN f.dataProcessor d
           WHERE d.id IN :dataProcessorIds
        """
    )
    Page<ExpectedFieldDTO> findDtoByDataProcessorId(java.util.List<java.util.UUID> dataProcessorIds, Pageable pageable);

    @Query(
        value = """
            SELECT NEW bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO(
            CAST(f.id as string), f.createdBy, f.createdAt, f.modifiedBy, f.modifiedAt,
            f.field, f.mandatory, f.format, CAST(d.id as string), d.name,
            f.many, f.fieldType, f.fieldLabel)
            FROM ExpectedField f
            LEFT JOIN f.dataProcessor d
            WHERE f.id = :id
        """
    )
    Optional<bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO> findDtoById(java.util.UUID id);

    @Query(
        value = """
            SELECT  COUNT(f.id)
            FROM ExpectedField f
            LEFT JOIN f.dataProcessor d
           WHERE d.id = :dataProcessorId
        """
    )
    Optional<bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO> countByDataProcessorId(java.util.UUID dataProcessorId);

}
