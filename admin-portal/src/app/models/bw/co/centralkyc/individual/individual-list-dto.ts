
import {KycComplianceStatus} from '@models/bw/co/centralkyc/kyc/kyc-compliance-status';
import {PepStatus} from '@models/bw/co/centralkyc/individual/pep-status';
import {Sex} from '@models/bw/co/centralkyc/individual/sex';
import {IndividualIdentityType} from '@models/bw/co/centralkyc/individual/individual-identity-type';

export class IndividualListDTO {
    id: string | any;
    
    name: string | any;
    
    identityNo: string | any;
    
    identityType: IndividualIdentityType | any;
    
    emailAddress: string | any;
    
    kycStatus: KycComplianceStatus | any;
    
    sex: Sex | any;
    
    pepStatus: PepStatus | any;
    
    userCreated: boolean | any = false;
    
    physicalAddress: string | any;
    
    postalAddress: string | any;
    
    constructor() {
        this.id = null;
        this.name = null;
        this.identityNo = null;
        this.identityType = null;
        this.emailAddress = null;
        this.kycStatus = null;
        this.sex = null;
        this.pepStatus = null;
        this.userCreated = false;
        this.physicalAddress = null;
        this.postalAddress = null;
    }
}
