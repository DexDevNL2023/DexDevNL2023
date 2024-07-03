import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IPrestationSoin, NewPrestationSoin } from '../prestation-soin.model';

export type PartialUpdatePrestationSoin = Partial<IPrestationSoin> & Pick<IPrestationSoin, 'id'>;

export type EntityResponseType = HttpResponse<IPrestationSoin>;
export type EntityArrayResponseType = HttpResponse<IPrestationSoin[]>;

@Injectable({ providedIn: 'root' })
export class PrestationSoinService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prestation-soins', 'claims');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/prestation-soins/_search', 'claims');

  create(prestationSoin: NewPrestationSoin): Observable<EntityResponseType> {
    return this.http.post<IPrestationSoin>(this.resourceUrl, prestationSoin, { observe: 'response' });
  }

  update(prestationSoin: IPrestationSoin): Observable<EntityResponseType> {
    return this.http.put<IPrestationSoin>(`${this.resourceUrl}/${this.getPrestationSoinIdentifier(prestationSoin)}`, prestationSoin, {
      observe: 'response',
    });
  }

  partialUpdate(prestationSoin: PartialUpdatePrestationSoin): Observable<EntityResponseType> {
    return this.http.patch<IPrestationSoin>(`${this.resourceUrl}/${this.getPrestationSoinIdentifier(prestationSoin)}`, prestationSoin, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrestationSoin>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrestationSoin[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrestationSoin[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IPrestationSoin[]>()], asapScheduler)));
  }

  getPrestationSoinIdentifier(prestationSoin: Pick<IPrestationSoin, 'id'>): number {
    return prestationSoin.id;
  }

  comparePrestationSoin(o1: Pick<IPrestationSoin, 'id'> | null, o2: Pick<IPrestationSoin, 'id'> | null): boolean {
    return o1 && o2 ? this.getPrestationSoinIdentifier(o1) === this.getPrestationSoinIdentifier(o2) : o1 === o2;
  }

  addPrestationSoinToCollectionIfMissing<Type extends Pick<IPrestationSoin, 'id'>>(
    prestationSoinCollection: Type[],
    ...prestationSoinsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const prestationSoins: Type[] = prestationSoinsToCheck.filter(isPresent);
    if (prestationSoins.length > 0) {
      const prestationSoinCollectionIdentifiers = prestationSoinCollection.map(prestationSoinItem =>
        this.getPrestationSoinIdentifier(prestationSoinItem),
      );
      const prestationSoinsToAdd = prestationSoins.filter(prestationSoinItem => {
        const prestationSoinIdentifier = this.getPrestationSoinIdentifier(prestationSoinItem);
        if (prestationSoinCollectionIdentifiers.includes(prestationSoinIdentifier)) {
          return false;
        }
        prestationSoinCollectionIdentifiers.push(prestationSoinIdentifier);
        return true;
      });
      return [...prestationSoinsToAdd, ...prestationSoinCollection];
    }
    return prestationSoinCollection;
  }
}
