import { ChangeDetectionStrategy, Component, computed, effect, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { KycRecordApi } from '@app/services/bw/co/knowvera/kyc/kyc-record-api';
import { AppEnvStore } from '@app/store/app-env.state';
import { KycRecordSummary } from '@app/models/bw/co/knowvera/kyc/kyc-record-summary';
import { Page } from '@app/models/page.model';
import { initials, labelOf, pillIcon, pillTone, riskTone } from './client-kyc-labels';

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
 * The clients of the signed-in member's organisation (those who accepted its request), with a link
 * to each client's KYC report as the organisation configured it.
 */
@Component({
  selector: 'app-client-kyc',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, DatePipe, MatIconModule, MatPaginatorModule, MatTabsModule],
  templateUrl: './client-kyc.html',
})
export class ClientKyc {
  private readonly kycRecordApi = inject(KycRecordApi);
  private readonly appEnvState = inject(AppEnvStore);

  protected readonly pageSize = 10;
  protected readonly organisation = computed(() => this.appEnvState.userOrganisation());
  protected readonly organisationLoaded = computed(() => this.appEnvState.userOrganisationLoaded());
  protected readonly selectedId = computed<string>(() => this.organisation()?.id || '');
  protected readonly lists = signal<Record<ClientKind, ClientList>>({
    individuals: { ...EMPTY_LIST },
    organisations: { ...EMPTY_LIST },
  });

  protected readonly kinds: { kind: ClientKind; title: string; icon: string; identity: string; empty: string }[] = [
    { kind: 'individuals', title: 'Individual Clients', icon: 'person', identity: 'Identity No.', empty: 'No individual clients.' },
    { kind: 'organisations', title: 'Organisation Clients', icon: 'corporate_fare', identity: 'Registration No.', empty: 'No organisation clients.' },
  ];

  protected readonly labelOf = labelOf;
  protected readonly riskTone = riskTone;
  protected readonly organisationInitials = computed(() => initials(this.organisation()?.name));

  protected readonly pillTone = pillTone;
  protected readonly pillIcon = pillIcon;
  protected readonly initials = initials;

  constructor() {
    // The organisation arrives after sign-in; list its clients once it is known
    effect(() => {
      if (this.selectedId()) {
        this.load('individuals', 0);
        this.load('organisations', 0);
      }
    });
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
