import { FormBuilder } from "@angular/forms";
import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {CompletionRequestMessage} from '@models/bw/co/centralkyc/lmstudio/completion-request-message';
import {ExpectedField} from '@models/bw/co/centralkyc/document/type/expected-field';

export class DocumentTypeDTO extends AuditableDTO {
    code: string | any;
    
    name: string | any;
    
    description: string | any;
    
    expectedFields: Array<ExpectedField> | any;
    
    validationPrompts: Array<CompletionRequestMessage> | any;
    
    textExtractionPrompts: Array<CompletionRequestMessage> | any;
    
    constructor() {
        super();
        this.code = null;
        this.name = null;
        this.description = null;
        this.expectedFields = [];
        this.validationPrompts = [];
        this.textExtractionPrompts = [];
    }
}
