
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { ContactDTO } from '@app/models/bw/co/centralkyc/contact/contact-dto';
import { ContactApi } from '@app/services/bw/co/centralkyc/contact/contact-api';
import { ContactType } from '@app/models/bw/co/centralkyc/contact/contact-type';

export type ContactApiState = AppState<any, any> & {};

const initialState: ContactApiState = {
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

export const ContactApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const contactApi = inject(ContactApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findById: rxMethod<{id: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: ContactDTO) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      findByType: rxMethod<{type: ContactType}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.findByType(data.type, ).pipe(
            tapResponse({
              next: (response: ContactDTO[]) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      findByTypePaged: rxMethod<{type: ContactType, pageNumber: number, pageSize: number}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.findByTypePaged(data.type, data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<ContactDTO>) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
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
          return contactApi.getAll().pipe(
            tapResponse({
              next: (response: ContactDTO[]) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      getAllPaged: rxMethod<{pageNumber: number, pageSize: number}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.getAllPaged(data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<ContactDTO>) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
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
          return contactApi.remove(data.id, ).pipe(
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      save: rxMethod<{document: ContactDTO}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.save(data.document, ).pipe(
            tapResponse({
              next: (response: ContactDTO) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      search: rxMethod<{criteria: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.search(data.criteria, ).pipe(
            tapResponse({
              next: (response: ContactDTO[]) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
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
