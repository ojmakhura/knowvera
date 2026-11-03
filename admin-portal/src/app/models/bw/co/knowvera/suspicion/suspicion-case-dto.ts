import {AuditableDTO} from '@models/bw/co/knowvera/auditable-dto';

import {SuspicionTrigger} from '@models/bw/co/knowvera/suspicion/suspicion-trigger';
import {SuspicionCaseStatus} from '@models/bw/co/knowvera/suspicion/suspicion-case-status';

export class SuspicionCaseDTO extends AuditableDTO {

    ref: string | any;
    kycRecordId: string | any;
    subjectName: string | any;
    cause: SuspicionTrigger | any;
    status: SuspicionCaseStatus | any;
    grounds: string | any;
    suspicionDate: Date | any;
    reportDeadline: Date | any;
    notes: Array<string> | any;
    reportedAt: Date | any;
    fiaReference: string | any;
    outcome: string | any;

    constructor() {
        super();
        this.ref = null;
        this.kycRecordId = null;
        this.subjectName = null;
        this.cause = null;
        this.status = null;
        this.grounds = null;
        this.suspicionDate = null;
        this.reportDeadline = null;
        this.notes = [];
        this.reportedAt = null;
        this.fiaReference = null;
        this.outcome = null;
    }
}
