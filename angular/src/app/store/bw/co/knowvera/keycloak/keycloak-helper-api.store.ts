
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { KeycloakHelperApi } from '@app/services/bw/co/knowvera/keycloak/keycloak-helper-api';

export type KeycloakHelperApiState = AppState<any, any> & {};

const initialState: KeycloakHelperApiState = {
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

export const KeycloakHelperApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const keycloakHelperApi = inject(KeycloakHelperApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      getClientRoles: rxMethod<{clientId: string}>(
        switchMap((data: {clientId: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return keycloakHelperApi.getClientRoles(data.clientId, ).pipe(
            tapResponse({
              next: (response: string[]) => {
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
      getRealmRoles: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return keycloakHelperApi.getRealmRoles().pipe(
            tapResponse({
              next: (response: string[]) => {
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
      getUserPermissions: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return keycloakHelperApi.getUserPermissions().pipe(
            tapResponse({
              next: (response: string[]) => {
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
