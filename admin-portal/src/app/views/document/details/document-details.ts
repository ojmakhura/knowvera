import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTabsModule } from '@angular/material/tabs';
import { TranslateModule } from '@ngx-translate/core';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  Input,
  linkedSignal,
  OnDestroy,
  OnInit,
  signal,
} from '@angular/core';
import { Loader } from '@app/@shared/loader/loader';
import { ToastrService } from 'ngx-toastr';
import { DocumentApiStore } from '@app/store/bw/co/kyvera/document/document-api.store';
import { TargetEntity } from '@app/models/bw/co/kyvera/target-entity';
import { DocumentVerificationStatus } from '@app/models/bw/co/kyvera/document/document-verification-status';
import { form, FormField, readonly } from '@angular/forms/signals';
import { MatSelectModule } from '@angular/material/select';
import { swalFire } from '@app/@shared/swal';
import Keycloak from 'keycloak-js';
import { HasRolesDirective } from 'keycloak-angular';
import { ExpectedFieldType } from '@app/models/bw/co/kyvera/document/type/field/expected-field-type';

@Component({
  selector: 'app-document-details',
  templateUrl: './document-details.html',
  styleUrls: ['./document-details.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDividerModule,
    MatListModule,
    MatTableModule,
    MatInputModule,
    MatFormFieldModule,
    MatTooltipModule,
    MatTabsModule,
    MatSelectModule,
    TranslateModule,
    Loader,
    FormField,
    HasRolesDirective
  ],
})
export class DocumentDetails implements OnInit, AfterViewInit, OnDestroy {
  toaster: ToastrService = inject(ToastrService);
  readonly documentApiStore = inject(DocumentApiStore);
  private readonly keycloak = inject(Keycloak);

  readonly isDocumentReviewer = computed(() => this.keycloak.hasRealmRole('DOCUMENT_REVIEWER') || this.keycloak.hasResourceRole('DOCUMENT_REVIEWER'));

  readonly verificationStatusOptions: DocumentVerificationStatus[] = [
    DocumentVerificationStatus.UNVERIFIED,
    DocumentVerificationStatus.VERIFIED,
    DocumentVerificationStatus.REJECTED,
    DocumentVerificationStatus.MANUAL_REVIEW,
    DocumentVerificationStatus.IN_PROGRESS,
  ];

  loaderMessage = linkedSignal(() => this.documentApiStore.loaderMessage());
  messages = linkedSignal(() => this.documentApiStore.messages());
  success = linkedSignal(() => this.documentApiStore.success());
  loading = linkedSignal(() => this.documentApiStore.loading());
  error = linkedSignal(() => this.documentApiStore.error());

  readonly ExpectedFieldType = ExpectedFieldType;

  @Input() id!: string;

  document = linkedSignal(() => this.documentApiStore.data());
  confidenceScore = computed(() => {
    return (this.document()?.validationResults?.score ?? 0) * 100;
  });
  matchLabel = computed(() => {
    const results = this.document()?.validationResults;
    if (!results) return '—';
    if (results.match) return 'Match';
    if (results.typeMatch) return 'Type Mismatch';
    return 'No Match';
  });

  fileContentCopied = signal(false);

  metadataEditing = signal(false);
  metadataFields = signal<Array<{ key: string; value: string }>>([]);

  documentForm = form(this.document, (path) => {
    readonly(path.id);
    readonly(path.target);
    readonly(path.targetId);
    readonly(path.url);
    readonly(path.documentType);
  });

  constructor() {
    effect(() => {
      let messages = this.messages();

      if (this.success() && !this.loading()) {
        this.toaster.success(messages[0]);
      }

      if (this.error() && !this.loading()) {
        this.toaster.error(messages[0]);
      }
    });

    effect(() => {

      const error = this.error();
      console.log('Error state changed:', error);
      if (error) {
        this.toaster.error(this.messages()[0] || 'An error occurred while processing the document. Please try again.');
      }
    });
  }

  removeVerificationTagResult(index: number): void {
    swalFire({
      title: 'Are you sure?',
      text: 'This action cannot be undone.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'Cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.document.update((doc) => {
          if (!doc) return doc;
          const updatedResults = [...(doc.dataVerifications ?? [])];
          updatedResults.splice(index, 1);
          return {
            ...doc,
            dataVerifications: updatedResults,
          };
        });
      }
    });
  }

  ngOnInit(): void {
    if (this.id) {
      this.documentApiStore.findById({ id: this.id });
    }
  }

  ngAfterViewInit(): void {

  }

  ngOnDestroy(): void { }

  get statusIcon(): string {
    switch (this.document()?.verificationStatus) {
      case 'VERIFIED':
        return 'check_circle';
      case 'REJECTED':
        return 'cancel';
      case 'MANUAL_REVIEW':
        return 'pending_actions';
      default:
        return 'radio_button_unchecked';
    }
  }

  get matchIcon(): string {
    return this.document()?.validationResults?.match ? 'check_circle' : 'cancel';
  }

  // get matchLabel(): string {
  //   return this.document()?.validationResults?.typeMatch ? 'Verified Match' : 'Type Mismatch';
  // }

  get thresholdLabel(): string {
    return this.document()?.validationResults?.match ? 'Pass' : 'Fail';
  }

  // entityTypeLabel = computed(() => {
  //   switch (this.document()?.target) {
  //     case TargetEntity.INDIVIDUAL:
  //       return 'Natural Person';
  //     case TargetEntity.ORGANISATION:
  //       return 'Organisation';
  //     case TargetEntity.BRANCH:
  //       return 'Branch';
  //     default:
  //       return this.document()?.target ?? '—';
  //   }
  // });

  analyticsStatusLabel = computed(() => {
    const status = this.document()?.analyticsStatus;

    if (!status) {
      return 'Unknown';
    }

    return status
      .toString()
      .toLowerCase()
      .replace(/_/g, ' ')
      .replace(/\b\w/g, (c: string) => c.toUpperCase());
  });

  confidenceSegments = computed(() => {
    const score = this.document()?.validationResults?.score ?? 0;
    return Array.from({ length: 10 }, (_, i) => i < Math.round(score / 10));
  });

  get integritySignalRows(): Array<{ key: string; label: string; value: any; isBoolean: boolean }> {
    const scores = this.document()?.validationResults?.signalScores;
    if (!scores) return [];
    return Object.entries(scores).map(([key, value]: [string, any]) => ({
      key,
      label: key.replace(/([A-Z])/g, ' $1').replace(/^./, (s) => s.toUpperCase()),
      value,
      isBoolean: typeof value === 'boolean',
    }));
  }

  copyFileContent(): void {
    // const content = this.fileContent();
    // if (!content) return;
    // navigator.clipboard.writeText(content).then(() => {
    //   this.fileContentCopied.set(true);
    //   setTimeout(() => this.fileContentCopied.set(false), 2000);
    // });
  }

  signalIcon(key: string): string {
    const icons: Record<string, string> = {
      imageClarity: 'photo_filter',
      mrzValid: 'qr_code_scanner',
      faceMatch: 'face',
      hologramCheck: 'security',
    };
    return icons[key] ?? 'check_circle';
  }

  formatVerificationValues(values: unknown): string {
    if (!Array.isArray(values)) {
      return '—';
    }

    const normalizedValues = values
      .map((value: unknown) => String(value ?? '').trim())
      .filter((value: string) => value.length > 0);

    return normalizedValues.length > 0 ? normalizedValues.join(', ') : '—';
  }

  openMetadataEdit(): void {
    const existing = this.document()?.metadata ?? {};
    this.metadataFields.set(
      Object.entries(existing).map(([key, value]) => ({
        key,
        value: value != null ? String(value) : '',
      })),
    );
    this.metadataEditing.set(true);
  }

  cancelMetadataEdit(): void {
    this.metadataEditing.set(false);
  }

  addMetadataField(): void {
    this.metadataFields.update((fields) => [...fields, { key: '', value: '' }]);
  }

  removeMetadataField(index: number): void {
    this.metadataFields.update((fields) => fields.filter((_, i) => i !== index));
  }

  updateMetadataKey(index: number, key: string): void {
    this.metadataFields.update((fields) =>
      fields.map((field, i) => (i === index ? { ...field, key } : field)),
    );
  }

  updateMetadataValue(index: number, value: string): void {
    this.metadataFields.update((fields) =>
      fields.map((field, i) => (i === index ? { ...field, value } : field)),
    );
  }

  saveMetadata(): void {
    const currentDocument = this.document();
    if (!currentDocument?.id) return;

    const metadata = this.metadataFields().reduce(
      (acc: Record<string, string>, { key, value }) => {
        const trimmedKey = key.trim();
        if (trimmedKey.length > 0) {
          acc[trimmedKey] = value;
        }
        return acc;
      },
      {},
    );

    this.document.update((doc) => ({ ...doc, metadata }));
    this.metadataEditing.set(false);
    this.documentApiStore.save({ document: { ...currentDocument, metadata } });
  }

  saveDocument(): void {
    const currentDocument = this.document();
    if (!currentDocument?.id) {
      return;
    }

    console.log('Saving document with updated verification tag results:', this.document());

    this.documentApiStore.save({
      document: currentDocument,
    });
  }

  analyseDocument(): void {
    const documentId = this.document()?.id;
    if (!documentId) return;

    this.documentApiStore.analyseDocument({ id: documentId });
  }

  verifyData(): void {
    const documentId = this.document()?.id;
    if (!documentId) return;

    this.documentApiStore.verifyData({ id: documentId });
  }

  updateStatus(status: DocumentVerificationStatus): void {
    const documentId = this.document()?.id;
    if (!documentId) return;

    this.documentApiStore.updateVerificationStatus({ id: documentId, status });
  }
}
