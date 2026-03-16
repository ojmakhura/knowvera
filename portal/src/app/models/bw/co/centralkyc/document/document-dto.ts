import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {CompletionRequestMessage} from '@models/bw/co/centralkyc/lmstudio/completion-request-message';
import {DocumentValidationResults} from '@models/bw/co/centralkyc/document/document-validation-results';
import {DocumentVerificationStatus} from '@models/bw/co/centralkyc/document/document-verification-status';
import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';

export class DocumentDTO extends AuditableDTO {
    target: TargetEntity | any;

    documentTypeId: string | any;

    documentType: string | any;

    url: string | any;

    targetId: string | any;

    metadata: any | any;

    fileName: string | any;

    fileContent: string | any;

    extractedInformation: any | any;

    expectedFields: any | any;


    validationResults: DocumentValidationResults | any;
    verificationStatus: DocumentVerificationStatus | any = DocumentVerificationStatus.UNVERIFIED;

    validationPrompts: Array<CompletionRequestMessage> | any;

    textExtractionPrompts: Array<CompletionRequestMessage> | any;

    targetLabel: string | any;

    constructor() {
        super();
        this.target = null;
        this.documentTypeId = null;
        this.documentType = null;
        this.url = null;
        this.targetId = null;
        this.metadata = null;
        this.fileName = null;
        this.fileContent = null;
        this.extractedInformation = null;
        this.expectedFields = null;
        this.validationResults = null;
        this.verificationStatus = DocumentVerificationStatus.UNVERIFIED;
        this.validationPrompts = [];
        this.textExtractionPrompts = [];
        this.targetLabel = null;
    }
}
