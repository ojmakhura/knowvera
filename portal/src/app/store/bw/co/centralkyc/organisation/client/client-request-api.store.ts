
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { ClientRequestDTO } from '@app/models/bw/co/centralkyc/organisation/client/client-request-dto';
import { ClientRequestApi } from '@app/services/bw/co/centralkyc/organisation/client/client-request-api';
import { ClientRequestSearchCriteria } from '@app/models/bw/co/centralkyc/organisation/client/client-request-search-criteria';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { ClientRequestStatus } from '@app/models/bw/co/centralkyc/organisation/client/client-request-status';

export type ClientRequestApiState = AppState<ClientRequestDTO, ClientRequestDTO> & {
  individualsRequests: ClientRequestDTO[];
  individualsRequestsPage: Page<ClientRequestDTO>;
  organisationsRequests: ClientRequestDTO[];
  organisationsRequestsPage: Page<ClientRequestDTO>;
  tokenConfirmed: boolean;
  identityConfirmationToken: string;
  registrationToken: string;
};

const initialState: ClientRequestApiState = {
  data: new ClientRequestDTO(),
  dataList: [],
  dataPage: new Page<ClientRequestDTO>(),
  individualsRequests: [],
  individualsRequestsPage: new Page<ClientRequestDTO>(),
  organisationsRequests: [],
  organisationsRequestsPage: new Page<ClientRequestDTO>(),
  searchCriteria: new SearchObject<ClientRequestSearchCriteria>(),
  loading: false,
  success: false,
  messages: [],
  loaderMessage: '',
  error: false,
  tokenConfirmed: false,
  identityConfirmationToken: '',
  registrationToken: '',
};

export const ClientRequestApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const clientRequestApi = inject(ClientRequestApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      // downloadRequestTemplate: rxMethod<void>(
      //   switchMap(() => {
      //     patchState(store, { loading: true, loaderMessage: 'Loading ...' });
      //     return clientRequestApi.downloadRequestTemplate().pipe(
      //       tapResponse({
      //         next: (response: any) => {
      //           patchState(
      //             store,
      //             {
      //               data: response,
      //               loading: false,
      //               success: true,
      //               messages: ['Success!!'],
      //               error: false,
      //             }
      //           );
      //         },
      //         error: (error: any) => {
      //           patchState(
      //             store, {
      //               status: (error?.status || 0),
      //               loading: false,
      //               success: false,
      //               error: true,
      //               messages: [error?.error?.message || 'An error occurred'],
      //             }
      //           );
      //         },
      //       }),
      //     );
      //   }),
      // ),
      findByDocument: rxMethod<{ documentId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByDocument(data.documentId,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
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
      findByDocumentPaged: rxMethod<{ documentId: string | any, pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByDocumentPaged(data.documentId, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
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
      findById: rxMethod<{ id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findById(data.id,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO | any) => {
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
      findByIndividual: rxMethod<{ individualId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByIndividual(data.individualId,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
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
      findByIndividualPaged: rxMethod<{ individualId: string | any, pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByIndividualPaged(data.individualId, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
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
      findByOrganisation: rxMethod<{ organisationId: string | any, target?: TargetEntity | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByOrganisation(data.organisationId, data.target,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
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
      findByOrganisationPaged: rxMethod<{ organisationId: string | any, pageNumber: number | any, pageSize: number | any, target?: TargetEntity | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByOrganisationPaged(data.organisationId, data.pageNumber, data.pageSize, data.target,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
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
      findByStatus: rxMethod<{ status: ClientRequestStatus | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByStatus(data.status,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
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
      findByStatusPaged: rxMethod<{ status: ClientRequestStatus | any, pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByStatusPaged(data.status, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
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
      findByTarget: rxMethod<{ target: TargetEntity | any, targetId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByTarget(data.target, data.targetId,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
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
      findByTargetPaged: rxMethod<{ target: TargetEntity, targetId: string, pageNumber: number, pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByTargetPaged(data.target, data.targetId, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO>) => {
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
      findIndividualsByOrganisation: rxMethod<{ organisationId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findIndividualsByOrganisation(data.organisationId,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    individualsRequests: response,
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
      findIndividualsByOrganisationPaged: rxMethod<{ organisationId: string | any, pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findIndividualsByOrganisationPaged(data.organisationId, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                patchState(
                  store,
                  {
                    individualsRequestsPage: response,
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
      findOrganisationsByOrganisation: rxMethod<{ organisationId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findOrganisationsByOrganisation(data.organisationId,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    organisationsRequests: response,
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
      findOrganisationsByOrganisationPaged: rxMethod<{ organisationId: string | any, pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findOrganisationsByOrganisationPaged(data.organisationId, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                patchState(
                  store,
                  {
                    organisationsRequestsPage: response,
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
          return clientRequestApi.getAll().pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
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
      getAllPaged: rxMethod<{ pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.getAllPaged(data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
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
      pagedSearch: rxMethod<{ criteria: SearchObject<ClientRequestSearchCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.pagedSearch(data.criteria,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
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
          return clientRequestApi.remove(data.id,).pipe(
            tapResponse({
              next: (response: boolean | any) => {
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
      save: rxMethod<{ clientRequest: ClientRequestDTO | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.save(data.clientRequest,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO | any) => {
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
      search: rxMethod<{ criteria: SearchObject<ClientRequestSearchCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.search(data.criteria,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
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
      uploadRequests: rxMethod<{ file: File, organisationId: string | any, target: TargetEntity | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.uploadRequests(data.file, data.organisationId, data.target,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
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
      updateStatus: rxMethod<{ id: string, status: ClientRequestStatus }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.updateStatus(data.id, data.status).pipe(
            tapResponse({
              next: (response: ClientRequestDTO) => {
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
      confirmToken: rxMethod<{ requestId: string, token: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.confirmToken(data.requestId, data.token).pipe(
            tapResponse({
              next: (response: string) => {
                console.log('TOKEN CONFIRMED RESPONSE:', response);

                let split = response.split('|');

                patchState(
                  store,
                  {
                    identityConfirmationToken: split[0],
                    registrationToken: split[1],
                    tokenConfirmed: true,
                    loading: false,
                    success: true,
                    messages: ['Registration token confirmed!!'],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                patchState(
                  store, {
                  status: (error?.status || 0),
                  identityConfirmationToken: '',
                  registrationToken: '',
                  tokenConfirmed: false,
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
      findMyRequests: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findMyRequests().pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
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
      findMyRequestsPaged: rxMethod<{ pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findMyRequestsPaged(data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
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
      findMyOrganisationRequests: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findMyOrganisationRequests().pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    organisationsRequests: response,
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
      findMyOrganisationRequestsPaged: rxMethod<{ pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findMyOrganisationRequestsPaged(data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                patchState(
                  store,
                  {
                    organisationsRequestsPage: response,
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
    }
  }),
);
