package bw.co.knowvera.settings;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SettingsRepository extends JpaRepository<Settings, UUID>, JpaSpecificationExecutor<Settings> {

    @Query(
        value = """
            SELECT DISTINCT s
            FROM Settings s
            LEFT JOIN FETCH s.invoiceDocumentType
            LEFT JOIN FETCH s.invoiceTemplate
            LEFT JOIN FETCH s.invoiceTemplateType
            LEFT JOIN FETCH s.quotationDocumentType
            LEFT JOIN FETCH s.quotationTemplateType
            LEFT JOIN FETCH s.quotationTemplate
            LEFT JOIN FETCH s.clientRequestFileType
            ORDER BY s.createdAt DESC
        """
    )
    java.util.List<bw.co.knowvera.settings.Settings> findAll();

    @Query(
        value = """
            SELECT DISTINCT s
            FROM Settings s
            LEFT JOIN FETCH s.invoiceDocumentType
            LEFT JOIN FETCH s.invoiceTemplate
            LEFT JOIN FETCH s.invoiceTemplateType
            LEFT JOIN FETCH s.quotationDocumentType
            LEFT JOIN FETCH s.quotationTemplateType
            LEFT JOIN FETCH s.quotationTemplate
            LEFT JOIN FETCH s.clientRequestFileType
            ORDER BY s.createdAt DESC
        """
    )
    Page<Settings> findAll(Pageable pageable);

}
