package bw.co.knowvera.individual.employment;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.kyc.KycRecordDTO;
import bw.co.knowvera.organisation.OrganisationListDTO;
import bw.co.knowvera.settings.SalaryRangeDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for EmploymentRecordDTO
 */
public class EmploymentRecordDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 7654753066480589491L;

    // Class attributes
    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.kycRecords
     */
    @Nullable
    protected Set<KycRecordDTO> kycRecords;
    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.positions
     */
    protected SortedSet<String> positions;
    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.employmentStart
     */
    @Nullable
    protected LocalDate employmentStart;
    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.employmentEnd
     */
    @Nullable
    protected LocalDate employmentEnd;
    /**
     * TODO: Model Documentation for EmploymentRecordDTO.name
     */
    protected String name;
    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.identityNo
     */
    protected String identityNo;
    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.individualId
     */
    @Nullable
    protected String individualId;
    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.employer
     */
    @Nullable
    protected OrganisationListDTO employer;
    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.salaryRange
     */
    @Nullable
    protected SalaryRangeDTO salaryRange;
    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.businessNature
     */
    @Nullable
    protected String businessNature;
    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.businessLocation
     */
    @Nullable
    protected String businessLocation;

    /** Default Constructor with no properties */
    public EmploymentRecordDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param positionsIn SortedSet<String> TODO: Model Documentation for EmploymentRecordDTO.positions
     * @param nameIn String TODO: Model Documentation for EmploymentRecordDTO.name
     * @param identityNoIn String TODO: Model Documentation for EmploymentRecordDTO.identityNo
     */
    public EmploymentRecordDTO(final SortedSet<String> positionsIn, final String nameIn, final String identityNoIn)
    {
        super();
        this.positions = positionsIn;
        this.name = nameIn;
        this.identityNo = identityNoIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param kycRecordsIn Set<KycRecordDTO>
     * @param positionsIn SortedSet<String>
     * @param employmentStartIn LocalDate
     * @param employmentEndIn LocalDate
     * @param nameIn String
     * @param identityNoIn String
     * @param individualIdIn String
     * @param employerIn OrganisationListDTO
     * @param salaryRangeIn SalaryRangeDTO
     * @param businessNatureIn String
     * @param businessLocationIn String
     */
    public EmploymentRecordDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final Set<KycRecordDTO> kycRecordsIn, final SortedSet<String> positionsIn, final LocalDate employmentStartIn, final LocalDate employmentEndIn, final String nameIn, final String identityNoIn, final String individualIdIn, final OrganisationListDTO employerIn, final SalaryRangeDTO salaryRangeIn, final String businessNatureIn, final String businessLocationIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.kycRecords = kycRecordsIn;
        this.positions = positionsIn;
        this.employmentStart = employmentStartIn;
        this.employmentEnd = employmentEndIn;
        this.name = nameIn;
        this.identityNo = identityNoIn;
        this.individualId = individualIdIn;
        this.employer = employerIn;
        this.salaryRange = salaryRangeIn;
        this.businessNature = businessNatureIn;
        this.businessLocation = businessLocationIn;
    }

    /**
     * Copies constructor from other EmploymentRecordDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public EmploymentRecordDTO(final EmploymentRecordDTO otherBean)
    {
        super(otherBean);
        this.kycRecords = otherBean.getKycRecords();
        this.positions = otherBean.getPositions();
        this.employmentStart = otherBean.getEmploymentStart();
        this.employmentEnd = otherBean.getEmploymentEnd();
        this.name = otherBean.getName();
        this.identityNo = otherBean.getIdentityNo();
        this.individualId = otherBean.getIndividualId();
        this.employer = otherBean.getEmployer();
        this.salaryRange = otherBean.getSalaryRange();
        this.businessNature = otherBean.getBusinessNature();
        this.businessLocation = otherBean.getBusinessLocation();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final EmploymentRecordDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setKycRecords(otherBean.getKycRecords());
            this.setPositions(otherBean.getPositions());
            this.setEmploymentStart(otherBean.getEmploymentStart());
            this.setEmploymentEnd(otherBean.getEmploymentEnd());
            this.setName(otherBean.getName());
            this.setIdentityNo(otherBean.getIdentityNo());
            this.setIndividualId(otherBean.getIndividualId());
            this.setEmployer(otherBean.getEmployer());
            this.setSalaryRange(otherBean.getSalaryRange());
            this.setBusinessNature(otherBean.getBusinessNature());
            this.setBusinessLocation(otherBean.getBusinessLocation());
        }
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.kycRecords
     * Get the kycRecords Attribute
     * @return kycRecords Set<KycRecordDTO>
     */
    public Set<KycRecordDTO> getKycRecords()
    {
        if (this.kycRecords == null)
        {
            this.kycRecords = new HashSet<KycRecordDTO>();
        }
        return this.kycRecords;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.kycRecords
     * @param value Set<KycRecordDTO>
     */
    public void setKycRecords(final Set<KycRecordDTO> value)
    {
        this.kycRecords = value;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.positions
     * Get the positions Attribute
     * @return positions SortedSet<String>
     */
    public SortedSet<String> getPositions()
    {
        if (this.positions == null)
        {
            this.positions = new TreeSet<String>();
        }
        return this.positions;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.positions
     * @param value SortedSet<String>
     */
    public void setPositions(final SortedSet<String> value)
    {
        this.positions = value;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.employmentStart
     * Get the employmentStart Attribute
     * @return employmentStart LocalDate
     */
    public LocalDate getEmploymentStart()
    {
        return this.employmentStart;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.employmentStart
     * @param value LocalDate
     */
    public void setEmploymentStart(final LocalDate value)
    {
        this.employmentStart = value;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.employmentEnd
     * Get the employmentEnd Attribute
     * @return employmentEnd LocalDate
     */
    public LocalDate getEmploymentEnd()
    {
        return this.employmentEnd;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.employmentEnd
     * @param value LocalDate
     */
    public void setEmploymentEnd(final LocalDate value)
    {
        this.employmentEnd = value;
    }

    /**
     * TODO: Model Documentation for EmploymentRecordDTO.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for EmploymentRecordDTO.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.identityNo
     * Get the identityNo Attribute
     * @return identityNo String
     */
    public String getIdentityNo()
    {
        return this.identityNo;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.identityNo
     * @param value String
     */
    public void setIdentityNo(final String value)
    {
        this.identityNo = value;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.individualId
     * Get the individualId Attribute
     * @return individualId String
     */
    public String getIndividualId()
    {
        return this.individualId;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.individualId
     * @param value String
     */
    public void setIndividualId(final String value)
    {
        this.individualId = value;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.employer
     * Get the employer Attribute
     * @return employer OrganisationListDTO
     */
    public OrganisationListDTO getEmployer()
    {
        return this.employer;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.employer
     * @param value OrganisationListDTO
     */
    public void setEmployer(final OrganisationListDTO value)
    {
        this.employer = value;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.salaryRange
     * Get the salaryRange Attribute
     * @return salaryRange SalaryRangeDTO
     */
    public SalaryRangeDTO getSalaryRange()
    {
        return this.salaryRange;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.salaryRange
     * @param value SalaryRangeDTO
     */
    public void setSalaryRange(final SalaryRangeDTO value)
    {
        this.salaryRange = value;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.businessNature
     * Get the businessNature Attribute
     * @return businessNature String
     */
    public String getBusinessNature()
    {
        return this.businessNature;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.businessNature
     * @param value String
     */
    public void setBusinessNature(final String value)
    {
        this.businessNature = value;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.businessLocation
     * Get the businessLocation Attribute
     * @return businessLocation String
     */
    public String getBusinessLocation()
    {
        return this.businessLocation;
    }

    /**
     * TODO: Model Documentation for
     * EmploymentRecordDTO.businessLocation
     * @param value String
     */
    public void setBusinessLocation(final String value)
    {
        this.businessLocation = value;
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
        EmploymentRecordDTO rhs = (EmploymentRecordDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getKycRecords(), rhs.getKycRecords())
            .append(this.getPositions(), rhs.getPositions())
            .append(this.getEmploymentStart(), rhs.getEmploymentStart())
            .append(this.getEmploymentEnd(), rhs.getEmploymentEnd())
            .append(this.getName(), rhs.getName())
            .append(this.getIdentityNo(), rhs.getIdentityNo())
            .append(this.getIndividualId(), rhs.getIndividualId())
            .append(this.getEmployer(), rhs.getEmployer())
            .append(this.getSalaryRange(), rhs.getSalaryRange())
            .append(this.getBusinessNature(), rhs.getBusinessNature())
            .append(this.getBusinessLocation(), rhs.getBusinessLocation())
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
        if (!(object instanceof EmploymentRecordDTO))
        {
            return -1;
        }
        EmploymentRecordDTO myClass = (EmploymentRecordDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getKycRecords(), myClass.getKycRecords())
            .append(this.getPositions(), myClass.getPositions())
            .append(this.getEmploymentStart(), myClass.getEmploymentStart())
            .append(this.getEmploymentEnd(), myClass.getEmploymentEnd())
            .append(this.getName(), myClass.getName())
            .append(this.getIdentityNo(), myClass.getIdentityNo())
            .append(this.getIndividualId(), myClass.getIndividualId())
            .append(this.getEmployer(), myClass.getEmployer())
            .append(this.getSalaryRange(), myClass.getSalaryRange())
            .append(this.getBusinessNature(), myClass.getBusinessNature())
            .append(this.getBusinessLocation(), myClass.getBusinessLocation())
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
            .append(this.getKycRecords())
            .append(this.getPositions())
            .append(this.getEmploymentStart())
            .append(this.getEmploymentEnd())
            .append(this.getName())
            .append(this.getIdentityNo())
            .append(this.getIndividualId())
            .append(this.getEmployer())
            .append(this.getSalaryRange())
            .append(this.getBusinessNature())
            .append(this.getBusinessLocation())
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
        builder.append("\t\"kycRecords\" : \"" + this.getKycRecords() + "\"");
        builder.append("\t,\"positions\" : \"" + this.getPositions() + "\"");
        builder.append("\t,\"employmentStart\" : \"" + this.getEmploymentStart() + "\"");
        builder.append("\t,\"employmentEnd\" : \"" + this.getEmploymentEnd() + "\"");
        builder.append("\t,\"name\" : \"" + this.getName() + "\"");
        builder.append("\t,\"identityNo\" : \"" + this.getIdentityNo() + "\"");
        builder.append("\t,\"individualId\" : \"" + this.getIndividualId() + "\"");
        builder.append("\t,\"employer\" : \"" + this.getEmployer() + "\"");
        builder.append("\t,\"salaryRange\" : \"" + this.getSalaryRange() + "\"");
        builder.append("\t,\"businessNature\" : \"" + this.getBusinessNature() + "\"");
        builder.append("\t,\"businessLocation\" : \"" + this.getBusinessLocation() + "\"");
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
        final EmploymentRecordDTO that = (EmploymentRecordDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getKycRecords(), that.getKycRecords())
            && equal(this.getPositions(), that.getPositions())
            && equal(this.getEmploymentStart(), that.getEmploymentStart())
            && equal(this.getEmploymentEnd(), that.getEmploymentEnd())
            && equal(this.getName(), that.getName())
            && equal(this.getIdentityNo(), that.getIdentityNo())
            && equal(this.getIndividualId(), that.getIndividualId())
            && equal(this.getEmployer(), that.getEmployer())
            && equal(this.getSalaryRange(), that.getSalaryRange())
            && equal(this.getBusinessNature(), that.getBusinessNature())
            && equal(this.getBusinessLocation(), that.getBusinessLocation())
        ;
    }

}