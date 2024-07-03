import { TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IPolicesAssurances } from '../polices-assurances.model';
import { PolicesAssurancesService } from '../service/polices-assurances.service';

import policesAssurancesResolve from './polices-assurances-routing-resolve.service';

describe('PolicesAssurances routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: PolicesAssurancesService;
  let resultPolicesAssurances: IPolicesAssurances | null | undefined;

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
    service = TestBed.inject(PolicesAssurancesService);
    resultPolicesAssurances = undefined;
  });

  describe('resolve', () => {
    it('should return IPolicesAssurances returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        policesAssurancesResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPolicesAssurances = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultPolicesAssurances).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        policesAssurancesResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPolicesAssurances = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPolicesAssurances).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPolicesAssurances>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        policesAssurancesResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPolicesAssurances = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultPolicesAssurances).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
