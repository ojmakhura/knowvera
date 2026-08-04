

export class SalaryRangeDTO {
    id: number | any;
    
    min: number | any;
    
    max: number | any;
    
    active: boolean | any = true;
    
    constructor() {
        this.id = null;
        this.min = null;
        this.max = null;
        this.active = true;
    }
}
