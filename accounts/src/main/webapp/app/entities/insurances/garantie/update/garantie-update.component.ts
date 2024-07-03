import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAssurance } from 'app/entities/insurances/assurance/assurance.model';
import { AssuranceService } from 'app/entities/insurances/assurance/service/assurance.service';
import { GarantieStatus } from 'app/entities/enumerations/garantie-status.model';
import { GarantieService } from '../service/garantie.service';
import { IGarantie } from '../garantie.model';
import { GarantieFormService, GarantieFormGroup } from './garantie-form.service';

@Component({
  standalone: true,
  selector: 'jhi-garantie-update',
  templateUrl: './garantie-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GarantieUpdateComponent implements OnInit {
  isSaving = false;
  garantie: IGarantie | null = null;
  garantieStatusValues = Object.keys(GarantieStatus);

  assurancesSharedCollection: IAssurance[] = [];

  protected garantieService = inject(GarantieService);
  protected garantieFormService = inject(GarantieFormService);
  protected assuranceService = inject(AssuranceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GarantieFormGroup = this.garantieFormService.createGarantieFormGroup();

  compareAssurance = (o1: IAssurance | null, o2: IAssurance | null): boolean => this.assuranceService.compareAssurance(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ garantie }) => {
      this.garantie = garantie;
      if (garantie) {
        this.updateForm(garantie);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const garantie = this.garantieFormService.getGarantie(this.editForm);
    if (garantie.id !== null) {
      this.subscribeToSaveResponse(this.garantieService.update(garantie));
    } else {
      this.subscribeToSaveResponse(this.garantieService.create(garantie));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGarantie>>): void {
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

  protected updateForm(garantie: IGarantie): void {
    this.garantie = garantie;
    this.garantieFormService.resetForm(this.editForm, garantie);

    this.assurancesSharedCollection = this.assuranceService.addAssuranceToCollectionIfMissing<IAssurance>(
      this.assurancesSharedCollection,
      ...(garantie.assurances ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.assuranceService
      .query()
      .pipe(map((res: HttpResponse<IAssurance[]>) => res.body ?? []))
      .pipe(
        map((assurances: IAssurance[]) =>
          this.assuranceService.addAssuranceToCollectionIfMissing<IAssurance>(assurances, ...(this.garantie?.assurances ?? [])),
        ),
      )
      .subscribe((assurances: IAssurance[]) => (this.assurancesSharedCollection = assurances));
  }
}
