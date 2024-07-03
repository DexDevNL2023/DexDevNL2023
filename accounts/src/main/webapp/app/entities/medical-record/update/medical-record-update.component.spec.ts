import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUserExtra } from 'app/entities/user-extra/user-extra.model';
import { UserExtraService } from 'app/entities/user-extra/service/user-extra.service';
import { MedicalRecordService } from '../service/medical-record.service';
import { IMedicalRecord } from '../medical-record.model';
import { MedicalRecordFormService } from './medical-record-form.service';

import { MedicalRecordUpdateComponent } from './medical-record-update.component';

describe('MedicalRecord Management Update Component', () => {
  let comp: MedicalRecordUpdateComponent;
  let fixture: ComponentFixture<MedicalRecordUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicalRecordFormService: MedicalRecordFormService;
  let medicalRecordService: MedicalRecordService;
  let userExtraService: UserExtraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MedicalRecordUpdateComponent],
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
      .overrideTemplate(MedicalRecordUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicalRecordUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicalRecordFormService = TestBed.inject(MedicalRecordFormService);
    medicalRecordService = TestBed.inject(MedicalRecordService);
    userExtraService = TestBed.inject(UserExtraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserExtra query and add missing value', () => {
      const medicalRecord: IMedicalRecord = { id: 456 };
      const utilisateur: IUserExtra = { id: 17575 };
      medicalRecord.utilisateur = utilisateur;

      const userExtraCollection: IUserExtra[] = [{ id: 21026 }];
      jest.spyOn(userExtraService, 'query').mockReturnValue(of(new HttpResponse({ body: userExtraCollection })));
      const additionalUserExtras = [utilisateur];
      const expectedCollection: IUserExtra[] = [...additionalUserExtras, ...userExtraCollection];
      jest.spyOn(userExtraService, 'addUserExtraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicalRecord });
      comp.ngOnInit();

      expect(userExtraService.query).toHaveBeenCalled();
      expect(userExtraService.addUserExtraToCollectionIfMissing).toHaveBeenCalledWith(
        userExtraCollection,
        ...additionalUserExtras.map(expect.objectContaining),
      );
      expect(comp.userExtrasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const medicalRecord: IMedicalRecord = { id: 456 };
      const utilisateur: IUserExtra = { id: 27079 };
      medicalRecord.utilisateur = utilisateur;

      activatedRoute.data = of({ medicalRecord });
      comp.ngOnInit();

      expect(comp.userExtrasSharedCollection).toContain(utilisateur);
      expect(comp.medicalRecord).toEqual(medicalRecord);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalRecord>>();
      const medicalRecord = { id: 123 };
      jest.spyOn(medicalRecordFormService, 'getMedicalRecord').mockReturnValue(medicalRecord);
      jest.spyOn(medicalRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalRecord }));
      saveSubject.complete();

      // THEN
      expect(medicalRecordFormService.getMedicalRecord).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicalRecordService.update).toHaveBeenCalledWith(expect.objectContaining(medicalRecord));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalRecord>>();
      const medicalRecord = { id: 123 };
      jest.spyOn(medicalRecordFormService, 'getMedicalRecord').mockReturnValue({ id: null });
      jest.spyOn(medicalRecordService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalRecord: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalRecord }));
      saveSubject.complete();

      // THEN
      expect(medicalRecordFormService.getMedicalRecord).toHaveBeenCalled();
      expect(medicalRecordService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalRecord>>();
      const medicalRecord = { id: 123 };
      jest.spyOn(medicalRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicalRecordService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUserExtra', () => {
      it('Should forward to userExtraService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userExtraService, 'compareUserExtra');
        comp.compareUserExtra(entity, entity2);
        expect(userExtraService.compareUserExtra).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
