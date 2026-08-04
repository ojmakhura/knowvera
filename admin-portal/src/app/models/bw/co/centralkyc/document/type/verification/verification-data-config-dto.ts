import {AuditableDTO} from '@models/bw/co/kyvera/auditable-dto';

import {ExpectedFieldDTO} from '@models/bw/co/kyvera/document/type/field/expected-field-dto';

export class VerificationDataConfigDTO extends AuditableDTO {
    name: string | any;
    
    documentTypeId: string | any;
    
    documentType: string | any;
    
    expectedFields: Array<ExpectedFieldDTO> | any;
    
    constructor() {
        super();
        this.name = null;
        this.documentTypeId = null;
        this.documentType = null;
        this.expectedFields = [];
    }
}
