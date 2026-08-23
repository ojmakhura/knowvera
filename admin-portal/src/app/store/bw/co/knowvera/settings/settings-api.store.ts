
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@app/models/search-object';
import { Page } from '@app/models/page.model';
import { SettingsDTO } from '@app/models/bw/co/knowvera/settings/settings-dto';
import { SettingsApi } from '@app/services/bw/co/knowvera/settings/settings-api';
import { TargetEntity } from '@app/models/bw/co/knowvera/target-entity';
import { DocumentTypePurpose } from '@app/models/bw/co/knowvera/settings/document-type-purpose';
import { DocumentRequirements } from '@app/models/bw/co/knowvera/settings/document-requirements';
import { FinancialSettings } from '@app/models/bw/co/knowvera/settings/financial-settings';
import { OperationalMetrics } from '@app/models/bw/co/knowvera/settings/operational-metrics';
import { PlatformIdentity } from '@app/models/bw/co/knowvera/settings/platform-identity';
import { SettingsFieldGroups } from '@app/models/bw/co/knowvera/settings/settings-field-groups';
import { SettingsToolSelectors } from '@app/models/bw/co/knowvera/settings/settings-tool-selectors';
import { TemplateMappings } from '@app/models/bw/co/knowvera/settings/template-mappings';
import { SalaryRangeDTO } from '@app/models/bw/co/knowvera/settings/salary-range-dto';

export type SettingsApiState = AppState<SettingsDTO, SettingsDTO> & {
  platformIdentity: PlatformIdentity;
  documentRequirements: DocumentRequirements;
  financialSettings: FinancialSettings;
  operationalMetrics: OperationalMetrics;
  settingsFieldGroups: SettingsFieldGroups;
  settingsToolSelectors: SettingsToolSelectors;
  templateMappings: TemplateMappings;
};

const initialState: SettingsApiState = {
  data: new SettingsDTO(),
  dataList: [],
  dataPage: new Page<any>(),
  platformIdentity: new PlatformIdentity(),
  documentRequirements: new DocumentRequirements(),
  financialSettings: new FinancialSettings(),
  operationalMetrics: new OperationalMetrics(),
  settingsFieldGroups: new SettingsFieldGroups(),
  settingsToolSelectors: new SettingsToolSelectors(),
  templateMappings: new TemplateMappings(),
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
      findById: rxMethod<{id: string }>(
        switchMap((data: any) => {
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
                    messages: [`Settings loaded.`],
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
          return settingsApi.getAll().pipe(
            tapResponse({
              next: (response: SettingsDTO[]) => {
                if(response.length > 0) {
                  patchState(
                    store,
                    {
                      data: response[0],
                    }
                  );
                }
                patchState(
                  store,
                  {
                    dataList: response,
                    loading: false,
                    success: true,
                    messages: [`Settings loaded.`],
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
      getAllPaged: rxMethod<{pageNumber: number , pageSize: number }>(
        switchMap((data: any) => {
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
                    messages: [`Settings loaded successfully!`],
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
      pagedSearch: rxMethod<{criteria: string , pageNumber: number , pageSize: number }>(
        switchMap((data: any) => {
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
                    messages: [`Settings searched successfully!`],
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
      remove: rxMethod<{id: string }>(
        switchMap((data: any) => {
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
                    messages: [`Settings removed successfully!`],
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
      save: rxMethod<{settings: SettingsDTO }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.save(data.settings, ).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
                patchState(
                  store,
                  {
                    data: response,
                    loading: false,
                    success: true,
                    messages: [`Settings saved successfully!`],
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
      search: rxMethod<{criteria: string }>(
        switchMap((data: any) => {
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
                    messages: [`Settings searched successfully!`],
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
      uploadTemplate: rxMethod<{template: File , target: TargetEntity }>(
        switchMap((data: any) => {
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
                    messages: [`Template uploaded successfully!`],
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
      attachDocumentType: rxMethod<{ documentTypeId: string; purpose: DocumentTypePurpose }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Attaching document type ...' });
          return settingsApi.attachDocumentType(data.documentTypeId, data.purpose).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [ 'Document type attached successfully!'],
                  error: false,
                });
              },
              error: (error: any) => {
                patchState(store, {
                  loading: false,
                  success: false,
                  error: true,
                  messages: [error?.error?.message || 'An error occurred while attaching document type'],
                });
              },
            }),
          );
        }),
      ),
      detachDocumentType: rxMethod<{ documentTypeId: string; purpose: DocumentTypePurpose }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Detaching document type ...' });
          return settingsApi.detachDocumentType(data.documentTypeId, data.purpose).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
                patchState(store, {
                  data: response,
                  loading: false,
                  success: true,
                  messages: [ 'Document type detached successfully!'],
                  error: false,
                });
              },
              error: (error: any) => {
                patchState(store, {
                  loading: false,
                  success: false,
                  error: true,
                  messages: [error?.error?.message || 'An error occurred while detaching document type'],
                });
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
                    documentRequirements: response,
                    loading: false, 
                    success: true, 
                    messages: ['Document requirements loaded successfully!'],
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
                    financialSettings: response,
                    loading: false, 
                    success: true, 
                    messages: ['Financial Settings Loaded Successfully!!'],
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
                    operationalMetrics: response,
                    loading: false, 
                    success: true, 
                    messages: ['Operational metrics loaded successfully!'],
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
          patchState(store, { loading: true, loaderMessage: 'Loading platform identity ...' });
          return settingsApi.getPlatformIdentity().pipe(
            tapResponse({
              next: (response: PlatformIdentity) => {
                patchState(
                  store, 
                  {
                    platformIdentity: response,
                    loading: false, 
                    success: true, 
                    messages: ['Platform identity loaded successfully!'],
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
                    settingsFieldGroups: response,
                    loading: false, 
                    success: true, 
                    messages: ['Settings field groups loaded successfully!'],
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
                    settingsToolSelectors: response,
                    loading: false, 
                    success: true, 
                    messages: ['Tool selectors loaded successfully!!'],
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
                    templateMappings: response,
                    loading: false, 
                    success: true, 
                    messages: ['Template mapping loaded successfully!!'],
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
                    messages: ['Settings loaded successfully!!'],
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
                    documentRequirements: response,
                    loading: false, 
                    success: true, 
                    messages: ['Document requirements saved successfully!'],
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
                    financialSettings: response,
                    loading: false, 
                    success: true, 
                    messages: ['Financial Settings Saved Successfully!!'],
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
                    operationalMetrics: response,
                    loading: false, 
                    success: true, 
                    messages: ['Operational metrics saved successfully!'],
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
                    platformIdentity: response,
                    loading: false, 
                    success: true, 
                    messages: ['Platform identity saved successfully!'],
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
                    settingsFieldGroups: response,
                    loading: false, 
                    success: true, 
                    messages: ['Settings field groups saved successfully!'],
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
                    settingsToolSelectors: response,
                    loading: false, 
                    success: true, 
                    messages: ['Tool selectors saved successfully!'],
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
                    templateMappings: response,
                    loading: false, 
                    success: true, 
                    messages: ['Template mapping saved successfully!'],
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
      saveSalaryRange: rxMethod<{salaryRange: SalaryRangeDTO}>(
        switchMap((data: {salaryRange: SalaryRangeDTO}) => {
          patchState(store, { loading: true, loaderMessage: 'Saving salary range ...' });
          return settingsApi.saveSalaryRange(data.salaryRange).pipe(
            tapResponse({
              next: (response: FinancialSettings) => {
                patchState(
                  store, 
                  {
                    financialSettings: response,
                    loading: false, 
                    success: true, 
                    messages: ['Salary range saved successfully!'],
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
      removeSalaryRange: rxMethod<{salaryRangeId: number}>(
        switchMap((data: {salaryRangeId: number}) => {
          patchState(store, { loading: true, loaderMessage: 'Removing salary range ...' });
          return settingsApi.removeSalaryRange(data.salaryRangeId).pipe(
            tapResponse({
              next: (response: FinancialSettings) => {
                patchState(
                  store, 
                  {
                    financialSettings: response,
                    loading: false, 
                    success: true, 
                    messages: ['Salary range removed successfully!'],
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
