
import {DocumentTypeDTO} from '@models/bw/co/knowvera/document/type/document-type-dto';

export class DocumentRequirements {

    organisationDocuments: Array<DocumentTypeDTO> | any;
    individualDocuments: Array<DocumentTypeDTO> | any;
    indKycDocuments: Array<DocumentTypeDTO> | any;
    orgKycDocuments: Array<DocumentTypeDTO> | any;
    clientRequestFileType: DocumentTypeDTO | any;
    user: string | any;
    maxDataVerificationFailureThreshold: number | any;
    dataVerificationThreshold: number | any;
    documentDurationLimit: number | any;

    constructor() {
        this.organisationDocuments = [];
        this.individualDocuments = [];
        this.indKycDocuments = [];
        this.orgKycDocuments = [];
        this.clientRequestFileType = null;
        this.user = null;
        this.maxDataVerificationFailureThreshold = null;
        this.dataVerificationThreshold = null;
        this.documentDurationLimit = null;
    }
}
