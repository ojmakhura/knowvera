import { FormBuilder } from "@angular/forms";
import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {ContactType} from '@models/bw/co/centralkyc/contact/contact-type';

export class ContactDTO extends AuditableDTO {
    message: string | any;
    
    email: string | any;
    
    type: ContactType | any;
    
    ref: string | any;
    
    constructor() {
        super();
        this.message = null;
        this.email = null;
        this.type = null;
        this.ref = null;
    }
}
