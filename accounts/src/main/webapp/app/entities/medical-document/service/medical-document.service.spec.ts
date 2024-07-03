import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMedicalDocument } from '../medical-document.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../medical-document.test-samples';

import { MedicalDocumentService } from './medical-document.service';

const requireRestSample: IMedicalDocument = {
  ...sampleWithRequiredData,
};

describe('MedicalDocument Service', () => {
  let service: MedicalDocumentService;
  let httpMock: HttpTestingController;
  let expectedResult: IMedicalDocument | IMedicalDocument[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MedicalDocumentService);
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

    it('should create a MedicalDocument', () => {
      const medicalDocument = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(medicalDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MedicalDocument', () => {
      const medicalDocument = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(medicalDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MedicalDocument', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MedicalDocument', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MedicalDocument', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMedicalDocumentToCollectionIfMissing', () => {
      it('should add a MedicalDocument to an empty array', () => {
        const medicalDocument: IMedicalDocument = sampleWithRequiredData;
        expectedResult = service.addMedicalDocumentToCollectionIfMissing([], medicalDocument);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicalDocument);
      });

      it('should not add a MedicalDocument to an array that contains it', () => {
        const medicalDocument: IMedicalDocument = sampleWithRequiredData;
        const medicalDocumentCollection: IMedicalDocument[] = [
          {
            ...medicalDocument,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMedicalDocumentToCollectionIfMissing(medicalDocumentCollection, medicalDocument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MedicalDocument to an array that doesn't contain it", () => {
        const medicalDocument: IMedicalDocument = sampleWithRequiredData;
        const medicalDocumentCollection: IMedicalDocument[] = [sampleWithPartialData];
        expectedResult = service.addMedicalDocumentToCollectionIfMissing(medicalDocumentCollection, medicalDocument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicalDocument);
      });

      it('should add only unique MedicalDocument to an array', () => {
        const medicalDocumentArray: IMedicalDocument[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const medicalDocumentCollection: IMedicalDocument[] = [sampleWithRequiredData];
        expectedResult = service.addMedicalDocumentToCollectionIfMissing(medicalDocumentCollection, ...medicalDocumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const medicalDocument: IMedicalDocument = sampleWithRequiredData;
        const medicalDocument2: IMedicalDocument = sampleWithPartialData;
        expectedResult = service.addMedicalDocumentToCollectionIfMissing([], medicalDocument, medicalDocument2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicalDocument);
        expect(expectedResult).toContain(medicalDocument2);
      });

      it('should accept null and undefined values', () => {
        const medicalDocument: IMedicalDocument = sampleWithRequiredData;
        expectedResult = service.addMedicalDocumentToCollectionIfMissing([], null, medicalDocument, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicalDocument);
      });

      it('should return initial array if no MedicalDocument is added', () => {
        const medicalDocumentCollection: IMedicalDocument[] = [sampleWithRequiredData];
        expectedResult = service.addMedicalDocumentToCollectionIfMissing(medicalDocumentCollection, undefined, null);
        expect(expectedResult).toEqual(medicalDocumentCollection);
      });
    });

    describe('compareMedicalDocument', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMedicalDocument(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMedicalDocument(entity1, entity2);
        const compareResult2 = service.compareMedicalDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMedicalDocument(entity1, entity2);
        const compareResult2 = service.compareMedicalDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMedicalDocument(entity1, entity2);
        const compareResult2 = service.compareMedicalDocument(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
