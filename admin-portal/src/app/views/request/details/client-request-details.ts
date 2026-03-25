import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy, OnInit, signal } from '@angular/core';

type LabeledValue = {
  label: string;
  value: string;
};

@Component({
  selector: 'app-client-request-details',
  templateUrl: './client-request-details.html',
  styleUrls: ['./client-request-details.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ClientRequestDetails implements OnInit, AfterViewInit, OnDestroy {
  protected readonly profile = signal<LabeledValue[]>([
    { label: 'Request ID', value: 'CR-001' },
    { label: 'Client Name', value: 'Alexander Vance Sterling' },
    { label: 'Email', value: 'a.sterling@nexfix.io' },
    { label: 'Request Type', value: 'KYC Verification' },
    { label: 'Status', value: 'Approved' },
    { label: 'Priority', value: 'Normal' },
    { label: 'Created Date', value: '2025-01-18' },
    { label: 'Organization', value: 'Nexus Financial' },
  ]);

  protected readonly details = signal<LabeledValue[]>([
    { label: 'Request Reference', value: 'CR-001-VX' },
    { label: 'Submission Date', value: '2025-01-18 14:32' },
    { label: 'Assigned Officer', value: 'Compliance Department' },
    { label: 'Review Status', value: 'Complete' },
    { label: 'Approval Date', value: '2025-01-20 09:15' },
    { label: 'Approved By', value: 'm.sterling@veritas.io' },
  ]);

  protected readonly timeline = signal<string[]>([
    '2025-01-20 · Request approved by compliance officer',
    '2025-01-19 · Documents reviewed and verified',
    '2025-01-18 · Request submitted and queued for review',
    '2025-01-18 · Client request created in the portal',
  ]);

  constructor() {}

  ngOnInit(): void {}

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}
}
