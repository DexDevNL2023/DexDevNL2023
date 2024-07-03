import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFinanceurSoin } from '../financeur-soin.model';
import { FinanceurSoinService } from '../service/financeur-soin.service';

const financeurSoinResolve = (route: ActivatedRouteSnapshot): Observable<null | IFinanceurSoin> => {
  const id = route.params['id'];
  if (id) {
    return inject(FinanceurSoinService)
      .find(id)
      .pipe(
        mergeMap((financeurSoin: HttpResponse<IFinanceurSoin>) => {
          if (financeurSoin.body) {
            return of(financeurSoin.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default financeurSoinResolve;
