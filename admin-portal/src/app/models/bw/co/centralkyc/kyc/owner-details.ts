import { FormBuilder } from "@angular/forms";

import {PhoneNumber} from '@models/bw/co/centralkyc/phone-number';
import { IndividualIdentityType } from "../individual/individual-identity-type";

export class OwnerDetails {
    id: string | any;

    phoneNumbers: Array<PhoneNumber> | any;

    physicalAddress: string | any;

    postalAddress: string | any;

    emailAddress: string | any;

    identityNo: string | any;

    name: string | any;

    identityType: IndividualIdentityType | any;

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
