import { ChangeDetectionStrategy, Component, signal } from '@angular/core';

type ContextItem = {
  label: string;
  value: string;
};

type AuditItem = {
  label: string;
  timestamp: string;
  user: string;
};

type RepositoryFile = {
  name: string;
  type: string;
  icon: string;
};

@Component({
  selector: 'app-invoice-details',
  templateUrl: './invoice-details.html',
  styleUrls: ['./invoice-details.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class InvoiceDetails {
  protected readonly context = signal<ContextItem[]>([
    { label: 'Legal Entity Name', value: 'Global Logistics Partners Ltd.' },
    { label: 'Organisation Code', value: 'GLP-EMEA-04' },
    { label: 'Registration No.', value: 'UK-9920384112' },
    { label: 'Internal Vault ID', value: 'ORG-772-B1' },
  ]);

  protected readonly audit = signal<AuditItem[]>([
    {
      label: 'Original Entry',
      timestamp: '2023-10-24 14:22:10',
      user: 'admin_sarah.j',
    },
    {
      label: 'Last Modification',
      timestamp: '2023-10-25 09:45:00',
      user: 'compliance_officer_k',
    },
  ]);

  protected readonly files = signal<RepositoryFile[]>([
    { name: 'INV-2024-0892.pdf', type: 'Invoice Document', icon: 'description' },
    { name: 'SWIFT_REF_982341.jpg', type: 'Proof of Payment', icon: 'verified_user' },
  ]);
}
