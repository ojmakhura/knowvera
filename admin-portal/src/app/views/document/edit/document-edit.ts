import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-document-edit',
  templateUrl: './document-edit.html',
  styleUrls: ['./document-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DocumentEdit {}
