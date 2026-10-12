
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { ClientRequestDTO } from '@app/models/bw/co/knowvera/organisation/client/client-request-dto';
import { ClientRequestApi } from '@app/services/bw/co/knowvera/organisation/client/client-request-api';
import { ClientRequestSearchCriteria } from '@app/models/bw/co/knowvera/organisation/client/client-request-search-criteria';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { ClientRequestStatus } from '@app/models/bw/co/knowvera/organisation/client/client-request-status';
import { toast } from 'ngx-sonner';

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
    const toastr = toast;
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
      //               messages: [`Success!!`],
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
      //               messages: [getErrormessage(error)],
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
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataList: response,
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
      findByDocumentPaged: rxMethod<{ documentId: string | any, pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByDocumentPaged(data.documentId, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataPage: response,
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
      findById: rxMethod<{ id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findById(data.id,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO | any) => {
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
      findByIndividual: rxMethod<{ individualId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByIndividual(data.individualId,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataList: response,
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
      findByIndividualPaged: rxMethod<{ individualId: string | any, pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByIndividualPaged(data.individualId, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataPage: response,
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
      findByOrganisation: rxMethod<{ organisationId: string | any, target?: TargetEntity | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByOrganisation(data.organisationId, data.target,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataList: response,
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
      findByOrganisationPaged: rxMethod<{ organisationId: string | any, pageNumber: number | any, pageSize: number | any, target?: TargetEntity | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByOrganisationPaged(data.organisationId, data.pageNumber, data.pageSize, data.target,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataPage: response,
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
      findByStatus: rxMethod<{ status: ClientRequestStatus | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByStatus(data.status,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataList: response,
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
      findByStatusPaged: rxMethod<{ status: ClientRequestStatus | any, pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByStatusPaged(data.status, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataPage: response,
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
      findByTarget: rxMethod<{ target: TargetEntity | any, targetId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByTarget(data.target, data.targetId,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataList: response,
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
      findByTargetPaged: rxMethod<{ target: TargetEntity, targetId: string, pageNumber: number, pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findByTargetPaged(data.target, data.targetId, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO>) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataPage: response,
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
      findIndividualsByOrganisation: rxMethod<{ organisationId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findIndividualsByOrganisation(data.organisationId,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    individualsRequests: response,
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
      findIndividualsByOrganisationPaged: rxMethod<{ organisationId: string | any, pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findIndividualsByOrganisationPaged(data.organisationId, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    individualsRequestsPage: response,
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
      findOrganisationsByOrganisation: rxMethod<{ organisationId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findOrganisationsByOrganisation(data.organisationId,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    organisationsRequests: response,
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
      findOrganisationsByOrganisationPaged: rxMethod<{ organisationId: string | any, pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findOrganisationsByOrganisationPaged(data.organisationId, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    organisationsRequestsPage: response,
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
      getAll: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.getAll().pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataList: response,
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
      getAllPaged: rxMethod<{ pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.getAllPaged(data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataPage: response,
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
      pagedSearch: rxMethod<{ criteria: SearchObject<ClientRequestSearchCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.pagedSearch(data.criteria,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataPage: response,
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
      remove: rxMethod<{ id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.remove(data.id,).pipe(
            tapResponse({
              next: (response: boolean | any) => {
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
      save: rxMethod<{ clientRequest: ClientRequestDTO | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.save(data.clientRequest,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO | any) => {
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
      search: rxMethod<{ criteria: SearchObject<ClientRequestSearchCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.search(data.criteria,).pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataList: response,
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
      uploadRequests: rxMethod<{ file: File, organisationId: string | any, target: TargetEntity | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.uploadRequests(data.file, data.organisationId, data.target,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataPage: response,
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
      updateStatus: rxMethod<{ id: string, status: ClientRequestStatus }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.updateStatus(data.id, data.status).pipe(
            tapResponse({
              next: (response: ClientRequestDTO) => {
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
      confirmToken: rxMethod<{ requestId: string, token: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.confirmToken(data.requestId, data.token).pipe(
            tapResponse({
              next: (response: string) => {
                console.log('TOKEN CONFIRMED RESPONSE:', response);

                let split = response.split('|');

                const message = 'Registration token confirmed!!';
                toastr.success(message);
                patchState(
                  store,
                  {
                    identityConfirmationToken: split[0],
                    registrationToken: split[1],
                    tokenConfirmed: true,
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
                  identityConfirmationToken: '',
                  registrationToken: '',
                  tokenConfirmed: false,
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
      findMyRequests: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findMyRequests().pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataList: response,
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
      findMyRequestsPaged: rxMethod<{ pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findMyRequestsPaged(data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    dataPage: response,
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
      findMyOrganisationRequests: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findMyOrganisationRequests().pipe(
            tapResponse({
              next: (response: ClientRequestDTO[] | any[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    organisationsRequests: response,
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
      findMyOrganisationRequestsPaged: rxMethod<{ pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return clientRequestApi.findMyOrganisationRequestsPaged(data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<ClientRequestDTO> | any) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(
                  store,
                  {
                    organisationsRequestsPage: response,
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
