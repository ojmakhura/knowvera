
export class AuditLogCriteria {
    user: string | any;
    
    entityType: string | any;
    
    minDate: Date | any;
    
    maxDate: Date | any;
    
    constructor() {
        this.user = null;
        this.entityType = null;
        this.minDate = null;
        this.maxDate = null;
    }
}
