import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {DocumentListDTO} from '@models/bw/co/centralkyc/document/document-list-dto';
import {PhoneNumber} from '@models/bw/co/centralkyc/phone-number';
import {OrganisationDomain} from '@models/bw/co/centralkyc/organisation/organisation-domain';
import {DocumentDTO} from '@models/bw/co/centralkyc/document/document-dto';
import {GeneralStatus} from '@models/bw/co/centralkyc/general-status';
import {BranchDTO} from '@models/bw/co/centralkyc/organisation/branch/branch-dto';
import {DocumentTypeDTO} from '@models/bw/co/centralkyc/document/type/document-type-dto';
import {KycComplianceStatus} from '@models/bw/co/centralkyc/kyc/kyc-compliance-status';

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
