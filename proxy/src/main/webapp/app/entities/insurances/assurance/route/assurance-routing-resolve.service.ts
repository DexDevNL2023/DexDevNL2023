import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssurance } from '../assurance.model';
import { AssuranceService } from '../service/assurance.service';

const assuranceResolve = (route: ActivatedRouteSnapshot): Observable<null | IAssurance> => {
  const id = route.params['id'];
  if (id) {
    return inject(AssuranceService)
      .find(id)
      .pipe(
        mergeMap((assurance: HttpResponse<IAssurance>) => {
          if (assurance.body) {
            return of(assurance.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default assuranceResolve;
