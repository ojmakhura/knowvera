import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

type LabeledValue = {
  label: string;
  value: string;
};

type ComplianceItem = {
  label: string;
  status: 'ok' | 'review' | 'pending';
  detail: string;
};

@Component({
  selector: 'app-individual-details',
  imports: [CommonModule, RouterLink],
  templateUrl: './individual-details.html',
  styleUrl: './individual-details.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class IndividualDetails implements OnInit, AfterViewInit, OnDestroy {
  protected readonly profile = signal<LabeledValue[]>([
    { label: 'Individual ID', value: 'IND-000274' },
    { label: 'Full Name', value: 'Mina Al Harbi' },
    { label: 'Category', value: 'Individual' },
    { label: 'Status', value: 'Active' },
    { label: 'Risk Level', value: 'Medium' },
    { label: 'Nationality', value: 'Saudi Arabia' },
    { label: 'Date of Birth', value: '1991-03-18' },
    { label: 'Country of Residence', value: 'Saudi Arabia' },
  ]);

  protected readonly identifiers = signal<LabeledValue[]>([
    { label: 'Primary Document', value: 'National ID' },
    { label: 'Document Number', value: '1098****472' },
    { label: 'Issue Date', value: '2021-05-10' },
    { label: 'Expiry Date', value: '2031-05-09' },
    { label: 'Tax Number', value: 'TX-00190844' },
    { label: 'PEP Flag', value: 'No' },
  ]);

  protected readonly contact = signal<LabeledValue[]>([
    { label: 'Primary Email', value: 'mina.alharbi@example.com' },
    { label: 'Mobile Number', value: '+966 55 000 7821' },
    { label: 'Secondary Number', value: '+966 11 000 3321' },
    { label: 'Preferred Contact', value: 'Email' },
    { label: 'Address', value: 'Riyadh, Saudi Arabia' },
  ]);

  protected readonly compliance = signal<ComplianceItem[]>([
    { label: 'KYC Verification', status: 'ok', detail: 'Verified on 2025-01-18' },
    { label: 'Sanctions Screening', status: 'ok', detail: 'No matches detected' },
    { label: 'AML Review', status: 'review', detail: 'Enhanced due diligence scheduled' },
    { label: 'Document Refresh', status: 'pending', detail: 'Renewal due in 42 days' },
  ]);

  protected readonly timeline = signal<string[]>([
    '2025-01-18 · KYC profile approved by compliance officer',
    '2024-10-04 · Address verification document updated',
    '2024-07-22 · Individual profile status set to Active',
    '2024-07-21 · Individual profile created in the portal',
  ]);

    constructor() {}

    ngOnInit(): void {}

    ngAfterViewInit(): void {}

    ngOnDestroy(): void {}
}
