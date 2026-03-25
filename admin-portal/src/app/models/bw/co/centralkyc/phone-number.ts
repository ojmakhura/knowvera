import { FormBuilder } from "@angular/forms";

import {PhoneType} from '@models/bw/co/centralkyc/phone-type';

export class PhoneNumber {
    type: PhoneType | any;
    
    phoneNumber: string | any;
    
    constructor() {
        this.type = null;
        this.phoneNumber = null;
    }
}
