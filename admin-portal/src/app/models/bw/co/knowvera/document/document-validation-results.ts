
import {DocumentVerificationStatus} from '@models/bw/co/knowvera/document/document-verification-status';

export class DocumentValidationResults {
    expectedType: string | any;
    
    detectedType: string | any;
    
    match: boolean | any = false;
    
    score: number | any;
    
    signalScores: any | any;
    
    reason: string | any;
    
    informationMatchResults: DocumentVerificationStatus | any;
    
    informationMatchDetails: string | any;
    
    typeMatch: boolean | any = false;
    
    constructor() {
        this.expectedType = null;
        this.detectedType = null;
        this.match = false;
        this.score = null;
        this.signalScores = null;
        this.reason = null;
        this.informationMatchResults = null;
        this.informationMatchDetails = null;
        this.typeMatch = false;
    }
}
