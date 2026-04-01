import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {DocumentValidationResults} from '@models/bw/co/centralkyc/document/document-validation-results';
import {VerificationTagResult} from '@models/bw/co/centralkyc/document/verification-tag-result';
import {DocumentVerificationStatus} from '@models/bw/co/centralkyc/document/document-verification-status';
import {CompletionRequestMessage} from '@models/bw/co/centralkyc/lmstudio/completion-request-message';
import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';
import {DocumentAnalyticsStatus} from '@models/bw/co/centralkyc/document/document-analytics-status';
import { VerificationTag } from '../kyc/verification/verification-tag';
import { DataComparisons } from './data-comparisons';

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
    
    expectedInformation: any | any;
    
    analyticsStatus: DocumentAnalyticsStatus | any = DocumentAnalyticsStatus.INITIALISED;
    
    verificationTagResults: Array<VerificationTagResult> | any;
    
    verificationTags: Array<VerificationTag> | any;
    
    dataComparisons: Array<DataComparisons> | any;
    
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
        this.expectedInformation = null;
        this.analyticsStatus = DocumentAnalyticsStatus.INITIALISED;
        this.verificationTagResults = [];
        this.verificationTags = [];
        this.dataComparisons = [];
    }
}
