import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UserExtraComponent } from './list/user-extra.component';
import { UserExtraDetailComponent } from './detail/user-extra-detail.component';
import { UserExtraUpdateComponent } from './update/user-extra-update.component';
import UserExtraResolve from './route/user-extra-routing-resolve.service';

const userExtraRoute: Routes = [
  {
    path: '',
    component: UserExtraComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserExtraDetailComponent,
    resolve: {
      userExtra: UserExtraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserExtraUpdateComponent,
    resolve: {
      userExtra: UserExtraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserExtraUpdateComponent,
    resolve: {
      userExtra: UserExtraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userExtraRoute;
