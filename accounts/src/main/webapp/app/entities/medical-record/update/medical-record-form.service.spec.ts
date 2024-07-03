import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../medical-record.test-samples';

import { MedicalRecordFormService } from './medical-record-form.service';

describe('MedicalRecord Form Service', () => {
  let service: MedicalRecordFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MedicalRecordFormService);
  });

  describe('Service methods', () => {
    describe('createMedicalRecordFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMedicalRecordFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numMedicalRecord: expect.any(Object),
            details: expect.any(Object),
            dateUpdated: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });

      it('passing IMedicalRecord should create a new form with FormGroup', () => {
        const formGroup = service.createMedicalRecordFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numMedicalRecord: expect.any(Object),
            details: expect.any(Object),
            dateUpdated: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });
    });

    describe('getMedicalRecord', () => {
      it('should return NewMedicalRecord for default MedicalRecord initial value', () => {
        const formGroup = service.createMedicalRecordFormGroup(sampleWithNewData);

        const medicalRecord = service.getMedicalRecord(formGroup) as any;

        expect(medicalRecord).toMatchObject(sampleWithNewData);
      });

      it('should return NewMedicalRecord for empty MedicalRecord initial value', () => {
        const formGroup = service.createMedicalRecordFormGroup();

        const medicalRecord = service.getMedicalRecord(formGroup) as any;

        expect(medicalRecord).toMatchObject({});
      });

      it('should return IMedicalRecord', () => {
        const formGroup = service.createMedicalRecordFormGroup(sampleWithRequiredData);

        const medicalRecord = service.getMedicalRecord(formGroup) as any;

        expect(medicalRecord).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMedicalRecord should not enable id FormControl', () => {
        const formGroup = service.createMedicalRecordFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMedicalRecord should disable id FormControl', () => {
        const formGroup = service.createMedicalRecordFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
