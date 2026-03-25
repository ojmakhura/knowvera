import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-invoice-edit',
  templateUrl: './invoice-edit.html',
  styleUrls: ['./invoice-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class InvoiceEdit {}
