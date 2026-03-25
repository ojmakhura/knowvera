import { FormBuilder } from "@angular/forms";
import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';


export class BranchDTO extends AuditableDTO {
    code: string | any;
    
    name: string | any;
    
    description: string | any;
    
    physicalAddress: string | any;
    
    organisationId: string | any;
    
    organisation: string | any;
    
    constructor() {
        super();
        this.code = null;
        this.name = null;
        this.description = null;
        this.physicalAddress = null;
        this.organisationId = null;
        this.organisation = null;
    }
}
