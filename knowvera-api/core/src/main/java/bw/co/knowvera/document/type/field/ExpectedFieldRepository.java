package bw.co.knowvera.document.type.field;

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
            SELECT NEW bw.co.knowvera.document.type.field.ExpectedFieldDTO(
            CAST(f.id as string), f.createdBy, f.createdAt, f.modifiedBy, f.modifiedAt,
            f.field, f.mandatory, f.format, CAST(d.id as string), d.name, f.matchTo,
            f.targetType, f.many, f.fieldType, f.fieldLabel, f.exactMatch)
            FROM ExpectedField f
            LEFT JOIN f.documentType d
           WHERE d.id IN :documentTypeIds
        """
    )
    java.util.List<bw.co.knowvera.document.type.field.ExpectedFieldDTO> findDtoByDocumentTypeId(java.util.List<java.util.UUID> documentTypeIds);

    @Query(
        value = """
            SELECT NEW bw.co.knowvera.document.type.field.ExpectedFieldDTO(
            CAST(f.id as string), f.createdBy, f.createdAt, f.modifiedBy, f.modifiedAt,
            f.field, f.mandatory, f.format, CAST(d.id as string), d.name, f.matchTo,
            f.targetType, f.many, f.fieldType, f.fieldLabel, f.exactMatch)
            FROM ExpectedField f
            LEFT JOIN f.documentType d
           WHERE d.id IN :documentTypeIds
        """
    )
    Page<ExpectedFieldDTO> findDtoByDocumentTypeId(java.util.List<java.util.UUID> documentTypeIds, Pageable pageable);

    @Query(
        value = """
            SELECT NEW bw.co.knowvera.document.type.field.ExpectedFieldDTO(
            CAST(f.id as string), f.createdBy, f.createdAt, f.modifiedBy, f.modifiedAt,
            f.field, f.mandatory, f.format, CAST(d.id as string), d.name, f.matchTo,
            f.targetType, f.many, f.fieldType, f.fieldLabel, f.exactMatch)
            FROM ExpectedField f
            LEFT JOIN f.documentType d
            WHERE f.id = :id
        """
    )
    Optional<bw.co.knowvera.document.type.field.ExpectedFieldDTO> findDtoById(java.util.UUID id);

    @Query(
        value = """
            SELECT  COUNT(f.id)
            FROM ExpectedField f
            LEFT JOIN f.documentType d
           WHERE d.id = :documentTypeId
        """
    )
    Optional<bw.co.knowvera.document.type.field.ExpectedFieldDTO> countByDocumentTypeId(java.util.UUID documentTypeId);

}
