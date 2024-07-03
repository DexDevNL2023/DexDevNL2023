import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../document-de-sinistre.test-samples';

import { DocumentDeSinistreFormService } from './document-de-sinistre-form.service';

describe('DocumentDeSinistre Form Service', () => {
  let service: DocumentDeSinistreFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentDeSinistreFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentDeSinistreFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentDeSinistreFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            type: expect.any(Object),
            url: expect.any(Object),
            dateTelechargement: expect.any(Object),
            sinistre: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentDeSinistre should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentDeSinistreFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            type: expect.any(Object),
            url: expect.any(Object),
            dateTelechargement: expect.any(Object),
            sinistre: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentDeSinistre', () => {
      it('should return NewDocumentDeSinistre for default DocumentDeSinistre initial value', () => {
        const formGroup = service.createDocumentDeSinistreFormGroup(sampleWithNewData);

        const documentDeSinistre = service.getDocumentDeSinistre(formGroup) as any;

        expect(documentDeSinistre).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentDeSinistre for empty DocumentDeSinistre initial value', () => {
        const formGroup = service.createDocumentDeSinistreFormGroup();

        const documentDeSinistre = service.getDocumentDeSinistre(formGroup) as any;

        expect(documentDeSinistre).toMatchObject({});
      });

      it('should return IDocumentDeSinistre', () => {
        const formGroup = service.createDocumentDeSinistreFormGroup(sampleWithRequiredData);

        const documentDeSinistre = service.getDocumentDeSinistre(formGroup) as any;

        expect(documentDeSinistre).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentDeSinistre should not enable id FormControl', () => {
        const formGroup = service.createDocumentDeSinistreFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentDeSinistre should disable id FormControl', () => {
        const formGroup = service.createDocumentDeSinistreFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
