import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMedicalRecord, NewMedicalRecord } from '../medical-record.model';

export type PartialUpdateMedicalRecord = Partial<IMedicalRecord> & Pick<IMedicalRecord, 'id'>;

type RestOf<T extends IMedicalRecord | NewMedicalRecord> = Omit<T, 'dateUpdated'> & {
  dateUpdated?: string | null;
};

export type RestMedicalRecord = RestOf<IMedicalRecord>;

export type NewRestMedicalRecord = RestOf<NewMedicalRecord>;

export type PartialUpdateRestMedicalRecord = RestOf<PartialUpdateMedicalRecord>;

export type EntityResponseType = HttpResponse<IMedicalRecord>;
export type EntityArrayResponseType = HttpResponse<IMedicalRecord[]>;

@Injectable({ providedIn: 'root' })
export class MedicalRecordService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/medical-records', 'accounts');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/medical-records/_search', 'accounts');

  create(medicalRecord: NewMedicalRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalRecord);
    return this.http
      .post<RestMedicalRecord>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(medicalRecord: IMedicalRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalRecord);
    return this.http
      .put<RestMedicalRecord>(`${this.resourceUrl}/${this.getMedicalRecordIdentifier(medicalRecord)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(medicalRecord: PartialUpdateMedicalRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalRecord);
    return this.http
      .patch<RestMedicalRecord>(`${this.resourceUrl}/${this.getMedicalRecordIdentifier(medicalRecord)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMedicalRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMedicalRecord[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMedicalRecord[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMedicalRecord[]>()], asapScheduler)),
    );
  }

  getMedicalRecordIdentifier(medicalRecord: Pick<IMedicalRecord, 'id'>): number {
    return medicalRecord.id;
  }

  compareMedicalRecord(o1: Pick<IMedicalRecord, 'id'> | null, o2: Pick<IMedicalRecord, 'id'> | null): boolean {
    return o1 && o2 ? this.getMedicalRecordIdentifier(o1) === this.getMedicalRecordIdentifier(o2) : o1 === o2;
  }

  addMedicalRecordToCollectionIfMissing<Type extends Pick<IMedicalRecord, 'id'>>(
    medicalRecordCollection: Type[],
    ...medicalRecordsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const medicalRecords: Type[] = medicalRecordsToCheck.filter(isPresent);
    if (medicalRecords.length > 0) {
      const medicalRecordCollectionIdentifiers = medicalRecordCollection.map(medicalRecordItem =>
        this.getMedicalRecordIdentifier(medicalRecordItem),
      );
      const medicalRecordsToAdd = medicalRecords.filter(medicalRecordItem => {
        const medicalRecordIdentifier = this.getMedicalRecordIdentifier(medicalRecordItem);
        if (medicalRecordCollectionIdentifiers.includes(medicalRecordIdentifier)) {
          return false;
        }
        medicalRecordCollectionIdentifiers.push(medicalRecordIdentifier);
        return true;
      });
      return [...medicalRecordsToAdd, ...medicalRecordCollection];
    }
    return medicalRecordCollection;
  }

  protected convertDateFromClient<T extends IMedicalRecord | NewMedicalRecord | PartialUpdateMedicalRecord>(medicalRecord: T): RestOf<T> {
    return {
      ...medicalRecord,
      dateUpdated: medicalRecord.dateUpdated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMedicalRecord: RestMedicalRecord): IMedicalRecord {
    return {
      ...restMedicalRecord,
      dateUpdated: restMedicalRecord.dateUpdated ? dayjs(restMedicalRecord.dateUpdated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMedicalRecord>): HttpResponse<IMedicalRecord> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMedicalRecord[]>): HttpResponse<IMedicalRecord[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
