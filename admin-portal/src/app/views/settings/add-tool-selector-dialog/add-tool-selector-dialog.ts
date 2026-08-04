import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { form, required, FormField } from '@angular/forms/signals';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

import { Tool } from '@app/models/bw/co/kyvera/settings/tool';
import { ToolSelectorDTO } from '@app/models/bw/co/kyvera/settings/tool-selector-dto';

interface AddToolSelectorDialogData {
  title: string;
  toolOptions: Tool[];
}

class AddToolSelectorForm {
  tool: Tool | any = null;
}

@Component({
  selector: 'app-add-tool-selector-dialog',
  standalone: true,
  template: `
    <h2 mat-dialog-title>{{ data.title }}</h2>

    <mat-dialog-content>
      <mat-form-field class="dialog-field">
        <mat-label>Tool</mat-label>
        <mat-select [formField]="addToolForm.tool" id="toolSelectorTool">
          @for (tool of data.toolOptions; track tool) {
          <mat-option [value]="tool">{{ tool }}</mat-option>
          }
        </mat-select>
      </mat-form-field>
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-stroked-button type="button" (click)="cancel()">Cancel</button>
      <button mat-flat-button color="primary" type="button" (click)="save()" [disabled]="addToolForm().invalid()">Add</button>
    </mat-dialog-actions>
  `,
  styles: [
    `
      .dialog-field {
        width: 100%;
        margin-top: 0.25rem;
      }
    `,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    FormField,
  ],
})
export class AddToolSelectorDialog {
  readonly dialogRef = inject(MatDialogRef<AddToolSelectorDialog>);
  readonly data: AddToolSelectorDialogData = inject(MAT_DIALOG_DATA);

  readonly formSignal = signal<AddToolSelectorForm>(new AddToolSelectorForm());
  readonly addToolForm = form(this.formSignal, (path) => {
    required(path.tool, { message: 'Tool is required' });
  });

  save(): void {
    if (!this.addToolForm().valid()) {
      return;
    }

    const model = new ToolSelectorDTO();
    model.tool = this.formSignal().tool;
    this.dialogRef.close(model);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}
