
import {DataVerificationStatus} from '@models/bw/co/knowvera/document/data-verification-status';

export class VerificationSummaryEntry {
    verificationStatus: DataVerificationStatus | any = DataVerificationStatus.UNVERIFIED;
    
    verifiedBy: string | any;
    
    verificationReport: string | any;
    
    verificationParameter: string | any;
    
    constructor() {
        this.verificationStatus = DataVerificationStatus.UNVERIFIED;
        this.verifiedBy = null;
        this.verificationReport = null;
        this.verificationParameter = null;
    }
}
