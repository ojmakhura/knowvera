
import {PepStatus} from '@models/bw/co/kyvera/individual/pep-status';

export class DeclarationDTO {
    pepStatus: PepStatus | any;
    
    pepDetails: string | any;
    
    sanctionsMatch: boolean | any = false;
    
    sanctionsDetails: string | any;
    
    constructor() {
        this.pepStatus = null;
        this.pepDetails = null;
        this.sanctionsMatch = false;
        this.sanctionsDetails = null;
    }
}
