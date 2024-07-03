import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISouscription, NewSouscription } from '../souscription.model';

export type PartialUpdateSouscription = Partial<ISouscription> & Pick<ISouscription, 'id'>;

type RestOf<T extends ISouscription | NewSouscription> = Omit<T, 'dateSouscription'> & {
  dateSouscription?: string | null;
};

export type RestSouscription = RestOf<ISouscription>;

export type NewRestSouscription = RestOf<NewSouscription>;

export type PartialUpdateRestSouscription = RestOf<PartialUpdateSouscription>;

export type EntityResponseType = HttpResponse<ISouscription>;
export type EntityArrayResponseType = HttpResponse<ISouscription[]>;

@Injectable({ providedIn: 'root' })
export class SouscriptionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/souscriptions', 'insurances');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/souscriptions/_search', 'insurances');

  create(souscription: NewSouscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(souscription);
    return this.http
      .post<RestSouscription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(souscription: ISouscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(souscription);
    return this.http
      .put<RestSouscription>(`${this.resourceUrl}/${this.getSouscriptionIdentifier(souscription)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(souscription: PartialUpdateSouscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(souscription);
    return this.http
      .patch<RestSouscription>(`${this.resourceUrl}/${this.getSouscriptionIdentifier(souscription)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSouscription>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSouscription[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestSouscription[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ISouscription[]>()], asapScheduler)),
    );
  }

  getSouscriptionIdentifier(souscription: Pick<ISouscription, 'id'>): number {
    return souscription.id;
  }

  compareSouscription(o1: Pick<ISouscription, 'id'> | null, o2: Pick<ISouscription, 'id'> | null): boolean {
    return o1 && o2 ? this.getSouscriptionIdentifier(o1) === this.getSouscriptionIdentifier(o2) : o1 === o2;
  }

  addSouscriptionToCollectionIfMissing<Type extends Pick<ISouscription, 'id'>>(
    souscriptionCollection: Type[],
    ...souscriptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const souscriptions: Type[] = souscriptionsToCheck.filter(isPresent);
    if (souscriptions.length > 0) {
      const souscriptionCollectionIdentifiers = souscriptionCollection.map(souscriptionItem =>
        this.getSouscriptionIdentifier(souscriptionItem),
      );
      const souscriptionsToAdd = souscriptions.filter(souscriptionItem => {
        const souscriptionIdentifier = this.getSouscriptionIdentifier(souscriptionItem);
        if (souscriptionCollectionIdentifiers.includes(souscriptionIdentifier)) {
          return false;
        }
        souscriptionCollectionIdentifiers.push(souscriptionIdentifier);
        return true;
      });
      return [...souscriptionsToAdd, ...souscriptionCollection];
    }
    return souscriptionCollection;
  }

  protected convertDateFromClient<T extends ISouscription | NewSouscription | PartialUpdateSouscription>(souscription: T): RestOf<T> {
    return {
      ...souscription,
      dateSouscription: souscription.dateSouscription?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSouscription: RestSouscription): ISouscription {
    return {
      ...restSouscription,
      dateSouscription: restSouscription.dateSouscription ? dayjs(restSouscription.dateSouscription) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSouscription>): HttpResponse<ISouscription> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSouscription[]>): HttpResponse<ISouscription[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
