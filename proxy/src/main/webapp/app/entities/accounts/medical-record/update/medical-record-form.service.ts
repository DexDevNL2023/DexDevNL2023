import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMedicalRecord, NewMedicalRecord } from '../medical-record.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedicalRecord for edit and NewMedicalRecordFormGroupInput for create.
 */
type MedicalRecordFormGroupInput = IMedicalRecord | PartialWithRequiredKeyOf<NewMedicalRecord>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMedicalRecord | NewMedicalRecord> = Omit<T, 'dateUpdated'> & {
  dateUpdated?: string | null;
};

type MedicalRecordFormRawValue = FormValueOf<IMedicalRecord>;

type NewMedicalRecordFormRawValue = FormValueOf<NewMedicalRecord>;

type MedicalRecordFormDefaults = Pick<NewMedicalRecord, 'id' | 'dateUpdated'>;

type MedicalRecordFormGroupContent = {
  id: FormControl<MedicalRecordFormRawValue['id'] | NewMedicalRecord['id']>;
  numMedicalRecord: FormControl<MedicalRecordFormRawValue['numMedicalRecord']>;
  details: FormControl<MedicalRecordFormRawValue['details']>;
  dateUpdated: FormControl<MedicalRecordFormRawValue['dateUpdated']>;
  utilisateur: FormControl<MedicalRecordFormRawValue['utilisateur']>;
};

export type MedicalRecordFormGroup = FormGroup<MedicalRecordFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MedicalRecordFormService {
  createMedicalRecordFormGroup(medicalRecord: MedicalRecordFormGroupInput = { id: null }): MedicalRecordFormGroup {
    const medicalRecordRawValue = this.convertMedicalRecordToMedicalRecordRawValue({
      ...this.getFormDefaults(),
      ...medicalRecord,
    });
    return new FormGroup<MedicalRecordFormGroupContent>({
      id: new FormControl(
        { value: medicalRecordRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numMedicalRecord: new FormControl(medicalRecordRawValue.numMedicalRecord, {
        validators: [Validators.required],
      }),
      details: new FormControl(medicalRecordRawValue.details, {
        validators: [Validators.required],
      }),
      dateUpdated: new FormControl(medicalRecordRawValue.dateUpdated, {
        validators: [Validators.required],
      }),
      utilisateur: new FormControl(medicalRecordRawValue.utilisateur),
    });
  }

  getMedicalRecord(form: MedicalRecordFormGroup): IMedicalRecord | NewMedicalRecord {
    return this.convertMedicalRecordRawValueToMedicalRecord(form.getRawValue() as MedicalRecordFormRawValue | NewMedicalRecordFormRawValue);
  }

  resetForm(form: MedicalRecordFormGroup, medicalRecord: MedicalRecordFormGroupInput): void {
    const medicalRecordRawValue = this.convertMedicalRecordToMedicalRecordRawValue({ ...this.getFormDefaults(), ...medicalRecord });
    form.reset(
      {
        ...medicalRecordRawValue,
        id: { value: medicalRecordRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MedicalRecordFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateUpdated: currentTime,
    };
  }

  private convertMedicalRecordRawValueToMedicalRecord(
    rawMedicalRecord: MedicalRecordFormRawValue | NewMedicalRecordFormRawValue,
  ): IMedicalRecord | NewMedicalRecord {
    return {
      ...rawMedicalRecord,
      dateUpdated: dayjs(rawMedicalRecord.dateUpdated, DATE_TIME_FORMAT),
    };
  }

  private convertMedicalRecordToMedicalRecordRawValue(
    medicalRecord: IMedicalRecord | (Partial<NewMedicalRecord> & MedicalRecordFormDefaults),
  ): MedicalRecordFormRawValue | PartialWithRequiredKeyOf<NewMedicalRecordFormRawValue> {
    return {
      ...medicalRecord,
      dateUpdated: medicalRecord.dateUpdated ? medicalRecord.dateUpdated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
