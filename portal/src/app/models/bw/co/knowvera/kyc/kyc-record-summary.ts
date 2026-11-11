
import {TargetEntity} from '@models/bw/co/knowvera/target-entity';
import {SourceOfWealth} from '@models/bw/co/knowvera/source-of-wealth';
import {KeyValue} from '@models/bw/co/knowvera/key-value';
import {PhoneNumber} from '@models/bw/co/knowvera/phone-number';
import {IndividualIdentityType} from '@models/bw/co/knowvera/individual/individual-identity-type';
import {SourceOfFunds} from '@models/bw/co/knowvera/source-of-funds';
import {KycComplianceStatus} from '@models/bw/co/knowvera/kyc/kyc-compliance-status';
import {PepStatus} from '@models/bw/co/knowvera/individual/pep-status';
import {RelatedParty} from '@models/bw/co/knowvera/kyc/related-party';
import {RiskLevel} from '@models/bw/co/knowvera/kyc/risk-level';

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
    wealthSources: Array<SourceOfWealth> | any;
    sourceOfWealthDetails: string | any;
    createdBy: string | any;
    reviewedBy: string | any;
    reviewedAt: Date | any;
    relatedParties: Array<RelatedParty> | any;
    riskLevel: RiskLevel | any;
    riskFactors: Array<string> | any;
    approvedBy: string | any;
    approvedAt: Date | any;

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
        this.wealthSources = [];
        this.sourceOfWealthDetails = null;
        this.createdBy = null;
        this.reviewedBy = null;
        this.reviewedAt = null;
        this.relatedParties = [];
        this.riskLevel = null;
        this.riskFactors = [];
        this.approvedBy = null;
        this.approvedAt = null;
    }
}
