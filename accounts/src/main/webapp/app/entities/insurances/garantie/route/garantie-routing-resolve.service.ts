import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGarantie } from '../garantie.model';
import { GarantieService } from '../service/garantie.service';

const garantieResolve = (route: ActivatedRouteSnapshot): Observable<null | IGarantie> => {
  const id = route.params['id'];
  if (id) {
    return inject(GarantieService)
      .find(id)
      .pipe(
        mergeMap((garantie: HttpResponse<IGarantie>) => {
          if (garantie.body) {
            return of(garantie.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default garantieResolve;
