import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IMedicalRecord } from 'app/entities/medical-record/medical-record.model';
import { MedicalRecordService } from 'app/entities/medical-record/service/medical-record.service';
import { MedicalDocumentService } from '../service/medical-document.service';
import { IMedicalDocument } from '../medical-document.model';
import { MedicalDocumentFormService } from './medical-document-form.service';

import { MedicalDocumentUpdateComponent } from './medical-document-update.component';

describe('MedicalDocument Management Update Component', () => {
  let comp: MedicalDocumentUpdateComponent;
  let fixture: ComponentFixture<MedicalDocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicalDocumentFormService: MedicalDocumentFormService;
  let medicalDocumentService: MedicalDocumentService;
  let medicalRecordService: MedicalRecordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MedicalDocumentUpdateComponent],
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
      .overrideTemplate(MedicalDocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicalDocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicalDocumentFormService = TestBed.inject(MedicalDocumentFormService);
    medicalDocumentService = TestBed.inject(MedicalDocumentService);
    medicalRecordService = TestBed.inject(MedicalRecordService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MedicalRecord query and add missing value', () => {
      const medicalDocument: IMedicalDocument = { id: 456 };
      const dossierMedical: IMedicalRecord = { id: 20742 };
      medicalDocument.dossierMedical = dossierMedical;

      const medicalRecordCollection: IMedicalRecord[] = [{ id: 16685 }];
      jest.spyOn(medicalRecordService, 'query').mockReturnValue(of(new HttpResponse({ body: medicalRecordCollection })));
      const additionalMedicalRecords = [dossierMedical];
      const expectedCollection: IMedicalRecord[] = [...additionalMedicalRecords, ...medicalRecordCollection];
      jest.spyOn(medicalRecordService, 'addMedicalRecordToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicalDocument });
      comp.ngOnInit();

      expect(medicalRecordService.query).toHaveBeenCalled();
      expect(medicalRecordService.addMedicalRecordToCollectionIfMissing).toHaveBeenCalledWith(
        medicalRecordCollection,
        ...additionalMedicalRecords.map(expect.objectContaining),
      );
      expect(comp.medicalRecordsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const medicalDocument: IMedicalDocument = { id: 456 };
      const dossierMedical: IMedicalRecord = { id: 613 };
      medicalDocument.dossierMedical = dossierMedical;

      activatedRoute.data = of({ medicalDocument });
      comp.ngOnInit();

      expect(comp.medicalRecordsSharedCollection).toContain(dossierMedical);
      expect(comp.medicalDocument).toEqual(medicalDocument);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalDocument>>();
      const medicalDocument = { id: 123 };
      jest.spyOn(medicalDocumentFormService, 'getMedicalDocument').mockReturnValue(medicalDocument);
      jest.spyOn(medicalDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalDocument }));
      saveSubject.complete();

      // THEN
      expect(medicalDocumentFormService.getMedicalDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicalDocumentService.update).toHaveBeenCalledWith(expect.objectContaining(medicalDocument));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalDocument>>();
      const medicalDocument = { id: 123 };
      jest.spyOn(medicalDocumentFormService, 'getMedicalDocument').mockReturnValue({ id: null });
      jest.spyOn(medicalDocumentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalDocument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalDocument }));
      saveSubject.complete();

      // THEN
      expect(medicalDocumentFormService.getMedicalDocument).toHaveBeenCalled();
      expect(medicalDocumentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalDocument>>();
      const medicalDocument = { id: 123 };
      jest.spyOn(medicalDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicalDocumentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMedicalRecord', () => {
      it('Should forward to medicalRecordService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(medicalRecordService, 'compareMedicalRecord');
        comp.compareMedicalRecord(entity, entity2);
        expect(medicalRecordService.compareMedicalRecord).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
