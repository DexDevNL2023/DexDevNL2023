import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDocumentDeSinistre, NewDocumentDeSinistre } from '../document-de-sinistre.model';

export type PartialUpdateDocumentDeSinistre = Partial<IDocumentDeSinistre> & Pick<IDocumentDeSinistre, 'id'>;

type RestOf<T extends IDocumentDeSinistre | NewDocumentDeSinistre> = Omit<T, 'dateTelechargement'> & {
  dateTelechargement?: string | null;
};

export type RestDocumentDeSinistre = RestOf<IDocumentDeSinistre>;

export type NewRestDocumentDeSinistre = RestOf<NewDocumentDeSinistre>;

export type PartialUpdateRestDocumentDeSinistre = RestOf<PartialUpdateDocumentDeSinistre>;

export type EntityResponseType = HttpResponse<IDocumentDeSinistre>;
export type EntityArrayResponseType = HttpResponse<IDocumentDeSinistre[]>;

@Injectable({ providedIn: 'root' })
export class DocumentDeSinistreService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-de-sinistres', 'claims');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/document-de-sinistres/_search', 'claims');

  create(documentDeSinistre: NewDocumentDeSinistre): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentDeSinistre);
    return this.http
      .post<RestDocumentDeSinistre>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentDeSinistre: IDocumentDeSinistre): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentDeSinistre);
    return this.http
      .put<RestDocumentDeSinistre>(`${this.resourceUrl}/${this.getDocumentDeSinistreIdentifier(documentDeSinistre)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentDeSinistre: PartialUpdateDocumentDeSinistre): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentDeSinistre);
    return this.http
      .patch<RestDocumentDeSinistre>(`${this.resourceUrl}/${this.getDocumentDeSinistreIdentifier(documentDeSinistre)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentDeSinistre>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentDeSinistre[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestDocumentDeSinistre[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IDocumentDeSinistre[]>()], asapScheduler)),
    );
  }

  getDocumentDeSinistreIdentifier(documentDeSinistre: Pick<IDocumentDeSinistre, 'id'>): number {
    return documentDeSinistre.id;
  }

  compareDocumentDeSinistre(o1: Pick<IDocumentDeSinistre, 'id'> | null, o2: Pick<IDocumentDeSinistre, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentDeSinistreIdentifier(o1) === this.getDocumentDeSinistreIdentifier(o2) : o1 === o2;
  }

  addDocumentDeSinistreToCollectionIfMissing<Type extends Pick<IDocumentDeSinistre, 'id'>>(
    documentDeSinistreCollection: Type[],
    ...documentDeSinistresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentDeSinistres: Type[] = documentDeSinistresToCheck.filter(isPresent);
    if (documentDeSinistres.length > 0) {
      const documentDeSinistreCollectionIdentifiers = documentDeSinistreCollection.map(documentDeSinistreItem =>
        this.getDocumentDeSinistreIdentifier(documentDeSinistreItem),
      );
      const documentDeSinistresToAdd = documentDeSinistres.filter(documentDeSinistreItem => {
        const documentDeSinistreIdentifier = this.getDocumentDeSinistreIdentifier(documentDeSinistreItem);
        if (documentDeSinistreCollectionIdentifiers.includes(documentDeSinistreIdentifier)) {
          return false;
        }
        documentDeSinistreCollectionIdentifiers.push(documentDeSinistreIdentifier);
        return true;
      });
      return [...documentDeSinistresToAdd, ...documentDeSinistreCollection];
    }
    return documentDeSinistreCollection;
  }

  protected convertDateFromClient<T extends IDocumentDeSinistre | NewDocumentDeSinistre | PartialUpdateDocumentDeSinistre>(
    documentDeSinistre: T,
  ): RestOf<T> {
    return {
      ...documentDeSinistre,
      dateTelechargement: documentDeSinistre.dateTelechargement?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentDeSinistre: RestDocumentDeSinistre): IDocumentDeSinistre {
    return {
      ...restDocumentDeSinistre,
      dateTelechargement: restDocumentDeSinistre.dateTelechargement ? dayjs(restDocumentDeSinistre.dateTelechargement) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentDeSinistre>): HttpResponse<IDocumentDeSinistre> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentDeSinistre[]>): HttpResponse<IDocumentDeSinistre[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
