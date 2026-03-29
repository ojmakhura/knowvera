import { FormBuilder } from "@angular/forms";
import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';
import {IndividualIdentityType} from '@models/bw/co/centralkyc/individual/individual-identity-type';
import {KycVerificationDTO} from '@models/bw/co/centralkyc/kyc/verification/kyc-verification-dto';
import {EmploymentRecordDTO} from '@models/bw/co/centralkyc/individual/employment/employment-record-dto';
import {DeclarationDTO} from '@models/bw/co/centralkyc/kyc/declaration-dto';
import {DocumentDTO} from '@models/bw/co/centralkyc/document/document-dto';
import {SourceOfFunds} from '@models/bw/co/centralkyc/source-of-funds';
import {KycComplianceStatus} from '@models/bw/co/centralkyc/kyc/kyc-compliance-status';
import { OwnerDetails } from "./owner-details";

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
    kycVerification: KycVerificationDTO | any;
    ref: string | any;
    ownerDetails: OwnerDetails | any;

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
        this.kycVerification = null;
        this.ref = null;
        this.ownerDetails = new OwnerDetails();
    }
}
