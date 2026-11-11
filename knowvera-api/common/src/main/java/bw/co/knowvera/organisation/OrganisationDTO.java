package bw.co.knowvera.organisation;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.GeneralStatus;
import bw.co.knowvera.PhoneNumber;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.DocumentListDTO;
import bw.co.knowvera.document.type.DocumentTypeDTO;
import bw.co.knowvera.kyc.KycComplianceStatus;
import bw.co.knowvera.organisation.branch.BranchDTO;
import bw.co.knowvera.settings.kyc.KycFieldGroupDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for OrganisationDTO
 */
public class OrganisationDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 8173395410478634137L;

    // Class attributes
    /**
     * TODO: Model Documentation for OrganisationDTO.code
     */
    protected String code;
    /**
     * TODO: Model Documentation for OrganisationDTO.name
     */
    protected String name;
    /**
     * TODO: Model Documentation for OrganisationDTO.description
     */
    @Nullable
    protected String description;
    /**
     * TODO: Model Documentation for OrganisationDTO.documents
     */
    @Nullable
    protected List<DocumentListDTO> documents;
    /**
     * TODO: Model Documentation for OrganisationDTO.registrationNo
     */
    protected String registrationNo;
    /**
     * TODO: Model Documentation for OrganisationDTO.phoneNumbers
     */
    @Nullable
    protected List<PhoneNumber> phoneNumbers;
    /**
     * TODO: Model Documentation for OrganisationDTO.physicalAddress
     */
    @Nullable
    protected String physicalAddress;
    /**
     * TODO: Model Documentation for OrganisationDTO.postalAddress
     */
    @Nullable
    protected String postalAddress;
    /**
     * TODO: Model Documentation for OrganisationDTO.status
     */
    protected GeneralStatus status;
    /**
     * TODO: Model Documentation for OrganisationDTO.contactEmailAddress
     */
    @Nullable
    protected String contactEmailAddress;
    /**
     * TODO: Model Documentation for
     * OrganisationDTO.organisationKycDocuments
     */
    @Nullable
    protected List<DocumentTypeDTO> organisationKycDocuments;
    /**
     * TODO: Model Documentation for OrganisationDTO.isClient
     */
    @Nullable
    protected Boolean isClient = false;
    /**
     * TODO: Model Documentation for OrganisationDTO.clientRequestsFiles
     */
    @Nullable
    protected List<DocumentDTO> clientRequestsFiles;
    /**
     * TODO: Model Documentation for OrganisationDTO.domains
     */
    protected List<OrganisationDomain> domains;
    /**
     * TODO: Model Documentation for OrganisationDTO.kycStatus
     */
    protected KycComplianceStatus kycStatus;
    /**
     * TODO: Model Documentation for
     * OrganisationDTO.countryOfRegistration
     */
    protected String countryOfRegistration;
    /**
     * TODO: Model Documentation for OrganisationDTO.keycloakId
     */
    @Nullable
    protected String keycloakId;
    /**
     * TODO: Model Documentation for OrganisationDTO.registrationDate
     */
    @Nullable
    protected LocalDate registrationDate;
    /**
     * TODO: Model Documentation for OrganisationDTO.branches
     */
    @Nullable
    protected List<BranchDTO> branches;
    /**
     * TODO: Model Documentation for
     * OrganisationDTO.individualKycDocuments
     */
    @Nullable
    protected List<DocumentTypeDTO> individualKycDocuments;
    /**
     * TODO: Model Documentation for
     * OrganisationDTO.individualFieldGroups
     */
    @Nullable
    protected List<KycFieldGroupDTO> individualFieldGroups;
    /**
     * TODO: Model Documentation for
     * OrganisationDTO.organisationFieldGroups
     */
    @Nullable
    protected List<KycFieldGroupDTO> organisationFieldGroups;
    /**
     * TODO: Model Documentation for OrganisationDTO.tradingName
     */
    @Nullable
    protected String tradingName;
    /**
     * TODO: Model Documentation for OrganisationDTO.natureOfBusiness
     */
    @Nullable
    protected String natureOfBusiness;
    /**
     * TODO: Model Documentation for OrganisationDTO.taxNumber
     */
    @Nullable
    protected String taxNumber;
    /**
     * TODO: Model Documentation for OrganisationDTO.vatNumber
     */
    @Nullable
    protected String vatNumber;
    /**
     * TODO: Model Documentation for OrganisationDTO.legalForm
     */
    @Nullable
    protected OrganisationLegalForm legalForm;
    /**
     * TODO: Model Documentation for OrganisationDTO.closedAt
     */
    @Nullable
    protected LocalDateTime closedAt;
    /**
     * TODO: Model Documentation for OrganisationDTO.retainUntil
     */
    @Nullable
    protected LocalDate retainUntil;

    /** Default Constructor with no properties */
    public OrganisationDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param codeIn String TODO: Model Documentation for OrganisationDTO.code
     * @param nameIn String TODO: Model Documentation for OrganisationDTO.name
     * @param registrationNoIn String TODO: Model Documentation for OrganisationDTO.registrationNo
     * @param statusIn GeneralStatus TODO: Model Documentation for OrganisationDTO.status
     * @param domainsIn List<OrganisationDomain> TODO: Model Documentation for OrganisationDTO.domains
     * @param kycStatusIn KycComplianceStatus TODO: Model Documentation for OrganisationDTO.kycStatus
     * @param countryOfRegistrationIn String TODO: Model Documentation for OrganisationDTO.countryOfRegistration
     */
    public OrganisationDTO(final String codeIn, final String nameIn, final String registrationNoIn, final GeneralStatus statusIn, final List<OrganisationDomain> domainsIn, final KycComplianceStatus kycStatusIn, final String countryOfRegistrationIn)
    {
        super();
        this.code = codeIn;
        this.name = nameIn;
        this.registrationNo = registrationNoIn;
        this.status = statusIn;
        this.domains = domainsIn;
        this.kycStatus = kycStatusIn;
        this.countryOfRegistration = countryOfRegistrationIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param codeIn String
     * @param nameIn String
     * @param descriptionIn String
     * @param documentsIn List<DocumentListDTO>
     * @param registrationNoIn String
     * @param phoneNumbersIn List<PhoneNumber>
     * @param physicalAddressIn String
     * @param postalAddressIn String
     * @param statusIn GeneralStatus
     * @param contactEmailAddressIn String
     * @param organisationKycDocumentsIn List<DocumentTypeDTO>
     * @param isClientIn Boolean
     * @param clientRequestsFilesIn List<DocumentDTO>
     * @param domainsIn List<OrganisationDomain>
     * @param kycStatusIn KycComplianceStatus
     * @param countryOfRegistrationIn String
     * @param keycloakIdIn String
     * @param registrationDateIn LocalDate
     * @param branchesIn List<BranchDTO>
     * @param individualKycDocumentsIn List<DocumentTypeDTO>
     * @param individualFieldGroupsIn List<KycFieldGroupDTO>
     * @param organisationFieldGroupsIn List<KycFieldGroupDTO>
     * @param tradingNameIn String
     * @param natureOfBusinessIn String
     * @param taxNumberIn String
     * @param vatNumberIn String
     * @param legalFormIn OrganisationLegalForm
     * @param closedAtIn LocalDateTime
     * @param retainUntilIn LocalDate
     */
    public OrganisationDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final String codeIn, final String nameIn, final String descriptionIn, final List<DocumentListDTO> documentsIn, final String registrationNoIn, final List<PhoneNumber> phoneNumbersIn, final String physicalAddressIn, final String postalAddressIn, final GeneralStatus statusIn, final String contactEmailAddressIn, final List<DocumentTypeDTO> organisationKycDocumentsIn, final Boolean isClientIn, final List<DocumentDTO> clientRequestsFilesIn, final List<OrganisationDomain> domainsIn, final KycComplianceStatus kycStatusIn, final String countryOfRegistrationIn, final String keycloakIdIn, final LocalDate registrationDateIn, final List<BranchDTO> branchesIn, final List<DocumentTypeDTO> individualKycDocumentsIn, final List<KycFieldGroupDTO> individualFieldGroupsIn, final List<KycFieldGroupDTO> organisationFieldGroupsIn, final String tradingNameIn, final String natureOfBusinessIn, final String taxNumberIn, final String vatNumberIn, final OrganisationLegalForm legalFormIn, final LocalDateTime closedAtIn, final LocalDate retainUntilIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.code = codeIn;
        this.name = nameIn;
        this.description = descriptionIn;
        this.documents = documentsIn;
        this.registrationNo = registrationNoIn;
        this.phoneNumbers = phoneNumbersIn;
        this.physicalAddress = physicalAddressIn;
        this.postalAddress = postalAddressIn;
        this.status = statusIn;
        this.contactEmailAddress = contactEmailAddressIn;
        this.organisationKycDocuments = organisationKycDocumentsIn;
        this.isClient = isClientIn;
        this.clientRequestsFiles = clientRequestsFilesIn;
        this.domains = domainsIn;
        this.kycStatus = kycStatusIn;
        this.countryOfRegistration = countryOfRegistrationIn;
        this.keycloakId = keycloakIdIn;
        this.registrationDate = registrationDateIn;
        this.branches = branchesIn;
        this.individualKycDocuments = individualKycDocumentsIn;
        this.individualFieldGroups = individualFieldGroupsIn;
        this.organisationFieldGroups = organisationFieldGroupsIn;
        this.tradingName = tradingNameIn;
        this.natureOfBusiness = natureOfBusinessIn;
        this.taxNumber = taxNumberIn;
        this.vatNumber = vatNumberIn;
        this.legalForm = legalFormIn;
        this.closedAt = closedAtIn;
        this.retainUntil = retainUntilIn;
    }

    /**
     * Copies constructor from other OrganisationDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public OrganisationDTO(final OrganisationDTO otherBean)
    {
        super(otherBean);
        this.code = otherBean.getCode();
        this.name = otherBean.getName();
        this.description = otherBean.getDescription();
        this.documents = otherBean.getDocuments();
        this.registrationNo = otherBean.getRegistrationNo();
        this.phoneNumbers = otherBean.getPhoneNumbers();
        this.physicalAddress = otherBean.getPhysicalAddress();
        this.postalAddress = otherBean.getPostalAddress();
        this.status = otherBean.getStatus();
        this.contactEmailAddress = otherBean.getContactEmailAddress();
        this.organisationKycDocuments = otherBean.getOrganisationKycDocuments();
        this.isClient = otherBean.getIsClient();
        this.clientRequestsFiles = otherBean.getClientRequestsFiles();
        this.domains = otherBean.getDomains();
        this.kycStatus = otherBean.getKycStatus();
        this.countryOfRegistration = otherBean.getCountryOfRegistration();
        this.keycloakId = otherBean.getKeycloakId();
        this.registrationDate = otherBean.getRegistrationDate();
        this.branches = otherBean.getBranches();
        this.individualKycDocuments = otherBean.getIndividualKycDocuments();
        this.individualFieldGroups = otherBean.getIndividualFieldGroups();
        this.organisationFieldGroups = otherBean.getOrganisationFieldGroups();
        this.tradingName = otherBean.getTradingName();
        this.natureOfBusiness = otherBean.getNatureOfBusiness();
        this.taxNumber = otherBean.getTaxNumber();
        this.vatNumber = otherBean.getVatNumber();
        this.legalForm = otherBean.getLegalForm();
        this.closedAt = otherBean.getClosedAt();
        this.retainUntil = otherBean.getRetainUntil();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final OrganisationDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setCode(otherBean.getCode());
            this.setName(otherBean.getName());
            this.setDescription(otherBean.getDescription());
            this.setDocuments(otherBean.getDocuments());
            this.setRegistrationNo(otherBean.getRegistrationNo());
            this.setPhoneNumbers(otherBean.getPhoneNumbers());
            this.setPhysicalAddress(otherBean.getPhysicalAddress());
            this.setPostalAddress(otherBean.getPostalAddress());
            this.setStatus(otherBean.getStatus());
            this.setContactEmailAddress(otherBean.getContactEmailAddress());
            this.setOrganisationKycDocuments(otherBean.getOrganisationKycDocuments());
            this.setIsClient(otherBean.getIsClient());
            this.setClientRequestsFiles(otherBean.getClientRequestsFiles());
            this.setDomains(otherBean.getDomains());
            this.setKycStatus(otherBean.getKycStatus());
            this.setCountryOfRegistration(otherBean.getCountryOfRegistration());
            this.setKeycloakId(otherBean.getKeycloakId());
            this.setRegistrationDate(otherBean.getRegistrationDate());
            this.setBranches(otherBean.getBranches());
            this.setIndividualKycDocuments(otherBean.getIndividualKycDocuments());
            this.setIndividualFieldGroups(otherBean.getIndividualFieldGroups());
            this.setOrganisationFieldGroups(otherBean.getOrganisationFieldGroups());
            this.setTradingName(otherBean.getTradingName());
            this.setNatureOfBusiness(otherBean.getNatureOfBusiness());
            this.setTaxNumber(otherBean.getTaxNumber());
            this.setVatNumber(otherBean.getVatNumber());
            this.setLegalForm(otherBean.getLegalForm());
            this.setClosedAt(otherBean.getClosedAt());
            this.setRetainUntil(otherBean.getRetainUntil());
        }
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.code
     * Get the code Attribute
     * @return code String
     */
    public String getCode()
    {
        return this.code;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.code
     * @param value String
     */
    public void setCode(final String value)
    {
        this.code = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.description
     * Get the description Attribute
     * @return description String
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.description
     * @param value String
     */
    public void setDescription(final String value)
    {
        this.description = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.documents
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
     * TODO: Model Documentation for OrganisationDTO.documents
     * @param value List<DocumentListDTO>
     */
    public void setDocuments(final List<DocumentListDTO> value)
    {
        this.documents = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.registrationNo
     * Get the registrationNo Attribute
     * @return registrationNo String
     */
    public String getRegistrationNo()
    {
        return this.registrationNo;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.registrationNo
     * @param value String
     */
    public void setRegistrationNo(final String value)
    {
        this.registrationNo = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.phoneNumbers
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
     * TODO: Model Documentation for OrganisationDTO.phoneNumbers
     * @param value List<PhoneNumber>
     */
    public void setPhoneNumbers(final List<PhoneNumber> value)
    {
        this.phoneNumbers = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.physicalAddress
     * Get the physicalAddress Attribute
     * @return physicalAddress String
     */
    public String getPhysicalAddress()
    {
        return this.physicalAddress;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.physicalAddress
     * @param value String
     */
    public void setPhysicalAddress(final String value)
    {
        this.physicalAddress = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.postalAddress
     * Get the postalAddress Attribute
     * @return postalAddress String
     */
    public String getPostalAddress()
    {
        return this.postalAddress;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.postalAddress
     * @param value String
     */
    public void setPostalAddress(final String value)
    {
        this.postalAddress = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.status
     * Get the status Attribute
     * @return status GeneralStatus
     */
    public GeneralStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.status
     * @param value GeneralStatus
     */
    public void setStatus(final GeneralStatus value)
    {
        this.status = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.contactEmailAddress
     * Get the contactEmailAddress Attribute
     * @return contactEmailAddress String
     */
    public String getContactEmailAddress()
    {
        return this.contactEmailAddress;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.contactEmailAddress
     * @param value String
     */
    public void setContactEmailAddress(final String value)
    {
        this.contactEmailAddress = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDTO.organisationKycDocuments
     * Get the organisationKycDocuments Attribute
     * @return organisationKycDocuments List<DocumentTypeDTO>
     */
    public List<DocumentTypeDTO> getOrganisationKycDocuments()
    {
        if (this.organisationKycDocuments == null)
        {
            this.organisationKycDocuments = new ArrayList<DocumentTypeDTO>();
        }
        return this.organisationKycDocuments;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDTO.organisationKycDocuments
     * @param value List<DocumentTypeDTO>
     */
    public void setOrganisationKycDocuments(final List<DocumentTypeDTO> value)
    {
        this.organisationKycDocuments = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.isClient
     * Get the isClient Attribute
     * @return isClient Boolean
     */
    public Boolean getIsClient()
    {
        return this.isClient;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.isClient
     * @param value Boolean
     */
    public void setIsClient(final Boolean value)
    {
        this.isClient = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.clientRequestsFiles
     * Get the clientRequestsFiles Attribute
     * @return clientRequestsFiles List<DocumentDTO>
     */
    public List<DocumentDTO> getClientRequestsFiles()
    {
        if (this.clientRequestsFiles == null)
        {
            this.clientRequestsFiles = new ArrayList<DocumentDTO>();
        }
        return this.clientRequestsFiles;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.clientRequestsFiles
     * @param value List<DocumentDTO>
     */
    public void setClientRequestsFiles(final List<DocumentDTO> value)
    {
        this.clientRequestsFiles = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.domains
     * Get the domains Attribute
     * @return domains List<OrganisationDomain>
     */
    public List<OrganisationDomain> getDomains()
    {
        if (this.domains == null)
        {
            this.domains = new ArrayList<OrganisationDomain>();
        }
        return this.domains;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.domains
     * @param value List<OrganisationDomain>
     */
    public void setDomains(final List<OrganisationDomain> value)
    {
        this.domains = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.kycStatus
     * Get the kycStatus Attribute
     * @return kycStatus KycComplianceStatus
     */
    public KycComplianceStatus getKycStatus()
    {
        return this.kycStatus;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.kycStatus
     * @param value KycComplianceStatus
     */
    public void setKycStatus(final KycComplianceStatus value)
    {
        this.kycStatus = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDTO.countryOfRegistration
     * Get the countryOfRegistration Attribute
     * @return countryOfRegistration String
     */
    public String getCountryOfRegistration()
    {
        return this.countryOfRegistration;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDTO.countryOfRegistration
     * @param value String
     */
    public void setCountryOfRegistration(final String value)
    {
        this.countryOfRegistration = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.keycloakId
     * Get the keycloakId Attribute
     * @return keycloakId String
     */
    public String getKeycloakId()
    {
        return this.keycloakId;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.keycloakId
     * @param value String
     */
    public void setKeycloakId(final String value)
    {
        this.keycloakId = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.registrationDate
     * Get the registrationDate Attribute
     * @return registrationDate LocalDate
     */
    public LocalDate getRegistrationDate()
    {
        return this.registrationDate;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.registrationDate
     * @param value LocalDate
     */
    public void setRegistrationDate(final LocalDate value)
    {
        this.registrationDate = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.branches
     * Get the branches Attribute
     * @return branches List<BranchDTO>
     */
    public List<BranchDTO> getBranches()
    {
        if (this.branches == null)
        {
            this.branches = new ArrayList<BranchDTO>();
        }
        return this.branches;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.branches
     * @param value List<BranchDTO>
     */
    public void setBranches(final List<BranchDTO> value)
    {
        this.branches = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDTO.individualKycDocuments
     * Get the individualKycDocuments Attribute
     * @return individualKycDocuments List<DocumentTypeDTO>
     */
    public List<DocumentTypeDTO> getIndividualKycDocuments()
    {
        if (this.individualKycDocuments == null)
        {
            this.individualKycDocuments = new ArrayList<DocumentTypeDTO>();
        }
        return this.individualKycDocuments;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDTO.individualKycDocuments
     * @param value List<DocumentTypeDTO>
     */
    public void setIndividualKycDocuments(final List<DocumentTypeDTO> value)
    {
        this.individualKycDocuments = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDTO.individualFieldGroups
     * Get the individualFieldGroups Attribute
     * @return individualFieldGroups List<KycFieldGroupDTO>
     */
    public List<KycFieldGroupDTO> getIndividualFieldGroups()
    {
        if (this.individualFieldGroups == null)
        {
            this.individualFieldGroups = new ArrayList<KycFieldGroupDTO>();
        }
        return this.individualFieldGroups;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDTO.individualFieldGroups
     * @param value List<KycFieldGroupDTO>
     */
    public void setIndividualFieldGroups(final List<KycFieldGroupDTO> value)
    {
        this.individualFieldGroups = value;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDTO.organisationFieldGroups
     * Get the organisationFieldGroups Attribute
     * @return organisationFieldGroups List<KycFieldGroupDTO>
     */
    public List<KycFieldGroupDTO> getOrganisationFieldGroups()
    {
        if (this.organisationFieldGroups == null)
        {
            this.organisationFieldGroups = new ArrayList<KycFieldGroupDTO>();
        }
        return this.organisationFieldGroups;
    }

    /**
     * TODO: Model Documentation for
     * OrganisationDTO.organisationFieldGroups
     * @param value List<KycFieldGroupDTO>
     */
    public void setOrganisationFieldGroups(final List<KycFieldGroupDTO> value)
    {
        this.organisationFieldGroups = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.tradingName
     * Get the tradingName Attribute
     * @return tradingName String
     */
    public String getTradingName()
    {
        return this.tradingName;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.tradingName
     * @param value String
     */
    public void setTradingName(final String value)
    {
        this.tradingName = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.natureOfBusiness
     * Get the natureOfBusiness Attribute
     * @return natureOfBusiness String
     */
    public String getNatureOfBusiness()
    {
        return this.natureOfBusiness;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.natureOfBusiness
     * @param value String
     */
    public void setNatureOfBusiness(final String value)
    {
        this.natureOfBusiness = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.taxNumber
     * Get the taxNumber Attribute
     * @return taxNumber String
     */
    public String getTaxNumber()
    {
        return this.taxNumber;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.taxNumber
     * @param value String
     */
    public void setTaxNumber(final String value)
    {
        this.taxNumber = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.vatNumber
     * Get the vatNumber Attribute
     * @return vatNumber String
     */
    public String getVatNumber()
    {
        return this.vatNumber;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.vatNumber
     * @param value String
     */
    public void setVatNumber(final String value)
    {
        this.vatNumber = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.legalForm
     * Get the legalForm Attribute
     * @return legalForm OrganisationLegalForm
     */
    public OrganisationLegalForm getLegalForm()
    {
        return this.legalForm;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.legalForm
     * @param value OrganisationLegalForm
     */
    public void setLegalForm(final OrganisationLegalForm value)
    {
        this.legalForm = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.closedAt
     * Get the closedAt Attribute
     * @return closedAt LocalDateTime
     */
    public LocalDateTime getClosedAt()
    {
        return this.closedAt;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.closedAt
     * @param value LocalDateTime
     */
    public void setClosedAt(final LocalDateTime value)
    {
        this.closedAt = value;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.retainUntil
     * Get the retainUntil Attribute
     * @return retainUntil LocalDate
     */
    public LocalDate getRetainUntil()
    {
        return this.retainUntil;
    }

    /**
     * TODO: Model Documentation for OrganisationDTO.retainUntil
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
        OrganisationDTO rhs = (OrganisationDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getCode(), rhs.getCode())
            .append(this.getName(), rhs.getName())
            .append(this.getDescription(), rhs.getDescription())
            .append(this.getDocuments(), rhs.getDocuments())
            .append(this.getRegistrationNo(), rhs.getRegistrationNo())
            .append(this.getPhoneNumbers(), rhs.getPhoneNumbers())
            .append(this.getPhysicalAddress(), rhs.getPhysicalAddress())
            .append(this.getPostalAddress(), rhs.getPostalAddress())
            .append(this.getStatus(), rhs.getStatus())
            .append(this.getContactEmailAddress(), rhs.getContactEmailAddress())
            .append(this.getOrganisationKycDocuments(), rhs.getOrganisationKycDocuments())
            .append(this.getIsClient(), rhs.getIsClient())
            .append(this.getClientRequestsFiles(), rhs.getClientRequestsFiles())
            .append(this.getDomains(), rhs.getDomains())
            .append(this.getKycStatus(), rhs.getKycStatus())
            .append(this.getCountryOfRegistration(), rhs.getCountryOfRegistration())
            .append(this.getKeycloakId(), rhs.getKeycloakId())
            .append(this.getRegistrationDate(), rhs.getRegistrationDate())
            .append(this.getBranches(), rhs.getBranches())
            .append(this.getIndividualKycDocuments(), rhs.getIndividualKycDocuments())
            .append(this.getIndividualFieldGroups(), rhs.getIndividualFieldGroups())
            .append(this.getOrganisationFieldGroups(), rhs.getOrganisationFieldGroups())
            .append(this.getTradingName(), rhs.getTradingName())
            .append(this.getNatureOfBusiness(), rhs.getNatureOfBusiness())
            .append(this.getTaxNumber(), rhs.getTaxNumber())
            .append(this.getVatNumber(), rhs.getVatNumber())
            .append(this.getLegalForm(), rhs.getLegalForm())
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
        if (!(object instanceof OrganisationDTO))
        {
            return -1;
        }
        OrganisationDTO myClass = (OrganisationDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getCode(), myClass.getCode())
            .append(this.getName(), myClass.getName())
            .append(this.getDescription(), myClass.getDescription())
            .append(this.getDocuments(), myClass.getDocuments())
            .append(this.getRegistrationNo(), myClass.getRegistrationNo())
            .append(this.getPhoneNumbers(), myClass.getPhoneNumbers())
            .append(this.getPhysicalAddress(), myClass.getPhysicalAddress())
            .append(this.getPostalAddress(), myClass.getPostalAddress())
            .append(this.getStatus(), myClass.getStatus())
            .append(this.getContactEmailAddress(), myClass.getContactEmailAddress())
            .append(this.getOrganisationKycDocuments(), myClass.getOrganisationKycDocuments())
            .append(this.getIsClient(), myClass.getIsClient())
            .append(this.getClientRequestsFiles(), myClass.getClientRequestsFiles())
            .append(this.getDomains(), myClass.getDomains())
            .append(this.getKycStatus(), myClass.getKycStatus())
            .append(this.getCountryOfRegistration(), myClass.getCountryOfRegistration())
            .append(this.getKeycloakId(), myClass.getKeycloakId())
            .append(this.getRegistrationDate(), myClass.getRegistrationDate())
            .append(this.getBranches(), myClass.getBranches())
            .append(this.getIndividualKycDocuments(), myClass.getIndividualKycDocuments())
            .append(this.getIndividualFieldGroups(), myClass.getIndividualFieldGroups())
            .append(this.getOrganisationFieldGroups(), myClass.getOrganisationFieldGroups())
            .append(this.getTradingName(), myClass.getTradingName())
            .append(this.getNatureOfBusiness(), myClass.getNatureOfBusiness())
            .append(this.getTaxNumber(), myClass.getTaxNumber())
            .append(this.getVatNumber(), myClass.getVatNumber())
            .append(this.getLegalForm(), myClass.getLegalForm())
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
            .append(this.getCode())
            .append(this.getName())
            .append(this.getDescription())
            .append(this.getDocuments())
            .append(this.getRegistrationNo())
            .append(this.getPhoneNumbers())
            .append(this.getPhysicalAddress())
            .append(this.getPostalAddress())
            .append(this.getStatus())
            .append(this.getContactEmailAddress())
            .append(this.getOrganisationKycDocuments())
            .append(this.getIsClient())
            .append(this.getClientRequestsFiles())
            .append(this.getDomains())
            .append(this.getKycStatus())
            .append(this.getCountryOfRegistration())
            .append(this.getKeycloakId())
            .append(this.getRegistrationDate())
            .append(this.getBranches())
            .append(this.getIndividualKycDocuments())
            .append(this.getIndividualFieldGroups())
            .append(this.getOrganisationFieldGroups())
            .append(this.getTradingName())
            .append(this.getNatureOfBusiness())
            .append(this.getTaxNumber())
            .append(this.getVatNumber())
            .append(this.getLegalForm())
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
        builder.append("\t\"code\" : \"" + this.getCode() + "\"");
        builder.append("\t,\"name\" : \"" + this.getName() + "\"");
        builder.append("\t,\"description\" : \"" + this.getDescription() + "\"");
        builder.append("\t,\"documents\" : \"" + this.getDocuments() + "\"");
        builder.append("\t,\"registrationNo\" : \"" + this.getRegistrationNo() + "\"");
        builder.append("\t,\"phoneNumbers\" : \"" + this.getPhoneNumbers() + "\"");
        builder.append("\t,\"physicalAddress\" : \"" + this.getPhysicalAddress() + "\"");
        builder.append("\t,\"postalAddress\" : \"" + this.getPostalAddress() + "\"");
        builder.append("\t,\"status\" : \"" + this.getStatus() + "\"");
        builder.append("\t,\"contactEmailAddress\" : \"" + this.getContactEmailAddress() + "\"");
        builder.append("\t,\"organisationKycDocuments\" : \"" + this.getOrganisationKycDocuments() + "\"");
        builder.append("\t,\"isClient\" : \"" + this.getIsClient() + "\"");
        builder.append("\t,\"clientRequestsFiles\" : \"" + this.getClientRequestsFiles() + "\"");
        builder.append("\t,\"domains\" : \"" + this.getDomains() + "\"");
        builder.append("\t,\"kycStatus\" : \"" + this.getKycStatus() + "\"");
        builder.append("\t,\"countryOfRegistration\" : \"" + this.getCountryOfRegistration() + "\"");
        builder.append("\t,\"keycloakId\" : \"" + this.getKeycloakId() + "\"");
        builder.append("\t,\"registrationDate\" : \"" + this.getRegistrationDate() + "\"");
        builder.append("\t,\"branches\" : \"" + this.getBranches() + "\"");
        builder.append("\t,\"individualKycDocuments\" : \"" + this.getIndividualKycDocuments() + "\"");
        builder.append("\t,\"individualFieldGroups\" : \"" + this.getIndividualFieldGroups() + "\"");
        builder.append("\t,\"organisationFieldGroups\" : \"" + this.getOrganisationFieldGroups() + "\"");
        builder.append("\t,\"tradingName\" : \"" + this.getTradingName() + "\"");
        builder.append("\t,\"natureOfBusiness\" : \"" + this.getNatureOfBusiness() + "\"");
        builder.append("\t,\"taxNumber\" : \"" + this.getTaxNumber() + "\"");
        builder.append("\t,\"vatNumber\" : \"" + this.getVatNumber() + "\"");
        builder.append("\t,\"legalForm\" : \"" + this.getLegalForm() + "\"");
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
        final OrganisationDTO that = (OrganisationDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getCode(), that.getCode())
            && equal(this.getName(), that.getName())
            && equal(this.getDescription(), that.getDescription())
            && equal(this.getDocuments(), that.getDocuments())
            && equal(this.getRegistrationNo(), that.getRegistrationNo())
            && equal(this.getPhoneNumbers(), that.getPhoneNumbers())
            && equal(this.getPhysicalAddress(), that.getPhysicalAddress())
            && equal(this.getPostalAddress(), that.getPostalAddress())
            && equal(this.getStatus(), that.getStatus())
            && equal(this.getContactEmailAddress(), that.getContactEmailAddress())
            && equal(this.getOrganisationKycDocuments(), that.getOrganisationKycDocuments())
            && equal(this.getIsClient(), that.getIsClient())
            && equal(this.getClientRequestsFiles(), that.getClientRequestsFiles())
            && equal(this.getDomains(), that.getDomains())
            && equal(this.getKycStatus(), that.getKycStatus())
            && equal(this.getCountryOfRegistration(), that.getCountryOfRegistration())
            && equal(this.getKeycloakId(), that.getKeycloakId())
            && equal(this.getRegistrationDate(), that.getRegistrationDate())
            && equal(this.getBranches(), that.getBranches())
            && equal(this.getIndividualKycDocuments(), that.getIndividualKycDocuments())
            && equal(this.getIndividualFieldGroups(), that.getIndividualFieldGroups())
            && equal(this.getOrganisationFieldGroups(), that.getOrganisationFieldGroups())
            && equal(this.getTradingName(), that.getTradingName())
            && equal(this.getNatureOfBusiness(), that.getNatureOfBusiness())
            && equal(this.getTaxNumber(), that.getTaxNumber())
            && equal(this.getVatNumber(), that.getVatNumber())
            && equal(this.getLegalForm(), that.getLegalForm())
            && equal(this.getClosedAt(), that.getClosedAt())
            && equal(this.getRetainUntil(), that.getRetainUntil())
        ;
    }

}