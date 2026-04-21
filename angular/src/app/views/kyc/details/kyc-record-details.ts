import { CommonModule } from "@angular/common";
import { AfterViewInit, Component, computed, effect, inject, linkedSignal, OnDestroy, OnInit, signal } from "@angular/core";
import { MatCardModule } from "@angular/material/card";
import { MatChipsModule } from "@angular/material/chips";
import { MatIconModule } from "@angular/material/icon";
import { MatTooltipModule } from "@angular/material/tooltip";
import { ActivatedRoute, Router, RouterModule } from "@angular/router";
import { Loader } from "@app/@shared/loader/loader";
import { DocumentDTO } from "@app/models/bw/co/centralkyc/document/document-dto";
import { DocumentTypeDTO } from "@app/models/bw/co/centralkyc/document/type/document-type-dto";
import { IndividualIdentityType } from "@app/models/bw/co/centralkyc/individual/individual-identity-type";
import { Sex } from "@app/models/bw/co/centralkyc/individual/sex";
import { KycComplianceStatus } from "@app/models/bw/co/centralkyc/kyc/kyc-compliance-status";
import { PhoneType } from "@app/models/bw/co/centralkyc/phone-type";
import { SettingsDTO } from "@app/models/bw/co/centralkyc/settings/settings-dto";
import { TargetEntity } from "@app/models/bw/co/centralkyc/target-entity";
import { DocumentApi } from "@app/services/bw/co/centralkyc/document/document-api";
import { KycRecordApiStore } from "@app/store/bw/co/centralkyc/kyc/kyc-record-api.store";
import { SettingsApiStore } from "@app/store/bw/co/centralkyc/settings/settings-api.store";
import { TranslateModule } from "@ngx-translate/core";
import { ToastrService } from "ngx-toastr";
import { IndividualApi } from "@app/services/bw/co/centralkyc/individual/individual-api";
import { OrganisationApi } from "@app/services/bw/co/centralkyc/organisation/organisation-api";
import { IndividualListDTO } from "@app/models/bw/co/centralkyc/individual/individual-list-dto";
import { OrganisationListDTO } from "@app/models/bw/co/centralkyc/organisation/organisation-list-dto";
import { MatButtonModule } from "@angular/material/button";

interface UploadProgress {
  [documentTypeId: string]: {
    isUploading: boolean;
    progress: number;
  };
}

@Component({
  selector: 'app-kyc-record-details',
  templateUrl: './kyc-record-details.html',
  styleUrls: ['./kyc-record-details.scss'],
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    TranslateModule,
    MatTooltipModule,
    MatChipsModule,
    MatButtonModule,
    Loader,
    RouterModule
  ]
})
export class KycRecordDetails implements OnInit, OnDestroy, AfterViewInit {

  kycRecordApiStore = inject(KycRecordApiStore);
  loading = linkedSignal(() => this.kycRecordApiStore.loading());
  loaderMessage = linkedSignal(() => this.kycRecordApiStore.loaderMessage());
  error = linkedSignal(() => this.kycRecordApiStore.error());
  kycRecord = linkedSignal(() => this.kycRecordApiStore.data());

  KycComplianceStatusT: any = KycComplianceStatus;
  KycComplianceStatusOptions = Object.keys(this.KycComplianceStatusT);
  TargetEntityT: any = TargetEntity;
  SexT: any = Sex;
  SexOptions = Object.keys(this.SexT);
  IndividualIdentityTypeT: any = IndividualIdentityType;
  IndividualIdentityTypeOptions = Object.keys(this.IndividualIdentityTypeT);
  PhoneTypeT: any = PhoneType;
  PhoneTypeOptions = Object.keys(this.PhoneTypeT);

  // Settings and document types
  settingsApiStore = inject(SettingsApiStore);
  settings = linkedSignal<SettingsDTO | null>(() => this.settingsApiStore.data());
  documentApi = inject(DocumentApi);
  document = linkedSignal(() => new DocumentDTO());

  // Individual and Organisation APIs
  individualApi = inject(IndividualApi);
  organisationApi = inject(OrganisationApi);
  individual = signal<IndividualListDTO | null>(null);
  organisation = signal<OrganisationListDTO | null>(null);

  // Document upload state
  uploadedDocuments = linkedSignal<DocumentDTO[]>(() => []);
  uploadProgress = signal<UploadProgress>({});

  // Computed property to get individual document types from settings
  individualDocumentTypes = computed(() => {
    const settings = this.settings();
    return settings?.individualDocuments || [];
  });
  kycIndDocumentTypes = computed(() => {
    const settings = this.settings();
    return settings?.indKycDocuments || [];
  });

  toaster: ToastrService = inject(ToastrService);
  protected router: Router = inject(Router);
  protected route: ActivatedRoute = inject(ActivatedRoute);

  constructor() {
    // Watch for kycRecord changes and load target entity
    effect(() => {
      const record = this.kycRecord();
      if (record && record.targetId && record.target) {
        this.loadTargetEntity();
      }
    });
  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }

  ngAfterViewInit(): void {

    this.route.queryParams.subscribe(params => {
      const kycRecordId = params['id'];
      if (kycRecordId) {
        this.kycRecordApiStore.findById({ id: kycRecordId });
      }
    });

    // Load settings
    this.settingsApiStore.getAll();
  }

  downloadDocument(doc: DocumentDTO): void {
    console.log(document);

    this.documentApi.downloadFile(doc.id).subscribe({
      next: (res: any) => {
        const url = window.URL.createObjectURL(res);
        const a = document.createElement('a');
        a.href = url;

        // Optional: set a meaningful filename
        a.download = `${doc.fileName}.pdf`; // or dynamically get filename from backend
        a.click();

        window.URL.revokeObjectURL(url); // clean up
      },
      error: (err: any) => {
        this.toaster.error(err.message || 'Failed to download file', 'Download Error');
      },
      complete: () => {
        // this.isDownloading.set('');
      },
    });
  }

  loadTargetEntity(): void {
    const record = this.kycRecord();
    if (!record || !record.targetId || !record.target) return;

    if (record.target === TargetEntity.INDIVIDUAL) {
      this.individualApi.findById(record.targetId).subscribe({
        next: (individual) => {
          this.individual.set({
            id: individual.id,
            name: `${individual.firstName || ''} ${individual.middleName || ''} ${individual.surname || ''}`.trim(),
            identityNo: individual.identityNo,
            identityType: individual.identityType,
            emailAddress: individual.emailAddress,
            kycStatus: individual.kycStatus,
            sex: individual.sex,
            pepStatus: individual.pepStatus,
            userCreated: individual.userCreated
          });
        },
        error: (err) => {
          console.error('Failed to load individual:', err);
        },
      });
    } else if (record.target === TargetEntity.ORGANISATION) {
      this.organisationApi.findById(record.targetId).subscribe({
        next: (org) => {
          this.organisation.set({
            id: org.id,
            code: org.code,
            name: org.name,
            registrationNo: org.registrationNo,
            status: org.status,
            contactEmailAddress: org.contactEmailAddress,
            isClient: org.isClient,
            kycStatus: org.kycStatus,
            keycloakId: org.keycloakId
          });
        },
        error: (err) => {
          console.error('Failed to load organisation:', err);
        },
      });
    }
  }

  deleteDocument(documentTypeId?: string): void {

    if (!documentTypeId) return;

    const document = this.getUploadedDocument(documentTypeId);
    if (!document) return;

    if (confirm(`Are you sure you want to delete "${document.fileName}"?`)) {
      this.documentApi.remove(document.id).subscribe({
        next: (response) => {
          if (response.success) {
            this.loadIndividualDocuments(document.targetId!);
            this.toaster.success('Document deleted successfully');
          } else {
            this.toaster.error(`Failed to delete document: ${response.message || 'Unknown error'}`);
          }
        },
        error: (error) => {
          console.error('Delete error:', error);
          this.toaster.error(`Failed to delete document: ${error.message || 'Delete failed'}`);
        },
      });
    }
  }

  private loadIndividualDocuments(individualId: string): void {
    this.documentApi.findByTarget(TargetEntity.INDIVIDUAL, individualId).subscribe({
      next: (res) => {
        this.uploadedDocuments.set(res);
      },
      error: (err) => {
        console.error('Failed to load documents:', err);
      },
    });
  }

  formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }


  // Document management methods
  getUploadedDocument(documentTypeId?: string): DocumentDTO | undefined {

    if (!documentTypeId) return undefined;

    const docs = this.uploadedDocuments() || [];
    return docs.find((doc) => doc.documentTypeId === documentTypeId);
  }

  isUploading(documentTypeId?: string): boolean {

    if (!documentTypeId) return false;

    return this.uploadProgress()[documentTypeId]?.isUploading || false;
  }

  getUploadProgress(documentTypeId?: string): number {

    if (!documentTypeId) return 0;

    return this.uploadProgress()[documentTypeId]?.progress || 0;
  }

  // Document upload event handlers
  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    const target = event.currentTarget as HTMLElement;
    target.classList.add('drag-over');
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    const target = event.currentTarget as HTMLElement;
    target.classList.remove('drag-over');
  }

  onFileDropped(event: DragEvent, docType: DocumentTypeDTO): void {
    event.preventDefault();
    event.stopPropagation();
    const target = event.currentTarget as HTMLElement;
    target.classList.remove('drag-over');

    const files = event.dataTransfer?.files;
    if (files && files.length > 0) {
      this.handleFileUpload(files[0], docType);
    }
  }

  onFileSelected(event: Event, docType: DocumentTypeDTO): void {
    const target = event.target as HTMLInputElement;
    const files = target.files;
    if (files && files.length > 0) {
      this.handleFileUpload(files[0], docType);
    }
    // Reset the input value to allow selecting the same file again
    target.value = '';
  }

  private handleFileUpload(file: File, docType: DocumentTypeDTO): void {
    // Validate file
    if (!this.validateFile(file)) {
      return;
    }

    if (!this.kycRecord().targetId) {
      this.toaster.error('No individual selected for document upload');
      return;
    }

    // Update upload progress to show uploading state
    this.updateUploadProgress(docType.id!, true, 0);

    // Use DocumentApiStore's upload method for better state management
    this.documentApi.upload(
      TargetEntity.INDIVIDUAL,
      this.kycRecord().targetId,
      docType.id!,
      file,
    ).subscribe({
      next: (res) => {
        this.toaster.success(`Document "${res.fileName}" uploaded successfully`);
        this.document.set(res);
      },
      error: (err) => {
        console.error('Upload error:', err);
        this.toaster.error(`Failed to upload document: ${err.message || 'Upload failed'}`);
      },
      complete: () => {
        // Reset upload progress
        // this.updateUploadProgress(docType.id!, false, 100);
        // // Reload documents
        // this.loadIndividualDocuments(individual.id);
      },
    });

    // Subscribe to the store's state changes to track upload progress
  }
  private validateFile(file: File): boolean {
    // Check file size (max 10MB)
    const maxSize = 10 * 1024 * 1024;
    if (file.size > maxSize) {
      this.toaster.error('File size must be less than 10MB');
      return false;
    }

    // Check file type
    const allowedTypes = [
      'application/pdf',
      'image/jpeg',
      'image/jpg',
      'image/png',
      'application/msword',
      'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    ];
    if (!allowedTypes.includes(file.type)) {
      this.toaster.error('Invalid file type. Only PDF, JPG, PNG, DOC, and DOCX files are allowed');
      return false;
    }

    return true;
  }

  private updateUploadProgress(documentTypeId: string, isUploading: boolean, progress: number): void {
    const currentProgress = this.uploadProgress();
    this.uploadProgress.set({
      ...currentProgress,
      [documentTypeId]: { isUploading, progress },
    });
  }

  // Document management methods
  // getUploadedDocument(documentTypeId?: string): DocumentDTO | undefined {

  //   if (!documentTypeId) return undefined;

  //   const docs = this.uploadedDocuments() || [];
  //   return docs.find((doc) => doc.documentTypeId === documentTypeId);
  // }

  // isUploading(documentTypeId?: string): boolean {

  //   if (!documentTypeId) return false;

  //   return this.uploadProgress()[documentTypeId]?.isUploading || false;
  // }
}
