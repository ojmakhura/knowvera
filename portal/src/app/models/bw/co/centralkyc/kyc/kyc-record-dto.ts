import { FormBuilder } from "@angular/forms";
import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {DeclarationDTO} from '@models/bw/co/centralkyc/kyc/declaration-dto';
import {SourceOfFunds} from '@models/bw/co/centralkyc/source-of-funds';
import {DocumentDTO} from '@models/bw/co/centralkyc/document/document-dto';
import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';
import {KycComplianceStatus} from '@models/bw/co/centralkyc/kyc/kyc-compliance-status';
import {KycVerificationDTO} from '@models/bw/co/centralkyc/kyc/verification/kyc-verification-dto';
import {EmploymentRecordDTO} from '@models/bw/co/centralkyc/individual/employment/employment-record-dto';
import {IndividualIdentityType} from '@models/bw/co/centralkyc/individual/individual-identity-type';

export class KycRecordDTO extends AuditableDTO {
    expiryDate: Date | any;
    
    uploadDate: Date | any;
    
    documents: Array<DocumentDTO> | any;
    
    name: string | any;
    
    identityNo: string | any;
    
    kycStatus: KycComplianceStatus | any;
    
    targetId: string | any;
    

    employmentRecord: EmploymentRecordDTO | any;
    target: TargetEntity | any;
    
    emailAddress: string | any;
    
    identityType: IndividualIdentityType | any;
    

    declaration: DeclarationDTO | any;
    sourceOfFunds: Array<SourceOfFunds> | any;
    
    sourceOfFundsDetails: string | any;
    

    kycVerification: KycVerificationDTO | any;
    physicalAddress: string | any;
    
    postalAddress: string | any;
    
    ref: string | any;
    
    constructor() {
        super();
        this.expiryDate = null;
        this.uploadDate = null;
        this.documents = [];
        this.name = null;
        this.identityNo = null;
        this.kycStatus = null;
        this.targetId = null;
        this.employmentRecord = null;
        this.target = null;
        this.emailAddress = null;
        this.identityType = null;
        this.declaration = null;
        this.sourceOfFunds = [];
        this.sourceOfFundsDetails = null;
        this.kycVerification = null;
        this.physicalAddress = null;
        this.postalAddress = null;
        this.ref = null;
    }
}
