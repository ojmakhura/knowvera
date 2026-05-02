import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';
import {GroupFieldDTO} from '@models/bw/co/centralkyc/settings/kyc/group-field-dto';

export class KycFieldGroupDTO extends AuditableDTO {
    label: string | any;
    
    description: string | any;
    
    groupFields: Array<GroupFieldDTO> | any;
    
    targetType: TargetEntity | any;
    
    position: number | any;
    
    constructor() {
        super();
        this.label = null;
        this.description = null;
        this.groupFields = [];
        this.targetType = null;
        this.position = null;
    }
}
