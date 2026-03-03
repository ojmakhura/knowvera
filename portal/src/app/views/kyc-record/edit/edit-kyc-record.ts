import { KycRecord } from './../kyc-record';
import { CommonModule, JsonPipe } from '@angular/common';
import { AfterViewInit, Component, computed, effect, inject, linkedSignal, OnDestroy, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { KycRecordApi } from '@app/services/bw/co/centralkyc/kyc/kyc-record-api';
import { KycRecordApiStore } from '@app/store/bw/co/centralkyc/kyc/kyc-record-api.store';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';
import { DocumentApiStore } from '@app/store/bw/co/centralkyc/document/document-api.store';
import Keycloak from 'keycloak-js';
import { form, FormField, required } from '@angular/forms/signals';
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
  kycStatus: KycComplianceStatus | any = null;
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

  indKycDocuments = linkedSignal(() => this.settingsApiStore.data().indKycDocuments);
  orgKycDocuments = linkedSignal(() => this.settingsApiStore.data().orgKycDocuments);

  currentIndividualRecord = linkedSignal(() => this.kycRecordApiStore.currentIndividualRecord());
  currentOrganisationRecord = linkedSignal(() => this.kycRecordApiStore.currentOrganisationRecord());
  myRecords = linkedSignal(() => this.kycRecordApiStore.data());

  private keycloak = inject(Keycloak);
  private route = inject(ActivatedRoute);
  router = inject(Router);
  private targetEntity: TargetEntity | null = null;
  private toastr = inject(ToastrService);

  // local record state and form (signals-based)
  recordSignal = signal<KycRecordForm>(new KycRecordForm());
  recordForm = form(this.recordSignal, (path) => {
    required(path.name, { message: 'Name is required' });
    required(path.identityNo, { message: 'Identity number is required' });
  });

  loading = computed(() => this.kycRecordApiStore.loading() || this.settingsApiStore.loading());
  // pending file uploads keyed by documentTypeId
  pendingUploads: Record<string, File> = {};
  // mark when a save+upload flow is in progress
  savingInProgress = signal(false);

  constructor() {

    // when store data updates (e.g., after findById), populate local form
    effect(() => {
      const data = this.kycRecordApiStore.data();
      if (data && data.id) {
        this.recordSignal.set({ ...data });
      }
    });

    // react to successful save/create and navigate back
    effect(async () => {
      if (this.kycRecordApiStore.success() && this.savingInProgress()) {
        const saved = this.kycRecordApiStore.data();
        // const uploads = Object.entries(this.pendingUploads).map(([documentTypeId, file]) => {
        //   return firstValueFrom(this.documentApiStore.upload({ target: saved.target, targetId: saved.id, documentTypeId, file }));
        // });

        // if (uploads.length > 0) {
        //   await Promise.allSettled(uploads);
        // }

        // clear pending uploads and reset saving flag
        this.pendingUploads = {};
        this.savingInProgress.set(false);

        this.toastr.success('KYC record saved', 'Success');
        this.router.navigate(['/kyc-record']);
      }
    });
  }

  ngOnInit(): void {
    this.settingsApiStore.getAll();
    // this.kycRecordApiStore.findMyCurrentIndividualRecord();
    // this.kycRecordApiStore.findMyCurrentOrganisationRecord();
    this.kycRecordApiStore.findMyRecords();

    // this.kycRecordApi.findMyCurrentRecord(TargetEntity.INDIVIDUAL).subscribe({
    //   next: (record) => {
    //     console.log(record)
    //   }
    // })

    this.keycloak.loadUserInfo().then((userInfo) => {
      console.log(userInfo);
    });

    this.route.queryParams.subscribe(params => {
      const target = params['target'] as TargetEntity;
      const id = params['id'] as string | undefined;
      if (target) {
        this.targetEntity = target;
        // pre-fill target on new record
        this.recordSignal.update((r) => ({ ...r, target }));
        console.log('Navigate to record creation for target:', target);
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
    } else {
      const doc = new DocumentDTO();
      doc.documentTypeId = type.id || type.code || type.name;
      doc.documentType = type.name;
      docs.push(doc);
    }
    this.recordSignal.update(r => ({ ...r, documents: docs }));
  }

  getDocKey(type: DocumentTypeDTO) {
    return String(type.id || type.code || type.name);
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
      docs[idx] = { ...docs[idx], fileName: file.name, documentTypeId: key, documentType: type.name } as DocumentDTO;
    } else {
      const doc = new DocumentDTO();
      doc.documentTypeId = key;
      doc.documentType = type.name;
      doc.fileName = file.name;
      docs.push(doc);
    }
    this.recordSignal.update(r => ({ ...r, documents: docs }));
  }

  submit() {
    // if (!this.recordForm.valid) {
    //   this.toastr.error('Please fix validation errors', 'Validation');
    //   return;
    // }

    // const payload = this.recordSignal();
    // this.savingInProgress.set(true);
    // this.kycRecordApiStore.save({ kycRecord: payload });
  }

  // expose target for template read-only access
  get selectedTarget(): TargetEntity | null {
    return this.targetEntity;
  }

}
