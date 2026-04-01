import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {SalaryRangeDTO} from '@models/bw/co/centralkyc/settings/salary-range-dto';
import {KycRecordDTO} from '@models/bw/co/centralkyc/kyc/kyc-record-dto';
import {OrganisationListDTO} from '@models/bw/co/centralkyc/organisation/organisation-list-dto';

export class EmploymentRecordDTO extends AuditableDTO {
    kycRecords: Array<KycRecordDTO> | any;
    
    positions: Array<string> | any;
    
    employmentStart: Date | any;
    
    employmentEnd: Date | any;
    
    name: string | any;
    
    identityNo: string | any;
    
    individualId: string | any;
    
    employer: OrganisationListDTO | any;
    

    salaryRange: SalaryRangeDTO | any;
    constructor() {
        super();
        this.kycRecords = [];
        this.positions = [];
        this.employmentStart = null;
        this.employmentEnd = null;
        this.name = null;
        this.identityNo = null;
        this.individualId = null;
        this.employer = null;
        this.salaryRange = null;
    }
}
