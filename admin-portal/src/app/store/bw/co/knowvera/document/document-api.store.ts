
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
      downloadFileByUrl: rxMethod<{ objectName: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.downloadFileByUrl(data.objectName,).pipe(
            tapResponse({
              next: (response: any) => {
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
      findByDocumentType: rxMethod<{ documentTypeId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.findByDocumentType(data.documentTypeId,).pipe(
            tapResponse({
              next: (response: DocumentDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: [`${response.length || 'No'} document(s) found for the given document type!!`],
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
      findById: rxMethod<{ id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.findById(data.id,).pipe(
            tapResponse({
              next: (response: DocumentDTO | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Document "${response?.fileName || 'unknown'}" loaded successfully!!`],
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
      findByTarget: rxMethod<{ target: TargetEntity | any, targetId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.findByTarget(data.target, data.targetId,).pipe(
            tapResponse({
              next: (response: DocumentDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: [`${response.length || 'No'} document(s) found for the given target!!`],
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
          return documentApi.getAll().pipe(
            tapResponse({
              next: (response: DocumentDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: [`${response.length || 'No'} document(s) found!!`],
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
      getAllPaged: rxMethod<{ pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.getAllPaged(data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (response: Page<DocumentDTO> | any) => {
                patchState(
                  store,
                  {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: [`${response.content.length || 'No'} document(s) found!!`],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                patchState(
                  store, {
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
      remove: rxMethod<{ id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.remove(data.id,).pipe(
            tapResponse({
              next: (response: boolean | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Document removed successfully!!`],
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
      save: rxMethod<{ document: DocumentDTO | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.save(data.document,).pipe(
            tapResponse({
              next: (response: DocumentDTO | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Document "${response?.fileName || 'unknown'}" saved successfully!!`],
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
      search: rxMethod<{ criteria: SearchObject<DocumentSearchCriteria> | string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.search(data.criteria,).pipe(
            tapResponse({
              next: (response: DocumentDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: [`${response.length || 'No'} document(s) found for the given criteria!!`],
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
      searchPaged: rxMethod<{ criteria: SearchObject<DocumentSearchCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.searchPaged(data.criteria,).pipe(
            tapResponse({
              next: (response: Page<DocumentDTO> | any) => {
                patchState(
                  store,
                  {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: [`${response.content.length || 'No'} document(s) found!!`],
                    error: false,
                  }
                );
              },
              error: (error: HttpErrorResponse) => {
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
      upload: rxMethod<{ target: TargetEntity, targetId: string, documentTypeId: string, file: File, purpose?: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: `Uploading document` });
          return documentApi.upload(data.target, data.targetId, data.documentTypeId, data.file, data.purpose).pipe(
            tapResponse({
              next: (response: DocumentDTO | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Upload ${response?.fileName || 'document'} successful!!`],
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
      updateFileContent: rxMethod<{ id: string, content: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Updating ...' });
          return documentApi.updateFileContent(data.id, data.content,).pipe(
            tapResponse({
              next: (response: DocumentDTO) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Document "${response?.fileName || 'unknown'}" updated successfully!!`],
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
      analyseDocument: rxMethod<{ id: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Analysing ...' });
          return documentApi.analyseDocument(data.id,).pipe(
            tapResponse({
              next: (response: DocumentDTO) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Document "${response?.fileName || 'unknown'}" analysis happening in the background!!`],
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
      verifyData: rxMethod<{ id: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Verifying ...' });
          return documentApi.verifyData(data.id,).pipe(
            tapResponse({
              next: (response: DocumentDTO) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Background verification "${response?.fileName || 'unknown'}" triggered successfully!!`],
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
      updateVerificationStatus: rxMethod<{ id: string, status: DocumentVerificationStatus }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Updating verification status ...' });
          return documentApi.updateVerificationStatus(data.id, data.status).pipe(
            tapResponse({
              next: (response: DocumentDTO) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Document "${response?.fileName || 'unknown'}" verification status updated successfully!!`],
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
      textExtraction: rxMethod<{ id: string, block: boolean }>(
        switchMap((data: { id: string, block: boolean }) => {
          patchState(store, { loading: true, loaderMessage: 'Extracting text ...' });
          return documentApi.textExtraction(data.id, data.block).pipe(
            tapResponse({
              next: (response: DocumentDTO) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Document "${response?.fileName || 'unknown'}" text extraction happening in the background!!`],
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
