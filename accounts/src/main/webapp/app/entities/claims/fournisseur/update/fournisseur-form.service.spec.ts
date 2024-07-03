import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../fournisseur.test-samples';

import { FournisseurFormService } from './fournisseur-form.service';

describe('Fournisseur Form Service', () => {
  let service: FournisseurFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FournisseurFormService);
  });

  describe('Service methods', () => {
    describe('createFournisseurFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFournisseurFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            adresse: expect.any(Object),
            telephone: expect.any(Object),
            email: expect.any(Object),
            addresse: expect.any(Object),
            ville: expect.any(Object),
            pays: expect.any(Object),
            servicesFournis: expect.any(Object),
            dateDebutContrat: expect.any(Object),
            dateFinContrat: expect.any(Object),
          }),
        );
      });

      it('passing IFournisseur should create a new form with FormGroup', () => {
        const formGroup = service.createFournisseurFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            adresse: expect.any(Object),
            telephone: expect.any(Object),
            email: expect.any(Object),
            addresse: expect.any(Object),
            ville: expect.any(Object),
            pays: expect.any(Object),
            servicesFournis: expect.any(Object),
            dateDebutContrat: expect.any(Object),
            dateFinContrat: expect.any(Object),
          }),
        );
      });
    });

    describe('getFournisseur', () => {
      it('should return NewFournisseur for default Fournisseur initial value', () => {
        const formGroup = service.createFournisseurFormGroup(sampleWithNewData);

        const fournisseur = service.getFournisseur(formGroup) as any;

        expect(fournisseur).toMatchObject(sampleWithNewData);
      });

      it('should return NewFournisseur for empty Fournisseur initial value', () => {
        const formGroup = service.createFournisseurFormGroup();

        const fournisseur = service.getFournisseur(formGroup) as any;

        expect(fournisseur).toMatchObject({});
      });

      it('should return IFournisseur', () => {
        const formGroup = service.createFournisseurFormGroup(sampleWithRequiredData);

        const fournisseur = service.getFournisseur(formGroup) as any;

        expect(fournisseur).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFournisseur should not enable id FormControl', () => {
        const formGroup = service.createFournisseurFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFournisseur should disable id FormControl', () => {
        const formGroup = service.createFournisseurFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
