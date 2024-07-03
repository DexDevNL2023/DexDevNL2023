import dayjs from 'dayjs/esm';

import { IAssurance, NewAssurance } from './assurance.model';

export const sampleWithRequiredData: IAssurance = {
  id: 30237,
  nom: 'meh terrify',
  type: 'BIEN',
  prixAssurance: 10186.01,
  dateDebut: dayjs('2024-07-02T14:43'),
  dateFin: dayjs('2024-07-03T07:50'),
};

export const sampleWithPartialData: IAssurance = {
  id: 13053,
  nom: 'pitch mollycoddle',
  conditions: 'drat',
  type: 'PERSONNE',
  prixAssurance: 26021.86,
  dateDebut: dayjs('2024-07-02T19:18'),
  dateFin: dayjs('2024-07-03T04:04'),
};

export const sampleWithFullData: IAssurance = {
  id: 19220,
  nom: 'aha ew hmph',
  description: 'doorstep considering',
  conditions: 'drat smile',
  type: 'AGRICOLE',
  prixAssurance: 6151.56,
  dateDebut: dayjs('2024-07-03T05:04'),
  dateFin: dayjs('2024-07-03T04:54'),
};

export const sampleWithNewData: NewAssurance = {
  nom: 'rainy',
  type: 'AGRICOLE',
  prixAssurance: 5920.93,
  dateDebut: dayjs('2024-07-02T14:22'),
  dateFin: dayjs('2024-07-02T15:26'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
