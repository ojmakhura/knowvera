import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { FormSubmissionApi } from '@app/services/bw/co/knowvera/form/submission/form-submission-api';
import { FormSubmissionDTO } from '@models/bw/co/knowvera/form/submission/form-submission-dto';
import {
  EDITABLE_FORM_SUBMISSION_STATUSES,
  FormSubmissionStatus,
} from '@models/bw/co/knowvera/form/submission/form-submission-status';

/**
 * Prompts a client to fill the forms that are outstanding on its KYC records, for instance a
 * form an organisation published after the client's KYC was already done. Shows nothing when
 * there is nothing to fill.
 */
@Component({
  selector: 'app-forms-prompt',
  templateUrl: './forms-prompt.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, RouterLink],
})
export class FormsPrompt implements OnInit {
  private readonly api = inject(FormSubmissionApi);

  private readonly submissions = signal<FormSubmissionDTO[]>([]);

  /** The forms the caller still has to complete, returned ones first. */
  readonly outstanding = computed(() =>
    this.submissions()
      .filter((submission) => EDITABLE_FORM_SUBMISSION_STATUSES.includes(submission.status))
      .sort((left, right) => Number(this.isReturned(right)) - Number(this.isReturned(left))),
  );

  ngOnInit(): void {
    // Asking also adds to the caller's KYC records the forms they do not have yet
    this.api.findMySubmissions().subscribe({
      next: (submissions) => this.submissions.set(submissions ?? []),
      error: () => this.submissions.set([]),
    });
  }

  isReturned(submission: FormSubmissionDTO): boolean {
    return submission.status === FormSubmissionStatus.RETURNED;
  }
}
