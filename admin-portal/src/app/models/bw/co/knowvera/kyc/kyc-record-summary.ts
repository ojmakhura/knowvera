
import {IndividualIdentityType} from '@models/bw/co/knowvera/individual/individual-identity-type';
import {SourceOfFunds} from '@models/bw/co/knowvera/source-of-funds';
import {KycComplianceStatus} from '@models/bw/co/knowvera/kyc/kyc-compliance-status';
import {TargetEntity} from '@models/bw/co/knowvera/target-entity';
import {PepStatus} from '@models/bw/co/knowvera/individual/pep-status';
import {PhoneNumber} from '@models/bw/co/knowvera/phone-number';
import {KeyValue} from '@models/bw/co/knowvera/key-value';

export class KycRecordSummary {
    ref: string | any;
    
    identityNo: string | any;
    
    name: string | any;
    
    identityType: IndividualIdentityType | any;
    
    emailAddress: string | any;
    
    postalAddress: string | any;
    
    physicalAddress: string | any;
    
    phoneNumbers: Array<PhoneNumber> | any;
    
    kycStatus: KycComplianceStatus | any;
    
    recordSummary: string | any;
    
    expiryDate: Date | any;
    
    pepStatus: PepStatus | any;
    
    pepDetails: string | any;
    
    sanctionsMatch: boolean | any = false;
    
    sanctionsDetails: string | any;
    
    documents: Array<KeyValue> | any;
    
    target: TargetEntity | any;
    
    sourceOfFundsDetails: string | any;
    
    sourceOfFunds: Array<SourceOfFunds> | any;
    
    dataVerifications: Array<KeyValue> | any;
    
    constructor() {
        this.ref = null;
        this.identityNo = null;
        this.name = null;
        this.identityType = null;
        this.emailAddress = null;
        this.postalAddress = null;
        this.physicalAddress = null;
        this.phoneNumbers = [];
        this.kycStatus = null;
        this.recordSummary = null;
        this.expiryDate = null;
        this.pepStatus = null;
        this.pepDetails = null;
        this.sanctionsMatch = false;
        this.sanctionsDetails = null;
        this.documents = [];
        this.target = null;
        this.sourceOfFundsDetails = null;
        this.sourceOfFunds = [];
        this.dataVerifications = [];
    }
}
