
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { SequenceGeneratorApi } from '@app/services/bw/co/knowvera/sequence/sequence-generator-api';
import { SequenceGeneratorDTO } from '@app/models/bw/co/knowvera/sequence/sequence-generator-dto';
import { toast } from 'ngx-sonner';

export type SequenceGeneratorApiState = AppState<SequenceGeneratorDTO, SequenceGeneratorDTO> & {};

const initialState: SequenceGeneratorApiState = {
  data: new SequenceGeneratorDTO(),
  dataList: [],
  dataPage: new Page<any>(),
  searchCriteria: new SearchObject<any>(),
  loading: false,
  success: false,
  messages: [],
  loaderMessage: '',
  error: false
};

export const SequenceGeneratorApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const sequenceGeneratorApi = inject(SequenceGeneratorApi);
    const toastr = toast;
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findById: rxMethod<{id: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return sequenceGeneratorApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: SequenceGeneratorDTO) => {
                const message = `${response.name} loaded successfully!!`;
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
      findByName: rxMethod<{name: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return sequenceGeneratorApi.findByName(data.name, ).pipe(
            tapResponse({
              next: (response: SequenceGeneratorDTO) => {
                const message = `${response.name} loaded successfully!!`;
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
      getAll: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return sequenceGeneratorApi.getAll().pipe(
            tapResponse({
              next: (response: SequenceGeneratorDTO[]) => {
                const message = `${response.length} sequence generators loaded successfully!!`;
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
      remove: rxMethod<{id: string}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return sequenceGeneratorApi.remove(data.id, ).pipe(
            tapResponse({
              next: (response: boolean) => {
                const message = `Sequence generator removed successfully!!`;
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
      save: rxMethod<{SequenceGeneratorDTO: SequenceGeneratorDTO}>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return sequenceGeneratorApi.save(data.SequenceGeneratorDTO, ).pipe(
            tapResponse({
              next: (response: SequenceGeneratorDTO) => {
                const message = `Sequence generator "${response.name}" saved successfully!!`;
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
          return sequenceGeneratorApi.search(data.criteria, ).pipe(
            tapResponse({
              next: (response: SequenceGeneratorDTO[]) => {
                const message = `${response.length} sequence generators loaded successfully!!`;
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
