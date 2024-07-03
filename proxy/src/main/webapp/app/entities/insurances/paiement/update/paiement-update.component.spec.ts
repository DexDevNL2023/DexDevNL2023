import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ISouscription } from 'app/entities/insurances/souscription/souscription.model';
import { SouscriptionService } from 'app/entities/insurances/souscription/service/souscription.service';
import { PaiementService } from '../service/paiement.service';
import { IPaiement } from '../paiement.model';
import { PaiementFormService } from './paiement-form.service';

import { PaiementUpdateComponent } from './paiement-update.component';

describe('Paiement Management Update Component', () => {
  let comp: PaiementUpdateComponent;
  let fixture: ComponentFixture<PaiementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paiementFormService: PaiementFormService;
  let paiementService: PaiementService;
  let souscriptionService: SouscriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PaiementUpdateComponent],
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
      .overrideTemplate(PaiementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaiementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paiementFormService = TestBed.inject(PaiementFormService);
    paiementService = TestBed.inject(PaiementService);
    souscriptionService = TestBed.inject(SouscriptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Souscription query and add missing value', () => {
      const paiement: IPaiement = { id: 456 };
      const souscription: ISouscription = { id: 32486 };
      paiement.souscription = souscription;

      const souscriptionCollection: ISouscription[] = [{ id: 1039 }];
      jest.spyOn(souscriptionService, 'query').mockReturnValue(of(new HttpResponse({ body: souscriptionCollection })));
      const additionalSouscriptions = [souscription];
      const expectedCollection: ISouscription[] = [...additionalSouscriptions, ...souscriptionCollection];
      jest.spyOn(souscriptionService, 'addSouscriptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paiement });
      comp.ngOnInit();

      expect(souscriptionService.query).toHaveBeenCalled();
      expect(souscriptionService.addSouscriptionToCollectionIfMissing).toHaveBeenCalledWith(
        souscriptionCollection,
        ...additionalSouscriptions.map(expect.objectContaining),
      );
      expect(comp.souscriptionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const paiement: IPaiement = { id: 456 };
      const souscription: ISouscription = { id: 15202 };
      paiement.souscription = souscription;

      activatedRoute.data = of({ paiement });
      comp.ngOnInit();

      expect(comp.souscriptionsSharedCollection).toContain(souscription);
      expect(comp.paiement).toEqual(paiement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiement>>();
      const paiement = { id: 123 };
      jest.spyOn(paiementFormService, 'getPaiement').mockReturnValue(paiement);
      jest.spyOn(paiementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiement }));
      saveSubject.complete();

      // THEN
      expect(paiementFormService.getPaiement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paiementService.update).toHaveBeenCalledWith(expect.objectContaining(paiement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiement>>();
      const paiement = { id: 123 };
      jest.spyOn(paiementFormService, 'getPaiement').mockReturnValue({ id: null });
      jest.spyOn(paiementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiement }));
      saveSubject.complete();

      // THEN
      expect(paiementFormService.getPaiement).toHaveBeenCalled();
      expect(paiementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiement>>();
      const paiement = { id: 123 };
      jest.spyOn(paiementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paiementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSouscription', () => {
      it('Should forward to souscriptionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(souscriptionService, 'compareSouscription');
        comp.compareSouscription(entity, entity2);
        expect(souscriptionService.compareSouscription).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
