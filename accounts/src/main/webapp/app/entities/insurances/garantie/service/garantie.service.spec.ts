import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IGarantie } from '../garantie.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../garantie.test-samples';

import { GarantieService, RestGarantie } from './garantie.service';

const requireRestSample: RestGarantie = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.toJSON(),
  dateFin: sampleWithRequiredData.dateFin?.toJSON(),
};

describe('Garantie Service', () => {
  let service: GarantieService;
  let httpMock: HttpTestingController;
  let expectedResult: IGarantie | IGarantie[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(GarantieService);
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

    it('should create a Garantie', () => {
      const garantie = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(garantie).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Garantie', () => {
      const garantie = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(garantie).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Garantie', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Garantie', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Garantie', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Garantie', () => {
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

    describe('addGarantieToCollectionIfMissing', () => {
      it('should add a Garantie to an empty array', () => {
        const garantie: IGarantie = sampleWithRequiredData;
        expectedResult = service.addGarantieToCollectionIfMissing([], garantie);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(garantie);
      });

      it('should not add a Garantie to an array that contains it', () => {
        const garantie: IGarantie = sampleWithRequiredData;
        const garantieCollection: IGarantie[] = [
          {
            ...garantie,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGarantieToCollectionIfMissing(garantieCollection, garantie);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Garantie to an array that doesn't contain it", () => {
        const garantie: IGarantie = sampleWithRequiredData;
        const garantieCollection: IGarantie[] = [sampleWithPartialData];
        expectedResult = service.addGarantieToCollectionIfMissing(garantieCollection, garantie);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(garantie);
      });

      it('should add only unique Garantie to an array', () => {
        const garantieArray: IGarantie[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const garantieCollection: IGarantie[] = [sampleWithRequiredData];
        expectedResult = service.addGarantieToCollectionIfMissing(garantieCollection, ...garantieArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const garantie: IGarantie = sampleWithRequiredData;
        const garantie2: IGarantie = sampleWithPartialData;
        expectedResult = service.addGarantieToCollectionIfMissing([], garantie, garantie2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(garantie);
        expect(expectedResult).toContain(garantie2);
      });

      it('should accept null and undefined values', () => {
        const garantie: IGarantie = sampleWithRequiredData;
        expectedResult = service.addGarantieToCollectionIfMissing([], null, garantie, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(garantie);
      });

      it('should return initial array if no Garantie is added', () => {
        const garantieCollection: IGarantie[] = [sampleWithRequiredData];
        expectedResult = service.addGarantieToCollectionIfMissing(garantieCollection, undefined, null);
        expect(expectedResult).toEqual(garantieCollection);
      });
    });

    describe('compareGarantie', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGarantie(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGarantie(entity1, entity2);
        const compareResult2 = service.compareGarantie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGarantie(entity1, entity2);
        const compareResult2 = service.compareGarantie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGarantie(entity1, entity2);
        const compareResult2 = service.compareGarantie(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
