package bw.co.knowvera.invoice;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface KycInvoiceRepository extends JpaRepository<KycInvoice, UUID>, JpaSpecificationExecutor<KycInvoice> {

    @Query(
        value = """
            SELECT new bw.co.knowvera.invoice.KycInvoiceDTO(
            CAST(i.id AS text),
            i.createdBy,
            i.createdAt,
            i.modifiedBy,
            i.modifiedAt,
            i.amount,
            i.ref,
            i.issueDate,
           CAST(o.id AS text),
            o.code,
            o.name,
            o.registrationNo,
            i.paid,
            i.paymentDate,
            new bw.co.knowvera.document.DocumentDTO('', ''),
            new bw.co.knowvera.document.DocumentDTO('', ''),
            i.paymentReference,
            s.ref,
            CAST(s.id AS text),
            i.startDate,
            i.endDate,
            s.period,
            i.vat,
            i.totalAmount
        )
FROM KycInvoice i
JOIN i.kycSubscription s
JOIN i.organisation o
WHERE o.id = :organisationId
ORDER BY i.issueDate DESC
        """
    )
    java.util.List<bw.co.knowvera.invoice.KycInvoiceDTO> findInvoicesByOrganisationId(java.util.UUID organisationId);

    @Query(
        value = """
            SELECT new bw.co.knowvera.invoice.KycInvoiceDTO(
            CAST(i.id AS text),
            i.createdBy,
            i.createdAt,
            i.modifiedBy,
            i.modifiedAt,
            i.amount,
            i.ref,
            i.issueDate,
           CAST(o.id AS text),
            o.code,
            o.name,
            o.registrationNo,
            i.paid,
            i.paymentDate,
            new bw.co.knowvera.document.DocumentDTO('', ''),
            new bw.co.knowvera.document.DocumentDTO('', ''),
            i.paymentReference,
            s.ref,
            CAST(s.id AS text),
            i.startDate,
            i.endDate,
            s.period,
            i.vat,
            i.totalAmount
        )
FROM KycInvoice i
JOIN i.kycSubscription s
JOIN i.organisation o
WHERE o.id = :organisationId
ORDER BY i.issueDate DESC
        """
    )
    Page<KycInvoiceDTO> findInvoicesByOrganisationId(java.util.UUID organisationId, Pageable pageable);

    @Query(
        value = """
            SELECT new bw.co.knowvera.invoice.KycInvoiceDTO(
            CAST(i.id AS text),
            i.createdBy,
            i.createdAt,
            i.modifiedBy,
            i.modifiedAt,
            i.amount,
            i.ref,
            i.issueDate,
           CAST(o.id AS text),
            o.code,
            o.name,
            o.registrationNo,
            i.paid,
            i.paymentDate,
            new bw.co.knowvera.document.DocumentDTO('', ''),
            new bw.co.knowvera.document.DocumentDTO('', ''),
            i.paymentReference,
            s.ref,
            CAST(s.id AS text),
            i.startDate,
            i.endDate,
            s.period,
            i.vat,
            i.totalAmount
        )
FROM KycInvoice i
JOIN i.kycSubscription s
JOIN i.organisation o
WHERE s.id = :subscriptionId
ORDER BY i.issueDate DESC
        """
    )
    java.util.List<bw.co.knowvera.invoice.KycInvoiceDTO> findInvoicesBySubscriptionId(java.util.UUID subscriptionId);

    @Query(
        value = """
            SELECT new bw.co.knowvera.invoice.KycInvoiceDTO(
            CAST(i.id AS text),
            i.createdBy,
            i.createdAt,
            i.modifiedBy,
            i.modifiedAt,
            i.amount,
            i.ref,
            i.issueDate,
           CAST(o.id AS text),
            o.code,
            o.name,
            o.registrationNo,
            i.paid,
            i.paymentDate,
            new bw.co.knowvera.document.DocumentDTO('', ''),
            new bw.co.knowvera.document.DocumentDTO('', ''),
            i.paymentReference,
            s.ref,
            CAST(s.id AS text),
            i.startDate,
            i.endDate,
            s.period,
            i.vat,
            i.totalAmount
        )
FROM KycInvoice i
JOIN i.kycSubscription s
JOIN i.organisation o
WHERE s.id = :subscriptionId
ORDER BY i.issueDate DESC
        """
    )
    Page<KycInvoiceDTO> findInvoicesBySubscriptionId(java.util.UUID subscriptionId, Pageable pageable);

    @Query(
        value = """
            SELECT COUNT(i.id)
FROM KycInvoice i
JOIN i.organisation o
WHERE o.id = :organisationId
        """
    )
    Optional<java.lang.Long> countInvoicesByOrganisationId(java.util.UUID organisationId);

    @Query(
        value = """
            SELECT COUNT(i.id)
FROM KycInvoice i
WHERE i.paid = :paid
        """
    )
    Optional<java.lang.Long> countInvoices(Boolean paid);

    @Query(
        value = """
            SELECT COUNT(i.id)
FROM KycInvoice i
JOIN i.organisation o
WHERE i.paid = :paid
AND o.id = :organisationId
        """
    )
    Optional<java.lang.Long> countOrganisationInvoices(Boolean paid, java.util.UUID organisationId);

    @Query(
        value = """
            SELECT i
            FROM KycInvoice i
            JOIN FETCH i.kycSubscription s
            JOIN FETCH i.organisation o
            LEFT JOIN i.invoiceDocument d
            LEFT JOIN i.proofOfPayment p
        """
    )
    java.util.List<bw.co.knowvera.invoice.KycInvoice> findAll();

    @Query(
        value = """
            SELECT i
            FROM KycInvoice i
            JOIN FETCH i.kycSubscription s
            JOIN FETCH i.organisation o
            LEFT JOIN i.invoiceDocument d
            LEFT JOIN i.proofOfPayment p
        """
    )
    Page<KycInvoice> findAll(Pageable pageable);

    @Query(
        value = """
            SELECT i
            FROM KycInvoice i
            JOIN FETCH i.kycSubscription s
            JOIN FETCH i.organisation o
            LEFT JOIN i.invoiceDocument d
            LEFT JOIN i.proofOfPayment p
            WHERE i.id = :id
        """
    )
    Optional<bw.co.knowvera.invoice.KycInvoice> findById(java.util.UUID id);

}
