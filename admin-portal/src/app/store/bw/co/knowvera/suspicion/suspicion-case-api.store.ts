
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { SuspicionCaseDTO } from '@app/models/bw/co/knowvera/suspicion/suspicion-case-dto';
import { SuspicionCaseApi } from '@app/services/bw/co/knowvera/suspicion/suspicion-case-api';

export type SuspicionCaseApiState = AppState<any, any> & {};

const initialState: SuspicionCaseApiState = {
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

export const SuspicionCaseApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const suspicionCaseApi = inject(SuspicionCaseApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      addNote: rxMethod<{id: string, note: string}>(
        switchMap((data: {id: string, note: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return suspicionCaseApi.addNote(data.id, data.note, ).pipe(
            tapResponse({
              next: (response: SuspicionCaseDTO) => {
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
      close: rxMethod<{id: string, outcome: string}>(
        switchMap((data: {id: string, outcome: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return suspicionCaseApi.close(data.id, data.outcome, ).pipe(
            tapResponse({
              next: (response: SuspicionCaseDTO) => {
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
      draftReport: rxMethod<{id: string}>(
        switchMap((data: {id: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return suspicionCaseApi.draftReport(data.id, ).pipe(
            tapResponse({
              next: (response: string) => {
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
      escalate: rxMethod<{id: string}>(
        switchMap((data: {id: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return suspicionCaseApi.escalate(data.id, ).pipe(
            tapResponse({
              next: (response: SuspicionCaseDTO) => {
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
      findActive: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return suspicionCaseApi.findActive().pipe(
            tapResponse({
              next: (response: SuspicionCaseDTO[]) => {
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
      findById: rxMethod<{id: string}>(
        switchMap((data: {id: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return suspicionCaseApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: SuspicionCaseDTO) => {
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
      findByRecord: rxMethod<{kycRecordId: string}>(
        switchMap((data: {kycRecordId: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return suspicionCaseApi.findByRecord(data.kycRecordId, ).pipe(
            tapResponse({
              next: (response: SuspicionCaseDTO[]) => {
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
      raise: rxMethod<{suspicionCase: SuspicionCaseDTO}>(
        switchMap((data: {suspicionCase: SuspicionCaseDTO}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return suspicionCaseApi.raise(data.suspicionCase, ).pipe(
            tapResponse({
              next: (response: SuspicionCaseDTO) => {
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
      recordReport: rxMethod<{id: string, fiaReference: string}>(
        switchMap((data: {id: string, fiaReference: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return suspicionCaseApi.recordReport(data.id, data.fiaReference, ).pipe(
            tapResponse({
              next: (response: SuspicionCaseDTO) => {
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
    }
  }),
);
