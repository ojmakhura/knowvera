
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { ScreeningHitDTO } from '@app/models/bw/co/knowvera/screening/screening-hit-dto';
import { ScreeningListStatus } from '@app/models/bw/co/knowvera/screening/screening-list-status';
import { ScreeningApi } from '@app/services/bw/co/knowvera/screening/screening-api';

export type ScreeningApiState = AppState<any, any> & {};

const initialState: ScreeningApiState = {
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

export const ScreeningApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const screeningApi = inject(ScreeningApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findHitsByRecord: rxMethod<{kycRecordId: string}>(
        switchMap((data: {kycRecordId: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return screeningApi.findHitsByRecord(data.kycRecordId, ).pipe(
            tapResponse({
              next: (response: ScreeningHitDTO[]) => {
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
      findPendingHits: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return screeningApi.findPendingHits().pipe(
            tapResponse({
              next: (response: ScreeningHitDTO[]) => {
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
      getListStatus: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return screeningApi.getListStatus().pipe(
            tapResponse({
              next: (response: ScreeningListStatus[]) => {
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
      reviewHit: rxMethod<{id: string, status: ScreeningHitStatus, notes: string}>(
        switchMap((data: {id: string, status: ScreeningHitStatus, notes: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return screeningApi.reviewHit(data.id, data.status, data.notes, ).pipe(
            tapResponse({
              next: (response: ScreeningHitDTO) => {
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
      screenRecord: rxMethod<{kycRecordId: string}>(
        switchMap((data: {kycRecordId: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return screeningApi.screenRecord(data.kycRecordId, ).pipe(
            tapResponse({
              next: (response: ScreeningHitDTO[]) => {
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
      updateSanctionsList: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return screeningApi.updateSanctionsList().pipe(
            tapResponse({
              next: (response: ScreeningListStatus) => {
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
