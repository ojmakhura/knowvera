
import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';
import {SequencePartDTO} from '@models/bw/co/centralkyc/sequence/sequence-part-dto';

export class SequenceGeneratorDTO {
    id: string | any;
    
    name: string | any;
    
    targetEntity: TargetEntity | any;
    
    sequenceParts: Array<SequencePartDTO> | any;
    
    constructor() {
        this.id = null;
        this.name = null;
        this.targetEntity = null;
        this.sequenceParts = [];
    }
}
