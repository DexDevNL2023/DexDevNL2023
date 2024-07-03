import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DocumentDeSinistreComponent } from './list/document-de-sinistre.component';
import { DocumentDeSinistreDetailComponent } from './detail/document-de-sinistre-detail.component';
import { DocumentDeSinistreUpdateComponent } from './update/document-de-sinistre-update.component';
import DocumentDeSinistreResolve from './route/document-de-sinistre-routing-resolve.service';

const documentDeSinistreRoute: Routes = [
  {
    path: '',
    component: DocumentDeSinistreComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentDeSinistreDetailComponent,
    resolve: {
      documentDeSinistre: DocumentDeSinistreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentDeSinistreUpdateComponent,
    resolve: {
      documentDeSinistre: DocumentDeSinistreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentDeSinistreUpdateComponent,
    resolve: {
      documentDeSinistre: DocumentDeSinistreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentDeSinistreRoute;
