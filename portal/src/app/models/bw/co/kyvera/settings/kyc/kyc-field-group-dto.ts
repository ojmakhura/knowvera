import {AuditableDTO} from '@models/bw/co/kyvera/auditable-dto';

import {GroupFieldDTO} from '@models/bw/co/kyvera/settings/kyc/group-field-dto';
import {TargetEntity} from '@models/bw/co/kyvera/target-entity';

export class KycFieldGroupDTO extends AuditableDTO {
    label: string | any;
    
    description: string | any;
    
    groupFields: Array<GroupFieldDTO> | any;
    
    targetType: TargetEntity | any;
    
    position: number | any;
    
    individualSettingsId: string | any;
    
    organisationSettingsId: string | any;
    
    constructor() {
        super();
        this.label = null;
        this.description = null;
        this.groupFields = [];
        this.targetType = null;
        this.position = null;
        this.individualSettingsId = null;
        this.organisationSettingsId = null;
    }
}


