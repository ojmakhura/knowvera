import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {ExpectedField} from '@models/bw/co/centralkyc/document/type/expected-field';
import {CompletionRequestMessage} from '@models/bw/co/centralkyc/lmstudio/completion-request-message';
import {VerificationTag} from '@models/bw/co/centralkyc/kyc/verification/verification-tag';

export class DocumentTypeDTO extends AuditableDTO {
    code: string | any;
    
    name: string | any;
    
    description: string | any;
    
    expectedFields: Array<ExpectedField> | any;
    
    validationPrompts: Array<CompletionRequestMessage> | any;
    
    textExtractionPrompts: Array<CompletionRequestMessage> | any;
    
    verificationTags: Array<VerificationTag> | any;
    
    constructor() {
        super();
        this.code = null;
        this.name = null;
        this.description = null;
        this.expectedFields = [];
        this.validationPrompts = [];
        this.textExtractionPrompts = [];
        this.verificationTags = [];
    }
}
