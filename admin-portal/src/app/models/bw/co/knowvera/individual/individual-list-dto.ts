
import {KycComplianceStatus} from '@models/bw/co/knowvera/kyc/kyc-compliance-status';
import {IndividualIdentityType} from '@models/bw/co/knowvera/individual/individual-identity-type';
import {Sex} from '@models/bw/co/knowvera/individual/sex';
import {PepStatus} from '@models/bw/co/knowvera/individual/pep-status';

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
    closedAt: Date | any;

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
        this.closedAt = null;
    }
}
