import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISouscription } from '../souscription.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../souscription.test-samples';

import { SouscriptionService, RestSouscription } from './souscription.service';

const requireRestSample: RestSouscription = {
  ...sampleWithRequiredData,
  dateSouscription: sampleWithRequiredData.dateSouscription?.toJSON(),
};

describe('Souscription Service', () => {
  let service: SouscriptionService;
  let httpMock: HttpTestingController;
  let expectedResult: ISouscription | ISouscription[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SouscriptionService);
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

    it('should create a Souscription', () => {
      const souscription = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(souscription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Souscription', () => {
      const souscription = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(souscription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Souscription', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Souscription', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Souscription', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Souscription', () => {
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

    describe('addSouscriptionToCollectionIfMissing', () => {
      it('should add a Souscription to an empty array', () => {
        const souscription: ISouscription = sampleWithRequiredData;
        expectedResult = service.addSouscriptionToCollectionIfMissing([], souscription);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(souscription);
      });

      it('should not add a Souscription to an array that contains it', () => {
        const souscription: ISouscription = sampleWithRequiredData;
        const souscriptionCollection: ISouscription[] = [
          {
            ...souscription,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSouscriptionToCollectionIfMissing(souscriptionCollection, souscription);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Souscription to an array that doesn't contain it", () => {
        const souscription: ISouscription = sampleWithRequiredData;
        const souscriptionCollection: ISouscription[] = [sampleWithPartialData];
        expectedResult = service.addSouscriptionToCollectionIfMissing(souscriptionCollection, souscription);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(souscription);
      });

      it('should add only unique Souscription to an array', () => {
        const souscriptionArray: ISouscription[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const souscriptionCollection: ISouscription[] = [sampleWithRequiredData];
        expectedResult = service.addSouscriptionToCollectionIfMissing(souscriptionCollection, ...souscriptionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const souscription: ISouscription = sampleWithRequiredData;
        const souscription2: ISouscription = sampleWithPartialData;
        expectedResult = service.addSouscriptionToCollectionIfMissing([], souscription, souscription2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(souscription);
        expect(expectedResult).toContain(souscription2);
      });

      it('should accept null and undefined values', () => {
        const souscription: ISouscription = sampleWithRequiredData;
        expectedResult = service.addSouscriptionToCollectionIfMissing([], null, souscription, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(souscription);
      });

      it('should return initial array if no Souscription is added', () => {
        const souscriptionCollection: ISouscription[] = [sampleWithRequiredData];
        expectedResult = service.addSouscriptionToCollectionIfMissing(souscriptionCollection, undefined, null);
        expect(expectedResult).toEqual(souscriptionCollection);
      });
    });

    describe('compareSouscription', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSouscription(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSouscription(entity1, entity2);
        const compareResult2 = service.compareSouscription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSouscription(entity1, entity2);
        const compareResult2 = service.compareSouscription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSouscription(entity1, entity2);
        const compareResult2 = service.compareSouscription(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
