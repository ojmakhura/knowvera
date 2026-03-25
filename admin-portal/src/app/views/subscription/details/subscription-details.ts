import { ChangeDetectionStrategy, Component, signal } from '@angular/core';

type OverviewItem = {
  label: string;
  value: string;
};

type TimelineItem = {
  icon: string;
  label: string;
  value: string;
};

type InvoiceItem = {
  ref: string;
  date: string;
  amount: string;
  status: string;
};

@Component({
  selector: 'app-subscription-details',
  templateUrl: './subscription-details.html',
  styleUrls: ['./subscription-details.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SubscriptionDetails {
  protected readonly overview = signal<OverviewItem[]>([
    { label: 'Reference ID', value: 'VP-SUB-9921-XQ' },
    { label: 'Status', value: 'Active' },
    { label: 'Annual Commitment', value: '$42,000 /yr' },
    { label: 'Service Tier', value: 'Enterprise Vault' },
  ]);

  protected readonly timeline = signal<TimelineItem[]>([
    { icon: 'event_available', label: 'Start Date', value: 'January 01, 2024' },
    { icon: 'event_busy', label: 'End Date', value: 'December 31, 2024' },
    { icon: 'schedule', label: 'Billing Period', value: 'Annual (Net-30)' },
  ]);

  protected readonly mapping = signal<OverviewItem[]>([
    { label: 'Legal Entity Name', value: 'Global FinTech Solutions Ltd.' },
    { label: 'Organisation Code', value: 'GFS-EMEA-88' },
    { label: 'Registration No.', value: 'REG-2023-441092-B' },
  ]);

  protected readonly invoices = signal<InvoiceItem[]>([
    { ref: 'INV-2024-001', date: 'Jan 05, 2024', amount: '$42,000.00', status: 'Paid' },
    { ref: 'INV-2023-012', date: 'Dec 05, 2023', amount: '$3,500.00', status: 'Paid' },
  ]);

  protected readonly progressSegments = signal([true, true, true, false, false]);
}
