
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { ContactDTO } from '@app/models/bw/co/knowvera/contact/contact-dto';
import { ContactApi } from '@app/services/bw/co/knowvera/contact/contact-api';
import { ContactType } from '@app/models/bw/co/knowvera/contact/contact-type';
import { toast } from 'ngx-sonner';

export type ContactApiState = AppState<ContactDTO, ContactDTO> & {};

const initialState: ContactApiState = {
  data: new ContactDTO(),
  dataList: [],
  dataPage: new Page<ContactDTO>(),
  searchCriteria: new SearchObject<any>(),
  loading: false,
  success: false,
  messages: [],
  loaderMessage: '',
  error: false
};

export const ContactApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const contactApi = inject(ContactApi);
    const toastr = toast;
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findById: rxMethod<{id: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: ContactDTO) => {
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
      findByType: rxMethod<{type: ContactType}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.findByType(data.type, ).pipe(
            tapResponse({
              next: (response: ContactDTO[]) => {
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
      findByTypePaged: rxMethod<{type: ContactType, pageNumber: number, pageSize: number}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.findByTypePaged(data.type, data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<ContactDTO>) => {
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
      getAll: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.getAll().pipe(
            tapResponse({
              next: (response: ContactDTO[]) => {
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
      getAllPaged: rxMethod<{pageNumber: number, pageSize: number}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.getAllPaged(data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<ContactDTO>) => {
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
      remove: rxMethod<{id: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.remove(data.id, ).pipe(
            tapResponse({
              next: (response: boolean) => {
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
      save: rxMethod<{document: ContactDTO}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.save(data.document, ).pipe(
            tapResponse({
              next: (response: ContactDTO) => {
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
      search: rxMethod<{criteria: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return contactApi.search(data.criteria, ).pipe(
            tapResponse({
              next: (response: ContactDTO[]) => {
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
    }
  }),
);
