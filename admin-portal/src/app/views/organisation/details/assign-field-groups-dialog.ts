import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { KycFieldGroupDTO } from '@app/models/bw/co/knowvera/settings/kyc/kyc-field-group-dto';

export type FieldGroupScope = 'ORG' | 'IND';
export type FieldGroupFilter = 'ALL' | FieldGroupScope;

export interface AssignFieldGroupsDialogData {
  /** Library groups (from settings) per scope. */
  organisationGroups: KycFieldGroupDTO[];
  individualGroups: KycFieldGroupDTO[];
  /** Groups currently assigned to the organisation. */
  assignedOrganisationGroups: KycFieldGroupDTO[];
  assignedIndividualGroups: KycFieldGroupDTO[];
  /** Initial filter chip, and a group to scroll into view (Manage Fields). */
  filter?: FieldGroupFilter;
  focusGroupId?: string | null;
}

export interface AssignFieldGroupsDialogResult {
  organisationReportGroups: KycFieldGroupDTO[];
  individualReportGroups: KycFieldGroupDTO[];
}

interface LibraryEntry {
  key: string;
  scope: FieldGroupScope;
  group: KycFieldGroupDTO;
}

const fieldKey = (field: any): string => field?.id || field?.fieldId || field?.field;
const groupKey = (scope: FieldGroupScope, group: KycFieldGroupDTO): string => `${scope}:${group.id || group.label}`;

/** Select KYC field groups (and their fields) from the settings library to assign to an organisation. */
@Component({
  selector: 'app-assign-field-groups-dialog',
  standalone: true,
  imports: [MatDialogModule, MatIconModule],
  templateUrl: './assign-field-groups-dialog.html',
  styleUrls: ['./assign-field-groups-dialog.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AssignFieldGroupsDialogComponent {
  private readonly dialogRef = inject(MatDialogRef<AssignFieldGroupsDialogComponent, AssignFieldGroupsDialogResult>);
  readonly data: AssignFieldGroupsDialogData = inject(MAT_DIALOG_DATA);

  readonly library: LibraryEntry[] = [
    ...(this.data.organisationGroups || []).map((group) => ({ key: groupKey('ORG', group), scope: 'ORG' as const, group })),
    ...(this.data.individualGroups || []).map((group) => ({ key: groupKey('IND', group), scope: 'IND' as const, group })),
  ];

  readonly filter = signal<FieldGroupFilter>(this.data.filter || 'ALL');
  readonly query = signal('');
  readonly focusKey = this.data.focusGroupId ? this.library.find((e) => e.group.id === this.data.focusGroupId)?.key : undefined;

  /** Selected group key -> selected field keys. */
  readonly selection = signal<Map<string, Set<string>>>(this.initialSelection());

  readonly orgCount = this.library.filter((e) => e.scope === 'ORG').length;
  readonly indCount = this.library.filter((e) => e.scope === 'IND').length;

  readonly visible = computed(() => {
    const filter = this.filter();
    const query = this.query().trim().toLowerCase();

    return this.library.filter((entry) => {
      if (filter !== 'ALL' && entry.scope !== filter) {
        return false;
      }
      if (!query) {
        return true;
      }
      const haystack = [entry.group.label, entry.group.description, ...(entry.group.groupFields || []).map((f: any) => f.field)]
        .join(' ')
        .toLowerCase();
      return haystack.includes(query);
    });
  });

  readonly selectedGroupCount = computed(() => this.selection().size);
  readonly selectedFieldCount = computed(() =>
    [...this.selection().values()].reduce((total, fields) => total + fields.size, 0),
  );

  isGroupSelected(entry: LibraryEntry): boolean {
    return this.selection().has(entry.key);
  }

  isFieldSelected(entry: LibraryEntry, field: any): boolean {
    return this.selection().get(entry.key)?.has(fieldKey(field)) ?? false;
  }

  selectedFieldsOf(entry: LibraryEntry): number {
    return this.selection().get(entry.key)?.size ?? 0;
  }

  toggleGroup(entry: LibraryEntry): void {
    this.selection.update((current) => {
      const next = new Map(current);
      if (next.has(entry.key)) {
        next.delete(entry.key);
      } else {
        next.set(entry.key, new Set((entry.group.groupFields || []).map(fieldKey)));
      }
      return next;
    });
  }

  toggleField(entry: LibraryEntry, field: any): void {
    this.selection.update((current) => {
      const next = new Map(current);
      const fields = new Set(next.get(entry.key) || []);
      const key = fieldKey(field);
      fields.has(key) ? fields.delete(key) : fields.add(key);
      next.set(entry.key, fields);
      return next;
    });
  }

  cancel(): void {
    this.dialogRef.close();
  }

  assign(): void {
    const selection = this.selection();
    const pick = (scope: FieldGroupScope) =>
      this.library
        .filter((entry) => entry.scope === scope && selection.has(entry.key))
        .map((entry) => ({
          ...entry.group,
          groupFields: (entry.group.groupFields || []).filter((field: any) => selection.get(entry.key)!.has(fieldKey(field))),
        }));

    this.dialogRef.close({
      organisationReportGroups: pick('ORG'),
      individualReportGroups: pick('IND'),
    });
  }

  private initialSelection(): Map<string, Set<string>> {
    const selection = new Map<string, Set<string>>();
    const seed = (scope: FieldGroupScope, assigned: KycFieldGroupDTO[]) => {
      for (const group of assigned || []) {
        const entry = this.library.find(
          (e) => e.scope === scope && ((group.id && e.group.id === group.id) || e.group.label === group.label),
        );
        if (entry) {
          selection.set(entry.key, new Set((group.groupFields || []).map(fieldKey)));
        }
      }
    };

    seed('ORG', this.data.assignedOrganisationGroups);
    seed('IND', this.data.assignedIndividualGroups);
    return selection;
  }
}
