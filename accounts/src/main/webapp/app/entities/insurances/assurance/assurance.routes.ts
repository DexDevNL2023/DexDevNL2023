import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AssuranceComponent } from './list/assurance.component';
import { AssuranceDetailComponent } from './detail/assurance-detail.component';
import { AssuranceUpdateComponent } from './update/assurance-update.component';
import AssuranceResolve from './route/assurance-routing-resolve.service';

const assuranceRoute: Routes = [
  {
    path: '',
    component: AssuranceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssuranceDetailComponent,
    resolve: {
      assurance: AssuranceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssuranceUpdateComponent,
    resolve: {
      assurance: AssuranceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssuranceUpdateComponent,
    resolve: {
      assurance: AssuranceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default assuranceRoute;
