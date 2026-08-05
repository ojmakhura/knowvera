
import {DataVerificationStatus} from '@models/bw/co/knowvera/document/data-verification-status';
import {KeyFieldMatchResult} from '@models/bw/co/knowvera/key-field-match-result';

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
