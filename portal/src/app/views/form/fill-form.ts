import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  OnInit,
  signal,
} from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { Observable, switchMap } from 'rxjs';
import { toast } from '@app/@shared/toast';
import { getErrormessage } from '@app/store/app-state';
import { FormSubmissionApi } from '@app/services/bw/co/knowvera/form/submission/form-submission-api';
import { FormFieldDTO } from '@models/bw/co/knowvera/form/field/form-field-dto';
import { FormSectionDTO } from '@models/bw/co/knowvera/form/section/form-section-dto';
import { FormFieldValueDTO } from '@models/bw/co/knowvera/form/submission/form-field-value-dto';
import { FormSubmissionDTO } from '@models/bw/co/knowvera/form/submission/form-submission-dto';
import {
  EDITABLE_FORM_SUBMISSION_STATUSES,
  FormSubmissionStatus,
} from '@models/bw/co/knowvera/form/submission/form-submission-status';
import { submissionTone } from './required-forms';

/** The HTML input type of the field types captured with a plain input. */
const INPUT_TYPES: Record<string, string> = {
  TEXT: 'text',
  NUMBER: 'number',
  DATE: 'date',
  DATETIME: 'datetime-local',
  TIME: 'time',
  EMAIL: 'email',
  TEL: 'tel',
  URL: 'url',
};

/**
 * A client fills one of the custom forms required for its KYC: saves the answers as a draft,
 * then submits them to the organisation. Read only once submitted.
 */
@Component({
  selector: 'app-fill-form',
  templateUrl: './fill-form.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, TranslateModule],
})
export class FillForm implements OnInit {
  private readonly api = inject(FormSubmissionApi);
  private readonly route = inject(ActivatedRoute);

  private id = '';

  readonly submission = signal<FormSubmissionDTO | null>(null);
  /** The answers by field id; the chosen options of a multiple choice in {@link choices}. */
  readonly answers = signal<Record<string, string>>({});
  readonly choices = signal<Record<string, string[]>>({});
  readonly busy = signal(false);
  readonly tone = submissionTone;

  readonly sections = computed<FormSectionDTO[]>(() => this.submission()?.form?.sections ?? []);
  readonly editable = computed(() =>
    EDITABLE_FORM_SUBMISSION_STATUSES.includes(this.submission()?.status),
  );
  readonly returned = computed(() => this.submission()?.status === FormSubmissionStatus.RETURNED);

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id') ?? '';
    this.api.findById(this.id).subscribe({
      next: (submission) => this.show(submission),
      error: (error) => toast.error(getErrormessage(error)),
    });
  }

  /** Takes over a submission from the server: its answers, or the defaults of a form not yet started. */
  private show(submission: FormSubmissionDTO): void {
    const answers: Record<string, string> = {};
    const choices: Record<string, string[]> = {};
    if (submission.status === FormSubmissionStatus.NEW) {
      for (const section of submission.form?.sections ?? []) {
        for (const field of section.fields ?? []) {
          if (field.defaultValue && field.fieldType !== 'MULTI_SELECT') {
            answers[field.fieldId] = field.defaultValue;
          }
        }
      }
    }
    for (const value of submission.values ?? []) {
      answers[value.fieldId] = value.value ?? '';
      choices[value.fieldId] = value.values ?? [];
    }
    this.submission.set(submission);
    this.answers.set(answers);
    this.choices.set(choices);
  }

  inputType(field: FormFieldDTO): string | null {
    return INPUT_TYPES[field.fieldType] ?? null;
  }

  answer(field: FormFieldDTO): string {
    return this.answers()[field.fieldId] ?? '';
  }

  setAnswer(field: FormFieldDTO, value: string): void {
    this.answers.update((answers) => ({ ...answers, [field.fieldId]: value }));
  }

  isChosen(field: FormFieldDTO, option: string): boolean {
    return (this.choices()[field.fieldId] ?? []).includes(option);
  }

  toggleChoice(field: FormFieldDTO, option: string, chosen: boolean): void {
    this.choices.update((choices) => {
      const current = choices[field.fieldId] ?? [];
      return {
        ...choices,
        [field.fieldId]: chosen
          ? [...current.filter((item) => item !== option), option]
          : current.filter((item) => item !== option),
      };
    });
  }

  /** Every field with its answer; an empty answer clears what was saved before. */
  private values(): FormFieldValueDTO[] {
    const values: FormFieldValueDTO[] = [];
    for (const section of this.sections()) {
      for (const field of section.fields ?? []) {
        const value = new FormFieldValueDTO();
        value.fieldId = field.fieldId;
        if (field.fieldType === 'MULTI_SELECT') {
          value.values = this.choices()[field.fieldId] ?? [];
        } else {
          value.value = this.answers()[field.fieldId] ?? '';
        }
        values.push(value);
      }
    }
    return values;
  }

  saveDraft(): void {
    this.run(this.api.saveValues(this.id, this.values()), 'Your answers are saved.');
  }

  submit(): void {
    this.run(
      this.api.saveValues(this.id, this.values()).pipe(switchMap(() => this.api.submit(this.id))),
      'The form is submitted.',
    );
  }

  private run(request: Observable<FormSubmissionDTO>, message: string): void {
    this.busy.set(true);
    request.subscribe({
      next: (submission) => {
        this.busy.set(false);
        this.show(submission);
        toast.success(message);
      },
      error: (error) => {
        this.busy.set(false);
        toast.error(getErrormessage(error));
      },
    });
  }
}
