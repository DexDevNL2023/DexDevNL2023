import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMedicalRecord } from 'app/entities/accounts/medical-record/medical-record.model';
import { MedicalRecordService } from 'app/entities/accounts/medical-record/service/medical-record.service';
import { IMedicalDocument } from '../medical-document.model';
import { MedicalDocumentService } from '../service/medical-document.service';
import { MedicalDocumentFormService, MedicalDocumentFormGroup } from './medical-document-form.service';

@Component({
  standalone: true,
  selector: 'jhi-medical-document-update',
  templateUrl: './medical-document-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicalDocumentUpdateComponent implements OnInit {
  isSaving = false;
  medicalDocument: IMedicalDocument | null = null;

  medicalRecordsSharedCollection: IMedicalRecord[] = [];

  protected medicalDocumentService = inject(MedicalDocumentService);
  protected medicalDocumentFormService = inject(MedicalDocumentFormService);
  protected medicalRecordService = inject(MedicalRecordService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicalDocumentFormGroup = this.medicalDocumentFormService.createMedicalDocumentFormGroup();

  compareMedicalRecord = (o1: IMedicalRecord | null, o2: IMedicalRecord | null): boolean =>
    this.medicalRecordService.compareMedicalRecord(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicalDocument }) => {
      this.medicalDocument = medicalDocument;
      if (medicalDocument) {
        this.updateForm(medicalDocument);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicalDocument = this.medicalDocumentFormService.getMedicalDocument(this.editForm);
    if (medicalDocument.id !== null) {
      this.subscribeToSaveResponse(this.medicalDocumentService.update(medicalDocument));
    } else {
      this.subscribeToSaveResponse(this.medicalDocumentService.create(medicalDocument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicalDocument>>): void {
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

  protected updateForm(medicalDocument: IMedicalDocument): void {
    this.medicalDocument = medicalDocument;
    this.medicalDocumentFormService.resetForm(this.editForm, medicalDocument);

    this.medicalRecordsSharedCollection = this.medicalRecordService.addMedicalRecordToCollectionIfMissing<IMedicalRecord>(
      this.medicalRecordsSharedCollection,
      medicalDocument.dossierMedical,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicalRecordService
      .query()
      .pipe(map((res: HttpResponse<IMedicalRecord[]>) => res.body ?? []))
      .pipe(
        map((medicalRecords: IMedicalRecord[]) =>
          this.medicalRecordService.addMedicalRecordToCollectionIfMissing<IMedicalRecord>(
            medicalRecords,
            this.medicalDocument?.dossierMedical,
          ),
        ),
      )
      .subscribe((medicalRecords: IMedicalRecord[]) => (this.medicalRecordsSharedCollection = medicalRecords));
  }
}
