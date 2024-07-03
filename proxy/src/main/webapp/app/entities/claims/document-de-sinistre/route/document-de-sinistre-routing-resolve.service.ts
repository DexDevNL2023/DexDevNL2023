import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentDeSinistre } from '../document-de-sinistre.model';
import { DocumentDeSinistreService } from '../service/document-de-sinistre.service';

const documentDeSinistreResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentDeSinistre> => {
  const id = route.params['id'];
  if (id) {
    return inject(DocumentDeSinistreService)
      .find(id)
      .pipe(
        mergeMap((documentDeSinistre: HttpResponse<IDocumentDeSinistre>) => {
          if (documentDeSinistre.body) {
            return of(documentDeSinistre.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default documentDeSinistreResolve;
