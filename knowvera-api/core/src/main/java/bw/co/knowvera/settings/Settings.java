// false
// true
package bw.co.knowvera.settings;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.document.type.DocumentType;
import bw.co.knowvera.settings.kyc.KycFieldGroup;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * TODO: Model Documentation for Settings
 */
@Entity
@Table(
    name = "SETTINGS")
// Uncomment to enable caching for Settings
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "Settings.findAll", query = "SELECT s FROM Settings AS s"),
})
public class Settings
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 8397860725558962255L;

    // Generate 16 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for Settings.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for Settings.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "KYC_DURATION", unique = false, nullable = true, insertable = true, updatable = true)
    private Integer kycDuration;

    /**
     * TODO: Model Documentation for Settings.kycDuration
     * @return this.kycDuration Integer
     */
    public Integer getKycDuration()
    {
        return this.kycDuration;
    }

    /**
     * TODO: Model Documentation for Settings.kycDuration
     * @param kycDurationIn Integer
     */
    public void setKycDuration(Integer kycDurationIn)
    {
        this.kycDuration = kycDurationIn;
    }


    @Column(name = "TIME_TO_ACCOUNT_CREATION", unique = false, nullable = true, insertable = true, updatable = true)
    private Integer timeToAccountCreation;

    /**
     * TODO: Model Documentation for Settings.timeToAccountCreation
     * @return this.timeToAccountCreation Integer
     */
    public Integer getTimeToAccountCreation()
    {
        return this.timeToAccountCreation;
    }

    /**
     * TODO: Model Documentation for Settings.timeToAccountCreation
     * @param timeToAccountCreationIn Integer
     */
    public void setTimeToAccountCreation(Integer timeToAccountCreationIn)
    {
        this.timeToAccountCreation = timeToAccountCreationIn;
    }


    @Column(name = "PLATFORM_NAME", unique = false, nullable = true, insertable = true, updatable = true)
    private String platformName;

    /**
     * TODO: Model Documentation for Settings.platformName
     * @return this.platformName String
     */
    public String getPlatformName()
    {
        return this.platformName;
    }

    /**
     * TODO: Model Documentation for Settings.platformName
     * @param platformNameIn String
     */
    public void setPlatformName(String platformNameIn)
    {
        this.platformName = platformNameIn;
    }


    @Column(name = "PLATFORM_URL", unique = false, nullable = true, insertable = true, updatable = true)
    private String platformUrl;

    /**
     * TODO: Model Documentation for Settings.platformUrl
     * @return this.platformUrl String
     */
    public String getPlatformUrl()
    {
        return this.platformUrl;
    }

    /**
     * TODO: Model Documentation for Settings.platformUrl
     * @param platformUrlIn String
     */
    public void setPlatformUrl(String platformUrlIn)
    {
        this.platformUrl = platformUrlIn;
    }


    @Column(name = "SUPPORT_CONTACT", unique = false, nullable = true, insertable = true, updatable = true)
    private String supportContact;

    /**
     * TODO: Model Documentation for Settings.supportContact
     * @return this.supportContact String
     */
    public String getSupportContact()
    {
        return this.supportContact;
    }

    /**
     * TODO: Model Documentation for Settings.supportContact
     * @param supportContactIn String
     */
    public void setSupportContact(String supportContactIn)
    {
        this.supportContact = supportContactIn;
    }


    @Column(name = "KYC_PORTAL_LINK", unique = false, nullable = true, insertable = true, updatable = true)
    private String kycPortalLink;

    /**
     * TODO: Model Documentation for Settings.kycPortalLink
     * @return this.kycPortalLink String
     */
    public String getKycPortalLink()
    {
        return this.kycPortalLink;
    }

    /**
     * TODO: Model Documentation for Settings.kycPortalLink
     * @param kycPortalLinkIn String
     */
    public void setKycPortalLink(String kycPortalLinkIn)
    {
        this.kycPortalLink = kycPortalLinkIn;
    }


    @Column(name = "ORGANISATION_ADMIN_ROLE", unique = false, nullable = true, insertable = true, updatable = true)
    private String organisationAdminRole;

    /**
     * TODO: Model Documentation for Settings.organisationAdminRole
     * @return this.organisationAdminRole String
     */
    public String getOrganisationAdminRole()
    {
        return this.organisationAdminRole;
    }

    /**
     * TODO: Model Documentation for Settings.organisationAdminRole
     * @param organisationAdminRoleIn String
     */
    public void setOrganisationAdminRole(String organisationAdminRoleIn)
    {
        this.organisationAdminRole = organisationAdminRoleIn;
    }


    @Column(name = "NORMAL_USER_ROLE", unique = false, nullable = true, insertable = true, updatable = true)
    private String normalUserRole;

    /**
     * TODO: Model Documentation for Settings.normalUserRole
     * @return this.normalUserRole String
     */
    public String getNormalUserRole()
    {
        return this.normalUserRole;
    }

    /**
     * TODO: Model Documentation for Settings.normalUserRole
     * @param normalUserRoleIn String
     */
    public void setNormalUserRole(String normalUserRoleIn)
    {
        this.normalUserRole = normalUserRoleIn;
    }


    @Column(name = "VAT", unique = false, nullable = true, insertable = true, updatable = true)
    private Double vat;

    /**
     * TODO: Model Documentation for Settings.vat
     * @return this.vat Double
     */
    public Double getVat()
    {
        return this.vat;
    }

    /**
     * TODO: Model Documentation for Settings.vat
     * @param vatIn Double
     */
    public void setVat(Double vatIn)
    {
        this.vat = vatIn;
    }


    @Column(name = "DOCUMENT_DURATION_LIMIT", unique = false, nullable = true, insertable = true, updatable = true)
    private Integer documentDurationLimit;

    /**
     * TODO: Model Documentation for Settings.documentDurationLimit
     * @return this.documentDurationLimit Integer
     */
    public Integer getDocumentDurationLimit()
    {
        return this.documentDurationLimit;
    }

    /**
     * TODO: Model Documentation for Settings.documentDurationLimit
     * @param documentDurationLimitIn Integer
     */
    public void setDocumentDurationLimit(Integer documentDurationLimitIn)
    {
        this.documentDurationLimit = documentDurationLimitIn;
    }


    @Column(name = "DATA_VERIFICATION_THRESHOLD", unique = false, nullable = true, insertable = true, updatable = true)
    private Double dataVerificationThreshold;

    /**
     * TODO: Model Documentation for Settings.dataVerificationThreshold
     * @return this.dataVerificationThreshold Double
     */
    public Double getDataVerificationThreshold()
    {
        return this.dataVerificationThreshold;
    }

    /**
     * TODO: Model Documentation for Settings.dataVerificationThreshold
     * @param dataVerificationThresholdIn Double
     */
    public void setDataVerificationThreshold(Double dataVerificationThresholdIn)
    {
        this.dataVerificationThreshold = dataVerificationThresholdIn;
    }


    @Column(name = "MAX_DATA_VERIFICATION_FAILURE_THRESHOLD", unique = false, nullable = true, insertable = true, updatable = true)
    private Double maxDataVerificationFailureThreshold;

    /**
     * TODO: Model Documentation for
     * Settings.maxDataVerificationFailureThreshold
     * @return this.maxDataVerificationFailureThreshold Double
     */
    public Double getMaxDataVerificationFailureThreshold()
    {
        return this.maxDataVerificationFailureThreshold;
    }

    /**
     * TODO: Model Documentation for
     * Settings.maxDataVerificationFailureThreshold
     * @param maxDataVerificationFailureThresholdIn Double
     */
    public void setMaxDataVerificationFailureThreshold(Double maxDataVerificationFailureThresholdIn)
    {
        this.maxDataVerificationFailureThreshold = maxDataVerificationFailureThresholdIn;
    }


    @Column(name = "MEDIUM_RISK_KYC_DURATION", unique = false, nullable = true, insertable = true, updatable = true)
    private Integer mediumRiskKycDuration;

    /**
     * TODO: Model Documentation for Settings.mediumRiskKycDuration
     * @return this.mediumRiskKycDuration Integer
     */
    public Integer getMediumRiskKycDuration()
    {
        return this.mediumRiskKycDuration;
    }

    /**
     * TODO: Model Documentation for Settings.mediumRiskKycDuration
     * @param mediumRiskKycDurationIn Integer
     */
    public void setMediumRiskKycDuration(Integer mediumRiskKycDurationIn)
    {
        this.mediumRiskKycDuration = mediumRiskKycDurationIn;
    }


    @Column(name = "HIGH_RISK_KYC_DURATION", unique = false, nullable = true, insertable = true, updatable = true)
    private Integer highRiskKycDuration;

    /**
     * TODO: Model Documentation for Settings.highRiskKycDuration
     * @return this.highRiskKycDuration Integer
     */
    public Integer getHighRiskKycDuration()
    {
        return this.highRiskKycDuration;
    }

    /**
     * TODO: Model Documentation for Settings.highRiskKycDuration
     * @param highRiskKycDurationIn Integer
     */
    public void setHighRiskKycDuration(Integer highRiskKycDurationIn)
    {
        this.highRiskKycDuration = highRiskKycDurationIn;
    }


    @Column(name = "HIGH_RISK_COUNTRIES", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> highRiskCountries;

    /**
     * TODO: Model Documentation for Settings.highRiskCountries
     * @return this.highRiskCountries List<String>
     */
    public List<String> getHighRiskCountries()
    {
        return this.highRiskCountries;
    }

    /**
     * TODO: Model Documentation for Settings.highRiskCountries
     * @param highRiskCountriesIn List<String>
     */
    public void setHighRiskCountries(List<String> highRiskCountriesIn)
    {
        this.highRiskCountries = highRiskCountriesIn;
    }

    // Generate 18 associations
    @OneToMany(cascade = {CascadeType.ALL},  fetch = FetchType.EAGER)
    @JoinTable
    (
        name = "ORGANISATION_DOCUMENTS2SETTINGS",
        joinColumns = {@JoinColumn(name = "SETTINGS_FK")},
        inverseJoinColumns = {@JoinColumn(name = "ORGANISATION_DOCUMENTS_FK")}
    )
    private List<DocumentType> organisationDocuments = new ArrayList<DocumentType>();
    
    /**
     * TODO: Model Documentation for DocumentType
     * @return this.organisationDocuments List<DocumentType>
     */

    public List<DocumentType> getOrganisationDocuments()
    {
        return this.organisationDocuments;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param organisationDocumentsIn List<DocumentType>
     */
    public void setOrganisationDocuments(List<DocumentType> organisationDocumentsIn)
    {
        this.organisationDocuments = organisationDocumentsIn;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToAdd DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addOrganisationDocuments(DocumentType elementToAdd)
    {
        return this.organisationDocuments.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToRemove DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeOrganisationDocuments(DocumentType elementToRemove)
    {
        return this.organisationDocuments.remove(elementToRemove);
    }

    @OneToMany( fetch = FetchType.EAGER)
    @JoinTable
    (
        name = "INDIVIDUAL_DOCUMENTS2SETTINGS",
        joinColumns = {@JoinColumn(name = "SETTINGS_FK")},
        inverseJoinColumns = {@JoinColumn(name = "INDIVIDUAL_DOCUMENTS_FK")}
    )
    private List<DocumentType> individualDocuments = new ArrayList<DocumentType>();
    
    /**
     * TODO: Model Documentation for DocumentType
     * @return this.individualDocuments List<DocumentType>
     */

    public List<DocumentType> getIndividualDocuments()
    {
        return this.individualDocuments;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param individualDocumentsIn List<DocumentType>
     */
    public void setIndividualDocuments(List<DocumentType> individualDocumentsIn)
    {
        this.individualDocuments = individualDocumentsIn;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToAdd DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addIndividualDocuments(DocumentType elementToAdd)
    {
        return this.individualDocuments.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToRemove DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeIndividualDocuments(DocumentType elementToRemove)
    {
        return this.individualDocuments.remove(elementToRemove);
    }

    @OneToMany( fetch = FetchType.EAGER)
    @JoinTable
    (
        name = "IND_KYC_DOCUMENTS2SETTINGS",
        joinColumns = {@JoinColumn(name = "SETTINGS_FK")},
        inverseJoinColumns = {@JoinColumn(name = "IND_KYC_DOCUMENTS_FK")}
    )
    private List<DocumentType> indKycDocuments = new ArrayList<DocumentType>();
    
    /**
     * TODO: Model Documentation for DocumentType
     * @return this.indKycDocuments List<DocumentType>
     */

    public List<DocumentType> getIndKycDocuments()
    {
        return this.indKycDocuments;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param indKycDocumentsIn List<DocumentType>
     */
    public void setIndKycDocuments(List<DocumentType> indKycDocumentsIn)
    {
        this.indKycDocuments = indKycDocumentsIn;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToAdd DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addIndKycDocuments(DocumentType elementToAdd)
    {
        return this.indKycDocuments.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToRemove DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeIndKycDocuments(DocumentType elementToRemove)
    {
        return this.indKycDocuments.remove(elementToRemove);
    }

    @OneToMany( fetch = FetchType.EAGER)
    @JoinTable
    (
        name = "ORG_KYC_DOCUMENTS2SETTINGS",
        joinColumns = {@JoinColumn(name = "SETTINGS_FK")},
        inverseJoinColumns = {@JoinColumn(name = "ORG_KYC_DOCUMENTS_FK")}
    )
    private List<DocumentType> orgKycDocuments = new ArrayList<DocumentType>();
    
    /**
     * TODO: Model Documentation for DocumentType
     * @return this.orgKycDocuments List<DocumentType>
     */

    public List<DocumentType> getOrgKycDocuments()
    {
        return this.orgKycDocuments;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param orgKycDocumentsIn List<DocumentType>
     */
    public void setOrgKycDocuments(List<DocumentType> orgKycDocumentsIn)
    {
        this.orgKycDocuments = orgKycDocumentsIn;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToAdd DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addOrgKycDocuments(DocumentType elementToAdd)
    {
        return this.orgKycDocuments.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param elementToRemove DocumentType
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeOrgKycDocuments(DocumentType elementToRemove)
    {
        return this.orgKycDocuments.remove(elementToRemove);
    }

    @OneToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "INVOICE_DOCUMENT_TYPE_FK", nullable = true, unique = true)
    private DocumentType invoiceDocumentType;

    /**
     * TODO: Model Documentation for DocumentType
     * @return this.invoiceDocumentType DocumentType
     */

    public DocumentType getInvoiceDocumentType()
    {
        return this.invoiceDocumentType;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param invoiceDocumentTypeIn DocumentType
     */
    public void setInvoiceDocumentType(DocumentType invoiceDocumentTypeIn)
    {
        this.invoiceDocumentType = invoiceDocumentTypeIn;
    }

    @OneToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "INVOICE_TEMPLATE_TYPE_FK", nullable = true, unique = true)
    private DocumentType invoiceTemplateType;

    /**
     * TODO: Model Documentation for DocumentType
     * @return this.invoiceTemplateType DocumentType
     */

    public DocumentType getInvoiceTemplateType()
    {
        return this.invoiceTemplateType;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param invoiceTemplateTypeIn DocumentType
     */
    public void setInvoiceTemplateType(DocumentType invoiceTemplateTypeIn)
    {
        this.invoiceTemplateType = invoiceTemplateTypeIn;
    }

    @OneToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "QUOTATION_DOCUMENT_TYPE_FK", nullable = true, unique = true)
    private DocumentType quotationDocumentType;

    /**
     * TODO: Model Documentation for DocumentType
     * @return this.quotationDocumentType DocumentType
     */

    public DocumentType getQuotationDocumentType()
    {
        return this.quotationDocumentType;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param quotationDocumentTypeIn DocumentType
     */
    public void setQuotationDocumentType(DocumentType quotationDocumentTypeIn)
    {
        this.quotationDocumentType = quotationDocumentTypeIn;
    }

    @OneToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "QUOTATION_TEMPLATE_TYPE_FK", nullable = true, unique = true)
    private DocumentType quotationTemplateType;

    /**
     * TODO: Model Documentation for DocumentType
     * @return this.quotationTemplateType DocumentType
     */

    public DocumentType getQuotationTemplateType()
    {
        return this.quotationTemplateType;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param quotationTemplateTypeIn DocumentType
     */
    public void setQuotationTemplateType(DocumentType quotationTemplateTypeIn)
    {
        this.quotationTemplateType = quotationTemplateTypeIn;
    }

    @OneToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_REQUEST_FILE_TYPE_FK", nullable = true, unique = true)
    private DocumentType clientRequestFileType;

    /**
     * TODO: Model Documentation for DocumentType
     * @return this.clientRequestFileType DocumentType
     */

    public DocumentType getClientRequestFileType()
    {
        return this.clientRequestFileType;
    }

    /**
     * TODO: Model Documentation for DocumentType
     * @param clientRequestFileTypeIn DocumentType
     */
    public void setClientRequestFileType(DocumentType clientRequestFileTypeIn)
    {
        this.clientRequestFileType = clientRequestFileTypeIn;
    }

    @OneToMany(cascade = {CascadeType.ALL} , mappedBy = "settings",  fetch = FetchType.EAGER)
    @OrderBy("min ASC")
    private List<SalaryRange> salaryRanges = new ArrayList<SalaryRange>();
    
    /**
     * TODO: Model Documentation for SalaryRange
     * @return this.salaryRanges List<SalaryRange>
     */

    public List<SalaryRange> getSalaryRanges()
    {
        return this.salaryRanges;
    }

    /**
     * TODO: Model Documentation for SalaryRange
     * @param salaryRangesIn List<SalaryRange>
     */
    public void setSalaryRanges(List<SalaryRange> salaryRangesIn)
    {
        this.salaryRanges = salaryRangesIn;
    }

    /**
     * TODO: Model Documentation for SalaryRange
     * @param elementToAdd SalaryRange
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addSalaryRanges(SalaryRange elementToAdd)
    {
        return this.salaryRanges.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for SalaryRange
     * @param elementToRemove SalaryRange
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeSalaryRanges(SalaryRange elementToRemove)
    {
        return this.salaryRanges.remove(elementToRemove);
    }

    @OneToOne()
    @JoinColumn(name = "QUOTATION_TEMPLATE_FK", nullable = true, unique = false)
    private Document quotationTemplate;

    /**
     * TODO: Model Documentation for Document
     * @return this.quotationTemplate Document
     */

    public Document getQuotationTemplate()
    {
        return this.quotationTemplate;
    }

    /**
     * TODO: Model Documentation for Document
     * @param quotationTemplateIn Document
     */
    public void setQuotationTemplate(Document quotationTemplateIn)
    {
        this.quotationTemplate = quotationTemplateIn;
    }

    @OneToOne()
    @JoinColumn(name = "INVOICE_TEMPLATE_FK", nullable = true, unique = false)
    private Document invoiceTemplate;

    /**
     * TODO: Model Documentation for Document
     * @return this.invoiceTemplate Document
     */

    public Document getInvoiceTemplate()
    {
        return this.invoiceTemplate;
    }

    /**
     * TODO: Model Documentation for Document
     * @param invoiceTemplateIn Document
     */
    public void setInvoiceTemplate(Document invoiceTemplateIn)
    {
        this.invoiceTemplate = invoiceTemplateIn;
    }

    @OneToMany(cascade = {CascadeType.ALL} , mappedBy = "individualSettings",  fetch = FetchType.EAGER, orphanRemoval = true)
    private List<KycFieldGroup> individualKycFieldGroups = new ArrayList<KycFieldGroup>();
    
    /**
     * TODO: Model Documentation for KycFieldGroup
     * @return this.individualKycFieldGroups List<KycFieldGroup>
     */

    public List<KycFieldGroup> getIndividualKycFieldGroups()
    {
        return this.individualKycFieldGroups;
    }

    /**
     * TODO: Model Documentation for KycFieldGroup
     * @param individualKycFieldGroupsIn List<KycFieldGroup>
     */
    public void setIndividualKycFieldGroups(List<KycFieldGroup> individualKycFieldGroupsIn)
    {
        this.individualKycFieldGroups = individualKycFieldGroupsIn;
    }

    /**
     * TODO: Model Documentation for KycFieldGroup
     * @param elementToAdd KycFieldGroup
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addIndividualKycFieldGroups(KycFieldGroup elementToAdd)
    {
        return this.individualKycFieldGroups.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for KycFieldGroup
     * @param elementToRemove KycFieldGroup
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeIndividualKycFieldGroups(KycFieldGroup elementToRemove)
    {
        return this.individualKycFieldGroups.remove(elementToRemove);
    }

    @OneToMany(cascade = {CascadeType.ALL} , mappedBy = "organisationSettings",  fetch = FetchType.EAGER, orphanRemoval = true)
    private List<KycFieldGroup> organisationKycFieldGroups = new ArrayList<KycFieldGroup>();
    
    /**
     * TODO: Model Documentation for KycFieldGroup
     * @return this.organisationKycFieldGroups List<KycFieldGroup>
     */

    public List<KycFieldGroup> getOrganisationKycFieldGroups()
    {
        return this.organisationKycFieldGroups;
    }

    /**
     * TODO: Model Documentation for KycFieldGroup
     * @param organisationKycFieldGroupsIn List<KycFieldGroup>
     */
    public void setOrganisationKycFieldGroups(List<KycFieldGroup> organisationKycFieldGroupsIn)
    {
        this.organisationKycFieldGroups = organisationKycFieldGroupsIn;
    }

    /**
     * TODO: Model Documentation for KycFieldGroup
     * @param elementToAdd KycFieldGroup
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addOrganisationKycFieldGroups(KycFieldGroup elementToAdd)
    {
        return this.organisationKycFieldGroups.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for KycFieldGroup
     * @param elementToRemove KycFieldGroup
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeOrganisationKycFieldGroups(KycFieldGroup elementToRemove)
    {
        return this.organisationKycFieldGroups.remove(elementToRemove);
    }

    /**
     * Returns <code>true</code> if the argument is an Settings instance and all identifiers for this entity
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
        if (!(object instanceof Settings))
        {
            return false;
        }
        final Settings that = (Settings)object;
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
     * Constructs new instances of {@link Settings}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link Settings}.
         * @return new Settings()
         */
        public static Settings newInstance()
        {
            return new Settings();
        }

        /**
         * Constructs a new instance of {@link Settings}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @return newInstance
         */
        public static Settings newInstance(LocalDateTime createdAt, String createdBy)
        {
            final Settings entity = new Settings();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            return entity;
        }

        /**
         * Constructs a new instance of {@link Settings}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param kycDuration Integer
         * @param timeToAccountCreation Integer
         * @param platformName String
         * @param platformUrl String
         * @param supportContact String
         * @param kycPortalLink String
         * @param organisationAdminRole String
         * @param normalUserRole String
         * @param vat Double
         * @param documentDurationLimit Integer
         * @param dataVerificationThreshold Double
         * @param maxDataVerificationFailureThreshold Double
         * @param mediumRiskKycDuration Integer
         * @param highRiskKycDuration Integer
         * @param highRiskCountries List<String>
         * @param organisationDocuments List<DocumentType>
         * @param individualDocuments List<DocumentType>
         * @param indKycDocuments List<DocumentType>
         * @param orgKycDocuments List<DocumentType>
         * @param invoiceDocumentType DocumentType
         * @param invoiceTemplateType DocumentType
         * @param quotationDocumentType DocumentType
         * @param quotationTemplateType DocumentType
         * @param clientRequestFileType DocumentType
         * @param salaryRanges List<SalaryRange>
         * @param quotationTemplate Document
         * @param invoiceTemplate Document
         * @param individualKycFieldGroups List<KycFieldGroup>
         * @param organisationKycFieldGroups List<KycFieldGroup>
         * @return newInstance Settings
         */
        public static Settings newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, Integer kycDuration, Integer timeToAccountCreation, String platformName, String platformUrl, String supportContact, String kycPortalLink, String organisationAdminRole, String normalUserRole, Double vat, Integer documentDurationLimit, Double dataVerificationThreshold, Double maxDataVerificationFailureThreshold, Integer mediumRiskKycDuration, Integer highRiskKycDuration, List<String> highRiskCountries, List<DocumentType> organisationDocuments, List<DocumentType> individualDocuments, List<DocumentType> indKycDocuments, List<DocumentType> orgKycDocuments, DocumentType invoiceDocumentType, DocumentType invoiceTemplateType, DocumentType quotationDocumentType, DocumentType quotationTemplateType, DocumentType clientRequestFileType, List<SalaryRange> salaryRanges, Document quotationTemplate, Document invoiceTemplate, List<KycFieldGroup> individualKycFieldGroups, List<KycFieldGroup> organisationKycFieldGroups)
        {
            final Settings entity = new Settings();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setKycDuration(kycDuration);
            entity.setTimeToAccountCreation(timeToAccountCreation);
            entity.setPlatformName(platformName);
            entity.setPlatformUrl(platformUrl);
            entity.setSupportContact(supportContact);
            entity.setKycPortalLink(kycPortalLink);
            entity.setOrganisationAdminRole(organisationAdminRole);
            entity.setNormalUserRole(normalUserRole);
            entity.setVat(vat);
            entity.setDocumentDurationLimit(documentDurationLimit);
            entity.setDataVerificationThreshold(dataVerificationThreshold);
            entity.setMaxDataVerificationFailureThreshold(maxDataVerificationFailureThreshold);
            entity.setMediumRiskKycDuration(mediumRiskKycDuration);
            entity.setHighRiskKycDuration(highRiskKycDuration);
            entity.setHighRiskCountries(highRiskCountries);
            entity.setOrganisationDocuments(organisationDocuments);
            entity.setIndividualDocuments(individualDocuments);
            entity.setIndKycDocuments(indKycDocuments);
            entity.setOrgKycDocuments(orgKycDocuments);
            entity.setInvoiceDocumentType(invoiceDocumentType);
            entity.setInvoiceTemplateType(invoiceTemplateType);
            entity.setQuotationDocumentType(quotationDocumentType);
            entity.setQuotationTemplateType(quotationTemplateType);
            entity.setClientRequestFileType(clientRequestFileType);
            entity.setSalaryRanges(salaryRanges);
            entity.setQuotationTemplate(quotationTemplate);
            entity.setInvoiceTemplate(invoiceTemplate);
            entity.setIndividualKycFieldGroups(individualKycFieldGroups);
            entity.setOrganisationKycFieldGroups(organisationKycFieldGroups);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(Settings other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getKycDuration() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getKycDuration().compareTo(other.getKycDuration()));
            }
            if (this.getTimeToAccountCreation() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTimeToAccountCreation().compareTo(other.getTimeToAccountCreation()));
            }
            if (this.getPlatformName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPlatformName().compareTo(other.getPlatformName()));
            }
            if (this.getPlatformUrl() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPlatformUrl().compareTo(other.getPlatformUrl()));
            }
            if (this.getSupportContact() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSupportContact().compareTo(other.getSupportContact()));
            }
            if (this.getKycPortalLink() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getKycPortalLink().compareTo(other.getKycPortalLink()));
            }
            if (this.getOrganisationAdminRole() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getOrganisationAdminRole().compareTo(other.getOrganisationAdminRole()));
            }
            if (this.getNormalUserRole() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getNormalUserRole().compareTo(other.getNormalUserRole()));
            }
            if (this.getVat() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getVat().compareTo(other.getVat()));
            }
            if (this.getDocumentDurationLimit() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDocumentDurationLimit().compareTo(other.getDocumentDurationLimit()));
            }
            if (this.getDataVerificationThreshold() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDataVerificationThreshold().compareTo(other.getDataVerificationThreshold()));
            }
            if (this.getMaxDataVerificationFailureThreshold() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMaxDataVerificationFailureThreshold().compareTo(other.getMaxDataVerificationFailureThreshold()));
            }
            if (this.getMediumRiskKycDuration() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMediumRiskKycDuration().compareTo(other.getMediumRiskKycDuration()));
            }
            if (this.getHighRiskKycDuration() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getHighRiskKycDuration().compareTo(other.getHighRiskKycDuration()));
            }
            if (this.getHighRiskCountries() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getHighRiskCountries().size()-other.getHighRiskCountries().size()));
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
        sb.append("Settings(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" kycDuration=").append(getKycDuration());
        sb.append(" timeToAccountCreation=").append(getTimeToAccountCreation());
        sb.append(" platformName=").append(getPlatformName());
        sb.append(" platformUrl=").append(getPlatformUrl());
        sb.append(" supportContact=").append(getSupportContact());
        sb.append(" kycPortalLink=").append(getKycPortalLink());
        sb.append(" organisationAdminRole=").append(getOrganisationAdminRole());
        sb.append(" normalUserRole=").append(getNormalUserRole());
        sb.append(" vat=").append(getVat());
        sb.append(" documentDurationLimit=").append(getDocumentDurationLimit());
        sb.append(" dataVerificationThreshold=").append(getDataVerificationThreshold());
        sb.append(" maxDataVerificationFailureThreshold=").append(getMaxDataVerificationFailureThreshold());
        sb.append(" mediumRiskKycDuration=").append(getMediumRiskKycDuration());
        sb.append(" highRiskKycDuration=").append(getHighRiskKycDuration());
        sb.append(" highRiskCountries=").append(getHighRiskCountries());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}