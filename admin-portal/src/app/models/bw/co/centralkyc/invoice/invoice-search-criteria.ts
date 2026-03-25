import { FormBuilder } from "@angular/forms";


export class InvoiceSearchCriteria {
    ref: string | any;
    
    organisatonId: string | any;
    
    organisatonCode: string | any;
    
    organisationName: string | any;
    
    organisationRegistrationNo: string | any;
    
    paid: boolean | any = false;
    
    constructor() {
        this.ref = null;
        this.organisatonId = null;
        this.organisatonCode = null;
        this.organisationName = null;
        this.organisationRegistrationNo = null;
        this.paid = false;
    }
}
