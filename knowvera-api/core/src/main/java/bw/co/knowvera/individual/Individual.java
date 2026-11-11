// false
// true
package bw.co.knowvera.individual;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.PhoneNumber;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.individual.employment.EmploymentRecord;
import bw.co.knowvera.kyc.KycComplianceStatus;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.branch.Branch;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * TODO: Model Documentation for Individual
 */
@Entity
@Table(
    name = "INDIVIDUAL", uniqueConstraints = {
        @UniqueConstraint(
            name="INDIVIDUAL_IDENTITY_UNIQUE",
            columnNames={
                "IDENTITY_NO",
                "IDENTITY_TYPE"
            }
        )
    }
)
// Uncomment to enable caching for Individual
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "Individual.findAll", query = "SELECT i FROM Individual AS i"),
})
public class Individual
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -6207856656895819250L;

    // Generate 22 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for Individual.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for Individual.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "FIRST_NAME", unique = false, nullable = false, insertable = true, updatable = true)
    private String firstName;

    /**
     * TODO: Model Documentation for Individual.firstName
     * @return this.firstName String
     */
    public String getFirstName()
    {
        return this.firstName;
    }

    /**
     * TODO: Model Documentation for Individual.firstName
     * @param firstNameIn String
     */
    public void setFirstName(String firstNameIn)
    {
        this.firstName = firstNameIn;
    }


    @Column(name = "MIDDLE_NAME", unique = false, nullable = true, insertable = true, updatable = true)
    private String middleName;

    /**
     * TODO: Model Documentation for Individual.middleName
     * @return this.middleName String
     */
    public String getMiddleName()
    {
        return this.middleName;
    }

    /**
     * TODO: Model Documentation for Individual.middleName
     * @param middleNameIn String
     */
    public void setMiddleName(String middleNameIn)
    {
        this.middleName = middleNameIn;
    }


    @Column(name = "SURNAME", unique = false, nullable = false, insertable = true, updatable = true)
    private String surname;

    /**
     * TODO: Model Documentation for Individual.surname
     * @return this.surname String
     */
    public String getSurname()
    {
        return this.surname;
    }

    /**
     * TODO: Model Documentation for Individual.surname
     * @param surnameIn String
     */
    public void setSurname(String surnameIn)
    {
        this.surname = surnameIn;
    }


    @Column(name = "IDENTITY_NO", unique = true, nullable = false, insertable = true, updatable = true)
    private String identityNo;

    /**
     * TODO: Model Documentation for Individual.identityNo
     * @return this.identityNo String
     */
    public String getIdentityNo()
    {
        return this.identityNo;
    }

    /**
     * TODO: Model Documentation for Individual.identityNo
     * @param identityNoIn String
     */
    public void setIdentityNo(String identityNoIn)
    {
        this.identityNo = identityNoIn;
    }


    @Column(name = "IDENTITY_TYPE", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private IndividualIdentityType identityType;

    /**
     * TODO: Model Documentation for Individual.identityType
     * @return this.identityType IndividualIdentityType
     */
    public IndividualIdentityType getIdentityType()
    {
        return this.identityType;
    }

    /**
     * TODO: Model Documentation for Individual.identityType
     * @param identityTypeIn IndividualIdentityType
     */
    public void setIdentityType(IndividualIdentityType identityTypeIn)
    {
        this.identityType = identityTypeIn;
    }


    @Column(name = "PHONE_NUMBERS", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<PhoneNumber> phoneNumbers;

    /**
     * TODO: Model Documentation for Individual.phoneNumbers
     * @return this.phoneNumbers List<PhoneNumber>
     */
    public List<PhoneNumber> getPhoneNumbers()
    {
        return this.phoneNumbers;
    }

    /**
     * TODO: Model Documentation for Individual.phoneNumbers
     * @param phoneNumbersIn List<PhoneNumber>
     */
    public void setPhoneNumbers(List<PhoneNumber> phoneNumbersIn)
    {
        this.phoneNumbers = phoneNumbersIn;
    }


    @Column(name = "PHYSICAL_ADDRESS", unique = false, nullable = true, insertable = true, updatable = true)
    private String physicalAddress;

    /**
     * TODO: Model Documentation for Individual.physicalAddress
     * @return this.physicalAddress String
     */
    public String getPhysicalAddress()
    {
        return this.physicalAddress;
    }

    /**
     * TODO: Model Documentation for Individual.physicalAddress
     * @param physicalAddressIn String
     */
    public void setPhysicalAddress(String physicalAddressIn)
    {
        this.physicalAddress = physicalAddressIn;
    }


    @Column(name = "POSTAL_ADDRESS", unique = false, nullable = true, insertable = true, updatable = true)
    private String postalAddress;

    /**
     * TODO: Model Documentation for Individual.postalAddress
     * @return this.postalAddress String
     */
    public String getPostalAddress()
    {
        return this.postalAddress;
    }

    /**
     * TODO: Model Documentation for Individual.postalAddress
     * @param postalAddressIn String
     */
    public void setPostalAddress(String postalAddressIn)
    {
        this.postalAddress = postalAddressIn;
    }


    @Column(name = "EMAIL_ADDRESS", unique = false, nullable = true, insertable = true, updatable = true)
    private String emailAddress;

    /**
     * TODO: Model Documentation for Individual.emailAddress
     * @return this.emailAddress String
     */
    public String getEmailAddress()
    {
        return this.emailAddress;
    }

    /**
     * TODO: Model Documentation for Individual.emailAddress
     * @param emailAddressIn String
     */
    public void setEmailAddress(String emailAddressIn)
    {
        this.emailAddress = emailAddressIn;
    }


    @Column(name = "HAS_USER", unique = false, nullable = true, insertable = true, updatable = true)
    private Boolean hasUser = Boolean.valueOf(false);

    /**
     * TODO: Model Documentation for Individual.hasUser
     * @return this.hasUser Boolean
     */
    public Boolean getHasUser()
    {
        return this.hasUser;
    }

    /**
     * TODO: Model Documentation for Individual.hasUser
     * @param hasUserIn Boolean
     */
    public void setHasUser(Boolean hasUserIn)
    {
        this.hasUser = hasUserIn;
    }


    @Column(name = "KYC_STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private KycComplianceStatus kycStatus;

    /**
     * TODO: Model Documentation for Individual.kycStatus
     * @return this.kycStatus KycComplianceStatus
     */
    public KycComplianceStatus getKycStatus()
    {
        return this.kycStatus;
    }

    /**
     * TODO: Model Documentation for Individual.kycStatus
     * @param kycStatusIn KycComplianceStatus
     */
    public void setKycStatus(KycComplianceStatus kycStatusIn)
    {
        this.kycStatus = kycStatusIn;
    }


    @Column(name = "SEX", unique = false, nullable = true, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    /**
     * TODO: Model Documentation for Individual.sex
     * @return this.sex Sex
     */
    public Sex getSex()
    {
        return this.sex;
    }

    /**
     * TODO: Model Documentation for Individual.sex
     * @param sexIn Sex
     */
    public void setSex(Sex sexIn)
    {
        this.sex = sexIn;
    }


    @Column(name = "NATIONALITY", unique = false, nullable = false, insertable = true, updatable = true)
    private String nationality;

    /**
     * TODO: Model Documentation for Individual.nationality
     * @return this.nationality String
     */
    public String getNationality()
    {
        return this.nationality;
    }

    /**
     * TODO: Model Documentation for Individual.nationality
     * @param nationalityIn String
     */
    public void setNationality(String nationalityIn)
    {
        this.nationality = nationalityIn;
    }


    @Column(name = "MARITAL_STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    /**
     * TODO: Model Documentation for Individual.maritalStatus
     * @return this.maritalStatus MaritalStatus
     */
    public MaritalStatus getMaritalStatus()
    {
        return this.maritalStatus;
    }

    /**
     * TODO: Model Documentation for Individual.maritalStatus
     * @param maritalStatusIn MaritalStatus
     */
    public void setMaritalStatus(MaritalStatus maritalStatusIn)
    {
        this.maritalStatus = maritalStatusIn;
    }


    @Column(name = "EMPLOYMENT_STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    /**
     * TODO: Model Documentation for Individual.employmentStatus
     * @return this.employmentStatus EmploymentStatus
     */
    public EmploymentStatus getEmploymentStatus()
    {
        return this.employmentStatus;
    }

    /**
     * TODO: Model Documentation for Individual.employmentStatus
     * @param employmentStatusIn EmploymentStatus
     */
    public void setEmploymentStatus(EmploymentStatus employmentStatusIn)
    {
        this.employmentStatus = employmentStatusIn;
    }


    @Column(name = "PEP_STATUS", unique = false, nullable = true, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private PepStatus pepStatus;

    /**
     * TODO: Model Documentation for Individual.pepStatus
     * @return this.pepStatus PepStatus
     */
    public PepStatus getPepStatus()
    {
        return this.pepStatus;
    }

    /**
     * TODO: Model Documentation for Individual.pepStatus
     * @param pepStatusIn PepStatus
     */
    public void setPepStatus(PepStatus pepStatusIn)
    {
        this.pepStatus = pepStatusIn;
    }


    @Column(name = "USER_CREATED", unique = false, nullable = true, insertable = true, updatable = true)
    private Boolean userCreated = Boolean.valueOf(false);

    /**
     * TODO: Model Documentation for Individual.userCreated
     * @return this.userCreated Boolean
     */
    public Boolean getUserCreated()
    {
        return this.userCreated;
    }

    /**
     * TODO: Model Documentation for Individual.userCreated
     * @param userCreatedIn Boolean
     */
    public void setUserCreated(Boolean userCreatedIn)
    {
        this.userCreated = userCreatedIn;
    }


    @Column(name = "USER_ID", unique = false, nullable = true, insertable = true, updatable = true)
    private String userId;

    /**
     * TODO: Model Documentation for Individual.userId
     * @return this.userId String
     */
    public String getUserId()
    {
        return this.userId;
    }

    /**
     * TODO: Model Documentation for Individual.userId
     * @param userIdIn String
     */
    public void setUserId(String userIdIn)
    {
        this.userId = userIdIn;
    }


    @Column(name = "DATE_OF_BIRTH", unique = false, nullable = false, insertable = true, updatable = true)
    private LocalDate dateOfBirth;

    /**
     * TODO: Model Documentation for Individual.dateOfBirth
     * @return this.dateOfBirth LocalDate
     */
    public LocalDate getDateOfBirth()
    {
        return this.dateOfBirth;
    }

    /**
     * TODO: Model Documentation for Individual.dateOfBirth
     * @param dateOfBirthIn LocalDate
     */
    public void setDateOfBirth(LocalDate dateOfBirthIn)
    {
        this.dateOfBirth = dateOfBirthIn;
    }


    @Column(name = "CLOSED_AT", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDateTime closedAt;

    /**
     * TODO: Model Documentation for Individual.closedAt
     * @return this.closedAt LocalDateTime
     */
    public LocalDateTime getClosedAt()
    {
        return this.closedAt;
    }

    /**
     * TODO: Model Documentation for Individual.closedAt
     * @param closedAtIn LocalDateTime
     */
    public void setClosedAt(LocalDateTime closedAtIn)
    {
        this.closedAt = closedAtIn;
    }


    @Column(name = "RETAIN_UNTIL", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDate retainUntil;

    /**
     * TODO: Model Documentation for Individual.retainUntil
     * @return this.retainUntil LocalDate
     */
    public LocalDate getRetainUntil()
    {
        return this.retainUntil;
    }

    /**
     * TODO: Model Documentation for Individual.retainUntil
     * @param retainUntilIn LocalDate
     */
    public void setRetainUntil(LocalDate retainUntilIn)
    {
        this.retainUntil = retainUntilIn;
    }

    // Generate 4 associations
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinTable
    (
        name = "DOCUMENTS2INDIVIDUAL",
        joinColumns = {@JoinColumn(name = "INDIVIDUAL_FK")},
        inverseJoinColumns = {@JoinColumn(name = "DOCUMENTS_FK")}
    )
    private List<Document> documents = new ArrayList<Document>();
    
    /**
     * TODO: Model Documentation for Document
     * @return this.documents List<Document>
     */

    public List<Document> getDocuments()
    {
        return this.documents;
    }

    /**
     * TODO: Model Documentation for Document
     * @param documentsIn List<Document>
     */
    public void setDocuments(List<Document> documentsIn)
    {
        this.documents = documentsIn;
    }

    /**
     * TODO: Model Documentation for Document
     * @param elementToAdd Document
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addDocuments(Document elementToAdd)
    {
        return this.documents.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for Document
     * @param elementToRemove Document
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeDocuments(Document elementToRemove)
    {
        return this.documents.remove(elementToRemove);
    }

    @OneToMany( mappedBy = "individual",  fetch = FetchType.EAGER, orphanRemoval = true)
    private List<EmploymentRecord> employmentRecords = new ArrayList<EmploymentRecord>();
    
    /**
     * TODO: Model Documentation for EmploymentRecord
     * @return this.employmentRecords List<EmploymentRecord>
     */

    public List<EmploymentRecord> getEmploymentRecords()
    {
        return this.employmentRecords;
    }

    /**
     * TODO: Model Documentation for EmploymentRecord
     * @param employmentRecordsIn List<EmploymentRecord>
     */
    public void setEmploymentRecords(List<EmploymentRecord> employmentRecordsIn)
    {
        this.employmentRecords = employmentRecordsIn;
    }

    /**
     * TODO: Model Documentation for EmploymentRecord
     * @param elementToAdd EmploymentRecord
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addEmploymentRecords(EmploymentRecord elementToAdd)
    {
        return this.employmentRecords.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for EmploymentRecord
     * @param elementToRemove EmploymentRecord
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeEmploymentRecords(EmploymentRecord elementToRemove)
    {
        return this.employmentRecords.remove(elementToRemove);
    }

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "BRANCH_FK")
    private Branch branch;

    /**
     * TODO: Model Documentation for Branch
     * @return this.branch Branch
     */

    public Branch getBranch()
    {
        return this.branch;
    }

    /**
     * TODO: Model Documentation for Branch
     * @param branchIn Branch
     */
    public void setBranch(Branch branchIn)
    {
        this.branch = branchIn;
    }

    @ManyToOne( fetch = FetchType.LAZY)
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

    /**
     * Returns <code>true</code> if the argument is an Individual instance and all identifiers for this entity
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
        if (!(object instanceof Individual))
        {
            return false;
        }
        final Individual that = (Individual)object;
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
     * Constructs new instances of {@link Individual}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link Individual}.
         * @return new Individual()
         */
        public static Individual newInstance()
        {
            return new Individual();
        }

        /**
         * Constructs a new instance of {@link Individual}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param firstName String
         * @param surname String
         * @param identityNo String
         * @param identityType IndividualIdentityType
         * @param kycStatus KycComplianceStatus
         * @param nationality String
         * @param maritalStatus MaritalStatus
         * @param employmentStatus EmploymentStatus
         * @param dateOfBirth LocalDate
         * @return newInstance
         */
        public static Individual newInstance(LocalDateTime createdAt, String createdBy, String firstName, String surname, String identityNo, IndividualIdentityType identityType, KycComplianceStatus kycStatus, String nationality, MaritalStatus maritalStatus, EmploymentStatus employmentStatus, LocalDate dateOfBirth)
        {
            final Individual entity = new Individual();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setFirstName(firstName);
            entity.setSurname(surname);
            entity.setIdentityNo(identityNo);
            entity.setIdentityType(identityType);
            entity.setKycStatus(kycStatus);
            entity.setNationality(nationality);
            entity.setMaritalStatus(maritalStatus);
            entity.setEmploymentStatus(employmentStatus);
            entity.setDateOfBirth(dateOfBirth);
            return entity;
        }

        /**
         * Constructs a new instance of {@link Individual}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param firstName String
         * @param middleName String
         * @param surname String
         * @param identityNo String
         * @param identityType IndividualIdentityType
         * @param phoneNumbers List<PhoneNumber>
         * @param physicalAddress String
         * @param postalAddress String
         * @param emailAddress String
         * @param hasUser Boolean
         * @param kycStatus KycComplianceStatus
         * @param sex Sex
         * @param nationality String
         * @param maritalStatus MaritalStatus
         * @param employmentStatus EmploymentStatus
         * @param pepStatus PepStatus
         * @param userCreated Boolean
         * @param userId String
         * @param dateOfBirth LocalDate
         * @param closedAt LocalDateTime
         * @param retainUntil LocalDate
         * @param documents List<Document>
         * @param employmentRecords List<EmploymentRecord>
         * @param branch Branch
         * @param organisation Organisation
         * @return newInstance Individual
         */
        public static Individual newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, String firstName, String middleName, String surname, String identityNo, IndividualIdentityType identityType, List<PhoneNumber> phoneNumbers, String physicalAddress, String postalAddress, String emailAddress, Boolean hasUser, KycComplianceStatus kycStatus, Sex sex, String nationality, MaritalStatus maritalStatus, EmploymentStatus employmentStatus, PepStatus pepStatus, Boolean userCreated, String userId, LocalDate dateOfBirth, LocalDateTime closedAt, LocalDate retainUntil, List<Document> documents, List<EmploymentRecord> employmentRecords, Branch branch, Organisation organisation)
        {
            final Individual entity = new Individual();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setFirstName(firstName);
            entity.setMiddleName(middleName);
            entity.setSurname(surname);
            entity.setIdentityNo(identityNo);
            entity.setIdentityType(identityType);
            entity.setPhoneNumbers(phoneNumbers);
            entity.setPhysicalAddress(physicalAddress);
            entity.setPostalAddress(postalAddress);
            entity.setEmailAddress(emailAddress);
            entity.setHasUser(hasUser);
            entity.setKycStatus(kycStatus);
            entity.setSex(sex);
            entity.setNationality(nationality);
            entity.setMaritalStatus(maritalStatus);
            entity.setEmploymentStatus(employmentStatus);
            entity.setPepStatus(pepStatus);
            entity.setUserCreated(userCreated);
            entity.setUserId(userId);
            entity.setDateOfBirth(dateOfBirth);
            entity.setClosedAt(closedAt);
            entity.setRetainUntil(retainUntil);
            entity.setDocuments(documents);
            entity.setEmploymentRecords(employmentRecords);
            entity.setBranch(branch);
            entity.setOrganisation(organisation);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(Individual other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getFirstName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFirstName().compareTo(other.getFirstName()));
            }
            if (this.getMiddleName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMiddleName().compareTo(other.getMiddleName()));
            }
            if (this.getSurname() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSurname().compareTo(other.getSurname()));
            }
            if (this.getIdentityNo() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getIdentityNo().compareTo(other.getIdentityNo()));
            }
            if (this.getIdentityType() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getIdentityType().compareTo(other.getIdentityType()));
            }
            if (this.getPhoneNumbers() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getPhoneNumbers().size()-other.getPhoneNumbers().size()));
            }
            if (this.getPhysicalAddress() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPhysicalAddress().compareTo(other.getPhysicalAddress()));
            }
            if (this.getPostalAddress() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPostalAddress().compareTo(other.getPostalAddress()));
            }
            if (this.getEmailAddress() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getEmailAddress().compareTo(other.getEmailAddress()));
            }
            if (this.getHasUser() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getHasUser().compareTo(other.getHasUser()));
            }
            if (this.getKycStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getKycStatus().compareTo(other.getKycStatus()));
            }
            if (this.getSex() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSex().compareTo(other.getSex()));
            }
            if (this.getNationality() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getNationality().compareTo(other.getNationality()));
            }
            if (this.getMaritalStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMaritalStatus().compareTo(other.getMaritalStatus()));
            }
            if (this.getEmploymentStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getEmploymentStatus().compareTo(other.getEmploymentStatus()));
            }
            if (this.getPepStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPepStatus().compareTo(other.getPepStatus()));
            }
            if (this.getUserCreated() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getUserCreated().compareTo(other.getUserCreated()));
            }
            if (this.getUserId() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getUserId().compareTo(other.getUserId()));
            }
            if (this.getDateOfBirth() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDateOfBirth().compareTo(other.getDateOfBirth()));
            }
            if (this.getClosedAt() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getClosedAt().compareTo(other.getClosedAt()));
            }
            if (this.getRetainUntil() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRetainUntil().compareTo(other.getRetainUntil()));
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
        sb.append("Individual(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" firstName=").append(getFirstName());
        sb.append(" middleName=").append(getMiddleName());
        sb.append(" surname=").append(getSurname());
        sb.append(" identityNo=").append(getIdentityNo());
        sb.append(" identityType=").append(getIdentityType());
        sb.append(" phoneNumbers=").append(getPhoneNumbers());
        sb.append(" physicalAddress=").append(getPhysicalAddress());
        sb.append(" postalAddress=").append(getPostalAddress());
        sb.append(" emailAddress=").append(getEmailAddress());
        sb.append(" hasUser=").append(getHasUser());
        sb.append(" kycStatus=").append(getKycStatus());
        sb.append(" sex=").append(getSex());
        sb.append(" nationality=").append(getNationality());
        sb.append(" maritalStatus=").append(getMaritalStatus());
        sb.append(" employmentStatus=").append(getEmploymentStatus());
        sb.append(" pepStatus=").append(getPepStatus());
        sb.append(" userCreated=").append(getUserCreated());
        sb.append(" userId=").append(getUserId());
        sb.append(" dateOfBirth=").append(getDateOfBirth());
        sb.append(" closedAt=").append(getClosedAt());
        sb.append(" retainUntil=").append(getRetainUntil());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}