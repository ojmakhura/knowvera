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
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
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

type DataPoint = {
  label: string;
  value: string;
};

type CoverageItem = {
  field: string;
  confidence: string;
};

type IntegritySignal = {
  label: string;
  value: string;
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
    Loader,
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
  fileContent = linkedSignal(() => this.document()?.fileContent ?? '');
  fileContentCopied = signal(false);

  saveDocument(): void {
    this.documentApiStore.updateFileContent({
      id: this.document().id,
      content: this.fileContent(),
    })
  }

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
  }

  ngOnInit(): void {
    if(this.id) {
      this.documentApiStore.findById({id: this.id});
    }
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}

  get statusIcon(): string {
    switch (this.document()?.verificationStatus) {
      case 'VERIFIED': return 'check_circle';
      case 'REJECTED': return 'cancel';
      case 'MANUAL_REVIEW': return 'pending_actions';
      default: return 'radio_button_unchecked';
    }
  }

  get matchIcon(): string {
    return this.document()?.validationResults?.match ? 'check_circle' : 'cancel';
  }

  get matchLabel(): string {
    return this.document()?.validationResults?.typeMatch ? 'Verified Match' : 'Type Mismatch';
  }

  get thresholdLabel(): string {
    return this.document()?.validationResults?.match ? 'Pass' : 'Fail';
  }

  get entityTypeLabel(): string {
    switch (this.document()?.target) {
      case TargetEntity.INDIVIDUAL: return 'Natural Person';
      case TargetEntity.ORGANISATION: return 'Organisation';
      case TargetEntity.BRANCH: return 'Branch';
      default: return this.document()?.target ?? '—';
    }
  }

  get confidenceSegments(): boolean[] {
    const score = this.document()?.validationResults?.score ?? 0;
    return Array.from({ length: 10 }, (_, i) => i < Math.round(score / 10));
  }

  get extractionRows(): Array<{ field: string; expected: any; extracted: any; matches: boolean }> {
    const doc = this.document();
    if (!doc) return [];
    const expected = doc.expectedInformation ?? {};
    const extracted = doc.extractedInformation ?? {};
    const fields = Array.from(new Set([...Object.keys(expected), ...Object.keys(extracted)]));
    return fields.map(field => ({
      field,
      expected: expected[field] ?? '—',
      extracted: extracted[field] ?? '—',
      matches: String(expected[field]) === String(extracted[field]),
    }));
  }

  get integritySignalRows(): Array<{ key: string; label: string; value: any; isBoolean: boolean }> {
    const scores = this.document()?.validationResults?.signalScores;
    if (!scores) return [];
    return Object.entries(scores).map(([key, value]: [string, any]) => ({
      key,
      label: key.replace(/([A-Z])/g, ' $1').replace(/^./, s => s.toUpperCase()),
      value,
      isBoolean: typeof value === 'boolean',
    }));
  }

  copyFileContent(): void {
    const content = this.fileContent();
    if (!content) return;
    navigator.clipboard.writeText(content).then(() => {
      this.fileContentCopied.set(true);
      setTimeout(() => this.fileContentCopied.set(false), 2000);
    });
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
}

