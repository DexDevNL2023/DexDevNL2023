import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../souscription.test-samples';

import { SouscriptionFormService } from './souscription-form.service';

describe('Souscription Form Service', () => {
  let service: SouscriptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SouscriptionFormService);
  });

  describe('Service methods', () => {
    describe('createSouscriptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSouscriptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroSouscription: expect.any(Object),
            dateSouscription: expect.any(Object),
            status: expect.any(Object),
            cumulSinistres: expect.any(Object),
            frequencePaiement: expect.any(Object),
            utilisateurId: expect.any(Object),
            policesAssurances: expect.any(Object),
          }),
        );
      });

      it('passing ISouscription should create a new form with FormGroup', () => {
        const formGroup = service.createSouscriptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroSouscription: expect.any(Object),
            dateSouscription: expect.any(Object),
            status: expect.any(Object),
            cumulSinistres: expect.any(Object),
            frequencePaiement: expect.any(Object),
            utilisateurId: expect.any(Object),
            policesAssurances: expect.any(Object),
          }),
        );
      });
    });

    describe('getSouscription', () => {
      it('should return NewSouscription for default Souscription initial value', () => {
        const formGroup = service.createSouscriptionFormGroup(sampleWithNewData);

        const souscription = service.getSouscription(formGroup) as any;

        expect(souscription).toMatchObject(sampleWithNewData);
      });

      it('should return NewSouscription for empty Souscription initial value', () => {
        const formGroup = service.createSouscriptionFormGroup();

        const souscription = service.getSouscription(formGroup) as any;

        expect(souscription).toMatchObject({});
      });

      it('should return ISouscription', () => {
        const formGroup = service.createSouscriptionFormGroup(sampleWithRequiredData);

        const souscription = service.getSouscription(formGroup) as any;

        expect(souscription).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISouscription should not enable id FormControl', () => {
        const formGroup = service.createSouscriptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSouscription should disable id FormControl', () => {
        const formGroup = service.createSouscriptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
