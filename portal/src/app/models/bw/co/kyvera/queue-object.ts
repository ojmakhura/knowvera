
import {TargetEntity} from '@models/bw/co/kyvera/target-entity';

export class QueueObject {
    objectId: string | any;
    
    target: TargetEntity | any;
    
    targetId: string | any;
    
    constructor() {
        this.objectId = null;
        this.target = null;
        this.targetId = null;
    }
}
