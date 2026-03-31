
import {IndividualIdentityType} from '@models/bw/co/centralkyc/individual/individual-identity-type';
import {PhoneNumber} from '@models/bw/co/centralkyc/phone-number';

export class OwnerDetails {
    phoneNumbers: Array<PhoneNumber> | any;
    
    physicalAddress: string | any;
    
    postalAddress: string | any;
    
    emailAddress: string | any;
    
    identityNo: string | any;
    
    name: string | any;
    
    identityType: IndividualIdentityType | any;
    
    id: string | any;
    
    constructor() {
        this.phoneNumbers = [];
        this.physicalAddress = null;
        this.postalAddress = null;
        this.emailAddress = null;
        this.identityNo = null;
        this.name = null;
        this.identityType = null;
        this.id = null;
    }
}
