import {AuditableDTO} from '@models/bw/co/centralkyc/auditable-dto';

import {KycFieldGroupDTO} from '@models/bw/co/centralkyc/settings/kyc/kyc-field-group-dto';
import {DocumentDTO} from '@models/bw/co/centralkyc/document/document-dto';
import {DocumentTypeDTO} from '@models/bw/co/centralkyc/document/type/document-type-dto';
import {SalaryRangeDTO} from '@models/bw/co/centralkyc/settings/salary-range-dto';

export class SettingsDTO extends AuditableDTO {
    kycDuration: number | any;
    
    organisationDocuments: Array<DocumentTypeDTO> | any;
    
    individualDocuments: Array<DocumentTypeDTO> | any;
    
    indKycDocuments: Array<DocumentTypeDTO> | any;
    
    orgKycDocuments: Array<DocumentTypeDTO> | any;
    

    invoiceDocumentType: DocumentTypeDTO | any;

    invoiceTemplate: DocumentDTO | any;

    invoiceTemplateType: DocumentTypeDTO | any;

    quotationDocumentType: DocumentTypeDTO | any;

    quotationTemplateType: DocumentTypeDTO | any;

    quotationTemplate: DocumentDTO | any;

    clientRequestFileType: DocumentTypeDTO | any;
    salaryRanges: Array<SalaryRangeDTO> | any;
    
    platformName: string | any;
    
    platformUrl: string | any;
    
    supportContact: string | any;
    
    kycPortalLink: string | any;
    
    organisationAdminRole: string | any;
    
    normalUserRole: string | any;
    
    timeToAccountCreation: number | any;
    
    vat: number | any;
    
    documentDurationLimit: number | any;
    
    dataVerificationThreshold: number | any;
    
    maxDataVerificationFailureThreshold: number | any;
    
    individualKycFieldGroups: Array<KycFieldGroupDTO> | any;
    
    organisationKycFieldGroups: Array<KycFieldGroupDTO> | any;
    
    constructor() {
        super();
        this.kycDuration = null;
        this.organisationDocuments = [];
        this.individualDocuments = [];
        this.indKycDocuments = [];
        this.orgKycDocuments = [];
        this.invoiceDocumentType = null;
        this.invoiceTemplate = null;
        this.invoiceTemplateType = null;
        this.quotationDocumentType = null;
        this.quotationTemplateType = null;
        this.quotationTemplate = null;
        this.clientRequestFileType = null;
        this.salaryRanges = [];
        this.platformName = null;
        this.platformUrl = null;
        this.supportContact = null;
        this.kycPortalLink = null;
        this.organisationAdminRole = null;
        this.normalUserRole = null;
        this.timeToAccountCreation = null;
        this.vat = null;
        this.documentDurationLimit = null;
        this.dataVerificationThreshold = null;
        this.maxDataVerificationFailureThreshold = null;
        this.individualKycFieldGroups = [];
        this.organisationKycFieldGroups = [];
    }
}
