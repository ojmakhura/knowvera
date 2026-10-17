import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const subscriptionRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./subscriptions').then((module) => module.Subscriptions),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/subscription-edit').then((module) => module.SubscriptionEdit),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./edit/subscription-edit').then((module) => module.SubscriptionEdit),
  },
  {
    path: ':id',
    canActivate: [AuthenticationGuard],
    loadComponent: () =>
      import('./details/subscription-details').then((module) => module.SubscriptionDetails),
  },
];
