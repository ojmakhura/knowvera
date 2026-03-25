import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, effect, inject, Input, OnDestroy, OnInit, signal } from '@angular/core';
import { form, FormField, required } from '@angular/forms/signals';
import { RouterLink } from '@angular/router';
import { SequencePartDTO } from '@app/models/bw/co/centralkyc/sequence/sequence-part-dto';
import { SequencePartType } from '@app/models/bw/co/centralkyc/sequence/sequence-part-type';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { SequenceGeneratorApiStore } from '@app/store/bw/co/centralkyc/sequence/sequence-generator-api.store';
import { TranslateModule } from '@ngx-translate/core';


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
    FormField,
    TranslateModule,
    RouterLink
  ]
})
export class SequenceEdit implements OnInit, AfterViewInit, OnDestroy {

  @Input() id: string | any = null;

  sequenceApiStore = inject(SequenceGeneratorApiStore);
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
  loaderMessage = signal('');
  messages = signal({});
  success = signal(false);
  loading = signal(false);
  error = signal(false);
  selected: any = null;

  constructor() {

    effect(() => {

      const sequence = this.sequenceApiStore.data();

      this.editSequenceSignal.update((value) =>( {
        ...value,
        id: sequence.id,
        targetEntity: sequence.targetEntity,
        name: sequence.name,
        sequenceParts: sequence.sequenceParts ?? []
      }));
    });
  }

  ngOnInit(): void {

    if (this.id && this.id !== '') {
      this.sequenceApiStore.findById({ id: this.id });
    }

  }

  ngAfterViewInit(): void {
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

  ngOnDestroy(): void {
  }

}
