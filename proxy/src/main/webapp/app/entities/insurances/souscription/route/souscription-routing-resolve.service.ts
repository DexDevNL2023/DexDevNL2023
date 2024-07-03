import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISouscription } from '../souscription.model';
import { SouscriptionService } from '../service/souscription.service';

const souscriptionResolve = (route: ActivatedRouteSnapshot): Observable<null | ISouscription> => {
  const id = route.params['id'];
  if (id) {
    return inject(SouscriptionService)
      .find(id)
      .pipe(
        mergeMap((souscription: HttpResponse<ISouscription>) => {
          if (souscription.body) {
            return of(souscription.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default souscriptionResolve;
