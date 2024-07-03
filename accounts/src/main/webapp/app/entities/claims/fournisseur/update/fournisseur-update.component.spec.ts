import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { FournisseurService } from '../service/fournisseur.service';
import { IFournisseur } from '../fournisseur.model';
import { FournisseurFormService } from './fournisseur-form.service';

import { FournisseurUpdateComponent } from './fournisseur-update.component';

describe('Fournisseur Management Update Component', () => {
  let comp: FournisseurUpdateComponent;
  let fixture: ComponentFixture<FournisseurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fournisseurFormService: FournisseurFormService;
  let fournisseurService: FournisseurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FournisseurUpdateComponent],
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
      .overrideTemplate(FournisseurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FournisseurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fournisseurFormService = TestBed.inject(FournisseurFormService);
    fournisseurService = TestBed.inject(FournisseurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fournisseur: IFournisseur = { id: 456 };

      activatedRoute.data = of({ fournisseur });
      comp.ngOnInit();

      expect(comp.fournisseur).toEqual(fournisseur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFournisseur>>();
      const fournisseur = { id: 123 };
      jest.spyOn(fournisseurFormService, 'getFournisseur').mockReturnValue(fournisseur);
      jest.spyOn(fournisseurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fournisseur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fournisseur }));
      saveSubject.complete();

      // THEN
      expect(fournisseurFormService.getFournisseur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fournisseurService.update).toHaveBeenCalledWith(expect.objectContaining(fournisseur));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFournisseur>>();
      const fournisseur = { id: 123 };
      jest.spyOn(fournisseurFormService, 'getFournisseur').mockReturnValue({ id: null });
      jest.spyOn(fournisseurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fournisseur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fournisseur }));
      saveSubject.complete();

      // THEN
      expect(fournisseurFormService.getFournisseur).toHaveBeenCalled();
      expect(fournisseurService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFournisseur>>();
      const fournisseur = { id: 123 };
      jest.spyOn(fournisseurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fournisseur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fournisseurService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
