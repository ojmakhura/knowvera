import { AppEnvStore } from './store/app-env.state';
import { Component, effect, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TranslationService } from '@core/services/translation.service';
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType, ReadyArgs, typeEventArgs } from 'keycloak-angular';
import Keycloak from 'keycloak-js';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('application');
  private translationService = inject(TranslationService);
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
            this.appEnvState.setProfile({
              firstName: profile.firstName || '',
              lastName: profile.lastName || '',
              email: profile.email || '',
              username: profile.username || ''
            });
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
