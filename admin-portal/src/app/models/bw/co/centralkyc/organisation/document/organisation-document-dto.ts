import { FormBuilder } from "@angular/forms";
import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {OrganisationDocumentStatus} from '@models/bw/co/centralkyc/organisation/document/organisation-document-status';
import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';

export class OrganisationDocumentDTO extends AuditableDTO {
    status: OrganisationDocumentStatus | any = OrganisationDocumentStatus.ACTIVE;
    
    organisationId: string | any;
    
    organisation: string | any;
    
    organisationRegistrationNo: string | any;
    
    documentTypeId: string | any;
    
    documentType: string | any;
    
    target: TargetEntity | any;
    
    targetId: string | any;
    
    fileName: string | any;
    
    metadata: any | any;
    
    url: string | any;
    
    constructor() {
        super();
        this.status = OrganisationDocumentStatus.ACTIVE;
        this.organisationId = null;
        this.organisation = null;
        this.organisationRegistrationNo = null;
        this.documentTypeId = null;
        this.documentType = null;
        this.target = null;
        this.targetId = null;
        this.fileName = null;
        this.metadata = null;
        this.url = null;
    }
}
