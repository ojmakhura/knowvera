import { Component, signal, inject, effect } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { TranslationService } from './services/translation.service';
import { Shell } from './shell/shell';

import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType, ReadyArgs, typeEventArgs } from 'keycloak-angular';
import Keycloak from 'keycloak-js';
import { AppEnvStore } from './store/app-env.state';

@Component({
  selector: 'app-root',
  imports: [TranslateModule, Shell],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('portal');
  protected readonly translationService = inject(TranslationService);
  readonly appEnvState = inject(AppEnvStore);
  env = this.appEnvState.env;
  private keycloakSignal = inject(KEYCLOAK_EVENT_SIGNAL);
  private keycloak = inject(Keycloak);

  constructor() {
    // Translation service is initialized automatically via constructor
    effect(() => {
      const keycloakEvent = this.keycloakSignal();

      console.log(this.keycloak.authenticated);

      if (keycloakEvent.type === KeycloakEventType.Ready) {
        if (this.keycloak.authenticated) {
          this.appEnvState.getEnv();
          this.appEnvState.setIsLoggedIn(typeEventArgs<ReadyArgs>(keycloakEvent.args));
          this.keycloak.loadUserProfile().then((profile) => {
            console.log(profile);
            this.appEnvState.setProfile({
              firstName: profile.firstName || '',
              lastName: profile.lastName || '',
              email: profile.email || '',
              username: profile.username || ''
            });
          });

          this.keycloak.loadUserInfo().then((userInfo) => {
            console.log(userInfo);
          });
        }
      }

      if (keycloakEvent.type === KeycloakEventType.AuthLogout) {
        this.appEnvState.reset();
      }
    });

    effect(() => {
      if (this.env()) {
        this.appEnvState.setAccountUri(
          `${this.env().authDomain}/realms/${this.env().realm}/account?referrer=' + ${encodeURIComponent(this.env().clientId)}&referrer_uri=' + ${encodeURIComponent(this.env().redirectUri)}`,
        );
      }
    });
  }
}
