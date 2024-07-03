import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPolicesAssurances } from '../polices-assurances.model';
import { PolicesAssurancesService } from '../service/polices-assurances.service';

const policesAssurancesResolve = (route: ActivatedRouteSnapshot): Observable<null | IPolicesAssurances> => {
  const id = route.params['id'];
  if (id) {
    return inject(PolicesAssurancesService)
      .find(id)
      .pipe(
        mergeMap((policesAssurances: HttpResponse<IPolicesAssurances>) => {
          if (policesAssurances.body) {
            return of(policesAssurances.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default policesAssurancesResolve;
