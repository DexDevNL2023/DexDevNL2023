import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IPolicesAssurances, NewPolicesAssurances } from '../polices-assurances.model';

export type PartialUpdatePolicesAssurances = Partial<IPolicesAssurances> & Pick<IPolicesAssurances, 'id'>;

type RestOf<T extends IPolicesAssurances | NewPolicesAssurances> = Omit<T, 'dateEmission' | 'dateExpiration'> & {
  dateEmission?: string | null;
  dateExpiration?: string | null;
};

export type RestPolicesAssurances = RestOf<IPolicesAssurances>;

export type NewRestPolicesAssurances = RestOf<NewPolicesAssurances>;

export type PartialUpdateRestPolicesAssurances = RestOf<PartialUpdatePolicesAssurances>;

export type EntityResponseType = HttpResponse<IPolicesAssurances>;
export type EntityArrayResponseType = HttpResponse<IPolicesAssurances[]>;

@Injectable({ providedIn: 'root' })
export class PolicesAssurancesService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/polices-assurances', 'insurances');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/polices-assurances/_search', 'insurances');

  create(policesAssurances: NewPolicesAssurances): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(policesAssurances);
    return this.http
      .post<RestPolicesAssurances>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(policesAssurances: IPolicesAssurances): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(policesAssurances);
    return this.http
      .put<RestPolicesAssurances>(`${this.resourceUrl}/${this.getPolicesAssurancesIdentifier(policesAssurances)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(policesAssurances: PartialUpdatePolicesAssurances): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(policesAssurances);
    return this.http
      .patch<RestPolicesAssurances>(`${this.resourceUrl}/${this.getPolicesAssurancesIdentifier(policesAssurances)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPolicesAssurances>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPolicesAssurances[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestPolicesAssurances[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IPolicesAssurances[]>()], asapScheduler)),
    );
  }

  getPolicesAssurancesIdentifier(policesAssurances: Pick<IPolicesAssurances, 'id'>): number {
    return policesAssurances.id;
  }

  comparePolicesAssurances(o1: Pick<IPolicesAssurances, 'id'> | null, o2: Pick<IPolicesAssurances, 'id'> | null): boolean {
    return o1 && o2 ? this.getPolicesAssurancesIdentifier(o1) === this.getPolicesAssurancesIdentifier(o2) : o1 === o2;
  }

  addPolicesAssurancesToCollectionIfMissing<Type extends Pick<IPolicesAssurances, 'id'>>(
    policesAssurancesCollection: Type[],
    ...policesAssurancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const policesAssurances: Type[] = policesAssurancesToCheck.filter(isPresent);
    if (policesAssurances.length > 0) {
      const policesAssurancesCollectionIdentifiers = policesAssurancesCollection.map(policesAssurancesItem =>
        this.getPolicesAssurancesIdentifier(policesAssurancesItem),
      );
      const policesAssurancesToAdd = policesAssurances.filter(policesAssurancesItem => {
        const policesAssurancesIdentifier = this.getPolicesAssurancesIdentifier(policesAssurancesItem);
        if (policesAssurancesCollectionIdentifiers.includes(policesAssurancesIdentifier)) {
          return false;
        }
        policesAssurancesCollectionIdentifiers.push(policesAssurancesIdentifier);
        return true;
      });
      return [...policesAssurancesToAdd, ...policesAssurancesCollection];
    }
    return policesAssurancesCollection;
  }

  protected convertDateFromClient<T extends IPolicesAssurances | NewPolicesAssurances | PartialUpdatePolicesAssurances>(
    policesAssurances: T,
  ): RestOf<T> {
    return {
      ...policesAssurances,
      dateEmission: policesAssurances.dateEmission?.toJSON() ?? null,
      dateExpiration: policesAssurances.dateExpiration?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPolicesAssurances: RestPolicesAssurances): IPolicesAssurances {
    return {
      ...restPolicesAssurances,
      dateEmission: restPolicesAssurances.dateEmission ? dayjs(restPolicesAssurances.dateEmission) : undefined,
      dateExpiration: restPolicesAssurances.dateExpiration ? dayjs(restPolicesAssurances.dateExpiration) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPolicesAssurances>): HttpResponse<IPolicesAssurances> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPolicesAssurances[]>): HttpResponse<IPolicesAssurances[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
