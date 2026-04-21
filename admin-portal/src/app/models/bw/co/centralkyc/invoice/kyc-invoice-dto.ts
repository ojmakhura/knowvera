import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {TimePeriod} from '@models/bw/co/centralkyc/time-period';
import {DocumentDTO} from '@models/bw/co/centralkyc/document/document-dto';

export class KycInvoiceDTO extends AuditableDTO {
    amount: number | any;
    
    ref: string | any;
    
    issueDate: Date | any;
    
    organisationId: string | any;
    
    organisationCode: string | any;
    
    organisationName: string | any;
    
    organisationRegistrationNo: string | any;
    
    paid: boolean | any = false;
    
    paymentDate: Date | any;
    

    invoiceDocument: DocumentDTO | any;

    proofOfPayment: DocumentDTO | any;
    paymentReference: string | any;
    
    subscriptionRef: string | any;
    
    subscriptionId: string | any;
    
    startDate: Date | any;
    
    endDate: Date | any;
    
    subscriptionPeriod: TimePeriod | any;
    
    vat: number | any;
    
    totalAmount: number | any;
    
    constructor() {
        super();
        this.amount = null;
        this.ref = null;
        this.issueDate = null;
        this.organisationId = null;
        this.organisationCode = null;
        this.organisationName = null;
        this.organisationRegistrationNo = null;
        this.paid = false;
        this.paymentDate = null;
        this.invoiceDocument = null;
        this.proofOfPayment = null;
        this.paymentReference = null;
        this.subscriptionRef = null;
        this.subscriptionId = null;
        this.startDate = null;
        this.endDate = null;
        this.subscriptionPeriod = null;
        this.vat = null;
        this.totalAmount = null;
    }
}
