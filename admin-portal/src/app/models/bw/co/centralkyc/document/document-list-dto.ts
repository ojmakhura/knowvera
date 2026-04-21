
import {DocumentAnalyticsStatus} from '@models/bw/co/centralkyc/document/document-analytics-status';
import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';
import {DocumentVerificationStatus} from '@models/bw/co/centralkyc/document/document-verification-status';

export class DocumentListDTO {
    id: string | any;
    
    target: TargetEntity | any;
    
    targetId: string | any;
    
    targetLabel: string | any;
    
    fileName: string | any;
    
    documentTypeId: string | any;
    
    documentType: string | any;
    
    analyticsStatus: DocumentAnalyticsStatus | any;
    
    verificationStatus: DocumentVerificationStatus | any = DocumentVerificationStatus.UNVERIFIED;
    
    constructor() {
        this.id = null;
        this.target = null;
        this.targetId = null;
        this.targetLabel = null;
        this.fileName = null;
        this.documentTypeId = null;
        this.documentType = null;
        this.analyticsStatus = null;
        this.verificationStatus = DocumentVerificationStatus.UNVERIFIED;
    }
}
