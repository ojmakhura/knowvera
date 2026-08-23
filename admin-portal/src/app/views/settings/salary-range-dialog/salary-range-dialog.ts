// views/settings/salary-range-dialog/salary-range-dialog.ts
import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { form, required, FormField } from '@angular/forms/signals';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { SalaryRangeDTO } from '@app/models/bw/co/knowvera/settings/salary-range-dto';

export interface SalaryRangeDialogData {
  range?: SalaryRangeDTO;
}

export type SalaryRangeDialogResult = SalaryRangeDTO | 'removed' | null;

class SalaryRangeForm {
  min: number | any = null;
  max: number | any = null;
  active: boolean = true;
}

@Component({
  selector: 'app-salary-range-dialog',
  templateUrl: './salary-range-dialog.html',
  styleUrls: ['./salary-range-dialog.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatDialogModule, MatIconModule, FormField],
})
export class SalaryRangeDialog {
  private readonly dialogRef = inject(MatDialogRef<SalaryRangeDialog, SalaryRangeDialogResult>);
  private readonly data: SalaryRangeDialogData = inject(MAT_DIALOG_DATA);

  readonly isEditing = !!this.data?.range;

  private readonly formSignal = signal<SalaryRangeForm>({
    min: this.data?.range?.min ?? null,
    max: this.data?.range?.max ?? null,
    active: this.data?.range?.active ?? true,
  });

  readonly rangeForm = form(this.formSignal, (path) => {
    required(path.min, { message: 'Min value is required' });
    required(path.max, { message: 'Max value is required' });
  });

  save(): void {
    if (this.rangeForm().invalid()) {
      return;
    }

    const value = this.formSignal();
    const range = this.data?.range ? { ...this.data.range } : new SalaryRangeDTO();
    range.min = value.min;
    range.max = value.max;
    range.active = value.active;

    this.dialogRef.close(range);
  }

  remove(): void {
    this.dialogRef.close('removed');
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}
