import { ChangeDetectionStrategy, Component, signal } from '@angular/core';

type SequencePart = {
  position: string;
  name: string;
  type: string;
  initial: string;
  range: string;
  random: boolean;
};

@Component({
  selector: 'app-sequence-edit',
  templateUrl: './sequence-edit.html',
  styleUrls: ['./sequence-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SequenceEdit {
  protected readonly parts = signal<SequencePart[]>([
    {
      position: '01',
      name: 'PREFIX_KYC',
      type: 'Static Prefix',
      initial: 'VER-',
      range: '—',
      random: false,
    },
    {
      position: '02',
      name: 'DATE_STAMP',
      type: 'ISO Date',
      initial: 'YYYYMMDD',
      range: '—',
      random: false,
    },
    {
      position: '03',
      name: 'MAIN_COUNTER',
      type: 'Incrementing Counter',
      initial: '1000',
      range: '1 / 999,999',
      random: true,
    },
  ]);
}
