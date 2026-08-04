import {AuditableDTO} from '@models/bw/co/kyvera/auditable-dto';

import {IndividualIdentityType} from '@models/bw/co/kyvera/individual/individual-identity-type';
import {OrganisationListDTO} from '@models/bw/co/kyvera/organisation/organisation-list-dto';
import {DocumentListDTO} from '@models/bw/co/kyvera/document/document-list-dto';
import {KycComplianceStatus} from '@models/bw/co/kyvera/kyc/kyc-compliance-status';
import {MaritalStatus} from '@models/bw/co/kyvera/individual/marital-status';
import {BranchDTO} from '@models/bw/co/kyvera/organisation/branch/branch-dto';
import {KycRecordDTO} from '@models/bw/co/kyvera/kyc/kyc-record-dto';
import {EmploymentRecordDTO} from '@models/bw/co/kyvera/individual/employment/employment-record-dto';
import {PepStatus} from '@models/bw/co/kyvera/individual/pep-status';
import {PhoneNumber} from '@models/bw/co/kyvera/phone-number';
import {EmploymentStatus} from '@models/bw/co/kyvera/individual/employment-status';
import {Sex} from '@models/bw/co/kyvera/individual/sex';

export class IndividualDTO extends AuditableDTO {
    firstName: string | any;
    
    middleName: string | any;
    
    surname: string | any;
    
    identityNo: string | any;
    
    identityType: IndividualIdentityType | any;
    
    phoneNumbers: Array<PhoneNumber> | any;
    
    physicalAddress: string | any;
    
    postalAddress: string | any;
    
    emailAddress: string | any;
    
    hasUser: boolean | any = false;
    
    organisation: OrganisationListDTO | any;
    

    branch: BranchDTO | any;
    roles: Array<string> | any;
    

    latestKyc: KycRecordDTO | any;
    kycStatus: KycComplianceStatus | any;
    
    employmentRecords: Array<EmploymentRecordDTO> | any;
    
    username: string | any;
    
    sex: Sex | any;
    
    nationality: string | any;
    
    maritalStatus: MaritalStatus | any;
    
    employmentStatus: EmploymentStatus | any;
    
    pepStatus: PepStatus | any;
    
    userCreated: boolean | any = false;
    
    userId: string | any;
    
    dateOfBirth: Date | any;
    
    documents: Array<DocumentListDTO> | any;
    
    constructor() {
        super();
        this.firstName = null;
        this.middleName = null;
        this.surname = null;
        this.identityNo = null;
        this.identityType = null;
        this.phoneNumbers = [];
        this.physicalAddress = null;
        this.postalAddress = null;
        this.emailAddress = null;
        this.hasUser = false;
        this.organisation = null;
        this.branch = null;
        this.roles = [];
        this.latestKyc = null;
        this.kycStatus = null;
        this.employmentRecords = [];
        this.username = null;
        this.sex = null;
        this.nationality = null;
        this.maritalStatus = null;
        this.employmentStatus = null;
        this.pepStatus = null;
        this.userCreated = false;
        this.userId = null;
        this.dateOfBirth = null;
        this.documents = [];
    }
}
