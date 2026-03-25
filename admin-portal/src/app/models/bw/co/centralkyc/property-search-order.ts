import { FormBuilder } from "@angular/forms";

import {SortOrder} from '@models/bw/co/centralkyc/sort-order';

export class PropertySearchOrder {
    propertyName: string | any;
    
    order: SortOrder | any;
    
    constructor() {
        this.propertyName = null;
        this.order = null;
    }
}
