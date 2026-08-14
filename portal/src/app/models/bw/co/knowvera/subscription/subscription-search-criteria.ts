
import {TimePeriod} from '@models/bw/co/knowvera/time-period';
import {KycSubsciptionStatus} from '@models/bw/co/knowvera/subscription/kyc-subsciption-status';

export class SubscriptionSearchCriteria {
    ref: string | any;
    
    organisatonId: string | any;
    
    organisatonCode: string | any;
    
    organisationName: string | any;
    
    organisationRegistrationNo: string | any;
    
    period: TimePeriod | any;
    
    startDate: Date | any;
    
    endDate: Date | any;
    
    status: KycSubsciptionStatus | any;
    
    constructor() {
        this.ref = null;
        this.organisatonId = null;
        this.organisatonCode = null;
        this.organisationName = null;
        this.organisationRegistrationNo = null;
        this.period = null;
        this.startDate = null;
        this.endDate = null;
        this.status = null;
    }
}
