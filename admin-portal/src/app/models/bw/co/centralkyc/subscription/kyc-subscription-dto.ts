import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {KycInvoiceDTO} from '@models/bw/co/centralkyc/invoice/kyc-invoice-dto';
import {TimePeriod} from '@models/bw/co/centralkyc/time-period';
import {KycSubsciptionStatus} from '@models/bw/co/centralkyc/subscription/kyc-subsciption-status';

export class KycSubscriptionDTO extends AuditableDTO {
    startDate: Date | any;
    
    endDate: Date | any;
    
    amount: number | any;
    
    ref: string | any;
    
    period: TimePeriod | any;
    
    organisationId: string | any;
    
    organisationCode: string | any;
    
    organisationName: string | any;
    
    organisationRegistrationNo: string | any;
    
    status: KycSubsciptionStatus | any;
    
    invoices: Array<KycInvoiceDTO> | any;
    
    constructor() {
        super();
        this.startDate = null;
        this.endDate = null;
        this.amount = null;
        this.ref = null;
        this.period = null;
        this.organisationId = null;
        this.organisationCode = null;
        this.organisationName = null;
        this.organisationRegistrationNo = null;
        this.status = null;
        this.invoices = [];
    }
}
