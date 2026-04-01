import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {KeyField} from '@models/bw/co/centralkyc/key-field';

export class ExpectedFieldDTO extends AuditableDTO {
    field: string | any;
    
    keyField: KeyField | any;
    
    mandatory: boolean | any = false;
    
    format: string | any;
    
    documentTypeId: string | any;
    
    documentType: string | any;
    
    constructor() {
        super();
        this.field = null;
        this.keyField = null;
        this.mandatory = false;
        this.format = null;
        this.documentTypeId = null;
        this.documentType = null;
    }
}
