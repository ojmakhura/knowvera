import { Routes } from '@angular/router';

export const recordsRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./records').then((module) => module.Records),
  },
];
