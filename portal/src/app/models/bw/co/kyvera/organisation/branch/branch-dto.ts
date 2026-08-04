import {AuditableDTO} from '@models/bw/co/kyvera/auditable-dto';

import {DocumentListDTO} from '@models/bw/co/kyvera/document/document-list-dto';

export class BranchDTO extends AuditableDTO {
    code: string | any;
    
    name: string | any;
    
    description: string | any;
    
    physicalAddress: string | any;
    
    organisationId: string | any;
    
    organisation: string | any;
    
    documents: Array<DocumentListDTO> | any;
    
    constructor() {
        super();
        this.code = null;
        this.name = null;
        this.description = null;
        this.physicalAddress = null;
        this.organisationId = null;
        this.organisation = null;
        this.documents = [];
    }
}
