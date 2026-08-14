
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
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Organisation ${response.name} loaded successfully!!`],
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
                    messages: [error?.error?.message || 'An error occurred while loading the organisation'],
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
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: [`${response.length} organisations loaded successfully!!`],
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
                    messages: [getErrormessage(error)],
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
                patchState(
                  store,
                  {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: [`Loaded ${response.page.size} organisations on page ${response.page.number + 1} successfully!!`],
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
                    messages: [getErrormessage(error)],
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
                patchState(
                  store,
                  {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: [`Loaded ${response.page.size} organisations on page ${response.page.number + 1} successfully!!`],
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
                    messages: [getErrormessage(error)],
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
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: ['Organisation removed successfully!!'],
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
                    messages: [getErrormessage(error)],
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
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Organisation ${response.name} saved successfully!!`],
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
                    messages: [getErrormessage(error)],
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
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
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
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Loaded my organisation successfully`],
                    error: false,
                    registrationOrganisationLoaded: true,
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
                    messages: [getErrormessage(error)],
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
