import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFournisseur, NewFournisseur } from '../fournisseur.model';

export type PartialUpdateFournisseur = Partial<IFournisseur> & Pick<IFournisseur, 'id'>;

type RestOf<T extends IFournisseur | NewFournisseur> = Omit<T, 'dateDebutContrat' | 'dateFinContrat'> & {
  dateDebutContrat?: string | null;
  dateFinContrat?: string | null;
};

export type RestFournisseur = RestOf<IFournisseur>;

export type NewRestFournisseur = RestOf<NewFournisseur>;

export type PartialUpdateRestFournisseur = RestOf<PartialUpdateFournisseur>;

export type EntityResponseType = HttpResponse<IFournisseur>;
export type EntityArrayResponseType = HttpResponse<IFournisseur[]>;

@Injectable({ providedIn: 'root' })
export class FournisseurService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fournisseurs', 'claims');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fournisseurs/_search', 'claims');

  create(fournisseur: NewFournisseur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fournisseur);
    return this.http
      .post<RestFournisseur>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fournisseur: IFournisseur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fournisseur);
    return this.http
      .put<RestFournisseur>(`${this.resourceUrl}/${this.getFournisseurIdentifier(fournisseur)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fournisseur: PartialUpdateFournisseur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fournisseur);
    return this.http
      .patch<RestFournisseur>(`${this.resourceUrl}/${this.getFournisseurIdentifier(fournisseur)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFournisseur>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFournisseur[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestFournisseur[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IFournisseur[]>()], asapScheduler)),
    );
  }

  getFournisseurIdentifier(fournisseur: Pick<IFournisseur, 'id'>): number {
    return fournisseur.id;
  }

  compareFournisseur(o1: Pick<IFournisseur, 'id'> | null, o2: Pick<IFournisseur, 'id'> | null): boolean {
    return o1 && o2 ? this.getFournisseurIdentifier(o1) === this.getFournisseurIdentifier(o2) : o1 === o2;
  }

  addFournisseurToCollectionIfMissing<Type extends Pick<IFournisseur, 'id'>>(
    fournisseurCollection: Type[],
    ...fournisseursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fournisseurs: Type[] = fournisseursToCheck.filter(isPresent);
    if (fournisseurs.length > 0) {
      const fournisseurCollectionIdentifiers = fournisseurCollection.map(fournisseurItem => this.getFournisseurIdentifier(fournisseurItem));
      const fournisseursToAdd = fournisseurs.filter(fournisseurItem => {
        const fournisseurIdentifier = this.getFournisseurIdentifier(fournisseurItem);
        if (fournisseurCollectionIdentifiers.includes(fournisseurIdentifier)) {
          return false;
        }
        fournisseurCollectionIdentifiers.push(fournisseurIdentifier);
        return true;
      });
      return [...fournisseursToAdd, ...fournisseurCollection];
    }
    return fournisseurCollection;
  }

  protected convertDateFromClient<T extends IFournisseur | NewFournisseur | PartialUpdateFournisseur>(fournisseur: T): RestOf<T> {
    return {
      ...fournisseur,
      dateDebutContrat: fournisseur.dateDebutContrat?.toJSON() ?? null,
      dateFinContrat: fournisseur.dateFinContrat?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFournisseur: RestFournisseur): IFournisseur {
    return {
      ...restFournisseur,
      dateDebutContrat: restFournisseur.dateDebutContrat ? dayjs(restFournisseur.dateDebutContrat) : undefined,
      dateFinContrat: restFournisseur.dateFinContrat ? dayjs(restFournisseur.dateFinContrat) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFournisseur>): HttpResponse<IFournisseur> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFournisseur[]>): HttpResponse<IFournisseur[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
