import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SinistreComponent } from './list/sinistre.component';
import { SinistreDetailComponent } from './detail/sinistre-detail.component';
import { SinistreUpdateComponent } from './update/sinistre-update.component';
import SinistreResolve from './route/sinistre-routing-resolve.service';

const sinistreRoute: Routes = [
  {
    path: '',
    component: SinistreComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SinistreDetailComponent,
    resolve: {
      sinistre: SinistreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SinistreUpdateComponent,
    resolve: {
      sinistre: SinistreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SinistreUpdateComponent,
    resolve: {
      sinistre: SinistreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sinistreRoute;
