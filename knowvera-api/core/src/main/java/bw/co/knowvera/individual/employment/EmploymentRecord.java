// false
// true
package bw.co.knowvera.individual.employment;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.settings.SalaryRange;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

/**
 * TODO: Model Documentation for EmploymentRecord
 */
@Entity
@Table(
    name = "EMPLOYMENT_RECORD")
// Uncomment to enable caching for EmploymentRecord
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQuery(name = "EmploymentRecord.findAll", query = "SELECT e FROM EmploymentRecord AS e")
public class EmploymentRecord
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -664068551346996642L;

    // Generate 7 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for EmploymentRecord.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for EmploymentRecord.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "EMPLOYMENT_RECORD_OCCUPATIONS", joinColumns = @JoinColumn(name = "EMPLOYMENT_RECORD_ID"))
    @Column(name = "OCCUPATIONS")
    private SortedSet<String> occupations;

    /**
     * TODO: Model Documentation for
     * EmploymentRecord.occupations
     * @return this.occupations SortedSet<String>
     */
    public SortedSet<String> getOccupations()
    {
        return this.occupations;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecord.occupations
     * @param occupationsIn SortedSet<String>
     */
    public void setOccupations(SortedSet<String> occupationsIn)
    {
        this.occupations = occupationsIn;
    }


    @Column(name = "EMPLOYMENT_START", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDate employmentStart;

    /**
     * TODO: Model Documentation for
     * EmploymentRecord.employmentStart
     * @return this.employmentStart LocalDate
     */
    public LocalDate getEmploymentStart()
    {
        return this.employmentStart;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecord.employmentStart
     * @param employmentStartIn LocalDate
     */
    public void setEmploymentStart(LocalDate employmentStartIn)
    {
        this.employmentStart = employmentStartIn;
    }


    @Column(name = "EMPLOYMENT_END", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDate employmentEnd;

    /**
     * TODO: Model Documentation for
     * EmploymentRecord.employmentEnd
     * @return this.employmentEnd LocalDate
     */
    public LocalDate getEmploymentEnd()
    {
        return this.employmentEnd;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecord.employmentEnd
     * @param employmentEndIn LocalDate
     */
    public void setEmploymentEnd(LocalDate employmentEndIn)
    {
        this.employmentEnd = employmentEndIn;
    }


    @Column(name = "EMPLOYER_ID", unique = false, nullable = true, insertable = true, updatable = true)
    private String employerId;

    /**
     * TODO: Model Documentation for
     * EmploymentRecord.employerId
     * @return this.employerId String
     */
    public String getEmployerId()
    {
        return this.employerId;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecord.employerId
     * @param employerIdIn String
     */
    public void setEmployerId(String employerIdIn)
    {
        this.employerId = employerIdIn;
    }


    @Column(name = "BUSINESS_NATURE", unique = false, nullable = true, insertable = true, updatable = true)
    private String businessNature;

    /**
     * TODO: Model Documentation for
     * EmploymentRecord.businessNature
     * @return this.businessNature String
     */
    public String getBusinessNature()
    {
        return this.businessNature;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecord.businessNature
     * @param businessNatureIn String
     */
    public void setBusinessNature(String businessNatureIn)
    {
        this.businessNature = businessNatureIn;
    }


    @Column(name = "BUSINESS_LOCATION", unique = false, nullable = true, insertable = true, updatable = true)
    private String businessLocation;

    /**
     * TODO: Model Documentation for
     * EmploymentRecord.businessLocation
     * @return this.businessLocation String
     */
    public String getBusinessLocation()
    {
        return this.businessLocation;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecord.businessLocation
     * @param businessLocationIn String
     */
    public void setBusinessLocation(String businessLocationIn)
    {
        this.businessLocation = businessLocationIn;
    }

    // Generate 3 associations
    @OneToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "SALARY_RANGE_FK", nullable = true, unique = true)
    private SalaryRange salaryRange;

    /**
     * TODO: Model Documentation for SalaryRange
     * @return this.salaryRange SalaryRange
     */

    public SalaryRange getSalaryRange()
    {
        return this.salaryRange;
    }

    /**
     * TODO: Model Documentation for SalaryRange
     * @param salaryRangeIn SalaryRange
     */
    public void setSalaryRange(SalaryRange salaryRangeIn)
    {
        this.salaryRange = salaryRangeIn;
    }

    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    @JoinColumn(name = "INDIVIDUAL_FK")
    private Individual individual;

    /**
     * TODO: Model Documentation for Individual
     * @return this.individual Individual
     */

    public Individual getIndividual()
    {
        return this.individual;
    }

    /**
     * TODO: Model Documentation for Individual
     * @param individualIn Individual
     */
    public void setIndividual(Individual individualIn)
    {
        this.individual = individualIn;
    }

    @OneToMany(cascade = {CascadeType.ALL} , mappedBy = "employmentRecord",  fetch = FetchType.EAGER, orphanRemoval = true)
    private List<KycRecord> kycRecords = new ArrayList<KycRecord>();
    
    /**
     * TODO: Model Documentation for KycRecord
     * @return this.kycRecords List<KycRecord>
     */

    public List<KycRecord> getKycRecords()
    {
        return this.kycRecords;
    }

    /**
     * TODO: Model Documentation for KycRecord
     * @param kycRecordsIn List<KycRecord>
     */
    public void setKycRecords(List<KycRecord> kycRecordsIn)
    {
        this.kycRecords = kycRecordsIn;
    }

    /**
     * TODO: Model Documentation for KycRecord
     * @param elementToAdd KycRecord
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addKycRecords(KycRecord elementToAdd)
    {
        return this.kycRecords.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for KycRecord
     * @param elementToRemove KycRecord
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeKycRecords(KycRecord elementToRemove)
    {
        return this.kycRecords.remove(elementToRemove);
    }

    /**
     * Returns <code>true</code> if the argument is an EmploymentRecord instance and all identifiers for this entity
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
        if (!(object instanceof EmploymentRecord))
        {
            return false;
        }
        final EmploymentRecord that = (EmploymentRecord)object;
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
     * Constructs new instances of {@link EmploymentRecord}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link EmploymentRecord}.
         * @return new EmploymentRecord()
         */
        public static EmploymentRecord newInstance()
        {
            return new EmploymentRecord();
        }

        /**
         * Constructs a new instance of {@link EmploymentRecord}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param occupations SortedSet<String>
         * @param individual Individual
         * @return newInstance
         */
        public static EmploymentRecord newInstance(LocalDateTime createdAt, String createdBy, SortedSet<String> occupations, Individual individual)
        {
            final EmploymentRecord entity = new EmploymentRecord();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setOccupations(occupations);
            entity.setIndividual(individual);
            return entity;
        }

        /**
         * Constructs a new instance of {@link EmploymentRecord}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param occupations SortedSet<String>
         * @param employmentStart LocalDate
         * @param employmentEnd LocalDate
         * @param employerId String
         * @param businessNature String
         * @param businessLocation String
         * @param salaryRange SalaryRange
         * @param individual Individual
         * @param kycRecords List<KycRecord>
         * @return newInstance EmploymentRecord
         */
        public static EmploymentRecord newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, SortedSet<String> occupations, LocalDate employmentStart, LocalDate employmentEnd, String employerId, String businessNature, String businessLocation, SalaryRange salaryRange, Individual individual, List<KycRecord> kycRecords)
        {
            final EmploymentRecord entity = new EmploymentRecord();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setOccupations(occupations);
            entity.setEmploymentStart(employmentStart);
            entity.setEmploymentEnd(employmentEnd);
            entity.setEmployerId(employerId);
            entity.setBusinessNature(businessNature);
            entity.setBusinessLocation(businessLocation);
            entity.setSalaryRange(salaryRange);
            entity.setIndividual(individual);
            entity.setKycRecords(kycRecords);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(EmploymentRecord other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getOccupations() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getOccupations().size()-other.getOccupations().size()));
            }
            if (this.getEmploymentStart() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getEmploymentStart().compareTo(other.getEmploymentStart()));
            }
            if (this.getEmploymentEnd() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getEmploymentEnd().compareTo(other.getEmploymentEnd()));
            }
            if (this.getEmployerId() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getEmployerId().compareTo(other.getEmployerId()));
            }
            if (this.getBusinessNature() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getBusinessNature().compareTo(other.getBusinessNature()));
            }
            if (this.getBusinessLocation() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getBusinessLocation().compareTo(other.getBusinessLocation()));
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
        sb.append("EmploymentRecord(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" occupations=").append(getOccupations());
        sb.append(" employmentStart=").append(getEmploymentStart());
        sb.append(" employmentEnd=").append(getEmploymentEnd());
        sb.append(" employerId=").append(getEmployerId());
        sb.append(" businessNature=").append(getBusinessNature());
        sb.append(" businessLocation=").append(getBusinessLocation());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}