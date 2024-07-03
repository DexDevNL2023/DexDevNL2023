import dayjs from 'dayjs/esm';

import { ISinistre, NewSinistre } from './sinistre.model';

export const sampleWithRequiredData: ISinistre = {
  id: 10580,
  numeroSinistre: 'restfully',
  dateDeclaration: dayjs('2024-07-02T22:43'),
  status: 'ANNULE',
  raison: 'creepy suddenly victorious',
  montantReclame: 15655.88,
  souscriptionId: 21222,
};

export const sampleWithPartialData: ISinistre = {
  id: 27058,
  numeroSinistre: 'fantasy',
  dateDeclaration: dayjs('2024-07-02T13:23'),
  status: 'APPROUVE',
  raison: 'conceptualize',
  montantReclame: 26213.71,
  montantApprouve: 2026.76,
  souscriptionId: 13285,
};

export const sampleWithFullData: ISinistre = {
  id: 2081,
  numeroSinistre: 'violently reborn',
  dateDeclaration: dayjs('2024-07-02T23:54'),
  dateTraitement: dayjs('2024-07-02T13:36'),
  status: 'ANNULE',
  raison: 'ew hmph knavishly',
  montantReclame: 7908.15,
  montantApprouve: 14733.31,
  souscriptionId: 7331,
};

export const sampleWithNewData: NewSinistre = {
  numeroSinistre: 'ha',
  dateDeclaration: dayjs('2024-07-03T03:55'),
  status: 'APPROUVE',
  raison: 'psst aged',
  montantReclame: 18488.84,
  souscriptionId: 1447,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
