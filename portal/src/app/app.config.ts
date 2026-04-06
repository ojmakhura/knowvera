import {
  ApplicationConfig,
  importProvidersFrom,
  provideBrowserGlobalErrorListeners,
} from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import {
  provideHttpClient,
  withFetch,
  withInterceptors,
  withInterceptorsFromDi,
} from '@angular/common/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { HttpClient } from '@angular/common/http';

import { routes } from './app.routes';
import { catchError, Observable, of } from 'rxjs';
import { provideToastr } from 'ngx-toastr';
import { CUSTOM_DATE_FORMATS } from './@shared/custom-date-formats';
import { MAT_DATE_FORMATS } from '@angular/material/core';
import { apiPrefixInterceptor } from './@core/http/api-prefix.interceptor';
import { errorHandlerInterceptor } from './@core/http/error-handler.interceptor';

import {
  AutoRefreshTokenService,
  createInterceptorCondition,
  INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
  IncludeBearerTokenCondition,
  includeBearerTokenInterceptor,
  provideKeycloak,
  UserActivityService,
  withAutoRefreshToken,
} from 'keycloak-angular';
import { provideQuillConfig } from 'ngx-quill';

const modules = {
  toolbar: [
      ['bold', 'italic', 'underline', 'strike'],
      ['blockquote', 'code-block'],
      [{ header: 1 }, { header: 2 }],
      [{ list: 'ordered' }, { list: 'bullet' }],
      [{ script: 'sub' }, { script: 'super' }],
      [{ indent: '-1' }, { indent: '+1' }],
      [{ size: ['small', false, 'large', 'huge'] }],
      [{ header: [1, 2, 3, 4, 5, 6, false] }],
      [{ color: [] }, { background: [] }],
      [{ font: [] }],
      [{ align: [] }],
      ['clean'],
      ['link', 'image'],
    ],
};

export const provideKeycloakAndInterceptor = (env: any) => {
  const urlConditions = [
    createInterceptorCondition<IncludeBearerTokenCondition>({
      // eslint-disable-next-line no-useless-escape
      urlPattern: new RegExp(`^${env.apiUrl}(\/.*)?$`, 'i'),
      bearerPrefix: 'Bearer',
    }),
    createInterceptorCondition<IncludeBearerTokenCondition>({
      // eslint-disable-next-line no-useless-escape
      urlPattern: new RegExp(`^${env.authDomain}(\/.*)?$`, 'i'),
      bearerPrefix: 'Bearer',
    }),
    // you can add more interceptors in this array...
  ];

  // in our case, we put the identity configuration in the environment files
  // const { identityServerUrl, clientId, realm } = environment.auth;

  return [
    provideKeycloak({
      config: {
        url: env.authDomain,
        realm: env.realm,
        clientId: env.clientId,
      },
      initOptions: {
        onLoad: 'check-sso',
        checkLoginIframe: true,
        silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
      },
      features: [
        withAutoRefreshToken({
          sessionTimeout: 300000,
          onInactivityTimeout: 'logout',
        }),
      ],
      providers: [AutoRefreshTokenService, UserActivityService],
    }),
    { provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG, useValue: urlConditions },
  ];
};

export class CustomTranslateLoader implements TranslateLoader {
  constructor(private http: HttpClient) {}

  getTranslation(lang: string): Observable<any> {
    return this.http.get(`/i18n/${lang}.json`).pipe(catchError(() => of({})));
  }
}

export function HttpLoaderFactory(http: HttpClient) {
  return new CustomTranslateLoader(http);
}

export const appConfig = (env: any) => {
  return {
    providers: [
      provideBrowserGlobalErrorListeners(),
      provideRouter(routes, withComponentInputBinding()),
      provideKeycloakAndInterceptor(env),
      provideHttpClient(
        withFetch(),
        withInterceptorsFromDi(),
        withInterceptors([
          apiPrefixInterceptor,
          errorHandlerInterceptor,
          includeBearerTokenInterceptor,
        ]),
      ),
      provideToastr({
        timeOut: 3000,
        positionClass: 'toast-top-right',
        preventDuplicates: true,
        progressBar: true,
        closeButton: true,
        newestOnTop: true,
        enableHtml: true,
        tapToDismiss: true,
        maxOpened: 5,
        autoDismiss: true,
      }),
      provideQuillConfig({
        modules: {
          toolbar: modules.toolbar
        }
      }),
      importProvidersFrom(
        TranslateModule.forRoot({
          defaultLanguage: 'en',
          loader: {
            provide: TranslateLoader,
            useFactory: HttpLoaderFactory,
            deps: [HttpClient],
          },
        }),
      ),
      { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'outline' } },
      { provide: MAT_DATE_FORMATS, useValue: CUSTOM_DATE_FORMATS },
    ],
  };
};
