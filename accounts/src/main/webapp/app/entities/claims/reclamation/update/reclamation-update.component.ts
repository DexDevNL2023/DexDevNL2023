import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISinistre } from 'app/entities/claims/sinistre/sinistre.model';
import { SinistreService } from 'app/entities/claims/sinistre/service/sinistre.service';
import { ReclamationStatus } from 'app/entities/enumerations/reclamation-status.model';
import { ReclamationService } from '../service/reclamation.service';
import { IReclamation } from '../reclamation.model';
import { ReclamationFormService, ReclamationFormGroup } from './reclamation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-reclamation-update',
  templateUrl: './reclamation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReclamationUpdateComponent implements OnInit {
  isSaving = false;
  reclamation: IReclamation | null = null;
  reclamationStatusValues = Object.keys(ReclamationStatus);

  sinistresSharedCollection: ISinistre[] = [];

  protected reclamationService = inject(ReclamationService);
  protected reclamationFormService = inject(ReclamationFormService);
  protected sinistreService = inject(SinistreService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReclamationFormGroup = this.reclamationFormService.createReclamationFormGroup();

  compareSinistre = (o1: ISinistre | null, o2: ISinistre | null): boolean => this.sinistreService.compareSinistre(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reclamation }) => {
      this.reclamation = reclamation;
      if (reclamation) {
        this.updateForm(reclamation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reclamation = this.reclamationFormService.getReclamation(this.editForm);
    if (reclamation.id !== null) {
      this.subscribeToSaveResponse(this.reclamationService.update(reclamation));
    } else {
      this.subscribeToSaveResponse(this.reclamationService.create(reclamation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReclamation>>): void {
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

  protected updateForm(reclamation: IReclamation): void {
    this.reclamation = reclamation;
    this.reclamationFormService.resetForm(this.editForm, reclamation);

    this.sinistresSharedCollection = this.sinistreService.addSinistreToCollectionIfMissing<ISinistre>(
      this.sinistresSharedCollection,
      reclamation.sinistre,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sinistreService
      .query()
      .pipe(map((res: HttpResponse<ISinistre[]>) => res.body ?? []))
      .pipe(
        map((sinistres: ISinistre[]) =>
          this.sinistreService.addSinistreToCollectionIfMissing<ISinistre>(sinistres, this.reclamation?.sinistre),
        ),
      )
      .subscribe((sinistres: ISinistre[]) => (this.sinistresSharedCollection = sinistres));
  }
}
