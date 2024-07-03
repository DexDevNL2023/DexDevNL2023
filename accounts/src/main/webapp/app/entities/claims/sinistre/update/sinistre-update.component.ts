import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFournisseur } from 'app/entities/claims/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/claims/fournisseur/service/fournisseur.service';
import { IPrestationSoin } from 'app/entities/claims/prestation-soin/prestation-soin.model';
import { PrestationSoinService } from 'app/entities/claims/prestation-soin/service/prestation-soin.service';
import { ClaimStatus } from 'app/entities/enumerations/claim-status.model';
import { SinistreService } from '../service/sinistre.service';
import { ISinistre } from '../sinistre.model';
import { SinistreFormService, SinistreFormGroup } from './sinistre-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sinistre-update',
  templateUrl: './sinistre-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SinistreUpdateComponent implements OnInit {
  isSaving = false;
  sinistre: ISinistre | null = null;
  claimStatusValues = Object.keys(ClaimStatus);

  fournisseursSharedCollection: IFournisseur[] = [];
  prestationSoinsSharedCollection: IPrestationSoin[] = [];

  protected sinistreService = inject(SinistreService);
  protected sinistreFormService = inject(SinistreFormService);
  protected fournisseurService = inject(FournisseurService);
  protected prestationSoinService = inject(PrestationSoinService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SinistreFormGroup = this.sinistreFormService.createSinistreFormGroup();

  compareFournisseur = (o1: IFournisseur | null, o2: IFournisseur | null): boolean => this.fournisseurService.compareFournisseur(o1, o2);

  comparePrestationSoin = (o1: IPrestationSoin | null, o2: IPrestationSoin | null): boolean =>
    this.prestationSoinService.comparePrestationSoin(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sinistre }) => {
      this.sinistre = sinistre;
      if (sinistre) {
        this.updateForm(sinistre);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sinistre = this.sinistreFormService.getSinistre(this.editForm);
    if (sinistre.id !== null) {
      this.subscribeToSaveResponse(this.sinistreService.update(sinistre));
    } else {
      this.subscribeToSaveResponse(this.sinistreService.create(sinistre));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISinistre>>): void {
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

  protected updateForm(sinistre: ISinistre): void {
    this.sinistre = sinistre;
    this.sinistreFormService.resetForm(this.editForm, sinistre);

    this.fournisseursSharedCollection = this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(
      this.fournisseursSharedCollection,
      sinistre.fournisseur,
    );
    this.prestationSoinsSharedCollection = this.prestationSoinService.addPrestationSoinToCollectionIfMissing<IPrestationSoin>(
      this.prestationSoinsSharedCollection,
      sinistre.prestationsSoin,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fournisseurService
      .query()
      .pipe(map((res: HttpResponse<IFournisseur[]>) => res.body ?? []))
      .pipe(
        map((fournisseurs: IFournisseur[]) =>
          this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(fournisseurs, this.sinistre?.fournisseur),
        ),
      )
      .subscribe((fournisseurs: IFournisseur[]) => (this.fournisseursSharedCollection = fournisseurs));

    this.prestationSoinService
      .query()
      .pipe(map((res: HttpResponse<IPrestationSoin[]>) => res.body ?? []))
      .pipe(
        map((prestationSoins: IPrestationSoin[]) =>
          this.prestationSoinService.addPrestationSoinToCollectionIfMissing<IPrestationSoin>(
            prestationSoins,
            this.sinistre?.prestationsSoin,
          ),
        ),
      )
      .subscribe((prestationSoins: IPrestationSoin[]) => (this.prestationSoinsSharedCollection = prestationSoins));
  }
}
