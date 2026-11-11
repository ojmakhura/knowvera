import {AuditableDTO} from '@models/bw/co/knowvera/auditable-dto';

import {ExpectedFieldDTO} from '@models/bw/co/knowvera/document/type/field/expected-field-dto';
import {VerificationDataConfigDTO} from '@models/bw/co/knowvera/document/type/verification/verification-data-config-dto';
import {TimePeriod} from '@models/bw/co/knowvera/time-period';

export class DocumentTypeDTO extends AuditableDTO {
    code: string | any;
    
    name: string | any;
    
    description: string | any;
    
    expectedFields: Array<ExpectedFieldDTO> | any;
    
    processingProfileCode: string | any;
    
    verificationDataConfigs: Array<VerificationDataConfigDTO> | any;
    
    expires: boolean | any = false;
    

    expiryField: ExpectedFieldDTO | any;
    expiryPeriod: TimePeriod | any;
    
    expiresIn: number | any;
    
    expectedFieldCount: number | any;
    
    verificationConfigCount: number | any;
    
    constructor() {
        super();
        this.code = null;
        this.name = null;
        this.description = null;
        this.expectedFields = [];
        this.processingProfileCode = null;
        this.verificationDataConfigs = [];
        this.expires = false;
        this.expiryField = null;
        this.expiryPeriod = null;
        this.expiresIn = null;
        this.expectedFieldCount = null;
        this.verificationConfigCount = null;
    }
}
