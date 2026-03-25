import { FormBuilder } from "@angular/forms";

import {IndividualIdentityType} from '@models/bw/co/centralkyc/individual/individual-identity-type';
import {Sex} from '@models/bw/co/centralkyc/individual/sex';
import {PepStatus} from '@models/bw/co/centralkyc/individual/pep-status';
import {KycComplianceStatus} from '@models/bw/co/centralkyc/kyc/kyc-compliance-status';

export class IndividualListDTO {
    id: string | any;
    
    name: string | any;
    
    identityNo: string | any;
    
    identityType: IndividualIdentityType | any;
    
    emailAddress: string | any;
    
    kycStatus: KycComplianceStatus | any;
    
    sex: Sex | any;
    
    pepStatus: PepStatus | any;
    
    userCreated: boolean | any = false;
    
    constructor() {
        this.id = null;
        this.name = null;
        this.identityNo = null;
        this.identityType = null;
        this.emailAddress = null;
        this.kycStatus = null;
        this.sex = null;
        this.pepStatus = null;
        this.userCreated = false;
    }
}
