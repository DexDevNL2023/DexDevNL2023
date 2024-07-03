import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAssurance } from 'app/entities/insurances/assurance/assurance.model';
import { AssuranceService } from 'app/entities/insurances/assurance/service/assurance.service';
import { IPolicesAssurances } from '../polices-assurances.model';
import { PolicesAssurancesService } from '../service/polices-assurances.service';
import { PolicesAssurancesFormService, PolicesAssurancesFormGroup } from './polices-assurances-form.service';

@Component({
  standalone: true,
  selector: 'jhi-polices-assurances-update',
  templateUrl: './polices-assurances-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PolicesAssurancesUpdateComponent implements OnInit {
  isSaving = false;
  policesAssurances: IPolicesAssurances | null = null;

  assurancesSharedCollection: IAssurance[] = [];

  protected policesAssurancesService = inject(PolicesAssurancesService);
  protected policesAssurancesFormService = inject(PolicesAssurancesFormService);
  protected assuranceService = inject(AssuranceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PolicesAssurancesFormGroup = this.policesAssurancesFormService.createPolicesAssurancesFormGroup();

  compareAssurance = (o1: IAssurance | null, o2: IAssurance | null): boolean => this.assuranceService.compareAssurance(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ policesAssurances }) => {
      this.policesAssurances = policesAssurances;
      if (policesAssurances) {
        this.updateForm(policesAssurances);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const policesAssurances = this.policesAssurancesFormService.getPolicesAssurances(this.editForm);
    if (policesAssurances.id !== null) {
      this.subscribeToSaveResponse(this.policesAssurancesService.update(policesAssurances));
    } else {
      this.subscribeToSaveResponse(this.policesAssurancesService.create(policesAssurances));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPolicesAssurances>>): void {
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

  protected updateForm(policesAssurances: IPolicesAssurances): void {
    this.policesAssurances = policesAssurances;
    this.policesAssurancesFormService.resetForm(this.editForm, policesAssurances);

    this.assurancesSharedCollection = this.assuranceService.addAssuranceToCollectionIfMissing<IAssurance>(
      this.assurancesSharedCollection,
      policesAssurances.assurance,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.assuranceService
      .query()
      .pipe(map((res: HttpResponse<IAssurance[]>) => res.body ?? []))
      .pipe(
        map((assurances: IAssurance[]) =>
          this.assuranceService.addAssuranceToCollectionIfMissing<IAssurance>(assurances, this.policesAssurances?.assurance),
        ),
      )
      .subscribe((assurances: IAssurance[]) => (this.assurancesSharedCollection = assurances));
  }
}
