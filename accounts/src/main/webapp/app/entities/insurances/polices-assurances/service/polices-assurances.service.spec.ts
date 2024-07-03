import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPolicesAssurances } from '../polices-assurances.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../polices-assurances.test-samples';

import { PolicesAssurancesService, RestPolicesAssurances } from './polices-assurances.service';

const requireRestSample: RestPolicesAssurances = {
  ...sampleWithRequiredData,
  dateEmission: sampleWithRequiredData.dateEmission?.toJSON(),
  dateExpiration: sampleWithRequiredData.dateExpiration?.toJSON(),
};

describe('PolicesAssurances Service', () => {
  let service: PolicesAssurancesService;
  let httpMock: HttpTestingController;
  let expectedResult: IPolicesAssurances | IPolicesAssurances[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PolicesAssurancesService);
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

    it('should create a PolicesAssurances', () => {
      const policesAssurances = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(policesAssurances).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PolicesAssurances', () => {
      const policesAssurances = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(policesAssurances).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PolicesAssurances', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PolicesAssurances', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PolicesAssurances', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a PolicesAssurances', () => {
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

    describe('addPolicesAssurancesToCollectionIfMissing', () => {
      it('should add a PolicesAssurances to an empty array', () => {
        const policesAssurances: IPolicesAssurances = sampleWithRequiredData;
        expectedResult = service.addPolicesAssurancesToCollectionIfMissing([], policesAssurances);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(policesAssurances);
      });

      it('should not add a PolicesAssurances to an array that contains it', () => {
        const policesAssurances: IPolicesAssurances = sampleWithRequiredData;
        const policesAssurancesCollection: IPolicesAssurances[] = [
          {
            ...policesAssurances,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPolicesAssurancesToCollectionIfMissing(policesAssurancesCollection, policesAssurances);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PolicesAssurances to an array that doesn't contain it", () => {
        const policesAssurances: IPolicesAssurances = sampleWithRequiredData;
        const policesAssurancesCollection: IPolicesAssurances[] = [sampleWithPartialData];
        expectedResult = service.addPolicesAssurancesToCollectionIfMissing(policesAssurancesCollection, policesAssurances);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(policesAssurances);
      });

      it('should add only unique PolicesAssurances to an array', () => {
        const policesAssurancesArray: IPolicesAssurances[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const policesAssurancesCollection: IPolicesAssurances[] = [sampleWithRequiredData];
        expectedResult = service.addPolicesAssurancesToCollectionIfMissing(policesAssurancesCollection, ...policesAssurancesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const policesAssurances: IPolicesAssurances = sampleWithRequiredData;
        const policesAssurances2: IPolicesAssurances = sampleWithPartialData;
        expectedResult = service.addPolicesAssurancesToCollectionIfMissing([], policesAssurances, policesAssurances2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(policesAssurances);
        expect(expectedResult).toContain(policesAssurances2);
      });

      it('should accept null and undefined values', () => {
        const policesAssurances: IPolicesAssurances = sampleWithRequiredData;
        expectedResult = service.addPolicesAssurancesToCollectionIfMissing([], null, policesAssurances, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(policesAssurances);
      });

      it('should return initial array if no PolicesAssurances is added', () => {
        const policesAssurancesCollection: IPolicesAssurances[] = [sampleWithRequiredData];
        expectedResult = service.addPolicesAssurancesToCollectionIfMissing(policesAssurancesCollection, undefined, null);
        expect(expectedResult).toEqual(policesAssurancesCollection);
      });
    });

    describe('comparePolicesAssurances', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePolicesAssurances(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePolicesAssurances(entity1, entity2);
        const compareResult2 = service.comparePolicesAssurances(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePolicesAssurances(entity1, entity2);
        const compareResult2 = service.comparePolicesAssurances(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePolicesAssurances(entity1, entity2);
        const compareResult2 = service.comparePolicesAssurances(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
