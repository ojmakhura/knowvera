
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
import { SearchObject } from '@app/models/search-object';
import { Page } from '@app/models/page.model';
import { OrganisationDTO } from '@app/models/bw/co/knowvera/organisation/organisation-dto';
import { OrganisationListDTO } from '@app/models/bw/co/knowvera/organisation/organisation-list-dto';
import { OrganisationApi } from '@app/services/bw/co/knowvera/organisation/organisation-api';
import { OrganisationSearchCriteria } from '@app/models/bw/co/knowvera/organisation/organisation-search-criteria';
import { toast } from 'ngx-sonner';

export type OrganisationApiState = AppState<OrganisationDTO, OrganisationListDTO> & {
  registrationOrganisationLoaded: boolean;
};

const initialState: OrganisationApiState = {
  data: new OrganisationDTO(),
  dataList: [],
  dataPage: new Page<OrganisationListDTO>(),
  searchCriteria: new SearchObject<any>(),
  loading: false,
  success: false,
  messages: [],
  loaderMessage: '',
  error: false,
  registrationOrganisationLoaded: false,
};

export const OrganisationApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const organisationApi = inject(OrganisationApi);
    const toastr = toast;
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findById: rxMethod<{id: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading organisation ...' });
          return organisationApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: OrganisationDTO) => {
                const message = `Organisation ${response.name} loaded successfully!!`;
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
      getAll: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading all organisations ...' });
          return organisationApi.getAll().pipe(
            tapResponse({
              next: (response: OrganisationListDTO[]) => {
                const message = `${response.length} organisations loaded successfully!!`;
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
      getAllPaged: rxMethod<{pageNumber: number , pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationApi.getAllPaged(data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<OrganisationListDTO> ) => {
                const message = `Loaded ${response.page.size} organisations on page ${response.page.number + 1} successfully!!`;
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
      pagedSearch: rxMethod<{criteria: SearchObject<OrganisationSearchCriteria> }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationApi.pagedSearch(data.criteria, ).pipe(
            tapResponse({
              next: (response: Page<OrganisationListDTO>) => {
                const message = `Loaded ${response.page.size} organisations on page ${response.page.number + 1} successfully!!`;
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
      remove: rxMethod<{id: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Removing organisation ...' });
          return organisationApi.remove(data.id, ).pipe(
            tapResponse({
              next: (response: boolean) => {
                const message = 'Organisation removed successfully!!';
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
      save: rxMethod<{organisation: OrganisationDTO }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Saving organisation ...' });
          return organisationApi.save(data.organisation, ).pipe(
            tapResponse({
              next: (response: OrganisationDTO) => {
                const message = `Organisation ${response.name} saved successfully!!`;
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
      search: rxMethod<{criteria: SearchObject<OrganisationSearchCriteria> }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationApi.search(data.criteria, ).pipe(
            tapResponse({
              next: (response: OrganisationListDTO[]) => {
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
      loadMyOrganisation: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return organisationApi.loadMyOrganisation().pipe(
            tapResponse({
              next: (response: OrganisationDTO) => {
                const message = `Loaded my organisation successfully`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [message],
                    error: false,
                    registrationOrganisationLoaded: true,
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
                    registrationOrganisationLoaded: true,
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
