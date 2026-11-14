import {FormFieldDTO} from '@models/bw/co/knowvera/form/field/form-field-dto';

export class FormSectionDTO {
    id: string | any;

    sectionId: string | any;

    label: string | any;

    description: string | any;

    position: number | any;

    fields: Array<FormFieldDTO> | any;

    constructor() {
        this.id = null;
        this.sectionId = null;
        this.label = null;
        this.description = null;
        this.position = null;
        this.fields = [];
    }
}
