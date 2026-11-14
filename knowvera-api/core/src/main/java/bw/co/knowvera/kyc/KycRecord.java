// false
// true
package bw.co.knowvera.kyc;

import bw.co.knowvera.Auditable;
import bw.co.knowvera.SourceOfFunds;
import bw.co.knowvera.SourceOfWealth;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.document.Document;
import bw.co.knowvera.individual.PepStatus;
import bw.co.knowvera.individual.employment.EmploymentRecord;
import bw.co.knowvera.kyc.fields.KycReportSection;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * TODO: Model Documentation for KycRecord
 */
@Entity
@Table(
    name = "KYC_RECORD", uniqueConstraints = {
        @UniqueConstraint(
            name="TARGET_ATTACHMENT_UNIQUE",
            columnNames={
                "TARGET",
                "TARGET_ID"
            }
        )
    }
)
// Uncomment to enable caching for KycRecord
// @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.read-write)
@NamedQueries
({
    @NamedQuery(name = "KycRecord.findAll", query = "SELECT k FROM KycRecord AS k"),
})
public class KycRecord
    extends Auditable
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -4072392296463223120L;

    // Generate 27 attributes
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)

    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private UUID id;

    /**
     * TODO: Model Documentation for KycRecord.id
     * @return this.id UUID
     */
    public UUID getId()
    {
        return this.id;
    }

    /**
     * TODO: Model Documentation for KycRecord.id
     * @param idIn UUID
     */
    public void setId(UUID idIn)
    {
        this.id = idIn;
    }


    @Column(name = "UPLOAD_DATE", unique = false, nullable = false, insertable = true, updatable = true)
    private LocalDate uploadDate;

    /**
     * TODO: Model Documentation for KycRecord.uploadDate
     * @return this.uploadDate LocalDate
     */
    public LocalDate getUploadDate()
    {
        return this.uploadDate;
    }

    /**
     * TODO: Model Documentation for KycRecord.uploadDate
     * @param uploadDateIn LocalDate
     */
    public void setUploadDate(LocalDate uploadDateIn)
    {
        this.uploadDate = uploadDateIn;
    }


    @Column(name = "EXPIRY_DATE", unique = false, nullable = false, insertable = true, updatable = true)
    private LocalDate expiryDate;

    /**
     * TODO: Model Documentation for KycRecord.expiryDate
     * @return this.expiryDate LocalDate
     */
    public LocalDate getExpiryDate()
    {
        return this.expiryDate;
    }

    /**
     * TODO: Model Documentation for KycRecord.expiryDate
     * @param expiryDateIn LocalDate
     */
    public void setExpiryDate(LocalDate expiryDateIn)
    {
        this.expiryDate = expiryDateIn;
    }


    @Column(name = "KYC_STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private KycComplianceStatus kycStatus;

    /**
     * TODO: Model Documentation for KycRecord.kycStatus
     * @return this.kycStatus KycComplianceStatus
     */
    public KycComplianceStatus getKycStatus()
    {
        return this.kycStatus;
    }

    /**
     * TODO: Model Documentation for KycRecord.kycStatus
     * @param kycStatusIn KycComplianceStatus
     */
    public void setKycStatus(KycComplianceStatus kycStatusIn)
    {
        this.kycStatus = kycStatusIn;
    }


    @Column(name = "TARGET", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private TargetEntity target;

    /**
     * TODO: Model Documentation for KycRecord.target
     * @return this.target TargetEntity
     */
    public TargetEntity getTarget()
    {
        return this.target;
    }

    /**
     * TODO: Model Documentation for KycRecord.target
     * @param targetIn TargetEntity
     */
    public void setTarget(TargetEntity targetIn)
    {
        this.target = targetIn;
    }


    @Column(name = "TARGET_ID", unique = false, nullable = false, insertable = true, updatable = true)
    private String targetId;

    /**
     * TODO: Model Documentation for KycRecord.targetId
     * @return this.targetId String
     */
    public String getTargetId()
    {
        return this.targetId;
    }

    /**
     * TODO: Model Documentation for KycRecord.targetId
     * @param targetIdIn String
     */
    public void setTargetId(String targetIdIn)
    {
        this.targetId = targetIdIn;
    }


    @Column(name = "SANCTIONS_DETAILS", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "TEXT")
    private String sanctionsDetails;

    /**
     * TODO: Model Documentation for KycRecord.sanctionsDetails
     * @return this.sanctionsDetails String
     */
    public String getSanctionsDetails()
    {
        return this.sanctionsDetails;
    }

    /**
     * TODO: Model Documentation for KycRecord.sanctionsDetails
     * @param sanctionsDetailsIn String
     */
    public void setSanctionsDetails(String sanctionsDetailsIn)
    {
        this.sanctionsDetails = sanctionsDetailsIn;
    }


    @Column(name = "SANCTIONS_MATCH", unique = false, nullable = true, insertable = true, updatable = true)
    private Boolean sanctionsMatch = Boolean.valueOf(false);

    /**
     * TODO: Model Documentation for KycRecord.sanctionsMatch
     * @return this.sanctionsMatch Boolean
     */
    public Boolean getSanctionsMatch()
    {
        return this.sanctionsMatch;
    }

    /**
     * TODO: Model Documentation for KycRecord.sanctionsMatch
     * @param sanctionsMatchIn Boolean
     */
    public void setSanctionsMatch(Boolean sanctionsMatchIn)
    {
        this.sanctionsMatch = sanctionsMatchIn;
    }


    @Column(name = "PEP_DETAILS", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "TEXT")
    private String pepDetails;

    /**
     * TODO: Model Documentation for KycRecord.pepDetails
     * @return this.pepDetails String
     */
    public String getPepDetails()
    {
        return this.pepDetails;
    }

    /**
     * TODO: Model Documentation for KycRecord.pepDetails
     * @param pepDetailsIn String
     */
    public void setPepDetails(String pepDetailsIn)
    {
        this.pepDetails = pepDetailsIn;
    }


    @Column(name = "PEP_STATUS", unique = false, nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private PepStatus pepStatus;

    /**
     * TODO: Model Documentation for KycRecord.pepStatus
     * @return this.pepStatus PepStatus
     */
    public PepStatus getPepStatus()
    {
        return this.pepStatus;
    }

    /**
     * TODO: Model Documentation for KycRecord.pepStatus
     * @param pepStatusIn PepStatus
     */
    public void setPepStatus(PepStatus pepStatusIn)
    {
        this.pepStatus = pepStatusIn;
    }


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "KYC_RECORD_SOURCE_OF_FUNDS", joinColumns = @JoinColumn(name = "KYC_RECORD_ID"))
    @Column(name = "SOURCE_OF_FUNDS")
    @Enumerated(EnumType.STRING)
    private List<SourceOfFunds> sourceOfFunds;

    /**
     * TODO: Model Documentation for KycRecord.sourceOfFunds
     * @return this.sourceOfFunds List<SourceOfFunds>
     */
    public List<SourceOfFunds> getSourceOfFunds()
    {
        return this.sourceOfFunds;
    }

    /**
     * TODO: Model Documentation for KycRecord.sourceOfFunds
     * @param sourceOfFundsIn List<SourceOfFunds>
     */
    public void setSourceOfFunds(List<SourceOfFunds> sourceOfFundsIn)
    {
        this.sourceOfFunds = sourceOfFundsIn;
    }


    @Column(name = "SOURCE_OF_FUNDS_DETAILS", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "TEXT")
    private String sourceOfFundsDetails;

    /**
     * TODO: Model Documentation for KycRecord.sourceOfFundsDetails
     * @return this.sourceOfFundsDetails String
     */
    public String getSourceOfFundsDetails()
    {
        return this.sourceOfFundsDetails;
    }

    /**
     * TODO: Model Documentation for KycRecord.sourceOfFundsDetails
     * @param sourceOfFundsDetailsIn String
     */
    public void setSourceOfFundsDetails(String sourceOfFundsDetailsIn)
    {
        this.sourceOfFundsDetails = sourceOfFundsDetailsIn;
    }


    @Column(name = "REF", unique = true, nullable = false, insertable = true, updatable = true)
    private String ref;

    /**
     * TODO: Model Documentation for KycRecord.ref
     * @return this.ref String
     */
    public String getRef()
    {
        return this.ref;
    }

    /**
     * TODO: Model Documentation for KycRecord.ref
     * @param refIn String
     */
    public void setRef(String refIn)
    {
        this.ref = refIn;
    }


    @Column(name = "OWNER_DETAILS", unique = false, nullable = false, insertable = true, updatable = true, columnDefinition = "jsonb")
    @Type(JsonType.class)
    private OwnerDetails ownerDetails;

    /**
     * TODO: Model Documentation for KycRecord.ownerDetails
     * @return this.ownerDetails OwnerDetails
     */
    public OwnerDetails getOwnerDetails()
    {
        return this.ownerDetails;
    }

    /**
     * TODO: Model Documentation for KycRecord.ownerDetails
     * @param ownerDetailsIn OwnerDetails
     */
    public void setOwnerDetails(OwnerDetails ownerDetailsIn)
    {
        this.ownerDetails = ownerDetailsIn;
    }


    @Column(name = "RECORD_SUMMARY", unique = false, nullable = true, insertable = true, updatable = true, columnDefinition = "text")
    private String recordSummary;

    /**
     * TODO: Model Documentation for KycRecord.recordSummary
     * @return this.recordSummary String
     */
    public String getRecordSummary()
    {
        return this.recordSummary;
    }

    /**
     * TODO: Model Documentation for KycRecord.recordSummary
     * @param recordSummaryIn String
     */
    public void setRecordSummary(String recordSummaryIn)
    {
        this.recordSummary = recordSummaryIn;
    }


    @Column(name = "DATA_VERIFICATION_SUMMARIES", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<VerificationSummaryEntry> dataVerificationSummaries;

    /**
     * TODO: Model Documentation for KycRecord.dataVerificationSummaries
     * @return this.dataVerificationSummaries List<VerificationSummaryEntry>
     */
    public List<VerificationSummaryEntry> getDataVerificationSummaries()
    {
        return this.dataVerificationSummaries;
    }

    /**
     * TODO: Model Documentation for KycRecord.dataVerificationSummaries
     * @param dataVerificationSummariesIn List<VerificationSummaryEntry>
     */
    public void setDataVerificationSummaries(List<VerificationSummaryEntry> dataVerificationSummariesIn)
    {
        this.dataVerificationSummaries = dataVerificationSummariesIn;
    }


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "KYC_RECORD_WEALTH_SOURCES", joinColumns = @JoinColumn(name = "KYC_RECORD_ID"))
    @Column(name = "WEALTH_SOURCES")
    @Enumerated(EnumType.STRING)
    private List<SourceOfWealth> wealthSources;

    /**
     * TODO: Model Documentation for KycRecord.wealthSources
     * @return this.wealthSources List<SourceOfWealth>
     */
    public List<SourceOfWealth> getWealthSources()
    {
        return this.wealthSources;
    }

    /**
     * TODO: Model Documentation for KycRecord.wealthSources
     * @param wealthSourcesIn List<SourceOfWealth>
     */
    public void setWealthSources(List<SourceOfWealth> wealthSourcesIn)
    {
        this.wealthSources = wealthSourcesIn;
    }


    @Column(name = "SOURCE_OF_WEALTH_DETAILS", unique = false, nullable = true, insertable = true, updatable = true)
    private String sourceOfWealthDetails;

    /**
     * TODO: Model Documentation for KycRecord.sourceOfWealthDetails
     * @return this.sourceOfWealthDetails String
     */
    public String getSourceOfWealthDetails()
    {
        return this.sourceOfWealthDetails;
    }

    /**
     * TODO: Model Documentation for KycRecord.sourceOfWealthDetails
     * @param sourceOfWealthDetailsIn String
     */
    public void setSourceOfWealthDetails(String sourceOfWealthDetailsIn)
    {
        this.sourceOfWealthDetails = sourceOfWealthDetailsIn;
    }


    @Column(name = "CLOSED_AT", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDateTime closedAt;

    /**
     * TODO: Model Documentation for KycRecord.closedAt
     * @return this.closedAt LocalDateTime
     */
    public LocalDateTime getClosedAt()
    {
        return this.closedAt;
    }

    /**
     * TODO: Model Documentation for KycRecord.closedAt
     * @param closedAtIn LocalDateTime
     */
    public void setClosedAt(LocalDateTime closedAtIn)
    {
        this.closedAt = closedAtIn;
    }


    @Column(name = "RETAIN_UNTIL", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDate retainUntil;

    /**
     * TODO: Model Documentation for KycRecord.retainUntil
     * @return this.retainUntil LocalDate
     */
    public LocalDate getRetainUntil()
    {
        return this.retainUntil;
    }

    /**
     * TODO: Model Documentation for KycRecord.retainUntil
     * @param retainUntilIn LocalDate
     */
    public void setRetainUntil(LocalDate retainUntilIn)
    {
        this.retainUntil = retainUntilIn;
    }


    @Column(name = "REVIEWED_BY", unique = false, nullable = true, insertable = true, updatable = true)
    private String reviewedBy;

    /**
     * TODO: Model Documentation for KycRecord.reviewedBy
     * @return this.reviewedBy String
     */
    public String getReviewedBy()
    {
        return this.reviewedBy;
    }

    /**
     * TODO: Model Documentation for KycRecord.reviewedBy
     * @param reviewedByIn String
     */
    public void setReviewedBy(String reviewedByIn)
    {
        this.reviewedBy = reviewedByIn;
    }


    @Column(name = "REVIEWED_AT", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDateTime reviewedAt;

    /**
     * TODO: Model Documentation for KycRecord.reviewedAt
     * @return this.reviewedAt LocalDateTime
     */
    public LocalDateTime getReviewedAt()
    {
        return this.reviewedAt;
    }

    /**
     * TODO: Model Documentation for KycRecord.reviewedAt
     * @param reviewedAtIn LocalDateTime
     */
    public void setReviewedAt(LocalDateTime reviewedAtIn)
    {
        this.reviewedAt = reviewedAtIn;
    }


    @Column(name = "RELATED_PARTIES", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<RelatedParty> relatedParties;

    /**
     * TODO: Model Documentation for KycRecord.relatedParties
     * @return this.relatedParties List<RelatedParty>
     */
    public List<RelatedParty> getRelatedParties()
    {
        return this.relatedParties;
    }

    /**
     * TODO: Model Documentation for KycRecord.relatedParties
     * @param relatedPartiesIn List<RelatedParty>
     */
    public void setRelatedParties(List<RelatedParty> relatedPartiesIn)
    {
        this.relatedParties = relatedPartiesIn;
    }


    @Column(name = "RISK_LEVEL", unique = false, nullable = true, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    /**
     * TODO: Model Documentation for KycRecord.riskLevel
     * @return this.riskLevel RiskLevel
     */
    public RiskLevel getRiskLevel()
    {
        return this.riskLevel;
    }

    /**
     * TODO: Model Documentation for KycRecord.riskLevel
     * @param riskLevelIn RiskLevel
     */
    public void setRiskLevel(RiskLevel riskLevelIn)
    {
        this.riskLevel = riskLevelIn;
    }


    @Column(name = "RISK_FACTORS", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> riskFactors;

    /**
     * TODO: Model Documentation for KycRecord.riskFactors
     * @return this.riskFactors List<String>
     */
    public List<String> getRiskFactors()
    {
        return this.riskFactors;
    }

    /**
     * TODO: Model Documentation for KycRecord.riskFactors
     * @param riskFactorsIn List<String>
     */
    public void setRiskFactors(List<String> riskFactorsIn)
    {
        this.riskFactors = riskFactorsIn;
    }


    @Column(name = "APPROVED_BY", unique = false, nullable = true, insertable = true, updatable = true)
    private String approvedBy;

    /**
     * TODO: Model Documentation for KycRecord.approvedBy
     * @return this.approvedBy String
     */
    public String getApprovedBy()
    {
        return this.approvedBy;
    }

    /**
     * TODO: Model Documentation for KycRecord.approvedBy
     * @param approvedByIn String
     */
    public void setApprovedBy(String approvedByIn)
    {
        this.approvedBy = approvedByIn;
    }


    @Column(name = "APPROVED_AT", unique = false, nullable = true, insertable = true, updatable = true)
    private LocalDateTime approvedAt;

    /**
     * TODO: Model Documentation for KycRecord.approvedAt
     * @return this.approvedAt LocalDateTime
     */
    public LocalDateTime getApprovedAt()
    {
        return this.approvedAt;
    }

    /**
     * TODO: Model Documentation for KycRecord.approvedAt
     * @param approvedAtIn LocalDateTime
     */
    public void setApprovedAt(LocalDateTime approvedAtIn)
    {
        this.approvedAt = approvedAtIn;
    }

    // Generate 3 associations
    @OneToMany(cascade = {CascadeType.ALL} , mappedBy = "kycRecord",  fetch = FetchType.EAGER, orphanRemoval = true)
    private List<KycReportSection> kycReportSections = new ArrayList<KycReportSection>();
    
    /**
     * TODO: Model Documentation for KycReportSection
     * @return this.kycReportSections List<KycReportSection>
     */

    public List<KycReportSection> getKycReportSections()
    {
        return this.kycReportSections;
    }

    /**
     * TODO: Model Documentation for KycReportSection
     * @param kycReportSectionsIn List<KycReportSection>
     */
    public void setKycReportSections(List<KycReportSection> kycReportSectionsIn)
    {
        this.kycReportSections = kycReportSectionsIn;
    }

    /**
     * TODO: Model Documentation for KycReportSection
     * @param elementToAdd KycReportSection
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addKycReportSections(KycReportSection elementToAdd)
    {
        return this.kycReportSections.add(elementToAdd);
    }

    /**
     * TODO: Model Documentation for KycReportSection
     * @param elementToRemove KycReportSection
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeKycReportSections(KycReportSection elementToRemove)
    {
        return this.kycReportSections.remove(elementToRemove);
    }

    @ManyToOne( cascade = {CascadeType.ALL},  fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYMENT_RECORD_FK")
    private EmploymentRecord employmentRecord;

    /**
     * TODO: Model Documentation for EmploymentRecord
     * @return this.employmentRecord EmploymentRecord
     */

    public EmploymentRecord getEmploymentRecord()
    {
        return this.employmentRecord;
    }

    /**
     * TODO: Model Documentation for EmploymentRecord
     * @param employmentRecordIn EmploymentRecord
     */
    public void setEmploymentRecord(EmploymentRecord employmentRecordIn)
    {
        this.employmentRecord = employmentRecordIn;
    }

    @OneToMany(cascade = {CascadeType.ALL},  fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable
    (
        name = "DOCUMENTS2KYC_RECORD",
        joinColumns = {@JoinColumn(name = "KYC_RECORD_FK")},
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

    /**
     * Returns <code>true</code> if the argument is an KycRecord instance and all identifiers for this entity
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
        if (!(object instanceof KycRecord))
        {
            return false;
        }
        final KycRecord that = (KycRecord)object;
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
     * Constructs new instances of {@link KycRecord}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link KycRecord}.
         * @return new KycRecord()
         */
        public static KycRecord newInstance()
        {
            return new KycRecord();
        }

        /**
         * Constructs a new instance of {@link KycRecord}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param createdAt LocalDateTime
         * @param createdBy String
         * @param uploadDate LocalDate
         * @param expiryDate LocalDate
         * @param kycStatus KycComplianceStatus
         * @param target TargetEntity
         * @param targetId String
         * @param pepStatus PepStatus
         * @param sourceOfFunds List<SourceOfFunds>
         * @param ref String
         * @param ownerDetails OwnerDetails
         * @return newInstance
         */
        public static KycRecord newInstance(LocalDateTime createdAt, String createdBy, LocalDate uploadDate, LocalDate expiryDate, KycComplianceStatus kycStatus, TargetEntity target, String targetId, PepStatus pepStatus, List<SourceOfFunds> sourceOfFunds, String ref, OwnerDetails ownerDetails)
        {
            final KycRecord entity = new KycRecord();
            entity.setCreatedAt(createdAt);
            entity.setCreatedBy(createdBy);
            entity.setUploadDate(uploadDate);
            entity.setExpiryDate(expiryDate);
            entity.setKycStatus(kycStatus);
            entity.setTarget(target);
            entity.setTargetId(targetId);
            entity.setPepStatus(pepStatus);
            entity.setSourceOfFunds(sourceOfFunds);
            entity.setRef(ref);
            entity.setOwnerDetails(ownerDetails);
            return entity;
        }

        /**
         * Constructs a new instance of {@link KycRecord}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param createdAt LocalDateTime
         * @param modifiedAt LocalDateTime
         * @param createdBy String
         * @param modifiedBy String
         * @param uploadDate LocalDate
         * @param expiryDate LocalDate
         * @param kycStatus KycComplianceStatus
         * @param target TargetEntity
         * @param targetId String
         * @param sanctionsDetails String
         * @param sanctionsMatch Boolean
         * @param pepDetails String
         * @param pepStatus PepStatus
         * @param sourceOfFunds List<SourceOfFunds>
         * @param sourceOfFundsDetails String
         * @param ref String
         * @param ownerDetails OwnerDetails
         * @param recordSummary String
         * @param dataVerificationSummaries List<VerificationSummaryEntry>
         * @param wealthSources List<SourceOfWealth>
         * @param sourceOfWealthDetails String
         * @param closedAt LocalDateTime
         * @param retainUntil LocalDate
         * @param reviewedBy String
         * @param reviewedAt LocalDateTime
         * @param relatedParties List<RelatedParty>
         * @param riskLevel RiskLevel
         * @param riskFactors List<String>
         * @param approvedBy String
         * @param approvedAt LocalDateTime
         * @param kycReportSections List<KycReportSection>
         * @param employmentRecord EmploymentRecord
         * @param documents List<Document>
         * @return newInstance KycRecord
         */
        public static KycRecord newInstance(LocalDateTime createdAt, LocalDateTime modifiedAt, String createdBy, String modifiedBy, LocalDate uploadDate, LocalDate expiryDate, KycComplianceStatus kycStatus, TargetEntity target, String targetId, String sanctionsDetails, Boolean sanctionsMatch, String pepDetails, PepStatus pepStatus, List<SourceOfFunds> sourceOfFunds, String sourceOfFundsDetails, String ref, OwnerDetails ownerDetails, String recordSummary, List<VerificationSummaryEntry> dataVerificationSummaries, List<SourceOfWealth> wealthSources, String sourceOfWealthDetails, LocalDateTime closedAt, LocalDate retainUntil, String reviewedBy, LocalDateTime reviewedAt, List<RelatedParty> relatedParties, RiskLevel riskLevel, List<String> riskFactors, String approvedBy, LocalDateTime approvedAt, List<KycReportSection> kycReportSections, EmploymentRecord employmentRecord, List<Document> documents)
        {
            final KycRecord entity = new KycRecord();
            entity.setCreatedAt(createdAt);
            entity.setModifiedAt(modifiedAt);
            entity.setCreatedBy(createdBy);
            entity.setModifiedBy(modifiedBy);
            entity.setUploadDate(uploadDate);
            entity.setExpiryDate(expiryDate);
            entity.setKycStatus(kycStatus);
            entity.setTarget(target);
            entity.setTargetId(targetId);
            entity.setSanctionsDetails(sanctionsDetails);
            entity.setSanctionsMatch(sanctionsMatch);
            entity.setPepDetails(pepDetails);
            entity.setPepStatus(pepStatus);
            entity.setSourceOfFunds(sourceOfFunds);
            entity.setSourceOfFundsDetails(sourceOfFundsDetails);
            entity.setRef(ref);
            entity.setOwnerDetails(ownerDetails);
            entity.setRecordSummary(recordSummary);
            entity.setDataVerificationSummaries(dataVerificationSummaries);
            entity.setWealthSources(wealthSources);
            entity.setSourceOfWealthDetails(sourceOfWealthDetails);
            entity.setClosedAt(closedAt);
            entity.setRetainUntil(retainUntil);
            entity.setReviewedBy(reviewedBy);
            entity.setReviewedAt(reviewedAt);
            entity.setRelatedParties(relatedParties);
            entity.setRiskLevel(riskLevel);
            entity.setRiskFactors(riskFactors);
            entity.setApprovedBy(approvedBy);
            entity.setApprovedAt(approvedAt);
            entity.setKycReportSections(kycReportSections);
            entity.setEmploymentRecord(employmentRecord);
            entity.setDocuments(documents);
            return entity;
        }
    }

    /**
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see Comparable#compareTo
     */
    public int compareTo(KycRecord other)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(other.getId());
        }
        else
        {
            if (this.getUploadDate() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getUploadDate().compareTo(other.getUploadDate()));
            }
            if (this.getExpiryDate() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getExpiryDate().compareTo(other.getExpiryDate()));
            }
            if (this.getKycStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getKycStatus().compareTo(other.getKycStatus()));
            }
            if (this.getTarget() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTarget().compareTo(other.getTarget()));
            }
            if (this.getTargetId() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTargetId().compareTo(other.getTargetId()));
            }
            if (this.getSanctionsDetails() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSanctionsDetails().compareTo(other.getSanctionsDetails()));
            }
            if (this.getSanctionsMatch() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSanctionsMatch().compareTo(other.getSanctionsMatch()));
            }
            if (this.getPepDetails() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPepDetails().compareTo(other.getPepDetails()));
            }
            if (this.getPepStatus() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPepStatus().compareTo(other.getPepStatus()));
            }
            if (this.getSourceOfFunds() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getSourceOfFunds().size()-other.getSourceOfFunds().size()));
            }
            if (this.getSourceOfFundsDetails() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSourceOfFundsDetails().compareTo(other.getSourceOfFundsDetails()));
            }
            if (this.getRef() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRef().compareTo(other.getRef()));
            }
            if (this.getOwnerDetails() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getOwnerDetails().compareTo(other.getOwnerDetails()));
            }
            if (this.getRecordSummary() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRecordSummary().compareTo(other.getRecordSummary()));
            }
            if (this.getDataVerificationSummaries() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getDataVerificationSummaries().size()-other.getDataVerificationSummaries().size()));
            }
            if (this.getWealthSources() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getWealthSources().size()-other.getWealthSources().size()));
            }
            if (this.getSourceOfWealthDetails() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSourceOfWealthDetails().compareTo(other.getSourceOfWealthDetails()));
            }
            if (this.getClosedAt() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getClosedAt().compareTo(other.getClosedAt()));
            }
            if (this.getRetainUntil() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRetainUntil().compareTo(other.getRetainUntil()));
            }
            if (this.getReviewedBy() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getReviewedBy().compareTo(other.getReviewedBy()));
            }
            if (this.getReviewedAt() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getReviewedAt().compareTo(other.getReviewedAt()));
            }
            if (this.getRelatedParties() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getRelatedParties().size()-other.getRelatedParties().size()));
            }
            if (this.getRiskLevel() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRiskLevel().compareTo(other.getRiskLevel()));
            }
            if (this.getRiskFactors() != null)
            {
                // Compare Collection sizes for ordering
                cmp = (cmp != 0 ? cmp : Math.abs(this.getRiskFactors().size()-other.getRiskFactors().size()));
            }
            if (this.getApprovedBy() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getApprovedBy().compareTo(other.getApprovedBy()));
            }
            if (this.getApprovedAt() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getApprovedAt().compareTo(other.getApprovedAt()));
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
        sb.append("KycRecord(");
        sb.append(super.toString());
        sb.append(" id=").append(getId());
        sb.append(" uploadDate=").append(getUploadDate());
        sb.append(" expiryDate=").append(getExpiryDate());
        sb.append(" kycStatus=").append(getKycStatus());
        sb.append(" target=").append(getTarget());
        sb.append(" targetId=").append(getTargetId());
        sb.append(" sanctionsDetails=").append(getSanctionsDetails());
        sb.append(" sanctionsMatch=").append(getSanctionsMatch());
        sb.append(" pepDetails=").append(getPepDetails());
        sb.append(" pepStatus=").append(getPepStatus());
        sb.append(" sourceOfFunds=").append(getSourceOfFunds());
        sb.append(" sourceOfFundsDetails=").append(getSourceOfFundsDetails());
        sb.append(" ref=").append(getRef());
        sb.append(" ownerDetails=").append(getOwnerDetails());
        sb.append(" recordSummary=").append(getRecordSummary());
        sb.append(" dataVerificationSummaries=").append(getDataVerificationSummaries());
        sb.append(" wealthSources=").append(getWealthSources());
        sb.append(" sourceOfWealthDetails=").append(getSourceOfWealthDetails());
        sb.append(" closedAt=").append(getClosedAt());
        sb.append(" retainUntil=").append(getRetainUntil());
        sb.append(" reviewedBy=").append(getReviewedBy());
        sb.append(" reviewedAt=").append(getReviewedAt());
        sb.append(" relatedParties=").append(getRelatedParties());
        sb.append(" riskLevel=").append(getRiskLevel());
        sb.append(" riskFactors=").append(getRiskFactors());
        sb.append(" approvedBy=").append(getApprovedBy());
        sb.append(" approvedAt=").append(getApprovedAt());
        sb.append(" createdAt=").append(getCreatedAt());
        sb.append(" modifiedAt=").append(getModifiedAt());
        sb.append(" createdBy=").append(getCreatedBy());
        sb.append(" modifiedBy=").append(getModifiedBy());
        sb.append(")");
        return sb.toString();
    }
}