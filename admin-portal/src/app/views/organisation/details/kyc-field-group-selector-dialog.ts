import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormField, form } from '@angular/forms/signals';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSelectModule } from '@angular/material/select';
import { KycFieldGroupDTO } from '@app/models/bw/co/knowvera/settings/kyc/kyc-field-group-dto';
import { GroupFieldDTO } from '@app/models/bw/co/knowvera/settings/kyc/group-field-dto';

export interface KycFieldGroupSelectorDialogData {
  groups: KycFieldGroupDTO[];
  selectedGroupId?: string | null;
  selectedGroup?: KycFieldGroupDTO | null;
  selectedFieldIds?: string[];
  selectedFields?: GroupFieldDTO[];
}


interface KycFieldGroupSelectionState {
  groupId: string;
  group: KycFieldGroupDTO | null;
  fieldIds: string[];
  fields: GroupFieldDTO[];
}

@Component({
  selector: 'app-kyc-field-group-selector-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormField,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatCheckboxModule,
    MatListModule,
    MatIconModule,
  ],
  templateUrl: './kyc-field-group-selector-dialog.html',
  styleUrls: ['./kyc-field-group-selector-dialog.scss'],
})
export class KycFieldGroupSelectorDialogComponent {
  private dialogRef = inject(
    MatDialogRef<KycFieldGroupSelectorDialogComponent, KycFieldGroupDTO>
  );
  data: KycFieldGroupSelectorDialogData = inject(MAT_DIALOG_DATA);

  private selectionModel = signal<KycFieldGroupSelectionState>({
    groupId: this.data?.selectedGroupId || (this.data?.groups?.[0]?.id ?? ''),
    group: this.data?.groups?.find((group) => group.id === this.data?.selectedGroupId) || null,
    fieldIds: [...(this.data?.selectedFieldIds || [])],
    fields: this.data?.groups?.find((group) => group.id === this.data?.selectedGroupId)?.groupFields || [],
  });
  selectionForm = form(this.selectionModel);

  selectedGroup = computed(() => this.selectionModel().group);
  selectedFields = computed(() => this.selectionModel().fields);

  selectedGroupData = computed<KycFieldGroupDTO | undefined>(() =>
    (this.data.groups || []).find((group) => group.id === this.selectedGroup()?.id)
  );

  allFieldsSelected = computed<boolean>(() => {
    const group = this.selectedGroupData();
    const fields = (group?.groupFields || []);

    return fields.length > 0 && fields.every((field: any) => this.selectedFields().includes(field.id || field.fieldId));
  });

  onGroupChange(groupId: string): void {
    this.selectionModel.update((selection) => {
      const group = this.data?.groups?.find((g) => g.id === groupId) || null;
      return { ...selection, groupId, group, fieldIds: [] };
    });
  }

  isFieldSelected(field: any): boolean {
    return this.selectedFields().includes(field.id || field.fieldId);
  }

  toggleField(field: any): void {
    const fieldId = field.id || field.fieldId;
    console.log(field);

    this.selectionModel.update((selection) => ({
      ...selection,
      fieldIds: selection.fieldIds.includes(fieldId)
        ? selection.fieldIds.filter((id) => id !== fieldId)
        : [...selection.fieldIds, fieldId],
      fields: selection.fields.includes(fieldId)
        ? selection.fields.filter((f) => (f.id || f.fieldId) !== fieldId)
        : [...selection.fields, field],
    }));
  }

  toggleSelectAll(): void {
    const group = this.selectedGroupData();
    const fieldIds = (group?.groupFields || []).map((field: any) => field.id || field.fieldId);

    this.selectionModel.update((selection) => ({
      ...selection,
      fieldIds: this.allFieldsSelected() ? [] : fieldIds,
      fields: this.allFieldsSelected() ? [] : (this.selectedGroupData()?.groupFields || []),
    }));
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onApply(): void {
    const group = this.selectedGroup();

    if (!group || !this.selectedFields().length) return;

    this.dialogRef.close({ ...group, groupFields: this.selectedFields() });
  }
}
