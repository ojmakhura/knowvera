package bw.co.knowvera.invoice;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.TimePeriod;
import bw.co.knowvera.document.DocumentDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for KycInvoiceDTO
 */
public class KycInvoiceDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -9131396560684968697L;

    // Class attributes
    /**
     * TODO: Model Documentation for KycInvoiceDTO.amount
     */
    @Nullable
    protected Double amount;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.ref
     */
    protected String ref;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.issueDate
     */
    protected Date issueDate;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.organisationId
     */
    protected String organisationId;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.organisationCode
     */
    protected String organisationCode;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.organisationName
     */
    protected String organisationName;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.organisationRegistrationNo
     */
    protected String organisationRegistrationNo;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.paid
     */
    @Nullable
    protected Boolean paid = false;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.paymentDate
     */
    @Nullable
    protected LocalDateTime paymentDate;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.invoiceDocument
     */
    @Nullable
    protected DocumentDTO invoiceDocument;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.proofOfPayment
     */
    @Nullable
    protected DocumentDTO proofOfPayment;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.paymentReference
     */
    @Nullable
    protected String paymentReference;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.subscriptionRef
     */
    protected String subscriptionRef;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.subscriptionId
     */
    protected String subscriptionId;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.startDate
     */
    @Nullable
    protected LocalDate startDate;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.endDate
     */
    @Nullable
    protected LocalDate endDate;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.subscriptionPeriod
     */
    protected TimePeriod subscriptionPeriod;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.vat
     */
    @Nullable
    protected Double vat;
    /**
     * TODO: Model Documentation for KycInvoiceDTO.totalAmount
     */
    @Nullable
    protected Double totalAmount;

    /** Default Constructor with no properties */
    public KycInvoiceDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param refIn String TODO: Model Documentation for KycInvoiceDTO.ref
     * @param issueDateIn Date TODO: Model Documentation for KycInvoiceDTO.issueDate
     * @param organisationIdIn String TODO: Model Documentation for KycInvoiceDTO.organisationId
     * @param organisationCodeIn String TODO: Model Documentation for KycInvoiceDTO.organisationCode
     * @param organisationNameIn String TODO: Model Documentation for KycInvoiceDTO.organisationName
     * @param organisationRegistrationNoIn String TODO: Model Documentation for KycInvoiceDTO.organisationRegistrationNo
     * @param subscriptionRefIn String TODO: Model Documentation for KycInvoiceDTO.subscriptionRef
     * @param subscriptionIdIn String TODO: Model Documentation for KycInvoiceDTO.subscriptionId
     * @param subscriptionPeriodIn TimePeriod TODO: Model Documentation for KycInvoiceDTO.subscriptionPeriod
     */
    public KycInvoiceDTO(final String refIn, final Date issueDateIn, final String organisationIdIn, final String organisationCodeIn, final String organisationNameIn, final String organisationRegistrationNoIn, final String subscriptionRefIn, final String subscriptionIdIn, final TimePeriod subscriptionPeriodIn)
    {
        super();
        this.ref = refIn;
        this.issueDate = issueDateIn;
        this.organisationId = organisationIdIn;
        this.organisationCode = organisationCodeIn;
        this.organisationName = organisationNameIn;
        this.organisationRegistrationNo = organisationRegistrationNoIn;
        this.subscriptionRef = subscriptionRefIn;
        this.subscriptionId = subscriptionIdIn;
        this.subscriptionPeriod = subscriptionPeriodIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param amountIn Double
     * @param refIn String
     * @param issueDateIn Date
     * @param organisationIdIn String
     * @param organisationCodeIn String
     * @param organisationNameIn String
     * @param organisationRegistrationNoIn String
     * @param paidIn Boolean
     * @param paymentDateIn LocalDateTime
     * @param invoiceDocumentIn DocumentDTO
     * @param proofOfPaymentIn DocumentDTO
     * @param paymentReferenceIn String
     * @param subscriptionRefIn String
     * @param subscriptionIdIn String
     * @param startDateIn LocalDate
     * @param endDateIn LocalDate
     * @param subscriptionPeriodIn TimePeriod
     * @param vatIn Double
     * @param totalAmountIn Double
     */
    public KycInvoiceDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final Double amountIn, final String refIn, final Date issueDateIn, final String organisationIdIn, final String organisationCodeIn, final String organisationNameIn, final String organisationRegistrationNoIn, final Boolean paidIn, final LocalDateTime paymentDateIn, final DocumentDTO invoiceDocumentIn, final DocumentDTO proofOfPaymentIn, final String paymentReferenceIn, final String subscriptionRefIn, final String subscriptionIdIn, final LocalDate startDateIn, final LocalDate endDateIn, final TimePeriod subscriptionPeriodIn, final Double vatIn, final Double totalAmountIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.amount = amountIn;
        this.ref = refIn;
        this.issueDate = issueDateIn;
        this.organisationId = organisationIdIn;
        this.organisationCode = organisationCodeIn;
        this.organisationName = organisationNameIn;
        this.organisationRegistrationNo = organisationRegistrationNoIn;
        this.paid = paidIn;
        this.paymentDate = paymentDateIn;
        this.invoiceDocument = invoiceDocumentIn;
        this.proofOfPayment = proofOfPaymentIn;
        this.paymentReference = paymentReferenceIn;
        this.subscriptionRef = subscriptionRefIn;
        this.subscriptionId = subscriptionIdIn;
        this.startDate = startDateIn;
        this.endDate = endDateIn;
        this.subscriptionPeriod = subscriptionPeriodIn;
        this.vat = vatIn;
        this.totalAmount = totalAmountIn;
    }

    /**
     * Copies constructor from other KycInvoiceDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public KycInvoiceDTO(final KycInvoiceDTO otherBean)
    {
        super(otherBean);
        this.amount = otherBean.getAmount();
        this.ref = otherBean.getRef();
        this.issueDate = otherBean.getIssueDate();
        this.organisationId = otherBean.getOrganisationId();
        this.organisationCode = otherBean.getOrganisationCode();
        this.organisationName = otherBean.getOrganisationName();
        this.organisationRegistrationNo = otherBean.getOrganisationRegistrationNo();
        this.paid = otherBean.getPaid();
        this.paymentDate = otherBean.getPaymentDate();
        this.invoiceDocument = otherBean.getInvoiceDocument();
        this.proofOfPayment = otherBean.getProofOfPayment();
        this.paymentReference = otherBean.getPaymentReference();
        this.subscriptionRef = otherBean.getSubscriptionRef();
        this.subscriptionId = otherBean.getSubscriptionId();
        this.startDate = otherBean.getStartDate();
        this.endDate = otherBean.getEndDate();
        this.subscriptionPeriod = otherBean.getSubscriptionPeriod();
        this.vat = otherBean.getVat();
        this.totalAmount = otherBean.getTotalAmount();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final KycInvoiceDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setAmount(otherBean.getAmount());
            this.setRef(otherBean.getRef());
            this.setIssueDate(otherBean.getIssueDate());
            this.setOrganisationId(otherBean.getOrganisationId());
            this.setOrganisationCode(otherBean.getOrganisationCode());
            this.setOrganisationName(otherBean.getOrganisationName());
            this.setOrganisationRegistrationNo(otherBean.getOrganisationRegistrationNo());
            this.setPaid(otherBean.getPaid());
            this.setPaymentDate(otherBean.getPaymentDate());
            this.setInvoiceDocument(otherBean.getInvoiceDocument());
            this.setProofOfPayment(otherBean.getProofOfPayment());
            this.setPaymentReference(otherBean.getPaymentReference());
            this.setSubscriptionRef(otherBean.getSubscriptionRef());
            this.setSubscriptionId(otherBean.getSubscriptionId());
            this.setStartDate(otherBean.getStartDate());
            this.setEndDate(otherBean.getEndDate());
            this.setSubscriptionPeriod(otherBean.getSubscriptionPeriod());
            this.setVat(otherBean.getVat());
            this.setTotalAmount(otherBean.getTotalAmount());
        }
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.amount
     * Get the amount Attribute
     * @return amount Double
     */
    public Double getAmount()
    {
        return this.amount;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.amount
     * @param value Double
     */
    public void setAmount(final Double value)
    {
        this.amount = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.ref
     * Get the ref Attribute
     * @return ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.ref
     * @param value String
     */
    public void setRef(final String value)
    {
        this.ref = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.issueDate
     * Get the issueDate Attribute
     * @return issueDate Date
     */
    public Date getIssueDate()
    {
        return this.issueDate;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.issueDate
     * @param value Date
     */
    public void setIssueDate(final Date value)
    {
        this.issueDate = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.organisationId
     * Get the organisationId Attribute
     * @return organisationId String
     */
    public String getOrganisationId()
    {
        return this.organisationId;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.organisationId
     * @param value String
     */
    public void setOrganisationId(final String value)
    {
        this.organisationId = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.organisationCode
     * Get the organisationCode Attribute
     * @return organisationCode String
     */
    public String getOrganisationCode()
    {
        return this.organisationCode;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.organisationCode
     * @param value String
     */
    public void setOrganisationCode(final String value)
    {
        this.organisationCode = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.organisationName
     * Get the organisationName Attribute
     * @return organisationName String
     */
    public String getOrganisationName()
    {
        return this.organisationName;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.organisationName
     * @param value String
     */
    public void setOrganisationName(final String value)
    {
        this.organisationName = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.organisationRegistrationNo
     * Get the organisationRegistrationNo Attribute
     * @return organisationRegistrationNo String
     */
    public String getOrganisationRegistrationNo()
    {
        return this.organisationRegistrationNo;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.organisationRegistrationNo
     * @param value String
     */
    public void setOrganisationRegistrationNo(final String value)
    {
        this.organisationRegistrationNo = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.paid
     * Get the paid Attribute
     * @return paid Boolean
     */
    public Boolean getPaid()
    {
        return this.paid;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.paid
     * @param value Boolean
     */
    public void setPaid(final Boolean value)
    {
        this.paid = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.paymentDate
     * Get the paymentDate Attribute
     * @return paymentDate LocalDateTime
     */
    public LocalDateTime getPaymentDate()
    {
        return this.paymentDate;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.paymentDate
     * @param value LocalDateTime
     */
    public void setPaymentDate(final LocalDateTime value)
    {
        this.paymentDate = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.invoiceDocument
     * Get the invoiceDocument Attribute
     * @return invoiceDocument DocumentDTO
     */
    public DocumentDTO getInvoiceDocument()
    {
        return this.invoiceDocument;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.invoiceDocument
     * @param value DocumentDTO
     */
    public void setInvoiceDocument(final DocumentDTO value)
    {
        this.invoiceDocument = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.proofOfPayment
     * Get the proofOfPayment Attribute
     * @return proofOfPayment DocumentDTO
     */
    public DocumentDTO getProofOfPayment()
    {
        return this.proofOfPayment;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.proofOfPayment
     * @param value DocumentDTO
     */
    public void setProofOfPayment(final DocumentDTO value)
    {
        this.proofOfPayment = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.paymentReference
     * Get the paymentReference Attribute
     * @return paymentReference String
     */
    public String getPaymentReference()
    {
        return this.paymentReference;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.paymentReference
     * @param value String
     */
    public void setPaymentReference(final String value)
    {
        this.paymentReference = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.subscriptionRef
     * Get the subscriptionRef Attribute
     * @return subscriptionRef String
     */
    public String getSubscriptionRef()
    {
        return this.subscriptionRef;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.subscriptionRef
     * @param value String
     */
    public void setSubscriptionRef(final String value)
    {
        this.subscriptionRef = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.subscriptionId
     * Get the subscriptionId Attribute
     * @return subscriptionId String
     */
    public String getSubscriptionId()
    {
        return this.subscriptionId;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.subscriptionId
     * @param value String
     */
    public void setSubscriptionId(final String value)
    {
        this.subscriptionId = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.startDate
     * Get the startDate Attribute
     * @return startDate LocalDate
     */
    public LocalDate getStartDate()
    {
        return this.startDate;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.startDate
     * @param value LocalDate
     */
    public void setStartDate(final LocalDate value)
    {
        this.startDate = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.endDate
     * Get the endDate Attribute
     * @return endDate LocalDate
     */
    public LocalDate getEndDate()
    {
        return this.endDate;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.endDate
     * @param value LocalDate
     */
    public void setEndDate(final LocalDate value)
    {
        this.endDate = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.subscriptionPeriod
     * Get the subscriptionPeriod Attribute
     * @return subscriptionPeriod TimePeriod
     */
    public TimePeriod getSubscriptionPeriod()
    {
        return this.subscriptionPeriod;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.subscriptionPeriod
     * @param value TimePeriod
     */
    public void setSubscriptionPeriod(final TimePeriod value)
    {
        this.subscriptionPeriod = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.vat
     * Get the vat Attribute
     * @return vat Double
     */
    public Double getVat()
    {
        return this.vat;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.vat
     * @param value Double
     */
    public void setVat(final Double value)
    {
        this.vat = value;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.totalAmount
     * Get the totalAmount Attribute
     * @return totalAmount Double
     */
    public Double getTotalAmount()
    {
        return this.totalAmount;
    }

    /**
     * TODO: Model Documentation for KycInvoiceDTO.totalAmount
     * @param value Double
     */
    public void setTotalAmount(final Double value)
    {
        this.totalAmount = value;
    }

    /**
     * @param object to compare this object against
     * @return boolean if equal
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object==null || object.getClass() != this.getClass())
        {
             return false;
        }
        // Check if the same object instance
        if (object==this)
        {
            return true;
        }
        KycInvoiceDTO rhs = (KycInvoiceDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getAmount(), rhs.getAmount())
            .append(this.getRef(), rhs.getRef())
            .append(this.getIssueDate(), rhs.getIssueDate())
            .append(this.getOrganisationId(), rhs.getOrganisationId())
            .append(this.getOrganisationCode(), rhs.getOrganisationCode())
            .append(this.getOrganisationName(), rhs.getOrganisationName())
            .append(this.getOrganisationRegistrationNo(), rhs.getOrganisationRegistrationNo())
            .append(this.getPaid(), rhs.getPaid())
            .append(this.getPaymentDate(), rhs.getPaymentDate())
            .append(this.getInvoiceDocument(), rhs.getInvoiceDocument())
            .append(this.getProofOfPayment(), rhs.getProofOfPayment())
            .append(this.getPaymentReference(), rhs.getPaymentReference())
            .append(this.getSubscriptionRef(), rhs.getSubscriptionRef())
            .append(this.getSubscriptionId(), rhs.getSubscriptionId())
            .append(this.getStartDate(), rhs.getStartDate())
            .append(this.getEndDate(), rhs.getEndDate())
            .append(this.getSubscriptionPeriod(), rhs.getSubscriptionPeriod())
            .append(this.getVat(), rhs.getVat())
            .append(this.getTotalAmount(), rhs.getTotalAmount())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final AuditableDTO object)
    {
        if (object==null)
        {
            return -1;
        }
        // Check if the same object instance
        if (object==this)
        {
            return 0;
        }
        if (!(object instanceof KycInvoiceDTO))
        {
            return -1;
        }
        KycInvoiceDTO myClass = (KycInvoiceDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getAmount(), myClass.getAmount())
            .append(this.getRef(), myClass.getRef())
            .append(this.getIssueDate(), myClass.getIssueDate())
            .append(this.getOrganisationId(), myClass.getOrganisationId())
            .append(this.getOrganisationCode(), myClass.getOrganisationCode())
            .append(this.getOrganisationName(), myClass.getOrganisationName())
            .append(this.getOrganisationRegistrationNo(), myClass.getOrganisationRegistrationNo())
            .append(this.getPaid(), myClass.getPaid())
            .append(this.getPaymentDate(), myClass.getPaymentDate())
            .append(this.getInvoiceDocument(), myClass.getInvoiceDocument())
            .append(this.getProofOfPayment(), myClass.getProofOfPayment())
            .append(this.getPaymentReference(), myClass.getPaymentReference())
            .append(this.getSubscriptionRef(), myClass.getSubscriptionRef())
            .append(this.getSubscriptionId(), myClass.getSubscriptionId())
            .append(this.getStartDate(), myClass.getStartDate())
            .append(this.getEndDate(), myClass.getEndDate())
            .append(this.getSubscriptionPeriod(), myClass.getSubscriptionPeriod())
            .append(this.getVat(), myClass.getVat())
            .append(this.getTotalAmount(), myClass.getTotalAmount())
            .toComparison();
    }

    /**
     * @return int hashCode value
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(1249046965, -82296885)
            .appendSuper(super.hashCode())
            .append(this.getAmount())
            .append(this.getRef())
            .append(this.getIssueDate())
            .append(this.getOrganisationId())
            .append(this.getOrganisationCode())
            .append(this.getOrganisationName())
            .append(this.getOrganisationRegistrationNo())
            .append(this.getPaid())
            .append(this.getPaymentDate())
            .append(this.getInvoiceDocument())
            .append(this.getProofOfPayment())
            .append(this.getPaymentReference())
            .append(this.getSubscriptionRef())
            .append(this.getSubscriptionId())
            .append(this.getStartDate())
            .append(this.getEndDate())
            .append(this.getSubscriptionPeriod())
            .append(this.getVat())
            .append(this.getTotalAmount())
            .toHashCode();
    }

    /**
     * @return String representation of object
     * @see Object#toString()
     */
    @Override
    public String toString()
    {

        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("\t\"amount\" : \"" + this.getAmount() + "\"");
        builder.append("\t,\"ref\" : \"" + this.getRef() + "\"");
        builder.append("\t,\"issueDate\" : \"" + this.getIssueDate() + "\"");
        builder.append("\t,\"organisationId\" : \"" + this.getOrganisationId() + "\"");
        builder.append("\t,\"organisationCode\" : \"" + this.getOrganisationCode() + "\"");
        builder.append("\t,\"organisationName\" : \"" + this.getOrganisationName() + "\"");
        builder.append("\t,\"organisationRegistrationNo\" : \"" + this.getOrganisationRegistrationNo() + "\"");
        builder.append("\t,\"paid\" : \"" + this.getPaid() + "\"");
        builder.append("\t,\"paymentDate\" : \"" + this.getPaymentDate() + "\"");
        builder.append("\t,\"invoiceDocument\" : \"" + this.getInvoiceDocument() + "\"");
        builder.append("\t,\"proofOfPayment\" : \"" + this.getProofOfPayment() + "\"");
        builder.append("\t,\"paymentReference\" : \"" + this.getPaymentReference() + "\"");
        builder.append("\t,\"subscriptionRef\" : \"" + this.getSubscriptionRef() + "\"");
        builder.append("\t,\"subscriptionId\" : \"" + this.getSubscriptionId() + "\"");
        builder.append("\t,\"startDate\" : \"" + this.getStartDate() + "\"");
        builder.append("\t,\"endDate\" : \"" + this.getEndDate() + "\"");
        builder.append("\t,\"subscriptionPeriod\" : \"" + this.getSubscriptionPeriod() + "\"");
        builder.append("\t,\"vat\" : \"" + this.getVat() + "\"");
        builder.append("\t,\"totalAmount\" : \"" + this.getTotalAmount() + "\"");
        builder.append("\t,\"id\" : \"" + this.getId() + "\"");
        builder.append("\t,\"createdBy\" : \"" + this.getCreatedBy() + "\"");
        builder.append("\t,\"createdAt\" : \"" + this.getCreatedAt() + "\"");
        builder.append("\t,\"modifiedBy\" : \"" + this.getModifiedBy() + "\"");
        builder.append("\t,\"modifiedAt\" : \"" + this.getModifiedAt() + "\"");
        builder.append("}");
        return builder.toString();

    }

    /**
     * Compares the properties of this instance to the properties of the argument. This method will return
     * {@code false} as soon as it detects that the argument is {@code null} or not of the same type as
     * (or a sub-type of) this instance's type.
     *
     * <p/>For array, collection or map properties the comparison will be done one level deep, in other words:
     * the elements will be compared using the {@code equals()} operation.
     *
     * <p/>Note that two properties will be considered equal when both values are {@code null}.
     *
     * @param thatObject the object containing the properties to compare against this instance
     * @return this method will return {@code true} in case the argument has the same type as this class, or is a
     *      sub-type of this class and all properties as found on this class have equal values when queried on that
     *      argument instance; in all other cases this method will return {@code false}
     */
    @Override
    public boolean equalProperties(final Object thatObject)
    {
        if (thatObject == null || !this.getClass().isAssignableFrom(thatObject.getClass()))
        {
            return false;
        }

        @SuppressWarnings("unchecked")
        final KycInvoiceDTO that = (KycInvoiceDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getAmount(), that.getAmount())
            && equal(this.getRef(), that.getRef())
            && equal(this.getIssueDate(), that.getIssueDate())
            && equal(this.getOrganisationId(), that.getOrganisationId())
            && equal(this.getOrganisationCode(), that.getOrganisationCode())
            && equal(this.getOrganisationName(), that.getOrganisationName())
            && equal(this.getOrganisationRegistrationNo(), that.getOrganisationRegistrationNo())
            && equal(this.getPaid(), that.getPaid())
            && equal(this.getPaymentDate(), that.getPaymentDate())
            && equal(this.getInvoiceDocument(), that.getInvoiceDocument())
            && equal(this.getProofOfPayment(), that.getProofOfPayment())
            && equal(this.getPaymentReference(), that.getPaymentReference())
            && equal(this.getSubscriptionRef(), that.getSubscriptionRef())
            && equal(this.getSubscriptionId(), that.getSubscriptionId())
            && equal(this.getStartDate(), that.getStartDate())
            && equal(this.getEndDate(), that.getEndDate())
            && equal(this.getSubscriptionPeriod(), that.getSubscriptionPeriod())
            && equal(this.getVat(), that.getVat())
            && equal(this.getTotalAmount(), that.getTotalAmount())
        ;
    }

}