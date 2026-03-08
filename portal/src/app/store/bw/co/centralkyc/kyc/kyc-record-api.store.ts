import { TargetEntity } from '@models/bw/co/centralkyc/target-entity';

import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { KycRecordDTO } from '@app/models/bw/co/centralkyc/kyc/kyc-record-dto';
import { KycRecordApi } from '@app/services/bw/co/centralkyc/kyc/kyc-record-api';
import { KycRecordSearchCriteria } from '@app/models/bw/co/centralkyc/kyc/kyc-record-search-criteria';

export type KycRecordApiState = AppState<KycRecordDTO, KycRecordDTO> & {
  currentIndividualRecord: KycRecordDTO | null;
  currentOrganisationRecord: KycRecordDTO | null;
};

const initialState: KycRecordApiState = {
  data: new KycRecordDTO(),
  dataList: [],
  dataPage: new Page<KycRecordDTO>(),
  searchCriteria: new SearchObject<KycRecordSearchCriteria>(),
  loading: false,
  success: false,
  messages: [],
  loaderMessage: '',
  error: false,
  currentIndividualRecord: null,
  currentOrganisationRecord: null,
};

export const KycRecordApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const kycRecordApi = inject(KycRecordApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      createIndividualRecord: rxMethod<{ individualId: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.createIndividualRecord(data.individualId,).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      createOrganisationRecord: rxMethod<{ organisationId: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.createOrganisationRecord(data.organisationId,).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      findById: rxMethod<{ id: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findById(data.id,).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      findByIdentityNo: rxMethod<{ identityNo: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findByIdentityNo(data.identityNo,).pipe(
            tapResponse({
              next: (response: KycRecordDTO[]) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      findByIdentityNoPaged: rxMethod<{ identityNo: string, pageNumber: number, pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findByIdentityNoPaged(data.identityNo, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<KycRecordDTO>) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      findByIndividual: rxMethod<{ individualId: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findByIndividual(data.individualId,).pipe(
            tapResponse({
              next: (response: KycRecordDTO[]) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      findByIndividualPaged: rxMethod<{ individualId: string, pageNumber: number, pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findByIndividualPaged(data.individualId, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<KycRecordDTO>) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      findByOrganisation: rxMethod<{ organisationId: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findByOrganisation(data.organisationId,).pipe(
            tapResponse({
              next: (response: KycRecordDTO[]) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      findByOrganisationRegistration: rxMethod<{ registrationNo: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findByOrganisationRegistration(data.registrationNo,).pipe(
            tapResponse({
              next: (response: KycRecordDTO[]) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      findByOrganisationRegistrationPaged: rxMethod<{ registrationNo: string, pageNumber: number, pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findByOrganisationRegistrationPaged(data.registrationNo, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<KycRecordDTO>) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
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
          return kycRecordApi.getAll().pipe(
            tapResponse({
              next: (response: KycRecordDTO[]) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      getAllPaged: rxMethod<{ pageNumber: number, pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.getAllPaged(data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<KycRecordDTO>) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      pagedSearch: rxMethod<{ criteria: SearchObject<KycRecordSearchCriteria> }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.pagedSearch(data.criteria).pipe(
            tapResponse({
              next: (response: Page<KycRecordDTO>) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      remove: rxMethod<{ id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.remove(data.id,).pipe(
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      save: rxMethod<{ kycRecord: KycRecordDTO }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.save(data.kycRecord,).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      search: rxMethod<{ criteria: KycRecordSearchCriteria }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.search(data.criteria,).pipe(
            tapResponse({
              next: (response: KycRecordDTO[]) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      findMyCurrentOrganisationRecord: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findMyCurrentRecord(TargetEntity.ORGANISATION).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {

                patchState(
                  store,
                  {
                    currentOrganisationRecord: response,
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      findMyCurrentIndividualRecord: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findMyCurrentRecord(TargetEntity.INDIVIDUAL).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {

                patchState(
                  store,
                  {
                    currentIndividualRecord: response,
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      findMyRecords: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findMyRecords().pipe(
            tapResponse({
              next: (response: KycRecordDTO[]) => {
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
                  messages: [error?.error?.message || 'An error occurred'],
                }
                );
              },
            }),
          );
        }),
      ),
      createNew: rxMethod<{ record: KycRecordDTO, files: File[] }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.createNew(data.record, data.files).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
                console.log('Create new record response:', response);
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
                console.log('Create new record error:', error);
                patchState(
                  store, {
                  status: (error?.status || 0),
                  loading: false,
                  success: false,
                  error: true,
                  messages: [error?.error?.message || 'An error occurred'],
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
