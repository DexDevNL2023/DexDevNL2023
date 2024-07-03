import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRapport } from '../rapport.model';
import { RapportService } from '../service/rapport.service';

const rapportResolve = (route: ActivatedRouteSnapshot): Observable<null | IRapport> => {
  const id = route.params['id'];
  if (id) {
    return inject(RapportService)
      .find(id)
      .pipe(
        mergeMap((rapport: HttpResponse<IRapport>) => {
          if (rapport.body) {
            return of(rapport.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default rapportResolve;
