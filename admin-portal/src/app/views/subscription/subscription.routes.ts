import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const subscriptionRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['subscriptions:list'] },
    loadComponent: () =>
      import('./subscriptions').then((module) => module.Subscriptions),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['subscriptions:edit'] },
    loadComponent: () =>
      import('./edit/subscription-edit').then((module) => module.SubscriptionEdit),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['subscriptions:edit'] },
    loadComponent: () =>
      import('./edit/subscription-edit').then((module) => module.SubscriptionEdit),
  },
  {
    path: ':id',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['subscriptions:view'] },
    loadComponent: () =>
      import('./details/subscription-details').then((module) => module.SubscriptionDetails),
  },
];
