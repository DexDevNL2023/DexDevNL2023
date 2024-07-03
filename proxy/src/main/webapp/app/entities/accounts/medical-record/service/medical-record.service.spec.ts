import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMedicalRecord } from '../medical-record.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../medical-record.test-samples';

import { MedicalRecordService, RestMedicalRecord } from './medical-record.service';

const requireRestSample: RestMedicalRecord = {
  ...sampleWithRequiredData,
  dateUpdated: sampleWithRequiredData.dateUpdated?.toJSON(),
};

describe('MedicalRecord Service', () => {
  let service: MedicalRecordService;
  let httpMock: HttpTestingController;
  let expectedResult: IMedicalRecord | IMedicalRecord[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MedicalRecordService);
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

    it('should create a MedicalRecord', () => {
      const medicalRecord = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(medicalRecord).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MedicalRecord', () => {
      const medicalRecord = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(medicalRecord).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MedicalRecord', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MedicalRecord', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MedicalRecord', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MedicalRecord', () => {
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

    describe('addMedicalRecordToCollectionIfMissing', () => {
      it('should add a MedicalRecord to an empty array', () => {
        const medicalRecord: IMedicalRecord = sampleWithRequiredData;
        expectedResult = service.addMedicalRecordToCollectionIfMissing([], medicalRecord);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicalRecord);
      });

      it('should not add a MedicalRecord to an array that contains it', () => {
        const medicalRecord: IMedicalRecord = sampleWithRequiredData;
        const medicalRecordCollection: IMedicalRecord[] = [
          {
            ...medicalRecord,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMedicalRecordToCollectionIfMissing(medicalRecordCollection, medicalRecord);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MedicalRecord to an array that doesn't contain it", () => {
        const medicalRecord: IMedicalRecord = sampleWithRequiredData;
        const medicalRecordCollection: IMedicalRecord[] = [sampleWithPartialData];
        expectedResult = service.addMedicalRecordToCollectionIfMissing(medicalRecordCollection, medicalRecord);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicalRecord);
      });

      it('should add only unique MedicalRecord to an array', () => {
        const medicalRecordArray: IMedicalRecord[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const medicalRecordCollection: IMedicalRecord[] = [sampleWithRequiredData];
        expectedResult = service.addMedicalRecordToCollectionIfMissing(medicalRecordCollection, ...medicalRecordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const medicalRecord: IMedicalRecord = sampleWithRequiredData;
        const medicalRecord2: IMedicalRecord = sampleWithPartialData;
        expectedResult = service.addMedicalRecordToCollectionIfMissing([], medicalRecord, medicalRecord2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicalRecord);
        expect(expectedResult).toContain(medicalRecord2);
      });

      it('should accept null and undefined values', () => {
        const medicalRecord: IMedicalRecord = sampleWithRequiredData;
        expectedResult = service.addMedicalRecordToCollectionIfMissing([], null, medicalRecord, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicalRecord);
      });

      it('should return initial array if no MedicalRecord is added', () => {
        const medicalRecordCollection: IMedicalRecord[] = [sampleWithRequiredData];
        expectedResult = service.addMedicalRecordToCollectionIfMissing(medicalRecordCollection, undefined, null);
        expect(expectedResult).toEqual(medicalRecordCollection);
      });
    });

    describe('compareMedicalRecord', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMedicalRecord(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMedicalRecord(entity1, entity2);
        const compareResult2 = service.compareMedicalRecord(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMedicalRecord(entity1, entity2);
        const compareResult2 = service.compareMedicalRecord(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMedicalRecord(entity1, entity2);
        const compareResult2 = service.compareMedicalRecord(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
