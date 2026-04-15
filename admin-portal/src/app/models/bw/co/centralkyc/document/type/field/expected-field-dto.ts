import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';
import {ExpectedFieldType} from '@models/bw/co/centralkyc/document/type/field/expected-field-type';

export class ExpectedFieldDTO extends AuditableDTO {
    field: string | any;
    
    mandatory: boolean | any = false;
    
    format: string | any;
    
    documentTypeId: string | any;
    
    documentType: string | any;
    
    matchTo: string | any;
    
    targetType: TargetEntity | any;
    
    targetField: string | any;
    
    many: boolean | any = false;
    
    fieldType: ExpectedFieldType | any;
    
    constructor() {
        super();
        this.field = null;
        this.mandatory = false;
        this.format = null;
        this.documentTypeId = null;
        this.documentType = null;
        this.matchTo = null;
        this.targetType = null;
        this.targetField = null;
        this.many = false;
        this.fieldType = null;
    }
}
