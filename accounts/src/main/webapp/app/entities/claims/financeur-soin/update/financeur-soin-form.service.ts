import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFinanceurSoin, NewFinanceurSoin } from '../financeur-soin.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFinanceurSoin for edit and NewFinanceurSoinFormGroupInput for create.
 */
type FinanceurSoinFormGroupInput = IFinanceurSoin | PartialWithRequiredKeyOf<NewFinanceurSoin>;

type FinanceurSoinFormDefaults = Pick<NewFinanceurSoin, 'id' | 'prestationSoins'>;

type FinanceurSoinFormGroupContent = {
  id: FormControl<IFinanceurSoin['id'] | NewFinanceurSoin['id']>;
  nom: FormControl<IFinanceurSoin['nom']>;
  type: FormControl<IFinanceurSoin['type']>;
  adresse: FormControl<IFinanceurSoin['adresse']>;
  telephone: FormControl<IFinanceurSoin['telephone']>;
  email: FormControl<IFinanceurSoin['email']>;
  servicesFournis: FormControl<IFinanceurSoin['servicesFournis']>;
  prestationSoins: FormControl<IFinanceurSoin['prestationSoins']>;
};

export type FinanceurSoinFormGroup = FormGroup<FinanceurSoinFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FinanceurSoinFormService {
  createFinanceurSoinFormGroup(financeurSoin: FinanceurSoinFormGroupInput = { id: null }): FinanceurSoinFormGroup {
    const financeurSoinRawValue = {
      ...this.getFormDefaults(),
      ...financeurSoin,
    };
    return new FormGroup<FinanceurSoinFormGroupContent>({
      id: new FormControl(
        { value: financeurSoinRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(financeurSoinRawValue.nom, {
        validators: [Validators.required],
      }),
      type: new FormControl(financeurSoinRawValue.type, {
        validators: [Validators.required],
      }),
      adresse: new FormControl(financeurSoinRawValue.adresse, {
        validators: [Validators.required],
      }),
      telephone: new FormControl(financeurSoinRawValue.telephone, {
        validators: [Validators.required],
      }),
      email: new FormControl(financeurSoinRawValue.email, {
        validators: [Validators.required],
      }),
      servicesFournis: new FormControl(financeurSoinRawValue.servicesFournis, {
        validators: [Validators.required],
      }),
      prestationSoins: new FormControl(financeurSoinRawValue.prestationSoins ?? []),
    });
  }

  getFinanceurSoin(form: FinanceurSoinFormGroup): IFinanceurSoin | NewFinanceurSoin {
    return form.getRawValue() as IFinanceurSoin | NewFinanceurSoin;
  }

  resetForm(form: FinanceurSoinFormGroup, financeurSoin: FinanceurSoinFormGroupInput): void {
    const financeurSoinRawValue = { ...this.getFormDefaults(), ...financeurSoin };
    form.reset(
      {
        ...financeurSoinRawValue,
        id: { value: financeurSoinRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FinanceurSoinFormDefaults {
    return {
      id: null,
      prestationSoins: [],
    };
  }
}
