import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDocumentDeSinistre } from '../document-de-sinistre.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../document-de-sinistre.test-samples';

import { DocumentDeSinistreService, RestDocumentDeSinistre } from './document-de-sinistre.service';

const requireRestSample: RestDocumentDeSinistre = {
  ...sampleWithRequiredData,
  dateTelechargement: sampleWithRequiredData.dateTelechargement?.toJSON(),
};

describe('DocumentDeSinistre Service', () => {
  let service: DocumentDeSinistreService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentDeSinistre | IDocumentDeSinistre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentDeSinistreService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a DocumentDeSinistre', () => {
      const documentDeSinistre = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentDeSinistre).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentDeSinistre', () => {
      const documentDeSinistre = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentDeSinistre).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentDeSinistre', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentDeSinistre', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentDeSinistre', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a DocumentDeSinistre', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addDocumentDeSinistreToCollectionIfMissing', () => {
      it('should add a DocumentDeSinistre to an empty array', () => {
        const documentDeSinistre: IDocumentDeSinistre = sampleWithRequiredData;
        expectedResult = service.addDocumentDeSinistreToCollectionIfMissing([], documentDeSinistre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentDeSinistre);
      });

      it('should not add a DocumentDeSinistre to an array that contains it', () => {
        const documentDeSinistre: IDocumentDeSinistre = sampleWithRequiredData;
        const documentDeSinistreCollection: IDocumentDeSinistre[] = [
          {
            ...documentDeSinistre,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentDeSinistreToCollectionIfMissing(documentDeSinistreCollection, documentDeSinistre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentDeSinistre to an array that doesn't contain it", () => {
        const documentDeSinistre: IDocumentDeSinistre = sampleWithRequiredData;
        const documentDeSinistreCollection: IDocumentDeSinistre[] = [sampleWithPartialData];
        expectedResult = service.addDocumentDeSinistreToCollectionIfMissing(documentDeSinistreCollection, documentDeSinistre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentDeSinistre);
      });

      it('should add only unique DocumentDeSinistre to an array', () => {
        const documentDeSinistreArray: IDocumentDeSinistre[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentDeSinistreCollection: IDocumentDeSinistre[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentDeSinistreToCollectionIfMissing(documentDeSinistreCollection, ...documentDeSinistreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentDeSinistre: IDocumentDeSinistre = sampleWithRequiredData;
        const documentDeSinistre2: IDocumentDeSinistre = sampleWithPartialData;
        expectedResult = service.addDocumentDeSinistreToCollectionIfMissing([], documentDeSinistre, documentDeSinistre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentDeSinistre);
        expect(expectedResult).toContain(documentDeSinistre2);
      });

      it('should accept null and undefined values', () => {
        const documentDeSinistre: IDocumentDeSinistre = sampleWithRequiredData;
        expectedResult = service.addDocumentDeSinistreToCollectionIfMissing([], null, documentDeSinistre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentDeSinistre);
      });

      it('should return initial array if no DocumentDeSinistre is added', () => {
        const documentDeSinistreCollection: IDocumentDeSinistre[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentDeSinistreToCollectionIfMissing(documentDeSinistreCollection, undefined, null);
        expect(expectedResult).toEqual(documentDeSinistreCollection);
      });
    });

    describe('compareDocumentDeSinistre', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentDeSinistre(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentDeSinistre(entity1, entity2);
        const compareResult2 = service.compareDocumentDeSinistre(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDocumentDeSinistre(entity1, entity2);
        const compareResult2 = service.compareDocumentDeSinistre(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDocumentDeSinistre(entity1, entity2);
        const compareResult2 = service.compareDocumentDeSinistre(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
