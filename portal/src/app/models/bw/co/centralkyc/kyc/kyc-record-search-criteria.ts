
import {KycComplianceStatus} from '@models/bw/co/kyvera/kyc/kyc-compliance-status';
import {TargetEntity} from '@models/bw/co/kyvera/target-entity';

export class KycRecordSearchCriteria {
    name: string | any;
    
    registration: string | any;
    
    target: TargetEntity | any;
    
    targetIds: Array<string> | any;
    
    statuses: Array<KycComplianceStatus> | any;
    
    constructor() {
        this.name = null;
        this.registration = null;
        this.target = null;
        this.targetIds = [];
        this.statuses = [];
    }
}
