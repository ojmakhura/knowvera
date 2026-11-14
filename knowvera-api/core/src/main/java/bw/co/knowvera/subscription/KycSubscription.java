// false
// true
package bw.co.knowvera.subscription;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.TimePeriod;
import bw.co.knowvera.invoice.KycInvoice;
import bw.co.knowvera.organisation.Organisation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for KycSubscription
 */
@Entity
@Table(
    name = "KYC_SUBSCRIPTION")
// Uncomment to enable caching for KycSubscription
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "KycSubscription.findAll", query = "SELECT k FROM KycSubscription AS k"),
})
public class KycSubscription
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -8999647920971039053L;

    // Generate 7 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for KycSubscription.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for KycSubscription.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "START_DATE", unique = false, nullable = false, insertable = true, updatable = true)
    private Date startDate;

    /**
     * TODO: Model Documentation for KycSubscription.startDate
     * @return this.startDate Date
     */
    public Date getStartDate()
    {
        return this.startDate;
    }

    /**
     * TODO: Model Documentation for KycSubscription.startDate
     * @param startDateIn Date
     */
    public void setStartDate(Date startDateIn)
    {
        this.startDate = startDateIn;
    }


    @Column(name = "END_DATE", unique = false, nullable = true, insertable = true, updatable = true)
    private Date endDate;

    /**
     * TODO: Model Documentation for KycSubscription.endDate
     * @return this.endDate Date
     */
    public Date getEndDate()
    {
        return this.endDate;
    }

    /**
     * TODO: Model Documentation for KycSubscription.endDate
     * @param endDateIn Date
     */
    public void setEndDate(Date endDateIn)
    {
        this.endDate = endDateIn;
    }


    @Column(name = "AMOUNT", unique = false, nullable = true, insertable = true, updatable = true)
    private Double amount;

    /**
     * TODO: Model Documentation for KycSubscription.amount
     * @return this.amount Double
     */
    public Double getAmount()
    {
        return this.amount;
    }

    /**
     * TODO: Model Documentation for KycSubscription.amount
     * @param amountIn Double
     */
    public void setAmount(Double amountIn)
    {
        this.amount = amountIn;
    }


    @Column(name = "REF", unique = true, nullable = false, insertable = true, updatable = true)
    private String ref;

    /**
     * TODO: Model Documentation for KycSubscription.ref
     * @return this.ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for KycSubscription.ref
     * @param refIn String
     */
    public void setRef(String refIn)
    {
        this.ref = refIn;
    }


    @Column(name = "PERIOD", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private TimePeriod period;

    /**
     * TODO: Model Documentation for KycSubscription.period
     * @return this.period TimePeriod
     */
    public TimePeriod getPeriod()
    {
        return this.period;
    }

    /**
     * TODO: Model Documentation for KycSubscription.period
     * @param periodIn TimePeriod
     */
    public void setPeriod(TimePeriod periodIn)
    {
        this.period = periodIn;
    }


    @Column(name = "STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private KycSubsciptionStatus status;

    /**
     * TODO: Model Documentation for KycSubscription.status
     * @return this.status KycSubsciptionStatus
     */
    public KycSubsciptionStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for KycSubscription.status
     * @param statusIn KycSubsciptionStatus
     */
    public void setStatus(KycSubsciptionStatus statusIn)
    {
        this.status = statusIn;
    }

    // Generate 2 associations
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

    @OneToMany( mappedBy = "kycSubscription",  fetch = FetchType.EAGER, orphanRemoval = true)
    private List<KycInvoice> kycInvoices = new ArrayList<KycInvoice>();
    
    /**
     * TODO: Model Documentation for KycInvoice
     * @return this.kycInvoices List<KycInvoice>
     */

    public List<KycInvoice> getKycInvoices()
    {
        return this.kycInvoices;
    }

    /**
     * TODO: Model Documentation for KycInvoice
     * @param kycInvoicesIn List<KycInvoice>
     */
    public void setKycInvoices(List<KycInvoice> kycInvoicesIn)
    {
        this.kycInvoices = kycInvoicesIn;
    }

    /**
     * TODO: Model Documentation for KycInvoice
     * @param elementToAdd KycInvoice
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addKycInvoices(KycInvoice elementToAdd)
    {
        return this.kycInvoices.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for KycInvoice
     * @param elementToRemove KycInvoice
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeKycInvoices(KycInvoice elementToRemove)
    {
        return this.kycInvoices.remove(elementToRemove);
    }

    /**
     * Returns <code>true</code> if the argument is an KycSubscription instance and all identifiers for this entity
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
        if (!(object instanceof KycSubscription))
        {
            return false;
        }
        final KycSubscription that = (KycSubscription)object;
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
     * Constructs new instances of {@link KycSubscription}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link KycSubscription}.
         * @return new KycSubscription()
         */
        public static KycSubscription newInstance()
        {
            return new KycSubscription();
        }

        /**
         * Constructs a new instance of {@link KycSubscription}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param startDate Date
         * @param ref String
         * @param period TimePeriod
         * @param status KycSubsciptionStatus
         * @param organisation Organisation
         * @return newInstance
         */
        public static KycSubscription newInstance(LocalDateTime createdAt, String createdBy, Date startDate, String ref, TimePeriod period, KycSubsciptionStatus status, Organisation organisation)
        {
            final KycSubscription entity = new KycSubscription();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setStartDate(startDate);
            entity.setRef(ref);
            entity.setPeriod(period);
            entity.setStatus(status);
            entity.setOrganisation(organisation);
            return entity;
        }

        /**
         * Constructs a new instance of {@link KycSubscription}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param startDate Date
         * @param endDate Date
         * @param amount Double
         * @param ref String
         * @param period TimePeriod
         * @param status KycSubsciptionStatus
         * @param organisation Organisation
         * @param kycInvoices List<KycInvoice>
         * @return newInstance KycSubscription
         */
        public static KycSubscription newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, Date startDate, Date endDate, Double amount, String ref, TimePeriod period, KycSubsciptionStatus status, Organisation organisation, List<KycInvoice> kycInvoices)
        {
            final KycSubscription entity = new KycSubscription();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setStartDate(startDate);
            entity.setEndDate(endDate);
            entity.setAmount(amount);
            entity.setRef(ref);
            entity.setPeriod(period);
            entity.setStatus(status);
            entity.setOrganisation(organisation);
            entity.setKycInvoices(kycInvoices);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(KycSubscription other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getStartDate() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getStartDate().compareTo(other.getStartDate()));
            }
            if (this.getEndDate() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getEndDate().compareTo(other.getEndDate()));
            }
            if (this.getAmount() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getAmount().compareTo(other.getAmount()));
            }
            if (this.getRef() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRef().compareTo(other.getRef()));
            }
            if (this.getPeriod() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPeriod().compareTo(other.getPeriod()));
            }
            if (this.getStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getStatus().compareTo(other.getStatus()));
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
        sb.append("KycSubscription(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" startDate=").append(getStartDate());
        sb.append(" endDate=").append(getEndDate());
        sb.append(" amount=").append(getAmount());
        sb.append(" ref=").append(getRef());
        sb.append(" period=").append(getPeriod());
        sb.append(" status=").append(getStatus());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}