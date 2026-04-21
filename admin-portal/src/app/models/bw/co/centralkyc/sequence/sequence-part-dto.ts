
import {SequencePartType} from '@models/bw/co/centralkyc/sequence/sequence-part-type';

export class SequencePartDTO {
    id: number | any;
    
    position: number | any;
    
    initialValue: string | any;
    
    type: SequencePartType | any;
    
    min: string | any;
    
    max: string | any;
    
    randomised: boolean | any = false;
    
    name: string | any;
    
    currentValue: string | any;
    
    constructor() {
        this.id = null;
        this.position = null;
        this.initialValue = null;
        this.type = null;
        this.min = null;
        this.max = null;
        this.randomised = false;
        this.name = null;
        this.currentValue = null;
    }
}
