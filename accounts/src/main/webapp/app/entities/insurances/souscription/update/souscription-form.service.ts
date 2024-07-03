import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISouscription, NewSouscription } from '../souscription.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISouscription for edit and NewSouscriptionFormGroupInput for create.
 */
type SouscriptionFormGroupInput = ISouscription | PartialWithRequiredKeyOf<NewSouscription>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISouscription | NewSouscription> = Omit<T, 'dateSouscription'> & {
  dateSouscription?: string | null;
};

type SouscriptionFormRawValue = FormValueOf<ISouscription>;

type NewSouscriptionFormRawValue = FormValueOf<NewSouscription>;

type SouscriptionFormDefaults = Pick<NewSouscription, 'id' | 'dateSouscription'>;

type SouscriptionFormGroupContent = {
  id: FormControl<SouscriptionFormRawValue['id'] | NewSouscription['id']>;
  numeroSouscription: FormControl<SouscriptionFormRawValue['numeroSouscription']>;
  dateSouscription: FormControl<SouscriptionFormRawValue['dateSouscription']>;
  status: FormControl<SouscriptionFormRawValue['status']>;
  cumulSinistres: FormControl<SouscriptionFormRawValue['cumulSinistres']>;
  frequencePaiement: FormControl<SouscriptionFormRawValue['frequencePaiement']>;
  utilisateurId: FormControl<SouscriptionFormRawValue['utilisateurId']>;
  policesAssurances: FormControl<SouscriptionFormRawValue['policesAssurances']>;
};

export type SouscriptionFormGroup = FormGroup<SouscriptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SouscriptionFormService {
  createSouscriptionFormGroup(souscription: SouscriptionFormGroupInput = { id: null }): SouscriptionFormGroup {
    const souscriptionRawValue = this.convertSouscriptionToSouscriptionRawValue({
      ...this.getFormDefaults(),
      ...souscription,
    });
    return new FormGroup<SouscriptionFormGroupContent>({
      id: new FormControl(
        { value: souscriptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numeroSouscription: new FormControl(souscriptionRawValue.numeroSouscription, {
        validators: [Validators.required],
      }),
      dateSouscription: new FormControl(souscriptionRawValue.dateSouscription, {
        validators: [Validators.required],
      }),
      status: new FormControl(souscriptionRawValue.status, {
        validators: [Validators.required],
      }),
      cumulSinistres: new FormControl(souscriptionRawValue.cumulSinistres, {
        validators: [Validators.required],
      }),
      frequencePaiement: new FormControl(souscriptionRawValue.frequencePaiement, {
        validators: [Validators.required],
      }),
      utilisateurId: new FormControl(souscriptionRawValue.utilisateurId, {
        validators: [Validators.required],
      }),
      policesAssurances: new FormControl(souscriptionRawValue.policesAssurances),
    });
  }

  getSouscription(form: SouscriptionFormGroup): ISouscription | NewSouscription {
    return this.convertSouscriptionRawValueToSouscription(form.getRawValue() as SouscriptionFormRawValue | NewSouscriptionFormRawValue);
  }

  resetForm(form: SouscriptionFormGroup, souscription: SouscriptionFormGroupInput): void {
    const souscriptionRawValue = this.convertSouscriptionToSouscriptionRawValue({ ...this.getFormDefaults(), ...souscription });
    form.reset(
      {
        ...souscriptionRawValue,
        id: { value: souscriptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SouscriptionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateSouscription: currentTime,
    };
  }

  private convertSouscriptionRawValueToSouscription(
    rawSouscription: SouscriptionFormRawValue | NewSouscriptionFormRawValue,
  ): ISouscription | NewSouscription {
    return {
      ...rawSouscription,
      dateSouscription: dayjs(rawSouscription.dateSouscription, DATE_TIME_FORMAT),
    };
  }

  private convertSouscriptionToSouscriptionRawValue(
    souscription: ISouscription | (Partial<NewSouscription> & SouscriptionFormDefaults),
  ): SouscriptionFormRawValue | PartialWithRequiredKeyOf<NewSouscriptionFormRawValue> {
    return {
      ...souscription,
      dateSouscription: souscription.dateSouscription ? souscription.dateSouscription.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
