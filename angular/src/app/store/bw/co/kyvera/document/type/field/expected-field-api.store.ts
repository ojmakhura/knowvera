
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { ExpectedFieldDTO } from '@app/models/bw/co/kyvera/document/type/field/expected-field-dto';
import { ExpectedFieldApi } from '@app/services/bw/co/kyvera/document/type/field/expected-field-api';

export type ExpectedFieldApiState = AppState<any, any> & {};

const initialState: ExpectedFieldApiState = {
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

export const ExpectedFieldApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const expectedFieldApi = inject(ExpectedFieldApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findByDocumentType: rxMethod<{documentTypeIds: string[]}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return expectedFieldApi.findByDocumentType(data.documentTypeIds, ).pipe(
            tapResponse({
              next: (response: ExpectedFieldDTO[]) => {
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
                    messages: [getErrormessage(error)], 
                  }
                );
              },
            }),
          );
        }),
      ),
      findByDocumentTypePage: rxMethod<{documentTypeIds: string[], pageNumber: number, pageSize: number}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return expectedFieldApi.findByDocumentTypePage(data.documentTypeIds, data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<ExpectedFieldDTO>) => {
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
                    messages: [getErrormessage(error)], 
                  }
                );
              },
            }),
          );
        }),
      ),
      findById: rxMethod<{id: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return expectedFieldApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: ExpectedFieldDTO) => {
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
                    messages: [getErrormessage(error)], 
                  }
                );
              },
            }),
          );
        }),
      ),
      remove: rxMethod<{id: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return expectedFieldApi.remove(data.id, ).pipe(
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
                    messages: [getErrormessage(error)], 
                  }
                );
              },
            }),
          );
        }),
      ),
      save: rxMethod<{documentType: ExpectedFieldDTO}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return expectedFieldApi.save(data.documentType, ).pipe(
            tapResponse({
              next: (response: ExpectedFieldDTO) => {
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
                    messages: [getErrormessage(error)], 
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
