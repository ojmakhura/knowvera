
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { GroupFieldValueDTO } from '@app/models/bw/co/knowvera/kyc/fields/group-field-value-dto';
import { KycReportSectionDTO } from '@app/models/bw/co/knowvera/kyc/fields/kyc-report-section-dto';
import { KycRecordDTO } from '@app/models/bw/co/knowvera/kyc/kyc-record-dto';
import { KycReportSectionApi } from '@app/services/bw/co/knowvera/kyc/fields/kyc-report-section-api';

export type KycReportSectionApiState = AppState<any, any> & {};

const initialState: KycReportSectionApiState = {
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

export const KycReportSectionApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const kycReportSectionApi = inject(KycReportSectionApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      addFieldValue: rxMethod<{fieldValue: GroupFieldValueDTO}>(
        switchMap((data: {fieldValue: GroupFieldValueDTO}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycReportSectionApi.addFieldValue(data.fieldValue, ).pipe(
            tapResponse({
              next: (response: KycReportSectionDTO) => {
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
      findById: rxMethod<{id: string}>(
        switchMap((data: {id: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycReportSectionApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: KycReportSectionDTO) => {
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
      remove: rxMethod<{id: string}>(
        switchMap((data: {id: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycReportSectionApi.remove(data.id, ).pipe(
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
      save: rxMethod<{kycReportSection: KycRecordDTO}>(
        switchMap((data: {kycReportSection: KycRecordDTO}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycReportSectionApi.save(data.kycReportSection, ).pipe(
            tapResponse({
              next: (response: KycReportSectionDTO) => {
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
