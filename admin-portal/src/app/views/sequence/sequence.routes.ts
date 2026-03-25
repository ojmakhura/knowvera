import { Routes } from '@angular/router';

export const sequenceRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./sequences').then((module) => module.Sequences),
  },
  {
    path: 'edit',
    loadComponent: () =>
      import('./edit/sequence-edit').then((module) => module.SequenceEdit),
  },
  {
    path: 'edit/:id',
    loadComponent: () =>
      import('./edit/sequence-edit').then((module) => module.SequenceEdit),
  },
];
