import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IFournisseur } from 'app/entities/claims/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/claims/fournisseur/service/fournisseur.service';
import { IPrestationSoin } from 'app/entities/claims/prestation-soin/prestation-soin.model';
import { PrestationSoinService } from 'app/entities/claims/prestation-soin/service/prestation-soin.service';
import { ISinistre } from '../sinistre.model';
import { SinistreService } from '../service/sinistre.service';
import { SinistreFormService } from './sinistre-form.service';

import { SinistreUpdateComponent } from './sinistre-update.component';

describe('Sinistre Management Update Component', () => {
  let comp: SinistreUpdateComponent;
  let fixture: ComponentFixture<SinistreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sinistreFormService: SinistreFormService;
  let sinistreService: SinistreService;
  let fournisseurService: FournisseurService;
  let prestationSoinService: PrestationSoinService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SinistreUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SinistreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SinistreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sinistreFormService = TestBed.inject(SinistreFormService);
    sinistreService = TestBed.inject(SinistreService);
    fournisseurService = TestBed.inject(FournisseurService);
    prestationSoinService = TestBed.inject(PrestationSoinService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Fournisseur query and add missing value', () => {
      const sinistre: ISinistre = { id: 456 };
      const fournisseur: IFournisseur = { id: 31366 };
      sinistre.fournisseur = fournisseur;

      const fournisseurCollection: IFournisseur[] = [{ id: 6147 }];
      jest.spyOn(fournisseurService, 'query').mockReturnValue(of(new HttpResponse({ body: fournisseurCollection })));
      const additionalFournisseurs = [fournisseur];
      const expectedCollection: IFournisseur[] = [...additionalFournisseurs, ...fournisseurCollection];
      jest.spyOn(fournisseurService, 'addFournisseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sinistre });
      comp.ngOnInit();

      expect(fournisseurService.query).toHaveBeenCalled();
      expect(fournisseurService.addFournisseurToCollectionIfMissing).toHaveBeenCalledWith(
        fournisseurCollection,
        ...additionalFournisseurs.map(expect.objectContaining),
      );
      expect(comp.fournisseursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PrestationSoin query and add missing value', () => {
      const sinistre: ISinistre = { id: 456 };
      const prestationsSoin: IPrestationSoin = { id: 7775 };
      sinistre.prestationsSoin = prestationsSoin;

      const prestationSoinCollection: IPrestationSoin[] = [{ id: 12139 }];
      jest.spyOn(prestationSoinService, 'query').mockReturnValue(of(new HttpResponse({ body: prestationSoinCollection })));
      const additionalPrestationSoins = [prestationsSoin];
      const expectedCollection: IPrestationSoin[] = [...additionalPrestationSoins, ...prestationSoinCollection];
      jest.spyOn(prestationSoinService, 'addPrestationSoinToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sinistre });
      comp.ngOnInit();

      expect(prestationSoinService.query).toHaveBeenCalled();
      expect(prestationSoinService.addPrestationSoinToCollectionIfMissing).toHaveBeenCalledWith(
        prestationSoinCollection,
        ...additionalPrestationSoins.map(expect.objectContaining),
      );
      expect(comp.prestationSoinsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sinistre: ISinistre = { id: 456 };
      const fournisseur: IFournisseur = { id: 3396 };
      sinistre.fournisseur = fournisseur;
      const prestationsSoin: IPrestationSoin = { id: 13206 };
      sinistre.prestationsSoin = prestationsSoin;

      activatedRoute.data = of({ sinistre });
      comp.ngOnInit();

      expect(comp.fournisseursSharedCollection).toContain(fournisseur);
      expect(comp.prestationSoinsSharedCollection).toContain(prestationsSoin);
      expect(comp.sinistre).toEqual(sinistre);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISinistre>>();
      const sinistre = { id: 123 };
      jest.spyOn(sinistreFormService, 'getSinistre').mockReturnValue(sinistre);
      jest.spyOn(sinistreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sinistre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sinistre }));
      saveSubject.complete();

      // THEN
      expect(sinistreFormService.getSinistre).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sinistreService.update).toHaveBeenCalledWith(expect.objectContaining(sinistre));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISinistre>>();
      const sinistre = { id: 123 };
      jest.spyOn(sinistreFormService, 'getSinistre').mockReturnValue({ id: null });
      jest.spyOn(sinistreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sinistre: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sinistre }));
      saveSubject.complete();

      // THEN
      expect(sinistreFormService.getSinistre).toHaveBeenCalled();
      expect(sinistreService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISinistre>>();
      const sinistre = { id: 123 };
      jest.spyOn(sinistreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sinistre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sinistreService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFournisseur', () => {
      it('Should forward to fournisseurService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(fournisseurService, 'compareFournisseur');
        comp.compareFournisseur(entity, entity2);
        expect(fournisseurService.compareFournisseur).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePrestationSoin', () => {
      it('Should forward to prestationSoinService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(prestationSoinService, 'comparePrestationSoin');
        comp.comparePrestationSoin(entity, entity2);
        expect(prestationSoinService.comparePrestationSoin).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
