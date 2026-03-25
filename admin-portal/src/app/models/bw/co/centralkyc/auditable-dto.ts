import { FormBuilder } from "@angular/forms";


export class AuditableDTO {
    id: string | any;
    
    createdBy: string | any;
    
    createdAt: Date | any;
    
    modifiedBy: string | any;
    
    modifiedAt: Date | any;
    
    constructor() {
        this.id = null;
        this.createdBy = null;
        this.createdAt = null;
        this.modifiedBy = null;
        this.modifiedAt = null;
    }
}
