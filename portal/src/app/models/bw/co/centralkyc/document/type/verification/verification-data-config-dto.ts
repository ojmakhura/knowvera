import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {KeyField} from '@models/bw/co/centralkyc/key-field';

export class VerificationDataConfigDTO extends AuditableDTO {
    name: string | any;
    
    keyFields: Array<KeyField> | any;
    
    documentTypeId: string | any;
    
    documentType: string | any;
    
    constructor() {
        super();
        this.name = null;
        this.keyFields = [];
        this.documentTypeId = null;
        this.documentType = null;
    }
}
