import { FormBuilder } from "@angular/forms";

import {PropertySearchOrder} from '@models/bw/co/centralkyc/property-search-order';

export class SearchObject {
    criteria?: any;

    paged?: boolean = false;

    pageNumber?: number;

    pageSize?: number;

    sortings?: Array<PropertySearchOrder>;

    constructor() {
    }
}
