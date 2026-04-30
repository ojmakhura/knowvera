import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {ExpectedFieldDTO} from '@models/bw/co/centralkyc/document/type/field/expected-field-dto';

export class KycFieldGroupDTO extends AuditableDTO {
    label: string | any;
    
    description: string | any;
    
    expectedFields: Array<ExpectedFieldDTO> | any;
    
    constructor() {
        super();
        this.label = null;
        this.description = null;
        this.expectedFields = [];
    }
}
