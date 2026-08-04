import {AuditableDTO} from '@models/bw/co/kyvera/auditable-dto';

import {ExpectedFieldDTO} from '@models/bw/co/kyvera/document/type/field/expected-field-dto';
import {VerificationDataConfigDTO} from '@models/bw/co/kyvera/document/type/verification/verification-data-config-dto';
import {PromptMessage} from '@models/bw/co/kyvera/llm/prompt-message';
import {TimePeriod} from '@models/bw/co/kyvera/time-period';

export class DocumentTypeDTO extends AuditableDTO {
    code: string | any;
    
    name: string | any;
    
    description: string | any;
    
    expectedFields: Array<ExpectedFieldDTO> | any;
    
    validationPrompts: Array<PromptMessage> | any;
    
    textExtractionPrompts: Array<PromptMessage> | any;
    
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
        this.validationPrompts = [];
        this.textExtractionPrompts = [];
        this.verificationDataConfigs = [];
        this.expires = false;
        this.expiryField = null;
        this.expiryPeriod = null;
        this.expiresIn = null;
        this.expectedFieldCount = null;
        this.verificationConfigCount = null;
    }
}
