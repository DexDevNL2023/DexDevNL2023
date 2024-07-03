import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedicalRecord } from '../medical-record.model';
import { MedicalRecordService } from '../service/medical-record.service';

const medicalRecordResolve = (route: ActivatedRouteSnapshot): Observable<null | IMedicalRecord> => {
  const id = route.params['id'];
  if (id) {
    return inject(MedicalRecordService)
      .find(id)
      .pipe(
        mergeMap((medicalRecord: HttpResponse<IMedicalRecord>) => {
          if (medicalRecord.body) {
            return of(medicalRecord.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default medicalRecordResolve;
