
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { CountDTO } from '@app/models/bw/co/centralkyc/analytics/count-dto';
import { AnalyticsApi } from '@app/services/bw/co/centralkyc/analytics/analytics-api';

export type AnalyticsApiState = AppState<any, any> & {};

const initialState: AnalyticsApiState = {
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

export const AnalyticsApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const analyticsApi = inject(AnalyticsApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      countAnalytics: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return analyticsApi.countAnalytics().pipe(
            tapResponse({
              next: (response: CountDTO) => {
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
                    messages: [error.message || 'An error occurred'], 
                  }
                );
              },
            }),
          );
        }),
      ),
      organisationCountAnalytics: rxMethod<{organisationId: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return analyticsApi.organisationCountAnalytics(data.organisationId, ).pipe(
            tapResponse({
              next: (response: CountDTO) => {
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
                    messages: [error.message || 'An error occurred'], 
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
