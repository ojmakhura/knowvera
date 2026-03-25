import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy, OnInit, signal } from '@angular/core';
import { FormField } from '@angular/forms/signals';
import { MatIconModule } from '@angular/material/icon';

type SequenceRow = {
  id: string;
  icon: string;
  name: string;
  subtitle: string;
  targetEntity: string;
  pattern: string;
  lastModified: string;
};

@Component({
  selector: 'app-sequences',
  templateUrl: './sequences.html',
  styleUrls: ['./sequences.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, FormField],
})
export class Sequences implements OnInit, AfterViewInit, OnDestroy {
  ngOnInit(): void {}
  ngAfterViewInit(): void {}
  ngOnDestroy(): void {}

  protected readonly rows = signal<SequenceRow[]>([
    {
      id: 'SEQ-001',
      icon: 'fingerprint',
      name: 'KYC-AUTH-MASTER',
      subtitle: 'Internal Identity Mapping',
      targetEntity: 'UserAccount',
      pattern: 'VER-{YYYY}-{0000X}',
      lastModified: '2 mins ago',
    },
    {
      id: 'SEQ-002',
      icon: 'account_balance',
      name: 'TRANS-LEDGER-ID',
      subtitle: 'Compliance Reporting Hook',
      targetEntity: 'LedgerEntry',
      pattern: 'TXN-{ORG}-{UUID}',
      lastModified: 'Oct 12, 2023',
    },
    {
      id: 'SEQ-003',
      icon: 'description',
      name: 'DOC-CERT-SEQ',
      subtitle: 'Certified Documentation Hub',
      targetEntity: 'ComplianceDoc',
      pattern: 'CERT-{ISO}-{SEQ}',
      lastModified: '1 week ago',
    },
    {
      id: 'SEQ-004',
      icon: 'hub',
      name: 'VAULT-NODE-KEY',
      subtitle: 'Distributed Node Addressing',
      targetEntity: 'IdentityVault',
      pattern: 'NODE-{CLUSTER}-{HEX8}',
      lastModified: 'Oct 08, 2023',
    },
  ]);

  protected readonly pages = signal([1, 2, 3]);
}
