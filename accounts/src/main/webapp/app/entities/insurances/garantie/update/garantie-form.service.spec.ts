import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../garantie.test-samples';

import { GarantieFormService } from './garantie-form.service';

describe('Garantie Form Service', () => {
  let service: GarantieFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GarantieFormService);
  });

  describe('Service methods', () => {
    describe('createGarantieFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGarantieFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            status: expect.any(Object),
            assurances: expect.any(Object),
          }),
        );
      });

      it('passing IGarantie should create a new form with FormGroup', () => {
        const formGroup = service.createGarantieFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            status: expect.any(Object),
            assurances: expect.any(Object),
          }),
        );
      });
    });

    describe('getGarantie', () => {
      it('should return NewGarantie for default Garantie initial value', () => {
        const formGroup = service.createGarantieFormGroup(sampleWithNewData);

        const garantie = service.getGarantie(formGroup) as any;

        expect(garantie).toMatchObject(sampleWithNewData);
      });

      it('should return NewGarantie for empty Garantie initial value', () => {
        const formGroup = service.createGarantieFormGroup();

        const garantie = service.getGarantie(formGroup) as any;

        expect(garantie).toMatchObject({});
      });

      it('should return IGarantie', () => {
        const formGroup = service.createGarantieFormGroup(sampleWithRequiredData);

        const garantie = service.getGarantie(formGroup) as any;

        expect(garantie).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGarantie should not enable id FormControl', () => {
        const formGroup = service.createGarantieFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGarantie should disable id FormControl', () => {
        const formGroup = service.createGarantieFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
