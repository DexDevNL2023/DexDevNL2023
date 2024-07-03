import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentDeSinistre, NewDocumentDeSinistre } from '../document-de-sinistre.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentDeSinistre for edit and NewDocumentDeSinistreFormGroupInput for create.
 */
type DocumentDeSinistreFormGroupInput = IDocumentDeSinistre | PartialWithRequiredKeyOf<NewDocumentDeSinistre>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentDeSinistre | NewDocumentDeSinistre> = Omit<T, 'dateTelechargement'> & {
  dateTelechargement?: string | null;
};

type DocumentDeSinistreFormRawValue = FormValueOf<IDocumentDeSinistre>;

type NewDocumentDeSinistreFormRawValue = FormValueOf<NewDocumentDeSinistre>;

type DocumentDeSinistreFormDefaults = Pick<NewDocumentDeSinistre, 'id' | 'dateTelechargement'>;

type DocumentDeSinistreFormGroupContent = {
  id: FormControl<DocumentDeSinistreFormRawValue['id'] | NewDocumentDeSinistre['id']>;
  nom: FormControl<DocumentDeSinistreFormRawValue['nom']>;
  type: FormControl<DocumentDeSinistreFormRawValue['type']>;
  url: FormControl<DocumentDeSinistreFormRawValue['url']>;
  dateTelechargement: FormControl<DocumentDeSinistreFormRawValue['dateTelechargement']>;
  sinistre: FormControl<DocumentDeSinistreFormRawValue['sinistre']>;
};

export type DocumentDeSinistreFormGroup = FormGroup<DocumentDeSinistreFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentDeSinistreFormService {
  createDocumentDeSinistreFormGroup(documentDeSinistre: DocumentDeSinistreFormGroupInput = { id: null }): DocumentDeSinistreFormGroup {
    const documentDeSinistreRawValue = this.convertDocumentDeSinistreToDocumentDeSinistreRawValue({
      ...this.getFormDefaults(),
      ...documentDeSinistre,
    });
    return new FormGroup<DocumentDeSinistreFormGroupContent>({
      id: new FormControl(
        { value: documentDeSinistreRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(documentDeSinistreRawValue.nom, {
        validators: [Validators.required],
      }),
      type: new FormControl(documentDeSinistreRawValue.type, {
        validators: [Validators.required],
      }),
      url: new FormControl(documentDeSinistreRawValue.url, {
        validators: [Validators.required],
      }),
      dateTelechargement: new FormControl(documentDeSinistreRawValue.dateTelechargement, {
        validators: [Validators.required],
      }),
      sinistre: new FormControl(documentDeSinistreRawValue.sinistre),
    });
  }

  getDocumentDeSinistre(form: DocumentDeSinistreFormGroup): IDocumentDeSinistre | NewDocumentDeSinistre {
    return this.convertDocumentDeSinistreRawValueToDocumentDeSinistre(
      form.getRawValue() as DocumentDeSinistreFormRawValue | NewDocumentDeSinistreFormRawValue,
    );
  }

  resetForm(form: DocumentDeSinistreFormGroup, documentDeSinistre: DocumentDeSinistreFormGroupInput): void {
    const documentDeSinistreRawValue = this.convertDocumentDeSinistreToDocumentDeSinistreRawValue({
      ...this.getFormDefaults(),
      ...documentDeSinistre,
    });
    form.reset(
      {
        ...documentDeSinistreRawValue,
        id: { value: documentDeSinistreRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentDeSinistreFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateTelechargement: currentTime,
    };
  }

  private convertDocumentDeSinistreRawValueToDocumentDeSinistre(
    rawDocumentDeSinistre: DocumentDeSinistreFormRawValue | NewDocumentDeSinistreFormRawValue,
  ): IDocumentDeSinistre | NewDocumentDeSinistre {
    return {
      ...rawDocumentDeSinistre,
      dateTelechargement: dayjs(rawDocumentDeSinistre.dateTelechargement, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentDeSinistreToDocumentDeSinistreRawValue(
    documentDeSinistre: IDocumentDeSinistre | (Partial<NewDocumentDeSinistre> & DocumentDeSinistreFormDefaults),
  ): DocumentDeSinistreFormRawValue | PartialWithRequiredKeyOf<NewDocumentDeSinistreFormRawValue> {
    return {
      ...documentDeSinistre,
      dateTelechargement: documentDeSinistre.dateTelechargement
        ? documentDeSinistre.dateTelechargement.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
