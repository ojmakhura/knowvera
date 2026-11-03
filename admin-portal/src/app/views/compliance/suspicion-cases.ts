import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { toast } from 'ngx-sonner';
import { getErrormessage } from '@app/store/app-state';
import { SuspicionCaseApi } from '@app/services/bw/co/knowvera/suspicion/suspicion-case-api';
import { SuspicionCaseDTO } from '@app/models/bw/co/knowvera/suspicion/suspicion-case-dto';

/**
 * Open suspicious transaction cases, most urgent first: each must be reported to the FIA within
 * 5 working days of the suspicion arising (Regulations r.22). Confidential (FI Act s.46).
 */
@Component({
  selector: 'app-suspicion-cases',
  templateUrl: './suspicion-cases.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, RouterLink, TranslateModule],
})
export class SuspicionCases implements OnInit {
  private readonly api = inject(SuspicionCaseApi);

  readonly cases = signal<SuspicionCaseDTO[]>([]);

  ngOnInit(): void {
    this.api.findActive().subscribe({
      next: (cases) => this.cases.set(cases ?? []),
      error: (error) => toast.error(getErrormessage(error)),
    });
  }

  /** Days left to report; negative once overdue. Reported cases have nothing left to do. */
  daysLeft(suspicion: SuspicionCaseDTO): number | null {
    if (!suspicion.reportDeadline || suspicion.status === 'REPORTED') {
      return null;
    }
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const deadline = new Date(suspicion.reportDeadline);
    deadline.setHours(0, 0, 0, 0);
    return Math.round((deadline.getTime() - today.getTime()) / 86_400_000);
  }
}
