import {AuditableDTO} from '@models/bw/co/knowvera/auditable-dto';

import {TargetEntity} from '@models/bw/co/knowvera/target-entity';
import {ExpectedFieldType} from '@models/bw/co/knowvera/document/type/field/expected-field-type';

export class ExpectedFieldDTO extends AuditableDTO {
    field: string | any;
    
    mandatory: boolean | any = false;
    
    format: string | any;
    
    documentTypeId: string | any;
    
    documentType: string | any;
    
    matchTo: string | any;
    
    targetType: TargetEntity | any;
    
    many: boolean | any = false;
    
    fieldType: ExpectedFieldType | any;
    
    fieldLabel: string | any;
    
    exactMatch: boolean | any = false;
    
    constructor() {
        super();
        this.field = null;
        this.mandatory = false;
        this.format = null;
        this.documentTypeId = null;
        this.documentType = null;
        this.matchTo = null;
        this.targetType = null;
        this.many = false;
        this.fieldType = null;
        this.fieldLabel = null;
        this.exactMatch = false;
    }
}
