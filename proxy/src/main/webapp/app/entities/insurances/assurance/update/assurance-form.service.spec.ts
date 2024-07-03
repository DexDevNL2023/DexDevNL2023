import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../assurance.test-samples';

import { AssuranceFormService } from './assurance-form.service';

describe('Assurance Form Service', () => {
  let service: AssuranceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssuranceFormService);
  });

  describe('Service methods', () => {
    describe('createAssuranceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAssuranceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            description: expect.any(Object),
            conditions: expect.any(Object),
            type: expect.any(Object),
            prixAssurance: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            garanties: expect.any(Object),
          }),
        );
      });

      it('passing IAssurance should create a new form with FormGroup', () => {
        const formGroup = service.createAssuranceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            description: expect.any(Object),
            conditions: expect.any(Object),
            type: expect.any(Object),
            prixAssurance: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            garanties: expect.any(Object),
          }),
        );
      });
    });

    describe('getAssurance', () => {
      it('should return NewAssurance for default Assurance initial value', () => {
        const formGroup = service.createAssuranceFormGroup(sampleWithNewData);

        const assurance = service.getAssurance(formGroup) as any;

        expect(assurance).toMatchObject(sampleWithNewData);
      });

      it('should return NewAssurance for empty Assurance initial value', () => {
        const formGroup = service.createAssuranceFormGroup();

        const assurance = service.getAssurance(formGroup) as any;

        expect(assurance).toMatchObject({});
      });

      it('should return IAssurance', () => {
        const formGroup = service.createAssuranceFormGroup(sampleWithRequiredData);

        const assurance = service.getAssurance(formGroup) as any;

        expect(assurance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAssurance should not enable id FormControl', () => {
        const formGroup = service.createAssuranceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAssurance should disable id FormControl', () => {
        const formGroup = service.createAssuranceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
