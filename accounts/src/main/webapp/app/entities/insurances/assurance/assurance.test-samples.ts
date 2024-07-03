import dayjs from 'dayjs/esm';

import { IAssurance, NewAssurance } from './assurance.model';

export const sampleWithRequiredData: IAssurance = {
  id: 11074,
  nom: 'vector',
  type: 'PERSONNE',
  prixAssurance: 25904.92,
  dateDebut: dayjs('2024-07-02T22:07'),
  dateFin: dayjs('2024-07-02T22:56'),
};

export const sampleWithPartialData: IAssurance = {
  id: 26808,
  nom: 'er delight',
  type: 'PERSONNE',
  prixAssurance: 12930.53,
  dateDebut: dayjs('2024-07-02T20:03'),
  dateFin: dayjs('2024-07-02T18:05'),
};

export const sampleWithFullData: IAssurance = {
  id: 10161,
  nom: 'meanwhile oval',
  description: 'distorted unto sans',
  conditions: 'aw',
  type: 'PERSONNE',
  prixAssurance: 18043.64,
  dateDebut: dayjs('2024-07-02T10:01'),
  dateFin: dayjs('2024-07-03T04:16'),
};

export const sampleWithNewData: NewAssurance = {
  nom: 'or fooey immediately',
  type: 'AGRICOLE',
  prixAssurance: 30452.25,
  dateDebut: dayjs('2024-07-03T05:04'),
  dateFin: dayjs('2024-07-02T10:34'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
