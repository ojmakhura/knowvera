import {AuditableDTO} from '@models/bw/co/knowvera/auditable-dto';

import {DocumentListDTO} from '@models/bw/co/knowvera/document/document-list-dto';
import {KycComplianceStatus} from '@models/bw/co/knowvera/kyc/kyc-compliance-status';
import {DocumentDTO} from '@models/bw/co/knowvera/document/document-dto';
import {DocumentTypeDTO} from '@models/bw/co/knowvera/document/type/document-type-dto';
import {BranchDTO} from '@models/bw/co/knowvera/organisation/branch/branch-dto';
import {GeneralStatus} from '@models/bw/co/knowvera/general-status';
import {PhoneNumber} from '@models/bw/co/knowvera/phone-number';
import {OrganisationDomain} from '@models/bw/co/knowvera/organisation/organisation-domain';

export class OrganisationDTO extends AuditableDTO {
    code: string | any;
    
    name: string | any;
    
    description: string | any;
    
    documents: Array<DocumentListDTO> | any;
    
    registrationNo: string | any;
    
    phoneNumbers: Array<PhoneNumber> | any;
    
    physicalAddress: string | any;
    
    postalAddress: string | any;
    
    status: GeneralStatus | any;
    
    contactEmailAddress: string | any;
    
    clientKycDocuments: Array<DocumentTypeDTO> | any;
    
    isClient: boolean | any = false;
    
    clientRequestsFiles: Array<DocumentDTO> | any;
    
    domains: Array<OrganisationDomain> | any;
    
    kycStatus: KycComplianceStatus | any;
    
    countryOfRegistration: string | any;
    
    keycloakId: string | any;
    
    registrationDate: Date | any;
    
    branches: Array<BranchDTO> | any;
    
    constructor() {
        super();
        this.code = null;
        this.name = null;
        this.description = null;
        this.documents = [];
        this.registrationNo = null;
        this.phoneNumbers = [];
        this.physicalAddress = null;
        this.postalAddress = null;
        this.status = null;
        this.contactEmailAddress = null;
        this.clientKycDocuments = [];
        this.isClient = false;
        this.clientRequestsFiles = [];
        this.domains = [];
        this.kycStatus = null;
        this.countryOfRegistration = null;
        this.keycloakId = null;
        this.registrationDate = null;
        this.branches = [];
    }
}
