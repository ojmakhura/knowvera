import { FormBuilder } from "@angular/forms";


export class DocumentValidationResults {
    expectedType: string | any;
    
    detectedType: string | any;
    
    match: boolean | any = false;
    
    score: number | any;
    
    signalScores: any | any;
    
    reason: string | any;
    
    constructor() {
        this.expectedType = null;
        this.detectedType = null;
        this.match = false;
        this.score = null;
        this.signalScores = null;
        this.reason = null;
    }
}
