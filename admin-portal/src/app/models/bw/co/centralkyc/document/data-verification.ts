
import {KeyFieldMatchResult} from '@models/bw/co/centralkyc/key-field-match-result';
import {DataVerificationStatus} from '@models/bw/co/centralkyc/document/data-verification-status';

export class DataVerification {
    verificationDataConfigId: string | any;
    
    verificationDataName: string | any;
    
    verificationStatus: DataVerificationStatus | any;
    
    verificationBy: string | any;
    
    verificationReport: string | any;
    
    score: number | any = 0.0;
    
    keyFieldMatches: Array<KeyFieldMatchResult> | any;
    
    hasMandatoryFields: boolean | any = false;
    
    constructor() {
        this.verificationDataConfigId = null;
        this.verificationDataName = null;
        this.verificationStatus = null;
        this.verificationBy = null;
        this.verificationReport = null;
        this.score = 0.0;
        this.keyFieldMatches = [];
        this.hasMandatoryFields = false;
    }
}
