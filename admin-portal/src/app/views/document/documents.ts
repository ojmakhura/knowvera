import { ChangeDetectionStrategy, Component, signal } from '@angular/core';

type SearchResult = {
  icon: string;
  fileName: string;
  documentType: string;
  target: string;
  status: 'Verified' | 'Pending' | 'Flagged';
  statusClass: string;
};

type VaultCard = {
  title: string;
  subtitle: string;
};

@Component({
  selector: 'app-documents',
  templateUrl: './documents.html',
  styleUrls: ['./documents.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Documents {
  protected readonly results = signal<SearchResult[]>([
    {
      icon: '◫',
      fileName: 'KYC_Passport_Smith_J.pdf',
      documentType: 'Identity Verification',
      target: 'Johnathan Smith',
      status: 'Verified',
      statusClass: 'status-verified',
    },
    {
      icon: '▤',
      fileName: 'Utility_Bill_May_2023.pdf',
      documentType: 'Proof of Residence',
      target: 'Alpha Corp Ltd',
      status: 'Pending',
      statusClass: 'status-pending',
    },
    {
      icon: '!',
      fileName: 'Incorporation_Cert_Final.pdf',
      documentType: 'Articles of Association',
      target: 'Global Ventures LLC',
      status: 'Flagged',
      statusClass: 'status-flagged',
    },
    {
      icon: '◪',
      fileName: 'Bank_Stmt_Q1_2023.pdf',
      documentType: 'Financial Statement',
      target: 'Sarah Jenkins',
      status: 'Verified',
      statusClass: 'status-verified',
    },
  ]);

  protected readonly pages = signal([1, 2, 3]);

  protected readonly vaultCards = signal<VaultCard[]>([
    { title: 'Secure Storage', subtitle: 'AES-256 Bit Encryption Active' },
    { title: 'Immutable Logs', subtitle: 'Real-time audit tracking enabled' },
    { title: 'Identity Vault', subtitle: 'Cross-referencing Interpol databases' },
  ]);
}
