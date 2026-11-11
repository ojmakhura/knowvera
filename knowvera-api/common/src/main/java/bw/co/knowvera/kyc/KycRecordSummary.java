package bw.co.knowvera.kyc;

import bw.co.knowvera.KeyValue;
import bw.co.knowvera.PhoneNumber;
import bw.co.knowvera.SourceOfFunds;
import bw.co.knowvera.SourceOfWealth;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.individual.IndividualIdentityType;
import bw.co.knowvera.individual.PepStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for KycRecordSummary
 */
public class KycRecordSummary
    implements Serializable, Comparable<KycRecordSummary>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -3140429009054784975L;

    // Class attributes
    /**
     * TODO: Model Documentation for KycRecordSummary.ref
     */
    @Nullable
    protected String ref;
    /**
     * TODO: Model Documentation for KycRecordSummary.identityNo
     */
    @Nullable
    protected String identityNo;
    /**
     * TODO: Model Documentation for KycRecordSummary.name
     */
    @Nullable
    protected String name;
    /**
     * TODO: Model Documentation for KycRecordSummary.identityType
     */
    protected IndividualIdentityType identityType;
    /**
     * TODO: Model Documentation for KycRecordSummary.emailAddress
     */
    @Nullable
    protected String emailAddress;
    /**
     * TODO: Model Documentation for KycRecordSummary.postalAddress
     */
    @Nullable
    protected String postalAddress;
    /**
     * TODO: Model Documentation for KycRecordSummary.physicalAddress
     */
    @Nullable
    protected String physicalAddress;
    /**
     * TODO: Model Documentation for KycRecordSummary.phoneNumbers
     */
    @Nullable
    protected Collection<PhoneNumber> phoneNumbers;
    /**
     * TODO: Model Documentation for KycRecordSummary.kycStatus
     */
    protected KycComplianceStatus kycStatus;
    /**
     * TODO: Model Documentation for KycRecordSummary.recordSummary
     */
    @Nullable
    protected String recordSummary;
    /**
     * TODO: Model Documentation for KycRecordSummary.expiryDate
     */
    @Nullable
    protected LocalDate expiryDate;
    /**
     * TODO: Model Documentation for KycRecordSummary.pepStatus
     */
    protected PepStatus pepStatus;
    /**
     * TODO: Model Documentation for KycRecordSummary.pepDetails
     */
    @Nullable
    protected String pepDetails;
    /**
     * TODO: Model Documentation for KycRecordSummary.sanctionsMatch
     */
    @Nullable
    protected Boolean sanctionsMatch = false;
    /**
     * TODO: Model Documentation for KycRecordSummary.sanctionsDetails
     */
    @Nullable
    protected String sanctionsDetails;
    /**
     * TODO: Model Documentation for KycRecordSummary.documents
     */
    @Nullable
    protected List<KeyValue> documents;
    /**
     * TODO: Model Documentation for KycRecordSummary.target
     */
    protected TargetEntity target;
    /**
     * TODO: Model Documentation for KycRecordSummary.sourceOfFundsDetails
     */
    @Nullable
    protected String sourceOfFundsDetails;
    /**
     * TODO: Model Documentation for KycRecordSummary.sourceOfFunds
     */
    protected Collection<SourceOfFunds> sourceOfFunds;
    /**
     * TODO: Model Documentation for KycRecordSummary.dataVerifications
     */
    @Nullable
    protected Collection<KeyValue> dataVerifications;
    /**
     * TODO: Model Documentation for KycRecordSummary.wealthSources
     */
    @Nullable
    protected List<SourceOfWealth> wealthSources;
    /**
     * TODO: Model Documentation for KycRecordSummary.sourceOfWealthDetails
     */
    @Nullable
    protected String sourceOfWealthDetails;
    /**
     * TODO: Model Documentation for KycRecordSummary.createdBy
     */
    @Nullable
    protected String createdBy;
    /**
     * TODO: Model Documentation for KycRecordSummary.reviewedBy
     */
    @Nullable
    protected String reviewedBy;
    /**
     * TODO: Model Documentation for KycRecordSummary.reviewedAt
     */
    @Nullable
    protected LocalDateTime reviewedAt;
    /**
     * TODO: Model Documentation for KycRecordSummary.relatedParties
     */
    @Nullable
    protected List<RelatedParty> relatedParties;
    /**
     * TODO: Model Documentation for KycRecordSummary.riskLevel
     */
    @Nullable
    protected RiskLevel riskLevel;
    /**
     * TODO: Model Documentation for KycRecordSummary.riskFactors
     */
    @Nullable
    protected List<String> riskFactors;
    /**
     * TODO: Model Documentation for KycRecordSummary.approvedBy
     */
    @Nullable
    protected String approvedBy;
    /**
     * TODO: Model Documentation for KycRecordSummary.approvedAt
     */
    @Nullable
    protected LocalDateTime approvedAt;

    /** Default Constructor with no properties */
    public KycRecordSummary()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param identityTypeIn IndividualIdentityType TODO: Model Documentation for KycRecordSummary.identityType
     * @param kycStatusIn KycComplianceStatus TODO: Model Documentation for KycRecordSummary.kycStatus
     * @param pepStatusIn PepStatus TODO: Model Documentation for KycRecordSummary.pepStatus
     * @param targetIn TargetEntity TODO: Model Documentation for KycRecordSummary.target
     * @param sourceOfFundsIn Collection<SourceOfFunds> TODO: Model Documentation for KycRecordSummary.sourceOfFunds
     */
    public KycRecordSummary(final IndividualIdentityType identityTypeIn, final KycComplianceStatus kycStatusIn, final PepStatus pepStatusIn, final TargetEntity targetIn, final Collection<SourceOfFunds> sourceOfFundsIn)
    {
        this.identityType = identityTypeIn;
        this.kycStatus = kycStatusIn;
        this.pepStatus = pepStatusIn;
        this.target = targetIn;
        this.sourceOfFunds = sourceOfFundsIn;
    }

    /**
     * Constructor with all properties
     * @param refIn String
     * @param identityNoIn String
     * @param nameIn String
     * @param identityTypeIn IndividualIdentityType
     * @param emailAddressIn String
     * @param postalAddressIn String
     * @param physicalAddressIn String
     * @param phoneNumbersIn Collection<PhoneNumber>
     * @param kycStatusIn KycComplianceStatus
     * @param recordSummaryIn String
     * @param expiryDateIn LocalDate
     * @param pepStatusIn PepStatus
     * @param pepDetailsIn String
     * @param sanctionsMatchIn Boolean
     * @param sanctionsDetailsIn String
     * @param documentsIn List<KeyValue>
     * @param targetIn TargetEntity
     * @param sourceOfFundsDetailsIn String
     * @param sourceOfFundsIn Collection<SourceOfFunds>
     * @param dataVerificationsIn Collection<KeyValue>
     * @param wealthSourcesIn List<SourceOfWealth>
     * @param sourceOfWealthDetailsIn String
     * @param createdByIn String
     * @param reviewedByIn String
     * @param reviewedAtIn LocalDateTime
     * @param relatedPartiesIn List<RelatedParty>
     * @param riskLevelIn RiskLevel
     * @param riskFactorsIn List<String>
     * @param approvedByIn String
     * @param approvedAtIn LocalDateTime
     */
    public KycRecordSummary(final String refIn, final String identityNoIn, final String nameIn, final IndividualIdentityType identityTypeIn, final String emailAddressIn, final String postalAddressIn, final String physicalAddressIn, final Collection<PhoneNumber> phoneNumbersIn, final KycComplianceStatus kycStatusIn, final String recordSummaryIn, final LocalDate expiryDateIn, final PepStatus pepStatusIn, final String pepDetailsIn, final Boolean sanctionsMatchIn, final String sanctionsDetailsIn, final List<KeyValue> documentsIn, final TargetEntity targetIn, final String sourceOfFundsDetailsIn, final Collection<SourceOfFunds> sourceOfFundsIn, final Collection<KeyValue> dataVerificationsIn, final List<SourceOfWealth> wealthSourcesIn, final String sourceOfWealthDetailsIn, final String createdByIn, final String reviewedByIn, final LocalDateTime reviewedAtIn, final List<RelatedParty> relatedPartiesIn, final RiskLevel riskLevelIn, final List<String> riskFactorsIn, final String approvedByIn, final LocalDateTime approvedAtIn)
    {
        this.ref = refIn;
        this.identityNo = identityNoIn;
        this.name = nameIn;
        this.identityType = identityTypeIn;
        this.emailAddress = emailAddressIn;
        this.postalAddress = postalAddressIn;
        this.physicalAddress = physicalAddressIn;
        this.phoneNumbers = phoneNumbersIn;
        this.kycStatus = kycStatusIn;
        this.recordSummary = recordSummaryIn;
        this.expiryDate = expiryDateIn;
        this.pepStatus = pepStatusIn;
        this.pepDetails = pepDetailsIn;
        this.sanctionsMatch = sanctionsMatchIn;
        this.sanctionsDetails = sanctionsDetailsIn;
        this.documents = documentsIn;
        this.target = targetIn;
        this.sourceOfFundsDetails = sourceOfFundsDetailsIn;
        this.sourceOfFunds = sourceOfFundsIn;
        this.dataVerifications = dataVerificationsIn;
        this.wealthSources = wealthSourcesIn;
        this.sourceOfWealthDetails = sourceOfWealthDetailsIn;
        this.createdBy = createdByIn;
        this.reviewedBy = reviewedByIn;
        this.reviewedAt = reviewedAtIn;
        this.relatedParties = relatedPartiesIn;
        this.riskLevel = riskLevelIn;
        this.riskFactors = riskFactorsIn;
        this.approvedBy = approvedByIn;
        this.approvedAt = approvedAtIn;
    }

    /**
     * Copies constructor from other KycRecordSummary
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public KycRecordSummary(final KycRecordSummary otherBean)
    {
        this.ref = otherBean.getRef();
        this.identityNo = otherBean.getIdentityNo();
        this.name = otherBean.getName();
        this.identityType = otherBean.getIdentityType();
        this.emailAddress = otherBean.getEmailAddress();
        this.postalAddress = otherBean.getPostalAddress();
        this.physicalAddress = otherBean.getPhysicalAddress();
        this.phoneNumbers = otherBean.getPhoneNumbers();
        this.kycStatus = otherBean.getKycStatus();
        this.recordSummary = otherBean.getRecordSummary();
        this.expiryDate = otherBean.getExpiryDate();
        this.pepStatus = otherBean.getPepStatus();
        this.pepDetails = otherBean.getPepDetails();
        this.sanctionsMatch = otherBean.getSanctionsMatch();
        this.sanctionsDetails = otherBean.getSanctionsDetails();
        this.documents = otherBean.getDocuments();
        this.target = otherBean.getTarget();
        this.sourceOfFundsDetails = otherBean.getSourceOfFundsDetails();
        this.sourceOfFunds = otherBean.getSourceOfFunds();
        this.dataVerifications = otherBean.getDataVerifications();
        this.wealthSources = otherBean.getWealthSources();
        this.sourceOfWealthDetails = otherBean.getSourceOfWealthDetails();
        this.createdBy = otherBean.getCreatedBy();
        this.reviewedBy = otherBean.getReviewedBy();
        this.reviewedAt = otherBean.getReviewedAt();
        this.relatedParties = otherBean.getRelatedParties();
        this.riskLevel = otherBean.getRiskLevel();
        this.riskFactors = otherBean.getRiskFactors();
        this.approvedBy = otherBean.getApprovedBy();
        this.approvedAt = otherBean.getApprovedAt();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final KycRecordSummary otherBean)
    {
        if (null != otherBean)
        {
            this.setRef(otherBean.getRef());
            this.setIdentityNo(otherBean.getIdentityNo());
            this.setName(otherBean.getName());
            this.setIdentityType(otherBean.getIdentityType());
            this.setEmailAddress(otherBean.getEmailAddress());
            this.setPostalAddress(otherBean.getPostalAddress());
            this.setPhysicalAddress(otherBean.getPhysicalAddress());
            this.setPhoneNumbers(otherBean.getPhoneNumbers());
            this.setKycStatus(otherBean.getKycStatus());
            this.setRecordSummary(otherBean.getRecordSummary());
            this.setExpiryDate(otherBean.getExpiryDate());
            this.setPepStatus(otherBean.getPepStatus());
            this.setPepDetails(otherBean.getPepDetails());
            this.setSanctionsMatch(otherBean.getSanctionsMatch());
            this.setSanctionsDetails(otherBean.getSanctionsDetails());
            this.setDocuments(otherBean.getDocuments());
            this.setTarget(otherBean.getTarget());
            this.setSourceOfFundsDetails(otherBean.getSourceOfFundsDetails());
            this.setSourceOfFunds(otherBean.getSourceOfFunds());
            this.setDataVerifications(otherBean.getDataVerifications());
            this.setWealthSources(otherBean.getWealthSources());
            this.setSourceOfWealthDetails(otherBean.getSourceOfWealthDetails());
            this.setCreatedBy(otherBean.getCreatedBy());
            this.setReviewedBy(otherBean.getReviewedBy());
            this.setReviewedAt(otherBean.getReviewedAt());
            this.setRelatedParties(otherBean.getRelatedParties());
            this.setRiskLevel(otherBean.getRiskLevel());
            this.setRiskFactors(otherBean.getRiskFactors());
            this.setApprovedBy(otherBean.getApprovedBy());
            this.setApprovedAt(otherBean.getApprovedAt());
        }
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.ref
     * Get the ref Attribute
     * @return ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.ref
     * @param value String
     */
    public void setRef(final String value)
    {
        this.ref = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.identityNo
     * Get the identityNo Attribute
     * @return identityNo String
     */
    public String getIdentityNo()
    {
        return this.identityNo;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.identityNo
     * @param value String
     */
    public void setIdentityNo(final String value)
    {
        this.identityNo = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.name
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.identityType
     * Get the identityType Attribute
     * @return identityType IndividualIdentityType
     */
    public IndividualIdentityType getIdentityType()
    {
        return this.identityType;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.identityType
     * @param value IndividualIdentityType
     */
    public void setIdentityType(final IndividualIdentityType value)
    {
        this.identityType = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.emailAddress
     * Get the emailAddress Attribute
     * @return emailAddress String
     */
    public String getEmailAddress()
    {
        return this.emailAddress;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.emailAddress
     * @param value String
     */
    public void setEmailAddress(final String value)
    {
        this.emailAddress = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.postalAddress
     * Get the postalAddress Attribute
     * @return postalAddress String
     */
    public String getPostalAddress()
    {
        return this.postalAddress;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.postalAddress
     * @param value String
     */
    public void setPostalAddress(final String value)
    {
        this.postalAddress = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.physicalAddress
     * Get the physicalAddress Attribute
     * @return physicalAddress String
     */
    public String getPhysicalAddress()
    {
        return this.physicalAddress;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.physicalAddress
     * @param value String
     */
    public void setPhysicalAddress(final String value)
    {
        this.physicalAddress = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.phoneNumbers
     * Get the phoneNumbers Attribute
     * @return phoneNumbers Collection<PhoneNumber>
     */
    public Collection<PhoneNumber> getPhoneNumbers()
    {
        if (this.phoneNumbers == null)
        {
            this.phoneNumbers = new ArrayList<PhoneNumber>();
        }
        return this.phoneNumbers;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.phoneNumbers
     * @param value Collection<PhoneNumber>
     */
    public void setPhoneNumbers(final Collection<PhoneNumber> value)
    {
        this.phoneNumbers = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.kycStatus
     * Get the kycStatus Attribute
     * @return kycStatus KycComplianceStatus
     */
    public KycComplianceStatus getKycStatus()
    {
        return this.kycStatus;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.kycStatus
     * @param value KycComplianceStatus
     */
    public void setKycStatus(final KycComplianceStatus value)
    {
        this.kycStatus = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.recordSummary
     * Get the recordSummary Attribute
     * @return recordSummary String
     */
    public String getRecordSummary()
    {
        return this.recordSummary;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.recordSummary
     * @param value String
     */
    public void setRecordSummary(final String value)
    {
        this.recordSummary = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.expiryDate
     * Get the expiryDate Attribute
     * @return expiryDate LocalDate
     */
    public LocalDate getExpiryDate()
    {
        return this.expiryDate;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.expiryDate
     * @param value LocalDate
     */
    public void setExpiryDate(final LocalDate value)
    {
        this.expiryDate = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.pepStatus
     * Get the pepStatus Attribute
     * @return pepStatus PepStatus
     */
    public PepStatus getPepStatus()
    {
        return this.pepStatus;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.pepStatus
     * @param value PepStatus
     */
    public void setPepStatus(final PepStatus value)
    {
        this.pepStatus = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.pepDetails
     * Get the pepDetails Attribute
     * @return pepDetails String
     */
    public String getPepDetails()
    {
        return this.pepDetails;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.pepDetails
     * @param value String
     */
    public void setPepDetails(final String value)
    {
        this.pepDetails = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.sanctionsMatch
     * Get the sanctionsMatch Attribute
     * @return sanctionsMatch Boolean
     */
    public Boolean getSanctionsMatch()
    {
        return this.sanctionsMatch;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.sanctionsMatch
     * @param value Boolean
     */
    public void setSanctionsMatch(final Boolean value)
    {
        this.sanctionsMatch = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.sanctionsDetails
     * Get the sanctionsDetails Attribute
     * @return sanctionsDetails String
     */
    public String getSanctionsDetails()
    {
        return this.sanctionsDetails;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.sanctionsDetails
     * @param value String
     */
    public void setSanctionsDetails(final String value)
    {
        this.sanctionsDetails = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.documents
     * Get the documents Attribute
     * @return documents List<KeyValue>
     */
    public List<KeyValue> getDocuments()
    {
        if (this.documents == null)
        {
            this.documents = new ArrayList<KeyValue>();
        }
        return this.documents;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.documents
     * @param value List<KeyValue>
     */
    public void setDocuments(final List<KeyValue> value)
    {
        this.documents = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.target
     * Get the target Attribute
     * @return target TargetEntity
     */
    public TargetEntity getTarget()
    {
        return this.target;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.target
     * @param value TargetEntity
     */
    public void setTarget(final TargetEntity value)
    {
        this.target = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.sourceOfFundsDetails
     * Get the sourceOfFundsDetails Attribute
     * @return sourceOfFundsDetails String
     */
    public String getSourceOfFundsDetails()
    {
        return this.sourceOfFundsDetails;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.sourceOfFundsDetails
     * @param value String
     */
    public void setSourceOfFundsDetails(final String value)
    {
        this.sourceOfFundsDetails = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.sourceOfFunds
     * Get the sourceOfFunds Attribute
     * @return sourceOfFunds Collection<SourceOfFunds>
     */
    public Collection<SourceOfFunds> getSourceOfFunds()
    {
        if (this.sourceOfFunds == null)
        {
            this.sourceOfFunds = new ArrayList<SourceOfFunds>();
        }
        return this.sourceOfFunds;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.sourceOfFunds
     * @param value Collection<SourceOfFunds>
     */
    public void setSourceOfFunds(final Collection<SourceOfFunds> value)
    {
        this.sourceOfFunds = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.dataVerifications
     * Get the dataVerifications Attribute
     * @return dataVerifications Collection<KeyValue>
     */
    public Collection<KeyValue> getDataVerifications()
    {
        if (this.dataVerifications == null)
        {
            this.dataVerifications = new ArrayList<KeyValue>();
        }
        return this.dataVerifications;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.dataVerifications
     * @param value Collection<KeyValue>
     */
    public void setDataVerifications(final Collection<KeyValue> value)
    {
        this.dataVerifications = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.wealthSources
     * Get the wealthSources Attribute
     * @return wealthSources List<SourceOfWealth>
     */
    public List<SourceOfWealth> getWealthSources()
    {
        if (this.wealthSources == null)
        {
            this.wealthSources = new ArrayList<SourceOfWealth>();
        }
        return this.wealthSources;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.wealthSources
     * @param value List<SourceOfWealth>
     */
    public void setWealthSources(final List<SourceOfWealth> value)
    {
        this.wealthSources = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.sourceOfWealthDetails
     * Get the sourceOfWealthDetails Attribute
     * @return sourceOfWealthDetails String
     */
    public String getSourceOfWealthDetails()
    {
        return this.sourceOfWealthDetails;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.sourceOfWealthDetails
     * @param value String
     */
    public void setSourceOfWealthDetails(final String value)
    {
        this.sourceOfWealthDetails = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.createdBy
     * Get the createdBy Attribute
     * @return createdBy String
     */
    public String getCreatedBy()
    {
        return this.createdBy;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.createdBy
     * @param value String
     */
    public void setCreatedBy(final String value)
    {
        this.createdBy = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.reviewedBy
     * Get the reviewedBy Attribute
     * @return reviewedBy String
     */
    public String getReviewedBy()
    {
        return this.reviewedBy;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.reviewedBy
     * @param value String
     */
    public void setReviewedBy(final String value)
    {
        this.reviewedBy = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.reviewedAt
     * Get the reviewedAt Attribute
     * @return reviewedAt LocalDateTime
     */
    public LocalDateTime getReviewedAt()
    {
        return this.reviewedAt;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.reviewedAt
     * @param value LocalDateTime
     */
    public void setReviewedAt(final LocalDateTime value)
    {
        this.reviewedAt = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.relatedParties
     * Get the relatedParties Attribute
     * @return relatedParties List<RelatedParty>
     */
    public List<RelatedParty> getRelatedParties()
    {
        if (this.relatedParties == null)
        {
            this.relatedParties = new ArrayList<RelatedParty>();
        }
        return this.relatedParties;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.relatedParties
     * @param value List<RelatedParty>
     */
    public void setRelatedParties(final List<RelatedParty> value)
    {
        this.relatedParties = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.riskLevel
     * Get the riskLevel Attribute
     * @return riskLevel RiskLevel
     */
    public RiskLevel getRiskLevel()
    {
        return this.riskLevel;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.riskLevel
     * @param value RiskLevel
     */
    public void setRiskLevel(final RiskLevel value)
    {
        this.riskLevel = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.riskFactors
     * Get the riskFactors Attribute
     * @return riskFactors List<String>
     */
    public List<String> getRiskFactors()
    {
        if (this.riskFactors == null)
        {
            this.riskFactors = new ArrayList<String>();
        }
        return this.riskFactors;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.riskFactors
     * @param value List<String>
     */
    public void setRiskFactors(final List<String> value)
    {
        this.riskFactors = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.approvedBy
     * Get the approvedBy Attribute
     * @return approvedBy String
     */
    public String getApprovedBy()
    {
        return this.approvedBy;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.approvedBy
     * @param value String
     */
    public void setApprovedBy(final String value)
    {
        this.approvedBy = value;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.approvedAt
     * Get the approvedAt Attribute
     * @return approvedAt LocalDateTime
     */
    public LocalDateTime getApprovedAt()
    {
        return this.approvedAt;
    }

    /**
     * TODO: Model Documentation for KycRecordSummary.approvedAt
     * @param value LocalDateTime
     */
    public void setApprovedAt(final LocalDateTime value)
    {
        this.approvedAt = value;
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
        KycRecordSummary rhs = (KycRecordSummary) object;
        return new EqualsBuilder()
            .append(this.getRef(), rhs.getRef())
            .append(this.getIdentityNo(), rhs.getIdentityNo())
            .append(this.getName(), rhs.getName())
            .append(this.getIdentityType(), rhs.getIdentityType())
            .append(this.getEmailAddress(), rhs.getEmailAddress())
            .append(this.getPostalAddress(), rhs.getPostalAddress())
            .append(this.getPhysicalAddress(), rhs.getPhysicalAddress())
            .append(this.getPhoneNumbers(), rhs.getPhoneNumbers())
            .append(this.getKycStatus(), rhs.getKycStatus())
            .append(this.getRecordSummary(), rhs.getRecordSummary())
            .append(this.getExpiryDate(), rhs.getExpiryDate())
            .append(this.getPepStatus(), rhs.getPepStatus())
            .append(this.getPepDetails(), rhs.getPepDetails())
            .append(this.getSanctionsMatch(), rhs.getSanctionsMatch())
            .append(this.getSanctionsDetails(), rhs.getSanctionsDetails())
            .append(this.getDocuments(), rhs.getDocuments())
            .append(this.getTarget(), rhs.getTarget())
            .append(this.getSourceOfFundsDetails(), rhs.getSourceOfFundsDetails())
            .append(this.getSourceOfFunds(), rhs.getSourceOfFunds())
            .append(this.getDataVerifications(), rhs.getDataVerifications())
            .append(this.getWealthSources(), rhs.getWealthSources())
            .append(this.getSourceOfWealthDetails(), rhs.getSourceOfWealthDetails())
            .append(this.getCreatedBy(), rhs.getCreatedBy())
            .append(this.getReviewedBy(), rhs.getReviewedBy())
            .append(this.getReviewedAt(), rhs.getReviewedAt())
            .append(this.getRelatedParties(), rhs.getRelatedParties())
            .append(this.getRiskLevel(), rhs.getRiskLevel())
            .append(this.getRiskFactors(), rhs.getRiskFactors())
            .append(this.getApprovedBy(), rhs.getApprovedBy())
            .append(this.getApprovedAt(), rhs.getApprovedAt())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final KycRecordSummary object)
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
        return new CompareToBuilder()
            .append(this.getRef(), object.getRef())
            .append(this.getIdentityNo(), object.getIdentityNo())
            .append(this.getName(), object.getName())
            .append(this.getIdentityType(), object.getIdentityType())
            .append(this.getEmailAddress(), object.getEmailAddress())
            .append(this.getPostalAddress(), object.getPostalAddress())
            .append(this.getPhysicalAddress(), object.getPhysicalAddress())
            .append(this.getPhoneNumbers(), object.getPhoneNumbers())
            .append(this.getKycStatus(), object.getKycStatus())
            .append(this.getRecordSummary(), object.getRecordSummary())
            .append(this.getExpiryDate(), object.getExpiryDate())
            .append(this.getPepStatus(), object.getPepStatus())
            .append(this.getPepDetails(), object.getPepDetails())
            .append(this.getSanctionsMatch(), object.getSanctionsMatch())
            .append(this.getSanctionsDetails(), object.getSanctionsDetails())
            .append(this.getDocuments(), object.getDocuments())
            .append(this.getTarget(), object.getTarget())
            .append(this.getSourceOfFundsDetails(), object.getSourceOfFundsDetails())
            .append(this.getSourceOfFunds(), object.getSourceOfFunds())
            .append(this.getDataVerifications(), object.getDataVerifications())
            .append(this.getWealthSources(), object.getWealthSources())
            .append(this.getSourceOfWealthDetails(), object.getSourceOfWealthDetails())
            .append(this.getCreatedBy(), object.getCreatedBy())
            .append(this.getReviewedBy(), object.getReviewedBy())
            .append(this.getReviewedAt(), object.getReviewedAt())
            .append(this.getRelatedParties(), object.getRelatedParties())
            .append(this.getRiskLevel(), object.getRiskLevel())
            .append(this.getRiskFactors(), object.getRiskFactors())
            .append(this.getApprovedBy(), object.getApprovedBy())
            .append(this.getApprovedAt(), object.getApprovedAt())
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
            .append(this.getRef())
            .append(this.getIdentityNo())
            .append(this.getName())
            .append(this.getIdentityType())
            .append(this.getEmailAddress())
            .append(this.getPostalAddress())
            .append(this.getPhysicalAddress())
            .append(this.getPhoneNumbers())
            .append(this.getKycStatus())
            .append(this.getRecordSummary())
            .append(this.getExpiryDate())
            .append(this.getPepStatus())
            .append(this.getPepDetails())
            .append(this.getSanctionsMatch())
            .append(this.getSanctionsDetails())
            .append(this.getDocuments())
            .append(this.getTarget())
            .append(this.getSourceOfFundsDetails())
            .append(this.getSourceOfFunds())
            .append(this.getDataVerifications())
            .append(this.getWealthSources())
            .append(this.getSourceOfWealthDetails())
            .append(this.getCreatedBy())
            .append(this.getReviewedBy())
            .append(this.getReviewedAt())
            .append(this.getRelatedParties())
            .append(this.getRiskLevel())
            .append(this.getRiskFactors())
            .append(this.getApprovedBy())
            .append(this.getApprovedAt())
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
        builder.append("\t\"ref\" : \"" + this.getRef() + "\"");
        builder.append("\t,\"identityNo\" : \"" + this.getIdentityNo() + "\"");
        builder.append("\t,\"name\" : \"" + this.getName() + "\"");
        builder.append("\t,\"identityType\" : \"" + this.getIdentityType() + "\"");
        builder.append("\t,\"emailAddress\" : \"" + this.getEmailAddress() + "\"");
        builder.append("\t,\"postalAddress\" : \"" + this.getPostalAddress() + "\"");
        builder.append("\t,\"physicalAddress\" : \"" + this.getPhysicalAddress() + "\"");
        builder.append("\t,\"phoneNumbers\" : \"" + this.getPhoneNumbers() + "\"");
        builder.append("\t,\"kycStatus\" : \"" + this.getKycStatus() + "\"");
        builder.append("\t,\"recordSummary\" : \"" + this.getRecordSummary() + "\"");
        builder.append("\t,\"expiryDate\" : \"" + this.getExpiryDate() + "\"");
        builder.append("\t,\"pepStatus\" : \"" + this.getPepStatus() + "\"");
        builder.append("\t,\"pepDetails\" : \"" + this.getPepDetails() + "\"");
        builder.append("\t,\"sanctionsMatch\" : \"" + this.getSanctionsMatch() + "\"");
        builder.append("\t,\"sanctionsDetails\" : \"" + this.getSanctionsDetails() + "\"");
        builder.append("\t,\"documents\" : \"" + this.getDocuments() + "\"");
        builder.append("\t,\"target\" : \"" + this.getTarget() + "\"");
        builder.append("\t,\"sourceOfFundsDetails\" : \"" + this.getSourceOfFundsDetails() + "\"");
        builder.append("\t,\"sourceOfFunds\" : \"" + this.getSourceOfFunds() + "\"");
        builder.append("\t,\"dataVerifications\" : \"" + this.getDataVerifications() + "\"");
        builder.append("\t,\"wealthSources\" : \"" + this.getWealthSources() + "\"");
        builder.append("\t,\"sourceOfWealthDetails\" : \"" + this.getSourceOfWealthDetails() + "\"");
        builder.append("\t,\"createdBy\" : \"" + this.getCreatedBy() + "\"");
        builder.append("\t,\"reviewedBy\" : \"" + this.getReviewedBy() + "\"");
        builder.append("\t,\"reviewedAt\" : \"" + this.getReviewedAt() + "\"");
        builder.append("\t,\"relatedParties\" : \"" + this.getRelatedParties() + "\"");
        builder.append("\t,\"riskLevel\" : \"" + this.getRiskLevel() + "\"");
        builder.append("\t,\"riskFactors\" : \"" + this.getRiskFactors() + "\"");
        builder.append("\t,\"approvedBy\" : \"" + this.getApprovedBy() + "\"");
        builder.append("\t,\"approvedAt\" : \"" + this.getApprovedAt() + "\"");
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
    public boolean equalProperties(final Object thatObject)
    {
        if (thatObject == null || !this.getClass().isAssignableFrom(thatObject.getClass()))
        {
            return false;
        }

        final KycRecordSummary that = (KycRecordSummary)thatObject;
        return
            equal(this.getRef(), that.getRef())
            && equal(this.getIdentityNo(), that.getIdentityNo())
            && equal(this.getName(), that.getName())
            && equal(this.getIdentityType(), that.getIdentityType())
            && equal(this.getEmailAddress(), that.getEmailAddress())
            && equal(this.getPostalAddress(), that.getPostalAddress())
            && equal(this.getPhysicalAddress(), that.getPhysicalAddress())
            && equal(this.getPhoneNumbers(), that.getPhoneNumbers())
            && equal(this.getKycStatus(), that.getKycStatus())
            && equal(this.getRecordSummary(), that.getRecordSummary())
            && equal(this.getExpiryDate(), that.getExpiryDate())
            && equal(this.getPepStatus(), that.getPepStatus())
            && equal(this.getPepDetails(), that.getPepDetails())
            && equal(this.getSanctionsMatch(), that.getSanctionsMatch())
            && equal(this.getSanctionsDetails(), that.getSanctionsDetails())
            && equal(this.getDocuments(), that.getDocuments())
            && equal(this.getTarget(), that.getTarget())
            && equal(this.getSourceOfFundsDetails(), that.getSourceOfFundsDetails())
            && equal(this.getSourceOfFunds(), that.getSourceOfFunds())
            && equal(this.getDataVerifications(), that.getDataVerifications())
            && equal(this.getWealthSources(), that.getWealthSources())
            && equal(this.getSourceOfWealthDetails(), that.getSourceOfWealthDetails())
            && equal(this.getCreatedBy(), that.getCreatedBy())
            && equal(this.getReviewedBy(), that.getReviewedBy())
            && equal(this.getReviewedAt(), that.getReviewedAt())
            && equal(this.getRelatedParties(), that.getRelatedParties())
            && equal(this.getRiskLevel(), that.getRiskLevel())
            && equal(this.getRiskFactors(), that.getRiskFactors())
            && equal(this.getApprovedBy(), that.getApprovedBy())
            && equal(this.getApprovedAt(), that.getApprovedAt())
        ;
    }

    /**
     * This is a convenient helper method which is able to detect whether or not two values are equal. Two values
     * are equal when they are both {@code null}, are arrays of the same length with equal elements or are
     * equal objects (this includes {@link Collection} and {@link java.util.Map} instances).
     *
     * <p/>Note that for array, collection or map instances the comparison runs one level deep.
     *
     * @param first the first object to compare, may be {@code null}
     * @param second the second object to compare, may be {@code null}
     * @return this method will return {@code true} in case both objects are equal as explained above;
     *      in all other cases this method will return {@code false}
     */
    protected static boolean equal(final Object first, final Object second)
    {
        final boolean equal;

        if (first == null)
        {
            equal = (second == null);
        }
        else if (first.getClass().isArray() && (second != null) && second.getClass().isArray())
        {
            equal = Arrays.equals((Object[])first, (Object[])second);
        }
        else // note that the following also covers Collection and java.util.Map
        {
            equal = first.equals(second);
        }

        return equal;
    }

}