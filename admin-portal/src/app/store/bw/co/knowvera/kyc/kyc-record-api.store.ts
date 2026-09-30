import { TargetEntity } from '@models/bw/co/knowvera/target-entity';

import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { KycRecordDTO } from '@app/models/bw/co/knowvera/kyc/kyc-record-dto';
import { KycRecordApi } from '@app/services/bw/co/knowvera/kyc/kyc-record-api';
import { KycRecordSearchCriteria } from '@app/models/bw/co/knowvera/kyc/kyc-record-search-criteria';
import { DocumentDTO } from '@app/models/bw/co/knowvera/document/document-dto';
import { KycRecordListDTO } from '@app/models/bw/co/knowvera/kyc/kyc-record-list-dto';
import { KycComplianceStatus } from '@app/models/bw/co/knowvera/kyc/kyc-compliance-status';
import { toast } from 'ngx-sonner';
import { KycRecordSummary } from '@app/models/bw/co/knowvera/kyc/kyc-record-summary';

export type KycRecordApiState = AppState<KycRecordDTO, KycRecordDTO> & {
  currentIndividualRecord: KycRecordDTO | null;
  currentOrganisationRecord: KycRecordDTO | null;
  individualSummaries: KycRecordSummary[] | null;
  organisationSummaries: KycRecordSummary[] | null;
  individualSummariesPaged: Page<KycRecordSummary> | null;
  organisationSummariesPaged: Page<KycRecordSummary> | null;
  summary: KycRecordSummary | null;
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
  individualSummaries: null,
  organisationSummaries: null,
  individualSummariesPaged: null,
  organisationSummariesPaged: null,
  summary: null,
};

export const KycRecordApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const kycRecordApi = inject(KycRecordApi);
    const toastr = toast;
    return {
      reset: () => {
        patchState(store, initialState);
      },
      createIndividualRecord: rxMethod<{ individualId: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.createIndividualRecord(data.individualId).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      createOrganisationRecord: rxMethod<{ organisationId: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.createOrganisationRecord(data.organisationId).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      findById: rxMethod<{ id: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findById(data.id).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      findByIdentityNo: rxMethod<{ identityNo: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findByIdentityNo(data.identityNo).pipe(
            tapResponse({
              next: (response: KycRecordListDTO[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  dataList: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      findByIdentityNoPaged: rxMethod<{ identityNo: string; pageNumber: number; pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi
            .findByIdentityNoPaged(data.identityNo, data.pageNumber, data.pageSize)
            .pipe(
              tapResponse({
                next: (response: Page<KycRecordListDTO>) => {
                  const message = `Success!!`;
                  toastr.success(message);
                  patchState(store, {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: [message],
                    error: false,
                  });
                },
                error: (error: any) => {
                  const message = getErrormessage(error);
                  toastr.error(message);
                  patchState(store, {
                    status: error?.status || 0,
                    loading: false,
                    success: false,
                    error: true,
                    messages: [message],
                  });
                },
              }),
            );
        }),
      ),
      findByIndividual: rxMethod<{ individualId: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findByIndividual(data.individualId).pipe(
            tapResponse({
              next: (response: KycRecordListDTO[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  dataList: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      findByIndividualPaged: rxMethod<{
        individualId: string;
        pageNumber: number;
        pageSize: number;
      }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi
            .findByIndividualPaged(data.individualId, data.pageNumber, data.pageSize)
            .pipe(
              tapResponse({
                next: (response: Page<KycRecordListDTO>) => {
                  const message = `Success!!`;
                  toastr.success(message);
                  patchState(store, {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: [message],
                    error: false,
                  });
                },
                error: (error: any) => {
                  const message = getErrormessage(error);
                  toastr.error(message);
                  patchState(store, {
                    status: error?.status || 0,
                    loading: false,
                    success: false,
                    error: true,
                    messages: [message],
                  });
                },
              }),
            );
        }),
      ),
      findByOrganisation: rxMethod<{ organisationId: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findByOrganisation(data.organisationId).pipe(
            tapResponse({
              next: (response: KycRecordListDTO[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  dataList: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      findByOrganisationRegistration: rxMethod<{ registrationNo: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findByOrganisationRegistration(data.registrationNo).pipe(
            tapResponse({
              next: (response: KycRecordListDTO[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  dataList: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      findByOrganisationRegistrationPaged: rxMethod<{
        registrationNo: string;
        pageNumber: number;
        pageSize: number;
      }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi
            .findByOrganisationRegistrationPaged(
              data.registrationNo,
              data.pageNumber,
              data.pageSize,
            )
            .pipe(
              tapResponse({
                next: (response: Page<KycRecordListDTO>) => {
                  const message = `Success!!`;
                  toastr.success(message);
                  patchState(store, {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: [message],
                    error: false,
                  });
                },
                error: (error: any) => {
                  const message = getErrormessage(error);
                  toastr.error(message);
                  patchState(store, {
                    status: error?.status || 0,
                    loading: false,
                    success: false,
                    error: true,
                    messages: [message],
                  });
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
              next: (response: KycRecordListDTO[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  dataList: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      getAllPaged: rxMethod<{ pageNumber: number; pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.getAllPaged(data.pageNumber, data.pageSize).pipe(
            tapResponse({
              next: (response: Page<KycRecordListDTO>) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  dataPage: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
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
              next: (response: Page<KycRecordListDTO>) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  dataPage: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      remove: rxMethod<{ id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.remove(data.id).pipe(
            tapResponse({
              next: (response: boolean) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      save: rxMethod<{ kycRecord: KycRecordDTO }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.save(data.kycRecord).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      search: rxMethod<{ criteria: KycRecordSearchCriteria }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.search(data.criteria).pipe(
            tapResponse({
              next: (response: KycRecordListDTO[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  dataList: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
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
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  currentOrganisationRecord: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
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
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  currentIndividualRecord: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
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
              next: (response: KycRecordListDTO[]) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  dataList: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      createNew: rxMethod<{ record: KycRecordDTO; files: File[] }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.createNew(data.record, data.files).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
                console.log('Create new record response:', response);
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                console.log('Create new record error:', error);
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      findMyRecordsPaged: rxMethod<{ pageNumber: number; pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return kycRecordApi.findMyRecordsPaged(data.pageNumber, data.pageSize).pipe(
            tapResponse({
              next: (response: Page<KycRecordListDTO>) => {
                const message = `Success!!`;
                toastr.success(message);
                patchState(store, {
                  dataPage: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      updateRecordFiles: rxMethod<{ id: string; documents: DocumentDTO[]; files: File[] }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Updating record files ...' });
          return kycRecordApi.updateRecordFiles(data.id, data.documents, data.files).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
                const message = 'Record files updated successfully!!';
                toastr.success(message);
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      removeRecordFile: rxMethod<{ id: string; documentId: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Removing record file ...' });
          return kycRecordApi.removeRecordFile(data.id, data.documentId).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
                const message = 'Record file removed successfully!!';
                toastr.success(message);
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      runVerifications: rxMethod<{ id: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Running verifications ...' });
          return kycRecordApi.runVerifications(data.id).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
                const message = 'Verifications run successfully!!';
                toastr.success(message);
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      generateKycReport: rxMethod<{ id: string }>(
        switchMap((data: { id: string }) => {
          patchState(store, { loading: true, loaderMessage: 'Generating KYC report ...' });
          return kycRecordApi.generateKycReport(data.id).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
                const message = 'KYC report generated successfully!!';
                toastr.success(message);
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      updateStatus: rxMethod<{ id: string; status: KycComplianceStatus }>(
        switchMap((data: { id: string; status: KycComplianceStatus }) => {
          patchState(store, { loading: true, loaderMessage: 'Updating KYC record status ...' });
          return kycRecordApi.updateStatus(data.id, data.status).pipe(
            tapResponse({
              next: (response: KycRecordDTO) => {
                const message = 'KYC record status updated successfully!!';
                toastr.success(message);
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      findClientIndividualSummaries: rxMethod<{ organisationId: string }>(
        switchMap((data: { organisationId: string }) => {
          patchState(store, { loading: true, loaderMessage: 'Fetching client individual summaries ...' });
          return kycRecordApi.findClientIndividualSummaries(data.organisationId).pipe(
            tapResponse({
              next: (response: KycRecordSummary[]) => {
                const message = 'Client individual summaries fetched successfully!!';
                toastr.success(message);
                patchState(store, {
                  individualSummaries: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      findClientIndividualSummariesPaged: rxMethod<{ organisationId: string; pageNumber: number; pageSize: number }>(
        switchMap((data: { organisationId: string; pageNumber: number; pageSize: number }) => {
          patchState(store, { loading: true, loaderMessage: 'Fetching client individual summaries (paged) ...' });
          return kycRecordApi.findClientIndividualSummariesPaged(data.organisationId, data.pageNumber, data.pageSize).pipe(
            tapResponse({
              next: (response: Page<KycRecordSummary>) => {
                const message = 'Client individual summaries (paged) fetched successfully!!';
                toastr.success(message);
                patchState(store, {
                  individualSummariesPaged: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      findClientOrganisationSummaries: rxMethod<{ organisationId: string }>(
        switchMap((data: { organisationId: string }) => {
          patchState(store, { loading: true, loaderMessage: 'Fetching client organisation summaries ...' });
          return kycRecordApi.findClientOrganisationSummaries(data.organisationId).pipe(
            tapResponse({
              next: (response: KycRecordSummary[]) => {
                const message = 'Client organisation summaries fetched successfully!!';
                toastr.success(message);
                patchState(store, {
                  organisationSummaries: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      findClientOrganisationSummariesPaged: rxMethod<{ organisationId: string; pageNumber: number; pageSize: number }>(
        switchMap((data: { organisationId: string; pageNumber: number; pageSize: number }) => {
          patchState(store, { loading: true, loaderMessage: 'Fetching client organisation summaries (paged) ...' });
          return kycRecordApi.findClientOrganisationSummariesPaged(data.organisationId, data.pageNumber, data.pageSize).pipe(
            tapResponse({
              next: (response: Page<KycRecordSummary>) => {
                const message = 'Client organisation summaries (paged) fetched successfully!!';
                toastr.success(message);
                patchState(store, {
                  organisationSummariesPaged: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      findSummaryById: rxMethod<{ id: string }>(
        switchMap((data: { id: string }) => {
          patchState(store, { loading: true, loaderMessage: 'Fetching summary by ID ...' });
          return kycRecordApi.findSummaryById(data.id).pipe(
            tapResponse({
              next: (response: KycRecordSummary) => {
                const message = 'Summary fetched successfully!!';
                toastr.success(message);
                patchState(store, {
                  summary: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      searchSummaries: rxMethod<{ criteria: KycRecordSearchCriteria }>(
        switchMap((data: { criteria: KycRecordSearchCriteria }) => {
          patchState(store, { loading: true, loaderMessage: 'Searching summaries ...' });
          return kycRecordApi.searchSummaries(data.criteria).pipe(
            tapResponse({
              next: (response: KycRecordSummary[]) => {
                const message = 'Summaries fetched successfully!!';
                toastr.success(message);
                patchState(store, {
                  summaries: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
      pagedSearchSummaries: rxMethod<{ criteria: SearchObject<KycRecordSearchCriteria> }>(
        switchMap((data: { criteria: SearchObject<KycRecordSearchCriteria> }) => {
          patchState(store, { loading: true, loaderMessage: 'Searching summaries (paged) ...' });
          return kycRecordApi.pagedSearchSummaries(data.criteria).pipe(
            tapResponse({
              next: (response: Page<KycRecordSummary>) => {
                const message = 'Paged summaries fetched successfully!!';
                toastr.success(message);
                patchState(store, {
                  pagedSummaries: response,
                  loading: false,
                  success: true,
                  messages: [message],
                  error: false,
                });
              },
              error: (error: any) => {
                const message = getErrormessage(error);
                toastr.error(message);
                patchState(store, {
                  status: error?.status || 0,
                  loading: false,
                  success: false,
                  error: true,
                  messages: [message],
                });
              },
            }),
          );
        }),
      ),
    };
  }),
);
