

export class KeyFieldMatchResult {
    keyField: string | any;
    
    expectedValue: string | any;
    
    extractedValue: string | any;
    
    similarity: number | any;
    
    mandatory: boolean | any = true;
    
    success: boolean | any = true;
    
    constructor() {
        this.keyField = null;
        this.expectedValue = null;
        this.extractedValue = null;
        this.similarity = null;
        this.mandatory = true;
        this.success = true;
    }
}
