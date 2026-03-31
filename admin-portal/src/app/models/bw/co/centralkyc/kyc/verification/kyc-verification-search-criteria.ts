
import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';
import {KycComplianceStatus} from '@models/bw/co/centralkyc/kyc/kyc-compliance-status';

export class KycVerificationSearchCriteria {
    recordId: string | any;
    
    registration: string | any;
    
    target: TargetEntity | any;
    
    targetIds: Array<string> | any;
    
    statuses: Array<KycComplianceStatus> | any;
    
    constructor() {
        this.recordId = null;
        this.registration = null;
        this.target = null;
        this.targetIds = [];
        this.statuses = [];
    }
}
