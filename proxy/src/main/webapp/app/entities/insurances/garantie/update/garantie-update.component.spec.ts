import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAssurance } from 'app/entities/insurances/assurance/assurance.model';
import { AssuranceService } from 'app/entities/insurances/assurance/service/assurance.service';
import { GarantieService } from '../service/garantie.service';
import { IGarantie } from '../garantie.model';
import { GarantieFormService } from './garantie-form.service';

import { GarantieUpdateComponent } from './garantie-update.component';

describe('Garantie Management Update Component', () => {
  let comp: GarantieUpdateComponent;
  let fixture: ComponentFixture<GarantieUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let garantieFormService: GarantieFormService;
  let garantieService: GarantieService;
  let assuranceService: AssuranceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [GarantieUpdateComponent],
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
      .overrideTemplate(GarantieUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GarantieUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    garantieFormService = TestBed.inject(GarantieFormService);
    garantieService = TestBed.inject(GarantieService);
    assuranceService = TestBed.inject(AssuranceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Assurance query and add missing value', () => {
      const garantie: IGarantie = { id: 456 };
      const assurances: IAssurance[] = [{ id: 29058 }];
      garantie.assurances = assurances;

      const assuranceCollection: IAssurance[] = [{ id: 21075 }];
      jest.spyOn(assuranceService, 'query').mockReturnValue(of(new HttpResponse({ body: assuranceCollection })));
      const additionalAssurances = [...assurances];
      const expectedCollection: IAssurance[] = [...additionalAssurances, ...assuranceCollection];
      jest.spyOn(assuranceService, 'addAssuranceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ garantie });
      comp.ngOnInit();

      expect(assuranceService.query).toHaveBeenCalled();
      expect(assuranceService.addAssuranceToCollectionIfMissing).toHaveBeenCalledWith(
        assuranceCollection,
        ...additionalAssurances.map(expect.objectContaining),
      );
      expect(comp.assurancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const garantie: IGarantie = { id: 456 };
      const assurance: IAssurance = { id: 8405 };
      garantie.assurances = [assurance];

      activatedRoute.data = of({ garantie });
      comp.ngOnInit();

      expect(comp.assurancesSharedCollection).toContain(assurance);
      expect(comp.garantie).toEqual(garantie);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGarantie>>();
      const garantie = { id: 123 };
      jest.spyOn(garantieFormService, 'getGarantie').mockReturnValue(garantie);
      jest.spyOn(garantieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ garantie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: garantie }));
      saveSubject.complete();

      // THEN
      expect(garantieFormService.getGarantie).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(garantieService.update).toHaveBeenCalledWith(expect.objectContaining(garantie));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGarantie>>();
      const garantie = { id: 123 };
      jest.spyOn(garantieFormService, 'getGarantie').mockReturnValue({ id: null });
      jest.spyOn(garantieService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ garantie: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: garantie }));
      saveSubject.complete();

      // THEN
      expect(garantieFormService.getGarantie).toHaveBeenCalled();
      expect(garantieService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGarantie>>();
      const garantie = { id: 123 };
      jest.spyOn(garantieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ garantie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(garantieService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAssurance', () => {
      it('Should forward to assuranceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(assuranceService, 'compareAssurance');
        comp.compareAssurance(entity, entity2);
        expect(assuranceService.compareAssurance).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
