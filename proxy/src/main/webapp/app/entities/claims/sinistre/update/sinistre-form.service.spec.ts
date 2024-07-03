import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sinistre.test-samples';

import { SinistreFormService } from './sinistre-form.service';

describe('Sinistre Form Service', () => {
  let service: SinistreFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SinistreFormService);
  });

  describe('Service methods', () => {
    describe('createSinistreFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSinistreFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroSinistre: expect.any(Object),
            dateDeclaration: expect.any(Object),
            dateTraitement: expect.any(Object),
            status: expect.any(Object),
            raison: expect.any(Object),
            montantReclame: expect.any(Object),
            montantApprouve: expect.any(Object),
            souscriptionId: expect.any(Object),
            fournisseur: expect.any(Object),
            prestationsSoin: expect.any(Object),
          }),
        );
      });

      it('passing ISinistre should create a new form with FormGroup', () => {
        const formGroup = service.createSinistreFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroSinistre: expect.any(Object),
            dateDeclaration: expect.any(Object),
            dateTraitement: expect.any(Object),
            status: expect.any(Object),
            raison: expect.any(Object),
            montantReclame: expect.any(Object),
            montantApprouve: expect.any(Object),
            souscriptionId: expect.any(Object),
            fournisseur: expect.any(Object),
            prestationsSoin: expect.any(Object),
          }),
        );
      });
    });

    describe('getSinistre', () => {
      it('should return NewSinistre for default Sinistre initial value', () => {
        const formGroup = service.createSinistreFormGroup(sampleWithNewData);

        const sinistre = service.getSinistre(formGroup) as any;

        expect(sinistre).toMatchObject(sampleWithNewData);
      });

      it('should return NewSinistre for empty Sinistre initial value', () => {
        const formGroup = service.createSinistreFormGroup();

        const sinistre = service.getSinistre(formGroup) as any;

        expect(sinistre).toMatchObject({});
      });

      it('should return ISinistre', () => {
        const formGroup = service.createSinistreFormGroup(sampleWithRequiredData);

        const sinistre = service.getSinistre(formGroup) as any;

        expect(sinistre).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISinistre should not enable id FormControl', () => {
        const formGroup = service.createSinistreFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSinistre should disable id FormControl', () => {
        const formGroup = service.createSinistreFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
