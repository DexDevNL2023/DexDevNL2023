import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../financeur-soin.test-samples';

import { FinanceurSoinFormService } from './financeur-soin-form.service';

describe('FinanceurSoin Form Service', () => {
  let service: FinanceurSoinFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FinanceurSoinFormService);
  });

  describe('Service methods', () => {
    describe('createFinanceurSoinFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFinanceurSoinFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            type: expect.any(Object),
            adresse: expect.any(Object),
            telephone: expect.any(Object),
            email: expect.any(Object),
            servicesFournis: expect.any(Object),
            prestationSoins: expect.any(Object),
          }),
        );
      });

      it('passing IFinanceurSoin should create a new form with FormGroup', () => {
        const formGroup = service.createFinanceurSoinFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            type: expect.any(Object),
            adresse: expect.any(Object),
            telephone: expect.any(Object),
            email: expect.any(Object),
            servicesFournis: expect.any(Object),
            prestationSoins: expect.any(Object),
          }),
        );
      });
    });

    describe('getFinanceurSoin', () => {
      it('should return NewFinanceurSoin for default FinanceurSoin initial value', () => {
        const formGroup = service.createFinanceurSoinFormGroup(sampleWithNewData);

        const financeurSoin = service.getFinanceurSoin(formGroup) as any;

        expect(financeurSoin).toMatchObject(sampleWithNewData);
      });

      it('should return NewFinanceurSoin for empty FinanceurSoin initial value', () => {
        const formGroup = service.createFinanceurSoinFormGroup();

        const financeurSoin = service.getFinanceurSoin(formGroup) as any;

        expect(financeurSoin).toMatchObject({});
      });

      it('should return IFinanceurSoin', () => {
        const formGroup = service.createFinanceurSoinFormGroup(sampleWithRequiredData);

        const financeurSoin = service.getFinanceurSoin(formGroup) as any;

        expect(financeurSoin).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFinanceurSoin should not enable id FormControl', () => {
        const formGroup = service.createFinanceurSoinFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFinanceurSoin should disable id FormControl', () => {
        const formGroup = service.createFinanceurSoinFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
