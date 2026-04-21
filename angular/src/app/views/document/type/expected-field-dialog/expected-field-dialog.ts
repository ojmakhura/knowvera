import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MaterialModule } from '@app/material.module';
import { ExpectedField } from '@models/bw/co/centralkyc/document/type/expected-field';
import { KeyField } from '@models/bw/co/centralkyc/key-field';
import { TranslateModule } from '@ngx-translate/core';

export interface ExpectedFieldDialogData {
  expectedField?: ExpectedField;
  keyFieldOptions: string[];
  keyFieldMap: any;
}

@Component({
  selector: 'app-expected-field-dialog',
  templateUrl: './expected-field-dialog.html',
  styles: [
    `
      .expected-field-dialog-form {
        display: flex;
        flex-direction: column;
        gap: 0.75rem;
        min-width: min(100%, 34rem);
      }
    `,
  ],
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslateModule,
    MaterialModule,
  ],
})
export class ExpectedFieldDialogComponent {
  private readonly fb = inject(FormBuilder);
  readonly dialogRef = inject(MatDialogRef<ExpectedFieldDialogComponent>);
  readonly data = inject<ExpectedFieldDialogData>(MAT_DIALOG_DATA);

  readonly keyFieldMap = this.data.keyFieldMap;
  readonly keyFieldOptions = this.data.keyFieldOptions;

  readonly form = this.fb.nonNullable.group({
    field: ['', Validators.required],
    keyField: ['' as KeyField | '', Validators.required],
    mandatory: false,
    format: '',
  });

  constructor() {
    if (this.data.expectedField) {
      this.form.patchValue({
        field: this.data.expectedField.field ?? '',
        keyField: this.data.expectedField.keyField ?? '',
        mandatory: !!this.data.expectedField.mandatory,
        format: this.data.expectedField.format ?? '',
      });
    }
  }

  close(): void {
    this.dialogRef.close();
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    const expectedField = new ExpectedField();
    expectedField.field = value.field;
    expectedField.keyField = value.keyField as KeyField;
    expectedField.mandatory = value.mandatory;
    expectedField.format = value.format;

    this.dialogRef.close(expectedField);
  }
}
