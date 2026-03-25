import { FormBuilder } from "@angular/forms";

import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';

export class QueueObject {
    documentId: string | any;
    
    target: TargetEntity | any;
    
    targetId: string | any;
    
    constructor() {
        this.documentId = null;
        this.target = null;
        this.targetId = null;
    }
}
