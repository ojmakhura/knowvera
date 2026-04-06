
import {PepStatus} from '@models/bw/co/centralkyc/individual/pep-status';

export class DeclarationDTO {
    pepStatus: PepStatus | any;

    pepDetails: string | any;

    sanctionsMatch: boolean | any = false;

    sanctionsDetails: string | any;

    constructor() {
        this.pepStatus = PepStatus.NOT_PEP;
        this.pepDetails = null;
        this.sanctionsMatch = false;
        this.sanctionsDetails = null;
    }
}
