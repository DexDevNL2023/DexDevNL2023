import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAssurance } from 'app/entities/insurances/assurance/assurance.model';
import { AssuranceService } from 'app/entities/insurances/assurance/service/assurance.service';
import { RapportService } from '../service/rapport.service';
import { IRapport } from '../rapport.model';
import { RapportFormService } from './rapport-form.service';

import { RapportUpdateComponent } from './rapport-update.component';

describe('Rapport Management Update Component', () => {
  let comp: RapportUpdateComponent;
  let fixture: ComponentFixture<RapportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rapportFormService: RapportFormService;
  let rapportService: RapportService;
  let assuranceService: AssuranceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RapportUpdateComponent],
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
      .overrideTemplate(RapportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RapportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rapportFormService = TestBed.inject(RapportFormService);
    rapportService = TestBed.inject(RapportService);
    assuranceService = TestBed.inject(AssuranceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Assurance query and add missing value', () => {
      const rapport: IRapport = { id: 456 };
      const assurance: IAssurance = { id: 4189 };
      rapport.assurance = assurance;

      const assuranceCollection: IAssurance[] = [{ id: 29410 }];
      jest.spyOn(assuranceService, 'query').mockReturnValue(of(new HttpResponse({ body: assuranceCollection })));
      const additionalAssurances = [assurance];
      const expectedCollection: IAssurance[] = [...additionalAssurances, ...assuranceCollection];
      jest.spyOn(assuranceService, 'addAssuranceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rapport });
      comp.ngOnInit();

      expect(assuranceService.query).toHaveBeenCalled();
      expect(assuranceService.addAssuranceToCollectionIfMissing).toHaveBeenCalledWith(
        assuranceCollection,
        ...additionalAssurances.map(expect.objectContaining),
      );
      expect(comp.assurancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rapport: IRapport = { id: 456 };
      const assurance: IAssurance = { id: 32186 };
      rapport.assurance = assurance;

      activatedRoute.data = of({ rapport });
      comp.ngOnInit();

      expect(comp.assurancesSharedCollection).toContain(assurance);
      expect(comp.rapport).toEqual(rapport);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRapport>>();
      const rapport = { id: 123 };
      jest.spyOn(rapportFormService, 'getRapport').mockReturnValue(rapport);
      jest.spyOn(rapportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rapport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rapport }));
      saveSubject.complete();

      // THEN
      expect(rapportFormService.getRapport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rapportService.update).toHaveBeenCalledWith(expect.objectContaining(rapport));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRapport>>();
      const rapport = { id: 123 };
      jest.spyOn(rapportFormService, 'getRapport').mockReturnValue({ id: null });
      jest.spyOn(rapportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rapport: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rapport }));
      saveSubject.complete();

      // THEN
      expect(rapportFormService.getRapport).toHaveBeenCalled();
      expect(rapportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRapport>>();
      const rapport = { id: 123 };
      jest.spyOn(rapportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rapport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rapportService.update).toHaveBeenCalled();
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
