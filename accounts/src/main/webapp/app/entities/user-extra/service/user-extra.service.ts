import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserExtra, NewUserExtra } from '../user-extra.model';

export type PartialUpdateUserExtra = Partial<IUserExtra> & Pick<IUserExtra, 'id'>;

type RestOf<T extends IUserExtra | NewUserExtra> = Omit<T, 'dateNaissance'> & {
  dateNaissance?: string | null;
};

export type RestUserExtra = RestOf<IUserExtra>;

export type NewRestUserExtra = RestOf<NewUserExtra>;

export type PartialUpdateRestUserExtra = RestOf<PartialUpdateUserExtra>;

export type EntityResponseType = HttpResponse<IUserExtra>;
export type EntityArrayResponseType = HttpResponse<IUserExtra[]>;

@Injectable({ providedIn: 'root' })
export class UserExtraService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-extras');

  create(userExtra: NewUserExtra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userExtra);
    return this.http
      .post<RestUserExtra>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userExtra: IUserExtra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userExtra);
    return this.http
      .put<RestUserExtra>(`${this.resourceUrl}/${this.getUserExtraIdentifier(userExtra)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userExtra: PartialUpdateUserExtra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userExtra);
    return this.http
      .patch<RestUserExtra>(`${this.resourceUrl}/${this.getUserExtraIdentifier(userExtra)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserExtra>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserExtra[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserExtraIdentifier(userExtra: Pick<IUserExtra, 'id'>): number {
    return userExtra.id;
  }

  compareUserExtra(o1: Pick<IUserExtra, 'id'> | null, o2: Pick<IUserExtra, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserExtraIdentifier(o1) === this.getUserExtraIdentifier(o2) : o1 === o2;
  }

  addUserExtraToCollectionIfMissing<Type extends Pick<IUserExtra, 'id'>>(
    userExtraCollection: Type[],
    ...userExtrasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userExtras: Type[] = userExtrasToCheck.filter(isPresent);
    if (userExtras.length > 0) {
      const userExtraCollectionIdentifiers = userExtraCollection.map(userExtraItem => this.getUserExtraIdentifier(userExtraItem));
      const userExtrasToAdd = userExtras.filter(userExtraItem => {
        const userExtraIdentifier = this.getUserExtraIdentifier(userExtraItem);
        if (userExtraCollectionIdentifiers.includes(userExtraIdentifier)) {
          return false;
        }
        userExtraCollectionIdentifiers.push(userExtraIdentifier);
        return true;
      });
      return [...userExtrasToAdd, ...userExtraCollection];
    }
    return userExtraCollection;
  }

  protected convertDateFromClient<T extends IUserExtra | NewUserExtra | PartialUpdateUserExtra>(userExtra: T): RestOf<T> {
    return {
      ...userExtra,
      dateNaissance: userExtra.dateNaissance?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUserExtra: RestUserExtra): IUserExtra {
    return {
      ...restUserExtra,
      dateNaissance: restUserExtra.dateNaissance ? dayjs(restUserExtra.dateNaissance) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserExtra>): HttpResponse<IUserExtra> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUserExtra[]>): HttpResponse<IUserExtra[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
