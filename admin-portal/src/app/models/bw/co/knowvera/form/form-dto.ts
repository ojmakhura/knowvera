import {AuditableDTO} from '@models/bw/co/knowvera/auditable-dto';

import {TargetEntity} from '@models/bw/co/knowvera/target-entity';

import {FormStatus} from '@models/bw/co/knowvera/form/form-status';

import {FormSectionDTO} from '@models/bw/co/knowvera/form/section/form-section-dto';

export class FormDTO extends AuditableDTO {
    code: string | any;

    name: string | any;

    description: string | any;

    targetType: TargetEntity | any;

    status: FormStatus | any;

    organisationId: string | any;

    organisation: string | any;

    sections: Array<FormSectionDTO> | any;

    constructor() {
        super();
        this.code = null;
        this.name = null;
        this.description = null;
        this.targetType = null;
        this.status = null;
        this.organisationId = null;
        this.organisation = null;
        this.sections = [];
    }
}
