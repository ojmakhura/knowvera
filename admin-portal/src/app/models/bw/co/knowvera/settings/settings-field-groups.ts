
import {KycFieldGroupDTO} from '@models/bw/co/knowvera/settings/kyc/kyc-field-group-dto';

export class SettingsFieldGroups {

    organisationKycFieldGroups: Array<KycFieldGroupDTO> | any;
    individualKycFieldGroups: Array<KycFieldGroupDTO> | any;
    user: string | any;

    constructor() {
        this.organisationKycFieldGroups = [];
        this.individualKycFieldGroups = [];
        this.user = null;
    }
}
