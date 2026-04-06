
import {KycComplianceStatus} from '@models/bw/co/centralkyc/kyc/kyc-compliance-status';

export class KycRecordListDTO {
    id: string | any;
    
    ref: string | any;
    
    identityNo: string | any;
    
    name: string | any;
    
    kycStatus: KycComplianceStatus | any;
    
    expiryDate: Date | any;
    
    constructor() {
        this.id = null;
        this.ref = null;
        this.identityNo = null;
        this.name = null;
        this.kycStatus = null;
        this.expiryDate = null;
    }
}
