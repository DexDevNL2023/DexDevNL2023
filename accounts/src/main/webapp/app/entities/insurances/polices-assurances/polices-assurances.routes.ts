import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PolicesAssurancesComponent } from './list/polices-assurances.component';
import { PolicesAssurancesDetailComponent } from './detail/polices-assurances-detail.component';
import { PolicesAssurancesUpdateComponent } from './update/polices-assurances-update.component';
import PolicesAssurancesResolve from './route/polices-assurances-routing-resolve.service';

const policesAssurancesRoute: Routes = [
  {
    path: '',
    component: PolicesAssurancesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PolicesAssurancesDetailComponent,
    resolve: {
      policesAssurances: PolicesAssurancesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PolicesAssurancesUpdateComponent,
    resolve: {
      policesAssurances: PolicesAssurancesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PolicesAssurancesUpdateComponent,
    resolve: {
      policesAssurances: PolicesAssurancesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default policesAssurancesRoute;
