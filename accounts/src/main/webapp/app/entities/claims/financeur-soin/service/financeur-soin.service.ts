import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFinanceurSoin, NewFinanceurSoin } from '../financeur-soin.model';

export type PartialUpdateFinanceurSoin = Partial<IFinanceurSoin> & Pick<IFinanceurSoin, 'id'>;

export type EntityResponseType = HttpResponse<IFinanceurSoin>;
export type EntityArrayResponseType = HttpResponse<IFinanceurSoin[]>;

@Injectable({ providedIn: 'root' })
export class FinanceurSoinService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/financeur-soins', 'claims');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/financeur-soins/_search', 'claims');

  create(financeurSoin: NewFinanceurSoin): Observable<EntityResponseType> {
    return this.http.post<IFinanceurSoin>(this.resourceUrl, financeurSoin, { observe: 'response' });
  }

  update(financeurSoin: IFinanceurSoin): Observable<EntityResponseType> {
    return this.http.put<IFinanceurSoin>(`${this.resourceUrl}/${this.getFinanceurSoinIdentifier(financeurSoin)}`, financeurSoin, {
      observe: 'response',
    });
  }

  partialUpdate(financeurSoin: PartialUpdateFinanceurSoin): Observable<EntityResponseType> {
    return this.http.patch<IFinanceurSoin>(`${this.resourceUrl}/${this.getFinanceurSoinIdentifier(financeurSoin)}`, financeurSoin, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFinanceurSoin>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFinanceurSoin[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFinanceurSoin[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IFinanceurSoin[]>()], asapScheduler)));
  }

  getFinanceurSoinIdentifier(financeurSoin: Pick<IFinanceurSoin, 'id'>): number {
    return financeurSoin.id;
  }

  compareFinanceurSoin(o1: Pick<IFinanceurSoin, 'id'> | null, o2: Pick<IFinanceurSoin, 'id'> | null): boolean {
    return o1 && o2 ? this.getFinanceurSoinIdentifier(o1) === this.getFinanceurSoinIdentifier(o2) : o1 === o2;
  }

  addFinanceurSoinToCollectionIfMissing<Type extends Pick<IFinanceurSoin, 'id'>>(
    financeurSoinCollection: Type[],
    ...financeurSoinsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const financeurSoins: Type[] = financeurSoinsToCheck.filter(isPresent);
    if (financeurSoins.length > 0) {
      const financeurSoinCollectionIdentifiers = financeurSoinCollection.map(financeurSoinItem =>
        this.getFinanceurSoinIdentifier(financeurSoinItem),
      );
      const financeurSoinsToAdd = financeurSoins.filter(financeurSoinItem => {
        const financeurSoinIdentifier = this.getFinanceurSoinIdentifier(financeurSoinItem);
        if (financeurSoinCollectionIdentifiers.includes(financeurSoinIdentifier)) {
          return false;
        }
        financeurSoinCollectionIdentifiers.push(financeurSoinIdentifier);
        return true;
      });
      return [...financeurSoinsToAdd, ...financeurSoinCollection];
    }
    return financeurSoinCollection;
  }
}
