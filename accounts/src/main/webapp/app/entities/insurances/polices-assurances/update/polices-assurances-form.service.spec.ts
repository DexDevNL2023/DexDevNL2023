import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../polices-assurances.test-samples';

import { PolicesAssurancesFormService } from './polices-assurances-form.service';

describe('PolicesAssurances Form Service', () => {
  let service: PolicesAssurancesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PolicesAssurancesFormService);
  });

  describe('Service methods', () => {
    describe('createPolicesAssurancesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPolicesAssurancesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroPolice: expect.any(Object),
            dateEmission: expect.any(Object),
            dateExpiration: expect.any(Object),
            description: expect.any(Object),
            montantAssure: expect.any(Object),
            plafondAssure: expect.any(Object),
            assurance: expect.any(Object),
          }),
        );
      });

      it('passing IPolicesAssurances should create a new form with FormGroup', () => {
        const formGroup = service.createPolicesAssurancesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroPolice: expect.any(Object),
            dateEmission: expect.any(Object),
            dateExpiration: expect.any(Object),
            description: expect.any(Object),
            montantAssure: expect.any(Object),
            plafondAssure: expect.any(Object),
            assurance: expect.any(Object),
          }),
        );
      });
    });

    describe('getPolicesAssurances', () => {
      it('should return NewPolicesAssurances for default PolicesAssurances initial value', () => {
        const formGroup = service.createPolicesAssurancesFormGroup(sampleWithNewData);

        const policesAssurances = service.getPolicesAssurances(formGroup) as any;

        expect(policesAssurances).toMatchObject(sampleWithNewData);
      });

      it('should return NewPolicesAssurances for empty PolicesAssurances initial value', () => {
        const formGroup = service.createPolicesAssurancesFormGroup();

        const policesAssurances = service.getPolicesAssurances(formGroup) as any;

        expect(policesAssurances).toMatchObject({});
      });

      it('should return IPolicesAssurances', () => {
        const formGroup = service.createPolicesAssurancesFormGroup(sampleWithRequiredData);

        const policesAssurances = service.getPolicesAssurances(formGroup) as any;

        expect(policesAssurances).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPolicesAssurances should not enable id FormControl', () => {
        const formGroup = service.createPolicesAssurancesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPolicesAssurances should disable id FormControl', () => {
        const formGroup = service.createPolicesAssurancesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
