import { FormBuilder } from "@angular/forms";

import {GeneralStatus} from '@models/bw/co/centralkyc/general-status';

export class OrganisationSearchCriteria {
    registrationNo: string | any;
    
    status: GeneralStatus | any;
    
    name: string | any;
    
    id: string | any;
    
    contactEmailAddress: string | any;
    
    isClient: boolean | any;
    
    constructor() {
        this.registrationNo = null;
        this.status = null;
        this.name = null;
        this.id = null;
        this.contactEmailAddress = null;
        this.isClient = null;
    }
}
