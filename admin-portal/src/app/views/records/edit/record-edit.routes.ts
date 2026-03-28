import { Routes } from '@angular/router';

export const recordEditRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./record-edit').then((module) => module.RecordEdit),
  },
];
