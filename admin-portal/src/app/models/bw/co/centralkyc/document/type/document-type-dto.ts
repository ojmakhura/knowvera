import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {VerificationDataConfigDTO} from '@models/bw/co/centralkyc/document/type/verification/verification-data-config-dto';
import {ExpectedFieldDTO} from '@models/bw/co/centralkyc/document/type/field/expected-field-dto';
import {VerificationTag} from '@models/bw/co/centralkyc/kyc/verification/verification-tag';
import {CompletionRequestMessage} from '@models/bw/co/centralkyc/lmstudio/completion-request-message';

export class DocumentTypeDTO extends AuditableDTO {
    code: string | any;
    
    name: string | any;
    
    description: string | any;
    
    expectedFields: Array<ExpectedFieldDTO> | any;
    
    validationPrompts: Array<CompletionRequestMessage> | any;
    
    textExtractionPrompts: Array<CompletionRequestMessage> | any;
    
    verificationTags: Array<VerificationTag> | any;
    
    verificationDataConfigs: Array<VerificationDataConfigDTO> | any;
    
    constructor() {
        super();
        this.code = null;
        this.name = null;
        this.description = null;
        this.expectedFields = [];
        this.validationPrompts = [];
        this.textExtractionPrompts = [];
        this.verificationTags = [];
        this.verificationDataConfigs = [];
    }
}
