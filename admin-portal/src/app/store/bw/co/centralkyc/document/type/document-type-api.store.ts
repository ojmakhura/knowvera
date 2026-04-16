
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
import { SearchObject } from '@app/models/search-object';
import { Page } from '@app/models/page.model';
import { DocumentTypeDTO } from '@app/models/bw/co/centralkyc/document/type/document-type-dto';
import { DocumentTypeApi } from '@app/services/bw/co/centralkyc/document/type/document-type-api';

export type DocumentTypeApiState = AppState<DocumentTypeDTO, DocumentTypeDTO> & {};

const initialState: DocumentTypeApiState = {
  data: new DocumentTypeDTO(),
  dataList: [],
  dataPage: new Page<any>(),
  searchCriteria: new SearchObject<any>(),
  loading: false,
  success: false,
  messages: [],
  loaderMessage: '',
  error: false
};

export const DocumentTypeApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const documentTypeApi = inject(DocumentTypeApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findById: rxMethod<{id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading document type ...' });
          return documentTypeApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: DocumentTypeDTO | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Document type "${response?.name || 'unknown'}" loaded successfully!!`],
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
          patchState(store, { loading: true, loaderMessage: 'Loading all document types ...' });
          return documentTypeApi.getAll().pipe(
            tapResponse({
              next: (response: DocumentTypeDTO[] | any[]) => {
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: [`${response.length} document types loaded successfully!!`],
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
          return documentTypeApi.getAllPaged(data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<DocumentTypeDTO> | any) => {
                patchState(
                  store,
                  {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: [`Page ${response.page.number + 1} of ${response.page.totalPages} document types loaded successfully!!`],
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
      pagedSearch: rxMethod<{criteria: string | any , pageNumber: number | any , pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Searching ...' });
          return documentTypeApi.pagedSearch(data.criteria, data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<DocumentTypeDTO>) => {
                patchState(
                  store,
                  {
                    dataPage: response,
                    loading: false,
                    success: true,
                    messages: [`Page ${response.page.number + 1} of ${response.page.totalPages} document types loaded successfully!!`],
                    error: false,
                  }
                );
              },
              error: (error: any) => {
                console.log('Error in pagedSearch:', error);
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
          return documentTypeApi.remove(data.id, ).pipe(
            tapResponse({
              next: (response: boolean | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Document type "${data.documentType.name}" removed successfully!!`],
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
      save: rxMethod<{documentType: DocumentTypeDTO | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: `Saving ${data.documentType.name} ...` });
          return documentTypeApi.save(data.documentType, ).pipe(
            tapResponse({
              next: (response: DocumentTypeDTO | any) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Document type "${response?.name || 'unknown'}" saved successfully!!`],
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
      search: rxMethod<{criteria: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return documentTypeApi.search(data.criteria, ).pipe(
            tapResponse({
              next: (response: DocumentTypeDTO[] | any[]) => {
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
    }
  }),
);
