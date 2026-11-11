
import {IndividualIdentityType} from '@models/bw/co/knowvera/individual/individual-identity-type';
import {PepStatus} from '@models/bw/co/knowvera/individual/pep-status';
import {RelatedPartyRole} from '@models/bw/co/knowvera/kyc/related-party-role';

export class RelatedParty {

    individualId: string | any;
    name: string | any;
    identityNo: string | any;
    identityType: IndividualIdentityType | any;
    nationality: string | any;
    role: RelatedPartyRole | any;
    ownershipPercentage: number | any;
    pepStatus: PepStatus | any;
    beneficialOwner: boolean | any = false;
    authorityReference: string | any;

    constructor() {
        this.individualId = null;
        this.name = null;
        this.identityNo = null;
        this.identityType = null;
        this.nationality = null;
        this.role = null;
        this.ownershipPercentage = null;
        this.pepStatus = null;
        this.beneficialOwner = false;
        this.authorityReference = null;
    }
}
