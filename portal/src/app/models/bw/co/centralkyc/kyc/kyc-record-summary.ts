
import {PhoneNumber} from '@models/bw/co/centralkyc/phone-number';
import {KycComplianceStatus} from '@models/bw/co/centralkyc/kyc/kyc-compliance-status';
import {KeyValue} from '@models/bw/co/centralkyc/key-value';
import {IndividualIdentityType} from '@models/bw/co/centralkyc/individual/individual-identity-type';
import {TargetEntity} from '@models/bw/co/centralkyc/target-entity';
import {PepStatus} from '@models/bw/co/centralkyc/individual/pep-status';
import {SourceOfFunds} from '@models/bw/co/centralkyc/source-of-funds';

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
