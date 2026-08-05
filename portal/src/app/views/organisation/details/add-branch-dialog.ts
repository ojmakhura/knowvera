import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { form, FormField, required } from '@angular/forms/signals';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { BranchDTO } from '@app/models/bw/co/knowvera/organisation/branch/branch-dto';

@Component({
  selector: 'app-branch-form-dialog',
  standalone: true,
  imports: [
    FormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    FormField,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <h2 mat-dialog-title>{{ isEditMode ? 'Edit Branch' : 'Add Branch' }}</h2>

    <mat-dialog-content>
      <div class="dialog-form">
        <mat-form-field appearance="outline">
          <mat-label>Code</mat-label>
          <input matInput [formField]="branchForm.code" placeholder="e.g. BR-001" />
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Name</mat-label>
          <input matInput [formField]="branchForm.name" placeholder="Branch name" />
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Physical Address</mat-label>
          <textarea
            matInput
            [formField]="branchForm.physicalAddress"
            rows="2"
            placeholder="Street, City, Country"
          ></textarea>
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Description</mat-label>
          <textarea
            matInput
            [formField]="branchForm.description"
            rows="2"
            placeholder="Optional description"
          ></textarea>
        </mat-form-field>
      </div>
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-stroked-button (click)="onCancel()">Cancel</button>
      <button mat-flat-button color="primary" (click)="onSave()" [disabled]="!branchForm().valid()">
        {{ isEditMode ? 'Update Branch' : 'Save Branch' }}
      </button>
    </mat-dialog-actions>
  `,
  styles: [
    `
      mat-dialog-content {
        min-width: 420px;
      }
      .dialog-form {
        display: flex;
        flex-direction: column;
        gap: 12px;
        padding-top: 8px;
      }
      .full-width {
        width: 100%;
      }
      mat-form-field {
        width: 100%;
      }
    `,
  ],
})
export class BranchFormDialogComponent {
  private dialogRef = inject(MatDialogRef<BranchFormDialogComponent>);
  data: BranchDTO = inject(MAT_DIALOG_DATA);

  branchSignal = signal<BranchDTO>(this.data ?? new BranchDTO());
  branchForm = form(this.branchSignal, (path) => {
    required(path.code);
    required(path.name);
    required(path.physicalAddress);
  });

  isEditMode = this.data?.id;

  //   code = signal(this.data.branch?.code ?? '');
  //   name = signal(this.data.branch?.name ?? '');
  //   physicalAddress = signal(this.data.branch?.physicalAddress ?? '');
  //   description = signal(this.data.branch?.description ?? '');

  //   isValid(): boolean {
  //     return this.code().trim().length > 0
  //       && this.name().trim().length > 0
  //       && this.physicalAddress().trim().length > 0;
  //   }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (!this.branchForm().valid()) return;

    const branch = this.branchSignal();

    this.dialogRef.close(branch);
  }
}
