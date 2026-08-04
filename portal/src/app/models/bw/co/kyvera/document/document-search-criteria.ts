
import {DocumentVerificationStatus} from '@models/bw/co/kyvera/document/document-verification-status';
import {TargetEntity} from '@models/bw/co/kyvera/target-entity';

export class DocumentSearchCriteria {
    target: TargetEntity | any;
    
    documentTypeId: string | any;
    
    documentType: string | any;
    
    targetId: string | any;
    
    fileName: string | any;
    
    verificationStatus: DocumentVerificationStatus | any = DocumentVerificationStatus.UNVERIFIED;
    
    constructor() {
        this.target = null;
        this.documentTypeId = null;
        this.documentType = null;
        this.targetId = null;
        this.fileName = null;
        this.verificationStatus = DocumentVerificationStatus.UNVERIFIED;
    }
}
