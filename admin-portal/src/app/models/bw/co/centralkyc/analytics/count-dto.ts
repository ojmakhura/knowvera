import { FormBuilder } from "@angular/forms";


export class CountDTO {
    organisationCount: number | any = 0;
    
    individualCount: number | any = 0;
    
    requestCount: number | any = 0;
    
    compliantCount: number | any = 0;
    
    pepCount: number | any = 0;
    
    pepRelativeCount: number | any = 0;
    
    pepAssociateCount: number | any = 0;
    
    kycComplianceExpiredCount: number | any = 0;
    
    kycComplianceAbsentCount: number | any = 0;
    
    kycComplianceIncompleteCount: number | any = 0;
    
    subscriptionCountActive: number | any = 0;
    
    subscriptionCountInactive: number | any = 0;
    
    subscriptionCountCancelled: number | any = 0;
    
    paidInvoicesCount: number | any = 0;
    
    unpaidInvoicesCount: number | any = 0;
    
    invoicesCount: number | any = 0;
    
    subscriptionCount: number | any = 0;
    
    constructor() {
        this.organisationCount = 0;
        this.individualCount = 0;
        this.requestCount = 0;
        this.compliantCount = 0;
        this.pepCount = 0;
        this.pepRelativeCount = 0;
        this.pepAssociateCount = 0;
        this.kycComplianceExpiredCount = 0;
        this.kycComplianceAbsentCount = 0;
        this.kycComplianceIncompleteCount = 0;
        this.subscriptionCountActive = 0;
        this.subscriptionCountInactive = 0;
        this.subscriptionCountCancelled = 0;
        this.paidInvoicesCount = 0;
        this.unpaidInvoicesCount = 0;
        this.invoicesCount = 0;
        this.subscriptionCount = 0;
    }
}
