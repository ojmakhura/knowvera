import { ExpectedFieldType } from "./type/field/expected-field-type";


export class DataComparisons {

    field: string | any;
    expected: string | any;
    extracted: string | any;
    matches: boolean | any = false;
    fieldType: ExpectedFieldType | any;
    
    constructor() {
        this.field = null;
        this.expected = null;
        this.extracted = null;
        this.matches = false;
        this.fieldType = null;
    }
}
