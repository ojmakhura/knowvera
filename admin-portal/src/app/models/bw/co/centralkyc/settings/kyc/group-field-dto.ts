import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';


export class GroupFieldDTO extends AuditableDTO {
    position: number | any;
    
    fieldGroupId: string | any;
    
    fieldId: string | any;
    
    field: string | any;
    
    constructor() {
        super();
        this.position = null;
        this.fieldGroupId = null;
        this.fieldId = null;
        this.field = null;
    }
}
