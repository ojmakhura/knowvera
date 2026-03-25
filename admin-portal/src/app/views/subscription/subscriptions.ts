import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy, OnInit, signal } from '@angular/core';
import { FormField } from '@angular/forms/signals';
import { MatIconModule } from '@angular/material/icon';

type SubscriptionRow = {
  id: string;
  refId: string;
  organisation: string;
  amount: string;
  status: 'VERIFIED' | 'FLAGGED' | 'PENDING';
  period: string;
  owner: string;
};

@Component({
  selector: 'app-subscriptions',
  templateUrl: './subscriptions.html',
  styleUrls: ['./subscriptions.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, FormField],
})
export class Subscriptions implements OnInit, AfterViewInit, OnDestroy {
  ngOnInit(): void {}
  ngAfterViewInit(): void {}
  ngOnDestroy(): void {}

  protected readonly rows = signal<SubscriptionRow[]>([
    {
      id: 'SUB-001',
      refId: '#VP-2024-001',
      organisation: 'Global Financial Corp',
      amount: '€12,450.00',
      status: 'VERIFIED',
      period: 'Annual (Ends Dec 2024)',
      owner: 'Enterprise Accounts',
    },
    {
      id: 'SUB-002',
      refId: '#VP-2024-089',
      organisation: 'Aether Systems Ltd',
      amount: '€4,200.00',
      status: 'FLAGGED',
      period: 'Monthly (Next: June 15)',
      owner: 'Customer Success',
    },
    {
      id: 'SUB-003',
      refId: '#VP-2024-112',
      organisation: 'NexGen BioTech',
      amount: '€8,900.00',
      status: 'PENDING',
      period: 'Quarterly (Ends Sept 2024)',
      owner: 'Compliance Desk',
    },
    {
      id: 'SUB-004',
      refId: '#VP-2024-245',
      organisation: 'Nordic Logistics SA',
      amount: '€15,750.00',
      status: 'VERIFIED',
      period: 'Annual (Ends Feb 2025)',
      owner: 'Enterprise Accounts',
    },
  ]);

  protected readonly pages = signal([1, 2, 3]);
}
