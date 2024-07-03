import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrestationSoin } from '../prestation-soin.model';
import { PrestationSoinService } from '../service/prestation-soin.service';

const prestationSoinResolve = (route: ActivatedRouteSnapshot): Observable<null | IPrestationSoin> => {
  const id = route.params['id'];
  if (id) {
    return inject(PrestationSoinService)
      .find(id)
      .pipe(
        mergeMap((prestationSoin: HttpResponse<IPrestationSoin>) => {
          if (prestationSoin.body) {
            return of(prestationSoin.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default prestationSoinResolve;
