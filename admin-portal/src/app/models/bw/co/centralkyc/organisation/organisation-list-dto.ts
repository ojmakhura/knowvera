import { FormBuilder } from "@angular/forms";

import {GeneralStatus} from '@models/bw/co/centralkyc/general-status';
import {KycComplianceStatus} from '@models/bw/co/centralkyc/kyc/kyc-compliance-status';

export class OrganisationListDTO {
    id: string | any;
    
    code: string | any;
    
    name: string | any;
    
    registrationNo: string | any;
    
    status: GeneralStatus | any;
    
    contactEmailAddress: string | any;
    
    kycStatus: KycComplianceStatus | any;
    
    isClient: boolean | any = false;
    
    keycloakId: string | any;
    
    constructor() {
        this.id = null;
        this.code = null;
        this.name = null;
        this.registrationNo = null;
        this.status = null;
        this.contactEmailAddress = null;
        this.kycStatus = null;
        this.isClient = false;
        this.keycloakId = null;
    }
}
