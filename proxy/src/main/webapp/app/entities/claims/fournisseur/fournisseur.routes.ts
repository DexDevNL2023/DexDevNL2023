import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FournisseurComponent } from './list/fournisseur.component';
import { FournisseurDetailComponent } from './detail/fournisseur-detail.component';
import { FournisseurUpdateComponent } from './update/fournisseur-update.component';
import FournisseurResolve from './route/fournisseur-routing-resolve.service';

const fournisseurRoute: Routes = [
  {
    path: '',
    component: FournisseurComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FournisseurDetailComponent,
    resolve: {
      fournisseur: FournisseurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FournisseurUpdateComponent,
    resolve: {
      fournisseur: FournisseurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FournisseurUpdateComponent,
    resolve: {
      fournisseur: FournisseurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default fournisseurRoute;
