
import {ScreeningListSource} from '@models/bw/co/knowvera/screening/screening-list-source';

export class ScreeningListStatus {

    source: ScreeningListSource | any;
    listVersion: string | any;
    entryCount: number | any;
    importedAt: Date | any;

    constructor() {
        this.source = null;
        this.listVersion = null;
        this.entryCount = null;
        this.importedAt = null;
    }
}
