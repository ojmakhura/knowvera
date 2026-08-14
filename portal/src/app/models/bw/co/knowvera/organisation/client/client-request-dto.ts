import {AuditableDTO} from '@models/bw/co/knowvera/auditable-dto';

import {IndividualIdentityType} from '@models/bw/co/knowvera/individual/individual-identity-type';
import {ClientRequestStatus} from '@models/bw/co/knowvera/organisation/client/client-request-status';
import {KycComplianceStatus} from '@models/bw/co/knowvera/kyc/kyc-compliance-status';
import {TargetEntity} from '@models/bw/co/knowvera/target-entity';

export class ClientRequestDTO extends AuditableDTO {
    name: string | any;
    
    registration: string | any;
    
    identityType: IndividualIdentityType | any;
    
    emailAddress: string | any;
    
    status: ClientRequestStatus | any = ClientRequestStatus.PENDING;
    
    organisationId: string | any;
    
    organisation: string | any;

    organisationCode: string | any;
    
    organisationRegistrationNo: string | any;
    
    documentId: string | any;
    
    fileName: string | any;
    
    fileUrl: string | any;
    
    documentTypeId: string | any;
    
    documentType: string | any;
    
    target: TargetEntity | any;
    
    targetId: string | any;
    
    targetKycStatus: KycComplianceStatus | any;
    
    ref: string | any;
    
    constructor() {
        super();
        this.name = null;
        this.registration = null;
        this.identityType = null;
        this.emailAddress = null;
        this.status = ClientRequestStatus.PENDING;
        this.organisationId = null;
        this.organisation = null;
        this.organisationCode = null;
        this.organisationRegistrationNo = null;
        this.documentId = null;
        this.fileName = null;
        this.fileUrl = null;
        this.documentTypeId = null;
        this.documentType = null;
        this.target = null;
        this.targetId = null;
        this.targetKycStatus = null;
        this.ref = null;
    }
}
