
export class AuditLogDTO {
    id: string | any;
    
    username: string | any;
    
    timestamp: Date | any;
    
    event: string | any;
    
    ipAddress: string | any;
    
    agent: string | any;
    
    eventLabel: string | any;
    
    logData: Record<string, any> | any;
    
    entityType: string | any;
    
    userId: string | any;
    
    traceId: string | any;
    
    spanId: string | any;
    
    constructor() {
        this.id = null;
        this.username = null;
        this.timestamp = null;
        this.event = null;
        this.ipAddress = null;
        this.agent = null;
        this.eventLabel = null;
        this.logData = null;
        this.entityType = null;
        this.userId = null;
        this.traceId = null;
        this.spanId = null;
    }
}
