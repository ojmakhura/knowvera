import { FormBuilder } from "@angular/forms";
import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {VerificationStatus} from '@models/bw/co/centralkyc/kyc/verification/verification-status';

export class KycVerificationDTO extends AuditableDTO {
    sanctionsDetailsVerification: VerificationStatus | any;
    
    sanctionsDetailsVerificationReport: string | any;
    
    sanctionsDetailsVerificationBy: string | any;
    
    sanctionsMatchVerification: VerificationStatus | any;
    
    sanctionsMatchVerificationBy: string | any;
    
    sanctionsMatchVerificationReport: string | any;
    
    pepStatusVerification: VerificationStatus | any;
    
    pepStatusVerificationBy: string | any;
    
    pepStatusVerificationReport: string | any;
    
    sourceOfFundsVerifications: Array<VerificationStatus> | any;
    
    sourceOfFundsVerificationReport: string | any;
    
    sourceOfFundsVerificationBy: string | any;
    
    employmentVerification: VerificationStatus | any;
    
    employmentVerificationReport: string | any;
    
    employmentVerificationBy: string | any;
    
    recordId: string | any;
    
    recordOwner: string | any;
    
    constructor() {
        super();
        this.sanctionsDetailsVerification = null;
        this.sanctionsDetailsVerificationReport = null;
        this.sanctionsDetailsVerificationBy = null;
        this.sanctionsMatchVerification = null;
        this.sanctionsMatchVerificationBy = null;
        this.sanctionsMatchVerificationReport = null;
        this.pepStatusVerification = null;
        this.pepStatusVerificationBy = null;
        this.pepStatusVerificationReport = null;
        this.sourceOfFundsVerifications = [];
        this.sourceOfFundsVerificationReport = null;
        this.sourceOfFundsVerificationBy = null;
        this.employmentVerification = null;
        this.employmentVerificationReport = null;
        this.employmentVerificationBy = null;
        this.recordId = null;
        this.recordOwner = null;
    }
}
