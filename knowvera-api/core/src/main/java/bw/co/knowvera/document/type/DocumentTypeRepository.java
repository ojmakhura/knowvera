package bw.co.knowvera.document.type;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, UUID>, JpaSpecificationExecutor<DocumentType> {

    @Query(
        value = """
            SELECT dt
	FROM DocumentType dt
	LEFT JOIN FETCH dt.expectedFields
	LEFT JOIN FETCH dt.expiryField
        """
    )
    java.util.List<bw.co.knowvera.document.type.DocumentType> findAll();

    @Query(
        value = """
            SELECT dt
	FROM DocumentType dt
	LEFT JOIN FETCH dt.expectedFields
	LEFT JOIN FETCH dt.expiryField
        """
    )
    Page<DocumentType> findAll(Pageable pageable);

    @Query(
        value = """
            SELECT dt
	FROM DocumentType dt
	LEFT JOIN FETCH dt.expectedFields
	LEFT JOIN FETCH dt.expiryField
	WHERE dt.id = :id
	
        """
    )
    Optional<bw.co.knowvera.document.type.DocumentType> findById(java.util.UUID id);

}
