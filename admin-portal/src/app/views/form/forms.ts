import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, OnInit, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { toast } from 'ngx-sonner';
import { hasAnyPermission } from '@app/auth/permissions';
import { getErrormessage } from '@app/store/app-state';
import { KeycloakHelperApiStore } from '@app/store/bw/co/knowvera/keycloak/keycloak-helper-api.store';
import { FormApi } from '@app/services/bw/co/knowvera/form/form-api';
import { FormDTO } from '@models/bw/co/knowvera/form/form-dto';
import { FormSectionDTO } from '@models/bw/co/knowvera/form/section/form-section-dto';

/**
 * The custom forms organisations have their clients fill as part of their KYC.
 */
@Component({
  selector: 'app-forms',
  templateUrl: './forms.html',
  styleUrl: './forms.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, MatTooltipModule, RouterLink, TranslateModule],
})
export class Forms implements OnInit {
  private readonly api = inject(FormApi);
  private readonly permissionStore = inject(KeycloakHelperApiStore);
  private readonly permissions = computed(() => this.permissionStore.permissions() ?? []);

  readonly canEdit = computed(() => hasAnyPermission(this.permissions(), ['forms:edit']));
  readonly canSeeSubmissions = computed(() =>
    hasAnyPermission(this.permissions(), ['form-submissions:list']),
  );

  readonly loading = signal(false);
  readonly forms = signal<FormDTO[]>([]);
  readonly criteria = signal('');
  readonly rows = computed(() => {
    const criteria = this.criteria().trim().toLowerCase();
    if (!criteria) {
      return this.forms();
    }
    return this.forms().filter((form) =>
      [form.code, form.name, form.organisation, form.description].some((text) =>
        (text ?? '').toLowerCase().includes(criteria),
      ),
    );
  });

  ngOnInit(): void {
    this.loading.set(true);
    this.api.getAll().subscribe({
      next: (forms) => {
        this.forms.set(forms ?? []);
        this.loading.set(false);
      },
      error: (error) => {
        this.loading.set(false);
        toast.error(getErrormessage(error));
      },
    });
  }

  fieldCount(form: FormDTO): number {
    return (form.sections ?? []).reduce(
      (count: number, section: FormSectionDTO) => count + (section.fields?.length ?? 0),
      0,
    );
  }

  statusTone(status: string): string {
    return status === 'PUBLISHED'
      ? 'kv-tone-success'
      : status === 'ARCHIVED'
        ? 'kv-tone-neutral'
        : 'kv-tone-warning';
  }
}
