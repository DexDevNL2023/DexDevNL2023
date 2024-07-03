import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISinistre, NewSinistre } from '../sinistre.model';

export type PartialUpdateSinistre = Partial<ISinistre> & Pick<ISinistre, 'id'>;

type RestOf<T extends ISinistre | NewSinistre> = Omit<T, 'dateDeclaration' | 'dateTraitement'> & {
  dateDeclaration?: string | null;
  dateTraitement?: string | null;
};

export type RestSinistre = RestOf<ISinistre>;

export type NewRestSinistre = RestOf<NewSinistre>;

export type PartialUpdateRestSinistre = RestOf<PartialUpdateSinistre>;

export type EntityResponseType = HttpResponse<ISinistre>;
export type EntityArrayResponseType = HttpResponse<ISinistre[]>;

@Injectable({ providedIn: 'root' })
export class SinistreService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sinistres', 'claims');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/sinistres/_search', 'claims');

  create(sinistre: NewSinistre): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sinistre);
    return this.http
      .post<RestSinistre>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(sinistre: ISinistre): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sinistre);
    return this.http
      .put<RestSinistre>(`${this.resourceUrl}/${this.getSinistreIdentifier(sinistre)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(sinistre: PartialUpdateSinistre): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sinistre);
    return this.http
      .patch<RestSinistre>(`${this.resourceUrl}/${this.getSinistreIdentifier(sinistre)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSinistre>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSinistre[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestSinistre[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ISinistre[]>()], asapScheduler)),
    );
  }

  getSinistreIdentifier(sinistre: Pick<ISinistre, 'id'>): number {
    return sinistre.id;
  }

  compareSinistre(o1: Pick<ISinistre, 'id'> | null, o2: Pick<ISinistre, 'id'> | null): boolean {
    return o1 && o2 ? this.getSinistreIdentifier(o1) === this.getSinistreIdentifier(o2) : o1 === o2;
  }

  addSinistreToCollectionIfMissing<Type extends Pick<ISinistre, 'id'>>(
    sinistreCollection: Type[],
    ...sinistresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sinistres: Type[] = sinistresToCheck.filter(isPresent);
    if (sinistres.length > 0) {
      const sinistreCollectionIdentifiers = sinistreCollection.map(sinistreItem => this.getSinistreIdentifier(sinistreItem));
      const sinistresToAdd = sinistres.filter(sinistreItem => {
        const sinistreIdentifier = this.getSinistreIdentifier(sinistreItem);
        if (sinistreCollectionIdentifiers.includes(sinistreIdentifier)) {
          return false;
        }
        sinistreCollectionIdentifiers.push(sinistreIdentifier);
        return true;
      });
      return [...sinistresToAdd, ...sinistreCollection];
    }
    return sinistreCollection;
  }

  protected convertDateFromClient<T extends ISinistre | NewSinistre | PartialUpdateSinistre>(sinistre: T): RestOf<T> {
    return {
      ...sinistre,
      dateDeclaration: sinistre.dateDeclaration?.toJSON() ?? null,
      dateTraitement: sinistre.dateTraitement?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSinistre: RestSinistre): ISinistre {
    return {
      ...restSinistre,
      dateDeclaration: restSinistre.dateDeclaration ? dayjs(restSinistre.dateDeclaration) : undefined,
      dateTraitement: restSinistre.dateTraitement ? dayjs(restSinistre.dateTraitement) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSinistre>): HttpResponse<ISinistre> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSinistre[]>): HttpResponse<ISinistre[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
