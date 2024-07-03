import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IGarantie } from 'app/entities/insurances/garantie/garantie.model';
import { GarantieService } from 'app/entities/insurances/garantie/service/garantie.service';
import { AssuranceService } from '../service/assurance.service';
import { IAssurance } from '../assurance.model';
import { AssuranceFormService } from './assurance-form.service';

import { AssuranceUpdateComponent } from './assurance-update.component';

describe('Assurance Management Update Component', () => {
  let comp: AssuranceUpdateComponent;
  let fixture: ComponentFixture<AssuranceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assuranceFormService: AssuranceFormService;
  let assuranceService: AssuranceService;
  let garantieService: GarantieService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AssuranceUpdateComponent],
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
      .overrideTemplate(AssuranceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssuranceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assuranceFormService = TestBed.inject(AssuranceFormService);
    assuranceService = TestBed.inject(AssuranceService);
    garantieService = TestBed.inject(GarantieService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Garantie query and add missing value', () => {
      const assurance: IAssurance = { id: 456 };
      const garanties: IGarantie[] = [{ id: 7679 }];
      assurance.garanties = garanties;

      const garantieCollection: IGarantie[] = [{ id: 49 }];
      jest.spyOn(garantieService, 'query').mockReturnValue(of(new HttpResponse({ body: garantieCollection })));
      const additionalGaranties = [...garanties];
      const expectedCollection: IGarantie[] = [...additionalGaranties, ...garantieCollection];
      jest.spyOn(garantieService, 'addGarantieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assurance });
      comp.ngOnInit();

      expect(garantieService.query).toHaveBeenCalled();
      expect(garantieService.addGarantieToCollectionIfMissing).toHaveBeenCalledWith(
        garantieCollection,
        ...additionalGaranties.map(expect.objectContaining),
      );
      expect(comp.garantiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assurance: IAssurance = { id: 456 };
      const garantie: IGarantie = { id: 18104 };
      assurance.garanties = [garantie];

      activatedRoute.data = of({ assurance });
      comp.ngOnInit();

      expect(comp.garantiesSharedCollection).toContain(garantie);
      expect(comp.assurance).toEqual(assurance);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssurance>>();
      const assurance = { id: 123 };
      jest.spyOn(assuranceFormService, 'getAssurance').mockReturnValue(assurance);
      jest.spyOn(assuranceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assurance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assurance }));
      saveSubject.complete();

      // THEN
      expect(assuranceFormService.getAssurance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(assuranceService.update).toHaveBeenCalledWith(expect.objectContaining(assurance));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssurance>>();
      const assurance = { id: 123 };
      jest.spyOn(assuranceFormService, 'getAssurance').mockReturnValue({ id: null });
      jest.spyOn(assuranceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assurance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assurance }));
      saveSubject.complete();

      // THEN
      expect(assuranceFormService.getAssurance).toHaveBeenCalled();
      expect(assuranceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssurance>>();
      const assurance = { id: 123 };
      jest.spyOn(assuranceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assurance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assuranceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGarantie', () => {
      it('Should forward to garantieService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(garantieService, 'compareGarantie');
        comp.compareGarantie(entity, entity2);
        expect(garantieService.compareGarantie).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
