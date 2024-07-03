import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPolicesAssurances } from 'app/entities/insurances/polices-assurances/polices-assurances.model';
import { PolicesAssurancesService } from 'app/entities/insurances/polices-assurances/service/polices-assurances.service';
import { SubscriptionStatus } from 'app/entities/enumerations/subscription-status.model';
import { PaymentFrequency } from 'app/entities/enumerations/payment-frequency.model';
import { SouscriptionService } from '../service/souscription.service';
import { ISouscription } from '../souscription.model';
import { SouscriptionFormService, SouscriptionFormGroup } from './souscription-form.service';

@Component({
  standalone: true,
  selector: 'jhi-souscription-update',
  templateUrl: './souscription-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SouscriptionUpdateComponent implements OnInit {
  isSaving = false;
  souscription: ISouscription | null = null;
  subscriptionStatusValues = Object.keys(SubscriptionStatus);
  paymentFrequencyValues = Object.keys(PaymentFrequency);

  policesAssurancesSharedCollection: IPolicesAssurances[] = [];

  protected souscriptionService = inject(SouscriptionService);
  protected souscriptionFormService = inject(SouscriptionFormService);
  protected policesAssurancesService = inject(PolicesAssurancesService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SouscriptionFormGroup = this.souscriptionFormService.createSouscriptionFormGroup();

  comparePolicesAssurances = (o1: IPolicesAssurances | null, o2: IPolicesAssurances | null): boolean =>
    this.policesAssurancesService.comparePolicesAssurances(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ souscription }) => {
      this.souscription = souscription;
      if (souscription) {
        this.updateForm(souscription);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const souscription = this.souscriptionFormService.getSouscription(this.editForm);
    if (souscription.id !== null) {
      this.subscribeToSaveResponse(this.souscriptionService.update(souscription));
    } else {
      this.subscribeToSaveResponse(this.souscriptionService.create(souscription));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISouscription>>): void {
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

  protected updateForm(souscription: ISouscription): void {
    this.souscription = souscription;
    this.souscriptionFormService.resetForm(this.editForm, souscription);

    this.policesAssurancesSharedCollection = this.policesAssurancesService.addPolicesAssurancesToCollectionIfMissing<IPolicesAssurances>(
      this.policesAssurancesSharedCollection,
      souscription.policesAssurances,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.policesAssurancesService
      .query()
      .pipe(map((res: HttpResponse<IPolicesAssurances[]>) => res.body ?? []))
      .pipe(
        map((policesAssurances: IPolicesAssurances[]) =>
          this.policesAssurancesService.addPolicesAssurancesToCollectionIfMissing<IPolicesAssurances>(
            policesAssurances,
            this.souscription?.policesAssurances,
          ),
        ),
      )
      .subscribe((policesAssurances: IPolicesAssurances[]) => (this.policesAssurancesSharedCollection = policesAssurances));
  }
}
