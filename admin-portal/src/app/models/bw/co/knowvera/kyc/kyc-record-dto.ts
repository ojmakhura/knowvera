import {AuditableDTO} from '@models/bw/co/knowvera/auditable-dto';

import {DocumentDTO} from '@models/bw/co/knowvera/document/document-dto';
import {EmploymentRecordDTO} from '@models/bw/co/knowvera/individual/employment/employment-record-dto';
import {TargetEntity} from '@models/bw/co/knowvera/target-entity';
import {DeclarationDTO} from '@models/bw/co/knowvera/kyc/declaration-dto';
import {SourceOfWealth} from '@models/bw/co/knowvera/source-of-wealth';
import {KycReportSectionDTO} from '@models/bw/co/knowvera/kyc/fields/kyc-report-section-dto';
import {SourceOfFunds} from '@models/bw/co/knowvera/source-of-funds';
import {KycComplianceStatus} from '@models/bw/co/knowvera/kyc/kyc-compliance-status';
import {RelatedParty} from '@models/bw/co/knowvera/kyc/related-party';
import {VerificationSummaryEntry} from '@models/bw/co/knowvera/kyc/verification-summary-entry';
import {RiskLevel} from '@models/bw/co/knowvera/kyc/risk-level';
import {OwnerDetails} from '@models/bw/co/knowvera/kyc/owner-details';

export class KycRecordDTO extends AuditableDTO {

    expiryDate: Date | any;
    uploadDate: Date | any;
    documents: Array<DocumentDTO> | any;
    kycStatus: KycComplianceStatus | any;
    targetId: string | any;
    employmentRecord: EmploymentRecordDTO | any;
    target: TargetEntity | any;
    declaration: DeclarationDTO | any;
    sourceOfFunds: Array<SourceOfFunds> | any;
    sourceOfFundsDetails: string | any;
    ref: string | any;
    ownerDetails: OwnerDetails | any;
    recordSummary: string | any;
    dataVerificationSummaries: Array<VerificationSummaryEntry> | any;
    kycReportSections: Array<KycReportSectionDTO> | any;
    wealthSources: Array<SourceOfWealth> | any;
    sourceOfWealthDetails: string | any;
    closedAt: Date | any;
    retainUntil: Date | any;
    reviewedBy: string | any;
    reviewedAt: Date | any;
    relatedParties: Array<RelatedParty> | any;
    riskLevel: RiskLevel | any;
    riskFactors: Array<string> | any;
    approvedBy: string | any;
    approvedAt: Date | any;

    constructor() {
        super();
        this.expiryDate = null;
        this.uploadDate = null;
        this.documents = [];
        this.kycStatus = null;
        this.targetId = null;
        this.employmentRecord = null;
        this.target = null;
        this.declaration = null;
        this.sourceOfFunds = [];
        this.sourceOfFundsDetails = null;
        this.ref = null;
        this.ownerDetails = null;
        this.recordSummary = null;
        this.dataVerificationSummaries = [];
        this.kycReportSections = [];
        this.wealthSources = [];
        this.sourceOfWealthDetails = null;
        this.closedAt = null;
        this.retainUntil = null;
        this.reviewedBy = null;
        this.reviewedAt = null;
        this.relatedParties = [];
        this.riskLevel = null;
        this.riskFactors = [];
        this.approvedBy = null;
        this.approvedAt = null;
    }
}
