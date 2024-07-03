import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISinistre } from '../sinistre.model';
import { SinistreService } from '../service/sinistre.service';

const sinistreResolve = (route: ActivatedRouteSnapshot): Observable<null | ISinistre> => {
  const id = route.params['id'];
  if (id) {
    return inject(SinistreService)
      .find(id)
      .pipe(
        mergeMap((sinistre: HttpResponse<ISinistre>) => {
          if (sinistre.body) {
            return of(sinistre.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default sinistreResolve;
