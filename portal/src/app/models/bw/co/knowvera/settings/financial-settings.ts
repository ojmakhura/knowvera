
import {SalaryRangeDTO} from '@models/bw/co/knowvera/settings/salary-range-dto';

export class FinancialSettings {

    salaryRanges: Array<SalaryRangeDTO> | any;
    vat: number | any;
    user: string | any;

    constructor() {
        this.salaryRanges = [];
        this.vat = null;
        this.user = null;
    }
}
