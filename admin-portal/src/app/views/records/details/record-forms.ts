import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, effect, inject, input, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { FormSubmissionApi } from '@app/services/bw/co/knowvera/form/submission/form-submission-api';
import { FormSubmissionDTO } from '@models/bw/co/knowvera/form/submission/form-submission-dto';
import { submissionTone } from '@app/views/form/submissions/form-submission-details';

/**
 * The custom forms that are part of a KYC record: the forms the organisations onboarding the
 * client require, with where the client stands on each. Shows nothing to a user who may not
 * read form submissions.
 */
@Component({
  selector: 'app-record-forms',
  templateUrl: './record-forms.html',
  styles: [':host { display: block; }'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, RouterLink, TranslateModule],
})
export class RecordForms {
  readonly kycRecordId = input<string | null | undefined>(null);

  private readonly api = inject(FormSubmissionApi);

  readonly submissions = signal<FormSubmissionDTO[]>([]);
  readonly available = signal(false);
  readonly tone = submissionTone;
  private loadedFor: string | null = null;

  constructor() {
    effect(() => {
      const id = this.kycRecordId();
      if (id && id !== this.loadedFor) {
        this.loadedFor = id;
        this.api.findByKycRecord(id).subscribe({
          next: (submissions) => {
            this.submissions.set(submissions ?? []);
            this.available.set(true);
          },
          error: () => this.available.set(false),
        });
      }
    });
  }
}
