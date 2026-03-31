
import {VerificationTagStatus} from '@models/bw/co/centralkyc/document/verification-tag-status';
import {VerificationTag} from '@models/bw/co/centralkyc/kyc/verification/verification-tag';

export class VerificationTagResult {
    verificationTag: VerificationTag | any;
    
    verificationTagStatus: VerificationTagStatus | any;
    
    score: number | any = 0.0;
    
    values: Array<string> | any;
    
    constructor() {
        this.verificationTag = null;
        this.verificationTagStatus = null;
        this.score = 0.0;
        this.values = [];
    }
}
