import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAssurance } from '../assurance.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../assurance.test-samples';

import { AssuranceService, RestAssurance } from './assurance.service';

const requireRestSample: RestAssurance = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.toJSON(),
  dateFin: sampleWithRequiredData.dateFin?.toJSON(),
};

describe('Assurance Service', () => {
  let service: AssuranceService;
  let httpMock: HttpTestingController;
  let expectedResult: IAssurance | IAssurance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AssuranceService);
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

    it('should create a Assurance', () => {
      const assurance = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(assurance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Assurance', () => {
      const assurance = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(assurance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Assurance', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Assurance', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Assurance', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Assurance', () => {
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

    describe('addAssuranceToCollectionIfMissing', () => {
      it('should add a Assurance to an empty array', () => {
        const assurance: IAssurance = sampleWithRequiredData;
        expectedResult = service.addAssuranceToCollectionIfMissing([], assurance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assurance);
      });

      it('should not add a Assurance to an array that contains it', () => {
        const assurance: IAssurance = sampleWithRequiredData;
        const assuranceCollection: IAssurance[] = [
          {
            ...assurance,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAssuranceToCollectionIfMissing(assuranceCollection, assurance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Assurance to an array that doesn't contain it", () => {
        const assurance: IAssurance = sampleWithRequiredData;
        const assuranceCollection: IAssurance[] = [sampleWithPartialData];
        expectedResult = service.addAssuranceToCollectionIfMissing(assuranceCollection, assurance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assurance);
      });

      it('should add only unique Assurance to an array', () => {
        const assuranceArray: IAssurance[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const assuranceCollection: IAssurance[] = [sampleWithRequiredData];
        expectedResult = service.addAssuranceToCollectionIfMissing(assuranceCollection, ...assuranceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assurance: IAssurance = sampleWithRequiredData;
        const assurance2: IAssurance = sampleWithPartialData;
        expectedResult = service.addAssuranceToCollectionIfMissing([], assurance, assurance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assurance);
        expect(expectedResult).toContain(assurance2);
      });

      it('should accept null and undefined values', () => {
        const assurance: IAssurance = sampleWithRequiredData;
        expectedResult = service.addAssuranceToCollectionIfMissing([], null, assurance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assurance);
      });

      it('should return initial array if no Assurance is added', () => {
        const assuranceCollection: IAssurance[] = [sampleWithRequiredData];
        expectedResult = service.addAssuranceToCollectionIfMissing(assuranceCollection, undefined, null);
        expect(expectedResult).toEqual(assuranceCollection);
      });
    });

    describe('compareAssurance', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAssurance(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAssurance(entity1, entity2);
        const compareResult2 = service.compareAssurance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAssurance(entity1, entity2);
        const compareResult2 = service.compareAssurance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAssurance(entity1, entity2);
        const compareResult2 = service.compareAssurance(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
