import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISinistre, NewSinistre } from '../sinistre.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISinistre for edit and NewSinistreFormGroupInput for create.
 */
type SinistreFormGroupInput = ISinistre | PartialWithRequiredKeyOf<NewSinistre>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISinistre | NewSinistre> = Omit<T, 'dateDeclaration' | 'dateTraitement'> & {
  dateDeclaration?: string | null;
  dateTraitement?: string | null;
};

type SinistreFormRawValue = FormValueOf<ISinistre>;

type NewSinistreFormRawValue = FormValueOf<NewSinistre>;

type SinistreFormDefaults = Pick<NewSinistre, 'id' | 'dateDeclaration' | 'dateTraitement'>;

type SinistreFormGroupContent = {
  id: FormControl<SinistreFormRawValue['id'] | NewSinistre['id']>;
  numeroSinistre: FormControl<SinistreFormRawValue['numeroSinistre']>;
  dateDeclaration: FormControl<SinistreFormRawValue['dateDeclaration']>;
  dateTraitement: FormControl<SinistreFormRawValue['dateTraitement']>;
  status: FormControl<SinistreFormRawValue['status']>;
  raison: FormControl<SinistreFormRawValue['raison']>;
  montantReclame: FormControl<SinistreFormRawValue['montantReclame']>;
  montantApprouve: FormControl<SinistreFormRawValue['montantApprouve']>;
  souscriptionId: FormControl<SinistreFormRawValue['souscriptionId']>;
  fournisseur: FormControl<SinistreFormRawValue['fournisseur']>;
  prestationsSoin: FormControl<SinistreFormRawValue['prestationsSoin']>;
};

export type SinistreFormGroup = FormGroup<SinistreFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SinistreFormService {
  createSinistreFormGroup(sinistre: SinistreFormGroupInput = { id: null }): SinistreFormGroup {
    const sinistreRawValue = this.convertSinistreToSinistreRawValue({
      ...this.getFormDefaults(),
      ...sinistre,
    });
    return new FormGroup<SinistreFormGroupContent>({
      id: new FormControl(
        { value: sinistreRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numeroSinistre: new FormControl(sinistreRawValue.numeroSinistre, {
        validators: [Validators.required],
      }),
      dateDeclaration: new FormControl(sinistreRawValue.dateDeclaration, {
        validators: [Validators.required],
      }),
      dateTraitement: new FormControl(sinistreRawValue.dateTraitement),
      status: new FormControl(sinistreRawValue.status, {
        validators: [Validators.required],
      }),
      raison: new FormControl(sinistreRawValue.raison, {
        validators: [Validators.required],
      }),
      montantReclame: new FormControl(sinistreRawValue.montantReclame, {
        validators: [Validators.required],
      }),
      montantApprouve: new FormControl(sinistreRawValue.montantApprouve),
      souscriptionId: new FormControl(sinistreRawValue.souscriptionId, {
        validators: [Validators.required],
      }),
      fournisseur: new FormControl(sinistreRawValue.fournisseur),
      prestationsSoin: new FormControl(sinistreRawValue.prestationsSoin),
    });
  }

  getSinistre(form: SinistreFormGroup): ISinistre | NewSinistre {
    return this.convertSinistreRawValueToSinistre(form.getRawValue() as SinistreFormRawValue | NewSinistreFormRawValue);
  }

  resetForm(form: SinistreFormGroup, sinistre: SinistreFormGroupInput): void {
    const sinistreRawValue = this.convertSinistreToSinistreRawValue({ ...this.getFormDefaults(), ...sinistre });
    form.reset(
      {
        ...sinistreRawValue,
        id: { value: sinistreRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SinistreFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDeclaration: currentTime,
      dateTraitement: currentTime,
    };
  }

  private convertSinistreRawValueToSinistre(rawSinistre: SinistreFormRawValue | NewSinistreFormRawValue): ISinistre | NewSinistre {
    return {
      ...rawSinistre,
      dateDeclaration: dayjs(rawSinistre.dateDeclaration, DATE_TIME_FORMAT),
      dateTraitement: dayjs(rawSinistre.dateTraitement, DATE_TIME_FORMAT),
    };
  }

  private convertSinistreToSinistreRawValue(
    sinistre: ISinistre | (Partial<NewSinistre> & SinistreFormDefaults),
  ): SinistreFormRawValue | PartialWithRequiredKeyOf<NewSinistreFormRawValue> {
    return {
      ...sinistre,
      dateDeclaration: sinistre.dateDeclaration ? sinistre.dateDeclaration.format(DATE_TIME_FORMAT) : undefined,
      dateTraitement: sinistre.dateTraitement ? sinistre.dateTraitement.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
