import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISinistre } from '../sinistre.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sinistre.test-samples';

import { SinistreService, RestSinistre } from './sinistre.service';

const requireRestSample: RestSinistre = {
  ...sampleWithRequiredData,
  dateDeclaration: sampleWithRequiredData.dateDeclaration?.toJSON(),
  dateTraitement: sampleWithRequiredData.dateTraitement?.toJSON(),
};

describe('Sinistre Service', () => {
  let service: SinistreService;
  let httpMock: HttpTestingController;
  let expectedResult: ISinistre | ISinistre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SinistreService);
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

    it('should create a Sinistre', () => {
      const sinistre = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sinistre).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sinistre', () => {
      const sinistre = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sinistre).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sinistre', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sinistre', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Sinistre', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Sinistre', () => {
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

    describe('addSinistreToCollectionIfMissing', () => {
      it('should add a Sinistre to an empty array', () => {
        const sinistre: ISinistre = sampleWithRequiredData;
        expectedResult = service.addSinistreToCollectionIfMissing([], sinistre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sinistre);
      });

      it('should not add a Sinistre to an array that contains it', () => {
        const sinistre: ISinistre = sampleWithRequiredData;
        const sinistreCollection: ISinistre[] = [
          {
            ...sinistre,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSinistreToCollectionIfMissing(sinistreCollection, sinistre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sinistre to an array that doesn't contain it", () => {
        const sinistre: ISinistre = sampleWithRequiredData;
        const sinistreCollection: ISinistre[] = [sampleWithPartialData];
        expectedResult = service.addSinistreToCollectionIfMissing(sinistreCollection, sinistre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sinistre);
      });

      it('should add only unique Sinistre to an array', () => {
        const sinistreArray: ISinistre[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sinistreCollection: ISinistre[] = [sampleWithRequiredData];
        expectedResult = service.addSinistreToCollectionIfMissing(sinistreCollection, ...sinistreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sinistre: ISinistre = sampleWithRequiredData;
        const sinistre2: ISinistre = sampleWithPartialData;
        expectedResult = service.addSinistreToCollectionIfMissing([], sinistre, sinistre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sinistre);
        expect(expectedResult).toContain(sinistre2);
      });

      it('should accept null and undefined values', () => {
        const sinistre: ISinistre = sampleWithRequiredData;
        expectedResult = service.addSinistreToCollectionIfMissing([], null, sinistre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sinistre);
      });

      it('should return initial array if no Sinistre is added', () => {
        const sinistreCollection: ISinistre[] = [sampleWithRequiredData];
        expectedResult = service.addSinistreToCollectionIfMissing(sinistreCollection, undefined, null);
        expect(expectedResult).toEqual(sinistreCollection);
      });
    });

    describe('compareSinistre', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSinistre(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSinistre(entity1, entity2);
        const compareResult2 = service.compareSinistre(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSinistre(entity1, entity2);
        const compareResult2 = service.compareSinistre(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSinistre(entity1, entity2);
        const compareResult2 = service.compareSinistre(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
