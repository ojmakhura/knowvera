// views/settings/field-groups/field-groups.ts
import { Component, computed, effect, inject, linkedSignal, OnInit, signal } from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
// import { ToastrService } from 'ngx-toastr';
import { swalFire } from '@app/@shared/swal';
import { DocumentTypeDTO } from '@app/models/bw/co/knowvera/document/type/document-type-dto';
import { ExpectedFieldDTO } from '@app/models/bw/co/knowvera/document/type/field/expected-field-dto';
import { GroupFieldDTO } from '@app/models/bw/co/knowvera/settings/kyc/group-field-dto';
import { KycFieldGroupDTO } from '@app/models/bw/co/knowvera/settings/kyc/kyc-field-group-dto';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { ExpectedFieldApi } from '@app/services/bw/co/knowvera/document/type/field/expected-field-api';
import { KycFieldGroupApi } from '@app/services/bw/co/knowvera/settings/kyc/kyc-field-group-api';
import { SettingsApiStore } from '@app/store/bw/co/knowvera/settings/settings-api.store';
import { AddKycFieldGroupDialog } from '../add-kyc-field-group-dialog/add-kyc-field-group-dialog';
import { LoaderState } from '@app/@shared/loader/loader.state';

class FieldGroupsModel {
  organisationKycFieldGroups: KycFieldGroupDTO[] = [];
  individualKycFieldGroups: KycFieldGroupDTO[] = [];
}

type FieldGroupsKey = 'organisationKycFieldGroups' | 'individualKycFieldGroups';

@Component({
  selector: 'app-field-groups',
  standalone: true,
  imports: [MatIconModule, MatDialogModule],
  templateUrl: './field-groups.html',
  styleUrls: ['./field-groups.scss'],
})
export class FieldGroups implements OnInit {
  settingsApiStore = inject(SettingsApiStore);
  private dialog = inject(MatDialog);
  // private // toastr = inject(ToastrService);
  private kycFieldGroupApi = inject(KycFieldGroupApi);
  private expectedFieldApi = inject(ExpectedFieldApi);

  loading = linkedSignal(() => this.settingsApiStore.loading());
  loaderMessage = linkedSignal(() => this.settingsApiStore.loaderMessage());
  success = linkedSignal(() => this.settingsApiStore.success());
  error = linkedSignal(() => this.settingsApiStore.error());
  messages = linkedSignal(() => this.settingsApiStore.messages());

  fieldGroupsSignal = linkedSignal(() => {
    let storeData = this.settingsApiStore.settingsFieldGroups();
    let model = new FieldGroupsModel();
    model.organisationKycFieldGroups = storeData?.organisationKycFieldGroups ?? [];
    model.individualKycFieldGroups = storeData?.individualKycFieldGroups ?? [];
    return model;
  });

  activeTab = signal<'individual' | 'organisation'>('individual');

  activeGroups = computed<KycFieldGroupDTO[]>(() => {
    const groups =
      this.activeTab() === 'organisation'
        ? this.fieldGroupsSignal().organisationKycFieldGroups
        : this.fieldGroupsSignal().individualKycFieldGroups;

    return [...(groups ?? [])].sort((a, b) => (a.position ?? 0) - (b.position ?? 0));
  });

  private individualExpectedFieldOptions = signal<ExpectedFieldDTO[]>([]);
  private organisationExpectedFieldOptions = signal<ExpectedFieldDTO[]>([]);

  activeExpectedFieldOptions = computed(() =>
    this.activeTab() === 'organisation'
      ? this.organisationExpectedFieldOptions()
      : this.individualExpectedFieldOptions(),
  );

  private activeFieldMap = computed<Record<string, ExpectedFieldDTO>>(() => {
    const map: Record<string, ExpectedFieldDTO> = {};
    for (const field of this.activeExpectedFieldOptions()) {
      if (field.id) {
        map[field.id] = field;
      }
    }
    return map;
  });

  loaderState = inject(LoaderState);

  constructor() {
    effect(() => {
      const requirements = this.settingsApiStore.documentRequirements();
      this.loadExpectedFieldOptions(
        requirements?.indKycDocuments ?? [],
        this.individualExpectedFieldOptions,
      );
      this.loadExpectedFieldOptions(
        requirements?.orgKycDocuments ?? [],
        this.organisationExpectedFieldOptions,
      );
    });

    effect(() => {
      this.loaderState.isLoading.set(this.settingsApiStore.loading());
    });
  }

  ngOnInit(): void {
    this.settingsApiStore.getSettingsFieldGroups();
    this.settingsApiStore.getDocumentRequirements();
  }

  fieldLabel(groupField: GroupFieldDTO): string {
    return (
      this.activeFieldMap()[groupField.fieldId]?.fieldLabel || groupField.field || 'Unknown Field'
    );
  }

  fieldCode(groupField: GroupFieldDTO): string {
    return this.activeFieldMap()[groupField.fieldId]?.field || groupField.field || '';
  }

  createGroup(): void {
    // this.openGroupDialog(this.currentTargetType());
  }

  editGroup(group: KycFieldGroupDTO): void {
    // this.openGroupDialog(this.currentTargetType(), group);
  }

  deleteGroup(group: KycFieldGroupDTO): void {
    // const key = this.currentKey();

    // swalFire({
    //   title: 'Delete field group?',
    //   text: `This will remove the field group "${group.label || 'Field Group'}" and all its fields.`,
    //   icon: 'warning',
    //   showCancelButton: true,
    //   confirmButtonText: 'Delete',
    //   cancelButtonText: 'Cancel',
    // }).then((result) => {
    //   if (!result.isConfirmed) {
    //     return;
    //   }

    //   this.kycFieldGroupApi.remove(group.id || '').subscribe({
    //     next: () => {
    //       // this.toastr.success('Field group removed successfully');
    //       this.fieldGroupsSignal.update((value) => ({
    //         ...value,
    //         [key]: (value[key] ?? []).filter((g) => g.id !== group.id),
    //       }));
    //     },
    //     error: (error) => {
    //       // this.toastr.error(
    //         error?.error?.message || error?.message || 'Unable to remove field group',
    //       );
    //     },
    //   });
    // });
  }

  removeGroupField(group: KycFieldGroupDTO, groupField: GroupFieldDTO): void {
    swalFire({
      title: 'Remove field?',
      text: `Remove "${this.fieldLabel(groupField)}" from ${group.label}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Remove',
      cancelButtonText: 'Cancel',
    }).then((result) => {
      if (!result.isConfirmed) {
        return;
      }

      this.kycFieldGroupApi.removeField(group.id || '', groupField.id || '').subscribe({
        next: (updated) => {
          // this.toastr.success('Field removed successfully');
          this.replaceGroup(group, updated);
        },
        error: (error) => {
          // this.toastr.error(error?.error?.message || error?.message || 'Unable to remove field');
        },
      });
    });
  }

  private currentTargetType(): TargetEntity {
    return this.activeTab() === 'organisation'
      ? TargetEntity.ORGANISATION
      : TargetEntity.INDIVIDUAL;
  }

  private currentKey(): FieldGroupsKey {
    return this.activeTab() === 'organisation'
      ? 'organisationKycFieldGroups'
      : 'individualKycFieldGroups';
  }

  private openGroupDialog(targetType: TargetEntity, editingGroup?: KycFieldGroupDTO): void {
    const isOrganisation = targetType === TargetEntity.ORGANISATION;
    const key = this.currentKey();

    this.dialog
      .open(AddKycFieldGroupDialog, {
        width: 'min(96vw, 920px)',
        maxWidth: '100vw',
        data: {
          expectedFieldOptions: this.activeExpectedFieldOptions(),
          existingGroups: this.fieldGroupsSignal()[key] ?? [],
          editingGroup,
          groupTypeLabel: isOrganisation
            ? 'Organisation KYC Field Group'
            : 'Individual KYC Field Group',
          suiteLabel: isOrganisation ? 'Org. KYC Suite' : 'Ind. KYC Suite',
          suiteDescription: isOrganisation
            ? 'organisation KYC suite documents'
            : 'individual KYC suite documents',
          targetType,
        },
      })
      .afterClosed()
      .subscribe((result: KycFieldGroupDTO | null) => {
        if (!result) {
          return;
        }

        this.kycFieldGroupApi.save(result).subscribe({
          next: (savedGroup) => {
            // this.toastr.success(
            //   editingGroup
            //     ? 'Field group updated successfully'
            //     : 'Field group created successfully',
            // );
            if (editingGroup) {
              this.replaceGroup(editingGroup, savedGroup);
            } else {
              this.fieldGroupsSignal.update((value) => ({
                ...value,
                [key]: [...(value[key] ?? []), savedGroup],
              }));
            }
          },
          error: (error) => {
            // this.toastr.error(
            //   error?.error?.message || error?.message || 'Unable to save field group',
            // );
          },
        });
      });
  }

  private replaceGroup(original: KycFieldGroupDTO, updated: KycFieldGroupDTO): void {
    const key = this.currentKey();

    this.fieldGroupsSignal.update((value) => {
      const groups = [...(value[key] ?? [])];
      const index = groups.findIndex((g) => g.id === original.id);
      if (index >= 0) {
        groups[index] = updated;
      }
      return { ...value, [key]: groups };
    });
  }

  private loadExpectedFieldOptions(
    documentTypes: DocumentTypeDTO[],
    target: ReturnType<typeof signal<ExpectedFieldDTO[]>>,
  ): void {
    const ids = (documentTypes || []).map((item) => item?.id).filter(Boolean) as string[];

    if (ids.length === 0) {
      target.set([]);
      return;
    }

    this.expectedFieldApi.findByDocumentType(ids).subscribe({
      next: (fields) => target.set(this.uniqueExpectedFields(fields || [])),
      error: (error) => {
        target.set([]);
      },
    });
  }

  private uniqueExpectedFields(fields: ExpectedFieldDTO[]): ExpectedFieldDTO[] {
    const seen = new Set<string>();

    return (fields || []).filter((field) => {
      const key = field?.id ? String(field.id) : '';
      if (!key || seen.has(key)) {
        return false;
      }
      seen.add(key);
      return true;
    });
  }
}
