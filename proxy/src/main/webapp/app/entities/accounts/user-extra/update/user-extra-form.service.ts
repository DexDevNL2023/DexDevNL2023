import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserExtra, NewUserExtra } from '../user-extra.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserExtra for edit and NewUserExtraFormGroupInput for create.
 */
type UserExtraFormGroupInput = IUserExtra | PartialWithRequiredKeyOf<NewUserExtra>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserExtra | NewUserExtra> = Omit<T, 'dateNaissance'> & {
  dateNaissance?: string | null;
};

type UserExtraFormRawValue = FormValueOf<IUserExtra>;

type NewUserExtraFormRawValue = FormValueOf<NewUserExtra>;

type UserExtraFormDefaults = Pick<NewUserExtra, 'id' | 'dateNaissance'>;

type UserExtraFormGroupContent = {
  id: FormControl<UserExtraFormRawValue['id'] | NewUserExtra['id']>;
  numNiu: FormControl<UserExtraFormRawValue['numNiu']>;
  nom: FormControl<UserExtraFormRawValue['nom']>;
  prenom: FormControl<UserExtraFormRawValue['prenom']>;
  email: FormControl<UserExtraFormRawValue['email']>;
  motDePasse: FormControl<UserExtraFormRawValue['motDePasse']>;
  dateNaissance: FormControl<UserExtraFormRawValue['dateNaissance']>;
  numCni: FormControl<UserExtraFormRawValue['numCni']>;
  sexe: FormControl<UserExtraFormRawValue['sexe']>;
  telephone: FormControl<UserExtraFormRawValue['telephone']>;
  addresse: FormControl<UserExtraFormRawValue['addresse']>;
  ville: FormControl<UserExtraFormRawValue['ville']>;
  pays: FormControl<UserExtraFormRawValue['pays']>;
  type: FormControl<UserExtraFormRawValue['type']>;
  signature: FormControl<UserExtraFormRawValue['signature']>;
  signatureContentType: FormControl<UserExtraFormRawValue['signatureContentType']>;
  internalUser: FormControl<UserExtraFormRawValue['internalUser']>;
};

export type UserExtraFormGroup = FormGroup<UserExtraFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserExtraFormService {
  createUserExtraFormGroup(userExtra: UserExtraFormGroupInput = { id: null }): UserExtraFormGroup {
    const userExtraRawValue = this.convertUserExtraToUserExtraRawValue({
      ...this.getFormDefaults(),
      ...userExtra,
    });
    return new FormGroup<UserExtraFormGroupContent>({
      id: new FormControl(
        { value: userExtraRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numNiu: new FormControl(userExtraRawValue.numNiu, {
        validators: [Validators.required],
      }),
      nom: new FormControl(userExtraRawValue.nom, {
        validators: [Validators.required],
      }),
      prenom: new FormControl(userExtraRawValue.prenom, {
        validators: [Validators.required],
      }),
      email: new FormControl(userExtraRawValue.email, {
        validators: [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')],
      }),
      motDePasse: new FormControl(userExtraRawValue.motDePasse, {
        validators: [Validators.required],
      }),
      dateNaissance: new FormControl(userExtraRawValue.dateNaissance, {
        validators: [Validators.required],
      }),
      numCni: new FormControl(userExtraRawValue.numCni, {
        validators: [Validators.required],
      }),
      sexe: new FormControl(userExtraRawValue.sexe, {
        validators: [Validators.required],
      }),
      telephone: new FormControl(userExtraRawValue.telephone, {
        validators: [Validators.required],
      }),
      addresse: new FormControl(userExtraRawValue.addresse, {
        validators: [Validators.required],
      }),
      ville: new FormControl(userExtraRawValue.ville, {
        validators: [Validators.required],
      }),
      pays: new FormControl(userExtraRawValue.pays, {
        validators: [Validators.required],
      }),
      type: new FormControl(userExtraRawValue.type, {
        validators: [Validators.required],
      }),
      signature: new FormControl(userExtraRawValue.signature, {
        validators: [Validators.required],
      }),
      signatureContentType: new FormControl(userExtraRawValue.signatureContentType),
      internalUser: new FormControl(userExtraRawValue.internalUser),
    });
  }

  getUserExtra(form: UserExtraFormGroup): IUserExtra | NewUserExtra {
    return this.convertUserExtraRawValueToUserExtra(form.getRawValue() as UserExtraFormRawValue | NewUserExtraFormRawValue);
  }

  resetForm(form: UserExtraFormGroup, userExtra: UserExtraFormGroupInput): void {
    const userExtraRawValue = this.convertUserExtraToUserExtraRawValue({ ...this.getFormDefaults(), ...userExtra });
    form.reset(
      {
        ...userExtraRawValue,
        id: { value: userExtraRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserExtraFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateNaissance: currentTime,
    };
  }

  private convertUserExtraRawValueToUserExtra(rawUserExtra: UserExtraFormRawValue | NewUserExtraFormRawValue): IUserExtra | NewUserExtra {
    return {
      ...rawUserExtra,
      dateNaissance: dayjs(rawUserExtra.dateNaissance, DATE_TIME_FORMAT),
    };
  }

  private convertUserExtraToUserExtraRawValue(
    userExtra: IUserExtra | (Partial<NewUserExtra> & UserExtraFormDefaults),
  ): UserExtraFormRawValue | PartialWithRequiredKeyOf<NewUserExtraFormRawValue> {
    return {
      ...userExtra,
      dateNaissance: userExtra.dateNaissance ? userExtra.dateNaissance.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
