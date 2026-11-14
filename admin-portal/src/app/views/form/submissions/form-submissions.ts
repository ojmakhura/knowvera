import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, Input, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { toast } from 'ngx-sonner';
import { getErrormessage } from '@app/store/app-state';
import { FormApi } from '@app/services/bw/co/knowvera/form/form-api';
import { FormSubmissionApi } from '@app/services/bw/co/knowvera/form/submission/form-submission-api';
import { FormDTO } from '@models/bw/co/knowvera/form/form-dto';
import { FormSubmissionDTO } from '@models/bw/co/knowvera/form/submission/form-submission-dto';
import { submissionTone } from './form-submission-details';

/**
 * The clients that have a form to fill, and where each of them stands.
 */
@Component({
  selector: 'app-form-submissions',
  templateUrl: './form-submissions.html',
  styleUrl: '../forms.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, RouterLink, TranslateModule],
})
export class FormSubmissions implements OnInit {
  @Input() id: string = '';

  private readonly formApi = inject(FormApi);
  private readonly api = inject(FormSubmissionApi);

  readonly form = signal<FormDTO | null>(null);
  readonly submissions = signal<FormSubmissionDTO[]>([]);
  readonly tone = submissionTone;

  ngOnInit(): void {
    this.formApi.findById(this.id).subscribe({
      next: (form) => this.form.set(form),
      error: (error) => toast.error(getErrormessage(error)),
    });
    this.api.findByForm(this.id).subscribe({
      next: (submissions) => this.submissions.set(submissions ?? []),
      error: (error) => toast.error(getErrormessage(error)),
    });
  }
}
