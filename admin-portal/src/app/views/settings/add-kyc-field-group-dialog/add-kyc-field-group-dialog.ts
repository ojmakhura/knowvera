import { ChangeDetectionStrategy, Component, computed, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { form, min, required } from '@angular/forms/signals';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

import { ExpectedFieldDTO } from '@app/models/bw/co/knowvera/document/type/field/expected-field-dto';
import { ExpectedFieldType } from '@app/models/bw/co/knowvera/document/type/field/expected-field-type';
import { KycFieldGroupDTO } from '@app/models/bw/co/knowvera/settings/kyc/kyc-field-group-dto';
import { GroupFieldDTO } from '@app/models/bw/co/knowvera/settings/kyc/group-field-dto';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';

interface DialogData {
  expectedFieldOptions: ExpectedFieldDTO[];
  existingGroups: KycFieldGroupDTO[];
  editingGroup?: KycFieldGroupDTO;
  groupTypeLabel: string;
  suiteLabel: string;
  suiteDescription: string;
  targetType: TargetEntity;
}

export class KycFieldGroupForm {
  label: string | any = null;
  description: string | any = null;
  position: number | any = 1;
  groupFields: Array<GroupFieldDTO> | any = [];
}

@Component({
  selector: 'app-add-kyc-field-group-dialog',
  standalone: true,
  templateUrl: './add-kyc-field-group-dialog.html',
  styleUrls: ['./add-kyc-field-group-dialog.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
  ],
})
export class AddKycFieldGroupDialog implements OnInit {
  readonly dialogRef = inject(MatDialogRef<AddKycFieldGroupDialog>);
  readonly data: DialogData = inject(MAT_DIALOG_DATA);

  groupSignal = signal<KycFieldGroupForm>(new KycFieldGroupForm());
  groupForm = form(this.groupSignal, (path) => {
    required(path.label, { message: 'Label is required' });
    min(path.position, 1, { message: 'Position must be at least 1' });
  });

  showBrowsePanel = signal(false);
  browseQuery = signal('');

  readonly TargetEntity = TargetEntity;

  allAvailableFields = computed<ExpectedFieldDTO[]>(() => {
    const seen = new Set<string>();
    const result: ExpectedFieldDTO[] = [];
    for (const field of this.data.expectedFieldOptions || []) {
      if (field.id && !seen.has(field.id)) {
        seen.add(field.id);
        result.push(field);
      }
    }
    return result;
  });

  fieldMap = computed<Record<string, ExpectedFieldDTO>>(() => {
    const map: Record<string, ExpectedFieldDTO> = {};
    for (const f of this.allAvailableFields()) {
      if (f.id) map[f.id] = f;
    }
    return map;
  });

  addedFieldIds = computed(() => new Set(this.groupSignal().groupFields.map((f: GroupFieldDTO) => f.fieldId)));

  filteredAvailableFields = computed(() => {
    const query = this.browseQuery().toLowerCase().trim();
    const added = this.addedFieldIds();
    return this.allAvailableFields()
      .filter(f => !added.has(f.id))
      .filter(f => !query ||
        f.fieldLabel?.toLowerCase().includes(query) ||
        f.field?.toLowerCase().includes(query)
      );
  });

  ngOnInit(): void {
    const editing = this.data.editingGroup;
    if (editing) {
      this.groupSignal.set({
        label: editing.label ?? null,
        description: editing.description ?? null,
        position: editing.position ?? 1,
        groupFields: editing.groupFields || [],
      });
    }

    console.log('Available fields:', this.data);
  }

  get isEditing(): boolean {
    return !!this.data.editingGroup;
  }

  get targetTypeLabel(): string {
    switch (this.data.targetType) {
      case TargetEntity.INDIVIDUAL: return 'Individual';
      case TargetEntity.ORGANISATION: return 'Organisation';
      case TargetEntity.BRANCH: return 'Branch';
      default: return this.data.targetType ?? '';
    }
  }

  getFieldLabel(gf: GroupFieldDTO): string {
    return this.fieldMap()[gf.fieldId]?.fieldLabel || gf.field || 'Unknown Field';
  }

  getFieldCode(gf: GroupFieldDTO): string {
    return this.fieldMap()[gf.fieldId]?.field || gf.field || '';
  }

  getFieldTypeBadge(gf: GroupFieldDTO): string {
    const ft = this.fieldMap()[gf.fieldId]?.fieldType;
    switch (ft) {
      case ExpectedFieldType.BOOLEAN: return 'Toggle';
      case ExpectedFieldType.STRING: return 'Text Input';
      case ExpectedFieldType.DATE: return 'Date';
      case ExpectedFieldType.NUMBER: return 'Number';
      case ExpectedFieldType.OBJECT: return 'Object';
      default: return 'Field';
    }
  }

  addFieldFromLibrary(field: ExpectedFieldDTO): void {
    const gf = new GroupFieldDTO();
    gf.fieldId = field.id;
    gf.field = field.field;
    gf.position = this.groupSignal().groupFields.length + 1;
    this.groupSignal.update(s => ({ ...s, groupFields: [...s.groupFields, gf] }));
  }

  removeField(idx: number): void {
    this.groupSignal.update(s => ({
      ...s,
      groupFields: s.groupFields.filter((_: any, i: number) => i !== idx).map((f: any, i: number) => ({ ...f, position: i + 1 }))
    }));
  }

  moveUp(idx: number): void {
    if (idx === 0) return;
    this.groupSignal.update(s => {
      const updated = [...s.groupFields];
      [updated[idx - 1], updated[idx]] = [updated[idx], updated[idx - 1]];
      return { ...s, groupFields: updated.map((f, i) => ({ ...f, position: i + 1 })) };
    });
  }

  moveDown(idx: number): void {
    if (idx >= this.groupSignal().groupFields.length - 1) return;
    this.groupSignal.update(s => {
      const updated = [...s.groupFields];
      [updated[idx], updated[idx + 1]] = [updated[idx + 1], updated[idx]];
      return { ...s, groupFields: updated.map((f, i) => ({ ...f, position: i + 1 })) };
    });
  }

  save(): void {
    if (!this.groupForm().valid()) return;
    const values = this.groupSignal();
    const group: KycFieldGroupDTO = this.isEditing
      ? { ...this.data.editingGroup! }
      : new KycFieldGroupDTO();
    group.label = values.label?.trim();
    group.description = values.description?.trim();
    group.position = values.position;
    group.targetType = this.data.targetType;
    group.groupFields = values.groupFields;
    this.dialogRef.close(group);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }

  toggleBrowsePanel(): void {
    this.showBrowsePanel.update(v => !v);
    if (!this.showBrowsePanel()) {
      this.browseQuery.set('');
    }
  }

  padIndex(i: number): string {
    return String(i + 1).padStart(2, '0');
  }
}
