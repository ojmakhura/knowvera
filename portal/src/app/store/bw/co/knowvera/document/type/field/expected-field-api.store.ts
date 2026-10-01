
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { ExpectedFieldDTO } from '@app/models/bw/co/knowvera/document/type/field/expected-field-dto';
import { ExpectedFieldApi } from '@app/services/bw/co/knowvera/document/type/field/expected-field-api';
import { toast } from 'ngx-sonner';

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
    const toastr = toast;
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
                const message = 'Success!!';
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
      findByDocumentTypePage: rxMethod<{documentTypeIds: string[], pageNumber: number, pageSize: number}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return expectedFieldApi.findByDocumentTypePage(data.documentTypeIds, data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<ExpectedFieldDTO>) => {
                const message = 'Success!!';
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
      findById: rxMethod<{id: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return expectedFieldApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: ExpectedFieldDTO) => {
                const message = 'Success!!';
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
      remove: rxMethod<{id: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return expectedFieldApi.remove(data.id, ).pipe(
            tapResponse({
              next: (response: boolean) => {
                const message = 'Success!!';
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
      save: rxMethod<{documentType: ExpectedFieldDTO}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return expectedFieldApi.save(data.documentType, ).pipe(
            tapResponse({
              next: (response: ExpectedFieldDTO) => {
                const message = 'Success!!';
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
    }
  }),
);
