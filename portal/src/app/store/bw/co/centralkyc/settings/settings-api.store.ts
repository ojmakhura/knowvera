
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@app/models/search-object';
import { Page } from '@app/models/page.model';
import { SettingsDTO } from '@app/models/bw/co/centralkyc/settings/settings-dto';
import { SettingsApi } from '@app/services/bw/co/centralkyc/settings/settings-api';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { DocumentTypePurpose } from '@app/models/bw/co/centralkyc/settings/document-type-purpose';

export type SettingsApiState = AppState<SettingsDTO, SettingsDTO> & {};

const initialState: SettingsApiState = {
  data: new SettingsDTO(),
  dataList: [],
  dataPage: new Page<any>(),
  searchCriteria: new SearchObject<any>(),
  loading: false,
  success: false,
  messages: [],
  loaderMessage: '',
  error: false
};

export const SettingsApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const settingsApi = inject(SettingsApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findById: rxMethod<{id: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
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
          return settingsApi.getAll().pipe(
            tapResponse({
              next: (response: SettingsDTO[]) => {
                if(response.length > 0) {
                  patchState(
                    store,
                    {
                      data: response[0],
                    }
                  );
                }
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
      getAllPaged: rxMethod<{pageNumber: number , pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getAllPaged(data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<SettingsDTO>) => {
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
      pagedSearch: rxMethod<{criteria: string , pageNumber: number , pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.pagedSearch(data.criteria, data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<SettingsDTO>) => {
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
      remove: rxMethod<{id: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.remove(data.id, ).pipe(
            tapResponse({
              next: (response: boolean) => {
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
      save: rxMethod<{setttings: SettingsDTO }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.save(data.setttings, ).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
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
      search: rxMethod<{criteria: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.search(data.criteria, ).pipe(
            tapResponse({
              next: (response: SettingsDTO[]) => {
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
      uploadTemplate: rxMethod<{template: File , target: TargetEntity }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.uploadTemplate(data.template, data.target, ).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
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
      attachDocumentType: rxMethod<{ documentTypeId: string; purpose: DocumentTypePurpose }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Attaching document type ...' });
          return settingsApi.attachDocumentType(data.documentTypeId, data.purpose).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [ 'Document type attached successfully!'],
                  error: false,
                });
              },
              error: (error: any) => {
                patchState(store, {
                  loading: false,
                  success: false,
                  error: true,
                  messages: [error?.error?.message || 'An error occurred while attaching document type'],
                });
              },
            }),
          );
        }),
      ),
      detachDocumentType: rxMethod<{ documentTypeId: string; purpose: DocumentTypePurpose }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Detaching document type ...' });
          return settingsApi.detachDocumentType(data.documentTypeId, data.purpose).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [ 'Document type detached successfully!'],
                  error: false,
                });
              },
              error: (error: any) => {
                patchState(store, {
                  loading: false,
                  success: false,
                  error: true,
                  messages: [error?.error?.message || 'An error occurred while detaching document type'],
                });
              },
            }),
          );
        }),
      ),
    }
  }),
);
