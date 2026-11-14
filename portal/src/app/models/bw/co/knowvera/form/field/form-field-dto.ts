import {FormFieldType} from '@models/bw/co/knowvera/form/field/form-field-type';

export class FormFieldDTO {
    id: string | any;

    fieldId: string | any;

    label: string | any;

    description: string | any;

    fieldType: FormFieldType | any;

    required: boolean | any = false;

    defaultValue: string | any;

    placeholder: string | any;

    minValue: number | any;

    maxValue: number | any;

    minLength: number | any;

    maxLength: number | any;

    pattern: string | any;

    options: Array<string> | any;

    position: number | any;

    constructor() {
        this.id = null;
        this.fieldId = null;
        this.label = null;
        this.description = null;
        this.fieldType = null;
        this.defaultValue = null;
        this.placeholder = null;
        this.minValue = null;
        this.maxValue = null;
        this.minLength = null;
        this.maxLength = null;
        this.pattern = null;
        this.options = [];
        this.position = null;
    }
}
