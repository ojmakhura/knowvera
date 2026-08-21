
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@models/search-object';
import { Page } from '@models/page.model';
import { TemplateMappings } from '@app/models/bw/co/knowvera/settings/template-mappings';
import { SettingsDTO } from '@app/models/bw/co/knowvera/settings/settings-dto';
import { SettingsFieldGroups } from '@app/models/bw/co/knowvera/settings/settings-field-groups';
import { OperationalMetrics } from '@app/models/bw/co/knowvera/settings/operational-metrics';
import { DocumentRequirements } from '@app/models/bw/co/knowvera/settings/document-requirements';
import { FinancialSettings } from '@app/models/bw/co/knowvera/settings/financial-settings';
import { PlatformIdentity } from '@app/models/bw/co/knowvera/settings/platform-identity';
import { SettingsToolSelectors } from '@app/models/bw/co/knowvera/settings/settings-tool-selectors';
import { SettingsApi } from '@app/services/bw/co/knowvera/settings/settings-api';

export type SettingsApiState = AppState<any, any> & {};

const initialState: SettingsApiState = {
  data: null,
  dataList: [],
  dataPage: new Page<any>(),
  searchCriteria: new SearchObject<any>(),
  loading: false,
  success: false,
  messages: [],
  loaderMessage: '',
  error: false
};

export const SettingsApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const settingsApi = inject(SettingsApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      attachDocumentType: rxMethod<{documentTypeId: string, purpose: DocumentTypePurpose}>(
        switchMap((data: {documentTypeId: string, purpose: DocumentTypePurpose}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.attachDocumentType(data.documentTypeId, data.purpose, ).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      detachDocumentType: rxMethod<{documentTypeId: string, purpose: DocumentTypePurpose}>(
        switchMap((data: {documentTypeId: string, purpose: DocumentTypePurpose}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.detachDocumentType(data.documentTypeId, data.purpose, ).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      findById: rxMethod<{id: string}>(
        switchMap((data: {id: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.findById(data.id, ).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
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
          return settingsApi.getAll().pipe(
            tapResponse({
              next: (response: SettingsDTO[]) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      getAllPaged: rxMethod<{pageNumber: number, pageSize: number}>(
        switchMap((data: {pageNumber: number, pageSize: number}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getAllPaged(data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<SettingsDTO>) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      getDocumentRequirements: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getDocumentRequirements().pipe(
            tapResponse({
              next: (response: DocumentRequirements) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      getFinancialSettings: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getFinancialSettings().pipe(
            tapResponse({
              next: (response: FinancialSettings) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      getOperationalMetrics: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getOperationalMetrics().pipe(
            tapResponse({
              next: (response: OperationalMetrics) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      getPlatformIdentity: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getPlatformIdentity().pipe(
            tapResponse({
              next: (response: PlatformIdentity) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      getSettingsFieldGroups: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getSettingsFieldGroups().pipe(
            tapResponse({
              next: (response: SettingsFieldGroups) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      getSettingsToolSelectors: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getSettingsToolSelectors().pipe(
            tapResponse({
              next: (response: SettingsToolSelectors) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      getTemplateMappings: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getTemplateMappings().pipe(
            tapResponse({
              next: (response: TemplateMappings) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      loadSettings: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.loadSettings().pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      pagedSearch: rxMethod<{criteria: string, pageNumber: number, pageSize: number}>(
        switchMap((data: {criteria: string, pageNumber: number, pageSize: number}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.pagedSearch(data.criteria, data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<SettingsDTO>) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      remove: rxMethod<{id: string}>(
        switchMap((data: {id: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.remove(data.id, ).pipe(
            tapResponse({
              next: (response: boolean) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      save: rxMethod<{setttings: SettingsDTO}>(
        switchMap((data: {setttings: SettingsDTO}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.save(data.setttings, ).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      saveDocumentRequirements: rxMethod<{documentRequirements: DocumentRequirements}>(
        switchMap((data: {documentRequirements: DocumentRequirements}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.saveDocumentRequirements(data.documentRequirements, ).pipe(
            tapResponse({
              next: (response: DocumentRequirements) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      saveFinancialSettings: rxMethod<{financialSettings: FinancialSettings}>(
        switchMap((data: {financialSettings: FinancialSettings}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.saveFinancialSettings(data.financialSettings, ).pipe(
            tapResponse({
              next: (response: FinancialSettings) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      saveOperationalMetrics: rxMethod<{operationalMetrics: OperationalMetrics}>(
        switchMap((data: {operationalMetrics: OperationalMetrics}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.saveOperationalMetrics(data.operationalMetrics, ).pipe(
            tapResponse({
              next: (response: OperationalMetrics) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      savePlatformIdentity: rxMethod<{platformIdentity: PlatformIdentity}>(
        switchMap((data: {platformIdentity: PlatformIdentity}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.savePlatformIdentity(data.platformIdentity, ).pipe(
            tapResponse({
              next: (response: PlatformIdentity) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      saveSettingsFieldGroups: rxMethod<{settingsFieldGroups: SettingsFieldGroups}>(
        switchMap((data: {settingsFieldGroups: SettingsFieldGroups}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.saveSettingsFieldGroups(data.settingsFieldGroups, ).pipe(
            tapResponse({
              next: (response: SettingsFieldGroups) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      saveSettingsToolSelectors: rxMethod<{settingsToolSelectors: SettingsToolSelectors}>(
        switchMap((data: {settingsToolSelectors: SettingsToolSelectors}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.saveSettingsToolSelectors(data.settingsToolSelectors, ).pipe(
            tapResponse({
              next: (response: SettingsToolSelectors) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      saveTemplateMappings: rxMethod<{templateMappings: TemplateMappings}>(
        switchMap((data: {templateMappings: TemplateMappings}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.saveTemplateMappings(data.templateMappings, ).pipe(
            tapResponse({
              next: (response: TemplateMappings) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      search: rxMethod<{criteria: string}>(
        switchMap((data: {criteria: string}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.search(data.criteria, ).pipe(
            tapResponse({
              next: (response: SettingsDTO[]) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
                  }
                );
              },
            }),
          );
        }),
      ),
      uploadTemplate: rxMethod<{template: MultipartFile, target: TargetEntity}>(
        switchMap((data: {template: MultipartFile, target: TargetEntity}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.uploadTemplate(data.template, data.target, ).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
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
                    messages: [error.error?.message ? error.error.message : (error.message || 'An error occurred')], 
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
