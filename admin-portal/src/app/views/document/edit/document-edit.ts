
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { AfterViewInit, ChangeDetectionStrategy, Component, effect, inject, Input, linkedSignal, OnDestroy, OnInit, signal } from '@angular/core';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { DocumentTypeDTO } from '@app/models/bw/co/centralkyc/document/type/document-type-dto';
import { DocumentVerificationStatus } from '@app/models/bw/co/centralkyc/document/document-verification-status';
import { form, FormField, readonly } from '@angular/forms/signals';
import { Loader } from '@app/@shared/loader/loader';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { DocumentApiStore } from '@app/store/bw/co/centralkyc/document/document-api.store';
import { DocumentTypeApiStore } from '@app/store/bw/co/centralkyc/document/type/document-type-api.store';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { DocumentApi } from '@app/services/bw/co/centralkyc/document/document-api';
import { TranslateModule } from '@ngx-translate/core';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { DocumentAnalyticsStatus } from '@app/models/bw/co/centralkyc/document/document-analytics-status';

class EditDocumentForm {
  id: string | any = null;
  target: TargetEntity | any = null;
  targetId: string | any = null;
  documentType: DocumentTypeDTO | any = null;
  documentTypeFilter: DocumentTypeDTO | any = null;
  fileName: string | any = null;
  verificationStatus: DocumentVerificationStatus | any = DocumentVerificationStatus.UNVERIFIED;
  url: string | any = null;
  analyticsStatus: DocumentAnalyticsStatus | any = DocumentAnalyticsStatus.INITIALISED;

}

@Component({
selector: 'app-document-edit',
  templateUrl: './document-edit.html',
  styleUrls: ['./document-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatDividerModule,
    MatProgressBarModule,
    FormField,
    Loader,
    TranslateModule,
    NgxMatSelectSearchModule
  ]
})
export class DocumentEdit implements OnInit, AfterViewInit, OnDestroy {

  @Input() id: string | null = null;

  readonly route = inject(ActivatedRoute);
  readonly router = inject(Router);
  readonly toaster = inject(ToastrService);
  readonly documentApiStore = inject(DocumentApiStore);
  readonly documentTypeApiStore = inject(DocumentTypeApiStore);
  readonly documentApi = inject(DocumentApi);

  editDocumentSignal = signal(new EditDocumentForm());
  editDocumentSignalForm = form(this.editDocumentSignal, (path) => {
    readonly(path.id);
    if(path.id) {
      // readonly(path.documentType);
      readonly(path.target);
      readonly(path.targetId);
      readonly(path.url);
    }
  });
  isSaving = signal(false);
  lastLoadedId: string | null = null;

  document = linkedSignal<DocumentDTO | any>(() => this.documentApiStore.data());
  loading = linkedSignal(() => this.documentApiStore.loading());
  loaderMessage = linkedSignal(() => this.documentApiStore.loaderMessage());
  success = linkedSignal(() => this.documentApiStore.success());
  error = linkedSignal(() => this.documentApiStore.error());
  messages = linkedSignal(() => this.documentApiStore.messages());

  documentTypeOptions = linkedSignal<DocumentTypeDTO[]>(() => this.documentTypeApiStore.dataList());

  TargetEntityT: any = TargetEntity;
  TargetEntityOptions = Object.keys(this.TargetEntityT);

  DocumentVerificationStatusT: any = DocumentVerificationStatus;
  DocumentVerificationStatusOptions = Object.keys(this.DocumentVerificationStatusT);

  constructor() {
    effect(() => {
      const current = this.document();
      this.documentTypeOptions();

      if (!current?.id) {
        return;
      }

      this.populateForm(current);
    });

    effect(() => {
      if (this.isSaving() && this.success() && !this.loading()) {
        this.isSaving.set(false);
        this.toaster.success(this.messages()?.[0] || 'Document saved');
        this.router.navigate(['/documents/details', this.editDocumentSignal().id]);
      }

      if (this.isSaving() && this.error() && !this.loading()) {
        this.isSaving.set(false);
        this.toaster.error(this.messages()?.[0] || 'Failed to save document');
      }
    });

    effect(() => {
      const error = this.error();
      
      if (error) {
        this.toaster.error(this.messages()?.[0] || 'An error occurred while loading document details.');
      }
    });

    effect(() => {
      const success = this.success();

      if (success && this.document()) {
        this.toaster.success(this.messages()?.[0] || 'Document details loaded successfully.');
      }
    });
  }

  ngOnInit(): void {

    this.documentApiStore.reset();
    this.documentTypeApiStore.getAll();

    if(this.id && this.id != '') {
      this.loadDocumentFromRoute();
    }
  }

  ngAfterViewInit(): void {
  }

  ngOnDestroy(): void {
  }

  documentTypeCompare(o1: DocumentTypeDTO | any, o2: DocumentTypeDTO | any): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  editDocumentSave(): void {
    const current = this.document();
    const value = this.editDocumentSignal();

    const payload = new DocumentDTO();
    Object.assign(payload, current || new DocumentDTO());

    payload.id = value.id || current.id;
    payload.target = value.target || null;
    payload.targetId = value.targetId || null;
    payload.documentTypeId = value.documentType?.id || null;
    payload.documentType = value.documentType?.name || null;
    payload.fileName = value.fileName || null;
    payload.url = value.url || null;
    payload.verificationStatus = value.verificationStatus || DocumentVerificationStatus.UNVERIFIED;

    this.isSaving.set(true);
    this.documentApiStore.save({ document: payload });
  }

  editDocumentReset(): void {
    const current = this.document();

    if (!current?.id) {
      this.editDocumentSignal.set(new EditDocumentForm());
      return;
    }

    this.populateForm(current);
  }

  cancelEdit(): void {
    const id = this.editDocumentSignal().id;

    if (id) {
      this.router.navigate(['/documents/details', id]);
      return;
    }

    this.router.navigate(['/documents']);
  }

  targetEntityLabel(value: string | null | undefined): string {
    return String(value || 'UNASSIGNED').replaceAll('_', ' ');
  }

  verificationStatusLabel(status: DocumentVerificationStatus | string | null | undefined): string {
    switch (status) {
      case DocumentVerificationStatus.MANUAL_REVIEW:
        return 'Pending Review';
      case DocumentVerificationStatus.REJECTED:
        return 'Rejected';
      case DocumentVerificationStatus.VERIFIED:
        return 'Verified';
      default:
        return 'Unverified';
    }
  }

  internalReference(): string {
    const id = this.editDocumentSignal().id;

    if (!id) {
      return 'DOC-KYC-NEW';
    }

    return `DOC-KYC-${String(id).slice(0, 8).toUpperCase()}`;
  }

  lastUpdatedLabel(): string {
    const value = this.document()?.modifiedAt || this.document()?.createdAt;

    if (!value) {
      return 'Updated: not yet saved';
    }

    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
      return `Updated: ${value}`;
    }

    return `Updated: ${new Intl.DateTimeFormat('en-GB', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      hour12: false,
      timeZone: 'UTC',
    }).format(date)} UTC`;
  }

  canViewDocument(): boolean {
    return Boolean(this.editDocumentSignal().url);
  }

  viewDocument(): void {
    const value = this.editDocumentSignal().url;

    if (!value) {
      this.toaster.error('No document URL is available for preview.');
      return;
    }

    if (/^https?:\/\//i.test(value)) {
      window.open(value, '_blank', 'noopener');
      return;
    }

    this.documentApi.downloadFileByUrl(value).subscribe({
      next: (blob: Blob) => {
        const objectUrl = window.URL.createObjectURL(blob);
        window.open(objectUrl, '_blank', 'noopener');
        window.setTimeout(() => window.URL.revokeObjectURL(objectUrl), 10000);
      },
      error: () => this.toaster.error('Failed to load document preview.'),
    });
  }

  private loadDocumentFromRoute(): void {

    this.lastLoadedId = this.id;
    this.documentApiStore.findById({ id: this.id });
  }

  private populateForm(document: DocumentDTO): void {
    const selectedType = this.documentTypeOptions().find((type) => type.id === document.documentTypeId)
      || ({ id: document.documentTypeId, name: document.documentType } as DocumentTypeDTO);

    this.editDocumentSignal.set({
      id: document.id,
      target: document.target,
      targetId: document.targetId,
      documentType: selectedType,
      fileName: document.fileName,
      verificationStatus: document.verificationStatus || DocumentVerificationStatus.UNVERIFIED,
      url: document.url,
      documentTypeFilter: null,
      analyticsStatus: document.analyticsStatus || DocumentAnalyticsStatus.INITIALISED
    });
  }

  filterDocumentType(): void {
    // const search = this.editSettingsSignal().quotationDocumentTypeFilter?.toLowerCase() || '';
    // this.loading.set(true);
    // this.loaderMessage.set(`Searching document types.`);
    // this.documentTypeApi.search(search).subscribe(
    //   (data) => {
    //     this.quotationDocumentTypeFilteredList.set(data || []);
    //     this.loading.set(false);
    //   },
    //   (error) => {
    //     this.toastr.error(error.error?.message ? error.error.message : error.message);
    //     this.loading.set(false);
    //   }
    // );
  }
}
