import { Routes } from '@angular/router';
import { AuthenticationGuard } from '@app/auth/authentication.guard';

export const formRoutes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['forms:list'] },
    loadComponent: () => import('./forms').then((module) => module.Forms),
  },
  {
    path: 'edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['forms:edit'] },
    loadComponent: () => import('./edit/form-edit').then((module) => module.FormEdit),
  },
  {
    path: 'submissions/:id',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['form-submissions:view'] },
    loadComponent: () =>
      import('./submissions/form-submission-details').then(
        (module) => module.FormSubmissionDetails,
      ),
  },
  {
    path: ':id/edit',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['forms:edit'] },
    loadComponent: () => import('./edit/form-edit').then((module) => module.FormEdit),
  },
  {
    path: ':id/submissions',
    canActivate: [AuthenticationGuard],
    data: { permissions: ['form-submissions:list'] },
    loadComponent: () =>
      import('./submissions/form-submissions').then((module) => module.FormSubmissions),
  },
];
