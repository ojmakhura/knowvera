
import {DocumentVerificationStatus} from '@models/bw/co/kyvera/document/document-verification-status';
import {TargetEntity} from '@models/bw/co/kyvera/target-entity';
import {DocumentAnalyticsStatus} from '@models/bw/co/kyvera/document/document-analytics-status';

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
