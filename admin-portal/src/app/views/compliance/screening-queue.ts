import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { toast } from 'ngx-sonner';
import { hasAnyPermission } from '@app/auth/permissions';
import { swalFire } from '@app/@shared/swal';
import { getErrormessage } from '@app/store/app-state';
import { KeycloakHelperApiStore } from '@app/store/bw/co/knowvera/keycloak/keycloak-helper-api.store';
import { ScreeningApi } from '@app/services/bw/co/knowvera/screening/screening-api';
import { ScreeningHitDTO } from '@app/models/bw/co/knowvera/screening/screening-hit-dto';
import { ScreeningHitStatus } from '@app/models/bw/co/knowvera/screening/screening-hit-status';
import { ScreeningListStatus } from '@app/models/bw/co/knowvera/screening/screening-list-status';

/**
 * Sanctions screening (FI Act s.27): the state of the lists, and the potential matches waiting
 * for a screening analyst's decision.
 */
@Component({
  selector: 'app-screening-queue',
  templateUrl: './screening-queue.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, RouterLink, TranslateModule],
})
export class ScreeningQueue implements OnInit {
  private readonly api = inject(ScreeningApi);
  private readonly permissionStore = inject(KeycloakHelperApiStore);

  readonly lists = signal<ScreeningListStatus[]>([]);
  readonly hits = signal<ScreeningHitDTO[]>([]);
  readonly busy = signal(false);

  private readonly permissions = computed(() => this.permissionStore.permissions() ?? []);
  readonly canUpdateLists = computed(() => hasAnyPermission(this.permissions(), ['screening:edit']));
  readonly canReview = computed(() => hasAnyPermission(this.permissions(), ['screening:review']));

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.api.getListStatus().subscribe({
      next: (lists) => this.lists.set(lists ?? []),
      error: (error) => toast.error(getErrormessage(error)),
    });
    this.api.findPendingHits().subscribe({
      next: (hits) => this.hits.set(hits ?? []),
      error: (error) => toast.error(getErrormessage(error)),
    });
  }

  updateLists(): void {
    this.busy.set(true);
    this.api.updateSanctionsList().subscribe({
      next: (status: ScreeningListStatus) => {
        this.busy.set(false);
        toast.success(`UN list ${status?.listVersion ?? ''} imported: ${status?.entryCount ?? 0} entries`);
        this.load();
      },
      error: (error) => {
        this.busy.set(false);
        toast.error(getErrormessage(error));
      },
    });
  }

  /** A decision needs written reasons: they are the record of why the match was cleared or confirmed. */
  decide(hit: ScreeningHitDTO, confirm: boolean): void {
    swalFire({
      title: confirm ? 'Confirm sanctions match' : 'Clear as false positive',
      text: confirm
        ? 'A confirmed match blocks the relationship (FI Act s.27) and raises a suspicion case for the MLRO.'
        : 'Explain why this is not the listed person or entity (e.g. different date of birth or nationality).',
      input: 'textarea',
      inputPlaceholder: 'Reasons for the decision',
      inputValidator: (value: string) => (!value || !value.trim() ? 'Record the reasons for the decision' : null),
      icon: confirm ? 'warning' : 'question',
      showCancelButton: true,
      confirmButtonText: confirm ? 'Confirm match' : 'Clear',
      reverseButtons: true,
    }).then((result: any) => {
      if (!result.isConfirmed) {
        return;
      }
      const status = confirm ? ScreeningHitStatus.CONFIRMED : ScreeningHitStatus.FALSE_POSITIVE;
      this.api.reviewHit(hit.id, status, result.value).subscribe({
        next: () => {
          toast.success(confirm ? 'Match confirmed; a suspicion case was raised' : 'Cleared as a false positive');
          this.load();
        },
        error: (error) => toast.error(getErrormessage(error)),
      });
    });
  }
}
