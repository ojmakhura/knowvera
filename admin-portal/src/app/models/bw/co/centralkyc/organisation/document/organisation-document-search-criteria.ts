import { FormBuilder } from "@angular/forms";

import {OrganisationDocumentStatus} from '@models/bw/co/centralkyc/organisation/document/organisation-document-status';
import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';

export class OrganisationDocumentSearchCriteria {
    documentTypeId: string | any;
    
    documentType: string | any;
    
    status: OrganisationDocumentStatus | any;
    
    organisation: string | any;
    
    organisationId: string | any;
    
    organisationRegistrationNo: string | any;
    
    target: TargetEntity | any;
    
    constructor() {
        this.documentTypeId = null;
        this.documentType = null;
        this.status = null;
        this.organisation = null;
        this.organisationId = null;
        this.organisationRegistrationNo = null;
        this.target = null;
    }
}
