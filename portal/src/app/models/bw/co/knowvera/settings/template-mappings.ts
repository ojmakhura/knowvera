
import {DocumentTypeDTO} from '@models/bw/co/knowvera/document/type/document-type-dto';
import {DocumentDTO} from '@models/bw/co/knowvera/document/document-dto';

export class TemplateMappings {

    user: string | any;
    invoiceDocumentType: DocumentTypeDTO | any;
    invoiceTemplateType: DocumentTypeDTO | any;
    invoiceTemplate: DocumentDTO | any;
    quotationDocumentType: DocumentTypeDTO | any;
    quotationTemplateType: DocumentTypeDTO | any;
    quotationTemplate: DocumentDTO | any;

    constructor() {
        this.user = null;
        this.invoiceDocumentType = null;
        this.invoiceTemplateType = null;
        this.invoiceTemplate = null;
        this.quotationDocumentType = null;
        this.quotationTemplateType = null;
        this.quotationTemplate = null;
    }
}
