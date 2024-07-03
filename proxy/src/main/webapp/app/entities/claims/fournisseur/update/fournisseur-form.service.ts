import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFournisseur, NewFournisseur } from '../fournisseur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFournisseur for edit and NewFournisseurFormGroupInput for create.
 */
type FournisseurFormGroupInput = IFournisseur | PartialWithRequiredKeyOf<NewFournisseur>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFournisseur | NewFournisseur> = Omit<T, 'dateDebutContrat' | 'dateFinContrat'> & {
  dateDebutContrat?: string | null;
  dateFinContrat?: string | null;
};

type FournisseurFormRawValue = FormValueOf<IFournisseur>;

type NewFournisseurFormRawValue = FormValueOf<NewFournisseur>;

type FournisseurFormDefaults = Pick<NewFournisseur, 'id' | 'dateDebutContrat' | 'dateFinContrat'>;

type FournisseurFormGroupContent = {
  id: FormControl<FournisseurFormRawValue['id'] | NewFournisseur['id']>;
  nom: FormControl<FournisseurFormRawValue['nom']>;
  adresse: FormControl<FournisseurFormRawValue['adresse']>;
  telephone: FormControl<FournisseurFormRawValue['telephone']>;
  email: FormControl<FournisseurFormRawValue['email']>;
  addresse: FormControl<FournisseurFormRawValue['addresse']>;
  ville: FormControl<FournisseurFormRawValue['ville']>;
  pays: FormControl<FournisseurFormRawValue['pays']>;
  servicesFournis: FormControl<FournisseurFormRawValue['servicesFournis']>;
  dateDebutContrat: FormControl<FournisseurFormRawValue['dateDebutContrat']>;
  dateFinContrat: FormControl<FournisseurFormRawValue['dateFinContrat']>;
};

export type FournisseurFormGroup = FormGroup<FournisseurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FournisseurFormService {
  createFournisseurFormGroup(fournisseur: FournisseurFormGroupInput = { id: null }): FournisseurFormGroup {
    const fournisseurRawValue = this.convertFournisseurToFournisseurRawValue({
      ...this.getFormDefaults(),
      ...fournisseur,
    });
    return new FormGroup<FournisseurFormGroupContent>({
      id: new FormControl(
        { value: fournisseurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(fournisseurRawValue.nom, {
        validators: [Validators.required],
      }),
      adresse: new FormControl(fournisseurRawValue.adresse, {
        validators: [Validators.required],
      }),
      telephone: new FormControl(fournisseurRawValue.telephone, {
        validators: [Validators.required],
      }),
      email: new FormControl(fournisseurRawValue.email, {
        validators: [Validators.required],
      }),
      addresse: new FormControl(fournisseurRawValue.addresse, {
        validators: [Validators.required],
      }),
      ville: new FormControl(fournisseurRawValue.ville, {
        validators: [Validators.required],
      }),
      pays: new FormControl(fournisseurRawValue.pays, {
        validators: [Validators.required],
      }),
      servicesFournis: new FormControl(fournisseurRawValue.servicesFournis, {
        validators: [Validators.required],
      }),
      dateDebutContrat: new FormControl(fournisseurRawValue.dateDebutContrat, {
        validators: [Validators.required],
      }),
      dateFinContrat: new FormControl(fournisseurRawValue.dateFinContrat),
    });
  }

  getFournisseur(form: FournisseurFormGroup): IFournisseur | NewFournisseur {
    return this.convertFournisseurRawValueToFournisseur(form.getRawValue() as FournisseurFormRawValue | NewFournisseurFormRawValue);
  }

  resetForm(form: FournisseurFormGroup, fournisseur: FournisseurFormGroupInput): void {
    const fournisseurRawValue = this.convertFournisseurToFournisseurRawValue({ ...this.getFormDefaults(), ...fournisseur });
    form.reset(
      {
        ...fournisseurRawValue,
        id: { value: fournisseurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FournisseurFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDebutContrat: currentTime,
      dateFinContrat: currentTime,
    };
  }

  private convertFournisseurRawValueToFournisseur(
    rawFournisseur: FournisseurFormRawValue | NewFournisseurFormRawValue,
  ): IFournisseur | NewFournisseur {
    return {
      ...rawFournisseur,
      dateDebutContrat: dayjs(rawFournisseur.dateDebutContrat, DATE_TIME_FORMAT),
      dateFinContrat: dayjs(rawFournisseur.dateFinContrat, DATE_TIME_FORMAT),
    };
  }

  private convertFournisseurToFournisseurRawValue(
    fournisseur: IFournisseur | (Partial<NewFournisseur> & FournisseurFormDefaults),
  ): FournisseurFormRawValue | PartialWithRequiredKeyOf<NewFournisseurFormRawValue> {
    return {
      ...fournisseur,
      dateDebutContrat: fournisseur.dateDebutContrat ? fournisseur.dateDebutContrat.format(DATE_TIME_FORMAT) : undefined,
      dateFinContrat: fournisseur.dateFinContrat ? fournisseur.dateFinContrat.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
