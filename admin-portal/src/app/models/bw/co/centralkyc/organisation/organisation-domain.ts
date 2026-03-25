import { FormBuilder } from "@angular/forms";


export class OrganisationDomain {
    name: string | any;
    
    verified: boolean | any = false;
    
    constructor() {
        this.name = null;
        this.verified = false;
    }
}
