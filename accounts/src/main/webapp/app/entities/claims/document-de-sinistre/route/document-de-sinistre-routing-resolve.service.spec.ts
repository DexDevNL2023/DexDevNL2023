import { TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IDocumentDeSinistre } from '../document-de-sinistre.model';
import { DocumentDeSinistreService } from '../service/document-de-sinistre.service';

import documentDeSinistreResolve from './document-de-sinistre-routing-resolve.service';

describe('DocumentDeSinistre routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: DocumentDeSinistreService;
  let resultDocumentDeSinistre: IDocumentDeSinistre | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(DocumentDeSinistreService);
    resultDocumentDeSinistre = undefined;
  });

  describe('resolve', () => {
    it('should return IDocumentDeSinistre returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        documentDeSinistreResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultDocumentDeSinistre = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultDocumentDeSinistre).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        documentDeSinistreResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultDocumentDeSinistre = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDocumentDeSinistre).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IDocumentDeSinistre>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        documentDeSinistreResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultDocumentDeSinistre = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultDocumentDeSinistre).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
