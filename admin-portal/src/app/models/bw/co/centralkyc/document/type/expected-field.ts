import { FormBuilder } from "@angular/forms";

import {KeyField} from '@models/bw/co/centralkyc/key-field';

export class ExpectedField {
    field: string | any;
    
    keyField: KeyField | any;
    
    mandatory: boolean | any = false;
    
    format: string | any;
    
    constructor() {
        this.field = null;
        this.keyField = null;
        this.mandatory = false;
        this.format = null;
    }
}
