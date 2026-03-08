import { KycRecord } from './../kyc-record';
import { CommonModule, JsonPipe } from '@angular/common';
import { AfterViewInit, Component, computed, effect, inject, linkedSignal, OnDestroy, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { KycRecordApi } from '@app/services/bw/co/centralkyc/kyc/kyc-record-api';
import { KycRecordApiStore } from '@app/store/bw/co/centralkyc/kyc/kyc-record-api.store';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';
import { DocumentApiStore } from '@app/store/bw/co/centralkyc/document/document-api.store';
import Keycloak from 'keycloak-js';
import { disabled, form, FormField, readonly, required } from '@angular/forms/signals';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSelectModule } from '@angular/material/select';
import { KycRecordDTO } from '@app/models/bw/co/centralkyc/kyc/kyc-record-dto';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { DocumentTypeDTO } from '@app/models/bw/co/centralkyc/document/type/document-type-dto';
import { ToastrService } from 'ngx-toastr';
import { firstValueFrom } from 'rxjs';
import { KycComplianceStatus } from '@app/models/bw/co/centralkyc/kyc/kyc-compliance-status';
import { EmploymentRecordDTO } from '@app/models/bw/co/centralkyc/individual/employment/employment-record-dto';
import { IndividualIdentityType } from '@app/models/bw/co/centralkyc/individual/individual-identity-type';
import { DeclarationDTO } from '@app/models/bw/co/centralkyc/kyc/declaration-dto';
import { SourceOfFunds } from '@app/models/bw/co/centralkyc/source-of-funds';
import { KycVerificationDTO } from '@app/models/bw/co/centralkyc/kyc/verification/kyc-verification-dto';
import { PepStatus } from '@app/models/bw/co/centralkyc/individual/pep-status';
import { VerificationStatus } from '@app/models/bw/co/centralkyc/kyc/verification/verification-status';
import { IndividualApiStore } from '@app/store/bw/co/centralkyc/individual/individual-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { Loader } from '@app/@shared/loader/loader';

class KycRecordForm {
  id: string | any = null;

  createdBy: string | any = null;
  createdAt: Date | any = null;
  modifiedBy: string | any = null;
  modifiedAt: Date | any = null;
  expiryDate: Date | any = null;
  uploadDate: Date | any = null;
  documents: Array<DocumentDTO> = [];
  name: string | any = null;
  identityNo: string | any = null;
  kycStatus: KycComplianceStatus | any = KycComplianceStatus.INCOMPLETE;
  targetId: string | any = null;
  employmentRecord: EmploymentRecordDTO = new EmploymentRecordDTO();
  target: TargetEntity | any = null;
  emailAddress: string | any = null;
  identityType: IndividualIdentityType | any = null;
  declaration: DeclarationDTO = new DeclarationDTO();
  sourceOfFunds: Array<SourceOfFunds> = [];
  sourceOfFundsDetails: string | any = null;
  kycVerification: KycVerificationDTO = new KycVerificationDTO();
}

@Component({
  selector: 'app-edit-kyc-record',
  imports: [
    CommonModule,
    FormField,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    MatSelectModule,
    Loader
  ],
  templateUrl: './edit-kyc-record.html',
  styleUrl: './edit-kyc-record.scss',
  providers: [JsonPipe]
})
export class EditKycRecord implements OnInit, OnDestroy, AfterViewInit {

  settingsApiStore = inject(SettingsApiStore);
  kycRecordApiStore = inject(KycRecordApiStore);
  kycRecordApi = inject(KycRecordApi);
  documentApiStore = inject(DocumentApiStore);

  loading = linkedSignal(() => this.kycRecordApiStore.loading() || this.settingsApiStore.loading());
  messages = linkedSignal(() => this.kycRecordApiStore.messages());
  error = linkedSignal(() => this.kycRecordApiStore.error());
  success = linkedSignal(() => this.kycRecordApiStore.success());

  readonly targetEntityEnum = TargetEntity;
  readonly kycComplianceStatusEnum = KycComplianceStatus;
  readonly identityTypeEnum = IndividualIdentityType;
  readonly sourceOfFundsEnum = SourceOfFunds;
  readonly pepStatusEnum = PepStatus;
  readonly verificationStatusEnum = VerificationStatus;

  readonly targetEntityOptions = Object.values(TargetEntity);
  readonly kycStatusOptions = Object.values(KycComplianceStatus);
  readonly identityTypeOptions = Object.values(IndividualIdentityType);
  readonly sourceOfFundsOptions = Object.values(SourceOfFunds);
  readonly pepStatusOptions = Object.values(PepStatus);
  readonly verificationStatusOptions = Object.values(VerificationStatus);

  indKycDocuments = linkedSignal(() => this.settingsApiStore.data().indKycDocuments);
  orgKycDocuments = linkedSignal(() => this.settingsApiStore.data().orgKycDocuments);

  currentIndividualRecord = linkedSignal(() => this.kycRecordApiStore.currentIndividualRecord());
  currentOrganisationRecord = linkedSignal(() => this.kycRecordApiStore.currentOrganisationRecord());
  myRecords = linkedSignal(() => this.kycRecordApiStore.data());

  individualApiStore = inject(IndividualApiStore);
  organisationApiStore = inject(OrganisationApiStore);

  private keycloak = inject(Keycloak);
  private route = inject(ActivatedRoute);
  router = inject(Router);
  private targetEntity: TargetEntity | null = null;
  private toastr = inject(ToastrService);

  // local record state and form (signals-based)
  recordSignal = signal<KycRecordForm>(new KycRecordForm());
  recordForm = form(this.recordSignal, (path) => {
    // required(path.name, { message: 'Name is required' });
    // required(path.identityNo, { message: 'Identity number is required' });
    readonly(path.name, () => true);
    readonly(path.identityNo, () => true);
    readonly(path.emailAddress, () => true);
    readonly(path.identityType, () => true);
    readonly(path.kycStatus, () => true);
    readonly(path.target, () => true);
  });

  availableDocumentTypes = computed(() => {
    const selectedTarget = this.selectedTarget ?? this.recordSignal().target;
    return selectedTarget === TargetEntity.INDIVIDUAL ? this.indKycDocuments() : this.orgKycDocuments();
  });
  // pending file uploads keyed by documentTypeId
  pendingUploads: Record<string, File> = {};
  // mark when a save+upload flow is in progress
  savingInProgress = false;

  constructor() {

    // when store data updates (e.g., after findById), populate local form
    effect(() => {
      const data = this.kycRecordApiStore.data();
      if (data && data.id) {
        this.recordSignal.set({ ...data });

      }

    });


    effect(() => {
      let individual = this.individualApiStore.data();

      if (individual && this.selectedTarget === TargetEntity.INDIVIDUAL) {

        let name = individual.firstName;
        if(individual.middleName) {
          name += ' ' + individual.middleName;
        }
        if(individual.surname) {
          name += ' ' + individual.surname;
        }

        this.recordSignal.update((record) => ({
          ...record,
          name: name,
          emailAddress: individual.emailAddress,
          identityNo: individual.identityNo,
          identityType: individual.identityType,
          targetId: individual.id,
        }));
      }

    });

    effect(() => {

      let org = this.organisationApiStore.data();

      if (org && this.selectedTarget === TargetEntity.ORGANISATION) {
        this.recordSignal.update((record) => ({
          ...record,
          name: org.name,
          emailAddress: org.contactEmailAddress,
          identityNo: org.registrationNo,
          targetId: org.id,
        }));
      }

    });

    effect(() => {
      let error = this.error();
      if (error) {
        this.toastr.error(error, (this.messages() || []).join(', ') || 'Error');
      }
      this.savingInProgress = false;
    });
  }

  ngOnInit(): void {
    this.individualApiStore.reset();
    this.organisationApiStore.reset();
    this.settingsApiStore.getAll();
    // this.kycRecordApiStore.findMyCurrentIndividualRecord();
    // this.kycRecordApiStore.findMyCurrentOrganisationRecord();
    this.kycRecordApiStore.findMyRecords();

    // this.kycRecordApi.findMyCurrentRecord(TargetEntity.INDIVIDUAL).subscribe({
    //   next: (record) => {
    //     console.log(record)
    //   }
    // })

    this.route.queryParams.subscribe((params: Params) => {
      const target = params['target'] as TargetEntity;
      const id = params['id'] as string | undefined;
      if (target) {
        this.targetEntity = target;
        // pre-fill target on new record
        this.recordSignal.update((record: KycRecordForm) => ({ ...record, target }));

        if(target === TargetEntity.INDIVIDUAL) {
          this.individualApiStore.loadMe();
        } else if(target === TargetEntity.ORGANISATION) {
          this.organisationApiStore.loadMyOrganisation();
        }

      }

      if (id) {
        // load existing record
        this.kycRecordApiStore.findById({ id });
      }
    });


  }

  // ngOnDestroy(): void {
  // }

  ngAfterViewInit(): void {
  }

  ngOnDestroy(): void { }

  // helper used by template to check if a document is selected
  isDocumentSelected(type: DocumentTypeDTO) {
    const docs: DocumentDTO[] = this.recordSignal().documents || [];
    return docs.some(d => d.documentTypeId === type.id || d.documentType === type.name || d.documentTypeId === type.code);
  }

  toggleDocument(type: DocumentTypeDTO) {
    const docs: DocumentDTO[] = [...(this.recordSignal().documents || [])];
    const idx = docs.findIndex(d => d.documentTypeId === type.id || d.documentType === type.name || d.documentTypeId === type.code);
    if (idx >= 0) {
      docs.splice(idx, 1);
      const keysToClear = [type.id, type.code, type.name, this.getDocKey(type)]
        .filter((value): value is string => !!value)
        .map((value) => String(value));
      for (const key of keysToClear) {
        delete this.pendingUploads[key];
      }
    } else {
      const doc = new DocumentDTO();
      doc.documentTypeId = type.id || type.code || type.name;
      doc.documentType = type.name;
      docs.push(doc);
    }
    this.recordSignal.update((record: KycRecordForm) => ({ ...record, documents: docs }));
  }

  getDocKey(type: DocumentTypeDTO) {
    return String(type.id || type.code || type.name);
  }

  getDocumentFileName(type: DocumentTypeDTO): string | null {
    const key = this.getDocKey(type);
    const pending = this.pendingUploads[key];
    if (pending) {
      return pending.name;
    }

    const docs: DocumentDTO[] = this.recordSignal().documents || [];
    const matched = docs.find(d => d.documentTypeId === key || d.documentType === type.name || d.documentTypeId === type.id || d.documentTypeId === type.code);
    return matched?.fileName || null;
  }

  toLabel(value: string | null | undefined): string {
    if (!value) {
      return '';
    }
    return value.toString().replace(/_/g, ' ');
  }

  toDateInputValue(value: Date | string | null | undefined): string {
    if (!value) {
      return '';
    }

    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
      return '';
    }

    return date.toISOString().slice(0, 10);
  }

  updateRecordDateField(field: 'createdAt' | 'modifiedAt' | 'expiryDate' | 'uploadDate', value: string) {
    this.updateRecordField(field, (value ? new Date(value) : null) as KycRecordForm[typeof field]);
  }

  updateEmploymentDateField(field: 'employmentStart' | 'employmentEnd', value: string) {
    this.updateEmploymentField(field, (value ? new Date(value) : null) as EmploymentRecordDTO[typeof field]);
  }

  toPositionsDisplay(positions: string[] | null | undefined): string {
    return (positions || []).join(', ');
  }

  parsePositionsInput(value: string): string[] {
    if (!value || !value.trim()) {
      return [];
    }

    return value
      .split(',')
      .map((part) => part.trim())
      .filter((part) => !!part);
  }

  onFileChange(event: Event, type: DocumentTypeDTO) {
    const input = event.target as HTMLInputElement;
    const file = input.files && input.files[0];
    if (!file) return;

    const key = this.getDocKey(type);
    this.pendingUploads[key] = file;

    // reflect selection in preview documents list
    const docs: DocumentDTO[] = [...(this.recordSignal().documents || [])];
    const idx = docs.findIndex(d => d.documentTypeId === key || d.documentType === type.name);
    if (idx >= 0) {
      docs[idx] = {
        ...docs[idx],
        fileName: file.name,
        documentTypeId: key,
        documentType: type.name,
        target: this.selectedTarget,
        targetId: this.recordSignal().targetId
      } as DocumentDTO;
    } else {
      const doc = new DocumentDTO();
      doc.documentTypeId = key;
      doc.documentType = type.name;
      doc.fileName = file.name;
      doc.target = this.recordSignal().target;
      doc.targetId = this.recordSignal().targetId;
      docs.push(doc);
    }
    this.recordSignal.update((record: KycRecordForm) => ({ ...record, documents: docs }));
  }

  updateRecordField<K extends keyof KycRecordForm>(field: K, value: KycRecordForm[K]) {
    this.recordSignal.update((record: KycRecordForm) => ({
      ...record,
      [field]: value,
    }));
  }

  updateEmploymentField<K extends keyof EmploymentRecordDTO>(field: K, value: EmploymentRecordDTO[K]) {
    this.recordSignal.update((record: KycRecordForm) => ({
      ...record,
      employmentRecord: {
        ...(record.employmentRecord || new EmploymentRecordDTO()),
        [field]: value,
      },
    }));
  }

  updateDeclarationField<K extends keyof DeclarationDTO>(field: K, value: DeclarationDTO[K]) {
    this.recordSignal.update((record: KycRecordForm) => ({
      ...record,
      declaration: {
        ...(record.declaration || new DeclarationDTO()),
        [field]: value,
      },
    }));
  }

  updateVerificationField<K extends keyof KycVerificationDTO>(field: K, value: KycVerificationDTO[K]) {
    this.recordSignal.update((record: KycRecordForm) => ({
      ...record,
      kycVerification: {
        ...(record.kycVerification || new KycVerificationDTO()),
        [field]: value,
      },
    }));
  }

  submit() {
    // if (!this.recordForm.valid) {
    //   this.toastr.error('Please fix validation errors', 'Validation');
    //   return;
    // }

    console.log('Submitting record:', this.recordSignal());
    console.log(this.pendingUploads);

    // const payload = this.recordSignal();
    this.savingInProgress = true;
    // this.kycRecordApiStore.save({ kycRecord: payload });
    this.kycRecordApiStore.createNew({
      record: this.recordSignal(),
      files: Object.values(this.pendingUploads)
    });
  }

  // expose target for template read-only access
  get selectedTarget(): TargetEntity | null {
    return this.targetEntity;
  }

}
