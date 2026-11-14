import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
  signal,
} from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { FormSubmissionApi } from '@app/services/bw/co/knowvera/form/submission/form-submission-api';
import { FormSubmissionDTO } from '@models/bw/co/knowvera/form/submission/form-submission-dto';
import {
  EDITABLE_FORM_SUBMISSION_STATUSES,
  FormSubmissionStatus,
} from '@models/bw/co/knowvera/form/submission/form-submission-status';

/** The chip tone of a submission status. */
export function submissionTone(status: string): string {
  switch (status) {
    case FormSubmissionStatus.ACCEPTED:
      return 'kv-tone-success';
    case FormSubmissionStatus.SUBMITTED:
      return 'kv-tone-info';
    case FormSubmissionStatus.RETURNED:
      return 'kv-tone-danger';
    case FormSubmissionStatus.DRAFT:
      return 'kv-tone-warning';
    default:
      return 'kv-tone-neutral';
  }
}

/**
 * The custom forms that are part of a KYC record: the published forms of the organisations
 * onboarding the record's client, which the client has to fill.
 */
@Component({
  selector: 'app-required-forms',
  templateUrl: './required-forms.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, RouterLink, TranslateModule],
})
export class RequiredForms {
  /** The KYC record the forms are part of; empty while a new record is not saved yet. */
  readonly kycRecordId = input<string | null>(null);
  /** Whether the forms open in a new tab, so that the page they are shown on keeps its unsaved changes. */
  readonly newTab = input(false);

  private readonly api = inject(FormSubmissionApi);

  readonly submissions = signal<FormSubmissionDTO[]>([]);
  readonly loaded = signal(false);
  readonly failed = signal(false);
  readonly tone = submissionTone;
  private loadedFor: string | null = null;

  /** How many forms the client still has to complete. */
  readonly outstanding = computed(
    () => this.submissions().filter((submission) => this.isEditable(submission)).length,
  );

  constructor() {
    // Loaded once the record exists: asking also adds the forms the record does not have yet
    effect(() => {
      const id = this.kycRecordId();
      if (id && id !== this.loadedFor) {
        this.loadedFor = id;
        this.reload();
      }
    });
  }

  /** Also picks up what was filled in on the form's own page. */
  reload(): void {
    const id = this.kycRecordId();
    if (!id) {
      return;
    }
    this.api.findByKycRecord(id).subscribe({
      next: (submissions) => {
        this.submissions.set(submissions ?? []);
        this.failed.set(false);
        this.loaded.set(true);
      },
      error: () => {
        this.failed.set(true);
        this.loaded.set(true);
      },
    });
  }

  isEditable(submission: FormSubmissionDTO): boolean {
    return EDITABLE_FORM_SUBMISSION_STATUSES.includes(submission.status);
  }
}
