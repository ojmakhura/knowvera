

export class ValueData {
    
    position: number | any;
    success: boolean | any = false;
    expectedValue: string | any;
    extractedValue: string | any;
    similarity: number | any;
    mandatory: boolean | any = false;
    
    constructor() {
        this.position = null;
        this.success = false;
        this.expectedValue = null;
        this.extractedValue = null;
        this.similarity = null;
        this.mandatory = false;
    }
}
