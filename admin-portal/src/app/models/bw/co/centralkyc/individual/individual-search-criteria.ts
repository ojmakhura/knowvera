import { FormBuilder } from "@angular/forms";

import {IndividualIdentityType} from '@models/bw/co/centralkyc/individual/individual-identity-type';
import {KycComplianceStatus} from '@models/bw/co/centralkyc/kyc/kyc-compliance-status';

export class IndividualSearchCriteria {
    emailAddress: string | any;
    
    identityNo: string | any;
    
    identityType: IndividualIdentityType | any;
    
    kycStatus: KycComplianceStatus | any;
    
    surname: string | any;
    
    middleName: string | any;
    
    firstName: string | any;
    
    constructor() {
        this.emailAddress = null;
        this.identityNo = null;
        this.identityType = null;
        this.kycStatus = null;
        this.surname = null;
        this.middleName = null;
        this.firstName = null;
    }
}
