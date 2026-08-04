import {AuditableDTO} from '@models/bw/co/kyvera/auditable-dto';

import {SourceOfFunds} from '@models/bw/co/kyvera/source-of-funds';
import {KycComplianceStatus} from '@models/bw/co/kyvera/kyc/kyc-compliance-status';
import {DocumentDTO} from '@models/bw/co/kyvera/document/document-dto';
import {VerificationSummaryEntry} from '@models/bw/co/kyvera/kyc/verification-summary-entry';
import {TargetEntity} from '@models/bw/co/kyvera/target-entity';
import {OwnerDetails} from '@models/bw/co/kyvera/kyc/owner-details';
import {EmploymentRecordDTO} from '@models/bw/co/kyvera/individual/employment/employment-record-dto';
import {DeclarationDTO} from '@models/bw/co/kyvera/kyc/declaration-dto';
import { KycReportSectionDTO } from './fields/kyc-report-section-dto';

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
    }
}
