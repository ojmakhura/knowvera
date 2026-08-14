
import {PhoneType} from '@models/bw/co/knowvera/phone-type';

export class PhoneNumber {
    type: PhoneType | any;
    
    phoneNumber: string | any;
    
    constructor() {
        this.type = null;
        this.phoneNumber = null;
    }
}
