// false
// true
package bw.co.knowvera.invoice;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.subscription.KycSubscription;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for KycInvoice
 */
@Entity
@Table(
    name = "KYC_INVOICE", uniqueConstraints = {
        @UniqueConstraint(
            name="UNIQUE_ORG_INVOICE",
            columnNames={
                "START_DATE",
                "ORGANISATION_FK",
                "KYC_SUBSCRIPTION_FK"
            }
        )
    }
)
// Uncomment to enable caching for KycInvoice
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "KycInvoice.findAll", query = "SELECT k FROM KycInvoice AS k"),
})
public class KycInvoice
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -6929290612249884528L;

    // Generate 11 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for KycInvoice.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for KycInvoice.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "ISSUE_DATE", unique = false, nullable = false, insertable = true, updatable = true)
    private Date issueDate;

    /**
     * TODO: Model Documentation for KycInvoice.issueDate
     * @return this.issueDate Date
     */
    public Date getIssueDate()
    {
        return this.issueDate;
    }

    /**
     * TODO: Model Documentation for KycInvoice.issueDate
     * @param issueDateIn Date
     */
    public void setIssueDate(Date issueDateIn)
    {
        this.issueDate = issueDateIn;
    }


    @Column(name = "AMOUNT", unique = false, nullable = true, insertable = true, updatable = true)
    private Double amount;

    /**
     * TODO: Model Documentation for KycInvoice.amount
     * @return this.amount Double
     */
    public Double getAmount()
    {
        return this.amount;
    }

    /**
     * TODO: Model Documentation for KycInvoice.amount
     * @param amountIn Double
     */
    public void setAmount(Double amountIn)
    {
        this.amount = amountIn;
    }


    @Column(name = "REF", unique = true, nullable = false, insertable = true, updatable = true)
    private String ref;

    /**
     * TODO: Model Documentation for KycInvoice.ref
     * @return this.ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for KycInvoice.ref
     * @param refIn String
     */
    public void setRef(String refIn)
    {
        this.ref = refIn;
    }


    @Column(name = "PAID", unique = false, nullable = true, insertable = true, updatable = true)
    private Boolean paid = Boolean.valueOf(false);

    /**
     * TODO: Model Documentation for KycInvoice.paid
     * @return this.paid Boolean
     */
    public Boolean getPaid()
    {
        return this.paid;
    }

    /**
     * TODO: Model Documentation for KycInvoice.paid
     * @param paidIn Boolean
     */
    public void setPaid(Boolean paidIn)
    {
        this.paid = paidIn;
    }


    @Column(name = "PAYMENT_DATE", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDateTime paymentDate;

    /**
     * TODO: Model Documentation for KycInvoice.paymentDate
     * @return this.paymentDate LocalDateTime
     */
    public LocalDateTime getPaymentDate()
    {
        return this.paymentDate;
    }

    /**
     * TODO: Model Documentation for KycInvoice.paymentDate
     * @param paymentDateIn LocalDateTime
     */
    public void setPaymentDate(LocalDateTime paymentDateIn)
    {
        this.paymentDate = paymentDateIn;
    }


    @Column(name = "PAYMENT_REFERENCE", unique = false, nullable = true, insertable = true, updatable = true)
    private String paymentReference;

    /**
     * TODO: Model Documentation for KycInvoice.paymentReference
     * @return this.paymentReference String
     */
    public String getPaymentReference()
    {
        return this.paymentReference;
    }

    /**
     * TODO: Model Documentation for KycInvoice.paymentReference
     * @param paymentReferenceIn String
     */
    public void setPaymentReference(String paymentReferenceIn)
    {
        this.paymentReference = paymentReferenceIn;
    }


    @Column(name = "START_DATE", unique = false, nullable = false, insertable = true, updatable = true)
    private LocalDate startDate;

    /**
     * TODO: Model Documentation for KycInvoice.startDate
     * @return this.startDate LocalDate
     */
    public LocalDate getStartDate()
    {
        return this.startDate;
    }

    /**
     * TODO: Model Documentation for KycInvoice.startDate
     * @param startDateIn LocalDate
     */
    public void setStartDate(LocalDate startDateIn)
    {
        this.startDate = startDateIn;
    }


    @Column(name = "END_DATE", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDate endDate;

    /**
     * TODO: Model Documentation for KycInvoice.endDate
     * @return this.endDate LocalDate
     */
    public LocalDate getEndDate()
    {
        return this.endDate;
    }

    /**
     * TODO: Model Documentation for KycInvoice.endDate
     * @param endDateIn LocalDate
     */
    public void setEndDate(LocalDate endDateIn)
    {
        this.endDate = endDateIn;
    }


    @Column(name = "VAT", unique = false, nullable = true, insertable = true, updatable = true)
    private Double vat;

    /**
     * TODO: Model Documentation for KycInvoice.vat
     * @return this.vat Double
     */
    public Double getVat()
    {
        return this.vat;
    }

    /**
     * TODO: Model Documentation for KycInvoice.vat
     * @param vatIn Double
     */
    public void setVat(Double vatIn)
    {
        this.vat = vatIn;
    }


    @Column(name = "TOTAL_AMOUNT", unique = false, nullable = true, insertable = true, updatable = true)
    private Double totalAmount;

    /**
     * TODO: Model Documentation for KycInvoice.totalAmount
     * @return this.totalAmount Double
     */
    public Double getTotalAmount()
    {
        return this.totalAmount;
    }

    /**
     * TODO: Model Documentation for KycInvoice.totalAmount
     * @param totalAmountIn Double
     */
    public void setTotalAmount(Double totalAmountIn)
    {
        this.totalAmount = totalAmountIn;
    }

    // Generate 4 associations
    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANISATION_FK")
    private Organisation organisation;

    /**
     * TODO: Model Documentation for Organisation
     * @return this.organisation Organisation
     */

    public Organisation getOrganisation()
    {
        return this.organisation;
    }

    /**
     * TODO: Model Documentation for Organisation
     * @param organisationIn Organisation
     */
    public void setOrganisation(Organisation organisationIn)
    {
        this.organisation = organisationIn;
    }

    @OneToOne()
    @JoinColumn(name = "PROOF_OF_PAYMENT_FK", nullable = true, unique = false)
    private Document proofOfPayment;

    /**
     * TODO: Model Documentation for Document
     * @return this.proofOfPayment Document
     */

    public Document getProofOfPayment()
    {
        return this.proofOfPayment;
    }

    /**
     * TODO: Model Documentation for Document
     * @param proofOfPaymentIn Document
     */
    public void setProofOfPayment(Document proofOfPaymentIn)
    {
        this.proofOfPayment = proofOfPaymentIn;
    }

    @OneToOne()
    @JoinColumn(name = "INVOICE_DOCUMENT_FK", nullable = true, unique = false)
    private Document invoiceDocument;

    /**
     * TODO: Model Documentation for Document
     * @return this.invoiceDocument Document
     */

    public Document getInvoiceDocument()
    {
        return this.invoiceDocument;
    }

    /**
     * TODO: Model Documentation for Document
     * @param invoiceDocumentIn Document
     */
    public void setInvoiceDocument(Document invoiceDocumentIn)
    {
        this.invoiceDocument = invoiceDocumentIn;
    }

    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "KYC_SUBSCRIPTION_FK")
    private KycSubscription kycSubscription;

    /**
     * TODO: Model Documentation for KycSubscription
     * @return this.kycSubscription KycSubscription
     */

    public KycSubscription getKycSubscription()
    {
        return this.kycSubscription;
    }

    /**
     * TODO: Model Documentation for KycSubscription
     * @param kycSubscriptionIn KycSubscription
     */
    public void setKycSubscription(KycSubscription kycSubscriptionIn)
    {
        this.kycSubscription = kycSubscriptionIn;
    }

    /**
     * Returns <code>true</code> if the argument is an KycInvoice instance and all identifiers for this entity
     * equal the identifiers of the argument entity. The <code>equals</code> method of the parent entity
     * will also need to return <code>true</code>. Returns <code>false</code> otherwise.
     *
     * @see Auditable#equals(Object)
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof KycInvoice))
        {
            return false;
        }
        final KycInvoice that = (KycInvoice)object;
        if (this.id == null || that.getId() == null || !this.id.equals(that.getId()))
        {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers and the hash code of the parent entity.
     *
     * @see Auditable#hashCode()
     */
    @Override
    public int hashCode()
    {
        int hashCode = super.hashCode();
        hashCode = 29 * hashCode + (this.id == null ? 0 : this.id.hashCode());

        return hashCode;
    }

    /**
     * Constructs new instances of {@link KycInvoice}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link KycInvoice}.
         * @return new KycInvoice()
         */
        public static KycInvoice newInstance()
        {
            return new KycInvoice();
        }

        /**
         * Constructs a new instance of {@link KycInvoice}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param issueDate Date
         * @param ref String
         * @param startDate LocalDate
         * @param organisation Organisation
         * @param kycSubscription KycSubscription
         * @return newInstance
         */
        public static KycInvoice newInstance(LocalDateTime createdAt, String createdBy, Date issueDate, String ref, LocalDate startDate, Organisation organisation, KycSubscription kycSubscription)
        {
            final KycInvoice entity = new KycInvoice();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setIssueDate(issueDate);
            entity.setRef(ref);
            entity.setStartDate(startDate);
            entity.setOrganisation(organisation);
            entity.setKycSubscription(kycSubscription);
            return entity;
        }

        /**
         * Constructs a new instance of {@link KycInvoice}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param issueDate Date
         * @param amount Double
         * @param ref String
         * @param paid Boolean
         * @param paymentDate LocalDateTime
         * @param paymentReference String
         * @param startDate LocalDate
         * @param endDate LocalDate
         * @param vat Double
         * @param totalAmount Double
         * @param organisation Organisation
         * @param proofOfPayment Document
         * @param invoiceDocument Document
         * @param kycSubscription KycSubscription
         * @return newInstance KycInvoice
         */
        public static KycInvoice newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, Date issueDate, Double amount, String ref, Boolean paid, LocalDateTime paymentDate, String paymentReference, LocalDate startDate, LocalDate endDate, Double vat, Double totalAmount, Organisation organisation, Document proofOfPayment, Document invoiceDocument, KycSubscription kycSubscription)
        {
            final KycInvoice entity = new KycInvoice();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setIssueDate(issueDate);
            entity.setAmount(amount);
            entity.setRef(ref);
            entity.setPaid(paid);
            entity.setPaymentDate(paymentDate);
            entity.setPaymentReference(paymentReference);
            entity.setStartDate(startDate);
            entity.setEndDate(endDate);
            entity.setVat(vat);
            entity.setTotalAmount(totalAmount);
            entity.setOrganisation(organisation);
            entity.setProofOfPayment(proofOfPayment);
            entity.setInvoiceDocument(invoiceDocument);
            entity.setKycSubscription(kycSubscription);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(KycInvoice other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getIssueDate() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getIssueDate().compareTo(other.getIssueDate()));
            }
            if (this.getAmount() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getAmount().compareTo(other.getAmount()));
            }
            if (this.getRef() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRef().compareTo(other.getRef()));
            }
            if (this.getPaid() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPaid().compareTo(other.getPaid()));
            }
            if (this.getPaymentDate() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPaymentDate().compareTo(other.getPaymentDate()));
            }
            if (this.getPaymentReference() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPaymentReference().compareTo(other.getPaymentReference()));
            }
            if (this.getStartDate() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getStartDate().compareTo(other.getStartDate()));
            }
            if (this.getEndDate() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getEndDate().compareTo(other.getEndDate()));
            }
            if (this.getVat() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getVat().compareTo(other.getVat()));
            }
            if (this.getTotalAmount() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTotalAmount().compareTo(other.getTotalAmount()));
            }
        }
        return cmp;
    }

    /**
     * Returns a String representation of the object
     * @return String Textual representation of the object displaying name/value pairs for all attributes
     * @see Object#toString
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("KycInvoice(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" issueDate=").append(getIssueDate());
        sb.append(" amount=").append(getAmount());
        sb.append(" ref=").append(getRef());
        sb.append(" paid=").append(getPaid());
        sb.append(" paymentDate=").append(getPaymentDate());
        sb.append(" paymentReference=").append(getPaymentReference());
        sb.append(" startDate=").append(getStartDate());
        sb.append(" endDate=").append(getEndDate());
        sb.append(" vat=").append(getVat());
        sb.append(" totalAmount=").append(getTotalAmount());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}