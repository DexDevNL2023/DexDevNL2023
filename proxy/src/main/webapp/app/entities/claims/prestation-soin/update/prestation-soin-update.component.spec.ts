import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IFinanceurSoin } from 'app/entities/claims/financeur-soin/financeur-soin.model';
import { FinanceurSoinService } from 'app/entities/claims/financeur-soin/service/financeur-soin.service';
import { PrestationSoinService } from '../service/prestation-soin.service';
import { IPrestationSoin } from '../prestation-soin.model';
import { PrestationSoinFormService } from './prestation-soin-form.service';

import { PrestationSoinUpdateComponent } from './prestation-soin-update.component';

describe('PrestationSoin Management Update Component', () => {
  let comp: PrestationSoinUpdateComponent;
  let fixture: ComponentFixture<PrestationSoinUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prestationSoinFormService: PrestationSoinFormService;
  let prestationSoinService: PrestationSoinService;
  let financeurSoinService: FinanceurSoinService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PrestationSoinUpdateComponent],
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
      .overrideTemplate(PrestationSoinUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrestationSoinUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prestationSoinFormService = TestBed.inject(PrestationSoinFormService);
    prestationSoinService = TestBed.inject(PrestationSoinService);
    financeurSoinService = TestBed.inject(FinanceurSoinService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FinanceurSoin query and add missing value', () => {
      const prestationSoin: IPrestationSoin = { id: 456 };
      const financeurSoin: IFinanceurSoin = { id: 20874 };
      prestationSoin.financeurSoin = financeurSoin;
      const financeurSoins: IFinanceurSoin[] = [{ id: 15645 }];
      prestationSoin.financeurSoins = financeurSoins;

      const financeurSoinCollection: IFinanceurSoin[] = [{ id: 7632 }];
      jest.spyOn(financeurSoinService, 'query').mockReturnValue(of(new HttpResponse({ body: financeurSoinCollection })));
      const additionalFinanceurSoins = [financeurSoin, ...financeurSoins];
      const expectedCollection: IFinanceurSoin[] = [...additionalFinanceurSoins, ...financeurSoinCollection];
      jest.spyOn(financeurSoinService, 'addFinanceurSoinToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prestationSoin });
      comp.ngOnInit();

      expect(financeurSoinService.query).toHaveBeenCalled();
      expect(financeurSoinService.addFinanceurSoinToCollectionIfMissing).toHaveBeenCalledWith(
        financeurSoinCollection,
        ...additionalFinanceurSoins.map(expect.objectContaining),
      );
      expect(comp.financeurSoinsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prestationSoin: IPrestationSoin = { id: 456 };
      const financeurSoin: IFinanceurSoin = { id: 26308 };
      prestationSoin.financeurSoin = financeurSoin;
      const financeurSoin: IFinanceurSoin = { id: 6179 };
      prestationSoin.financeurSoins = [financeurSoin];

      activatedRoute.data = of({ prestationSoin });
      comp.ngOnInit();

      expect(comp.financeurSoinsSharedCollection).toContain(financeurSoin);
      expect(comp.financeurSoinsSharedCollection).toContain(financeurSoin);
      expect(comp.prestationSoin).toEqual(prestationSoin);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrestationSoin>>();
      const prestationSoin = { id: 123 };
      jest.spyOn(prestationSoinFormService, 'getPrestationSoin').mockReturnValue(prestationSoin);
      jest.spyOn(prestationSoinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestationSoin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prestationSoin }));
      saveSubject.complete();

      // THEN
      expect(prestationSoinFormService.getPrestationSoin).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(prestationSoinService.update).toHaveBeenCalledWith(expect.objectContaining(prestationSoin));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrestationSoin>>();
      const prestationSoin = { id: 123 };
      jest.spyOn(prestationSoinFormService, 'getPrestationSoin').mockReturnValue({ id: null });
      jest.spyOn(prestationSoinService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestationSoin: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prestationSoin }));
      saveSubject.complete();

      // THEN
      expect(prestationSoinFormService.getPrestationSoin).toHaveBeenCalled();
      expect(prestationSoinService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrestationSoin>>();
      const prestationSoin = { id: 123 };
      jest.spyOn(prestationSoinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestationSoin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prestationSoinService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFinanceurSoin', () => {
      it('Should forward to financeurSoinService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(financeurSoinService, 'compareFinanceurSoin');
        comp.compareFinanceurSoin(entity, entity2);
        expect(financeurSoinService.compareFinanceurSoin).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
