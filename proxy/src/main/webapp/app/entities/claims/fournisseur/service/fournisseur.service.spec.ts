import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFournisseur } from '../fournisseur.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../fournisseur.test-samples';

import { FournisseurService, RestFournisseur } from './fournisseur.service';

const requireRestSample: RestFournisseur = {
  ...sampleWithRequiredData,
  dateDebutContrat: sampleWithRequiredData.dateDebutContrat?.toJSON(),
  dateFinContrat: sampleWithRequiredData.dateFinContrat?.toJSON(),
};

describe('Fournisseur Service', () => {
  let service: FournisseurService;
  let httpMock: HttpTestingController;
  let expectedResult: IFournisseur | IFournisseur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FournisseurService);
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

    it('should create a Fournisseur', () => {
      const fournisseur = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fournisseur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Fournisseur', () => {
      const fournisseur = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fournisseur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Fournisseur', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Fournisseur', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Fournisseur', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Fournisseur', () => {
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

    describe('addFournisseurToCollectionIfMissing', () => {
      it('should add a Fournisseur to an empty array', () => {
        const fournisseur: IFournisseur = sampleWithRequiredData;
        expectedResult = service.addFournisseurToCollectionIfMissing([], fournisseur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fournisseur);
      });

      it('should not add a Fournisseur to an array that contains it', () => {
        const fournisseur: IFournisseur = sampleWithRequiredData;
        const fournisseurCollection: IFournisseur[] = [
          {
            ...fournisseur,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFournisseurToCollectionIfMissing(fournisseurCollection, fournisseur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Fournisseur to an array that doesn't contain it", () => {
        const fournisseur: IFournisseur = sampleWithRequiredData;
        const fournisseurCollection: IFournisseur[] = [sampleWithPartialData];
        expectedResult = service.addFournisseurToCollectionIfMissing(fournisseurCollection, fournisseur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fournisseur);
      });

      it('should add only unique Fournisseur to an array', () => {
        const fournisseurArray: IFournisseur[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fournisseurCollection: IFournisseur[] = [sampleWithRequiredData];
        expectedResult = service.addFournisseurToCollectionIfMissing(fournisseurCollection, ...fournisseurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fournisseur: IFournisseur = sampleWithRequiredData;
        const fournisseur2: IFournisseur = sampleWithPartialData;
        expectedResult = service.addFournisseurToCollectionIfMissing([], fournisseur, fournisseur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fournisseur);
        expect(expectedResult).toContain(fournisseur2);
      });

      it('should accept null and undefined values', () => {
        const fournisseur: IFournisseur = sampleWithRequiredData;
        expectedResult = service.addFournisseurToCollectionIfMissing([], null, fournisseur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fournisseur);
      });

      it('should return initial array if no Fournisseur is added', () => {
        const fournisseurCollection: IFournisseur[] = [sampleWithRequiredData];
        expectedResult = service.addFournisseurToCollectionIfMissing(fournisseurCollection, undefined, null);
        expect(expectedResult).toEqual(fournisseurCollection);
      });
    });

    describe('compareFournisseur', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFournisseur(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFournisseur(entity1, entity2);
        const compareResult2 = service.compareFournisseur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFournisseur(entity1, entity2);
        const compareResult2 = service.compareFournisseur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFournisseur(entity1, entity2);
        const compareResult2 = service.compareFournisseur(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
