import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFinanceurSoin } from 'app/entities/claims/financeur-soin/financeur-soin.model';
import { FinanceurSoinService } from 'app/entities/claims/financeur-soin/service/financeur-soin.service';
import { PrestationType } from 'app/entities/enumerations/prestation-type.model';
import { PrestationSoinService } from '../service/prestation-soin.service';
import { IPrestationSoin } from '../prestation-soin.model';
import { PrestationSoinFormService, PrestationSoinFormGroup } from './prestation-soin-form.service';

@Component({
  standalone: true,
  selector: 'jhi-prestation-soin-update',
  templateUrl: './prestation-soin-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PrestationSoinUpdateComponent implements OnInit {
  isSaving = false;
  prestationSoin: IPrestationSoin | null = null;
  prestationTypeValues = Object.keys(PrestationType);

  financeurSoinsSharedCollection: IFinanceurSoin[] = [];

  protected prestationSoinService = inject(PrestationSoinService);
  protected prestationSoinFormService = inject(PrestationSoinFormService);
  protected financeurSoinService = inject(FinanceurSoinService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PrestationSoinFormGroup = this.prestationSoinFormService.createPrestationSoinFormGroup();

  compareFinanceurSoin = (o1: IFinanceurSoin | null, o2: IFinanceurSoin | null): boolean =>
    this.financeurSoinService.compareFinanceurSoin(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prestationSoin }) => {
      this.prestationSoin = prestationSoin;
      if (prestationSoin) {
        this.updateForm(prestationSoin);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prestationSoin = this.prestationSoinFormService.getPrestationSoin(this.editForm);
    if (prestationSoin.id !== null) {
      this.subscribeToSaveResponse(this.prestationSoinService.update(prestationSoin));
    } else {
      this.subscribeToSaveResponse(this.prestationSoinService.create(prestationSoin));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrestationSoin>>): void {
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

  protected updateForm(prestationSoin: IPrestationSoin): void {
    this.prestationSoin = prestationSoin;
    this.prestationSoinFormService.resetForm(this.editForm, prestationSoin);

    this.financeurSoinsSharedCollection = this.financeurSoinService.addFinanceurSoinToCollectionIfMissing<IFinanceurSoin>(
      this.financeurSoinsSharedCollection,
      prestationSoin.financeurSoin,
      ...(prestationSoin.financeurSoins ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.financeurSoinService
      .query()
      .pipe(map((res: HttpResponse<IFinanceurSoin[]>) => res.body ?? []))
      .pipe(
        map((financeurSoins: IFinanceurSoin[]) =>
          this.financeurSoinService.addFinanceurSoinToCollectionIfMissing<IFinanceurSoin>(
            financeurSoins,
            this.prestationSoin?.financeurSoin,
            ...(this.prestationSoin?.financeurSoins ?? []),
          ),
        ),
      )
      .subscribe((financeurSoins: IFinanceurSoin[]) => (this.financeurSoinsSharedCollection = financeurSoins));
  }
}
