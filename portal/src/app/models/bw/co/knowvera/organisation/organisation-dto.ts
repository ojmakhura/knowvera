import {AuditableDTO} from '@models/bw/co/knowvera/auditable-dto';

import {DocumentListDTO} from '@models/bw/co/knowvera/document/document-list-dto';
import {OrganisationLegalForm} from '@models/bw/co/knowvera/organisation/organisation-legal-form';
import {KycComplianceStatus} from '@models/bw/co/knowvera/kyc/kyc-compliance-status';
import {KycFieldGroupDTO} from '@models/bw/co/knowvera/settings/kyc/kyc-field-group-dto';
import {DocumentTypeDTO} from '@models/bw/co/knowvera/document/type/document-type-dto';
import {DocumentDTO} from '@models/bw/co/knowvera/document/document-dto';
import {BranchDTO} from '@models/bw/co/knowvera/organisation/branch/branch-dto';
import {OrganisationDomain} from '@models/bw/co/knowvera/organisation/organisation-domain';
import {PhoneNumber} from '@models/bw/co/knowvera/phone-number';
import {GeneralStatus} from '@models/bw/co/knowvera/general-status';

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
    organisationKycDocuments: Array<DocumentTypeDTO> | any;
    isClient: boolean | any = false;
    clientRequestsFiles: Array<DocumentDTO> | any;
    domains: Array<OrganisationDomain> | any;
    kycStatus: KycComplianceStatus | any;
    countryOfRegistration: string | any;
    keycloakId: string | any;
    registrationDate: Date | any;
    branches: Array<BranchDTO> | any;
    individualKycDocuments: Array<DocumentTypeDTO> | any;
    individualFieldGroups: Array<KycFieldGroupDTO> | any;
    organisationFieldGroups: Array<KycFieldGroupDTO> | any;
    tradingName: string | any;
    natureOfBusiness: string | any;
    taxNumber: string | any;
    vatNumber: string | any;
    legalForm: OrganisationLegalForm | any;
    closedAt: Date | any;
    retainUntil: Date | any;

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
        this.organisationKycDocuments = [];
        this.isClient = false;
        this.clientRequestsFiles = [];
        this.domains = [];
        this.kycStatus = null;
        this.countryOfRegistration = null;
        this.keycloakId = null;
        this.registrationDate = null;
        this.branches = [];
        this.individualKycDocuments = [];
        this.individualFieldGroups = [];
        this.organisationFieldGroups = [];
        this.tradingName = null;
        this.natureOfBusiness = null;
        this.taxNumber = null;
        this.vatNumber = null;
        this.legalForm = null;
        this.closedAt = null;
        this.retainUntil = null;
    }
}
