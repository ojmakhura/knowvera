import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatDividerModule } from '@angular/material/divider';
import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, effect, inject, Input, linkedSignal, OnDestroy, OnInit, signal } from '@angular/core';
import { form, required } from '@angular/forms/signals';
import { Router } from '@angular/router';
import { SequenceGeneratorDTO } from '@app/models/bw/co/knowvera/sequence/sequence-generator-dto';
import { SequencePartDTO } from '@app/models/bw/co/knowvera/sequence/sequence-part-dto';
import { SequencePartType } from '@app/models/bw/co/knowvera/sequence/sequence-part-type';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { SequenceGeneratorApiStore } from '@app/store/bw/co/knowvera/sequence/sequence-generator-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { Loader } from '@app/@shared/loader/loader';


export class EditSequenceVarsForm {
  id: string | any = null;
  targetEntity: TargetEntity | any = null;
  name: string | any = null;
  sequenceParts: SequencePartDTO[] = [];
}

@Component({
  selector: 'app-sequence-edit',
  standalone: true,
  templateUrl: './sequence-edit.html',
  styleUrl: './sequence-edit.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    TranslateModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    MatCheckboxModule,
    MatSlideToggleModule,
    MatDividerModule,
    Loader
  ]
})
export class SequenceEdit implements OnInit, AfterViewInit, OnDestroy {

  @Input() id: string | any = null;

  sequenceApiStore = inject(SequenceGeneratorApiStore);
  router = inject(Router);
  editSequenceVarsForm: EditSequenceVarsForm = new EditSequenceVarsForm();
  editSequenceSignal = signal(this.editSequenceVarsForm);
  editSequenceSignalForm = form(this.editSequenceSignal, (path) => {
    required(path.targetEntity, { message: 'target.entity.required' })
    required(path.name, { message: 'name.required' })
    required(path.sequenceParts, { message: 'sequence.parts.required' })
  });

  TargetEntityT: any = TargetEntity;
  TargetEntityOptions = Object.keys(this.TargetEntityT);
  SequencePartTypeT: any = SequencePartType;
  SequencePartTypeOptions = Object.keys(this.SequencePartTypeT);
  dialogOpen = signal(false);
  newPart = signal({
    name: '',
    type: this.SequencePartTypeOptions[0] ?? '',
    initialValue: '',
    min: '',
    max: '',
    randomised: false
  });
  loaderMessage = linkedSignal(() => this.sequenceApiStore.loaderMessage());
  messages = linkedSignal(() => this.sequenceApiStore.messages());
  success = linkedSignal(() => this.sequenceApiStore.success());
  loading = linkedSignal(() => this.sequenceApiStore.loading());
  error = linkedSignal(() => this.sequenceApiStore.error());
  selected: any = null;

  saving = false;

  toastr = inject(ToastrService);

  constructor() {

    this.sequenceApiStore.reset();

    effect(() => {
      const sequence = this.sequenceApiStore.data();

      if (!sequence) {
        return;
      }

      this.editSequenceSignal.update((value) => ({
        ...value,
        id: sequence.id,
        targetEntity: sequence.targetEntity,
        name: sequence.name,
        sequenceParts: sequence.sequenceParts ?? []
      }));
    });

    effect(() => {
      const error = this.error();
      if (error) {
        this.toastr.error(this.messages()[0], 'Error ');
      }
    });

    effect(() => {
      const success = this.success();
      if (success) {
        this.toastr.success(this.messages()[0], 'Success');
      }
    });
  }

  ngOnInit(): void {

    if (this.id && this.id !== '') {
      this.sequenceApiStore.findById({ id: this.id });
    }

  }

  ngAfterViewInit(): void {
  }

  saveSequence(): void {
    const formValue = this.editSequenceSignal();

    if (!formValue.name || !formValue.targetEntity) {
      return;
    }
    this.saving = true;
    this.sequenceApiStore.save({
      SequenceGeneratorDTO: this.sequencePayload()
    });
  }

  cancel(): void {
    this.router.navigate(['/sequence']);
  }

  openAddPartDialog(): void {
    this.dialogOpen.set(true);
  }

  closeAddPartDialog(): void {
    this.dialogOpen.set(false);
    this.newPart.set({
      name: '',
      type: this.SequencePartTypeOptions[0] ?? '',
      initialValue: '',
      min: '',
      max: '',
      randomised: false
    });
  }

  updateNewPart<K extends keyof ReturnType<typeof this.newPart>>(field: K, value: ReturnType<typeof this.newPart>[K]): void {
    this.newPart.update((current) => ({
      ...current,
      [field]: value
    }));
  }

  addSequencePart(): void {
    const draft = this.newPart();
    if (!draft.name || !draft.type) {
      return;
    }

    const nextPosition = this.editSequenceSignal().sequenceParts.length + 1;
    const newPart: SequencePartDTO = {
      id: null,
      position: nextPosition,
      name: draft.name,
      type: draft.type,
      initialValue: draft.initialValue,
      min: draft.min,
      max: draft.max,
      randomised: draft.randomised,
      currentValue: draft.initialValue
    };

    this.editSequenceSignal.update((value) => ({
      ...value,
      sequenceParts: [...value.sequenceParts, newPart]
    }));

    this.closeAddPartDialog();
  }

  removeSequencePart(index: number): void {
    this.editSequenceSignal.update((value) => {
      const nextParts = value.sequenceParts
        .filter((_, partIndex) => partIndex !== index)
        .map((part, idx) => ({
          ...part,
          position: idx + 1
        }));

      return {
        ...value,
        sequenceParts: nextParts
      };
    });
  }

  generatedPreview(): string {
    const formValue = this.editSequenceSignal();
    const preview = [...(formValue.sequenceParts || [])]
      .sort((left, right) => (left.position || 0) - (right.position || 0))
      .map((part) => this.previewToken(part))
      .join('');

    return preview || 'VER-20231027-1001';
  }

  targetEntityLabel(value: string | null | undefined): string {
    return String(value || 'SEQUENCE').replaceAll('_', ' ');
  }

  partTypeLabel(value: string | null | undefined): string {
    return String(value || 'UNDEFINED').replaceAll('_', ' ');
  }

  rangeLabel(part: SequencePartDTO): string {
    if (part.type !== SequencePartType.COUNTER) {
      return '—';
    }

    const min = part.min || '1';
    const max = part.max || '999,999';
    return `${min} / ${max}`;
  }

  initialLabel(part: SequencePartDTO): string {
    const initial = part.initialValue || part.currentValue;
    return initial || '—';
  }

  isDraft(): boolean {
    return !this.editSequenceSignal().id;
  }

  auditVersion(): string {
    return this.editSequenceSignal().id ? `Record ${this.editSequenceSignal().id}` : 'Draft';
  }

  private sequencePayload(): SequenceGeneratorDTO {
    const payload = new SequenceGeneratorDTO();
    const current = this.editSequenceSignal();

    payload.id = current.id;
    payload.name = current.name;
    payload.targetEntity = current.targetEntity;
    payload.sequenceParts = current.sequenceParts.map((part, index) => ({
      ...part,
      position: index + 1,
      currentValue: part.currentValue || part.initialValue,
    }));

    return payload;
  }

  private previewToken(part: SequencePartDTO): string {
    if (part.initialValue) {
      return part.initialValue;
    }

    switch (part.type) {
      case SequencePartType.YEAR:
        return '2023';
      case SequencePartType.MONTH:
        return '1027';
      case SequencePartType.COUNTER:
        return part.min || '1001';
      case SequencePartType.STATIC:
        return part.name || 'VER-';
      default:
        return part.name || 'X';
    }
  }

  ngOnDestroy(): void {
  }

}
