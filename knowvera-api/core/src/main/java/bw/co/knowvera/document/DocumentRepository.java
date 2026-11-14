package bw.co.knowvera.document;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DocumentRepository extends JpaRepository<Document, UUID>, JpaSpecificationExecutor<Document> {

    @Query(
        value = """
            SELECT d
FROM Document d
JOIN FETCH d.documentType
        """
    )
    java.util.List<bw.co.knowvera.document.Document> findAll();

    @Query(
        value = """
            SELECT d
FROM Document d
JOIN FETCH d.documentType
        """
    )
    Page<Document> findAll(Pageable pageable);

    @Query(
        value = """
            SELECT d
FROM Document d
JOIN FETCH d.documentType
WHERE d.id = :id
        """
    )
    Optional<bw.co.knowvera.document.Document> findById(java.util.UUID id);

}
