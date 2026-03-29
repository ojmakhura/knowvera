import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  Input,
  linkedSignal,
  OnInit,
  signal,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { Loader } from '@app/@shared/loader/loader';
import { disabled, form, FormField, readonly, required } from '@angular/forms/signals';
import { KycRecordDTO } from '@app/models/bw/co/centralkyc/kyc/kyc-record-dto';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { KycRecordApiStore } from '@app/store/bw/co/centralkyc/kyc/kyc-record-api.store';
import { DeclarationDTO } from '@app/models/bw/co/centralkyc/kyc/declaration-dto';
import { PepStatus } from '@app/models/bw/co/centralkyc/individual/pep-status';
import { KycComplianceStatus } from '@app/models/bw/co/centralkyc/kyc/kyc-compliance-status';
import { SourceOfFunds } from '@app/models/bw/co/centralkyc/source-of-funds';
import { IndividualIdentityType } from '@app/models/bw/co/centralkyc/individual/individual-identity-type';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { DocumentTypeDTO } from '@app/models/bw/co/centralkyc/document/type/document-type-dto';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { IndividualApiStore } from '@app/store/bw/co/centralkyc/individual/individual-api.store';
import { SearchObject } from '@app/models/search-object';
import { OrganisationSearchCriteria } from '@app/models/bw/co/centralkyc/organisation/organisation-search-criteria';
import { IndividualSearchCriteria } from '@app/models/bw/co/centralkyc/individual/individual-search-criteria';
import { QuillEditorComponent, QuillModule } from 'ngx-quill';
import { OwnerDetails } from '@app/models/bw/co/centralkyc/kyc/owner-details';
import { KycVerificationDTO } from '@app/models/bw/co/centralkyc/kyc/verification/kyc-verification-dto';
import { EmploymentRecordDTO } from '@app/models/bw/co/centralkyc/individual/employment/employment-record-dto';

type QueuedDocumentUpload = {
  file: File;
  documentTypeId: string | null;
};

export class EditRecordVarsForm {
  id: string | any = null;
  createdAt: Date | any = null;
  createdBy: string | any = null;
  modifiedAt: Date | any = null;
  modifiedBy: string | any = null;
  ref: string | any = null;
  target: TargetEntity | any = TargetEntity.INDIVIDUAL;
  targetId: string | any = null;
  ownerDetails: OwnerDetails = new OwnerDetails();
  recordOwnerFilter: any = null;
  expiryDate: Date | any = null;
  uploadDate: Date | any = null;
  kycStatus: KycComplianceStatus | any = null;
  declaration: DeclarationDTO | any = null;
  sourceOfFunds: SourceOfFunds[] | any = [];
  sourceOfFundsDetails: string | any = null;
  documents: DocumentDTO[] | any = [];
  documentsToUpload: QueuedDocumentUpload[] = [];
  kycVerification: KycVerificationDTO | any = new KycVerificationDTO();
  employmentRecord: EmploymentRecordDTO | any = new EmploymentRecordDTO();

  constructor() {
    this.declaration = new DeclarationDTO();
  }
}

@Component({
  selector: 'app-record-edit',
  templateUrl: './record-edit.html',
  styleUrls: ['./record-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatButtonToggleModule,
    TranslateModule,
    Loader,
    FormField,
    MatDatepickerModule,
    MatListModule,
    MatProgressBarModule,
    NgxMatSelectSearchModule,
    QuillEditorComponent
  ],
})
export class RecordEdit implements OnInit {
  editRecordVarsForm: EditRecordVarsForm = new EditRecordVarsForm();
  editRecordSignal = signal(this.editRecordVarsForm);
  editRecordSignalForm = form(this.editRecordSignal, (path) => {
    disabled(path.ref);
    disabled(path.uploadDate);
    disabled(path.target);
    required(path.ownerDetails.name, { message: 'record.name.required' });

    // Conditionally require identity fields only for individuals
    // const target = this.editRecordSignal().target;
    // if (target === TargetEntity.INDIVIDUAL) {
    //   required(path.identityNo, { message: 'record.identity.required' });
    //   required(path.identityType, { message: 'record.identity.type.required' });
    // }

    disabled(path.ownerDetails.identityNo);
    disabled(path.ownerDetails.identityType);
  });

  protected route: ActivatedRoute = inject(ActivatedRoute);
  protected router: Router = inject(Router);
  toaster: ToastrService = inject(ToastrService);
  readonly kycRecordApiStore = inject(KycRecordApiStore);
  readonly settingsApiStore = inject(SettingsApiStore);
  readonly organisationApiStore = inject(OrganisationApiStore);
  readonly individualApiStore = inject(IndividualApiStore);

  readonly pepStatusOptions = Object.values(PepStatus);
  readonly kycStatusOptions = Object.values(KycComplianceStatus);
  readonly sourceOfFundsOptions = Object.values(SourceOfFunds);
  readonly identityTypeOptions = Object.values(IndividualIdentityType);
  readonly TargetEntity = TargetEntity;  // Export enum for template use

  loaderMessage = signal('');
  messages = linkedSignal(() => this.kycRecordApiStore.messages());
  loading = linkedSignal(() => this.kycRecordApiStore.loading());
  private readonly saveRequested = signal(false);
  readonly allowedDocumentTypes = signal<DocumentTypeDTO[]>([]);
  private readonly quillEditors = new Map<string, any>();

  ownerOptions = linkedSignal(() => {
    const target = this.editRecordSignal().target;

    if (target === TargetEntity.ORGANISATION) {
      return (this.organisationApiStore.dataList() || []).map((org) => ({
        id: org.id,
        name: org.name,
        identityNo: org.registrationNo,
        emailAddress: org.contactEmailAddress,
        identityType: null
      }));
    } else if (target === TargetEntity.INDIVIDUAL) {
      return (this.individualApiStore.dataList() || []).map((ind) => ({
        id: ind.id,
        name: ind.name,
        identityNo: ind.identityNo,
        emailAddress: ind.emailAddress,
        identityType: ind.identityType
      }));
    }

    return [];
  });

  @Input() id: string | null = null;

  constructor() {
    effect(() => {
      const settings = this.settingsApiStore.data();
      const target = this.editRecordSignal().target;

      if (!settings) {
        return;
      }

      // Update allowed document types based on target entity
      if (target === TargetEntity.INDIVIDUAL) {
        this.allowedDocumentTypes.set(settings.indKycDocuments || []);
      } else if (target === TargetEntity.ORGANISATION) {
        this.allowedDocumentTypes.set(settings.orgKycDocuments || []);
      }
    });

    effect(() => {
      const record = this.kycRecordApiStore.data();

      if (!record || !record.id) {
        return;
      }

      this.editRecordSignal.update((form) => ({
        ...form,
        id: record.id,
        createdAt: record.createdAt,
        createdBy: record.createdBy,
        modifiedAt: record.modifiedAt,
        modifiedBy: record.modifiedBy,
        ref: record.ref,
        target: record.target || TargetEntity.INDIVIDUAL,
        targetId: record.targetId,
        ownerDetails: record.ownerDetails,
        expiryDate: record.expiryDate,
        uploadDate: record.uploadDate,
        kycStatus: record.kycStatus,
        declaration: record.declaration || new DeclarationDTO(),
        sourceOfFunds: record.sourceOfFunds || [],
        sourceOfFundsDetails: record.sourceOfFundsDetails,
        documents: record.documents || [],
      }));

      this.setQuillContent('pepDetails', record.declaration?.pepDetails ?? null);
      this.setQuillContent('sanctionsDetails', record.declaration?.sanctionsDetails ?? null);
      this.setQuillContent('sourceOfFundsDetails', record.sourceOfFundsDetails ?? null);
    });

    this.kycRecordApiStore.reset();
    this.settingsApiStore.getAll();

    effect(() => {
      if (!this.saveRequested() || this.loading()) {
        return;
      }

      if (this.kycRecordApiStore.error()) {
        this.toaster.error(this.messages()[0] || 'Failed to save record.');
        this.saveRequested.set(false);
        return;
      }

      const savedRecord = this.kycRecordApiStore.data();

      if (savedRecord?.id) {
        this.toaster.success(this.messages()[0] || 'Record saved successfully.');
        this.saveRequested.set(false);

        if (!this.id && !this.route.snapshot.queryParamMap.get('id')) {
          this.router.navigate(['/', 'records', 'edit', savedRecord.id]);
        }
      }
    });
  }

  ngOnInit(): void {
    if (this.id) {
      this.kycRecordApiStore.findById({ id: this.id });
    }

    const routeId = this.route.snapshot.queryParamMap.get('id');

    if (routeId) {
      this.kycRecordApiStore.findById({ id: routeId });
    }
  }

  saveRecord(): void {
    // if (this.editRecordSignalForm().invalid()) {
    //   this.toaster.error('Complete the required record fields before saving.');
    //   return;
    // }

    // if (this.hasDocumentsWithoutType()) {
    //   this.toaster.error('Select a document type for each file before saving.');
    //   return;
    // }

    let record: KycRecordDTO = {
      id: this.editRecordSignal().id,
      ref: this.editRecordSignal().ref,
      target: this.editRecordSignal().target,
      targetId: this.editRecordSignal().targetId,
      ownerDetails: this.editRecordSignal().ownerDetails,
      expiryDate: this.editRecordSignal().expiryDate,
      uploadDate: this.editRecordSignal().uploadDate,
      kycStatus: this.editRecordSignal().kycStatus,
      declaration: this.editRecordSignal().declaration,
      sourceOfFunds: this.editRecordSignal().sourceOfFunds,
      sourceOfFundsDetails: this.editRecordSignal().sourceOfFundsDetails,
      documents: this.editRecordSignal().documents,
      createdAt: this.editRecordSignal().createdAt,
      createdBy: this.editRecordSignal().createdBy,
      modifiedAt: this.editRecordSignal().modifiedAt,
      modifiedBy: this.editRecordSignal().modifiedBy,
      employmentRecord: this.editRecordSignal().employmentRecord,
      kycVerification: this.editRecordSignal().kycVerification,

    }

    console.log('Saving record with data:', this.editRecordSignal());

    // this.saveRequested.set(true);
    this.kycRecordApiStore.save({ kycRecord: record });
  }

  cancel(): void {
    this.router.navigate(['/', 'records']);
  }

  toggleSourceOfFunds(fund: SourceOfFunds): void {
    const current = this.editRecordSignal().sourceOfFunds || [];
    const idx = current.findIndex((f: SourceOfFunds) => f === fund);

    if (idx > -1) {
      current.splice(idx, 1);
    } else {
      current.push(fund);
    }

    this.editRecordSignal.update((form) => ({
      ...form,
      sourceOfFunds: [...current],
    }));
  }

  hasSourceOfFunds(fund: SourceOfFunds): boolean {
    return (this.editRecordSignal().sourceOfFunds || []).includes(fund);
  }

  pepStatusLabel(value: PepStatus): string {
    const labels: { [key in PepStatus]: string } = {
      [PepStatus.NOT_PEP]: 'Not PEP',
      [PepStatus.PEP_SELF]: 'PEP (Self)',
      [PepStatus.PEP_RELATIVE]: 'PEP (Relative)',
      [PepStatus.PEP_ASSOCIATE]: 'PEP (Associate)',
    };
    return labels[value] || value;
  }

  identityTypeLabel(value: IndividualIdentityType): string {
    const labels: { [key in IndividualIdentityType]: string } = {
      [IndividualIdentityType.OMANG]: 'Omang',
      [IndividualIdentityType.PASSPORT]: 'Passport',
      [IndividualIdentityType.RESIDENCE_PERMIT]: 'Residence Permit',
      [IndividualIdentityType.BIRTH_CERTIFICATE]: 'Birth Certificate',
    };
    return labels[value] || value;
  }

  kycStatusLabel(value: KycComplianceStatus): string {
    const labels: { [key in KycComplianceStatus]: string } = {
      [KycComplianceStatus.CURRENT]: 'Current',
      [KycComplianceStatus.EXPIRED]: 'Expired',
      [KycComplianceStatus.ABSENT]: 'Absent',
      [KycComplianceStatus.INCOMPLETE]: 'Incomplete',
    };
    return labels[value] || value;
  }

  // sourceOfFundsLabel(value: SourceOfFunds): string {
  //   const labels: { [key in SourceOfFunds]: string } = {
  //     [SourceOfFunds.SALARY]: 'Salary',
  //     [SourceOfFunds.BUSINESS_INCOME]: 'Business Income',
  //     [SourceOfFunds.INVESTMENTS]: 'Investments',
  //     [SourceOfFunds.PENSIONS]: 'Pensions',
  //     [SourceOfFunds.GIFTS]: 'Gifts',
  //     [SourceOfFunds.REMITTANCE]: 'Remittance',
  //     [SourceOfFunds.OTHER]: 'Other',
  //   };
  //   return labels[value] || value;
  // }

  // targetLabel(value: TargetEntity): string {
  //   const labels: { [key in TargetEntity]: string } = {
  //     [TargetEntity.INDIVIDUAL]: 'Individual',
  //     [TargetEntity.ORGANISATION]: 'Organisation',
  //     [TargetEntity.BRANCH]: 'Branch',
  //     [TargetEntity.SUBSCRIPTION]: 'Subscription',
  //     [TargetEntity.INVOICE]: 'Invoice',
  //     [TargetEntity.QUOTATION]: 'Quotation',
  //     [TargetEntity.CLIENT_REQUEST]: 'Client Request',
  //     [TargetEntity.KYC_RECORD]: 'KYC Record',
  //     [TargetEntity.CONTACT]: 'Contact',
  //     [TargetEntity.SETTINGS]: 'Settings',
  //   };
  //   return labels[value] || value;
  // }

  setTarget(target: TargetEntity): void {
    this.editRecordSignal.update((form) => ({
      ...form,
      target,
      ownerDetails: {
        name: null,
        identityNo: null,
        identityType: null,
        emailAddress: null,
        phoneNumbers: [],
        physicalAddress: null,
        postalAddress: null,
      },
    }));
  }

  onDocumentSelected(event: any): void {
    const files: FileList = event.target.files;
    if (files && files.length > 0) {
      const defaultDocumentTypeId = this.allowedDocumentTypes()[0]?.id || null;
      const newFiles = Array.from(files).filter((file) =>
        !this.editRecordSignal().documentsToUpload.some((entry) => entry.file.name === file.name)
      );
      this.editRecordSignal.update((form) => ({
        ...form,
        documentsToUpload: [
          ...form.documentsToUpload,
          ...newFiles.map((file) => ({
            file,
            documentTypeId: defaultDocumentTypeId,
          })),
        ],
      }));
    }
    // Reset file input
    if (event.target) {
      event.target.value = '';
    }
  }

  removeDocument(index: number): void {
    this.editRecordSignal.update((form) => ({
      ...form,
      documentsToUpload: form.documentsToUpload.filter((_, i) => i !== index),
    }));
  }

  setUploadDocumentType(index: number, documentTypeId: string | null): void {
    this.editRecordSignal.update((form) => ({
      ...form,
      documentsToUpload: form.documentsToUpload.map((entry, i) =>
        i === index
          ? {
            ...entry,
            documentTypeId,
          }
          : entry
      ),
    }));
  }

  private hasDocumentsWithoutType(): boolean {
    return this.editRecordSignal().documentsToUpload.some((entry) => !entry.documentTypeId);
  }

  getDocumentTypeName(documentTypeId: string): string {
    const docType = this.allowedDocumentTypes().find((dt) => dt.id === documentTypeId);
    return docType?.name || 'Unknown';
  }

  onQuillEditorCreated(key: string, editor: any): void {
    this.quillEditors.set(key, editor);
    const content = this.getQuillFieldContent(key);
    if (content) {
      editor.clipboard.dangerouslyPasteHTML(content);
    }
  }

  onQuillContentChanged(key: string, event: any): void {
    if (event.source !== 'user') return;
    if (key === 'pepDetails' || key === 'sanctionsDetails') {
      this.updateDeclarationField(key as keyof DeclarationDTO, event.html);
    } else if (key === 'sourceOfFundsDetails') {
      this.editRecordSignal.update(form => ({ ...form, sourceOfFundsDetails: event.html }));
    }
  }

  private getQuillFieldContent(key: string): string | null {
    const s = this.editRecordSignal();
    if (key === 'pepDetails') return s.declaration?.pepDetails ?? null;
    if (key === 'sanctionsDetails') return s.declaration?.sanctionsDetails ?? null;
    if (key === 'sourceOfFundsDetails') return s.sourceOfFundsDetails ?? null;
    return null;
  }

  private setQuillContent(key: string, html: string | null): void {
    const editor = this.quillEditors.get(key);
    if (editor && html) {
      editor.clipboard.dangerouslyPasteHTML(html);
    }
  }

  updateDeclarationField(field: keyof DeclarationDTO, value: any): void {
    this.editRecordSignal.update((form) => ({
      ...form,
      declaration: {
        ...form.declaration,
        [field]: value,
      },
    }));
  }

  // private buildRecordPayload(): KycRecordDTO {
  //   const current = this.editRecordSignal();

  //   return {
  //     ...new KycRecordDTO(),
  //     id: current.id,
  //     ref: current.ref,
  //     target: current.target || TargetEntity.INDIVIDUAL,
  //     targetId: current.targetId,
  //     name: current.name?.trim() || null,
  //     identityNo: current.identityNo?.trim() || null,
  //     identityType: current.identityType || null,
  //     emailAddress: current.emailAddress?.trim() || null,
  //     physicalAddress: current.physicalAddress?.trim() || null,
  //     postalAddress: current.postalAddress?.trim() || null,
  //     expiryDate: current.expiryDate || null,
  //     uploadDate: current.uploadDate || null,
  //     kycStatus: current.kycStatus || null,
  //     declaration: current.declaration || new DeclarationDTO(),
  //     sourceOfFunds: current.sourceOfFunds || [],
  //     sourceOfFundsDetails: current.sourceOfFundsDetails?.trim() || null,
  //     documents: current.documents || [],
  //     createdAt: current.createdAt,
  //     createdBy: current.createdBy,
  //     modifiedAt: current.modifiedAt,
  //     modifiedBy: current.modifiedBy,
  //   } as KycRecordDTO;
  // }

  organisationSearch(): void {}

  individualSearch(): void {}

  recordOwnerCompare(o1: any, o2: any): boolean {
    return o1 && o2 && o1.id === o2.id;
  }

  filterOwners(): void {

    const filterValue = this.editRecordSignal().recordOwnerFilter || '';
    const target = this.editRecordSignal().target;

    console.log('Filtering owners with value:', filterValue, 'for target:', target);

    if (target === TargetEntity.ORGANISATION) {
      let criteria = new SearchObject<OrganisationSearchCriteria>();
      criteria.criteria = {
        name: filterValue,
      };
      this.organisationApiStore.search({ criteria });

    } else if (target === TargetEntity.INDIVIDUAL) {

      let criteria = new SearchObject<IndividualSearchCriteria>(); // Replace 'any' with actual IndividualSearchCriteria if available

      criteria.criteria = {
        name: filterValue,
        identityNo: filterValue,
        emailAddress: filterValue,
      };
      this.individualApiStore.search({ criteria });
    }

  }
}
