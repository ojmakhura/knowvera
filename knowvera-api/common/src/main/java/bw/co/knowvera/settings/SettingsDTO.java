package bw.co.knowvera.settings;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.type.DocumentTypeDTO;
import bw.co.knowvera.settings.kyc.KycFieldGroupDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for SettingsDTO
 */
public class SettingsDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 2027310546384490686L;

    // Class attributes
    /**
     * TODO: Model Documentation for SettingsDTO.kycDuration
     */
    @Nullable
    protected Integer kycDuration;
    /**
     * TODO: Model Documentation for SettingsDTO.organisationDocuments
     */
    @Nullable
    protected List<DocumentTypeDTO> organisationDocuments;
    /**
     * TODO: Model Documentation for SettingsDTO.individualDocuments
     */
    @Nullable
    protected List<DocumentTypeDTO> individualDocuments;
    /**
     * TODO: Model Documentation for SettingsDTO.indKycDocuments
     */
    @Nullable
    protected List<DocumentTypeDTO> indKycDocuments;
    /**
     * TODO: Model Documentation for SettingsDTO.orgKycDocuments
     */
    @Nullable
    protected List<DocumentTypeDTO> orgKycDocuments;
    /**
     * TODO: Model Documentation for SettingsDTO.invoiceDocumentType
     */
    @Nullable
    protected DocumentTypeDTO invoiceDocumentType;
    /**
     * TODO: Model Documentation for SettingsDTO.invoiceTemplate
     */
    @Nullable
    protected DocumentDTO invoiceTemplate;
    /**
     * TODO: Model Documentation for SettingsDTO.invoiceTemplateType
     */
    @Nullable
    protected DocumentTypeDTO invoiceTemplateType;
    /**
     * TODO: Model Documentation for SettingsDTO.quotationDocumentType
     */
    @Nullable
    protected DocumentTypeDTO quotationDocumentType;
    /**
     * TODO: Model Documentation for SettingsDTO.quotationTemplateType
     */
    @Nullable
    protected DocumentTypeDTO quotationTemplateType;
    /**
     * TODO: Model Documentation for SettingsDTO.quotationTemplate
     */
    @Nullable
    protected DocumentDTO quotationTemplate;
    /**
     * TODO: Model Documentation for SettingsDTO.clientRequestFileType
     */
    @Nullable
    protected DocumentTypeDTO clientRequestFileType;
    /**
     * TODO: Model Documentation for SettingsDTO.salaryRanges
     */
    @Nullable
    protected List<SalaryRangeDTO> salaryRanges;
    /**
     * TODO: Model Documentation for SettingsDTO.platformName
     */
    @Nullable
    protected String platformName;
    /**
     * TODO: Model Documentation for SettingsDTO.platformUrl
     */
    @Nullable
    protected String platformUrl;
    /**
     * TODO: Model Documentation for SettingsDTO.supportContact
     */
    @Nullable
    protected String supportContact;
    /**
     * TODO: Model Documentation for SettingsDTO.kycPortalLink
     */
    @Nullable
    protected String kycPortalLink;
    /**
     * TODO: Model Documentation for SettingsDTO.organisationAdminRole
     */
    @Nullable
    protected String organisationAdminRole;
    /**
     * TODO: Model Documentation for SettingsDTO.normalUserRole
     */
    @Nullable
    protected String normalUserRole;
    /**
     * TODO: Model Documentation for SettingsDTO.timeToAccountCreation
     */
    @Nullable
    protected Integer timeToAccountCreation;
    /**
     * TODO: Model Documentation for SettingsDTO.vat
     */
    @Nullable
    protected Double vat;
    /**
     * TODO: Model Documentation for SettingsDTO.documentDurationLimit
     */
    @Nullable
    protected Integer documentDurationLimit;
    /**
     * TODO: Model Documentation for SettingsDTO.dataVerificationThreshold
     */
    @Nullable
    protected Double dataVerificationThreshold;
    /**
     * TODO: Model Documentation for
     * SettingsDTO.maxDataVerificationFailureThreshold
     */
    @Nullable
    protected Double maxDataVerificationFailureThreshold;
    /**
     * TODO: Model Documentation for SettingsDTO.individualKycFieldGroups
     */
    @Nullable
    protected List<KycFieldGroupDTO> individualKycFieldGroups;
    /**
     * TODO: Model Documentation for SettingsDTO.organisationKycFieldGroups
     */
    @Nullable
    protected List<KycFieldGroupDTO> organisationKycFieldGroups;
    /**
     * TODO: Model Documentation for SettingsDTO.mediumRiskKycDuration
     */
    @Nullable
    protected Integer mediumRiskKycDuration;
    /**
     * TODO: Model Documentation for SettingsDTO.highRiskKycDuration
     */
    @Nullable
    protected Integer highRiskKycDuration;
    /**
     * TODO: Model Documentation for SettingsDTO.highRiskCountries
     */
    @Nullable
    protected List<String> highRiskCountries;

    /**
     * Constructor taking only required properties
     */
    public SettingsDTO()
    {
        super();
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param kycDurationIn Integer
     * @param organisationDocumentsIn List<DocumentTypeDTO>
     * @param individualDocumentsIn List<DocumentTypeDTO>
     * @param indKycDocumentsIn List<DocumentTypeDTO>
     * @param orgKycDocumentsIn List<DocumentTypeDTO>
     * @param invoiceDocumentTypeIn DocumentTypeDTO
     * @param invoiceTemplateIn DocumentDTO
     * @param invoiceTemplateTypeIn DocumentTypeDTO
     * @param quotationDocumentTypeIn DocumentTypeDTO
     * @param quotationTemplateTypeIn DocumentTypeDTO
     * @param quotationTemplateIn DocumentDTO
     * @param clientRequestFileTypeIn DocumentTypeDTO
     * @param salaryRangesIn List<SalaryRangeDTO>
     * @param platformNameIn String
     * @param platformUrlIn String
     * @param supportContactIn String
     * @param kycPortalLinkIn String
     * @param organisationAdminRoleIn String
     * @param normalUserRoleIn String
     * @param timeToAccountCreationIn Integer
     * @param vatIn Double
     * @param documentDurationLimitIn Integer
     * @param dataVerificationThresholdIn Double
     * @param maxDataVerificationFailureThresholdIn Double
     * @param individualKycFieldGroupsIn List<KycFieldGroupDTO>
     * @param organisationKycFieldGroupsIn List<KycFieldGroupDTO>
     * @param mediumRiskKycDurationIn Integer
     * @param highRiskKycDurationIn Integer
     * @param highRiskCountriesIn List<String>
     */
    public SettingsDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final Integer kycDurationIn, final List<DocumentTypeDTO> organisationDocumentsIn, final List<DocumentTypeDTO> individualDocumentsIn, final List<DocumentTypeDTO> indKycDocumentsIn, final List<DocumentTypeDTO> orgKycDocumentsIn, final DocumentTypeDTO invoiceDocumentTypeIn, final DocumentDTO invoiceTemplateIn, final DocumentTypeDTO invoiceTemplateTypeIn, final DocumentTypeDTO quotationDocumentTypeIn, final DocumentTypeDTO quotationTemplateTypeIn, final DocumentDTO quotationTemplateIn, final DocumentTypeDTO clientRequestFileTypeIn, final List<SalaryRangeDTO> salaryRangesIn, final String platformNameIn, final String platformUrlIn, final String supportContactIn, final String kycPortalLinkIn, final String organisationAdminRoleIn, final String normalUserRoleIn, final Integer timeToAccountCreationIn, final Double vatIn, final Integer documentDurationLimitIn, final Double dataVerificationThresholdIn, final Double maxDataVerificationFailureThresholdIn, final List<KycFieldGroupDTO> individualKycFieldGroupsIn, final List<KycFieldGroupDTO> organisationKycFieldGroupsIn, final Integer mediumRiskKycDurationIn, final Integer highRiskKycDurationIn, final List<String> highRiskCountriesIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.kycDuration = kycDurationIn;
        this.organisationDocuments = organisationDocumentsIn;
        this.individualDocuments = individualDocumentsIn;
        this.indKycDocuments = indKycDocumentsIn;
        this.orgKycDocuments = orgKycDocumentsIn;
        this.invoiceDocumentType = invoiceDocumentTypeIn;
        this.invoiceTemplate = invoiceTemplateIn;
        this.invoiceTemplateType = invoiceTemplateTypeIn;
        this.quotationDocumentType = quotationDocumentTypeIn;
        this.quotationTemplateType = quotationTemplateTypeIn;
        this.quotationTemplate = quotationTemplateIn;
        this.clientRequestFileType = clientRequestFileTypeIn;
        this.salaryRanges = salaryRangesIn;
        this.platformName = platformNameIn;
        this.platformUrl = platformUrlIn;
        this.supportContact = supportContactIn;
        this.kycPortalLink = kycPortalLinkIn;
        this.organisationAdminRole = organisationAdminRoleIn;
        this.normalUserRole = normalUserRoleIn;
        this.timeToAccountCreation = timeToAccountCreationIn;
        this.vat = vatIn;
        this.documentDurationLimit = documentDurationLimitIn;
        this.dataVerificationThreshold = dataVerificationThresholdIn;
        this.maxDataVerificationFailureThreshold = maxDataVerificationFailureThresholdIn;
        this.individualKycFieldGroups = individualKycFieldGroupsIn;
        this.organisationKycFieldGroups = organisationKycFieldGroupsIn;
        this.mediumRiskKycDuration = mediumRiskKycDurationIn;
        this.highRiskKycDuration = highRiskKycDurationIn;
        this.highRiskCountries = highRiskCountriesIn;
    }

    /**
     * Copies constructor from other SettingsDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public SettingsDTO(final SettingsDTO otherBean)
    {
        super(otherBean);
        this.kycDuration = otherBean.getKycDuration();
        this.organisationDocuments = otherBean.getOrganisationDocuments();
        this.individualDocuments = otherBean.getIndividualDocuments();
        this.indKycDocuments = otherBean.getIndKycDocuments();
        this.orgKycDocuments = otherBean.getOrgKycDocuments();
        this.invoiceDocumentType = otherBean.getInvoiceDocumentType();
        this.invoiceTemplate = otherBean.getInvoiceTemplate();
        this.invoiceTemplateType = otherBean.getInvoiceTemplateType();
        this.quotationDocumentType = otherBean.getQuotationDocumentType();
        this.quotationTemplateType = otherBean.getQuotationTemplateType();
        this.quotationTemplate = otherBean.getQuotationTemplate();
        this.clientRequestFileType = otherBean.getClientRequestFileType();
        this.salaryRanges = otherBean.getSalaryRanges();
        this.platformName = otherBean.getPlatformName();
        this.platformUrl = otherBean.getPlatformUrl();
        this.supportContact = otherBean.getSupportContact();
        this.kycPortalLink = otherBean.getKycPortalLink();
        this.organisationAdminRole = otherBean.getOrganisationAdminRole();
        this.normalUserRole = otherBean.getNormalUserRole();
        this.timeToAccountCreation = otherBean.getTimeToAccountCreation();
        this.vat = otherBean.getVat();
        this.documentDurationLimit = otherBean.getDocumentDurationLimit();
        this.dataVerificationThreshold = otherBean.getDataVerificationThreshold();
        this.maxDataVerificationFailureThreshold = otherBean.getMaxDataVerificationFailureThreshold();
        this.individualKycFieldGroups = otherBean.getIndividualKycFieldGroups();
        this.organisationKycFieldGroups = otherBean.getOrganisationKycFieldGroups();
        this.mediumRiskKycDuration = otherBean.getMediumRiskKycDuration();
        this.highRiskKycDuration = otherBean.getHighRiskKycDuration();
        this.highRiskCountries = otherBean.getHighRiskCountries();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final SettingsDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setKycDuration(otherBean.getKycDuration());
            this.setOrganisationDocuments(otherBean.getOrganisationDocuments());
            this.setIndividualDocuments(otherBean.getIndividualDocuments());
            this.setIndKycDocuments(otherBean.getIndKycDocuments());
            this.setOrgKycDocuments(otherBean.getOrgKycDocuments());
            this.setInvoiceDocumentType(otherBean.getInvoiceDocumentType());
            this.setInvoiceTemplate(otherBean.getInvoiceTemplate());
            this.setInvoiceTemplateType(otherBean.getInvoiceTemplateType());
            this.setQuotationDocumentType(otherBean.getQuotationDocumentType());
            this.setQuotationTemplateType(otherBean.getQuotationTemplateType());
            this.setQuotationTemplate(otherBean.getQuotationTemplate());
            this.setClientRequestFileType(otherBean.getClientRequestFileType());
            this.setSalaryRanges(otherBean.getSalaryRanges());
            this.setPlatformName(otherBean.getPlatformName());
            this.setPlatformUrl(otherBean.getPlatformUrl());
            this.setSupportContact(otherBean.getSupportContact());
            this.setKycPortalLink(otherBean.getKycPortalLink());
            this.setOrganisationAdminRole(otherBean.getOrganisationAdminRole());
            this.setNormalUserRole(otherBean.getNormalUserRole());
            this.setTimeToAccountCreation(otherBean.getTimeToAccountCreation());
            this.setVat(otherBean.getVat());
            this.setDocumentDurationLimit(otherBean.getDocumentDurationLimit());
            this.setDataVerificationThreshold(otherBean.getDataVerificationThreshold());
            this.setMaxDataVerificationFailureThreshold(otherBean.getMaxDataVerificationFailureThreshold());
            this.setIndividualKycFieldGroups(otherBean.getIndividualKycFieldGroups());
            this.setOrganisationKycFieldGroups(otherBean.getOrganisationKycFieldGroups());
            this.setMediumRiskKycDuration(otherBean.getMediumRiskKycDuration());
            this.setHighRiskKycDuration(otherBean.getHighRiskKycDuration());
            this.setHighRiskCountries(otherBean.getHighRiskCountries());
        }
    }

    /**
     * TODO: Model Documentation for SettingsDTO.kycDuration
     * Get the kycDuration Attribute
     * @return kycDuration Integer
     */
    public Integer getKycDuration()
    {
        return this.kycDuration;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.kycDuration
     * @param value Integer
     */
    public void setKycDuration(final Integer value)
    {
        this.kycDuration = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.organisationDocuments
     * Get the organisationDocuments Attribute
     * @return organisationDocuments List<DocumentTypeDTO>
     */
    public List<DocumentTypeDTO> getOrganisationDocuments()
    {
        if (this.organisationDocuments == null)
        {
            this.organisationDocuments = new ArrayList<DocumentTypeDTO>();
        }
        return this.organisationDocuments;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.organisationDocuments
     * @param value List<DocumentTypeDTO>
     */
    public void setOrganisationDocuments(final List<DocumentTypeDTO> value)
    {
        this.organisationDocuments = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.individualDocuments
     * Get the individualDocuments Attribute
     * @return individualDocuments List<DocumentTypeDTO>
     */
    public List<DocumentTypeDTO> getIndividualDocuments()
    {
        if (this.individualDocuments == null)
        {
            this.individualDocuments = new ArrayList<DocumentTypeDTO>();
        }
        return this.individualDocuments;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.individualDocuments
     * @param value List<DocumentTypeDTO>
     */
    public void setIndividualDocuments(final List<DocumentTypeDTO> value)
    {
        this.individualDocuments = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.indKycDocuments
     * Get the indKycDocuments Attribute
     * @return indKycDocuments List<DocumentTypeDTO>
     */
    public List<DocumentTypeDTO> getIndKycDocuments()
    {
        if (this.indKycDocuments == null)
        {
            this.indKycDocuments = new ArrayList<DocumentTypeDTO>();
        }
        return this.indKycDocuments;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.indKycDocuments
     * @param value List<DocumentTypeDTO>
     */
    public void setIndKycDocuments(final List<DocumentTypeDTO> value)
    {
        this.indKycDocuments = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.orgKycDocuments
     * Get the orgKycDocuments Attribute
     * @return orgKycDocuments List<DocumentTypeDTO>
     */
    public List<DocumentTypeDTO> getOrgKycDocuments()
    {
        if (this.orgKycDocuments == null)
        {
            this.orgKycDocuments = new ArrayList<DocumentTypeDTO>();
        }
        return this.orgKycDocuments;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.orgKycDocuments
     * @param value List<DocumentTypeDTO>
     */
    public void setOrgKycDocuments(final List<DocumentTypeDTO> value)
    {
        this.orgKycDocuments = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.invoiceDocumentType
     * Get the invoiceDocumentType Attribute
     * @return invoiceDocumentType DocumentTypeDTO
     */
    public DocumentTypeDTO getInvoiceDocumentType()
    {
        return this.invoiceDocumentType;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.invoiceDocumentType
     * @param value DocumentTypeDTO
     */
    public void setInvoiceDocumentType(final DocumentTypeDTO value)
    {
        this.invoiceDocumentType = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.invoiceTemplate
     * Get the invoiceTemplate Attribute
     * @return invoiceTemplate DocumentDTO
     */
    public DocumentDTO getInvoiceTemplate()
    {
        return this.invoiceTemplate;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.invoiceTemplate
     * @param value DocumentDTO
     */
    public void setInvoiceTemplate(final DocumentDTO value)
    {
        this.invoiceTemplate = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.invoiceTemplateType
     * Get the invoiceTemplateType Attribute
     * @return invoiceTemplateType DocumentTypeDTO
     */
    public DocumentTypeDTO getInvoiceTemplateType()
    {
        return this.invoiceTemplateType;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.invoiceTemplateType
     * @param value DocumentTypeDTO
     */
    public void setInvoiceTemplateType(final DocumentTypeDTO value)
    {
        this.invoiceTemplateType = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.quotationDocumentType
     * Get the quotationDocumentType Attribute
     * @return quotationDocumentType DocumentTypeDTO
     */
    public DocumentTypeDTO getQuotationDocumentType()
    {
        return this.quotationDocumentType;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.quotationDocumentType
     * @param value DocumentTypeDTO
     */
    public void setQuotationDocumentType(final DocumentTypeDTO value)
    {
        this.quotationDocumentType = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.quotationTemplateType
     * Get the quotationTemplateType Attribute
     * @return quotationTemplateType DocumentTypeDTO
     */
    public DocumentTypeDTO getQuotationTemplateType()
    {
        return this.quotationTemplateType;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.quotationTemplateType
     * @param value DocumentTypeDTO
     */
    public void setQuotationTemplateType(final DocumentTypeDTO value)
    {
        this.quotationTemplateType = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.quotationTemplate
     * Get the quotationTemplate Attribute
     * @return quotationTemplate DocumentDTO
     */
    public DocumentDTO getQuotationTemplate()
    {
        return this.quotationTemplate;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.quotationTemplate
     * @param value DocumentDTO
     */
    public void setQuotationTemplate(final DocumentDTO value)
    {
        this.quotationTemplate = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.clientRequestFileType
     * Get the clientRequestFileType Attribute
     * @return clientRequestFileType DocumentTypeDTO
     */
    public DocumentTypeDTO getClientRequestFileType()
    {
        return this.clientRequestFileType;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.clientRequestFileType
     * @param value DocumentTypeDTO
     */
    public void setClientRequestFileType(final DocumentTypeDTO value)
    {
        this.clientRequestFileType = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.salaryRanges
     * Get the salaryRanges Attribute
     * @return salaryRanges List<SalaryRangeDTO>
     */
    public List<SalaryRangeDTO> getSalaryRanges()
    {
        if (this.salaryRanges == null)
        {
            this.salaryRanges = new ArrayList<SalaryRangeDTO>();
        }
        return this.salaryRanges;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.salaryRanges
     * @param value List<SalaryRangeDTO>
     */
    public void setSalaryRanges(final List<SalaryRangeDTO> value)
    {
        this.salaryRanges = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.platformName
     * Get the platformName Attribute
     * @return platformName String
     */
    public String getPlatformName()
    {
        return this.platformName;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.platformName
     * @param value String
     */
    public void setPlatformName(final String value)
    {
        this.platformName = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.platformUrl
     * Get the platformUrl Attribute
     * @return platformUrl String
     */
    public String getPlatformUrl()
    {
        return this.platformUrl;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.platformUrl
     * @param value String
     */
    public void setPlatformUrl(final String value)
    {
        this.platformUrl = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.supportContact
     * Get the supportContact Attribute
     * @return supportContact String
     */
    public String getSupportContact()
    {
        return this.supportContact;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.supportContact
     * @param value String
     */
    public void setSupportContact(final String value)
    {
        this.supportContact = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.kycPortalLink
     * Get the kycPortalLink Attribute
     * @return kycPortalLink String
     */
    public String getKycPortalLink()
    {
        return this.kycPortalLink;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.kycPortalLink
     * @param value String
     */
    public void setKycPortalLink(final String value)
    {
        this.kycPortalLink = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.organisationAdminRole
     * Get the organisationAdminRole Attribute
     * @return organisationAdminRole String
     */
    public String getOrganisationAdminRole()
    {
        return this.organisationAdminRole;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.organisationAdminRole
     * @param value String
     */
    public void setOrganisationAdminRole(final String value)
    {
        this.organisationAdminRole = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.normalUserRole
     * Get the normalUserRole Attribute
     * @return normalUserRole String
     */
    public String getNormalUserRole()
    {
        return this.normalUserRole;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.normalUserRole
     * @param value String
     */
    public void setNormalUserRole(final String value)
    {
        this.normalUserRole = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.timeToAccountCreation
     * Get the timeToAccountCreation Attribute
     * @return timeToAccountCreation Integer
     */
    public Integer getTimeToAccountCreation()
    {
        return this.timeToAccountCreation;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.timeToAccountCreation
     * @param value Integer
     */
    public void setTimeToAccountCreation(final Integer value)
    {
        this.timeToAccountCreation = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.vat
     * Get the vat Attribute
     * @return vat Double
     */
    public Double getVat()
    {
        return this.vat;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.vat
     * @param value Double
     */
    public void setVat(final Double value)
    {
        this.vat = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.documentDurationLimit
     * Get the documentDurationLimit Attribute
     * @return documentDurationLimit Integer
     */
    public Integer getDocumentDurationLimit()
    {
        return this.documentDurationLimit;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.documentDurationLimit
     * @param value Integer
     */
    public void setDocumentDurationLimit(final Integer value)
    {
        this.documentDurationLimit = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.dataVerificationThreshold
     * Get the dataVerificationThreshold Attribute
     * @return dataVerificationThreshold Double
     */
    public Double getDataVerificationThreshold()
    {
        return this.dataVerificationThreshold;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.dataVerificationThreshold
     * @param value Double
     */
    public void setDataVerificationThreshold(final Double value)
    {
        this.dataVerificationThreshold = value;
    }

    /**
     * TODO: Model Documentation for
     * SettingsDTO.maxDataVerificationFailureThreshold
     * Get the maxDataVerificationFailureThreshold Attribute
     * @return maxDataVerificationFailureThreshold Double
     */
    public Double getMaxDataVerificationFailureThreshold()
    {
        return this.maxDataVerificationFailureThreshold;
    }

    /**
     * TODO: Model Documentation for
     * SettingsDTO.maxDataVerificationFailureThreshold
     * @param value Double
     */
    public void setMaxDataVerificationFailureThreshold(final Double value)
    {
        this.maxDataVerificationFailureThreshold = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.individualKycFieldGroups
     * Get the individualKycFieldGroups Attribute
     * @return individualKycFieldGroups List<KycFieldGroupDTO>
     */
    public List<KycFieldGroupDTO> getIndividualKycFieldGroups()
    {
        if (this.individualKycFieldGroups == null)
        {
            this.individualKycFieldGroups = new ArrayList<KycFieldGroupDTO>();
        }
        return this.individualKycFieldGroups;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.individualKycFieldGroups
     * @param value List<KycFieldGroupDTO>
     */
    public void setIndividualKycFieldGroups(final List<KycFieldGroupDTO> value)
    {
        this.individualKycFieldGroups = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.organisationKycFieldGroups
     * Get the organisationKycFieldGroups Attribute
     * @return organisationKycFieldGroups List<KycFieldGroupDTO>
     */
    public List<KycFieldGroupDTO> getOrganisationKycFieldGroups()
    {
        if (this.organisationKycFieldGroups == null)
        {
            this.organisationKycFieldGroups = new ArrayList<KycFieldGroupDTO>();
        }
        return this.organisationKycFieldGroups;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.organisationKycFieldGroups
     * @param value List<KycFieldGroupDTO>
     */
    public void setOrganisationKycFieldGroups(final List<KycFieldGroupDTO> value)
    {
        this.organisationKycFieldGroups = value;
    }









    /**
     * TODO: Model Documentation for SettingsDTO.mediumRiskKycDuration
     * Get the mediumRiskKycDuration Attribute
     * @return mediumRiskKycDuration Integer
     */
    public Integer getMediumRiskKycDuration()
    {
        return this.mediumRiskKycDuration;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.mediumRiskKycDuration
     * @param value Integer
     */
    public void setMediumRiskKycDuration(final Integer value)
    {
        this.mediumRiskKycDuration = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.highRiskKycDuration
     * Get the highRiskKycDuration Attribute
     * @return highRiskKycDuration Integer
     */
    public Integer getHighRiskKycDuration()
    {
        return this.highRiskKycDuration;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.highRiskKycDuration
     * @param value Integer
     */
    public void setHighRiskKycDuration(final Integer value)
    {
        this.highRiskKycDuration = value;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.highRiskCountries
     * Get the highRiskCountries Attribute
     * @return highRiskCountries List<String>
     */
    public List<String> getHighRiskCountries()
    {
        if (this.highRiskCountries == null)
        {
            this.highRiskCountries = new ArrayList<String>();
        }
        return this.highRiskCountries;
    }

    /**
     * TODO: Model Documentation for SettingsDTO.highRiskCountries
     * @param value List<String>
     */
    public void setHighRiskCountries(final List<String> value)
    {
        this.highRiskCountries = value;
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
        SettingsDTO rhs = (SettingsDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getKycDuration(), rhs.getKycDuration())
            .append(this.getOrganisationDocuments(), rhs.getOrganisationDocuments())
            .append(this.getIndividualDocuments(), rhs.getIndividualDocuments())
            .append(this.getIndKycDocuments(), rhs.getIndKycDocuments())
            .append(this.getOrgKycDocuments(), rhs.getOrgKycDocuments())
            .append(this.getInvoiceDocumentType(), rhs.getInvoiceDocumentType())
            .append(this.getInvoiceTemplate(), rhs.getInvoiceTemplate())
            .append(this.getInvoiceTemplateType(), rhs.getInvoiceTemplateType())
            .append(this.getQuotationDocumentType(), rhs.getQuotationDocumentType())
            .append(this.getQuotationTemplateType(), rhs.getQuotationTemplateType())
            .append(this.getQuotationTemplate(), rhs.getQuotationTemplate())
            .append(this.getClientRequestFileType(), rhs.getClientRequestFileType())
            .append(this.getSalaryRanges(), rhs.getSalaryRanges())
            .append(this.getPlatformName(), rhs.getPlatformName())
            .append(this.getPlatformUrl(), rhs.getPlatformUrl())
            .append(this.getSupportContact(), rhs.getSupportContact())
            .append(this.getKycPortalLink(), rhs.getKycPortalLink())
            .append(this.getOrganisationAdminRole(), rhs.getOrganisationAdminRole())
            .append(this.getNormalUserRole(), rhs.getNormalUserRole())
            .append(this.getTimeToAccountCreation(), rhs.getTimeToAccountCreation())
            .append(this.getVat(), rhs.getVat())
            .append(this.getDocumentDurationLimit(), rhs.getDocumentDurationLimit())
            .append(this.getDataVerificationThreshold(), rhs.getDataVerificationThreshold())
            .append(this.getMaxDataVerificationFailureThreshold(), rhs.getMaxDataVerificationFailureThreshold())
            .append(this.getIndividualKycFieldGroups(), rhs.getIndividualKycFieldGroups())
            .append(this.getOrganisationKycFieldGroups(), rhs.getOrganisationKycFieldGroups())
            .append(this.getMediumRiskKycDuration(), rhs.getMediumRiskKycDuration())
            .append(this.getHighRiskKycDuration(), rhs.getHighRiskKycDuration())
            .append(this.getHighRiskCountries(), rhs.getHighRiskCountries())
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
        if (!(object instanceof SettingsDTO))
        {
            return -1;
        }
        SettingsDTO myClass = (SettingsDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getKycDuration(), myClass.getKycDuration())
            .append(this.getOrganisationDocuments(), myClass.getOrganisationDocuments())
            .append(this.getIndividualDocuments(), myClass.getIndividualDocuments())
            .append(this.getIndKycDocuments(), myClass.getIndKycDocuments())
            .append(this.getOrgKycDocuments(), myClass.getOrgKycDocuments())
            .append(this.getInvoiceDocumentType(), myClass.getInvoiceDocumentType())
            .append(this.getInvoiceTemplate(), myClass.getInvoiceTemplate())
            .append(this.getInvoiceTemplateType(), myClass.getInvoiceTemplateType())
            .append(this.getQuotationDocumentType(), myClass.getQuotationDocumentType())
            .append(this.getQuotationTemplateType(), myClass.getQuotationTemplateType())
            .append(this.getQuotationTemplate(), myClass.getQuotationTemplate())
            .append(this.getClientRequestFileType(), myClass.getClientRequestFileType())
            .append(this.getSalaryRanges(), myClass.getSalaryRanges())
            .append(this.getPlatformName(), myClass.getPlatformName())
            .append(this.getPlatformUrl(), myClass.getPlatformUrl())
            .append(this.getSupportContact(), myClass.getSupportContact())
            .append(this.getKycPortalLink(), myClass.getKycPortalLink())
            .append(this.getOrganisationAdminRole(), myClass.getOrganisationAdminRole())
            .append(this.getNormalUserRole(), myClass.getNormalUserRole())
            .append(this.getTimeToAccountCreation(), myClass.getTimeToAccountCreation())
            .append(this.getVat(), myClass.getVat())
            .append(this.getDocumentDurationLimit(), myClass.getDocumentDurationLimit())
            .append(this.getDataVerificationThreshold(), myClass.getDataVerificationThreshold())
            .append(this.getMaxDataVerificationFailureThreshold(), myClass.getMaxDataVerificationFailureThreshold())
            .append(this.getIndividualKycFieldGroups(), myClass.getIndividualKycFieldGroups())
            .append(this.getOrganisationKycFieldGroups(), myClass.getOrganisationKycFieldGroups())
            .append(this.getMediumRiskKycDuration(), myClass.getMediumRiskKycDuration())
            .append(this.getHighRiskKycDuration(), myClass.getHighRiskKycDuration())
            .append(this.getHighRiskCountries(), myClass.getHighRiskCountries())
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
            .append(this.getKycDuration())
            .append(this.getOrganisationDocuments())
            .append(this.getIndividualDocuments())
            .append(this.getIndKycDocuments())
            .append(this.getOrgKycDocuments())
            .append(this.getInvoiceDocumentType())
            .append(this.getInvoiceTemplate())
            .append(this.getInvoiceTemplateType())
            .append(this.getQuotationDocumentType())
            .append(this.getQuotationTemplateType())
            .append(this.getQuotationTemplate())
            .append(this.getClientRequestFileType())
            .append(this.getSalaryRanges())
            .append(this.getPlatformName())
            .append(this.getPlatformUrl())
            .append(this.getSupportContact())
            .append(this.getKycPortalLink())
            .append(this.getOrganisationAdminRole())
            .append(this.getNormalUserRole())
            .append(this.getTimeToAccountCreation())
            .append(this.getVat())
            .append(this.getDocumentDurationLimit())
            .append(this.getDataVerificationThreshold())
            .append(this.getMaxDataVerificationFailureThreshold())
            .append(this.getIndividualKycFieldGroups())
            .append(this.getOrganisationKycFieldGroups())
            .append(this.getMediumRiskKycDuration())
            .append(this.getHighRiskKycDuration())
            .append(this.getHighRiskCountries())
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
        builder.append("\t\"kycDuration\" : \"" + this.getKycDuration() + "\"");
        builder.append("\t,\"organisationDocuments\" : \"" + this.getOrganisationDocuments() + "\"");
        builder.append("\t,\"individualDocuments\" : \"" + this.getIndividualDocuments() + "\"");
        builder.append("\t,\"indKycDocuments\" : \"" + this.getIndKycDocuments() + "\"");
        builder.append("\t,\"orgKycDocuments\" : \"" + this.getOrgKycDocuments() + "\"");
        builder.append("\t,\"invoiceDocumentType\" : \"" + this.getInvoiceDocumentType() + "\"");
        builder.append("\t,\"invoiceTemplate\" : \"" + this.getInvoiceTemplate() + "\"");
        builder.append("\t,\"invoiceTemplateType\" : \"" + this.getInvoiceTemplateType() + "\"");
        builder.append("\t,\"quotationDocumentType\" : \"" + this.getQuotationDocumentType() + "\"");
        builder.append("\t,\"quotationTemplateType\" : \"" + this.getQuotationTemplateType() + "\"");
        builder.append("\t,\"quotationTemplate\" : \"" + this.getQuotationTemplate() + "\"");
        builder.append("\t,\"clientRequestFileType\" : \"" + this.getClientRequestFileType() + "\"");
        builder.append("\t,\"salaryRanges\" : \"" + this.getSalaryRanges() + "\"");
        builder.append("\t,\"platformName\" : \"" + this.getPlatformName() + "\"");
        builder.append("\t,\"platformUrl\" : \"" + this.getPlatformUrl() + "\"");
        builder.append("\t,\"supportContact\" : \"" + this.getSupportContact() + "\"");
        builder.append("\t,\"kycPortalLink\" : \"" + this.getKycPortalLink() + "\"");
        builder.append("\t,\"organisationAdminRole\" : \"" + this.getOrganisationAdminRole() + "\"");
        builder.append("\t,\"normalUserRole\" : \"" + this.getNormalUserRole() + "\"");
        builder.append("\t,\"timeToAccountCreation\" : \"" + this.getTimeToAccountCreation() + "\"");
        builder.append("\t,\"vat\" : \"" + this.getVat() + "\"");
        builder.append("\t,\"documentDurationLimit\" : \"" + this.getDocumentDurationLimit() + "\"");
        builder.append("\t,\"dataVerificationThreshold\" : \"" + this.getDataVerificationThreshold() + "\"");
        builder.append("\t,\"maxDataVerificationFailureThreshold\" : \"" + this.getMaxDataVerificationFailureThreshold() + "\"");
        builder.append("\t,\"individualKycFieldGroups\" : \"" + this.getIndividualKycFieldGroups() + "\"");
        builder.append("\t,\"organisationKycFieldGroups\" : \"" + this.getOrganisationKycFieldGroups() + "\"");
        builder.append("\t,\"mediumRiskKycDuration\" : \"" + this.getMediumRiskKycDuration() + "\"");
        builder.append("\t,\"highRiskKycDuration\" : \"" + this.getHighRiskKycDuration() + "\"");
        builder.append("\t,\"highRiskCountries\" : \"" + this.getHighRiskCountries() + "\"");
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
        final SettingsDTO that = (SettingsDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getKycDuration(), that.getKycDuration())
            && equal(this.getOrganisationDocuments(), that.getOrganisationDocuments())
            && equal(this.getIndividualDocuments(), that.getIndividualDocuments())
            && equal(this.getIndKycDocuments(), that.getIndKycDocuments())
            && equal(this.getOrgKycDocuments(), that.getOrgKycDocuments())
            && equal(this.getInvoiceDocumentType(), that.getInvoiceDocumentType())
            && equal(this.getInvoiceTemplate(), that.getInvoiceTemplate())
            && equal(this.getInvoiceTemplateType(), that.getInvoiceTemplateType())
            && equal(this.getQuotationDocumentType(), that.getQuotationDocumentType())
            && equal(this.getQuotationTemplateType(), that.getQuotationTemplateType())
            && equal(this.getQuotationTemplate(), that.getQuotationTemplate())
            && equal(this.getClientRequestFileType(), that.getClientRequestFileType())
            && equal(this.getSalaryRanges(), that.getSalaryRanges())
            && equal(this.getPlatformName(), that.getPlatformName())
            && equal(this.getPlatformUrl(), that.getPlatformUrl())
            && equal(this.getSupportContact(), that.getSupportContact())
            && equal(this.getKycPortalLink(), that.getKycPortalLink())
            && equal(this.getOrganisationAdminRole(), that.getOrganisationAdminRole())
            && equal(this.getNormalUserRole(), that.getNormalUserRole())
            && equal(this.getTimeToAccountCreation(), that.getTimeToAccountCreation())
            && equal(this.getVat(), that.getVat())
            && equal(this.getDocumentDurationLimit(), that.getDocumentDurationLimit())
            && equal(this.getDataVerificationThreshold(), that.getDataVerificationThreshold())
            && equal(this.getMaxDataVerificationFailureThreshold(), that.getMaxDataVerificationFailureThreshold())
            && equal(this.getIndividualKycFieldGroups(), that.getIndividualKycFieldGroups())
            && equal(this.getOrganisationKycFieldGroups(), that.getOrganisationKycFieldGroups())
            && equal(this.getMediumRiskKycDuration(), that.getMediumRiskKycDuration())
            && equal(this.getHighRiskKycDuration(), that.getHighRiskKycDuration())
            && equal(this.getHighRiskCountries(), that.getHighRiskCountries())
        ;
    }

}