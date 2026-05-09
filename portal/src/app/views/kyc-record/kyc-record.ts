import { CommonModule, JsonPipe } from '@angular/common';
import { AfterViewInit, Component, computed, effect, inject, linkedSignal, OnDestroy, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import Swal from 'sweetalert2';
import { KycRecordApiStore } from '@app/store/bw/co/centralkyc/kyc/kyc-record-api.store';
import { SettingsApiStore } from '@app/store/bw/co/centralkyc/settings/settings-api.store';
import { DocumentApi } from '@app/services/bw/co/centralkyc/document/document-api';
import { DocumentTypeDTO } from '@app/models/bw/co/centralkyc/document/type/document-type-dto';
import { Loader } from '@app/@shared/loader/loader';
import { ToastrService } from 'ngx-toastr';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { DocumentVerificationStatus } from '@app/models/bw/co/centralkyc/document/document-verification-status';
import { MatIconModule } from '@angular/material/icon';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-kyc-record',
  imports: [CommonModule, Loader, MatIconModule, TranslateModule],
  templateUrl: './kyc-record.html',
  styleUrl: './kyc-record.scss',
  providers: [
    CommonModule,
    Loader,

  ]
})
export class KycRecord implements OnInit, OnDestroy, AfterViewInit {

  settingsApiStore = inject(SettingsApiStore);
  kycRecordApiStore = inject(KycRecordApiStore);
  documentApi = inject(DocumentApi);

  indKycDocuments = linkedSignal(() => this.settingsApiStore.data().indKycDocuments);
  orgKycDocuments = linkedSignal(() => this.settingsApiStore.data().orgKycDocuments);

  myRecords = linkedSignal(() => this.kycRecordApiStore.data());

  record = linkedSignal(() => this.kycRecordApiStore.data());

  selectedDocumentIndex = signal(0);
  showUploadForm = signal(false);
  selectedAnalysisTab = signal<'report' | 'documents'>('report');

  availableDocumentTypes = computed(() => {
    const record = this.record();
    const allTypes = record?.target === 'INDIVIDUAL' ? this.indKycDocuments() : this.orgKycDocuments();
    const uploadedTypes = record?.documents?.map((d: DocumentDTO) => d.documentTypeId) || [];
    return allTypes.filter((type: DocumentTypeDTO) => !uploadedTypes.includes(type.id));
  });

  selectedFile: File | null = null;
  selectedDocumentType: string = '';
  updatingDocument: DocumentDTO | null = null;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  toaster: ToastrService = inject(ToastrService);

  loading = linkedSignal(() => this.kycRecordApiStore.loading());
  error = linkedSignal(() => this.kycRecordApiStore.error());
  messages = linkedSignal(() => this.kycRecordApiStore.messages());
  loaderMessage = linkedSignal(() => this.kycRecordApiStore.loaderMessage());
  success = linkedSignal(() => this.kycRecordApiStore.success());

  constructor() {

    effect(() => {

      let success = this.success();
      if(success) {
        this.toaster.success(this.messages()[0], "Success");

      }
    });

    effect(() => {
      let error = this.error();
      if(error) {
        this.toaster.error("An error occurred: " + JSON.stringify(error), "Error");

      }
    });
  }

  ngOnInit(): void {
    this.settingsApiStore.getAll();

    this.route.queryParams.subscribe(params => {
      const target = params['target'] as TargetEntity;
      if (target) {
        // this.navigateToRecordCreation(target);
        console.log('Navigate to record creation for target:', target);
      }
    });

    this.route.params.subscribe(params => {
      console.log('Route params:', params);
      const recordId = params['id'];
      if (recordId) {
        this.kycRecordApiStore.findById({id: recordId});
      }
    });
  }

  ngOnDestroy(): void {
  }

  ngAfterViewInit(): void {
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      if (this.updatingDocument) {
        this.updateDocumentFile();
      }
    }
  }

  triggerFileInput(): void {
    this.showUploadForm.set(true);
    setTimeout(() => {
      const fileInput = document.getElementById('fileInput') as HTMLInputElement;
      if (fileInput) fileInput.click();
    }, 100);
  }

  onDocumentTypeSelected(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.selectedDocumentType = select.value;
  }

  uploadDocument(): void {
    const record = this.record();
    if (!record || !this.selectedFile || !this.selectedDocumentType) {
      alert('Please select a file and document type');
      return;
    }

    const doc = new DocumentDTO();
    doc.target = TargetEntity.KYC_RECORD;
    doc.targetId = record.id;
    doc.documentTypeId = this.selectedDocumentType;
    doc.fileName = this.selectedFile.name;

    this.kycRecordApiStore.updateRecordFiles({
      id: record.id,
      documents: [doc],
      files: [this.selectedFile]
    });
  }

  downloadDocument(doc: DocumentDTO): void {
    if (doc.url) {
      window.open(doc.url, '_blank');
      return;
    }

    if (!doc.id) {
      alert('Cannot download: document does not have an ID');
      return;
    }

    this.documentApi.downloadFile(doc.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const anchor = document.createElement('a');
        anchor.href = url;
        anchor.download = doc.fileName || 'document';
        anchor.click();
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Download failed:', error);
        alert('Failed to download document');
      },
    });
  }

  removeDocument(doc: DocumentDTO): void {
    if (!doc.id) {

      Swal.fire({
        title: 'Cannot remove document',
        text: 'This document does not have an ID and cannot be removed.',
        icon: 'error',
        confirmButtonText: 'OK'
      }).then(() => {
        // Optional: You can add any additional logic here after the user acknowledges the alert
      });
      return;
    }

    Swal.fire({
      title: 'Confirm Removal',
      text: `Are you sure you want to remove the document "${doc.fileName}"?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, remove it!',
      cancelButtonText: 'No, keep it'
    }).then((result) => {
      if (result.isConfirmed) {
        const record = this.record();
        if (record) {
          this.kycRecordApiStore.removeRecordFile({
            id: record.id,
            documentId: doc.id
          });
        }
      }
    });

  }

  updateDocument(doc: DocumentDTO): void {
    this.updatingDocument = doc;
    // Trigger file input click to select new file
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.click();
    }
  }

  private updateDocumentFile(): void {
    const record = this.record();
    if (!record || !this.selectedFile || !this.updatingDocument) {
      return;
    }

    const updatedDoc = { ...this.updatingDocument };
    updatedDoc.fileName = this.selectedFile.name;

    this.kycRecordApiStore.updateRecordFiles({
      id: record.id,
      documents: [updatedDoc],
      files: [this.selectedFile]
    });

    this.updatingDocument = null;
  }

  getFileIcon(fileName: string): string {
    if (!fileName) return 'file';

    const extension = fileName.split('.').pop()?.toLowerCase();
    if (!extension) return 'file';

    if (['pdf'].includes(extension)) return 'pdf';
    if (['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'].includes(extension)) return 'image';
    if (['doc', 'docx'].includes(extension)) return 'word';
    if (['xls', 'xlsx'].includes(extension)) return 'excel';

    return 'file';
  }

  getDocumentVerificationStatus(doc: DocumentDTO): string {

    console.log(doc)
    if (!doc) return 'Unknown';

    // Check if the record has verification data
    // if (record.kycVerification) {
      // If the record is verified, documents are considered verified
      if (doc.verificationStatus === DocumentVerificationStatus.VERIFIED) {
        return 'Verified';
      }
      // If verification is in progress or pending
      else if (doc.verificationStatus === DocumentVerificationStatus.MANUAL_REVIEW) {
        return 'Under Review';
      }
      // If verification failed
      else if (doc.verificationStatus === DocumentVerificationStatus.REJECTED) {
        return 'Verification Failed';
      }
    // }

    // Default status for documents in unverified records
    return 'Pending Verification';
  }

  getVerificationStatusClass(doc: DocumentDTO): string {
    const status = this.getDocumentVerificationStatus(doc).toLowerCase();

    if (status.includes('verified') && !status.includes('failed')) {
      return 'verified';
    } else if (status.includes('pending')) {
      return 'pending';
    } else if (status.includes('failed')) {
      return 'failed';
    } else if (status.includes('review')) {
      return 'review';
    }

    return 'pending'; // default
  }

  getDocumentIconMaterial(fileName: string): string {
    const fileType = this.getFileIcon(fileName);

    switch (fileType) {
      case 'pdf':
        return 'picture_as_pdf';
      case 'image':
        return 'image';
      case 'word':
        return 'description';
      case 'excel':
        return 'table_chart';
      default:
        return 'description';
    }
  }
}
