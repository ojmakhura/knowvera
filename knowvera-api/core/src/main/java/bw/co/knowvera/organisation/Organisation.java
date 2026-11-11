// false
// true
package bw.co.knowvera.organisation;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.GeneralStatus;
import bw.co.knowvera.PhoneNumber;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.document.type.DocumentType;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.invoice.KycInvoice;
import bw.co.knowvera.kyc.KycComplianceStatus;
import bw.co.knowvera.organisation.branch.Branch;
import bw.co.knowvera.organisation.client.ClientRequest;
import bw.co.knowvera.settings.kyc.GroupField;
import bw.co.knowvera.subscription.KycSubscription;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * TODO: Model Documentation for Organisation
 */
@Entity
@Table(
    name = "ORGANISATION")
// Uncomment to enable caching for Organisation
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "Organisation.findAll", query = "SELECT o FROM Organisation AS o"),
})
public class Organisation
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -2232528535817981110L;

    // Generate 23 attributes

    @Column(name = "CODE", unique = true, nullable = false, insertable = true, updatable = true)
    private String code;

    /**
     * TODO: Model Documentation for Organisation.code
     * @return this.code String
     */
    public String getCode()
    {
        return this.code;
    }

    /**
     * TODO: Model Documentation for Organisation.code
     * @param codeIn String
     */
    public void setCode(String codeIn)
    {
        this.code = codeIn;
    }


    @Column(name = "NAME", unique = true, nullable = false, insertable = true, updatable = true)
    private String name;

    /**
     * TODO: Model Documentation for Organisation.name
     * @return this.name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for Organisation.name
     * @param nameIn String
     */
    public void setName(String nameIn)
    {
        this.name = nameIn;
    }


    @Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true)
    private String description;

    /**
     * TODO: Model Documentation for Organisation.description
     * @return this.description String
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * TODO: Model Documentation for Organisation.description
     * @param descriptionIn String
     */
    public void setDescription(String descriptionIn)
    {
        this.description = descriptionIn;
    }


    @Column(name = "REGISTRATION_NO", unique = true, nullable = false, insertable = true, updatable = true)
    private String registrationNo;

    /**
     * TODO: Model Documentation for Organisation.registrationNo
     * @return this.registrationNo String
     */
    public String getRegistrationNo()
    {
        return this.registrationNo;
    }

    /**
     * TODO: Model Documentation for Organisation.registrationNo
     * @param registrationNoIn String
     */
    public void setRegistrationNo(String registrationNoIn)
    {
        this.registrationNo = registrationNoIn;
    }


    @Column(name = "PHYSICAL_ADDRESS", unique = false, nullable = true, insertable = true, updatable = true)
    private String physicalAddress;

    /**
     * TODO: Model Documentation for Organisation.physicalAddress
     * @return this.physicalAddress String
     */
    public String getPhysicalAddress()
    {
        return this.physicalAddress;
    }

    /**
     * TODO: Model Documentation for Organisation.physicalAddress
     * @param physicalAddressIn String
     */
    public void setPhysicalAddress(String physicalAddressIn)
    {
        this.physicalAddress = physicalAddressIn;
    }


    @Column(name = "POSTAL_ADDRESS", unique = false, nullable = true, insertable = true, updatable = true)
    private String postalAddress;

    /**
     * TODO: Model Documentation for Organisation.postalAddress
     * @return this.postalAddress String
     */
    public String getPostalAddress()
    {
        return this.postalAddress;
    }

    /**
     * TODO: Model Documentation for Organisation.postalAddress
     * @param postalAddressIn String
     */
    public void setPostalAddress(String postalAddressIn)
    {
        this.postalAddress = postalAddressIn;
    }


    @Column(name = "STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private GeneralStatus status;

    /**
     * TODO: Model Documentation for Organisation.status
     * @return this.status GeneralStatus
     */
    public GeneralStatus getStatus()
    {
        return this.status;
    }

    /**
     * TODO: Model Documentation for Organisation.status
     * @param statusIn GeneralStatus
     */
    public void setStatus(GeneralStatus statusIn)
    {
        this.status = statusIn;
    }


    @Column(name = "CONTACT_EMAIL_ADDRESS", unique = false, nullable = true, insertable = true, updatable = true)
    private String contactEmailAddress;

    /**
     * TODO: Model Documentation for Organisation.contactEmailAddress
     * @return this.contactEmailAddress String
     */
    public String getContactEmailAddress()
    {
        return this.contactEmailAddress;
    }

    /**
     * TODO: Model Documentation for Organisation.contactEmailAddress
     * @param contactEmailAddressIn String
     */
    public void setContactEmailAddress(String contactEmailAddressIn)
    {
        this.contactEmailAddress = contactEmailAddressIn;
    }


    @Column(name = "IS_CLIENT", unique = false, nullable = true, insertable = true, updatable = true)
    private Boolean isClient = Boolean.valueOf(false);

    /**
     * TODO: Model Documentation for Organisation.isClient
     * @return this.isClient Boolean
     */
    public Boolean getIsClient()
    {
        return this.isClient;
    }

    /**
     * TODO: Model Documentation for Organisation.isClient
     * @param isClientIn Boolean
     */
    public void setIsClient(Boolean isClientIn)
    {
        this.isClient = isClientIn;
    }


    @Column(name = "KYC_STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private KycComplianceStatus kycStatus;

    /**
     * TODO: Model Documentation for Organisation.kycStatus
     * @return this.kycStatus KycComplianceStatus
     */
    public KycComplianceStatus getKycStatus()
    {
        return this.kycStatus;
    }

    /**
     * TODO: Model Documentation for Organisation.kycStatus
     * @param kycStatusIn KycComplianceStatus
     */
    public void setKycStatus(KycComplianceStatus kycStatusIn)
    {
        this.kycStatus = kycStatusIn;
    }


    @Column(name = "COUNTRY_OF_REGISTRATION", unique = false, nullable = false, insertable = true, updatable = true)
    private String countryOfRegistration;

    /**
     * TODO: Model Documentation for Organisation.countryOfRegistration
     * @return this.countryOfRegistration String
     */
    public String getCountryOfRegistration()
    {
        return this.countryOfRegistration;
    }

    /**
     * TODO: Model Documentation for Organisation.countryOfRegistration
     * @param countryOfRegistrationIn String
     */
    public void setCountryOfRegistration(String countryOfRegistrationIn)
    {
        this.countryOfRegistration = countryOfRegistrationIn;
    }


    @Column(name = "PHONE_NUMBERS", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Collection<PhoneNumber> phoneNumbers;

    /**
     * TODO: Model Documentation for Organisation.phoneNumbers
     * @return this.phoneNumbers Collection<PhoneNumber>
     */
    public Collection<PhoneNumber> getPhoneNumbers()
    {
        return this.phoneNumbers;
    }

    /**
     * TODO: Model Documentation for Organisation.phoneNumbers
     * @param phoneNumbersIn Collection<PhoneNumber>
     */
    public void setPhoneNumbers(Collection<PhoneNumber> phoneNumbersIn)
    {
        this.phoneNumbers = phoneNumbersIn;
    }


    @Column(name = "DOMAINS", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<OrganisationDomain> domains;

    /**
     * TODO: Model Documentation for Organisation.domains
     * @return this.domains List<OrganisationDomain>
     */
    public List<OrganisationDomain> getDomains()
    {
        return this.domains;
    }

    /**
     * TODO: Model Documentation for Organisation.domains
     * @param domainsIn List<OrganisationDomain>
     */
    public void setDomains(List<OrganisationDomain> domainsIn)
    {
        this.domains = domainsIn;
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for Organisation.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for Organisation.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "REGISTRATION_DATE", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDate registrationDate;

    /**
     * TODO: Model Documentation for Organisation.registrationDate
     * @return this.registrationDate LocalDate
     */
    public LocalDate getRegistrationDate()
    {
        return this.registrationDate;
    }

    /**
     * TODO: Model Documentation for Organisation.registrationDate
     * @param registrationDateIn LocalDate
     */
    public void setRegistrationDate(LocalDate registrationDateIn)
    {
        this.registrationDate = registrationDateIn;
    }


    @Column(name = "KEYCLOAK_ID", unique = false, nullable = true, insertable = true, updatable = true)
    private String keycloakId;

    /**
     * TODO: Model Documentation for Organisation.keycloakId
     * @return this.keycloakId String
     */
    public String getKeycloakId()
    {
        return this.keycloakId;
    }

    /**
     * TODO: Model Documentation for Organisation.keycloakId
     * @param keycloakIdIn String
     */
    public void setKeycloakId(String keycloakIdIn)
    {
        this.keycloakId = keycloakIdIn;
    }


    @Column(name = "TRADING_NAME", unique = false, nullable = true, insertable = true, updatable = true)
    private String tradingName;

    /**
     * TODO: Model Documentation for Organisation.tradingName
     * @return this.tradingName String
     */
    public String getTradingName()
    {
        return this.tradingName;
    }

    /**
     * TODO: Model Documentation for Organisation.tradingName
     * @param tradingNameIn String
     */
    public void setTradingName(String tradingNameIn)
    {
        this.tradingName = tradingNameIn;
    }


    @Column(name = "NATURE_OF_BUSINESS", unique = false, nullable = true, insertable = true, updatable = true)
    private String natureOfBusiness;

    /**
     * TODO: Model Documentation for Organisation.natureOfBusiness
     * @return this.natureOfBusiness String
     */
    public String getNatureOfBusiness()
    {
        return this.natureOfBusiness;
    }

    /**
     * TODO: Model Documentation for Organisation.natureOfBusiness
     * @param natureOfBusinessIn String
     */
    public void setNatureOfBusiness(String natureOfBusinessIn)
    {
        this.natureOfBusiness = natureOfBusinessIn;
    }


    @Column(name = "TAX_NUMBER", unique = false, nullable = true, insertable = true, updatable = true)
    private String taxNumber;

    /**
     * TODO: Model Documentation for Organisation.taxNumber
     * @return this.taxNumber String
     */
    public String getTaxNumber()
    {
        return this.taxNumber;
    }

    /**
     * TODO: Model Documentation for Organisation.taxNumber
     * @param taxNumberIn String
     */
    public void setTaxNumber(String taxNumberIn)
    {
        this.taxNumber = taxNumberIn;
    }


    @Column(name = "VAT_NUMBER", unique = false, nullable = true, insertable = true, updatable = true)
    private String vatNumber;

    /**
     * TODO: Model Documentation for Organisation.vatNumber
     * @return this.vatNumber String
     */
    public String getVatNumber()
    {
        return this.vatNumber;
    }

    /**
     * TODO: Model Documentation for Organisation.vatNumber
     * @param vatNumberIn String
     */
    public void setVatNumber(String vatNumberIn)
    {
        this.vatNumber = vatNumberIn;
    }


    @Column(name = "LEGAL_FORM", unique = false, nullable = true, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private OrganisationLegalForm legalForm;

    /**
     * TODO: Model Documentation for Organisation.legalForm
     * @return this.legalForm OrganisationLegalForm
     */
    public OrganisationLegalForm getLegalForm()
    {
        return this.legalForm;
    }

    /**
     * TODO: Model Documentation for Organisation.legalForm
     * @param legalFormIn OrganisationLegalForm
     */
    public void setLegalForm(OrganisationLegalForm legalFormIn)
    {
        this.legalForm = legalFormIn;
    }


    @Column(name = "CLOSED_AT", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDateTime closedAt;

    /**
     * TODO: Model Documentation for Organisation.closedAt
     * @return this.closedAt LocalDateTime
     */
    public LocalDateTime getClosedAt()
    {
        return this.closedAt;
    }

    /**
     * TODO: Model Documentation for Organisation.closedAt
     * @param closedAtIn LocalDateTime
     */
    public void setClosedAt(LocalDateTime closedAtIn)
    {
        this.closedAt = closedAtIn;
    }


    @Column(name = "RETAIN_UNTIL", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDate retainUntil;

    /**
     * TODO: Model Documentation for Organisation.retainUntil
     * @return this.retainUntil LocalDate
     */
    public LocalDate getRetainUntil()
    {
        return this.retainUntil;
    }

    /**
     * TODO: Model Documentation for Organisation.retainUntil
     * @param retainUntilIn LocalDate
     */
    public void setRetainUntil(LocalDate retainUntilIn)
    {
        this.retainUntil = retainUntilIn;
    }

    // Generate 11 associations
    @OneToMany( fetch = FetchType.EAGER)
    @JoinTable
    (
        name = "INDIVIDUAL_KYC_DOCUMENTS2ORGANISATION",
        joinColumns = {@JoinColumn(name = "ORGANISATION_FK")},
        inverseJoinColumns = {@JoinColumn(name = "INDIVIDUAL_KYC_DOCUMENTS_FK")}
    )
    private List<DocumentType> individualKycDocuments = new ArrayList<DocumentType>();
    
    /**
     * TODO: Model Documentation for DocumentType
     * @return this.individualKycDocuments List<DocumentType>
     */

    public List<DocumentType> getIndividualKycDocuments()
    {
        return this.individualKycDocuments;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param individualKycDocumentsIn List<DocumentType>
     */
    public void setIndividualKycDocuments(List<DocumentType> individualKycDocumentsIn)
    {
        this.individualKycDocuments = individualKycDocumentsIn;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToAdd DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addIndividualKycDocuments(DocumentType elementToAdd)
    {
        return this.individualKycDocuments.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToRemove DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeIndividualKycDocuments(DocumentType elementToRemove)
    {
        return this.individualKycDocuments.remove(elementToRemove);
    }

    @OneToMany( mappedBy = "organisation",  fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Branch> branches = new ArrayList<Branch>();
    
    /**
     * TODO: Model Documentation for Branch
     * @return this.branches List<Branch>
     */

    public List<Branch> getBranches()
    {
        return this.branches;
    }

    /**
     * TODO: Model Documentation for Branch
     * @param branchesIn List<Branch>
     */
    public void setBranches(List<Branch> branchesIn)
    {
        this.branches = branchesIn;
    }

    /**
     * TODO: Model Documentation for Branch
     * @param elementToAdd Branch
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addBranches(Branch elementToAdd)
    {
        return this.branches.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for Branch
     * @param elementToRemove Branch
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeBranches(Branch elementToRemove)
    {
        return this.branches.remove(elementToRemove);
    }

    @OneToMany(cascade = {CascadeType.ALL},  fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable
    (
        name = "CLIENT_REQUESTS_FILES2ORGANISATION",
        joinColumns = {@JoinColumn(name = "ORGANISATION_FK")},
        inverseJoinColumns = {@JoinColumn(name = "CLIENT_REQUESTS_FILES_FK")}
    )
    private List<Document> clientRequestsFiles = new ArrayList<Document>();
    
    /**
     * TODO: Model Documentation for Document
     * @return this.clientRequestsFiles List<Document>
     */

    public List<Document> getClientRequestsFiles()
    {
        return this.clientRequestsFiles;
    }

    /**
     * TODO: Model Documentation for Document
     * @param clientRequestsFilesIn List<Document>
     */
    public void setClientRequestsFiles(List<Document> clientRequestsFilesIn)
    {
        this.clientRequestsFiles = clientRequestsFilesIn;
    }

    /**
     * TODO: Model Documentation for Document
     * @param elementToAdd Document
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addClientRequestsFiles(Document elementToAdd)
    {
        return this.clientRequestsFiles.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for Document
     * @param elementToRemove Document
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeClientRequestsFiles(Document elementToRemove)
    {
        return this.clientRequestsFiles.remove(elementToRemove);
    }

    @OneToMany( fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable
    (
        name = "ORGANISATION2ORGANISATION_FIELDS",
        joinColumns = {@JoinColumn(name = "ORGANISATION_FK")},
        inverseJoinColumns = {@JoinColumn(name = "ORGANISATION_FIELDS_FK")}
    )
    private List<GroupField> organisationFields = new ArrayList<GroupField>();
    
    /**
     * TODO: Model Documentation for GroupField
     * @return this.organisationFields List<GroupField>
     */

    public List<GroupField> getOrganisationFields()
    {
        return this.organisationFields;
    }

    /**
     * TODO: Model Documentation for GroupField
     * @param organisationFieldsIn List<GroupField>
     */
    public void setOrganisationFields(List<GroupField> organisationFieldsIn)
    {
        this.organisationFields = organisationFieldsIn;
    }

    /**
     * TODO: Model Documentation for GroupField
     * @param elementToAdd GroupField
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addOrganisationFields(GroupField elementToAdd)
    {
        return this.organisationFields.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for GroupField
     * @param elementToRemove GroupField
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeOrganisationFields(GroupField elementToRemove)
    {
        return this.organisationFields.remove(elementToRemove);
    }

    @OneToMany( fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable
    (
        name = "INDIVIDUAL_FIELDS2ORGANISATION",
        joinColumns = {@JoinColumn(name = "ORGANISATION_FK")},
        inverseJoinColumns = {@JoinColumn(name = "INDIVIDUAL_FIELDS_FK")}
    )
    private List<GroupField> individualFields = new ArrayList<GroupField>();
    
    /**
     * TODO: Model Documentation for GroupField
     * @return this.individualFields List<GroupField>
     */

    public List<GroupField> getIndividualFields()
    {
        return this.individualFields;
    }

    /**
     * TODO: Model Documentation for GroupField
     * @param individualFieldsIn List<GroupField>
     */
    public void setIndividualFields(List<GroupField> individualFieldsIn)
    {
        this.individualFields = individualFieldsIn;
    }

    /**
     * TODO: Model Documentation for GroupField
     * @param elementToAdd GroupField
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addIndividualFields(GroupField elementToAdd)
    {
        return this.individualFields.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for GroupField
     * @param elementToRemove GroupField
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeIndividualFields(GroupField elementToRemove)
    {
        return this.individualFields.remove(elementToRemove);
    }

    @OneToMany( fetch = FetchType.EAGER)
    @JoinTable
    (
        name = "ORGANISATION2ORGANISATION_KYC_DOCUMENTS",
        joinColumns = {@JoinColumn(name = "ORGANISATION_FK")},
        inverseJoinColumns = {@JoinColumn(name = "ORGANISATION_KYC_DOCUMENTS_FK")}
    )
    private List<DocumentType> organisationKycDocuments = new ArrayList<DocumentType>();
    
    /**
     * TODO: Model Documentation for DocumentType
     * @return this.organisationKycDocuments List<DocumentType>
     */

    public List<DocumentType> getOrganisationKycDocuments()
    {
        return this.organisationKycDocuments;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param organisationKycDocumentsIn List<DocumentType>
     */
    public void setOrganisationKycDocuments(List<DocumentType> organisationKycDocumentsIn)
    {
        this.organisationKycDocuments = organisationKycDocumentsIn;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToAdd DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addOrganisationKycDocuments(DocumentType elementToAdd)
    {
        return this.organisationKycDocuments.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToRemove DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeOrganisationKycDocuments(DocumentType elementToRemove)
    {
        return this.organisationKycDocuments.remove(elementToRemove);
    }

    @OneToMany( mappedBy = "organisation")
    private List<Individual> individuals = new ArrayList<Individual>();
    
    /**
     * TODO: Model Documentation for Individual
     * @return this.individuals List<Individual>
     */

    public List<Individual> getIndividuals()
    {
        return this.individuals;
    }

    /**
     * TODO: Model Documentation for Individual
     * @param individualsIn List<Individual>
     */
    public void setIndividuals(List<Individual> individualsIn)
    {
        this.individuals = individualsIn;
    }

    /**
     * TODO: Model Documentation for Individual
     * @param elementToAdd Individual
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addIndividuals(Individual elementToAdd)
    {
        return this.individuals.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for Individual
     * @param elementToRemove Individual
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeIndividuals(Individual elementToRemove)
    {
        return this.individuals.remove(elementToRemove);
    }

    @OneToMany( mappedBy = "organisation")
    private List<ClientRequest> clientRequests = new ArrayList<ClientRequest>();
    
    /**
     * TODO: Model Documentation for ClientRequest
     * @return this.clientRequests List<ClientRequest>
     */

    public List<ClientRequest> getClientRequests()
    {
        return this.clientRequests;
    }

    /**
     * TODO: Model Documentation for ClientRequest
     * @param clientRequestsIn List<ClientRequest>
     */
    public void setClientRequests(List<ClientRequest> clientRequestsIn)
    {
        this.clientRequests = clientRequestsIn;
    }

    /**
     * TODO: Model Documentation for ClientRequest
     * @param elementToAdd ClientRequest
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addClientRequests(ClientRequest elementToAdd)
    {
        return this.clientRequests.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for ClientRequest
     * @param elementToRemove ClientRequest
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeClientRequests(ClientRequest elementToRemove)
    {
        return this.clientRequests.remove(elementToRemove);
    }

    @OneToMany(cascade = {CascadeType.ALL},  fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable
    (
        name = "DOCUMENTS2ORGANISATION",
        joinColumns = {@JoinColumn(name = "ORGANISATION_FK")},
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

    @OneToMany( mappedBy = "organisation")
    private List<KycSubscription> kycSubscriptions = new ArrayList<KycSubscription>();
    
    /**
     * TODO: Model Documentation for KycSubscription
     * @return this.kycSubscriptions List<KycSubscription>
     */

    public List<KycSubscription> getKycSubscriptions()
    {
        return this.kycSubscriptions;
    }

    /**
     * TODO: Model Documentation for KycSubscription
     * @param kycSubscriptionsIn List<KycSubscription>
     */
    public void setKycSubscriptions(List<KycSubscription> kycSubscriptionsIn)
    {
        this.kycSubscriptions = kycSubscriptionsIn;
    }

    /**
     * TODO: Model Documentation for KycSubscription
     * @param elementToAdd KycSubscription
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addKycSubscriptions(KycSubscription elementToAdd)
    {
        return this.kycSubscriptions.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for KycSubscription
     * @param elementToRemove KycSubscription
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeKycSubscriptions(KycSubscription elementToRemove)
    {
        return this.kycSubscriptions.remove(elementToRemove);
    }

    @OneToMany( mappedBy = "organisation")
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
     * Returns <code>true</code> if the argument is an Organisation instance and all identifiers for this entity
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
        if (!(object instanceof Organisation))
        {
            return false;
        }
        final Organisation that = (Organisation)object;
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
     * Constructs new instances of {@link Organisation}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link Organisation}.
         * @return new Organisation()
         */
        public static Organisation newInstance()
        {
            return new Organisation();
        }

        /**
         * Constructs a new instance of {@link Organisation}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param code String
         * @param name String
         * @param registrationNo String
         * @param status GeneralStatus
         * @param kycStatus KycComplianceStatus
         * @param countryOfRegistration String
         * @return newInstance
         */
        public static Organisation newInstance(LocalDateTime createdAt, String createdBy, String code, String name, String registrationNo, GeneralStatus status, KycComplianceStatus kycStatus, String countryOfRegistration)
        {
            final Organisation entity = new Organisation();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setCode(code);
            entity.setName(name);
            entity.setRegistrationNo(registrationNo);
            entity.setStatus(status);
            entity.setKycStatus(kycStatus);
            entity.setCountryOfRegistration(countryOfRegistration);
            return entity;
        }

        /**
         * Constructs a new instance of {@link Organisation}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param code String
         * @param name String
         * @param description String
         * @param registrationNo String
         * @param physicalAddress String
         * @param postalAddress String
         * @param status GeneralStatus
         * @param contactEmailAddress String
         * @param isClient Boolean
         * @param kycStatus KycComplianceStatus
         * @param countryOfRegistration String
         * @param phoneNumbers Collection<PhoneNumber>
         * @param domains List<OrganisationDomain>
         * @param registrationDate LocalDate
         * @param keycloakId String
         * @param tradingName String
         * @param natureOfBusiness String
         * @param taxNumber String
         * @param vatNumber String
         * @param legalForm OrganisationLegalForm
         * @param closedAt LocalDateTime
         * @param retainUntil LocalDate
         * @param individualKycDocuments List<DocumentType>
         * @param branches List<Branch>
         * @param clientRequestsFiles List<Document>
         * @param organisationFields List<GroupField>
         * @param individualFields List<GroupField>
         * @param organisationKycDocuments List<DocumentType>
         * @param individuals List<Individual>
         * @param clientRequests List<ClientRequest>
         * @param documents List<Document>
         * @param kycSubscriptions List<KycSubscription>
         * @param kycInvoices List<KycInvoice>
         * @return newInstance Organisation
         */
        public static Organisation newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, String code, String name, String description, String registrationNo, String physicalAddress, String postalAddress, GeneralStatus status, String contactEmailAddress, Boolean isClient, KycComplianceStatus kycStatus, String countryOfRegistration, Collection<PhoneNumber> phoneNumbers, List<OrganisationDomain> domains, LocalDate registrationDate, String keycloakId, String tradingName, String natureOfBusiness, String taxNumber, String vatNumber, OrganisationLegalForm legalForm, LocalDateTime closedAt, LocalDate retainUntil, List<DocumentType> individualKycDocuments, List<Branch> branches, List<Document> clientRequestsFiles, List<GroupField> organisationFields, List<GroupField> individualFields, List<DocumentType> organisationKycDocuments, List<Individual> individuals, List<ClientRequest> clientRequests, List<Document> documents, List<KycSubscription> kycSubscriptions, List<KycInvoice> kycInvoices)
        {
            final Organisation entity = new Organisation();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setCode(code);
            entity.setName(name);
            entity.setDescription(description);
            entity.setRegistrationNo(registrationNo);
            entity.setPhysicalAddress(physicalAddress);
            entity.setPostalAddress(postalAddress);
            entity.setStatus(status);
            entity.setContactEmailAddress(contactEmailAddress);
            entity.setIsClient(isClient);
            entity.setKycStatus(kycStatus);
            entity.setCountryOfRegistration(countryOfRegistration);
            entity.setPhoneNumbers(phoneNumbers);
            entity.setDomains(domains);
            entity.setRegistrationDate(registrationDate);
            entity.setKeycloakId(keycloakId);
            entity.setTradingName(tradingName);
            entity.setNatureOfBusiness(natureOfBusiness);
            entity.setTaxNumber(taxNumber);
            entity.setVatNumber(vatNumber);
            entity.setLegalForm(legalForm);
            entity.setClosedAt(closedAt);
            entity.setRetainUntil(retainUntil);
            entity.setIndividualKycDocuments(individualKycDocuments);
            entity.setBranches(branches);
            entity.setClientRequestsFiles(clientRequestsFiles);
            entity.setOrganisationFields(organisationFields);
            entity.setIndividualFields(individualFields);
            entity.setOrganisationKycDocuments(organisationKycDocuments);
            entity.setIndividuals(individuals);
            entity.setClientRequests(clientRequests);
            entity.setDocuments(documents);
            entity.setKycSubscriptions(kycSubscriptions);
            entity.setKycInvoices(kycInvoices);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(Organisation other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getCode() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getCode().compareTo(other.getCode()));
            }
            if (this.getName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getName().compareTo(other.getName()));
            }
            if (this.getDescription() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDescription().compareTo(other.getDescription()));
            }
            if (this.getRegistrationNo() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRegistrationNo().compareTo(other.getRegistrationNo()));
            }
            if (this.getPhysicalAddress() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPhysicalAddress().compareTo(other.getPhysicalAddress()));
            }
            if (this.getPostalAddress() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPostalAddress().compareTo(other.getPostalAddress()));
            }
            if (this.getStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getStatus().compareTo(other.getStatus()));
            }
            if (this.getContactEmailAddress() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getContactEmailAddress().compareTo(other.getContactEmailAddress()));
            }
            if (this.getIsClient() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getIsClient().compareTo(other.getIsClient()));
            }
            if (this.getKycStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getKycStatus().compareTo(other.getKycStatus()));
            }
            if (this.getCountryOfRegistration() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getCountryOfRegistration().compareTo(other.getCountryOfRegistration()));
            }
            if (this.getPhoneNumbers() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getPhoneNumbers().size()-other.getPhoneNumbers().size()));
            }
            if (this.getDomains() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getDomains().size()-other.getDomains().size()));
            }
            if (this.getRegistrationDate() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRegistrationDate().compareTo(other.getRegistrationDate()));
            }
            if (this.getKeycloakId() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getKeycloakId().compareTo(other.getKeycloakId()));
            }
            if (this.getTradingName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTradingName().compareTo(other.getTradingName()));
            }
            if (this.getNatureOfBusiness() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getNatureOfBusiness().compareTo(other.getNatureOfBusiness()));
            }
            if (this.getTaxNumber() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTaxNumber().compareTo(other.getTaxNumber()));
            }
            if (this.getVatNumber() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getVatNumber().compareTo(other.getVatNumber()));
            }
            if (this.getLegalForm() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getLegalForm().compareTo(other.getLegalForm()));
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
        sb.append("Organisation(");
        sb.append(super.toString());
        sb.append(" code=").append(getCode());
        sb.append(" name=").append(getName());
        sb.append(" description=").append(getDescription());
        sb.append(" registrationNo=").append(getRegistrationNo());
        sb.append(" physicalAddress=").append(getPhysicalAddress());
        sb.append(" postalAddress=").append(getPostalAddress());
        sb.append(" status=").append(getStatus());
        sb.append(" contactEmailAddress=").append(getContactEmailAddress());
        sb.append(" isClient=").append(getIsClient());
        sb.append(" kycStatus=").append(getKycStatus());
        sb.append(" countryOfRegistration=").append(getCountryOfRegistration());
        sb.append(" phoneNumbers=").append(getPhoneNumbers());
        sb.append(" domains=").append(getDomains());
        sb.append(" id=").append(getId());
        sb.append(" registrationDate=").append(getRegistrationDate());
        sb.append(" keycloakId=").append(getKeycloakId());
        sb.append(" tradingName=").append(getTradingName());
        sb.append(" natureOfBusiness=").append(getNatureOfBusiness());
        sb.append(" taxNumber=").append(getTaxNumber());
        sb.append(" vatNumber=").append(getVatNumber());
        sb.append(" legalForm=").append(getLegalForm());
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