package bw.co.knowvera.individual;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.PhoneNumber;
import bw.co.knowvera.document.DocumentListDTO;
import bw.co.knowvera.individual.employment.EmploymentRecordDTO;
import bw.co.knowvera.kyc.KycComplianceStatus;
import bw.co.knowvera.kyc.KycRecordDTO;
import bw.co.knowvera.organisation.OrganisationListDTO;
import bw.co.knowvera.organisation.branch.BranchDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for IndividualDTO
 */
public class IndividualDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 3190575547230700828L;

    // Class attributes
    /**
     * TODO: Model Documentation for IndividualDTO.firstName
     */
    protected String firstName;
    /**
     * TODO: Model Documentation for IndividualDTO.middleName
     */
    @Nullable
    protected String middleName;
    /**
     * TODO: Model Documentation for IndividualDTO.surname
     */
    protected String surname;
    /**
     * TODO: Model Documentation for IndividualDTO.identityNo
     */
    protected String identityNo;
    /**
     * TODO: Model Documentation for IndividualDTO.identityType
     */
    protected IndividualIdentityType identityType;
    /**
     * TODO: Model Documentation for IndividualDTO.phoneNumbers
     */
    @Nullable
    protected List<PhoneNumber> phoneNumbers;
    /**
     * TODO: Model Documentation for IndividualDTO.physicalAddress
     */
    @Nullable
    protected String physicalAddress;
    /**
     * TODO: Model Documentation for IndividualDTO.postalAddress
     */
    @Nullable
    protected String postalAddress;
    /**
     * TODO: Model Documentation for IndividualDTO.emailAddress
     */
    @Nullable
    protected String emailAddress;
    /**
     * TODO: Model Documentation for IndividualDTO.hasUser
     */
    @Nullable
    protected Boolean hasUser = false;
    /**
     * TODO: Model Documentation for IndividualDTO.organisation
     */
    @Nullable
    protected OrganisationListDTO organisation;
    /**
     * TODO: Model Documentation for IndividualDTO.branch
     */
    @Nullable
    protected BranchDTO branch;
    /**
     * TODO: Model Documentation for IndividualDTO.roles
     */
    @Nullable
    protected Set<String> roles;
    /**
     * TODO: Model Documentation for IndividualDTO.latestKyc
     */
    @Nullable
    protected KycRecordDTO latestKyc;
    /**
     * TODO: Model Documentation for IndividualDTO.kycStatus
     */
    protected KycComplianceStatus kycStatus;
    /**
     * TODO: Model Documentation for IndividualDTO.employmentRecords
     */
    @Nullable
    protected List<EmploymentRecordDTO> employmentRecords;
    /**
     * TODO: Model Documentation for IndividualDTO.username
     */
    @Nullable
    protected String username;
    /**
     * TODO: Model Documentation for IndividualDTO.sex
     */
    protected Sex sex;
    /**
     * TODO: Model Documentation for IndividualDTO.nationality
     */
    protected String nationality;
    /**
     * TODO: Model Documentation for IndividualDTO.maritalStatus
     */
    protected MaritalStatus maritalStatus;
    /**
     * TODO: Model Documentation for IndividualDTO.employmentStatus
     */
    protected EmploymentStatus employmentStatus;
    /**
     * TODO: Model Documentation for IndividualDTO.pepStatus
     */
    @Nullable
    protected PepStatus pepStatus;
    /**
     * TODO: Model Documentation for IndividualDTO.userCreated
     */
    @Nullable
    protected Boolean userCreated = false;
    /**
     * TODO: Model Documentation for IndividualDTO.userId
     */
    @Nullable
    protected String userId;
    /**
     * TODO: Model Documentation for IndividualDTO.dateOfBirth
     */
    @Nullable
    protected LocalDate dateOfBirth;
    /**
     * TODO: Model Documentation for IndividualDTO.documents
     */
    @Nullable
    protected List<DocumentListDTO> documents;
    /**
     * TODO: Model Documentation for IndividualDTO.closedAt
     */
    @Nullable
    protected LocalDateTime closedAt;
    /**
     * TODO: Model Documentation for IndividualDTO.retainUntil
     */
    @Nullable
    protected LocalDate retainUntil;

    /** Default Constructor with no properties */
    public IndividualDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param firstNameIn String TODO: Model Documentation for IndividualDTO.firstName
     * @param surnameIn String TODO: Model Documentation for IndividualDTO.surname
     * @param identityNoIn String TODO: Model Documentation for IndividualDTO.identityNo
     * @param identityTypeIn IndividualIdentityType TODO: Model Documentation for IndividualDTO.identityType
     * @param kycStatusIn KycComplianceStatus TODO: Model Documentation for IndividualDTO.kycStatus
     * @param sexIn Sex TODO: Model Documentation for IndividualDTO.sex
     * @param nationalityIn String TODO: Model Documentation for IndividualDTO.nationality
     * @param maritalStatusIn MaritalStatus TODO: Model Documentation for IndividualDTO.maritalStatus
     * @param employmentStatusIn EmploymentStatus TODO: Model Documentation for IndividualDTO.employmentStatus
     */
    public IndividualDTO(final String firstNameIn, final String surnameIn, final String identityNoIn, final IndividualIdentityType identityTypeIn, final KycComplianceStatus kycStatusIn, final Sex sexIn, final String nationalityIn, final MaritalStatus maritalStatusIn, final EmploymentStatus employmentStatusIn)
    {
        super();
        this.firstName = firstNameIn;
        this.surname = surnameIn;
        this.identityNo = identityNoIn;
        this.identityType = identityTypeIn;
        this.kycStatus = kycStatusIn;
        this.sex = sexIn;
        this.nationality = nationalityIn;
        this.maritalStatus = maritalStatusIn;
        this.employmentStatus = employmentStatusIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param firstNameIn String
     * @param middleNameIn String
     * @param surnameIn String
     * @param identityNoIn String
     * @param identityTypeIn IndividualIdentityType
     * @param phoneNumbersIn List<PhoneNumber>
     * @param physicalAddressIn String
     * @param postalAddressIn String
     * @param emailAddressIn String
     * @param hasUserIn Boolean
     * @param organisationIn OrganisationListDTO
     * @param branchIn BranchDTO
     * @param rolesIn Set<String>
     * @param latestKycIn KycRecordDTO
     * @param kycStatusIn KycComplianceStatus
     * @param employmentRecordsIn List<EmploymentRecordDTO>
     * @param usernameIn String
     * @param sexIn Sex
     * @param nationalityIn String
     * @param maritalStatusIn MaritalStatus
     * @param employmentStatusIn EmploymentStatus
     * @param pepStatusIn PepStatus
     * @param userCreatedIn Boolean
     * @param userIdIn String
     * @param dateOfBirthIn LocalDate
     * @param documentsIn List<DocumentListDTO>
     * @param closedAtIn LocalDateTime
     * @param retainUntilIn LocalDate
     */
    public IndividualDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final String firstNameIn, final String middleNameIn, final String surnameIn, final String identityNoIn, final IndividualIdentityType identityTypeIn, final List<PhoneNumber> phoneNumbersIn, final String physicalAddressIn, final String postalAddressIn, final String emailAddressIn, final Boolean hasUserIn, final OrganisationListDTO organisationIn, final BranchDTO branchIn, final Set<String> rolesIn, final KycRecordDTO latestKycIn, final KycComplianceStatus kycStatusIn, final List<EmploymentRecordDTO> employmentRecordsIn, final String usernameIn, final Sex sexIn, final String nationalityIn, final MaritalStatus maritalStatusIn, final EmploymentStatus employmentStatusIn, final PepStatus pepStatusIn, final Boolean userCreatedIn, final String userIdIn, final LocalDate dateOfBirthIn, final List<DocumentListDTO> documentsIn, final LocalDateTime closedAtIn, final LocalDate retainUntilIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.firstName = firstNameIn;
        this.middleName = middleNameIn;
        this.surname = surnameIn;
        this.identityNo = identityNoIn;
        this.identityType = identityTypeIn;
        this.phoneNumbers = phoneNumbersIn;
        this.physicalAddress = physicalAddressIn;
        this.postalAddress = postalAddressIn;
        this.emailAddress = emailAddressIn;
        this.hasUser = hasUserIn;
        this.organisation = organisationIn;
        this.branch = branchIn;
        this.roles = rolesIn;
        this.latestKyc = latestKycIn;
        this.kycStatus = kycStatusIn;
        this.employmentRecords = employmentRecordsIn;
        this.username = usernameIn;
        this.sex = sexIn;
        this.nationality = nationalityIn;
        this.maritalStatus = maritalStatusIn;
        this.employmentStatus = employmentStatusIn;
        this.pepStatus = pepStatusIn;
        this.userCreated = userCreatedIn;
        this.userId = userIdIn;
        this.dateOfBirth = dateOfBirthIn;
        this.documents = documentsIn;
        this.closedAt = closedAtIn;
        this.retainUntil = retainUntilIn;
    }

    /**
     * Copies constructor from other IndividualDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public IndividualDTO(final IndividualDTO otherBean)
    {
        super(otherBean);
        this.firstName = otherBean.getFirstName();
        this.middleName = otherBean.getMiddleName();
        this.surname = otherBean.getSurname();
        this.identityNo = otherBean.getIdentityNo();
        this.identityType = otherBean.getIdentityType();
        this.phoneNumbers = otherBean.getPhoneNumbers();
        this.physicalAddress = otherBean.getPhysicalAddress();
        this.postalAddress = otherBean.getPostalAddress();
        this.emailAddress = otherBean.getEmailAddress();
        this.hasUser = otherBean.getHasUser();
        this.organisation = otherBean.getOrganisation();
        this.branch = otherBean.getBranch();
        this.roles = otherBean.getRoles();
        this.latestKyc = otherBean.getLatestKyc();
        this.kycStatus = otherBean.getKycStatus();
        this.employmentRecords = otherBean.getEmploymentRecords();
        this.username = otherBean.getUsername();
        this.sex = otherBean.getSex();
        this.nationality = otherBean.getNationality();
        this.maritalStatus = otherBean.getMaritalStatus();
        this.employmentStatus = otherBean.getEmploymentStatus();
        this.pepStatus = otherBean.getPepStatus();
        this.userCreated = otherBean.getUserCreated();
        this.userId = otherBean.getUserId();
        this.dateOfBirth = otherBean.getDateOfBirth();
        this.documents = otherBean.getDocuments();
        this.closedAt = otherBean.getClosedAt();
        this.retainUntil = otherBean.getRetainUntil();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final IndividualDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setFirstName(otherBean.getFirstName());
            this.setMiddleName(otherBean.getMiddleName());
            this.setSurname(otherBean.getSurname());
            this.setIdentityNo(otherBean.getIdentityNo());
            this.setIdentityType(otherBean.getIdentityType());
            this.setPhoneNumbers(otherBean.getPhoneNumbers());
            this.setPhysicalAddress(otherBean.getPhysicalAddress());
            this.setPostalAddress(otherBean.getPostalAddress());
            this.setEmailAddress(otherBean.getEmailAddress());
            this.setHasUser(otherBean.getHasUser());
            this.setOrganisation(otherBean.getOrganisation());
            this.setBranch(otherBean.getBranch());
            this.setRoles(otherBean.getRoles());
            this.setLatestKyc(otherBean.getLatestKyc());
            this.setKycStatus(otherBean.getKycStatus());
            this.setEmploymentRecords(otherBean.getEmploymentRecords());
            this.setUsername(otherBean.getUsername());
            this.setSex(otherBean.getSex());
            this.setNationality(otherBean.getNationality());
            this.setMaritalStatus(otherBean.getMaritalStatus());
            this.setEmploymentStatus(otherBean.getEmploymentStatus());
            this.setPepStatus(otherBean.getPepStatus());
            this.setUserCreated(otherBean.getUserCreated());
            this.setUserId(otherBean.getUserId());
            this.setDateOfBirth(otherBean.getDateOfBirth());
            this.setDocuments(otherBean.getDocuments());
            this.setClosedAt(otherBean.getClosedAt());
            this.setRetainUntil(otherBean.getRetainUntil());
        }
    }

    /**
     * TODO: Model Documentation for IndividualDTO.firstName
     * Get the firstName Attribute
     * @return firstName String
     */
    public String getFirstName()
    {
        return this.firstName;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.firstName
     * @param value String
     */
    public void setFirstName(final String value)
    {
        this.firstName = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.middleName
     * Get the middleName Attribute
     * @return middleName String
     */
    public String getMiddleName()
    {
        return this.middleName;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.middleName
     * @param value String
     */
    public void setMiddleName(final String value)
    {
        this.middleName = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.surname
     * Get the surname Attribute
     * @return surname String
     */
    public String getSurname()
    {
        return this.surname;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.surname
     * @param value String
     */
    public void setSurname(final String value)
    {
        this.surname = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.identityNo
     * Get the identityNo Attribute
     * @return identityNo String
     */
    public String getIdentityNo()
    {
        return this.identityNo;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.identityNo
     * @param value String
     */
    public void setIdentityNo(final String value)
    {
        this.identityNo = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.identityType
     * Get the identityType Attribute
     * @return identityType IndividualIdentityType
     */
    public IndividualIdentityType getIdentityType()
    {
        return this.identityType;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.identityType
     * @param value IndividualIdentityType
     */
    public void setIdentityType(final IndividualIdentityType value)
    {
        this.identityType = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.phoneNumbers
     * Get the phoneNumbers Attribute
     * @return phoneNumbers List<PhoneNumber>
     */
    public List<PhoneNumber> getPhoneNumbers()
    {
        if (this.phoneNumbers == null)
        {
            this.phoneNumbers = new ArrayList<PhoneNumber>();
        }
        return this.phoneNumbers;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.phoneNumbers
     * @param value List<PhoneNumber>
     */
    public void setPhoneNumbers(final List<PhoneNumber> value)
    {
        this.phoneNumbers = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.physicalAddress
     * Get the physicalAddress Attribute
     * @return physicalAddress String
     */
    public String getPhysicalAddress()
    {
        return this.physicalAddress;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.physicalAddress
     * @param value String
     */
    public void setPhysicalAddress(final String value)
    {
        this.physicalAddress = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.postalAddress
     * Get the postalAddress Attribute
     * @return postalAddress String
     */
    public String getPostalAddress()
    {
        return this.postalAddress;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.postalAddress
     * @param value String
     */
    public void setPostalAddress(final String value)
    {
        this.postalAddress = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.emailAddress
     * Get the emailAddress Attribute
     * @return emailAddress String
     */
    public String getEmailAddress()
    {
        return this.emailAddress;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.emailAddress
     * @param value String
     */
    public void setEmailAddress(final String value)
    {
        this.emailAddress = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.hasUser
     * Get the hasUser Attribute
     * @return hasUser Boolean
     */
    public Boolean getHasUser()
    {
        return this.hasUser;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.hasUser
     * @param value Boolean
     */
    public void setHasUser(final Boolean value)
    {
        this.hasUser = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.organisation
     * Get the organisation Attribute
     * @return organisation OrganisationListDTO
     */
    public OrganisationListDTO getOrganisation()
    {
        return this.organisation;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.organisation
     * @param value OrganisationListDTO
     */
    public void setOrganisation(final OrganisationListDTO value)
    {
        this.organisation = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.branch
     * Get the branch Attribute
     * @return branch BranchDTO
     */
    public BranchDTO getBranch()
    {
        return this.branch;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.branch
     * @param value BranchDTO
     */
    public void setBranch(final BranchDTO value)
    {
        this.branch = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.roles
     * Get the roles Attribute
     * @return roles Set<String>
     */
    public Set<String> getRoles()
    {
        if (this.roles == null)
        {
            this.roles = new HashSet<String>();
        }
        return this.roles;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.roles
     * @param value Set<String>
     */
    public void setRoles(final Set<String> value)
    {
        this.roles = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.latestKyc
     * Get the latestKyc Attribute
     * @return latestKyc KycRecordDTO
     */
    public KycRecordDTO getLatestKyc()
    {
        return this.latestKyc;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.latestKyc
     * @param value KycRecordDTO
     */
    public void setLatestKyc(final KycRecordDTO value)
    {
        this.latestKyc = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.kycStatus
     * Get the kycStatus Attribute
     * @return kycStatus KycComplianceStatus
     */
    public KycComplianceStatus getKycStatus()
    {
        return this.kycStatus;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.kycStatus
     * @param value KycComplianceStatus
     */
    public void setKycStatus(final KycComplianceStatus value)
    {
        this.kycStatus = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.employmentRecords
     * Get the employmentRecords Attribute
     * @return employmentRecords List<EmploymentRecordDTO>
     */
    public List<EmploymentRecordDTO> getEmploymentRecords()
    {
        if (this.employmentRecords == null)
        {
            this.employmentRecords = new ArrayList<EmploymentRecordDTO>();
        }
        return this.employmentRecords;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.employmentRecords
     * @param value List<EmploymentRecordDTO>
     */
    public void setEmploymentRecords(final List<EmploymentRecordDTO> value)
    {
        this.employmentRecords = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.username
     * Get the username Attribute
     * @return username String
     */
    public String getUsername()
    {
        return this.username;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.username
     * @param value String
     */
    public void setUsername(final String value)
    {
        this.username = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.sex
     * Get the sex Attribute
     * @return sex Sex
     */
    public Sex getSex()
    {
        return this.sex;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.sex
     * @param value Sex
     */
    public void setSex(final Sex value)
    {
        this.sex = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.nationality
     * Get the nationality Attribute
     * @return nationality String
     */
    public String getNationality()
    {
        return this.nationality;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.nationality
     * @param value String
     */
    public void setNationality(final String value)
    {
        this.nationality = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.maritalStatus
     * Get the maritalStatus Attribute
     * @return maritalStatus MaritalStatus
     */
    public MaritalStatus getMaritalStatus()
    {
        return this.maritalStatus;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.maritalStatus
     * @param value MaritalStatus
     */
    public void setMaritalStatus(final MaritalStatus value)
    {
        this.maritalStatus = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.employmentStatus
     * Get the employmentStatus Attribute
     * @return employmentStatus EmploymentStatus
     */
    public EmploymentStatus getEmploymentStatus()
    {
        return this.employmentStatus;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.employmentStatus
     * @param value EmploymentStatus
     */
    public void setEmploymentStatus(final EmploymentStatus value)
    {
        this.employmentStatus = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.pepStatus
     * Get the pepStatus Attribute
     * @return pepStatus PepStatus
     */
    public PepStatus getPepStatus()
    {
        return this.pepStatus;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.pepStatus
     * @param value PepStatus
     */
    public void setPepStatus(final PepStatus value)
    {
        this.pepStatus = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.userCreated
     * Get the userCreated Attribute
     * @return userCreated Boolean
     */
    public Boolean getUserCreated()
    {
        return this.userCreated;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.userCreated
     * @param value Boolean
     */
    public void setUserCreated(final Boolean value)
    {
        this.userCreated = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.userId
     * Get the userId Attribute
     * @return userId String
     */
    public String getUserId()
    {
        return this.userId;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.userId
     * @param value String
     */
    public void setUserId(final String value)
    {
        this.userId = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.dateOfBirth
     * Get the dateOfBirth Attribute
     * @return dateOfBirth LocalDate
     */
    public LocalDate getDateOfBirth()
    {
        return this.dateOfBirth;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.dateOfBirth
     * @param value LocalDate
     */
    public void setDateOfBirth(final LocalDate value)
    {
        this.dateOfBirth = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.documents
     * Get the documents Attribute
     * @return documents List<DocumentListDTO>
     */
    public List<DocumentListDTO> getDocuments()
    {
        if (this.documents == null)
        {
            this.documents = new ArrayList<DocumentListDTO>();
        }
        return this.documents;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.documents
     * @param value List<DocumentListDTO>
     */
    public void setDocuments(final List<DocumentListDTO> value)
    {
        this.documents = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.closedAt
     * Get the closedAt Attribute
     * @return closedAt LocalDateTime
     */
    public LocalDateTime getClosedAt()
    {
        return this.closedAt;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.closedAt
     * @param value LocalDateTime
     */
    public void setClosedAt(final LocalDateTime value)
    {
        this.closedAt = value;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.retainUntil
     * Get the retainUntil Attribute
     * @return retainUntil LocalDate
     */
    public LocalDate getRetainUntil()
    {
        return this.retainUntil;
    }

    /**
     * TODO: Model Documentation for IndividualDTO.retainUntil
     * @param value LocalDate
     */
    public void setRetainUntil(final LocalDate value)
    {
        this.retainUntil = value;
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
        IndividualDTO rhs = (IndividualDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getFirstName(), rhs.getFirstName())
            .append(this.getMiddleName(), rhs.getMiddleName())
            .append(this.getSurname(), rhs.getSurname())
            .append(this.getIdentityNo(), rhs.getIdentityNo())
            .append(this.getIdentityType(), rhs.getIdentityType())
            .append(this.getPhoneNumbers(), rhs.getPhoneNumbers())
            .append(this.getPhysicalAddress(), rhs.getPhysicalAddress())
            .append(this.getPostalAddress(), rhs.getPostalAddress())
            .append(this.getEmailAddress(), rhs.getEmailAddress())
            .append(this.getHasUser(), rhs.getHasUser())
            .append(this.getOrganisation(), rhs.getOrganisation())
            .append(this.getBranch(), rhs.getBranch())
            .append(this.getRoles(), rhs.getRoles())
            .append(this.getLatestKyc(), rhs.getLatestKyc())
            .append(this.getKycStatus(), rhs.getKycStatus())
            .append(this.getEmploymentRecords(), rhs.getEmploymentRecords())
            .append(this.getUsername(), rhs.getUsername())
            .append(this.getSex(), rhs.getSex())
            .append(this.getNationality(), rhs.getNationality())
            .append(this.getMaritalStatus(), rhs.getMaritalStatus())
            .append(this.getEmploymentStatus(), rhs.getEmploymentStatus())
            .append(this.getPepStatus(), rhs.getPepStatus())
            .append(this.getUserCreated(), rhs.getUserCreated())
            .append(this.getUserId(), rhs.getUserId())
            .append(this.getDateOfBirth(), rhs.getDateOfBirth())
            .append(this.getDocuments(), rhs.getDocuments())
            .append(this.getClosedAt(), rhs.getClosedAt())
            .append(this.getRetainUntil(), rhs.getRetainUntil())
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
        if (!(object instanceof IndividualDTO))
        {
            return -1;
        }
        IndividualDTO myClass = (IndividualDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getFirstName(), myClass.getFirstName())
            .append(this.getMiddleName(), myClass.getMiddleName())
            .append(this.getSurname(), myClass.getSurname())
            .append(this.getIdentityNo(), myClass.getIdentityNo())
            .append(this.getIdentityType(), myClass.getIdentityType())
            .append(this.getPhoneNumbers(), myClass.getPhoneNumbers())
            .append(this.getPhysicalAddress(), myClass.getPhysicalAddress())
            .append(this.getPostalAddress(), myClass.getPostalAddress())
            .append(this.getEmailAddress(), myClass.getEmailAddress())
            .append(this.getHasUser(), myClass.getHasUser())
            .append(this.getOrganisation(), myClass.getOrganisation())
            .append(this.getBranch(), myClass.getBranch())
            .append(this.getRoles(), myClass.getRoles())
            .append(this.getLatestKyc(), myClass.getLatestKyc())
            .append(this.getKycStatus(), myClass.getKycStatus())
            .append(this.getEmploymentRecords(), myClass.getEmploymentRecords())
            .append(this.getUsername(), myClass.getUsername())
            .append(this.getSex(), myClass.getSex())
            .append(this.getNationality(), myClass.getNationality())
            .append(this.getMaritalStatus(), myClass.getMaritalStatus())
            .append(this.getEmploymentStatus(), myClass.getEmploymentStatus())
            .append(this.getPepStatus(), myClass.getPepStatus())
            .append(this.getUserCreated(), myClass.getUserCreated())
            .append(this.getUserId(), myClass.getUserId())
            .append(this.getDateOfBirth(), myClass.getDateOfBirth())
            .append(this.getDocuments(), myClass.getDocuments())
            .append(this.getClosedAt(), myClass.getClosedAt())
            .append(this.getRetainUntil(), myClass.getRetainUntil())
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
            .append(this.getFirstName())
            .append(this.getMiddleName())
            .append(this.getSurname())
            .append(this.getIdentityNo())
            .append(this.getIdentityType())
            .append(this.getPhoneNumbers())
            .append(this.getPhysicalAddress())
            .append(this.getPostalAddress())
            .append(this.getEmailAddress())
            .append(this.getHasUser())
            .append(this.getOrganisation())
            .append(this.getBranch())
            .append(this.getRoles())
            .append(this.getLatestKyc())
            .append(this.getKycStatus())
            .append(this.getEmploymentRecords())
            .append(this.getUsername())
            .append(this.getSex())
            .append(this.getNationality())
            .append(this.getMaritalStatus())
            .append(this.getEmploymentStatus())
            .append(this.getPepStatus())
            .append(this.getUserCreated())
            .append(this.getUserId())
            .append(this.getDateOfBirth())
            .append(this.getDocuments())
            .append(this.getClosedAt())
            .append(this.getRetainUntil())
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
        builder.append("\t\"firstName\" : \"" + this.getFirstName() + "\"");
        builder.append("\t,\"middleName\" : \"" + this.getMiddleName() + "\"");
        builder.append("\t,\"surname\" : \"" + this.getSurname() + "\"");
        builder.append("\t,\"identityNo\" : \"" + this.getIdentityNo() + "\"");
        builder.append("\t,\"identityType\" : \"" + this.getIdentityType() + "\"");
        builder.append("\t,\"phoneNumbers\" : \"" + this.getPhoneNumbers() + "\"");
        builder.append("\t,\"physicalAddress\" : \"" + this.getPhysicalAddress() + "\"");
        builder.append("\t,\"postalAddress\" : \"" + this.getPostalAddress() + "\"");
        builder.append("\t,\"emailAddress\" : \"" + this.getEmailAddress() + "\"");
        builder.append("\t,\"hasUser\" : \"" + this.getHasUser() + "\"");
        builder.append("\t,\"organisation\" : \"" + this.getOrganisation() + "\"");
        builder.append("\t,\"branch\" : \"" + this.getBranch() + "\"");
        builder.append("\t,\"roles\" : \"" + this.getRoles() + "\"");
        builder.append("\t,\"latestKyc\" : \"" + this.getLatestKyc() + "\"");
        builder.append("\t,\"kycStatus\" : \"" + this.getKycStatus() + "\"");
        builder.append("\t,\"employmentRecords\" : \"" + this.getEmploymentRecords() + "\"");
        builder.append("\t,\"username\" : \"" + this.getUsername() + "\"");
        builder.append("\t,\"sex\" : \"" + this.getSex() + "\"");
        builder.append("\t,\"nationality\" : \"" + this.getNationality() + "\"");
        builder.append("\t,\"maritalStatus\" : \"" + this.getMaritalStatus() + "\"");
        builder.append("\t,\"employmentStatus\" : \"" + this.getEmploymentStatus() + "\"");
        builder.append("\t,\"pepStatus\" : \"" + this.getPepStatus() + "\"");
        builder.append("\t,\"userCreated\" : \"" + this.getUserCreated() + "\"");
        builder.append("\t,\"userId\" : \"" + this.getUserId() + "\"");
        builder.append("\t,\"dateOfBirth\" : \"" + this.getDateOfBirth() + "\"");
        builder.append("\t,\"documents\" : \"" + this.getDocuments() + "\"");
        builder.append("\t,\"closedAt\" : \"" + this.getClosedAt() + "\"");
        builder.append("\t,\"retainUntil\" : \"" + this.getRetainUntil() + "\"");
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
        final IndividualDTO that = (IndividualDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getFirstName(), that.getFirstName())
            && equal(this.getMiddleName(), that.getMiddleName())
            && equal(this.getSurname(), that.getSurname())
            && equal(this.getIdentityNo(), that.getIdentityNo())
            && equal(this.getIdentityType(), that.getIdentityType())
            && equal(this.getPhoneNumbers(), that.getPhoneNumbers())
            && equal(this.getPhysicalAddress(), that.getPhysicalAddress())
            && equal(this.getPostalAddress(), that.getPostalAddress())
            && equal(this.getEmailAddress(), that.getEmailAddress())
            && equal(this.getHasUser(), that.getHasUser())
            && equal(this.getOrganisation(), that.getOrganisation())
            && equal(this.getBranch(), that.getBranch())
            && equal(this.getRoles(), that.getRoles())
            && equal(this.getLatestKyc(), that.getLatestKyc())
            && equal(this.getKycStatus(), that.getKycStatus())
            && equal(this.getEmploymentRecords(), that.getEmploymentRecords())
            && equal(this.getUsername(), that.getUsername())
            && equal(this.getSex(), that.getSex())
            && equal(this.getNationality(), that.getNationality())
            && equal(this.getMaritalStatus(), that.getMaritalStatus())
            && equal(this.getEmploymentStatus(), that.getEmploymentStatus())
            && equal(this.getPepStatus(), that.getPepStatus())
            && equal(this.getUserCreated(), that.getUserCreated())
            && equal(this.getUserId(), that.getUserId())
            && equal(this.getDateOfBirth(), that.getDateOfBirth())
            && equal(this.getDocuments(), that.getDocuments())
            && equal(this.getClosedAt(), that.getClosedAt())
            && equal(this.getRetainUntil(), that.getRetainUntil())
        ;
    }

}