import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {GeneralStatus} from '@models/bw/co/centralkyc/general-status';
import {DocumentDTO} from '@models/bw/co/centralkyc/document/document-dto';
import {OrganisationDomain} from '@models/bw/co/centralkyc/organisation/organisation-domain';
import {KycComplianceStatus} from '@models/bw/co/centralkyc/kyc/kyc-compliance-status';
import {PhoneNumber} from '@models/bw/co/centralkyc/phone-number';
import {DocumentTypeDTO} from '@models/bw/co/centralkyc/document/type/document-type-dto';

export class OrganisationDTO extends AuditableDTO {
    code: string | any;
    
    name: string | any;
    
    description: string | any;
    
    documents: Array<DocumentDTO> | any;
    
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
    }
}
