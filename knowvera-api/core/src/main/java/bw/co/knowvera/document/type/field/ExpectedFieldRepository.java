package bw.co.knowvera.document.type.field;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ExpectedFieldRepository extends JpaRepository<ExpectedField, UUID>, JpaSpecificationExecutor<ExpectedField> {

    // The definition of a field (label, type, format, many, mandatory) is not a column: the fields
    // are loaded as entities, which get it when they load (ExpectedFieldDefinitionListener)

    @Query("SELECT f FROM ExpectedField f WHERE f.documentType.id IN :documentTypeIds")
    java.util.List<ExpectedField> findByDocumentTypeIds(java.util.List<java.util.UUID> documentTypeIds);

    @Query("SELECT f FROM ExpectedField f WHERE f.documentType.id IN :documentTypeIds")
    Page<ExpectedField> findByDocumentTypeIds(java.util.List<java.util.UUID> documentTypeIds, Pageable pageable);

}
