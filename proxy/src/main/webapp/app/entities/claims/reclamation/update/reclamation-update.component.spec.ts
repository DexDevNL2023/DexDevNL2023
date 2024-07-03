import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ISinistre } from 'app/entities/claims/sinistre/sinistre.model';
import { SinistreService } from 'app/entities/claims/sinistre/service/sinistre.service';
import { ReclamationService } from '../service/reclamation.service';
import { IReclamation } from '../reclamation.model';
import { ReclamationFormService } from './reclamation-form.service';

import { ReclamationUpdateComponent } from './reclamation-update.component';

describe('Reclamation Management Update Component', () => {
  let comp: ReclamationUpdateComponent;
  let fixture: ComponentFixture<ReclamationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reclamationFormService: ReclamationFormService;
  let reclamationService: ReclamationService;
  let sinistreService: SinistreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReclamationUpdateComponent],
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
      .overrideTemplate(ReclamationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReclamationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reclamationFormService = TestBed.inject(ReclamationFormService);
    reclamationService = TestBed.inject(ReclamationService);
    sinistreService = TestBed.inject(SinistreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sinistre query and add missing value', () => {
      const reclamation: IReclamation = { id: 456 };
      const sinistre: ISinistre = { id: 9161 };
      reclamation.sinistre = sinistre;

      const sinistreCollection: ISinistre[] = [{ id: 19396 }];
      jest.spyOn(sinistreService, 'query').mockReturnValue(of(new HttpResponse({ body: sinistreCollection })));
      const additionalSinistres = [sinistre];
      const expectedCollection: ISinistre[] = [...additionalSinistres, ...sinistreCollection];
      jest.spyOn(sinistreService, 'addSinistreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reclamation });
      comp.ngOnInit();

      expect(sinistreService.query).toHaveBeenCalled();
      expect(sinistreService.addSinistreToCollectionIfMissing).toHaveBeenCalledWith(
        sinistreCollection,
        ...additionalSinistres.map(expect.objectContaining),
      );
      expect(comp.sinistresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reclamation: IReclamation = { id: 456 };
      const sinistre: ISinistre = { id: 15955 };
      reclamation.sinistre = sinistre;

      activatedRoute.data = of({ reclamation });
      comp.ngOnInit();

      expect(comp.sinistresSharedCollection).toContain(sinistre);
      expect(comp.reclamation).toEqual(reclamation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReclamation>>();
      const reclamation = { id: 123 };
      jest.spyOn(reclamationFormService, 'getReclamation').mockReturnValue(reclamation);
      jest.spyOn(reclamationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reclamation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reclamation }));
      saveSubject.complete();

      // THEN
      expect(reclamationFormService.getReclamation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reclamationService.update).toHaveBeenCalledWith(expect.objectContaining(reclamation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReclamation>>();
      const reclamation = { id: 123 };
      jest.spyOn(reclamationFormService, 'getReclamation').mockReturnValue({ id: null });
      jest.spyOn(reclamationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reclamation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reclamation }));
      saveSubject.complete();

      // THEN
      expect(reclamationFormService.getReclamation).toHaveBeenCalled();
      expect(reclamationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReclamation>>();
      const reclamation = { id: 123 };
      jest.spyOn(reclamationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reclamation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reclamationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSinistre', () => {
      it('Should forward to sinistreService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sinistreService, 'compareSinistre');
        comp.compareSinistre(entity, entity2);
        expect(sinistreService.compareSinistre).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
