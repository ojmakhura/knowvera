import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { DocumentTypeDTO } from '@app/models/bw/co/knowvera/document/type/document-type-dto';

export interface ManageRequirementsDialogData {
  /** Every document type there is: the ones that can be required. */
  documentTypes: DocumentTypeDTO[];
  /** What the organisation requires of itself now. */
  organisationKycDocuments: DocumentTypeDTO[];
  /** What the organisation requires of each individual client now. */
  individualKycDocuments: DocumentTypeDTO[];
}

export interface ManageRequirementsDialogResult {
  organisationKycDocuments: DocumentTypeDTO[];
  individualKycDocuments: DocumentTypeDTO[];
}

/**
 * The documents an organisation requires: of itself (its own KYC) and of each individual client
 * it onboards. Opened from the organisation's "KYC Document Requirements" panel.
 */
@Component({
  selector: 'app-manage-requirements-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [MatDialogModule, MatButtonModule, MatCheckboxModule],
  template: `
    <h2 mat-dialog-title>KYC document requirements</h2>
    <mat-dialog-content>
      <p class="text-sm text-on-surface-variant mb-4">
        Tick the documents this organisation requires. Clients are asked for them when they complete their KYC.
      </p>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        @for (column of columns; track column.key) {
          <section [attr.aria-label]="column.title">
            <h3 class="kv-label mb-2">{{ column.title }} ({{ selected()[column.key].size }})</h3>
            <ul class="flex flex-col gap-1">
              @for (type of documentTypes; track type.id) {
                <li>
                  <mat-checkbox
                    [checked]="selected()[column.key].has(type.id)"
                    (change)="toggle(column.key, type.id, $event.checked)">
                    {{ type.name }} <span class="text-xs text-on-surface-variant">({{ type.code }})</span>
                  </mat-checkbox>
                </li>
              }
            </ul>
          </section>
        }
      </div>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button type="button" (click)="dialogRef.close()">Cancel</button>
      <button mat-flat-button type="button" (click)="save()">Save requirements</button>
    </mat-dialog-actions>
  `,
})
export class ManageRequirementsDialogComponent {
  protected readonly dialogRef = inject(MatDialogRef<ManageRequirementsDialogComponent, ManageRequirementsDialogResult>);
  private readonly data: ManageRequirementsDialogData = inject(MAT_DIALOG_DATA);

  protected readonly documentTypes = [...(this.data.documentTypes || [])].sort((a, b) =>
    (a.name ?? '').localeCompare(b.name ?? ''),
  );

  protected readonly columns = [
    { key: 'organisation' as const, title: 'Of the organisation' },
    { key: 'individual' as const, title: 'Of each individual client' },
  ];

  protected readonly selected = signal({
    organisation: new Set<string>((this.data.organisationKycDocuments || []).map((type) => type.id)),
    individual: new Set<string>((this.data.individualKycDocuments || []).map((type) => type.id)),
  });

  protected toggle(key: 'organisation' | 'individual', id: string, checked: boolean): void {
    this.selected.update((current) => {
      const ids = new Set(current[key]);
      if (checked) {
        ids.add(id);
      } else {
        ids.delete(id);
      }
      return { ...current, [key]: ids };
    });
  }

  protected save(): void {
    const pick = (ids: Set<string>) => this.documentTypes.filter((type) => ids.has(type.id));
    this.dialogRef.close({
      organisationKycDocuments: pick(this.selected().organisation),
      individualKycDocuments: pick(this.selected().individual),
    });
  }
}
