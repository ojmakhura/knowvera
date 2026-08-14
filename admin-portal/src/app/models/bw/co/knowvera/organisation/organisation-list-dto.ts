
import {KycComplianceStatus} from '@models/bw/co/knowvera/kyc/kyc-compliance-status';
import {GeneralStatus} from '@models/bw/co/knowvera/general-status';

export class OrganisationListDTO {
    id: string | any;
    
    code: string | any;
    
    name: string | any;
    
    registrationNo: string | any;
    
    status: GeneralStatus | any;
    
    contactEmailAddress: string | any;
    
    kycStatus: KycComplianceStatus | any;
    
    isClient: boolean | any = false;
    
    keycloakId: string | any;
    
    postalAddress: string | any;
    
    physicalAddress: string | any;
    
    constructor() {
        this.id = null;
        this.code = null;
        this.name = null;
        this.registrationNo = null;
        this.status = null;
        this.contactEmailAddress = null;
        this.kycStatus = null;
        this.isClient = false;
        this.keycloakId = null;
        this.postalAddress = null;
        this.physicalAddress = null;
    }
}
