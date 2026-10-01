
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState, getErrormessage } from '@app/store/app-state';
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
import { toast } from 'ngx-sonner';

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
    const toastr = toast;
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
                const message = `Settings loaded.`;
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
                const message = `Settings loaded.`;
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
      getAllPaged: rxMethod<{pageNumber: number , pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getAllPaged(data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<SettingsDTO>) => {
                const message = `Settings loaded successfully!`;
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
      pagedSearch: rxMethod<{criteria: string , pageNumber: number , pageSize: number }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.pagedSearch(data.criteria, data.pageNumber, data.pageSize, ).pipe(
            tapResponse({
              next: (response: Page<SettingsDTO>) => {
                const message = `Settings searched successfully!`;
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
      remove: rxMethod<{id: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.remove(data.id, ).pipe(
            tapResponse({
              next: (response: boolean) => {
                const message = `Settings removed successfully!`;
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
      save: rxMethod<{settings: SettingsDTO }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.save(data.settings, ).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
                const message = `Settings saved successfully!`;
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
      search: rxMethod<{criteria: string }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.search(data.criteria, ).pipe(
            tapResponse({
              next: (response: SettingsDTO[]) => {
                const message = `Settings searched successfully!`;
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
      uploadTemplate: rxMethod<{template: File , target: TargetEntity }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.uploadTemplate(data.template, data.target, ).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
                const message = `Template uploaded successfully!`;
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
      attachDocumentType: rxMethod<{ documentTypeId: string; purpose: DocumentTypePurpose }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Attaching document type ...' });
          return settingsApi.attachDocumentType(data.documentTypeId, data.purpose).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
                const message = 'Document type attached successfully!';
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
      detachDocumentType: rxMethod<{ documentTypeId: string; purpose: DocumentTypePurpose }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Detaching document type ...' });
          return settingsApi.detachDocumentType(data.documentTypeId, data.purpose).pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
                const message = 'Document type detached successfully!';
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
      getDocumentRequirements: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getDocumentRequirements().pipe(
            tapResponse({
              next: (response: DocumentRequirements) => {
                const message = 'Document requirements loaded successfully!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    documentRequirements: response,
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
      getFinancialSettings: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getFinancialSettings().pipe(
            tapResponse({
              next: (response: FinancialSettings) => {
                const message = 'Financial Settings Loaded Successfully!!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    financialSettings: response,
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
      getOperationalMetrics: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getOperationalMetrics().pipe(
            tapResponse({
              next: (response: OperationalMetrics) => {
                const message = 'Operational metrics loaded successfully!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    operationalMetrics: response,
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
      getPlatformIdentity: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading platform identity ...' });
          return settingsApi.getPlatformIdentity().pipe(
            tapResponse({
              next: (response: PlatformIdentity) => {
                const message = 'Platform identity loaded successfully!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    platformIdentity: response,
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
      getSettingsFieldGroups: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getSettingsFieldGroups().pipe(
            tapResponse({
              next: (response: SettingsFieldGroups) => {
                const message = 'Settings field groups loaded successfully!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    settingsFieldGroups: response,
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
      getSettingsToolSelectors: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getSettingsToolSelectors().pipe(
            tapResponse({
              next: (response: SettingsToolSelectors) => {
                const message = 'Tool selectors loaded successfully!!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    settingsToolSelectors: response,
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
      getTemplateMappings: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.getTemplateMappings().pipe(
            tapResponse({
              next: (response: TemplateMappings) => {
                const message = 'Template mapping loaded successfully!!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    templateMappings: response,
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
      loadSettings: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.loadSettings().pipe(
            tapResponse({
              next: (response: SettingsDTO) => {
                const message = 'Settings loaded successfully!!';
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
      saveDocumentRequirements: rxMethod<{documentRequirements: DocumentRequirements}>(
        switchMap((data: {documentRequirements: DocumentRequirements}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.saveDocumentRequirements(data.documentRequirements, ).pipe(
            tapResponse({
              next: (response: DocumentRequirements) => {
                const message = 'Document requirements saved successfully!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    documentRequirements: response,
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
      saveFinancialSettings: rxMethod<{financialSettings: FinancialSettings}>(
        switchMap((data: {financialSettings: FinancialSettings}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.saveFinancialSettings(data.financialSettings, ).pipe(
            tapResponse({
              next: (response: FinancialSettings) => {
                const message = 'Financial Settings Saved Successfully!!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    financialSettings: response,
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
      saveOperationalMetrics: rxMethod<{operationalMetrics: OperationalMetrics}>(
        switchMap((data: {operationalMetrics: OperationalMetrics}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.saveOperationalMetrics(data.operationalMetrics, ).pipe(
            tapResponse({
              next: (response: OperationalMetrics) => {
                const message = 'Operational metrics saved successfully!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    operationalMetrics: response,
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
      savePlatformIdentity: rxMethod<{platformIdentity: PlatformIdentity}>(
        switchMap((data: {platformIdentity: PlatformIdentity}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.savePlatformIdentity(data.platformIdentity, ).pipe(
            tapResponse({
              next: (response: PlatformIdentity) => {
                const message = 'Platform identity saved successfully!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    platformIdentity: response,
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
      saveSettingsFieldGroups: rxMethod<{settingsFieldGroups: SettingsFieldGroups}>(
        switchMap((data: {settingsFieldGroups: SettingsFieldGroups}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.saveSettingsFieldGroups(data.settingsFieldGroups, ).pipe(
            tapResponse({
              next: (response: SettingsFieldGroups) => {
                const message = 'Settings field groups saved successfully!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    settingsFieldGroups: response,
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
      saveSettingsToolSelectors: rxMethod<{settingsToolSelectors: SettingsToolSelectors}>(
        switchMap((data: {settingsToolSelectors: SettingsToolSelectors}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.saveSettingsToolSelectors(data.settingsToolSelectors, ).pipe(
            tapResponse({
              next: (response: SettingsToolSelectors) => {
                const message = 'Tool selectors saved successfully!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    settingsToolSelectors: response,
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
      saveTemplateMappings: rxMethod<{templateMappings: TemplateMappings}>(
        switchMap((data: {templateMappings: TemplateMappings}) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return settingsApi.saveTemplateMappings(data.templateMappings, ).pipe(
            tapResponse({
              next: (response: TemplateMappings) => {
                const message = 'Template mapping saved successfully!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    templateMappings: response,
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
      saveSalaryRange: rxMethod<{salaryRange: SalaryRangeDTO}>(
        switchMap((data: {salaryRange: SalaryRangeDTO}) => {
          patchState(store, { loading: true, loaderMessage: 'Saving salary range ...' });
          return settingsApi.saveSalaryRange(data.salaryRange).pipe(
            tapResponse({
              next: (response: FinancialSettings) => {
                const message = 'Salary range saved successfully!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    financialSettings: response,
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
      removeSalaryRange: rxMethod<{salaryRangeId: number}>(
        switchMap((data: {salaryRangeId: number}) => {
          patchState(store, { loading: true, loaderMessage: 'Removing salary range ...' });
          return settingsApi.removeSalaryRange(data.salaryRangeId).pipe(
            tapResponse({
              next: (response: FinancialSettings) => {
                const message = 'Salary range removed successfully!';
                toastr.success(message);
                patchState(
                  store, 
                  {
                    financialSettings: response,
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
