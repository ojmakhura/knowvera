import { FormBuilder } from "@angular/forms";

import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';

export class DocumentListDTO {
    id: string | any;
    
    target: TargetEntity | any;
    
    targetId: string | any;
    
    targetLabel: string | any;
    
    fileName: string | any;
    
    documentTypeId: string | any;
    
    documentType: string | any;
    
    constructor() {
        this.id = null;
        this.target = null;
        this.targetId = null;
        this.targetLabel = null;
        this.fileName = null;
        this.documentTypeId = null;
        this.documentType = null;
    }
}
