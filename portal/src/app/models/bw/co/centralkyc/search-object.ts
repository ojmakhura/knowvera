
import {PropertySearchOrder} from '@models/bw/co/kyvera/property-search-order';

export class SearchObject<T> {
    criteria: T | any;
    
    paged: boolean | any = false;
    
    pageNumber: number | any;
    
    pageSize: number | any;
    
    sortings: Array<PropertySearchOrder> | any;
    
    constructor() {
        this.criteria = null;
        this.paged = false;
        this.pageNumber = null;
        this.pageSize = null;
        this.sortings = [];
    }
}
