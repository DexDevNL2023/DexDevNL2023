import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReclamation, NewReclamation } from '../reclamation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReclamation for edit and NewReclamationFormGroupInput for create.
 */
type ReclamationFormGroupInput = IReclamation | PartialWithRequiredKeyOf<NewReclamation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReclamation | NewReclamation> = Omit<T, 'dateReclamation'> & {
  dateReclamation?: string | null;
};

type ReclamationFormRawValue = FormValueOf<IReclamation>;

type NewReclamationFormRawValue = FormValueOf<NewReclamation>;

type ReclamationFormDefaults = Pick<NewReclamation, 'id' | 'dateReclamation'>;

type ReclamationFormGroupContent = {
  id: FormControl<ReclamationFormRawValue['id'] | NewReclamation['id']>;
  numeroReclamation: FormControl<ReclamationFormRawValue['numeroReclamation']>;
  dateReclamation: FormControl<ReclamationFormRawValue['dateReclamation']>;
  description: FormControl<ReclamationFormRawValue['description']>;
  status: FormControl<ReclamationFormRawValue['status']>;
  sinistre: FormControl<ReclamationFormRawValue['sinistre']>;
};

export type ReclamationFormGroup = FormGroup<ReclamationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReclamationFormService {
  createReclamationFormGroup(reclamation: ReclamationFormGroupInput = { id: null }): ReclamationFormGroup {
    const reclamationRawValue = this.convertReclamationToReclamationRawValue({
      ...this.getFormDefaults(),
      ...reclamation,
    });
    return new FormGroup<ReclamationFormGroupContent>({
      id: new FormControl(
        { value: reclamationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numeroReclamation: new FormControl(reclamationRawValue.numeroReclamation, {
        validators: [Validators.required],
      }),
      dateReclamation: new FormControl(reclamationRawValue.dateReclamation, {
        validators: [Validators.required],
      }),
      description: new FormControl(reclamationRawValue.description, {
        validators: [Validators.required],
      }),
      status: new FormControl(reclamationRawValue.status, {
        validators: [Validators.required],
      }),
      sinistre: new FormControl(reclamationRawValue.sinistre),
    });
  }

  getReclamation(form: ReclamationFormGroup): IReclamation | NewReclamation {
    return this.convertReclamationRawValueToReclamation(form.getRawValue() as ReclamationFormRawValue | NewReclamationFormRawValue);
  }

  resetForm(form: ReclamationFormGroup, reclamation: ReclamationFormGroupInput): void {
    const reclamationRawValue = this.convertReclamationToReclamationRawValue({ ...this.getFormDefaults(), ...reclamation });
    form.reset(
      {
        ...reclamationRawValue,
        id: { value: reclamationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReclamationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateReclamation: currentTime,
    };
  }

  private convertReclamationRawValueToReclamation(
    rawReclamation: ReclamationFormRawValue | NewReclamationFormRawValue,
  ): IReclamation | NewReclamation {
    return {
      ...rawReclamation,
      dateReclamation: dayjs(rawReclamation.dateReclamation, DATE_TIME_FORMAT),
    };
  }

  private convertReclamationToReclamationRawValue(
    reclamation: IReclamation | (Partial<NewReclamation> & ReclamationFormDefaults),
  ): ReclamationFormRawValue | PartialWithRequiredKeyOf<NewReclamationFormRawValue> {
    return {
      ...reclamation,
      dateReclamation: reclamation.dateReclamation ? reclamation.dateReclamation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
