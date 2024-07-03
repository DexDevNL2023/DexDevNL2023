import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../medical-document.test-samples';

import { MedicalDocumentFormService } from './medical-document-form.service';

describe('MedicalDocument Form Service', () => {
  let service: MedicalDocumentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MedicalDocumentFormService);
  });

  describe('Service methods', () => {
    describe('createMedicalDocumentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMedicalDocumentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            type: expect.any(Object),
            url: expect.any(Object),
            dossierMedical: expect.any(Object),
          }),
        );
      });

      it('passing IMedicalDocument should create a new form with FormGroup', () => {
        const formGroup = service.createMedicalDocumentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            type: expect.any(Object),
            url: expect.any(Object),
            dossierMedical: expect.any(Object),
          }),
        );
      });
    });

    describe('getMedicalDocument', () => {
      it('should return NewMedicalDocument for default MedicalDocument initial value', () => {
        const formGroup = service.createMedicalDocumentFormGroup(sampleWithNewData);

        const medicalDocument = service.getMedicalDocument(formGroup) as any;

        expect(medicalDocument).toMatchObject(sampleWithNewData);
      });

      it('should return NewMedicalDocument for empty MedicalDocument initial value', () => {
        const formGroup = service.createMedicalDocumentFormGroup();

        const medicalDocument = service.getMedicalDocument(formGroup) as any;

        expect(medicalDocument).toMatchObject({});
      });

      it('should return IMedicalDocument', () => {
        const formGroup = service.createMedicalDocumentFormGroup(sampleWithRequiredData);

        const medicalDocument = service.getMedicalDocument(formGroup) as any;

        expect(medicalDocument).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMedicalDocument should not enable id FormControl', () => {
        const formGroup = service.createMedicalDocumentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMedicalDocument should disable id FormControl', () => {
        const formGroup = service.createMedicalDocumentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
