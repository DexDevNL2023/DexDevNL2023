import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMedicalDocument, NewMedicalDocument } from '../medical-document.model';

export type PartialUpdateMedicalDocument = Partial<IMedicalDocument> & Pick<IMedicalDocument, 'id'>;

export type EntityResponseType = HttpResponse<IMedicalDocument>;
export type EntityArrayResponseType = HttpResponse<IMedicalDocument[]>;

@Injectable({ providedIn: 'root' })
export class MedicalDocumentService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/medical-documents', 'accounts');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/medical-documents/_search', 'accounts');

  create(medicalDocument: NewMedicalDocument): Observable<EntityResponseType> {
    return this.http.post<IMedicalDocument>(this.resourceUrl, medicalDocument, { observe: 'response' });
  }

  update(medicalDocument: IMedicalDocument): Observable<EntityResponseType> {
    return this.http.put<IMedicalDocument>(`${this.resourceUrl}/${this.getMedicalDocumentIdentifier(medicalDocument)}`, medicalDocument, {
      observe: 'response',
    });
  }

  partialUpdate(medicalDocument: PartialUpdateMedicalDocument): Observable<EntityResponseType> {
    return this.http.patch<IMedicalDocument>(`${this.resourceUrl}/${this.getMedicalDocumentIdentifier(medicalDocument)}`, medicalDocument, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMedicalDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedicalDocument[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMedicalDocument[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IMedicalDocument[]>()], asapScheduler)));
  }

  getMedicalDocumentIdentifier(medicalDocument: Pick<IMedicalDocument, 'id'>): number {
    return medicalDocument.id;
  }

  compareMedicalDocument(o1: Pick<IMedicalDocument, 'id'> | null, o2: Pick<IMedicalDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getMedicalDocumentIdentifier(o1) === this.getMedicalDocumentIdentifier(o2) : o1 === o2;
  }

  addMedicalDocumentToCollectionIfMissing<Type extends Pick<IMedicalDocument, 'id'>>(
    medicalDocumentCollection: Type[],
    ...medicalDocumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const medicalDocuments: Type[] = medicalDocumentsToCheck.filter(isPresent);
    if (medicalDocuments.length > 0) {
      const medicalDocumentCollectionIdentifiers = medicalDocumentCollection.map(medicalDocumentItem =>
        this.getMedicalDocumentIdentifier(medicalDocumentItem),
      );
      const medicalDocumentsToAdd = medicalDocuments.filter(medicalDocumentItem => {
        const medicalDocumentIdentifier = this.getMedicalDocumentIdentifier(medicalDocumentItem);
        if (medicalDocumentCollectionIdentifiers.includes(medicalDocumentIdentifier)) {
          return false;
        }
        medicalDocumentCollectionIdentifiers.push(medicalDocumentIdentifier);
        return true;
      });
      return [...medicalDocumentsToAdd, ...medicalDocumentCollection];
    }
    return medicalDocumentCollection;
  }
}
