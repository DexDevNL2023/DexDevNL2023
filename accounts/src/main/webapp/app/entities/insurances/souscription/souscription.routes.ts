import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SouscriptionComponent } from './list/souscription.component';
import { SouscriptionDetailComponent } from './detail/souscription-detail.component';
import { SouscriptionUpdateComponent } from './update/souscription-update.component';
import SouscriptionResolve from './route/souscription-routing-resolve.service';

const souscriptionRoute: Routes = [
  {
    path: '',
    component: SouscriptionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SouscriptionDetailComponent,
    resolve: {
      souscription: SouscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SouscriptionUpdateComponent,
    resolve: {
      souscription: SouscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SouscriptionUpdateComponent,
    resolve: {
      souscription: SouscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default souscriptionRoute;
