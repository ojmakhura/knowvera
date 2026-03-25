import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  linkedSignal,
  OnDestroy,
  OnInit,
  Signal,
  signal,
  ViewChild,
} from '@angular/core';
import { form, FormField } from '@angular/forms/signals';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginator } from '@angular/material/paginator';
import { Router, RouterLink } from '@angular/router';
import { TableComponent } from '@app/components/table/table';
import { ActionTemplate } from '@app/models/action-template';
import { OrganisationListDTO } from '@app/models/bw/co/centralkyc/organisation/organisation-list-dto';
import { OrganisationSearchCriteria } from '@app/models/bw/co/centralkyc/organisation/organisation-search-criteria';
import { ColumnModel } from '@app/models/column.model';
import { SearchObject } from '@app/models/search-object';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';

export class SearchOrganisationsVarsForm {
  criteria: string | any = null;
  organisations: Array<OrganisationListDTO> = [];
}

@Component({
  selector: 'app-organisations',
  imports: [RouterLink, TableComponent, FormField, MatCardModule, MatIconModule, TranslateModule],
  templateUrl: './organisations.html',
  styleUrls: ['./organisations.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Organisations implements OnInit, AfterViewInit, OnDestroy {
  searchOrganisationsVarsForm: SearchOrganisationsVarsForm = new SearchOrganisationsVarsForm();
  searchOrganisationsSignal = signal(this.searchOrganisationsVarsForm);
  searchOrganisationsSignalForm = form(this.searchOrganisationsSignal, (path) => {});

  toaster: ToastrService = inject(ToastrService);
  readonly organisationApiStore = inject(OrganisationApiStore);
  @ViewChild('organisationsTable') organisationsTable!: TableComponent<Array<OrganisationListDTO>>;

  organisationsTableColumns: ColumnModel[] = [
    new ColumnModel('registrationNo', 'registration.no', false),
    new ColumnModel('code', 'code', false),
    new ColumnModel('name', 'name', false),
  ];

  organisationsTableColumnsActions: ActionTemplate[] = [
    {
      id: 'organisation-edit',
      label: 'edit',
      icon: 'edit',
      tooltip: 'edit',
    },
    {
      id: 'organisation-details',
      label: 'details',
      icon: 'remove_red_eye',
      tooltip: 'details',
    },
  ];
  loaderMessage: Signal<string> = signal('');
  messages: Signal<any> = signal({});
  success: Signal<boolean> = signal(false);
  loading: Signal<boolean> = signal(false);
  error: Signal<boolean> = signal(false);

  organisationsTableSignal = linkedSignal(() => this.organisationApiStore.dataPage());
  router = inject(Router);

  constructor() {
    effect(() => {
      let messages = this.messages();

      if (this.success() && !this.loading()) {
        this.toaster.success(messages[0]);
      }

      if (this.error() && !this.loading()) {
        this.toaster.error(messages[0]);
      }
    });
  }
  ngOnInit(): void {
    this.organisationsTable?.tablePaginator?.page?.subscribe({
      next: (paginator: MatPaginator) => {
        this.doSearch(paginator.pageIndex, paginator.pageSize);
      },
    });

    this.doSearch();
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}

  doSearch(pageNumber: number = 0, pageSize: number = 10): void {

    let value = this.searchOrganisationsSignal().criteria;

    let criteria = new SearchObject<OrganisationSearchCriteria>()
    criteria.pageNumber = pageNumber;
    criteria.pageSize = pageSize;
    criteria.criteria = value;

    this.organisationApiStore.pagedSearch({
      criteria: criteria
    });
  }

  organisationsTableActionClicked(event: any): void {
    console.log('organisationsTableActionClicked', event);
    switch (event.action) {
      case 'organisation-edit':
        
        this.router.navigate(['organisation', 'edit', event.row.id]);
        break;
      case 'organisation-details':
        
        this.router.navigate(['organisation', 'details', event.row.id]);
        break;
    }
  }
}
