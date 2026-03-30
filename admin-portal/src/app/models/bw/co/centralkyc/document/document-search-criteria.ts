import { FormBuilder } from "@angular/forms";

import { TargetEntity } from '@models/bw/co/centralkyc/target-entity';
import { DocumentVerificationStatus } from '@models/bw/co/centralkyc/document/document-verification-status';
import { DocumentAnalyticsStatus } from "./document-analytics-status";

export class DocumentSearchCriteria {
  target: TargetEntity | any;

  documentTypeId: string | any;

  documentType: string | any;

  targetId: string | any;

  fileName: string | any;

  verificationStatus: DocumentVerificationStatus | any;

  analyticsStatus: DocumentAnalyticsStatus | any;

  constructor() {
    this.target = null;
    this.documentTypeId = null;
    this.documentType = null;
    this.targetId = null;
    this.fileName = null;
    this.verificationStatus = null;
    this.analyticsStatus = null;
  }
}
