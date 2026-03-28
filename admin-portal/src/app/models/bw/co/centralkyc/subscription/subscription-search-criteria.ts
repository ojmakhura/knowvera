import {TimePeriod} from '@models/bw/co/centralkyc/time-period';

export class SubscriptionSearchCriteria {
    ref: string | any;

    organisatonId: string | any;

    organisatonCode: string | any;

    organisationName: string | any;

    organisationRegistrationNo: string | any;

    period: TimePeriod | any;

    startDate: Date | any;

    endDate: Date | any;

    constructor() {
        this.ref = null;
        this.organisatonId = null;
        this.organisatonCode = null;
        this.organisationName = null;
        this.organisationRegistrationNo = null;
        this.period = null;
        this.startDate = null;
        this.endDate = null;
    }
}
