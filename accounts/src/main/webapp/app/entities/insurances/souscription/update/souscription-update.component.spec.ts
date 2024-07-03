import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IPolicesAssurances } from 'app/entities/insurances/polices-assurances/polices-assurances.model';
import { PolicesAssurancesService } from 'app/entities/insurances/polices-assurances/service/polices-assurances.service';
import { SouscriptionService } from '../service/souscription.service';
import { ISouscription } from '../souscription.model';
import { SouscriptionFormService } from './souscription-form.service';

import { SouscriptionUpdateComponent } from './souscription-update.component';

describe('Souscription Management Update Component', () => {
  let comp: SouscriptionUpdateComponent;
  let fixture: ComponentFixture<SouscriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let souscriptionFormService: SouscriptionFormService;
  let souscriptionService: SouscriptionService;
  let policesAssurancesService: PolicesAssurancesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SouscriptionUpdateComponent],
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
      .overrideTemplate(SouscriptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SouscriptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    souscriptionFormService = TestBed.inject(SouscriptionFormService);
    souscriptionService = TestBed.inject(SouscriptionService);
    policesAssurancesService = TestBed.inject(PolicesAssurancesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PolicesAssurances query and add missing value', () => {
      const souscription: ISouscription = { id: 456 };
      const policesAssurances: IPolicesAssurances = { id: 26276 };
      souscription.policesAssurances = policesAssurances;

      const policesAssurancesCollection: IPolicesAssurances[] = [{ id: 21767 }];
      jest.spyOn(policesAssurancesService, 'query').mockReturnValue(of(new HttpResponse({ body: policesAssurancesCollection })));
      const additionalPolicesAssurances = [policesAssurances];
      const expectedCollection: IPolicesAssurances[] = [...additionalPolicesAssurances, ...policesAssurancesCollection];
      jest.spyOn(policesAssurancesService, 'addPolicesAssurancesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ souscription });
      comp.ngOnInit();

      expect(policesAssurancesService.query).toHaveBeenCalled();
      expect(policesAssurancesService.addPolicesAssurancesToCollectionIfMissing).toHaveBeenCalledWith(
        policesAssurancesCollection,
        ...additionalPolicesAssurances.map(expect.objectContaining),
      );
      expect(comp.policesAssurancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const souscription: ISouscription = { id: 456 };
      const policesAssurances: IPolicesAssurances = { id: 17565 };
      souscription.policesAssurances = policesAssurances;

      activatedRoute.data = of({ souscription });
      comp.ngOnInit();

      expect(comp.policesAssurancesSharedCollection).toContain(policesAssurances);
      expect(comp.souscription).toEqual(souscription);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISouscription>>();
      const souscription = { id: 123 };
      jest.spyOn(souscriptionFormService, 'getSouscription').mockReturnValue(souscription);
      jest.spyOn(souscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ souscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: souscription }));
      saveSubject.complete();

      // THEN
      expect(souscriptionFormService.getSouscription).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(souscriptionService.update).toHaveBeenCalledWith(expect.objectContaining(souscription));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISouscription>>();
      const souscription = { id: 123 };
      jest.spyOn(souscriptionFormService, 'getSouscription').mockReturnValue({ id: null });
      jest.spyOn(souscriptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ souscription: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: souscription }));
      saveSubject.complete();

      // THEN
      expect(souscriptionFormService.getSouscription).toHaveBeenCalled();
      expect(souscriptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISouscription>>();
      const souscription = { id: 123 };
      jest.spyOn(souscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ souscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(souscriptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePolicesAssurances', () => {
      it('Should forward to policesAssurancesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(policesAssurancesService, 'comparePolicesAssurances');
        comp.comparePolicesAssurances(entity, entity2);
        expect(policesAssurancesService.comparePolicesAssurances).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
