import {AuditableDTO} from '@models/bw/co/kyvera/auditable-dto';

import {ContactType} from '@models/bw/co/kyvera/contact/contact-type';

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
