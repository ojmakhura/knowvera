import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { DocumentTypeDTO } from '@app/models/bw/co/knowvera/document/type/document-type-dto';

export interface UploadDocumentDialogData {
  documentTypes: DocumentTypeDTO[];
}

export interface UploadDocumentDialogResult {
  documentTypeId: string;
  file: File;
}

@Component({
  selector: 'app-individual-upload-document-dialog',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
  ],
  template: `
    <h2 mat-dialog-title>Upload Individual Document</h2>

    <mat-dialog-content>
      <div class="upload-form">
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Document Type</mat-label>
          <mat-select [(value)]="selectedDocumentTypeId">
            @for (type of data.documentTypes; track type.id) {
              <mat-option [value]="type.id">{{ type.name || type.code }}</mat-option>
            } @empty {
              <mat-option disabled>No document types configured</mat-option>
            }
          </mat-select>
        </mat-form-field>

        <div class="file-row">
          <input
            type="file"
            #fileInput
            hidden
            (change)="onFileSelected($event)"
            accept="*/*"
          />

          <button mat-stroked-button type="button" (click)="fileInput.click()">
            <mat-icon>attach_file</mat-icon>
            Choose File
          </button>

          <span class="file-name">{{ selectedFile()?.name || 'No file selected' }}</span>
        </div>
      </div>
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-stroked-button type="button" (click)="onCancel()">Cancel</button>
      <button
        mat-flat-button
        color="primary"
        type="button"
        [disabled]="!canUpload()"
        (click)="onUpload()"
      >
        Upload
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    mat-dialog-content {
      min-width: 400px;
    }

    .upload-form {
      display: flex;
      flex-direction: column;
      gap: 1rem;
      padding-top: 0.5rem;
    }

    .full-width {
      width: 100%;
    }

    .file-row {
      display: flex;
      align-items: center;
      gap: 0.75rem;
    }

    .file-name {
      font-size: 0.85rem;
      color: #43474f;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  `],
})
export class IndividualUploadDocumentDialogComponent {
  readonly dialogRef = inject(MatDialogRef<IndividualUploadDocumentDialogComponent>);
  readonly data: UploadDocumentDialogData = inject(MAT_DIALOG_DATA);

  selectedDocumentTypeId: string = '';
  selectedFile = signal<File | null>(null);

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files && input.files.length > 0 ? input.files[0] : null;
    this.selectedFile.set(file);
  }

  canUpload(): boolean {
    return !!this.selectedDocumentTypeId.trim() && !!this.selectedFile();
  }

  onUpload(): void {
    const file = this.selectedFile();
    if (!file || !this.selectedDocumentTypeId.trim()) {
      return;
    }

    const result: UploadDocumentDialogResult = {
      documentTypeId: this.selectedDocumentTypeId.trim(),
      file,
    };

    this.dialogRef.close(result);
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
