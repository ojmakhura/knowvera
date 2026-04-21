import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {KeyField} from '@models/bw/co/centralkyc/key-field';
import { ExpectedFieldDTO } from '../field/expected-field-dto';

export class VerificationDataConfigDTO extends AuditableDTO {
    name: string | any;
    
    documentTypeId: string | any;
    
    documentType: string | any;

    expectedFields: ExpectedFieldDTO[] = []
    
    constructor() {
        super();
        this.name = null;
        this.documentTypeId = null;
        this.documentType = null;
        this.expectedFields = [];
    }
}
