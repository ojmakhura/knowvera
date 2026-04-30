import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {DataVerification} from '@models/bw/co/centralkyc/document/data-verification';
import {VerificationDataConfigDTO} from '@models/bw/co/centralkyc/document/type/verification/verification-data-config-dto';
import {PromptMessage} from '@models/bw/co/centralkyc/llm/prompt-message';
import {DocumentVerificationStatus} from '@models/bw/co/centralkyc/document/document-verification-status';
import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';
import {DocumentAnalyticsStatus} from '@models/bw/co/centralkyc/document/document-analytics-status';
import {DataComparisons} from '@models/bw/co/centralkyc/document/data-comparisons';
import {DocumentValidationResults} from '@models/bw/co/centralkyc/document/document-validation-results';

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
    
    validationPrompts: Array<PromptMessage> | any;
    
    textExtractionPrompts: Array<PromptMessage> | any;
    
    targetLabel: string | any;
    
    expectedInformation: any | any;
    
    analyticsStatus: DocumentAnalyticsStatus | any = DocumentAnalyticsStatus.INITIALISED;
    
    dataVerifications: Array<DataVerification> | any;
    
    dataComparisons: Array<DataComparisons> | any;
    
    verificationDataConfigs: Array<VerificationDataConfigDTO> | any;
    
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
        this.dataVerifications = [];
        this.dataComparisons = [];
        this.verificationDataConfigs = [];
    }
}
