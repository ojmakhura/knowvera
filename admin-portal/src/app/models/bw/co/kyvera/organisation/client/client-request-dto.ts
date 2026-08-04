import {AuditableDTO} from '@models/bw/co/kyvera/auditable-dto';

import {IndividualIdentityType} from '@models/bw/co/kyvera/individual/individual-identity-type';
import {ClientRequestStatus} from '@models/bw/co/kyvera/organisation/client/client-request-status';
import {KycComplianceStatus} from '@models/bw/co/kyvera/kyc/kyc-compliance-status';
import {TargetEntity} from '@models/bw/co/kyvera/target-entity';

export class ClientRequestDTO extends AuditableDTO {
    name: string | any;
    
    registration: string | any;
    
    identityType: IndividualIdentityType | any;
    
    emailAddress: string | any;
    
    status: ClientRequestStatus | any = ClientRequestStatus.PENDING;
    
    organisationId: string | any;
    
    organisationCode: string | any;
    
    organisation: string | any;
    
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
        this.organisationCode = null;
        this.organisation = null;
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
