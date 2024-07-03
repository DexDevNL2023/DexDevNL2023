import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FinanceurSoinComponent } from './list/financeur-soin.component';
import { FinanceurSoinDetailComponent } from './detail/financeur-soin-detail.component';
import { FinanceurSoinUpdateComponent } from './update/financeur-soin-update.component';
import FinanceurSoinResolve from './route/financeur-soin-routing-resolve.service';

const financeurSoinRoute: Routes = [
  {
    path: '',
    component: FinanceurSoinComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FinanceurSoinDetailComponent,
    resolve: {
      financeurSoin: FinanceurSoinResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FinanceurSoinUpdateComponent,
    resolve: {
      financeurSoin: FinanceurSoinResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FinanceurSoinUpdateComponent,
    resolve: {
      financeurSoin: FinanceurSoinResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default financeurSoinRoute;
