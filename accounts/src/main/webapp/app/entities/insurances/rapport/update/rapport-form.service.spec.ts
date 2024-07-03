import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../rapport.test-samples';

import { RapportFormService } from './rapport-form.service';

describe('Rapport Form Service', () => {
  let service: RapportFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RapportFormService);
  });

  describe('Service methods', () => {
    describe('createRapportFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRapportFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titre: expect.any(Object),
            description: expect.any(Object),
            type: expect.any(Object),
            dateGeneration: expect.any(Object),
            url: expect.any(Object),
            assurance: expect.any(Object),
          }),
        );
      });

      it('passing IRapport should create a new form with FormGroup', () => {
        const formGroup = service.createRapportFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titre: expect.any(Object),
            description: expect.any(Object),
            type: expect.any(Object),
            dateGeneration: expect.any(Object),
            url: expect.any(Object),
            assurance: expect.any(Object),
          }),
        );
      });
    });

    describe('getRapport', () => {
      it('should return NewRapport for default Rapport initial value', () => {
        const formGroup = service.createRapportFormGroup(sampleWithNewData);

        const rapport = service.getRapport(formGroup) as any;

        expect(rapport).toMatchObject(sampleWithNewData);
      });

      it('should return NewRapport for empty Rapport initial value', () => {
        const formGroup = service.createRapportFormGroup();

        const rapport = service.getRapport(formGroup) as any;

        expect(rapport).toMatchObject({});
      });

      it('should return IRapport', () => {
        const formGroup = service.createRapportFormGroup(sampleWithRequiredData);

        const rapport = service.getRapport(formGroup) as any;

        expect(rapport).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRapport should not enable id FormControl', () => {
        const formGroup = service.createRapportFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRapport should disable id FormControl', () => {
        const formGroup = service.createRapportFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
