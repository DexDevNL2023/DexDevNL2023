import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { GarantieComponent } from './list/garantie.component';
import { GarantieDetailComponent } from './detail/garantie-detail.component';
import { GarantieUpdateComponent } from './update/garantie-update.component';
import GarantieResolve from './route/garantie-routing-resolve.service';

const garantieRoute: Routes = [
  {
    path: '',
    component: GarantieComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GarantieDetailComponent,
    resolve: {
      garantie: GarantieResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GarantieUpdateComponent,
    resolve: {
      garantie: GarantieResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GarantieUpdateComponent,
    resolve: {
      garantie: GarantieResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default garantieRoute;
