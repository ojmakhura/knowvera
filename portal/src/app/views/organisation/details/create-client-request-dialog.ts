import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, effect, inject, linkedSignal, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { IndividualApiStore } from '@app/store/bw/co/centralkyc/individual/individual-api.store';
import { OrganisationListDTO } from '@app/models/bw/co/centralkyc/organisation/organisation-list-dto';
import { IndividualListDTO } from '@app/models/bw/co/centralkyc/individual/individual-list-dto';
import { SearchObject } from '@app/models/search-object';
import { OrganisationSearchCriteria } from '@app/models/bw/co/centralkyc/organisation/organisation-search-criteria';
import { IndividualSearchCriteria } from '@app/models/bw/co/centralkyc/individual/individual-search-criteria';

type CreateClientRequestDialogData = {
  defaultTarget?: TargetEntity;
};

@Component({
  selector: 'app-create-client-request-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    NgxMatSelectSearchModule,
    ReactiveFormsModule,
  ],
  template: `
    <h2 mat-dialog-title>Create Client Request</h2>

    <mat-dialog-content>
      <p>Select the target type and entity for this client request.</p>

      <mat-form-field appearance="outline" class="full-width">
        <mat-label>Target Type</mat-label>
        <mat-select [value]="selectedTarget()" (selectionChange)="onTargetChange($any($event.value))">
          <mat-option [value]="targetEntity.INDIVIDUAL">Individual</mat-option>
          <mat-option [value]="targetEntity.ORGANISATION">Organisation</mat-option>
        </mat-select>
      </mat-form-field>

      @if (selectedTarget() === targetEntity.INDIVIDUAL) {
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Select Individual</mat-label>
          <mat-select [value]="selectedIndividual()" (selectionChange)="onIndividualChange($any($event.value))">
            <mat-option>
              <ngx-mat-select-search
                [formControl]="individualFilterCtrl"
                (keyup.enter)="filterIndividuals()"
                placeholderLabel="Search individual..."
                noEntriesFoundLabel="No matching individuals found"
              ></ngx-mat-select-search>
            </mat-option>
            @for (individual of filteredIndividuals(); track individual.id) {
              <mat-option [value]="individual">
                {{ individual.name }} ({{ individual.identityNo }})
              </mat-option>
            }
          </mat-select>
        </mat-form-field>
      } @else if (selectedTarget() === targetEntity.ORGANISATION) {
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Select Organisation</mat-label>
          <mat-select [value]="selectedOrganisation()" (selectionChange)="onOrganisationChange($any($event.value))">
            <mat-option>
              <ngx-mat-select-search
                [formControl]="organisationFilterCtrl"
                (keyup.enter)="filterOrganisations()"
                placeholderLabel="Search organisation..."
                noEntriesFoundLabel="No matching organisations found"
              ></ngx-mat-select-search>
            </mat-option>
            @for (organisation of filteredOrganisations(); track organisation.id) {
              <mat-option [value]="organisation">
                {{ organisation.name }} ({{ organisation.registrationNo }})
              </mat-option>
            }
          </mat-select>
        </mat-form-field>
      }
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-button type="button" (click)="cancel()">Cancel</button>
      <button
        mat-flat-button
        color="primary"
        type="button"
        (click)="create()"
        [disabled]="!isComplete()"
      >
        Continue
      </button>
    </mat-dialog-actions>
  `,
  styles: [
    `
      ::ng-deep {
        .full-width {
          width: 100%;
        }
      }

      mat-form-field {
        width: 100%;
        margin-bottom: 1rem;
      }
    `,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CreateClientRequestDialogComponent {
  private readonly dialogRef = inject(MatDialogRef<CreateClientRequestDialogComponent>);
  private readonly data: CreateClientRequestDialogData = inject(MAT_DIALOG_DATA);
  private readonly organisationApiStore = inject(OrganisationApiStore);
  private readonly individualApiStore = inject(IndividualApiStore);

  readonly targetEntity = TargetEntity;
  readonly selectedTarget = signal<TargetEntity>(this.data?.defaultTarget || TargetEntity.INDIVIDUAL);
  readonly selectedIndividual = signal<IndividualListDTO | null>(null);
  readonly selectedOrganisation = signal<OrganisationListDTO | null>(null);

  readonly individualFilterCtrl = new FormControl<string>('');
  readonly organisationFilterCtrl = new FormControl<string>('');

  readonly organisationList = linkedSignal(() => this.organisationApiStore.dataList());
  readonly individualList = linkedSignal(() => this.individualApiStore.dataList());

  readonly filteredOrganisations = linkedSignal(() => this.organisationList());
  readonly filteredIndividuals = linkedSignal(() => this.individualList());

  constructor() {
    effect(() => {
      const filter = this.organisationFilterCtrl.value || '';
      const list = this.organisationList();

      if (!filter.trim()) {
        this.filteredOrganisations.set(list);
        return;
      }

      const lowerFilter = filter.toLowerCase();
      this.filteredOrganisations.set(
        list.filter(
          (org) =>
            org.name?.toLowerCase().includes(lowerFilter) ||
            org.registrationNo?.toLowerCase().includes(lowerFilter),
        ),
      );
    });

    effect(() => {
      const filter = this.individualFilterCtrl.value || '';
      const list = this.individualList();

      if (!filter.trim()) {
        this.filteredIndividuals.set(list);
        return;
      }

      const lowerFilter = filter.toLowerCase();
      this.filteredIndividuals.set(
        list.filter(
          (ind) =>
            ind.name?.toLowerCase().includes(lowerFilter) ||
            ind.identityNo?.toLowerCase().includes(lowerFilter),
        ),
      );
    });

    this.individualFilterCtrl.valueChanges.subscribe(() => {
      // Trigger the effect to filter individuals
    });

    this.organisationFilterCtrl.valueChanges.subscribe(() => {
      // Trigger the effect to filter organisations
    });

    this.loadInitialData();
  }

  private loadInitialData(): void {
    if (this.selectedTarget() === TargetEntity.ORGANISATION) {
      this.filterOrganisations();
    } else {
      this.filterIndividuals();
    }
  }

  onTargetChange(value: TargetEntity): void {
    this.selectedTarget.set(value);
    this.selectedIndividual.set(null);
    this.selectedOrganisation.set(null);
    this.individualFilterCtrl.reset();
    this.organisationFilterCtrl.reset();

    if (value === TargetEntity.ORGANISATION) {
      this.filterOrganisations();
    } else {
      this.filterIndividuals();
    }
  }

  onIndividualChange(value: IndividualListDTO): void {
    this.selectedIndividual.set(value);
  }

  onOrganisationChange(value: OrganisationListDTO): void {
    this.selectedOrganisation.set(value);
  }

  filterOrganisations(): void {
    const filter = this.organisationFilterCtrl.value || '';
    const criteria = new SearchObject<OrganisationSearchCriteria>();
    criteria.criteria = {
      name: filter,
      registrationNo: filter,
      isClient: true,
    };
    this.organisationApiStore.search({ criteria });
  }

  filterIndividuals(): void {
    const filter = this.individualFilterCtrl.value || '';
    const criteria = new SearchObject<IndividualSearchCriteria>();
    criteria.criteria = {
      firstName: filter,
      surname: filter,
      identityNo: filter,
    };
    this.individualApiStore.search({ criteria });
  }

  isComplete(): boolean {
    if (this.selectedTarget() === TargetEntity.INDIVIDUAL) {
      return !!this.selectedIndividual();
    }
    return !!this.selectedOrganisation();
  }

  cancel(): void {
    this.dialogRef.close();
  }

  create(): void {
    if (!this.isComplete()) {
      return;
    }

    const result = {
      target: this.selectedTarget(),
      individual: this.selectedIndividual(),
      organisation: this.selectedOrganisation(),
    };

    this.dialogRef.close(result);
  }
}
