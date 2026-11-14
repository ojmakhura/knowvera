import {AuditableDTO} from '@models/bw/co/knowvera/auditable-dto';

import {ExpectedFieldDTO} from '@models/bw/co/knowvera/document/type/field/expected-field-dto';
import {VerificationDataConfigDTO} from '@models/bw/co/knowvera/document/type/verification/verification-data-config-dto';
import {TimePeriod} from '@models/bw/co/knowvera/time-period';
import {PromptMessage} from '@models/bw/co/knowvera/llm/prompt-message';

export class DocumentTypeDTO extends AuditableDTO {
    code: string | any;
    
    name: string | any;
    
    description: string | any;
    
    expectedFields: Array<ExpectedFieldDTO> | any;
    
    processingProfileCode: string | any;

    /** This type's own classification prompts (a system and a user message); empty: the profile's. */
    validationPrompts: Array<PromptMessage> | any;

    /** This type's own text extraction prompts; empty: the profile's. */
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
        this.processingProfileCode = null;
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
