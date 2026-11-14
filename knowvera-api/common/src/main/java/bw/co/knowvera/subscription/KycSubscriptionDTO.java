package bw.co.knowvera.subscription;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.TimePeriod;
import bw.co.knowvera.invoice.KycInvoiceDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for KycSubscriptionDTO
 */
public class KycSubscriptionDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 8551893972800293147L;

    // Class attributes
    /**
     * TODO: Model Documentation for KycSubscriptionDTO.startDate
     */
    protected Date startDate;
    /**
     * TODO: Model Documentation for KycSubscriptionDTO.endDate
     */
    @Nullable
    protected Date endDate;
    /**
     * TODO: Model Documentation for KycSubscriptionDTO.amount
     */
    @Nullable
    protected Double amount;
    /**
     * TODO: Model Documentation for KycSubscriptionDTO.ref
     */
    @Nullable
    protected String ref;
    /**
     * TODO: Model Documentation for KycSubscriptionDTO.period
     */
    protected TimePeriod period;
    /**
     * TODO: Model Documentation for KycSubscriptionDTO.organisationId
     */
    @Nullable
    protected String organisationId;
    /**
     * TODO: Model Documentation for KycSubscriptionDTO.organisationCode
     */
    protected String organisationCode;
    /**
     * TODO: Model Documentation for KycSubscriptionDTO.organisationName
     */
    protected String organisationName;
    /**
     * TODO: Model Documentation for
     * KycSubscriptionDTO.organisationRegistrationNo
     */
    protected String organisationRegistrationNo;
    /**
     * TODO: Model Documentation for KycSubscriptionDTO.status
     */
    @Nullable
    protected KycSubsciptionStatus status;
    /**
     * TODO: Model Documentation for KycSubscriptionDTO.invoices
     */
    @Nullable
    protected List<KycInvoiceDTO> invoices;

    /** Default Constructor with no properties */
    public KycSubscriptionDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param startDateIn Date TODO: Model Documentation for KycSubscriptionDTO.startDate
     * @param periodIn TimePeriod TODO: Model Documentation for KycSubscriptionDTO.period
     * @param organisationCodeIn String TODO: Model Documentation for KycSubscriptionDTO.organisationCode
     * @param organisationNameIn String TODO: Model Documentation for KycSubscriptionDTO.organisationName
     * @param organisationRegistrationNoIn String TODO: Model Documentation for
KycSubscriptionDTO.organisationRegistrationNo
     */
    public KycSubscriptionDTO(final Date startDateIn, final TimePeriod periodIn, final String organisationCodeIn, final String organisationNameIn, final String organisationRegistrationNoIn)
    {
        super();
        this.startDate = startDateIn;
        this.period = periodIn;
        this.organisationCode = organisationCodeIn;
        this.organisationName = organisationNameIn;
        this.organisationRegistrationNo = organisationRegistrationNoIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param startDateIn Date
     * @param endDateIn Date
     * @param amountIn Double
     * @param refIn String
     * @param periodIn TimePeriod
     * @param organisationIdIn String
     * @param organisationCodeIn String
     * @param organisationNameIn String
     * @param organisationRegistrationNoIn String
     * @param statusIn KycSubsciptionStatus
     * @param invoicesIn List<KycInvoiceDTO>
     */
    public KycSubscriptionDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final Date startDateIn, final Date endDateIn, final Double amountIn, final String refIn, final TimePeriod periodIn, final String organisationIdIn, final String organisationCodeIn, final String organisationNameIn, final String organisationRegistrationNoIn, final KycSubsciptionStatus statusIn, final List<KycInvoiceDTO> invoicesIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.startDate = startDateIn;
        this.endDate = endDateIn;
        this.amount = amountIn;
        this.ref = refIn;
        this.period = periodIn;
        this.organisationId = organisationIdIn;
        this.organisationCode = organisationCodeIn;
        this.organisationName = organisationNameIn;
        this.organisationRegistrationNo = organisationRegistrationNoIn;
        this.status = statusIn;
        this.invoices = invoicesIn;
    }

    /**
     * Copies constructor from other KycSubscriptionDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public KycSubscriptionDTO(final KycSubscriptionDTO otherBean)
    {
        super(otherBean);
        this.startDate = otherBean.getStartDate();
        this.endDate = otherBean.getEndDate();
        this.amount = otherBean.getAmount();
        this.ref = otherBean.getRef();
        this.period = otherBean.getPeriod();
        this.organisationId = otherBean.getOrganisationId();
        this.organisationCode = otherBean.getOrganisationCode();
        this.organisationName = otherBean.getOrganisationName();
        this.organisationRegistrationNo = otherBean.getOrganisationRegistrationNo();
        this.status = otherBean.getStatus();
        this.invoices = otherBean.getInvoices();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final KycSubscriptionDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setStartDate(otherBean.getStartDate());
            this.setEndDate(otherBean.getEndDate());
            this.setAmount(otherBean.getAmount());
            this.setRef(otherBean.getRef());
            this.setPeriod(otherBean.getPeriod());
            this.setOrganisationId(otherBean.getOrganisationId());
            this.setOrganisationCode(otherBean.getOrganisationCode());
            this.setOrganisationName(otherBean.getOrganisationName());
            this.setOrganisationRegistrationNo(otherBean.getOrganisationRegistrationNo());
            this.setStatus(otherBean.getStatus());
            this.setInvoices(otherBean.getInvoices());
        }
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.startDate
     * Get the startDate Attribute
     * @return startDate Date
     */
    public Date getStartDate()
    {
        return this.startDate;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.startDate
     * @param value Date
     */
    public void setStartDate(final Date value)
    {
        this.startDate = value;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.endDate
     * Get the endDate Attribute
     * @return endDate Date
     */
    public Date getEndDate()
    {
        return this.endDate;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.endDate
     * @param value Date
     */
    public void setEndDate(final Date value)
    {
        this.endDate = value;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.amount
     * Get the amount Attribute
     * @return amount Double
     */
    public Double getAmount()
    {
        return this.amount;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.amount
     * @param value Double
     */
    public void setAmount(final Double value)
    {
        this.amount = value;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.ref
     * Get the ref Attribute
     * @return ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.ref
     * @param value String
     */
    public void setRef(final String value)
    {
        this.ref = value;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.period
     * Get the period Attribute
     * @return period TimePeriod
     */
    public TimePeriod getPeriod()
    {
        return this.period;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.period
     * @param value TimePeriod
     */
    public void setPeriod(final TimePeriod value)
    {
        this.period = value;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.organisationId
     * Get the organisationId Attribute
     * @return organisationId String
     */
    public String getOrganisationId()
    {
        return this.organisationId;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.organisationId
     * @param value String
     */
    public void setOrganisationId(final String value)
    {
        this.organisationId = value;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.organisationCode
     * Get the organisationCode Attribute
     * @return organisationCode String
     */
    public String getOrganisationCode()
    {
        return this.organisationCode;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.organisationCode
     * @param value String
     */
    public void setOrganisationCode(final String value)
    {
        this.organisationCode = value;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.organisationName
     * Get the organisationName Attribute
     * @return organisationName String
     */
    public String getOrganisationName()
    {
        return this.organisationName;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.organisationName
     * @param value String
     */
    public void setOrganisationName(final String value)
    {
        this.organisationName = value;
    }

    /**
     * TODO: Model Documentation for
     * KycSubscriptionDTO.organisationRegistrationNo
     * Get the organisationRegistrationNo Attribute
     * @return organisationRegistrationNo String
     */
    public String getOrganisationRegistrationNo()
    {
        return this.organisationRegistrationNo;
    }

    /**
     * TODO: Model Documentation for
     * KycSubscriptionDTO.organisationRegistrationNo
     * @param value String
     */
    public void setOrganisationRegistrationNo(final String value)
    {
        this.organisationRegistrationNo = value;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.status
     * Get the status Attribute
     * @return status KycSubsciptionStatus
     */
    public KycSubsciptionStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.status
     * @param value KycSubsciptionStatus
     */
    public void setStatus(final KycSubsciptionStatus value)
    {
        this.status = value;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.invoices
     * Get the invoices Attribute
     * @return invoices List<KycInvoiceDTO>
     */
    public List<KycInvoiceDTO> getInvoices()
    {
        if (this.invoices == null)
        {
            this.invoices = new ArrayList<KycInvoiceDTO>();
        }
        return this.invoices;
    }

    /**
     * TODO: Model Documentation for KycSubscriptionDTO.invoices
     * @param value List<KycInvoiceDTO>
     */
    public void setInvoices(final List<KycInvoiceDTO> value)
    {
        this.invoices = value;
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
        KycSubscriptionDTO rhs = (KycSubscriptionDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getStartDate(), rhs.getStartDate())
            .append(this.getEndDate(), rhs.getEndDate())
            .append(this.getAmount(), rhs.getAmount())
            .append(this.getRef(), rhs.getRef())
            .append(this.getPeriod(), rhs.getPeriod())
            .append(this.getOrganisationId(), rhs.getOrganisationId())
            .append(this.getOrganisationCode(), rhs.getOrganisationCode())
            .append(this.getOrganisationName(), rhs.getOrganisationName())
            .append(this.getOrganisationRegistrationNo(), rhs.getOrganisationRegistrationNo())
            .append(this.getStatus(), rhs.getStatus())
            .append(this.getInvoices(), rhs.getInvoices())
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
        if (!(object instanceof KycSubscriptionDTO))
        {
            return -1;
        }
        KycSubscriptionDTO myClass = (KycSubscriptionDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getStartDate(), myClass.getStartDate())
            .append(this.getEndDate(), myClass.getEndDate())
            .append(this.getAmount(), myClass.getAmount())
            .append(this.getRef(), myClass.getRef())
            .append(this.getPeriod(), myClass.getPeriod())
            .append(this.getOrganisationId(), myClass.getOrganisationId())
            .append(this.getOrganisationCode(), myClass.getOrganisationCode())
            .append(this.getOrganisationName(), myClass.getOrganisationName())
            .append(this.getOrganisationRegistrationNo(), myClass.getOrganisationRegistrationNo())
            .append(this.getStatus(), myClass.getStatus())
            .append(this.getInvoices(), myClass.getInvoices())
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
            .append(this.getStartDate())
            .append(this.getEndDate())
            .append(this.getAmount())
            .append(this.getRef())
            .append(this.getPeriod())
            .append(this.getOrganisationId())
            .append(this.getOrganisationCode())
            .append(this.getOrganisationName())
            .append(this.getOrganisationRegistrationNo())
            .append(this.getStatus())
            .append(this.getInvoices())
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
        builder.append("\t\"startDate\" : \"" + this.getStartDate() + "\"");
        builder.append("\t,\"endDate\" : \"" + this.getEndDate() + "\"");
        builder.append("\t,\"amount\" : \"" + this.getAmount() + "\"");
        builder.append("\t,\"ref\" : \"" + this.getRef() + "\"");
        builder.append("\t,\"period\" : \"" + this.getPeriod() + "\"");
        builder.append("\t,\"organisationId\" : \"" + this.getOrganisationId() + "\"");
        builder.append("\t,\"organisationCode\" : \"" + this.getOrganisationCode() + "\"");
        builder.append("\t,\"organisationName\" : \"" + this.getOrganisationName() + "\"");
        builder.append("\t,\"organisationRegistrationNo\" : \"" + this.getOrganisationRegistrationNo() + "\"");
        builder.append("\t,\"status\" : \"" + this.getStatus() + "\"");
        builder.append("\t,\"invoices\" : \"" + this.getInvoices() + "\"");
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
        final KycSubscriptionDTO that = (KycSubscriptionDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getStartDate(), that.getStartDate())
            && equal(this.getEndDate(), that.getEndDate())
            && equal(this.getAmount(), that.getAmount())
            && equal(this.getRef(), that.getRef())
            && equal(this.getPeriod(), that.getPeriod())
            && equal(this.getOrganisationId(), that.getOrganisationId())
            && equal(this.getOrganisationCode(), that.getOrganisationCode())
            && equal(this.getOrganisationName(), that.getOrganisationName())
            && equal(this.getOrganisationRegistrationNo(), that.getOrganisationRegistrationNo())
            && equal(this.getStatus(), that.getStatus())
            && equal(this.getInvoices(), that.getInvoices())
        ;
    }

}