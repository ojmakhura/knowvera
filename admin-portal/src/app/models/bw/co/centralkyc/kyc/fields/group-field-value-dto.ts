import { ValueData } from './value-data';

export class GroupFieldValueDTO {

    position: number | any;
    fieldGroupId: string | any;
    fieldId: string | any;
    field: string | any;
    id: string | any;
    data: ValueData | any;
    reportSectionId: string | any;
    fieldLabel: string | any;
    
    constructor() {
        this.position = null;
        this.fieldGroupId = null;
        this.fieldId = null;
        this.field = null;
        this.id = null;
        this.data = null;
        this.reportSectionId = null;
        this.fieldLabel = null;
    }
}
