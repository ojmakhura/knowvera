
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@app/models/search-object';
import { Page } from '@app/models/page.model';
import { IndividualListDTO } from '@app/models/bw/co/centralkyc/individual/individual-list-dto';
import { IndividualDTO } from '@app/models/bw/co/centralkyc/individual/individual-dto';
import { IndividualApi } from '@app/services/bw/co/centralkyc/individual/individual-api';
import { IndividualSearchCriteria } from '@app/models/bw/co/centralkyc/individual/individual-search-criteria';

export type IndividualApiState = AppState<IndividualDTO, IndividualListDTO> & {
  registrationIndividualLoaded: boolean;
};

const initialState: IndividualApiState = {
  data: new IndividualDTO(),
  dataList: [],
  dataPage: new Page<IndividualListDTO>(),
  searchCriteria: new SearchObject<any>(),
  loading: false,
  success: false,
  messages: [],
  loaderMessage: '',
  error: false,
  registrationIndividualLoaded: false,
};

export const IndividualApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const individualApi = inject(IndividualApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findById: rxMethod<{id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return individualApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: IndividualDTO | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: ['Success!!'],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                patchState(
                  store, {
                    status: (error?.status || 0),
                    loading: false,
                    success: false,
                    error: true,
                    messages: [error?.error?.message || 'An error occurred'],
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
          return individualApi.getAll().pipe(
            tapResponse({
              next: (response: IndividualListDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: ['Success!!'],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                patchState(
                  store, {
                    status: (error?.status || 0),
                    loading: false,
                    success: false,
                    error: true,
                    messages: [error?.error?.message || 'An error occurred'],
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
          return individualApi.getAllPaged(data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<IndividualListDTO> | any) => {
                patchState(
                  store,
                  {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: ['Success!!'],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                patchState(
                  store, {
                    status: (error?.status || 0),
                    loading: false,
                    success: false,
                    error: true,
                    messages: [error?.error?.message || 'An error occurred'],
                  }
                );
              },
            }),
          );
        }),
      ),
      getOrganisationClients: rxMethod<{organisationId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return individualApi.getOrganisationClients(data.organisationId, ).pipe(
            tapResponse({
              next: (response: IndividualListDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: ['Success!!'],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                patchState(
                  store, {
                    status: (error?.status || 0),
                    loading: false,
                    success: false,
                    error: true,
                    messages: [error?.error?.message || 'An error occurred'],
                  }
                );
              },
            }),
          );
        }),
      ),
      getOrganisationClientsPaged: rxMethod<{organisationId: string | any , pageNumber: number | any , pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return individualApi.getOrganisationClientsPaged(data.organisationId, data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<IndividualListDTO> | any) => {
                patchState(
                  store,
                  {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: ['Success!!'],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                patchState(
                  store, {
                    status: (error?.status || 0),
                    loading: false,
                    success: false,
                    error: true,
                    messages: [error?.error?.message || 'An error occurred'],
                  }
                );
              },
            }),
          );
        }),
      ),
      pagedSearch: rxMethod<{criteria: SearchObject<IndividualSearchCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return individualApi.pagedSearch(data.criteria, ).pipe(
            tapResponse({
              next: (response: Page<IndividualListDTO> | any) => {
                patchState(
                  store,
                  {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: ['Success!!'],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                patchState(
                  store, {
                    status: (error?.status || 0),
                    loading: false,
                    success: false,
                    error: true,
                    messages: [error?.error?.message || 'An error occurred'],
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
          return individualApi.remove(data.id, ).pipe(
            tapResponse({
              next: (response: boolean | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: ['Success!!'],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                patchState(
                  store, {
                    status: (error?.status || 0),
                    loading: false,
                    success: false,
                    error: true,
                    messages: [error?.error?.message || 'An error occurred'],
                  }
                );
              },
            }),
          );
        }),
      ),
      save: rxMethod<{individual: IndividualDTO | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return individualApi.save(data.individual, ).pipe(
            tapResponse({
              next: (response: IndividualDTO | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: ['Success!!'],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                patchState(
                  store, {
                    status: (error?.status || 0),
                    loading: false,
                    success: false,
                    error: true,
                    messages: [error?.error?.message || 'An error occurred'],
                  }
                );
              },
            }),
          );
        }),
      ),
      search: rxMethod<{criteria: SearchObject<IndividualSearchCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return individualApi.search(data.criteria, ).pipe(
            tapResponse({
              next: (response: IndividualListDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: ['Success!!'],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                patchState(
                  store, {
                    status: (error?.status || 0),
                    dataList: [],
                    loading: false,
                    success: false,
                    error: true,
                    messages: [error?.error?.message || 'An error occurred'],
                  }
                );
              },
            }),
          );
        }),
      ),
      loadRequestIndividual: rxMethod<{requestId: string, identityConfirmationToken: string, identityNo: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return individualApi.loadRequestIndividual(data.requestId, data.identityConfirmationToken, data.identityNo).pipe(
            tapResponse({
              next: (response: IndividualDTO) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: ['Client request individual successfully loaded!!'],
                    error: false,
                    registrationIndividualLoaded: true,
                  }
                );
              },
              error: (error: any) => {
                patchState(
                  store, {
                    status: (error?.status || 0),
                    data: new IndividualDTO(),
                    loading: false,
                    success: false,
                    error: true,
                    messages: [error?.error?.message || 'An error occurred'],
                    registrationIndividualLoaded: false,
                  }
                );
              },
            }),
          );
        }),
      ),
      loadMe: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return individualApi.loadMe().pipe(
            tapResponse({
              next: (response: IndividualDTO) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: ['Client individual successfully loaded!!'],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                patchState(
                  store, {
                    status: (error?.status || 0),
                    data: new IndividualDTO(),
                    loading: false,
                    success: false,
                    error: true,
                    messages: [error?.error?.message || 'An error occurred'],
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
