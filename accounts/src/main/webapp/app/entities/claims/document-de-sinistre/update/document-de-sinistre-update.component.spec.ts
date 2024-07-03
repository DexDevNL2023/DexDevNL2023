import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ISinistre } from 'app/entities/claims/sinistre/sinistre.model';
import { SinistreService } from 'app/entities/claims/sinistre/service/sinistre.service';
import { DocumentDeSinistreService } from '../service/document-de-sinistre.service';
import { IDocumentDeSinistre } from '../document-de-sinistre.model';
import { DocumentDeSinistreFormService } from './document-de-sinistre-form.service';

import { DocumentDeSinistreUpdateComponent } from './document-de-sinistre-update.component';

describe('DocumentDeSinistre Management Update Component', () => {
  let comp: DocumentDeSinistreUpdateComponent;
  let fixture: ComponentFixture<DocumentDeSinistreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentDeSinistreFormService: DocumentDeSinistreFormService;
  let documentDeSinistreService: DocumentDeSinistreService;
  let sinistreService: SinistreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentDeSinistreUpdateComponent],
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
      .overrideTemplate(DocumentDeSinistreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentDeSinistreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentDeSinistreFormService = TestBed.inject(DocumentDeSinistreFormService);
    documentDeSinistreService = TestBed.inject(DocumentDeSinistreService);
    sinistreService = TestBed.inject(SinistreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sinistre query and add missing value', () => {
      const documentDeSinistre: IDocumentDeSinistre = { id: 456 };
      const sinistre: ISinistre = { id: 26035 };
      documentDeSinistre.sinistre = sinistre;

      const sinistreCollection: ISinistre[] = [{ id: 15616 }];
      jest.spyOn(sinistreService, 'query').mockReturnValue(of(new HttpResponse({ body: sinistreCollection })));
      const additionalSinistres = [sinistre];
      const expectedCollection: ISinistre[] = [...additionalSinistres, ...sinistreCollection];
      jest.spyOn(sinistreService, 'addSinistreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentDeSinistre });
      comp.ngOnInit();

      expect(sinistreService.query).toHaveBeenCalled();
      expect(sinistreService.addSinistreToCollectionIfMissing).toHaveBeenCalledWith(
        sinistreCollection,
        ...additionalSinistres.map(expect.objectContaining),
      );
      expect(comp.sinistresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const documentDeSinistre: IDocumentDeSinistre = { id: 456 };
      const sinistre: ISinistre = { id: 27563 };
      documentDeSinistre.sinistre = sinistre;

      activatedRoute.data = of({ documentDeSinistre });
      comp.ngOnInit();

      expect(comp.sinistresSharedCollection).toContain(sinistre);
      expect(comp.documentDeSinistre).toEqual(documentDeSinistre);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentDeSinistre>>();
      const documentDeSinistre = { id: 123 };
      jest.spyOn(documentDeSinistreFormService, 'getDocumentDeSinistre').mockReturnValue(documentDeSinistre);
      jest.spyOn(documentDeSinistreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentDeSinistre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentDeSinistre }));
      saveSubject.complete();

      // THEN
      expect(documentDeSinistreFormService.getDocumentDeSinistre).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentDeSinistreService.update).toHaveBeenCalledWith(expect.objectContaining(documentDeSinistre));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentDeSinistre>>();
      const documentDeSinistre = { id: 123 };
      jest.spyOn(documentDeSinistreFormService, 'getDocumentDeSinistre').mockReturnValue({ id: null });
      jest.spyOn(documentDeSinistreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentDeSinistre: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentDeSinistre }));
      saveSubject.complete();

      // THEN
      expect(documentDeSinistreFormService.getDocumentDeSinistre).toHaveBeenCalled();
      expect(documentDeSinistreService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentDeSinistre>>();
      const documentDeSinistre = { id: 123 };
      jest.spyOn(documentDeSinistreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentDeSinistre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentDeSinistreService.update).toHaveBeenCalled();
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
