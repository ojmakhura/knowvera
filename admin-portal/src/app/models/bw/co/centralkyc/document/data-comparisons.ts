

export class DataComparisons {
    field: string | any;
    
    expected: string | any;
    
    extracted: string | any;
    
    matches: boolean | any = false;
    
    constructor() {
        this.field = null;
        this.expected = null;
        this.extracted = null;
        this.matches = false;
    }
}
