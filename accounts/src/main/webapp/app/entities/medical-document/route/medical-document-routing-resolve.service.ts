import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedicalDocument } from '../medical-document.model';
import { MedicalDocumentService } from '../service/medical-document.service';

const medicalDocumentResolve = (route: ActivatedRouteSnapshot): Observable<null | IMedicalDocument> => {
  const id = route.params['id'];
  if (id) {
    return inject(MedicalDocumentService)
      .find(id)
      .pipe(
        mergeMap((medicalDocument: HttpResponse<IMedicalDocument>) => {
          if (medicalDocument.body) {
            return of(medicalDocument.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default medicalDocumentResolve;
