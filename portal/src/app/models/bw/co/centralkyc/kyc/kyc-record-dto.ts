import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {SourceOfFunds} from '@models/bw/co/centralkyc/source-of-funds';
import {VerificationSummaryEntry} from '@models/bw/co/centralkyc/kyc/verification-summary-entry';
import {KycComplianceStatus} from '@models/bw/co/centralkyc/kyc/kyc-compliance-status';
import {DocumentDTO} from '@models/bw/co/centralkyc/document/document-dto';
import {EmploymentRecordDTO} from '@models/bw/co/centralkyc/individual/employment/employment-record-dto';
import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';
import {OwnerDetails} from '@models/bw/co/centralkyc/kyc/owner-details';
import {DeclarationDTO} from '@models/bw/co/centralkyc/kyc/declaration-dto';

export class KycRecordDTO extends AuditableDTO {
    expiryDate: Date | any;

    uploadDate: Date | any;

    documents: Array<DocumentDTO> | any;

    kycStatus: KycComplianceStatus | any;

    targetId: string | any;


    employmentRecord: EmploymentRecordDTO | any = new EmploymentRecordDTO();
    target: TargetEntity | any;


    declaration: DeclarationDTO | any = new DeclarationDTO();
    sourceOfFunds: Array<SourceOfFunds> | any;

    sourceOfFundsDetails: string | any;

    ref: string | any;


    ownerDetails: OwnerDetails | any;
    recordSummary: string | any;

    dataVerificationSummaries: Array<VerificationSummaryEntry> | any;

    constructor() {
        super();
        this.expiryDate = null;
        this.uploadDate = null;
        this.documents = [];
        this.kycStatus = null;
        this.targetId = null;
        this.employmentRecord = new EmploymentRecordDTO();
        this.target = null;
        this.declaration = null;
        this.sourceOfFunds = [];
        this.sourceOfFundsDetails = null;
        this.ref = null;
        this.ownerDetails = new OwnerDetails();
        this.recordSummary = null;
        this.dataVerificationSummaries = [];
    }
}
