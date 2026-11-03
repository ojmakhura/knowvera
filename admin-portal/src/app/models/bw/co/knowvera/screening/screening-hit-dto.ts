import {AuditableDTO} from '@models/bw/co/knowvera/auditable-dto';

import {ScreeningListSource} from '@models/bw/co/knowvera/screening/screening-list-source';
import {ScreeningHitStatus} from '@models/bw/co/knowvera/screening/screening-hit-status';

export class ScreeningHitDTO extends AuditableDTO {

    kycRecordId: string | any;
    subjectName: string | any;
    subjectRole: string | any;
    source: ScreeningListSource | any;
    entryReference: string | any;
    entryName: string | any;
    score: number | any;
    status: ScreeningHitStatus | any;
    listVersion: string | any;
    reviewedBy: string | any;
    reviewedAt: Date | any;
    reviewNotes: string | any;

    constructor() {
        super();
        this.kycRecordId = null;
        this.subjectName = null;
        this.subjectRole = null;
        this.source = null;
        this.entryReference = null;
        this.entryName = null;
        this.score = null;
        this.status = null;
        this.listVersion = null;
        this.reviewedBy = null;
        this.reviewedAt = null;
        this.reviewNotes = null;
    }
}
