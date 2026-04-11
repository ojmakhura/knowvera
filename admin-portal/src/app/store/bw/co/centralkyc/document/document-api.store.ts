
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
import { SearchObject } from '@app/models/search-object';
import { Page } from '@app/models/page.model';
import { DocumentDTO } from '@app/models/bw/co/centralkyc/document/document-dto';
import { DocumentApi } from '@app/services/bw/co/centralkyc/document/document-api';
import { TargetEntity } from '@app/models/bw/co/centralkyc/target-entity';
import { DocumentSearchCriteria } from '@app/models/bw/co/centralkyc/document/document-search-criteria';
import { DocumentListDTO } from '@app/models/bw/co/centralkyc/document/document-list-dto';
import { HttpErrorResponse } from '@angular/common/http';

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
                    messages: ['Success!!'],
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
                    messages: ['Success!!'],
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
      upload: rxMethod<{ target: TargetEntity | any, targetId: string | any, documentTypeId: string | any, file: File | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentApi.upload(data.target, data.targetId, data.documentTypeId, data.file,).pipe(
            tapResponse({
              next: (response: DocumentDTO | any) => {
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
                    messages: ['Update successful!!'],
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
                    messages: ['Analysis successful!!'],
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
                    messages: ['Verification successful!!'],
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
      )
    }
  }),
);
