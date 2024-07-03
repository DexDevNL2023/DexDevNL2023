import { TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IFinanceurSoin } from '../financeur-soin.model';
import { FinanceurSoinService } from '../service/financeur-soin.service';

import financeurSoinResolve from './financeur-soin-routing-resolve.service';

describe('FinanceurSoin routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: FinanceurSoinService;
  let resultFinanceurSoin: IFinanceurSoin | null | undefined;

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
    service = TestBed.inject(FinanceurSoinService);
    resultFinanceurSoin = undefined;
  });

  describe('resolve', () => {
    it('should return IFinanceurSoin returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        financeurSoinResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultFinanceurSoin = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultFinanceurSoin).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        financeurSoinResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultFinanceurSoin = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFinanceurSoin).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IFinanceurSoin>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        financeurSoinResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultFinanceurSoin = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultFinanceurSoin).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
