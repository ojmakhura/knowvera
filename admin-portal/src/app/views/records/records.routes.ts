import { Routes } from '@angular/router';

export const recordsRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./records').then((module) => module.Records),
  },
  {
    path: 'details/:id',
    loadComponent: () => import('./details/record-details').then((module) => module.RecordDetails),
  },
  {
    path: 'details',
    loadComponent: () => import('./details/record-details').then((module) => module.RecordDetails),
  },
  {
    path: 'edit/:id',
    loadChildren: () => import('./edit/record-edit.routes').then(m => m.recordEditRoutes),
  },
  {
    path: 'edit',
    loadChildren: () => import('./edit/record-edit.routes').then(m => m.recordEditRoutes),
  },
];
