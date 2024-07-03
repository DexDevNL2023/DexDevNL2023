import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserExtra } from 'app/entities/user-extra/user-extra.model';
import { UserExtraService } from 'app/entities/user-extra/service/user-extra.service';
import { IMedicalRecord } from '../medical-record.model';
import { MedicalRecordService } from '../service/medical-record.service';
import { MedicalRecordFormService, MedicalRecordFormGroup } from './medical-record-form.service';

@Component({
  standalone: true,
  selector: 'jhi-medical-record-update',
  templateUrl: './medical-record-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicalRecordUpdateComponent implements OnInit {
  isSaving = false;
  medicalRecord: IMedicalRecord | null = null;

  userExtrasSharedCollection: IUserExtra[] = [];

  protected medicalRecordService = inject(MedicalRecordService);
  protected medicalRecordFormService = inject(MedicalRecordFormService);
  protected userExtraService = inject(UserExtraService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicalRecordFormGroup = this.medicalRecordFormService.createMedicalRecordFormGroup();

  compareUserExtra = (o1: IUserExtra | null, o2: IUserExtra | null): boolean => this.userExtraService.compareUserExtra(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicalRecord }) => {
      this.medicalRecord = medicalRecord;
      if (medicalRecord) {
        this.updateForm(medicalRecord);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicalRecord = this.medicalRecordFormService.getMedicalRecord(this.editForm);
    if (medicalRecord.id !== null) {
      this.subscribeToSaveResponse(this.medicalRecordService.update(medicalRecord));
    } else {
      this.subscribeToSaveResponse(this.medicalRecordService.create(medicalRecord));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicalRecord>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(medicalRecord: IMedicalRecord): void {
    this.medicalRecord = medicalRecord;
    this.medicalRecordFormService.resetForm(this.editForm, medicalRecord);

    this.userExtrasSharedCollection = this.userExtraService.addUserExtraToCollectionIfMissing<IUserExtra>(
      this.userExtrasSharedCollection,
      medicalRecord.utilisateur,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userExtraService
      .query()
      .pipe(map((res: HttpResponse<IUserExtra[]>) => res.body ?? []))
      .pipe(
        map((userExtras: IUserExtra[]) =>
          this.userExtraService.addUserExtraToCollectionIfMissing<IUserExtra>(userExtras, this.medicalRecord?.utilisateur),
        ),
      )
      .subscribe((userExtras: IUserExtra[]) => (this.userExtrasSharedCollection = userExtras));
  }
}
