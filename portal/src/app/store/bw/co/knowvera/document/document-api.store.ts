
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
import { SearchObject } from '@app/models/search-object';
import { Page } from '@app/models/page.model';
import { DocumentDTO } from '@app/models/bw/co/knowvera/document/document-dto';
import { DocumentApi } from '@app/services/bw/co/knowvera/document/document-api';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { DocumentSearchCriteria } from '@app/models/bw/co/knowvera/document/document-search-criteria';
import { DocumentListDTO } from '@app/models/bw/co/knowvera/document/document-list-dto';
import { HttpErrorResponse } from '@angular/common/http';
import { DocumentVerificationStatus } from '@app/models/bw/co/knowvera/document/document-verification-status';
import { toast } from 'ngx-sonner';

export type DocumentApiState = AppState<DocumentDTO, DocumentListDTO> & {};

const initialState: DocumentApiState = {
  data: new DocumentDTO(),
  dataList: [],
  dataPage: new Page<DocumentListDTO>(),
  searchCriteria: new SearchObject<any>(),
  loading: false,
  success: false,
  messages: [],
  loaderMessage: '',
  error: false
};

export const DocumentApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const documentApi = inject(DocumentApi);
    const toastr = toast;
    return {
      reset: () => {
        patchState(store, initialState);
      },
      downloadFile: rxMethod<{ id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.downloadFile(data.id,).pipe(
            tapResponse({
              next: (response: any) => {
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
      downloadFileByUrl: rxMethod<{ objectName: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.downloadFileByUrl(data.objectName,).pipe(
            tapResponse({
              next: (response: any) => {
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
      findByDocumentType: rxMethod<{ documentTypeId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.findByDocumentType(data.documentTypeId,).pipe(
            tapResponse({
              next: (response: DocumentDTO[] | any[]) => {
                const message = `${response.length || 'No'} document(s) found for the given document type!!`;
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
      findById: rxMethod<{ id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.findById(data.id,).pipe(
            tapResponse({
              next: (response: DocumentDTO | any) => {
                const message = `Document "${response?.fileName || 'unknown'}" loaded successfully!!`;
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
      findByTarget: rxMethod<{ target: TargetEntity | any, targetId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.findByTarget(data.target, data.targetId,).pipe(
            tapResponse({
              next: (response: DocumentDTO[] | any[]) => {
                const message = `${response.length || 'No'} document(s) found for the given target!!`;
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
      getAll: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.getAll().pipe(
            tapResponse({
              next: (response: DocumentDTO[] | any[]) => {
                const message = `${response.length || 'No'} document(s) found!!`;
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
          return documentApi.getAllPaged(data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<DocumentDTO> | any) => {
                const message = `${response.content.length || 'No'} document(s) found!!`;
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
          return documentApi.remove(data.id,).pipe(
            tapResponse({
              next: (response: boolean | any) => {
                const message = `Document removed successfully!!`;
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
      save: rxMethod<{ document: DocumentDTO | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.save(data.document,).pipe(
            tapResponse({
              next: (response: DocumentDTO | any) => {
                const message = `Document "${response?.fileName || 'unknown'}" saved successfully!!`;
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
      search: rxMethod<{ criteria: SearchObject<DocumentSearchCriteria> | string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.search(data.criteria,).pipe(
            tapResponse({
              next: (response: DocumentDTO[] | any[]) => {
                const message = `${response.length || 'No'} document(s) found for the given criteria!!`;
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
      searchPaged: rxMethod<{ criteria: SearchObject<DocumentSearchCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.searchPaged(data.criteria,).pipe(
            tapResponse({
              next: (response: Page<DocumentDTO> | any) => {
                const message = `${response.content.length || 'No'} document(s) found!!`;
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
              error: (error: HttpErrorResponse) => {
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
      upload: rxMethod<{ target: TargetEntity, targetId: string, documentTypeId: string, file: File, purpose?: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: `Uploading document` });
          return documentApi.upload(data.target, data.targetId, data.documentTypeId, data.file, data.purpose).pipe(
            tapResponse({
              next: (response: DocumentDTO | any) => {
                const message = `Upload ${response?.fileName || 'document'} successful!!`;
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
      updateFileContent: rxMethod<{ id: string, content: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Updating ...' });
          return documentApi.updateFileContent(data.id, data.content,).pipe(
            tapResponse({
              next: (response: DocumentDTO) => {
                const message = `Document "${response?.fileName || 'unknown'}" updated successfully!!`;
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
      analyseDocument: rxMethod<{ id: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Analysing ...' });
          return documentApi.analyseDocument(data.id,).pipe(
            tapResponse({
              next: (response: DocumentDTO) => {
                const message = `Document "${response?.fileName || 'unknown'}" analysis happening in the background!!`;
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
      verifyData: rxMethod<{ id: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Verifying ...' });
          return documentApi.verifyData(data.id,).pipe(
            tapResponse({
              next: (response: DocumentDTO) => {
                const message = `Background verification "${response?.fileName || 'unknown'}" triggered successfully!!`;
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
      updateVerificationStatus: rxMethod<{ id: string, status: DocumentVerificationStatus }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Updating verification status ...' });
          return documentApi.updateVerificationStatus(data.id, data.status).pipe(
            tapResponse({
              next: (response: DocumentDTO) => {
                const message = `Document "${response?.fileName || 'unknown'}" verification status updated successfully!!`;
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
      textExtraction: rxMethod<{ id: string, block: boolean }>(
        switchMap((data: { id: string, block: boolean }) => {
          patchState(store, { loading: true, loaderMessage: 'Extracting text ...' });
          return documentApi.textExtraction(data.id, data.block).pipe(
            tapResponse({
              next: (response: DocumentDTO) => {
                const message = `Document "${response?.fileName || 'unknown'}" text extraction happening in the background!!`;
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
