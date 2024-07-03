import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAssurance } from 'app/entities/insurances/assurance/assurance.model';
import { AssuranceService } from 'app/entities/insurances/assurance/service/assurance.service';
import { PolicesAssurancesService } from '../service/polices-assurances.service';
import { IPolicesAssurances } from '../polices-assurances.model';
import { PolicesAssurancesFormService } from './polices-assurances-form.service';

import { PolicesAssurancesUpdateComponent } from './polices-assurances-update.component';

describe('PolicesAssurances Management Update Component', () => {
  let comp: PolicesAssurancesUpdateComponent;
  let fixture: ComponentFixture<PolicesAssurancesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let policesAssurancesFormService: PolicesAssurancesFormService;
  let policesAssurancesService: PolicesAssurancesService;
  let assuranceService: AssuranceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PolicesAssurancesUpdateComponent],
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
      .overrideTemplate(PolicesAssurancesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PolicesAssurancesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    policesAssurancesFormService = TestBed.inject(PolicesAssurancesFormService);
    policesAssurancesService = TestBed.inject(PolicesAssurancesService);
    assuranceService = TestBed.inject(AssuranceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Assurance query and add missing value', () => {
      const policesAssurances: IPolicesAssurances = { id: 456 };
      const assurance: IAssurance = { id: 20996 };
      policesAssurances.assurance = assurance;

      const assuranceCollection: IAssurance[] = [{ id: 27046 }];
      jest.spyOn(assuranceService, 'query').mockReturnValue(of(new HttpResponse({ body: assuranceCollection })));
      const additionalAssurances = [assurance];
      const expectedCollection: IAssurance[] = [...additionalAssurances, ...assuranceCollection];
      jest.spyOn(assuranceService, 'addAssuranceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ policesAssurances });
      comp.ngOnInit();

      expect(assuranceService.query).toHaveBeenCalled();
      expect(assuranceService.addAssuranceToCollectionIfMissing).toHaveBeenCalledWith(
        assuranceCollection,
        ...additionalAssurances.map(expect.objectContaining),
      );
      expect(comp.assurancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const policesAssurances: IPolicesAssurances = { id: 456 };
      const assurance: IAssurance = { id: 20240 };
      policesAssurances.assurance = assurance;

      activatedRoute.data = of({ policesAssurances });
      comp.ngOnInit();

      expect(comp.assurancesSharedCollection).toContain(assurance);
      expect(comp.policesAssurances).toEqual(policesAssurances);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPolicesAssurances>>();
      const policesAssurances = { id: 123 };
      jest.spyOn(policesAssurancesFormService, 'getPolicesAssurances').mockReturnValue(policesAssurances);
      jest.spyOn(policesAssurancesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ policesAssurances });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: policesAssurances }));
      saveSubject.complete();

      // THEN
      expect(policesAssurancesFormService.getPolicesAssurances).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(policesAssurancesService.update).toHaveBeenCalledWith(expect.objectContaining(policesAssurances));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPolicesAssurances>>();
      const policesAssurances = { id: 123 };
      jest.spyOn(policesAssurancesFormService, 'getPolicesAssurances').mockReturnValue({ id: null });
      jest.spyOn(policesAssurancesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ policesAssurances: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: policesAssurances }));
      saveSubject.complete();

      // THEN
      expect(policesAssurancesFormService.getPolicesAssurances).toHaveBeenCalled();
      expect(policesAssurancesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPolicesAssurances>>();
      const policesAssurances = { id: 123 };
      jest.spyOn(policesAssurancesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ policesAssurances });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(policesAssurancesService.update).toHaveBeenCalled();
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
