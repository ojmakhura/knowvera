import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  Input,
  linkedSignal,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterLink } from '@angular/router';
import { Loader } from '@app/@shared/loader/loader';
import { KycInvoiceDTO } from '@app/models/bw/co/knowvera/invoice/kyc-invoice-dto';
import { KycSubsciptionStatus } from '@app/models/bw/co/knowvera/subscription/kyc-subsciption-status';
import { KycSubscriptionDTO } from '@app/models/bw/co/knowvera/subscription/kyc-subscription-dto';
import { AppEnvStore } from '@app/store/app-env.state';
import { KycInvoiceApiStore } from '@app/store/bw/co/knowvera/invoice/kyc-invoice-api.store';
import { OrganisationApiStore } from '@app/store/bw/co/knowvera/organisation/organisation-api.store';
import { KycSubscriptionApiStore } from '@app/store/bw/co/knowvera/subscription/kyc-subscription-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { swalFire } from '@app/@shared/swal';

type TimelineItem = {
  icon: string;
  label: string;
  value: string;
  tone?: 'primary' | 'warn';
};

@Component({
  selector: 'app-subscription-details',
  standalone: true,
  templateUrl: './subscription-details.html',
  styleUrls: ['./subscription-details.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatIconModule,
    MatPaginatorModule,
    MatTableModule,
    MatTooltipModule,
    RouterLink,
    Loader,
    TranslateModule
  ],
  providers: [DatePipe, CurrencyPipe],
})
export class SubscriptionDetails implements OnInit, AfterViewInit, OnDestroy {
  @Input() id: string = '';

  organisationApiStore = inject(OrganisationApiStore);
  kycInvoiceApiStore = inject(KycInvoiceApiStore);
  protected appEnvState = inject(AppEnvStore);

  private readonly router = inject(Router);
  private readonly datePipe = inject(DatePipe);
  private readonly currencyPipe = inject(CurrencyPipe);
  private readonly kycSubscriptionApiStore = inject(KycSubscriptionApiStore);

  protected readonly displayedInvoiceColumns = ['ref', 'date', 'amount', 'status', 'actions'];
  protected readonly statusEnum = KycSubsciptionStatus;
  protected readonly loading = linkedSignal(() => this.kycSubscriptionApiStore.loading());
  protected readonly error = linkedSignal(() => this.kycSubscriptionApiStore.error());
  protected readonly messages = linkedSignal(() => this.kycSubscriptionApiStore.messages())
  protected readonly success = linkedSignal(() => this.kycSubscriptionApiStore.success());
  protected readonly loaderMessage = linkedSignal(() => {
    if (this.loading()) {
      return 'Loading subscription details...';
    }
    if (this.error()) {
      return 'Failed to load subscription details.';
    }
    return '';
  });
  protected readonly subscription = linkedSignal<KycSubscriptionDTO>(
    () => this.kycSubscriptionApiStore.data() || new KycSubscriptionDTO(),
  );
  protected readonly invoices = computed(() => this.kycInvoiceApiStore.dataList() || []);
  protected readonly invoiceDataSource = new MatTableDataSource<KycInvoiceDTO>([]);
  @ViewChild(MatPaginator) invoicesPaginator?: MatPaginator;
  protected readonly progressSegments = computed(() => {
    const filled = Math.round(this.progressRatio() * 5);

    return Array.from({ length: 5 }, (_, index) => index < filled);
  });

  protected readonly timeline = computed<TimelineItem[]>(() => {
    const subscription = this.subscription();

    return [
      {
        icon: 'event_available',
        label: 'Start Date',
        value: this.formatDate(subscription.startDate),
        tone: 'primary',
      },
      {
        icon: 'event_busy',
        label: 'End Date',
        value: this.formatDate(subscription.endDate),
        tone: 'warn',
      },
      {
        icon: 'schedule',
        label: 'Billing Period',
        value: this.billingPeriodLabel(),
        tone: 'primary',
      },
    ];
  });

  constructor() {
    effect(() => {
      this.invoiceDataSource.data = this.invoices();
    });
  }

  ngOnInit(): void {
    this.organisationApiStore.reset();
    this.kycInvoiceApiStore.reset();
    this.kycSubscriptionApiStore.reset();
    if (this.id) {
      this.kycSubscriptionApiStore.findById({ id: this.id });
      this.kycInvoiceApiStore.findBySubscription({ subscriptionId: this.id });
    }
  }

  ngAfterViewInit(): void {
    if (this.invoicesPaginator) {
      this.invoiceDataSource.paginator = this.invoicesPaginator;
    }
  }

  ngOnDestroy(): void {
    this.kycSubscriptionApiStore.reset();
    this.kycInvoiceApiStore.reset();
    this.organisationApiStore.reset();

  }

  backToSubscriptions(): void {
    this.router.navigate(['/subscription']);
  }

  openRenew(): void {
    this.router.navigate(['/subscription', 'edit', this.subscription().id || this.id]);
  }

  openInvoice(invoice: KycInvoiceDTO): void {
    if (!invoice?.id) {
      return;
    }

    this.router.navigate(['/invoice', 'details', invoice.id]);
  }

  printView(): void {
    window.print();
  }

  copyOrganisationCode(): void {
    const code = this.subscription().organisationCode;

    if (!code || !navigator.clipboard) {
      return;
    }

    navigator.clipboard.writeText(String(code));
  }

  title(): string {
    return this.subscription().ref || 'Subscription Details';
  }

  subtitle(): string {
    return 'Knowvera KYC / Compliance Environment';
  }

  annualCommitment(): string {
    return this.formatAmount(this.subscription().amount);
  }

  statusLabel(status: string | null | undefined): string {
    return status ? `${status.charAt(0)}${status.slice(1).toLowerCase()}` : 'Unknown';
  }

  statusClass(status: string | null | undefined): string {
    switch (status) {
      case KycSubsciptionStatus.ACTIVE:
        return 'active';
      case KycSubsciptionStatus.INACTIVE:
        return 'inactive';
      case KycSubsciptionStatus.CANCELLED:
        return 'cancelled';
      default:
        return 'unknown';
    }
  }

  serviceTier(): string {
    const amount = Number(this.subscription().amount || 0);

    if (amount >= 20000) {
      return 'Enterprise Vault';
    }

    if (amount >= 5000) {
      return 'Professional Vault';
    }

    return 'Core Vault';
  }

  billingPeriodLabel(): string {
    return this.subscription().period || 'Custom billing period';
  }

  amountSuffix(): string {
    const period = String(this.subscription().period || '').toLowerCase();

    if (period.includes('year') || period.includes('annual')) {
      return '/yr';
    }

    if (period.includes('month')) {
      return '/mo';
    }

    if (period.includes('quarter')) {
      return '/qtr';
    }

    return '';
  }

  daysRemaining(): number {
    const endDate = this.dateValue(this.subscription().endDate);

    if (!endDate) {
      return 0;
    }

    const difference = endDate.getTime() - Date.now();

    return Math.max(0, Math.ceil(difference / (1000 * 60 * 60 * 24)));
  }

  progressPercent(): number {
    return Math.round(this.progressRatio() * 100);
  }

  invoiceStatusLabel(invoice: KycInvoiceDTO): string {
    return invoice.paid ? 'Paid' : 'Pending';
  }

  invoiceStatusClass(invoice: KycInvoiceDTO): string {
    return invoice.paid ? 'paid' : 'pending';
  }

  createdBy(): string {
    return this.subscription().createdBy || 'System';
  }

  lastModified(): string {
    const modified = this.subscription().modifiedAt || this.subscription().createdAt;
    return this.formatDateTime(modified);
  }

  organisationName(): string {
    return this.subscription().organisationName || 'Not available';
  }

  organisationCode(): string {
    return this.subscription().organisationCode || 'Not available';
  }

  organisationRegistration(): string {
    return this.subscription().organisationRegistrationNo || 'Not available';
  }

  formatInvoiceAmount(invoice: KycInvoiceDTO): string {
    return this.formatAmount(invoice.amount);
  }

  formatInvoiceDate(invoice: KycInvoiceDTO): string {
    return this.formatDate(invoice.issueDate);
  }

  private progressRatio(): number {
    const startDate = this.dateValue(this.subscription().startDate);
    const endDate = this.dateValue(this.subscription().endDate);

    if (!startDate || !endDate || endDate <= startDate) {
      return 0;
    }

    const now = Date.now();
    const duration = endDate.getTime() - startDate.getTime();
    const elapsed = Math.min(Math.max(now - startDate.getTime(), 0), duration);

    return elapsed / duration;
  }

  private dateValue(value: Date | string | null | undefined): Date | null {
    if (!value) {
      return null;
    }

    const date = new Date(value);
    return Number.isNaN(date.getTime()) ? null : date;
  }

  private formatDate(value: Date | string | null | undefined): string {
    const date = this.dateValue(value);
    return date
      ? this.datePipe.transform(date, 'MMMM dd, yyyy') || 'Not available'
      : 'Not available';
  }

  private formatDateTime(value: Date | string | null | undefined): string {
    const date = this.dateValue(value);
    return date
      ? this.datePipe.transform(date, 'MMM d, yyyy • HH:mm') || 'Not available'
      : 'Not available';
  }

  private formatAmount(value: number | string | null | undefined): string {
    const amount = typeof value === 'string' ? Number(value) : value;

    if (amount === null || amount === undefined || Number.isNaN(amount)) {
      return '—';
    }

    return this.currencyPipe.transform(amount, 'EUR', 'symbol', '1.2-2') || '—';
  }

  addInvoice(): void {
    swalFire({
      title: 'Are you sure?',
      text: 'Do you want to generate a new invoice for this subscription?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, generate it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.kycInvoiceApiStore.generateInvoice({
          subscriptionId: this.subscription()?.id,
        });
      }
    });
  }
}
