import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, Input, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { toast } from 'ngx-sonner';
import { hasAnyPermission } from '@app/auth/permissions';
import { swalFire } from '@app/@shared/swal';
import { getErrormessage } from '@app/store/app-state';
import { KeycloakHelperApiStore } from '@app/store/bw/co/knowvera/keycloak/keycloak-helper-api.store';
import { SuspicionCaseApi } from '@app/services/bw/co/knowvera/suspicion/suspicion-case-api';
import { SuspicionCaseDTO } from '@app/models/bw/co/knowvera/suspicion/suspicion-case-dto';
import { Observable } from 'rxjs';

/** One suspicion case: its grounds, history and the MLRO's decisions on it. */
@Component({
  selector: 'app-suspicion-case-details',
  templateUrl: './suspicion-case-details.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, RouterLink, TranslateModule],
})
export class SuspicionCaseDetails implements OnInit {
  private readonly api = inject(SuspicionCaseApi);
  private readonly permissionStore = inject(KeycloakHelperApiStore);

  @Input() id: string | null = null;

  readonly suspicion = signal<SuspicionCaseDTO | null>(null);

  private readonly permissions = computed(() => this.permissionStore.permissions() ?? []);
  readonly canNote = computed(() => hasAnyPermission(this.permissions(), ['suspicion-cases:edit']));
  readonly canDecide = computed(() => hasAnyPermission(this.permissions(), ['suspicion-cases:review']));
  readonly canExport = computed(() => hasAnyPermission(this.permissions(), ['suspicion-cases:export']));
  readonly closed = computed(() => this.suspicion()?.status === 'CLOSED');

  ngOnInit(): void {
    if (this.id) {
      this.api.findById(this.id).subscribe({
        next: (suspicion) => this.suspicion.set(suspicion),
        error: (error) => toast.error(getErrormessage(error)),
      });
    }
  }

  addNote(): void {
    this.prompt('Add to the case history', 'What happened or was found', 'Add', (note) => this.api.addNote(this.id, note));
  }

  escalate(): void {
    swalFire({
      title: 'Escalate to the MLRO',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Escalate',
      reverseButtons: true,
    }).then((result: any) => {
      if (result.isConfirmed) {
        this.apply(this.api.escalate(this.id), 'Escalated');
      }
    });
  }

  recordReport(): void {
    this.prompt('Report filed with the FIA', "The FIA's reference for the report (Form C)", 'Record report',
      (reference) => this.api.recordReport(this.id, reference), 'text');
  }

  close(): void {
    this.prompt('Close the case', 'Outcome: reported, no grounds after review, relationship terminated…', 'Close case',
      (outcome) => this.api.close(this.id, outcome));
  }

  /** The Form C content, saved as a text file for the MLRO to file on the FIA portal. */
  downloadDraft(): void {
    this.api.draftReport(this.id).subscribe({
      next: (text: string) => {
        const url = URL.createObjectURL(new Blob([text], { type: 'text/plain' }));
        const anchor = document.createElement('a');
        anchor.href = url;
        anchor.download = `${this.suspicion()?.ref ?? 'suspicion-case'}-draft-report.txt`;
        anchor.click();
        URL.revokeObjectURL(url);
      },
      error: (error) => toast.error(getErrormessage(error)),
    });
  }

  private prompt(title: string, placeholder: string, confirm: string, send: (value: string) => Observable<any>,
    input: 'text' | 'textarea' = 'textarea'): void {
    swalFire({
      title,
      input,
      inputPlaceholder: placeholder,
      inputValidator: (value: string) => (!value || !value.trim() ? 'This is required' : null),
      showCancelButton: true,
      confirmButtonText: confirm,
      reverseButtons: true,
    }).then((result: any) => {
      if (result.isConfirmed) {
        this.apply(send(result.value.trim()), 'Saved');
      }
    });
  }

  private apply(request: Observable<any>, message: string): void {
    request.subscribe({
      next: (suspicion: SuspicionCaseDTO) => {
        this.suspicion.set(suspicion);
        toast.success(message);
      },
      error: (error) => toast.error(getErrormessage(error)),
    });
  }
}
