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
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { toast } from 'ngx-sonner';
import { swalFire } from '@app/@shared/swal';
import { getErrormessage } from '@app/store/app-state';
import { FormApi } from '@app/services/bw/co/knowvera/form/form-api';
import { OrganisationApi } from '@app/services/bw/co/knowvera/organisation/organisation-api';
import { FormDTO } from '@models/bw/co/knowvera/form/form-dto';
import { FormStatus } from '@models/bw/co/knowvera/form/form-status';
import { FormFieldDTO } from '@models/bw/co/knowvera/form/field/form-field-dto';
import {
  FORM_FIELD_TYPES_WITH_OPTIONS,
  FormFieldType,
} from '@models/bw/co/knowvera/form/field/form-field-type';
import { FormSectionDTO } from '@models/bw/co/knowvera/form/section/form-section-dto';
import { OrganisationListDTO } from '@models/bw/co/knowvera/organisation/organisation-list-dto';
import { TargetEntity } from '@models/bw/co/knowvera/target-entity';

/** A field as it is edited: the options are typed one per line. */
export interface EditFormField {
  id: string | null;
  fieldId: string;
  label: string;
  description: string;
  fieldType: FormFieldType;
  required: boolean;
  defaultValue: string;
  placeholder: string;
  minValue: number | null;
  maxValue: number | null;
  minLength: number | null;
  maxLength: number | null;
  pattern: string;
  optionsText: string;
  /** Whether the field's settings are shown. */
  open: boolean;
}

export interface EditFormSection {
  id: string | null;
  sectionId: string;
  label: string;
  description: string;
  fields: EditFormField[];
}

export interface EditForm {
  id: string | null;
  code: string;
  name: string;
  description: string;
  targetType: TargetEntity;
  status: FormStatus;
  organisationId: string;
  organisation: string;
  sections: EditFormSection[];
  createdAt: Date | null;
  createdBy: string | null;
  modifiedAt: Date | null;
  modifiedBy: string | null;
}

function emptyForm(): EditForm {
  return {
    id: null,
    code: '',
    name: '',
    description: '',
    targetType: TargetEntity.INDIVIDUAL,
    status: FormStatus.DRAFT,
    organisationId: '',
    organisation: '',
    sections: [],
    createdAt: null,
    createdBy: null,
    modifiedAt: null,
    modifiedBy: null,
  };
}

function emptyField(): EditFormField {
  return {
    id: null,
    fieldId: '',
    label: '',
    description: '',
    fieldType: FormFieldType.TEXT,
    required: false,
    defaultValue: '',
    placeholder: '',
    minValue: null,
    maxValue: null,
    minLength: null,
    maxLength: null,
    pattern: '',
    optionsText: '',
    open: true,
  };
}

/**
 * Designs a custom form: its sections and the fields of each section. The whole form is saved
 * at once; the order on the page is the order the client sees.
 */
@Component({
  selector: 'app-form-edit',
  templateUrl: './form-edit.html',
  styleUrl: './form-edit.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, MatTooltipModule, RouterLink, TranslateModule],
})
export class FormEdit implements OnInit {
  @Input() id: string = '';
  /** Query parameter: the organisation a new form is started for. */
  @Input() organisationId: string = '';

  private readonly api = inject(FormApi);
  private readonly organisationApi = inject(OrganisationApi);
  private readonly router = inject(Router);

  readonly model = signal<EditForm>(emptyForm());
  readonly saving = signal(false);
  readonly organisations = signal<OrganisationListDTO[]>([]);

  readonly fieldTypes = Object.values(FormFieldType);
  readonly statuses = Object.values(FormStatus);
  readonly targetTypes = [TargetEntity.INDIVIDUAL, TargetEntity.ORGANISATION];

  /** What still stops the form from being saved, in words. */
  readonly problems = computed(() => {
    const form = this.model();
    const problems: string[] = [];
    if (!form.organisationId) {
      problems.push('Choose the organisation the form belongs to.');
    }
    if (!form.code.trim()) {
      problems.push('Give the form a code.');
    } else if (!/^[A-Za-z0-9][A-Za-z0-9_-]{0,49}$/.test(form.code.trim())) {
      problems.push("The code takes up to 50 letters, digits, '-' or '_'.");
    }
    if (!form.name.trim()) {
      problems.push('Give the form a name.');
    }
    form.sections.forEach((section, index) => {
      const name = section.label.trim() || `Section ${index + 1}`;
      if (!section.label.trim()) {
        problems.push(`${name} needs a label.`);
      }
      section.fields.forEach((field, fieldIndex) => {
        if (!field.label.trim()) {
          problems.push(`Field ${fieldIndex + 1} of ${name} needs a label.`);
        } else if (this.hasOptions(field) && this.options(field).length === 0) {
          problems.push(`"${field.label.trim()}" needs options to choose from.`);
        }
      });
    });
    if (
      form.status === FormStatus.PUBLISHED &&
      !form.sections.some((section) => section.fields.length > 0)
    ) {
      problems.push('A form without fields cannot be published.');
    }
    return problems;
  });

  ngOnInit(): void {
    this.organisationApi.getAll().subscribe({
      next: (organisations) => this.organisations.set(organisations ?? []),
      error: (error) => toast.error(getErrormessage(error)),
    });
    if (this.id) {
      this.load();
    } else if (this.organisationId) {
      this.model.update((form) => ({ ...form, organisationId: this.organisationId }));
    }
  }

  private load(): void {
    this.api.findById(this.id).subscribe({
      next: (form) => this.model.set(this.toModel(form)),
      error: (error) => toast.error(getErrormessage(error)),
    });
  }

  discardChanges(): void {
    if (this.id) {
      this.load();
    } else {
      this.model.set(emptyForm());
    }
  }

  // ---- form -------------------------------------------------------------

  patch(change: Partial<EditForm>): void {
    this.model.update((form) => ({ ...form, ...change }));
  }

  // ---- sections ---------------------------------------------------------

  addSection(): void {
    this.model.update((form) => ({
      ...form,
      sections: [...form.sections, { id: null, sectionId: '', label: '', description: '', fields: [] }],
    }));
  }

  patchSection(index: number, change: Partial<EditFormSection>): void {
    this.model.update((form) => ({
      ...form,
      sections: form.sections.map((section, i) => (i === index ? { ...section, ...change } : section)),
    }));
  }

  moveSection(index: number, by: number): void {
    this.model.update((form) => ({ ...form, sections: this.moved(form.sections, index, by) }));
  }

  removeSection(index: number): void {
    const section = this.model().sections[index];
    const remove = () =>
      this.model.update((form) => ({
        ...form,
        sections: form.sections.filter((_, i) => i !== index),
      }));
    if (section.fields.length === 0) {
      remove();
      return;
    }
    swalFire({
      title: 'Remove the section?',
      text: `"${section.label || 'This section'}" and its ${section.fields.length} field(s) are removed when the form is saved.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Remove',
    }).then((result: any) => {
      if (result.isConfirmed) {
        remove();
      }
    });
  }

  // ---- fields -----------------------------------------------------------

  addField(sectionIndex: number): void {
    const section = this.model().sections[sectionIndex];
    this.patchSection(sectionIndex, { fields: [...section.fields, emptyField()] });
  }

  patchField(sectionIndex: number, index: number, change: Partial<EditFormField>): void {
    const section = this.model().sections[sectionIndex];
    this.patchSection(sectionIndex, {
      fields: section.fields.map((field, i) => (i === index ? { ...field, ...change } : field)),
    });
  }

  moveField(sectionIndex: number, index: number, by: number): void {
    const section = this.model().sections[sectionIndex];
    this.patchSection(sectionIndex, { fields: this.moved(section.fields, index, by) });
  }

  removeField(sectionIndex: number, index: number): void {
    const section = this.model().sections[sectionIndex];
    this.patchSection(sectionIndex, { fields: section.fields.filter((_, i) => i !== index) });
  }

  hasOptions(field: EditFormField): boolean {
    return FORM_FIELD_TYPES_WITH_OPTIONS.includes(field.fieldType);
  }

  isText(field: EditFormField): boolean {
    return [FormFieldType.TEXT, FormFieldType.TEXTAREA, FormFieldType.TEL].includes(field.fieldType);
  }

  isNumber(field: EditFormField): boolean {
    return field.fieldType === FormFieldType.NUMBER;
  }

  /** The options of the field: one per line, without blanks and repeats. */
  options(field: EditFormField): string[] {
    return [
      ...new Set(
        field.optionsText
          .split('\n')
          .map((option) => option.trim())
          .filter((option) => option.length > 0),
      ),
    ];
  }

  /** The value of a number input: empty is no limit. */
  numberOf(value: string): number | null {
    return value === '' || Number.isNaN(Number(value)) ? null : Number(value);
  }

  private moved<T>(items: T[], index: number, by: number): T[] {
    const target = index + by;
    if (target < 0 || target >= items.length) {
      return items;
    }
    const result = [...items];
    [result[index], result[target]] = [result[target], result[index]];
    return result;
  }

  // ---- saving -----------------------------------------------------------

  save(): void {
    if (this.problems().length > 0) {
      toast.error(this.problems()[0]);
      return;
    }
    const creating = !this.model().id;
    this.saving.set(true);
    this.api.save(this.toDTO(this.model())).subscribe({
      next: (saved) => {
        this.saving.set(false);
        toast.success(`Form "${saved.name}" saved.`);
        if (creating) {
          this.router.navigate(['/', 'forms', saved.id, 'edit']);
        }
        this.model.set(this.toModel(saved));
      },
      error: (error) => {
        this.saving.set(false);
        toast.error(getErrormessage(error));
      },
    });
  }

  private toModel(form: FormDTO): EditForm {
    return {
      id: form.id ?? null,
      code: form.code ?? '',
      name: form.name ?? '',
      description: form.description ?? '',
      targetType: form.targetType ?? TargetEntity.INDIVIDUAL,
      status: form.status ?? FormStatus.DRAFT,
      organisationId: form.organisationId ?? '',
      organisation: form.organisation ?? '',
      createdAt: form.createdAt ?? null,
      createdBy: form.createdBy ?? null,
      modifiedAt: form.modifiedAt ?? null,
      modifiedBy: form.modifiedBy ?? null,
      sections: (form.sections ?? []).map((section: FormSectionDTO) => ({
        id: section.id ?? null,
        sectionId: section.sectionId ?? '',
        label: section.label ?? '',
        description: section.description ?? '',
        fields: (section.fields ?? []).map((field: FormFieldDTO) => ({
          id: field.id ?? null,
          fieldId: field.fieldId ?? '',
          label: field.label ?? '',
          description: field.description ?? '',
          fieldType: field.fieldType ?? FormFieldType.TEXT,
          required: !!field.required,
          defaultValue: field.defaultValue ?? '',
          placeholder: field.placeholder ?? '',
          minValue: field.minValue ?? null,
          maxValue: field.maxValue ?? null,
          minLength: field.minLength ?? null,
          maxLength: field.maxLength ?? null,
          pattern: field.pattern ?? '',
          optionsText: (field.options ?? []).join('\n'),
          open: false,
        })),
      })),
    };
  }

  private toDTO(model: EditForm): FormDTO {
    const form = new FormDTO();
    form.id = model.id;
    form.code = model.code.trim();
    form.name = model.name.trim();
    form.description = model.description.trim() || null;
    form.targetType = model.targetType;
    form.status = model.status;
    form.organisationId = model.organisationId;
    form.sections = model.sections.map((section) => {
      const sectionDTO = new FormSectionDTO();
      sectionDTO.id = section.id;
      sectionDTO.sectionId = section.sectionId || null;
      sectionDTO.label = section.label.trim();
      sectionDTO.description = section.description.trim() || null;
      sectionDTO.fields = section.fields.map((field) => {
        const fieldDTO = new FormFieldDTO();
        fieldDTO.id = field.id;
        fieldDTO.fieldId = field.fieldId || null;
        fieldDTO.label = field.label.trim();
        fieldDTO.description = field.description.trim() || null;
        fieldDTO.fieldType = field.fieldType;
        fieldDTO.required = field.required;
        fieldDTO.defaultValue = field.defaultValue.trim() || null;
        fieldDTO.placeholder = field.placeholder.trim() || null;
        fieldDTO.minValue = this.isNumber(field) ? field.minValue : null;
        fieldDTO.maxValue = this.isNumber(field) ? field.maxValue : null;
        fieldDTO.minLength = this.isText(field) ? field.minLength : null;
        fieldDTO.maxLength = this.isText(field) ? field.maxLength : null;
        fieldDTO.pattern = this.isText(field) ? field.pattern.trim() || null : null;
        fieldDTO.options = this.hasOptions(field) ? this.options(field) : [];
        return fieldDTO;
      });
      return sectionDTO;
    });
    return form;
  }
}
