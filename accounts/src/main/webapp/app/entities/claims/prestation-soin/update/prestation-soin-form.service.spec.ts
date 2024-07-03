import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../prestation-soin.test-samples';

import { PrestationSoinFormService } from './prestation-soin-form.service';

describe('PrestationSoin Form Service', () => {
  let service: PrestationSoinFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrestationSoinFormService);
  });

  describe('Service methods', () => {
    describe('createPrestationSoinFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPrestationSoinFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            description: expect.any(Object),
            type: expect.any(Object),
            montant: expect.any(Object),
            financeurSoin: expect.any(Object),
            financeurSoins: expect.any(Object),
          }),
        );
      });

      it('passing IPrestationSoin should create a new form with FormGroup', () => {
        const formGroup = service.createPrestationSoinFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            description: expect.any(Object),
            type: expect.any(Object),
            montant: expect.any(Object),
            financeurSoin: expect.any(Object),
            financeurSoins: expect.any(Object),
          }),
        );
      });
    });

    describe('getPrestationSoin', () => {
      it('should return NewPrestationSoin for default PrestationSoin initial value', () => {
        const formGroup = service.createPrestationSoinFormGroup(sampleWithNewData);

        const prestationSoin = service.getPrestationSoin(formGroup) as any;

        expect(prestationSoin).toMatchObject(sampleWithNewData);
      });

      it('should return NewPrestationSoin for empty PrestationSoin initial value', () => {
        const formGroup = service.createPrestationSoinFormGroup();

        const prestationSoin = service.getPrestationSoin(formGroup) as any;

        expect(prestationSoin).toMatchObject({});
      });

      it('should return IPrestationSoin', () => {
        const formGroup = service.createPrestationSoinFormGroup(sampleWithRequiredData);

        const prestationSoin = service.getPrestationSoin(formGroup) as any;

        expect(prestationSoin).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPrestationSoin should not enable id FormControl', () => {
        const formGroup = service.createPrestationSoinFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPrestationSoin should disable id FormControl', () => {
        const formGroup = service.createPrestationSoinFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
