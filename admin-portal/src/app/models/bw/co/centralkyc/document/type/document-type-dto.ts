import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {KeyField} from '@models/bw/co/centralkyc/key-field';
import {PromptMessage} from '@models/bw/co/centralkyc/llm/prompt-message';
import {ExpectedFieldDTO} from '@models/bw/co/centralkyc/document/type/field/expected-field-dto';
import {VerificationDataConfigDTO} from '@models/bw/co/centralkyc/document/type/verification/verification-data-config-dto';

export class DocumentTypeDTO extends AuditableDTO {
    code: string | any;
    
    name: string | any;
    
    description: string | any;
    
    expectedFields: Array<ExpectedFieldDTO> | any;
    
    validationPrompts: Array<PromptMessage> | any;
    
    textExtractionPrompts: Array<PromptMessage> | any;
    
    verificationDataConfigs: Array<VerificationDataConfigDTO> | any;
    
    expires: boolean | any = false;
    
    expiryField: KeyField | any;
    
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
    }
}
