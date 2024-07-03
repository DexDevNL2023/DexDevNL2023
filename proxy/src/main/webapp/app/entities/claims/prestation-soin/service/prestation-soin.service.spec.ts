import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPrestationSoin } from '../prestation-soin.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../prestation-soin.test-samples';

import { PrestationSoinService } from './prestation-soin.service';

const requireRestSample: IPrestationSoin = {
  ...sampleWithRequiredData,
};

describe('PrestationSoin Service', () => {
  let service: PrestationSoinService;
  let httpMock: HttpTestingController;
  let expectedResult: IPrestationSoin | IPrestationSoin[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PrestationSoinService);
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

    it('should create a PrestationSoin', () => {
      const prestationSoin = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(prestationSoin).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PrestationSoin', () => {
      const prestationSoin = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(prestationSoin).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PrestationSoin', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PrestationSoin', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PrestationSoin', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a PrestationSoin', () => {
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

    describe('addPrestationSoinToCollectionIfMissing', () => {
      it('should add a PrestationSoin to an empty array', () => {
        const prestationSoin: IPrestationSoin = sampleWithRequiredData;
        expectedResult = service.addPrestationSoinToCollectionIfMissing([], prestationSoin);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prestationSoin);
      });

      it('should not add a PrestationSoin to an array that contains it', () => {
        const prestationSoin: IPrestationSoin = sampleWithRequiredData;
        const prestationSoinCollection: IPrestationSoin[] = [
          {
            ...prestationSoin,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPrestationSoinToCollectionIfMissing(prestationSoinCollection, prestationSoin);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrestationSoin to an array that doesn't contain it", () => {
        const prestationSoin: IPrestationSoin = sampleWithRequiredData;
        const prestationSoinCollection: IPrestationSoin[] = [sampleWithPartialData];
        expectedResult = service.addPrestationSoinToCollectionIfMissing(prestationSoinCollection, prestationSoin);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prestationSoin);
      });

      it('should add only unique PrestationSoin to an array', () => {
        const prestationSoinArray: IPrestationSoin[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const prestationSoinCollection: IPrestationSoin[] = [sampleWithRequiredData];
        expectedResult = service.addPrestationSoinToCollectionIfMissing(prestationSoinCollection, ...prestationSoinArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prestationSoin: IPrestationSoin = sampleWithRequiredData;
        const prestationSoin2: IPrestationSoin = sampleWithPartialData;
        expectedResult = service.addPrestationSoinToCollectionIfMissing([], prestationSoin, prestationSoin2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prestationSoin);
        expect(expectedResult).toContain(prestationSoin2);
      });

      it('should accept null and undefined values', () => {
        const prestationSoin: IPrestationSoin = sampleWithRequiredData;
        expectedResult = service.addPrestationSoinToCollectionIfMissing([], null, prestationSoin, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prestationSoin);
      });

      it('should return initial array if no PrestationSoin is added', () => {
        const prestationSoinCollection: IPrestationSoin[] = [sampleWithRequiredData];
        expectedResult = service.addPrestationSoinToCollectionIfMissing(prestationSoinCollection, undefined, null);
        expect(expectedResult).toEqual(prestationSoinCollection);
      });
    });

    describe('comparePrestationSoin', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePrestationSoin(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePrestationSoin(entity1, entity2);
        const compareResult2 = service.comparePrestationSoin(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePrestationSoin(entity1, entity2);
        const compareResult2 = service.comparePrestationSoin(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePrestationSoin(entity1, entity2);
        const compareResult2 = service.comparePrestationSoin(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
