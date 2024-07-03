import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IGarantie } from 'app/entities/insurances/garantie/garantie.model';
import { GarantieService } from 'app/entities/insurances/garantie/service/garantie.service';
import { InsuranceType } from 'app/entities/enumerations/insurance-type.model';
import { AssuranceService } from '../service/assurance.service';
import { IAssurance } from '../assurance.model';
import { AssuranceFormService, AssuranceFormGroup } from './assurance-form.service';

@Component({
  standalone: true,
  selector: 'jhi-assurance-update',
  templateUrl: './assurance-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AssuranceUpdateComponent implements OnInit {
  isSaving = false;
  assurance: IAssurance | null = null;
  insuranceTypeValues = Object.keys(InsuranceType);

  garantiesSharedCollection: IGarantie[] = [];

  protected assuranceService = inject(AssuranceService);
  protected assuranceFormService = inject(AssuranceFormService);
  protected garantieService = inject(GarantieService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AssuranceFormGroup = this.assuranceFormService.createAssuranceFormGroup();

  compareGarantie = (o1: IGarantie | null, o2: IGarantie | null): boolean => this.garantieService.compareGarantie(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assurance }) => {
      this.assurance = assurance;
      if (assurance) {
        this.updateForm(assurance);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assurance = this.assuranceFormService.getAssurance(this.editForm);
    if (assurance.id !== null) {
      this.subscribeToSaveResponse(this.assuranceService.update(assurance));
    } else {
      this.subscribeToSaveResponse(this.assuranceService.create(assurance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssurance>>): void {
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

  protected updateForm(assurance: IAssurance): void {
    this.assurance = assurance;
    this.assuranceFormService.resetForm(this.editForm, assurance);

    this.garantiesSharedCollection = this.garantieService.addGarantieToCollectionIfMissing<IGarantie>(
      this.garantiesSharedCollection,
      ...(assurance.garanties ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.garantieService
      .query()
      .pipe(map((res: HttpResponse<IGarantie[]>) => res.body ?? []))
      .pipe(
        map((garanties: IGarantie[]) =>
          this.garantieService.addGarantieToCollectionIfMissing<IGarantie>(garanties, ...(this.assurance?.garanties ?? [])),
        ),
      )
      .subscribe((garanties: IGarantie[]) => (this.garantiesSharedCollection = garanties));
  }
}
