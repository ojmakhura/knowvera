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
import { DocumentApiStore } from '@app/store/bw/co/centralkyc/document/document-api.store';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { VerificationTagStatus } from '@app/models/bw/co/centralkyc/document/verification-tag-status';
import { VerificationTag } from '@app/models/bw/co/centralkyc/kyc/verification/verification-tag';
import { VerificationTagResult } from '@app/models/bw/co/centralkyc/document/verification-tag-result';
import { form, FormField, readonly } from '@angular/forms/signals';
import { MatSelectModule } from '@angular/material/select';
import Swal from 'sweetalert2';

type EditableVerificationTagResult = {
  verificationTag: VerificationTag | null;
  verificationTagStatus: VerificationTagStatus;
  score: number | null;
  values: string[];
};

@Component({
  selector: 'app-document-details',
  templateUrl: './document-details.html',
  styleUrls: ['./document-details.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
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
  ],
})
export class DocumentDetails implements OnInit, AfterViewInit, OnDestroy {
  toaster: ToastrService = inject(ToastrService);
  readonly documentApiStore = inject(DocumentApiStore);

  loaderMessage = linkedSignal(() => this.documentApiStore.loaderMessage());
  messages = linkedSignal(() => this.documentApiStore.messages());
  success = linkedSignal(() => this.documentApiStore.success());
  loading = linkedSignal(() => this.documentApiStore.loading());
  error = linkedSignal(() => this.documentApiStore.error());

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

  // fileContent = linkedSignal(() => this.document()?.fileContent ?? '');
  fileContentCopied = signal(false);
  verificationTagResults = linkedSignal(() => this.document().verificationTagResults ?? []);
  readonly verificationTagOptions: VerificationTag[] = Object.values(VerificationTag);
  readonly verificationTagStatusOptions: VerificationTagStatus[] =
    Object.values(VerificationTagStatus);

  documentForm = form(this.document, (path) => {
    readonly(path.id);
    readonly(path.target);
    readonly(path.targetId);
    readonly(path.url);
    readonly(path.documentType);
  });

  constructor() {
    effect(() => {
      const results = this.document()?.verificationTagResults ?? [];
      this.verificationTagResults.set(
        results.map((result: any) => ({
          verificationTag: (result?.verificationTag as VerificationTag) ?? null,
          verificationTagStatus:
            (result?.verificationTagStatus as VerificationTagStatus) ??
            VerificationTagStatus.UNCHECKED,
          score: typeof result?.score === 'number' ? result.score : null,
          values: Array.isArray(result?.values)
            ? result.values.map((value: any) => String(value))
            : [],
        })),
      );
    });

    effect(() => {
      let messages = this.messages();

      if (this.success() && !this.loading()) {
        this.toaster.success(messages[0]);
      }

      if (this.error() && !this.loading()) {
        this.toaster.error(messages[0]);
      }
    });
  }

  addVerificationTagResult(): void {
    this.document.update((doc) => {
      if (!doc) return doc;
      const newResult: VerificationTagResult = {
        verificationTag: null,
        verificationTagStatus: VerificationTagStatus.UNCHECKED,
        score: 0,
        values: [],
      };
      const updatedResults = [newResult, ...(doc.verificationTagResults ?? [])];
      return {
        ...doc,
        verificationTagResults: updatedResults,
      };
    });
  }

  removeVerificationTagResult(index: number): void {
    Swal.fire({
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
          const updatedResults = [...(doc.verificationTagResults ?? [])];
          updatedResults.splice(index, 1);
          return {
            ...doc,
            verificationTagResults: updatedResults,
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

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}

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

  entityTypeLabel = computed(() => {
    switch (this.document()?.target) {
      case TargetEntity.INDIVIDUAL:
        return 'Natural Person';
      case TargetEntity.ORGANISATION:
        return 'Organisation';
      case TargetEntity.BRANCH:
        return 'Branch';
      default:
        return this.document()?.target ?? '—';
    }
  });

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

  // get extractionRows(): Array<{ field: string; expected: any; extracted: any; matches: boolean }> {
  //   const doc = this.document();
  //   if (!doc) return [];
  //   const expected = doc.expectedInformation ?? {};
  //   const extracted = doc.extractedInformation ?? {};
  //   const fields = Array.from(new Set([...Object.keys(expected), ...Object.keys(extracted)]));
  //   return fields.map((field) => ({
  //     field,
  //     expected: expected[field] ?? '—',
  //     extracted: extracted[field] ?? '—',
  //     matches: String(expected[field]) === String(extracted[field]),
  //   }));
  // }

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

  verificationTagStatusLabel(status: VerificationTagStatus | string | null | undefined): string {
    switch (status) {
      case VerificationTagStatus.SUCCESSFUL:
        return 'Successful';
      case VerificationTagStatus.FAILED:
        return 'Failed';
      case VerificationTagStatus.UNCHECKED:
      default:
        return 'Unchecked';
    }
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
}
