import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAssurance } from 'app/entities/insurances/assurance/assurance.model';
import { AssuranceService } from 'app/entities/insurances/assurance/service/assurance.service';
import { RapportType } from 'app/entities/enumerations/rapport-type.model';
import { RapportService } from '../service/rapport.service';
import { IRapport } from '../rapport.model';
import { RapportFormService, RapportFormGroup } from './rapport-form.service';

@Component({
  standalone: true,
  selector: 'jhi-rapport-update',
  templateUrl: './rapport-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RapportUpdateComponent implements OnInit {
  isSaving = false;
  rapport: IRapport | null = null;
  rapportTypeValues = Object.keys(RapportType);

  assurancesSharedCollection: IAssurance[] = [];

  protected rapportService = inject(RapportService);
  protected rapportFormService = inject(RapportFormService);
  protected assuranceService = inject(AssuranceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RapportFormGroup = this.rapportFormService.createRapportFormGroup();

  compareAssurance = (o1: IAssurance | null, o2: IAssurance | null): boolean => this.assuranceService.compareAssurance(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rapport }) => {
      this.rapport = rapport;
      if (rapport) {
        this.updateForm(rapport);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rapport = this.rapportFormService.getRapport(this.editForm);
    if (rapport.id !== null) {
      this.subscribeToSaveResponse(this.rapportService.update(rapport));
    } else {
      this.subscribeToSaveResponse(this.rapportService.create(rapport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRapport>>): void {
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

  protected updateForm(rapport: IRapport): void {
    this.rapport = rapport;
    this.rapportFormService.resetForm(this.editForm, rapport);

    this.assurancesSharedCollection = this.assuranceService.addAssuranceToCollectionIfMissing<IAssurance>(
      this.assurancesSharedCollection,
      rapport.assurance,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.assuranceService
      .query()
      .pipe(map((res: HttpResponse<IAssurance[]>) => res.body ?? []))
      .pipe(
        map((assurances: IAssurance[]) =>
          this.assuranceService.addAssuranceToCollectionIfMissing<IAssurance>(assurances, this.rapport?.assurance),
        ),
      )
      .subscribe((assurances: IAssurance[]) => (this.assurancesSharedCollection = assurances));
  }
}
