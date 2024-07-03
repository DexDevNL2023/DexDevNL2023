import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IPrestationSoin } from 'app/entities/claims/prestation-soin/prestation-soin.model';
import { PrestationSoinService } from 'app/entities/claims/prestation-soin/service/prestation-soin.service';
import { FinanceurSoinService } from '../service/financeur-soin.service';
import { IFinanceurSoin } from '../financeur-soin.model';
import { FinanceurSoinFormService } from './financeur-soin-form.service';

import { FinanceurSoinUpdateComponent } from './financeur-soin-update.component';

describe('FinanceurSoin Management Update Component', () => {
  let comp: FinanceurSoinUpdateComponent;
  let fixture: ComponentFixture<FinanceurSoinUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let financeurSoinFormService: FinanceurSoinFormService;
  let financeurSoinService: FinanceurSoinService;
  let prestationSoinService: PrestationSoinService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FinanceurSoinUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FinanceurSoinUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FinanceurSoinUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    financeurSoinFormService = TestBed.inject(FinanceurSoinFormService);
    financeurSoinService = TestBed.inject(FinanceurSoinService);
    prestationSoinService = TestBed.inject(PrestationSoinService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PrestationSoin query and add missing value', () => {
      const financeurSoin: IFinanceurSoin = { id: 456 };
      const prestationSoins: IPrestationSoin[] = [{ id: 5291 }];
      financeurSoin.prestationSoins = prestationSoins;

      const prestationSoinCollection: IPrestationSoin[] = [{ id: 2178 }];
      jest.spyOn(prestationSoinService, 'query').mockReturnValue(of(new HttpResponse({ body: prestationSoinCollection })));
      const additionalPrestationSoins = [...prestationSoins];
      const expectedCollection: IPrestationSoin[] = [...additionalPrestationSoins, ...prestationSoinCollection];
      jest.spyOn(prestationSoinService, 'addPrestationSoinToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ financeurSoin });
      comp.ngOnInit();

      expect(prestationSoinService.query).toHaveBeenCalled();
      expect(prestationSoinService.addPrestationSoinToCollectionIfMissing).toHaveBeenCalledWith(
        prestationSoinCollection,
        ...additionalPrestationSoins.map(expect.objectContaining),
      );
      expect(comp.prestationSoinsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const financeurSoin: IFinanceurSoin = { id: 456 };
      const prestationSoin: IPrestationSoin = { id: 1269 };
      financeurSoin.prestationSoins = [prestationSoin];

      activatedRoute.data = of({ financeurSoin });
      comp.ngOnInit();

      expect(comp.prestationSoinsSharedCollection).toContain(prestationSoin);
      expect(comp.financeurSoin).toEqual(financeurSoin);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinanceurSoin>>();
      const financeurSoin = { id: 123 };
      jest.spyOn(financeurSoinFormService, 'getFinanceurSoin').mockReturnValue(financeurSoin);
      jest.spyOn(financeurSoinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financeurSoin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financeurSoin }));
      saveSubject.complete();

      // THEN
      expect(financeurSoinFormService.getFinanceurSoin).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(financeurSoinService.update).toHaveBeenCalledWith(expect.objectContaining(financeurSoin));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinanceurSoin>>();
      const financeurSoin = { id: 123 };
      jest.spyOn(financeurSoinFormService, 'getFinanceurSoin').mockReturnValue({ id: null });
      jest.spyOn(financeurSoinService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financeurSoin: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financeurSoin }));
      saveSubject.complete();

      // THEN
      expect(financeurSoinFormService.getFinanceurSoin).toHaveBeenCalled();
      expect(financeurSoinService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinanceurSoin>>();
      const financeurSoin = { id: 123 };
      jest.spyOn(financeurSoinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ financeurSoin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(financeurSoinService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePrestationSoin', () => {
      it('Should forward to prestationSoinService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(prestationSoinService, 'comparePrestationSoin');
        comp.comparePrestationSoin(entity, entity2);
        expect(prestationSoinService.comparePrestationSoin).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
