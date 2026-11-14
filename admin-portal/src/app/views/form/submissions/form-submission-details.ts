import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  Input,
  OnInit,
  signal,
} from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { toast } from 'ngx-sonner';
import { swalFire } from '@app/@shared/swal';
import { hasAnyPermission } from '@app/auth/permissions';
import { getErrormessage } from '@app/store/app-state';
import { KeycloakHelperApiStore } from '@app/store/bw/co/knowvera/keycloak/keycloak-helper-api.store';
import { FormSubmissionApi } from '@app/services/bw/co/knowvera/form/submission/form-submission-api';
import { FormFieldDTO } from '@models/bw/co/knowvera/form/field/form-field-dto';
import { FormSectionDTO } from '@models/bw/co/knowvera/form/section/form-section-dto';
import { FormFieldValueDTO } from '@models/bw/co/knowvera/form/submission/form-field-value-dto';
import { FormSubmissionDTO } from '@models/bw/co/knowvera/form/submission/form-submission-dto';
import { FormSubmissionStatus } from '@models/bw/co/knowvera/form/submission/form-submission-status';

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
 * A client's answers to a form, section by section, and the decision on them: accept, or return
 * to the client with a note on what to correct.
 */
@Component({
  selector: 'app-form-submission-details',
  templateUrl: './form-submission-details.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, RouterLink, TranslateModule],
})
export class FormSubmissionDetails implements OnInit {
  @Input() id: string = '';

  private readonly api = inject(FormSubmissionApi);
  private readonly permissionStore = inject(KeycloakHelperApiStore);
  private readonly permissions = computed(() => this.permissionStore.permissions() ?? []);

  readonly submission = signal<FormSubmissionDTO | null>(null);
  readonly tone = submissionTone;

  readonly canReview = computed(
    () =>
      hasAnyPermission(this.permissions(), ['form-submissions:review']) &&
      this.submission()?.status === FormSubmissionStatus.SUBMITTED,
  );

  /** The sections of the form with the answer to each field. */
  readonly sections = computed(() => {
    const submission = this.submission();
    const answers = new Map<string, FormFieldValueDTO>(
      (submission?.values ?? []).map((value: FormFieldValueDTO) => [value.fieldId, value]),
    );
    return (submission?.form?.sections ?? []).map((section: FormSectionDTO) => ({
      label: section.label,
      description: section.description,
      fields: (section.fields ?? []).map((field: FormFieldDTO) => ({
        label: field.label,
        required: !!field.required,
        answer: this.answerText(field, answers.get(field.fieldId)),
      })),
    }));
  });

  ngOnInit(): void {
    this.api.findById(this.id).subscribe({
      next: (submission) => this.submission.set(submission),
      error: (error) => toast.error(getErrormessage(error)),
    });
  }

  private answerText(field: FormFieldDTO, value?: FormFieldValueDTO): string {
    if (!value) {
      return '';
    }
    if (field.fieldType === 'MULTI_SELECT') {
      return (value.values ?? []).join(', ');
    }
    if (field.fieldType === 'CHECKBOX') {
      return value.value === 'true' ? 'Yes' : 'No';
    }
    return value.value ?? '';
  }

  accept(): void {
    swalFire({
      title: 'Accept the answers?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Accept',
      reverseButtons: true,
    }).then((result: any) => {
      if (result.isConfirmed) {
        this.decide(FormSubmissionStatus.ACCEPTED, undefined, 'Accepted');
      }
    });
  }

  returnToClient(): void {
    swalFire({
      title: 'Return to the client',
      input: 'textarea',
      inputPlaceholder: 'What the client has to correct',
      inputValidator: (value: string) => (!value || !value.trim() ? 'This is required' : null),
      showCancelButton: true,
      confirmButtonText: 'Return',
      reverseButtons: true,
    }).then((result: any) => {
      if (result.isConfirmed) {
        this.decide(FormSubmissionStatus.RETURNED, result.value.trim(), 'Returned to the client');
      }
    });
  }

  private decide(status: FormSubmissionStatus, note: string | undefined, message: string): void {
    this.api.updateStatus(this.id, status, note).subscribe({
      next: (submission) => {
        this.submission.set(submission);
        toast.success(message);
      },
      error: (error) => toast.error(getErrormessage(error)),
    });
  }
}
