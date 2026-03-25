import { Routes } from '@angular/router';
import { AuthenticationGuard } from './auth/authentication.guard';
import { SystemSettings } from './views/settings/system-settings';

export const routes: Routes = [
	{
		path: 'document',
		canActivate: [AuthenticationGuard],
		loadChildren: () =>
			import('./views/document/document.routes').then((module) => module.documentRoutes),
	},
	{
		path: 'organisation',
		canActivate: [AuthenticationGuard],
		loadChildren: () =>
			import('./views/organisation/organisation.routes').then((module) => module.organisationRoutes),
	},
	{
		path: 'settings',
		canActivate: [AuthenticationGuard],
		component: SystemSettings
	},
];