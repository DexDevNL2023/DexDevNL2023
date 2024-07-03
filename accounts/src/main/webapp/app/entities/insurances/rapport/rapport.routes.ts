import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RapportComponent } from './list/rapport.component';
import { RapportDetailComponent } from './detail/rapport-detail.component';
import { RapportUpdateComponent } from './update/rapport-update.component';
import RapportResolve from './route/rapport-routing-resolve.service';

const rapportRoute: Routes = [
  {
    path: '',
    component: RapportComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RapportDetailComponent,
    resolve: {
      rapport: RapportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RapportUpdateComponent,
    resolve: {
      rapport: RapportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RapportUpdateComponent,
    resolve: {
      rapport: RapportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default rapportRoute;
