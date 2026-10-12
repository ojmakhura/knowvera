
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { VerificationDataConfigDTO } from '@app/models/bw/co/knowvera/document/type/verification/verification-data-config-dto';
import { VerificationDataConfigApi } from '@app/services/bw/co/knowvera/document/type/verification/verification-data-config-api';
import { toast } from 'ngx-sonner';

export type VerificationDataConfigApiState = AppState<any, any> & {};

const initialState: VerificationDataConfigApiState = {
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

export const VerificationDataConfigApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const verificationDataConfigApi = inject(VerificationDataConfigApi);
    const toastr = toast;
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findById: rxMethod<{id: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return verificationDataConfigApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: VerificationDataConfigDTO) => {
                const message = `Success!!`;
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
          return verificationDataConfigApi.remove(data.id, ).pipe(
            tapResponse({
              next: (response: boolean) => {
                const message = `Success!!`;
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
      save: rxMethod<{documentType: VerificationDataConfigDTO}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return verificationDataConfigApi.save(data.documentType, ).pipe(
            tapResponse({
              next: (response: VerificationDataConfigDTO) => {
                const message = `Success!!`;
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
