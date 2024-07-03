import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRapport, NewRapport } from '../rapport.model';

export type PartialUpdateRapport = Partial<IRapport> & Pick<IRapport, 'id'>;

type RestOf<T extends IRapport | NewRapport> = Omit<T, 'dateGeneration'> & {
  dateGeneration?: string | null;
};

export type RestRapport = RestOf<IRapport>;

export type NewRestRapport = RestOf<NewRapport>;

export type PartialUpdateRestRapport = RestOf<PartialUpdateRapport>;

export type EntityResponseType = HttpResponse<IRapport>;
export type EntityArrayResponseType = HttpResponse<IRapport[]>;

@Injectable({ providedIn: 'root' })
export class RapportService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rapports', 'insurances');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/rapports/_search', 'insurances');

  create(rapport: NewRapport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rapport);
    return this.http
      .post<RestRapport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(rapport: IRapport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rapport);
    return this.http
      .put<RestRapport>(`${this.resourceUrl}/${this.getRapportIdentifier(rapport)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(rapport: PartialUpdateRapport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rapport);
    return this.http
      .patch<RestRapport>(`${this.resourceUrl}/${this.getRapportIdentifier(rapport)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRapport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRapport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestRapport[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IRapport[]>()], asapScheduler)),
    );
  }

  getRapportIdentifier(rapport: Pick<IRapport, 'id'>): number {
    return rapport.id;
  }

  compareRapport(o1: Pick<IRapport, 'id'> | null, o2: Pick<IRapport, 'id'> | null): boolean {
    return o1 && o2 ? this.getRapportIdentifier(o1) === this.getRapportIdentifier(o2) : o1 === o2;
  }

  addRapportToCollectionIfMissing<Type extends Pick<IRapport, 'id'>>(
    rapportCollection: Type[],
    ...rapportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rapports: Type[] = rapportsToCheck.filter(isPresent);
    if (rapports.length > 0) {
      const rapportCollectionIdentifiers = rapportCollection.map(rapportItem => this.getRapportIdentifier(rapportItem));
      const rapportsToAdd = rapports.filter(rapportItem => {
        const rapportIdentifier = this.getRapportIdentifier(rapportItem);
        if (rapportCollectionIdentifiers.includes(rapportIdentifier)) {
          return false;
        }
        rapportCollectionIdentifiers.push(rapportIdentifier);
        return true;
      });
      return [...rapportsToAdd, ...rapportCollection];
    }
    return rapportCollection;
  }

  protected convertDateFromClient<T extends IRapport | NewRapport | PartialUpdateRapport>(rapport: T): RestOf<T> {
    return {
      ...rapport,
      dateGeneration: rapport.dateGeneration?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRapport: RestRapport): IRapport {
    return {
      ...restRapport,
      dateGeneration: restRapport.dateGeneration ? dayjs(restRapport.dateGeneration) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRapport>): HttpResponse<IRapport> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRapport[]>): HttpResponse<IRapport[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
