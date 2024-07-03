import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPrestationSoin, NewPrestationSoin } from '../prestation-soin.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPrestationSoin for edit and NewPrestationSoinFormGroupInput for create.
 */
type PrestationSoinFormGroupInput = IPrestationSoin | PartialWithRequiredKeyOf<NewPrestationSoin>;

type PrestationSoinFormDefaults = Pick<NewPrestationSoin, 'id' | 'financeurSoins'>;

type PrestationSoinFormGroupContent = {
  id: FormControl<IPrestationSoin['id'] | NewPrestationSoin['id']>;
  nom: FormControl<IPrestationSoin['nom']>;
  description: FormControl<IPrestationSoin['description']>;
  type: FormControl<IPrestationSoin['type']>;
  montant: FormControl<IPrestationSoin['montant']>;
  financeurSoin: FormControl<IPrestationSoin['financeurSoin']>;
  financeurSoins: FormControl<IPrestationSoin['financeurSoins']>;
};

export type PrestationSoinFormGroup = FormGroup<PrestationSoinFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PrestationSoinFormService {
  createPrestationSoinFormGroup(prestationSoin: PrestationSoinFormGroupInput = { id: null }): PrestationSoinFormGroup {
    const prestationSoinRawValue = {
      ...this.getFormDefaults(),
      ...prestationSoin,
    };
    return new FormGroup<PrestationSoinFormGroupContent>({
      id: new FormControl(
        { value: prestationSoinRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(prestationSoinRawValue.nom, {
        validators: [Validators.required],
      }),
      description: new FormControl(prestationSoinRawValue.description),
      type: new FormControl(prestationSoinRawValue.type, {
        validators: [Validators.required],
      }),
      montant: new FormControl(prestationSoinRawValue.montant, {
        validators: [Validators.required],
      }),
      financeurSoin: new FormControl(prestationSoinRawValue.financeurSoin),
      financeurSoins: new FormControl(prestationSoinRawValue.financeurSoins ?? []),
    });
  }

  getPrestationSoin(form: PrestationSoinFormGroup): IPrestationSoin | NewPrestationSoin {
    return form.getRawValue() as IPrestationSoin | NewPrestationSoin;
  }

  resetForm(form: PrestationSoinFormGroup, prestationSoin: PrestationSoinFormGroupInput): void {
    const prestationSoinRawValue = { ...this.getFormDefaults(), ...prestationSoin };
    form.reset(
      {
        ...prestationSoinRawValue,
        id: { value: prestationSoinRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PrestationSoinFormDefaults {
    return {
      id: null,
      financeurSoins: [],
    };
  }
}
