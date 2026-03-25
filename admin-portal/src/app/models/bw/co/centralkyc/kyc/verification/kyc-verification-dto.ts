import { FormBuilder } from "@angular/forms";
import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {VerificationStatus} from '@models/bw/co/centralkyc/kyc/verification/verification-status';

export class KycVerificationDTO extends AuditableDTO {
    sanctionsDetailsVerification: VerificationStatus | any = VerificationStatus.UNVERIFIED;
    
    sanctionsDetailsVerificationReport: string | any;
    
    sanctionsDetailsVerificationBy: string | any;
    
    sanctionsMatchVerification: VerificationStatus | any = VerificationStatus.UNVERIFIED;
    
    sanctionsMatchVerificationBy: string | any;
    
    sanctionsMatchVerificationReport: string | any;
    
    pepStatusVerification: VerificationStatus | any = VerificationStatus.UNVERIFIED;
    
    pepStatusVerificationBy: string | any;
    
    pepStatusVerificationReport: string | any;
    
    sourceOfFundsVerification: VerificationStatus | any = VerificationStatus.UNVERIFIED;
    
    sourceOfFundsVerificationReport: string | any;
    
    sourceOfFundsVerificationBy: string | any;
    
    employmentVerification: VerificationStatus | any = VerificationStatus.UNVERIFIED;
    
    employmentVerificationReport: string | any;
    
    employmentVerificationBy: string | any;
    
    recordId: string | any;
    
    recordOwner: string | any;
    
    identityVerification: VerificationStatus | any = VerificationStatus.UNVERIFIED;
    
    identityVerificationBy: string | any;
    
    identityVerificationReport: string | any;
    
    addressVerification: VerificationStatus | any = VerificationStatus.UNVERIFIED;
    
    addressVerificationBy: string | any;
    
    addressVerificationReport: string | any;
    
    constructor() {
        super();
        this.sanctionsDetailsVerification = VerificationStatus.UNVERIFIED;
        this.sanctionsDetailsVerificationReport = null;
        this.sanctionsDetailsVerificationBy = null;
        this.sanctionsMatchVerification = VerificationStatus.UNVERIFIED;
        this.sanctionsMatchVerificationBy = null;
        this.sanctionsMatchVerificationReport = null;
        this.pepStatusVerification = VerificationStatus.UNVERIFIED;
        this.pepStatusVerificationBy = null;
        this.pepStatusVerificationReport = null;
        this.sourceOfFundsVerification = VerificationStatus.UNVERIFIED;
        this.sourceOfFundsVerificationReport = null;
        this.sourceOfFundsVerificationBy = null;
        this.employmentVerification = VerificationStatus.UNVERIFIED;
        this.employmentVerificationReport = null;
        this.employmentVerificationBy = null;
        this.recordId = null;
        this.recordOwner = null;
        this.identityVerification = VerificationStatus.UNVERIFIED;
        this.identityVerificationBy = null;
        this.identityVerificationReport = null;
        this.addressVerification = VerificationStatus.UNVERIFIED;
        this.addressVerificationBy = null;
        this.addressVerificationReport = null;
    }
}
