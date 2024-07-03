import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IRapport } from '../rapport.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../rapport.test-samples';

import { RapportService, RestRapport } from './rapport.service';

const requireRestSample: RestRapport = {
  ...sampleWithRequiredData,
  dateGeneration: sampleWithRequiredData.dateGeneration?.toJSON(),
};

describe('Rapport Service', () => {
  let service: RapportService;
  let httpMock: HttpTestingController;
  let expectedResult: IRapport | IRapport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RapportService);
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

    it('should create a Rapport', () => {
      const rapport = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(rapport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Rapport', () => {
      const rapport = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(rapport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Rapport', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Rapport', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Rapport', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Rapport', () => {
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

    describe('addRapportToCollectionIfMissing', () => {
      it('should add a Rapport to an empty array', () => {
        const rapport: IRapport = sampleWithRequiredData;
        expectedResult = service.addRapportToCollectionIfMissing([], rapport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rapport);
      });

      it('should not add a Rapport to an array that contains it', () => {
        const rapport: IRapport = sampleWithRequiredData;
        const rapportCollection: IRapport[] = [
          {
            ...rapport,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRapportToCollectionIfMissing(rapportCollection, rapport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Rapport to an array that doesn't contain it", () => {
        const rapport: IRapport = sampleWithRequiredData;
        const rapportCollection: IRapport[] = [sampleWithPartialData];
        expectedResult = service.addRapportToCollectionIfMissing(rapportCollection, rapport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rapport);
      });

      it('should add only unique Rapport to an array', () => {
        const rapportArray: IRapport[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const rapportCollection: IRapport[] = [sampleWithRequiredData];
        expectedResult = service.addRapportToCollectionIfMissing(rapportCollection, ...rapportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rapport: IRapport = sampleWithRequiredData;
        const rapport2: IRapport = sampleWithPartialData;
        expectedResult = service.addRapportToCollectionIfMissing([], rapport, rapport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rapport);
        expect(expectedResult).toContain(rapport2);
      });

      it('should accept null and undefined values', () => {
        const rapport: IRapport = sampleWithRequiredData;
        expectedResult = service.addRapportToCollectionIfMissing([], null, rapport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rapport);
      });

      it('should return initial array if no Rapport is added', () => {
        const rapportCollection: IRapport[] = [sampleWithRequiredData];
        expectedResult = service.addRapportToCollectionIfMissing(rapportCollection, undefined, null);
        expect(expectedResult).toEqual(rapportCollection);
      });
    });

    describe('compareRapport', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRapport(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRapport(entity1, entity2);
        const compareResult2 = service.compareRapport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRapport(entity1, entity2);
        const compareResult2 = service.compareRapport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRapport(entity1, entity2);
        const compareResult2 = service.compareRapport(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
