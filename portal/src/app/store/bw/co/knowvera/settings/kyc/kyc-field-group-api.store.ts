
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { KycFieldGroupDTO } from '@app/models/bw/co/knowvera/settings/kyc/kyc-field-group-dto';
import { KycFieldGroupApi } from '@app/services/bw/co/knowvera/settings/kyc/kyc-field-group-api';
import { toast } from 'ngx-sonner';

export type KycFieldGroupApiState = AppState<any, any> & {};

const initialState: KycFieldGroupApiState = {
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

export const KycFieldGroupApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const kycFieldGroupApi = inject(KycFieldGroupApi);
    const toastr = toast;
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findById: rxMethod<{id: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycFieldGroupApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: KycFieldGroupDTO) => {
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
          return kycFieldGroupApi.remove(data.id, ).pipe(
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
      save: rxMethod<{fieldGroup: KycFieldGroupDTO}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycFieldGroupApi.save(data.fieldGroup, ).pipe(
            tapResponse({
              next: (response: KycFieldGroupDTO) => {
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
      removeField: rxMethod<{id: string, fieldId: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Removing field ...' });
          return kycFieldGroupApi.removeField(data.id, data.fieldId).pipe(
            tapResponse({
              next: (response: KycFieldGroupDTO) => {
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
