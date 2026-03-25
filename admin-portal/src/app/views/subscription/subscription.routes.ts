import { Routes } from '@angular/router';

export const subscriptionRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./subscriptions').then((module) => module.Subscriptions),
  },
  {
    path: 'edit',
    loadComponent: () =>
      import('./edit/subscription-edit').then((module) => module.SubscriptionEdit),
  },
  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./edit/subscription-edit').then((module) => module.SubscriptionEdit),
  },
  {
    path: 'details',
    loadComponent: () =>
      import('./details/subscription-details').then((module) => module.SubscriptionDetails),
  },
  {
    path: 'details/:id',
    loadComponent: () =>
      import('./details/subscription-details').then((module) => module.SubscriptionDetails),
  },
];
