import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAssurance, NewAssurance } from '../assurance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssurance for edit and NewAssuranceFormGroupInput for create.
 */
type AssuranceFormGroupInput = IAssurance | PartialWithRequiredKeyOf<NewAssurance>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAssurance | NewAssurance> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

type AssuranceFormRawValue = FormValueOf<IAssurance>;

type NewAssuranceFormRawValue = FormValueOf<NewAssurance>;

type AssuranceFormDefaults = Pick<NewAssurance, 'id' | 'dateDebut' | 'dateFin' | 'garanties'>;

type AssuranceFormGroupContent = {
  id: FormControl<AssuranceFormRawValue['id'] | NewAssurance['id']>;
  nom: FormControl<AssuranceFormRawValue['nom']>;
  description: FormControl<AssuranceFormRawValue['description']>;
  conditions: FormControl<AssuranceFormRawValue['conditions']>;
  type: FormControl<AssuranceFormRawValue['type']>;
  prixAssurance: FormControl<AssuranceFormRawValue['prixAssurance']>;
  dateDebut: FormControl<AssuranceFormRawValue['dateDebut']>;
  dateFin: FormControl<AssuranceFormRawValue['dateFin']>;
  garanties: FormControl<AssuranceFormRawValue['garanties']>;
};

export type AssuranceFormGroup = FormGroup<AssuranceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssuranceFormService {
  createAssuranceFormGroup(assurance: AssuranceFormGroupInput = { id: null }): AssuranceFormGroup {
    const assuranceRawValue = this.convertAssuranceToAssuranceRawValue({
      ...this.getFormDefaults(),
      ...assurance,
    });
    return new FormGroup<AssuranceFormGroupContent>({
      id: new FormControl(
        { value: assuranceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(assuranceRawValue.nom, {
        validators: [Validators.required],
      }),
      description: new FormControl(assuranceRawValue.description),
      conditions: new FormControl(assuranceRawValue.conditions),
      type: new FormControl(assuranceRawValue.type, {
        validators: [Validators.required],
      }),
      prixAssurance: new FormControl(assuranceRawValue.prixAssurance, {
        validators: [Validators.required],
      }),
      dateDebut: new FormControl(assuranceRawValue.dateDebut, {
        validators: [Validators.required],
      }),
      dateFin: new FormControl(assuranceRawValue.dateFin, {
        validators: [Validators.required],
      }),
      garanties: new FormControl(assuranceRawValue.garanties ?? []),
    });
  }

  getAssurance(form: AssuranceFormGroup): IAssurance | NewAssurance {
    return this.convertAssuranceRawValueToAssurance(form.getRawValue() as AssuranceFormRawValue | NewAssuranceFormRawValue);
  }

  resetForm(form: AssuranceFormGroup, assurance: AssuranceFormGroupInput): void {
    const assuranceRawValue = this.convertAssuranceToAssuranceRawValue({ ...this.getFormDefaults(), ...assurance });
    form.reset(
      {
        ...assuranceRawValue,
        id: { value: assuranceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AssuranceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDebut: currentTime,
      dateFin: currentTime,
      garanties: [],
    };
  }

  private convertAssuranceRawValueToAssurance(rawAssurance: AssuranceFormRawValue | NewAssuranceFormRawValue): IAssurance | NewAssurance {
    return {
      ...rawAssurance,
      dateDebut: dayjs(rawAssurance.dateDebut, DATE_TIME_FORMAT),
      dateFin: dayjs(rawAssurance.dateFin, DATE_TIME_FORMAT),
    };
  }

  private convertAssuranceToAssuranceRawValue(
    assurance: IAssurance | (Partial<NewAssurance> & AssuranceFormDefaults),
  ): AssuranceFormRawValue | PartialWithRequiredKeyOf<NewAssuranceFormRawValue> {
    return {
      ...assurance,
      dateDebut: assurance.dateDebut ? assurance.dateDebut.format(DATE_TIME_FORMAT) : undefined,
      dateFin: assurance.dateFin ? assurance.dateFin.format(DATE_TIME_FORMAT) : undefined,
      garanties: assurance.garanties ?? [],
    };
  }
}
