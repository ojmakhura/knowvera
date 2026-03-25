import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy, OnInit, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from 'node_modules/@angular/common/types/_common_module-chunk';
import { FormField } from 'node_modules/@angular/forms/types/_structure-chunk';
import { MatIconModule } from 'node_modules/@angular/material/types/_icon-module-chunk';
import { RouterLink } from 'node_modules/@angular/router/types/_router_module-chunk';

type InvoiceRow = {
  id: string;
  ref: string;
  organisation: string;
  billingDate: string;
  amount: string;
  status: 'VERIFIED PAID' | 'AWAITING SETTLEMENT' | 'OVERDUE';
  issuedBy: string;
};

@Component({
  selector: 'app-invoices',
  templateUrl: './invoices.html',
  styleUrls: ['./invoices.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, MatButtonModule, FormField, RouterLink],
})
export class Invoices implements OnInit, AfterViewInit, OnDestroy {
  ngOnInit(): void {}
  ngAfterViewInit(): void {}
  ngOnDestroy(): void {}

  protected readonly rows = signal<InvoiceRow[]>([
    {
      id: 'INV-001',
      ref: '#INV-2024-001',
      organisation: 'Global Tech Solutions',
      billingDate: 'Oct 12, 2024',
      amount: '€42,000.00',
      status: 'VERIFIED PAID',
      issuedBy: 'Billing Operations',
    },
    {
      id: 'INV-002',
      ref: '#INV-2024-002',
      organisation: 'FinEdge Compliance Ltd',
      billingDate: 'Oct 14, 2024',
      amount: '€12,500.00',
      status: 'AWAITING SETTLEMENT',
      issuedBy: 'Finance Control',
    },
    {
      id: 'INV-003',
      ref: '#INV-2024-003',
      organisation: 'SecureFlow Systems',
      billingDate: 'Oct 15, 2024',
      amount: '€8,900.50',
      status: 'VERIFIED PAID',
      issuedBy: 'Accounts Receivable',
    },
    {
      id: 'INV-004',
      ref: '#INV-2024-004',
      organisation: 'Apex Capital Partners',
      billingDate: 'Oct 16, 2024',
      amount: '€3,200.00',
      status: 'OVERDUE',
      issuedBy: 'Settlement Desk',
    },
  ]);

  protected readonly pages = signal([1, 2, 3]);
}
