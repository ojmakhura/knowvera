package bw.co.knowvera.kyc;

import bw.co.knowvera.AuditableDTO;
import bw.co.knowvera.SourceOfFunds;
import bw.co.knowvera.SourceOfWealth;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.individual.employment.EmploymentRecordDTO;
import bw.co.knowvera.kyc.fields.KycReportSectionDTO;
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
 * TODO: Model Documentation for KycRecordDTO
 */
public class KycRecordDTO
    extends AuditableDTO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 6699934436520881910L;

    // Class attributes
    /**
     * TODO: Model Documentation for KycRecordDTO.expiryDate
     */
    @Nullable
    protected LocalDate expiryDate;
    /**
     * TODO: Model Documentation for KycRecordDTO.uploadDate
     */
    @Nullable
    protected LocalDate uploadDate;
    /**
     * TODO: Model Documentation for KycRecordDTO.documents
     */
    @Nullable
    protected List<DocumentDTO> documents;
    /**
     * TODO: Model Documentation for KycRecordDTO.kycStatus
     */
    protected KycComplianceStatus kycStatus;
    /**
     * TODO: Model Documentation for KycRecordDTO.targetId
     */
    protected String targetId;
    /**
     * TODO: Model Documentation for KycRecordDTO.employmentRecord
     */
    @Nullable
    protected EmploymentRecordDTO employmentRecord;
    /**
     * TODO: Model Documentation for KycRecordDTO.target
     */
    protected TargetEntity target;
    /**
     * TODO: Model Documentation for KycRecordDTO.declaration
     */
    @Nullable
    protected DeclarationDTO declaration;
    /**
     * TODO: Model Documentation for KycRecordDTO.sourceOfFunds
     */
    protected List<SourceOfFunds> sourceOfFunds;
    /**
     * TODO: Model Documentation for KycRecordDTO.sourceOfFundsDetails
     */
    @Nullable
    protected String sourceOfFundsDetails;
    /**
     * TODO: Model Documentation for KycRecordDTO.ref
     */
    @Nullable
    protected String ref;
    /**
     * TODO: Model Documentation for KycRecordDTO.ownerDetails
     */
    protected OwnerDetails ownerDetails;
    /**
     * TODO: Model Documentation for KycRecordDTO.recordSummary
     */
    @Nullable
    protected String recordSummary;
    /**
     * TODO: Model Documentation for KycRecordDTO.dataVerificationSummaries
     */
    @Nullable
    protected List<VerificationSummaryEntry> dataVerificationSummaries;
    /**
     * TODO: Model Documentation for KycRecordDTO.kycReportSections
     */
    @Nullable
    protected List<KycReportSectionDTO> kycReportSections;
    /**
     * TODO: Model Documentation for KycRecordDTO.wealthSources
     */
    @Nullable
    protected List<SourceOfWealth> wealthSources;
    /**
     * TODO: Model Documentation for KycRecordDTO.sourceOfWealthDetails
     */
    @Nullable
    protected String sourceOfWealthDetails;
    /**
     * TODO: Model Documentation for KycRecordDTO.closedAt
     */
    @Nullable
    protected LocalDateTime closedAt;
    /**
     * TODO: Model Documentation for KycRecordDTO.retainUntil
     */
    @Nullable
    protected LocalDate retainUntil;
    /**
     * TODO: Model Documentation for KycRecordDTO.reviewedBy
     */
    @Nullable
    protected String reviewedBy;
    /**
     * TODO: Model Documentation for KycRecordDTO.reviewedAt
     */
    @Nullable
    protected LocalDateTime reviewedAt;
    /**
     * TODO: Model Documentation for KycRecordDTO.relatedParties
     */
    @Nullable
    protected List<RelatedParty> relatedParties;
    /**
     * TODO: Model Documentation for KycRecordDTO.riskLevel
     */
    @Nullable
    protected RiskLevel riskLevel;
    /**
     * TODO: Model Documentation for KycRecordDTO.riskFactors
     */
    @Nullable
    protected List<String> riskFactors;
    /**
     * TODO: Model Documentation for KycRecordDTO.approvedBy
     */
    @Nullable
    protected String approvedBy;
    /**
     * TODO: Model Documentation for KycRecordDTO.approvedAt
     */
    @Nullable
    protected LocalDateTime approvedAt;

    /** Default Constructor with no properties */
    public KycRecordDTO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param kycStatusIn KycComplianceStatus TODO: Model Documentation for KycRecordDTO.kycStatus
     * @param targetIdIn String TODO: Model Documentation for KycRecordDTO.targetId
     * @param targetIn TargetEntity TODO: Model Documentation for KycRecordDTO.target
     * @param sourceOfFundsIn List<SourceOfFunds> TODO: Model Documentation for KycRecordDTO.sourceOfFunds
     * @param ownerDetailsIn OwnerDetails TODO: Model Documentation for KycRecordDTO.ownerDetails
     */
    public KycRecordDTO(final KycComplianceStatus kycStatusIn, final String targetIdIn, final TargetEntity targetIn, final List<SourceOfFunds> sourceOfFundsIn, final OwnerDetails ownerDetailsIn)
    {
        super();
        this.kycStatus = kycStatusIn;
        this.targetId = targetIdIn;
        this.target = targetIn;
        this.sourceOfFunds = sourceOfFundsIn;
        this.ownerDetails = ownerDetailsIn;
    }

    /**
     * Constructor with all properties
     * @param idIn String
     * @param createdByIn String
     * @param createdAtIn LocalDateTime
     * @param modifiedByIn String
     * @param modifiedAtIn LocalDateTime
     * @param expiryDateIn LocalDate
     * @param uploadDateIn LocalDate
     * @param documentsIn List<DocumentDTO>
     * @param kycStatusIn KycComplianceStatus
     * @param targetIdIn String
     * @param employmentRecordIn EmploymentRecordDTO
     * @param targetIn TargetEntity
     * @param declarationIn DeclarationDTO
     * @param sourceOfFundsIn List<SourceOfFunds>
     * @param sourceOfFundsDetailsIn String
     * @param refIn String
     * @param ownerDetailsIn OwnerDetails
     * @param recordSummaryIn String
     * @param dataVerificationSummariesIn List<VerificationSummaryEntry>
     * @param kycReportSectionsIn List<KycReportSectionDTO>
     * @param wealthSourcesIn List<SourceOfWealth>
     * @param sourceOfWealthDetailsIn String
     * @param closedAtIn LocalDateTime
     * @param retainUntilIn LocalDate
     * @param reviewedByIn String
     * @param reviewedAtIn LocalDateTime
     * @param relatedPartiesIn List<RelatedParty>
     * @param riskLevelIn RiskLevel
     * @param riskFactorsIn List<String>
     * @param approvedByIn String
     * @param approvedAtIn LocalDateTime
     */
    public KycRecordDTO(final String idIn, final String createdByIn, final LocalDateTime createdAtIn, final String modifiedByIn, final LocalDateTime modifiedAtIn, final LocalDate expiryDateIn, final LocalDate uploadDateIn, final List<DocumentDTO> documentsIn, final KycComplianceStatus kycStatusIn, final String targetIdIn, final EmploymentRecordDTO employmentRecordIn, final TargetEntity targetIn, final DeclarationDTO declarationIn, final List<SourceOfFunds> sourceOfFundsIn, final String sourceOfFundsDetailsIn, final String refIn, final OwnerDetails ownerDetailsIn, final String recordSummaryIn, final List<VerificationSummaryEntry> dataVerificationSummariesIn, final List<KycReportSectionDTO> kycReportSectionsIn, final List<SourceOfWealth> wealthSourcesIn, final String sourceOfWealthDetailsIn, final LocalDateTime closedAtIn, final LocalDate retainUntilIn, final String reviewedByIn, final LocalDateTime reviewedAtIn, final List<RelatedParty> relatedPartiesIn, final RiskLevel riskLevelIn, final List<String> riskFactorsIn, final String approvedByIn, final LocalDateTime approvedAtIn)
    {
        super(idIn, createdByIn, createdAtIn, modifiedByIn, modifiedAtIn);
        this.expiryDate = expiryDateIn;
        this.uploadDate = uploadDateIn;
        this.documents = documentsIn;
        this.kycStatus = kycStatusIn;
        this.targetId = targetIdIn;
        this.employmentRecord = employmentRecordIn;
        this.target = targetIn;
        this.declaration = declarationIn;
        this.sourceOfFunds = sourceOfFundsIn;
        this.sourceOfFundsDetails = sourceOfFundsDetailsIn;
        this.ref = refIn;
        this.ownerDetails = ownerDetailsIn;
        this.recordSummary = recordSummaryIn;
        this.dataVerificationSummaries = dataVerificationSummariesIn;
        this.kycReportSections = kycReportSectionsIn;
        this.wealthSources = wealthSourcesIn;
        this.sourceOfWealthDetails = sourceOfWealthDetailsIn;
        this.closedAt = closedAtIn;
        this.retainUntil = retainUntilIn;
        this.reviewedBy = reviewedByIn;
        this.reviewedAt = reviewedAtIn;
        this.relatedParties = relatedPartiesIn;
        this.riskLevel = riskLevelIn;
        this.riskFactors = riskFactorsIn;
        this.approvedBy = approvedByIn;
        this.approvedAt = approvedAtIn;
    }

    /**
     * Copies constructor from other KycRecordDTO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public KycRecordDTO(final KycRecordDTO otherBean)
    {
        super(otherBean);
        this.expiryDate = otherBean.getExpiryDate();
        this.uploadDate = otherBean.getUploadDate();
        this.documents = otherBean.getDocuments();
        this.kycStatus = otherBean.getKycStatus();
        this.targetId = otherBean.getTargetId();
        this.employmentRecord = otherBean.getEmploymentRecord();
        this.target = otherBean.getTarget();
        this.declaration = otherBean.getDeclaration();
        this.sourceOfFunds = otherBean.getSourceOfFunds();
        this.sourceOfFundsDetails = otherBean.getSourceOfFundsDetails();
        this.ref = otherBean.getRef();
        this.ownerDetails = otherBean.getOwnerDetails();
        this.recordSummary = otherBean.getRecordSummary();
        this.dataVerificationSummaries = otherBean.getDataVerificationSummaries();
        this.kycReportSections = otherBean.getKycReportSections();
        this.wealthSources = otherBean.getWealthSources();
        this.sourceOfWealthDetails = otherBean.getSourceOfWealthDetails();
        this.closedAt = otherBean.getClosedAt();
        this.retainUntil = otherBean.getRetainUntil();
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
    public void copy(final KycRecordDTO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setExpiryDate(otherBean.getExpiryDate());
            this.setUploadDate(otherBean.getUploadDate());
            this.setDocuments(otherBean.getDocuments());
            this.setKycStatus(otherBean.getKycStatus());
            this.setTargetId(otherBean.getTargetId());
            this.setEmploymentRecord(otherBean.getEmploymentRecord());
            this.setTarget(otherBean.getTarget());
            this.setDeclaration(otherBean.getDeclaration());
            this.setSourceOfFunds(otherBean.getSourceOfFunds());
            this.setSourceOfFundsDetails(otherBean.getSourceOfFundsDetails());
            this.setRef(otherBean.getRef());
            this.setOwnerDetails(otherBean.getOwnerDetails());
            this.setRecordSummary(otherBean.getRecordSummary());
            this.setDataVerificationSummaries(otherBean.getDataVerificationSummaries());
            this.setKycReportSections(otherBean.getKycReportSections());
            this.setWealthSources(otherBean.getWealthSources());
            this.setSourceOfWealthDetails(otherBean.getSourceOfWealthDetails());
            this.setClosedAt(otherBean.getClosedAt());
            this.setRetainUntil(otherBean.getRetainUntil());
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
     * TODO: Model Documentation for KycRecordDTO.expiryDate
     * Get the expiryDate Attribute
     * @return expiryDate LocalDate
     */
    public LocalDate getExpiryDate()
    {
        return this.expiryDate;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.expiryDate
     * @param value LocalDate
     */
    public void setExpiryDate(final LocalDate value)
    {
        this.expiryDate = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.uploadDate
     * Get the uploadDate Attribute
     * @return uploadDate LocalDate
     */
    public LocalDate getUploadDate()
    {
        return this.uploadDate;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.uploadDate
     * @param value LocalDate
     */
    public void setUploadDate(final LocalDate value)
    {
        this.uploadDate = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.documents
     * Get the documents Attribute
     * @return documents List<DocumentDTO>
     */
    public List<DocumentDTO> getDocuments()
    {
        if (this.documents == null)
        {
            this.documents = new ArrayList<DocumentDTO>();
        }
        return this.documents;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.documents
     * @param value List<DocumentDTO>
     */
    public void setDocuments(final List<DocumentDTO> value)
    {
        this.documents = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.kycStatus
     * Get the kycStatus Attribute
     * @return kycStatus KycComplianceStatus
     */
    public KycComplianceStatus getKycStatus()
    {
        return this.kycStatus;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.kycStatus
     * @param value KycComplianceStatus
     */
    public void setKycStatus(final KycComplianceStatus value)
    {
        this.kycStatus = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.targetId
     * Get the targetId Attribute
     * @return targetId String
     */
    public String getTargetId()
    {
        return this.targetId;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.targetId
     * @param value String
     */
    public void setTargetId(final String value)
    {
        this.targetId = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.employmentRecord
     * Get the employmentRecord Attribute
     * @return employmentRecord EmploymentRecordDTO
     */
    public EmploymentRecordDTO getEmploymentRecord()
    {
        return this.employmentRecord;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.employmentRecord
     * @param value EmploymentRecordDTO
     */
    public void setEmploymentRecord(final EmploymentRecordDTO value)
    {
        this.employmentRecord = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.target
     * Get the target Attribute
     * @return target TargetEntity
     */
    public TargetEntity getTarget()
    {
        return this.target;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.target
     * @param value TargetEntity
     */
    public void setTarget(final TargetEntity value)
    {
        this.target = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.declaration
     * Get the declaration Attribute
     * @return declaration DeclarationDTO
     */
    public DeclarationDTO getDeclaration()
    {
        return this.declaration;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.declaration
     * @param value DeclarationDTO
     */
    public void setDeclaration(final DeclarationDTO value)
    {
        this.declaration = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.sourceOfFunds
     * Get the sourceOfFunds Attribute
     * @return sourceOfFunds List<SourceOfFunds>
     */
    public List<SourceOfFunds> getSourceOfFunds()
    {
        if (this.sourceOfFunds == null)
        {
            this.sourceOfFunds = new ArrayList<SourceOfFunds>();
        }
        return this.sourceOfFunds;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.sourceOfFunds
     * @param value List<SourceOfFunds>
     */
    public void setSourceOfFunds(final List<SourceOfFunds> value)
    {
        this.sourceOfFunds = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.sourceOfFundsDetails
     * Get the sourceOfFundsDetails Attribute
     * @return sourceOfFundsDetails String
     */
    public String getSourceOfFundsDetails()
    {
        return this.sourceOfFundsDetails;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.sourceOfFundsDetails
     * @param value String
     */
    public void setSourceOfFundsDetails(final String value)
    {
        this.sourceOfFundsDetails = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.ref
     * Get the ref Attribute
     * @return ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.ref
     * @param value String
     */
    public void setRef(final String value)
    {
        this.ref = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.ownerDetails
     * Get the ownerDetails Attribute
     * @return ownerDetails OwnerDetails
     */
    public OwnerDetails getOwnerDetails()
    {
        return this.ownerDetails;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.ownerDetails
     * @param value OwnerDetails
     */
    public void setOwnerDetails(final OwnerDetails value)
    {
        this.ownerDetails = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.recordSummary
     * Get the recordSummary Attribute
     * @return recordSummary String
     */
    public String getRecordSummary()
    {
        return this.recordSummary;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.recordSummary
     * @param value String
     */
    public void setRecordSummary(final String value)
    {
        this.recordSummary = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.dataVerificationSummaries
     * Get the dataVerificationSummaries Attribute
     * @return dataVerificationSummaries List<VerificationSummaryEntry>
     */
    public List<VerificationSummaryEntry> getDataVerificationSummaries()
    {
        if (this.dataVerificationSummaries == null)
        {
            this.dataVerificationSummaries = new ArrayList<VerificationSummaryEntry>();
        }
        return this.dataVerificationSummaries;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.dataVerificationSummaries
     * @param value List<VerificationSummaryEntry>
     */
    public void setDataVerificationSummaries(final List<VerificationSummaryEntry> value)
    {
        this.dataVerificationSummaries = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.kycReportSections
     * Get the kycReportSections Attribute
     * @return kycReportSections List<KycReportSectionDTO>
     */
    public List<KycReportSectionDTO> getKycReportSections()
    {
        if (this.kycReportSections == null)
        {
            this.kycReportSections = new ArrayList<KycReportSectionDTO>();
        }
        return this.kycReportSections;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.kycReportSections
     * @param value List<KycReportSectionDTO>
     */
    public void setKycReportSections(final List<KycReportSectionDTO> value)
    {
        this.kycReportSections = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.wealthSources
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
     * TODO: Model Documentation for KycRecordDTO.wealthSources
     * @param value List<SourceOfWealth>
     */
    public void setWealthSources(final List<SourceOfWealth> value)
    {
        this.wealthSources = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.sourceOfWealthDetails
     * Get the sourceOfWealthDetails Attribute
     * @return sourceOfWealthDetails String
     */
    public String getSourceOfWealthDetails()
    {
        return this.sourceOfWealthDetails;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.sourceOfWealthDetails
     * @param value String
     */
    public void setSourceOfWealthDetails(final String value)
    {
        this.sourceOfWealthDetails = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.closedAt
     * Get the closedAt Attribute
     * @return closedAt LocalDateTime
     */
    public LocalDateTime getClosedAt()
    {
        return this.closedAt;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.closedAt
     * @param value LocalDateTime
     */
    public void setClosedAt(final LocalDateTime value)
    {
        this.closedAt = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.retainUntil
     * Get the retainUntil Attribute
     * @return retainUntil LocalDate
     */
    public LocalDate getRetainUntil()
    {
        return this.retainUntil;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.retainUntil
     * @param value LocalDate
     */
    public void setRetainUntil(final LocalDate value)
    {
        this.retainUntil = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.reviewedBy
     * Get the reviewedBy Attribute
     * @return reviewedBy String
     */
    public String getReviewedBy()
    {
        return this.reviewedBy;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.reviewedBy
     * @param value String
     */
    public void setReviewedBy(final String value)
    {
        this.reviewedBy = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.reviewedAt
     * Get the reviewedAt Attribute
     * @return reviewedAt LocalDateTime
     */
    public LocalDateTime getReviewedAt()
    {
        return this.reviewedAt;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.reviewedAt
     * @param value LocalDateTime
     */
    public void setReviewedAt(final LocalDateTime value)
    {
        this.reviewedAt = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.relatedParties
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
     * TODO: Model Documentation for KycRecordDTO.relatedParties
     * @param value List<RelatedParty>
     */
    public void setRelatedParties(final List<RelatedParty> value)
    {
        this.relatedParties = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.riskLevel
     * Get the riskLevel Attribute
     * @return riskLevel RiskLevel
     */
    public RiskLevel getRiskLevel()
    {
        return this.riskLevel;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.riskLevel
     * @param value RiskLevel
     */
    public void setRiskLevel(final RiskLevel value)
    {
        this.riskLevel = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.riskFactors
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
     * TODO: Model Documentation for KycRecordDTO.riskFactors
     * @param value List<String>
     */
    public void setRiskFactors(final List<String> value)
    {
        this.riskFactors = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.approvedBy
     * Get the approvedBy Attribute
     * @return approvedBy String
     */
    public String getApprovedBy()
    {
        return this.approvedBy;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.approvedBy
     * @param value String
     */
    public void setApprovedBy(final String value)
    {
        this.approvedBy = value;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.approvedAt
     * Get the approvedAt Attribute
     * @return approvedAt LocalDateTime
     */
    public LocalDateTime getApprovedAt()
    {
        return this.approvedAt;
    }

    /**
     * TODO: Model Documentation for KycRecordDTO.approvedAt
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
        KycRecordDTO rhs = (KycRecordDTO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getExpiryDate(), rhs.getExpiryDate())
            .append(this.getUploadDate(), rhs.getUploadDate())
            .append(this.getDocuments(), rhs.getDocuments())
            .append(this.getKycStatus(), rhs.getKycStatus())
            .append(this.getTargetId(), rhs.getTargetId())
            .append(this.getEmploymentRecord(), rhs.getEmploymentRecord())
            .append(this.getTarget(), rhs.getTarget())
            .append(this.getDeclaration(), rhs.getDeclaration())
            .append(this.getSourceOfFunds(), rhs.getSourceOfFunds())
            .append(this.getSourceOfFundsDetails(), rhs.getSourceOfFundsDetails())
            .append(this.getRef(), rhs.getRef())
            .append(this.getOwnerDetails(), rhs.getOwnerDetails())
            .append(this.getRecordSummary(), rhs.getRecordSummary())
            .append(this.getDataVerificationSummaries(), rhs.getDataVerificationSummaries())
            .append(this.getKycReportSections(), rhs.getKycReportSections())
            .append(this.getWealthSources(), rhs.getWealthSources())
            .append(this.getSourceOfWealthDetails(), rhs.getSourceOfWealthDetails())
            .append(this.getClosedAt(), rhs.getClosedAt())
            .append(this.getRetainUntil(), rhs.getRetainUntil())
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
        if (!(object instanceof KycRecordDTO))
        {
            return -1;
        }
        KycRecordDTO myClass = (KycRecordDTO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getExpiryDate(), myClass.getExpiryDate())
            .append(this.getUploadDate(), myClass.getUploadDate())
            .append(this.getDocuments(), myClass.getDocuments())
            .append(this.getKycStatus(), myClass.getKycStatus())
            .append(this.getTargetId(), myClass.getTargetId())
            .append(this.getEmploymentRecord(), myClass.getEmploymentRecord())
            .append(this.getTarget(), myClass.getTarget())
            .append(this.getDeclaration(), myClass.getDeclaration())
            .append(this.getSourceOfFunds(), myClass.getSourceOfFunds())
            .append(this.getSourceOfFundsDetails(), myClass.getSourceOfFundsDetails())
            .append(this.getRef(), myClass.getRef())
            .append(this.getOwnerDetails(), myClass.getOwnerDetails())
            .append(this.getRecordSummary(), myClass.getRecordSummary())
            .append(this.getDataVerificationSummaries(), myClass.getDataVerificationSummaries())
            .append(this.getKycReportSections(), myClass.getKycReportSections())
            .append(this.getWealthSources(), myClass.getWealthSources())
            .append(this.getSourceOfWealthDetails(), myClass.getSourceOfWealthDetails())
            .append(this.getClosedAt(), myClass.getClosedAt())
            .append(this.getRetainUntil(), myClass.getRetainUntil())
            .append(this.getReviewedBy(), myClass.getReviewedBy())
            .append(this.getReviewedAt(), myClass.getReviewedAt())
            .append(this.getRelatedParties(), myClass.getRelatedParties())
            .append(this.getRiskLevel(), myClass.getRiskLevel())
            .append(this.getRiskFactors(), myClass.getRiskFactors())
            .append(this.getApprovedBy(), myClass.getApprovedBy())
            .append(this.getApprovedAt(), myClass.getApprovedAt())
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
            .append(this.getExpiryDate())
            .append(this.getUploadDate())
            .append(this.getDocuments())
            .append(this.getKycStatus())
            .append(this.getTargetId())
            .append(this.getEmploymentRecord())
            .append(this.getTarget())
            .append(this.getDeclaration())
            .append(this.getSourceOfFunds())
            .append(this.getSourceOfFundsDetails())
            .append(this.getRef())
            .append(this.getOwnerDetails())
            .append(this.getRecordSummary())
            .append(this.getDataVerificationSummaries())
            .append(this.getKycReportSections())
            .append(this.getWealthSources())
            .append(this.getSourceOfWealthDetails())
            .append(this.getClosedAt())
            .append(this.getRetainUntil())
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
        builder.append("\t\"expiryDate\" : \"" + this.getExpiryDate() + "\"");
        builder.append("\t,\"uploadDate\" : \"" + this.getUploadDate() + "\"");
        builder.append("\t,\"documents\" : \"" + this.getDocuments() + "\"");
        builder.append("\t,\"kycStatus\" : \"" + this.getKycStatus() + "\"");
        builder.append("\t,\"targetId\" : \"" + this.getTargetId() + "\"");
        builder.append("\t,\"employmentRecord\" : \"" + this.getEmploymentRecord() + "\"");
        builder.append("\t,\"target\" : \"" + this.getTarget() + "\"");
        builder.append("\t,\"declaration\" : \"" + this.getDeclaration() + "\"");
        builder.append("\t,\"sourceOfFunds\" : \"" + this.getSourceOfFunds() + "\"");
        builder.append("\t,\"sourceOfFundsDetails\" : \"" + this.getSourceOfFundsDetails() + "\"");
        builder.append("\t,\"ref\" : \"" + this.getRef() + "\"");
        builder.append("\t,\"ownerDetails\" : \"" + this.getOwnerDetails() + "\"");
        builder.append("\t,\"recordSummary\" : \"" + this.getRecordSummary() + "\"");
        builder.append("\t,\"dataVerificationSummaries\" : \"" + this.getDataVerificationSummaries() + "\"");
        builder.append("\t,\"kycReportSections\" : \"" + this.getKycReportSections() + "\"");
        builder.append("\t,\"wealthSources\" : \"" + this.getWealthSources() + "\"");
        builder.append("\t,\"sourceOfWealthDetails\" : \"" + this.getSourceOfWealthDetails() + "\"");
        builder.append("\t,\"closedAt\" : \"" + this.getClosedAt() + "\"");
        builder.append("\t,\"retainUntil\" : \"" + this.getRetainUntil() + "\"");
        builder.append("\t,\"reviewedBy\" : \"" + this.getReviewedBy() + "\"");
        builder.append("\t,\"reviewedAt\" : \"" + this.getReviewedAt() + "\"");
        builder.append("\t,\"relatedParties\" : \"" + this.getRelatedParties() + "\"");
        builder.append("\t,\"riskLevel\" : \"" + this.getRiskLevel() + "\"");
        builder.append("\t,\"riskFactors\" : \"" + this.getRiskFactors() + "\"");
        builder.append("\t,\"approvedBy\" : \"" + this.getApprovedBy() + "\"");
        builder.append("\t,\"approvedAt\" : \"" + this.getApprovedAt() + "\"");
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
        final KycRecordDTO that = (KycRecordDTO)thatObject;
        return super.equalProperties(that)
            && equal(this.getExpiryDate(), that.getExpiryDate())
            && equal(this.getUploadDate(), that.getUploadDate())
            && equal(this.getDocuments(), that.getDocuments())
            && equal(this.getKycStatus(), that.getKycStatus())
            && equal(this.getTargetId(), that.getTargetId())
            && equal(this.getEmploymentRecord(), that.getEmploymentRecord())
            && equal(this.getTarget(), that.getTarget())
            && equal(this.getDeclaration(), that.getDeclaration())
            && equal(this.getSourceOfFunds(), that.getSourceOfFunds())
            && equal(this.getSourceOfFundsDetails(), that.getSourceOfFundsDetails())
            && equal(this.getRef(), that.getRef())
            && equal(this.getOwnerDetails(), that.getOwnerDetails())
            && equal(this.getRecordSummary(), that.getRecordSummary())
            && equal(this.getDataVerificationSummaries(), that.getDataVerificationSummaries())
            && equal(this.getKycReportSections(), that.getKycReportSections())
            && equal(this.getWealthSources(), that.getWealthSources())
            && equal(this.getSourceOfWealthDetails(), that.getSourceOfWealthDetails())
            && equal(this.getClosedAt(), that.getClosedAt())
            && equal(this.getRetainUntil(), that.getRetainUntil())
            && equal(this.getReviewedBy(), that.getReviewedBy())
            && equal(this.getReviewedAt(), that.getReviewedAt())
            && equal(this.getRelatedParties(), that.getRelatedParties())
            && equal(this.getRiskLevel(), that.getRiskLevel())
            && equal(this.getRiskFactors(), that.getRiskFactors())
            && equal(this.getApprovedBy(), that.getApprovedBy())
            && equal(this.getApprovedAt(), that.getApprovedAt())
        ;
    }

}