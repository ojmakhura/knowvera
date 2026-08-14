
import {GroupFieldValueDTO} from '@models/bw/co/knowvera/kyc/fields/group-field-value-dto';

export class KycReportSectionDTO {

    label: string | any;
    position: number | any;
    id: string | any;
    fieldValues: Array<GroupFieldValueDTO> | any;
    kycRecordId: string | any;
    kycRecordRef: string | any;
    
    constructor() {
        this.label = null;
        this.position = null;
        this.id = null;
        this.fieldValues = [];
        this.kycRecordId = null;
        this.kycRecordRef = null;
    }
}
