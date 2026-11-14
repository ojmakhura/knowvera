import { ChangeDetectionStrategy, Component, computed, inject, input, OnInit, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { KycRecordApi } from '@app/services/bw/co/knowvera/kyc/kyc-record-api';
import { OrganisationApi } from '@app/services/bw/co/knowvera/organisation/organisation-api';
import { KycRecordSummary } from '@app/models/bw/co/knowvera/kyc/kyc-record-summary';
import { OrganisationListDTO } from '@app/models/bw/co/knowvera/organisation/organisation-list-dto';
import { Page } from '@app/models/page.model';
import { complianceTone, labelOf } from './kyc-report-labels';

type ClientKind = 'individuals' | 'organisations';

interface ClientList {
  rows: KycRecordSummary[];
  total: number;
  pageIndex: number;
  loading: boolean;
  failed: boolean;
}

const EMPTY_LIST: ClientList = { rows: [], total: 0, pageIndex: 0, loading: false, failed: false };

/**
 * Pick an organisation, see its clients, and open a client's KYC report as that organisation
 * configured it.
 */
@Component({
  selector: 'app-kyc-reports',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, DatePipe, MatIconModule, MatSelectModule, MatFormFieldModule, MatPaginatorModule],
  templateUrl: './kyc-reports.html',
})
export class KycReports implements OnInit {
  private readonly kycRecordApi = inject(KycRecordApi);
  private readonly organisationApi = inject(OrganisationApi);
  private readonly router = inject(Router);

  /** The organisation chosen, kept in the address so the list survives a reload or a back. */
  readonly organisation = input<string>('');

  protected readonly pageSize = 10;
  protected readonly organisations = signal<OrganisationListDTO[]>([]);
  protected readonly organisationsFailed = signal(false);
  protected readonly selectedId = signal('');
  protected readonly selected = computed(() => this.organisations().find((o) => o.id === this.selectedId()));
  protected readonly lists = signal<Record<ClientKind, ClientList>>({
    individuals: { ...EMPTY_LIST },
    organisations: { ...EMPTY_LIST },
  });

  protected readonly kinds: { kind: ClientKind; title: string; icon: string; identity: string; empty: string }[] = [
    { kind: 'individuals', title: 'Individual Clients', icon: 'person', identity: 'Identity No.', empty: 'No individual clients.' },
    { kind: 'organisations', title: 'Organisation Clients', icon: 'corporate_fare', identity: 'Registration No.', empty: 'No organisation clients.' },
  ];

  protected readonly labelOf = labelOf;
  protected readonly complianceTone = complianceTone;

  ngOnInit(): void {
    this.organisationApi.getAll().subscribe({
      next: (organisations) =>
        this.organisations.set([...(organisations || [])].sort((a, b) => (a.name ?? '').localeCompare(b.name ?? ''))),
      error: () => this.organisationsFailed.set(true),
    });
    if (this.organisation()) {
      this.choose(this.organisation());
    }
  }

  protected choose(organisationId: string): void {
    this.selectedId.set(organisationId || '');
    this.router.navigate([], { queryParams: { organisation: organisationId || null }, replaceUrl: true });
    this.lists.set({ individuals: { ...EMPTY_LIST }, organisations: { ...EMPTY_LIST } });
    if (organisationId) {
      this.load('individuals', 0);
      this.load('organisations', 0);
    }
  }

  protected page(kind: ClientKind, event: PageEvent): void {
    this.load(kind, event.pageIndex);
  }

  private load(kind: ClientKind, pageIndex: number): void {
    const organisationId = this.selectedId();
    this.patch(kind, { loading: true, failed: false, pageIndex });
    const request =
      kind === 'individuals'
        ? this.kycRecordApi.findClientIndividualSummariesPaged(organisationId, pageIndex, this.pageSize)
        : this.kycRecordApi.findClientOrganisationSummariesPaged(organisationId, pageIndex, this.pageSize);
    request.subscribe({
      next: (page: Page<KycRecordSummary>) => {
        if (organisationId !== this.selectedId()) {
          return; // another organisation was chosen meanwhile
        }
        this.patch(kind, {
          rows: page?.content || [],
          total: page?.page?.totalElements ?? page?.totalElements ?? 0,
          loading: false,
        });
      },
      error: () => this.patch(kind, { loading: false, failed: true }),
    });
  }

  private patch(kind: ClientKind, change: Partial<ClientList>): void {
    this.lists.update((lists) => ({ ...lists, [kind]: { ...lists[kind], ...change } }));
  }
}
