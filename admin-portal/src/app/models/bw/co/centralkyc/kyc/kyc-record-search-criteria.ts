
import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';
import {KycComplianceStatus} from '@models/bw/co/centralkyc/kyc/kyc-compliance-status';

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
