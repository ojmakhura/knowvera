import { CommonModule } from "@angular/common";
import { ChangeDetectionStrategy, Component, computed, effect, inject, linkedSignal } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatChipsModule } from "@angular/material/chips";
import { MatDividerModule } from "@angular/material/divider";
import { MatIconModule } from "@angular/material/icon";
import { MatListModule } from "@angular/material/list";
import { Router } from "@angular/router";
import { DocumentListDTO } from "@app/models/bw/co/knowvera/document/document-list-dto";
import { EmploymentRecordDTO } from "@app/models/bw/co/knowvera/individual/employment/employment-record-dto";
import { PhoneNumber } from "@app/models/bw/co/knowvera/phone-number";
import { AppEnvStore } from "@app/store/app-env.state";
import { IndividualApiStore } from "@app/store/bw/co/knowvera/individual/individual-api.store";
import { SettingsApiStore } from "@app/store/bw/co/knowvera/settings/settings-api.store";

@Component({
  selector: 'app-individual-details',
  imports: [CommonModule, MatButtonModule, MatCardModule, MatChipsModule, MatDividerModule, MatIconModule, MatListModule],
  templateUrl: './individual-details.html',
  styleUrl: './individual-details.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class IndividualDetails {

  appEnvStore = inject(AppEnvStore);
  readonly individualApiStore = inject(IndividualApiStore);
  readonly settingsApiStore = inject(SettingsApiStore);
  readonly router = inject(Router);

  settings = linkedSignal(() => this.settingsApiStore.data());

  individual = linkedSignal(() => this.individualApiStore.data());

  loaderMessage = linkedSignal(() => this.individualApiStore.loaderMessage());
  messages = linkedSignal(() => this.individualApiStore.messages());
  success = linkedSignal(() => this.individualApiStore.success());
  loading = linkedSignal(() => this.individualApiStore.loading());
  error = linkedSignal(() => this.individualApiStore.error());

  readonly fullName = computed(() => this.joinParts([
    this.individual().firstName,
    this.individual().middleName,
    this.individual().surname,
  ]) || 'Individual profile');

  readonly initials = computed(() => {
    const name = this.joinParts([
      this.individual().firstName,
      this.individual().surname,
    ]) || this.fullName();

    return name
      .split(' ')
      .filter(Boolean)
      .slice(0, 2)
      .map((part) => part.charAt(0).toUpperCase())
      .join('') || 'IP';
  });

  readonly organizationLabel = computed(() => this.joinParts([
    this.individual().organisation?.name,
    this.individual().branch?.name,
  ], ' • '));

  readonly identityTypeLabel = computed(() => this.humanize(this.individual().identityType));
  readonly sexLabel = computed(() => this.humanize(this.individual().sex));
  readonly maritalStatusLabel = computed(() => this.humanize(this.individual().maritalStatus));
  readonly nationalityLabel = computed(() => this.individual().nationality || 'Not provided');
  readonly kycStatusLabel = computed(() => this.humanize(this.individual().kycStatus));
  readonly employmentStatusLabel = computed(() => this.humanize(this.individual().employmentStatus));
  readonly accountStatusLabel = computed(() => this.individual().hasUser ? 'Portal account enabled' : 'No portal account');
  readonly documentCount = computed(() => this.documents().length);
  readonly phoneCount = computed(() => this.phoneNumbers().length);
  readonly employmentCount = computed(() => this.employmentRecords().length);
  readonly currentEmployment = computed(() => this.employmentRecords()[0] ?? null);
  readonly currentEmployerName = computed(() => this.currentEmployment()?.employer?.name || this.individual().organisation?.name || 'Not provided');
  readonly currentPositionLabel = computed(() => this.currentEmployment()?.positions?.[0] || this.currentEmployment()?.name || 'Not provided');
  readonly latestKycLabel = computed(() => this.humanize(this.individual().latestKyc?.kycStatus));

  readonly phoneNumbers = computed(() => (this.individual().phoneNumbers ?? []) as PhoneNumber[]);
  readonly employmentRecords = computed(() => (this.individual().employmentRecords ?? []) as EmploymentRecordDTO[]);
  readonly documents = computed(() => (this.individual().documents ?? []) as DocumentListDTO[]);

  constructor() {

    effect(() => {

      const id = this.appEnvStore.individual()?.id;

      if (id) {
        this.individualApiStore.findById({ id });
      }

    });
  }

  openEditProfile(): void {
    this.router.navigate(['/individual/edit']);
  }

  protected humanize(value: unknown): string {
    if (value === null || value === undefined || value === '') {
      return 'Not provided';
    }

    return String(value)
      .replace(/_/g, ' ')
      .toLowerCase()
      .replace(/\b\w/g, (character) => character.toUpperCase());
  }

  protected joinParts(parts: Array<string | null | undefined>, separator = ' '): string {
    return parts
      .map((part) => (part ?? '').trim())
      .filter((part) => part.length > 0)
      .join(separator);
  }
}