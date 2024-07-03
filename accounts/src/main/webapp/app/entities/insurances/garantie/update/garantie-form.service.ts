import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IGarantie, NewGarantie } from '../garantie.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGarantie for edit and NewGarantieFormGroupInput for create.
 */
type GarantieFormGroupInput = IGarantie | PartialWithRequiredKeyOf<NewGarantie>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IGarantie | NewGarantie> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

type GarantieFormRawValue = FormValueOf<IGarantie>;

type NewGarantieFormRawValue = FormValueOf<NewGarantie>;

type GarantieFormDefaults = Pick<NewGarantie, 'id' | 'dateDebut' | 'dateFin' | 'assurances'>;

type GarantieFormGroupContent = {
  id: FormControl<GarantieFormRawValue['id'] | NewGarantie['id']>;
  description: FormControl<GarantieFormRawValue['description']>;
  dateDebut: FormControl<GarantieFormRawValue['dateDebut']>;
  dateFin: FormControl<GarantieFormRawValue['dateFin']>;
  status: FormControl<GarantieFormRawValue['status']>;
  assurances: FormControl<GarantieFormRawValue['assurances']>;
};

export type GarantieFormGroup = FormGroup<GarantieFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GarantieFormService {
  createGarantieFormGroup(garantie: GarantieFormGroupInput = { id: null }): GarantieFormGroup {
    const garantieRawValue = this.convertGarantieToGarantieRawValue({
      ...this.getFormDefaults(),
      ...garantie,
    });
    return new FormGroup<GarantieFormGroupContent>({
      id: new FormControl(
        { value: garantieRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(garantieRawValue.description, {
        validators: [Validators.required],
      }),
      dateDebut: new FormControl(garantieRawValue.dateDebut, {
        validators: [Validators.required],
      }),
      dateFin: new FormControl(garantieRawValue.dateFin, {
        validators: [Validators.required],
      }),
      status: new FormControl(garantieRawValue.status, {
        validators: [Validators.required],
      }),
      assurances: new FormControl(garantieRawValue.assurances ?? []),
    });
  }

  getGarantie(form: GarantieFormGroup): IGarantie | NewGarantie {
    return this.convertGarantieRawValueToGarantie(form.getRawValue() as GarantieFormRawValue | NewGarantieFormRawValue);
  }

  resetForm(form: GarantieFormGroup, garantie: GarantieFormGroupInput): void {
    const garantieRawValue = this.convertGarantieToGarantieRawValue({ ...this.getFormDefaults(), ...garantie });
    form.reset(
      {
        ...garantieRawValue,
        id: { value: garantieRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): GarantieFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDebut: currentTime,
      dateFin: currentTime,
      assurances: [],
    };
  }

  private convertGarantieRawValueToGarantie(rawGarantie: GarantieFormRawValue | NewGarantieFormRawValue): IGarantie | NewGarantie {
    return {
      ...rawGarantie,
      dateDebut: dayjs(rawGarantie.dateDebut, DATE_TIME_FORMAT),
      dateFin: dayjs(rawGarantie.dateFin, DATE_TIME_FORMAT),
    };
  }

  private convertGarantieToGarantieRawValue(
    garantie: IGarantie | (Partial<NewGarantie> & GarantieFormDefaults),
  ): GarantieFormRawValue | PartialWithRequiredKeyOf<NewGarantieFormRawValue> {
    return {
      ...garantie,
      dateDebut: garantie.dateDebut ? garantie.dateDebut.format(DATE_TIME_FORMAT) : undefined,
      dateFin: garantie.dateFin ? garantie.dateFin.format(DATE_TIME_FORMAT) : undefined,
      assurances: garantie.assurances ?? [],
    };
  }
}
