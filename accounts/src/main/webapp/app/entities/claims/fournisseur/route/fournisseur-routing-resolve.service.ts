import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFournisseur } from '../fournisseur.model';
import { FournisseurService } from '../service/fournisseur.service';

const fournisseurResolve = (route: ActivatedRouteSnapshot): Observable<null | IFournisseur> => {
  const id = route.params['id'];
  if (id) {
    return inject(FournisseurService)
      .find(id)
      .pipe(
        mergeMap((fournisseur: HttpResponse<IFournisseur>) => {
          if (fournisseur.body) {
            return of(fournisseur.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default fournisseurResolve;
