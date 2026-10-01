
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { OrganisationDocumentDTO } from '@models/bw/co/knowvera/organisation/document/organisation-document-dto';
import { OrganisationDocumentApi } from '@services/bw/co/knowvera/organisation/document/organisation-document-api';
import { OrganisationDocumentStatus } from '@app/models/bw/co/knowvera/organisation/document/organisation-document-status';
import { OrganisationDocumentSearchCriteria } from '@app/models/bw/co/knowvera/organisation/document/organisation-document-search-criteria';
import { toast } from 'ngx-sonner';

export type OrganisationDocumentApiState = AppState<any, any> & {};

const initialState: OrganisationDocumentApiState = {
  data: null,
  dataList: [],
  dataPage: new Page<any>(),
  searchCriteria: new SearchObject<any>(),
  loading: false,
  success: false,
  messages: [],
  loaderMessage: '',
  error: false
};

export const OrganisationDocumentApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const organisationDocumentApi = inject(OrganisationDocumentApi);
    const toastr = toast;
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findById: rxMethod<{id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationDocumentApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: OrganisationDocumentDTO | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store, 
                  {
                    data: response,
                    loading: false, 
                    success: true, 
                    messages: [message],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(
                  store, { 
                    status: (error?.status || 0), 
                    loading: false, 
                    success: false,
                    error: true,
                    messages: [message],
                  }
                );
              },
            }),
          );
        }),
      ),
      findByOrganisation: rxMethod<{organisationId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationDocumentApi.findByOrganisation(data.organisationId, ).pipe(
            tapResponse({
              next: (response: OrganisationDocumentDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store, 
                  {
                    dataList: response, 
                    loading: false, 
                    success: true, 
                    messages: [message],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(
                  store, { 
                    status: (error?.status || 0), 
                    loading: false, 
                    success: false,
                    error: true,
                    messages: [message],
                  }
                );
              },
            }),
          );
        }),
      ),
      findByOrganisationPaged: rxMethod<{organisationId: string | any , pageNumber: number | any , pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationDocumentApi.findByOrganisationPaged(data.organisationId, data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<OrganisationDocumentDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store, 
                  {
                    dataPage: response,
                    loading: false, 
                    success: true, 
                    messages: [message],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(
                  store, { 
                    status: (error?.status || 0), 
                    loading: false, 
                    success: false,
                    error: true,
                    messages: [message],
                  }
                );
              },
            }),
          );
        }),
      ),
      findByStatus: rxMethod<{status: OrganisationDocumentStatus | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationDocumentApi.findByStatus(data.status, ).pipe(
            tapResponse({
              next: (response: OrganisationDocumentDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store, 
                  {
                    dataList: response, 
                    loading: false, 
                    success: true, 
                    messages: [message],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(
                  store, { 
                    status: (error?.status || 0), 
                    loading: false, 
                    success: false,
                    error: true,
                    messages: [message],
                  }
                );
              },
            }),
          );
        }),
      ),
      findByStatusPaged: rxMethod<{status: OrganisationDocumentStatus | any , pageNumber: number | any , pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationDocumentApi.findByStatusPaged(data.status, data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<OrganisationDocumentDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store, 
                  {
                    dataPage: response,
                    loading: false, 
                    success: true, 
                    messages: [message],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(
                  store, { 
                    status: (error?.status || 0), 
                    loading: false, 
                    success: false,
                    error: true,
                    messages: [message],
                  }
                );
              },
            }),
          );
        }),
      ),
      getAll: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationDocumentApi.getAll().pipe(
            tapResponse({
              next: (response: OrganisationDocumentDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store, 
                  {
                    dataList: response, 
                    loading: false, 
                    success: true, 
                    messages: [message],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(
                  store, { 
                    status: (error?.status || 0), 
                    loading: false, 
                    success: false,
                    error: true,
                    messages: [message],
                  }
                );
              },
            }),
          );
        }),
      ),
      getAllPaged: rxMethod<{pageNumber: number | any , pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationDocumentApi.getAllPaged(data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<OrganisationDocumentDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store, 
                  {
                    dataPage: response,
                    loading: false, 
                    success: true, 
                    messages: [message],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(
                  store, { 
                    status: (error?.status || 0), 
                    loading: false, 
                    success: false,
                    error: true,
                    messages: [message],
                  }
                );
              },
            }),
          );
        }),
      ),
      pagedSearch: rxMethod<{criteria: SearchObject<OrganisationDocumentSearchCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationDocumentApi.pagedSearch(data.criteria, ).pipe(
            tapResponse({
              next: (response: Page<OrganisationDocumentDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store, 
                  {
                    dataPage: response,
                    loading: false, 
                    success: true, 
                    messages: [message],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(
                  store, { 
                    status: (error?.status || 0), 
                    loading: false, 
                    success: false,
                    error: true,
                    messages: [message],
                  }
                );
              },
            }),
          );
        }),
      ),
      remove: rxMethod<{id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationDocumentApi.remove(data.id, ).pipe(
            tapResponse({
              next: (response: boolean | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store, 
                  {
                    data: response,
                    loading: false, 
                    success: true, 
                    messages: [message],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(
                  store, { 
                    status: (error?.status || 0), 
                    loading: false, 
                    success: false,
                    error: true,
                    messages: [message],
                  }
                );
              },
            }),
          );
        }),
      ),
      save: rxMethod<{clientRequest: OrganisationDocumentDTO | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationDocumentApi.save(data.clientRequest, ).pipe(
            tapResponse({
              next: (response: OrganisationDocumentDTO | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store, 
                  {
                    data: response,
                    loading: false, 
                    success: true, 
                    messages: [message],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(
                  store, { 
                    status: (error?.status || 0), 
                    loading: false, 
                    success: false,
                    error: true,
                    messages: [message],
                  }
                );
              },
            }),
          );
        }),
      ),
      search: rxMethod<{criteria: SearchObject<OrganisationDocumentSearchCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationDocumentApi.search(data.criteria, ).pipe(
            tapResponse({
              next: (response: OrganisationDocumentDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store, 
                  {
                    dataList: response, 
                    loading: false, 
                    success: true, 
                    messages: [message],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(
                  store, { 
                    status: (error?.status || 0), 
                    loading: false, 
                    success: false,
                    error: true,
                    messages: [message],
                  }
                );
              },
            }),
          );
        }),
      ),
    }
  }),
);
