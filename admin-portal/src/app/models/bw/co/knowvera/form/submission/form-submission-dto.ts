import {AuditableDTO} from '@models/bw/co/knowvera/auditable-dto';

import {TargetEntity} from '@models/bw/co/knowvera/target-entity';

import {FormDTO} from '@models/bw/co/knowvera/form/form-dto';

import {FormFieldValueDTO} from '@models/bw/co/knowvera/form/submission/form-field-value-dto';

import {FormSubmissionStatus} from '@models/bw/co/knowvera/form/submission/form-submission-status';

export class FormSubmissionDTO extends AuditableDTO {
    formId: string | any;

    formCode: string | any;

    formName: string | any;

    organisationId: string | any;

    organisation: string | any;

    kycRecordId: string | any;

    kycRecordRef: string | any;

    clientName: string | any;

    target: TargetEntity | any;

    targetId: string | any;

    status: FormSubmissionStatus | any;

    dueDate: string | any;

    submittedBy: string | any;

    submittedAt: Date | any;

    reviewedBy: string | any;

    reviewedAt: Date | any;

    reviewNote: string | any;

    values: Array<FormFieldValueDTO> | any;

    form: FormDTO | any;

    constructor() {
        super();
        this.formId = null;
        this.formCode = null;
        this.formName = null;
        this.organisationId = null;
        this.organisation = null;
        this.kycRecordId = null;
        this.kycRecordRef = null;
        this.clientName = null;
        this.target = null;
        this.targetId = null;
        this.status = null;
        this.dueDate = null;
        this.submittedBy = null;
        this.submittedAt = null;
        this.reviewedBy = null;
        this.reviewedAt = null;
        this.reviewNote = null;
        this.values = [];
        this.form = null;
    }
}
