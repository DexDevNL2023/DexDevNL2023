import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PrestationSoinComponent } from './list/prestation-soin.component';
import { PrestationSoinDetailComponent } from './detail/prestation-soin-detail.component';
import { PrestationSoinUpdateComponent } from './update/prestation-soin-update.component';
import PrestationSoinResolve from './route/prestation-soin-routing-resolve.service';

const prestationSoinRoute: Routes = [
  {
    path: '',
    component: PrestationSoinComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrestationSoinDetailComponent,
    resolve: {
      prestationSoin: PrestationSoinResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrestationSoinUpdateComponent,
    resolve: {
      prestationSoin: PrestationSoinResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrestationSoinUpdateComponent,
    resolve: {
      prestationSoin: PrestationSoinResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default prestationSoinRoute;
