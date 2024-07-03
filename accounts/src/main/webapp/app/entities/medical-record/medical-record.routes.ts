import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MedicalRecordComponent } from './list/medical-record.component';
import { MedicalRecordDetailComponent } from './detail/medical-record-detail.component';
import { MedicalRecordUpdateComponent } from './update/medical-record-update.component';
import MedicalRecordResolve from './route/medical-record-routing-resolve.service';

const medicalRecordRoute: Routes = [
  {
    path: '',
    component: MedicalRecordComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MedicalRecordDetailComponent,
    resolve: {
      medicalRecord: MedicalRecordResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MedicalRecordUpdateComponent,
    resolve: {
      medicalRecord: MedicalRecordResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MedicalRecordUpdateComponent,
    resolve: {
      medicalRecord: MedicalRecordResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default medicalRecordRoute;
