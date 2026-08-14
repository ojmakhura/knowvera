import { Routes } from "@angular/router";
import { authGuard } from "@app/@core/guards/auth.guard";

export const individualRoutes: Routes = [
    {
        path: '',
        data: { title: 'Individual Details' },
        canActivate: [authGuard],
        loadComponent: () => import('./details/individual-details').then(m => m.IndividualDetails)
    },
    {
        path: 'edit',
        data: { title: 'Edit Individual' },
        canActivate: [authGuard],
        loadComponent: () => import('./edit/edit-individual').then(m => m.EditIndividual)
    }
]