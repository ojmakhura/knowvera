
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
import { SearchObject } from '@app/models/search-object';
import { Page } from '@app/models/page.model';
import { KycInvoiceDTO } from '@app/models/bw/co/knowvera/invoice/kyc-invoice-dto';
import { KycInvoiceApi } from '@app/services/bw/co/knowvera/invoice/kyc-invoice-api';
import { InvoiceSearchCriteria } from '@app/models/bw/co/knowvera/invoice/invoice-search-criteria';
import { UploadPurpose } from '@app/models/bw/co/knowvera/invoice/upload-purpose';

export type KycInvoiceApiState = AppState<KycInvoiceDTO, KycInvoiceDTO> & {};

const initialState: KycInvoiceApiState = {
  data: new KycInvoiceDTO(),
  dataList: [],
  dataPage: new Page<KycInvoiceDTO>(),
  searchCriteria: new SearchObject<any>(),
  loading: false,
  success: false,
  messages: [],
  loaderMessage: '',
  error: false
};

export const KycInvoiceApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const kycInvoiceApi = inject(KycInvoiceApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findById: rxMethod<{id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycInvoiceApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: KycInvoiceDTO | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
                  }
                );
              },
            }),
          );
        }),
      ),
      findByOrganisation: rxMethod<{organisationId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycInvoiceApi.findByOrganisation(data.organisationId, ).pipe(
            tapResponse({
              next: (response: KycInvoiceDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
                  }
                );
              },
            }),
          );
        }),
      ),
      findByOrganisationPaged: rxMethod<{organisationId: string | any , pageNumber: number | any , pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycInvoiceApi.findByOrganisationPaged(data.organisationId, data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<KycInvoiceDTO> | any) => {
                patchState(
                  store,
                  {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
                  }
                );
              },
            }),
          );
        }),
      ),
      findBySubscription: rxMethod<{subscriptionId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycInvoiceApi.findBySubscription(data.subscriptionId, ).pipe(
            tapResponse({
              next: (response: KycInvoiceDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
                  }
                );
              },
            }),
          );
        }),
      ),
      findBySubscriptionPaged: rxMethod<{subscriptionId: string | any , pageNumber: number | any , pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycInvoiceApi.findBySubscriptionPaged(data.subscriptionId, data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<KycInvoiceDTO> | any) => {
                patchState(
                  store,
                  {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
                  }
                );
              },
            }),
          );
        }),
      ),
      generateInvoice: rxMethod<{subscriptionId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycInvoiceApi.generateInvoice(data.subscriptionId, ).pipe(
            tapResponse({
              next: (response: KycInvoiceDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
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
          return kycInvoiceApi.getAll().pipe(
            tapResponse({
              next: (response: KycInvoiceDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
                  }
                );
              },
            }),
          );
        }),
      ),
      getAllPaged: rxMethod<{pageNumber: number | any , pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycInvoiceApi.getAllPaged(data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<KycInvoiceDTO> | any) => {
                patchState(
                  store,
                  {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
                  }
                );
              },
            }),
          );
        }),
      ),
      pagedSearch: rxMethod<{criteria: SearchObject<InvoiceSearchCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycInvoiceApi.pagedSearch(data.criteria, ).pipe(
            tapResponse({
              next: (response: Page<KycInvoiceDTO> | any) => {
                patchState(
                  store,
                  {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
                  }
                );
              },
            }),
          );
        }),
      ),
      remove: rxMethod<{id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycInvoiceApi.remove(data.id, ).pipe(
            tapResponse({
              next: (response: boolean | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
                  }
                );
              },
            }),
          );
        }),
      ),
      save: rxMethod<{invoice: KycInvoiceDTO | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycInvoiceApi.save(data.invoice, ).pipe(
            tapResponse({
              next: (response: KycInvoiceDTO | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
                  }
                );
              },
            }),
          );
        }),
      ),
      search: rxMethod<{criteria: SearchObject<InvoiceSearchCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycInvoiceApi.search(data.criteria, ).pipe(
            tapResponse({
              next: (response: KycInvoiceDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
                  }
                );
              },
            }),
          );
        }),
      ),
      upload: rxMethod<{id: string | any , purpose: UploadPurpose | any , file: File | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycInvoiceApi.upload(data.id, data.purpose, data.file, ).pipe(
            tapResponse({
              next: (response: KycInvoiceDTO | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Success!!`],
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
                    messages: [getErrormessage(error)],
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
