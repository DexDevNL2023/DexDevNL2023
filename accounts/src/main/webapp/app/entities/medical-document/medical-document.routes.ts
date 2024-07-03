import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MedicalDocumentComponent } from './list/medical-document.component';
import { MedicalDocumentDetailComponent } from './detail/medical-document-detail.component';
import { MedicalDocumentUpdateComponent } from './update/medical-document-update.component';
import MedicalDocumentResolve from './route/medical-document-routing-resolve.service';

const medicalDocumentRoute: Routes = [
  {
    path: '',
    component: MedicalDocumentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MedicalDocumentDetailComponent,
    resolve: {
      medicalDocument: MedicalDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MedicalDocumentUpdateComponent,
    resolve: {
      medicalDocument: MedicalDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MedicalDocumentUpdateComponent,
    resolve: {
      medicalDocument: MedicalDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default medicalDocumentRoute;
