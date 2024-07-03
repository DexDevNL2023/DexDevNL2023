import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPolicesAssurances, NewPolicesAssurances } from '../polices-assurances.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPolicesAssurances for edit and NewPolicesAssurancesFormGroupInput for create.
 */
type PolicesAssurancesFormGroupInput = IPolicesAssurances | PartialWithRequiredKeyOf<NewPolicesAssurances>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPolicesAssurances | NewPolicesAssurances> = Omit<T, 'dateEmission' | 'dateExpiration'> & {
  dateEmission?: string | null;
  dateExpiration?: string | null;
};

type PolicesAssurancesFormRawValue = FormValueOf<IPolicesAssurances>;

type NewPolicesAssurancesFormRawValue = FormValueOf<NewPolicesAssurances>;

type PolicesAssurancesFormDefaults = Pick<NewPolicesAssurances, 'id' | 'dateEmission' | 'dateExpiration'>;

type PolicesAssurancesFormGroupContent = {
  id: FormControl<PolicesAssurancesFormRawValue['id'] | NewPolicesAssurances['id']>;
  numeroPolice: FormControl<PolicesAssurancesFormRawValue['numeroPolice']>;
  dateEmission: FormControl<PolicesAssurancesFormRawValue['dateEmission']>;
  dateExpiration: FormControl<PolicesAssurancesFormRawValue['dateExpiration']>;
  description: FormControl<PolicesAssurancesFormRawValue['description']>;
  montantAssure: FormControl<PolicesAssurancesFormRawValue['montantAssure']>;
  plafondAssure: FormControl<PolicesAssurancesFormRawValue['plafondAssure']>;
  assurance: FormControl<PolicesAssurancesFormRawValue['assurance']>;
};

export type PolicesAssurancesFormGroup = FormGroup<PolicesAssurancesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PolicesAssurancesFormService {
  createPolicesAssurancesFormGroup(policesAssurances: PolicesAssurancesFormGroupInput = { id: null }): PolicesAssurancesFormGroup {
    const policesAssurancesRawValue = this.convertPolicesAssurancesToPolicesAssurancesRawValue({
      ...this.getFormDefaults(),
      ...policesAssurances,
    });
    return new FormGroup<PolicesAssurancesFormGroupContent>({
      id: new FormControl(
        { value: policesAssurancesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numeroPolice: new FormControl(policesAssurancesRawValue.numeroPolice, {
        validators: [Validators.required],
      }),
      dateEmission: new FormControl(policesAssurancesRawValue.dateEmission, {
        validators: [Validators.required],
      }),
      dateExpiration: new FormControl(policesAssurancesRawValue.dateExpiration, {
        validators: [Validators.required],
      }),
      description: new FormControl(policesAssurancesRawValue.description),
      montantAssure: new FormControl(policesAssurancesRawValue.montantAssure, {
        validators: [Validators.required],
      }),
      plafondAssure: new FormControl(policesAssurancesRawValue.plafondAssure, {
        validators: [Validators.required],
      }),
      assurance: new FormControl(policesAssurancesRawValue.assurance),
    });
  }

  getPolicesAssurances(form: PolicesAssurancesFormGroup): IPolicesAssurances | NewPolicesAssurances {
    return this.convertPolicesAssurancesRawValueToPolicesAssurances(
      form.getRawValue() as PolicesAssurancesFormRawValue | NewPolicesAssurancesFormRawValue,
    );
  }

  resetForm(form: PolicesAssurancesFormGroup, policesAssurances: PolicesAssurancesFormGroupInput): void {
    const policesAssurancesRawValue = this.convertPolicesAssurancesToPolicesAssurancesRawValue({
      ...this.getFormDefaults(),
      ...policesAssurances,
    });
    form.reset(
      {
        ...policesAssurancesRawValue,
        id: { value: policesAssurancesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PolicesAssurancesFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateEmission: currentTime,
      dateExpiration: currentTime,
    };
  }

  private convertPolicesAssurancesRawValueToPolicesAssurances(
    rawPolicesAssurances: PolicesAssurancesFormRawValue | NewPolicesAssurancesFormRawValue,
  ): IPolicesAssurances | NewPolicesAssurances {
    return {
      ...rawPolicesAssurances,
      dateEmission: dayjs(rawPolicesAssurances.dateEmission, DATE_TIME_FORMAT),
      dateExpiration: dayjs(rawPolicesAssurances.dateExpiration, DATE_TIME_FORMAT),
    };
  }

  private convertPolicesAssurancesToPolicesAssurancesRawValue(
    policesAssurances: IPolicesAssurances | (Partial<NewPolicesAssurances> & PolicesAssurancesFormDefaults),
  ): PolicesAssurancesFormRawValue | PartialWithRequiredKeyOf<NewPolicesAssurancesFormRawValue> {
    return {
      ...policesAssurances,
      dateEmission: policesAssurances.dateEmission ? policesAssurances.dateEmission.format(DATE_TIME_FORMAT) : undefined,
      dateExpiration: policesAssurances.dateExpiration ? policesAssurances.dateExpiration.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
