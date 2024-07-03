import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPrestationSoin } from 'app/entities/claims/prestation-soin/prestation-soin.model';
import { PrestationSoinService } from 'app/entities/claims/prestation-soin/service/prestation-soin.service';
import { FinanceurType } from 'app/entities/enumerations/financeur-type.model';
import { FinanceurSoinService } from '../service/financeur-soin.service';
import { IFinanceurSoin } from '../financeur-soin.model';
import { FinanceurSoinFormService, FinanceurSoinFormGroup } from './financeur-soin-form.service';

@Component({
  standalone: true,
  selector: 'jhi-financeur-soin-update',
  templateUrl: './financeur-soin-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FinanceurSoinUpdateComponent implements OnInit {
  isSaving = false;
  financeurSoin: IFinanceurSoin | null = null;
  financeurTypeValues = Object.keys(FinanceurType);

  prestationSoinsSharedCollection: IPrestationSoin[] = [];

  protected financeurSoinService = inject(FinanceurSoinService);
  protected financeurSoinFormService = inject(FinanceurSoinFormService);
  protected prestationSoinService = inject(PrestationSoinService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FinanceurSoinFormGroup = this.financeurSoinFormService.createFinanceurSoinFormGroup();

  comparePrestationSoin = (o1: IPrestationSoin | null, o2: IPrestationSoin | null): boolean =>
    this.prestationSoinService.comparePrestationSoin(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ financeurSoin }) => {
      this.financeurSoin = financeurSoin;
      if (financeurSoin) {
        this.updateForm(financeurSoin);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const financeurSoin = this.financeurSoinFormService.getFinanceurSoin(this.editForm);
    if (financeurSoin.id !== null) {
      this.subscribeToSaveResponse(this.financeurSoinService.update(financeurSoin));
    } else {
      this.subscribeToSaveResponse(this.financeurSoinService.create(financeurSoin));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinanceurSoin>>): void {
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

  protected updateForm(financeurSoin: IFinanceurSoin): void {
    this.financeurSoin = financeurSoin;
    this.financeurSoinFormService.resetForm(this.editForm, financeurSoin);

    this.prestationSoinsSharedCollection = this.prestationSoinService.addPrestationSoinToCollectionIfMissing<IPrestationSoin>(
      this.prestationSoinsSharedCollection,
      ...(financeurSoin.prestationSoins ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.prestationSoinService
      .query()
      .pipe(map((res: HttpResponse<IPrestationSoin[]>) => res.body ?? []))
      .pipe(
        map((prestationSoins: IPrestationSoin[]) =>
          this.prestationSoinService.addPrestationSoinToCollectionIfMissing<IPrestationSoin>(
            prestationSoins,
            ...(this.financeurSoin?.prestationSoins ?? []),
          ),
        ),
      )
      .subscribe((prestationSoins: IPrestationSoin[]) => (this.prestationSoinsSharedCollection = prestationSoins));
  }
}
