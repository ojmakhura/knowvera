import { inject, Injectable, signal } from '@angular/core';
import Keycloak from 'keycloak-js';
import { AppEnvStore } from '@app/store/app-env.state';
import { Router } from '@angular/router';

export interface UserProfile {
  firstName: string;
  lastName: string;
  email: string;
  username: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private keycloak = inject(Keycloak);
  private appEnvState = inject(AppEnvStore);
  private router = inject(Router);

  readonly isAuthenticated = signal(false);
  readonly userProfile = signal<UserProfile | null>(null);

  constructor() {
    this.isAuthenticated.set(this.keycloak.authenticated || false);
  }

  async login(redirectUri?: string): Promise<void> {
    try {
      await this.keycloak.login({
        redirectUri: redirectUri || window.location.origin,
      });
    } catch (error) {
      console.error('Login failed:', error);
      throw error;
    }
  }

  async logout(): Promise<void> {
    try {
      this.appEnvState.reset();
      this.isAuthenticated.set(false);
      this.userProfile.set(null);
      
      await this.keycloak.logout({
        redirectUri: window.location.origin,
      });
    } catch (error) {
      console.error('Logout failed:', error);
      throw error;
    }
  }

  async loadUserProfile(): Promise<UserProfile | null> {
    if (!this.keycloak.authenticated) {
      return null;
    }

    try {
      const profile = await this.keycloak.loadUserProfile();
      const userProfile: UserProfile = {
        firstName: profile.firstName || '',
        lastName: profile.lastName || '',
        email: profile.email || '',
        username: profile.username || '',
      };
      
      this.userProfile.set(userProfile);
      this.appEnvState.setProfile(userProfile);
      return userProfile;
    } catch (error) {
      console.error('Failed to load user profile:', error);
      return null;
    }
  }

  hasRole(role: string): boolean {
    return this.keycloak.hasRealmRole(role);
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some((role) => this.hasRole(role));
  }

  getToken(): string | undefined {
    return this.keycloak.token;
  }

  async updateToken(minValidity: number = 30): Promise<boolean> {
    try {
      return await this.keycloak.updateToken(minValidity);
    } catch (error) {
      console.error('Failed to update token:', error);
      return false;
    }
  }

  getAccountUrl(): string {
    const env = this.appEnvState.env();
    if (!env) {
      return '#';
    }
    
    return `${env.authDomain}/realms/${env.realm}/account?referrer=${encodeURIComponent(env.clientId)}&referrer_uri=${encodeURIComponent(window.location.origin)}`;
  }
}
