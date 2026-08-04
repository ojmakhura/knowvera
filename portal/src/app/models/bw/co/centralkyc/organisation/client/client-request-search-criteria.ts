
import {ClientRequestStatus} from '@models/bw/co/kyvera/organisation/client/client-request-status';
import {TargetEntity} from '@models/bw/co/kyvera/target-entity';

export class ClientRequestSearchCriteria {
    emailAddress: string | any;
    
    name: string | any;
    
    request: ClientRequestStatus | any;
    
    organisation: string | any;
    
    organisationId: string | any;
    
    organisationRegistrationNo: string | any;
    
    registration: string | any;
    
    target: TargetEntity | any;
    
    targetId: string | any;
    
    statuses: Array<ClientRequestStatus> | any;
    
    constructor() {
        this.emailAddress = null;
        this.name = null;
        this.request = null;
        this.organisation = null;
        this.organisationId = null;
        this.organisationRegistrationNo = null;
        this.registration = null;
        this.target = null;
        this.targetId = null;
        this.statuses = [];
    }
}
